package net.jzx7.regios.Commands;

import net.jzx7.regios.Data.ConfigurationData;
import net.jzx7.regios.Permissions.PermissionsCore;
import net.jzx7.regios.regions.RegionManager;
import net.jzx7.regiosapi.regions.CuboidRegion;
import net.jzx7.regiosapi.regions.PolyRegion;
import net.jzx7.regiosapi.regions.Region;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ModificationCommands extends PermissionsCore {

	private static final RegionManager rm = new RegionManager();

	public void expand(Player p, String[] args) {
		if (PermissionsCore.doesHaveNode(p, "regios.modify.expand")) {
			if (args.length == 4) {
				setExpand(rm.getRegion(args[1]), args[1], args[3], args[2], p);
			} else if (args.length == 3) {
				setExpand(rm.getRegion(args[1]), args[1], Integer.toString(0), args[2], p);
			} else {
				p.sendMessage(ChatColor.RED + "[Regios] Invalid number of arguments specified.");
				p.sendMessage("Proper usage: /regios expand north/south/east/west/up/down/max/out <region> [value]");
				return;
			}
		} else {
			PermissionsCore.sendInvalidPerms(p);
			return;
		}
	}

	public void shrink(Player p, String[] args) {
		if (doesHaveNode(p, "regios.modify.shrink")) {
			if (args.length == 4) {
				setShrink(rm.getRegion(args[1]), args[1], args[3], args[2], p);
			} else {
				p.sendMessage(ChatColor.RED + "[Regios] Invalid number of arguments specified.");
				p.sendMessage("Proper usage: /regios shrink north/south/east/west/up/down/in <region> [value]");
				return;
			}
		} else {
			sendInvalidPerms(p);
			return;
		}
	}

	public void delete(Player p, String[] args) {
		if (doesHaveNode(p, "regios.data.delete")) {
			if (args.length == 2) {
				setDelete(rm.getRegion(args[1]), args[1], args[1], p);
			} else {
				p.sendMessage(ChatColor.RED + "[Regios] Invalid number of arguments specified.");
				p.sendMessage("Proper usage: /regios delete <region>");
			}
		} else {
			sendInvalidPerms(p);
		}
	}

	public void rename(Player p, String[] args) {
		if (doesHaveNode(p, "regios.data.rename")) {
			if (args.length == 3) {
				if (args[2].equalsIgnoreCase("all") || args[2].equalsIgnoreCase("placeholder") || args[2].equalsIgnoreCase("confirm")) {
					p.sendMessage(ChatColor.RED + "[Regios] " + ChatColor.BLUE + args[2] + ChatColor.RED + " is a reserved word!");
					return;
				}
				setRename(rm.getRegion(args[1]), args[1], args[2], p);
			} else {
				p.sendMessage(ChatColor.RED + "[Regios] Invalid number of arguments specified.");
				p.sendMessage("Proper usage: /regios rename <region> <newname>");
				return;
			}
		} else {
			sendInvalidPerms(p);
		}
	}

	public void modify(Player p, String[] args) {
		if (doesHaveNode(p, "regios.modify.modify")) {
			if (args.length == 2) {
				if (args[1].equalsIgnoreCase("confirm")) {
					setModifyPoints(CreationCommands.mod1.get(p), CreationCommands.mod2.get(p), p);
				} else {
					startModification(rm.getRegion(args[1]), args[1], p);
				}
			} else {
				p.sendMessage(ChatColor.RED + "[Regios] Invalid number of arguments specified.");
				p.sendMessage("Proper usage: /regios modify <region>/confirm");
				return;
			}
		} else {
			sendInvalidPerms(p);
		}
	}

	private void setExpand(Region r, String region, String input, String direction, Player p) {
		int val;
		try {
			val = Integer.parseInt(input);
		} catch (Exception bfe) {
			p.sendMessage(ChatColor.RED + "[Regios] The value for the 2nd paramteter must be an integer!");
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

			if (rm.expandRegion(r, val, direction, p)) {
				p.sendMessage(ChatColor.GREEN + "[Regios] Region " + ChatColor.BLUE + region + ChatColor.GREEN + " expanded up by : " + ChatColor.BLUE + val);
			}
		}
	}

	private void setShrink(Region r, String region, String input, String direction, Player p) {
		int val;
		try {
			val = Integer.parseInt(input);
		} catch (Exception bfe) {
			p.sendMessage(ChatColor.RED + "[Regios] The value for the 2nd paramteter must be an integer!");
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
			if (rm.shrinkRegion(r, val, direction, p)) {
				p.sendMessage(ChatColor.GREEN + "[Regios] Region " + ChatColor.BLUE + region + ChatColor.GREEN + " shrunk up by : " + ChatColor.BLUE + val);
			}
		}
	}

	private void setDelete(Region r, String region, String input, Player p) {
		if (r == null) {
			p.sendMessage(ChatColor.RED + "[Regios] The region " + ChatColor.BLUE + region + ChatColor.RED + " doesn't exist!");
			return;
		} else {
			if (!r.canModify(p)) {
				p.sendMessage(ChatColor.RED + "[Regios] You are not permitted to modify this region!");
				return;
			}
			if (rm.deleteRegion(r, p)) {
				p.sendMessage(ChatColor.GREEN + "[Regios] Region " + ChatColor.BLUE + region + ChatColor.GREEN + " deleted successfully.");
			}
		}

	}

	private void setRename(Region r, String region, String input, Player p) {
		if (rm.getRegion(input) != null) {
			p.sendMessage(ChatColor.RED + "[Regios] A Region with name " + ChatColor.BLUE + input + ChatColor.RED + " already exists!");
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
		}
		if (rm.renameRegion(r, input, p)) {
			p.sendMessage(ChatColor.GREEN + "[Regios] Region " + ChatColor.BLUE + region + ChatColor.GREEN + " renamed to : " + ChatColor.BLUE + input);
		}
	}

	private void setModifyPoints(Location l1, Location l2, Player p) {
		if (l1 == null || l2 == null) {
			p.sendMessage(ChatColor.RED + "[Regios] You have not set 2 points!");
			return;
		}
		Region r = CreationCommands.modRegion.get(p);
		if (!r.canModify(p)) {
			p.sendMessage(ChatColor.RED + "[Regios] You are not permitted to modify this region!");
			return;
		}
		if (rm.modifyRegionPoints(r, l1, l2, p)) {
			p.sendMessage(ChatColor.GREEN + "[Regios] Region " + ChatColor.BLUE + r.getName() + ChatColor.GREEN + ", points modified successfully");
		}
	}

	private void startModification(Region r, String region, Player p) {
		if (!p.getInventory().contains(new ItemStack(ConfigurationData.defaultSelectionTool, 1))) {
			ItemStack is = new ItemStack(ConfigurationData.defaultSelectionTool, 1);

			p.getInventory().addItem(is);

			if (p.getItemInHand() == new ItemStack(Material.AIR, 0)) {
				p.setItemInHand(is);
			}

		}
		if (r == null) {
			p.sendMessage(ChatColor.RED + "[Regios] The region " + ChatColor.BLUE + region + ChatColor.RED + " doesn't exist!");
			return;
		} else {
			if (!r.canModify(p)) {
				p.sendMessage(ChatColor.RED + "[Regios] You are not permitted to modify this region!");
				return;
			}
			p.sendMessage(ChatColor.GREEN + "[Regios] Modifying points for region " + ChatColor.BLUE + region);
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
