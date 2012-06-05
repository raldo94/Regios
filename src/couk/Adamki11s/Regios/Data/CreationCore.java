package couk.Adamki11s.Regios.Data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.Logger;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class CreationCore {

	private final File root = new File("plugins" + File.separator + "Regios")
				, db_root = new File(root + File.separator + "Database")
				, config_root = new File(root + File.separator + "Configuration")
				, backup_root = new File(root + File.separator + "Backups")
				, version_root = new File(root + File.separator + "Versions")
				, updates = new File("plugins" + File.separator + "Update")
				, other = new File(root + File.separator + "Other")
				, shares = new File(root + File.separator + "Blueprints")
				, depend = new File(root + File.separator + "Dependancies")
				, restrict = new File(root + File.separator + "Restrictions");

	private final Logger log = Logger.getLogger("Minecraft.Regios");
	private final String prefix = "[Regios]";

	public void setup() throws IOException {
		directories();
		configuration();
		new LoaderCore().setup();
	}

	private void directories() throws IOException {
		log.info(prefix + " Checking directories.");
		boolean flawless = true;

		if (!updates.exists()) {
			flawless = false;
			updates.mkdir();
			log.info(prefix + " Creating directory @_root/Update");
		}
		if (!root.exists()) {
			flawless = false;
			root.mkdir();
			log.info(prefix + " Creating directory @_root/plugins/Regios");
		}

		if (!db_root.exists()) {
			flawless = false;
			db_root.mkdir();
			log.info(prefix + " Creating directory @_root/plugins/Regios/Database");
		}

		if (!config_root.exists()) {
			flawless = false;
			config_root.mkdir();
			log.info(prefix + " Creating directory @_root/plugins/Regios/Configuration");
		}

		if (!backup_root.exists()) {
			flawless = false;
			backup_root.mkdir();
			log.info(prefix + " Creating directory @_root/plugins/Regios/Backups");
		}

		if (!other.exists()) {
			flawless = false;
			other.mkdir();
			log.info(prefix + " Creating directory @_root/plugins/Regios/Other");
		}

		if (depend.exists()) {
			depend.delete();
			log.info(prefix + " Deleting directory @_root/plugins/Regios/Dependancies");
		}
		
		if (!restrict.exists()) {
			flawless = false;
			restrict.mkdir();
			log.info(prefix + " Creating directory @_root/plugins/Regios/Restrictions");
		}

		if (!shares.exists()) {
			flawless = false;
			shares.mkdir();
			log.info(prefix + " Creating directory @_root/plugins/Regios/Blueprints");
		}

		if (!(new File(other + File.separator + "Pending").exists())) {
			flawless = false;
			new File(other + File.separator + "Pending").mkdir();
			log.info(prefix + " Creating directory @_root/plugins/Regios/Other/Pending");
		}

		if (!version_root.exists()) {
			flawless = false;
			version_root.mkdir();
			log.info(prefix + " Creating directory @_root/plugins/Regios/Versions");
			File vtt = new File(version_root + File.separator + "Version Tracker");
			File readme = new File(version_root + File.separator + "README.txt");
			readme.createNewFile();
			FileOutputStream fos = new FileOutputStream(readme);
			PrintWriter pw = new PrintWriter(fos);
			pw.println("Do not modify or delete any files within the Version Tracker folder!");
			pw.println("They are used to check version changes and make any necessary updates to the database.");
			pw.println("Editing these files could cause problems.");
			pw.flush();
			pw.close();
			fos.flush();
			fos.close();
			if (!vtt.exists()) {
				vtt.mkdir();
			}
		}

		if (!flawless) {
			log.info(prefix + " Required directories created successfully!");
		}
		log.info(prefix + " Directory check completed.");
	}

	private void configuration() throws IOException {
		log.info(prefix + " Checking configuration files.");
		boolean flawless = true;
		File defaultregions = new File(config_root + File.separator + "DefaultRegion.config")
		   , generalconfig = new File(config_root + File.separator + "GeneralSettings.config")
		   , restrictionconfig = new File(config_root + File.separator + "Restrictions.config");

		if (!generalconfig.exists()) {
			log.info(prefix + " Creating general configuration.");
			generalconfig.createNewFile();
			FileConfiguration c = YamlConfiguration.loadConfiguration(generalconfig);
			c.set("Region.UseEconomy", false);
			c.set("Region.UseWorldEdit", false);
			c.set("Region.LogsEnabled", true);
			c.set("Region.Tools.Setting.ID", Material.WOOD_AXE.getId());
			c.save(generalconfig);
		}
		if (!defaultregions.exists()) {
			log.info(prefix + " Creating default region settings configuration.");
			defaultregions.createNewFile();
			FileConfiguration c = YamlConfiguration.loadConfiguration(defaultregions);
			c.set("DefaultSettings.General.Protected.BlockBreak", false);
			c.set("DefaultSettings.General.Protected.BlockPlace", false);
			c.set("DefaultSettings.General.Protected.General", false);
			c.set("DefaultSettings.General.PreventEntry", false);
			c.set("DefaultSettings.General.PreventExit", false);
			c.set("DefaultSettings.General.MobSpawns", true);
			c.set("DefaultSettings.General.MonsterSpawns", true);
			c.set("DefaultSettings.General.PvP", false);
			c.set("DefaultSettings.General.DoorsLocked", false);
			c.set("DefaultSettings.General.ChestsLocked", false);
			c.set("DefaultSettings.General.DispenserLocked", false);
			c.set("DefaultSettings.General.PreventInteraction", false);

			c.set("DefaultSettings.Protection.FireProtection", false);
			c.set("DefaultSettings.Protection.FireSpread", true);
			c.set("DefaultSettings.Protection.TNTEnabled", true);

			c.set("DefaultSettings.Messages.WelcomeMessage", "<BGREEN>Welcome to <BLUE>[NAME] <BGREEN>owned by <YELLOW>[OWNER]");
			c.set("DefaultSettings.Messages.LeaveMessage", "<RED>You left <BLUE>[NAME] <RED>owned by <YELLOW>[OWNER]");
			c.set("DefaultSettings.Messages.ProtectionMessage", "<RED>This region is protected by owner <YELLOW>[OWNER]!");
			c.set("DefaultSettings.Messages.PreventEntryMessage", "<RED>You cannot enter this region : <BLUE>[NAME]");
			c.set("DefaultSettings.Messages.PreventExitMessage", "<RED>You cannot exit this region : <BLUE>[NAME]");
			c.set("DefaultSettings.Messages.ShowWelcomeMessage", true);
			c.set("DefaultSettings.Messages.ShowLeaveMessage", true);
			c.set("DefaultSettings.Messages.ShowProtectionMessage", true);
			c.set("DefaultSettings.Messages.ShowPreventEntryMessage", true);
			c.set("DefaultSettings.Messages.ShowPreventExitMessage", true);
			c.set("DefaultSettings.Messages.ShowPvPWarning", true);

			c.set("DefaultSettings.Permissions.TemporaryCache.AddNodes", "");
			c.set("DefaultSettings.Permissions.PermanentCache.AddNodes", "");
			c.set("DefaultSettings.Permissions.PermanentCache.RemoveNodes", "");

			c.set("DefaultSettings.Other.LSPS", 0);
			c.set("DefaultSettings.Other.HealthEnabled", true);
			c.set("DefaultSettings.Other.HealthRegenRate", 0);
			c.set("DefaultSettings.Other.VelocityWarp", 0);

			c.set("DefaultSettings.Modes.ProtectionMode", "WHITELIST");
			c.set("DefaultSettings.Modes.PreventEntryMode", "WHITELIST");
			c.set("DefaultSettings.Modes.PreventExitMode", "WHITELIST");
			c.set("DefaultSettings.Modes.ItemControlMode", "WHITELIST");

			c.set("DefaultSettings.Inventory.PermWipeOnEnter", false);
			c.set("DefaultSettings.Inventory.PermWipeOnExit", false);
			c.set("DefaultSettings.Inventory.WipeAndCacheOnEnter", false);
			c.set("DefaultSettings.Inventory.WipeAndCacheOnExit", false);

			c.set("DefaultSettings.Command.ForceCommand", false);
			c.set("DefaultSettings.Command.CommandSet", "");

			c.set("DefaultSettings.Economy.ForSale", false);
			c.set("DefaultSettings.Economy.SalePrice", 0);

			c.set("DefaultSettings.Password.PasswordProtection", false);
			c.set("DefaultSettings.Password.Password", "NA");
			c.set("DefaultSettings.Password.PasswordMessage", "<RED>Authentication required! Do /regios auth <password>");
			c.set("DefaultSettings.Password.PasswordSuccessMessage", "Authentication successful!");

			c.set("DefaultSettings.Spout.SpoutWelcomeIconID", Material.GRASS.getId());
			c.set("DefaultSettings.Spout.SpoutLeaveIconID", Material.DIRT.getId());
			c.set("DefaultSettings.Spout.Sound.PlayCustomMusic", false);
			c.set("DefaultSettings.Spout.Sound.CustomMusicURL", "");

			c.set("DefaultSettings.General.PlayerCap.Cap", 0);

			c.set("DefaultSettings.Block.BlockForm.Enabled", true);
			
			c.set("DefaultSettings.GameMode.Type", "SURVIVAL");
			c.set("DefaultSettings.GameMode.Change", false);

			c.save(defaultregions);
		}
		if (!restrictionconfig.exists()) {
			log.info(prefix + " Creating restriction configuration.");
			restrictionconfig.createNewFile();
			FileConfiguration c = YamlConfiguration.loadConfiguration(restrictionconfig);
			c.set("Restrictions.Size", size);
			c.set("Restrictions.Count", count);
			c.save(restrictionconfig);
		}
		if (!flawless) {
			log.info(prefix + " Required configurations created successfully!");
		}
		log.info(prefix + " Configuration check completed.");
	}
	
	ArrayList<String> size = new ArrayList<String>() {
		private static final long serialVersionUID = -6829028729172255886L;
	{
		add("regios.restrictions.size10");
		add("regios.restrictions.size20");
		add("regios.restrictions.size30");
		add("regios.restrictions.size40");
		add("regios.restrictions.size50");
		add("regios.restrictions.size60");
		add("regios.restrictions.size70");
		add("regios.restrictions.size75");
		add("regios.restrictions.size80");
		add("regios.restrictions.size90");
		add("regios.restrictions.size100");
		add("regios.restrictions.size125");
		add("regios.restrictions.size150");
		add("regios.restrictions.size175");
		add("regios.restrictions.size200");
		
	}};
	
	ArrayList<String> count = new ArrayList<String>() {
		private static final long serialVersionUID = 1373000493169672932L;
	{
		add("regios.restrictions.1");
		add("regios.restrictions.2");
		add("regios.restrictions.3");
		add("regios.restrictions.5");
		add("regios.restrictions.10");
		add("regios.restrictions.20");
		add("regios.restrictions.30");
		add("regios.restrictions.40");
		add("regios.restrictions.50");
		add("regios.restrictions.60");
		add("regios.restrictions.70");
		add("regios.restrictions.80");
		add("regios.restrictions.90");
		add("regios.restrictions.100");
		add("regios.restrictions.250");
		add("regios.restrictions.500");
		add("regios.restrictions.1000");
		
	}};


}
