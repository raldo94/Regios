package couk.Adamki11s.Regios.Regions;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import couk.Adamki11s.Extras.Regions.ExtrasRegions;


public class GlobalRegionManager {
	
	private static ArrayList<Region> regions = new ArrayList<Region>()
							, regionsInWorld = new ArrayList<Region>()
							, regionsAtLocation = new ArrayList<Region>();
	private static final ExtrasRegions extReg = new ExtrasRegions();
	
	private static ArrayList<GlobalWorldSetting> worldSettings = new ArrayList<GlobalWorldSetting>();
	
	public static ArrayList<Region> getRegions(){
		return regions;
	}
	
	public static ArrayList<Region> getRegions(World w){
		regionsInWorld.clear();
		for(Region r : regions) {
			if(r.getWorld().getName().equalsIgnoreCase(w.getName())) {
				regionsInWorld.add(r);
			}
		}
		return regionsInWorld;
	}
	
	public static ArrayList<Region> getRegions(Location l){
		regionsAtLocation.clear();
		for(Region r : regions) {
			if(extReg.isInsideCuboid(l, r.getL1(), r.getL2())) {
				regionsAtLocation.add(r);
			}
		}
		return regionsAtLocation;
	}
	
	public static ArrayList<GlobalWorldSetting> getWorldSettings(){
		return worldSettings;
	}
	
	public static boolean deleteRegionFromCache(Region r){
		if(regions.contains(r)){
			regions.remove(r);
			return true;
		}
		return false;
	}
	
	public static boolean doesExist(String name){
		for(Region r : regions){
			if(r.getName().equalsIgnoreCase(name)){
				return true;
			}
		}
		return false;
	}
	
	public static void purgeRegions(){
		regions.clear();
		worldSettings.clear();
		System.gc();
	}
	
	public static void purgeWorldSettings(){
		worldSettings.clear();
	}
	
	public static Region getRegion(Player p){
		for(Region r : regions){
			if(r.getPlayersInRegion().contains(p)){
				return r;
			}
		}
		return null;
	}
	
	public static Region getRegion(Location l){
		for(Region r : regions){
			if(extReg.isInsideCuboid(l, r.getL1(), r.getL2())){
				return r;
			}
		}
		return null;
	}
	
	public static Region getRegion(String name){
		for(Region r : regions){
			if(r.getName().equalsIgnoreCase(name)){
				return r;
			}
		}
		return null;
	}
	
	public static int getOwnedRegions(String name){
		int count = 0;
		for(Region r : regions){
			if(r.getOwner().equalsIgnoreCase(name)){
				count++;
			}
		}
		return count;
	}
	
	public static GlobalWorldSetting getGlobalWorldSetting(World w){
		for(GlobalWorldSetting gws : worldSettings){
			if(gws.world.equalsIgnoreCase(w.getName())){
				return gws;
			}
		}
		return null;
	}
	
	public static ChunkGrid getChunkGrid(Region r){
		return r.getChunkGrid();
	}
	
	public static void addRegion(Region genericRegion){
		regions.add(genericRegion);
	}
	
	public static void addWorldSetting(GlobalWorldSetting gws){
		worldSettings.add(gws);
	}
	
}