package net.jzx7.regios.Listeners;

import net.jzx7.regios.worlds.WorldManager;
import net.jzx7.regiosapi.worlds.RegiosWorld;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.LightningStrikeEvent;

public class RegiosWeatherListener implements Listener {

	private static final WorldManager wm = new  WorldManager();
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onLightningStrike(LightningStrikeEvent evt){
		RegiosWorld w = wm.getRegiosWorld(evt.getWorld());
		if(!w.getLightningEnabled()){
			evt.setCancelled(true);
		}
	}
}
