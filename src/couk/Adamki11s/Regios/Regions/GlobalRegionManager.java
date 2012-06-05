package couk.Adamki11s.Regios.Regions;

import java.util.ArrayList;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import couk.Adamki11s.Extras.Regions.ExtrasRegions;

public class GlobalRegionManager {
	
	private static ArrayList<Region> regions = new ArrayList<Region>()
							, regionsInWorld = new ArrayList<Region>();
	private static final ExtrasRegions extReg = new ExtrasRegions();
	
	private static ArrayList<GlobalWorldSetting> worldSettings = new ArrayList<GlobalWorldSetting>();
	private final static SubRegionManager srm = new SubRegionManager();
	
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
		World w = l.getWorld();
		Chunk c = w.getChunkAt(l);

		ArrayList<Region> regionSet = new ArrayList<Region>();

		for (Region region : GlobalRegionManager.getRegions()) {
			for (Chunk chunk : region.getChunkGrid().getChunks()) {
				if (chunk.getWorld() == w) {
					if (extReg.areChunksEqual(chunk, c)) {
						if (!regionSet.contains(region)) {
							regionSet.add(region);
						}
					}
				}
			}
		}

		if (regionSet.isEmpty()) {
			return regionSet;
		}

		ArrayList<Region> currentRegionSet = new ArrayList<Region>();

		for (Region reg : regionSet) {
			if (extReg.isInsideCuboid(l, reg.getL1(), reg.getL2())) {
				currentRegionSet.add(reg);
			}
		}

		return currentRegionSet;
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
	
	public static boolean doesRegionExist(String name){
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
			World w = l.getWorld();
			Chunk c = w.getChunkAt(l);

			ArrayList<Region> regionSet = new ArrayList<Region>();

			for (Region region : GlobalRegionManager.getRegions()) {
				for (Chunk chunk : region.getChunkGrid().getChunks()) {
					if (chunk.getWorld() == w) {
						if (extReg.areChunksEqual(chunk, c)) {
							if (!regionSet.contains(region)) {
								regionSet.add(region);
							}
						}
					}
				}
			}

			if (regionSet.isEmpty()) {
				return null;
			}

			ArrayList<Region> currentRegionSet = new ArrayList<Region>();

			for (Region reg : regionSet) {
				if (extReg.isInsideCuboid(l, reg.getL1(), reg.getL2())) {
					currentRegionSet.add(reg);
				}
			}

			if (currentRegionSet.isEmpty()) {
				return null;
			}

			if (currentRegionSet.size() > 1) {
				return srm.getCurrentRegion(currentRegionSet);
			} else {
				return currentRegionSet.get(0);
			}
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