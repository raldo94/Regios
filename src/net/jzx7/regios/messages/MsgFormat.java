package net.jzx7.regios.messages;

import net.jzx7.regiosapi.entity.RegiosPlayer;
import net.jzx7.regiosapi.regions.Region;

public class MsgFormat {
	
	public static String colourFormat(String message) {
		message = message.replaceAll("<BLACK>", "§0");
		message = message.replaceAll("<0>", "§0");

		message = message.replaceAll("<DBLUE>", "§1");
		message = message.replaceAll("<1>", "§1");

		message = message.replaceAll("<DGREEN>", "§2");
		message = message.replaceAll("<2>", "§2");

		message = message.replaceAll("<DTEAL>", "§3");
		message = message.replaceAll("<3>", "§3");

		message = message.replaceAll("<DRED>", "§4");
		message = message.replaceAll("<4>", "§4");

		message = message.replaceAll("<PURPLE>", "§5");
		message = message.replaceAll("<5>", "§5");

		message = message.replaceAll("<GOLD>", "§6");
		message = message.replaceAll("<6>", "§6");

		message = message.replaceAll("<GREY>", "§7");
		message = message.replaceAll("<7>", "§7");

		message = message.replaceAll("<DGREY>", "§8");
		message = message.replaceAll("<8>", "§8");

		message = message.replaceAll("<BLUE>", "§9");
		message = message.replaceAll("<9>", "§9");

		message = message.replaceAll("<BGREEN>", "§a");
		message = message.replaceAll("<A>", "§a");

		message = message.replaceAll("<TEAL>", "§b");
		message = message.replaceAll("<B>", "§b");

		message = message.replaceAll("<RED>", "§c");
		message = message.replaceAll("<C>", "§c");

		message = message.replaceAll("<PINK>", "§d");
		message = message.replaceAll("<D>", "§d");

		message = message.replaceAll("<YELLOW>", "§e");
		message = message.replaceAll("<E>", "§e");

		message = message.replaceAll("<WHITE>", "§f");
		message = message.replaceAll("<F>", "§f");

//		message = message.replaceAll("\\[", "");
//		message = message.replaceAll("\\]", "");

		return message;
	}
	
	public static String liveFormat(String original, RegiosPlayer p, Region r) {
		original = original.replaceAll("\\[", "");
		original = original.replaceAll("\\]", "");
		if (original.contains("PLAYER-COUNT")) {
			original = original.replaceAll("PLAYER-COUNT", "" + r.getPlayersInRegion().size());
		}
		if (original.contains("BUILD-RIGHTS")) {
			original = original.replaceAll("BUILD-RIGHTS", "" + r.canBypassProtection(p));
		}
		if (original.contains("PLAYER")) {
			original = original.replaceAll("PLAYER", "" + p.getName());
		}
		if (original.contains("PLAYER-LIST")) {
			StringBuilder builder = new StringBuilder();
			builder.append("");
			for (String play : r.getPlayersInRegion()) {
				builder.append(play).append(", ");
			}
			original = original.substring(0, original.length() - 1);
			original = original.replaceAll("PLAYER-LIST", "" + builder.toString());
		}
		if (original.contains("OWNER")) {
			original = original.replaceAll("OWNER", r.getOwner());
		}
		if (original.contains("NAME")) {
			original = original.replaceAll("NAME", r.getName());
		}
		return original;
	}
	
	public static String removeColourFormat(String message) {
		message = message.replaceAll("<BLACK>", "");
		message = message.replaceAll("<0>", "");

		message = message.replaceAll("<DBLUE>", "");
		message = message.replaceAll("<1>", "");

		message = message.replaceAll("<DGREEN>", "");
		message = message.replaceAll("<2>", "");

		message = message.replaceAll("<DTEAL>", "");
		message = message.replaceAll("<3>", "");

		message = message.replaceAll("<DRED>", "");
		message = message.replaceAll("<4>", "");

		message = message.replaceAll("<PURPLE>", "");
		message = message.replaceAll("<5>", "");

		message = message.replaceAll("<GOLD>", "");
		message = message.replaceAll("<6>", "");

		message = message.replaceAll("<GREY>", "");
		message = message.replaceAll("<7>", "");

		message = message.replaceAll("<DGREY>", "");
		message = message.replaceAll("<8>", "");

		message = message.replaceAll("<BLUE>", "");
		message = message.replaceAll("<9>", "");

		message = message.replaceAll("<BGREEN>", "");
		message = message.replaceAll("<A>", "");

		message = message.replaceAll("<TEAL>", "");
		message = message.replaceAll("<B>", "");

		message = message.replaceAll("<RED>", "");
		message = message.replaceAll("<C>", "");

		message = message.replaceAll("<PINK>", "");
		message = message.replaceAll("<D>", "");

		message = message.replaceAll("<YELLOW>", "");
		message = message.replaceAll("<E>", "");

		message = message.replaceAll("<WHITE>", "");
		message = message.replaceAll("<F>", "");

//		message = message.replaceAll("\\[", "");
//		message = message.replaceAll("\\]", "");

		return message;
	}
}