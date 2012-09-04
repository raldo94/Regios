package net.jzx7.regios.worlds;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

import net.jzx7.regiosapi.worlds.RegiosWorld;

import org.bukkit.Bukkit;
import org.bukkit.World;

public class WorldManager {

	private static HashMap<UUID, RegiosWorld> worlds = new HashMap<UUID, RegiosWorld>();
	
	public Collection<RegiosWorld> getRegiosWorlds() {
		return worlds.values();
	}

	public RegiosWorld getRegiosWorld(World world) {
		if (world == null) {
			return null;
		}
		return getRegiosWorld(world.getUID());
	}

	public RegiosWorld getRegiosWorld(UUID id) {
		if (worlds.containsKey(id)) {
			return worlds.get(id);
		}
		RegiosWorld world = new RegWorld(Bukkit.getServer().getWorld(id));
		worlds.put(id, world);
		return world;
	}
	
	public void purgeWorlds(){
		worlds.clear();
		System.gc();
	}
}
