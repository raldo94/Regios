package net.jzx7.regios.RBF;

import java.util.ArrayList;
import java.util.HashMap;

import net.jzx7.regiosapi.block.RegiosBlock;
import net.jzx7.regiosapi.entity.RegiosPlayer;

public class RBF_Core {
	
	private final static HashMap<String, ArrayList<PBD>> undoCache = new HashMap<String, ArrayList<PBD>>();
	
	public static final Blueprint blueprint = new Blueprint();
	public static final Backup backup = new Backup();
	public static final Schematic schematic = new Schematic();

	public static void undoLoad(RegiosPlayer p) {
		if (!getUndoCache().containsKey(p.getName())) {
			p.sendMessage("<RED>" + "[Regios] Nothing to undo!");
			return;
		} else {
			ArrayList<PBD> bb = new ArrayList<PBD>();
			bb = getUndoCache().get(p.getName());
			for (PBD b : bb) {
				RegiosBlock block = p.getRegiosWorld().getBlockAt(b.getL());
				block.setId(b.getId());
				block.setData(b.getData());
			}
			bb.clear();
			getUndoCache().remove(p.getName());
			p.sendMessage("<DGREEN>" + "[Regios] Undo successful!");
		}
	}

	public static HashMap<String, ArrayList<PBD>> getUndoCache() {
		return undoCache;
	}
}
