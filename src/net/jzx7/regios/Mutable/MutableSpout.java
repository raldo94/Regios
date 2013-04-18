package net.jzx7.regios.Mutable;

import java.io.IOException;

import net.jzx7.regios.regions.RegionManager;
import net.jzx7.regiosapi.regions.Region;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class MutableSpout {
	
	private final static RegionManager rm = new RegionManager();
	
	public boolean checkMusicUrl(String url, Region r){
		for(String URL : r.getCustomSoundUrl()){
			if(URL.equalsIgnoreCase(url)){
				return true;
			}
		}
		return false;
	}
	
	public void editWelcomeMessage(Region r, String message){
		FileConfiguration c = YamlConfiguration.loadConfiguration(rm.getConfigFile(r));
		c.set("Region.Spout.Welcome.Message", message);
		r.setSpoutEntryMessage(message);
		try {
			c.save(rm.getConfigFile(r));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void editWelcomeEnabled(Region r, boolean val){
		FileConfiguration c = YamlConfiguration.loadConfiguration(rm.getConfigFile(r));
		c.set("Region.Spout.Welcome.Enabled", val);
		r.setSpoutWelcomeEnabled(val);
		try {
			c.save(rm.getConfigFile(r));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void editLeaveEnabled(Region r, boolean val){
		FileConfiguration c = YamlConfiguration.loadConfiguration(rm.getConfigFile(r));
		c.set("Region.Spout.Leave.Enabled", val);
		r.setSpoutLeaveEnabled(val);
		try {
			c.save(rm.getConfigFile(r));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void editLeaveMessage(Region r, String message){
		FileConfiguration c = YamlConfiguration.loadConfiguration(rm.getConfigFile(r));
		c.set("Region.Spout.Leave.Message", message);
		r.setSpoutExitMessage(message);
		try {
			c.save(rm.getConfigFile(r));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void editWelcomeMaterial(Region r, int mat){
		FileConfiguration c = YamlConfiguration.loadConfiguration(rm.getConfigFile(r));
		c.set("Region.Spout.Welcome.IconID", mat);
		r.setSpoutEntryMaterial(mat);
		try {
			c.save(rm.getConfigFile(r));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void editLeaveMaterial(Region r, int mat){
		FileConfiguration c = YamlConfiguration.loadConfiguration(rm.getConfigFile(r));
		c.set("Region.Spout.Leave.IconID", mat);
		r.setSpoutExitMaterial(mat);
		try {
			c.save(rm.getConfigFile(r));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void editTexturePackURL(Region r, String message){
		FileConfiguration c = YamlConfiguration.loadConfiguration(rm.getConfigFile(r));
		c.set("Region.Spout.Texture.TexturePackURL", message);
		r.setSpoutExitMessage(message);
		try {
			c.save(rm.getConfigFile(r));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void editUseTexturePack(Region r, boolean val){
		FileConfiguration c = YamlConfiguration.loadConfiguration(rm.getConfigFile(r));
		c.set("Region.Spout.Texture.Region.Spout.Texture.UseTexture", val);
		r.setUseSpoutTexturePack(val);
		try {
			c.save(rm.getConfigFile(r));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void editUseMusic(Region r, boolean val){
		FileConfiguration c = YamlConfiguration.loadConfiguration(rm.getConfigFile(r));
		c.set("Region.Spout.Sound.PlayCustomMusic", val);
		r.setPlayCustomSoundUrl(val);
		try {
			c.save(rm.getConfigFile(r));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void editAddToMusicList(Region r, String message){
		FileConfiguration c = YamlConfiguration.loadConfiguration(rm.getConfigFile(r));
		String current = (String)c.get("Region.Spout.Sound.CustomMusicURL");
		if(current != null) {
			c.set("Region.Spout.Sound.CustomMusicURL", current.trim() + message.trim() + ",");
			r.setCustomSoundUrl((current.trim() + "," + message.trim()).split(","));
		} else {
			c.set("Region.Spout.Sound.CustomMusicURL", message.trim() + ",");
			r.setCustomSoundUrl((message.trim()).split(","));
		}
		
		try {
			c.save(rm.getConfigFile(r));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void editRemoveFromMusicList(Region r, String message){
		FileConfiguration c = YamlConfiguration.loadConfiguration(rm.getConfigFile(r));
		String current = (String)c.get("Region.Spout.Sound.CustomMusicURL");
		current = current.replaceAll(" ", "");
		current = current.replaceAll(message + ",", "");
		current = current.replaceAll(",,", ",");
		c.set("Region.Spout.Sound.CustomMusicURL", current.trim());
		r.setCustomSoundUrl((current.trim()).split(","));
		try {
			c.save(rm.getConfigFile(r));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void editResetMusicList(Region r){
		FileConfiguration c = YamlConfiguration.loadConfiguration(rm.getConfigFile(r));
		c.set("Region.Spout.Sound.CustomMusicURL", "");
		r.setCustomSoundUrl(("").split(","));
		try {
			c.save(rm.getConfigFile(r));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
