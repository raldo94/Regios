package couk.Adamki11s.Regios.WorldEdit.Commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import com.sk89q.worldedit.bukkit.selections.Polygonal2DSelection;
import com.sk89q.worldedit.bukkit.selections.Selection;

import couk.Adamki11s.Regios.CustomExceptions.RegionNameExistsException;
import couk.Adamki11s.Regios.Permissions.PermissionsCore;
import couk.Adamki11s.Regios.RBF.RBF_Core;
import couk.Adamki11s.Regios.Regions.CubeRegion;
import couk.Adamki11s.Regios.Regions.GlobalRegionManager;
import couk.Adamki11s.Regios.Restrictions.RestrictionParameters;
import couk.Adamki11s.Regios.WorldEdit.WorldEditInterface;

public class WorldEditCommands extends PermissionsCore {

	private static char[] invalidModifiers = { '!', '\'', '£', '$', '%', '^', '&', '*', '¬', '`', '/', '?', '<', '>', '|', '\\' };

	public void createRegionWE(Player p, String name) throws RegionNameExistsException, CommandException {
		if (GlobalRegionManager.doesRegionExist(name)) {
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

		WorldEditPlugin worldEdit = WorldEditInterface.getWorldEdit();

		// Attempt to get the player's selection from WorldEdit
		Selection sel = worldEdit.getSelection(p);

		if(sel == null)
		{
			throw new CommandException("Select a region with WorldEdit first.");
		}

		// Detect the type of region from WorldEdit
		if (sel instanceof Polygonal2DSelection) {
			throw new CommandException("Regios doesn't support polygonal regions.... yet :D");
		} else if (sel instanceof CuboidSelection) {
			int rCount = GlobalRegionManager.getOwnedRegions(p.getName());

			RestrictionParameters params = RestrictionParameters.getRestrictions(p);

			if(sel.getWidth() > params.getRegionWidthLimit()){
				p.sendMessage(ChatColor.RED + "[Regios] You cannot create a region of this width!");
				p.sendMessage(ChatColor.RED + "[Regios] Maximum width : " + ChatColor.BLUE + params.getRegionWidthLimit() + ChatColor.RED + ", your width : " + ChatColor.BLUE + sel.getWidth());
				return;
			}

			if(sel.getHeight() > params.getRegionHeightLimit()){
				p.sendMessage(ChatColor.RED + "[Regios] You cannot create a region of this height!");
				p.sendMessage(ChatColor.RED + "[Regios] Maximum height : " + ChatColor.BLUE + params.getRegionHeightLimit() + ChatColor.RED + ", your height : " + ChatColor.BLUE + sel.getHeight());
				return;
			}

			if(sel.getLength() > params.getRegionLengthLimit()){
				p.sendMessage(ChatColor.RED + "[Regios] You cannot create a region of this length!");
				p.sendMessage(ChatColor.RED + "[Regios] Maximum length : " + ChatColor.BLUE + params.getRegionLengthLimit() + ChatColor.RED + ", your length : " + ChatColor.BLUE + sel.getLength());
				return;
			}

			if(rCount >= params.getRegionLimit()){
				p.sendMessage(ChatColor.RED + "[Regios] You cannot create more than " + ChatColor.YELLOW + params.getRegionLimit() + ChatColor.RED + " regions!");
				return;
			}

			new CubeRegion(p.getName(), name, sel.getMinimumPoint(), sel.getMaximumPoint(), p.getWorld(), null, true);
			p.sendMessage(ChatColor.GREEN + "[Regios] Region " + ChatColor.BLUE + name + ChatColor.GREEN + " created successfully!");
		} else {
			throw new CommandException("The type of region selected in WorldEdit is unsupported in Regios");
		}
	}

	public void createBlueprintWE(Player p, String name)  throws CommandException {
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

		WorldEditPlugin worldEdit = WorldEditInterface.getWorldEdit();

		// Attempt to get the player's selection from WorldEdit
		Selection sel = worldEdit.getSelection(p);

		if(sel == null)
		{
			throw new CommandException("Select a region with WorldEdit first.");
		}

		if (sel instanceof Polygonal2DSelection) {
			throw new CommandException("Regios doesn't support polygonal regions.... yet :D");
		} else if (sel instanceof CuboidSelection) {
			RBF_Core.rbf_save.startSave(null, sel.getMinimumPoint(), sel.getMaximumPoint(), name, p, true);
		} else {
			throw new CommandException("The type of region selected in WorldEdit is unsupported in Regios");
		}
	}
}