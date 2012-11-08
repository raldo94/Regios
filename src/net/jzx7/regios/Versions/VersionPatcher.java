package net.jzx7.regios.Versions;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Map;
import java.util.Map.Entry;

import net.jzx7.regios.Data.ConfigurationData;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;


public class VersionPatcher {

	private static final File root = new File("plugins" + File.separator + "Regios"), config_root = new File(root + File.separator + "Configuration");

	static final File patch4057F = new File(root + File.separator + "Versions" + File.separator + "Version Tracker" + File.separator + "4.0.57.rv");
	static final File patch4063F = new File(root + File.separator + "Versions" + File.separator + "Version Tracker" + File.separator + "4.0.63.rv");
	static final File patch4071F = new File(root + File.separator + "Versions" + File.separator + "Version Tracker" + File.separator + "4.0.71.rv");
	static final File patch5021F = new File(root + File.separator + "Versions" + File.separator + "Version Tracker" + File.separator + "5.0.21.rv");
	static final File patch5051F = new File(root + File.separator + "Versions" + File.separator + "Version Tracker" + File.separator + "5.0.51.rv");
	static final File patch590F = new File(root + File.separator + "Versions" + File.separator + "Version Tracker" + File.separator + "5.9.0.rv");

	public static void runPatch(String version) throws IOException {
		if (version.equalsIgnoreCase("5.9.3")) {
			if (!patch4057F.exists()) {
				patch4057(version);
				patch4057F.createNewFile();
			}
			if (!patch4063F.exists()) {
				patch4063(version);
				patch4063F.createNewFile();
			}
			if (!patch4071F.exists()) {
				patch4071(version);
				patch4071F.createNewFile();
			}
			if (!patch5021F.exists()) {
				patch5021(version);
				patch5021F.createNewFile();
			}
			if (!patch5051F.exists()) {
				patch5051(version);
				patch5051F.createNewFile();
			}
			if (!patch590F.exists()) {
				patch590(version);
				patch590F.createNewFile();
			}
		}
	}

	static final PrintStream outstream = System.out;

	private static void patch4057(String v) {
		outstream.println("[Regios][Patch] Patching files for version : " + v);
		outstream.println("[Regios][Patch] Modifying general configuration file...");
		File generalconfig = new File(config_root + File.separator + "GeneralSettings.config");
		FileConfiguration c = YamlConfiguration.loadConfiguration(generalconfig);
		String value = c.getString("Regios.Economy", "NONE");
		int oldID = c.getInt("Region.Tools.Setting.ID", 271);
		c = YamlConfiguration.loadConfiguration(generalconfig);
		c.set("Region.LogsEnabled", true);
		c.set("Region.Tools.Setting.ID", oldID);
		c.set("Region.Economy", value);
		try {
			c.save(generalconfig);
		} catch (IOException e) {
			e.printStackTrace();
		}
		ConfigurationData.logs = true;
		outstream.println("[Regios][Patch] Region.LogsEnabled property added.");
		outstream.println("[Regios][Patch] Region.Economy property modified from Regios.Economy.");
		outstream.println("[Regios][Patch] Patch completed!");
	}

	private static void patch4063(String v) {
		outstream.println("[Regios][Patch] Patching files for version : " + v);
		outstream.println("[Regios][Patch] Modifying 'DefaultRegion' configuration file...");
		File f = new File("plugins" + File.separator + "Regios" + File.separator + "Configuration" + File.separator + "DefaultRegion.config");
		FileConfiguration c = YamlConfiguration.loadConfiguration(f);
		Map<String, Object> map = c.getValues(true);
		for (Entry<String, Object> ent : map.entrySet()) {
			c.set(ent.getKey(), ent.getValue());
		}
		c.set("DefaultSettings.Economy.ForSale", false);
		c.set("DefaultSettings.Economy.SalePrice", 0);
		try {
			c.save(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
		outstream.println("[Regios][Patch] Economy.ForSale property added.");
		outstream.println("[Regios][Patch] Economy.SalePrice property added.");
		outstream.println("[Regios][Patch] Patch completed!");
	}

	private static void patch4071(String v) {
		outstream.println("[Regios][Patch] Patching files for version : " + v);
		outstream.println("[Regios][Patch] Modifying existing region files...");
		File fff = new File("plugins" + File.separator + "Regios" + File.separator + "Database");
		for (File tmp : fff.listFiles()) {
			if (tmp.isDirectory()) {
				File f = new File(tmp + File.separator + tmp.getName() + ".rz");
				FileConfiguration c = YamlConfiguration.loadConfiguration(f);
				Map<String, Object> map = c.getValues(true);
				for (Entry<String, Object> ent : map.entrySet()) {
					c.set(ent.getKey(), ent.getValue());
				}
				c.set("Region.Spout.Welcome.Enabled",map.get("Region.Messages.ShowWelcomeMessage"));
				c.set("Region.Spout.Leave.Enabled", map.get("Region.Messages.ShowLeaveMessage"));
				try {
					c.save(f);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		outstream.println("[Regios][Patch] Spout welcome/leave popup toggle property added!");
		outstream.println("[Regios][Patch] Patch completed!");
	}

	private static void patch5021(String v) {
		outstream.println("[Regios][Patch] Patching files for version : " + v);
		outstream.println("[Regios][Patch] Modifying general configuration file...");
		File generalconfig = new File(config_root + File.separator + "GeneralSettings.config");
		FileConfiguration c = YamlConfiguration.loadConfiguration(generalconfig);
		String value = c.getString("Region.Economy", "NONE");
		int oldID = c.getInt("Region.Tools.Setting.ID", 271);
		boolean oldLog = c.getBoolean("Region.LogsEnabled", true);
		c = YamlConfiguration.loadConfiguration(generalconfig);
		c.set("Region.LogsEnabled", oldLog);
		c.set("Region.Tools.Setting.ID", oldID);
		c.set("Region.Economy", null);
		if(value.equalsIgnoreCase("NONE")) {
			c.set("Region.UseEconomy", false);
		} else {
			c.set("Region.UseEconomy", true);
		}
		try {
			c.save(generalconfig);
		} catch (IOException e) {
			e.printStackTrace();
		}
		ConfigurationData.logs = true;
		outstream.println("[Regios][Patch] Region.UseEconomy property modified from Region.Economy.");
		outstream.println("[Regios][Patch] Patch completed!");
	}

	private static void patch5051(String v) {
		outstream.println("[Regios][Patch] Patching files for version : " + v);
		outstream.println("[Regios][Patch] Modifying general configuration file...");
		File generalconfig = new File(config_root + File.separator + "GeneralSettings.config");
		FileConfiguration c = YamlConfiguration.loadConfiguration(generalconfig);
		boolean value = c.getBoolean("Region.UseWorldEdit", false);
		c = YamlConfiguration.loadConfiguration(generalconfig);
		c.set("Region.UseWorldEdit", value);
		try {
			c.save(generalconfig);
		} catch (IOException e) {
			e.printStackTrace();
		}

		outstream.println("[Regios][Patch] Modifying World configuration files...");
		File worldfolder = new File(config_root + File.separator + "WorldConfigurations" + File.separator);
		for (File w : worldfolder.listFiles())
		{
			int pos = w.getName().lastIndexOf(".");
			FileConfiguration wc = YamlConfiguration.loadConfiguration(w);
			boolean wvalue = wc.getBoolean(w.getName().substring(0, pos) + ".Protection.FireSpreadEnabled", true);
			boolean dragon = wc.getBoolean(w.getName().substring(0, pos) + ".Protection.DragonProtect", true);
			boolean enderman = wc.getBoolean(w.getName().substring(0, pos) + ".Protection.EndermanBlock", false);
			wc = YamlConfiguration.loadConfiguration(w);
			wc.set(w.getName().substring(0, pos) + ".Protection.FireSpreadEnabled", wvalue);
			wc.set(w.getName().substring(0, pos) + ".Protection.DragonProtect", dragon);
			wc.set(w.getName().substring(0, pos) + ".Protection.EndermanBlock", enderman);
			try {
				wc.save(w);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		outstream.println("[Regios][Patch] Modifying DefaultRegion file...");
		File dr = new File(config_root + File.separator + "DefaultRegion.config");
		FileConfiguration drc = YamlConfiguration.loadConfiguration(dr);
		boolean drvalue = drc.getBoolean("DefaultSettings.General.DispensersLocked", false);
		boolean drender = drc.getBoolean("DefaultSettings.General.EndermanBlock", false);
		String drgmt = drc.getString("DefaultSettings.GameMode.Type", "SURVIVAL");
		boolean drgmc = drc.getBoolean("DefaultSettings.GameMode.Change", false);
		drc.set("DefaultSettings.General.DispensersLocked", drvalue);
		drc.set("DefaultSettings.Protection.EndermanBlock", drender);
		drc.set("DefaultSettings.GameMode.Type", drgmt);
		drc.set("DefaultSettings.GameMode.Change", drgmc);
		try {
			drc.save(dr);
		} catch (IOException e) {
			e.printStackTrace();
		}
		ConfigurationData.logs = true;
		outstream.println("[Regios][Patch] Region.UseWorldEdit property added to generalsettings.config.");
		outstream.println("[Regios][Patch] FireSpreadEnabled, DragonProtect, and EndermanBlock added to world configurations.");
		outstream.println("[Regios][Patch] Added DispensersLocked, GameMode.Type, and GameMode.Change to DefaultRegion.config");
		outstream.println("[Regios][Patch] Patch completed!");
	}

	private static void patch590(String v) {
		outstream.println("[Regios][Patch] Patching files for version : " + v);
		outstream.println("[Regios][Patch] Modifying World configuration files...");
		File worldfolder = new File(config_root + File.separator + "WorldConfigurations" + File.separator);
		for (File w : worldfolder.listFiles())
		{
			int pos = w.getName().lastIndexOf(".");
			FileConfiguration wc = YamlConfiguration.loadConfiguration(w);
			boolean tnt = wc.getBoolean(w.getName().substring(0, pos) + ".Protection.TNTEnabled", true);
			boolean portal = wc.getBoolean(w.getName().substring(0, pos) + ".Protection.DragonCreatesPortal", true);
			wc = YamlConfiguration.loadConfiguration(w);
			if (tnt) {
				wc.set(w.getName().substring(0, pos) + ".Protection.ExplosionsEnabled", tnt);
				wc.set(w.getName().substring(0, pos) + ".Protection.DragonCreatesPortal", portal);
				wc.set(w.getName().substring(0, pos) + ".Mobs.Creeper.DoesExplode", null);
				wc.set(w.getName().substring(0, pos) + ".Mobs.Creeper", null);
				wc.set(w.getName().substring(0, pos) + ".Protection.TNTEnabled", null);
			}
			try {
				wc.save(w);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		outstream.println("[Regios][Patch] Modifying DefaultRegion file...");
		File dr = new File(config_root + File.separator + "DefaultRegion.config");
		FileConfiguration drc = YamlConfiguration.loadConfiguration(dr);
		boolean tnt = drc.getBoolean("DefaultSettings.General.Protection.TNTEnabled", true);
		drc.set("DefaultSettings.General.Protection.ExplosionsEnabled", tnt);
		drc.set("DefaultSettings.General.Protection.TNTEnabled", null);
		try {
			drc.save(dr);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		File fff = new File("plugins" + File.separator + "Regios" + File.separator + "Database");
		for (File tmp : fff.listFiles()) {
			if (tmp.isDirectory()) {
				File f = new File(tmp + File.separator + tmp.getName() + ".rz");
				FileConfiguration c = YamlConfiguration.loadConfiguration(f);
				boolean tnt1 = c.getBoolean("Region.General.TNTEnabled" ,true);
				c.set("Region.General.ExplosionsEnabled",tnt1);
				c.set("Region.General.TNTEnabled", null);
				try {
					c.save(f);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		ConfigurationData.logs = true;
		outstream.println("[Regios][Patch] TNTEnabled changed to ExplosionsEnabled in world config");
		outstream.println("[Regios][Patch] Creepers.DoesExplode removed from world config");
		outstream.println("[Regios][Patch] DragonCreatesPortal added to world config");
		outstream.println("[Regios][Patch] Patch completed!");
	}
}
