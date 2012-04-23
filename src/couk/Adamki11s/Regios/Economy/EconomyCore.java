package couk.Adamki11s.Regios.Economy;

import java.util.logging.Logger;

import couk.Adamki11s.Regios.Mutable.MutableAdministration;
import couk.Adamki11s.Regios.Mutable.MutableEconomy;
import couk.Adamki11s.Regios.Regions.Region;
import net.milkbowl.vault.economy.Economy;

public class EconomyCore {

	public static Logger log = Logger.getLogger("Minecraft.Regios");

	public static Economy economy = null;

	public static boolean economySupport = false;
	
	static MutableAdministration admin = new MutableAdministration();
	static MutableEconomy econ = new MutableEconomy();

	public static boolean isEconomySupportEnabled(){
		return economySupport;
	}

	public static boolean canAffordRegion(String p, int price){
		return economy.getBalance(p) >= price;
	}
	public static void buyRegion(Region r, String buyer, String seller, int price){
		economy.depositPlayer(seller, (double)price);
		economy.withdrawPlayer(buyer, (double)price);
		buy(seller, buyer, r, price);
	}
	
	public static void buy(String seller, String buyer, Region r, int value){
		econ.editForSale(r, false);
		admin.setOwner(r, buyer);
		r.setOwner(buyer);
		EconomyPending.sendAppropriatePending(seller, buyer, r.getName(), value);
	}

}
