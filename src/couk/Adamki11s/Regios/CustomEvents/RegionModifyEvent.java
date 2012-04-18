package couk.Adamki11s.Regios.CustomEvents;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import couk.Adamki11s.Regios.Regions.Region;

public class RegionModifyEvent extends Event {
	private static final HandlerList handlers = new HandlerList();
	private Region region;

    public RegionModifyEvent(String name) {
    }
    
    public Region getRegion(){
    	return this.region;
    }

    public void setProperties(Region genericRegion) {
        this.region = genericRegion;
    }

    public HandlerList getHandlers() {
        return handlers;
    }
     
    public static HandlerList getHandlerList() {
        return handlers;
    }

}
