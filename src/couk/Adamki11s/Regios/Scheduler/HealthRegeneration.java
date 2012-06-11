package couk.Adamki11s.Regios.Scheduler;

import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class HealthRegeneration {

	public static HashMap<String, Integer> regenerators = new HashMap<String, Integer>();

	public static void addRegenerator(Player p, int rate) {
		regenerators.put(p.getName(), rate);
	}

	public static void removeRegenerator(Player p) {
		if (regenerators.containsKey(p.getName())) {
			regenerators.remove(p.getName());
		}
	}

	public static boolean isRegenerator(Player p) {
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
				Player p = Bukkit.getPlayer(entry.getKey());
				if (((p.getHealth() - damage) > 0)) {
					p.damage(-damage);
				} else {
					p.damage(p.getHealth());
				}
			} else {
				Player p = Bukkit.getPlayer(entry.getKey());
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
