package couk.Adamki11s.Regios.Permissions;

import net.milkbowl.vault.permission.Permission;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.getspout.spoutapi.player.SpoutPlayer;

import couk.Adamki11s.Regios.Regions.Region;

public class PermissionsCore {

	public static Permission permission = null;
	public static boolean hasPermissions = false;

	public static boolean doesHaveNode(Player p, String node) {
		if (p.isOp()) {
			return true;
		}

		if(hasPermissions) {
			return permission.has(p, node);
		} else {
			return p.hasPermission(node);
		}
	}

	public static void sendInvalidPerms(Player p) {
		p.sendMessage(ChatColor.RED + "[Regios] You do not have permissions to do this!");
	}

	public static void sendInvalidPermsPopup(SpoutPlayer p) {
		p.sendNotification("Permissions", ChatColor.RED + "You cannot do this!", Material.FIRE);
	}

	public static boolean canModify(Region r, Player p) {
		if (doesHaveNode(p, ("regios.override." + r.getName())) || doesHaveNode(p, "regios.override.all")) {
			return true;
		}
		if (r.getOwner().equalsIgnoreCase(p.getName())) {
			return true;
		}
		if (p.isOp()) {
			return true;
		}
		for (String s : r.getSubOwners()) {
			if (s.equalsIgnoreCase(p.getName())) {
				return true;
			}
		}
		return false;
	}
	
	public static void addTempUserPermission(Player p, String node) {
		if (hasPermissions) {
			permission.playerAddTransient(p.getWorld().getName(), p.getName(), node);
		}
	}

	public static void removeTempUserPermission(Player p, String node) {
		if (hasPermissions) {
			permission.playerRemoveTransient(p.getWorld().getName(), p.getName(), node);
		}
	}

	public static void addUserPermission(Player p, String node) {
		if (hasPermissions) {
			permission.playerAdd(p.getWorld().getName(), p.getName(), node);
		}
	}

	public static void removeUserPermission(Player p, String node) {
		if (hasPermissions) {
			permission.playerRemove(p.getWorld().getName(), p.getName(), node);
		}
	}

}
