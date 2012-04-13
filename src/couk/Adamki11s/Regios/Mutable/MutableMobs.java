package couk.Adamki11s.Regios.Mutable;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import couk.Adamki11s.Regios.Regions.Region;

public class MutableMobs {
	
	public void editMobSpawn(Region r, boolean val){
		File file = r.getConfigFile();
		FileConfiguration c = YamlConfiguration.loadConfiguration(file);
		Map<String, Object> all = c.getValues(true);
		all.remove("Region.Other.MobSpawns");
		for(Entry<String, Object> entry : all.entrySet()){
			c.set(entry.getKey(), entry.getValue());
		}
		c.set("Region.Other.MobSpawns", val);
		r.setMobSpawns(val);
		try {
			c.save(r.getConfigFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void editMonsterSpawn(Region r, boolean val){
		File file = r.getConfigFile();
		FileConfiguration c = YamlConfiguration.loadConfiguration(file);
		Map<String, Object> all = c.getValues(true);
		all.remove("Region.Other.MonsterSpawns");
		for(Entry<String, Object> entry : all.entrySet()){
			c.set(entry.getKey(), entry.getValue());
		}
		c.set("Region.Other.MonsterSpawns", val);
		r.setMonsterSpawns(val);
		try {
			c.save(r.getConfigFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
