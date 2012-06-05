package couk.Adamki11s.Regios.Spout;

import java.util.HashMap;

import org.bukkit.entity.Player;

public class SpoutInterface{
	
	public static boolean global_spoutEnabled = false;
	
	public static HashMap<Player, Boolean> spoutEnabled = new HashMap<Player, Boolean>();
	
	public static boolean doesPlayerHaveSpout(Player p){
		if(spoutEnabled.containsKey(p)){
			return spoutEnabled.get(p);
		} else {
			return false;
		}
	}
	
}
