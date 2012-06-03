package couk.Adamki11s.Regios.Data;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import couk.Adamki11s.Regios.Economy.EconomyCore;
import couk.Adamki11s.Regios.Regions.CubeRegion;
import couk.Adamki11s.Regios.Regions.GlobalRegionManager;
import couk.Adamki11s.Regios.Regions.GlobalWorldSetting;
import couk.Adamki11s.Regios.Regions.Region;
import couk.Adamki11s.Regios.Scheduler.LightningRunner;

public class LoaderCore {

	private final File root = new File("plugins" + File.separator + "Regios")
		, db_root = new File(root + File.separator + "Database")
		, config_root = new File(root + File.separator + "Configuration");
	
	File updateconfig = new File(config_root + File.separator + "Updates.config")
		, defaultregions = new File(config_root + File.separator + "DefaultRegion.config")
		, generalconfig = new File(config_root + File.separator + "GeneralSettings.config");

	private final Logger log = Logger.getLogger("Minecraft.Regios");
	private final String prefix = "[Regios]";

	public void setup() {
		loadConfiguration();
		loadRegions(false);
	}

	public void silentReload() {
		GlobalRegionManager.purgeRegions();
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
				, TNTEnabled = c.getBoolean("DefaultSettings.Protection.TNTEnabled", false)
				, playmusic = c.getBoolean("DefaultSettings.Spout.Sound.PlayCustomMusic", false)
				, permWipeOnEnter = c.getBoolean("DefaultSettings.Inventory.PermWipeOnEnter", false)
				, permWipeOnExit = c.getBoolean("DefaultSettings.Inventory.PermWipeOnExit", false)
				, wipeAndCacheOnEnter = c.getBoolean("DefaultSettings.Inventory.WipeAndCacheOnEnter", false)
				, wipeAndCacheOnExit = c.getBoolean("DefaultSettings.Inventory.WipeAndCacheOnExit", false)
				, forceCommand = c.getBoolean("DefaultSettings.Command.ForceCommand", false)
				, form = c.getBoolean("DefaultSettings.Block.BlockForm.Enabled", true)
				, forSale = c.getBoolean("DefaultSettings.Economy.ForSale", false);

		int LSPS = c.getInt("DefaultSettings.Other.LSPS", 0)
				, HealthRegen = c.getInt("DefaultSettings.Other.HealthRegen", 0)
				, Velocity = c.getInt("DefaultSettings.Other.VelocityWarp", 0)
				, playerCap = c.getInt("DefaultSettings.General.PlayerCap.Cap", 0)
				, salePrice = c.getInt("DefaultSettings.Economy.SalePrice", 0);

		MODE ProtectMode = MODE.toMode(c.getString("DefaultSettings.Modes.ProtectionMode"))
				, PrevEntryMode = MODE.toMode(c.getString("DefaultSettings.Modes.PreventEntryMode"))
				, PrevExitMode = MODE.toMode(c.getString("DefaultSettings.Modes.PreventExitMode"))
				, item = MODE.toMode(c.getString("DefaultSettings.Modes.ItemControlMode"));

		Material welcomeIcon = Material.getMaterial(c.getInt("DefaultSettings.Spout.SpoutWelcomeIconID", Material.GRASS.getId()))
				, leaveIcon = Material.getMaterial(c.getInt("DefaultSettings.Spout.SpoutLeaveIconID", Material.DIRT.getId()));

		String[] musicUrl = c.getString("DefaultSettings.Spout.Sound.CustomMusicURL", "").trim().split(",")
				, commandSet = c.getString("DefaultSettings.Command.CommandSet", "").trim().split(",")
				, tempAddCache = c.getString("Region.Permissions.TemporaryCache.AddNodes", "").trim().split(",")
				, tempRemCache = c.getString("Region.Permissions.TemporaryCache.RemoveNodes", "").trim().split(",")
				, permAddCache = c.getString("Region.Permissions.PermanentCache.AddNodes", "").trim().split(",")
				, permRemCache = c.getString("Region.Permissions.PermanentCache.RemoveNodes", "").trim().split(",");

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
							, forceCommand
							, commandSet
							, tempAddCache
							, tempRemCache
							, permAddCache
							, permRemCache
							, form
							, playerCap
							, protectPlace
							, protectBreak
							, forSale
							, salePrice
							, TNTEnabled);

		System.out.println("[Regios] Loaded default region configuation file.");
		// Initialises variables in configuration data.

		c = YamlConfiguration.loadConfiguration(generalconfig);

		int id = c.getInt("Region.Tools.Setting.ID", Material.WOOD_AXE.getId());
		ConfigurationData.defaultSelectionTool = Material.getMaterial(id);
		
		boolean useWE = c.getBoolean("Region.UseWorldEdit", false);
		ConfigurationData.useWorldEdit = useWE;

		boolean econ = c.getBoolean("Region.UseEconomy", false)
				, logs = c.getBoolean("Region.LogsEnabled", true);

		ConfigurationData.logs = logs;

		if (!econ) {
			EconomyCore.economySupport = false;
		} else {
			EconomyCore.economySupport = true;
		}

		GlobalWorldSetting.writeWorldsToConfiguration();
		GlobalWorldSetting.loadWorldsFromConfiguration();

		log.info(prefix + " Configuration files loaded successfully!");
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
						, l11 = c.getString("Region.Essentials.Points.Point1", "world,0,0,0")
						, l22 = c.getString("Region.Essentials.Points.Point2", "world,0,0,0")
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
						, TNTEnabled = c.getBoolean("Region.General.TNTEnabled", false)
						, mobSpawns = c.getBoolean("Region.Other.MobSpawns", true)
						, monsterSpawns = c.getBoolean("Region.Other.MonsterSpawns", true)
						, pvp = c.getBoolean("Region.Other.PvP", true)
						, healthEnabled = c.getBoolean("Region.Other.HealthEnabled", true)
						, playmusic = c.getBoolean("Region.Spout.Sound.PlayCustomMusic", false)
						, permWipeOnEnter = c.getBoolean("Region.Inventory.PermWipeOnEnter", false)
						, permWipeOnExit = c.getBoolean("Region.Inventory.PermWipeOnExit", false)
						, wipeAndCacheOnEnter = c.getBoolean("Region.Inventory.WipeAndCacheOnEnter", false)
						, wipeAndCacheOnExit = c.getBoolean("Region.Inventory.WipeAndCacheOnExit", false)
						, forceCommand = c.getBoolean("Region.Command.ForceCommand", false)
						, form = c.getBoolean("Region.Block.BlockForm.Enabled", true)
						, forSale = c.getBoolean("Region.Economy.ForSale", false)
						, useTexture = c.getBoolean("Region.Spout.Texture.UseTexture", false)
						, sw = c.getBoolean("Region.Spout.Welcome.Enabled", true)
						, sl = c.getBoolean("Region.Spout.Leave.Enabled", true);

				MODE itemMode = MODE.toMode(c.getString("Region.Modes.ItemControlMode", "Whitelist"))
						, protectionMode = MODE.toMode(c.getString("Region.Modes.ProtectionMode", "Whitelist"))
						, preventEntryMode = MODE.toMode(c.getString("Region.Modes.PreventEntryMode", "Whitelist"))
						, preventExitMode = MODE.toMode(c.getString("Region.Modes.PreventExitMode", "Whitelist"));

				int healthRegen = c.getInt("Region.Other.HealthRegen", 0)
						, lsps = c.getInt("Region.Other.LSPS", 0)
						, playerCap = c.getInt("Region.General.PlayerCap.Cap", 0)
						, price = c.getInt("Region.Economy.Price", 0);

				double velocityWarp = c.getDouble("Region.Other.VelocityWarp", 0);
				
				World world = Bukkit.getServer().getWorld(ww);

				Location l1 = toLocation(l11)
						, l2 = toLocation(l22)
						, warp = toLocation(c.getString("Region.Teleportation.Warp.Location", ww + ",0,0,0"));

				Material spoutWelcomeMaterial = Material.getMaterial(c.getInt("Region.Spout.Welcome.IconID", Material.GRASS.getId())), spoutLeaveMaterial = Material
						.getMaterial(c.getInt("Region.Spout.Leave.IconID", Material.DIRT.getId()));

				String[] musicUrl = c.getString("Region.Spout.Sound.CustomMusicURL", "").trim().split(",")
						, commandSet = c.getString("Region.Command.CommandSet", "").trim().split(",")
						, tempAddCache = c.getString("Region.Permissions.TemporaryCache.AddNodes", "").trim().split(",")
						, tempRemCache = c.getString("Region.Permissions.TemporaryCache.RemoveNodes", "").trim().split(",")
						, permAddCache = c.getString("Region.Permissions.PermanentCache.AddNodes", "").trim().split(",")
						, permRemCache = c.getString("Region.Permissions.PermanentCache.RemoveNodes", "").trim().split(",")
						, subOwners = c.getString("Region.Essentials.SubOwners", "").trim().split(",");

				Region r = new CubeRegion(owner, name, l1, l2, world, null, false);

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
				r.setTNTEnabled(TNTEnabled);

				r.setPlayCustomSoundUrl(playmusic);
				r.setCustomSoundUrl(musicUrl);

				r.setPermWipeOnEnter(permWipeOnEnter);
				r.setPermWipeOnExit(permWipeOnExit);
				r.setWipeAndCacheOnEnter(wipeAndCacheOnEnter);
				r.setWipeAndCacheOnExit(wipeAndCacheOnExit);
				r.setForceCommand(forceCommand);

				r.setCommandSet(commandSet);
				r.setItems(items);

				r.setTempNodesCacheAdd(tempAddCache);
				r.setTempNodesCacheRem(tempRemCache);
				r.setPermanentNodesCacheAdd(permAddCache);
				r.setPermanentNodesCacheRemove(permRemCache);

				r.setSubOwners(subOwners);

				r.setWarp(warp);

				r.setBlockForm(form);

				r.setPlayerCap(playerCap);

				r.setSalePrice(price);
				r.setForSale(forSale);

				if (r.getLSPS() > 0 && !LightningRunner.doesStrikesContain(r)) {
					LightningRunner.addRegion(r);
				} else if (r.getLSPS() == 0 && LightningRunner.doesStrikesContain(r)) {
					LightningRunner.removeRegion(r);
				}

			}
		}
	}

	public Location toLocation(String loc) {
		Location l = null;
		try {
			String[] locations = loc.split(",");
			l = new Location(Bukkit.getServer().getWorld(locations[0].trim()), Double.parseDouble(locations[1].trim()), Double.parseDouble(locations[2].trim()),
					Double.parseDouble(locations[3].trim()));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return l;
	}

}
