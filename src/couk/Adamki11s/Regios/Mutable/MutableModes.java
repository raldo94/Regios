package couk.Adamki11s.Regios.Mutable;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import couk.Adamki11s.Regios.Data.MODE;
import couk.Adamki11s.Regios.Regions.Region;

public class MutableModes {
	
	public void editProtectionMode(Region r, MODE mode){
		File file = r.getConfigFile();
		FileConfiguration c = YamlConfiguration.loadConfiguration(file);
		Map<String, Object> all = c.getValues(true);
		all.remove("Region.Modes.ProtectionMode");
		for(Entry<String, Object> entry : all.entrySet()){
			c.set(entry.getKey(), entry.getValue());
		}
		c.set("Region.Modes.ProtectionMode", mode.toString());
		r.setProtectionMode(mode);
		try {
			c.save(r.getConfigFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void editPreventEntryMode(Region r, MODE mode){
		File file = r.getConfigFile();
		FileConfiguration c = YamlConfiguration.loadConfiguration(file);
		Map<String, Object> all = c.getValues(true);
		all.remove("Region.Modes.PreventEntryMode");
		for(Entry<String, Object> entry : all.entrySet()){
			c.set(entry.getKey(), entry.getValue());
		}
		c.set("Region.Modes.PreventEntryMode", mode.toString());
		r.setPreventEntryMode(mode);
		try {
			c.save(r.getConfigFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void editPreventExitMode(Region r, MODE mode){
		File file = r.getConfigFile();
		FileConfiguration c = YamlConfiguration.loadConfiguration(file);
		Map<String, Object> all = c.getValues(true);
		all.remove("Region.Modes.PreventExitMode");
		for(Entry<String, Object> entry : all.entrySet()){
			c.set(entry.getKey(), entry.getValue());
		}
		c.set("Region.Modes.PreventExitMode", mode.toString());
		r.setPreventExitMode(mode);
		try {
			c.save(r.getConfigFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void editItemControlMode(Region r, MODE mode){
		File file = r.getConfigFile();
		FileConfiguration c = YamlConfiguration.loadConfiguration(file);
		Map<String, Object> all = c.getValues(true);
		all.remove("Region.Modes.ItemControlMode");
		for(Entry<String, Object> entry : all.entrySet()){
			c.set(entry.getKey(), entry.getValue());
		}
		c.set("Region.Modes.ItemControlMode", mode.toString());
		r.setItemMode(mode);
		try {
			c.save(r.getConfigFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
