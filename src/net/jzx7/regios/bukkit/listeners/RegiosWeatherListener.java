package net.jzx7.regios.bukkit.listeners;

import net.jzx7.regios.util.RegiosConversions;
import net.jzx7.regiosapi.worlds.RegiosWorld;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.LightningStrikeEvent;

public class RegiosWeatherListener implements Listener {
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onLightningStrike(LightningStrikeEvent evt){
		RegiosWorld w = RegiosConversions.getRegiosWorld(evt.getWorld());
		if(!w.getLightningEnabled()){
			evt.setCancelled(true);
		}
	}
}
