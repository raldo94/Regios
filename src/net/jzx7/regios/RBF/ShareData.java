package net.jzx7.regios.RBF;

import org.bukkit.entity.Player;

public class ShareData {
	
	public Player player;
	public String shareName;
	
	public ShareData(String name, Player p){
		this.player = p;
		this.shareName = name;
	}

}
