package net.jzx7.regios.RBF;

import net.jzx7.regiosapi.entity.RegiosPlayer;

public class ShareData {
	
	private RegiosPlayer player;
	private String shareName;
	private String shareType;
	
	public ShareData(String name, String type, RegiosPlayer p){
		this.player = p;
		this.shareName = name;
		this.shareType = type;
	}

	public RegiosPlayer getPlayer() {
		return player;
	}

	public void setPlayer(RegiosPlayer player) {
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
