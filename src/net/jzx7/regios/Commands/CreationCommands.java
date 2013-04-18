package net.jzx7.regios.Commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import net.jzx7.regios.Data.ConfigurationData;
import net.jzx7.regios.Permissions.PermissionsCore;
import net.jzx7.regios.RBF.RBF_Core;
import net.jzx7.regios.WorldEdit.Commands.WorldEditCommands;
import net.jzx7.regios.entity.PlayerManager;
import net.jzx7.regios.messages.CMDText;
import net.jzx7.regios.messages.Message;
import net.jzx7.regios.messages.MsgFormat;
import net.jzx7.regios.regions.RegionManager;
import net.jzx7.regiosapi.entity.RegiosPlayer;
import net.jzx7.regiosapi.exceptions.RegionNameExistsException;
import net.jzx7.regiosapi.exceptions.RegionPointsNotSetException;
import net.jzx7.regiosapi.inventory.RegiosItemStack;
import net.jzx7.regiosapi.location.RegiosPoint;
import net.jzx7.regiosapi.regions.Region;

public class CreationCommands extends PermissionsCore {

	public static HashMap<String, RegiosPoint> point1 = new HashMap<String, RegiosPoint>();
	public static HashMap<String, RegiosPoint> point2 = new HashMap<String, RegiosPoint>();
	public static HashMap<String, ArrayList<RegiosPoint>> regiosPoints = new HashMap<String, ArrayList<RegiosPoint>>();
	public static HashMap<String, ArrayList<RegiosPoint>> mpoints = new HashMap<String, ArrayList<RegiosPoint>>();

	public static HashMap<String, String> setting = new HashMap<String, String>();
	public static HashMap<String, String> modding = new HashMap<String, String>();

	public static HashMap<String, RegiosPoint> mod1 = new HashMap<String, RegiosPoint>();
	public static HashMap<String, RegiosPoint> mod2 = new HashMap<String, RegiosPoint>();

	public static HashMap<String, Region> modRegion = new HashMap<String, Region>();

	private static char[] invalidModifiers = { '!', '\'', '£', '$', '%', '^', '&', '*', '¬', '`', '/', '?', '<', '>', '|', '\\' };

	private static final RegionManager rm = new RegionManager();
	private static final PlayerManager pm = new PlayerManager();

	public boolean isSetting(RegiosPlayer p) {
		return (setting.containsKey(p.getName()) ? true : false);
	}

	public String getSettingType(RegiosPlayer p) {
		return setting.get(p.getName());
	}

	public boolean isModding(RegiosPlayer p) {
		return (modding.containsKey(p.getName()) ? true : false);
	}

	public boolean set(RegiosPlayer p, String[] args) {
		if(ConfigurationData.useWorldEdit)
		{
			p.sendMessage(Message.WORLDEDITMODETRUE.getMessage());
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
				p.sendMessage(Message.INVALIDARGUMENTCOUNT.getMessage());
				p.sendMessage(Message.PROPERUSAGE.getMessage() + CMDText.CREATE.getText());
				return true;
			}
		} else {
			sendInvalidPerms(p);
			return true;
		}
		return false;
	}

	public boolean create(RegiosPlayer p, String[] args) {
		if (doesHaveNode(p, "regios.data.create")) {
			if (args.length == 2) {
				if (args[1].equalsIgnoreCase("all") || args[1].equalsIgnoreCase("placeholder") || args[1].equalsIgnoreCase("confirm")) {
					p.sendMessage(Message.RESERVEDWORD.getMessage() + MsgFormat.colourFormat("<BLUE>" + args[1]));
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
				p.sendMessage(Message.INVALIDARGUMENTCOUNT.getMessage());
				p.sendMessage(Message.PROPERUSAGE.getMessage() + CMDText.CREATE.getText());
				return true;
			}
		} else {
			sendInvalidPerms(p);
			return true;
		}
	}

	public void cancel(RegiosPlayer p, String[] args) {
		if (doesHaveNode(p, "regios.data.create")) {
			if (args.length == 1) {
				clearAll(p);
				return;
			} else {
				p.sendMessage(Message.INVALIDARGUMENTCOUNT.getMessage());
				p.sendMessage(Message.PROPERUSAGE.getMessage() + CMDText.CANCEL.getText());
				return;
			}
		} else {
			sendInvalidPerms(p);
			return;
		}
	}

	private void giveTool(RegiosPlayer p, String type) {
		if (isSetting(p)) {
			if (!p.inventoryContains(new RegiosItemStack(ConfigurationData.defaultSelectionTool, 1))) {
				RegiosItemStack is = new RegiosItemStack(ConfigurationData.defaultSelectionTool, 1);
				p.addItem(is);

				if (p.getItemInHand() == new RegiosItemStack(0,0)) {
					p.setItemInHand(is);
				}

			}
			p.sendMessage(Message.PLAYERALREADYSETTINGPOINTS.getMessage());
			return;
		} else {
			setting.put(p.getName(), type);
			modding.put(p.getName(), type);
		}
		if (!p.inventoryContains(new RegiosItemStack(ConfigurationData.defaultSelectionTool, 1))) {
			RegiosItemStack is = new RegiosItemStack(ConfigurationData.defaultSelectionTool, 1);

			p.addItem(is);

			if (p.getItemInHand() == new RegiosItemStack(0,0)) {
				p.setItemInHand(is);
			}

		}
		if (type.equalsIgnoreCase("cuboid")) {
			p.sendMessage(Message.PLAYERSETCUBOIDINSTRUCT.getMessage());
		} else if (type.equalsIgnoreCase("polygon")) {
			p.sendMessage(Message.PLAYERSETPOLYGONINSTRUCT.getMessage());
		}
	}

	public void createRegion(RegiosPlayer p, String name) throws RegionNameExistsException, RegionPointsNotSetException {

		if (!arePointsSet(p)) {
			if (setting.containsKey(p.getName())) {
				if (setting.get(p.getName()).equalsIgnoreCase("cuboid")) {
					p.sendMessage(Message.REGIONCUBEPOINTSNOTSET.getMessage());
				} else if (setting.get(p.getName()).equalsIgnoreCase("polygon")) {
					p.sendMessage(Message.REGIONPOLYPOINTSNOTSET.getMessage());
				}
			} else {
				p.sendMessage(Message.REGIONPOINTSNOTSET.getMessage());
			}
			throw new RegionPointsNotSetException(name);
		}
		if (setting.get(p.getName()).equalsIgnoreCase("cuboid")) {
			if (rm.createRegion(p, name, point1.get(p.getName()), point2.get(p.getName()))) {
				p.sendMessage(MsgFormat.colourFormat(Message.REGIONCREATEDSUCCESS.getMessage() + "<BLUE>" + name));
				clearPoints(p);
				modding.remove(p.getName());
				setting.remove(p.getName());
			}
		} else if (setting.get(p.getName()).equalsIgnoreCase("polygon")) {
			double minY = 999999999, maxY = -999999999;
			ArrayList<Integer> xPointsA = new ArrayList<Integer>(), zPointsA = new ArrayList<Integer>();


			for (RegiosPoint l : regiosPoints.get(p.getName())) {
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
				p.sendMessage(MsgFormat.colourFormat(Message.REGIONCREATEDSUCCESS.getMessage() + "<BLUE>" + name));
				clearPoints(p);
				modding.remove(p.getName());
				setting.remove(p.getName());
			}
		}
	}

	public void createBlueprint(RegiosPlayer p, String name) {
		if (!arePointsSet(p)) {
			p.sendMessage(Message.REGIONCUBEPOINTSNOTSET.getMessage());
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
				invalidName.append(MsgFormat.colourFormat("<RED>"+ch));
			} else {
				invalidName.append(MsgFormat.colourFormat("<BGREEN>"+ch));
			}
		}

		if (!integrity) {
			p.sendMessage(Message.INVALIDCHARACTERS.getMessage() + invalidName.toString());
			return;
		}

		RBF_Core.blueprint.startSave(point1.get(p.getName()), point2.get(p.getName()), name, p);
		clearPoints(p);
		modding.remove(p.getName());
		setting.remove(p.getName());
	}

	public static void createBlueprint(String name, RegiosPoint l1, RegiosPoint l2, RegiosPlayer p) {
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
				invalidName.append(MsgFormat.colourFormat("<RED>"+ch));
			} else {
				invalidName.append(MsgFormat.colourFormat("<BGREEN>"+ch));
			}
		}

		if (!integrity) {
			p.sendMessage(Message.INVALIDCHARACTERS.getMessage() + invalidName.toString());
			return;
		}

		RBF_Core.blueprint.startSave(l1, l2, name, p);
	}

	public void createSchematic(RegiosPlayer p, String name) {
		if (!arePointsSet(p)) {
			p.sendMessage(Message.REGIONCUBEPOINTSNOTSET.getMessage());
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
				invalidName.append(MsgFormat.colourFormat("<RED>"+ch));
			} else {
				invalidName.append(MsgFormat.colourFormat("<BGREEN>"+ch));
			}
		}

		if (!integrity) {
			p.sendMessage(Message.INVALIDCHARACTERS.getMessage() + invalidName.toString());
			return;
		}

		RBF_Core.schematic.startSave(point1.get(p.getName()), point2.get(p.getName()), name, p);
		clearPoints(p);
		modding.remove(p.getName());
		setting.remove(p.getName());
	}

	public boolean arePointsSet(RegiosPlayer p) {
		if (setting.containsKey(p.getName())) {
			if (setting.get(p.getName()).equalsIgnoreCase("cuboid")) {
				return point1.containsKey(p.getName()) && point2.containsKey(p.getName());
			} else if (setting.get(p.getName()).equalsIgnoreCase("polygon")) {
				return regiosPoints.get(p.getName()).size() >= 3;
			}
		}
		return false;
	}

	public boolean areModPointsSet(RegiosPlayer p) {
		return mod1.containsKey(p.getName()) && mod2.containsKey(p.getName());
	}

	public void expandMaxSelection(RegiosPlayer p) {
		if(doesHaveNode(p, "regios.modify.expand")) {
			if (ConfigurationData.useWorldEdit)
			{
				p.sendMessage(Message.WORLDEDITMODETRUE.getMessage());
				return;
			}
			else if (arePointsSet(p)) {
				if (setting.get(p.getName()).equalsIgnoreCase("cuboid")) {
					point1.put(p.getName(), (new RegiosPoint(p.getRegiosWorld(), point1.get(p.getName()).getX(), 0, point1.get(p.getName()).getZ())));
					point2.put(p.getName(), (new RegiosPoint(p.getRegiosWorld(), point2.get(p.getName()).getX(), p.getRegiosWorld().getMaxHeight(), point2.get(p.getName()).getZ())));
					p.sendMessage(Message.REGIONEXPANDMAX.getMessage());
					return;
				} else if (setting.get(p.getName()).equalsIgnoreCase("polygon")) {
					regiosPoints.get(p.getName()).get(regiosPoints.get(p.getName()).size() - 1).setY(p.getRegiosWorld().getMaxHeight());
					regiosPoints.get(p.getName()).get(regiosPoints.get(p.getName()).size() - 2).setY(0);
					p.sendMessage(Message.REGIONEXPANDMAX.getMessage());
					return;
				}
			} else if (areModPointsSet(p)) {
				if (modding.get(p.getName()).equalsIgnoreCase("cuboid")) {
					mod1.put(p.getName(), (new RegiosPoint(p.getRegiosWorld(), mod1.get(p.getName()).getX(), 0, mod1.get(p.getName()).getZ())));
					mod2.put(p.getName(), (new RegiosPoint(p.getRegiosWorld(), mod2.get(p.getName()).getX(), p.getRegiosWorld().getMaxHeight(), mod2.get(p.getName()).getZ())));
					p.sendMessage(Message.REGIONEXPANDMAX.getMessage());
					return;
				} else if (modding.get(p.getName()).equalsIgnoreCase("polygon")) {
					//TODO: Implement polygon modification
					p.sendMessage(Message.NOTIMPLEMENTED.getMessage());
				}
			} else {
				p.sendMessage(Message.REGIONPOINTSNOTSET.getMessage());
				return;
			}
		} else {
			sendInvalidPerms(p);
		}
	}

	public void addPoint(RegiosPlayer p, RegiosPoint l) {
		if (p.getItemInHand().getId() == ConfigurationData.defaultSelectionTool) {
			try {
				if (!regiosPoints.get(p.getName()).contains(l)) {
					regiosPoints.get(p.getName()).add(l);
					p.sendMessage(MsgFormat.colourFormat("[Regios] <PINK>" + String.format("X : %d, Y : %d, Z : %d ", l.getBlockX(), l.getBlockY(), l.getBlockZ()) + "<BLUE>added."));
				}
			} catch (NullPointerException npe) {
				regiosPoints.put(p.getName(), new ArrayList<RegiosPoint>());
				addPoint(p, l);
			}
		}
	}

	public void removeLastPoint(RegiosPlayer p) {
		if (p.getItemInHand().getId() == ConfigurationData.defaultSelectionTool) {
			RegiosPoint l = regiosPoints.get(p.getName()).get(regiosPoints.get(p.getName()).size() - 1);
			regiosPoints.get(p.getName()).remove(l);
			p.sendMessage(MsgFormat.colourFormat("[Regios] <PINK>" + String.format("X : %d, Y : %d, Z : %d ", l.getBlockX(), l.getBlockY(), l.getBlockZ()) + "<BLUE>removed."));
		}
	}

	public void addMPoint(RegiosPlayer p, RegiosPoint l) {
		if (p.getItemInHand().getId() == ConfigurationData.defaultSelectionTool) {
			try {
				if (!mpoints.get(p.getName()).contains(l)) {
					mpoints.get(p.getName()).add(l);
					p.sendMessage(MsgFormat.colourFormat("[Regios] <PINK>" + String.format("X : %d, Y : %d, Z : %d ", l.getBlockX(), l.getBlockY(), l.getBlockZ()) + "<BLUE>added."));
				}
			} catch (NullPointerException npe) {
				regiosPoints.put(p.getName(), new ArrayList<RegiosPoint>());
				addMPoint(p, l);
			}
		}
	}

	public void removeLastMPoint(RegiosPlayer p) {
		if (p.getItemInHand().getId() == ConfigurationData.defaultSelectionTool) {
			RegiosPoint l = mpoints.get(p.getName()).get(regiosPoints.get(p.getName()).size() - 1);
			mpoints.get(p.getName()).remove(l);
			p.sendMessage(MsgFormat.colourFormat("[Regios] <PINK>" + String.format("X : %d, Y : %d, Z : %d ", l.getBlockX(), l.getBlockY(), l.getBlockZ()) + "<BLUE>removed."));
		}
	}

	public void setFirst(RegiosPlayer p, RegiosPoint l) {
		if (p.getItemInHand().getId() == ConfigurationData.defaultSelectionTool) {
			point1.put(p.getName(), l);
			p.sendMessage(MsgFormat.colourFormat("[Regios] <BLUE>[1] <PINK>" + String.format("X : %d, Y : %d, Z : %d", l.getBlockX(), l.getBlockY(), l.getBlockZ())));
		}
	}

	public void setSecond(RegiosPlayer p, RegiosPoint l) {
		if (p.getItemInHand().getId() == ConfigurationData.defaultSelectionTool) {
			point2.put(p.getName(), l);
			p.sendMessage(MsgFormat.colourFormat("[Regios] <BLUE>[2] <PINK>" + String.format("X : %d, Y : %d, Z : %d", l.getBlockX(), l.getBlockY(), l.getBlockZ())));
		}
	}

	public void setFirstMod(RegiosPlayer p, RegiosPoint l) {
		if (p.getItemInHand().getId() == ConfigurationData.defaultSelectionTool) {
			mod1.put(p.getName(), l);
			p.sendMessage(MsgFormat.colourFormat("[Regios] <BLUE>[1] <PINK>" + String.format("X : %d, Y : %d, Z : %d", l.getBlockX(), l.getBlockY(), l.getBlockZ())));
		}
	}

	public void setSecondMod(RegiosPlayer p, RegiosPoint l) {
		if (p.getItemInHand().getId() == ConfigurationData.defaultSelectionTool) {
			mod2.put(p.getName(), l);
			p.sendMessage(MsgFormat.colourFormat("[Regios] <BLUE>[2] <PINK>" + String.format("X : %d, Y : %d, Z : %d", l.getBlockX(), l.getBlockY(), l.getBlockZ())));
		}
	}

	public void clearAll(RegiosPlayer p) {
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
		if (pm.getLoadingTerrain().containsKey(p.getName())) {
			pm.getLoadingTerrain().remove(p.getName());
		}
		p.sendMessage(Message.REGIONSETTINGCANCELLED.getMessage());
	}

	public static void clearPoints(RegiosPlayer p) {
		if (point1.containsKey(p.getName())) {
			point1.remove(p.getName());
		}
		if (point2.containsKey(p.getName())) {
			point2.remove(p.getName());
		}
		if (regiosPoints.containsKey(p.getName())) {
			regiosPoints.remove(p.getName());
		}
		if (mpoints.containsKey(p.getName())) {
			mpoints.remove(p.getName());
		}
	}
}
