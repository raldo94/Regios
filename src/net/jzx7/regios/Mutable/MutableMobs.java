package net.jzx7.regios.Mutable;

import java.io.IOException;

import net.jzx7.regios.regions.RegionManager;
import net.jzx7.regiosapi.regions.Region;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class MutableMobs {
	
	private final static RegionManager rm = new RegionManager();
	
	public void editMobSpawn(Region r, boolean val){
		FileConfiguration c = YamlConfiguration.loadConfiguration(rm.getConfigFile(r));
		c.set("Region.Other.MobSpawns", val);
		r.setMobSpawns(val);
		try {
			c.save(rm.getConfigFile(r));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void editMonsterSpawn(Region r, boolean val){
		FileConfiguration c = YamlConfiguration.loadConfiguration(rm.getConfigFile(r));
		c.set("Region.Other.MonsterSpawns", val);
		r.setMonsterSpawns(val);
		try {
			c.save(rm.getConfigFile(r));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
