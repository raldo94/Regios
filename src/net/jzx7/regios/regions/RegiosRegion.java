package net.jzx7.regios.regions;

import java.util.ArrayList;
import java.util.HashMap;

import net.jzx7.regios.Data.ConfigurationData;
import net.jzx7.regios.Data.Saveable;
import net.jzx7.regios.GameMode.GameModeCacheManager;
import net.jzx7.regios.Inventory.InventoryCacheManager;
import net.jzx7.regios.Permissions.PermChecks;
import net.jzx7.regios.Scheduler.LightningRunner;
import net.jzx7.regios.Scheduler.LogRunner;
import net.jzx7.regios.util.EncryptUtil;
import net.jzx7.regios.util.RegiosConversions;
import net.jzx7.regiosapi.data.MODE;
import net.jzx7.regiosapi.entity.RegiosPlayer;
import net.jzx7.regiosapi.events.RegionCreateEvent;
import net.jzx7.regiosapi.events.RegionLoadEvent;
import net.jzx7.regiosapi.location.RegiosPoint;
import net.jzx7.regiosapi.regions.Region;
import net.jzx7.regiosapi.worlds.RegiosWorld;

import org.bukkit.Bukkit;

public abstract class RegiosRegion extends PermChecks implements Region {

	protected static final RegionManager rm = new RegionManager();

	protected static final Saveable saveable = new Saveable();

	protected boolean _protection = false, _protectionPlace = false,
			_protectionBreak = false, preventEntry = false,
			preventExit = false, mobSpawns = true, monsterSpawns = true,
			healthEnabled = true, pvp = true, doorsLocked = false,
			chestsLocked = false, dispensersLocked = false,
			preventInteraction = false, showPvpWarning = true,
			passwordEnabled = false, showWelcomeMessage = true,
			showLeaveMessage = true, showProtectionMessage = true,
			showPreventEntryMessage = true, showPreventExitMessage = true,
			fireProtection = false, fireSpread = true,
			playCustomSoundUrl = false, permWipeOnEnter = false,
			permWipeOnExit = false, wipeAndCacheOnEnter = false,
			wipeAndCacheOnExit = false, forceCommand = false, blockForm = true,
			forSale = false, useSpoutTexturePack = false,
			spoutWelcomeEnabled = false, spoutLeaveEnabled = false,
			explosionsEnabled = true, changeGameMode = false,
			blockEndermanMod = false;

	protected HashMap<String, Boolean> authentication = new HashMap<String, Boolean>(),
			welcomeMessageSent = new HashMap<String, Boolean>(),
			leaveMessageSent = new HashMap<String, Boolean>();

	protected String[] customSoundUrl, commandSet, temporaryNodesCacheAdd,
			temporaryNodesCacheRem, permanentNodesCacheAdd,
			permanentNodesCacheRemove, permanentGroupAdd, permanentGroupRemove,
			temporaryGroupAdd, temporaryGroupRemove, subOwners;

	protected ArrayList<String> exceptions = new ArrayList<String>(),
			nodes = new ArrayList<String>();

	protected EncryptUtil encUtil = new EncryptUtil();

	protected int gameMode = 0;

	protected GameModeCacheManager gmcm = new GameModeCacheManager();

	protected InventoryCacheManager icm = new InventoryCacheManager();

	protected ArrayList<Integer> items = new ArrayList<Integer>();

	protected int LSPS = 0, healthRegen = 0, playerCap = 0, salePrice = 0;

	protected ArrayList<String> playersInRegion = new ArrayList<String>();

	protected MODE protectionMode = MODE.Whitelist,
			preventEntryMode = MODE.Whitelist,
			preventExitMode = MODE.Whitelist, itemMode = MODE.Whitelist;

	protected int spoutEntryMaterial = 2,
			spoutExitMaterial = 3;

	protected HashMap<String, Long> timeStamps = new HashMap<String, Long>();

	protected double velocityWarp = 0;

	protected RegiosPoint warp = null;

	protected String welcomeMessage = "", leaveMessage = "",
			protectionMessage = "", preventEntryMessage = "",
			preventExitMessage = "", password = "", name = "", owner = "",
			spoutEntryMessage = "", spoutExitMessage = "",
			spoutTexturePack = "";

	protected RegiosWorld world;

	public RegiosRegion(String owner, String name, RegiosWorld world, RegiosPlayer p,
			boolean save) {
		this.owner = owner;
		this.name = name;

		if (world != null) {
			this.world = world;
		} else {
			world = RegiosConversions.getRegiosWorld(Bukkit.getServer().getWorlds().get(0));
		}

		if (save) {
			RegionCreateEvent event = new RegionCreateEvent("RegionCreateEvent");
			event.setProperties(RegiosConversions.getPlayer(p), this);
			Bukkit.getServer().getPluginManager().callEvent(event);
		} else {
			RegionLoadEvent event = new RegionLoadEvent("RegionLoadEvent");
			event.setProperties(this);
			Bukkit.getServer().getPluginManager().callEvent(event);
		}

		welcomeMessage = ConfigurationData.defaultWelcomeMessage;

		leaveMessage = ConfigurationData.defaultLeaveMessage;
		protectionMessage = ConfigurationData.defaultProtectionMessage;
		preventEntryMessage = ConfigurationData.defaultPreventEntryMessage;
		preventExitMessage = ConfigurationData.defaultPreventExitMessage;
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
	public void addException(String exception) {
		exceptions.add(exception);
		LogRunner.addLogMessage(this, LogRunner.getPrefix(this)
				+ (" Exception '" + exception + "' added."));
	}

	@Override
	public void addExceptionNode(String node) {
		nodes.add(node);
		LogRunner.addLogMessage(this, LogRunner.getPrefix(this)
				+ (" Exception node '" + node + "' added."));
	}

	@Override
	public void addItemException(int id) {
		items.add(id);
	}

	@Override
	public void addPlayer(RegiosPlayer p) {
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
	public boolean canBuild(RegiosPlayer p) {
		return super.canBypassProtection(p, this);
	}

	@Override
	public boolean canBypassProtection(RegiosPlayer p) {
		return super.canBypassProtection(p, this);
	}

	@Override
	public boolean canEnter(RegiosPlayer p) {
		return super.canEnter(p, this);
	}

	@Override
	public boolean canExit(RegiosPlayer p) {
		return super.canExit(p, this);
	}

	@Override
	public boolean canMobsSpawn() {
		return mobSpawns;
	}

	@Override
	public boolean canModify(RegiosPlayer p) {
		return super.canModify(p, this);
	}

	@Override
	public boolean canMonstersSpawn() {
		return monsterSpawns;
	}

	@Override
	public boolean canPlaceItem(RegiosPlayer p, int m) {
		return super.canItemBePlaced(p, m, this);
	}

	@Override
	public HashMap<String, Boolean> getAuthentication() {
		return authentication;
	}

	@Override
	public boolean getAuthentication(String password, RegiosPlayer p) {
		if (encUtil.compareHashes(encUtil.computeSHA2_384BitHash(password), (this.password))) {
			authentication.put(p.getName(), true);
			return true;
		} else {
			authentication.put(p.getName(), false);
			return false;
		}
	}

	@Override
	public String[] getCommandSet() {
		return commandSet;
	}

	@Override
	public String[] getCustomSoundUrl() {
		return customSoundUrl;
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
	public int getGameMode() {
		return gameMode;
	}

	public GameModeCacheManager getGMCM() {
		return gmcm;
	}

	@Override
	public int getHealthRegen() {
		return healthRegen;
	}

	public InventoryCacheManager getICM() {
		return icm;
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
	public String[] getPermAddGroups() {
		return permanentGroupAdd;
	}

	@Override
	public String[] getPermAddNodes() {
		return permanentNodesCacheAdd;
	}

	@Override
	public String[] getPermRemoveGroups() {
		return permanentGroupRemove;
	}

	@Override
	public String[] getPermRemoveNodes() {
		return permanentNodesCacheRemove;
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
	public int getSalePrice() {
		return salePrice;
	}

	@Override
	public int getSpoutLeaveMaterial() {
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
	public int getSpoutWelcomeMaterial() {
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
	public String[] getTempAddGroups() {
		return temporaryGroupAdd;
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
	public String[] getTempRemoveGroups() {
		return temporaryGroupRemove;
	}

	@Override
	public long getTimestamp(RegiosPlayer p) {
		return timeStamps.get(p);
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
	public RegiosPoint getWarp() {
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
	public RegiosWorld getWorld() {
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
	public boolean isAuthenticated(RegiosPlayer p) {
		if (authentication.containsKey(p.getName())) {
			return authentication.get(p.getName());
		} else {
			return false;
		}
	}

	@Override
	public boolean isBlockEndermanMod() {
		return blockEndermanMod;
	}

	@Override
	public boolean isBlockForm() {
		return blockForm;
	}

	@Override
	public boolean isChangeGameMode() {
		return changeGameMode;
	}

	@Override
	public boolean isExplosionsEnabled() {
		return explosionsEnabled;
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

	@Override
	public boolean isLeaveMessageSent(RegiosPlayer p) {
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
	public boolean isPlayerInRegion(RegiosPlayer p) {
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
	public boolean isRegionFull(RegiosPlayer p) {
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
	public boolean isUseSpoutTexturePack() {
		return useSpoutTexturePack;
	}

	@Override
	public boolean isWelcomeMessageSent(RegiosPlayer p) {
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
	public void removeException(String exception) {
		if (exceptions.contains(exception)) {
			exceptions.remove(exception);
			LogRunner.addLogMessage(this, LogRunner.getPrefix(this)
					+ (" Exception '" + exception + "' removed."));
		}
	}

	@Override
	public void removeExceptionNode(String node) {
		if (nodes.contains(node)) {
			LogRunner.addLogMessage(this, LogRunner.getPrefix(this)
					+ (" Exception node '" + node + "' removed."));
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
	public void removePlayer(RegiosPlayer p) {
		if (playersInRegion.contains(p)) {
			playersInRegion.remove(p);
		}
	}

	@Override
	public void resetAuthentication(RegiosPlayer p) {
		authentication.put(p.getName(), false);
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
	public void setBlockEndermanMod(boolean val) {
		blockEndermanMod = val;
	}

	@Override
	public void setBlockForm(boolean blockForm) {
		this.blockForm = blockForm;
	}

	@Override
	public void setChangeGameMode(boolean val) {
		changeGameMode = val;
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
	public void setDispensersLocked(boolean dispensersLocked) {
		this.dispensersLocked = dispensersLocked;
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
	public void setExplosionsEnabled(boolean explosionsEnabled) {
		this.explosionsEnabled = explosionsEnabled;
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
	public void setGameMode(int gm) {
		gameMode = gm;
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
	public void setPermAddGroups(String[] val) {
		permanentGroupAdd = val;
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
	public void setPermRemoveGroups(String[] val) {
		permanentGroupRemove = val;
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
	public void setSpoutEntryMaterial(int spoutEntryMaterial) {
		this.spoutEntryMaterial = spoutEntryMaterial;
	}

	@Override
	public void setSpoutEntryMessage(String spoutEntryMessage) {
		this.spoutEntryMessage = spoutEntryMessage;
	}

	@Override
	public void setSpoutExitMaterial(int spoutExitMaterial) {
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
	public void setTempAddGroups(String[] val) {
		temporaryGroupAdd = val;
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
	public void setTempRemoveGroups(String[] val) {
		temporaryGroupRemove = val;
	}

	@Override
	public void setTimestamp(RegiosPlayer p) {
		timeStamps.put(p.getName(), System.currentTimeMillis());
	}

	@Override
	public void setTimeStamps(HashMap<String, Long> timeStamps) {
		this.timeStamps = timeStamps;
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
	public void setWarp(RegiosPoint warp) {
		this.warp = warp;
	}

	@Override
	public void setWelcomeMessage(String welcomeMessage) {
		this.welcomeMessage = welcomeMessage;
	}

	@Override
	public void setWelcomeMessageSent(
			HashMap<String, Boolean> welcomeMessageSent) {
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
	public void setWorld(RegiosWorld world) {
		this.world = world;
	}
}
