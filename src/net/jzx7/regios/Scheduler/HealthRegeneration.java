package net.jzx7.regios.Scheduler;

import java.util.HashMap;
import java.util.Map.Entry;

import net.jzx7.regios.util.RegiosConversions;
import net.jzx7.regiosapi.entity.RegiosPlayer;

public class HealthRegeneration {

	public static HashMap<String, Integer> regenerators = new HashMap<String, Integer>();

	public static void addRegenerator(RegiosPlayer p, int rate) {
		regenerators.put(p.getName(), rate);
	}

	public static void removeRegenerator(RegiosPlayer p) {
		if (regenerators.containsKey(p.getName())) {
			regenerators.remove(p.getName());
		}
	}

	public static boolean isRegenerator(RegiosPlayer p) {
		if (regenerators.containsKey(p.getName())) {
			return regenerators.containsKey(p.getName());
		} else {
			return false;
		}
	}

	public static void loopRegenerators() {
		for (Entry<String, Integer> entry : regenerators.entrySet()) {
			int damage = entry.getValue();
			if (damage < 0) {
				RegiosPlayer p = RegiosConversions.getRegiosPlayer(entry.getKey());
				if (((p.getHealth() - damage) > 0)) {
					p.damage(-damage);
				} else {
					p.damage(p.getHealth());
				}
			} else {
				RegiosPlayer p = RegiosConversions.getRegiosPlayer(entry.getKey());
				if (p.getHealth() < 20) {
					if (p.getHealth() + damage <= 20) {
						p.setHealth(p.getHealth() + damage);
					} else {
						p.setHealth(20);
					}
				}
			}
		}
	}

}
