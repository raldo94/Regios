package net.jzx7.regios.Inventory;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import couk.Adamki11s.Extras.Inventory.ExtrasInventory;

public class InventoryCacheManager {
	
	private HashMap<String, ItemStack[]> inventMain = new HashMap<String, ItemStack[]>();
	private HashMap<String, ItemStack[]> inventArmour = new HashMap<String, ItemStack[]>();
	
	static ExtrasInventory exi = new ExtrasInventory();
	
	public boolean doesCacheContain(Player p){
		return inventMain.containsKey(p.getName()) && inventArmour.containsKey(p.getName());
	}
	
	public void cacheInventory(Player p){
		inventMain.put(p.getName(), p.getInventory().getContents());
		inventArmour.put(p.getName(), p.getInventory().getArmorContents());
	}
	
	public void restoreInventory(Player p){
		exi.wipeInventory(p);
		p.getInventory().setContents(inventMain.get(p.getName()));
		p.getInventory().setArmorContents(inventArmour.get(p.getName()));
		inventMain.remove(p.getName());
		inventArmour.remove(p.getName());
	}
	
	public void wipeInventory(Player p){
		exi.wipeInventory(p);
	}

}
