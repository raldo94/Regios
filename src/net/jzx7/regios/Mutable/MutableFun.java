package net.jzx7.regios.Mutable;

import java.io.IOException;

import net.jzx7.regios.Scheduler.LightningRunner;
import net.jzx7.regiosapi.regions.Region;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;


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
