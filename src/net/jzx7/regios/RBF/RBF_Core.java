package net.jzx7.regios.RBF;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class RBF_Core {
	
	public static final HashMap<String, ArrayList<PBD>> undoCache = new HashMap<String, ArrayList<PBD>>();
	
	public static final Blueprint blueprint = new Blueprint();
	public static final Backup backup = new Backup();
	public static final Schematic schematic = new Schematic();

	public static void undoLoad(Player p) {
		if (!undoCache.containsKey(p.getName())) {
			p.sendMessage(ChatColor.RED + "[Regios] Nothing to undo!");
			return;
		} else {
			ArrayList<PBD> bb = new ArrayList<PBD>();
			bb = undoCache.get(p.getName());
			for (PBD b : bb) {
				Block block = p.getWorld().getBlockAt(b.getL());
				block.setTypeId(b.getId());
				block.setData(b.getData());
			}
			bb.clear();
			undoCache.remove(p.getName());
			p.sendMessage(ChatColor.GREEN + "[Regios] Undo successful!");
		}
	}
}
