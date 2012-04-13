package couk.Adamki11s.Regios.Mutable;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import couk.Adamki11s.Regios.Regions.Region;
import couk.Adamki11s.Regios.Scheduler.LightningRunner;

public class MutableFun {
	
	public void editLSPS(Region r, int val){
		File file = r.getConfigFile();
		FileConfiguration c = YamlConfiguration.loadConfiguration(file);
		Map<String, Object> all = c.getValues(true);
		all.remove("Region.Other.LSPS");
		for(Entry<String, Object> entry : all.entrySet()){
			c.set(entry.getKey(), entry.getValue());
		}
		c.set("Region.Other.LSPS", val);
		r.setLSPS(val);
		try {
	c.save(r.getConfigFile());
} catch (IOException e) {
	e.printStackTrace();
}
		if(val == 0){
			LightningRunner.removeRegion(r);
		} else if(val > 0){
			LightningRunner.addRegion(r);
		}
	}
	
	public void editHealthRegen(Region r, int val){
		File file = r.getConfigFile();
		FileConfiguration c = YamlConfiguration.loadConfiguration(file);
		Map<String, Object> all = c.getValues(true);
		all.remove("Region.Other.HealthRegen");
		for(Entry<String, Object> entry : all.entrySet()){
			c.set(entry.getKey(), entry.getValue());
		}
		c.set("Region.Other.HealthRegen", val);
		r.setHealthRegen(val);
		try {
	c.save(r.getConfigFile());
} catch (IOException e) {
	e.printStackTrace();
}
	}
	
	public void editVelocityWarp(Region r, double val){
		File file = r.getConfigFile();
		FileConfiguration c = YamlConfiguration.loadConfiguration(file);
		Map<String, Object> all = c.getValues(true);
		all.remove("Region.Other.VelocityWarp");
		for(Entry<String, Object> entry : all.entrySet()){
			c.set(entry.getKey(), entry.getValue());
		}
		c.set("Region.Other.VelocityWarp", val);
		r.setVelocityWarp(val);
		try {
	c.save(r.getConfigFile());
} catch (IOException e) {
	e.printStackTrace();
}
	}
	
	public void editWarpLocation(Region r, Location val){
		File file = r.getConfigFile();
		FileConfiguration c = YamlConfiguration.loadConfiguration(file);
		Map<String, Object> all = c.getValues(true);
		all.remove("Region.Teleportation.Warp.Location");
		for(Entry<String, Object> entry : all.entrySet()){
			c.set(entry.getKey(), entry.getValue());
		}
		c.set("Region.Teleportation.Warp.Location", val.getWorld().getName() + "," + val.getBlockX() + "," + val.getBlockY() + "," + val.getBlockZ());
		r.setWarp(val);
		try {
	c.save(r.getConfigFile());
} catch (IOException e) {
	e.printStackTrace();
}
	}
	
	public void editRemoveWarpLocation(Region r){
		File file = r.getConfigFile();
		FileConfiguration c = YamlConfiguration.loadConfiguration(file);
		Map<String, Object> all = c.getValues(true);
		all.remove("Region.Teleportation.Warp.Location");
		for(Entry<String, Object> entry : all.entrySet()){
			c.set(entry.getKey(), entry.getValue());
		}
		c.set("Region.Teleportation.Warp.Location", r.getWorld().getName() + ",0,0,0");
		r.setWarp(new Location(r.getWorld(), 0, 0, 0));
		try {
	c.save(r.getConfigFile());
} catch (IOException e) {
	e.printStackTrace();
}
	}
	
	public void editHealthEnabled(Region r, boolean val){
		File file = r.getConfigFile();
		FileConfiguration c = YamlConfiguration.loadConfiguration(file);
		Map<String, Object> all = c.getValues(true);
		all.remove("Region.Other.HealthEnabled");
		for(Entry<String, Object> entry : all.entrySet()){
			c.set(entry.getKey(), entry.getValue());
		}
		c.set("Region.Other.HealthEnabled", val);
		r.setHealthEnabled(val);
		try {
	c.save(r.getConfigFile());
} catch (IOException e) {
	e.printStackTrace();
}
	}
	
	public void editPvPEnabled(Region r, boolean val){
		File file = r.getConfigFile();
		FileConfiguration c = YamlConfiguration.loadConfiguration(file);
		Map<String, Object> all = c.getValues(true);
		all.remove("Region.Other.PvP");
		for(Entry<String, Object> entry : all.entrySet()){
			c.set(entry.getKey(), entry.getValue());
		}
		c.set("Region.Other.PvP", val);
		r.setPvp(val);
		try {
	c.save(r.getConfigFile());
} catch (IOException e) {
	e.printStackTrace();
}
	}

}
