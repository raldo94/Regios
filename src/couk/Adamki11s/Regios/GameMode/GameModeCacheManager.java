package couk.Adamki11s.Regios.GameMode;

import java.util.HashMap;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class GameModeCacheManager {
	private static HashMap<String, GameMode> gamemodeCache = new HashMap<String, GameMode>();
	
	public static boolean doesCacheContain(Player p){
		return gamemodeCache.containsKey(p.getName());
	}
	
	public static void cacheGameMode(Player p) {
		gamemodeCache.put(p.getName(), p.getGameMode());
	}
	
	public static void restoreGameMode(Player p) {
		p.setGameMode(gamemodeCache.get(p.getName()));
		gamemodeCache.remove(p.getName());
	}
}