package net.jzx7.regios.Permissions;

import java.util.ArrayList;
import java.util.HashMap;

import net.jzx7.regiosapi.entity.RegiosPlayer;
import net.jzx7.regiosapi.regions.Region;

public class PermissionsCacheManager {

	public static HashMap<String, ArrayList<String>> temporaryAddCache = new HashMap<String, ArrayList<String>>();
	public static HashMap<String, ArrayList<String>> temporaryRemCache = new HashMap<String, ArrayList<String>>();
	public static HashMap<String, ArrayList<String>> temporaryAddGroups = new HashMap<String, ArrayList<String>>();
	public static HashMap<String, ArrayList<String>> temporaryRemGroups = new HashMap<String, ArrayList<String>>();

	public static void cacheAddNodes(RegiosPlayer p, Region r) {
		ArrayList<String> nodeCache = new ArrayList<String>();
		for (String node : r.getTempNodesCacheAdd()) {
			nodeCache.add(node.trim());
			PermissionsCore.addTempUserPermission(p, node.trim());
		}
		temporaryAddCache.put(p.getName(), nodeCache);
	}

	public static void unCacheAddNodes(RegiosPlayer p, Region r) {
		if (temporaryAddCache.containsKey(p.getName())) {
			ArrayList<String> cache = temporaryAddCache.get(p.getName());
			if (!cache.isEmpty()) {
				for (String node : cache) {
					PermissionsCore.removeTempUserPermission(p, node.trim());
				}
			}
			temporaryAddCache.remove(p.getName());
		}
	}

	public static void cacheRemNodes(RegiosPlayer p, Region r) {
		ArrayList<String> nodeCache = new ArrayList<String>();
		for (String node : r.getTempNodesCacheRem()) {
			nodeCache.add(node.trim());
			PermissionsCore.removeUserPermission(p, node.trim());
		}
		temporaryRemCache.put(p.getName(), nodeCache);
	}

	public static void unCacheRemNodes(RegiosPlayer p, Region r) {
		if (temporaryRemCache.containsKey(p.getName())) {
			ArrayList<String> cache = temporaryRemCache.get(p.getName());
			if (!cache.isEmpty()) {
				for (String node : cache) {
					PermissionsCore.addUserPermission(p, node.trim());
				}
			}
			temporaryRemCache.remove(p.getName());
		}
	}

	public static void permAddNodes(RegiosPlayer p, Region r) {
		for (String node : r.getPermAddNodes()) {
			if(!PermissionsCore.doesHaveNode(p, node.trim())) {
				PermissionsCore.addUserPermission(p, node.trim());
			}
		}
	}

	public static void permRemoveNodes(RegiosPlayer p, Region r) {
		for (String node : r.getPermRemoveNodes()) {
			if (PermissionsCore.doesHaveNode(p, node.trim())) {
				PermissionsCore.removeUserPermission(p, node.trim());
			}
		}
	}
	
	public static void permAddGroups(RegiosPlayer p, Region r) {
		for (String group : r.getPermAddGroups()) {
			if(!PermissionsCore.isInGroup(p, group.trim())) {
				PermissionsCore.addGroup(p, group.trim());
			}
		}
	}

	public static void permRemoveGroups(RegiosPlayer p, Region r) {
		for (String group : r.getPermRemoveGroups()) {
			if (PermissionsCore.isInGroup(p, group.trim())) {
				PermissionsCore.removeGroup(p, group.trim());
			}
		}
	}
	
	public static void cacheAddGroups(RegiosPlayer p, Region r) {
		ArrayList<String> groupCache = new ArrayList<String>();
		for (String group : r.getTempAddGroups()) {
			groupCache.add(group.trim());
			PermissionsCore.addGroup(p, group.trim());
		}
		temporaryAddGroups.put(p.getName(), groupCache);
	}
	
	public static void cacheRemoveGroups(RegiosPlayer p, Region r) {
		ArrayList<String> groupCache = new ArrayList<String>();
		for (String group : r.getTempRemoveGroups()) {
			groupCache.add(group.trim());
			PermissionsCore.removeGroup(p, group.trim());
		}
		temporaryRemGroups.put(p.getName(), groupCache);
	}
	
	public static void unCacheAddGroups(RegiosPlayer p, Region r) {
		if (temporaryAddGroups.containsKey(p.getName())) {
			ArrayList<String> cache = temporaryAddGroups.get(p.getName());
			if (!cache.isEmpty()) {
				for (String group : cache) {
					PermissionsCore.removeGroup(p, group);
				}
			}
			temporaryAddGroups.remove(p.getName());
		}
	}
	
	public static void unCacheRemoveGroups(RegiosPlayer p, Region r) {
		if (temporaryRemGroups.containsKey(p.getName())) {
			ArrayList<String> cache = temporaryRemGroups.get(p.getName());
			if (!cache.isEmpty()) {
				for (String group : cache) {
					PermissionsCore.addGroup(p, group);
				}
			}
			temporaryRemGroups.remove(p.getName());
		}
	}
}
