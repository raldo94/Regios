package net.jzx7.regios.WorldEdit.Commands;

import net.jzx7.regios.Permissions.PermissionsCore;
import net.jzx7.regios.RBF.RBF_Core;
import net.jzx7.regios.WorldEdit.WorldEditInterface;
import net.jzx7.regios.regions.RegionManager;
import net.jzx7.regiosapi.exceptions.RegionNameExistsException;
import net.jzx7.regiosapi.exceptions.RegionPointsNotSetException;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.worldedit.BlockVector2D;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import com.sk89q.worldedit.bukkit.selections.Polygonal2DSelection;
import com.sk89q.worldedit.bukkit.selections.Selection;


public class WorldEditCommands extends PermissionsCore {

	private static char[] invalidModifiers = { '!', '\'', '£', '$', '%', '^', '&', '*', '¬', '`', '/', '?', '<', '>', '|', '\\' };
	
	private RegionManager rm = new RegionManager();

	public void createRegionWE(Player p, String name) throws RegionNameExistsException, RegionPointsNotSetException {
		if (rm.doesRegionExist(name)) {
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
			p.sendMessage(ChatColor.RED + "[Regios] Name contained invalid characters : " + invalidName.toString());
			return;
		}

		WorldEditPlugin worldEdit = null;
		try {
			worldEdit = WorldEditInterface.getWorldEdit();
		} catch (CommandException e) {
			p.sendMessage(e.getMessage());
		}

		// Attempt to get the player's selection from WorldEdit
		Selection sel = worldEdit.getSelection(p);

		if(sel == null)
		{
			try {
				throw new CommandException("Select a region with WorldEdit first.");
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
			rm.createRegion(p, name, sel.getMinimumPoint(), sel.getMaximumPoint());
		} else {
			try {
				throw new CommandException("The type of region selected in WorldEdit is unsupported in Regios");
			} catch (CommandException e) {
				p.sendMessage(e.getMessage());
			}
		}
	}

	public void createBlueprintWE(Player p, String name) {
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

		WorldEditPlugin worldEdit = null;
		try {
			worldEdit = WorldEditInterface.getWorldEdit();
		} catch (CommandException e) {
			p.sendMessage(e.getMessage());
			return;
		}

		// Attempt to get the player's selection from WorldEdit
		Selection sel = worldEdit.getSelection(p);

		if(sel == null)
		{
			try {
				throw new CommandException("Select a region with WorldEdit first.");
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
			RBF_Core.blueprint.startSave(sel.getMinimumPoint(), sel.getMaximumPoint(), name, p);
		} else {
			try {
				throw new CommandException("The type of region selected in WorldEdit is unsupported in Regios");
			} catch (CommandException e) {
				p.sendMessage(e.getMessage());
				return;
			}
		}
	}

	public void createSchematicWE(Player p, String name) {
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

		WorldEditPlugin worldEdit = null;
		try {
			worldEdit = WorldEditInterface.getWorldEdit();
		} catch (CommandException e) {
			p.sendMessage(e.getMessage());
			return;
		}

		// Attempt to get the player's selection from WorldEdit
		Selection sel = worldEdit.getSelection(p);

		if(sel == null)
		{
			try {
				throw new CommandException("Select a region with WorldEdit first.");
			} catch (CommandException e) {
				p.sendMessage(e.getMessage());
				return;
			}
		}

		if (sel instanceof Polygonal2DSelection) {
			p.sendMessage("Sorry, schematics don't support non-cuboid regions!");
		} else if (sel instanceof CuboidSelection) {
			RBF_Core.schematic.startSave(sel.getMinimumPoint(), sel.getMaximumPoint(), name, p);
		} else {
			try {
				throw new CommandException("The type of region selected in WorldEdit is unsupported in Regios");
			} catch (CommandException e) {
				p.sendMessage(e.getMessage());
				return;
			}
		}
	}
}