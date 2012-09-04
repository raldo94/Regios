package net.jzx7.regios.worlds;

import java.util.ArrayList;

import net.jzx7.regios.Permissions.PermissionsCore;
import net.jzx7.regiosapi.worlds.RegiosWorld;

import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;


public class RegWorld implements RegiosWorld {
	World world;
	
	private boolean invert_protection = false,
			invert_pvp = false,
			overridingPvp = false, 
			lightning_enabled = true, 
			stormEnabled = true, 
			explosionsEnabled = true, 
			fireEnabled = true,
			fireSpreadEnabled = true,
			blockForm_enabled = true,
			dragonProtect = true,
			blockEndermanMod = false,
			dragonPortal = true;
	
	private ArrayList<EntityType> creaturesWhoSpawn = new ArrayList<EntityType>();
	
	public RegWorld (World world) {
		this.world = world;
	}
	
	@Override
	public World getWorld() {
		return world;
	}

	@Override
	public boolean getProtection() {
		return invert_protection;
	}

	@Override
	public void setProtection(boolean val) {
		invert_protection = val;
	}
	
	@Override
	public boolean getPVP() {
		return invert_pvp;
	}

	@Override
	public void setPVP(boolean val) {
		invert_pvp = val;
	}

	@Override
	public boolean getOverridePVP() {
		return overridingPvp;
	}

	@Override
	public void setOverridePVP(boolean val) {
		overridingPvp = val;
	}

	@Override
	public boolean getLightningEnabled() {
		return lightning_enabled;
	}

	@Override
	public void setLightningEnabled(boolean val) {
		lightning_enabled = val;
	}

	@Override
	public boolean getStormsEnabled() {
		return stormEnabled;
	}

	@Override
	public void setStormsEnabled(boolean val) {
		stormEnabled = val;
	}

	@Override
	public boolean getExplosionsEnabled() {
		return explosionsEnabled;
	}

	@Override
	public void setExplosionsEnabled(boolean val) {
		explosionsEnabled = val;
	}

	@Override
	public boolean getFireEnabled() {
		return fireEnabled;
	}

	@Override
	public void setFireEnabled(boolean val) {
		fireEnabled = val;
	}

	@Override
	public boolean getFireSpreadEnabled() {
		return fireSpreadEnabled;
	}

	@Override
	public void setFireSpreadEnabled(boolean val) {
		fireSpreadEnabled = val;
	}

	@Override
	public boolean getBlockFormEnabled() {
		return blockForm_enabled;
	}

	@Override
	public void setBlockFormEnabled(boolean val) {
		blockForm_enabled = val;
	}

	@Override
	public boolean getDragonProtectionEnabled() {
		return dragonProtect;
	}

	@Override
	public void setDragonProtectionEnabled(boolean val) {
		dragonProtect = val;
	}

	@Override
	public boolean getEndermanProtectionEnabled() {
		return blockEndermanMod;
	}

	@Override
	public void setEndermanProtectionEnabled(boolean val) {
		blockEndermanMod = val;
	}
	
	@Override
	public boolean canCreatureSpawn(EntityType entityType){
		return creaturesWhoSpawn.contains(entityType);
	}
	
	@Override
	public void addCreatureSpawn(EntityType ct){
		creaturesWhoSpawn.add(ct);
	}
	
	@Override
	public boolean canBypassWorldChecks(Player p){
		return (PermissionsCore.doesHaveNode(p, "regios.worldprotection.bypass") || PermissionsCore.doesHaveNode(p, "regios." + world.getName() + ".bypass") || p.isOp());
	}

	@Override
	public boolean getEnderDragonCreatesPortal() {
		return dragonPortal;
	}

	@Override
	public void setEnderDragonCreatesPortal(boolean val) {
		dragonPortal = val;
		
	}
}