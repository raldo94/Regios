package net.jzx7.regios.Inventory;

import java.util.HashMap;

import net.jzx7.regiosapi.entity.RegiosPlayer;
import net.jzx7.regiosapi.inventory.RegiosItemStack;

public class InventoryCacheManager {
	
	private HashMap<String, RegiosItemStack[]> inventMain = new HashMap<String, RegiosItemStack[]>();
	private HashMap<String, RegiosItemStack[]> inventArmour = new HashMap<String, RegiosItemStack[]>();
	
	public boolean doesCacheContain(RegiosPlayer p){
		return inventMain.containsKey(p.getName()) && inventArmour.containsKey(p.getName());
	}
	
	public void cacheInventory(RegiosPlayer p){
		inventMain.put(p.getName(), p.getInventoryContents());
		inventArmour.put(p.getName(), p.getArmorContents());
	}
	
	public void restoreInventory(RegiosPlayer p){
		p.clearInventory();
		p.setInventoryContents(inventMain.get(p.getName()));
		p.setArmorContents(inventArmour.get(p.getName()));
		inventMain.remove(p.getName());
		inventArmour.remove(p.getName());
	}
	
	public void wipeInventory(RegiosPlayer p){
		p.clearInventory();
	}

}
