package net.jzx7.regios.Economy;

import java.util.UUID;

import net.jzx7.regiosapi.regions.Region;

import org.bukkit.Location;

public class RegionSign {
	
	Region r = null;
	Location l = null;
	UUID uuid = null;
	
	public RegionSign(Region region, Location loc, UUID uuid){
		this.r = region;
		this.l = loc;
		this.uuid = uuid;
	}
	
	public String getRegionName(){
		return this.r.getName();
	}
	
	public UUID getUUID(){
		return this.uuid;
	}
	
	public Region getRegion(){
		return this.r;
	}
	
	public Location getLocation(){
		return this.l;
	}

}
