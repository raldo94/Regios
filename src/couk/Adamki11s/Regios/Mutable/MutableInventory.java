package couk.Adamki11s.Regios.Mutable;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import couk.Adamki11s.Regios.Regions.Region;

public class MutableInventory {
	
	public void editPermWipeOnEnter(Region r, boolean val){
		File file = r.getConfigFile();
		FileConfiguration c = YamlConfiguration.loadConfiguration(file);
		Map<String, Object> all = c.getValues(true);
		all.remove("Region.Inventory.PermWipeOnEnter");
		for(Entry<String, Object> entry : all.entrySet()){
			c.set(entry.getKey(), entry.getValue());
		}
		c.set("Region.Inventory.PermWipeOnEnter", val);
		r.setPermWipeOnEnter(val);
		try {
	c.save(r.getConfigFile());
} catch (IOException e) {
	e.printStackTrace();
}
	}
	
	public void editPermWipeOnExit(Region r, boolean val){
		File file = r.getConfigFile();
		FileConfiguration c = YamlConfiguration.loadConfiguration(file);
		Map<String, Object> all = c.getValues(true);
		all.remove("Region.Inventory.PermWipeOnExit");
		for(Entry<String, Object> entry : all.entrySet()){
			c.set(entry.getKey(), entry.getValue());
		}
		c.set("Region.Inventory.PermWipeOnExit", val);
		r.setPermWipeOnExit(val);
		try {
	c.save(r.getConfigFile());
} catch (IOException e) {
	e.printStackTrace();
}
	}
	
	public void editWipeAndCacheOnEnter(Region r, boolean val){
		File file = r.getConfigFile();
		FileConfiguration c = YamlConfiguration.loadConfiguration(file);
		Map<String, Object> all = c.getValues(true);
		all.remove("Region.Inventory.WipeAndCacheOnEnter");
		for(Entry<String, Object> entry : all.entrySet()){
			c.set(entry.getKey(), entry.getValue());
		}
		c.set("Region.Inventory.WipeAndCacheOnEnter", val);
		r.setWipeAndCacheOnEnter(val);
		try {
	c.save(r.getConfigFile());
} catch (IOException e) {
	e.printStackTrace();
}
	}
	
	public void editWipeAndCacheOnExit(Region r, boolean val){
		File file = r.getConfigFile();
		FileConfiguration c = YamlConfiguration.loadConfiguration(file);
		Map<String, Object> all = c.getValues(true);
		all.remove("Region.Inventory.WipeAndCacheOnExit");
		for(Entry<String, Object> entry : all.entrySet()){
			c.set(entry.getKey(), entry.getValue());
		}
		c.set("Region.Inventory.WipeAndCacheOnExit", val);
		r.setWipeAndCacheOnExit(val);
		try {
	c.save(r.getConfigFile());
} catch (IOException e) {
	e.printStackTrace();
}
	}

}
