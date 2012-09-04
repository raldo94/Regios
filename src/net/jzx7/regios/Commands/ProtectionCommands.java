package net.jzx7.regios.Commands;

import net.jzx7.regios.Mutable.MutableProtection;
import net.jzx7.regios.Permissions.PermissionsCore;
import net.jzx7.regios.regions.RegionManager;
import net.jzx7.regiosapi.regions.Region;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;


public class ProtectionCommands extends PermissionsCore {

	MutableProtection mutable = new MutableProtection();
	private RegionManager rm = new RegionManager();

	public void protect(Player p, String[] args) {
		if (doesHaveNode(p, "regios.protection.protection")) {
			if (args.length == 2) {
				setProtected(rm.getRegion(args[1]), args[1], p);
			} else if (args.length == 3) {
				if (args[1].equalsIgnoreCase("all")) {
					setProtectedAll(rm.getRegion(args[2]), args[2], p);
				} else if (args[1].equalsIgnoreCase("bb") || args[1].equalsIgnoreCase("break")) {
					setProtectedBB(rm.getRegion(args[2]), args[2], p);
				} else if (args[1].equalsIgnoreCase("bp") || args[1].equalsIgnoreCase("place")) {
					setProtectedBP(rm.getRegion(args[2]), args[2], p);
				} else {
					p.sendMessage(ChatColor.RED + "[Regios] Invalid argument specified.");
					p.sendMessage("Proper usage: /regios protect all/break/protect <region>");
				}
			} else {
				p.sendMessage(ChatColor.RED + "[Regios] Invalid number of arguments specified.");
				p.sendMessage("Proper usage: /regios protect [all/break/protect] <region>");
			}

		} else {
			sendInvalidPerms(p);
		}
	}

	public void unprotect(Player p, String[] args) {
		if (doesHaveNode(p, "regios.protection.protection")) {
			if (args.length == 2) {
				setUnProtected(rm.getRegion(args[1]), args[1], p);
			} else if (args.length == 3) {
				if (args[1].equalsIgnoreCase("all")) {
					setUnProtectAll(rm.getRegion(args[2]), args[2], p);
				} else if (args[1].equalsIgnoreCase("bb") || args[1].equalsIgnoreCase("break")) {
					setUnProtectedBB(rm.getRegion(args[2]), args[2], p);
				} else if (args[1].equalsIgnoreCase("bp") || args[1].equalsIgnoreCase("place")) {
					setUnProtectedBP(rm.getRegion(args[2]), args[2], p);
				} else {
					p.sendMessage(ChatColor.RED + "[Regios] Invalid argument specified.");
					p.sendMessage("Proper usage: /regios unprotect all/break/protect <region>");
				}
			} else {
				p.sendMessage(ChatColor.RED + "[Regios] Invalid number of arguments specified.");
				p.sendMessage("Proper usage: /regios unprotect [all/break/protect] <region>");
			}

		} else {
			sendInvalidPerms(p);
		}
	}

	public void allowentry(Player p, String[] args) {
		if (doesHaveNode(p, "regios.protection.entry-control")) {
			if (args.length == 2) {
				setAllowEntry(rm.getRegion(args[1]), args[1], p);
			} else {
				p.sendMessage(ChatColor.RED + "[Regios] Invalid number of arguments specified.");
				p.sendMessage("Proper usage: /regios allow-entry <region>");
				return;
			}
		} else {
			sendInvalidPerms(p);
		}
	}

	public void allowexit(Player p, String[] args) {
		if (doesHaveNode(p, "regios.protection.exit-control")) {
			if (args.length == 2) {
				setAllowExit(rm.getRegion(args[1]), args[1], p);
			} else {
				p.sendMessage(ChatColor.RED + "[Regios] Invalid number of arguments specified.");
				p.sendMessage("Proper usage: /regios allow-exit <region>");
				return;
			}
		} else {
			PermissionsCore.sendInvalidPerms(p);
		}
	}

	public void prevententry(Player p, String[] args) {
		if (doesHaveNode(p, "regios.protection.entry-control")) {
			if (args.length == 2) {
				setPreventEntry(rm.getRegion(args[1]), args[1], p);
			} else {
				p.sendMessage(ChatColor.RED + "[Regios] Invalid number of arguments specified.");
				p.sendMessage("Proper usage: /regios prevent-entry <region>");
				return;
			}
		} else {
			sendInvalidPerms(p);
		}
	}

	public void preventexit(Player p, String[] args) {
		if (doesHaveNode(p, "regios.protection.exit-control")) {
			if (args.length == 2) {
				setPreventExit(rm.getRegion(args[1]), args[1], p);
			} else {
				p.sendMessage(ChatColor.RED + "[Regios] Invalid number of arguments specified.");
				p.sendMessage("Proper usage: /regios prevent-exit <region>");
				return;
			}
		} else {
			sendInvalidPerms(p);
		}
	}

	private void setProtected(Region r, String region, Player p){
		if(r == null){ p.sendMessage(ChatColor.RED + "[Regios] The region " + ChatColor.BLUE + region + ChatColor.RED + " doesn't exist!"); return; } else {
			if(!r.canModify(p)){
				p.sendMessage(ChatColor.RED + "[Regios] You are not permitted to modify this region!");
				return;
			}
			p.sendMessage(ChatColor.GREEN + "[Regios] General Protection enabled for region " + ChatColor.BLUE + region);
		}
		mutable.editProtect(r);
	}

	private void setProtectedAll(Region r, String region, Player p){
		if(r == null){ p.sendMessage(ChatColor.RED + "[Regios] The region " + ChatColor.BLUE + region + ChatColor.RED + " doesn't exist!"); return; } else {
			if(!r.canModify(p)){
				p.sendMessage(ChatColor.RED + "[Regios] You are not permitted to modify this region!");
				return;
			}
			p.sendMessage(ChatColor.GREEN + "[Regios] All Protection enabled for region " + ChatColor.BLUE + region);
		}
		mutable.editProtect(r);
		mutable.editProtectBB(r);
		mutable.editProtectBP(r);
	}

	private void setProtectedBB(Region r, String region, Player p){
		if(r == null){ p.sendMessage(ChatColor.RED + "[Regios] The region " + ChatColor.BLUE + region + ChatColor.RED + " doesn't exist!"); return; } else {
			if(!r.canModify(p)){
				p.sendMessage(ChatColor.RED + "[Regios] You are not permitted to modify this region!");
				return;
			}
			p.sendMessage(ChatColor.GREEN + "[Regios] Block Break Protection enabled for region " + ChatColor.BLUE + region);
		}
		mutable.editProtectBB(r);
	}

	private void setProtectedBP(Region r, String region, Player p){
		if(r == null){ p.sendMessage(ChatColor.RED + "[Regios] The region " + ChatColor.BLUE + region + ChatColor.RED + " doesn't exist!"); return; } else {
			if(!r.canModify(p)){
				p.sendMessage(ChatColor.RED + "[Regios] You are not permitted to modify this region!");
				return;
			}
			p.sendMessage(ChatColor.GREEN + "[Regios] Block Place Protection enabled for region " + ChatColor.BLUE + region);
		}
		mutable.editProtectBP(r);
	}

	private void setUnProtectedBP(Region r, String region, Player p){
		if(r == null){ p.sendMessage(ChatColor.RED + "[Regios] The region " + ChatColor.BLUE + region + ChatColor.RED + " doesn't exist!"); return; } else {
			if(!r.canModify(p)){
				p.sendMessage(ChatColor.RED + "[Regios] You are not permitted to modify this region!");
				return;
			}
			p.sendMessage(ChatColor.GREEN + "[Regios] Block Place Protection disabled for region " + ChatColor.BLUE + region);
		}
		mutable.editUnProtectBP(r);
	}

	private void setUnProtectedBB(Region r, String region, Player p){
		if(r == null){ p.sendMessage(ChatColor.RED + "[Regios] The region " + ChatColor.BLUE + region + ChatColor.RED + " doesn't exist!"); return; } else {
			if(!r.canModify(p)){
				p.sendMessage(ChatColor.RED + "[Regios] You are not permitted to modify this region!");
				return;
			}
			p.sendMessage(ChatColor.GREEN + "[Regios] Block Break Protection disabled for region " + ChatColor.BLUE + region);
		}
		mutable.editUnProtectBB(r);
	}

	private void setUnProtected(Region r, String region, Player p){
		if(r == null){ p.sendMessage(ChatColor.RED + "[Regios] The region " + ChatColor.BLUE + region + ChatColor.RED + " doesn't exist!"); return; } else {
			if(!r.canModify(p)){
				p.sendMessage(ChatColor.RED + "[Regios] You are not permitted to modify this region!");
				return;
			}
			p.sendMessage(ChatColor.GREEN + "[Regios] General Protection disabled for region " + ChatColor.BLUE + region);
		}
		mutable.editUnprotect(r);
	}

	private void setUnProtectAll(Region r, String region, Player p){
		if(r == null){ p.sendMessage(ChatColor.RED + "[Regios] The region " + ChatColor.BLUE + region + ChatColor.RED + " doesn't exist!"); return; } else {
			if(!r.canModify(p)){
				p.sendMessage(ChatColor.RED + "[Regios] You are not permitted to modify this region!");
				return;
			}
			p.sendMessage(ChatColor.GREEN + "[Regios] All Protection disabled for region " + ChatColor.BLUE + region);
		}
		mutable.editUnprotect(r);
		mutable.editUnProtectBB(r);
		mutable.editUnProtectBP(r);
	}

	private void setPreventEntry(Region r, String region, Player p){
		if(r == null){ p.sendMessage(ChatColor.RED + "[Regios] The region " + ChatColor.BLUE + region + ChatColor.RED + " doesn't exist!"); return; } else {
			if(!r.canModify(p)){
				p.sendMessage(ChatColor.RED + "[Regios] You are not permitted to modify this region!");
				return;
			}
			p.sendMessage(ChatColor.GREEN + "[Regios] Prevent entry enabled for region " + ChatColor.BLUE + region);
		}
		mutable.editPreventEntry(r);
	}

	private void setAllowEntry(Region r, String region, Player p){
		if(r == null){ p.sendMessage(ChatColor.RED + "[Regios] The region " + ChatColor.BLUE + region + ChatColor.RED + " doesn't exist!"); return; } else {
			if(!r.canModify(p)){
				p.sendMessage(ChatColor.RED + "[Regios] You are not permitted to modify this region!");
				return;
			}
			p.sendMessage(ChatColor.GREEN + "[Regios] Prevent entry disabled for region " + ChatColor.BLUE + region);
		}
		mutable.editAllowEntry(r);
	}

	private void setPreventExit(Region r, String region, Player p){
		if(r == null){ p.sendMessage(ChatColor.RED + "[Regios] The region " + ChatColor.BLUE + region + ChatColor.RED + " doesn't exist!"); return; } else {
			if(!r.canModify(p)){
				p.sendMessage(ChatColor.RED + "[Regios] You are not permitted to modify this region!");
				return;
			}
			p.sendMessage(ChatColor.GREEN + "[Regios] Prevent exit enabled for region " + ChatColor.BLUE + region);
		}
		mutable.editPreventExit(r);
	}

	private void setAllowExit(Region r, String region, Player p){
		if(r == null){ p.sendMessage(ChatColor.RED + "[Regios] The region " + ChatColor.BLUE + region + ChatColor.RED + " doesn't exist!"); return; } else {
			if(!r.canModify(p)){
				p.sendMessage(ChatColor.RED + "[Regios] You are not permitted to modify this region!");
				return;
			}
			p.sendMessage(ChatColor.GREEN + "[Regios] Prevent exit disabled for region " + ChatColor.BLUE + region);
		}
		mutable.editAllowExit(r);
	}

}
