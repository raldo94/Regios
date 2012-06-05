package couk.Adamki11s.Regios.GameMode;

import java.util.HashMap;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class GameModeCacheManager {
	private static HashMap<Player, GameMode> gamemodeCache = new HashMap<Player, GameMode>();
	
	public static boolean doesCacheContain(Player p){
		return gamemodeCache.containsKey(p);
	}
	
	public static void cacheGameMode(Player p) {
		gamemodeCache.put(p, p.getGameMode());
	}
	
	public static void restoreGameMode(Player p) {
		p.setGameMode(gamemodeCache.get(p));
		gamemodeCache.remove(p);
	}
}