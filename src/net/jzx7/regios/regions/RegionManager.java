package net.jzx7.regios.regions;

import java.awt.Polygon;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import net.jzx7.regios.Commands.AdministrationCommands;
import net.jzx7.regios.Data.Direction;
import net.jzx7.regios.Listeners.RegiosPlayerListener;
import net.jzx7.regios.Permissions.PermissionsCacheManager;
import net.jzx7.regios.Restrictions.RestrictionParameters;
import net.jzx7.regios.Scheduler.HealthRegeneration;
import net.jzx7.regios.Scheduler.LogRunner;
import net.jzx7.regios.Spout.SpoutInterface;
import net.jzx7.regios.Spout.SpoutRegion;
import net.jzx7.regiosapi.regions.CuboidRegion;
import net.jzx7.regiosapi.regions.PolyRegion;
import net.jzx7.regiosapi.regions.Region;
import net.jzx7.regiosapi.events.RegionDeleteEvent;
import net.jzx7.regiosapi.events.RegionEnterEvent;
import net.jzx7.regiosapi.events.RegionExitEvent;
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

	private static final ExtrasRegions extReg = new ExtrasRegions();
	private static char[] invalidModifiers = { '!', '\'', '£', '$', '%', '^', '&', '*', '¬', '`', '/', '?', '<', '>', '|', '\\' };

	private static ArrayList<Region> regions = new ArrayList<Region>()
			, regionsInWorld = new ArrayList<Region>();

	private final static SubRegionManager srm = new SubRegionManager();

	public void addRegion(Region genericRegion){
		regions.add(genericRegion);
	}

	private boolean canSetRegionSize(int[] xPoints, int[] zPoints, int nPoints, double minY, double maxY, Player p) {
		Rectangle2D rect = new Polygon(xPoints, zPoints, nPoints).getBounds2D();

		return canSetRegionSize(new Vector(rect.getMinX(), minY, rect.getMinY()), new Vector(rect.getMaxX(), maxY, rect.getMaxY()), p);
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

	public String colourFormat(String message) {
		message = message.replaceAll("<BLACK>", "\u00A70");
		message = message.replaceAll("<0>", "\u00A70");

		message = message.replaceAll("<DBLUE>", "\u00A71");
		message = message.replaceAll("<1>", "\u00A71");

		message = message.replaceAll("<DGREEN>", "\u00A72");
		message = message.replaceAll("<2>", "\u00A72");

		message = message.replaceAll("<DTEAL>", "\u00A73");
		message = message.replaceAll("<3>", "\u00A73");

		message = message.replaceAll("<DRED>", "\u00A74");
		message = message.replaceAll("<4>", "\u00A74");

		message = message.replaceAll("<PURPLE>", "\u00A75");
		message = message.replaceAll("<5>", "\u00A75");

		message = message.replaceAll("<GOLD>", "\u00A76");
		message = message.replaceAll("<6>", "\u00A76");

		message = message.replaceAll("<GREY>", "\u00A77");
		message = message.replaceAll("<7>", "\u00A77");

		message = message.replaceAll("<DGREY>", "\u00A78");
		message = message.replaceAll("<8>", "\u00A78");

		message = message.replaceAll("<BLUE>", "\u00A79");
		message = message.replaceAll("<9>", "\u00A79");

		message = message.replaceAll("<BGREEN>", "\u00A7a");
		message = message.replaceAll("<A>", "\u00A7a");

		message = message.replaceAll("<TEAL>", "\u00A7b");
		message = message.replaceAll("<B>", "\u00A7b");

		message = message.replaceAll("<RED>", "\u00A7c");
		message = message.replaceAll("<C>", "\u00A7c");

		message = message.replaceAll("<PINK>", "\u00A7d");
		message = message.replaceAll("<D>", "\u00A7d");

		message = message.replaceAll("<YELLOW>", "\u00A7e");
		message = message.replaceAll("<E>", "\u00A7e");

		message = message.replaceAll("<WHITE>", "\u00A7f");
		message = message.replaceAll("<F>", "\u00A7f");

		message = message.replaceAll("\\[", "");
		message = message.replaceAll("\\]", "");

		return message;
	}

	private String convertLocation(Location l) {
		return l.getWorld().getName() + "," + l.getX() + "," + l.getY() + "," + l.getZ();
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
			
			if (maxY > pr.getWorld().getMaxHeight()) {
				p.sendMessage("You cannot expand regions higher than the world height limit!)");
				return false;
			}
			
			if (minY < 0) {
				p.sendMessage("You cannot expand regions into the void!)");
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

			if (dirStr.equalsIgnoreCase("max")) {
				pos1.setY(0);
				pos2.setY(r.getWorld().getMaxHeight());
			}
			else if (dirStr.equalsIgnoreCase("out")) {
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
			
			if (pos1.getY() > cr.getWorld().getMaxHeight() || pos2.getY() > cr.getWorld().getMaxHeight()) {
				p.sendMessage("You cannot expand regions higher than the world height limit!");
				return false;
			}
			
			if (pos1.getY() < cr.getWorld().getMaxHeight() || pos2.getY() < cr.getWorld().getMaxHeight()) {
				p.sendMessage("You cannot expand regions into the void!");
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

	public int getOwnedRegions(String name){
		int count = 0;
		for(Region r : regions){
			if(r.getOwner().equalsIgnoreCase(name)){
				count++;
			}
		}
		return count;
	}

	public Region getRegion(Location l){

		ArrayList<Region> currentRegionSet = new ArrayList<Region>();

		for (Region reg : getRegions()) {
			if (reg instanceof CuboidRegion) {
				CuboidRegion cr = (CuboidRegion)reg;
				if (extReg.isInsideCuboid(l, cr.getL1(), cr.getL2()) && (l.getWorld().getName() == cr.getWorld().getName())) {
					currentRegionSet.add(reg);
				}
			} else if (reg instanceof PolyRegion) {
				PolyRegion pr = (PolyRegion) reg;
				if (extReg.isInsidePolygon(l, pr.get2DPolygon(), pr.getMinY(), pr.getMaxY()) && (l.getWorld().getName() == pr.getWorld().getName())) {
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

	public Region getRegion(Player p){
		for(Region r : regions){
			if(r.getPlayersInRegion().contains(p)){
				return r;
			}
		}
		return null;
	}

	public Region getRegion(String name){
		for(Region r : regions){
			if(r.getName().equalsIgnoreCase(name)){
				return r;
			}
		}
		return null;
	}

	public ArrayList<Region> getRegions(){
		return regions;
	}

	public ArrayList<Region> getRegions(Location l){

		ArrayList<Region> currentRegionSet = new ArrayList<Region>();

		for (Region reg : getRegions()) {
			if (reg instanceof CuboidRegion) {
				CuboidRegion cr = (CuboidRegion)reg;
				if (extReg.isInsideCuboid(l, cr.getL1(), cr.getL2()) && (l.getWorld().getName() == cr.getWorld().getName())) {
					currentRegionSet.add(reg);
				}
			} else if (reg instanceof PolyRegion) {
				PolyRegion pr = (PolyRegion) reg;
				if (extReg.isInsidePolygon(l, pr.get2DPolygon(), pr.getMinY(), pr.getMaxY()) && (l.getWorld().getName() == pr.getWorld().getName())) {
					currentRegionSet.add(reg);
				}
			}
		}

		return currentRegionSet;
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

	public boolean isInRegion(Location l) {
		return (getRegion(l) == null ? false : true);
	}

	public boolean isInRegion(Player p) {
		return (getRegion(p) == null ? false : true);
	}

	private boolean isSendable(Player p, Region r) {
		boolean outcome = (r.getTimeStamps().containsKey(p.getName()) ? (System.currentTimeMillis() > r.getTimeStamps().get(p.getName()) + 5000) : true);
		if (outcome) {
			r.setTimestamp(p);
		}
		return outcome;
	}

	public String liveFormat(String original, Player p, Region r) {
		original = original.replaceAll("\\[", "");
		original = original.replaceAll("\\]", "");
		if (original.contains("PLAYER-COUNT")) {
			original = original.replaceAll("PLAYER-COUNT", "" + r.getPlayersInRegion().size());
		}
		if (original.contains("BUILD-RIGHTS")) {
			original = original.replaceAll("BUILD-RIGHTS", "" + r.canBypassProtection(p));
		}
		if (original.contains("PLAYER")) {
			original = original.replaceAll("PLAYER", "" + p.getName());
		}
		if (original.contains("PLAYER-LIST")) {
			StringBuilder builder = new StringBuilder();
			builder.append("");
			for (String play : r.getPlayersInRegion()) {
				builder.append(ChatColor.WHITE).append(play).append(ChatColor.BLUE).append(", ");
			}
			original = original.replaceAll("PLAYER-LIST", "" + builder.toString());
		}
		if (original.contains("OWNER")) {
			original = original.replaceAll("OWNER", r.getOwner());
		}
		if (original.contains("NAME")) {
			original = original.replaceAll("NAME", r.getName());
		}
		return original;
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

	public void purgeRegions(){
		regions.clear();
		System.gc();
	}

	private void registerExitEvent(Player p, Region r) {
		RegionExitEvent event = new RegionExitEvent("RegionExitEvent");
		event.setProperties(p, r);
		Bukkit.getServer().getPluginManager().callEvent(event);
	}

	private void registerWelcomeEvent(Player p, Region r) {
		RegionEnterEvent event = new RegionEnterEvent("RegionEnterEvent");
		event.setProperties(p, r);
		Bukkit.getServer().getPluginManager().callEvent(event);
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

	public void sendBuildMessage(Player p, Region r) {
		if (r.isShowProtectionMessage() && isSendable(p,r)) {
			LogRunner.addLogMessage(r, LogRunner.getPrefix(r) + (" Player '" + p.getName() + "' tried to build but did not have permissions."));
			p.sendMessage(colourFormat(liveFormat(r.getProtectionMessage(), p, r)));
		}
	}

	public void sendLeaveMessage(Player p, Region r) {
		if (!r.isLeaveMessageSent(p)) {
			registerExitEvent(p, r);
			LogRunner.addLogMessage(r, LogRunner.getPrefix(r) + (" Player '" + p.getName() + "' left region."));
			if (RegiosPlayerListener.currentRegion.containsKey(p.getName())) {
				RegiosPlayerListener.currentRegion.remove(p.getName());
			}
			r.getLeaveMessageSent().put(p.getName(), true);
			r.getWelcomeMessageSent().remove(p.getName());
			r.removePlayer(p);
			if (HealthRegeneration.isRegenerator(p)) {
				HealthRegeneration.removeRegenerator(p);
			}
			if (r.isPermWipeOnExit()) {
				if (!r.canBypassProtection(p)) {
					((RegiosRegion) r).getICM().wipeInventory(p);
				}
			}
			if (r.isWipeAndCacheOnEnter()) {
				if (!r.canBypassProtection(p)) {
					if (((RegiosRegion) r).getICM().doesCacheContain(p)) {
						((RegiosRegion) r).getICM().restoreInventory(p);
						LogRunner.addLogMessage(r, LogRunner.getPrefix(r) + (" Player '" + p.getName() + "' inventory restored upon exit"));
					}
				}
			}
			if (r.isWipeAndCacheOnExit()) {
				if (!r.canBypassProtection(p)) {
					if (!((RegiosRegion) r).getICM().doesCacheContain(p)) {
						((RegiosRegion) r).getICM().cacheInventory(p);
						((RegiosRegion) r).getICM().wipeInventory(p);
						LogRunner.addLogMessage(r, LogRunner.getPrefix(r) + (" Player '" + p.getName() + "' inventory cached upon exit"));
					}
				}
			}

			if (r.isChangeGameMode()) {
				if (((RegiosRegion) r).getGMCM().doesCacheContain(p))
				{
					((RegiosRegion) r).getGMCM().restoreGameMode(p);
				}
			}

			LogRunner.addLogMessage(r, LogRunner.getPrefix(r) + (" Player '" + p.getName() + "' left region."));

			try {
				/*
				 * Permission Nodes
				 */
				if (r.getTempNodesCacheAdd() != null) {
					if (r.getTempNodesCacheAdd().length > 0) {
						PermissionsCacheManager.unCacheAddNodes(p, r);
						LogRunner.addLogMessage(r, LogRunner.getPrefix(r) + (" Temporary add node caches wiped upon region exit for player '" + p.getName() + "'"));
					}
				}
				if (r.getTempNodesCacheRem() != null) {
					if (r.getTempNodesCacheRem().length > 0) {
						PermissionsCacheManager.unCacheRemNodes(p, r);
						LogRunner.addLogMessage(r, LogRunner.getPrefix(r) + (" Temporary remove node caches restored upon region exit for player '" + p.getName() + "'"));
					}
				}
				if (r.getPermRemoveNodes() != null) {
					if (r.getPermRemoveNodes().length > 0) {
						PermissionsCacheManager.permRemoveNodes(p, r);
						LogRunner.addLogMessage(r, LogRunner.getPrefix(r) + (" Permanent nodes wiped upon region exit for player '" + p.getName() + "'"));
					}
				}
				/*
				 * End Permission Nodes
				 */
				/*
				 * Groups
				 */
				if (r.getTempAddGroups() != null) {
					if (r.getTempAddGroups().length > 0) {
						PermissionsCacheManager.unCacheAddGroups(p, r);
						LogRunner.addLogMessage(r, LogRunner.getPrefix(r) + (" Temporary add groups wiped upon region exit for player '" + p.getName() + "'"));
					}
				}
				if (r.getTempRemoveGroups() != null) {
					if (r.getTempRemoveGroups().length > 0) {
						PermissionsCacheManager.unCacheRemoveGroups(p, r);
						LogRunner.addLogMessage(r, LogRunner.getPrefix(r) + (" Temporary remove groups restored upon region exit for player '" + p.getName() + "'"));
					}
				}
				if (r.getPermRemoveGroups() != null) {
					if (r.getPermRemoveGroups().length > 0) {
						PermissionsCacheManager.permRemoveGroups(p, r);
						LogRunner.addLogMessage(r, LogRunner.getPrefix(r) + (" Permanent groups wiped upon region exit for player '" + p.getName() + "'"));
					}
				}
				/*
				 * End Groups
				 */
			} catch (Exception ex) {
				// Fail silently if the operation is unsupported
			}
			if (r.isShowLeaveMessage()) {
				p.sendMessage(colourFormat(liveFormat(r.getLeaveMessage(), p, r)));
			}
			if (SpoutInterface.doesPlayerHaveSpout(p)) {
				if (r.isSpoutLeaveEnabled()) {
					SpoutRegion.sendLeaveMessage(p, r);
				}
				if (r.isPlayCustomSoundUrl()) {
					SpoutRegion.stopMusicPlaying(p, r);
				}
				if (r.isUseSpoutTexturePack()) {
					SpoutRegion.resetTexturePack(p);
				}
			}
		}
	}

	public void sendPreventEntryMessage(Player p, Region r) {
		if (r.isShowPreventEntryMessage() && isSendable(p,r)) {
			LogRunner.addLogMessage(r, LogRunner.getPrefix(r) + (" Player '" + p.getName() + "' tried to enter but did not have permissions."));
			p.sendMessage(colourFormat(liveFormat(r.getPreventEntryMessage(), p, r)));
		}
	}

	public void sendPreventExitMessage(Player p, Region r) {
		if (r.isShowPreventExitMessage() && isSendable(p, r)) {
			LogRunner.addLogMessage(r, LogRunner.getPrefix(r) + (" Player '" + p.getName() + "' tried to leave but did not have permissions."));
			p.sendMessage(colourFormat(liveFormat(r.getPreventExitMessage(), p, r)));
		}
	}

	public void sendWelcomeMessage(Player p, Region r) {
		if (!r.isWelcomeMessageSent(p)) {
			registerWelcomeEvent(p, r);
			LogRunner.addLogMessage(r, LogRunner.getPrefix(r) + (" Player '" + p.getName() + "' entered region."));
			if (r.isUseSpoutTexturePack() && SpoutInterface.doesPlayerHaveSpout(p)) {
				SpoutRegion.forceTexturePack(p, r);
			}
			RegiosPlayerListener.currentRegion.put(p.getName(), r);
			r.getWelcomeMessageSent().put(p.getName(), true);
			r.getLeaveMessageSent().remove(p.getName());
			r.addPlayer(p);
			if (!HealthRegeneration.isRegenerator(p)) {
				if (r.getHealthRegen() < 0 && !r.canBypassProtection(p)) {
					HealthRegeneration.addRegenerator(p, r.getHealthRegen());
				} else if (r.getHealthRegen() > 0) {
					HealthRegeneration.addRegenerator(p, r.getHealthRegen());
				}
			}
			if (r.isPermWipeOnEnter()) {
				if (!r.canBypassProtection(p)) {
					((RegiosRegion) r).getICM().wipeInventory(p);
				}
			}
			if (r.isWipeAndCacheOnEnter()) {
				if (!r.canBypassProtection(p)) {
					if (!((RegiosRegion) r).getICM().doesCacheContain(p)) {
						((RegiosRegion) r).getICM().cacheInventory(p);
						((RegiosRegion) r).getICM().wipeInventory(p);
						LogRunner.addLogMessage(r, LogRunner.getPrefix(r) + (" Player '" + p.getName() + "' inventory cached upon entry"));
					}
				}
			}
			if (r.isWipeAndCacheOnExit()) {
				if (!r.canBypassProtection(p)) {
					if (((RegiosRegion) r).getICM().doesCacheContain(p)) {
						((RegiosRegion) r).getICM().restoreInventory(p);
						LogRunner.addLogMessage(r, LogRunner.getPrefix(r) + (" Player '" + p.getName() + "' inventory restored upon entry"));
					}
				}
			}
			if (r.isChangeGameMode()) {
				if(!((RegiosRegion) r).getGMCM().doesCacheContain(p)) {
					((RegiosRegion) r).getGMCM().cacheGameMode(p);
					p.setGameMode(r.getGameMode());
				}
			}
			LogRunner.addLogMessage(r, LogRunner.getPrefix(r) + (" Player '" + p.getName() + "' entered region."));
			if (r.isForceCommand()) {
				if (r.getCommandSet() != null) {
					if (r.getCommandSet().length > 0) {
						for (String s : r.getCommandSet()) {
							if (s.length() > 1) {
								LogRunner.addLogMessage(r, LogRunner.getPrefix(r) + (" Player forced command '" + s + "' upon enter."));
								p.performCommand(s.trim());
							}
						}
					}
				}
			}
			try {
				/*
				 * Permission Nodes
				 */
				if (r.getTempNodesCacheAdd() != null) {
					if (r.getTempNodesCacheAdd().length > 0) {
						PermissionsCacheManager.cacheAddNodes(p, r);
						LogRunner.addLogMessage(r, LogRunner.getPrefix(r) + (" Temporary add node caches added upon region enter for player '" + p.getName() + "'"));
					}

				}
				if (r.getTempNodesCacheRem() != null) {
					if (r.getTempNodesCacheRem().length > 0) {
						PermissionsCacheManager.cacheRemNodes(p, r);
						LogRunner.addLogMessage(r, LogRunner.getPrefix(r) + (" Temporary remove node caches wiped upon region enter for player '" + p.getName() + "'"));
					}

				}
				if (r.getPermAddNodes() != null) {
					if (r.getPermAddNodes().length > 0) {
						PermissionsCacheManager.permAddNodes(p, r);
						LogRunner.addLogMessage(r, LogRunner.getPrefix(r) + (" Permanent nodes added upon region enter for player '" + p.getName() + "'"));
					}
				}
				/*
				 * End Permission Nodes
				 */
				/*
				 * Groups
				 */
				if (r.getTempAddGroups() != null) {
					if (r.getTempAddGroups().length > 0) {
						PermissionsCacheManager.cacheAddGroups(p, r);
						LogRunner.addLogMessage(r, LogRunner.getPrefix(r) + (" Temporary add groups added upon region enter for player '" + p.getName() + "'"));
					}

				}
				if (r.getTempRemoveGroups() != null) {
					if (r.getTempRemoveGroups().length > 0) {
						PermissionsCacheManager.cacheRemoveGroups(p, r);
						LogRunner.addLogMessage(r, LogRunner.getPrefix(r) + (" Temporary remove groups wiped upon region enter for player '" + p.getName() + "'"));
					}

				}
				if (r.getPermAddGroups() != null) {
					if (r.getPermAddGroups().length > 0) {
						PermissionsCacheManager.permAddGroups(p, r);
						LogRunner.addLogMessage(r, LogRunner.getPrefix(r) + (" Permanent groups added upon region enter for player '" + p.getName() + "'"));
					}
				}
				/*
				 * End Groups
				 */
			} catch (Exception ex) {
				// Fail silently if the operation is unsupported
			}
			if (r.isShowWelcomeMessage()) {
				p.sendMessage(colourFormat(liveFormat(r.getWelcomeMessage(), p, r)));
			}
			if (SpoutInterface.doesPlayerHaveSpout(p)) {
				if (r.isSpoutWelcomeEnabled()) {
					SpoutRegion.sendWelcomeMessage(p, r);
				}
				if (r.isPlayCustomSoundUrl()) {
					SpoutRegion.playToPlayerMusicFromUrl(p, r);
				}
				if (r.isUseSpoutTexturePack()) {
					SpoutRegion.forceTexturePack(p, r);
				}
			}
		}
	}


	public boolean shrinkRegion(Region r, int value, String dirStr, Player p) {
		if (r instanceof PolyRegion) {
			PolyRegion pr = (PolyRegion) r;
			double minY = pr.getMinY(), maxY = pr.getMaxY();
			if (dirStr.equalsIgnoreCase("up")) {
				minY = minY + value; 
			} else if (dirStr.equalsIgnoreCase("down")) {
				maxY = maxY - value;
			} else {
				p.sendMessage("You can only shrink polygonal regions up or down");
				return false;
			}

			if (!canSetRegionSize(pr.get2DPolygon().xpoints, pr.get2DPolygon().ypoints, pr.get2DPolygon().npoints, minY, maxY, p)) {
				return false;
			}
			
			if (maxY > pr.getWorld().getMaxHeight()) {
				p.sendMessage("You cannot shrink regions higher than the world height limit!)");
				return false;
			}
			
			if (minY < 0) {
				p.sendMessage("You cannot shrink regions into the void!)");
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

			if (dirStr.equalsIgnoreCase("in")) {
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
			
			if (pos1.getY() > cr.getWorld().getMaxHeight() || pos2.getY() > cr.getWorld().getMaxHeight()) {
				p.sendMessage("You cannot expand regions higher than the world height limit!");
				return false;
			}
			
			if (pos1.getY() < cr.getWorld().getMaxHeight() || pos2.getY() < cr.getWorld().getMaxHeight()) {
				p.sendMessage("You cannot expand regions into the void!");
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

	public boolean shiftRegion(Region r, int value, String dirStr, Player p) {
		if (r instanceof PolyRegion) {
			PolyRegion pr = (PolyRegion) r;
			double minY = pr.getMinY(), maxY = pr.getMaxY();
			Polygon poly = pr.get2DPolygon();
			if (dirStr.equalsIgnoreCase("up")) {
				maxY = maxY + value;
				minY = minY + value;
			} else if (dirStr.equalsIgnoreCase("down")) {
				minY = minY - value;
				maxY = maxY - value;
			} else {
				Vector change;
				
				try {
					change = Direction.getDirection(dirStr).getVector().multiply(value);
					poly.translate((int)change.getX(), (int)change.getZ());
				} catch (InvalidDirectionException e) {
					p.sendMessage("Invalid direction entered!");
					return false;
				}
			}

			if (!canSetRegionSize(poly.xpoints, poly.ypoints, poly.npoints, minY, maxY, p)) {
				return false;
			}
			
			if (maxY > pr.getWorld().getMaxHeight()) {
				p.sendMessage("You cannot shrink regions higher than the world height limit!)");
				return false;
			}
			
			if (minY < 0) {
				p.sendMessage("You cannot shrink regions into the void!)");
				return false;
			}

			FileConfiguration c = YamlConfiguration.loadConfiguration(r.getConfigFile());
			c.set("Region.Essentials.Points.MinY", minY);
			c.set("Region.Essentials.Points.MaxY", maxY);
			c.set("Region.Essentials.Points.xPoints", poly.xpoints);
			c.set("Region.Essentials.Points.zPoints", poly.ypoints);
			pr.setMinY(minY);
			pr.setMaxY(maxY);
			pr.set2DPolygon(poly);
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

			try {
				change = Direction.getDirection(dirStr).getVector().multiply(value);

				pos1 = pos1.add(new Vector(change.getX(), change.getY(), change.getZ()));
				pos2 = pos2.add(new Vector(change.getX(), change.getY(), change.getZ()));

			} catch (InvalidDirectionException e1) {
				p.sendMessage("Invalid direction entered!");
				return false;
			}

			if (!canSetRegionSize(pos1.toVector(), pos2.toVector(), p))
			{
				return false;
			}
			
			if (pos1.getY() > cr.getWorld().getMaxHeight() || pos2.getY() > cr.getWorld().getMaxHeight()) {
				p.sendMessage("You cannot shift regions higher than the world height limit!");
				return false;
			}
			
			if (pos1.getY() < cr.getWorld().getMaxHeight() || pos2.getY() < cr.getWorld().getMaxHeight()) {
				p.sendMessage("You cannot shift regions into the void!");
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
}