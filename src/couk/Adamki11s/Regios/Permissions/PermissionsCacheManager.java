package couk.Adamki11s.Regios.Permissions;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.entity.Player;

import couk.Adamki11s.Regios.Regions.Region;

public class PermissionsCacheManager {

	public static HashMap<Player, ArrayList<String>> temporaryAddCache = new HashMap<Player, ArrayList<String>>();
	public static HashMap<Player, ArrayList<String>> temporaryRemCache = new HashMap<Player, ArrayList<String>>();

	public static void cacheAddNodes(Player p, Region r) {
		ArrayList<String> nodeCache = new ArrayList<String>();
		for (String node : r.getTempNodesCacheAdd()) {
			nodeCache.add(node.trim());
			PermissionsCore.addTempUserPermission(p, node.trim());
		}
		temporaryAddCache.put(p, nodeCache);
	}

	public static void unCacheAddNodes(Player p, Region r) {
		if (temporaryAddCache.containsKey(p)) {
			ArrayList<String> cache = temporaryAddCache.get(p);
			if (!cache.isEmpty()) {
				for (String node : cache) {
					PermissionsCore.removeTempUserPermission(p, node.trim());
				}
			}
			temporaryAddCache.remove(p);
		}
	}

	public static void cacheRemNodes(Player p, Region r) {
		ArrayList<String> nodeCache = new ArrayList<String>();
		for (String node : r.getTempNodesCacheRem()) {
			nodeCache.add(node.trim());
			PermissionsCore.removeUserPermission(p, node.trim());
		}
		temporaryRemCache.put(p, nodeCache);
	}

	public static void unCacheRemNodes(Player p, Region r) {
		if (temporaryRemCache.containsKey(p)) {
			ArrayList<String> cache = temporaryRemCache.get(p);
			if (!cache.isEmpty()) {
				for (String node : cache) {
					PermissionsCore.addUserPermission(p, node.trim());
				}
			}
			temporaryRemCache.remove(p);
		}
	}

	public static void permAddNodes(Player p, Region r) {
		for (String node : r.getPermAddNodes()) {
			if(!PermissionsCore.doesHaveNode(p, node.trim())) {
				PermissionsCore.addUserPermission(p, node.trim());
			}
		}
	}

	public static void permRemoveNodes(Player p, Region r) {
		for (String node : r.getPermRemoveNodes()) {
			if (PermissionsCore.doesHaveNode(p, node.trim())) {
				PermissionsCore.removeUserPermission(p, node.trim());
			}
		}
	}

}
