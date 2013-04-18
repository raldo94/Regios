package net.jzx7.regios.messages;

public enum CMDText {
	
	//---General---//
	HELP("\r help"),
	SET("/r set [cube/cuboid/poly/polygon]"),
	CREATE("/r create <region>"),
	DELETE("/r delete <region>"),
	RENAME("/r rename <oldname> <newname>"),
	INFO("/r info <region>"),
	LIST("/r list [owned/playername]"),
	RELOAD("/r reload"),
	CANCEL("/r cancel"),
	SETOWNER("/r set-owner <region> <owner>"),
	
	//---MODE---//
	PROTECTIONMODE("/r protection-mode <region> <bl/wl>"),
	PREVENENTRYMODE("/r prevent-entry-mode <region> <bl/wl>"),
	PREVENTEXITMODE("/r prevent-exit-mode <region> <bl/wl>"),
	ITEMMODE("/r item-mode <region> <bl/wl>"),
	
	//---Protection---//
	SENDAUTH("/r send-auth <password>"),
	PROTECT("/r un/protect [all/break/bb/place/bp] <region>"),
	PREVENTENTRY("/r prevent-entry <region>"),
	ALLOWENTRY("/r allow-entry <region>"),
	PREVENTEXIT("/r prevent-exit <region>"),
	ALLOWEXIT("/r allow-exit <region>"),
	PREVENTINTERACTION("/r prevent-interaction <region> <T/F>"),
	DOORSLOCKED("/r doors-locked <region> <T/F>"),
	CHESTSLOCKED("/r chests-locked <region> <T/F>"),
	DISPENSERSLOCKED("/r dispensers-locked <region> <T/F>"),
	SETPASSWORD("/r set-password <region> <password>"),
	USEPASSWORD("/r use-password <region> <T/F>"),
	FIREPROTECTION("/r fire-protection <region> <T/F>"),
	FIRESPREAD("/r fire-spread <region> <T/F>"),
	EXPLOSIONSENABLED("/r explosions-enabled <region> <T/F>"),
	PLAYERCAP("/r player-cap <region> <cap>"),
	BLOCKFORM("/r block-form <region> <T/F>"),
	
	//---Messages---//
	SETWELCOME("/r set-welcome <region> <message>"),
	SHOWWELCOME("/r show-welcome <region> <T/F>"),
	SETLEAVE("/r set-leave <region> <message>"),
	SHOWLEAVE("/r show-leave <region> <T/F>"),
	SETPREVENTEXIT("/r set-prevent-exit <region> <message>"),
	SHOWPREVENTEXIT("/r show-prevent-exit <region> <T/F>"),
	SETPREVENTENTRY("/r set-prevent-entry <region> <message>"),
	SHOWPREVENTENTRY("/r show-prevent-entry <region> <T/F>"),
	SETPROTECTION("/r set-protection <region> <message>"),
	SHOWPROTECTION("/r show-protection <region> <T/F>"),
	SHOWPVP("/r show-pvp <region> <T/F>"),
	
	//---Fun---//
	SETWARP("/r setwarp"),
	WARPTO("/r warp-to <region>"),
	RESETWARP("/r reset-warp <region>"),
	LSPS("/r lsps <region> <rate>"),
	PVP("/r pvp <region> <T/F>"),
	HEALTHREGEN("/r health-regen <region> <rate>"),
	HEALTHENABLED("/r health-enabled <region> <T/F>"),
	VELWARP("/r vel-warp <region> <rate>"),
	SETPRICE("/r set-price <region> <price>"),
	FORSALE("/r for-sale <region> <T/F>"),
	LISTFORSALE("/r list-for-sale"),
	BUY("/r buy <region>"),

	//---Misc---//
	SETMOBSPAWNS("/r setmobspawns <passive/hostile> <region> <true/false>"),
	CMDSETADDREMOVE("/r cmdset add/remove <region> <cmd>"),
	CMDSETLISTRESET("/r cmdset list/reset <region>"),
	CMDSETENABLED("/r cmdset enabled <region> <T/F>"),
	GAMEMODESET("/r gamemode set <region> <SURVIVAL/CREATIVE/ADVENTURE/0/1/2>"),
	GAMEMODECHANGE("/r gamemode change <region> <T/F>"),

	//---Inventory---//
	PERMWIPEENTRY("/r perm-wipe-entry <region> <T/F>"),
	PERMWIPEEXIT("/r perm-wipe-exit <region> <T/F>"),
	CACHEWIPEENTRY("/r cache-wipe-entry <region> <T/F>"),
	CACHEWIPEEXIT("/r cache-wipe-exit <region> <T/F>"),

	//---Exceptions---//
	PLAYEREXADDREMOVE("/r playerex add/rem <region> <player>"),
	PLAYEREXLISTRESET("/r playerex list/reset <region>"),
	ITEMEXADDREMOVE("/r itemex add/rem <region> <itemid>"),
	ITEMEXLISTRESET("/r itemex list/reset <region>"),
	NODEEXADDREMOVE("/r nodeex add/rem <region> <node>"),
	NODEEXLISTRESET("/r nodeex list/reset <region>"),
	SUBOWNERADDREMOVE("/r subowner add/rem <region> <node>"),
	SUBOWNERLISTRESET("/r subowner list/reset <region>"),

	//---Modification---//
	EXPAND("/r expand <region> up/down/north/south/east/west/max/out <value>"),
	SHRINK("/r shrink <region> up/down/north/south/east/west/in <value>"),
	SHIFT("/r shift <region> up/down/north/south/east/west <value>"),
	MODIFY("/r modify <region>"),
	MODIFYCONFIRM("/r modify confirm"),
	INHERIT("/r inherit <toinherit> <inheritfrom>"),

	//---SpoutPlugin---//
	EDIT("/r edit <region>"),
	SETSPOUTWELCOME("/r set-spout-welcome <region> <message>"),
	SETSPOUTLEAVE("/r set-spout-leave <region> <message>"),
	SETSPOUTWELCOMEID("/r set-wlecome-id <region> <itemid>"),
	SETSPOUTLEAVEID("/r set-leave-id <region> <itemid>"),
	SPOUTTEXTUREURL("/r spout-texture-url <region> <url>"),
	USETEXTUREURL("/r use-texture-url <region> <T/F>"),
	USEMUSICURL("/r use-music-url <region> <T/F>"),
	ADDMUSICURL("/r add-music-url <region> <url>"),
	REMMUSICURL("/r rem-music-url <region> <url>"),
	RESETMUSICURL("/r reset-music-url <region>"),

	//---Permissions---//
	PERMCACHEADD("/r perm-cache-add <region> <node>"),
	PERMCACHEREM("/r perm-cache-rem <region> <node>"),
	PERMCACHELIST("/r perm-cache-list <region>"),
	PERMCACHERESET("/r perm-cache-reset <region>"),
	PERMADDADD("/r perm-add-add <region> <node>"),
	PERMADDREM("/r perm-add-rem <region> <node>"),
	PERMADDLIST("/r perm-add-list <region>"),
	PERMADDRESET("/r perm-add-reset <region>"),
	PERMREMADD("/r perm-rem-add <region> <node>"),
	PERMREMREM("/r perm-rem-rem <region> <node>"),
	PERMREMLIST("/r perm-rem-list <region>"),
	PERMREMRESET("/r perm-rem-reset <region>"),

	//---Data---//
	SAVEREGION("/r save-region <region> <name>"),
	LOADREGION("/r load-region <region> <name>"),
	LISTBACKUPS("/r list-backups <region>"),
	BACKUPDATABASE("/r backup-database <region> <name>"),
	SAVEBLUEPRINT("/r saveblp <name>"),
	LOADBLUEPRINT("/r loadblp <name>"),
	SAVESCHEMATIC("/r savesch <name>"),
	LOADSCHEMATIC("/r loadsch <name>"),
	UNDO("/r undo"),
	VERSION("/r version"),
	CHECK("/r check");
	
	private String cmdText;
	
	CMDText(String text) {
		this.cmdText = text;
	}
	
	public String getText() {
		return cmdText;
	}
}