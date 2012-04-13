package couk.Adamki11s.Regios.CustomEvents;

import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import couk.Adamki11s.Regios.Regions.Region;

public class RegionLightningStrikeEvent extends Event {
	private static final HandlerList handlers = new HandlerList();
	private Region region;
	private Location location;

    public RegionLightningStrikeEvent(String name) {
        super();
    }
    
    public Region getRegion(){
    	return this.region;
    }
    
    public Location getLocation(){
    	return this.location;
    }

    public void setProperties(Location location, Region r) {
    	this.location = location;
        this.region = r;
    }

    public HandlerList getHandlers() {
        return handlers;
    }
     
    public static HandlerList getHandlerList() {
        return handlers;
    }

}
