package couk.Adamki11s.Regios.Main;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.getspout.spoutapi.player.SpoutPlayer;

import couk.Adamki11s.Regios.Commands.CommandCore;
import couk.Adamki11s.Regios.Data.ConfigurationData;
import couk.Adamki11s.Regios.Data.CreationCore;
import couk.Adamki11s.Regios.Economy.EconomyCore;
import couk.Adamki11s.Regios.Listeners.RegiosBlockListener;
import couk.Adamki11s.Regios.Listeners.RegiosEntityListener;
import couk.Adamki11s.Regios.Listeners.RegiosPlayerListener;
import couk.Adamki11s.Regios.Listeners.RegiosWeatherListener;
import couk.Adamki11s.Regios.Permissions.PermissionsCore;
import couk.Adamki11s.Regios.Regions.GlobalRegionManager;
import couk.Adamki11s.Regios.Regions.GlobalWorldSetting;
import couk.Adamki11s.Regios.Scheduler.MainRunner;
import couk.Adamki11s.Regios.Spout.SpoutCraftListener;
import couk.Adamki11s.Regios.Spout.SpoutInterface;
import couk.Adamki11s.Regios.Spout.GUI.CacheHandler;
import couk.Adamki11s.Regios.Spout.GUI.Screen_Listener;
import couk.Adamki11s.Regios.Versions.VersionTracker;

public class Regios extends JavaPlugin {

	Logger log = Logger.getLogger("Minecraft.Regios");
	public static String prefix = "[Regios]", version, author;
	public static List<String> authors;

	public final RegiosBlockListener blockListener = new RegiosBlockListener();
	public final RegiosPlayerListener playerListener = new RegiosPlayerListener();
	public final RegiosEntityListener entityListener = new RegiosEntityListener();
	public final RegiosWeatherListener weatherListener = new RegiosWeatherListener();

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

		for(World w : Bukkit.getServer().getWorlds()){
			GlobalWorldSetting gws = GlobalRegionManager.getGlobalWorldSetting(w);
			if(gws.overridingPvp && !w.getPVP()){
				w.setPVP(true);
				log.info("[Regios] PvP Setting for world : " + w.getName() + ", overridden!");
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
}
