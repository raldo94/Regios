package net.jzx7.regios.Commands;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import net.jzx7.regios.Data.ConfigurationData;
import net.jzx7.regios.Mutable.MutableEconomy;
import net.jzx7.regios.Mutable.MutableMisc;
import net.jzx7.regios.Mutable.MutableProtection;
import net.jzx7.regios.Permissions.PermissionsCore;
import net.jzx7.regios.regions.RegionManager;
import net.jzx7.regios.regions.SubRegionManager;
import net.jzx7.regiosapi.regions.CuboidRegion;
import net.jzx7.regiosapi.regions.PolyRegion;
import net.jzx7.regiosapi.regions.Region;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import couk.Adamki11s.Extras.Regions.ExtrasRegions;

public class MiscCommands extends PermissionsCore {

	private final static ExtrasRegions extReg = new ExtrasRegions();
	private final static SubRegionManager srm = new SubRegionManager();
	private final static MutableEconomy eco = new MutableEconomy();
	private final static MutableProtection prot = new MutableProtection();
	private final static MutableMisc mutable = new MutableMisc();
	private final static RegionManager rm = new RegionManager();

	public boolean check(Player p, String[] args) {
		if (doesHaveNode(p, "regios.other.check")) {
			if (args.length == 1) {
				checkRegion(p);
				return true;
			} else {
				p.sendMessage(ChatColor.RED + "[Regios] Invalid number of arguments specified.");
				p.sendMessage("Proper usage: /regios check <region>");
				return true;
			}
		} else {
			sendInvalidPerms(p);
			return true;
		}
	}

	public boolean cmdset(Player p, String[] args) {
		if (doesHaveNode(p, "regios.other.cmd-set")) {
			if (args.length > 1) {
				if (args[1].equalsIgnoreCase("add")){
					if (args.length >= 4) {
						addToCommandSet(rm.getRegion(args[2]), args[2], args, p);
						return true;
					} else {
						p.sendMessage(ChatColor.RED + "[Regios] Invalid number of arguments specified.");
						p.sendMessage("Proper usage: /regios cmdset add <region> <commands>");
						return true;
					}
				}else if (args[1].equalsIgnoreCase("remove") || args[1].equalsIgnoreCase("rem")){
					if (args.length >= 4) {
						removeFromCommandSet(rm.getRegion(args[2]), args[2], args, p);
						return true;
					} else {
						p.sendMessage(ChatColor.RED + "[Regios] Invalid number of arguments specified.");
						p.sendMessage("Proper usage: /regios cmdset remove <region> <commands>");
						return true;
					}
				}else if (args[1].equalsIgnoreCase("reset") || args[1].equalsIgnoreCase("res")){
					if (args.length == 3) {
						resetCommandSet(rm.getRegion(args[2]), args[2], p);
						return true;
					} else {
						p.sendMessage(ChatColor.RED + "[Regios] Invalid number of arguments specified.");
						p.sendMessage("Proper usage: /regios cmdset reset <region>");
						return true;
					}
				}else if (args[1].equalsIgnoreCase("list")){
					if (args.length == 3) {
						listCommandSet(rm.getRegion(args[2]), args[2], p);
						return true;
					} else {
						p.sendMessage(ChatColor.RED + "[Regios] Invalid number of arguments specified.");
						p.sendMessage("Proper usage: /regios cmdset list <region>");
						return true;
					}
				}else if (args[1].equalsIgnoreCase("enabled") || args[1].equalsIgnoreCase("use")){
					if (args.length == 4) {
						setForceCommand(rm.getRegion(args[2]), args[2], args[3], p);
						return true;
					} else {
						p.sendMessage(ChatColor.RED + "[Regios] Invalid number of arguments specified.");
						p.sendMessage("Proper usage: /regios cmdset enabled <region> <true/false>");
						return true;
					}
				} else {
					p.sendMessage(ChatColor.RED + "[Regios] Invalid number of arguments specified.");
					p.sendMessage("Proper usage: /regios cmdset add/remove/reset/list/enabled");
					return true;
				}
			} else {
				p.sendMessage(ChatColor.RED + "[Regios] Invalid number of arguments specified.");
				p.sendMessage("Proper usage: /regios cmdset add/remove/reset/list/enabled");
				return true;
			}
		} else {
			sendInvalidPerms(p);
			return true;
		}
	}

	public boolean gamemode(Player p, String[] args) {
		if (doesHaveNode(p, "regios.other.gamemode")) {
			if (args[1].equalsIgnoreCase("set")) {
				if (args.length == 4) {
					setGameModeType(rm.getRegion(args[2]), args[2], args[3], p);
					setGameModeChange(rm.getRegion(args[2]), args[2], "true", p);
					return true;
				} else {
					p.sendMessage(ChatColor.RED + "[Regios] Invalid number of arguments specified.");
					p.sendMessage("Proper usage: /regios gamemode set <region> <SURVIVAL/CREATIVE/ADVENTURE/0/1/2>");
					return true;
				}
			} else if (args[1].equalsIgnoreCase("change")) {
				if (args.length == 4) {
					setGameModeChange(rm.getRegion(args[2]), args[2], args[3], p);
					return true;
				} else {
					p.sendMessage(ChatColor.RED + "[Regios] Invalid number of arguments specified.");
					p.sendMessage("Proper usage: /regios gamemode change <region> <true/false>");
					return true;
				}
			} else {
				p.sendMessage(ChatColor.RED + "[Regios] Invalid number of arguments specified.");
				p.sendMessage("Proper usage: /regios gamemode set/change");
				return true;
			}
		} else {
			sendInvalidPerms(p);
			return true;
		}
	}

	public boolean plot(Player p, String[] args){
		if (doesHaveNode(p, "regios.data.plot")) {
			if (args.length == 2) {
				createAllotment(p, args[1], rm.getRegion(args[1]));
				return true;
			} else {
				p.sendMessage(ChatColor.RED + "[Regios] Invalid number of arguments specified.");
				p.sendMessage("Proper usage: /regios plot <region>");
				return true;
			}
		} else {
			sendInvalidPerms(p);
			return true;
		}
	}

	private void createAllotment(Player p, String region, Region r){
		if (r == null) {
			p.sendMessage(ChatColor.RED + "[Regios] The region " + ChatColor.BLUE + region + ChatColor.RED + " doesn't exist!");
			return;
		} else {
			if (!r.canModify(p)) {
				p.sendMessage(ChatColor.RED + "[Regios] You are not permitted to modify this region!");
				return;
			}
			rm.expandRegion(r, 0, "max", p);
			eco.editSalePrice(r, ConfigurationData.salePrice);
			eco.editForSale(r, true);
			prot.editProtect(r);
			prot.editProtectBB(r);
			prot.editProtectBP(r);
			p.sendMessage(ChatColor.GREEN + "[Regios] Region expanded to max, protected and for sale!");
		}
	}

	private Region returnClosestRegion(ArrayList<Region> regs, Player p){
		Location origin = p.getLocation();
		double distance = 999999999;
		Region binding = null;
		Vector rv1 = null, rv2 = null;
		for(Region r : regs){
			if (r instanceof PolyRegion) {
				Rectangle2D rect = ((PolyRegion) r).get2DPolygon().getBounds2D();
				rv1 = new Vector(rect.getMinX(), ((PolyRegion) r).getMinY(), rect.getMinY());
				rv2 = new Vector(rect.getMaxX(), ((PolyRegion) r).getMaxY(), rect.getMaxY());
			} else if (r instanceof CuboidRegion) {
				rv1 = ((CuboidRegion) r).getL1().toVector();
				rv2 = ((CuboidRegion) r).getL2().toVector();
			}
			double x_mid, z_mid;
			x_mid = (Math.max(rv1.getX(), rv2.getX()) - Math.min(rv1.getX(), rv2.getX()));
			z_mid = (Math.max(rv1.getZ(), rv2.getZ()) - Math.min(rv1.getZ(), rv2.getZ()));
			double hypot_x, hypot_z;
			hypot_x = origin.getX()- x_mid;
			hypot_z = origin.getZ() - z_mid;
			double direct_dist = Math.hypot(hypot_x, hypot_z);
			if(direct_dist < distance){
				binding = r;
				distance = direct_dist;
			}
		}
		return binding;
	}

	private void checkRegion(Player p){
		Location l = p.getLocation();
		Region r;

		ArrayList<Region> currentRegionSet = new ArrayList<Region>();

		for (Region reg : rm.getRegions()) {
			if (reg instanceof CuboidRegion) {
				CuboidRegion cr = (CuboidRegion)reg;
				if (extReg.isInsideCuboid(l, cr.getL1(), cr.getL2())) {
					currentRegionSet.add(reg);
				}
			} else if (reg instanceof PolyRegion) {
				PolyRegion pr = (PolyRegion) reg;
				if (extReg.isInsidePolygon(l, pr.get2DPolygon(), pr.getMinY(), pr.getMaxY())) {
					currentRegionSet.add(reg);
				}
			}
		}

		if (currentRegionSet.isEmpty()) { // If player is in chunk range but not
			// inside region then cancel the
			// check.
			
			Vector rv1 = null, rv2 = null;
			r = returnClosestRegion(rm.getRegions(), p);
			p.sendMessage(ChatColor.GREEN + "[Regios][Check] You are not in a region.");
			p.sendMessage(ChatColor.GREEN + "[Regios][Check] The closest region is : " + ChatColor.BLUE + r.getName());
			
			if (r instanceof CuboidRegion) {
				CuboidRegion cr = (CuboidRegion) r;
				rv1 = cr.getL1().toVector();
				rv2 = cr.getL2().toVector();
			} else if (r instanceof PolyRegion) {
				PolyRegion pr = (PolyRegion) r;
				Rectangle2D rect = pr.get2DPolygon().getBounds2D();
				rv1 = new Vector(rect.getMinX(), pr.getMinY(), rect.getMinY());
				rv2 = new Vector(rect.getMaxX(), pr.getMaxY(), rect.getMaxY());
			}
			
			p.sendMessage(ChatColor.GREEN + "[Regios][Check] " + ChatColor.LIGHT_PURPLE + "Coords[1] : X : " + rv1.getX() + ", Y : " + rv1.getX() + ", Z : " + rv1.getZ());
			p.sendMessage(ChatColor.GREEN + "[Regios][Check] " + ChatColor.LIGHT_PURPLE + "Coords[2] : X : " + rv2.getX() + ", Y : " + rv2.getX() + ", Z : " + rv2.getZ());
			return;
		}

		if (currentRegionSet.size() > 1) {
			r = srm.getCurrentRegion(currentRegionSet);
		} else {
			r = currentRegionSet.get(0);
		}

		p.sendMessage(ChatColor.GREEN + "[Regios][Check] You are in region " + ChatColor.BLUE + r.getName());
		return;
	}

	private void addToCommandSet(Region r, String region, String[] message, Player p) {
		String msg = "";
		if (r == null) {
			p.sendMessage(ChatColor.RED + "[Regios] The region " + ChatColor.BLUE + region + ChatColor.RED + " doesn't exist!");
			return;
		} else {
			if (!r.canModify(p)) {
				p.sendMessage(ChatColor.RED + "[Regios] You are not permitted to modify this region!");
				return;
			}
			String builder = "";
			for(int index = 3; index < message.length; index++){
				builder += message[index] + " ";
			}
			msg = builder;
			if(mutable.checkCommandSet(r, msg)){
				p.sendMessage(ChatColor.RED + "[Regios] The Command " + ChatColor.BLUE + msg + ChatColor.RED + " already exists!");
				return;
			}
			p.sendMessage(ChatColor.GREEN + "[Regios] Command " + ChatColor.BLUE + msg + ChatColor.GREEN + " added to region set " + ChatColor.BLUE + r.getName());
		}
		mutable.editAddToForceCommandSet(r, msg);
	}

	private void removeFromCommandSet(Region r, String region, String[] message, Player p) {
		String msg = "";
		if (r == null) {
			p.sendMessage(ChatColor.RED + "[Regios] The region " + ChatColor.BLUE + region + ChatColor.RED + " doesn't exist!");
			return;
		} else {
			if(!r.canModify(p)){
				p.sendMessage(ChatColor.RED + "[Regios] You are not permitted to modify this region!");
				return;
			}
			String builder = "";
			for(int index = 3; index < message.length; index++){
				builder += message[index] + " ";
			}
			msg = builder;
			if(!mutable.checkCommandSet(r, msg)){
				p.sendMessage(ChatColor.RED + "[Regios] The command " + ChatColor.BLUE + msg + ChatColor.RED + " did not match any in the set!");
				return;
			} else {
				p.sendMessage(ChatColor.GREEN + "[Regios] Command " + ChatColor.BLUE + msg + ChatColor.GREEN + " removed from region set " + ChatColor.BLUE + r.getName());
			}
		}
		mutable.editRemoveFromForceCommandSet(r, msg);
	}

	private void resetCommandSet(Region r, String region, Player p) {
		if (r == null) {
			p.sendMessage(ChatColor.RED + "[Regios] The region " + ChatColor.BLUE + region + ChatColor.RED + " doesn't exist!");
			return;
		} else {
			if(!r.canModify(p)){
				p.sendMessage(ChatColor.RED + "[Regios] You are not permitted to modify this region!");
				return;
			}
			p.sendMessage(ChatColor.GREEN + "[Regios] Command Set reset for region " + ChatColor.BLUE + r.getName());
		}
		mutable.editResetForceCommandSet(r);
	}

	private void listCommandSet(Region r, String region, Player p) {
		if (r == null) {
			p.sendMessage(ChatColor.RED + "[Regios] The region " + ChatColor.BLUE + region + ChatColor.RED + " doesn't exist!");
			return;
		} else {
			String regionSet = mutable.listCommandSet(r);
			p.sendMessage(ChatColor.GREEN + "Force Command Set List : " + ChatColor.BLUE + r.getName());
			p.sendMessage(regionSet);
		}
	}

	private void setForceCommand(Region r, String region, String input, Player p) {
		boolean val;
		try {
			val = Boolean.parseBoolean(input);
		} catch (Exception bfe) {
			p.sendMessage(ChatColor.RED + "[Regios] The value for the 2nd paramteter must be boolean!");
			return;
		}
		if (r == null) {
			p.sendMessage(ChatColor.RED + "[Regios] The region " + ChatColor.BLUE + region + ChatColor.RED + " doesn't exist!");
			return;
		} else {
			if(!r.canModify(p)){
				p.sendMessage(ChatColor.RED + "[Regios] You are not permitted to modify this region!");
				return;
			}
			if (val) {
				p.sendMessage(ChatColor.GREEN + "[Regios] Force Commands enabled for region " + ChatColor.BLUE + r.getName());
			} else {
				p.sendMessage(ChatColor.GREEN + "[Regios] Force Commands disabled for region " + ChatColor.BLUE + r.getName());
			}
		}
		mutable.editSetForceCommand(r, val);
	}

	private void setGameModeType(Region r, String region, String input, Player p) {
		GameMode gm = null;
		try {
			gm = GameMode.valueOf(input.toUpperCase());
		} catch (Exception ex) {
			try {
				gm = GameMode.getByValue(Integer.parseInt(input));
			} catch (Exception ex2) {
				p.sendMessage(ChatColor.RED + "[Regios] Invalid GameMode! Correct modes are SURVIVAL, CREATIVE, 0, 1.");
			}
		}
		if(r == null)
		{
			p.sendMessage(ChatColor.RED + "[Regios] The region " + ChatColor.BLUE + region + ChatColor.RED + " doesn't exist!");
			return;
		} else {
			if(!r.canModify(p)){
				p.sendMessage(ChatColor.RED + "[Regios] You are not permitted to modify this region!");
				return;
			}
			p.sendMessage(ChatColor.GREEN + "[Regios] GameMode set to " + ChatColor.BLUE + gm.toString() + ChatColor.GREEN + " for region " + ChatColor.BLUE + r.getName());
		}
		mutable.editGameModeType(r, gm);
	}

	private void setGameModeChange(Region r, String region, String input, Player p) {
		boolean val;
		try {
			val = Boolean.parseBoolean(input);
		} catch (Exception bfe) {
			p.sendMessage(ChatColor.RED + "[Regios] The value for the 2nd paramteter must be boolean!");
			return;
		}
		if(r == null)
		{
			p.sendMessage(ChatColor.RED + "[Regios] The region " + ChatColor.BLUE + region + ChatColor.RED + " doesn't exist!");
			return;
		} else {
			if(!r.canModify(p)){
				p.sendMessage(ChatColor.RED + "[Regios] You are not permitted to modify this region!");
				return;
			}
			if (val) {
				p.sendMessage(ChatColor.GREEN + "[Regios] GameMode change enabled for region " + ChatColor.BLUE + r.getName());
			} else {
				p.sendMessage(ChatColor.GREEN + "[Regios] GameMode change disabled for region " + ChatColor.BLUE + r.getName());
			}
		}
		mutable.editGameModeChange(r, val);
	}
}
