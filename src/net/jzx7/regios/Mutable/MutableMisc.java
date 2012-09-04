package net.jzx7.regios.Mutable;

import java.io.IOException;

import net.jzx7.regiosapi.regions.Region;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class MutableMisc {

	public void editAddToForceCommandSet(Region r, String message){
		FileConfiguration c = YamlConfiguration.loadConfiguration(r.getConfigFile());
		String current = (String)c.get("Region.Command.CommandSet");
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
		FileConfiguration c = YamlConfiguration.loadConfiguration(r.getConfigFile());
		String current = (String)c.get("Region.Command.CommandSet");
		current = current.replaceAll(" ", "");
		current = current.replaceAll(message + ",", "");
		current = current.replaceAll(",,", ",");
		c.set("Region.Command.CommandSet", current.trim());
		r.setCommandSet((current.trim()).split(","));
		try {
			c.save(r.getConfigFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void editSetForceCommand(Region r, boolean val){
		FileConfiguration c = YamlConfiguration.loadConfiguration(r.getConfigFile());
		c.set("Region.Command.ForceCommand", val);
		r.setForceCommand(val);
		try {
			c.save(r.getConfigFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void editGameModeType(Region r, GameMode gm) {
		FileConfiguration c = YamlConfiguration.loadConfiguration(r.getConfigFile());
		c.set("Region.GameMode.Type", gm.toString());
		r.setGameMode(gm);
		try {
			c.save(r.getConfigFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void editGameModeChange(Region r, boolean val) {
		FileConfiguration c = YamlConfiguration.loadConfiguration(r.getConfigFile());
		c.set("Region.GameMode.Change", val);
		r.setChangeGameMode(val);
		try {
			c.save(r.getConfigFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void editResetForceCommandSet(Region r){
		FileConfiguration c = YamlConfiguration.loadConfiguration(r.getConfigFile());
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
