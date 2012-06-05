package couk.Adamki11s.Regios.Mutable;

import java.io.IOException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import couk.Adamki11s.Regios.Regions.Region;

public class MutablePermissions {

	public void editAddToTempAddCache(Region r, String message) {
		FileConfiguration c = YamlConfiguration.loadConfiguration(r.getConfigFile());
		String current = (String) c.get("Region.Permissions.TemporaryCache.AddNodes");
		c.set("Region.Permissions.TemporaryCache.AddNodes", current.trim() + message.trim() + ",");
		r.setTempNodesCacheAdd((current.trim() + "," + message.trim()).split(","));
		try {
			c.save(r.getConfigFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void editRemoveFromTempAddCache(Region r, String message) {
		FileConfiguration c = YamlConfiguration.loadConfiguration(r.getConfigFile());
		String current = (String) c.get("Region.Permissions.TemporaryCache.AddNodes");
		current = current.replaceAll(" ", "");
		current = current.replaceAll(message + ",", "");
		current = current.replaceAll(",,", ",");
		c.set("Region.Permissions.TemporaryCache.AddNodes", current.trim());
		r.setTempNodesCacheAdd((current.trim()).split(","));
		try {
			c.save(r.getConfigFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void editResetTempAddCache(Region r) {
		FileConfiguration c = YamlConfiguration.loadConfiguration(r.getConfigFile());
		c.set("Region.Permissions.TemporaryCache.AddNodes", "");
		r.setTempNodesCacheAdd(("").split(","));
		try {
			c.save(r.getConfigFile());
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
		FileConfiguration c = YamlConfiguration.loadConfiguration(r.getConfigFile());
		String current = (String) c.get("Region.Permissions.TemporaryCache.RemoveNodes");
		c.set("Region.Permissions.TemporaryCache.RemoveNodes", current.trim() + message.trim() + ",");
		r.setTempNodesCacheAdd((current.trim() + "," + message.trim()).split(","));
		try {
			c.save(r.getConfigFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void editRemoveFromTempRemCache(Region r, String message) {
		FileConfiguration c = YamlConfiguration.loadConfiguration(r.getConfigFile());
		String current = (String) c.get("Region.Permissions.TemporaryCache.RemoveNodes");
		current = current.replaceAll(" ", "");
		current = current.replaceAll(message + ",", "");
		current = current.replaceAll(",,", ",");
		c.set("Region.Permissions.TemporaryCache.RemoveNodes", current.trim());
		r.setTempNodesCacheAdd((current.trim()).split(","));
		try {
			c.save(r.getConfigFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void editResetTempRemCache(Region r) {
		FileConfiguration c = YamlConfiguration.loadConfiguration(r.getConfigFile());
		c.set("Region.Permissions.TemporaryCache.RemoveNodes", "");
		r.setTempNodesCacheAdd(("").split(","));
		try {
			c.save(r.getConfigFile());
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
		FileConfiguration c = YamlConfiguration.loadConfiguration(r.getConfigFile());
		String current = (String) c.get("Region.Permissions.PermanentCache.AddNodes");
		c.set("Region.Permissions.PermanentCache.AddNodes", current.trim() + message.trim() + ",");
		r.setPermanentNodesCacheAdd((current.trim() + "," + message.trim()).split(","));
		try {
			c.save(r.getConfigFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void editRemoveFromPermAddCache(Region r, String message) {
		FileConfiguration c = YamlConfiguration.loadConfiguration(r.getConfigFile());
		String current = (String) c.get("Region.Permissions.PermanentCache.AddNodes");
		current = current.replaceAll(" ", "");
		current = current.replaceAll(message + ",", "");
		current = current.replaceAll(",,", ",");
		c.set("Region.Permissions.PermanentCache.AddNodes", current.trim());
		r.setPermanentNodesCacheAdd((current.trim()).split(","));
		try {
			c.save(r.getConfigFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void editResetPermAddCache(Region r) {
		FileConfiguration c = YamlConfiguration.loadConfiguration(r.getConfigFile());
		c.set("Region.Permissions.PermanentCache.AddNodes", "");
		r.setPermanentNodesCacheAdd(("").split(","));
		try {
			c.save(r.getConfigFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void editAddToPermRemoveCache(Region r, String message) {
		FileConfiguration c = YamlConfiguration.loadConfiguration(r.getConfigFile());
		String current = (String) c.get("Region.Permissions.PermanentCache.RemoveNodes");
		c.set("Region.Permissions.PermanentCache.RemoveNodes", current.trim() + message.trim() + ",");
		r.setPermanentNodesCacheRemove((current.trim() + "," + message.trim()).split(","));
		try {
			c.save(r.getConfigFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void editRemoveFromPermRemoveCache(Region r, String message) {
		FileConfiguration c = YamlConfiguration.loadConfiguration(r.getConfigFile());
		String current = (String) c.get("Region.Permissions.PermanentCache.RemoveNodes");
		current = current.replaceAll(" ", "");
		current = current.replaceAll(message + ",", "");
		current = current.replaceAll(",,", ",");
		c.set("Region.Permissions.PermanentCache.RemoveNodes", current.trim());
		r.setPermanentNodesCacheRemove((current.trim()).split(","));
	}

	public void editResetPermRemoveCache(Region r) {
		FileConfiguration c = YamlConfiguration.loadConfiguration(r.getConfigFile());
		c.set("Region.Permissions.PermanentCache.RemoveNodes", "");
		r.setPermanentNodesCacheRemove(("").split(","));
		try {
			c.save(r.getConfigFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String listTempAddCache(Region r) {
		FileConfiguration c = YamlConfiguration.loadConfiguration(r.getConfigFile());
		String s = c.getString("Region.Permissions.TemporaryCache.AddNodes", "");
		return s;
	}

	public String listTempRemCache(Region r) {
		FileConfiguration c = YamlConfiguration.loadConfiguration(r.getConfigFile());
		String s = c.getString("Region.Permissions.TemporaryCache.RemoveNodes", "");
		return s;
	}

	public String listPermAddCache(Region r) {
		FileConfiguration c = YamlConfiguration.loadConfiguration(r.getConfigFile());
		String s = c.getString("Region.Permissions.PermanentCache.AddNodes", "");
		return s;
	}

	public String listPermRemCache(Region r) {
		FileConfiguration c = YamlConfiguration.loadConfiguration(r.getConfigFile());
		String s = c.getString("Region.Permissions.PermanentCache.RemoveNodes", "");
		return s;
	}

}
