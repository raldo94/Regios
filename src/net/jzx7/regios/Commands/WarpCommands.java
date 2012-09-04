package net.jzx7.regios.Commands;

import java.io.IOException;

import net.jzx7.regios.Listeners.RegiosPlayerListener;
import net.jzx7.regios.Permissions.PermissionsCore;
import net.jzx7.regios.regions.RegionManager;
import net.jzx7.regiosapi.regions.Region;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;


public class WarpCommands extends PermissionsCore {

	private static final RegionManager rm = new RegionManager();

	public void warp(Player p, String[] args) {
		if (doesHaveNode(p, "regios.fun.warpto")) {
			if (args.length >= 2) {
				warpToRegion(args[1], p);
				return;
			} else {
				p.sendMessage(ChatColor.RED + "[Regios] Invalid number of arguments specified.");
				p.sendMessage("Proper usage: /regios warpto <region>");
				return;
			}
		} else {
			sendInvalidPerms(p);
			return;
		}
	}
	
	public void setwarp(Player p, String[] args) {
		if (doesHaveNode(p, "regios.fun.set-warp")) {
			if (args.length == 1) {
				setWarp(p);
			} else {
				p.sendMessage(ChatColor.RED + "[Regios] Invalid number of arguments specified.");
				p.sendMessage("Proper usage: /regios setwarp");
				return;
			}
		} else {
			sendInvalidPerms(p);
		}
	}
	
	public void resetwarp(Player p, String[] args) {
		if (doesHaveNode(p, "regios.fun.set-warp")) {
			if (args.length == 2) {
				resetWarp(rm.getRegion(args[1]), args[1], p);
			} else {
				p.sendMessage(ChatColor.RED + "[Regios] Invalid number of arguments specified.");
				p.sendMessage("Proper usage: /regios resetwarp <region>");
				return;
			}
		} else {
			sendInvalidPerms(p);
		}
	}

	private void warpToRegion(String region, Player p){
		Region reg = rm.getRegion(region);
		if(reg == null){
			p.sendMessage(ChatColor.RED + "[Regios] The region " + ChatColor.BLUE + region + ChatColor.RED + " does not exist!");
			return;
		} else {
			warpTo(p, reg);
		}
	}

	private void warpTo(Player p, Region r){
		if(r.getWarp() == null){
			p.sendMessage(ChatColor.RED + "[Regios] No warp has been set for region : " + ChatColor.BLUE + r.getName());
			return;
		} if(r.getWarp().getWorld() == null){
			p.sendMessage(ChatColor.RED + "[Regios] No warp has been set for region : " + ChatColor.BLUE + r.getName());
			return;
		}
		if(r.getWarp().getBlockX() == 0 && r.getWarp().getBlockY() == 0 && r.getWarp().getBlockZ() == 0){
			p.sendMessage(ChatColor.RED + "[Regios] No warp has been set for region : " + ChatColor.BLUE + r.getName());
			return;
		} else {
			if(r.isPreventEntry() && !r.canEnter(p)){
				p.sendMessage(ChatColor.RED + "[Regios] No are not permitted to warp to region : " + ChatColor.BLUE + r.getName());
				return;
			} else {
				p.teleport(r.getWarp());
				return;
			}
		}
	}

	private void setWarp(Player p){
		Location loc = p.getLocation();
		Region r = null;
		if(RegiosPlayerListener.currentRegion.containsKey(p.getName())){
			r = RegiosPlayerListener.currentRegion.get(p.getName());
		}
		if(r == null){ p.sendMessage(ChatColor.RED + "[Regios] You are not in a Region!"); return; } else {
			if(!r.canModify(p)){
				p.sendMessage(ChatColor.RED + "[Regios] You are not permitted to modify this region!");
				return;
			}
			FileConfiguration c = YamlConfiguration.loadConfiguration(r.getConfigFile());
			c.set("Region.Teleportation.Warp.Location", loc.getWorld().getName() + "," + loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ());
			r.setWarp(loc);
			try {
				c.save(r.getConfigFile());
			} catch (IOException e) {
				e.printStackTrace();
			}
			p.sendMessage(ChatColor.GREEN + "[Regios] Warp for region " + ChatColor.BLUE + r.getName() + ChatColor.GREEN + " set to current location");
		}
	}

	private void resetWarp(Region r, String region, Player p){
		if(r == null){ p.sendMessage(ChatColor.RED + "[Regios] The region " + ChatColor.BLUE + region + ChatColor.RED + " doesn't exist!"); return; } else {
			if(!r.canModify(p)){
				p.sendMessage(ChatColor.RED + "[Regios] You are not permitted to modify this region!");
				return;
			}
			FileConfiguration c = YamlConfiguration.loadConfiguration(r.getConfigFile());
			c.set("Region.Teleportation.Warp.Location", r.getWorld().getName() + ",0,0,0");
			r.setWarp(new Location(r.getWorld(), 0, 0, 0));
			try {
				c.save(r.getConfigFile());
			} catch (IOException e) {
				e.printStackTrace();
			}
			p.sendMessage(ChatColor.GREEN + "[Regios] Warp Location for region " + ChatColor.BLUE + region + ChatColor.GREEN + " reset.");
		}
	}
}
