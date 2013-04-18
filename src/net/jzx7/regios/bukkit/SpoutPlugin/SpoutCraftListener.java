package net.jzx7.regios.bukkit.SpoutPlugin;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.getspout.spoutapi.event.spout.SpoutCraftEnableEvent;

public class SpoutCraftListener implements Listener {
	
	@EventHandler
	public void onSpoutCraftEnable(SpoutCraftEnableEvent event) {
        SpoutInterface.getSpoutEnabled().put(event.getPlayer().getName(), true);
    }

}
