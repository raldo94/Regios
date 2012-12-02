package net.jzx7.regios;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.zip.DataFormatException;

import net.jzx7.Metrics.Metrics;
import net.jzx7.Metrics.Metrics.Graph;
import net.jzx7.regios.Commands.CommandCore;
import net.jzx7.regios.Data.ConfigurationData;
import net.jzx7.regios.Data.CreationCore;
import net.jzx7.regios.Economy.EconomyCore;
import net.jzx7.regios.Listeners.RegiosBlockListener;
import net.jzx7.regios.Listeners.RegiosEntityListener;
import net.jzx7.regios.Listeners.RegiosPlayerListener;
import net.jzx7.regios.Listeners.RegiosWeatherListener;
import net.jzx7.regios.Permissions.PermissionsCore;
import net.jzx7.regios.RBF.RBF_Core;
import net.jzx7.regios.Scheduler.MainRunner;
import net.jzx7.regios.Spout.SpoutCraftListener;
import net.jzx7.regios.Spout.SpoutInterface;
import net.jzx7.regios.Spout.GUI.CacheHandler;
import net.jzx7.regios.Spout.GUI.Screen_Listener;
import net.jzx7.regios.Versions.VersionTracker;
import net.jzx7.regios.regions.RegionManager;
import net.jzx7.regios.worlds.WorldManager;
import net.jzx7.regiosapi.RegiosAPI;
import net.jzx7.regiosapi.exceptions.FileExistanceException;
import net.jzx7.regiosapi.exceptions.InvalidNBTFormat;
import net.jzx7.regiosapi.exceptions.RegionExistanceException;
import net.jzx7.regiosapi.regions.CuboidRegion;
import net.jzx7.regiosapi.regions.PolyRegion;
import net.jzx7.regiosapi.regions.Region;
import net.jzx7.regiosapi.worlds.RegiosWorld;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.getspout.spoutapi.player.SpoutPlayer;


public class RegiosPlugin extends JavaPlugin implements RegiosAPI {

	private final RegionManager rm = new RegionManager();
	private final WorldManager wm = new WorldManager();
	
	Logger log = Logger.getLogger("Minecraft.Regios");
	private static String prefix = "[Regios]", version;
	private static List<String> authors;

	private final RegiosBlockListener blockListener = new RegiosBlockListener();
	private final RegiosPlayerListener playerListener = new RegiosPlayerListener();
	private final RegiosEntityListener entityListener = new RegiosEntityListener();
	private final RegiosWeatherListener weatherListener = new RegiosWeatherListener();

	public static Plugin regios;
	
	@Override
	public void onDisable() {
		for (Player p : Bukkit.getServer().getOnlinePlayers()) {
			if (SpoutInterface.doesPlayerHaveSpout(p)) {
				((SpoutPlayer) p).getMainScreen().closePopup();
			}
		}
		log.info(prefix + " Shutting down scheduler task...");
		MainRunner.stopMainRunner();
		log.info(prefix + " Scheduler task stopped successfully!");
		log.info(prefix + " Regios version " + version + " disabled successfully!");
	}

	@Override
	public void onEnable() {
		version = this.getDescription().getVersion();
		authors = this.getDescription().getAuthors();
		PluginManager pm = this.getServer().getPluginManager();
		
		Plugin p = pm.getPlugin("Spout");
		if(p == null){
			log.info("[Regios] Spout not detected. No Spout support.");
		} else {
			SpoutInterface.global_spoutEnabled = true;
			log.info("[Regios] Spout detected! Spout support enabled!");
		}
		
		p = pm.getPlugin("WorldEdit");
		if(p == null){
			log.info("[Regios] WorldEdit not detected. No WorldEdit support.");
		} else {
			ConfigurationData.global_worldEditEnabled = true;
			log.info("[Regios] WorldEdit detected! WorldEdit support enabled!");
		}
		try {
			new CreationCore().setup();
		} catch (IOException e) {
			e.printStackTrace();
		}

		regios = this;

		pm.registerEvents(playerListener, this);
		pm.registerEvents(blockListener, this);
		pm.registerEvents(entityListener, this);
		pm.registerEvents(weatherListener, this);

		p = pm.getPlugin("Vault");
		if(p==null) {
			EconomyCore.economySupport = false;
			log.info("[Regios] Vault not detected. No economy or permissions support. OP or superperms only.");
		} else {
			log.info("[Regios] Vault detected, Vault support enabled!");
			if(setupEconomy()) {
				log.info("[Regios] Economy support enabled for " + EconomyCore.economy.getName());
			}

			if(setupPermissions()) {
				PermissionsCore.hasPermissions = true;
				log.info("[Regios] Permissions support enabled for " + PermissionsCore.permission.getName());
			}
		}

		getCommand("regios").setExecutor(new CommandCore());

		for(World world : Bukkit.getServer().getWorlds()){
			RegiosWorld w = wm.getRegiosWorld(world);
			if(w.getOverridePVP() && !w.getPVP()){
				world.setPVP(true);
				log.info("[Regios] PvP Setting for world : " + world.getName() + ", overridden!");
			}
		}

		if (SpoutInterface.global_spoutEnabled) {
			CacheHandler.cacheObjects();
			pm.registerEvents(new SpoutCraftListener(), this);
			pm.registerEvents(new Screen_Listener(), this);
		}

		log.info(prefix + " Starting scheduler task...");
		MainRunner.startMainRunner();
		log.info(prefix + " Scheduler task initiated!");

		VersionTracker.createCurrentTracker();

		log.info(prefix + " Regios version " + version + " enabled successfully!");
		log.info(prefix + " Regios Developed by "+ authors.toString() + ".");
		
		try {
		    Metrics metrics = new Metrics(this);
		    
		    int aCount = 0;
		    int bCount = 0;
		    
		    Graph graph = metrics.createGraph("Region Count by Type");
		    
		    for (Region r : getRegions()) {
		    	if (r instanceof CuboidRegion) {
		    		aCount++;
		    	} else if (r instanceof PolyRegion) {
		    		bCount++;
		    	}
		    }
		    
		    final int crCount = aCount;
		    final int prCount = bCount;
		    
		    graph.addPlotter(new Metrics.Plotter("Cuboid Regions") {
				
				@Override
				public int getValue() {
					return crCount;
				}
			});
		    
		    graph.addPlotter(new Metrics.Plotter("Polygonal Regions") {
				
				@Override
				public int getValue() {
					return prCount;
				}
			});
		    
		    metrics.start();
		} catch (IOException e) {
		    // Failed to submit the stats :-(
		}

	}

	private boolean setupPermissions() {
		RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(Permission.class);
		if (permissionProvider != null) {
			PermissionsCore.permission = permissionProvider.getProvider();
		}
		return (PermissionsCore.permission != null);

	}

	private boolean setupEconomy()
	{
		if(EconomyCore.economySupport) {
			RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(Economy.class);
			if (economyProvider != null) {
				EconomyCore.economy = economyProvider.getProvider();
			} else {
				log.info("[Regios] UseEconomy set to true, but no economy provider was found. Disabling economy support.");
				EconomyCore.economySupport = false;
				return false;
			}

			return (EconomyCore.economy != null);
		}
		return false;
	}

	@Override
	public String getRegiosVersion() {
		return version;
	}
	
	@Override
	public ArrayList<Region> getRegions() {
		return rm.getRegions();
	}

	@Override
	public ArrayList<Region> getRegions(World w) {
		return rm.getRegions(w);
	}

	@Override
	public ArrayList<Region> getRegions(Location l) {
		return rm.getRegions(l);
	}

	@Override
	public Region getRegion(Player p) {
		return rm.getRegion(p);
	}

	@Override
	public Region getRegion(Location l) {
		return rm.getRegion(l);
	}

	@Override
	public Region getRegion(String name) {
		return rm.getRegion(name);
	}

	@Override
	public int getOwnedRegions(String name) {
		return rm.getOwnedRegions(name);
	}

	@Override
	public boolean isInRegion(Player p) {
		return rm.isInRegion(p);
	}

	@Override
	public boolean isInRegion(Location l) {
		return rm.isInRegion(l);
	}
	
	@Override
	public boolean isSpoutEnabled(Player p) {
		return SpoutInterface.doesPlayerHaveSpout(p);
	}

	@Override
	public boolean isSpoutEnabled() {
		return SpoutInterface.global_spoutEnabled;
	}

	@Override
	public void backupRegion(Region r, String backupName, Player p) {
		RBF_Core.backup.startSave(r, backupName, p);
	}

	@Override
	public boolean loadBackup(Region r, String backupName, Player p) throws RegionExistanceException, FileExistanceException, InvalidNBTFormat {
		try {
			RBF_Core.backup.loadBackup(r, backupName, p);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public void saveBlueprint(String name, Location l1, Location l2, Player p) {
		RBF_Core.blueprint.startSave(l1, l2, name, p);
	}

	@Override
	public boolean loadBlueprint(String name, Player p, Location pasteLocation) {
		File f = new File("plugins" + File.separator + "Regios" + File.separator + "Blueprints" + File.separator + name + ".blp");
		if (!f.exists()) {
			System.out.println("[Regios] A Blueprint file with the name " + name + " does not exist!");
			return false;
		}
		try {
			RBF_Core.blueprint.loadBlueprint(name, p, pasteLocation);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	@Override
	public void saveSchematic(String name, Location l1, Location l2, Player p) {
		RBF_Core.schematic.startSave(l1, l2, name, p);
	}

	@Override
	public boolean loadSchematic(String name, Player p, Location pasteLocation) {
		File f = new File("plugins" + File.separator + "Regios" + File.separator + "Schematics" + File.separator + name + ".schematic");
		if (!f.exists()) {
			System.out.println("[Regios] A Schematic file with the name " + name + " does not exist!");
			return false;
		}
		try {
			RBF_Core.schematic.loadSchematic(name, p, pasteLocation);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} catch (DataFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public Collection<RegiosWorld> getRegiosWorlds() {
		return wm.getRegiosWorlds();
	}

	@Override
	public RegiosWorld getRegiosWorld(World world) {
		return wm.getRegiosWorld(world);
	}

	@Override
	public RegiosWorld getRegiosWorld(UUID id) {
		return wm.getRegiosWorld(id);
	}
}
