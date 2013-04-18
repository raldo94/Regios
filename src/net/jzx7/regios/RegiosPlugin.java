package net.jzx7.regios;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import net.jzx7.Metrics.Metrics;
import net.jzx7.Metrics.Metrics.Graph;
import net.jzx7.regios.Data.ConfigurationData;
import net.jzx7.regios.Data.CreationCore;
import net.jzx7.regios.Economy.EconomyCore;
import net.jzx7.regios.Permissions.PermissionsCore;
import net.jzx7.regios.RBF.RBF_Core;
import net.jzx7.regios.Scheduler.MainRunner;
import net.jzx7.regios.Versions.VersionTracker;
import net.jzx7.regios.bukkit.SpoutPlugin.SpoutCraftListener;
import net.jzx7.regios.bukkit.SpoutPlugin.SpoutInterface;
import net.jzx7.regios.bukkit.SpoutPlugin.GUI.CacheHandler;
import net.jzx7.regios.bukkit.SpoutPlugin.GUI.Screen_Listener;
import net.jzx7.regios.bukkit.commands.CommandCore;
import net.jzx7.regios.bukkit.listeners.RegiosBlockListener;
import net.jzx7.regios.bukkit.listeners.RegiosEntityListener;
import net.jzx7.regios.bukkit.listeners.RegiosPlayerListener;
import net.jzx7.regios.bukkit.listeners.RegiosServerListener;
import net.jzx7.regios.bukkit.listeners.RegiosWeatherListener;
import net.jzx7.regios.messages.Message;
import net.jzx7.regios.regions.RegionManager;
import net.jzx7.regios.util.RegiosConversions;
import net.jzx7.regios.worlds.WorldManager;
import net.jzx7.regiosapi.RegiosAPI;
import net.jzx7.regiosapi.exceptions.FileExistanceException;
import net.jzx7.regiosapi.exceptions.InvalidNBTData;
import net.jzx7.regiosapi.exceptions.InvalidNBTFormat;
import net.jzx7.regiosapi.exceptions.RegionExistanceException;
import net.jzx7.regiosapi.location.RegiosPoint;
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
	private static String version;
	private static List<String> authors;

	private final RegiosBlockListener blockListener = new RegiosBlockListener();
	private final RegiosPlayerListener playerListener = new RegiosPlayerListener();
	private final RegiosServerListener serverListener = new RegiosServerListener();
	private final RegiosEntityListener entityListener = new RegiosEntityListener();
	private final RegiosWeatherListener weatherListener = new RegiosWeatherListener();

	public static RegiosPlugin regios;
	
	@Override
	public void onDisable() {
		for (Player p : Bukkit.getServer().getOnlinePlayers()) {
			if (SpoutInterface.doesPlayerHaveSpout(RegiosConversions.getRegiosPlayer(p))) {
				((SpoutPlayer) p).getMainScreen().closePopup();
			}
		}
		log.info(Message.SCHEDULERSTOPPING.getUnformattedMessage());
		MainRunner.stopMainRunner();
		log.info(Message.SCHEDULERSTOPPED.getUnformattedMessage());
		log.info(Message.REGIOSDISABLED.getUnformattedMessage());
	}

	@Override
	public void onEnable() {
		version = this.getDescription().getVersion();
		authors = this.getDescription().getAuthors();
		regios = this;
		PluginManager pm = this.getServer().getPluginManager();
		
		Plugin p = pm.getPlugin("Spout");
		if(p == null){
			log.info(Message.SPOUTNOTDETECTED.getUnformattedMessage());
		} else {
			SpoutInterface.setGlobal_spoutEnabled(true);
			log.info(Message.SPOUTDETECTED.getUnformattedMessage());
		}
		
		p = pm.getPlugin("WorldEdit");
		if(p == null){
			log.info(Message.WORLDEDITNOTDETECTED.getUnformattedMessage());
		} else {
			ConfigurationData.global_worldEditEnabled = true;
			log.info(Message.WORLDEDITDETECTED.getUnformattedMessage());
		}
		try {
			new CreationCore().setup();
		} catch (IOException e) {
			e.printStackTrace();
		}

		pm.registerEvents(playerListener, this);
		pm.registerEvents(blockListener, this);
		pm.registerEvents(entityListener, this);
		pm.registerEvents(weatherListener, this);
		pm.registerEvents(serverListener, this);

		p = pm.getPlugin("Vault");
		if(p==null) {
			EconomyCore.economySupport = false;
			log.info(Message.VALUTNOTDETECTED.getUnformattedMessage());
		} else {
			log.info(Message.VAULTDETECTED.getUnformattedMessage());
			if(setupEconomy()) {
				log.info(Message.ECONOMYSUPPORT.getUnformattedMessage() + EconomyCore.economy.getName());
			}

			if(setupPermissions()) {
				PermissionsCore.hasPermissions = true;
				log.info(Message.PERMISSONSSUPPORT.getUnformattedMessage() + PermissionsCore.permission.getName());
			}
		}

		getCommand("regios").setExecutor(new CommandCore());

		for(World world : Bukkit.getServer().getWorlds()){
			RegiosWorld w = RegiosConversions.getRegiosWorld(world);
			if(w.getOverridePVP() && !w.getPVP()){
				world.setPVP(true);
				log.info("[Regios] PvP Setting for world : " + world.getName() + ", overridden!");
			}
		}

		if (SpoutInterface.isGlobal_spoutEnabled()) {
			CacheHandler.cacheObjects();
			pm.registerEvents(new SpoutCraftListener(), this);
			pm.registerEvents(new Screen_Listener(), this);
		}

		log.info(Message.SCHEDULERSTART.getUnformattedMessage());
		MainRunner.startMainRunner();
		log.info(Message.SCHEDULERRUNNING.getUnformattedMessage());

		VersionTracker.createCurrentTracker();

		log.info(Message.REGIOSENABLED.getUnformattedMessage());
		log.info("[Regios] Regios developed by "+ authors.toString() + ".");
		
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
		    log.info("[Regios] Metrics Started");
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
				log.info(Message.ECONOMYDISABLED.getUnformattedMessage());
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
		return rm.getRegions(RegiosConversions.getRegiosWorld(w));
	}

	@Override
	public ArrayList<Region> getRegions(Location l) {
		return rm.getRegions(new RegiosPoint(getRegiosWorld(l.getWorld()), l.getX(), l.getY(), l.getZ()));
	}

	@Override
	public Region getRegion(Player p) {
		return rm.getRegion(RegiosConversions.getRegiosPlayer(p));
	}

	@Override
	public Region getRegion(Location l) {
		return rm.getRegion(new RegiosPoint(getRegiosWorld(l.getWorld()), l.getX(), l.getY(), l.getZ()));
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
		return rm.isInRegion(RegiosConversions.getRegiosPlayer(p));
	}

	@Override
	public boolean isInRegion(Location l) {
		return rm.isInRegion(new RegiosPoint(getRegiosWorld(l.getWorld()), l.getX(), l.getY(), l.getZ()));
	}
	
	@Override
	public boolean isSpoutEnabled(Player p) {
		return SpoutInterface.doesPlayerHaveSpout(RegiosConversions.getRegiosPlayer(p));
	}

	@Override
	public boolean isSpoutEnabled() {
		return SpoutInterface.isGlobal_spoutEnabled();
	}

	@Override
	public void backupRegion(Region r, String backupName, Player p) {
		RBF_Core.backup.startSave(r, backupName, RegiosConversions.getRegiosPlayer(p));
	}

	@Override
	public boolean loadBackup(Region r, String backupName, Player p) throws RegionExistanceException, FileExistanceException, InvalidNBTFormat {
		try {
			RBF_Core.backup.loadBackup(r, backupName, RegiosConversions.getRegiosPlayer(p));
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public void saveBlueprint(String name, Location l1, Location l2, Player p) {
		RBF_Core.blueprint.startSave(RegiosConversions.getPoint(l1), RegiosConversions.getPoint(l2), name, RegiosConversions.getRegiosPlayer(p));
	}

	@Override
	public boolean loadBlueprint(String name, Player p, Location pasteLocation) {
		File f = new File("plugins" + File.separator + "Regios" + File.separator + "Blueprints" + File.separator + name + ".blp");
		if (!f.exists()) {
			System.out.println(Message.BLUEPRINTDOESNTEXIST.getUnformattedMessage());
			return false;
		}
		try {
			RBF_Core.blueprint.loadBlueprint(name, RegiosConversions.getRegiosPlayer(p), RegiosConversions.getPoint(pasteLocation));
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	@Override
	public void saveSchematic(String name, Location l1, Location l2, Player p) {
		RBF_Core.schematic.startSave(RegiosConversions.getPoint(l1), RegiosConversions.getPoint(l2), name, RegiosConversions.getRegiosPlayer(p));
	}

	@Override
	public boolean loadSchematic(String name, Player p, Location pasteLocation) {
		File f = new File("plugins" + File.separator + "Regios" + File.separator + "Schematics" + File.separator + name + ".schematic");
		if (!f.exists()) {
			System.out.println(Message.SCHEMATICDOESNTEXIST.getUnformattedMessage());
			return false;
		}
		try {
			try {
				RBF_Core.schematic.loadSchematic(name, RegiosConversions.getRegiosPlayer(p), RegiosConversions.getPoint(pasteLocation));
			} catch (InvalidNBTData e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public Collection<RegiosWorld> getRegiosWorlds() {
		return wm.getRegiosWorlds();
	}

	@Override
	public RegiosWorld getRegiosWorld(World world) {
		return RegiosConversions.getRegiosWorld(world);
	}

	@Override
	public RegiosWorld getRegiosWorld(UUID id) {
		return RegiosConversions.getRegiosWorld(id);
	}
}
