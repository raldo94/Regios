package net.jzx7.regios.Commands;

import net.jzx7.regios.Mutable.MutableMessages;
import net.jzx7.regios.Permissions.PermissionsCore;
import net.jzx7.regios.regions.RegionManager;
import net.jzx7.regiosapi.regions.Region;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;


public class MessageCommands extends PermissionsCore {

	MutableMessages mutable = new MutableMessages();
	RegionManager rm = new RegionManager();

	public void setwelcome(Player p, String[] args) {
		if (doesHaveNode(p, "regios.messages.set-welcome")) {
			if (args.length >= 3) {
				setWelcome(rm.getRegion(args[1]), args[1], args, p);
			} else {
				p.sendMessage(ChatColor.RED + "[Regios] Invalid number of arguments specified.");
				p.sendMessage("Proper usage: /regios set-welcome <region> <message>");
				return;
			}
		} else {
			sendInvalidPerms(p);
		}
	}
	
	public void setleave(Player p, String[] args) {
		if (doesHaveNode(p, "regios.messages.set-leave")) {
			if (args.length >= 3) {
				setLeave(rm.getRegion(args[1]), args[1], args, p);
			} else {
				p.sendMessage(ChatColor.RED + "[Regios] Invalid number of arguments specified.");
				p.sendMessage("Proper usage: /regios set-leave <region> <message>");
				return;
			}
		} else {
			sendInvalidPerms(p);
		}
	}
	
	public void setpreventexit(Player p, String[] args) {
		if (doesHaveNode(p, "regios.messages.set-prevent-exit")) {
			if (args.length >= 3) {
				setPreventExit(rm.getRegion(args[1]), args[1], args, p);
			} else {
				p.sendMessage(ChatColor.RED + "[Regios] Invalid number of arguments specified.");
				p.sendMessage("Proper usage: /regios set-prevent-exit <region> <message>");
				return;
			}
		} else {
			sendInvalidPerms(p);
		}
	}
	
	public void setprevententry(Player p, String[] args) {
		if (doesHaveNode(p, "regios.messages.set-prevent-entry")) {
			if (args.length >= 3) {
				setPreventEntry(rm.getRegion(args[1]), args[1], args, p);
			} else {
				p.sendMessage(ChatColor.RED + "[Regios] Invalid number of arguments specified.");
				p.sendMessage("Proper usage: /regios set-prevent-entry <region> <message>");
				return;
			}
		} else {
			sendInvalidPerms(p);
		}
	}

	public void setprotection(Player p, String[] args) {
		if (doesHaveNode(p, "regios.messages.set-protection")) {
			if (args.length >= 3) {
				setProtection(rm.getRegion(args[1]), args[1], args, p);
			} else {
				p.sendMessage(ChatColor.RED + "[Regios] Invalid number of arguments specified.");
				p.sendMessage("Proper usage: /regios set-protection <region> <message>");
				return;
			}
		} else {
			sendInvalidPerms(p);
		}
	}

	public void showwelcome(Player p, String[] args) {
		if (doesHaveNode(p, "regios.messages.set-welcome")) {
			if (args.length >= 3) {
				setShowWelcome(rm.getRegion(args[1]), args[1], args[2], p);
			} else {
				p.sendMessage(ChatColor.RED + "[Regios] Invalid number of arguments specified.");
				p.sendMessage("Proper usage: /regios show-welcome <region> <T/F>");
				return;
			}
		} else {
			sendInvalidPerms(p);
		}
	}

	public void showleave(Player p, String[] args) {
		if (doesHaveNode(p, "regios.messages.set-leave")) {
			if (args.length >= 3) {
				setShowLeave(rm.getRegion(args[1]), args[1], args[2], p);
			} else {
				p.sendMessage(ChatColor.RED + "[Regios] Invalid number of arguments specified.");
				p.sendMessage("Proper usage: /regios show-leave <region> <T/F>");
				return;
			}
		} else {
			sendInvalidPerms(p);
		}
	}

	public void showprevententry(Player p, String[] args) {
		if (doesHaveNode(p, "regios.messages.set-prevent-entry")) {
			if (args.length >= 3) {
				setShowPreventEntry(rm.getRegion(args[1]), args[1], args[2], p);
			} else {
				p.sendMessage(ChatColor.RED + "[Regios] Invalid number of arguments specified.");
				p.sendMessage("Proper usage: /regios show-prevent-entry <region> <T/F>");
				return;
			}
		} else {
			sendInvalidPerms(p);
		}
	}

	public void showpreventexit(Player p, String[] args) {
		if (doesHaveNode(p, "regios.messages.set-prevent-exit")) {
			if (args.length >= 3) {
				setShowPreventExit(rm.getRegion(args[1]), args[1], args[2], p);
			} else {
				p.sendMessage(ChatColor.RED + "[Regios] Invalid number of arguments specified.");
				p.sendMessage("Proper usage: /regios show-prevent-exit <region> <T/F>");
				return;
			}
		} else {
			sendInvalidPerms(p);
		}
	}

	public void showprotection(Player p, String[] args) {
		if (doesHaveNode(p, "regios.messages.set-protection")) {
			if (args.length >= 3) {
				setShowProtection(rm.getRegion(args[1]), args[1], args[2], p);
			} else {
				p.sendMessage(ChatColor.RED + "[Regios] Invalid number of arguments specified.");
				p.sendMessage("Proper usage: /regios show-protection <region> <T/F>");
				return;
			}
		} else {
			sendInvalidPerms(p);
		}
	}

	public void showpvp(Player p, String[] args) {
		if (doesHaveNode(p, "regios.messages.set-pvp-toggle")) {
			if (args.length >= 3) {
				setShowPvpWarning(rm.getRegion(args[1]), args[1], args[2], p);
			} else {
				p.sendMessage(ChatColor.RED + "[Regios] Invalid number of arguments specified.");
				p.sendMessage("Proper usage: /regios show-pvp <region> <T/F>");
				return;
			}
		} else {
			sendInvalidPerms(p);
		}
	}

	private void setWelcome(Region r, String region, String[] message, Player p){
		String builder = "";
		for(int index = 2; index < message.length; index++){
			builder += message[index] + " ";
		}
		if(r == null){ p.sendMessage(ChatColor.RED + "[Regios] The region " + ChatColor.BLUE + region + ChatColor.RED + " doesn't exist!"); return; } else {
			if(!r.canModify(p)){
				p.sendMessage(ChatColor.RED + "[Regios] You are not permitted to modify this region!");
				return;
			}
			p.sendMessage(ChatColor.GREEN + "[Regios] Welcome message updated for region " + ChatColor.BLUE + region);
		}
		mutable.editWelcomeMessage(r, builder);
	}

	private void setLeave(Region r, String region, String[] message, Player p){
		String builder = "";
		for(int index = 2; index < message.length; index++){
			builder += message[index] + " ";
		}
		if(r == null){ p.sendMessage(ChatColor.RED + "[Regios] The region " + ChatColor.BLUE + region + ChatColor.RED + " doesn't exist!"); return; } else {
			if(!r.canModify(p)){
				p.sendMessage(ChatColor.RED + "[Regios] You are not permitted to modify this region!");
				return;
			}
			p.sendMessage(ChatColor.GREEN + "[Regios] Leave message updated for region " + ChatColor.BLUE + region);
		}
		mutable.editLeaveMessage(r, builder);
	}

	private void setPreventEntry(Region r, String region, String[] message, Player p){
		String builder = "";
		for(int index = 2; index < message.length; index++){
			builder += message[index] + " ";
		}
		if(r == null){ p.sendMessage(ChatColor.RED + "[Regios] The region " + ChatColor.BLUE + region + ChatColor.RED + " doesn't exist!"); return; } else {
			if(!r.canModify(p)){
				p.sendMessage(ChatColor.RED + "[Regios] You are not permitted to modify this region!");
				return;
			}
			p.sendMessage(ChatColor.GREEN + "[Regios] Prevent-entry message updated for region " + ChatColor.BLUE + region);
		}
		mutable.editPreventEntryMessage(r, builder);
	}

	private void setPreventExit(Region r, String region, String[] message, Player p){
		String builder = "";
		for(int index = 2; index < message.length; index++){
			builder += message[index] + " ";
		}
		if(r == null){ p.sendMessage(ChatColor.RED + "[Regios] The region " + ChatColor.BLUE + region + ChatColor.RED + " doesn't exist!"); return; } else {
			if(!r.canModify(p)){
				p.sendMessage(ChatColor.RED + "[Regios] You are not permitted to modify this region!");
				return;
			}
			p.sendMessage(ChatColor.GREEN + "[Regios] Prevent-exit message updated for region " + ChatColor.BLUE + region);
		}
		mutable.editPreventExitMessage(r, builder);
	}

	private void setProtection(Region r, String region, String[] message, Player p){
		String builder = "";
		for(int index = 2; index < message.length; index++){
			builder += message[index] + " ";
		}
		if(r == null){ p.sendMessage(ChatColor.RED + "[Regios] The region " + ChatColor.BLUE + region + ChatColor.RED + " doesn't exist!"); return; } else {
			if(!r.canModify(p)){
				p.sendMessage(ChatColor.RED + "[Regios] You are not permitted to modify this region!");
				return;
			}
			p.sendMessage(ChatColor.GREEN + "[Regios] Protection message updated for region " + ChatColor.BLUE + region);
		}
		mutable.editProtectionMessage(r, builder);
	}

	private void setShowWelcome(Region r, String region, String input, Player p){
		boolean val;
		try{
			val = Boolean.parseBoolean(input);
		} catch (Exception bfe){
			p.sendMessage(ChatColor.RED + "[Regios] The value for the 2nd paramteter must be boolean!");
			return;
		}
		if(r == null){ p.sendMessage(ChatColor.RED + "[Regios] The region " + ChatColor.BLUE + region + ChatColor.RED + " doesn't exist!"); return; } else {
			if(!r.canModify(p)){
				p.sendMessage(ChatColor.RED + "[Regios] You are not permitted to modify this region!");
				return;
			}
			p.sendMessage(ChatColor.GREEN + "[Regios] Show welcome message : " + ChatColor.BLUE + val);
		}
		mutable.editShowWelcomeMessage(r, val);
	}

	private void setShowLeave(Region r, String region, String input, Player p){
		boolean val;
		try{
			val = Boolean.parseBoolean(input);
		} catch (Exception bfe){
			p.sendMessage(ChatColor.RED + "[Regios] The value for the 2nd paramteter must be boolean!");
			return;
		}
		if(r == null){ p.sendMessage(ChatColor.RED + "[Regios] The region " + ChatColor.BLUE + region + ChatColor.RED + " doesn't exist!"); return; } else {
			if(!r.canModify(p)){
				p.sendMessage(ChatColor.RED + "[Regios] You are not permitted to modify this region!");
				return;
			}
			p.sendMessage(ChatColor.GREEN + "[Regios] Show leave message : " + ChatColor.BLUE + val);
		}
		mutable.editShowLeaveMessage(r, val);
	}

	private void setShowPreventEntry(Region r, String region, String input, Player p){
		boolean val;
		try{
			val = Boolean.parseBoolean(input);
		} catch (Exception bfe){
			p.sendMessage(ChatColor.RED + "[Regios] The value for the 2nd paramteter must be boolean!");
			return;
		}
		if(r == null){ p.sendMessage(ChatColor.RED + "[Regios] The region " + ChatColor.BLUE + region + ChatColor.RED + " doesn't exist!"); return; } else {
			if(!r.canModify(p)){
				p.sendMessage(ChatColor.RED + "[Regios] You are not permitted to modify this region!");
				return;
			}
			p.sendMessage(ChatColor.GREEN + "[Regios] Show prevent entry message : " + ChatColor.BLUE + val);
		}
		mutable.editShowPreventEntryMessage(r, val);
	}

	private void setShowPreventExit(Region r, String region, String input, Player p){
		boolean val;
		try{
			val = Boolean.parseBoolean(input);
		} catch (Exception bfe){
			p.sendMessage(ChatColor.RED + "[Regios] The value for the 2nd paramteter must be boolean!");
			return;
		}
		if(r == null){ p.sendMessage(ChatColor.RED + "[Regios] The region " + ChatColor.BLUE + region + ChatColor.RED + " doesn't exist!"); return; } else {
			if(!r.canModify(p)){
				p.sendMessage(ChatColor.RED + "[Regios] You are not permitted to modify this region!");
				return;
			}
			p.sendMessage(ChatColor.GREEN + "[Regios] Show prevent exit message : " + ChatColor.BLUE + val);
		}
		mutable.editShowPreventExitMessage(r, val);
	}

	private void setShowProtection(Region r, String region, String input, Player p){
		boolean val;
		try{
			val = Boolean.parseBoolean(input);
		} catch (Exception bfe){
			p.sendMessage(ChatColor.RED + "[Regios] The value for the 2nd paramteter must be boolean!");
			return;
		}
		if(r == null){ p.sendMessage(ChatColor.RED + "[Regios] The region " + ChatColor.BLUE + region + ChatColor.RED + " doesn't exist!"); return; } else {
			if(!r.canModify(p)){
				p.sendMessage(ChatColor.RED + "[Regios] You are not permitted to modify this region!");
				return;
			}
			p.sendMessage(ChatColor.GREEN + "[Regios] Show protection message : " + ChatColor.BLUE + val);
		}
		mutable.editShowProtectionMessage(r, val);
	}

	private void setShowPvpWarning(Region r, String region, String input, Player p){
		boolean val;
		try{
			val = Boolean.parseBoolean(input);
		} catch (Exception bfe){
			p.sendMessage(ChatColor.RED + "[Regios] The value for the 2nd paramteter must be boolean!");
			return;
		}
		if(r == null){ p.sendMessage(ChatColor.RED + "[Regios] The region " + ChatColor.BLUE + region + ChatColor.RED + " doesn't exist!"); return; } else {
			if(!r.canModify(p)){
				p.sendMessage(ChatColor.RED + "[Regios] You are not permitted to modify this region!");
				return;
			}
			p.sendMessage(ChatColor.GREEN + "[Regios] Show pvp message : " + ChatColor.BLUE + val);
		}
		mutable.editShowPvpWarningMessage(r, val);
	}

}
