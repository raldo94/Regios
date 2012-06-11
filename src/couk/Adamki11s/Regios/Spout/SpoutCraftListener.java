package couk.Adamki11s.Regios.Spout;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.getspout.spoutapi.event.spout.SpoutCraftEnableEvent;

public class SpoutCraftListener implements Listener {
	
	@EventHandler
	public void onSpoutCraftEnable(SpoutCraftEnableEvent event) {
        SpoutInterface.spoutEnabled.put(event.getPlayer().getName(), true);
    }

}
