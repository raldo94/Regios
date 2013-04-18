package net.jzx7.regios.Commands;

import net.jzx7.regios.Mutable.MutableMobs;
import net.jzx7.regios.Permissions.PermissionsCore;
import net.jzx7.regios.regions.RegionManager;
import net.jzx7.regiosapi.entity.RegiosPlayer;
import net.jzx7.regiosapi.regions.Region;

public class MobCommands extends PermissionsCore {

	private MutableMobs mutable = new MutableMobs();
	private RegionManager rm = new RegionManager();
	
	
	public void setMobSpawns(RegiosPlayer p, String[] args) {
		if (args.length > 1) {
			if (args[1].equalsIgnoreCase("passive")) {
				if (doesHaveNode(p, "regios.other.creature-spawns")) {
					if (args.length == 4) {
						setMobSpawn(rm.getRegion(args[2]), args[2], args[3], p);
					} else {
						p.sendMessage("<RED>" + "[Regios] Invalid number of arguments specified.");
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
						p.sendMessage("<RED>" + "[Regios] Invalid number of arguments specified.");
						p.sendMessage("Proper usage: /regios setmobspawns hostile <region> <true/false>");
						return;
					}
				} else {
					sendInvalidPerms(p);
					return;
				}
			} else {
				p.sendMessage("<RED>" + "[Regios] Invalid mob type specified.");
				p.sendMessage("Proper usage: /regios setmobspawns <passive/hostile> <region> <true/false>");
				return;
			}
		} else {
			p.sendMessage("<RED>" + "[Regios] Invalid number of arguments specified.");
			p.sendMessage("Proper usage: /regios setmobspawns <passive/hostile> <region> <true/false>");
			return;
		}
	}
	
	private void setMobSpawn(Region r, String region, String input, RegiosPlayer p){
		boolean val;
		try{
			val = Boolean.parseBoolean(input);
		} catch (Exception bfe){
			p.sendMessage("<RED>" + "[Regios] The value for the 2nd paramteter must be boolean!");
			return;
		}
		if(r == null){ p.sendMessage("<RED>" + "[Regios] The region " + "<BLUE>" + region + "<RED>" + " doesn't exist!"); return; } else {
			if(!r.canModify(p)){
				p.sendMessage("<RED>" + "[Regios] You are not permitted to modify this region!");
				return;
			}
			if(val){
				p.sendMessage("<DGREEN>" + "[Regios] Mob spawns enabled for region " + "<BLUE>" + region);
			} else {
				p.sendMessage("<DGREEN>" + "[Regios] Mob spawns disabled for region " + "<BLUE>" + region);
			}
		}
		mutable.editMobSpawn(r, val);
	}
	
	private void setMonsterSpawn(Region r, String region, String input, RegiosPlayer p){
		boolean val;
		try{
			val = Boolean.parseBoolean(input);
		} catch (Exception bfe){
			p.sendMessage("<RED>" + "[Regios] The value for the 2nd paramteter must be boolean!");
			return;
		}
		if(r == null){ p.sendMessage("<RED>" + "[Regios] The region " + "<BLUE>" + region + "<RED>" + " doesn't exist!"); return; } else {
			if(!r.canModify(p)){
				return;
			}
			if(val){
				p.sendMessage("<DGREEN>" + "[Regios] Monster spawns enabled for region " + "<BLUE>" + region);
			} else {
				p.sendMessage("<DGREEN>" + "[Regios] Monster spawns disabled for region " + "<BLUE>" + region);
			}
		}
		mutable.editMonsterSpawn(r, val);
	}
}
