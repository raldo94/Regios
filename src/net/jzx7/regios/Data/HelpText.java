package net.jzx7.regios.Data;

public enum HelpText {
	GENERAL (new String[] { ("/r set cube/cuboid/poly/polygon")
			, ("/r create <region>")
			, ("/r delete <region>")
			, ("/r rename <oldname> <newname>")
			, ("/r info <region>")
			, ("/r list [owned/playername]")
			, ("/r list-for-sale")
			, ("/r delete <region>")
			, ("/r reload")
			, ("/r cancel")
			, ("/r set-owner <region> <owner>")}),
			
	MODE (new String[] { ("/r protection-mode <region> <bl/wl>")
			, ("/r prevent-entry-mode <region> <bl/wl>")
			, ("/r prevent-exit-mode <region> <bl/wl>")
			, ("/r item-mode <region> <bl/wl>") }),
			
	PROTECTION (new String[] { ("/r send-auth <password>")
			, ("/r un/protect [all/break/bb/place/bp] <region>")
			, ("/r prevent-entry <region>")
			, ("/r allow-entry <region>")
			, ("/r prevent-exit <region>")
			, ("/r allow-exit <region>")
			, ("/r prevent-interaction <region> <T/F>")
			, ("/r doors-locked <region> <T/F>")
			, ("/r chests-locked <region> <T/F>")
			, ("/r dispensers-locked <region> <T/F>")
			, ("/r set-password <region> <password>")
			, ("/r use-password <region> <T/F>")
			, ("/r fire-protection <region> <T/F>")
			, ("/r fire-spread <region> <T/F>")
			, ("/r explosions-enabled <region> <T/F>")
			, ("/r player-cap <region> <cap>")
			, ("/r block-form <region> <T/F>") }),
			
	MESSAGES (new String[] { ("/r set-welcome <region> <message>")
			, ("/r show-welcome <region> <T/F>")
			, ("/r set-leave <region> <message>")
			, ("/r show-leave <region> <T/F>")
			, ("/r set-prevent-exit <region> <message>")
			, ("/r show-prevent-exit <region> <T/F>")
			, ("/r set-prevent-entry <region> <message>")
			, ("/r show-prevent-entry <region> <T/F>")
			, ("/r set-protection <region> <message>")
			, ("/r show-protection <region> <T/F>")
			, ("/r show-pvp <region> <T/F>") }),
			
	FUN	(new String[] { ("/r setwarp")
			, ("/r warp-to <region>")
			, ("/r reset-warp <region>")
			, ("/r lsps <region> <rate>")
			, ("/r pvp <region> <T/F>")
			, ("/r health-regen <region> <rate>")
			, ("/r health-enabled <region> <T/F>")
			, ("/r vel-warp <region> <rate>")
			, ("/r set-price <region> <price>")
			, ("/r for-sale <region> <T/F>")
			, ("/r list-for-sale")
			, ("/r buy <region>") }),

	MISC (new String[] { ("/r setmobspawns <passive/hostile> <region> <true/false>")
			, ("/r cmdset add/remove <region> <cmd>")
			, ("/r cmdset list/reset <region>")
			, ("/r cmdset enabled <region> <T/F>")
			, ("/r gamemode set <region> <SURVIVAL/CREATIVE/ADVENTURE/0/1/2>")
			, ("/r gamemode change <region> <T/F>")}),

	INVENTORY (new String[] { ("/r perm-wipe-entry <region> <T/F>")
			, ("/r perm-wipe-exit <region> <T/F>")
			, ("/r cache-wipe-entry <region> <T/F>")
			, ("/r cache-wipe-exit <region> <T/F>") }),

	EXCEPTIONS (new String[] { ("/r playerex add/rem <region> <player>")
			, ("/r playerex list/reset <region>")
			, ("/r itemex add/rem <region> <itemid>")
			, ("/r itemex list/reset <region>")
			, ("/r nodeex add/rem <region> <node>")
			, ("/r nodeex list/reset <region>")
			, ("/r subowner add/rem <region> <node>")
			, ("/r subowner list/reset <region>") }),

	MODIFICATION (new String[] { ("/r expand <region> up/down/north/south/east/west/max/out <value>")
			, ("/r shrink <region> up/down/north/south/east/west/in <value>")
			, ("/r modify <region>")
			, ("/r modify confirm")
			, ("/r inherit <toinherit> <inheritfrom>") }),

	SPOUT (new String[] { ("/r set-spout-welcome <region> <message>")
			, ("/r set-spout-leave <region> <message>")
			, ("/r set-wlecome-id <region> <itemid>")
			, ("/r set-leave-id <region> <itemid>")
			, ("/r spout-texture-url <region> <url>")
			, ("/r use-texture-url <region> <T/F>")
			, ("/r use-music-url <region> <T/F>")
			, ("/r add-music-url <region> <url>")
			, ("/r rem-music-url <region> <url>")
			, ("/r reset-music-url <region>") }),

	PERMISSIONS (new String[] { ("/r perm-cache-add <region> <node>")
			, ("/r perm-cache-rem <region> <node>")
			, ("/r perm-cache-list <region>")
			, ("/r perm-cache-reset <region>")
			, ("/r perm-add-add <region> <node>")
			, ("/r perm-add-rem <region> <node>")
			, ("/r perm-add-list <region>")
			, ("/r perm-add-reset <region>")
			, ("/r perm-rem-add <region> <node>")
			, ("/r perm-rem-rem <region> <node>")
			, ("/r perm-rem-list <region>")
			, ("/r perm-rem-reset <region>") }),

	DATA (new String[] { ("/r save-region <region> <name>")
			, ("/r load-region <region> <name>")
			, ("/r list-backups <region>")
			, ("/r backup-database <region> <name>")
			, ("/r version")
			, ("/r check")
			, ("/r info <region>") });
			
	private String[] helpText;
			
	HelpText(String[] helpText) {
		this.helpText = helpText;
	}
	
	public String[] getText() {
		return helpText;
	}
}