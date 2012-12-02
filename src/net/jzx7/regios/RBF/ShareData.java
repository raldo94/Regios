package net.jzx7.regios.RBF;

import org.bukkit.entity.Player;

public class ShareData {
	
	private Player player;
	private String shareName;
	private String shareType;
	
	public ShareData(String name, String type, Player p){
		this.setPlayer(p);
		this.setShareName(name);
		this.setShareType(type);
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public String getShareName() {
		return shareName;
	}

	public void setShareName(String shareName) {
		this.shareName = shareName;
	}

	public String getShareType() {
		return shareType;
	}

	public void setShareType(String shareType) {
		this.shareType = shareType;
	}

}
