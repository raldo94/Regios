package net.jzx7.regios.bukkit.listeners;

import net.jzx7.regios.Data.LoaderCore;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;

public class RegiosServerListener implements Listener {
	
	private LoaderCore lc = new LoaderCore();
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onWorldLoad(WorldLoadEvent evt) {
		System.out.print("World load detected. Reloading configuration.");
		lc.silentReload();
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onWorldUnload(WorldUnloadEvent evt) {
		System.out.print("World load detected. Reloading configuration.");
		lc.silentReload();
	}
}