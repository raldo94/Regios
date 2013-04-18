package net.jzx7.regios.Permissions;

import net.jzx7.regios.messages.Message;
import net.jzx7.regios.util.RegiosConversions;
import net.jzx7.regiosapi.entity.RegiosPlayer;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.Material;
import org.getspout.spoutapi.player.SpoutPlayer;

public class PermissionsCore {

	public static Permission permission = null;
	public static boolean hasPermissions = false;

	public static boolean doesHaveNode(RegiosPlayer p, String node) {
		if (p.isOp()) {
			return true;
		}

		if(hasPermissions) {
			return permission.has(RegiosConversions.getWorld(p.getRegiosWorld()), p.getName(), node);
		} else {
			return p.hasPermission(node);
		}
	}
	
	public static boolean isInGroup(RegiosPlayer p, String group) {
		if (hasPermissions) {
			return permission.playerInGroup(RegiosConversions.getWorld(p.getRegiosWorld()), p.getName(), group);
		} else {
			return false;
		}
	}

	public static void sendInvalidPerms(RegiosPlayer p) {
		p.sendMessage(Message.PERMISSIONDENIED.getMessage());
	}

	public static void sendInvalidPermsPopup(SpoutPlayer p) {
		p.sendNotification("Permissions", Message.PERMISSIONDENIED.getMessage(), Material.FIRE);
	}
	
	public static void addTempUserPermission(RegiosPlayer p, String node) {
		if (hasPermissions) {
			permission.playerAddTransient(p.getRegiosWorld().getName(), p.getName(), node);
		}
	}

	public static void removeTempUserPermission(RegiosPlayer p, String node) {
		if (hasPermissions) {
			permission.playerRemoveTransient(p.getRegiosWorld().getName(), p.getName(), node);
		}
	}

	public static void addUserPermission(RegiosPlayer p, String node) {
		if (hasPermissions) {
			permission.playerAdd(RegiosConversions.getWorld(p.getRegiosWorld()), p.getName(), node);
		}
	}

	public static void removeUserPermission(RegiosPlayer p, String node) {
		if (hasPermissions) {
			permission.playerRemove(RegiosConversions.getWorld(p.getRegiosWorld()), p.getName(), node);
		}
	}
	
	public static void removeGroup(RegiosPlayer p, String group) {
		if(hasPermissions) {
			permission.playerRemoveGroup(RegiosConversions.getWorld(p.getRegiosWorld()), p.getName(), group);
		}
	}
	
	public static void addGroup(RegiosPlayer p, String group) {
		if(hasPermissions) {
			permission.playerAddGroup(RegiosConversions.getWorld(p.getRegiosWorld()), p.getName(), group);
		}
	}

	public static void removeAllGroups(RegiosPlayer p) {
		if(hasPermissions) {
			for (String group : permission.getPlayerGroups(RegiosConversions.getWorld(p.getRegiosWorld()), p.getName())) {
				permission.playerRemoveGroup(RegiosConversions.getWorld(p.getRegiosWorld()), p.getName(), group);
			}
		}
	}
}
