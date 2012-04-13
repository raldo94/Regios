package couk.Adamki11s.Regios.Mutable;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import couk.Adamki11s.Regios.Regions.Region;

public class MutableMisc {
	
	public void editAddToForceCommandSet(Region r, String message){
		File file = r.getConfigFile();
		FileConfiguration c = YamlConfiguration.loadConfiguration(file);
		Map<String, Object> all = c.getValues(true);
		String current = (String)all.get("Region.Command.CommandSet");
		all.remove("Region.Command.CommandSet");
		for(Entry<String, Object> entry : all.entrySet()){
			c.set(entry.getKey(), entry.getValue());
		}
		c.set("Region.Command.CommandSet", current.trim() + message.trim() + ",");
		r.setCommandSet((current.trim() + "," + message.trim()).split(","));
		try {
	c.save(r.getConfigFile());
} catch (IOException e) {
	e.printStackTrace();
}
	}
	
	public boolean checkCommandSet(Region r, String addition){
		for (String s : r.getCommandSet()) {
			if (s.equalsIgnoreCase(addition)) {
				return true;
			}
		}
		return false;
	}
	
	public void editRemoveFromForceCommandSet(Region r, String message){
		File file = r.getConfigFile();
		FileConfiguration c = YamlConfiguration.loadConfiguration(file);
		Map<String, Object> all = c.getValues(true);
		String current = (String)all.get("Region.Command.CommandSet");
		current = current.replaceAll(" ", "");
		current = current.replaceAll(message + ",", "");
		current = current.replaceAll(",,", ",");
		all.remove("Region.Command.CommandSet");
		for(Entry<String, Object> entry : all.entrySet()){
			c.set(entry.getKey(), entry.getValue());
		}
		c.set("Region.Command.CommandSet", current.trim());
		r.setCommandSet((current.trim()).split(","));
		try {
	c.save(r.getConfigFile());
} catch (IOException e) {
	e.printStackTrace();
}
	}
	
	public void editSetForceCommand(Region r, boolean val){
		File file = r.getConfigFile();
		FileConfiguration c = YamlConfiguration.loadConfiguration(file);
		Map<String, Object> all = c.getValues(true);
		all.remove("Region.Command.ForceCommand");
		for(Entry<String, Object> entry : all.entrySet()){
			c.set(entry.getKey(), entry.getValue());
		}
		c.set("Region.Command.ForceCommand", val);
		r.setForceCommand(val);
		try {
	c.save(r.getConfigFile());
} catch (IOException e) {
	e.printStackTrace();
}
	}
	
	public void editResetForceCommandSet(Region r){
		File file = r.getConfigFile();
		FileConfiguration c = YamlConfiguration.loadConfiguration(file);
		Map<String, Object> all = c.getValues(true);
		all.remove("Region.Command.CommandSet");
		for(Entry<String, Object> entry : all.entrySet()){
			c.set(entry.getKey(), entry.getValue());
		}
		c.set("Region.Command.CommandSet", "");
		r.setCommandSet(("").split(","));
		try {
	c.save(r.getConfigFile());
} catch (IOException e) {
	e.printStackTrace();
}
	}
	
	public String listCommandSet(Region r) {
		StringBuilder sb = new StringBuilder();
		for (String s : r.getCommandSet()) {
			sb.append(ChatColor.WHITE + s).append(ChatColor.BLUE + ", ");
		}
		return sb.toString();
	}

}
