package couk.Adamki11s.Regios.Mutable;

import java.io.IOException;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import couk.Adamki11s.Regios.Regions.GlobalRegionManager;
import couk.Adamki11s.Regios.Regions.Region;

public class MutableEconomy {

	public void editForSale(Region r, boolean val) {
		FileConfiguration c = YamlConfiguration.loadConfiguration(r.getConfigFile() );
		c.set("Region.Economy.ForSale", val);
		r.setForSale(val);
		try {
			c.save(r.getConfigFile());
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
		FileConfiguration c = YamlConfiguration.loadConfiguration(r.getConfigFile());
		c.set("Region.Economy.Price", val);
		r.setSalePrice(val);
		try {
			c.save(r.getConfigFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String listRegionsForSale() {
		StringBuilder sb = new StringBuilder();
		for (Region r : GlobalRegionManager.getRegions()) {
			if (r.isForSale()) {
				sb.append(ChatColor.WHITE).append(r.getName()).append(ChatColor.YELLOW).append(" : ").append(r.getSalePrice()).append(ChatColor.BLUE + ", ");
			}
		}
		if (sb.toString().length() < 3) {
			return ChatColor.RED + "[Regios] No Regions for sale!";
		} else {
			return sb.toString();
		}
	}

}
