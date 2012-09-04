package net.jzx7.regios.Commands;

import net.jzx7.regios.Economy.EconomyCore;
import net.jzx7.regios.Mutable.MutableEconomy;
import net.jzx7.regios.Permissions.PermissionsCore;
import net.jzx7.regios.Restrictions.RestrictionParameters;
import net.jzx7.regios.regions.RegionManager;
import net.jzx7.regiosapi.regions.Region;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;


public class EconomyCommands extends PermissionsCore {

	private static final MutableEconomy mutable = new MutableEconomy();
	private static final RegionManager rm = new RegionManager();

	public void listforsale(Player p, String[] args) {
		if (PermissionsCore.doesHaveNode(p, "regios.fun.listsell")) {
			if (args.length == 1) {
				listRegionsForSale(p);
			} else {
				p.sendMessage(ChatColor.RED + "[Regios] Invalid number of arguments specified.");
				p.sendMessage("Proper usage: /regios list-forsale");
				return;
			}
		} else {
			PermissionsCore.sendInvalidPerms(p);
		}
	}
	
	public void forsale(Player p, String[] args) {
		if (doesHaveNode(p, "regios.fun.sell")) {
			if (args.length == 3) {
				setForSale(rm.getRegion(args[1]), args[1], args[2], p);
			} else {
				p.sendMessage(ChatColor.RED + "[Regios] Invalid number of arguments specified.");
				p.sendMessage("Proper usage: /regios edit <region>");
				return;
			}
		} else {
			sendInvalidPerms(p);
		}
	}
	
	public void setprice(Player p, String[] args) {
		if (doesHaveNode(p, "regios.fun.sell")) {
			if (args.length == 3) {
				setSalePrice(rm.getRegion(args[1]), args[1], args[2], p);
			} else {
				p.sendMessage(ChatColor.RED + "[Regios] Invalid number of arguments specified.");
				p.sendMessage("Proper usage: /regios edit <region>");
				return;
			}
		} else {
			sendInvalidPerms(p);
		}
	}
	
	public void buy(Player p, String[] args) {
		if (doesHaveNode(p, "regios.fun.buy")) {
			if (args.length == 2) {
				buyRegion(rm.getRegion(args[1]), args[1], p);
			} else {
				p.sendMessage(ChatColor.RED + "[Regios] Invalid number of arguments specified.");
				p.sendMessage("Proper usage: /regios edit <region>");
				return;
			}
		} else {
			sendInvalidPerms(p);
		}
	}
	
	private void setForSale(Region r, String region, String input, Player p) {
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
			if (!r.canModify(p)) {
				p.sendMessage(ChatColor.RED + "[Regios] You are not permitted to sell this region!");
				return;
			}
			if (val) {
				p.sendMessage(ChatColor.GREEN + "[Regios] Region " + ChatColor.BLUE + region + ChatColor.GREEN + " is now for sale!");
			} else {
				p.sendMessage(ChatColor.GREEN + "[Regios] Region " + ChatColor.BLUE + region + ChatColor.GREEN + " is no longer for sale!");
			}
		}
		mutable.editForSale(r, val);
	}

	private void listRegionsForSale(Player p) {
		p.sendMessage(mutable.listRegionsForSale());
	}

	private void buyRegion(Region r, String region, Player p) {
		if (!EconomyCore.economySupport) {
			p.sendMessage(ChatColor.RED + "[Regios] Economy support is not enabled!");
			return;
		} else {
			if (r == null) {
				p.sendMessage(ChatColor.RED + "[Regios] The region " + ChatColor.BLUE + region + ChatColor.RED + " doesn't exist!");
				return;
			}
			if (!PermissionsCore.doesHaveNode(p, "regios.fun.buy")) {
				p.sendMessage(ChatColor.RED + "[Regios] You are not permitted to buy regions!");
				return;
			}
			if(!r.isForSale())
			{
				p.sendMessage(ChatColor.RED + "[Regios] This region is not for sale!");
				return;
			}
			RestrictionParameters params = RestrictionParameters.getRestrictions(p);
			if (rm.getOwnedRegions(p.getName()) + 1 > params.getRegionLimit())
			{
				p.sendMessage("[Regios] You are already at your ownership limit, you cannot own another region!");
				return;
			}
			int price = r.getSalePrice();
			if (!EconomyCore.canAffordRegion(p.getName(), price)) {
				p.sendMessage(ChatColor.RED + "[Regios] You cannot afford this region!");
				return;
			} else {
				EconomyCore.buyRegion(r, p.getName(), r.getOwner(), price);
				p.sendMessage(ChatColor.GREEN + "[Regios] Region " + ChatColor.BLUE + r.getName() + ChatColor.GREEN + " purchased for " + ChatColor.GOLD + price
						+ ChatColor.GREEN + "!");
				return;
			}
		}
	}

	private void setSalePrice(Region r, String region, String input, Player p) {
		int val;
		try {
			val = Integer.parseInt(input);
		} catch (Exception bfe) {
			p.sendMessage(ChatColor.RED + "[Regios] The value for the 2nd paramteter must be an integer!");
			return;
		}
		if (val < 0) {
			p.sendMessage(ChatColor.RED + "[Regios] The sale price must be a positive value!");
			return;
		}
		if (r == null) {
			p.sendMessage(ChatColor.RED + "[Regios] The region " + ChatColor.BLUE + region + ChatColor.RED + " doesn't exist!");
			return;
		} else {
			if (!r.canModify(p)) {
				p.sendMessage(ChatColor.RED + "[Regios] You are not permitted to modify this region!");
				return;
			}
			p.sendMessage(ChatColor.GREEN + "[Regios] Sale price for region " + ChatColor.BLUE + region + ChatColor.GREEN + " set to " + ChatColor.BLUE + val);
			mutable.editSalePrice(r, val);
		}
	}

}
