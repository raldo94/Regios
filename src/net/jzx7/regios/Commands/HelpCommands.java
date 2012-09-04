package net.jzx7.regios.Commands;

import net.jzx7.regios.Data.HelpText;
import net.jzx7.regios.Spout.SpoutInterface;
import net.jzx7.regios.Spout.Commands.SpoutHelp;
import net.jzx7.regios.Spout.GUI.ScreenHolder;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.getspout.spoutapi.player.SpoutPlayer;


public class HelpCommands {
	
	public boolean help(Player p, String[] args) {
		if (SpoutInterface.doesPlayerHaveSpout(p)) {
			if(!SpoutInterface.global_spoutEnabled){
				p.sendMessage(ChatColor.RED + "The Spout server plugin is required for this feature!");
				return true;
			}
			ScreenHolder sh = ScreenHolder.getScreenHolder((SpoutPlayer) p);
			sh.addScreenHolder((SpoutPlayer)p, sh);
			new SpoutHelp().getSpoutHelp((SpoutPlayer) p, sh);
			return true;
		} else {
			getStandardHelp(p, args);
			return true;
		}
	}

	private void getStandardHelp(Player p, String[] args) {
		String pre = ChatColor.GREEN + "[Regios] ";
		if (args.length == 1) {
			p.sendMessage(ChatColor.LIGHT_PURPLE + "[Regios] For more help use the commands below.");
			p.sendMessage(ChatColor.LIGHT_PURPLE + "[Regios] -----------------------------------------");
			p.sendMessage(ChatColor.GREEN + "[Regios] /r help general");
			p.sendMessage(ChatColor.GREEN + "[Regios] /r help protection");
			p.sendMessage(ChatColor.GREEN + "[Regios] /r help fun");
			p.sendMessage(ChatColor.GREEN + "[Regios] /r help data");
			p.sendMessage(ChatColor.GREEN + "[Regios] /r help messages");
			p.sendMessage(ChatColor.GREEN + "[Regios] /r help inventory");
			p.sendMessage(ChatColor.GREEN + "[Regios] /r help modes");
			p.sendMessage(ChatColor.GREEN + "[Regios] /r help modify");
			p.sendMessage(ChatColor.GREEN + "[Regios] /r help exceptions");
			p.sendMessage(ChatColor.GREEN + "[Regios] /r help spout");
			p.sendMessage(ChatColor.GREEN + "[Regios] /r help permissions");
			p.sendMessage(ChatColor.GREEN + "[Regios] /r help other");
			p.sendMessage(ChatColor.LIGHT_PURPLE + "[Regios] -----------------------------------------");
			return;
		} else if (args.length == 2) {
			p.sendMessage(ChatColor.LIGHT_PURPLE + "[Regios] -----------------------------------------");
			if (args[1].equalsIgnoreCase("general")) {
				p.sendMessage(ChatColor.DARK_RED + "[General]");
				for (String gl : HelpText.GENERAL.getText()) {
					p.sendMessage(pre + gl);
				}
				p.sendMessage(ChatColor.DARK_RED + "[General]");
				p.sendMessage(ChatColor.LIGHT_PURPLE + "[Regios] -----------------------------------------");
				return;
			} else if (args[1].equalsIgnoreCase("protection")) {
				p.sendMessage(ChatColor.DARK_RED + "[Protection]");
				for (String gl : HelpText.PROTECTION.getText()) {
					p.sendMessage(pre + gl);
				}
				p.sendMessage(ChatColor.DARK_RED + "[Protection]");

				p.sendMessage(ChatColor.LIGHT_PURPLE + "[Regios] -----------------------------------------");
				return;
			} else if (args[1].equalsIgnoreCase("fun")) {
				p.sendMessage(ChatColor.DARK_RED + "[Fun]");
				for (String gl : HelpText.FUN.getText()) {
					p.sendMessage(pre + gl);
				}
				p.sendMessage(ChatColor.DARK_RED + "[Fun]");

				p.sendMessage(ChatColor.LIGHT_PURPLE + "[Regios] -----------------------------------------");
				return;
			} else if (args[1].equalsIgnoreCase("data")) {
				p.sendMessage(ChatColor.DARK_RED + "[Data]");
				for (String gl : HelpText.DATA.getText()) {
					p.sendMessage(pre + gl);
				}
				p.sendMessage(ChatColor.DARK_RED + "[Data]");

				p.sendMessage(ChatColor.LIGHT_PURPLE + "[Regios] -----------------------------------------");
				return;
			} else if (args[1].equalsIgnoreCase("messages")) {
				p.sendMessage(ChatColor.DARK_RED + "[Messages]");
				for (String gl : HelpText.MESSAGES.getText()) {
					p.sendMessage(pre + gl);
				}
				p.sendMessage(ChatColor.DARK_RED + "[Messages]");

				p.sendMessage(ChatColor.LIGHT_PURPLE + "[Regios] -----------------------------------------");
				return;
			} else if (args[1].equalsIgnoreCase("inventory")) {
				p.sendMessage(ChatColor.DARK_RED + "[this.inventory]");
				for (String gl : HelpText.INVENTORY.getText()) {
					p.sendMessage(pre + gl);
				}
				p.sendMessage(ChatColor.DARK_RED + "[Inventory]");

				p.sendMessage(ChatColor.LIGHT_PURPLE + "[Regios] -----------------------------------------");
				return;
			} else if (args[1].equalsIgnoreCase("modes")) {
				p.sendMessage(ChatColor.DARK_RED + "[Modes]");
				for (String gl : HelpText.MODE.getText()) {
					p.sendMessage(pre + gl);
				}
				p.sendMessage(ChatColor.DARK_RED + "[Modes]");

				p.sendMessage(ChatColor.LIGHT_PURPLE + "[Regios] -----------------------------------------");
				return;
			} else if (args[1].equalsIgnoreCase("modify")) {
				p.sendMessage(ChatColor.DARK_RED + "[Modify]");
				for (String gl : HelpText.MODIFICATION.getText()) {
					p.sendMessage(pre + gl);
				}
				p.sendMessage(ChatColor.DARK_RED + "[Modify]");

				p.sendMessage(ChatColor.LIGHT_PURPLE + "[Regios] -----------------------------------------");
				return;
			} else if (args[1].equalsIgnoreCase("exceptions")) {
				p.sendMessage(ChatColor.DARK_RED + "[Exceptions]");
				for (String gl : HelpText.EXCEPTIONS.getText()) {
					p.sendMessage(pre + gl);
				}
				p.sendMessage(ChatColor.DARK_RED + "[Exceptions]");

				p.sendMessage(ChatColor.LIGHT_PURPLE + "[Regios] -----------------------------------------");
				return;
			} else if (args[1].equalsIgnoreCase("spout")) {
				p.sendMessage(ChatColor.DARK_RED + "[Spout]");
				for (String gl : HelpText.SPOUT.getText()) {
					p.sendMessage(pre + gl);
				}
				p.sendMessage(ChatColor.DARK_RED + "[Spout]");

				p.sendMessage(ChatColor.LIGHT_PURPLE + "[Regios] -----------------------------------------");
				return;
			} else if (args[1].equalsIgnoreCase("permissions")) {
				p.sendMessage(ChatColor.DARK_RED + "[Permissions]");
				for (String gl : HelpText.PERMISSIONS.getText()) {
					p.sendMessage(pre + gl);
				}
				p.sendMessage(ChatColor.DARK_RED + "[Permissions]");

				p.sendMessage(ChatColor.LIGHT_PURPLE + "[Regios] -----------------------------------------");
				return;
			} else if (args[1].equalsIgnoreCase("other")) {
				p.sendMessage(ChatColor.DARK_RED + "[Other]");
				for (String gl : HelpText.MISC.getText()) {
					p.sendMessage(pre + gl);
				}
				p.sendMessage(ChatColor.DARK_RED + "[Other]");

				p.sendMessage(ChatColor.LIGHT_PURPLE + "[Regios] -----------------------------------------");
				return;
			}
			p.sendMessage(ChatColor.GREEN + "[Regios] /r help general");
			p.sendMessage(ChatColor.GREEN + "[Regios] /r help protection");
			p.sendMessage(ChatColor.GREEN + "[Regios] /r help fun");
			p.sendMessage(ChatColor.GREEN + "[Regios] /r help data");
			p.sendMessage(ChatColor.GREEN + "[Regios] /r help messages");
			p.sendMessage(ChatColor.GREEN + "[Regios] /r help inventory");
			p.sendMessage(ChatColor.GREEN + "[Regios] /r help modes");
			p.sendMessage(ChatColor.GREEN + "[Regios] /r help modify");
			p.sendMessage(ChatColor.GREEN + "[Regios] /r help exceptions");
			p.sendMessage(ChatColor.GREEN + "[Regios] /r help spout");
			p.sendMessage(ChatColor.GREEN + "[Regios] /r help permissions");
			p.sendMessage(ChatColor.GREEN + "[Regios] /r help other");
			p.sendMessage(ChatColor.LIGHT_PURPLE + "[Regios] -----------------------------------------");
		}
	}

	

	

}
