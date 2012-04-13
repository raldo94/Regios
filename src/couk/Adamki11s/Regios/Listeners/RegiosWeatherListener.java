package couk.Adamki11s.Regios.Listeners;

import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.LightningStrikeEvent;

import couk.Adamki11s.Regios.Regions.GlobalRegionManager;

public class RegiosWeatherListener implements Listener {
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onLightningStrike(LightningStrikeEvent evt){
		World w = evt.getWorld();
		if(GlobalRegionManager.getGlobalWorldSetting(w) == null){
			return;
		}
		if(!GlobalRegionManager.getGlobalWorldSetting(w).lightning_enabled){
			evt.setCancelled(true);
		}
	}

}
