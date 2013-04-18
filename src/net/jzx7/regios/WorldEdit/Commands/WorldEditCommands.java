package net.jzx7.regios.WorldEdit.Commands;

import net.jzx7.regios.Permissions.PermissionsCore;
import net.jzx7.regios.RBF.RBF_Core;
import net.jzx7.regios.WorldEdit.WorldEditInterface;
import net.jzx7.regios.messages.Message;
import net.jzx7.regios.messages.MsgFormat;
import net.jzx7.regios.regions.RegionManager;
import net.jzx7.regios.util.RegiosConversions;
import net.jzx7.regiosapi.entity.RegiosPlayer;
import net.jzx7.regiosapi.exceptions.RegionNameExistsException;
import net.jzx7.regiosapi.exceptions.RegionPointsNotSetException;

import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.worldedit.BlockVector2D;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import com.sk89q.worldedit.bukkit.selections.Polygonal2DSelection;
import com.sk89q.worldedit.bukkit.selections.Selection;

public class WorldEditCommands extends PermissionsCore {

	private static char[] invalidModifiers = { '!', '\'', '£', '$', '%', '^', '&', '*', '¬', '`', '/', '?', '<', '>', '|', '\\' };
	
	private RegionManager rm = new RegionManager();

	public void createRegionWE(RegiosPlayer p, String name) throws RegionNameExistsException, RegionPointsNotSetException {
		if (rm.doesRegionExist(name)) {
			p.sendMessage(Message.REGIONALREADYEXISTS.getMessage() + MsgFormat.colourFormat("<BLUE>" + name));
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
				invalidName.append("<RED>").append(ch);
			} else {
				invalidName.append("<DGREEN>").append(ch);
			}
		}

		if (!integrity) {
			p.sendMessage(Message.INVALIDCHARACTERS.getMessage() + MsgFormat.colourFormat(invalidName.toString()));
			return;
		}

		WorldEditPlugin worldEdit = null;
		try {
			worldEdit = WorldEditInterface.getWorldEdit();
		} catch (CommandException e) {
			p.sendMessage(e.getMessage());
		}

		// Attempt to get the player's selection from WorldEdit
		Selection sel = worldEdit.getSelection(RegiosConversions.getPlayer(p));

		if(sel == null)
		{
			try {
				throw new CommandException(Message.WORLDEDITSELECTREGIONFIRST.getMessage());
			} catch (CommandException e) {
				p.sendMessage(e.getMessage());
			}
		}

		// Detect the type of region from WorldEdit
		if (sel instanceof Polygonal2DSelection) {
			int nPoints = ((Polygonal2DSelection) sel).getNativePoints().size();
			int[] xPoints = new int[nPoints];
			int[] zPoints = new int[nPoints];
			
			int i = 0;
			for (BlockVector2D bv : ((Polygonal2DSelection) sel).getNativePoints()) {
				xPoints[i] = bv.getBlockX();
				zPoints[i] = bv.getBlockZ();
				i++;
			}
			
			rm.createRegion(p, name, xPoints, zPoints, nPoints, sel.getNativeMinimumPoint().getBlockY(), sel.getNativeMaximumPoint().getBlockY());
			
		} else if (sel instanceof CuboidSelection) {
			rm.createRegion(p, name, RegiosConversions.getPoint(sel.getMinimumPoint()), RegiosConversions.getPoint(sel.getMaximumPoint()));
		} else {
			try {
				throw new CommandException(Message.WORLDEDITREGIONUNSUPPORTED.getUnformattedMessage());
			} catch (CommandException e) {
				p.sendMessage(e.getMessage());
			}
		}
	}

	public void createBlueprintWE(RegiosPlayer p, String name) {
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
				invalidName.append("<RED>").append(ch);
			} else {
				invalidName.append("<DGREEN>").append(ch);
			}
		}

		if (!integrity) {
			p.sendMessage(Message.INVALIDCHARACTERS.getMessage() + MsgFormat.colourFormat(invalidName.toString()));
			return;
		}

		WorldEditPlugin worldEdit = null;
		try {
			worldEdit = WorldEditInterface.getWorldEdit();
		} catch (CommandException e) {
			p.sendMessage(e.getMessage());
			return;
		}

		// Attempt to get the player's selection from WorldEdit
		Selection sel = worldEdit.getSelection(RegiosConversions.getPlayer(p));

		if(sel == null)
		{
			try {
				throw new CommandException(Message.WORLDEDITSELECTREGIONFIRST.getMessage());
			} catch (CommandException e) {
				p.sendMessage(e.getMessage());
				return;
			}
		}

		if (sel instanceof Polygonal2DSelection) {
			int nPoints = ((Polygonal2DSelection) sel).getNativePoints().size();
			int[] xPoints = new int[nPoints];
			int[] zPoints = new int[nPoints];
			
			int i = 0;
			for (BlockVector2D bv : ((Polygonal2DSelection) sel).getNativePoints()) {
				xPoints[i] = bv.getBlockX();
				zPoints[i] = bv.getBlockZ();
				i++;
			}
			RBF_Core.blueprint.startSave(xPoints, zPoints, nPoints, sel.getNativeMinimumPoint().getBlockY(), sel.getNativeMaximumPoint().getBlockY(), name, p, true);
		} else if (sel instanceof CuboidSelection) {
			RBF_Core.blueprint.startSave(RegiosConversions.getPoint(sel.getMinimumPoint()), RegiosConversions.getPoint(sel.getMaximumPoint()), name, p);
		} else {
			try {
				throw new CommandException(Message.WORLDEDITREGIONUNSUPPORTED.getUnformattedMessage());
			} catch (CommandException e) {
				p.sendMessage(e.getMessage());
				return;
			}
		}
	}

	public void createSchematicWE(RegiosPlayer p, String name) {
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
				invalidName.append("<RED>").append(ch);
			} else {
				invalidName.append("<DGREEN>").append(ch);
			}
		}

		if (!integrity) {
			p.sendMessage(Message.INVALIDCHARACTERS.getMessage() + MsgFormat.colourFormat(invalidName.toString()));
			return;
		}

		WorldEditPlugin worldEdit = null;
		try {
			worldEdit = WorldEditInterface.getWorldEdit();
		} catch (CommandException e) {
			p.sendMessage(e.getMessage());
			return;
		}

		// Attempt to get the player's selection from WorldEdit
		Selection sel = worldEdit.getSelection(RegiosConversions.getPlayer(p));

		if(sel == null)
		{
			try {
				throw new CommandException(Message.WORLDEDITSELECTREGIONFIRST.getMessage());
			} catch (CommandException e) {
				p.sendMessage(e.getMessage());
				return;
			}
		}

		if (sel instanceof Polygonal2DSelection) {
			p.sendMessage("Sorry, schematics don't support non-cuboid regions!");
		} else if (sel instanceof CuboidSelection) {
			RBF_Core.schematic.startSave(RegiosConversions.getPoint(sel.getMinimumPoint()), RegiosConversions.getPoint(sel.getMaximumPoint()), name, p);
		} else {
			try {
				throw new CommandException(Message.WORLDEDITREGIONUNSUPPORTED.getMessage());
			} catch (CommandException e) {
				p.sendMessage(e.getMessage());
				return;
			}
		}
	}
}