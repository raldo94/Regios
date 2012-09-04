package net.jzx7.regios.Permissions;

import net.milkbowl.vault.permission.Permission;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.getspout.spoutapi.player.SpoutPlayer;

public class PermissionsCore {

	public static Permission permission = null;
	public static boolean hasPermissions = false;

	public static boolean doesHaveNode(Player p, String node) {
		if (p.isOp()) {
			return true;
		}

		if(hasPermissions) {
			return permission.has(p.getWorld(), p.getName(), node);
		} else {
			return p.hasPermission(node);
		}
	}
	
	public static boolean isInGroup(Player p, String group) {
		if (hasPermissions) {
			return permission.playerInGroup(p.getWorld(), p.getName(), group);
		} else {
			return false;
		}
	}

	public static void sendInvalidPerms(Player p) {
		p.sendMessage(ChatColor.RED + "[Regios] You do not have permissions to do this!");
	}

	public static void sendInvalidPermsPopup(SpoutPlayer p) {
		p.sendNotification("Permissions", ChatColor.RED + "You cannot do this!", Material.FIRE);
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
			permission.playerAdd(p.getWorld(), p.getName(), node);
		}
	}

	public static void removeUserPermission(Player p, String node) {
		if (hasPermissions) {
			permission.playerRemove(p.getWorld(), p.getName(), node);
		}
	}
	
	public static void removeGroup(Player p, String group) {
		if(hasPermissions) {
			permission.playerRemoveGroup(p.getWorld(), p.getName(), group);
		}
	}
	
	public static void addGroup(Player p, String group) {
		if(hasPermissions) {
			permission.playerAddGroup(p.getWorld(), p.getName(), group);
		}
	}

	public static void removeAllGroups(Player p) {
		if(hasPermissions) {
			for (String group : permission.getPlayerGroups(p.getWorld(), p.getName())) {
				permission.playerRemoveGroup(p.getWorld(), p.getName(), group);
			}
		}
	}
}
