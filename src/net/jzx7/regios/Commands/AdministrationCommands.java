package net.jzx7.regios.Commands;

import java.io.File;

import net.jzx7.regios.Mutable.MutableAdministration;
import net.jzx7.regios.Permissions.PermissionsCore;
import net.jzx7.regiosapi.regions.Region;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;


public class AdministrationCommands extends PermissionsCore {
	
	MutableAdministration mutable = new MutableAdministration();
	
	public void listRegions(Player p, String[] args){
		if (args.length == 1) {
			if (PermissionsCore.doesHaveNode(p, "regios.data.list")) {
				p.sendMessage(mutable.listRegions());
			} else {
				PermissionsCore.sendInvalidPerms(p);
			}
		} else if (args.length == 2) {
			if (args[1].equalsIgnoreCase("owned")) {
				if (PermissionsCore.doesHaveNode(p, "regios.data.list-owned")) {
					p.sendMessage(mutable.listOwnedRegions(p));
				} else {
					PermissionsCore.sendInvalidPerms(p);
				}
			} else {
				if (Bukkit.getPlayer(args[1].toString()) != null) {
					if (PermissionsCore.doesHaveNode(p, "regios.data.list-player")) {
						p.sendMessage(mutable.listOwnedRegions(Bukkit.getPlayer(args[1].toString())));
					} else {
						PermissionsCore.sendInvalidPerms(p);
					}
				} else {
					p.sendMessage("[Regios] Invalid player specified!");
				}
			}
		} else {
			p.sendMessage(ChatColor.RED + "[Regios] Invalid number of arguments specified.");
			p.sendMessage("Proper usage: /regios list [owned/playername]");
		}
	}
	
	public void reload(Player p){
		mutable.reload();
		p.sendMessage(ChatColor.GREEN + "[Regios] Complete reload completed.");
	}
	
	public void listRegionBackups(Region r, String region, Player p){
		if(r == null){
			p.sendMessage(ChatColor.RED + "[Regios] The region to inherit : " + ChatColor.BLUE + region + ChatColor.RED + " does not exist!");
			return;
		}
		File f = r.getBackupsDirectory();
		if(f.listFiles().length < 1){
			p.sendMessage(ChatColor.RED + "[Regios] The region " + ChatColor.BLUE + region + ChatColor.RED + " has no backups!");
		} else {
			StringBuilder sb = new StringBuilder();
			for(File backup : f.listFiles()){
				sb.append(ChatColor.WHITE).append(backup.getName().substring(0, backup.getName().lastIndexOf("."))).append(ChatColor.BLUE).append(", ");
			}
			p.sendMessage(sb.toString());
			return;
		}
	}
	
	public void setOwner(Region r, String name, String owner, Player p){
		if(r == null){
			p.sendMessage(ChatColor.RED + "[Regios] The region to inherit : " + ChatColor.BLUE + name + ChatColor.RED + " does not exist!");
			return;
		}
		if(!r.canModify(p)){
			p.sendMessage(ChatColor.RED + "[Regios] You are not permitted to modify this region!");
			return;
		}
		mutable.setOwner(r, owner);
		p.sendMessage(ChatColor.GREEN + "[Regios] Owner for region " + ChatColor.BLUE + name + ChatColor.GREEN + " changed to " + ChatColor.BLUE + owner);
	}
	
	public void inherit(Region tin, Region inf, String tinName, String infName, Player p){
		if(tin == null){
			p.sendMessage(ChatColor.RED + "[Regios] The region to inherit : " + ChatColor.BLUE + tinName + ChatColor.RED + " does not exist!");
			return;
		}
		if(inf == null){
			p.sendMessage(ChatColor.RED + "[Regios] The region to inherit from : " + ChatColor.BLUE + infName + ChatColor.RED + " does not exist!");
			return;
		}
		if(!tin.canModify(p)){
			p.sendMessage(ChatColor.RED + "[Regios] You are not permitted to modify this region!");
			return;
		}
		mutable.inherit(tin, inf);
		p.sendMessage(ChatColor.GREEN + "[Regios] Region " + ChatColor.BLUE + tinName + ChatColor.GREEN + " inherited properties from region " + ChatColor.BLUE + infName);
		return;
	}

}
