package net.jzx7.regios.worlds;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

import net.jzx7.regios.util.RegiosConversions;
import net.jzx7.regiosapi.worlds.RegiosWorld;

public class WorldManager {

	private static HashMap<UUID, RegiosWorld> worlds = new HashMap<UUID, RegiosWorld>();
	
	public Collection<RegiosWorld> getRegiosWorlds() {
		if (worlds != null) {
			return worlds.values();
		} else {
			RegiosConversions.loadServerWorlds();
			return worlds.values();
		}
	}
	
	public RegiosWorld getRegiosWorld(UUID id) {
		if (worlds.containsKey(id)) {
			return worlds.get(id);
		} else {
			return null;
		}
	}
	
	public void addRegiosWorld(UUID id, RegiosWorld w) {
		if (!worlds.containsKey(id)) {
			worlds.put(id, w);
		}
	}
	
	public void purgeRegiosWorlds(){
		worlds.clear();
		System.gc();
	}
}
