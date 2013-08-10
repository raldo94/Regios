package net.jzx7.regios.Data;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Logger;

import net.jzx7.regios.Economy.EconomyCore;
import net.jzx7.regios.Restrictions.RestrictionParameters;
import net.jzx7.regios.Scheduler.LightningRunner;
import net.jzx7.regios.regions.RegionManager;
import net.jzx7.regios.regions.RegiosCuboidRegion;
import net.jzx7.regios.regions.RegiosPolyRegion;
import net.jzx7.regios.util.RegiosConversions;
import net.jzx7.regios.worlds.WorldManager;
import net.jzx7.regiosapi.data.MODE;
import net.jzx7.regiosapi.location.RegiosPoint;
import net.jzx7.regiosapi.regions.Region;
import net.jzx7.regiosapi.worlds.RegiosWorld;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;


public class LoaderCore {

	private final File root = new File("plugins" + File.separator + "Regios")
	, db_root = new File(root + File.separator + "Database")
	, config_root = new File(root + File.separator + "Configuration");

	File updateconfig = new File(config_root + File.separator + "Updates.config")
	, defaultregions = new File(config_root + File.separator + "DefaultRegion.config")
	, generalconfig = new File(config_root + File.separator + "GeneralSettings.config")
	, restrictionconfig = new File(config_root + File.separator + "Restrictions.config");

	private final Logger log = Logger.getLogger("Minecraft.Regios");
	private final String prefix = "[Regios]";

	private final RegionManager rm = new RegionManager();
	private final WorldManager wm = new WorldManager();
	private final Saveable save = new Saveable();

	public void setup() {
		loadConfiguration();
		loadRegions(false);
	}

	public void silentReload() {
		rm.purgeRegions();
		wm.purgeRegiosWorlds();
		loadConfiguration();
		loadRegions(true);
	}

	public void loadConfiguration() {
		log.info(prefix + " Loading configuration files.");
		FileConfiguration c = YamlConfiguration.loadConfiguration(defaultregions);

		String WelcomeMessage = c.getString("DefaultSettings.Messages.WelcomeMessage", "")
				, LeaveMessage = c.getString("DefaultSettings.Messages.LeaveMessage", "")
				, ProtectionMessage = c.getString("DefaultSettings.Messages.ProtectionMessage", "")
				, PreventEntryMessage = c.getString("DefaultSettings.Messages.PreventEntryMessage", "")
				, PreventExitMessage = c.getString("DefaultSettings.Messages.PreventExitMessage", "")
				, pass = c.getString("DefaultSettings.Password.Password", "")
				, dam = c.getString("DefaultSettings.Password.PasswordMessage", "Authentication required! Do /regios auth <password>")
				, dasm = c.getString("DefaultSettings.Password.PasswordSuccessMessage", "Authentication successful!");

		boolean ShowWelcome = c.getBoolean("DefaultSettings.Messages.ShowWelcomeMessage", true)
				, ShowLeave = c.getBoolean("DefaultSettings.Messages.ShowLeaveMessage", true)
				, ShowProtectMessage = c.getBoolean("DefaultSettings.Messages.ShowProtectionMessage", true)
				, ShowPrevEntryMessage = c.getBoolean("DefaultSettings.Messages.ShowPreventEntryMessage", true)
				, ShowPrevExitMessage = c.getBoolean("DefaultSettings.Messages.ShowPreventExitMessage", true)
				, GenProtection = c.getBoolean("DefaultSettings.General.Protected.General", false)
				, protectPlace = c.getBoolean("DefaultSettings.General.Protected.BlockPlace", false)
				, protectBreak = c.getBoolean("DefaultSettings.General.Protected.BlockBreak", false)
				, PrevEntry = c.getBoolean("DefaultSettings.General.PreventEntry", false)
				, PrevExit = c.getBoolean("DefaultSettings.General.PreventExit", false)
				, MobSpawns = c.getBoolean("DefaultSettings.General.MobSpawns", true)
				, MonstSpawns = c.getBoolean("DefaultSettings.General.MonsterSpawns", true)
				, Health = c.getBoolean("DefaultSettings.Other.HealthEnabled", true)
				, PvP = c.getBoolean("DefaultSettings.General.PvP", false)
				, Doors = c.getBoolean("DefaultSettings.General.DoorsLocked", false)
				, Chests = c.getBoolean("DefaultSettings.General.ChestsLocked", false)
				, Dispensers = c.getBoolean("DefaultSettings.General.DispensersLocked", false)
				, PrevInter = c.getBoolean("DefaultSettings.General.PreventInteraction", false)
				, ShowPvP = c.getBoolean("DefaultSettings.Messages.ShowPvPWarning", true)
				, pe = c.getBoolean("DefaultSettings.Password.PasswordProtection", false)
				, fireProtection = c.getBoolean("DefaultSettings.Protection.FireProtection", false)
				, fireSpread = c.getBoolean("DefaultSettings.Protection.FireSpread", true)
				, explosionsEnabled = c.getBoolean("DefaultSettings.Protection.ExplosionsEnabled", false)
				, playmusic = c.getBoolean("DefaultSettings.Spout.Sound.PlayCustomMusic", false)
				, permWipeOnEnter = c.getBoolean("DefaultSettings.Inventory.PermWipeOnEnter", false)
				, permWipeOnExit = c.getBoolean("DefaultSettings.Inventory.PermWipeOnExit", false)
				, changeGameMode = c.getBoolean("DefaultSettings.GameMode.Change", false)
				, wipeAndCacheOnEnter = c.getBoolean("DefaultSettings.Inventory.WipeAndCacheOnEnter", false)
				, wipeAndCacheOnExit = c.getBoolean("DefaultSettings.Inventory.WipeAndCacheOnExit", false)
				, forceCommand = c.getBoolean("DefaultSettings.Command.ForceCommand", false)
				, form = c.getBoolean("DefaultSettings.Block.BlockForm.Enabled", true)
				, forSale = c.getBoolean("DefaultSettings.Economy.ForSale", false)
				, blockEnderman = c.getBoolean("DefaultSettings.Protection.EndermanBlock", false);

		int LSPS = c.getInt("DefaultSettings.Other.LSPS", 0)
				, HealthRegen = c.getInt("DefaultSettings.Other.HealthRegen", 0)
				, Velocity = c.getInt("DefaultSettings.Other.VelocityWarp", 0)
				, playerCap = c.getInt("DefaultSettings.General.PlayerCap.Cap", 0)
				, salePrice = c.getInt("DefaultSettings.Economy.SalePrice", 0);

		MODE ProtectMode = MODE.toMode(c.getString("DefaultSettings.Modes.ProtectionMode"))
				, PrevEntryMode = MODE.toMode(c.getString("DefaultSettings.Modes.PreventEntryMode"))
				, PrevExitMode = MODE.toMode(c.getString("DefaultSettings.Modes.PreventExitMode"))
				, item = MODE.toMode(c.getString("DefaultSettings.Modes.ItemControlMode"));

		int gameMode = c.getInt("DefaultSettings.GameMode.Type", 0);

		int welcomeIcon = c.getInt("DefaultSettings.Spout.SpoutWelcomeIconID", 2)
				, leaveIcon = c.getInt("DefaultSettings.Spout.SpoutLeaveIconID", 3);

		String[] musicUrl = c.getString("DefaultSettings.Spout.Sound.CustomMusicURL", "").trim().split(",")
				, commandSet = c.getString("DefaultSettings.Command.CommandSet", "").trim().split(",")
				, tempAddCache = c.getString("DefaultSettings.Permissions.TemporaryCache.AddNodes", "").trim().split(",")
				, tempRemCache = c.getString("DefaultSettings.Permissions.TemporaryCache.RemoveNodes", "").trim().split(",")
				, permAddCache = c.getString("DefaultSettings.Permissions.PermanentCache.AddNodes", "").trim().split(",")
				, permRemCache = c.getString("DefaultSettings.Permissions.PermanentCache.RemoveNodes", "").trim().split(",")
				, tempAddGroup = c.getString("DefaultSettings.Permissions.TempGroups.AddGroups", "").trim().split(",")
				, tempRemGroup = c.getString("DefaultSettings.Permissions.TempGroups.RemoveGroups", "").trim().split(",")
				, permAddGroup = c.getString("DefaultSettings.Permissions.PermGroups.AddGroups", "").trim().split(",")
				, permRemGroup = c.getString("DefaultSettings.Permissions.PermGroups.RemoveGroups", "").trim().split(",");

		// ___________________________________

		c = YamlConfiguration.loadConfiguration(updateconfig);

		boolean cfu = c.getBoolean("CheckForUpdates", true)
				, dua = c.getBoolean("DownloadUpdatesAutomatically", true)
				, cov = c.getBoolean("CacheOldVersions", true)
				, fr = c.getBoolean("ForceReload", true);

		new ConfigurationData(WelcomeMessage
				, LeaveMessage
				, ProtectionMessage
				, PreventEntryMessage
				, PreventExitMessage
				, pass
				, GenProtection
				, PrevEntry
				, MobSpawns
				, MonstSpawns
				, Health
				, PvP
				, Doors
				, Chests
				, Dispensers
				, PrevInter
				, ShowPvP
				, pe
				, LSPS
				, HealthRegen
				, Velocity
				, ProtectMode
				, PrevEntryMode
				, PrevExitMode
				, item
				, cfu
				, dua
				, cov
				, fr
				, PrevExit
				, dam
				, dasm
				, welcomeIcon
				, leaveIcon
				, ShowWelcome
				, ShowLeave
				, ShowProtectMessage
				, ShowPrevEntryMessage
				, ShowPrevExitMessage
				, fireProtection
				, fireSpread
				, musicUrl
				, playmusic
				, permWipeOnEnter
				, permWipeOnExit
				, wipeAndCacheOnEnter
				, wipeAndCacheOnExit
				, changeGameMode
				, forceCommand
				, commandSet
				, tempAddCache
				, permAddCache
				, tempRemCache
				, permRemCache
				, tempAddGroup
				, permAddGroup
				, tempRemGroup
				, permRemGroup
				, form
				, playerCap
				, protectPlace
				, protectBreak
				, forSale
				, salePrice
				, explosionsEnabled
				, gameMode
				, blockEnderman);

		log.info("[Regios] Loaded default region configuation file.");
		// Initialises variables in configuration data.

		c = YamlConfiguration.loadConfiguration(generalconfig);

		ConfigurationData.defaultSelectionTool = c.getInt("Region.Tools.Setting.ID", 271);
		if (ConfigurationData.global_worldEditEnabled)
		{
			ConfigurationData.useWorldEdit = c.getBoolean("Region.UseWorldEdit", false);
		} else {
			ConfigurationData.useWorldEdit = false;
		}

		boolean econ = c.getBoolean("Region.UseEconomy", false)
				, logs = c.getBoolean("Region.LogsEnabled", true);

		ConfigurationData.logs = logs;

		if (!econ) {
			EconomyCore.economySupport = false;
		} else {
			EconomyCore.economySupport = true;
		}

		c = YamlConfiguration.loadConfiguration(restrictionconfig);

		@SuppressWarnings("unchecked")
		ArrayList<String> size = (ArrayList<String>) c.getList("Restrictions.Size");
		RestrictionParameters.size = size;

		@SuppressWarnings("unchecked")
		ArrayList<String> count = (ArrayList<String>) c.getList("Restrictions.Count");
		RestrictionParameters.count = count;

		RegiosConversions.loadServerWorlds();
		save.saveWorlds();
		loadWorlds();

		log.info(prefix + " Configuration files loaded successfully!");
	}

	public void loadWorlds(){
		for(RegiosWorld w : wm.getRegiosWorlds()){
			log.info("[Regios] Loading world configuration for world: " + w.getName());
			File root = new File("plugins" + File.separator + "Regios" + File.separator + "Configuration" + File.separator + "WorldConfigurations");
			if(!root.exists()){ root.mkdir(); }
			File f = new File("plugins" + File.separator + "Regios" + File.separator + "Configuration" + File.separator + "WorldConfigurations" + File.separator + w.getName() + ".rwc");
			FileConfiguration c = YamlConfiguration.loadConfiguration(f);
			w.setProtection(c.getBoolean(w.getName() + ".Protection.ProtectionEnabledOutsideRegions", false));
			w.setFireEnabled(c.getBoolean(w.getName() + ".Protection.FireEnabled", true));
			w.setFireSpreadEnabled(c.getBoolean(w.getName() + ".Protection.FireSpreadEnabled", true));
			w.setExplosionsEnabled(c.getBoolean(w.getName() + ".Protection.ExplosionsEnabled", true));
			w.setDragonProtectionEnabled(c.getBoolean(w.getName() + ".Protection.DragonProtect", true));
			w.setEndermanProtectionEnabled(c.getBoolean(w.getName() + ".Protection.EndermanBlock", false));
			w.setEnderDragonCreatesPortal(c.getBoolean(w.getName() + ".Protection.DragonCreatesPortal", false));
			w.setPVP(c.getBoolean(w.getName() + ".PvP.EnabledOutsideRegions", true));
			w.setLightningEnabled(c.getBoolean(w.getName() + ".Weather.LightningEnabled", true));
			w.setOverridePVP(c.getBoolean(w.getName() + ".PvP.OverrideServerPvP", true));
			w.setBlockFormEnabled(c.getBoolean(w.getName() + ".Block.BlockForm.Enabled", true));
			if(c.getBoolean(w.getName() + ".Mobs.Spawning.Chicken", true)){ w.addCreatureSpawn(RegiosConversions.getEntityTypeID("CHICKEN")); }
			if(c.getBoolean(w.getName() + ".Mobs.Spawning.Cow", true)){ w.addCreatureSpawn(RegiosConversions.getEntityTypeID("COW")); }
			if(c.getBoolean(w.getName() + ".Mobs.Spawning.Creeper", true)){ w.addCreatureSpawn(RegiosConversions.getEntityTypeID("CREEPER")); }
			if(c.getBoolean(w.getName() + ".Mobs.Spawning.Ghast", true)){ w.addCreatureSpawn(RegiosConversions.getEntityTypeID("GHAST")); }
			if(c.getBoolean(w.getName() + ".Mobs.Spawning.Giant", true)){ w.addCreatureSpawn(RegiosConversions.getEntityTypeID("GIANT")); }
			if(c.getBoolean(w.getName() + ".Mobs.Spawning.Pig", true)){ w.addCreatureSpawn(RegiosConversions.getEntityTypeID("PIG")); }
			if(c.getBoolean(w.getName() + ".Mobs.Spawning.PigZombie", true)){ w.addCreatureSpawn(RegiosConversions.getEntityTypeID("PIG_ZOMBIE")); }
			if(c.getBoolean(w.getName() + ".Mobs.Spawning.Sheep", true)){ w.addCreatureSpawn(RegiosConversions.getEntityTypeID("SHEEP")); }
			if(c.getBoolean(w.getName() + ".Mobs.Spawning.Skeleton", true)){ w.addCreatureSpawn(RegiosConversions.getEntityTypeID("SKELETON")); }
			if(c.getBoolean(w.getName() + ".Mobs.Spawning.Slime", true)){ w.addCreatureSpawn(RegiosConversions.getEntityTypeID("SLIME")); }
			if(c.getBoolean(w.getName() + ".Mobs.Spawning.Spider", true)){ w.addCreatureSpawn(RegiosConversions.getEntityTypeID("SPIDER")); }
			if(c.getBoolean(w.getName() + ".Mobs.Spawning.Squid", true)){ w.addCreatureSpawn(RegiosConversions.getEntityTypeID("SQUID")); }
			if(c.getBoolean(w.getName() + ".Mobs.Spawning.Zombie", true)){ w.addCreatureSpawn(RegiosConversions.getEntityTypeID("ZOMBIE")); }
			if(c.getBoolean(w.getName() + ".Mobs.Spawning.Wolf", true)){ w.addCreatureSpawn(RegiosConversions.getEntityTypeID("WOLF")); }
			if(c.getBoolean(w.getName() + ".Mobs.Spawning.CaveSpider", true)){ w.addCreatureSpawn(RegiosConversions.getEntityTypeID("CAVE_SPIDER")); }
			if(c.getBoolean(w.getName() + ".Mobs.Spawning.Enderman", true)){ w.addCreatureSpawn(RegiosConversions.getEntityTypeID("ENDERMAN")); }
			if(c.getBoolean(w.getName() + ".Mobs.Spawning.Silverfish", true)){ w.addCreatureSpawn(RegiosConversions.getEntityTypeID("SILVERFISH")); }
			if(c.getBoolean(w.getName() + ".Mobs.Spawning.EnderDragon", true)){ w.addCreatureSpawn(RegiosConversions.getEntityTypeID("ENDER_DRAGON")); }
			if(c.getBoolean(w.getName() + ".Mobs.Spawning.Villager", true)){ w.addCreatureSpawn(RegiosConversions.getEntityTypeID("VILLAGER")); }
			if(c.getBoolean(w.getName() + ".Mobs.Spawning.Blaze", true)){ w.addCreatureSpawn(RegiosConversions.getEntityTypeID("BLAZE")); }
			if(c.getBoolean(w.getName() + ".Mobs.Spawning.MushroomCow", true)){ w.addCreatureSpawn(RegiosConversions.getEntityTypeID("MUSHROOM_COW")); }
			if(c.getBoolean(w.getName() + ".Mobs.Spawning.MagmaCube", true)){ w.addCreatureSpawn(RegiosConversions.getEntityTypeID("MAGMA_CUBE")); }
			if(c.getBoolean(w.getName() + ".Mobs.Spawning.Snowman", true)){ w.addCreatureSpawn(RegiosConversions.getEntityTypeID("SNOWMAN")); }
			if(c.getBoolean(w.getName() + ".Mobs.Spawning.IronGolem", true)){ w.addCreatureSpawn(RegiosConversions.getEntityTypeID("IRON_GOLEM")); }
			if(c.getBoolean(w.getName() + ".Mobs.Spawning.Ocelot", true)){ w.addCreatureSpawn(RegiosConversions.getEntityTypeID("OCELOT")); }
			if(c.getBoolean(w.getName() + ".Mobs.Spawning.Bat", true)){ w.addCreatureSpawn(RegiosConversions.getEntityTypeID("BAT")); }
			if(c.getBoolean(w.getName() + ".Mobs.Spawning.Wither", true)){ w.addCreatureSpawn(RegiosConversions.getEntityTypeID("WITHER")); }
			if(c.getBoolean(w.getName() + ".Mobs.Spawning.WitherSkull", true)){ w.addCreatureSpawn(RegiosConversions.getEntityTypeID("WITHER_SKULL")); }
			if(c.getBoolean(w.getName() + ".Mobs.Spawning.Witch", true)){ w.addCreatureSpawn(RegiosConversions.getEntityTypeID("WITCH")); }
			if(c.getBoolean(w.getName() + ".Mobs.Spawning.Horse", true)){ w.addCreatureSpawn(RegiosConversions.getEntityTypeID("HORSE")); }
		}
	}

	public void loadRegions(boolean silent) {
		File[] children = db_root.listFiles();
		if (children.length > 0) {
			if (!silent) {
				log.info(prefix + " Loading [" + children.length + "] Regions.");
			}
		} else {
			if (!silent) {
				log.info(prefix + " No Regions to load.");
			}
		}
		for (File root : children) {
			if (root.isDirectory()) {
				ArrayList<String> exceptionsPlayers = new ArrayList<String>()
						, exceptionsNodes = new ArrayList<String>();
				ArrayList<Integer> items = new ArrayList<Integer>();

				File toload = new File(db_root + File.separator + root.getName() + File.separator + root.getName() + ".rz")
				, excep = new File(db_root + File.separator + root.getName() + File.separator + "Exceptions" + File.separator + "Players")
				, nodes = new File(db_root + File.separator + root.getName() + File.separator + "Exceptions" + File.separator + "Nodes")
				, it = new File(db_root + File.separator + root.getName() + File.separator + "Items");

				if (excep.exists()) {
					for (File ex : excep.listFiles()) {
						if (!ex.getName().contains("Placeholder")) {
							exceptionsPlayers.add(ex.getName().substring(0, ex.getName().lastIndexOf(".")));
						}
					}
				}

				if (nodes.exists()) {
					for (File nd : nodes.listFiles()) {
						if (!nd.getName().contains("Placeholder")) {
							exceptionsNodes.add(nd.getName().substring(0, nd.getName().lastIndexOf(".")));
						}
					}
				}

				if (it.exists()) {
					if (it.listFiles().length > 0) {
						for (File f : it.listFiles()) {
							if (!f.getName().contains("Placeholder")) {
								try {
									items.add(Integer.parseInt(f.getName().substring(0, f.getName().lastIndexOf("."))));
								} catch (NumberFormatException nfe) {
									log.severe(prefix + " Error parsing integer in item file! File : " + f.getName());
								}
							}
						}
					}
				}

				FileConfiguration c = YamlConfiguration.loadConfiguration(toload);

				String welcomeMessage = c.getString("Region.Messages.WelcomeMessage", "")
						, leaveMessage = c.getString("Region.Messages.LeaveMessage", "")
						, protectionMessage = c.getString("Region.Messages.ProtectionMessage", "")
						, preventEntryMessage = c.getString("Region.Messages.PreventEntryMessage", "")
						, preventExitMessage = c.getString("Region.Messages.PreventExitMessage", "")
						, password = c.getString("Region.General.Password.Password", "")
						, owner = c.getString("Region.Essentials.Owner", "")
						, name = c.getString("Region.Essentials.Name", "")
						, ww = c.getString("Region.Essentials.World", "world")
						, l11 = c.getString("Region.Essentials.Points.Point1", null)
						, l22 = c.getString("Region.Essentials.Points.Point2", null)
						, xp = c.getString("Region.Essentials.Points.xPoints", null)
						, zp = c.getString("Region.Essentials.Points.zPoints", null)
						, np = c.getString("Region.Essentials.Points.nPoints", null)
						, miY = c.getString("Region.Essentials.Points.MinY", null)
						, maY = c.getString("Region.Essentials.Points.MaxY", null)
						, textureUrl = c.getString("Region.Spout.Texture.TexturePackURL", "")
						, spoutWelcomeMessage = c.getString("Region.Spout.Welcome.Message", "")
						, spoutLeaveMessage = c.getString("Region.Spout.Leave.Message", "");

				boolean showPvpWarning = c.getBoolean("Region.Messages.ShowPvpWarning", true)
						, showWelcomeMessage = c.getBoolean("Region.Messages.ShowWelcomeMessage", true)
						, showLeaveMessage = c.getBoolean("Region.Messages.ShowLeaveMessage", true)
						, showProtectionMessage = c.getBoolean("Region.Messages.ShowProtectionMessage", true)
						, showPreventEntryMessage = c.getBoolean("Region.Messages.ShowPreventEntryMessage", true)
						, showPreventExitMessage = c.getBoolean("Region.Messages.ShowPreventExitMessage", true)
						, _protected = c.getBoolean("Region.General.Protected.General", false)
						, _protectedPlace = c.getBoolean("Region.General.Protected.BlockPlace", false)
						, _protectedBreak = c.getBoolean("Region.General.Protected.BlockBreak", false)
						, preventEntry = c.getBoolean("Region.General.PreventEntry", false)
						, preventExit = c.getBoolean("Region.General.PreventExit", false)
						, preventInteraction = c.getBoolean("Region.General.PreventInteraction", false)
						, doorsLocked = c.getBoolean("Region.General.DoorsLocked", false)
						, chestsLocked = c.getBoolean("Region.General.ChestsLocked", false)
						, dispensersLocked = c.getBoolean("Region.General.DispensersLocked", false)
						, passwordEnabled = c.getBoolean("Region.General.Password.Enabled", false)
						, fireProtection = c.getBoolean("Region.General.FireProtection", false)
						, fireSpread = c.getBoolean("Region.General.FireSpread", true)
						, explosionsEnabled = c.getBoolean("Region.General.ExplosionsEnabled", false)
						, mobSpawns = c.getBoolean("Region.Other.MobSpawns", true)
						, monsterSpawns = c.getBoolean("Region.Other.MonsterSpawns", true)
						, pvp = c.getBoolean("Region.Other.PvP", true)
						, healthEnabled = c.getBoolean("Region.Other.HealthEnabled", true)
						, playmusic = c.getBoolean("Region.Spout.Sound.PlayCustomMusic", false)
						, permWipeOnEnter = c.getBoolean("Region.Inventory.PermWipeOnEnter", false)
						, permWipeOnExit = c.getBoolean("Region.Inventory.PermWipeOnExit", false)
						, wipeAndCacheOnEnter = c.getBoolean("Region.Inventory.WipeAndCacheOnEnter", false)
						, wipeAndCacheOnExit = c.getBoolean("Region.Inventory.WipeAndCacheOnExit", false)
						, changeGM = c.getBoolean("Region.GameMode.Change", false)
						, forceCommand = c.getBoolean("Region.Command.ForceCommand", false)
						, form = c.getBoolean("Region.Block.BlockForm.Enabled", true)
						, forSale = c.getBoolean("Region.Economy.ForSale", false)
						, useTexture = c.getBoolean("Region.Spout.Texture.UseTexture", false)
						, sw = c.getBoolean("Region.Spout.Welcome.Enabled", true)
						, sl = c.getBoolean("Region.Spout.Leave.Enabled", true)
						, enderman = c.getBoolean("Region.General.EndermanBlock", false);

				MODE itemMode = MODE.toMode(c.getString("Region.Modes.ItemControlMode", "Whitelist"))
						, protectionMode = MODE.toMode(c.getString("Region.Modes.ProtectionMode", "Whitelist"))
						, preventEntryMode = MODE.toMode(c.getString("Region.Modes.PreventEntryMode", "Whitelist"))
						, preventExitMode = MODE.toMode(c.getString("Region.Modes.PreventExitMode", "Whitelist"));

				int gm = c.getInt("Region.GameMode.Type", 0);

				int healthRegen = c.getInt("Region.Other.HealthRegen", 0)
						, lsps = c.getInt("Region.Other.LSPS", 0)
						, playerCap = c.getInt("Region.General.PlayerCap.Cap", 0)
						, price = c.getInt("Region.Economy.Price", 0);

				double velocityWarp = c.getDouble("Region.Other.VelocityWarp", 0);

				RegiosWorld world = RegiosConversions.getRegiosWorld(ww);

				RegiosPoint warp = toPoint(c.getString("Region.Teleportation.Warp.Location", ww + ",0,0,0"));

				int spoutWelcomeMaterial = c.getInt("Region.Spout.Welcome.IconID", 2), spoutLeaveMaterial =  c.getInt("Region.Spout.Leave.IconID", 3);

				String[] musicUrl = c.getString("Region.Spout.Sound.CustomMusicURL", "").trim().split(",")
						, commandSet = c.getString("Region.Command.CommandSet", "").trim().split(",")
						, tempAddCache = c.getString("Region.Permissions.TemporaryCache.AddNodes", "").trim().split(",")
						, tempRemCache = c.getString("Region.Permissions.TemporaryCache.RemoveNodes", "").trim().split(",")
						, permAddCache = c.getString("Region.Permissions.PermanentCache.AddNodes", "").trim().split(",")
						, permRemCache = c.getString("Region.Permissions.PermanentCache.RemoveNodes", "").trim().split(",")
						, tempAddGroup = c.getString("Region.Permissions.TempGroups.AddGroups", "").trim().split(",")
						, tempRemGroup = c.getString("Region.Permissions.TempGroups.RemoveGroups", "").trim().split(",")
						, permAddGroup = c.getString("Region.Permissions.PermGroups.AddGroups", "").trim().split(",")
						, permRemGroup = c.getString("Region.Permissions.PermGroups.RemoveGroups", "").trim().split(",")
						, subOwners = c.getString("Region.Essentials.SubOwners", "").trim().split(",");

				Region r;

				if (l11 == null && l22 == null) {
					try {
						String[] xPointsStr = xp.split(","), zPointsStr = zp.split(",");
						int[] xPoints = new int[xPointsStr.length], zPoints = new int[zPointsStr.length];
						int i = 0;

						for (String s : xPointsStr) {
							xPoints[i] = Integer.parseInt(s);
							i++;
						}
						i = 0;
						for (String s : zPointsStr) {
							zPoints[i] = Integer.parseInt(s);
							i++;
						}


						r = new RegiosPolyRegion(owner, name, xPoints, zPoints, Integer.parseInt(np), Double.parseDouble(miY), Double.parseDouble(maY), world, null, false);
					} catch (Exception ex) {
						//TODO: figure out what to do here.
						r = new RegiosCuboidRegion(owner, name, toPoint(l11), toPoint(l22), world, null, false);
					}
				} else {
					r = new RegiosCuboidRegion(owner, name, toPoint(l11), toPoint(l22), world, null, false);
				}

				for (String s : exceptionsPlayers) {
					r.addException(s);
				}
				for (String s : exceptionsNodes) {
					r.addExceptionNode(s);
				}

				r.setSpoutWelcomeEnabled(sw);
				r.setSpoutLeaveEnabled(sl);

				r.setUseSpoutTexturePack(useTexture);
				r.setSpoutTexturePack(textureUrl);

				r.setSpoutExitMaterial(spoutLeaveMaterial);
				r.setSpoutEntryMaterial(spoutWelcomeMaterial);
				r.setSpoutEntryMessage(spoutWelcomeMessage);
				r.setSpoutExitMessage(spoutLeaveMessage);

				r.setVelocityWarp(velocityWarp);
				r.setLSPS(lsps);
				r.setHealthRegen(healthRegen);

				r.setHealthEnabled(healthEnabled);
				r.setPvp(pvp);
				r.setMonsterSpawns(monsterSpawns);
				r.setMobSpawns(mobSpawns);

				r.setPassword(password);

				r.setPasswordEnabled(passwordEnabled);
				r.setChestsLocked(chestsLocked);
				r.setDoorsLocked(doorsLocked);
				r.setDispensersLocked(dispensersLocked);
				r.setPreventInteraction(preventInteraction);
				r.setPreventExit(preventExit);
				r.setPreventEntry(preventEntry);
				r.set_protection(_protected);
				r.set_protectionPlace(_protectedPlace);
				r.set_protectionBreak(_protectedBreak);

				r.setPreventExitMode(preventExitMode);
				r.setPreventEntryMode(preventEntryMode);
				r.setProtectionMode(protectionMode);
				r.setItemMode(itemMode);

				r.setShowPreventExitMessage(showPreventExitMessage);
				r.setShowPreventEntryMessage(showPreventEntryMessage);
				r.setShowProtectionMessage(showProtectionMessage);
				r.setShowLeaveMessage(showLeaveMessage);
				r.setShowWelcomeMessage(showWelcomeMessage);
				r.setShowPvpWarning(showPvpWarning);

				r.setPreventExitMessage((preventExitMessage));
				r.setPreventEntryMessage((preventEntryMessage));
				r.setProtectionMessage((protectionMessage));
				r.setLeaveMessage((leaveMessage));
				r.setWelcomeMessage((welcomeMessage));

				r.setFireProtection(fireProtection);
				r.setFireSpread(fireSpread);
				r.setExplosionsEnabled(explosionsEnabled);

				r.setPlayCustomSoundUrl(playmusic);
				r.setCustomSoundUrl(musicUrl);

				r.setPermWipeOnEnter(permWipeOnEnter);
				r.setPermWipeOnExit(permWipeOnExit);
				r.setWipeAndCacheOnEnter(wipeAndCacheOnEnter);
				r.setWipeAndCacheOnExit(wipeAndCacheOnExit);
				r.setChangeGameMode(changeGM);
				r.setForceCommand(forceCommand);

				r.setCommandSet(commandSet);
				r.setItems(items);

				r.setTempNodesCacheAdd(tempAddCache);
				r.setTempNodesCacheRem(tempRemCache);
				r.setPermanentNodesCacheAdd(permAddCache);
				r.setPermanentNodesCacheRemove(permRemCache);

				r.setTempAddGroups(tempAddGroup);
				r.setTempRemoveGroups(tempRemGroup);
				r.setPermAddGroups(permAddGroup);
				r.setPermRemoveGroups(permRemGroup);

				r.setSubOwners(subOwners);

				r.setWarp(warp);

				r.setBlockForm(form);

				r.setPlayerCap(playerCap);

				r.setSalePrice(price);
				r.setForSale(forSale);

				r.setGameMode(gm);

				r.setBlockEndermanMod(enderman);

				if (r.getLSPS() > 0 && !LightningRunner.doesStrikesContain(r)) {
					LightningRunner.addRegion(r);
				} else if (r.getLSPS() == 0 && LightningRunner.doesStrikesContain(r)) {
					LightningRunner.removeRegion(r);
				}

			}
		}
	}

	public RegiosPoint toPoint(String loc) {
		RegiosPoint l = null;
		try {
			String[] locations = loc.split(",");
			l = new RegiosPoint(RegiosConversions.getRegiosWorld(locations[0].trim()), Double.parseDouble(locations[1].trim()), Double.parseDouble(locations[2].trim()), Double.parseDouble(locations[3].trim()));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return l;
	}

}
