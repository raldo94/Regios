package net.jzx7.regios.GameMode;

import java.util.HashMap;

import net.jzx7.regiosapi.entity.RegiosPlayer;

public class GameModeCacheManager {
	private HashMap<String, Integer> gamemodeCache = new HashMap<String, Integer>();
	
	public boolean doesCacheContain(RegiosPlayer p){
		return gamemodeCache.containsKey(p.getName());
	}
	
	public void cacheGameMode(RegiosPlayer p) {
		gamemodeCache.put(p.getName(), p.getGameMode());
	}
	
	public void restoreGameMode(RegiosPlayer p) {
		p.setGameMode(gamemodeCache.get(p.getName()));
		gamemodeCache.remove(p.getName());
	}
}