package net.jzx7.regios.Commands;

import net.jzx7.regios.bukkit.SpoutPlugin.SpoutInterface;
import net.jzx7.regios.bukkit.SpoutPlugin.Commands.SpoutHelp;
import net.jzx7.regios.bukkit.SpoutPlugin.GUI.ScreenHolder;
import net.jzx7.regios.messages.HelpText;
import net.jzx7.regiosapi.entity.RegiosPlayer;

import org.getspout.spoutapi.player.SpoutPlayer;

public class HelpCommands {
	
	public boolean help(RegiosPlayer p, String[] args) {
		if (SpoutInterface.doesPlayerHaveSpout(p)) {
			if(!SpoutInterface.isGlobal_spoutEnabled()){
				p.sendMessage("<RED>" + "The Spout server plugin is required for this feature!");
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

	private void getStandardHelp(RegiosPlayer p, String[] args) {
		String pre = "<DGREEN>" + "[Regios] ";
		if (args.length == 1) {
			p.sendMessage("<PURPLE>" + "[Regios] For more help use the commands below.");
			p.sendMessage("<PURPLE>" + "[Regios] -----------------------------------------");
			p.sendMessage("<DGREEN>" + "[Regios] /r help general");
			p.sendMessage("<DGREEN>" + "[Regios] /r help protection");
			p.sendMessage("<DGREEN>" + "[Regios] /r help fun");
			p.sendMessage("<DGREEN>" + "[Regios] /r help data");
			p.sendMessage("<DGREEN>" + "[Regios] /r help messages");
			p.sendMessage("<DGREEN>" + "[Regios] /r help inventory");
			p.sendMessage("<DGREEN>" + "[Regios] /r help modes");
			p.sendMessage("<DGREEN>" + "[Regios] /r help modify");
			p.sendMessage("<DGREEN>" + "[Regios] /r help exceptions");
			p.sendMessage("<DGREEN>" + "[Regios] /r help spout");
			p.sendMessage("<DGREEN>" + "[Regios] /r help permissions");
			p.sendMessage("<DGREEN>" + "[Regios] /r help other");
			p.sendMessage("<PURPLE>" + "[Regios] -----------------------------------------");
			return;
		} else if (args.length == 2) {
			p.sendMessage("<PURPLE>" + "[Regios] -----------------------------------------");
			if (args[1].equalsIgnoreCase("general")) {
				p.sendMessage("<DRED>" + "[General]");
				for (String gl : HelpText.GENERAL.getText()) {
					p.sendMessage(pre + gl);
				}
				p.sendMessage("<DRED>" + "[General]");
				p.sendMessage("<PURPLE>" + "[Regios] -----------------------------------------");
				return;
			} else if (args[1].equalsIgnoreCase("protection")) {
				p.sendMessage("<DRED>" + "[Protection]");
				for (String gl : HelpText.PROTECTION.getText()) {
					p.sendMessage(pre + gl);
				}
				p.sendMessage("<DRED>" + "[Protection]");

				p.sendMessage("<PURPLE>" + "[Regios] -----------------------------------------");
				return;
			} else if (args[1].equalsIgnoreCase("fun")) {
				p.sendMessage("<DRED>" + "[Fun]");
				for (String gl : HelpText.FUN.getText()) {
					p.sendMessage(pre + gl);
				}
				p.sendMessage("<DRED>" + "[Fun]");

				p.sendMessage("<PURPLE>" + "[Regios] -----------------------------------------");
				return;
			} else if (args[1].equalsIgnoreCase("data")) {
				p.sendMessage("<DRED>" + "[Data]");
				for (String gl : HelpText.DATA.getText()) {
					p.sendMessage(pre + gl);
				}
				p.sendMessage("<DRED>" + "[Data]");

				p.sendMessage("<PURPLE>" + "[Regios] -----------------------------------------");
				return;
			} else if (args[1].equalsIgnoreCase("messages")) {
				p.sendMessage("<DRED>" + "[Messages]");
				for (String gl : HelpText.MESSAGES.getText()) {
					p.sendMessage(pre + gl);
				}
				p.sendMessage("<DRED>" + "[Messages]");

				p.sendMessage("<PURPLE>" + "[Regios] -----------------------------------------");
				return;
			} else if (args[1].equalsIgnoreCase("inventory")) {
				p.sendMessage("<DRED>" + "[this.inventory]");
				for (String gl : HelpText.INVENTORY.getText()) {
					p.sendMessage(pre + gl);
				}
				p.sendMessage("<DRED>" + "[Inventory]");

				p.sendMessage("<PURPLE>" + "[Regios] -----------------------------------------");
				return;
			} else if (args[1].equalsIgnoreCase("modes")) {
				p.sendMessage("<DRED>" + "[Modes]");
				for (String gl : HelpText.MODE.getText()) {
					p.sendMessage(pre + gl);
				}
				p.sendMessage("<DRED>" + "[Modes]");

				p.sendMessage("<PURPLE>" + "[Regios] -----------------------------------------");
				return;
			} else if (args[1].equalsIgnoreCase("modify")) {
				p.sendMessage("<DRED>" + "[Modify]");
				for (String gl : HelpText.MODIFICATION.getText()) {
					p.sendMessage(pre + gl);
				}
				p.sendMessage("<DRED>" + "[Modify]");

				p.sendMessage("<PURPLE>" + "[Regios] -----------------------------------------");
				return;
			} else if (args[1].equalsIgnoreCase("exceptions")) {
				p.sendMessage("<DRED>" + "[Exceptions]");
				for (String gl : HelpText.EXCEPTIONS.getText()) {
					p.sendMessage(pre + gl);
				}
				p.sendMessage("<DRED>" + "[Exceptions]");

				p.sendMessage("<PURPLE>" + "[Regios] -----------------------------------------");
				return;
			} else if (args[1].equalsIgnoreCase("spout")) {
				p.sendMessage("<DRED>" + "[Spout]");
				for (String gl : HelpText.SPOUT.getText()) {
					p.sendMessage(pre + gl);
				}
				p.sendMessage("<DRED>" + "[Spout]");

				p.sendMessage("<PURPLE>" + "[Regios] -----------------------------------------");
				return;
			} else if (args[1].equalsIgnoreCase("permissions")) {
				p.sendMessage("<DRED>" + "[Permissions]");
				for (String gl : HelpText.PERMISSIONS.getText()) {
					p.sendMessage(pre + gl);
				}
				p.sendMessage("<DRED>" + "[Permissions]");

				p.sendMessage("<PURPLE>" + "[Regios] -----------------------------------------");
				return;
			} else if (args[1].equalsIgnoreCase("other")) {
				p.sendMessage("<DRED>" + "[Other]");
				for (String gl : HelpText.MISC.getText()) {
					p.sendMessage(pre + gl);
				}
				p.sendMessage("<DRED>" + "[Other]");

				p.sendMessage("<PURPLE>" + "[Regios] -----------------------------------------");
				return;
			}
			p.sendMessage("<DGREEN>" + "[Regios] /r help general");
			p.sendMessage("<DGREEN>" + "[Regios] /r help protection");
			p.sendMessage("<DGREEN>" + "[Regios] /r help fun");
			p.sendMessage("<DGREEN>" + "[Regios] /r help data");
			p.sendMessage("<DGREEN>" + "[Regios] /r help messages");
			p.sendMessage("<DGREEN>" + "[Regios] /r help inventory");
			p.sendMessage("<DGREEN>" + "[Regios] /r help modes");
			p.sendMessage("<DGREEN>" + "[Regios] /r help modify");
			p.sendMessage("<DGREEN>" + "[Regios] /r help exceptions");
			p.sendMessage("<DGREEN>" + "[Regios] /r help spout");
			p.sendMessage("<DGREEN>" + "[Regios] /r help permissions");
			p.sendMessage("<DGREEN>" + "[Regios] /r help other");
			p.sendMessage("<PURPLE>" + "[Regios] -----------------------------------------");
		}
	}

	

	

}
