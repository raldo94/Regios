package net.jzx7.regios.Commands;

import net.jzx7.regios.Economy.EconomyCore;
import net.jzx7.regios.Mutable.MutableEconomy;
import net.jzx7.regios.Permissions.PermissionsCore;
import net.jzx7.regios.Restrictions.RestrictionParameters;
import net.jzx7.regios.regions.RegionManager;
import net.jzx7.regiosapi.entity.RegiosPlayer;
import net.jzx7.regiosapi.regions.Region;

public class EconomyCommands extends PermissionsCore {

	private static final MutableEconomy mutable = new MutableEconomy();
	private static final RegionManager rm = new RegionManager();

	public void listforsale(RegiosPlayer p, String[] args) {
		if (PermissionsCore.doesHaveNode(p, "regios.fun.listsale")) {
			if (args.length == 1) {
				listRegionsForSale(p);
			} else {
				p.sendMessage("<RED>" + "[Regios] Invalid number of arguments specified.");
				p.sendMessage("Proper usage: /regios list-forsale");
				return;
			}
		} else {
			PermissionsCore.sendInvalidPerms(p);
		}
	}
	
	public void forsale(RegiosPlayer p, String[] args) {
		if (doesHaveNode(p, "regios.fun.sell")) {
			if (args.length == 3) {
				setForSale(rm.getRegion(args[1]), args[1], args[2], p);
			} else {
				p.sendMessage("<RED>" + "[Regios] Invalid number of arguments specified.");
				p.sendMessage("Proper usage: /regios edit <region>");
				return;
			}
		} else {
			sendInvalidPerms(p);
		}
	}
	
	public void setprice(RegiosPlayer p, String[] args) {
		if (doesHaveNode(p, "regios.fun.sell")) {
			if (args.length == 3) {
				setSalePrice(rm.getRegion(args[1]), args[1], args[2], p);
			} else {
				p.sendMessage("<RED>" + "[Regios] Invalid number of arguments specified.");
				p.sendMessage("Proper usage: /regios edit <region>");
				return;
			}
		} else {
			sendInvalidPerms(p);
		}
	}
	
	public void buy(RegiosPlayer p, String[] args) {
		if (doesHaveNode(p, "regios.fun.buy")) {
			if (args.length == 2) {
				buyRegion(rm.getRegion(args[1]), args[1], p);
			} else {
				p.sendMessage("<RED>" + "[Regios] Invalid number of arguments specified.");
				p.sendMessage("Proper usage: /regios edit <region>");
				return;
			}
		} else {
			sendInvalidPerms(p);
		}
	}
	
	private void setForSale(Region r, String region, String input, RegiosPlayer p) {
		boolean val;
		try {
			val = Boolean.parseBoolean(input);
		} catch (Exception bfe) {
			p.sendMessage("<RED>" + "[Regios] The value for the 2nd paramteter must be boolean!");
			return;
		}
		if (r == null) {
			p.sendMessage("<RED>" + "[Regios] The region " + "<BLUE>" + region + "<RED>" + " doesn't exist!");
			return;
		} else {
			if (!r.canModify(p)) {
				p.sendMessage("<RED>" + "[Regios] You are not permitted to sell this region!");
				return;
			}
			if (val) {
				p.sendMessage("<DGREEN>" + "[Regios] Region " + "<BLUE>" + region + "<DGREEN>" + " is now for sale!");
			} else {
				p.sendMessage("<DGREEN>" + "[Regios] Region " + "<BLUE>" + region + "<DGREEN>" + " is no longer for sale!");
			}
		}
		mutable.editForSale(r, val);
	}

	private void listRegionsForSale(RegiosPlayer p) {
		p.sendMessage(mutable.listRegionsForSale());
	}

	private void buyRegion(Region r, String region, RegiosPlayer p) {
		if (!EconomyCore.economySupport) {
			p.sendMessage("<RED>" + "[Regios] Economy support is not enabled!");
			return;
		} else {
			if (r == null) {
				p.sendMessage("<RED>" + "[Regios] The region " + "<BLUE>" + region + "<RED>" + " doesn't exist!");
				return;
			}
			if (!PermissionsCore.doesHaveNode(p, "regios.fun.buy")) {
				p.sendMessage("<RED>" + "[Regios] You are not permitted to buy regions!");
				return;
			}
			if(!r.isForSale())
			{
				p.sendMessage("<RED>" + "[Regios] This region is not for sale!");
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
				p.sendMessage("<RED>" + "[Regios] You cannot afford this region!");
				return;
			} else {
				EconomyCore.buyRegion(r, p.getName(), r.getOwner(), price);
				p.sendMessage("<DGREEN>" + "[Regios] Region " + "<BLUE>" + r.getName() + "<DGREEN>" + " purchased for " + "<GOLD>" + price
						+ "<DGREEN>" + "!");
				return;
			}
		}
	}

	private void setSalePrice(Region r, String region, String input, RegiosPlayer p) {
		int val;
		try {
			val = Integer.parseInt(input);
		} catch (Exception bfe) {
			p.sendMessage("<RED>" + "[Regios] The value for the 2nd paramteter must be an integer!");
			return;
		}
		if (val < 0) {
			p.sendMessage("<RED>" + "[Regios] The sale price must be a positive value!");
			return;
		}
		if (r == null) {
			p.sendMessage("<RED>" + "[Regios] The region " + "<BLUE>" + region + "<RED>" + " doesn't exist!");
			return;
		} else {
			if (!r.canModify(p)) {
				p.sendMessage("<RED>" + "[Regios] You are not permitted to modify this region!");
				return;
			}
			p.sendMessage("<DGREEN>" + "[Regios] Sale price for region " + "<BLUE>" + region + "<DGREEN>" + " set to " + "<BLUE>" + val);
			mutable.editSalePrice(r, val);
		}
	}

}
