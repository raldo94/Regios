package net.jzx7.regios.Commands;

import java.io.IOException;

import net.jzx7.regios.Permissions.PermissionsCore;
import net.jzx7.regios.entity.PlayerManager;
import net.jzx7.regios.regions.RegionManager;
import net.jzx7.regiosapi.entity.RegiosPlayer;
import net.jzx7.regiosapi.location.RegiosPoint;
import net.jzx7.regiosapi.regions.Region;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;


public class WarpCommands extends PermissionsCore {

	private static final RegionManager rm = new RegionManager();
	private static final PlayerManager pm = new PlayerManager();

	public void warp(RegiosPlayer p, String[] args) {
		if (doesHaveNode(p, "regios.fun.warpto")) {
			if (args.length >= 2) {
				warpToRegion(args[1], p);
				return;
			} else {
				p.sendMessage("<RED>" + "[Regios] Invalid number of arguments specified.");
				p.sendMessage("Proper usage: /regios warpto <region>");
				return;
			}
		} else {
			sendInvalidPerms(p);
			return;
		}
	}
	
	public void setwarp(RegiosPlayer p, String[] args) {
		if (doesHaveNode(p, "regios.fun.set-warp")) {
			if (args.length == 1) {
				setWarp(p);
			} else {
				p.sendMessage("<RED>" + "[Regios] Invalid number of arguments specified.");
				p.sendMessage("Proper usage: /regios setwarp");
				return;
			}
		} else {
			sendInvalidPerms(p);
		}
	}
	
	public void resetwarp(RegiosPlayer p, String[] args) {
		if (doesHaveNode(p, "regios.fun.set-warp")) {
			if (args.length == 2) {
				resetWarp(rm.getRegion(args[1]), args[1], p);
			} else {
				p.sendMessage("<RED>" + "[Regios] Invalid number of arguments specified.");
				p.sendMessage("Proper usage: /regios resetwarp <region>");
				return;
			}
		} else {
			sendInvalidPerms(p);
		}
	}

	private void warpToRegion(String region, RegiosPlayer p){
		Region reg = rm.getRegion(region);
		if(reg == null){
			p.sendMessage("<RED>" + "[Regios] The region " + "<BLUE>" + region + "<RED>" + " does not exist!");
			return;
		} else {
			warpTo(p, reg);
		}
	}

	private void warpTo(RegiosPlayer p, Region r){
		if(r.getWarp() == null){
			p.sendMessage("<RED>" + "[Regios] No warp has been set for region : " + "<BLUE>" + r.getName());
			return;
		} if(r.getWarp().getRegiosWorld() == null){
			p.sendMessage("<RED>" + "[Regios] No warp has been set for region : " + "<BLUE>" + r.getName());
			return;
		}
		if(r.getWarp().getBlockX() == 0 && r.getWarp().getBlockY() == 0 && r.getWarp().getBlockZ() == 0){
			p.sendMessage("<RED>" + "[Regios] No warp has been set for region : " + "<BLUE>" + r.getName());
			return;
		} else {
			if(r.isPreventEntry() && !r.canEnter(p)){
				p.sendMessage("<RED>" + "[Regios] No are not permitted to warp to region : " + "<BLUE>" + r.getName());
				return;
			} else {
				p.teleport(r.getWarp());
				return;
			}
		}
	}

	private void setWarp(RegiosPlayer p){
		RegiosPoint loc = p.getPoint();
		Region r = null;
		if(pm.getCurrentRegion().containsKey(p.getName())){
			r = pm.getCurrentRegion().get(p.getName());
		}
		if(r == null){ p.sendMessage("<RED>" + "[Regios] You are not in a Region!"); return; } else {
			if(!r.canModify(p)){
				p.sendMessage("<RED>" + "[Regios] You are not permitted to modify this region!");
				return;
			}
			FileConfiguration c = YamlConfiguration.loadConfiguration(rm.getConfigFile(r));
			c.set("Region.Teleportation.Warp.Point", loc.getRegiosWorld().getName() + "," + loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ());
			r.setWarp(loc);
			try {
				c.save(rm.getConfigFile(r));
			} catch (IOException e) {
				e.printStackTrace();
			}
			p.sendMessage("<DGREEN>" + "[Regios] Warp for region " + "<BLUE>" + r.getName() + "<DGREEN>" + " set to current location");
		}
	}

	private void resetWarp(Region r, String region, RegiosPlayer p){
		if(r == null){ p.sendMessage("<RED>" + "[Regios] The region " + "<BLUE>" + region + "<RED>" + " doesn't exist!"); return; } else {
			if(!r.canModify(p)){
				p.sendMessage("<RED>" + "[Regios] You are not permitted to modify this region!");
				return;
			}
			FileConfiguration c = YamlConfiguration.loadConfiguration(rm.getConfigFile(r));
			c.set("Region.Teleportation.Warp.Point", r.getWorld().getName() + ",0,0,0");
			r.setWarp(new RegiosPoint(r.getWorld(), 0, 0, 0));
			try {
				c.save(rm.getConfigFile(r));
			} catch (IOException e) {
				e.printStackTrace();
			}
			p.sendMessage("<DGREEN>" + "[Regios] Warp Point for region " + "<BLUE>" + region + "<DGREEN>" + " reset.");
		}
	}
}
