package net.jzx7.regios.Inventory;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import couk.Adamki11s.Extras.Inventory.ExtrasInventory;

public class InventoryCacheManager {
	
	private static HashMap<String, ItemStack[]> inventMain = new HashMap<String, ItemStack[]>();
	private static HashMap<String, ItemStack[]> inventArmour = new HashMap<String, ItemStack[]>();
	
	static ExtrasInventory exi = new ExtrasInventory();
	
	public static boolean doesCacheContain(Player p){
		return inventMain.containsKey(p.getName()) && inventArmour.containsKey(p.getName());
	}
	
	public static void cacheInventory(Player p){
		inventMain.put(p.getName(), p.getInventory().getContents());
		inventArmour.put(p.getName(), p.getInventory().getArmorContents());
	}
	
	@SuppressWarnings("deprecation")
	public static void restoreInventory(Player p){
		exi.wipeInventory(p);
		p.getInventory().setContents(inventMain.get(p.getName()));
		p.getInventory().setArmorContents(inventArmour.get(p.getName()));
		inventMain.remove(p.getName());
		inventArmour.remove(p.getName());
		p.updateInventory();
	}
	
	public static void wipeInventory(Player p){
		exi.wipeInventory(p);
	}

}
