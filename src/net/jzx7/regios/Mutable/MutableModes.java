package net.jzx7.regios.Mutable;

import java.io.IOException;

import net.jzx7.regios.regions.RegionManager;
import net.jzx7.regiosapi.data.MODE;
import net.jzx7.regiosapi.regions.Region;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class MutableModes {

	private final static RegionManager rm = new RegionManager();
	
	public void editProtectionMode(Region r, MODE mode){
		FileConfiguration c = YamlConfiguration.loadConfiguration(rm.getConfigFile(r));
		c.set("Region.Modes.ProtectionMode", mode.toString());
		r.setProtectionMode(mode);
		try {
			c.save(rm.getConfigFile(r));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void editPreventEntryMode(Region r, MODE mode){
		FileConfiguration c = YamlConfiguration.loadConfiguration(rm.getConfigFile(r));
		c.set("Region.Modes.PreventEntryMode", mode.toString());
		r.setPreventEntryMode(mode);
		try {
			c.save(rm.getConfigFile(r));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void editPreventExitMode(Region r, MODE mode){
		FileConfiguration c = YamlConfiguration.loadConfiguration(rm.getConfigFile(r));
		c.set("Region.Modes.PreventExitMode", mode.toString());
		r.setPreventExitMode(mode);
		try {
			c.save(rm.getConfigFile(r));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void editItemControlMode(Region r, MODE mode){
		FileConfiguration c = YamlConfiguration.loadConfiguration(rm.getConfigFile(r));
		c.set("Region.Modes.ItemControlMode", mode.toString());
		r.setItemMode(mode);
		try {
			c.save(rm.getConfigFile(r));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
