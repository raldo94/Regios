package net.jzx7.regios.Commands;

import net.jzx7.regios.Permissions.PermissionsCore;
import net.jzx7.regios.regions.RegionManager;
import net.jzx7.regiosapi.regions.Region;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;


public class InfoCommands extends PermissionsCore{
	
	private final RegionManager rm = new RegionManager();
	
	public boolean info(Player p, String[] args) {
		if (doesHaveNode(p, "regios.data.info")) {
			if (args.length == 2) {
				showInfo(rm.getRegion(args[1]), args[1], p);
				return true;
			} else {
				p.sendMessage(ChatColor.RED + "[Regios] Invalid number of arguments specified.");
				p.sendMessage("Proper usage: /regios info <region>");
				return true;
			}
		} else {
			sendInvalidPerms(p);
			return true;
		}
	}
	
	private void showInfo(Region r, String region, Player p){
		if (r == null) {
			p.sendMessage(ChatColor.RED + "[Regios] The region " + ChatColor.BLUE + region + ChatColor.RED + " doesn't exist!");
			return;
		} else {
			p.sendMessage(ChatColor.GREEN + "[Regios] Info for region " + ChatColor.BLUE + region);
			p.sendMessage(ChatColor.BLUE + "Owner : " + ChatColor.GREEN + r.getOwner());
			p.sendMessage(ChatColor.BLUE + "Protected : " + ChatColor.GREEN + r.isProtected());
			p.sendMessage(ChatColor.BLUE + "Protected-BB : " + ChatColor.GREEN + r.is_protectionBreak());
			p.sendMessage(ChatColor.BLUE + "Protected-BP : " + ChatColor.GREEN + r.is_protectionPlace());
			p.sendMessage(ChatColor.BLUE + "Prevent-Entry : " + ChatColor.GREEN + r.isPreventEntry());
			p.sendMessage(ChatColor.BLUE + "Prevent-Exit : " + ChatColor.GREEN + r.isPreventExit());
			p.sendMessage(ChatColor.BLUE + "Player Cap : " + ChatColor.GREEN + r.getPlayerCap());
			p.sendMessage(ChatColor.BLUE + "Health : " + ChatColor.GREEN + r.isHealthEnabled());
			p.sendMessage(ChatColor.BLUE + "Health-Regen : " + ChatColor.GREEN + r.getHealthRegen());
			p.sendMessage(ChatColor.BLUE + "PvP : " + ChatColor.GREEN + r.isPvp());
			return;
		}
	}

}
