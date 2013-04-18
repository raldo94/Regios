package net.jzx7.regios.Mutable;

import java.io.IOException;

import net.jzx7.regios.regions.RegionManager;
import net.jzx7.regiosapi.regions.Region;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class MutablePermissions {

	private final static RegionManager rm = new RegionManager();
	
	public void editAddToTempAddCache(Region r, String message) {
		FileConfiguration c = YamlConfiguration.loadConfiguration(rm.getConfigFile(r));
		String current = (String) c.get("Region.Permissions.TemporaryCache.AddNodes", "");
		c.set("Region.Permissions.TemporaryCache.AddNodes", current.trim() + message.trim() + ",");
		r.setTempNodesCacheAdd((current.trim() + "," + message.trim()).split(","));
		try {
			c.save(rm.getConfigFile(r));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void editRemoveFromTempAddCache(Region r, String message) {
		FileConfiguration c = YamlConfiguration.loadConfiguration(rm.getConfigFile(r));
		String current = (String) c.get("Region.Permissions.TemporaryCache.AddNodes", "");
		current = current.replaceAll(" ", "");
		current = current.replaceAll(message + ",", "");
		current = current.replaceAll(",,", ",");
		c.set("Region.Permissions.TemporaryCache.AddNodes", current.trim());
		r.setTempNodesCacheAdd((current.trim()).split(","));
		try {
			c.save(rm.getConfigFile(r));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void editResetTempAddCache(Region r) {
		FileConfiguration c = YamlConfiguration.loadConfiguration(rm.getConfigFile(r));
		c.set("Region.Permissions.TemporaryCache.AddNodes", "");
		r.setTempNodesCacheAdd(("").split(","));
		try {
			c.save(rm.getConfigFile(r));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean checkTempAddCache(Region r, String match) {
		for (String s : r.getTempNodesCacheAdd()) {
			if (s.trim().equalsIgnoreCase(match.trim())) {
				return true;
			}
		}
		return false;
	}

	public void editAddToTempRemCache(Region r, String message) {
		FileConfiguration c = YamlConfiguration.loadConfiguration(rm.getConfigFile(r));
		String current = (String) c.get("Region.Permissions.TemporaryCache.RemoveNodes", "");
		c.set("Region.Permissions.TemporaryCache.RemoveNodes", current.trim() + message.trim() + ",");
		r.setTempNodesCacheRem((current.trim() + "," + message.trim()).split(","));
		try {
			c.save(rm.getConfigFile(r));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void editRemoveFromTempRemCache(Region r, String message) {
		FileConfiguration c = YamlConfiguration.loadConfiguration(rm.getConfigFile(r));
		String current = (String) c.get("Region.Permissions.TemporaryCache.RemoveNodes", "");
		current = current.replaceAll(" ", "");
		current = current.replaceAll(message + ",", "");
		current = current.replaceAll(",,", ",");
		c.set("Region.Permissions.TemporaryCache.RemoveNodes", current.trim());
		r.setTempNodesCacheRem((current.trim()).split(","));
		try {
			c.save(rm.getConfigFile(r));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void editResetTempRemCache(Region r) {
		FileConfiguration c = YamlConfiguration.loadConfiguration(rm.getConfigFile(r));
		c.set("Region.Permissions.TemporaryCache.RemoveNodes", "");
		r.setTempNodesCacheRem(("").split(","));
		try {
			c.save(rm.getConfigFile(r));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean checkTempRemCache(Region r, String match) {
		for (String s : r.getTempNodesCacheRem()) {
			if (s.trim().equalsIgnoreCase(match.trim())) {
				return true;
			}
		}
		return false;
	}

	public boolean checkPermAdd(Region r, String match) {
		for (String s : r.getPermAddNodes()) {
			if (s.trim().equalsIgnoreCase(match.trim())) {
				return true;
			}
		}
		return false;
	}

	public boolean checkPermRemove(Region r, String match) {
		for (String s : r.getPermRemoveNodes()) {
			if (s.trim().equalsIgnoreCase(match.trim())) {
				return true;
			}
		}
		return false;
	}


	public void editAddToPermAddCache(Region r, String message) {
		FileConfiguration c = YamlConfiguration.loadConfiguration(rm.getConfigFile(r));
		String current = (String) c.get("Region.Permissions.PermanentCache.AddNodes", "");
		c.set("Region.Permissions.PermanentCache.AddNodes", current.trim() + message.trim() + ",");
		r.setPermanentNodesCacheAdd((current.trim() + "," + message.trim()).split(","));
		try {
			c.save(rm.getConfigFile(r));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void editRemoveFromPermAddCache(Region r, String message) {
		FileConfiguration c = YamlConfiguration.loadConfiguration(rm.getConfigFile(r));
		String current = (String) c.get("Region.Permissions.PermanentCache.AddNodes", "");
		current = current.replaceAll(" ", "");
		current = current.replaceAll(message + ",", "");
		current = current.replaceAll(",,", ",");
		c.set("Region.Permissions.PermanentCache.AddNodes", current.trim());
		r.setPermanentNodesCacheAdd((current.trim()).split(","));
		try {
			c.save(rm.getConfigFile(r));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void editResetPermAddCache(Region r) {
		FileConfiguration c = YamlConfiguration.loadConfiguration(rm.getConfigFile(r));
		c.set("Region.Permissions.PermanentCache.AddNodes", "");
		r.setPermanentNodesCacheAdd(("").split(","));
		try {
			c.save(rm.getConfigFile(r));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void editAddToPermRemoveCache(Region r, String message) {
		FileConfiguration c = YamlConfiguration.loadConfiguration(rm.getConfigFile(r));
		String current = (String) c.get("Region.Permissions.PermanentCache.RemoveNodes", "");
		c.set("Region.Permissions.PermanentCache.RemoveNodes", current.trim() + message.trim() + ",");
		r.setPermanentNodesCacheRemove((current.trim() + "," + message.trim()).split(","));
		try {
			c.save(rm.getConfigFile(r));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void editRemoveFromPermRemoveCache(Region r, String message) {
		FileConfiguration c = YamlConfiguration.loadConfiguration(rm.getConfigFile(r));
		String current = (String) c.get("Region.Permissions.PermanentCache.RemoveNodes", "");
		current = current.replaceAll(" ", "");
		current = current.replaceAll(message + ",", "");
		current = current.replaceAll(",,", ",");
		c.set("Region.Permissions.PermanentCache.RemoveNodes", current.trim());
		r.setPermanentNodesCacheRemove((current.trim()).split(","));
	}

	public void editResetPermRemoveCache(Region r) {
		FileConfiguration c = YamlConfiguration.loadConfiguration(rm.getConfigFile(r));
		c.set("Region.Permissions.PermanentCache.RemoveNodes", "");
		r.setPermanentNodesCacheRemove(("").split(","));
		try {
			c.save(rm.getConfigFile(r));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String listTempAddCache(Region r) {
		FileConfiguration c = YamlConfiguration.loadConfiguration(rm.getConfigFile(r));
		String s = c.getString("Region.Permissions.TemporaryCache.AddNodes", "");
		return s;
	}

	public String listTempRemCache(Region r) {
		FileConfiguration c = YamlConfiguration.loadConfiguration(rm.getConfigFile(r));
		String s = c.getString("Region.Permissions.TemporaryCache.RemoveNodes", "");
		return s;
	}

	public String listPermAddCache(Region r) {
		FileConfiguration c = YamlConfiguration.loadConfiguration(rm.getConfigFile(r));
		String s = c.getString("Region.Permissions.PermanentCache.AddNodes", "");
		return s;
	}

	public String listPermRemCache(Region r) {
		FileConfiguration c = YamlConfiguration.loadConfiguration(rm.getConfigFile(r));
		String s = c.getString("Region.Permissions.PermanentCache.RemoveNodes", "");
		return s;
	}
	
	public String listTempAddGroup(Region r) {
		FileConfiguration c = YamlConfiguration.loadConfiguration(rm.getConfigFile(r));
		String s = c.getString("Region.Permissions.TempGroups.AddGroups", "");
		return s;
	}

	public String listTempRemGroup(Region r) {
		FileConfiguration c = YamlConfiguration.loadConfiguration(rm.getConfigFile(r));
		String s = c.getString("Region.Permissions.TempGroups.RemoveGroups", "");
		return s;
	}

	public String listPermAddGroup(Region r) {
		FileConfiguration c = YamlConfiguration.loadConfiguration(rm.getConfigFile(r));
		String s = c.getString("Region.Permissions.PermGroups.AddGroups", "");
		return s;
	}

	public String listPermRemGroup(Region r) {
		FileConfiguration c = YamlConfiguration.loadConfiguration(rm.getConfigFile(r));
		String s = c.getString("Region.Permissions.PermGroups.RemoveGroups", "");
		return s;
	}
	
	public void editAddPermGroupAdd(Region r, String group) {
		FileConfiguration c = YamlConfiguration.loadConfiguration(rm.getConfigFile(r));
		String current = (String) c.get("Region.Permissions.PermGroups.AddGroups", "");
		c.set("Region.Permissions.PermGroups.AddGroups", current.trim() + group.trim() + ",");
		r.setPermAddGroups((current.trim() + "," + group.trim() + ",").split(","));
		try {
			c.save(rm.getConfigFile(r));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void editAddPermGroupRemove(Region r, String group) {
		FileConfiguration c = YamlConfiguration.loadConfiguration(rm.getConfigFile(r));
		String current = (String) c.get("Region.Permissions.PermGroups.RemoveGroups", "");
		c.set("Region.Permissions.PermGroups.RemoveGroups", current.trim() + group.trim() + ",");
		r.setPermRemoveGroups((current.trim() + "," + group.trim() + ",").split(","));
		try {
			c.save(rm.getConfigFile(r));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void editRemovePermGroupAdd(Region r, String group) {
		FileConfiguration c = YamlConfiguration.loadConfiguration(rm.getConfigFile(r));
		String current = (String) c.get("Region.Permissions.PermGroups.AddGroups", "");
		current = current.replaceAll(" ", "");
		current = current.replaceAll(group + ",", "");
		current = current.replaceAll(",,", ",");
		c.set("Region.Permissions.PermGroups.AddGroups", current.trim());
		r.setPermAddGroups(current.trim().split(","));
		try {
			c.save(rm.getConfigFile(r));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void editRemovePermGroupRemove(Region r, String group) {
		FileConfiguration c = YamlConfiguration.loadConfiguration(rm.getConfigFile(r));
		String current = (String) c.get("Region.Permissions.PermGroups.RemoveGroups", "");
		current = current.replaceAll(" ", "");
		current = current.replaceAll(group + ",", "");
		current = current.replaceAll(",,", ",");
		c.set("Region.Permissions.PermGroups.RemoveGroups", current.trim());
		r.setPermRemoveGroups(current.trim().split(","));
		try {
			c.save(rm.getConfigFile(r));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void editAddTempGroupAdd(Region r, String group) {
		FileConfiguration c = YamlConfiguration.loadConfiguration(rm.getConfigFile(r));
		String current = (String) c.get("Region.Permissions.TempGroups.AddGroups", "");
		c.set("Region.Permissions.TempGroups.AddGroups", current.trim() + group.trim() + ",");
		r.setTempAddGroups((current.trim() + "," + group.trim() + ",").split(","));
		try {
			c.save(rm.getConfigFile(r));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void editAddTempGroupRemove(Region r, String group) {
		FileConfiguration c = YamlConfiguration.loadConfiguration(rm.getConfigFile(r));
		String current = (String) c.get("Region.Permissions.TempGroups.RemoveGroups", "");
		c.set("Region.Permissions.TempGroups.RemoveGroups", current.trim() + group.trim() + ",");
		r.setTempRemoveGroups((current.trim() + "," + group.trim() + ",").split(","));
		try {
			c.save(rm.getConfigFile(r));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void editRemoveTempGroupAdd(Region r, String group) {
		FileConfiguration c = YamlConfiguration.loadConfiguration(rm.getConfigFile(r));
		String current = (String) c.get("Region.Permissions.TempGroups.AddGroups", "");
		current = current.replaceAll(" ", "");
		current = current.replaceAll(group + ",", "");
		current = current.replaceAll(",,", ",");
		c.set("Region.Permissions.TempGroups.AddGroups", current.trim());
		r.setTempAddGroups(current.trim().split(","));
		try {
			c.save(rm.getConfigFile(r));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void editRemoveTempGroupRemove(Region r, String group) {
		FileConfiguration c = YamlConfiguration.loadConfiguration(rm.getConfigFile(r));
		String current = (String) c.get("Region.Permissions.TempGroups.RemoveGroups", "");
		current = current.replaceAll(" ", "");
		current = current.replaceAll(group + ",", "");
		current = current.replaceAll(",,", ",");
		c.set("Region.Permissions.TempGroups.RemoveGroups", current.trim());
		r.setTempRemoveGroups(current.trim().split(","));
		try {
			c.save(rm.getConfigFile(r));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
