package net.jzx7.regios.bukkit.SpoutPlugin;

import java.util.HashMap;

import net.jzx7.regiosapi.entity.RegiosPlayer;

public class SpoutInterface{
	
	private static boolean global_spoutEnabled = false;
	
	private static HashMap<String, Boolean> spoutEnabled = new HashMap<String, Boolean>();
	
	public static boolean doesPlayerHaveSpout(RegiosPlayer p){
		if(spoutEnabled.containsKey(p.getName())){
			return spoutEnabled.get(p.getName());
		} else {
			return false;
		}
	}

	public static boolean isGlobal_spoutEnabled() {
		return global_spoutEnabled;
	}

	public static void setGlobal_spoutEnabled(boolean global_spoutEnabled) {
		SpoutInterface.global_spoutEnabled = global_spoutEnabled;
	}

	public static HashMap<String, Boolean> getSpoutEnabled() {
		return spoutEnabled;
	}

	public static void setSpoutEnabled(HashMap<String, Boolean> spoutEnabled) {
		SpoutInterface.spoutEnabled = spoutEnabled;
	}
	
}
