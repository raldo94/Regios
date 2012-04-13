package couk.Adamki11s.Regios.CustomEvents;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import couk.Adamki11s.Regios.Regions.Region;

public class RegionCreateEvent extends Event {
	private static final HandlerList handlers = new HandlerList();
	private Region region;
	private Player player;

    public RegionCreateEvent(String name) {
        super();
    }
    
    public Region getRegion(){
    	return this.region;
    }
    
    public Player getPlayer(){
    	return this.player;
    }

    public void setProperties(Player player, Region genericRegion) {
    	this.player = player;
        this.region = genericRegion;
    }

    public HandlerList getHandlers() {
        return handlers;
    }
     
    public static HandlerList getHandlerList() {
        return handlers;
    }
	
}
