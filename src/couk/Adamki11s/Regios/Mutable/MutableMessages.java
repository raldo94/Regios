package couk.Adamki11s.Regios.Mutable;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import couk.Adamki11s.Regios.Regions.Region;

public class MutableMessages {
	
	public void editWelcomeMessage(Region r, String message){
		File file = r.getConfigFile();
		FileConfiguration c = YamlConfiguration.loadConfiguration(file);
		Map<String, Object> all = c.getValues(true);
		all.remove("Region.Messages.WelcomeMessage");
		for(Entry<String, Object> entry : all.entrySet()){
			c.set(entry.getKey(), entry.getValue());
		}
		c.set("Region.Messages.WelcomeMessage", message);
		r.setWelcomeMessage(message);
		try {
	c.save(r.getConfigFile());
} catch (IOException e) {
	e.printStackTrace();
}
	}
	
	public void editLeaveMessage(Region r, String message){
		File file = r.getConfigFile();
		FileConfiguration c = YamlConfiguration.loadConfiguration(file);
		Map<String, Object> all = c.getValues(true);
		all.remove("Region.Messages.LeaveMessage");
		for(Entry<String, Object> entry : all.entrySet()){
			c.set(entry.getKey(), entry.getValue());
		}
		c.set("Region.Messages.LeaveMessage", message);
		r.setLeaveMessage(message);
		try {
	c.save(r.getConfigFile());
} catch (IOException e) {
	e.printStackTrace();
}
	}
	
	public void editPreventEntryMessage(Region r, String message){
		File file = r.getConfigFile();
		FileConfiguration c = YamlConfiguration.loadConfiguration(file);
		Map<String, Object> all = c.getValues(true);
		all.remove("Region.Messages.PreventEntryMessage");
		for(Entry<String, Object> entry : all.entrySet()){
			c.set(entry.getKey(), entry.getValue());
		}
		c.set("Region.Messages.PreventEntryMessage", message);
		r.setPreventEntryMessage(message);
		try {
	c.save(r.getConfigFile());
} catch (IOException e) {
	e.printStackTrace();
}
	}
	
	public void editPreventExitMessage(Region r, String message){
		File file = r.getConfigFile();
		FileConfiguration c = YamlConfiguration.loadConfiguration(file);
		Map<String, Object> all = c.getValues(true);
		all.remove("Region.Messages.PreventExitMessage");
		for(Entry<String, Object> entry : all.entrySet()){
			c.set(entry.getKey(), entry.getValue());
		}
		c.set("Region.Messages.PreventExitMessage", message);
		r.setPreventExitMessage(message);
		try {
	c.save(r.getConfigFile());
} catch (IOException e) {
	e.printStackTrace();
}
	}
	
	public void editProtectionMessage(Region r, String message){
		File file = r.getConfigFile();
		FileConfiguration c = YamlConfiguration.loadConfiguration(file);
		Map<String, Object> all = c.getValues(true);
		all.remove("Region.Messages.ProtectionMessage");
		for(Entry<String, Object> entry : all.entrySet()){
			c.set(entry.getKey(), entry.getValue());
		}
		c.set("Region.Messages.ProtectionMessage", message);
		r.setProtectionMessage(message);
		try {
	c.save(r.getConfigFile());
} catch (IOException e) {
	e.printStackTrace();
}
	}
	
	public void editShowWelcomeMessage(Region r, boolean val){
		File file = r.getConfigFile();
		FileConfiguration c = YamlConfiguration.loadConfiguration(file);
		Map<String, Object> all = c.getValues(true);
		all.remove("Region.Messages.ShowWelcomeMessage");
		for(Entry<String, Object> entry : all.entrySet()){
			c.set(entry.getKey(), entry.getValue());
		}
		c.set("Region.Messages.ShowWelcomeMessage", val);
		r.setShowWelcomeMessage(val);
		try {
	c.save(r.getConfigFile());
} catch (IOException e) {
	e.printStackTrace();
}
	}
	
	public void editShowLeaveMessage(Region r, boolean val){
		File file = r.getConfigFile();
		FileConfiguration c = YamlConfiguration.loadConfiguration(file);
		Map<String, Object> all = c.getValues(true);
		all.remove("Region.Messages.ShowLeaveMessage");
		for(Entry<String, Object> entry : all.entrySet()){
			c.set(entry.getKey(), entry.getValue());
		}
		c.set("Region.Messages.ShowLeaveMessage", val);
		r.setShowLeaveMessage(val);
		try {
	c.save(r.getConfigFile());
} catch (IOException e) {
	e.printStackTrace();
}
	}
	
	public void editShowProtectionMessage(Region r, boolean val){
		File file = r.getConfigFile();
		FileConfiguration c = YamlConfiguration.loadConfiguration(file);
		Map<String, Object> all = c.getValues(true);
		all.remove("Region.Messages.ShowProtectionMessage");
		for(Entry<String, Object> entry : all.entrySet()){
			c.set(entry.getKey(), entry.getValue());
		}
		c.set("Region.Messages.ShowProtectionMessage", val);
		r.setShowProtectionMessage(val);
		try {
	c.save(r.getConfigFile());
} catch (IOException e) {
	e.printStackTrace();
}
	}
	
	public void editShowPreventEntryMessage(Region r, boolean val){
		File file = r.getConfigFile();
		FileConfiguration c = YamlConfiguration.loadConfiguration(file);
		Map<String, Object> all = c.getValues(true);
		all.remove("Region.Messages.ShowPreventEntryMessage");
		for(Entry<String, Object> entry : all.entrySet()){
			c.set(entry.getKey(), entry.getValue());
		}
		c.set("Region.Messages.ShowPreventEntryMessage", val);
		r.setShowPreventEntryMessage(val);
		try {
	c.save(r.getConfigFile());
} catch (IOException e) {
	e.printStackTrace();
}
	}
	
	public void editShowPreventExitMessage(Region r, boolean val){
		File file = r.getConfigFile();
		FileConfiguration c = YamlConfiguration.loadConfiguration(file);
		Map<String, Object> all = c.getValues(true);
		all.remove("Region.Messages.ShowPreventExitMessage");
		for(Entry<String, Object> entry : all.entrySet()){
			c.set(entry.getKey(), entry.getValue());
		}
		c.set("Region.Messages.ShowPreventExitMessage", val);
		r.setShowPreventExitMessage(val);
		try {
	c.save(r.getConfigFile());
} catch (IOException e) {
	e.printStackTrace();
}
	}
	
	public void editShowPvpWarningMessage(Region r, boolean val){
		File file = r.getConfigFile();
		FileConfiguration c = YamlConfiguration.loadConfiguration(file);
		Map<String, Object> all = c.getValues(true);
		all.remove("Region.Messages.ShowPvpWarning");
		for(Entry<String, Object> entry : all.entrySet()){
			c.set(entry.getKey(), entry.getValue());
		}
		c.set("Region.Messages.ShowPvpWarning", val);
		r.setShowPvpWarning(val);
		try {
	c.save(r.getConfigFile());
} catch (IOException e) {
	e.printStackTrace();
}
	}

}
