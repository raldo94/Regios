package net.jzx7.regios.Commands;

import net.jzx7.regios.Mutable.MutableMobs;
import net.jzx7.regios.Permissions.PermissionsCore;
import net.jzx7.regios.regions.RegionManager;
import net.jzx7.regiosapi.regions.Region;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;


public class MobCommands extends PermissionsCore {

	private MutableMobs mutable = new MutableMobs();
	private RegionManager rm = new RegionManager();
	
	
	public void setMobSpawns(Player p, String[] args) {
		if (args.length > 1) {
			if (args[1].equalsIgnoreCase("passive")) {
				if (doesHaveNode(p, "regios.other.creature-spawns")) {
					if (args.length == 4) {
						setMobSpawn(rm.getRegion(args[2]), args[2], args[3], p);
					} else {
						p.sendMessage(ChatColor.RED + "[Regios] Invalid number of arguments specified.");
						p.sendMessage("Proper usage: /regios setmobspawns passive <region> <true/false>");
						return;
					}
				} else {
					sendInvalidPerms(p);
					return;
				}
			} else if (args[1].equalsIgnoreCase("hostile")) {
				if (doesHaveNode(p, "regios.other.monster-spawns")) {
					if (args.length == 4) {
						setMonsterSpawn(rm.getRegion(args[2]), args[2], args[3], p);
					} else {
						p.sendMessage(ChatColor.RED + "[Regios] Invalid number of arguments specified.");
						p.sendMessage("Proper usage: /regios setmobspawns hostile <region> <true/false>");
						return;
					}
				} else {
					sendInvalidPerms(p);
					return;
				}
			} else {
				p.sendMessage(ChatColor.RED + "[Regios] Invalid mob type specified.");
				p.sendMessage("Proper usage: /regios setmobspawns <passive/hostile> <region> <true/false>");
				return;
			}
		} else {
			p.sendMessage(ChatColor.RED + "[Regios] Invalid number of arguments specified.");
			p.sendMessage("Proper usage: /regios setmobspawns <passive/hostile> <region> <true/false>");
			return;
		}
	}
	
	private void setMobSpawn(Region r, String region, String input, Player p){
		boolean val;
		try{
			val = Boolean.parseBoolean(input);
		} catch (Exception bfe){
			p.sendMessage(ChatColor.RED + "[Regios] The value for the 2nd paramteter must be boolean!");
			return;
		}
		if(r == null){ p.sendMessage(ChatColor.RED + "[Regios] The region " + ChatColor.BLUE + region + ChatColor.RED + " doesn't exist!"); return; } else {
			if(!r.canModify(p)){
				p.sendMessage(ChatColor.RED + "[Regios] You are not permitted to modify this region!");
				return;
			}
			if(val){
				p.sendMessage(ChatColor.GREEN + "[Regios] Mob spawns enabled for region " + ChatColor.BLUE + region);
			} else {
				p.sendMessage(ChatColor.GREEN + "[Regios] Mob spawns disabled for region " + ChatColor.BLUE + region);
			}
		}
		mutable.editMobSpawn(r, val);
	}
	
	private void setMonsterSpawn(Region r, String region, String input, Player p){
		boolean val;
		try{
			val = Boolean.parseBoolean(input);
		} catch (Exception bfe){
			p.sendMessage(ChatColor.RED + "[Regios] The value for the 2nd paramteter must be boolean!");
			return;
		}
		if(r == null){ p.sendMessage(ChatColor.RED + "[Regios] The region " + ChatColor.BLUE + region + ChatColor.RED + " doesn't exist!"); return; } else {
			if(!r.canModify(p)){
				p.sendMessage(ChatColor.RED + "[Regios] You are not permitted to modify this region!");
				return;
			}
			if(val){
				p.sendMessage(ChatColor.GREEN + "[Regios] Monster spawns enabled for region " + ChatColor.BLUE + region);
			} else {
				p.sendMessage(ChatColor.GREEN + "[Regios] Monster spawns disabled for region " + ChatColor.BLUE + region);
			}
		}
		mutable.editMonsterSpawn(r, val);
	}
}
