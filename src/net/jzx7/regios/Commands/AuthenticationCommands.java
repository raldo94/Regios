package net.jzx7.regios.Commands;

import net.jzx7.regios.Listeners.RegiosPlayerListener;
import net.jzx7.regiosapi.regions.Region;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class AuthenticationCommands {
	
	public void auth(Player p, String[] args) {
		if (args.length == 2) {
			sendPassword(p, args[1], RegiosPlayerListener.regionBinding.get(p));
			return;
		} else {
			p.sendMessage(ChatColor.RED + "[Regios] Invalid number of arguments specified.");
			p.sendMessage("Proper usage: /regios auth <password>");
			return;
		}
	}
	
	private void sendPassword(Player p, String password, Region r){
		if(r == null){
			p.sendMessage(ChatColor.RED + "[Regios] You must first try to enter a region before authorising yourself!");
			return;
		}
		
		if(r.getAuthentication(password, p)){
			p.sendMessage(ChatColor.GREEN + "[Regios] Authentication successfull! You can now enter/exit region " + ChatColor.BLUE + r.getName());
			return;
		} else {
			p.sendMessage(ChatColor.RED + "[Regios] Invalid password for region " + ChatColor.BLUE + r.getName());
			return;
		}
		
	}

}
