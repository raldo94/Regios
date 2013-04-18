package net.jzx7.regios.Mutable;

import java.io.IOException;

import net.jzx7.regios.regions.RegionManager;
import net.jzx7.regiosapi.regions.Region;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class MutableEconomy {
	
	final static RegionManager rm = new RegionManager();

	public void editForSale(Region r, boolean val) {
		FileConfiguration c = YamlConfiguration.loadConfiguration(rm.getConfigFile(r) );
		c.set("Region.Economy.ForSale", val);
		r.setForSale(val);
		try {
			c.save(rm.getConfigFile(r));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void editSalePrice(Region r, int val) {
		if (val > 0) {
			editForSale(r, true);// Forces the region to be for sale if a price has been set.
		} else {
			editForSale(r, false);
		}
		FileConfiguration c = YamlConfiguration.loadConfiguration(rm.getConfigFile(r));
		c.set("Region.Economy.Price", val);
		r.setSalePrice(val);
		try {
			c.save(rm.getConfigFile(r));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String listRegionsForSale() {
		StringBuilder sb = new StringBuilder();
		for (Region r : rm.getRegions()) {
			if (r.isForSale()) {
				sb.append("<WHITE>").append(r.getName()).append("<YELLOW>").append(" : ").append(r.getSalePrice()).append("<BLUE>" + ", ");
			}
		}
		if (sb.toString().length() < 3) {
			return "<RED>" + "[Regios] No Regions for sale!";
		} else {
			return sb.toString();
		}
	}

}
