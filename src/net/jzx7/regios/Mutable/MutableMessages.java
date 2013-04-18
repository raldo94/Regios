package net.jzx7.regios.Mutable;

import java.io.IOException;

import net.jzx7.regios.regions.RegionManager;
import net.jzx7.regiosapi.regions.Region;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class MutableMessages {

	private final static RegionManager rm = new RegionManager();
	
	public void editWelcomeMessage(Region r, String message){
		FileConfiguration c = YamlConfiguration.loadConfiguration(rm.getConfigFile(r));
		c.set("Region.Messages.WelcomeMessage", message);
		r.setWelcomeMessage(message);
		try {
			c.save(rm.getConfigFile(r));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void editLeaveMessage(Region r, String message){
		FileConfiguration c = YamlConfiguration.loadConfiguration(rm.getConfigFile(r));
		c.set("Region.Messages.LeaveMessage", message);
		r.setLeaveMessage(message);
		try {
			c.save(rm.getConfigFile(r));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void editPreventEntryMessage(Region r, String message){
		FileConfiguration c = YamlConfiguration.loadConfiguration(rm.getConfigFile(r));
		c.set("Region.Messages.PreventEntryMessage", message);
		r.setPreventEntryMessage(message);
		try {
			c.save(rm.getConfigFile(r));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void editPreventExitMessage(Region r, String message){
		FileConfiguration c = YamlConfiguration.loadConfiguration(rm.getConfigFile(r));
		c.set("Region.Messages.PreventExitMessage", message);
		r.setPreventExitMessage(message);
		try {
			c.save(rm.getConfigFile(r));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void editProtectionMessage(Region r, String message){
		FileConfiguration c = YamlConfiguration.loadConfiguration(rm.getConfigFile(r));
		c.set("Region.Messages.ProtectionMessage", message);
		r.setProtectionMessage(message);
		try {
			c.save(rm.getConfigFile(r));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void editShowWelcomeMessage(Region r, boolean val){
		FileConfiguration c = YamlConfiguration.loadConfiguration(rm.getConfigFile(r));
		c.set("Region.Messages.ShowWelcomeMessage", val);
		r.setShowWelcomeMessage(val);
		try {
			c.save(rm.getConfigFile(r));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void editShowLeaveMessage(Region r, boolean val){
		FileConfiguration c = YamlConfiguration.loadConfiguration(rm.getConfigFile(r));
		c.set("Region.Messages.ShowLeaveMessage", val);
		r.setShowLeaveMessage(val);
		try {
			c.save(rm.getConfigFile(r));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void editShowProtectionMessage(Region r, boolean val){
		FileConfiguration c = YamlConfiguration.loadConfiguration(rm.getConfigFile(r));
		c.set("Region.Messages.ShowProtectionMessage", val);
		r.setShowProtectionMessage(val);
		try {
			c.save(rm.getConfigFile(r));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void editShowPreventEntryMessage(Region r, boolean val){
		FileConfiguration c = YamlConfiguration.loadConfiguration(rm.getConfigFile(r));
		c.set("Region.Messages.ShowPreventEntryMessage", val);
		r.setShowPreventEntryMessage(val);
		try {
			c.save(rm.getConfigFile(r));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void editShowPreventExitMessage(Region r, boolean val){
		FileConfiguration c = YamlConfiguration.loadConfiguration(rm.getConfigFile(r));
		c.set("Region.Messages.ShowPreventExitMessage", val);
		r.setShowPreventExitMessage(val);
		try {
			c.save(rm.getConfigFile(r));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void editShowPvpWarningMessage(Region r, boolean val){
		FileConfiguration c = YamlConfiguration.loadConfiguration(rm.getConfigFile(r));
		c.set("Region.Messages.ShowPvpWarning", val);
		r.setShowPvpWarning(val);
		try {
			c.save(rm.getConfigFile(r));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
