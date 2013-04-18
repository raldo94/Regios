package net.jzx7.regios.entity;

import java.util.Collection;
import java.util.HashMap;

import net.jzx7.regios.RBF.ShareData;
import net.jzx7.regiosapi.entity.RegiosPlayer;
import net.jzx7.regiosapi.location.RegiosPoint;
import net.jzx7.regiosapi.regions.Region;

public class PlayerManager {
	
	private static HashMap<String, Region> regionBinding = new HashMap<String, Region>();
	private static HashMap<String, Region> currentRegion = new HashMap<String, Region>();

	private static HashMap<String, RegiosPoint> outsideRegionLocation = new HashMap<String, RegiosPoint>();
	private static HashMap<String, RegiosPoint> insideRegionLocation = new HashMap<String, RegiosPoint>();

	private static HashMap<String, Long> timeStampsProtection = new HashMap<String, Long>();
	private static HashMap<String, Long> timeStampsAuth = new HashMap<String, Long>();
	private static HashMap<String, Long> timeStampsPreventEntry = new HashMap<String, Long>();
	private static HashMap<String, Long> timeStampsPreventExit = new HashMap<String, Long>();
	private static HashMap<String, Long> timeStampsEconomy = new HashMap<String, Long>();

	private static HashMap<String, ShareData> loadingTerrain = new HashMap<String, ShareData>();
	
	private static HashMap<String, RegiosPlayer> players = new HashMap<String, RegiosPlayer>();
	
	public Collection<RegiosPlayer> getRegiosPlayers() {
		return players.values();
	}
	
	public RegiosPlayer getRegiosPlayer(String name) {
		if (players.containsKey(name)) {
			return players.get(name);
		} else {
			return null;
		}
	}
	
	public void addRegiosPlayer(String name, RegiosPlayer p) {
		if (!players.containsKey(name)) {
			players.put(name, p);
		}
	}
	
	public void removeRegiosPlayer(String name) {
		if (players.containsKey(name)) {
			players.remove(name);
		}
	}
	
	public void purgeRegiosPlayers() {
		players.clear();
		System.gc();
	}
	
	/**
	 * @return the regionBinding
	 */
	public HashMap<String, Region> getRegionBinding() {
		return regionBinding;
	}

	/**
	 * @return the currentRegion
	 */
	public HashMap<String, Region> getCurrentRegion() {
		return currentRegion;
	}

	/**
	 * @return the outsideRegionLocation
	 */
	public HashMap<String, RegiosPoint> getOutsideRegionLocation() {
		return outsideRegionLocation;
	}

	/**
	 * @return the insideRegionLocation
	 */
	public HashMap<String, RegiosPoint> getInsideRegionLocation() {
		return insideRegionLocation;
	}

	/**
	 * @return the timeStampsProtection
	 */
	public HashMap<String, Long> getTimeStampsProtection() {
		return timeStampsProtection;
	}

	/**
	 * @return the timeStampsAuth
	 */
	public HashMap<String, Long> getTimeStampsAuth() {
		return timeStampsAuth;
	}

	/**
	 * @return the timeStampsPreventEntry
	 */
	public HashMap<String, Long> getTimeStampsPreventEntry() {
		return timeStampsPreventEntry;
	}

	/**
	 * @return the timeStampsPreventExit
	 */
	public HashMap<String, Long> getTimeStampsPreventExit() {
		return timeStampsPreventExit;
	}

	/**
	 * @return the timeStampsEconomy
	 */
	public HashMap<String, Long> getTimeStampsEconomy() {
		return timeStampsEconomy;
	}

	/**
	 * @return the loadingTerrain
	 */
	public HashMap<String, ShareData> getLoadingTerrain() {
		return loadingTerrain;
	}
}