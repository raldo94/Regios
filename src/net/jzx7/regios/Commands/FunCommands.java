package net.jzx7.regios.Commands;

import net.jzx7.regios.Mutable.MutableFun;
import net.jzx7.regios.Permissions.PermissionsCore;
import net.jzx7.regios.regions.RegionManager;
import net.jzx7.regiosapi.regions.Region;
import org.bukkit.ChatColor;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;


public class FunCommands extends PermissionsCore {

	MutableFun mutable = new MutableFun();
	RegionManager rm = new RegionManager();

	public void velocity(Player p, String[] args) {
		if (doesHaveNode(p, "regios.fun.vel-warp")) {
			if (args.length == 3) {
				setVelocityWarp(rm.getRegion(args[1]), args[1], args[2], p);
			} else {
				p.sendMessage(ChatColor.RED + "[Regios] Invalid number of arguments specified.");
				p.sendMessage("Proper usage: /regios vel-warp <region> <rate>");
				return;
			}
		} else {
			sendInvalidPerms(p);
		}
	}

	public void health(Player p, String[] args) {
		if (doesHaveNode(p, "regios.fun.health")) {
			if (args.length == 3) {
				setHealthEnabled(rm.getRegion(args[1]), args[1], args[2], p);
			} else {
				p.sendMessage(ChatColor.RED + "[Regios] Invalid number of arguments specified.");
				p.sendMessage("Proper usage: /regios health-enabled <region> <T/F>");
				return;
			}
		} else {
			sendInvalidPerms(p);
		}
	}
	
	public void healthregen(Player p, String[] args) {
		if (doesHaveNode(p, "regios.fun.health-regen")) {
			if (args.length == 3) {
				setHealthRegen(rm.getRegion(args[1]), args[1], args[2], p);
			} else {
				p.sendMessage(ChatColor.RED + "[Regios] Invalid number of arguments specified.");
				p.sendMessage("Proper usage: /regios health-regen <region> <rate>");
				return;
			}
		} else {
			sendInvalidPerms(p);
		}
	}
	
	public void lsps(Player p, String[] args) {
		if (doesHaveNode(p, "regios.fun.lsps")) {
			if (args.length == 3) {
				setLSPS(rm.getRegion(args[1]), args[1], args[2], p);
			} else {
				p.sendMessage(ChatColor.RED + "[Regios] Invalid number of arguments specified.");
				p.sendMessage("Proper usage: /regios lsps <region> <rate>");
				return;
			}
		} else {
			sendInvalidPerms(p);
		}
	}
	
	public void pvp(Player p, String[] args) {
		if (doesHaveNode(p, "regios.fun.pvp")) {
			if (args.length == 3) {
				setPvP(rm.getRegion(args[1]), args[1], args[2], p);
			} else {
				p.sendMessage(ChatColor.RED + "[Regios] Invalid number of arguments specified.");
				p.sendMessage("Proper usage: /regios pvp <region> <T/F>");
				return;
			}
		} else {
			sendInvalidPerms(p);
		}
	}

	public void biome(Player p, String[] args) {
		if (doesHaveNode(p, "regios.fun.setbiome")) {
			if (args.length == 3) {
				setBiome(rm.getRegion(args[1]), args[1], getBiome(args[2]), p);
				return;
			} else {
				p.sendMessage(ChatColor.RED + "[Regios] Invalid number of arguments specified.");
				p.sendMessage("Proper usage: /regios setbiome <region> <biome>");
				return;
			}
		} else {
			sendInvalidPerms(p);
			return;
		}
	}

	private void setLSPS(Region r, String region, String input, Player p){
		int val;
		try{
			val = Integer.parseInt(input);
		} catch (Exception bfe){
			p.sendMessage(ChatColor.RED + "[Regios] The value for the 2nd paramteter must be an integer!");
			return;
		}
		if(r == null){ p.sendMessage(ChatColor.RED + "[Regios] The region " + ChatColor.BLUE + region + ChatColor.RED + " doesn't exist!"); return; } else {
			if(!r.canModify(p)){
				p.sendMessage(ChatColor.RED + "[Regios] You are not permitted to modify this region!");
				return;
			}
			p.sendMessage(ChatColor.GREEN + "[Regios] LSPS for region " + ChatColor.BLUE + region + ChatColor.GREEN + " set to " + ChatColor.BLUE + val);
			mutable.editLSPS(r, val);
		}
	}

	private void setHealthRegen(Region r, String region, String input, Player p){
		int val;
		try{
			val = Integer.parseInt(input);
		} catch (Exception bfe){
			p.sendMessage(ChatColor.RED + "[Regios] The value for the 2nd paramteter must be an integer!");
			return;
		}
		if(r == null){ p.sendMessage(ChatColor.RED + "[Regios] The region " + ChatColor.BLUE + region + ChatColor.RED + " doesn't exist!"); return; } else {
			if(!r.canModify(p)){
				p.sendMessage(ChatColor.RED + "[Regios] You are not permitted to modify this region!");
				return;
			}
			p.sendMessage(ChatColor.GREEN + "[Regios] Health Regen for region " + ChatColor.BLUE + region + ChatColor.GREEN + " set to " + ChatColor.BLUE + val);
			mutable.editHealthRegen(r, val);
		}
	}

	private void setPvP(Region r, String region, String input, Player p){
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
				p.sendMessage(ChatColor.GREEN + "[Regios] PvP enabled for region " + ChatColor.BLUE + region);
			} else {
				p.sendMessage(ChatColor.GREEN + "[Regios] PvP spawns disabled for region " + ChatColor.BLUE + region);
			}
		}
		mutable.editPvPEnabled(r, val);
	}

	private void setHealthEnabled(Region r, String region, String input, Player p){
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
				p.sendMessage(ChatColor.GREEN + "[Regios] Health enabled for region " + ChatColor.BLUE + region);
			} else {
				p.sendMessage(ChatColor.GREEN + "[Regios] Health disabled for region " + ChatColor.BLUE + region);
			}
		}
		mutable.editHealthEnabled(r, val);
	}

	private void setVelocityWarp(Region r, String region, String input, Player p){
		int val;
		try{
			val = Integer.parseInt(input);
		} catch (Exception bfe){
			p.sendMessage(ChatColor.RED + "[Regios] The value for the 2nd paramteter must be an integer!");
			return;
		}
		if(r == null){ p.sendMessage(ChatColor.RED + "[Regios] The region " + ChatColor.BLUE + region + ChatColor.RED + " doesn't exist!"); return; } else {
			if(!r.canModify(p)){
				p.sendMessage(ChatColor.RED + "[Regios] You are not permitted to modify this region!");
				return;
			}
			p.sendMessage(ChatColor.GREEN + "[Regios] Velocity Warp for region " + ChatColor.BLUE + region + ChatColor.GREEN + " set to " + ChatColor.BLUE + val);
			mutable.editVelocityWarp(r, val);
		}
	}

	private void setBiome(Region r, String region, Biome biome, Player p) {
		if(r == null){ p.sendMessage(ChatColor.RED + "[Regios] The region " + ChatColor.BLUE + region + ChatColor.RED + " doesn't exist!"); return; } else {
			if(!r.canModify(p)){
				p.sendMessage(ChatColor.RED + "[Regios] You are not permitted to modify this region!");
				return;
			}
			if (biome == null) {
				p.sendMessage(ChatColor.RED + "[Regios] Invalid biome entered!");
				p.sendMessage(ChatColor.GREEN + "[Regios] Valid biomes are: " + listBiomes());
				return;
			}
			r.setBiome(biome, p);
			p.sendMessage(ChatColor.GREEN + "[Regios] Biome for region " + ChatColor.BLUE + region + ChatColor.GREEN + " set to " + ChatColor.BLUE + biome.toString() + ChatColor.GREEN + ".");
		}
	}

	private Biome getBiome(String s)
	{
		for (Biome b : Biome.values()) {
			if (b.name().equalsIgnoreCase(s)) {
				return b;
			}

		}

		for (Biome b : Biome.values()) {
			if (b.name().toLowerCase().contains(s.toLowerCase())) {
				return b;
			}
		}

		return null;
	}

	private String listBiomes() {
		String biomes = "";

		for (Biome b : Biome.values()) {
			biomes += b.name() + ", ";
		}

		return biomes.substring(0, biomes.length() - 1);
	}
}
