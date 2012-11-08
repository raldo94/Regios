package net.jzx7.regios.Commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import net.jzx7.regios.Data.ConfigurationData;
import net.jzx7.regios.Listeners.RegiosPlayerListener;
import net.jzx7.regios.Permissions.PermissionsCore;
import net.jzx7.regios.RBF.RBF_Core;
import net.jzx7.regios.regions.RegionManager;
import net.jzx7.regios.WorldEdit.Commands.WorldEditCommands;
import net.jzx7.regiosapi.exceptions.RegionNameExistsException;
import net.jzx7.regiosapi.exceptions.RegionPointsNotSetException;
import net.jzx7.regiosapi.regions.Region;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CreationCommands extends PermissionsCore {

	public static HashMap<String, Location> point1 = new HashMap<String, Location>();
	public static HashMap<String, Location> point2 = new HashMap<String, Location>();
	public static HashMap<String, ArrayList<Location>> points = new HashMap<String, ArrayList<Location>>();
	public static HashMap<String, ArrayList<Location>> mpoints = new HashMap<String, ArrayList<Location>>();

	public static HashMap<String, String> setting = new HashMap<String, String>();
	public static HashMap<String, String> modding = new HashMap<String, String>();

	public static HashMap<String, Location> mod1 = new HashMap<String, Location>();
	public static HashMap<String, Location> mod2 = new HashMap<String, Location>();

	public static HashMap<String, Region> modRegion = new HashMap<String, Region>();

	private static char[] invalidModifiers = { '!', '\'', '£', '$', '%', '^', '&', '*', '¬', '`', '/', '?', '<', '>', '|', '\\' };

	private static final RegionManager rm = new RegionManager();

	public boolean isSetting(Player p) {
		return (setting.containsKey(p.getName()) ? true : false);
	}

	public String getSettingType(Player p) {
		return setting.get(p.getName());
	}

	public boolean isModding(Player p) {
		return (modding.containsKey(p.getName()) ? true : false);
	}

	public boolean set(Player p, String[] args) {
		if(ConfigurationData.useWorldEdit)
		{
			p.sendMessage("[Regios] Command unavailable while WorldEdit mode is true");
			return true;
		}
		if (doesHaveNode(p, "regios.data.create")) {
			if (args.length == 1) {
				giveTool(p, "cuboid");
				return true;
			} else if (args.length == 2) {
				if (args[1].equalsIgnoreCase("cube") || args[1].equalsIgnoreCase("cuboid")) {
					giveTool(p, "cuboid");
					return true;
				} else if (args[1].equalsIgnoreCase("poly") || args[1].equalsIgnoreCase("polygon")) {
					giveTool(p, "polygon");
					return true;
				}
			} else {
				p.sendMessage(ChatColor.RED + "[Regios] Invalid number of arguments specified.");
				p.sendMessage("Proper usage: /regios set cube/cuboid/poly/polygon");
				return true;
			}
		} else {
			sendInvalidPerms(p);
			return true;
		}
		return false;
	}

	public boolean create(Player p, String[] args) {
		if (doesHaveNode(p, "regios.data.create")) {
			if (args.length == 2) {
				if (args[1].equalsIgnoreCase("all") || args[1].equalsIgnoreCase("placeholder") || args[1].equalsIgnoreCase("confirm")) {
					p.sendMessage(ChatColor.RED + "[Regios] " + ChatColor.BLUE + args[1] + ChatColor.RED + " is a reserved word!");
					return true;
				}
				if (ConfigurationData.useWorldEdit) {
					try {
						new WorldEditCommands().createRegionWE(p, args[1]);
					} catch (RegionNameExistsException e) {} 
					catch (RegionPointsNotSetException e) {}
					return true;
				} else {
					try {
						createRegion(p, args[1]);
					} catch (RegionNameExistsException e) {} 
					catch (RegionPointsNotSetException e) {}
					return true;
				}
			} else {
				p.sendMessage(ChatColor.RED + "[Regios] Invalid number of arguments specified.");
				p.sendMessage("Proper usage: /regios create <regionname>");
				return true;
			}
		} else {
			sendInvalidPerms(p);
			return true;
		}
	}

	public void cancel(Player p, String[] args) {
		if (doesHaveNode(p, "regios.data.create")) {
			if (args.length == 1) {
				clearAll(p);
				return;
			} else {
				p.sendMessage(ChatColor.RED + "[Regios] Invalid number of arguments specified.");
				p.sendMessage("Proper usage: /regios cancel");
				return;
			}
		} else {
			sendInvalidPerms(p);
			return;
		}
	}

	private void giveTool(Player p, String type) {
		if (isSetting(p)) {
			if (!p.getInventory().contains(new ItemStack(ConfigurationData.defaultSelectionTool, 1))) {
				ItemStack is = new ItemStack(ConfigurationData.defaultSelectionTool, 1);
				p.getInventory().addItem(is);

				if (p.getItemInHand() == new ItemStack(Material.AIR, 0)) {
					p.setItemInHand(is);
				}

			}
			p.sendMessage(ChatColor.RED + "[Regios] You are already setting a region!");
			return;
		} else {
			setting.put(p.getName(), type);
			modding.put(p.getName(), type);
		}
		if (!p.getInventory().contains(new ItemStack(ConfigurationData.defaultSelectionTool, 1))) {
			ItemStack is = new ItemStack(ConfigurationData.defaultSelectionTool, 1);

			p.getInventory().addItem(is);

			if (p.getItemInHand() == new ItemStack(Material.AIR, 0)) {
				p.setItemInHand(is);
			}

		}
		if (type.equalsIgnoreCase("cuboid")) {
			p.sendMessage(ChatColor.GREEN + "[Regios] Left and right click to select points.");
		} else if (type.equalsIgnoreCase("polygon")) {
			p.sendMessage(ChatColor.GREEN + "[Regios] Left click to add point and right click to remove last point.");
		}
	}

	public void createRegion(Player p, String name) throws RegionNameExistsException, RegionPointsNotSetException {

		if (!arePointsSet(p)) {
			if (setting.get(p.getName()).equalsIgnoreCase("cuboid")) {
				p.sendMessage(ChatColor.RED + "[Regios] You must set 2 points!");
			} else if (setting.get(p.getName()).equalsIgnoreCase("polygon")) {
				p.sendMessage(ChatColor.RED + "[Regios] You must set at least 3 points!");
			}
			throw new RegionPointsNotSetException(name);
		}
		if (setting.get(p.getName()).equalsIgnoreCase("cuboid")) {
			if (rm.createRegion(p, name, point1.get(p.getName()), point2.get(p.getName()))) {
				p.sendMessage(ChatColor.GREEN + "[Regios] Region " + ChatColor.BLUE + name + ChatColor.GREEN + " created successfully!");
				clearPoints(p);
				modding.remove(p.getName());
				setting.remove(p.getName());
			}
		} else if (setting.get(p.getName()).equalsIgnoreCase("polygon")) {
			double minY = 999999999, maxY = -999999999;
			ArrayList<Integer> xPointsA = new ArrayList<Integer>(), zPointsA = new ArrayList<Integer>();


			for (Location l : points.get(p.getName())) {
				if (l.getY() <= minY) {
					minY = l.getY();
				}

				if (l.getY() >= maxY) {
					maxY = l.getY();
				}

				xPointsA.add(l.getBlockX());
				zPointsA.add(l.getBlockZ());
			}

			int[] xPoints = new int[xPointsA.size()];
			int[] zPoints = new int[zPointsA.size()];

			Iterator<Integer> iteratorX = xPointsA.iterator();
			Iterator<Integer> iteratorZ = zPointsA.iterator();

			for (int i = 0; i < xPoints.length; i++) {
				xPoints[i] = iteratorX.next().intValue();
			}
			for (int i = 0; i < zPoints.length; i++) {
				zPoints[i] = iteratorZ.next().intValue();
			}


			if (rm.createRegion(p, name, xPoints, zPoints, xPoints.length, minY, maxY)) {
				p.sendMessage(ChatColor.GREEN + "[Regios] Region " + ChatColor.BLUE + name + ChatColor.GREEN + " created successfully!");
				clearPoints(p);
				modding.remove(p.getName());
				setting.remove(p.getName());
			}
		}
	}

	public void createBlueprint(Player p, String name) {
		if (!arePointsSet(p)) {
			p.sendMessage(ChatColor.RED + "[Regios] You must set 2 points!");
			return;
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
			return;
		}

		RBF_Core.rbf_save.startSave(null, point1.get(p.getName()), point2.get(p.getName()), name, p, true);
		clearPoints(p);
		modding.remove(p.getName());
		setting.remove(p.getName());
	}

	public static void createBlueprint(String name, Location l1, Location l2) {
		if (l1 == null || l2 == null) {
			return;
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
			return;
		}

		RBF_Core.rbf_save.startSave(null, l1, l2, name, null, true);
	}

	public boolean arePointsSet(Player p) {
		if (setting.containsKey(p.getName())) {
			if (setting.get(p.getName()).equalsIgnoreCase("cuboid")) {
				return point1.containsKey(p.getName()) && point2.containsKey(p.getName());
			} else if (setting.get(p.getName()).equalsIgnoreCase("polygon")) {
				return points.get(p.getName()).size() >= 3;
			}
		}
		return false;
	}

	public boolean areModPointsSet(Player p) {
		return mod1.containsKey(p.getName()) && mod2.containsKey(p.getName());
	}

	public void expandMaxSelection(Player p) {
		if(doesHaveNode(p, "regios.modify.expand")) {
			if (ConfigurationData.useWorldEdit)
			{
				p.sendMessage("Command unavailable while WorldEdit mode is true.");
				return;
			}
			else if (arePointsSet(p)) {
				if (setting.get(p.getName()).equalsIgnoreCase("cuboid")) {
					point1.put(p.getName(), (new Location(p.getWorld(), point1.get(p.getName()).getX(), 0, point1.get(p.getName()).getZ())));
					point2.put(p.getName(), (new Location(p.getWorld(), point2.get(p.getName()).getX(), p.getWorld().getMaxHeight(), point2.get(p.getName()).getZ())));
					p.sendMessage(ChatColor.GREEN + "[Regios] Selection expanded from bedrock to sky.");
					return;
				} else if (setting.get(p.getName()).equalsIgnoreCase("polygon")) {
					points.get(p.getName()).get(points.get(p.getName()).size() - 1).setY(p.getWorld().getMaxHeight());
					points.get(p.getName()).get(points.get(p.getName()).size() - 2).setY(0);
					p.sendMessage(ChatColor.GREEN + "[Regios] Selection expanded from bedrock to sky.");
					return;
				}
			} else if (areModPointsSet(p)) {
				if (modding.get(p.getName()).equalsIgnoreCase("cuboid")) {
					mod1.put(p.getName(), (new Location(p.getWorld(), mod1.get(p.getName()).getX(), 0, mod1.get(p.getName()).getZ())));
					mod2.put(p.getName(), (new Location(p.getWorld(), mod2.get(p.getName()).getX(), p.getWorld().getMaxHeight(), mod2.get(p.getName()).getZ())));
					p.sendMessage(ChatColor.GREEN + "[Regios] Selection expanded from bedrock to sky.");
					return;
				} else if (modding.get(p.getName()).equalsIgnoreCase("cuboid")) {
					//TODO: Implement polygon modification
					p.sendMessage("[Regios] Not implemented yet. Sorry! :(");
				}
			} else {
				p.sendMessage(ChatColor.RED + "[Regios] You must set 2 points for cuboid selections or 3 for polygonal!");
				return;
			}
		} else {
			sendInvalidPerms(p);
		}
	}

	public void addPoint(Player p, Location l) {
		if (p.getItemInHand().getType() == ConfigurationData.defaultSelectionTool) {
			try {
				if (!points.get(p.getName()).contains(l)) {
					points.get(p.getName()).add(l);
					p.sendMessage(ChatColor.GREEN + "[Regios]" + ChatColor.LIGHT_PURPLE
							+ String.format("X : %d, Y : %d, Z : %d ", l.getBlockX(), l.getBlockY(), l.getBlockZ()) + ChatColor.BLUE + "added.");
				}
			} catch (NullPointerException npe) {
				points.put(p.getName(), new ArrayList<Location>());

				if (!points.get(p.getName()).contains(l)) {
					points.get(p.getName()).add(l);
					p.sendMessage(ChatColor.GREEN + "[Regios]" + ChatColor.LIGHT_PURPLE
							+ String.format("X : %d, Y : %d, Z : %d ", l.getBlockX(), l.getBlockY(), l.getBlockZ()) + ChatColor.BLUE + "added.");
				}
			}
		}
	}

	public void removeLastPoint(Player p) {
		if (p.getItemInHand().getType() == ConfigurationData.defaultSelectionTool) {
			Location l = points.get(p.getName()).get(points.get(p.getName()).size() - 1);
			points.get(p.getName()).remove(l);
			p.sendMessage(ChatColor.GREEN + "[Regios]" + ChatColor.LIGHT_PURPLE
					+ String.format("X : %d, Y : %d, Z : %d ", l.getBlockX(), l.getBlockY(), l.getBlockZ()) + ChatColor.BLUE + "removed.");
		}
	}

	public void addMPoint(Player p, Location l) {
		if (p.getItemInHand().getType() == ConfigurationData.defaultSelectionTool) {
			try {
				if (!mpoints.get(p.getName()).contains(l)) {
					mpoints.get(p.getName()).add(l);
					p.sendMessage(ChatColor.GREEN + "[Regios]" + ChatColor.LIGHT_PURPLE
							+ String.format("X : %d, Y : %d, Z : %d ", l.getBlockX(), l.getBlockY(), l.getBlockZ()) + ChatColor.BLUE + "added.");
				}
			} catch (NullPointerException npe) {
				points.put(p.getName(), new ArrayList<Location>());

				if (!mpoints.get(p.getName()).contains(l)) {
					mpoints.get(p.getName()).add(l);
					p.sendMessage(ChatColor.GREEN + "[Regios]" + ChatColor.LIGHT_PURPLE
							+ String.format("X : %d, Y : %d, Z : %d ", l.getBlockX(), l.getBlockY(), l.getBlockZ()) + ChatColor.BLUE + "added.");
				}
			}
		}
	}

	public void removeLastMPoint(Player p) {
		if (p.getItemInHand().getType() == ConfigurationData.defaultSelectionTool) {
			Location l = mpoints.get(p.getName()).get(points.get(p.getName()).size() - 1);
			mpoints.get(p.getName()).remove(l);
			p.sendMessage(ChatColor.GREEN + "[Regios]" + ChatColor.LIGHT_PURPLE
					+ String.format("X : %d, Y : %d, Z : %d ", l.getBlockX(), l.getBlockY(), l.getBlockZ()) + ChatColor.BLUE + "removed.");
		}
	}

	public void setFirst(Player p, Location l) {
		if (p.getItemInHand().getType() == ConfigurationData.defaultSelectionTool) {
			point1.put(p.getName(), l);
			p.sendMessage(ChatColor.GREEN + "[Regios]" + ChatColor.BLUE + "[1] " + ChatColor.LIGHT_PURPLE
					+ String.format("X : %d, Y : %d, Z : %d", l.getBlockX(), l.getBlockY(), l.getBlockZ()));
		}
	}

	public void setSecond(Player p, Location l) {
		if (p.getItemInHand().getType() == ConfigurationData.defaultSelectionTool) {
			point2.put(p.getName(), l);
			p.sendMessage(ChatColor.GREEN + "[Regios]" + ChatColor.BLUE + "[2] " + ChatColor.LIGHT_PURPLE
					+ String.format("X : %d, Y : %d, Z : %d", l.getBlockX(), l.getBlockY(), l.getBlockZ()));
		}
	}

	public void setFirstMod(Player p, Location l) {
		if (p.getItemInHand().getType() == ConfigurationData.defaultSelectionTool) {
			mod1.put(p.getName(), l);
			p.sendMessage(ChatColor.GREEN + "[Regios]" + ChatColor.BLUE + "[1] " + ChatColor.LIGHT_PURPLE
					+ String.format("X : %d, Y : %d, Z : %d", l.getBlockX(), l.getBlockY(), l.getBlockZ()));
		}
	}

	public void setSecondMod(Player p, Location l) {
		if (p.getItemInHand().getType() == ConfigurationData.defaultSelectionTool) {
			mod2.put(p.getName(), l);
			p.sendMessage(ChatColor.GREEN + "[Regios]" + ChatColor.BLUE + "[2] " + ChatColor.LIGHT_PURPLE
					+ String.format("X : %d, Y : %d, Z : %d", l.getBlockX(), l.getBlockY(), l.getBlockZ()));
		}
	}

	public void clearAll(Player p) {
		clearPoints(p);
		if (mod1.containsKey(p.getName())) {
			mod1.remove(p.getName());
		}
		if (mod2.containsKey(p.getName())) {
			mod2.remove(p.getName());
		}
		if (setting.containsKey(p.getName())) {
			setting.remove(p.getName());
		}
		if (modding.containsKey(p.getName())) {
			modding.remove(p.getName());
		}
		if (RegiosPlayerListener.loadingTerrain.containsKey(p.getName())) {
			RegiosPlayerListener.loadingTerrain.remove(p.getName());
		}
		p.sendMessage(ChatColor.RED + "[Regios] Region setting cancelled.");
	}

	public static void clearPoints(Player p) {
		if (point1.containsKey(p.getName())) {
			point1.remove(p.getName());
		}
		if (point2.containsKey(p.getName())) {
			point2.remove(p.getName());
		}
		if (points.containsKey(p.getName())) {
			points.remove(p.getName());
		}
		if (mpoints.containsKey(p.getName())) {
			mpoints.remove(p.getName());
		}
	}
}
