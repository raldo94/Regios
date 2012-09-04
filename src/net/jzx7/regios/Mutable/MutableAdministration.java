package net.jzx7.regios.Mutable;

import java.io.IOException;

import net.jzx7.regios.Data.LoaderCore;
import net.jzx7.regios.Data.Saveable;
import net.jzx7.regios.regions.RegionManager;
import net.jzx7.regiosapi.regions.Region;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;


public class MutableAdministration extends Saveable {

	private final static LoaderCore lc = new LoaderCore();
	
	private final static RegionManager rm = new RegionManager();

	public void setOwner(Region r, String owner) {
		FileConfiguration c = YamlConfiguration.loadConfiguration(r.getConfigFile());
		c.set("Region.Essentials.Owner", owner);
		r.setOwner(owner);
		try {
			c.save(r.getConfigFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void reload() {
		lc.silentReload();
	}

	public String listRegions() {
		StringBuilder sb = new StringBuilder();
		int build = 0;
		for (Region r : rm.getRegions()) {
			build++;
			sb.append(ChatColor.WHITE).append(r.getName().trim()).append(ChatColor.BLUE).append(", ");
		}
		if (build == 0) {
			return ChatColor.RED + "[Regios] No Regions Found!";
		} else {
			return sb.toString();
		}
	}
	
	public String listOwnedRegions(Player p) {
		StringBuilder sb = new StringBuilder();
		int count = 0;
		for(Region r : rm.getRegions()){
			if(r.getOwner().equalsIgnoreCase(p.getName())){
				count++;
				sb.append(ChatColor.WHITE).append(r.getName().trim()).append(ChatColor.BLUE).append(", ");
			}
		}
		if (count == 0) {
			return ChatColor.RED + "[Regios] No Regions Found!";
		} else {
			return sb.toString();
		}
	}

	public void inherit(Region tin, Region inf) {
		tin.set_protection(inf.isProtected());
		tin.setBlockForm(inf.isBlockForm());
		tin.setChestsLocked(inf.areChestsLocked());
		tin.setCommandSet(inf.getCommandSet());
		tin.setCustomSoundUrl(inf.getCustomSoundUrl());
		tin.setDoorsLocked(inf.areDoorsLocked());
		tin.setExceptions(inf.getExceptions());
		tin.setFireProtection(inf.isFireProtection());
		tin.setFireSpread(inf.isFireSpread());
		tin.setForceCommand(inf.isForceCommand());
		tin.setForSale(inf.isForSale());
		tin.setHealthEnabled(inf.isHealthEnabled());
		tin.setHealthRegen(inf.getHealthRegen());
		tin.setItemMode(inf.getItemMode());
		tin.setItems(inf.getItems());
		tin.setLeaveMessage(inf.getLeaveMessage());
		tin.setLSPS(inf.getLSPS());
		tin.setMobSpawns(inf.canMobsSpawn());
		tin.setMonsterSpawns(inf.canMonstersSpawn());
		tin.setNodes(inf.getNodes());
		tin.setPassword(inf.getPassword());
		tin.setPasswordEnabled(inf.isPasswordEnabled());
		tin.setPermanentNodesCacheAdd(inf.getPermAddNodes());
		tin.setPermanentNodesCacheRemove(inf.getPermRemoveNodes());
		tin.setPermWipeOnEnter(inf.isPermWipeOnEnter());
		tin.setPermWipeOnExit(inf.isPermWipeOnExit());
		tin.setPlayCustomSoundUrl(inf.isPlayCustomSoundUrl());
		tin.setPlayerCap(inf.getPlayerCap());
		tin.setPreventEntry(inf.isPreventEntry());
		tin.setPreventEntryMessage(inf.getPreventEntryMessage());
		tin.setPreventEntryMode(inf.getPreventEntryMode());
		tin.setPreventExit(inf.isPreventExit());
		tin.setPreventExitMessage(inf.getPreventExitMessage());
		tin.setPreventExitMode(inf.getPreventExitMode());
		tin.setPreventInteraction(inf.isPreventInteraction());
		tin.setProtectionMessage(inf.getProtectionMessage());
		tin.setProtectionMode(inf.getProtectionMode());
		tin.setPvp(inf.isPvp());
		tin.setSalePrice(inf.getSalePrice());
		tin.setShowLeaveMessage(inf.isShowLeaveMessage());
		tin.setShowPreventEntryMessage(inf.isShowPreventEntryMessage());
		tin.setShowPreventExitMessage(inf.isShowPreventExitMessage());
		tin.setShowProtectionMessage(inf.isShowProtectionMessage());
		tin.setShowPvpWarning(inf.isShowPvpWarning());
		tin.setShowWelcomeMessage(inf.isShowWelcomeMessage());
		tin.setSpoutEntryMaterial(inf.getSpoutWelcomeMaterial());
		tin.setSpoutEntryMessage(inf.getSpoutWelcomeMessage());
		tin.setSpoutExitMaterial(inf.getSpoutLeaveMaterial());
		tin.setSpoutExitMessage(inf.getSpoutLeaveMessage());
		tin.setSpoutTexturePack(inf.getSpoutTexturePack());
		tin.setSubOwners(inf.getSubOwners());
		tin.setTempNodesCacheAdd(inf.getTempNodesCacheAdd());
		tin.setTempNodesCacheRem(inf.getTempNodesCacheRem());
		tin.setUseSpoutTexturePack(inf.isUseSpoutTexturePack());
		tin.setVelocityWarp(inf.getVelocityWarp());
		tin.setWarp(inf.getWarp());
		tin.setWelcomeMessage(inf.getWelcomeMessage());
		tin.setWipeAndCacheOnEnter(inf.isWipeAndCacheOnEnter());
		tin.setWipeAndCacheOnExit(inf.isWipeAndCacheOnExit());
		tin.setChangeGameMode(inf.isChangeGameMode());
		tin.setGameMode(inf.getGameMode());
		tin.setBlockEndermanMod(inf.isBlockEndermanMod());
		tin.setWorld(inf.getWorld());
		try {
			super.updateInheritedRegion(tin);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
