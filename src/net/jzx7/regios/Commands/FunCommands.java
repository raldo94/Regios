package net.jzx7.regios.Commands;

import net.jzx7.regios.Mutable.MutableFun;
import net.jzx7.regios.Permissions.PermissionsCore;
import net.jzx7.regios.regions.RegionManager;
import net.jzx7.regios.util.RegiosConversions;
import net.jzx7.regiosapi.block.RegiosBiome;
import net.jzx7.regiosapi.entity.RegiosPlayer;
import net.jzx7.regiosapi.regions.Region;

public class FunCommands extends PermissionsCore {

	MutableFun mutable = new MutableFun();
	RegionManager rm = new RegionManager();

	public void velocity(RegiosPlayer p, String[] args) {
		if (doesHaveNode(p, "regios.fun.vel-warp")) {
			if (args.length == 3) {
				setVelocityWarp(rm.getRegion(args[1]), args[1], args[2], p);
			} else {
				p.sendMessage("<RED>" + "[Regios] Invalid number of arguments specified.");
				p.sendMessage("Proper usage: /regios vel-warp <region> <rate>");
				return;
			}
		} else {
			sendInvalidPerms(p);
		}
	}

	public void health(RegiosPlayer p, String[] args) {
		if (doesHaveNode(p, "regios.fun.health")) {
			if (args.length == 3) {
				setHealthEnabled(rm.getRegion(args[1]), args[1], args[2], p);
			} else {
				p.sendMessage("<RED>" + "[Regios] Invalid number of arguments specified.");
				p.sendMessage("Proper usage: /regios health-enabled <region> <T/F>");
				return;
			}
		} else {
			sendInvalidPerms(p);
		}
	}
	
	public void healthregen(RegiosPlayer p, String[] args) {
		if (doesHaveNode(p, "regios.fun.health-regen")) {
			if (args.length == 3) {
				setHealthRegen(rm.getRegion(args[1]), args[1], args[2], p);
			} else {
				p.sendMessage("<RED>" + "[Regios] Invalid number of arguments specified.");
				p.sendMessage("Proper usage: /regios health-regen <region> <rate>");
				return;
			}
		} else {
			sendInvalidPerms(p);
		}
	}
	
	public void lsps(RegiosPlayer p, String[] args) {
		if (doesHaveNode(p, "regios.fun.lsps")) {
			if (args.length == 3) {
				setLSPS(rm.getRegion(args[1]), args[1], args[2], p);
			} else {
				p.sendMessage("<RED>" + "[Regios] Invalid number of arguments specified.");
				p.sendMessage("Proper usage: /regios lsps <region> <rate>");
				return;
			}
		} else {
			sendInvalidPerms(p);
		}
	}
	
	public void pvp(RegiosPlayer p, String[] args) {
		if (doesHaveNode(p, "regios.fun.pvp")) {
			if (args.length == 3) {
				setPvP(rm.getRegion(args[1]), args[1], args[2], p);
			} else {
				p.sendMessage("<RED>" + "[Regios] Invalid number of arguments specified.");
				p.sendMessage("Proper usage: /regios pvp <region> <T/F>");
				return;
			}
		} else {
			sendInvalidPerms(p);
		}
	}

	public void biome(RegiosPlayer p, String[] args) {
		if (doesHaveNode(p, "regios.fun.setbiome")) {
			if (args.length == 3) {
				setBiome(rm.getRegion(args[1]), args[1], RegiosConversions.getBiome(args[2]), p);
				return;
			} else {
				p.sendMessage("<RED>" + "[Regios] Invalid number of arguments specified.");
				p.sendMessage("Proper usage: /regios setbiome <region> <biome>");
				return;
			}
		} else {
			sendInvalidPerms(p);
			return;
		}
	}

	private void setLSPS(Region r, String region, String input, RegiosPlayer p){
		int val;
		try{
			val = Integer.parseInt(input);
		} catch (Exception bfe){
			p.sendMessage("<RED>" + "[Regios] The value for the 2nd paramteter must be an integer!");
			return;
		}
		if(r == null){ p.sendMessage("<RED>" + "[Regios] The region " + "<BLUE>" + region + "<RED>" + " doesn't exist!"); return; } else {
			if(!r.canModify(p)){
				p.sendMessage("<RED>" + "[Regios] You are not permitted to modify this region!");
				return;
			}
			p.sendMessage("<DGREEN>" + "[Regios] LSPS for region " + "<BLUE>" + region + "<DGREEN>" + " set to " + "<BLUE>" + val);
			mutable.editLSPS(r, val);
		}
	}

	private void setHealthRegen(Region r, String region, String input, RegiosPlayer p){
		int val;
		try{
			val = Integer.parseInt(input);
		} catch (Exception bfe){
			p.sendMessage("<RED>" + "[Regios] The value for the 2nd paramteter must be an integer!");
			return;
		}
		if(r == null){ p.sendMessage("<RED>" + "[Regios] The region " + "<BLUE>" + region + "<RED>" + " doesn't exist!"); return; } else {
			if(!r.canModify(p)){
				p.sendMessage("<RED>" + "[Regios] You are not permitted to modify this region!");
				return;
			}
			p.sendMessage("<DGREEN>" + "[Regios] Health Regen for region " + "<BLUE>" + region + "<DGREEN>" + " set to " + "<BLUE>" + val);
			mutable.editHealthRegen(r, val);
		}
	}

	private void setPvP(Region r, String region, String input, RegiosPlayer p){
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
				p.sendMessage("<DGREEN>" + "[Regios] PvP enabled for region " + "<BLUE>" + region);
			} else {
				p.sendMessage("<DGREEN>" + "[Regios] PvP spawns disabled for region " + "<BLUE>" + region);
			}
		}
		mutable.editPvPEnabled(r, val);
	}

	private void setHealthEnabled(Region r, String region, String input, RegiosPlayer p){
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
				p.sendMessage("<DGREEN>" + "[Regios] Health enabled for region " + "<BLUE>" + region);
			} else {
				p.sendMessage("<DGREEN>" + "[Regios] Health disabled for region " + "<BLUE>" + region);
			}
		}
		mutable.editHealthEnabled(r, val);
	}

	private void setVelocityWarp(Region r, String region, String input, RegiosPlayer p){
		int val;
		try{
			val = Integer.parseInt(input);
		} catch (Exception bfe){
			p.sendMessage("<RED>" + "[Regios] The value for the 2nd paramteter must be an integer!");
			return;
		}
		if(r == null){ p.sendMessage("<RED>" + "[Regios] The region " + "<BLUE>" + region + "<RED>" + " doesn't exist!"); return; } else {
			if(!r.canModify(p)){
				p.sendMessage("<RED>" + "[Regios] You are not permitted to modify this region!");
				return;
			}
			p.sendMessage("<DGREEN>" + "[Regios] Velocity Warp for region " + "<BLUE>" + region + "<DGREEN>" + " set to " + "<BLUE>" + val);
			mutable.editVelocityWarp(r, val);
		}
	}

	private void setBiome(Region r, String region, RegiosBiome biome, RegiosPlayer p) {
		if(r == null){ p.sendMessage("<RED>" + "[Regios] The region " + "<BLUE>" + region + "<RED>" + " doesn't exist!"); return; } else {
			if(!r.canModify(p)){
				p.sendMessage("<RED>" + "[Regios] You are not permitted to modify this region!");
				return;
			}
			if (biome == null) {
				p.sendMessage("<RED>" + "[Regios] Invalid biome entered!");
				p.sendMessage("<DGREEN>" + "[Regios] Valid biomes are: " + RegiosConversions.listBiomes());
				return;
			}
			r.setBiome(biome, p);
			p.sendMessage("<DGREEN>" + "[Regios] Biome for region " + "<BLUE>" + region + "<DGREEN>" + " set to " + "<BLUE>" + biome.toString() + "<DGREEN>" + ".");
		}
	}
}
