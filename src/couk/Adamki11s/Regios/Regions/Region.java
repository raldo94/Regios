package couk.Adamki11s.Regios.Regions;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

import couk.Adamki11s.Extras.Cryptography.ExtrasCryptography;
import couk.Adamki11s.Regios.CustomEvents.RegionCreateEvent;
import couk.Adamki11s.Regios.CustomEvents.RegionEnterEvent;
import couk.Adamki11s.Regios.CustomEvents.RegionExitEvent;
import couk.Adamki11s.Regios.CustomEvents.RegionLoadEvent;
import couk.Adamki11s.Regios.Data.ConfigurationData;
import couk.Adamki11s.Regios.Data.MODE;
import couk.Adamki11s.Regios.Data.Saveable;
import couk.Adamki11s.Regios.GameMode.GameModeCacheManager;
import couk.Adamki11s.Regios.Inventory.InventoryCacheManager;
import couk.Adamki11s.Regios.Listeners.RegiosPlayerListener;
import couk.Adamki11s.Regios.Permissions.PermChecks;
import couk.Adamki11s.Regios.Permissions.PermissionsCacheManager;
import couk.Adamki11s.Regios.Scheduler.HealthRegeneration;
import couk.Adamki11s.Regios.Scheduler.LightningRunner;
import couk.Adamki11s.Regios.Scheduler.LogRunner;
import couk.Adamki11s.Regios.SpoutInterface.SpoutInterface;
import couk.Adamki11s.Regios.SpoutInterface.SpoutRegion;

public class Region extends PermChecks {

	public static GlobalRegionManager getGrm() {
		return grm;
	}

	public static Saveable getSaveable() {
		return saveable;
	}

	protected Location l1;
	protected Location l2;

	protected static final GlobalRegionManager grm = new GlobalRegionManager();

	protected static final Saveable saveable = new Saveable();

	protected ChunkGrid chunkGrid;

	protected World world;

	protected Location warp = null;

	protected String[] customSoundUrl
			, commandSet
			, temporaryNodesCacheAdd
			, temporaryNodesCacheRem
			, permanentNodesCacheAdd
			, permanentNodesCacheRemove
			, subOwners;

	protected ArrayList<String> exceptions = new ArrayList<String>()
			, nodes = new ArrayList<String>();

	protected ArrayList<Integer> items = new ArrayList<Integer>();

	protected String welcomeMessage = ""
			, leaveMessage = ""
			, protectionMessage = ""
			, preventEntryMessage = ""
			, preventExitMessage = ""
			, password = ""
			, name = ""
			, owner = ""
			, spoutEntryMessage = ""
			, spoutExitMessage = ""
			, spoutTexturePack = "";

	protected Material spoutEntryMaterial = Material.GRASS
			, spoutExitMaterial = Material.DIRT;

	protected boolean _protection = false
			, _protectionPlace = false
			, _protectionBreak = false
			, preventEntry = false
			, preventExit = false
			, mobSpawns = true
			, monsterSpawns = true
			, healthEnabled = true
			, pvp = true
			, doorsLocked = false
			, chestsLocked = false
			, dispensersLocked = false
			, preventInteraction = false
			, showPvpWarning = true
			, passwordEnabled = false
			, showWelcomeMessage = true
			, showLeaveMessage = true
			, showProtectionMessage = true
			, showPreventEntryMessage = true
			, showPreventExitMessage = true
			, fireProtection = false
			, fireSpread = true
			, playCustomSoundUrl = false
			, permWipeOnEnter = false
			, permWipeOnExit = false
			, wipeAndCacheOnEnter = false
			, wipeAndCacheOnExit = false
			, forceCommand = false
			, blockForm = true
			, forSale = false
			, useSpoutTexturePack = false
			, spoutWelcomeEnabled = false
			, spoutLeaveEnabled = false
			, TNTEnabled = true
			, changeGameMode = false;

	protected int LSPS = 0
			, healthRegen = 0
			, playerCap = 0
			, salePrice = 0;
	
	protected GameMode gameMode = GameMode.SURVIVAL;

	protected double velocityWarp = 0;

	protected MODE protectionMode = MODE.Whitelist
			, preventEntryMode = MODE.Whitelist
			, preventExitMode = MODE.Whitelist
			, itemMode = MODE.Whitelist;

	protected HashMap<Player, Boolean> authentication = new HashMap<Player, Boolean>()
			, welcomeMessageSent = new HashMap<Player, Boolean>()
			, leaveMessageSent = new HashMap<Player, Boolean>();

	protected HashMap<Player, Long> timeStamps = new HashMap<Player, Long>();

	protected ArrayList<Player> playersInRegion = new ArrayList<Player>();

	protected HashMap<Player, PlayerInventory> inventoryCache = new HashMap<Player, PlayerInventory>();

	protected ExtrasCryptography exCrypt = new ExtrasCryptography();

	public Region(String owner, String name, World world, Player p, boolean save) {
		this.owner = owner;
		this.name = name;

		if (world != null) {
			this.world = world;
		} else {
			world = Bukkit.getServer().getWorlds().get(0);
		}

		if (save) {
			RegionCreateEvent event = new RegionCreateEvent("RegionCreateEvent");
			event.setProperties(p, this);
			Bukkit.getServer().getPluginManager().callEvent(event);
		} else {
			RegionLoadEvent event = new RegionLoadEvent("RegionLoadEvent");
			event.setProperties(this);
			Bukkit.getServer().getPluginManager().callEvent(event);
		}

		welcomeMessage = ConfigurationData.defaultWelcomeMessage.toString();

		leaveMessage = ConfigurationData.defaultLeaveMessage.toString();
		protectionMessage = (ConfigurationData.defaultProtectionMessage.toString());
		preventEntryMessage = (ConfigurationData.defaultPreventEntryMessage.toString());
		preventExitMessage = (ConfigurationData.defaultPreventExitMessage.toString());
		if (ConfigurationData.passwordEnabled) {
			passwordEnabled = true;
			password = ConfigurationData.password;
		} else {
			passwordEnabled = false;
			password = "";
		}
		_protection = ConfigurationData.regionProtected;
		_protectionBreak = ConfigurationData.regionProtectedBreak;
		_protectionPlace = ConfigurationData.regionPlaceProtected;
		preventEntry = ConfigurationData.regionPreventEntry;
		mobSpawns = ConfigurationData.mobSpawns;
		monsterSpawns = ConfigurationData.monsterSpawns;
		healthEnabled = ConfigurationData.healthEnabled;
		pvp = ConfigurationData.pvp;
		doorsLocked = ConfigurationData.doorsLocked;
		chestsLocked = ConfigurationData.chestsLocked;
		dispensersLocked = ConfigurationData.dispensersLocked;
		preventInteraction = ConfigurationData.preventInteraction;
		showPvpWarning = ConfigurationData.showPvpWarning;
		LSPS = ConfigurationData.LSPS;
		healthRegen = ConfigurationData.healthRegen;
		velocityWarp = ConfigurationData.velocityWarp;
		protectionMode = ConfigurationData.protectionMode;
		preventEntryMode = ConfigurationData.preventEntryMode;
		preventExitMode = ConfigurationData.preventExitMode;
		preventExit = ConfigurationData.regionPreventExit;
		spoutEntryMaterial = ConfigurationData.defaultSpoutWelcomeMaterial;
		spoutExitMaterial = ConfigurationData.defaultSpoutLeaveMaterial;
		spoutEntryMessage = "Welcome to [NAME]";
		spoutExitMessage = "You left [NAME]";
		showWelcomeMessage = ConfigurationData.showWelcomeMessage;
		showLeaveMessage = ConfigurationData.showLeaveMessage;
		showProtectionMessage = ConfigurationData.showProtectionMessage;
		showPreventEntryMessage = ConfigurationData.showPreventEntryMessage;
		showPreventExitMessage = ConfigurationData.showPreventExitMessage;
		fireProtection = ConfigurationData.fireProtection;
		fireSpread = ConfigurationData.fireSpread;
		permWipeOnEnter = ConfigurationData.permWipeOnEnter;
		permWipeOnExit = ConfigurationData.permWipeOnExit;
		wipeAndCacheOnEnter = ConfigurationData.wipeAndCacheOnEnter;
		wipeAndCacheOnExit = ConfigurationData.wipeAndCacheOnExit;
		changeGameMode = ConfigurationData.changeGameMode;
		forceCommand = ConfigurationData.forceCommand;
		commandSet = ConfigurationData.commandSet;
		temporaryNodesCacheAdd = ConfigurationData.temporaryNodesCacheAdd;
		spoutTexturePack = "";
		useSpoutTexturePack = false;
		forSale = ConfigurationData.forSale;
		salePrice = ConfigurationData.salePrice;
		blockForm = ConfigurationData.blockForm;
		TNTEnabled = ConfigurationData.tntEnabled;
		gameMode = ConfigurationData.gameMode;
		if (LSPS > 0 && !LightningRunner.doesStrikesContain(this)) {
			LightningRunner.addRegion(this);
		} else if (LSPS == 0 && LightningRunner.doesStrikesContain(this)) {
			LightningRunner.removeRegion(this);
		}
	}

	public void addException(String exception) {
		exceptions.add(exception);
		LogRunner.addLogMessage(this, LogRunner.getPrefix(this) + (" Exception '" + exception + "' added."));
	}

	public void addExceptionNode(String node) {
		nodes.add(node);
		LogRunner.addLogMessage(this, LogRunner.getPrefix(this) + (" Exception node '" + node + "' added."));
	}

	public void addItemException(int id) {
		items.add(id);
	}

	public void addPlayer(Player p) {
		playersInRegion.add(p);
	}

	public boolean areChestsLocked() {
		return chestsLocked;
	}
	
	public boolean areDispensersLocked() {
		return dispensersLocked;
	}

	public boolean areDoorsLocked() {
		return doorsLocked;
	}

	public void cacheInventory(Player p) {
		inventoryCache.put(p, p.getInventory());
	}

	public boolean canBypassProtection(Player p) {
		return super.canBypassProtection(p, this);
	}

	public boolean canEnter(Player p) {
		return super.canEnter(p, this);
	}

	public boolean canExit(Player p) {
		return super.canExit(p, this);
	}
	
	public GameMode getGameMode() {
		return gameMode;
	}
	
	public void setGameMode(GameMode gm) {
		gameMode = gm;
	}
	
	public void setChangeGameMode(boolean val)
	{
		changeGameMode = val;
	}
	
	public boolean isChangeGameMode()
	{
		return changeGameMode;
	}

	public boolean canMobsSpawn() {
		return mobSpawns;
	}

	public boolean canMonstersSpawn() {
		return monsterSpawns;
	}

	public String colourFormat(String message) {
		// In case old regions were not patched properly
		message = message.replaceAll("%BLACK%", "<BLACK>");
		message = message.replaceAll("\\&0", "<BLACK>");
		message = message.replaceAll("\\$0", "<BLACK>");

		message = message.replaceAll("%DBLUE%", "<DBLUE>");
		message = message.replaceAll("\\&1", "<DBLUE>");
		message = message.replaceAll("\\$1", "<DBLUE>");

		message = message.replaceAll("%DGREEN%", "<DGREEN>");
		message = message.replaceAll("\\&2", "<DGREEN>");
		message = message.replaceAll("\\$2", "<DGREEN>");

		message = message.replaceAll("%DTEAL%", "<DTEAL>");
		message = message.replaceAll("\\&3", "<DTEAL>");
		message = message.replaceAll("\\$3", "<DTEAL>");

		message = message.replaceAll("%DRED%", "<DRED>");
		message = message.replaceAll("\\&4", "<DRED>");
		message = message.replaceAll("\\$4", "<DRED>");

		message = message.replaceAll("%PURPLE%", "<PURPLE>");
		message = message.replaceAll("\\&5", "<PURPLE>");
		message = message.replaceAll("\\$5", "<PURPLE>");

		message = message.replaceAll("%GOLD%", "<GOLD>");
		message = message.replaceAll("\\&6", "<GOLD>");
		message = message.replaceAll("\\$6", "<GOLD>");

		message = message.replaceAll("%GREY%", "<GREY>");
		message = message.replaceAll("\\&7", "<GREY>");
		message = message.replaceAll("\\$7", "<GREY>");

		message = message.replaceAll("%DGREY%", "<DGREY>");
		message = message.replaceAll("\\&8", "<DGREY>");
		message = message.replaceAll("\\$8", "<DGREY>");

		message = message.replaceAll("%BLUE%", "<BLUE>");
		message = message.replaceAll("\\&9", "<BLUE>");
		message = message.replaceAll("\\$9", "<BLUE>");

		message = message.replaceAll("%BGREEN%", "<BGREEN>");
		message = message.replaceAll("\\&A", "<BGREEN>");
		message = message.replaceAll("\\$A", "<BGREEN>");

		message = message.replaceAll("%TEAL%", "<TEAL>");
		message = message.replaceAll("\\&B", "<TEAL>");
		message = message.replaceAll("\\$B", "<TEAL>");

		message = message.replaceAll("%RED%", "<RED>");
		message = message.replaceAll("\\&C", "<RED>");
		message = message.replaceAll("\\$C", "<RED>");

		message = message.replaceAll("%PINK%", "<PINK>");
		message = message.replaceAll("\\&D", "<PINK>");
		message = message.replaceAll("\\$D", "<PINK>");

		message = message.replaceAll("%YELLOW%", "<YELLOW>");
		message = message.replaceAll("\\&E", "<YELLOW>");
		message = message.replaceAll("\\$E", "<YELLOW>");

		message = message.replaceAll("%WHITE%", "<WHITE>");
		message = message.replaceAll("\\&F", "<WHITE>");
		message = message.replaceAll("\\$F", "<WHITE>");
		// In case old regions were not patched properly

		message = message.replaceAll("<BLACK>", "\u00A70");
		message = message.replaceAll("<0>", "\u00A70");

		message = message.replaceAll("<DBLUE>", "\u00A71");
		message = message.replaceAll("<1>", "\u00A71");

		message = message.replaceAll("<DGREEN>", "\u00A72");
		message = message.replaceAll("<2>", "\u00A72");

		message = message.replaceAll("<DTEAL>", "\u00A73");
		message = message.replaceAll("<3>", "\u00A73");

		message = message.replaceAll("<DRED>", "\u00A74");
		message = message.replaceAll("<4>", "\u00A74");

		message = message.replaceAll("<PURPLE>", "\u00A75");
		message = message.replaceAll("<5>", "\u00A75");

		message = message.replaceAll("<GOLD>", "\u00A76");
		message = message.replaceAll("<6>", "\u00A76");

		message = message.replaceAll("<GREY>", "\u00A77");
		message = message.replaceAll("<7>", "\u00A77");

		message = message.replaceAll("<DGREY>", "\u00A78");
		message = message.replaceAll("<8>", "\u00A78");

		message = message.replaceAll("<BLUE>", "\u00A79");
		message = message.replaceAll("<9>", "\u00A79");

		message = message.replaceAll("<BGREEN>", "\u00A7a");
		message = message.replaceAll("<A>", "\u00A7a");

		message = message.replaceAll("<TEAL>", "\u00A7b");
		message = message.replaceAll("<B>", "\u00A7b");

		message = message.replaceAll("<RED>", "\u00A7c");
		message = message.replaceAll("<C>", "\u00A7c");

		message = message.replaceAll("<PINK>", "\u00A7d");
		message = message.replaceAll("<D>", "\u00A7d");

		message = message.replaceAll("<YELLOW>", "\u00A7e");
		message = message.replaceAll("<E>", "\u00A7e");

		message = message.replaceAll("<WHITE>", "\u00A7f");
		message = message.replaceAll("<F>", "\u00A7f");

		message = message.replaceAll("\\[", "");
		message = message.replaceAll("\\]", "");
		message = message.replaceAll("OWNER", getOwner());
		message = message.replaceAll("NAME", getName());

		return message;
	}

	public HashMap<Player, Boolean> getAuthentication() {
		return authentication;
	}

	public boolean getAuthentication(String password, Player p) {
		if (exCrypt.compareHashes(exCrypt.computeSHA2_384BitHash(password), exCrypt.computeSHA2_384BitHash(this.password))) {
			authentication.put(p, true);
			return true;
		} else {
			authentication.put(p, false);
			return false;
		}
	}

	public File getBackupsDirectory() {
		return new File("plugins" + File.separator + "Regios" + File.separator + "Database" + File.separator + name + File.separator + "Backups");
	}

	public ChunkGrid getChunkGrid() {
		return chunkGrid;
	}

	public String[] getCommandSet() {
		return commandSet;
	}

	public File getConfigFile() {
		return new File("plugins" + File.separator + "Regios" + File.separator + "Database" + File.separator + name + File.separator + name
				+ ".rz");
	}

	public String[] getCustomSoundUrl() {
		return customSoundUrl;
	}

	public File getDirectory(){
		return new File("plugins" + File.separator + "Regios" + File.separator + "Database" + File.separator + name);
	}

	public File getExceptionDirectory() {
		return new File("plugins" + File.separator + "Regios" + File.separator + "Database" + File.separator + name + File.separator + "Exceptions");
	}

	public ArrayList<String> getExceptionNodes() {
		return nodes;
	}

	public ArrayList<String> getExceptions() {
		return exceptions;
	}

	public ExtrasCryptography getExCrypt() {
		return exCrypt;
	}

	public int getHealthRegen() {
		return healthRegen;
	}

	public HashMap<Player, PlayerInventory> getInventoryCache() {
		return inventoryCache;
	}

	public PlayerInventory getInventoryCache(Player p) {
		return inventoryCache.containsKey(p) ? inventoryCache.get(p) : null;
	}

	public MODE getItemMode() {
		return itemMode;
	}

	public ArrayList<Integer> getItems() {
		return items;
	}

	public Location getL1() {
		return new Location(world, l1.getX(), l1.getY(), l1.getZ());
	}

	public Location getL2() {
		return new Location(world, l2.getX(), l2.getY(), l2.getZ());
	}

	public String getLeaveMessage() {
		return leaveMessage;
	}

	public HashMap<Player, Boolean> getLeaveMessageSent() {
		return leaveMessageSent;
	}

	public File getLogFile() {
		return new File("plugins" + File.separator + "Regios" + File.separator + "Database" + File.separator + name + File.separator + "Logs" + File.separator
				+ name + ".log");
	}

	public int getLSPS() {
		return LSPS;
	}

	public String getName() {
		return name;
	}

	public ArrayList<String> getNodes() {
		return nodes;
	}

	public String getOwner() {
		return owner;
	}

	public String getPassword() {
		return password;
	}

	public String[] getPermAddNodes() {
		return permanentNodesCacheAdd;
	}

	public String[] getPermRemoveNodes() {
		return permanentNodesCacheRemove;
	}

	public int getPlayerCap() {
		return playerCap;
	}

	public ArrayList<Player> getPlayersInRegion() {
		return playersInRegion;
	}

	public String getPreventEntryMessage() {
		return preventEntryMessage;
	}

	public MODE getPreventEntryMode() {
		return preventEntryMode;
	}

	public String getPreventExitMessage() {
		return preventExitMessage;
	}

	public MODE getPreventExitMode() {
		return preventExitMode;
	}

	public String getProtectionMessage() {
		return protectionMessage;
	}

	public MODE getProtectionMode() {
		return protectionMode;
	}

	public File getRawConfigFile(){
		return new File(("plugins" + File.separator + "Regios" + File.separator + "Database" + File.separator + name + File.separator + name + ".rz"));
	}

	public Region getRegion() {
		return this;
	}

	public int getSalePrice() {
		return salePrice;
	}

	public Material getSpoutLeaveMaterial() {
		return spoutExitMaterial;
	}

	public String getSpoutLeaveMessage() {
		return spoutExitMessage;
	}

	public String getSpoutTexturePack() {
		return spoutTexturePack;
	}

	public Material getSpoutWelcomeMaterial() {
		return spoutEntryMaterial;
	}

	public String getSpoutWelcomeMessage() {
		return spoutEntryMessage;
	}

	public String[] getSubOwners() {
		return subOwners;
	}

	public String[] getTempNodesCacheAdd() {
		return temporaryNodesCacheAdd;
	}

	public String[] getTempNodesCacheRem() {
		return temporaryNodesCacheRem;
	}

	public HashMap<Player, Long> getTimeStamps() {
		return timeStamps;
	}

	public double getVelocityWarp() {
		return velocityWarp;
	}

	public Location getWarp() {
		return warp;
	}

	public String getWelcomeMessage() {
		return welcomeMessage;
	}

	public HashMap<Player, Boolean> getWelcomeMessageSent() {
		return welcomeMessageSent;
	}

	public World getWorld() {
		return world;
	}

	public boolean is_protectionBreak() {
		return _protectionBreak;
	}

	public boolean is_protectionPlace() {
		return _protectionPlace;
	}

	public boolean isAuthenticated(Player p) {
		if (authentication.containsKey(p)) {
			return authentication.get(p);
		} else {
			return false;
		}
	}

	public boolean isBlockForm() {
		return blockForm;
	}

	public boolean isFireProtection() {
		return fireProtection;
	}
	
	public boolean isFireSpread() {
		return fireSpread;
	}

	public boolean isForceCommand() {
		return forceCommand;
	}

	public boolean isForSale() {
		return forSale;
	}

	public boolean isHealthEnabled() {
		return healthEnabled;
	}

	private boolean isLeaveMessageSent(Player p) {
		if (!leaveMessageSent.containsKey(p)) {
			return false;
		} else {
			return leaveMessageSent.get(p);
		}
	}

	public boolean isPasswordEnabled() {
		return passwordEnabled;
	}

	public boolean isPermWipeOnEnter() {
		return permWipeOnEnter;
	}

	public boolean isPermWipeOnExit() {
		return permWipeOnExit;
	}

	public boolean isPlayCustomSoundUrl() {
		return playCustomSoundUrl;
	}

	public boolean isPlayerInRegion(Player p) {
		if (playersInRegion.contains(p)) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isPreventEntry() {
		return preventEntry;
	}

	public boolean isPreventExit() {
		return preventExit;
	}

	public boolean isPreventInteraction() {
		return preventInteraction;
	}

	public boolean isProtected() {
		return _protection;
	}

	public boolean isPvp() {
		return pvp;
	}

	public boolean isRegionFull(Player p) {
		if (playerCap > 0) {
			if (playersInRegion.size() > playerCap) {
				if (!canBypassProtection(p)) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public boolean isSendable(Player p) {
		boolean outcome = (timeStamps.containsKey(p) ? (System.currentTimeMillis() > timeStamps.get(p) + 5000) : true);
		if (outcome) {
			setTimestamp(p);
		}
		return outcome;
	}

	public boolean isShowLeaveMessage() {
		return showLeaveMessage;
	}

	public boolean isShowPreventEntryMessage() {
		return showPreventEntryMessage;
	}

	public boolean isShowPreventExitMessage() {
		return showPreventExitMessage;
	}

	public boolean isShowProtectionMessage() {
		return showProtectionMessage;
	}

	public boolean isShowPvpWarning() {
		return showPvpWarning;
	}

	public boolean isShowWelcomeMessage() {
		return showWelcomeMessage;
	}

	public boolean isSpoutLeaveEnabled() {
		return spoutLeaveEnabled;
	}

	public boolean isSpoutWelcomeEnabled() {
		return spoutWelcomeEnabled;
	}

	public boolean isTNTEnabled() {
		return TNTEnabled;
	}

	public boolean isUseSpoutTexturePack() {
		return useSpoutTexturePack;
	}

	private boolean isWelcomeMessageSent(Player p) {
		if (!welcomeMessageSent.containsKey(p)) {
			return false;
		} else {
			return welcomeMessageSent.get(p);
		}
	}

	public boolean isWipeAndCacheOnEnter() {
		return wipeAndCacheOnEnter;
	}

	public boolean isWipeAndCacheOnExit() {
		return wipeAndCacheOnExit;
	}

	public String liveFormat(String original, Player p) {
		original = original.replaceAll("\\[", "");
		original = original.replaceAll("\\]", "");
		if (original.contains("PLAYER-COUNT")) {
			original = original.replaceAll("PLAYER-COUNT", "" + getPlayersInRegion().size());
		}
		if (original.contains("BUILD-RIGHTS")) {
			original = original.replaceAll("BUILD-RIGHTS", "" + canBypassProtection(p));
		}
		if (original.contains("PLAYER")) {
			original = original.replaceAll("PLAYER", "" + p.getName());
		}
		if (original.contains("PLAYER-LIST")) {
			StringBuilder builder = new StringBuilder();
			builder.append("");
			for (Player play : playersInRegion) {
				builder.append(ChatColor.WHITE).append(play.getName()).append(ChatColor.BLUE).append(", ");
			}
			original = original.replaceAll("PLAYER-LIST", "" + builder.toString());
		}
		return original;
	}

	private void registerExitEvent(Player p) {
		RegionExitEvent event = new RegionExitEvent("RegionExitEvent");
		event.setProperties(p, this);
		Bukkit.getServer().getPluginManager().callEvent(event);
	}

	private void registerWelcomeEvent(Player p) {
		RegionEnterEvent event = new RegionEnterEvent("RegionEnterEvent");
		event.setProperties(p, this);
		Bukkit.getServer().getPluginManager().callEvent(event);
	}

	public void removeException(String exception) {
		if (exceptions.contains(exception)) {
			exceptions.remove(exception);
			LogRunner.addLogMessage(this, LogRunner.getPrefix(this) + (" Exception '" + exception + "' removed."));
		}
	}

	public void removeExceptionNode(String node) {
		if (nodes.contains(node)) {
			LogRunner.addLogMessage(this, LogRunner.getPrefix(this) + (" Exception node '" + node + "' removed."));
			nodes.remove(node);
		}
	}

	public void removeItemException(int id) {
		if (items.contains((Object) id)) {
			items.remove((Object) id);
		}
	}

	public void removePlayer(Player p) {
		if (playersInRegion.contains(p)) {
			playersInRegion.remove(p);
		}
	}

	public void resetAuthentication(Player p) {
		authentication.put(p, false);
	}

	public void sendBuildMessage(Player p) {
		if (showProtectionMessage && isSendable(p)) {
			LogRunner.addLogMessage(this, LogRunner.getPrefix(this) + (" Player '" + p.getName() + "' tried to build but did not have permissions."));
			p.sendMessage(colourFormat(liveFormat(protectionMessage, p)));
		}
	}

	public void sendLeaveMessage(Player p) {
		if (!isLeaveMessageSent(p)) {
			registerExitEvent(p);
			LogRunner.addLogMessage(this, LogRunner.getPrefix(this) + (" Player '" + p.getName() + "' left region."));
			if (RegiosPlayerListener.currentRegion.containsKey(p)) {
				RegiosPlayerListener.currentRegion.remove(p);
			}
			leaveMessageSent.put(p, true);
			welcomeMessageSent.remove(p);
			removePlayer(p);
			if (HealthRegeneration.isRegenerator(p)) {
				HealthRegeneration.removeRegenerator(p);
			}
			if (permWipeOnExit) {
				if (!canBypassProtection(p)) {
					InventoryCacheManager.wipeInventory(p);
				}
			}
			if (wipeAndCacheOnEnter) {
				if (!canBypassProtection(p)) {
					if (InventoryCacheManager.doesCacheContain(p)) {
						InventoryCacheManager.restoreInventory(p);
						LogRunner.addLogMessage(this, LogRunner.getPrefix(this) + (" Player '" + p.getName() + "' inventory restored upon exit"));
					}
				}
			}
			if (wipeAndCacheOnExit) {
				if (!canBypassProtection(p)) {
					if (!InventoryCacheManager.doesCacheContain(p)) {
						InventoryCacheManager.cacheInventory(p);
						InventoryCacheManager.wipeInventory(p);
						LogRunner.addLogMessage(this, LogRunner.getPrefix(this) + (" Player '" + p.getName() + "' inventory cached upon exit"));
					}
				}
			}
			
			if (changeGameMode) {
				if (GameModeCacheManager.doesCacheContain(p))
				{
					GameModeCacheManager.restoreGameMode(p);
				}
			}
			
			LogRunner.addLogMessage(this, LogRunner.getPrefix(this) + (" Player '" + p.getName() + "' left region."));

			try {
				if (temporaryNodesCacheAdd != null) {
					if (temporaryNodesCacheAdd.length > 0) {
						PermissionsCacheManager.unCacheAddNodes(p, this);
						LogRunner.addLogMessage(this, LogRunner.getPrefix(this) + (" Temporary add node caches wiped upon region exit for player '" + p.getName() + "'"));
					}
				}
				if (temporaryNodesCacheRem != null) {
					if (temporaryNodesCacheRem.length > 0) {
						PermissionsCacheManager.unCacheRemNodes(p, this);
						LogRunner.addLogMessage(this, LogRunner.getPrefix(this) + (" Temporary remove node caches restored upon region exit for player '" + p.getName() + "'"));
					}
				}
				if (permanentNodesCacheRemove != null) {
					if (permanentNodesCacheRemove.length > 0) {
						PermissionsCacheManager.permRemoveNodes(p, this);
						LogRunner.addLogMessage(this, LogRunner.getPrefix(this) + (" Permanent nodes wiped upon region exit for player '" + p.getName() + "'"));
					}
				}
			} catch (Exception ex) {
				// Fail silently if the operation is unsupported
			}
			if (showLeaveMessage) {
				p.sendMessage(colourFormat(liveFormat(leaveMessage, p)));
			}
			if (SpoutInterface.doesPlayerHaveSpout(p)) {
				if (spoutLeaveEnabled) {
					SpoutRegion.sendLeaveMessage(p, this);
				}
				if (playCustomSoundUrl) {
					SpoutRegion.stopMusicPlaying(p, this);
				}
				if (useSpoutTexturePack) {
					SpoutRegion.resetTexturePack(p);
				}
			}
		}
	}

	public void sendPreventEntryMessage(Player p) {
		if (showPreventEntryMessage && isSendable(p)) {
			LogRunner.addLogMessage(this, LogRunner.getPrefix(this) + (" Player '" + p.getName() + "' tried to enter but did not have permissions."));
			p.sendMessage(colourFormat(liveFormat(preventEntryMessage, p)));
		}
	}

	public void sendPreventExitMessage(Player p) {
		if (showPreventExitMessage && isSendable(p)) {
			LogRunner.addLogMessage(this, LogRunner.getPrefix(this) + (" Player '" + p.getName() + "' tried to leave but did not have permissions."));
			p.sendMessage(colourFormat(liveFormat(preventExitMessage, p)));
		}
	}

	public void sendWelcomeMessage(Player p) {
		if (!isWelcomeMessageSent(p)) {
			registerWelcomeEvent(p);
			LogRunner.addLogMessage(this, LogRunner.getPrefix(this) + (" Player '" + p.getName() + "' entered region."));
			if (useSpoutTexturePack && SpoutInterface.doesPlayerHaveSpout(p)) {
				SpoutRegion.forceTexturePack(p, this);
			}
			RegiosPlayerListener.currentRegion.put(p, this);
			welcomeMessageSent.put(p, true);
			leaveMessageSent.remove(p);
			addPlayer(p);
			if (!HealthRegeneration.isRegenerator(p)) {
				if (healthRegen < 0 && !canBypassProtection(p)) {
					HealthRegeneration.addRegenerator(p, healthRegen);
				} else if (healthRegen > 0) {
					HealthRegeneration.addRegenerator(p, healthRegen);
				}
			}
			if (permWipeOnEnter) {
				if (!canBypassProtection(p)) {
					InventoryCacheManager.wipeInventory(p);
				}
			}
			if (wipeAndCacheOnEnter) {
				if (!canBypassProtection(p)) {
					if (!InventoryCacheManager.doesCacheContain(p)) {
						InventoryCacheManager.cacheInventory(p);
						InventoryCacheManager.wipeInventory(p);
						LogRunner.addLogMessage(this, LogRunner.getPrefix(this) + (" Player '" + p.getName() + "' inventory cached upon entry"));
					}
				}
			}
			if (wipeAndCacheOnExit) {
				if (!canBypassProtection(p)) {
					if (InventoryCacheManager.doesCacheContain(p)) {
						InventoryCacheManager.restoreInventory(p);
						LogRunner.addLogMessage(this, LogRunner.getPrefix(this) + (" Player '" + p.getName() + "' inventory restored upon entry"));
					}
				}
			}
			if (changeGameMode) {
					if(!GameModeCacheManager.doesCacheContain(p)) {
						GameModeCacheManager.cacheGameMode(p);
						p.setGameMode(getGameMode());
					}
			}
			LogRunner.addLogMessage(this, LogRunner.getPrefix(this) + (" Player '" + p.getName() + "' entered region."));
			if (commandSet != null) {
				if (commandSet.length > 0) {
					for (String s : commandSet) {
						if (s.length() > 1) {
							LogRunner.addLogMessage(this, LogRunner.getPrefix(this) + (" Player forced command '" + s + "' upon enter."));
							p.performCommand(s.trim());
						}
					}
				}
			}
			try {
				if (temporaryNodesCacheAdd != null) {
					if (temporaryNodesCacheAdd.length > 0) {
						PermissionsCacheManager.cacheAddNodes(p, this);
						LogRunner.addLogMessage(this, LogRunner.getPrefix(this) + (" Temporary add node caches added upon region enter for player '" + p.getName() + "'"));
					}

				}
				if (temporaryNodesCacheRem != null) {
					if (temporaryNodesCacheRem.length > 0) {
						PermissionsCacheManager.cacheRemNodes(p, this);
						LogRunner.addLogMessage(this, LogRunner.getPrefix(this) + (" Temporary remove node caches wiped upon region enter for player '" + p.getName() + "'"));
					}

				}
				if (permanentNodesCacheAdd != null) {
					if (permanentNodesCacheAdd.length > 0) {
						PermissionsCacheManager.permAddNodes(p, this);
						LogRunner.addLogMessage(this, LogRunner.getPrefix(this) + (" Permanent nodes added upon region enter for player '" + p.getName() + "'"));
					}
				}
			} catch (Exception ex) {
				// Fail silently if the operation is unsupported
			}
			if (showWelcomeMessage) {
				p.sendMessage(colourFormat(liveFormat(welcomeMessage, p)));
			}
			if (SpoutInterface.doesPlayerHaveSpout(p)) {
				if (spoutWelcomeEnabled) {
					SpoutRegion.sendWelcomeMessage(p, this);
				}
				if (playCustomSoundUrl) {
					SpoutRegion.playToPlayerMusicFromUrl(p, this);
				}
				if (useSpoutTexturePack) {
					SpoutRegion.forceTexturePack(p, this);
				}
			}
		}
	}

	public void set_protection(boolean _protection) {
		this._protection = _protection;
	}

	public void set_protectionBreak(boolean _protectionBreak) {
		this._protectionBreak = _protectionBreak;
	}

	public void set_protectionPlace(boolean _protectionPlace) {
		this._protectionPlace = _protectionPlace;
	}

	public void setAuthentication(HashMap<Player, Boolean> authentication) {
		this.authentication = authentication;
	}

	public void setBlockForm(boolean blockForm) {
		this.blockForm = blockForm;
	}

	public void setChestsLocked(boolean chestsLocked) {
		this.chestsLocked = chestsLocked;
	}

	public void setChunkGrid(ChunkGrid chunkGrid) {
		this.chunkGrid = chunkGrid;
	}

	public void setCommandSet(String[] commandSet) {
		this.commandSet = commandSet;
	}

	public void setCustomSoundUrl(String[] customSoundUrl) {
		this.customSoundUrl = customSoundUrl;
	}

	public void setDoorsLocked(boolean doorsLocked) {
		this.doorsLocked = doorsLocked;
	}

	public void setExceptions(ArrayList<String> exceptions) {
		this.exceptions = exceptions;
	}

	public void setExCrypt(ExtrasCryptography exCrypt) {
		this.exCrypt = exCrypt;
	}

	public void setFireProtection(boolean fireProtection) {
		this.fireProtection = fireProtection;
	}
	
	public void setFireSpread(boolean fireSpread) {
		this.fireSpread = fireSpread;
	}

	public void setForceCommand(boolean forceCommand) {
		this.forceCommand = forceCommand;
	}

	public void setForSale(boolean forSale) {
		this.forSale = forSale;
	}

	public void setHealthEnabled(boolean healthEnabled) {
		this.healthEnabled = healthEnabled;
	}

	public void setHealthRegen(int healthRegen) {
		this.healthRegen = healthRegen;
	}

	public void setInventoryCache(HashMap<Player, PlayerInventory> inventoryCache) {
		this.inventoryCache = inventoryCache;
	}

	public void setItemMode(MODE itemMode) {
		this.itemMode = itemMode;
	}

	public void setItems(ArrayList<Integer> items) {
		this.items = items;
	}

	public void setL1(Location l1) {
		this.l1 = l1;
	}

	public void setL1(World w, double x, double y, double z) {
		l1 = new Location(w, x, y, z);
	}

	public void setL2(Location l2) {
		this.l2 = l2;
	}

	public void setL2(World w, double x, double y, double z) {
		l2 = new Location(w, x, y, z);
	}

	public void setLeaveMessage(String leaveMessage) {
		this.leaveMessage = leaveMessage;
	}

	public void setLeaveMessageSent(HashMap<Player, Boolean> leaveMessageSent) {
		this.leaveMessageSent = leaveMessageSent;
	}

	public void setLSPS(int lSPS) {
		LSPS = lSPS;
	}

	public void setMobSpawns(boolean mobSpawns) {
		this.mobSpawns = mobSpawns;
	}

	public void setMonsterSpawns(boolean monsterSpawns) {
		this.monsterSpawns = monsterSpawns;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNodes(ArrayList<String> nodes) {
		this.nodes = nodes;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setPasswordEnabled(boolean passwordEnabled) {
		this.passwordEnabled = passwordEnabled;
	}

	public void setPermanentNodesCacheAdd(String[] permanentNodesCacheAdd) {
		this.permanentNodesCacheAdd = permanentNodesCacheAdd;
	}

	public void setPermanentNodesCacheRemove(String[] permanentNodesCacheRemove) {
		this.permanentNodesCacheRemove = permanentNodesCacheRemove;
	}

	public void setPermWipeOnEnter(boolean permWipeOnEnter) {
		this.permWipeOnEnter = permWipeOnEnter;
	}

	public void setPermWipeOnExit(boolean permWipeOnExit) {
		this.permWipeOnExit = permWipeOnExit;
	}

	public void setPlayCustomSoundUrl(boolean playCustomSoundUrl) {
		this.playCustomSoundUrl = playCustomSoundUrl;
	}

	public void setPlayerCap(int playerCap) {
		this.playerCap = playerCap;
	}

	public void setPlayersInRegion(ArrayList<Player> playersInRegion) {
		this.playersInRegion = playersInRegion;
	}

	public void setPreventEntry(boolean preventEntry) {
		this.preventEntry = preventEntry;
	}

	public void setPreventEntryMessage(String preventEntryMessage) {
		this.preventEntryMessage = preventEntryMessage;
	}

	public void setPreventEntryMode(MODE preventEntryMode) {
		this.preventEntryMode = preventEntryMode;
	}

	public void setPreventExit(boolean preventExit) {
		this.preventExit = preventExit;
	}

	public void setPreventExitMessage(String preventExitMessage) {
		this.preventExitMessage = preventExitMessage;
	}

	public void setPreventExitMode(MODE preventExitMode) {
		this.preventExitMode = preventExitMode;
	}

	public void setPreventInteraction(boolean preventInteraction) {
		this.preventInteraction = preventInteraction;
	}

	public void setProtectionMessage(String protectionMessage) {
		this.protectionMessage = protectionMessage;
	}

	public void setProtectionMode(MODE protectionMode) {
		this.protectionMode = protectionMode;
	}

	public void setPvp(boolean pvp) {
		this.pvp = pvp;
	}

	public void setSalePrice(int salePrice) {
		this.salePrice = salePrice;
	}

	public void setShowLeaveMessage(boolean showLeaveMessage) {
		this.showLeaveMessage = showLeaveMessage;
	}

	public void setShowPreventEntryMessage(boolean showPreventEntryMessage) {
		this.showPreventEntryMessage = showPreventEntryMessage;
	}

	public void setShowPreventExitMessage(boolean showPreventExitMessage) {
		this.showPreventExitMessage = showPreventExitMessage;
	}

	public void setShowProtectionMessage(boolean showProtectionMessage) {
		this.showProtectionMessage = showProtectionMessage;
	}

	public void setShowPvpWarning(boolean showPvpWarning) {
		this.showPvpWarning = showPvpWarning;
	}

	public void setShowWelcomeMessage(boolean showWelcomeMessage) {
		this.showWelcomeMessage = showWelcomeMessage;
	}

	public void setSpoutEntryMaterial(Material spoutEntryMaterial) {
		this.spoutEntryMaterial = spoutEntryMaterial;
	}

	public void setSpoutEntryMessage(String spoutEntryMessage) {
		this.spoutEntryMessage = spoutEntryMessage;
	}

	public void setSpoutExitMaterial(Material spoutExitMaterial) {
		this.spoutExitMaterial = spoutExitMaterial;
	}

	public void setSpoutExitMessage(String spoutExitMessage) {
		this.spoutExitMessage = spoutExitMessage;
	}

	public void setSpoutLeaveEnabled(boolean spoutLeaveEnabled) {
		this.spoutLeaveEnabled = spoutLeaveEnabled;
	}

	public void setSpoutTexturePack(String spoutTexturePack) {
		this.spoutTexturePack = spoutTexturePack;
	}

	public void setSpoutWelcomeEnabled(boolean spoutWelcomeEnabled) {
		this.spoutWelcomeEnabled = spoutWelcomeEnabled;
	}

	public void setSubOwners(String[] subOwners) {
		this.subOwners = subOwners;
	}

	public void setTempNodesCacheAdd(String[] temporaryNodesCacheAdd) {
		this.temporaryNodesCacheAdd = temporaryNodesCacheAdd;
	}

	public void setTempNodesCacheRem(String[] temporaryNodesCacheRem) {
		this.temporaryNodesCacheRem = temporaryNodesCacheRem;
	}

	private void setTimestamp(Player p) {
		timeStamps.put(p, System.currentTimeMillis());
	}

	public void setTimeStamps(HashMap<Player, Long> timeStamps) {
		this.timeStamps = timeStamps;
	}

	public void setTNTEnabled(boolean TNTEnabled) {
		this.TNTEnabled = TNTEnabled;
	}

	public void setUseSpoutTexturePack(boolean useSpoutTexturePack) {
		this.useSpoutTexturePack = useSpoutTexturePack;
	}

	public void setVelocityWarp(double velocityWarp) {
		this.velocityWarp = velocityWarp;
	}

	public void setWarp(Location warp) {
		this.warp = warp;
	}

	public void setWelcomeMessage(String welcomeMessage) {
		this.welcomeMessage = welcomeMessage;
	}

	public void setWelcomeMessageSent(HashMap<Player, Boolean> welcomeMessageSent) {
		this.welcomeMessageSent = welcomeMessageSent;
	}

	public void setWipeAndCacheOnEnter(boolean wipeAndCacheOnEnter) {
		this.wipeAndCacheOnEnter = wipeAndCacheOnEnter;
	}

	public void setWipeAndCacheOnExit(boolean wipeAndCacheOnExit) {
		this.wipeAndCacheOnExit = wipeAndCacheOnExit;
	}

	public void setWorld(World world) {
		this.world = world;
	}

	public void setDispensersLocked(boolean dispensersLocked) {
		this.dispensersLocked = dispensersLocked;
		
	}
}
