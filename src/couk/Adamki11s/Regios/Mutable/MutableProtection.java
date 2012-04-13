package couk.Adamki11s.Regios.Mutable;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import couk.Adamki11s.Regios.Regions.Region;

public class MutableProtection {
	
	public void editProtectBP(Region r){
		File file = r.getConfigFile();
		FileConfiguration c = YamlConfiguration.loadConfiguration(file);
		Map<String, Object> all = c.getValues(true);
		all.remove("Region.General.Protected.BlockPlace");
		for(Entry<String, Object> entry : all.entrySet()){
			c.set(entry.getKey(), entry.getValue());
		}
		c.set("Region.General.Protected.BlockPlace", true);
		r.set_protectionPlace(true);
		try {
			c.save(r.getConfigFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void editUnProtectBP(Region r){
		File file = r.getConfigFile();
		FileConfiguration c = YamlConfiguration.loadConfiguration(file);
		Map<String, Object> all = c.getValues(true);
		all.remove("Region.General.Protected.BlockPlace");
		for(Entry<String, Object> entry : all.entrySet()){
			c.set(entry.getKey(), entry.getValue());
		}
		c.set("Region.General.Protected.BlockPlace", false);
		r.set_protectionPlace(false);
		try {
			c.save(r.getConfigFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void editProtectBB(Region r){
		File file = r.getConfigFile();
		FileConfiguration c = YamlConfiguration.loadConfiguration(file);
		Map<String, Object> all = c.getValues(true);
		all.remove("Region.General.Protected.BlockBreak");
		for(Entry<String, Object> entry : all.entrySet()){
			c.set(entry.getKey(), entry.getValue());
		}
		c.set("Region.General.Protected.BlockBreak", true);
		r.set_protectionBreak(true);
		try {
			c.save(r.getConfigFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void editUnProtectBB(Region r){
		File file = r.getConfigFile();
		FileConfiguration c = YamlConfiguration.loadConfiguration(file);
		Map<String, Object> all = c.getValues(true);
		all.remove("Region.General.Protected.BlockBreak");
		for(Entry<String, Object> entry : all.entrySet()){
			c.set(entry.getKey(), entry.getValue());
		}
		c.set("Region.General.Protected.BlockBreak", false);
		r.set_protectionBreak(false);
		try {
			c.save(r.getConfigFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void editProtect(Region r){
		File file = r.getConfigFile();
		FileConfiguration c = YamlConfiguration.loadConfiguration(file);
		Map<String, Object> all = c.getValues(true);
		all.remove("Region.General.Protected.General");
		for(Entry<String, Object> entry : all.entrySet()){
			c.set(entry.getKey(), entry.getValue());
		}
		c.set("Region.General.Protected.General", true);
		r.set_protection(true);
		try {
			c.save(r.getConfigFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void editUnprotect(Region r){
		File file = r.getConfigFile();
		FileConfiguration c = YamlConfiguration.loadConfiguration(file);
		Map<String, Object> all = c.getValues(true);
		all.remove("Region.General.Protected.General");
		for(Entry<String, Object> entry : all.entrySet()){
			c.set(entry.getKey(), entry.getValue());
		}
		c.set("Region.General.Protected.General", false);
		r.set_protection(false);
		try {
			c.save(r.getConfigFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void editPreventEntry(Region r){
		File file = r.getConfigFile();
		FileConfiguration c = YamlConfiguration.loadConfiguration(file);
		Map<String, Object> all = c.getValues(true);
		all.remove("Region.General.PreventEntry");
		for(Entry<String, Object> entry : all.entrySet()){
			c.set(entry.getKey(), entry.getValue());
		}
		c.set("Region.General.PreventEntry", true);
		r.setPreventEntry(true);
		try {
			c.save(r.getConfigFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void editAllowEntry(Region r){
		File file = r.getConfigFile();
		FileConfiguration c = YamlConfiguration.loadConfiguration(file);
		Map<String, Object> all = c.getValues(true);
		all.remove("Region.General.PreventEntry");
		for(Entry<String, Object> entry : all.entrySet()){
			c.set(entry.getKey(), entry.getValue());
		}
		c.set("Region.General.PreventEntry", false);
		r.setPreventEntry(false);
		try {
			c.save(r.getConfigFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void editPreventExit(Region r){
		File file = r.getConfigFile();
		FileConfiguration c = YamlConfiguration.loadConfiguration(file);
		Map<String, Object> all = c.getValues(true);
		all.remove("Region.General.PreventExit");
		for(Entry<String, Object> entry : all.entrySet()){
			c.set(entry.getKey(), entry.getValue());
		}
		c.set("Region.General.PreventExit", true);
		r.setPreventExit(true);
		try {
			c.save(r.getConfigFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void editAllowExit(Region r){
		File file = r.getConfigFile();
		FileConfiguration c = YamlConfiguration.loadConfiguration(file);
		Map<String, Object> all = c.getValues(true);
		all.remove("Region.General.PreventExit");
		for(Entry<String, Object> entry : all.entrySet()){
			c.set(entry.getKey(), entry.getValue());
		}
		c.set("Region.General.PreventExit", false);
		r.setPreventExit(false);
		try {
			c.save(r.getConfigFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
