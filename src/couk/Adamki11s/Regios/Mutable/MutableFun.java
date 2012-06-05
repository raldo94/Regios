package couk.Adamki11s.Regios.Mutable;

import java.io.IOException;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import couk.Adamki11s.Regios.Regions.Region;
import couk.Adamki11s.Regios.Scheduler.LightningRunner;

public class MutableFun {

	public void editLSPS(Region r, int val){
		FileConfiguration c = YamlConfiguration.loadConfiguration(r.getConfigFile());
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
		FileConfiguration c = YamlConfiguration.loadConfiguration(r.getConfigFile());
		c.set("Region.Other.HealthRegen", val);
		r.setHealthRegen(val);
		try {
			c.save(r.getConfigFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void editVelocityWarp(Region r, double val){
		FileConfiguration c = YamlConfiguration.loadConfiguration(r.getConfigFile());
		c.set("Region.Other.VelocityWarp", val);
		r.setVelocityWarp(val);
		try {
			c.save(r.getConfigFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void editWarpLocation(Region r, Location val){
		FileConfiguration c = YamlConfiguration.loadConfiguration(r.getConfigFile());
		c.set("Region.Teleportation.Warp.Location", val.getWorld().getName() + "," + val.getBlockX() + "," + val.getBlockY() + "," + val.getBlockZ());
		r.setWarp(val);
		try {
			c.save(r.getConfigFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void editRemoveWarpLocation(Region r){
		FileConfiguration c = YamlConfiguration.loadConfiguration(r.getConfigFile());
		c.set("Region.Teleportation.Warp.Location", r.getWorld().getName() + ",0,0,0");
		r.setWarp(new Location(r.getWorld(), 0, 0, 0));
		try {
			c.save(r.getConfigFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void editHealthEnabled(Region r, boolean val){
		FileConfiguration c = YamlConfiguration.loadConfiguration(r.getConfigFile());
		c.set("Region.Other.HealthEnabled", val);
		r.setHealthEnabled(val);
		try {
			c.save(r.getConfigFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void editPvPEnabled(Region r, boolean val){
		FileConfiguration c = YamlConfiguration.loadConfiguration(r.getConfigFile());
		c.set("Region.Other.PvP", val);
		r.setPvp(val);
		try {
			c.save(r.getConfigFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
