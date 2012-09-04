package net.jzx7.regios.Spout;

import java.util.HashMap;

import org.bukkit.entity.Player;

public class SpoutInterface{
	
	public static boolean global_spoutEnabled = false;
	
	public static HashMap<String, Boolean> spoutEnabled = new HashMap<String, Boolean>();
	
	public static boolean doesPlayerHaveSpout(Player p){
		if(spoutEnabled.containsKey(p.getName())){
			return spoutEnabled.get(p.getName());
		} else {
			return false;
		}
	}
	
}
