package net.jzx7.regios.GameMode;

import java.util.HashMap;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class GameModeCacheManager {
	private HashMap<String, GameMode> gamemodeCache = new HashMap<String, GameMode>();
	
	public boolean doesCacheContain(Player p){
		return gamemodeCache.containsKey(p.getName());
	}
	
	public void cacheGameMode(Player p) {
		gamemodeCache.put(p.getName(), p.getGameMode());
	}
	
	public void restoreGameMode(Player p) {
		p.setGameMode(gamemodeCache.get(p.getName()));
		gamemodeCache.remove(p.getName());
	}
}