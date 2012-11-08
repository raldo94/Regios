package net.jzx7.regios.regions;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import net.jzx7.regios.Data.ConfigurationData;
import net.jzx7.regios.Data.Saveable;
import net.jzx7.regios.GameMode.GameModeCacheManager;
import net.jzx7.regios.Inventory.InventoryCacheManager;
import net.jzx7.regios.Listeners.RegiosPlayerListener;
import net.jzx7.regios.Permissions.PermChecks;
import net.jzx7.regios.Permissions.PermissionsCacheManager;
import net.jzx7.regios.Scheduler.HealthRegeneration;
import net.jzx7.regios.Scheduler.LightningRunner;
import net.jzx7.regios.Scheduler.LogRunner;
import net.jzx7.regios.Spout.SpoutInterface;
import net.jzx7.regios.Spout.SpoutRegion;
import net.jzx7.regiosapi.regions.Region;
import net.jzx7.regiosapi.data.MODE;
import net.jzx7.regiosapi.events.RegionCreateEvent;
import net.jzx7.regiosapi.events.RegionEnterEvent;
import net.jzx7.regiosapi.events.RegionExitEvent;
import net.jzx7.regiosapi.events.RegionLoadEvent;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

import couk.Adamki11s.Extras.Cryptography.ExtrasCryptography;

public class RegiosRegion extends PermChecks implements Region {

	protected static final RegionManager rm = new RegionManager();
	
	protected GameModeCacheManager gmcm = new GameModeCacheManager();
	
	protected InventoryCacheManager icm = new InventoryCacheManager();

	protected static final Saveable saveable = new Saveable();

	protected World world;

	protected Location warp = null;

	protected String[] customSoundUrl,
	commandSet,
	temporaryNodesCacheAdd,
	temporaryNodesCacheRem,
	permanentNodesCacheAdd,
	permanentNodesCacheRemove,
	permanentGroupAdd,
	permanentGroupRemove,
	temporaryGroupAdd,
	temporaryGroupRemove,
	subOwners;

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
			, explosionsEnabled = true
			, changeGameMode = false
			, blockEndermanMod = false;

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

	protected HashMap<String, Boolean> authentication = new HashMap<String, Boolean>()
			, welcomeMessageSent = new HashMap<String, Boolean>()
			, leaveMessageSent = new HashMap<String, Boolean>();

	protected HashMap<String, Long> timeStamps = new HashMap<String, Long>();

	protected ArrayList<String> playersInRegion = new ArrayList<String>();

	protected HashMap<String, PlayerInventory> inventoryCache = new HashMap<String, PlayerInventory>();

	protected ExtrasCryptography exCrypt = new ExtrasCryptography();

	public RegiosRegion(String owner, String name, World world, Player p, boolean save) {
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
		temporaryNodesCacheRem = ConfigurationData.temporaryNodesCacheRem;
		permanentNodesCacheAdd = ConfigurationData.permanentNodesCacheAdd;
		permanentNodesCacheRemove = ConfigurationData.permanentNodesCacheRemove;
		temporaryGroupAdd = ConfigurationData.temporaryGroupsAdd;
		temporaryGroupRemove = ConfigurationData.temporaryGroupsRem;
		permanentGroupAdd = ConfigurationData.permanentGroupsAdd;
		permanentGroupRemove = ConfigurationData.permanentGroupsRemove;
		spoutTexturePack = "";
		useSpoutTexturePack = false;
		forSale = ConfigurationData.forSale;
		salePrice = ConfigurationData.salePrice;
		blockForm = ConfigurationData.blockForm;
		explosionsEnabled = ConfigurationData.explosionsEnabled;
		gameMode = ConfigurationData.gameMode;
		blockEndermanMod = ConfigurationData.blockEndermanMod;
		if (LSPS > 0 && !LightningRunner.doesStrikesContain(this)) {
			LightningRunner.addRegion(this);
		} else if (LSPS == 0 && LightningRunner.doesStrikesContain(this)) {
			LightningRunner.removeRegion(this);
		}
	}
	
	@Override
	public void setBiome(Biome biome, Player p) {
		//Placeholder for setBiome
	}
	
	@Override
	public void addException(String exception) {
		exceptions.add(exception);
		LogRunner.addLogMessage(this, LogRunner.getPrefix(this) + (" Exception '" + exception + "' added."));
	}

	@Override
	public void addExceptionNode(String node) {
		nodes.add(node);
		LogRunner.addLogMessage(this, LogRunner.getPrefix(this) + (" Exception node '" + node + "' added."));
	}

	@Override
	public void addItemException(int id) {
		items.add(id);
	}

	@Override
	public void addPlayer(Player p) {
		playersInRegion.add(p.getName());
	}

	@Override
	public boolean areChestsLocked() {
		return chestsLocked;
	}

	@Override
	public boolean areDispensersLocked() {
		return dispensersLocked;
	}

	@Override
	public boolean areDoorsLocked() {
		return doorsLocked;
	}

	@Override
	public boolean canBypassProtection(Player p) {
		return super.canBypassProtection(p, this);
	}

	@Override
	public boolean canModify(Player p) {
		return super.canModify(p, this);
	}

	@Override
	public boolean canBuild(Player p) {
		return super.canBypassProtection(p, this);
	}

	@Override
	public boolean canPlaceItem(Player p, Material m) {
		return super.canItemBePlaced(p, m, this);
	}

	@Override
	public boolean canEnter(Player p) {
		return super.canEnter(p, this);
	}

	@Override
	public boolean canExit(Player p) {
		return super.canExit(p, this);
	}

	@Override
	public GameMode getGameMode() {
		return gameMode;
	}

	@Override
	public void setGameMode(GameMode gm) {
		gameMode = gm;
	}

	@Override
	public void setChangeGameMode(boolean val)
	{
		changeGameMode = val;
	}

	@Override
	public boolean isChangeGameMode()
	{
		return changeGameMode;
	}

	@Override
	public void setBlockEndermanMod(boolean val)
	{
		blockEndermanMod = val;
	}

	@Override
	public boolean isBlockEndermanMod()
	{
		return blockEndermanMod;
	}

	@Override
	public boolean canMobsSpawn() {
		return mobSpawns;
	}

	@Override
	public boolean canMonstersSpawn() {
		return monsterSpawns;
	}

	@Override
	public HashMap<String, Boolean> getAuthentication() {
		return authentication;
	}

	@Override
	public boolean getAuthentication(String password, Player p) {
		if (exCrypt.compareHashes(exCrypt.computeSHA2_384BitHash(password), exCrypt.computeSHA2_384BitHash(this.password))) {
			authentication.put(p.getName(), true);
			return true;
		} else {
			authentication.put(p.getName(), false);
			return false;
		}
	}

	@Override
	public File getBackupsDirectory() {
		return new File("plugins" + File.separator + "Regios" + File.separator + "Database" + File.separator + name + File.separator + "Backups");
	}

	@Override
	public String[] getCommandSet() {
		return commandSet;
	}

	@Override
	public File getConfigFile() {
		return new File("plugins" + File.separator + "Regios" + File.separator + "Database" + File.separator + name + File.separator + name
				+ ".rz");
	}

	@Override
	public String[] getCustomSoundUrl() {
		return customSoundUrl;
	}

	@Override
	public File getDirectory(){
		return new File("plugins" + File.separator + "Regios" + File.separator + "Database" + File.separator + name);
	}

	@Override
	public File getExceptionDirectory() {
		return new File("plugins" + File.separator + "Regios" + File.separator + "Database" + File.separator + name + File.separator + "Exceptions");
	}

	@Override
	public ArrayList<String> getExceptionNodes() {
		return nodes;
	}

	@Override
	public ArrayList<String> getExceptions() {
		return exceptions;
	}

	@Override
	public int getHealthRegen() {
		return healthRegen;
	}

	@Override
	public HashMap<String, PlayerInventory> getInventoryCache() {
		return inventoryCache;
	}

	@Override
	public PlayerInventory getInventoryCache(Player p) {
		return inventoryCache.containsKey(p.getName()) ? inventoryCache.get(p.getName()) : null;
	}

	@Override
	public MODE getItemMode() {
		return itemMode;
	}

	@Override
	public ArrayList<Integer> getItems() {
		return items;
	}

	@Override
	public String getLeaveMessage() {
		return leaveMessage;
	}

	@Override
	public HashMap<String, Boolean> getLeaveMessageSent() {
		return leaveMessageSent;
	}

	@Override
	public File getLogFile() {
		return new File("plugins" + File.separator + "Regios" + File.separator + "Database" + File.separator + name + File.separator + "Logs" + File.separator
				+ name + ".log");
	}

	@Override
	public int getLSPS() {
		return LSPS;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public ArrayList<String> getNodes() {
		return nodes;
	}

	@Override
	public String getOwner() {
		return owner;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String[] getPermAddNodes() {
		return permanentNodesCacheAdd;
	}

	@Override
	public String[] getPermRemoveNodes() {
		return permanentNodesCacheRemove;
	}

	@Override
	public String[] getPermAddGroups() {
		return permanentGroupAdd;
	}

	@Override
	public String[] getPermRemoveGroups() {
		return permanentGroupRemove;
	}

	@Override
	public String[] getTempAddGroups() {
		return temporaryGroupAdd;
	}

	@Override
	public String[] getTempRemoveGroups() {
		return temporaryGroupRemove;
	}

	@Override
	public void setPermAddGroups(String[] val) {
		permanentGroupAdd = val;
	}

	@Override
	public void setPermRemoveGroups(String[] val) {
		permanentGroupRemove = val;
	}

	@Override
	public void setTempAddGroups(String[] val) {
		temporaryGroupAdd = val;
	}

	@Override
	public void setTempRemoveGroups(String[] val) {
		temporaryGroupRemove = val;
	}

	@Override
	public int getPlayerCap() {
		return playerCap;
	}

	@Override
	public ArrayList<String> getPlayersInRegion() {
		return playersInRegion;
	}

	@Override
	public String getPreventEntryMessage() {
		return preventEntryMessage;
	}

	@Override
	public MODE getPreventEntryMode() {
		return preventEntryMode;
	}

	@Override
	public String getPreventExitMessage() {
		return preventExitMessage;
	}

	@Override
	public MODE getPreventExitMode() {
		return preventExitMode;
	}

	@Override
	public String getProtectionMessage() {
		return protectionMessage;
	}

	@Override
	public MODE getProtectionMode() {
		return protectionMode;
	}

	@Override
	public File getRawConfigFile(){
		return new File(("plugins" + File.separator + "Regios" + File.separator + "Database" + File.separator + name + File.separator + name + ".rz"));
	}

	@Override
	public int getSalePrice() {
		return salePrice;
	}

	@Override
	public Material getSpoutLeaveMaterial() {
		return spoutExitMaterial;
	}

	@Override
	public String getSpoutLeaveMessage() {
		return spoutExitMessage;
	}

	@Override
	public String getSpoutTexturePack() {
		return spoutTexturePack;
	}

	@Override
	public Material getSpoutWelcomeMaterial() {
		return spoutEntryMaterial;
	}

	@Override
	public String getSpoutWelcomeMessage() {
		return spoutEntryMessage;
	}

	@Override
	public String[] getSubOwners() {
		return subOwners;
	}

	@Override
	public String[] getTempNodesCacheAdd() {
		return temporaryNodesCacheAdd;
	}

	@Override
	public String[] getTempNodesCacheRem() {
		return temporaryNodesCacheRem;
	}

	@Override
	public HashMap<String, Long> getTimeStamps() {
		return timeStamps;
	}

	@Override
	public double getVelocityWarp() {
		return velocityWarp;
	}

	@Override
	public Location getWarp() {
		return warp;
	}

	@Override
	public String getWelcomeMessage() {
		return welcomeMessage;
	}

	@Override
	public HashMap<String, Boolean> getWelcomeMessageSent() {
		return welcomeMessageSent;
	}

	@Override
	public World getWorld() {
		return world;
	}

	@Override
	public boolean is_protectionBreak() {
		return _protectionBreak;
	}

	@Override
	public boolean is_protectionPlace() {
		return _protectionPlace;
	}

	@Override
	public boolean isAuthenticated(Player p) {
		if (authentication.containsKey(p.getName())) {
			return authentication.get(p.getName());
		} else {
			return false;
		}
	}

	@Override
	public boolean isBlockForm() {
		return blockForm;
	}

	@Override
	public boolean isFireProtection() {
		return fireProtection;
	}

	@Override
	public boolean isFireSpread() {
		return fireSpread;
	}

	@Override
	public boolean isForceCommand() {
		return forceCommand;
	}

	@Override
	public boolean isForSale() {
		return forSale;
	}

	@Override
	public boolean isHealthEnabled() {
		return healthEnabled;
	}

	private boolean isLeaveMessageSent(Player p) {
		if (!leaveMessageSent.containsKey(p.getName())) {
			return false;
		} else {
			return leaveMessageSent.get(p.getName());
		}
	}

	@Override
	public boolean isPasswordEnabled() {
		return passwordEnabled;
	}

	@Override
	public boolean isPermWipeOnEnter() {
		return permWipeOnEnter;
	}

	@Override
	public boolean isPermWipeOnExit() {
		return permWipeOnExit;
	}

	@Override
	public boolean isPlayCustomSoundUrl() {
		return playCustomSoundUrl;
	}

	@Override
	public boolean isPlayerInRegion(Player p) {
		if (playersInRegion.contains(p)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean isPreventEntry() {
		return preventEntry;
	}

	@Override
	public boolean isPreventExit() {
		return preventExit;
	}

	@Override
	public boolean isPreventInteraction() {
		return preventInteraction;
	}

	@Override
	public boolean isProtected() {
		return _protection;
	}

	@Override
	public boolean isPvp() {
		return pvp;
	}

	@Override
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

	private boolean isSendable(Player p) {
		boolean outcome = (timeStamps.containsKey(p.getName()) ? (System.currentTimeMillis() > timeStamps.get(p.getName()) + 5000) : true);
		if (outcome) {
			setTimestamp(p);
		}
		return outcome;
	}

	@Override
	public boolean isShowLeaveMessage() {
		return showLeaveMessage;
	}

	@Override
	public boolean isShowPreventEntryMessage() {
		return showPreventEntryMessage;
	}

	@Override
	public boolean isShowPreventExitMessage() {
		return showPreventExitMessage;
	}

	@Override
	public boolean isShowProtectionMessage() {
		return showProtectionMessage;
	}

	@Override
	public boolean isShowPvpWarning() {
		return showPvpWarning;
	}

	@Override
	public boolean isShowWelcomeMessage() {
		return showWelcomeMessage;
	}

	@Override
	public boolean isSpoutLeaveEnabled() {
		return spoutLeaveEnabled;
	}

	@Override
	public boolean isSpoutWelcomeEnabled() {
		return spoutWelcomeEnabled;
	}

	@Override
	public boolean isExplosionsEnabled() {
		return explosionsEnabled;
	}

	@Override
	public boolean isUseSpoutTexturePack() {
		return useSpoutTexturePack;
	}

	private boolean isWelcomeMessageSent(Player p) {
		if (!welcomeMessageSent.containsKey(p.getName())) {
			return false;
		} else {
			return welcomeMessageSent.get(p.getName());
		}
	}

	@Override
	public boolean isWipeAndCacheOnEnter() {
		return wipeAndCacheOnEnter;
	}

	@Override
	public boolean isWipeAndCacheOnExit() {
		return wipeAndCacheOnExit;
	}

	@Override
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
			for (String play : playersInRegion) {
				builder.append(ChatColor.WHITE).append(play).append(ChatColor.BLUE).append(", ");
			}
			original = original.replaceAll("PLAYER-LIST", "" + builder.toString());
		}
		return original;
	}

	@Override
	public String colourFormat(String message) {
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
		message = message.replaceAll("OWNER", this.getOwner());
		message = message.replaceAll("NAME", this.getName());

		return message;
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

	@Override
	public void removeException(String exception) {
		if (exceptions.contains(exception)) {
			exceptions.remove(exception);
			LogRunner.addLogMessage(this, LogRunner.getPrefix(this) + (" Exception '" + exception + "' removed."));
		}
	}

	@Override
	public void removeExceptionNode(String node) {
		if (nodes.contains(node)) {
			LogRunner.addLogMessage(this, LogRunner.getPrefix(this) + (" Exception node '" + node + "' removed."));
			nodes.remove(node);
		}
	}

	@Override
	public void removeItemException(int id) {
		if (items.contains(id)) {
			items.remove((Object) id);
		}
	}

	@Override
	public void removePlayer(Player p) {
		if (playersInRegion.contains(p)) {
			playersInRegion.remove(p);
		}
	}

	@Override
	public void resetAuthentication(Player p) {
		authentication.put(p.getName(), false);
	}

	@Override
	public void sendBuildMessage(Player p) {
		if (showProtectionMessage && isSendable(p)) {
			LogRunner.addLogMessage(this, LogRunner.getPrefix(this) + (" Player '" + p.getName() + "' tried to build but did not have permissions."));
			p.sendMessage(colourFormat(liveFormat(protectionMessage, p)));
		}
	}

	@Override
	public void sendLeaveMessage(Player p) {
		if (!isLeaveMessageSent(p)) {
			registerExitEvent(p);
			LogRunner.addLogMessage(this, LogRunner.getPrefix(this) + (" Player '" + p.getName() + "' left region."));
			if (RegiosPlayerListener.currentRegion.containsKey(p.getName())) {
				RegiosPlayerListener.currentRegion.remove(p.getName());
			}
			leaveMessageSent.put(p.getName(), true);
			welcomeMessageSent.remove(p.getName());
			removePlayer(p);
			if (HealthRegeneration.isRegenerator(p)) {
				HealthRegeneration.removeRegenerator(p);
			}
			if (permWipeOnExit) {
				if (!canBypassProtection(p)) {
					icm.wipeInventory(p);
				}
			}
			if (wipeAndCacheOnEnter) {
				if (!canBypassProtection(p)) {
					if (icm.doesCacheContain(p)) {
						icm.restoreInventory(p);
						LogRunner.addLogMessage(this, LogRunner.getPrefix(this) + (" Player '" + p.getName() + "' inventory restored upon exit"));
					}
				}
			}
			if (wipeAndCacheOnExit) {
				if (!canBypassProtection(p)) {
					if (!icm.doesCacheContain(p)) {
						icm.cacheInventory(p);
						icm.wipeInventory(p);
						LogRunner.addLogMessage(this, LogRunner.getPrefix(this) + (" Player '" + p.getName() + "' inventory cached upon exit"));
					}
				}
			}

			if (changeGameMode) {
				if (gmcm.doesCacheContain(p))
				{
					gmcm.restoreGameMode(p);
				}
			}

			LogRunner.addLogMessage(this, LogRunner.getPrefix(this) + (" Player '" + p.getName() + "' left region."));

			try {
				/*
				 * Permission Nodes
				 */
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
				/*
				 * End Permission Nodes
				 */
				/*
				 * Groups
				 */
				if (temporaryGroupAdd != null) {
					if (temporaryNodesCacheAdd.length > 0) {
						PermissionsCacheManager.unCacheAddGroups(p, this);
						LogRunner.addLogMessage(this, LogRunner.getPrefix(this) + (" Temporary add groups wiped upon region exit for player '" + p.getName() + "'"));
					}
				}
				if (temporaryGroupRemove != null) {
					if (temporaryGroupRemove.length > 0) {
						PermissionsCacheManager.unCacheRemoveGroups(p, this);
						LogRunner.addLogMessage(this, LogRunner.getPrefix(this) + (" Temporary remove groups restored upon region exit for player '" + p.getName() + "'"));
					}
				}
				if (permanentGroupRemove != null) {
					if (permanentGroupRemove.length > 0) {
						PermissionsCacheManager.permRemoveGroups(p, this);
						LogRunner.addLogMessage(this, LogRunner.getPrefix(this) + (" Permanent groups wiped upon region exit for player '" + p.getName() + "'"));
					}
				}
				/*
				 * End Groups
				 */
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

	@Override
	public void sendPreventEntryMessage(Player p) {
		if (showPreventEntryMessage && isSendable(p)) {
			LogRunner.addLogMessage(this, LogRunner.getPrefix(this) + (" Player '" + p.getName() + "' tried to enter but did not have permissions."));
			p.sendMessage(colourFormat(liveFormat(preventEntryMessage, p)));
		}
	}

	@Override
	public void sendPreventExitMessage(Player p) {
		if (showPreventExitMessage && isSendable(p)) {
			LogRunner.addLogMessage(this, LogRunner.getPrefix(this) + (" Player '" + p.getName() + "' tried to leave but did not have permissions."));
			p.sendMessage(colourFormat(liveFormat(preventExitMessage, p)));
		}
	}

	@Override
	public void sendWelcomeMessage(Player p) {
		if (!isWelcomeMessageSent(p)) {
			registerWelcomeEvent(p);
			LogRunner.addLogMessage(this, LogRunner.getPrefix(this) + (" Player '" + p.getName() + "' entered region."));
			if (useSpoutTexturePack && SpoutInterface.doesPlayerHaveSpout(p)) {
				SpoutRegion.forceTexturePack(p, this);
			}
			RegiosPlayerListener.currentRegion.put(p.getName(), this);
			welcomeMessageSent.put(p.getName(), true);
			leaveMessageSent.remove(p.getName());
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
					icm.wipeInventory(p);
				}
			}
			if (wipeAndCacheOnEnter) {
				if (!canBypassProtection(p)) {
					if (!icm.doesCacheContain(p)) {
						icm.cacheInventory(p);
						icm.wipeInventory(p);
						LogRunner.addLogMessage(this, LogRunner.getPrefix(this) + (" Player '" + p.getName() + "' inventory cached upon entry"));
					}
				}
			}
			if (wipeAndCacheOnExit) {
				if (!canBypassProtection(p)) {
					if (icm.doesCacheContain(p)) {
						icm.restoreInventory(p);
						LogRunner.addLogMessage(this, LogRunner.getPrefix(this) + (" Player '" + p.getName() + "' inventory restored upon entry"));
					}
				}
			}
			if (changeGameMode) {
				if(!gmcm.doesCacheContain(p)) {
					gmcm.cacheGameMode(p);
					p.setGameMode(getGameMode());
				}
			}
			LogRunner.addLogMessage(this, LogRunner.getPrefix(this) + (" Player '" + p.getName() + "' entered region."));
			if (forceCommand) {
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
			}
			try {
				/*
				 * Permission Nodes
				 */
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
				/*
				 * End Permission Nodes
				 */
				/*
				 * Groups
				 */
				if (temporaryGroupAdd != null) {
					if (temporaryGroupAdd.length > 0) {
						PermissionsCacheManager.cacheAddGroups(p, this);
						LogRunner.addLogMessage(this, LogRunner.getPrefix(this) + (" Temporary add groups added upon region enter for player '" + p.getName() + "'"));
					}

				}
				if (temporaryGroupRemove != null) {
					if (temporaryGroupRemove.length > 0) {
						PermissionsCacheManager.cacheRemoveGroups(p, this);
						LogRunner.addLogMessage(this, LogRunner.getPrefix(this) + (" Temporary remove groups wiped upon region enter for player '" + p.getName() + "'"));
					}

				}
				if (permanentGroupAdd != null) {
					if (permanentGroupAdd.length > 0) {
						PermissionsCacheManager.permAddGroups(p, this);
						LogRunner.addLogMessage(this, LogRunner.getPrefix(this) + (" Permanent groups added upon region enter for player '" + p.getName() + "'"));
					}
				}
				/*
				 * End Groups
				 */
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

	@Override
	public void set_protection(boolean _protection) {
		this._protection = _protection;
	}

	@Override
	public void set_protectionBreak(boolean _protectionBreak) {
		this._protectionBreak = _protectionBreak;
	}

	@Override
	public void set_protectionPlace(boolean _protectionPlace) {
		this._protectionPlace = _protectionPlace;
	}

	@Override
	public void setAuthentication(HashMap<String, Boolean> authentication) {
		this.authentication = authentication;
	}

	@Override
	public void setBlockForm(boolean blockForm) {
		this.blockForm = blockForm;
	}

	@Override
	public void setChestsLocked(boolean chestsLocked) {
		this.chestsLocked = chestsLocked;
	}

	@Override
	public void setCommandSet(String[] commandSet) {
		this.commandSet = commandSet;
	}

	@Override
	public void setCustomSoundUrl(String[] customSoundUrl) {
		this.customSoundUrl = customSoundUrl;
	}

	@Override
	public void setDoorsLocked(boolean doorsLocked) {
		this.doorsLocked = doorsLocked;
	}

	@Override
	public void setExceptions(ArrayList<String> exceptions) {
		this.exceptions = exceptions;
	}

	@Override
	public void setFireProtection(boolean fireProtection) {
		this.fireProtection = fireProtection;
	}

	@Override
	public void setFireSpread(boolean fireSpread) {
		this.fireSpread = fireSpread;
	}

	@Override
	public void setForceCommand(boolean forceCommand) {
		this.forceCommand = forceCommand;
	}

	@Override
	public void setForSale(boolean forSale) {
		this.forSale = forSale;
	}

	@Override
	public void setHealthEnabled(boolean healthEnabled) {
		this.healthEnabled = healthEnabled;
	}

	@Override
	public void setHealthRegen(int healthRegen) {
		this.healthRegen = healthRegen;
	}

	@Override
	public void setInventoryCache(HashMap<String, PlayerInventory> inventoryCache) {
		this.inventoryCache = inventoryCache;
	}

	@Override
	public void setItemMode(MODE itemMode) {
		this.itemMode = itemMode;
	}

	@Override
	public void setItems(ArrayList<Integer> items) {
		this.items = items;
	}

	@Override
	public void setLeaveMessage(String leaveMessage) {
		this.leaveMessage = leaveMessage;
	}

	@Override
	public void setLeaveMessageSent(HashMap<String, Boolean> leaveMessageSent) {
		this.leaveMessageSent = leaveMessageSent;
	}

	@Override
	public void setLSPS(int lSPS) {
		LSPS = lSPS;
	}

	@Override
	public void setMobSpawns(boolean mobSpawns) {
		this.mobSpawns = mobSpawns;
	}

	@Override
	public void setMonsterSpawns(boolean monsterSpawns) {
		this.monsterSpawns = monsterSpawns;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void setNodes(ArrayList<String> nodes) {
		this.nodes = nodes;
	}

	@Override
	public void setOwner(String owner) {
		this.owner = owner;
	}

	@Override
	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public void setPasswordEnabled(boolean passwordEnabled) {
		this.passwordEnabled = passwordEnabled;
	}

	@Override
	public void setPermanentNodesCacheAdd(String[] permanentNodesCacheAdd) {
		this.permanentNodesCacheAdd = permanentNodesCacheAdd;
	}

	@Override
	public void setPermanentNodesCacheRemove(String[] permanentNodesCacheRemove) {
		this.permanentNodesCacheRemove = permanentNodesCacheRemove;
	}

	@Override
	public void setPermWipeOnEnter(boolean permWipeOnEnter) {
		this.permWipeOnEnter = permWipeOnEnter;
	}

	@Override
	public void setPermWipeOnExit(boolean permWipeOnExit) {
		this.permWipeOnExit = permWipeOnExit;
	}

	@Override
	public void setPlayCustomSoundUrl(boolean playCustomSoundUrl) {
		this.playCustomSoundUrl = playCustomSoundUrl;
	}

	@Override
	public void setPlayerCap(int playerCap) {
		this.playerCap = playerCap;
	}

	@Override
	public void setPlayersInRegion(ArrayList<String> playersInRegion) {
		this.playersInRegion = playersInRegion;
	}

	@Override
	public void setPreventEntry(boolean preventEntry) {
		this.preventEntry = preventEntry;
	}

	@Override
	public void setPreventEntryMessage(String preventEntryMessage) {
		this.preventEntryMessage = preventEntryMessage;
	}

	@Override
	public void setPreventEntryMode(MODE preventEntryMode) {
		this.preventEntryMode = preventEntryMode;
	}

	@Override
	public void setPreventExit(boolean preventExit) {
		this.preventExit = preventExit;
	}

	@Override
	public void setPreventExitMessage(String preventExitMessage) {
		this.preventExitMessage = preventExitMessage;
	}

	@Override
	public void setPreventExitMode(MODE preventExitMode) {
		this.preventExitMode = preventExitMode;
	}

	@Override
	public void setPreventInteraction(boolean preventInteraction) {
		this.preventInteraction = preventInteraction;
	}

	@Override
	public void setProtectionMessage(String protectionMessage) {
		this.protectionMessage = protectionMessage;
	}

	@Override
	public void setProtectionMode(MODE protectionMode) {
		this.protectionMode = protectionMode;
	}

	@Override
	public void setPvp(boolean pvp) {
		this.pvp = pvp;
	}

	@Override
	public void setSalePrice(int salePrice) {
		this.salePrice = salePrice;
	}

	@Override
	public void setShowLeaveMessage(boolean showLeaveMessage) {
		this.showLeaveMessage = showLeaveMessage;
	}

	@Override
	public void setShowPreventEntryMessage(boolean showPreventEntryMessage) {
		this.showPreventEntryMessage = showPreventEntryMessage;
	}

	@Override
	public void setShowPreventExitMessage(boolean showPreventExitMessage) {
		this.showPreventExitMessage = showPreventExitMessage;
	}

	@Override
	public void setShowProtectionMessage(boolean showProtectionMessage) {
		this.showProtectionMessage = showProtectionMessage;
	}

	@Override
	public void setShowPvpWarning(boolean showPvpWarning) {
		this.showPvpWarning = showPvpWarning;
	}

	@Override
	public void setShowWelcomeMessage(boolean showWelcomeMessage) {
		this.showWelcomeMessage = showWelcomeMessage;
	}

	@Override
	public void setSpoutEntryMaterial(Material spoutEntryMaterial) {
		this.spoutEntryMaterial = spoutEntryMaterial;
	}

	@Override
	public void setSpoutEntryMessage(String spoutEntryMessage) {
		this.spoutEntryMessage = spoutEntryMessage;
	}

	@Override
	public void setSpoutExitMaterial(Material spoutExitMaterial) {
		this.spoutExitMaterial = spoutExitMaterial;
	}

	@Override
	public void setSpoutExitMessage(String spoutExitMessage) {
		this.spoutExitMessage = spoutExitMessage;
	}

	@Override
	public void setSpoutLeaveEnabled(boolean spoutLeaveEnabled) {
		this.spoutLeaveEnabled = spoutLeaveEnabled;
	}

	@Override
	public void setSpoutTexturePack(String spoutTexturePack) {
		this.spoutTexturePack = spoutTexturePack;
	}

	@Override
	public void setSpoutWelcomeEnabled(boolean spoutWelcomeEnabled) {
		this.spoutWelcomeEnabled = spoutWelcomeEnabled;
	}

	@Override
	public void setSubOwners(String[] subOwners) {
		this.subOwners = subOwners;
	}

	@Override
	public void setTempNodesCacheAdd(String[] temporaryNodesCacheAdd) {
		this.temporaryNodesCacheAdd = temporaryNodesCacheAdd;
	}

	@Override
	public void setTempNodesCacheRem(String[] temporaryNodesCacheRem) {
		this.temporaryNodesCacheRem = temporaryNodesCacheRem;
	}

	@Override
	public void setTimestamp(Player p) {
		timeStamps.put(p.getName(), System.currentTimeMillis());
	}

	@Override
	public void setTimeStamps(HashMap<String, Long> timeStamps) {
		this.timeStamps = timeStamps;
	}

	@Override
	public void setExplosionsEnabled(boolean explosionsEnabled) {
		this.explosionsEnabled = explosionsEnabled;
	}

	@Override
	public void setUseSpoutTexturePack(boolean useSpoutTexturePack) {
		this.useSpoutTexturePack = useSpoutTexturePack;
	}

	@Override
	public void setVelocityWarp(double velocityWarp) {
		this.velocityWarp = velocityWarp;
	}

	@Override
	public void setWarp(Location warp) {
		this.warp = warp;
	}

	@Override
	public void setWelcomeMessage(String welcomeMessage) {
		this.welcomeMessage = welcomeMessage;
	}

	@Override
	public void setWelcomeMessageSent(HashMap<String, Boolean> welcomeMessageSent) {
		this.welcomeMessageSent = welcomeMessageSent;
	}

	@Override
	public void setWipeAndCacheOnEnter(boolean wipeAndCacheOnEnter) {
		this.wipeAndCacheOnEnter = wipeAndCacheOnEnter;
	}

	@Override
	public void setWipeAndCacheOnExit(boolean wipeAndCacheOnExit) {
		this.wipeAndCacheOnExit = wipeAndCacheOnExit;
	}

	@Override
	public void setWorld(World world) {
		this.world = world;
	}

	@Override
	public void setDispensersLocked(boolean dispensersLocked) {
		this.dispensersLocked = dispensersLocked;
	}
}
