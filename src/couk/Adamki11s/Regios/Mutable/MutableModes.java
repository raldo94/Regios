package couk.Adamki11s.Regios.Mutable;

import java.io.IOException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import couk.Adamki11s.Regios.Data.MODE;
import couk.Adamki11s.Regios.Regions.Region;

public class MutableModes {

	public void editProtectionMode(Region r, MODE mode){
		FileConfiguration c = YamlConfiguration.loadConfiguration(r.getConfigFile());
		c.set("Region.Modes.ProtectionMode", mode.toString());
		r.setProtectionMode(mode);
		try {
			c.save(r.getConfigFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void editPreventEntryMode(Region r, MODE mode){
		FileConfiguration c = YamlConfiguration.loadConfiguration(r.getConfigFile());
		c.set("Region.Modes.PreventEntryMode", mode.toString());
		r.setPreventEntryMode(mode);
		try {
			c.save(r.getConfigFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void editPreventExitMode(Region r, MODE mode){
		FileConfiguration c = YamlConfiguration.loadConfiguration(r.getConfigFile());
		c.set("Region.Modes.PreventExitMode", mode.toString());
		r.setPreventExitMode(mode);
		try {
			c.save(r.getConfigFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void editItemControlMode(Region r, MODE mode){
		FileConfiguration c = YamlConfiguration.loadConfiguration(r.getConfigFile());
		c.set("Region.Modes.ItemControlMode", mode.toString());
		r.setItemMode(mode);
		try {
			c.save(r.getConfigFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
