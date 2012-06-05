package couk.Adamki11s.Regios.Mutable;

import java.io.File;
import java.io.IOException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import couk.Adamki11s.Regios.Commands.AdministrationCommands;
import couk.Adamki11s.Regios.CustomEvents.RegionDeleteEvent;
import couk.Adamki11s.Regios.CustomEvents.RegionModifyEvent;
import couk.Adamki11s.Regios.CustomExceptions.RegionExistanceException;
import couk.Adamki11s.Regios.Data.LoaderCore;
import couk.Adamki11s.Regios.Regions.GlobalRegionManager;
import couk.Adamki11s.Regios.Regions.Region;
import couk.Adamki11s.Regios.Restrictions.RestrictionParameters;
import couk.Adamki11s.Regios.Scheduler.LogRunner;

public class MutableModification {

	final static LoaderCore lc = new LoaderCore();

	private String convertLocation(Location l) {
		return l.getWorld().getName() + "," + l.getX() + "," + l.getY() + "," + l.getZ();
	}
	
	private boolean canChangeSize(Location smaller, Location bigger, Player p) {
		double width = Math.max(smaller.getX(), bigger.getX()) - Math.min(smaller.getX(), bigger.getX())
				, height = Math.max(smaller.getY(), bigger.getY()) - Math.min(smaller.getY(), bigger.getY())
				, length = Math.max(smaller.getZ(), bigger.getZ()) - Math.min(smaller.getZ(), bigger.getZ());
			
			RestrictionParameters params = RestrictionParameters.getRestrictions(p);

			if(width > params.getRegionWidthLimit()){
				p.sendMessage(ChatColor.RED + "[Regios] You cannot change a region to this width!");
				p.sendMessage(ChatColor.RED + "[Regios] Maximum width : " + ChatColor.BLUE + params.getRegionWidthLimit() + ChatColor.RED + ", your width : " + ChatColor.BLUE + width);
				return false;
			}

			if(height > params.getRegionHeightLimit()){
				p.sendMessage(ChatColor.RED + "[Regios] You cannot change a region to this height!");
				p.sendMessage(ChatColor.RED + "[Regios] Maximum height : " + ChatColor.BLUE + params.getRegionHeightLimit() + ChatColor.RED + ", your height : " + ChatColor.BLUE + height);
				return false;
			}

			if(length > params.getRegionLengthLimit()){
				p.sendMessage(ChatColor.RED + "[Regios] You cannot change a region to this length!");
				p.sendMessage(ChatColor.RED + "[Regios] Maximum length : " + ChatColor.BLUE + params.getRegionLengthLimit() + ChatColor.RED + ", your length : " + ChatColor.BLUE + length);
				return false;
			}
			return true;
	}

	public void editExpandUp(Region r, int value, Player p) {
		Location smaller = new Location(r.getL1().getWorld(), Math.min(r.getL1().getX(), r.getL2().getX())
				, Math.min(r.getL1().getY(), r.getL2().getY())
				, Math.min(r.getL1().getZ(), r.getL2().getZ()))
		, bigger = new Location(r.getL1().getWorld(), Math.max(r.getL1().getX(), r.getL2().getX())
				, Math.max(r.getL1().getY(), r.getL2().getY())
				, Math.max(r.getL1().getZ(), r.getL2().getZ()));
		bigger.add(0, value, 0);
		
		if (!canChangeSize(smaller, bigger, p))
		{
			return;
		}
		
		FileConfiguration c = YamlConfiguration.loadConfiguration(r.getConfigFile());
		c.set("Region.Essentials.Points.Point1", convertLocation(smaller));
		c.set("Region.Essentials.Points.Point2", convertLocation(bigger));
		r.setL1(smaller);
		r.setL2(bigger);
		try {
			c.save(r.getConfigFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
		RegionModifyEvent event = new RegionModifyEvent("RegionModifyEvent");
		event.setProperties(r);
		Bukkit.getServer().getPluginManager().callEvent(event);
	}

	public void editExpandDown(Region r, int value, Player p) {
		Location smaller = new Location(r.getL1().getWorld(), Math.min(r.getL1().getX(), r.getL2().getX())
				, Math.min(r.getL1().getY(), r.getL2().getY())
				, Math.min(r.getL1().getZ(), r.getL2().getZ()))
		, bigger = new Location(r.getL1().getWorld(), Math.max(r.getL1().getX(), r.getL2().getX())
				, Math.max(r.getL1().getY(), r.getL2().getY())
				, Math.max(r.getL1().getZ(), r.getL2().getZ()));
		smaller.add(0, value, 0);
		
		if (!canChangeSize(smaller, bigger, p))
		{
			return;
		}
		
		FileConfiguration c = YamlConfiguration.loadConfiguration(r.getConfigFile());
		c.set("Region.Essentials.Points.Point1", convertLocation(smaller));
		c.set("Region.Essentials.Points.Point2", convertLocation(bigger));
		r.setL1(smaller);
		r.setL2(bigger);
		try {
			c.save(r.getConfigFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
		RegionModifyEvent event = new RegionModifyEvent("RegionModifyEvent");
		event.setProperties(r);
		Bukkit.getServer().getPluginManager().callEvent(event);
	}

	public void editShrinkUp(Region r, int value, Player p) {
		Location smaller = new Location(r.getL1().getWorld(), Math.min(r.getL1().getX(), r.getL2().getX())
				, Math.min(r.getL1().getY(), r.getL2().getY())
				, Math.min(r.getL1().getZ(), r.getL2().getZ()))
		, bigger = new Location(r.getL1().getWorld(), Math.max(r.getL1().getX(), r.getL2().getX())
				, Math.max(r.getL1().getY(), r.getL2().getY())
				, Math.max(r.getL1().getZ(), r.getL2().getZ()));
		smaller.subtract(0, value, 0);
		
		if (!canChangeSize(smaller, bigger, p))
		{
			return;
		}
		
		FileConfiguration c = YamlConfiguration.loadConfiguration(r.getConfigFile());
		c.set("Region.Essentials.Points.Point1", convertLocation(smaller));
		c.set("Region.Essentials.Points.Point2", convertLocation(bigger));
		r.setL1(smaller);
		r.setL2(bigger);
		try {
			c.save(r.getConfigFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
		RegionModifyEvent event = new RegionModifyEvent("RegionModifyEvent");
		event.setProperties(r);
		Bukkit.getServer().getPluginManager().callEvent(event);
	}

	public void editShrinkDown(Region r, int value, Player p) {
		Location smaller = new Location(r.getL1().getWorld(), Math.min(r.getL1().getX(), r.getL2().getX())
				, Math.min(r.getL1().getY(), r.getL2().getY())
				, Math.min(r.getL1().getZ(), r.getL2().getZ()))
		, bigger = new Location(r.getL1().getWorld(), Math.max(r.getL1().getX(), r.getL2().getX())
				, Math.max(r.getL1().getY(), r.getL2().getY())
				, Math.max(r.getL1().getZ(), r.getL2().getZ()));
		bigger.subtract(0, value, 0);
		
		if (!canChangeSize(smaller, bigger, p))
		{
			return;
		}
		
		FileConfiguration c = YamlConfiguration.loadConfiguration(r.getConfigFile());
		c.set("Region.Essentials.Points.Point1", convertLocation(smaller));
		c.set("Region.Essentials.Points.Point2", convertLocation(bigger));
		r.setL1(smaller);
		r.setL2(bigger);
		try {
			c.save(r.getConfigFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
		RegionModifyEvent event = new RegionModifyEvent("RegionModifyEvent");
		event.setProperties(r);
		Bukkit.getServer().getPluginManager().callEvent(event);
	}

	public void editExpandMax(Region r, Player p) {
		Location smaller = new Location(r.getL1().getWorld(), Math.min(r.getL1().getX(), r.getL2().getX())
				, Math.min(r.getL1().getY(), r.getL2().getY())
				, Math.min(r.getL1().getZ(), r.getL2().getZ()))
		, bigger = new Location(r.getL1().getWorld(), Math.max(r.getL1().getX(), r.getL2().getX())
				, Math.max(r.getL1().getY(), r.getL2().getY())
				, Math.max(r.getL1().getZ(), r.getL2().getZ()));
		smaller.setY(0);
		bigger.setY(r.getWorld().getMaxHeight());
		
		if (!canChangeSize(smaller, bigger, p))
		{
			return;
		}
		
		FileConfiguration c = YamlConfiguration.loadConfiguration(r.getConfigFile());
		c.set("Region.Essentials.Points.Point1", convertLocation(smaller));
		c.set("Region.Essentials.Points.Point2", convertLocation(bigger));
		r.setL1(smaller);
		r.setL2(bigger);
		try {
			c.save(r.getConfigFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
		RegionModifyEvent event = new RegionModifyEvent("RegionModifyEvent");
		event.setProperties(r);
		Bukkit.getServer().getPluginManager().callEvent(event);
	}

	public void editModifyPoints(Region r, Location l1, Location l2, Player p) {
		if (!canChangeSize(l1, l2, p))
		{
			return;
		}
		
		FileConfiguration c = YamlConfiguration.loadConfiguration(r.getConfigFile());
		c.set("Region.Essentials.Points.Point1", convertLocation(l1));
		c.set("Region.Essentials.Points.Point2", convertLocation(l2));
		r.setL1(l1);
		r.setL2(l2);
		try {
			c.save(r.getConfigFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
		RegionModifyEvent event = new RegionModifyEvent("RegionModifyEvent");
		event.setProperties(r);
		Bukkit.getServer().getPluginManager().callEvent(event);
	}

	public void editExpandOut(Region r, int expand, Player p) {
		Location smaller = new Location(r.getL1().getWorld(), Math.min(r.getL1().getX(), r.getL2().getX()), Math.min(r.getL1().getY(), r.getL2().getY()), Math.min(r.getL1()
				.getZ(), r.getL2().getZ()));
		Location bigger = new Location(r.getL1().getWorld(), Math.max(r.getL1().getX(), r.getL2().getX()), Math.max(r.getL1().getY(), r.getL2().getY()), Math.max(r.getL1()
				.getZ(), r.getL2().getZ()));
		smaller.subtract(expand, 0, expand);
		bigger.add(expand, 0, expand);
		
		if (!canChangeSize(smaller, bigger, p))
		{
			return;
		}
		
		FileConfiguration c = YamlConfiguration.loadConfiguration(r.getConfigFile());
		c.set("Region.Essentials.Points.Point1", convertLocation(smaller));
		c.set("Region.Essentials.Points.Point2", convertLocation(bigger));
		r.setL1(smaller.getWorld(), smaller.getBlockX(), smaller.getBlockY(), smaller.getBlockZ());
		r.setL2(bigger.getWorld(), bigger.getBlockX(), bigger.getBlockY(), bigger.getBlockZ());
		try {
			c.save(r.getConfigFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
		RegionModifyEvent event = new RegionModifyEvent("RegionModifyEvent");
		event.setProperties(r);
		Bukkit.getServer().getPluginManager().callEvent(event);
	}

	public void editShrinkIn(Region r, int shrink, Player p) {
		Location smaller = new Location(r.getL1().getWorld(), Math.min(r.getL1().getX(), r.getL2().getX()), Math.min(r.getL1().getY(), r.getL2().getY()), Math.min(r.getL1()
				.getZ(), r.getL2().getZ()));
		Location bigger = new Location(r.getL1().getWorld(), Math.max(r.getL1().getX(), r.getL2().getX()), Math.max(r.getL1().getY(), r.getL2().getY()), Math.max(r.getL1()
				.getZ(), r.getL2().getZ()));
		smaller.add(shrink, 0, shrink);
		bigger.subtract(shrink, 0, shrink);
		
		if (!canChangeSize(smaller, bigger, p))
		{
			return;
		}
		
		FileConfiguration c = YamlConfiguration.loadConfiguration(r.getConfigFile());
		c.set("Region.Essentials.Points.Point1", convertLocation(smaller));
		c.set("Region.Essentials.Points.Point2", convertLocation(bigger));
		r.setL1(smaller.getWorld(), smaller.getBlockX(), smaller.getBlockY(), smaller.getBlockZ());
		r.setL2(bigger.getWorld(), bigger.getBlockX(), bigger.getBlockY(), bigger.getBlockZ());
		try {
			c.save(r.getConfigFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
		RegionModifyEvent event = new RegionModifyEvent("RegionModifyEvent");
		event.setProperties(r);
		Bukkit.getServer().getPluginManager().callEvent(event);
	}

	public void editRename(Region r, String new_name, Player p) {

		if (GlobalRegionManager.getRegion(new_name) != null) {
			p.sendMessage(ChatColor.RED + "[Regios] The region " + ChatColor.BLUE + new_name + ChatColor.RED + " already exists!");
			return;
		}

		LogRunner.log.remove(r);

		FileConfiguration c = YamlConfiguration.loadConfiguration(r.getConfigFile());
		c.set("Region.Essentials.Name", new_name);
		try {
			c.save(r.getConfigFile());
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		r.getConfigFile().renameTo(new File(r.getDirectory() + File.separator + new_name + ".rz"));

		r.getDirectory().renameTo(new File("plugins" + File.separator + "Regios" + File.separator + "Database" + File.separator + new_name));

		new AdministrationCommands().reloadRegions(p);
	}

	public static void renameRegion(Region r, String new_name) throws RegionExistanceException {

		if (GlobalRegionManager.getRegion(new_name) != null) {
			throw new RegionExistanceException(new_name);
		}

		LogRunner.log.remove(r);

		FileConfiguration c = YamlConfiguration.loadConfiguration(r.getConfigFile());
		c.set("Region.Essentials.Name", new_name);
		try {
			c.save(r.getConfigFile());
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		r.getConfigFile().renameTo(new File(r.getDirectory() + File.separator + new_name + ".rz"));

		r.getDirectory().renameTo(new File("plugins" + File.separator + "Regios" + File.separator + "Database" + File.separator + new_name));

		new AdministrationCommands().reloadRegions(null);
	}

	public void editDeleteRegion(Region r, boolean reload, Player p) {
		File f = r.getLogFile().getParentFile().getParentFile();
		deleteDir(f);
		GlobalRegionManager.deleteRegionFromCache(r);
		LogRunner.log.remove(r);
		new AdministrationCommands().reloadRegions(p);
		RegionDeleteEvent event = new RegionDeleteEvent("RegionDeleteEvent");
		event.setProperties(p, r);
		Bukkit.getServer().getPluginManager().callEvent(event);
	}

	public static void deleteRegion(String name) throws RegionExistanceException{
		Region r = GlobalRegionManager.getRegion(name);
		if(r == null){
			throw new RegionExistanceException(name);
		}
		File f = r.getLogFile().getParentFile().getParentFile();
		deleteDir(f);
		GlobalRegionManager.deleteRegionFromCache(r);
		LogRunner.log.remove(r);
		new AdministrationCommands().reloadRegions(null);
		RegionDeleteEvent event = new RegionDeleteEvent("RegionDeleteEvent");
		event.setProperties(null, r);
		Bukkit.getServer().getPluginManager().callEvent(event);
	}

	public static boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}

		// The directory is now empty so delete it
		return dir.delete();
	}

}
