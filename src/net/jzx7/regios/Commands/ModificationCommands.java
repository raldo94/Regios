package net.jzx7.regios.Commands;

import net.jzx7.regios.Data.ConfigurationData;
import net.jzx7.regios.Permissions.PermissionsCore;
import net.jzx7.regios.regions.RegionManager;
import net.jzx7.regiosapi.entity.RegiosPlayer;
import net.jzx7.regiosapi.inventory.RegiosItemStack;
import net.jzx7.regiosapi.location.RegiosPoint;
import net.jzx7.regiosapi.regions.CuboidRegion;
import net.jzx7.regiosapi.regions.PolyRegion;
import net.jzx7.regiosapi.regions.Region;

public class ModificationCommands extends PermissionsCore {

	private static final RegionManager rm = new RegionManager();

	public void expand(RegiosPlayer p, String[] args) {
		if (PermissionsCore.doesHaveNode(p, "regios.modify.expand")) {
			if (args.length == 4) {
				setExpand(rm.getRegion(args[1]), args[1], args[3], args[2], p);
			} else if (args.length == 3) {
				setExpand(rm.getRegion(args[1]), args[1], Integer.toString(0), args[2], p);
			} else {
				p.sendMessage("<RED>" + "[Regios] Invalid number of arguments specified.");
				p.sendMessage("Proper usage: /regios expand north/south/east/west/up/down/max/out <region> [value]");
				return;
			}
		} else {
			PermissionsCore.sendInvalidPerms(p);
			return;
		}
	}

	public void shrink(RegiosPlayer p, String[] args) {
		if (doesHaveNode(p, "regios.modify.shrink")) {
			if (args.length == 4) {
				setShrink(rm.getRegion(args[1]), args[1], args[3], args[2], p);
			} else {
				p.sendMessage("<RED>" + "[Regios] Invalid number of arguments specified.");
				p.sendMessage("Proper usage: /regios shrink north/south/east/west/up/down/in <region> [value]");
				return;
			}
		} else {
			sendInvalidPerms(p);
			return;
		}
	}

	public void shift(RegiosPlayer p, String[] args) {
		if (doesHaveNode(p, "regios.modify.shift")) {
			if (args.length == 4) {
				setShift(rm.getRegion(args[1]), args[1], args [3], args[2], p);
			} else {
				p.sendMessage("<RED>" + "[Regios] Invalid number of arguments specified.");
				p.sendMessage("Proper usage: /regios shift north/south/east/west/up/down <region> [value]");
				return;
			}
		} else {
			sendInvalidPerms(p);
			return;
		}
	}

	public void delete(RegiosPlayer p, String[] args) {
		if (doesHaveNode(p, "regios.data.delete")) {
			if (args.length == 2) {
				setDelete(rm.getRegion(args[1]), args[1], args[1], p);
			} else {
				p.sendMessage("<RED>" + "[Regios] Invalid number of arguments specified.");
				p.sendMessage("Proper usage: /regios delete <region>");
			}
		} else {
			sendInvalidPerms(p);
		}
	}

	public void rename(RegiosPlayer p, String[] args) {
		if (doesHaveNode(p, "regios.data.rename")) {
			if (args.length == 3) {
				if (args[2].equalsIgnoreCase("all") || args[2].equalsIgnoreCase("placeholder") || args[2].equalsIgnoreCase("confirm")) {
					p.sendMessage("<RED>" + "[Regios] " + "<BLUE>" + args[2] + "<RED>" + " is a reserved word!");
					return;
				}
				setRename(rm.getRegion(args[1]), args[1], args[2], p);
			} else {
				p.sendMessage("<RED>" + "[Regios] Invalid number of arguments specified.");
				p.sendMessage("Proper usage: /regios rename <region> <newname>");
				return;
			}
		} else {
			sendInvalidPerms(p);
		}
	}

	public void modify(RegiosPlayer p, String[] args) {
		if (doesHaveNode(p, "regios.modify.modify")) {
			if (args.length == 2) {
				if (args[1].equalsIgnoreCase("confirm")) {
					setModifyPoints(CreationCommands.mod1.get(p), CreationCommands.mod2.get(p), p);
				} else {
					startModification(rm.getRegion(args[1]), args[1], p);
				}
			} else {
				p.sendMessage("<RED>" + "[Regios] Invalid number of arguments specified.");
				p.sendMessage("Proper usage: /regios modify <region>/confirm");
				return;
			}
		} else {
			sendInvalidPerms(p);
		}
	}

	private void setExpand(Region r, String region, String input, String direction, RegiosPlayer p) {
		int val;
		try {
			val = Integer.parseInt(input);
		} catch (Exception bfe) {
			p.sendMessage("<RED>" + "[Regios] The value for the 2nd parameter must be an integer!");
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

			if (rm.expandRegion(r, val, direction, p)) {
				p.sendMessage("<DGREEN>" + "[Regios] Region " + "<BLUE>" + region + "<DGREEN>" + " expanded " + direction + " by : " + "<BLUE>" + val);
			}
		}
	}

	private void setShrink(Region r, String region, String input, String direction, RegiosPlayer p) {
		int val;
		try {
			val = Integer.parseInt(input);
		} catch (Exception bfe) {
			p.sendMessage("<RED>" + "[Regios] The value for the 2nd parameter must be an integer!");
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
			if (rm.shrinkRegion(r, val, direction, p)) {
				p.sendMessage("<DGREEN>" + "[Regios] Region " + "<BLUE>" + region + "<DGREEN>" + " shrunk " + direction + " by : " + "<BLUE>" + val);
			}
		}
	}

	private void setShift(Region r, String region, String input, String direction, RegiosPlayer p) {
		int val;
		try {
			val = Integer.parseInt(input);
		} catch (Exception bfe) {
			p.sendMessage("<RED>" + "[Regios] The value for the 2nd parameter must be an integer!");
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
			if (rm.shiftRegion(r, val, direction, p)) {
				p.sendMessage("<DGREEN>" + "[Regios] Region " + "<BLUE>" + region + "<DGREEN>" + " shifted " + direction + " by : " + "<BLUE>" + val);
			}
		}
	}

	private void setDelete(Region r, String region, String input, RegiosPlayer p) {
		if (r == null) {
			p.sendMessage("<RED>" + "[Regios] The region " + "<BLUE>" + region + "<RED>" + " doesn't exist!");
			return;
		} else {
			if (!r.canModify(p)) {
				p.sendMessage("<RED>" + "[Regios] You are not permitted to modify this region!");
				return;
			}
			if (rm.deleteRegion(r, p)) {
				p.sendMessage("<DGREEN>" + "[Regios] Region " + "<BLUE>" + region + "<DGREEN>" + " deleted successfully.");
			}
		}

	}

	private void setRename(Region r, String region, String input, RegiosPlayer p) {
		if (rm.getRegion(input) != null) {
			p.sendMessage("<RED>" + "[Regios] A Region with name " + "<BLUE>" + input + "<RED>" + " already exists!");
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
		}
		if (rm.renameRegion(r, input, p)) {
			p.sendMessage("<DGREEN>" + "[Regios] Region " + "<BLUE>" + region + "<DGREEN>" + " renamed to : " + "<BLUE>" + input);
		}
	}

	private void setModifyPoints(RegiosPoint l1, RegiosPoint l2, RegiosPlayer p) {
		if (l1 == null || l2 == null) {
			p.sendMessage("<RED>" + "[Regios] You have not set 2 points!");
			return;
		}
		Region r = CreationCommands.modRegion.get(p);
		if (!r.canModify(p)) {
			p.sendMessage("<RED>" + "[Regios] You are not permitted to modify this region!");
			return;
		}
		if (rm.modifyRegionPoints(r, l1, l2, p)) {
			p.sendMessage("<DGREEN>" + "[Regios] Region " + "<BLUE>" + r.getName() + "<DGREEN>" + ", points modified successfully");
		}
	}

	private void startModification(Region r, String region, RegiosPlayer p) {
		if (!p.inventoryContains(new RegiosItemStack(ConfigurationData.defaultSelectionTool, 1))) {
			RegiosItemStack is = new RegiosItemStack(ConfigurationData.defaultSelectionTool, 1);

			p.addItem(is);

			if (p.getItemInHand() == new RegiosItemStack(0, 0)) {
				p.setItemInHand(is);
			}

		}
		if (r == null) {
			p.sendMessage("<RED>" + "[Regios] The region " + "<BLUE>" + region + "<RED>" + " doesn't exist!");
			return;
		} else {
			if (!r.canModify(p)) {
				p.sendMessage("<RED>" + "[Regios] You are not permitted to modify this region!");
				return;
			}
			p.sendMessage("<DGREEN>" + "[Regios] Modifying points for region " + "<BLUE>" + region);
		}
		CreationCommands.modRegion.put(p.getName(), r);
		if (r instanceof PolyRegion) {
			CreationCommands.modding.put(p.getName(), "polygon");
		} else if (r instanceof CuboidRegion) {
			CreationCommands.modding.put(p.getName(), "cuboid");
		}
		CreationCommands.setting.remove(p.getName());
	}

}
