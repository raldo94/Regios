package couk.Adamki11s.Regios.WorldEdit;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;

public class WorldEditInterface {
	/**
	 * Gets a copy of the WorldEdit plugin.
	 *
	 * @return The WorldEditPlugin instance
	 * @throws CommandException If there is no WorldEditPlugin available
	 */
	public static WorldEditPlugin getWorldEdit() throws CommandException {
		Plugin worldEdit = Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
		if (worldEdit == null) {
			throw new CommandException("WorldEdit does not appear to be installed.");
		}

		if (worldEdit instanceof WorldEditPlugin) {
			return (WorldEditPlugin) worldEdit;
		} else {
			throw new CommandException("WorldEdit detection failed (report error).");
		}
	}
}