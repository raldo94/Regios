package net.jzx7.regios.Economy;

import java.io.File;
import java.io.IOException;

import net.jzx7.regios.messages.MsgFormat;
import net.jzx7.regios.util.RegiosConversions;
import net.jzx7.regiosapi.entity.RegiosPlayer;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class EconomyPending {

	static final File root = new File("plugins" + File.separator + "Regios" + File.separator + "Other" + File.separator + "Pending");

	public static void sendAppropriatePending(String seller, String buyer, String region_name, int price){
		RegiosPlayer p = RegiosConversions.getRegiosPlayer(seller);
		if(p == null){
			createPending(seller, buyer, region_name, price);
		} else {
			p.sendMessage(MsgFormat.colourFormat("<DGREEN>[Regios] Player <BLUE>" + buyer + " <DGREEN>bought your region <BLUE>" + region_name + " <DGREEN>for <BLUE>" + price));
		}
	}
	
	private static void createPending(String seller, String buyer, String region_name, int price) {
		File pending = new File(root + File.separator + seller + ".pending");
		if (pending.exists()) {
			pending.delete();
			try {
				pending.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				pending.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		FileConfiguration c = YamlConfiguration.loadConfiguration(pending);
		c.set("Buyer", buyer);
		c.set("Region", region_name);
		c.set("Price", price);
		try {
			c.save(pending);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean isPending(RegiosPlayer p) {
		File pending = new File(root + File.separator + p.getName() + ".pending");
		return pending.exists();
	}

	public static void loadAndSendPending(RegiosPlayer p) {
		File pending = new File(root + File.separator + p.getName() + ".pending");
		FileConfiguration c = YamlConfiguration.loadConfiguration(pending);
		p.sendMessage(MsgFormat.colourFormat("<DGREEN>[Regios] Player <BLUE>" + c.getString("Buyer", "NULL") + " <DGREEN>bought your region <BLUE>" + c.getString("Region", "NULL") + " <DGREEN>for <BLUE>" + c.getInt("Price", 0)));
		pending.delete();
	}

}
