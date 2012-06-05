package couk.Adamki11s.Regios.Mutable;

import java.io.IOException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import couk.Adamki11s.Regios.Regions.Region;

public class MutableInventory {

	public void editPermWipeOnEnter(Region r, boolean val){
		FileConfiguration c = YamlConfiguration.loadConfiguration(r.getConfigFile());
		c.set("Region.Inventory.PermWipeOnEnter", val);
		r.setPermWipeOnEnter(val);
		try {
			c.save(r.getConfigFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void editPermWipeOnExit(Region r, boolean val){
		FileConfiguration c = YamlConfiguration.loadConfiguration(r.getConfigFile());
		c.set("Region.Inventory.PermWipeOnExit", val);
		r.setPermWipeOnExit(val);
		try {
			c.save(r.getConfigFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void editWipeAndCacheOnEnter(Region r, boolean val){
		FileConfiguration c = YamlConfiguration.loadConfiguration(r.getConfigFile());
		c.set("Region.Inventory.WipeAndCacheOnEnter", val);
		r.setWipeAndCacheOnEnter(val);
		try {
			c.save(r.getConfigFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void editWipeAndCacheOnExit(Region r, boolean val){
		FileConfiguration c = YamlConfiguration.loadConfiguration(r.getConfigFile());
		c.set("Region.Inventory.WipeAndCacheOnExit", val);
		r.setWipeAndCacheOnExit(val);
		try {
			c.save(r.getConfigFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
