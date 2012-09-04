package net.jzx7.regios.regions;

import java.awt.Polygon;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import net.jzx7.regios.Commands.AdministrationCommands;
import net.jzx7.regios.Data.Direction;
import net.jzx7.regios.Restrictions.RestrictionParameters;
import net.jzx7.regios.Scheduler.LogRunner;
import net.jzx7.regiosapi.regions.CuboidRegion;
import net.jzx7.regiosapi.regions.PolyRegion;
import net.jzx7.regiosapi.regions.Region;
import net.jzx7.regiosapi.events.RegionDeleteEvent;
import net.jzx7.regiosapi.events.RegionModifyEvent;
import net.jzx7.regiosapi.exceptions.InvalidDirectionException;
import net.jzx7.regiosapi.exceptions.RegionExistanceException;
import net.jzx7.regiosapi.exceptions.RegionNameExistsException;
import net.jzx7.regiosapi.exceptions.RegionPointsNotSetException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import couk.Adamki11s.Extras.Regions.ExtrasRegions;

public class RegionManager {

	private static ArrayList<Region> regions = new ArrayList<Region>()
			, regionsInWorld = new ArrayList<Region>();
	private static final ExtrasRegions extReg = new ExtrasRegions();

	private final static SubRegionManager srm = new SubRegionManager();

	private static char[] invalidModifiers = { '!', '\'', '£', '$', '%', '^', '&', '*', '¬', '`', '/', '?', '<', '>', '|', '\\' };

	public ArrayList<Region> getRegions(){
		return regions;
	}

	public ArrayList<Region> getRegions(World w){
		regionsInWorld.clear();
		for(Region r : regions) {
			if(r.getWorld().getName().equalsIgnoreCase(w.getName())) {
				regionsInWorld.add(r);
			}
		}
		return regionsInWorld;
	}

	public ArrayList<Region> getRegions(Location l){

		ArrayList<Region> currentRegionSet = new ArrayList<Region>();

		for (Region reg : getRegions()) {
			if (reg instanceof CuboidRegion) {
				CuboidRegion cr = (CuboidRegion)reg;
				if (extReg.isInsideCuboid(l, cr.getL1(), cr.getL2())) {
					currentRegionSet.add(reg);
				}
			} else if (reg instanceof PolyRegion) {
				PolyRegion pr = (PolyRegion) reg;
				if (extReg.isInsidePolygon(l, pr.get2DPolygon(), pr.getMinY(), pr.getMaxY())) {
					currentRegionSet.add(reg);
				}
			}
		}

		return currentRegionSet;
	}

	public boolean deleteRegionFromCache(Region r){
		if(regions.contains(r)){
			regions.remove(r);
			return true;
		}
		return false;
	}

	public boolean doesRegionExist(String name){
		for(Region r : regions){
			if(r.getName().equalsIgnoreCase(name)){
				return true;
			}
		}
		return false;
	}

	public void purgeRegions(){
		regions.clear();
		System.gc();
	}

	public Region getRegion(Player p){
		for(Region r : regions){
			if(r.getPlayersInRegion().contains(p)){
				return r;
			}
		}
		return null;
	}

	public Region getRegion(Location l){

		ArrayList<Region> currentRegionSet = new ArrayList<Region>();

		for (Region reg : getRegions()) {
			if (reg instanceof CuboidRegion) {
				CuboidRegion cr = (CuboidRegion)reg;
				if (extReg.isInsideCuboid(l, cr.getL1(), cr.getL2())) {
					currentRegionSet.add(reg);
				}
			} else if (reg instanceof PolyRegion) {
				PolyRegion pr = (PolyRegion) reg;
				if (extReg.isInsidePolygon(l, pr.get2DPolygon(), pr.getMinY(), pr.getMaxY())) {
					currentRegionSet.add(reg);
				}
			}
		}

		if (currentRegionSet.isEmpty()) {
			return null;
		}

		if (currentRegionSet.size() > 1) {
			return srm.getCurrentRegion(currentRegionSet);
		} else {
			return currentRegionSet.get(0);
		}
	}

	public Region getRegion(String name){
		for(Region r : regions){
			if(r.getName().equalsIgnoreCase(name)){
				return r;
			}
		}
		return null;
	}

	public int getOwnedRegions(String name){
		int count = 0;
		for(Region r : regions){
			if(r.getOwner().equalsIgnoreCase(name)){
				count++;
			}
		}
		return count;
	}

	public void addRegion(Region genericRegion){
		regions.add(genericRegion);
	}

	public boolean isInRegion(Player p) {
		return (getRegion(p) == null ? false : true);
	}

	public boolean isInRegion(Location l) {
		return (getRegion(l) == null ? false : true);
	}

	public boolean createRegion(Player p, String name, Location point1, Location point2) throws RegionNameExistsException, RegionPointsNotSetException {
		if (doesRegionExist(name)) {
			p.sendMessage(ChatColor.RED + "[Regios] A region with name : " + ChatColor.BLUE + name + ChatColor.RED + " already exists!");
			throw new RegionNameExistsException(name);
		}

		StringBuilder invalidName = new StringBuilder();
		boolean integrity = true;
		for (char ch : name.toCharArray()) {
			boolean valid = true;
			for (char inv : invalidModifiers) {
				if (ch == inv) {
					valid = false;
					integrity = false;
				}
			}
			if (!valid) {
				invalidName.append(ChatColor.RED).append(ch);
			} else {
				invalidName.append(ChatColor.GREEN).append(ch);
			}
		}

		if (!integrity) {
			p.sendMessage(ChatColor.RED + "[Regios] Name contained  invalid characters : " + invalidName.toString());
			return false;
		}

		int rCount = getOwnedRegions(p.getName());

		RestrictionParameters params = RestrictionParameters.getRestrictions(p);

		if(rCount >= params.getRegionLimit()){
			p.sendMessage(ChatColor.RED + "[Regios] You cannot create more than " + ChatColor.YELLOW + params.getRegionLimit() + ChatColor.RED + " regions!");
			return false;
		}

		if (!canSetRegionSize(point1.toVector(), point2.toVector(), p)) {
			return false;
		}

		new RegiosCuboidRegion(p.getName(), name, point1, point2, p.getWorld(), null, true);
		return true;
	}

	public boolean renameRegion(Region r, String new_name, Player p) {

		if (getRegion(new_name) != null) {
			p.sendMessage(ChatColor.RED + "[Regios] The region " + ChatColor.BLUE + new_name + ChatColor.RED + " already exists!");
			return false;
		}

		LogRunner.log.remove(r);

		FileConfiguration c = YamlConfiguration.loadConfiguration(r.getConfigFile());
		c.set("Region.Essentials.Name", new_name);
		try {
			c.save(r.getConfigFile());
		} catch (IOException e1) {
			e1.printStackTrace();
			return false;
		}

		r.getConfigFile().renameTo(new File(r.getDirectory() + File.separator + new_name + ".rz"));

		r.getDirectory().renameTo(new File("plugins" + File.separator + "Regios" + File.separator + "Database" + File.separator + new_name));

		new AdministrationCommands().reload(p);

		return true;
	}

	public boolean deleteRegion(Region r, Player p) {
		File f = r.getLogFile().getParentFile().getParentFile();
		if (deleteDir(f)) {
			deleteRegionFromCache(r);
			LogRunner.log.remove(r);
			new AdministrationCommands().reload(p);
			RegionDeleteEvent event = new RegionDeleteEvent("RegionDeleteEvent");
			event.setProperties(p, r);
			Bukkit.getServer().getPluginManager().callEvent(event);
			return true;
		} else {
			p.sendMessage(ChatColor.RED + "[Regios] Can't delete region folder! Does the region actually exist?");
			return false;
		}
	}

	public boolean deleteRegion(String name, Player p) throws RegionExistanceException{
		Region r = getRegion(name);
		if(r == null){
			throw new RegionExistanceException(name);
		}
		return deleteRegion(r, p);
	}

	public boolean canSetRegionSize(Vector smaller, Vector bigger, Player p) {
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

	private boolean canSetRegionSize(int[] xPoints, int[] zPoints, int nPoints, double minY, double maxY, Player p) {
		Rectangle2D rect = new Polygon(xPoints, zPoints, nPoints).getBounds2D();

		return canSetRegionSize(new Vector(rect.getMinX(), rect.getMinY(), minY), new Vector(rect.getMaxX(), rect.getMaxY(), maxY), p);
	}

	public boolean expandRegion(Region r, int value, String dirStr, Player p) {
		if (r instanceof PolyRegion) {
			PolyRegion pr = (PolyRegion) r;
			double minY = pr.getMinY(), maxY = pr.getMaxY();
			if (dirStr.equalsIgnoreCase("up")) {
				maxY = maxY + value; 
			} else if (dirStr.equalsIgnoreCase("down")) {
				minY = minY - value;
			} else {
				p.sendMessage("You can only expand polygonal regions up or down");
				return false;
			}

			if (!canSetRegionSize(pr.get2DPolygon().xpoints, pr.get2DPolygon().ypoints, pr.get2DPolygon().npoints, minY, maxY, p)) {
				return false;
			}

			FileConfiguration c = YamlConfiguration.loadConfiguration(r.getConfigFile());
			c.set("Region.Essentials.Points.MinY", minY);
			c.set("Region.Essentials.Points.MaxY", maxY);
			pr.setMinY(minY);
			pr.setMaxY(maxY);
			try {
				c.save(r.getConfigFile());
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else if (r instanceof CuboidRegion) {
			CuboidRegion cr = (CuboidRegion) r;

			Vector change;
			Location pos1 = cr.getL1();
			Location pos2 = cr.getL2();

			if (dirStr.toLowerCase() == "max") {
				pos1.setY(0);
				pos2.setY(r.getWorld().getMaxHeight());
			}
			else if (dirStr.toLowerCase() == "out") {
				Location smaller = new Location(cr.getL1().getWorld(), Math.min(cr.getL1().getX(), cr.getL2().getX())
						, Math.min(cr.getL1().getY(), cr.getL2().getY())
						, Math.min(cr.getL1().getZ(), cr.getL2().getZ()));
				Location bigger = new Location(cr.getL1().getWorld(), Math.max(cr.getL1().getX(), cr.getL2().getX())
						, Math.max(cr.getL1().getY(), cr.getL2().getY())
						, Math.max(cr.getL1().getZ(), cr.getL2().getZ()));
				pos1 = smaller.subtract(value, 0, value);
				pos2 = bigger.add(value, 0, value);
			}
			else {
				try {
					change = Direction.getDirection(dirStr).getVector().multiply(value);

					if (change.getX() > 0) {
						if (Math.max(pos1.getX(), pos2.getX()) == pos1.getX()) {
							pos1 = pos1.add(new Vector(change.getX(), 0, 0));
						} else {
							pos2 = pos2.add(new Vector(change.getX(), 0, 0));
						}
					} else {
						if (Math.min(pos1.getX(), pos2.getX()) == pos1.getX()) {
							pos1 = pos1.add(new Vector(change.getX(), 0, 0));
						} else {
							pos2 = pos2.add(new Vector(change.getX(), 0, 0));
						}
					}

					if (change.getY() > 0) {
						if (Math.max(pos1.getY(), pos2.getY()) == pos1.getY()) {
							pos1 = pos1.add(new Vector(0, change.getY(), 0));
						} else {
							pos2 = pos2.add(new Vector(0, change.getY(), 0));
						}
					} else {
						if (Math.min(pos1.getY(), pos2.getY()) == pos1.getY()) {
							pos1 = pos1.add(new Vector(0, change.getY(), 0));
						} else {
							pos2 = pos2.add(new Vector(0, change.getY(), 0));
						}
					}

					if (change.getZ() > 0) {
						if (Math.max(pos1.getZ(), pos2.getZ()) == pos1.getZ()) {
							pos1 = pos1.add(new Vector(0, 0, change.getZ()));
						} else {
							pos2 = pos2.add(new Vector(0, 0, change.getZ()));
						}
					} else {
						if (Math.min(pos1.getZ(), pos2.getZ()) == pos1.getZ()) {
							pos1 = pos1.add(new Vector(0, 0, change.getZ()));
						} else {
							pos2 = pos2.add(new Vector(0, 0, change.getZ()));
						}
					}
				} catch (InvalidDirectionException e1) {
					p.sendMessage("Invalid direction entered!");
					return false;
				}
			}

			if (!canSetRegionSize(pos1.toVector(), pos2.toVector(), p))
			{
				return false;
			}

			FileConfiguration c = YamlConfiguration.loadConfiguration(r.getConfigFile());
			c.set("Region.Essentials.Points.Point1", convertLocation(pos1));
			c.set("Region.Essentials.Points.Point2", convertLocation(pos2));
			cr.setL1(pos1);
			cr.setL2(pos2);
			try {
				c.save(r.getConfigFile());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		RegionModifyEvent event = new RegionModifyEvent("RegionModifyEvent");
		event.setProperties(r);
		Bukkit.getServer().getPluginManager().callEvent(event);

		return true;
	}

	public boolean shrinkRegion(Region r, int value, String dirStr, Player p) {
		if (r instanceof PolyRegion) {
			PolyRegion pr = (PolyRegion) r;
			double minY = pr.getMinY(), maxY = pr.getMaxY();
			if (dirStr.equalsIgnoreCase("up")) {
				maxY = maxY + value; 
			} else if (dirStr.equalsIgnoreCase("down")) {
				minY = minY - value;
			} else {
				p.sendMessage("You can only shrink polygonal regions up or down");
				return false;
			}

			if (!canSetRegionSize(pr.get2DPolygon().xpoints, pr.get2DPolygon().ypoints, pr.get2DPolygon().npoints, minY, maxY, p)) {
				return false;
			}

			FileConfiguration c = YamlConfiguration.loadConfiguration(r.getConfigFile());
			c.set("Region.Essentials.Points.MinY", minY);
			c.set("Region.Essentials.Points.MaxY", maxY);
			pr.setMinY(minY);
			pr.setMaxY(maxY);
			try {
				c.save(r.getConfigFile());
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else if (r instanceof CuboidRegion) {
			CuboidRegion cr = (CuboidRegion) r;

			Vector change;
			Location pos1 = cr.getL1();
			Location pos2 = cr.getL2();

			if (dirStr.toLowerCase() == "in") {
				Location smaller = new Location(cr.getL1().getWorld(), Math.min(cr.getL1().getX(), cr.getL2().getX())
						, Math.min(cr.getL1().getY(), cr.getL2().getY())
						, Math.min(cr.getL1().getZ(), cr.getL2().getZ()));
				Location bigger = new Location(cr.getL1().getWorld(), Math.max(cr.getL1().getX(), cr.getL2().getX())
						, Math.max(cr.getL1().getY(), cr.getL2().getY())
						, Math.max(cr.getL1().getZ(), cr.getL2().getZ()));
				pos1 = smaller.add(value, 0, value);
				pos2 = bigger.subtract(value, 0, value);
			}
			else {
				try {
					change = Direction.getDirection(dirStr).getVector().multiply(value);

					if (change.getX() < 0) {
						if (Math.max(pos1.getX(), pos2.getX()) == pos1.getX()) {
							pos1 = pos1.add(new Vector(change.getX(), 0, 0));
						} else {
							pos2 = pos2.add(new Vector(change.getX(), 0, 0));
						}
					} else {
						if (Math.min(pos1.getX(), pos2.getX()) == pos1.getX()) {
							pos1 = pos1.add(new Vector(change.getX(), 0, 0));
						} else {
							pos2 = pos2.add(new Vector(change.getX(), 0, 0));
						}
					}

					if (change.getY() < 0) {
						if (Math.max(pos1.getY(), pos2.getY()) == pos1.getY()) {
							pos1 = pos1.add(new Vector(0, change.getY(), 0));
						} else {
							pos2 = pos2.add(new Vector(0, change.getY(), 0));
						}
					} else {
						if (Math.min(pos1.getY(), pos2.getY()) == pos1.getY()) {
							pos1 = pos1.add(new Vector(0, change.getY(), 0));
						} else {
							pos2 = pos2.add(new Vector(0, change.getY(), 0));
						}
					}

					if (change.getZ() < 0) {
						if (Math.max(pos1.getZ(), pos2.getZ()) == pos1.getZ()) {
							pos1 = pos1.add(new Vector(0, 0, change.getZ()));
						} else {
							pos2 = pos2.add(new Vector(0, 0, change.getZ()));
						}
					} else {
						if (Math.min(pos1.getZ(), pos2.getZ()) == pos1.getZ()) {
							pos1 = pos1.add(new Vector(0, 0, change.getZ()));
						} else {
							pos2 = pos2.add(new Vector(0, 0, change.getZ()));
						}
					}
				} catch (InvalidDirectionException e1) {
					p.sendMessage("Invalid direction entered!");
					return false;
				}
			}

			if (!canSetRegionSize(pos1.toVector(), pos2.toVector(), p))
			{
				return false;
			}

			FileConfiguration c = YamlConfiguration.loadConfiguration(r.getConfigFile());
			c.set("Region.Essentials.Points.Point1", convertLocation(pos1));
			c.set("Region.Essentials.Points.Point2", convertLocation(pos2));
			cr.setL1(pos1);
			cr.setL2(pos2);
			try {
				c.save(r.getConfigFile());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		RegionModifyEvent event = new RegionModifyEvent("RegionModifyEvent");
		event.setProperties(r);
		Bukkit.getServer().getPluginManager().callEvent(event);

		return true;
	}

	public boolean modifyRegionPoints(Region r, Location l1, Location l2, Player p) {
		if (!canSetRegionSize(l1.toVector(), l2.toVector(), p))
		{
			return false;
		}

		FileConfiguration c = YamlConfiguration.loadConfiguration(r.getConfigFile());
		c.set("Region.Essentials.Points.Point1", convertLocation(l1));
		c.set("Region.Essentials.Points.Point2", convertLocation(l2));
		((CuboidRegion) r).setL1(l1);
		((CuboidRegion) r).setL2(l2);
		try {
			c.save(r.getConfigFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
		RegionModifyEvent event = new RegionModifyEvent("RegionModifyEvent");
		event.setProperties(r);
		Bukkit.getServer().getPluginManager().callEvent(event);

		return true;
	}

	private String convertLocation(Location l) {
		return l.getWorld().getName() + "," + l.getX() + "," + l.getY() + "," + l.getZ();
	}

	private boolean deleteDir(File dir) {
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

	public boolean createRegion(Player p, String name, int[] xPoints, int[] zPoints, int nPoints, double minY, double maxY) throws RegionNameExistsException {
		if (doesRegionExist(name)) {
			p.sendMessage(ChatColor.RED + "[Regios] A region with name : " + ChatColor.BLUE + name + ChatColor.RED + " already exists!");
			throw new RegionNameExistsException(name);
		}

		StringBuilder invalidName = new StringBuilder();
		boolean integrity = true;
		for (char ch : name.toCharArray()) {
			boolean valid = true;
			for (char inv : invalidModifiers) {
				if (ch == inv) {
					valid = false;
					integrity = false;
				}
			}
			if (!valid) {
				invalidName.append(ChatColor.RED).append(ch);
			} else {
				invalidName.append(ChatColor.GREEN).append(ch);
			}
		}

		if (!integrity) {
			p.sendMessage(ChatColor.RED + "[Regios] Name contained  invalid characters : " + invalidName.toString());
			return false;
		}

		int rCount = getOwnedRegions(p.getName());

		RestrictionParameters params = RestrictionParameters.getRestrictions(p);

		if(rCount >= params.getRegionLimit()){
			p.sendMessage(ChatColor.RED + "[Regios] You cannot create more than " + ChatColor.YELLOW + params.getRegionLimit() + ChatColor.RED + " regions!");
			return false;
		}

		if (!canSetRegionSize(xPoints, zPoints, nPoints, minY, maxY, p)) {
			return false;
		}

		new RegiosPolyRegion(p.getName(), name, xPoints, zPoints, nPoints, minY, maxY, p.getWorld(), null, true);
		return true;

	}
}