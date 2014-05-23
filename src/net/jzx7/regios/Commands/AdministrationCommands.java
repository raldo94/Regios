package net.jzx7.regios.Commands;

import java.io.File;

import net.jzx7.regios.Mutable.MutableAdministration;
import net.jzx7.regios.Permissions.PermissionsCore;
import net.jzx7.regios.regions.RegionManager;
import net.jzx7.regios.util.RegiosConversions;
import net.jzx7.regiosapi.entity.RegiosPlayer;
import net.jzx7.regiosapi.regions.Region;

public class AdministrationCommands extends PermissionsCore {
	
	protected MutableAdministration mutable = new MutableAdministration();
	
	protected final RegionManager rm = new RegionManager();
	
	public void listRegions(RegiosPlayer p, String[] args){
		if (args.length == 1) {
			if (PermissionsCore.doesHaveNode(p, "regios.data.list")) {
				p.sendMessage(mutable.listRegions());
			} else {
				PermissionsCore.sendInvalidPerms(p);
			}
		} else if (args.length == 2) {
			if (args[1].equalsIgnoreCase("owned")) {
				if (PermissionsCore.doesHaveNode(p, "regios.data.list-owned")) {
					p.sendMessage(mutable.listOwnedRegions(p.getName()));
				} else {
					PermissionsCore.sendInvalidPerms(p);
				}
			} else {
				if (RegiosConversions.getRegiosPlayer(args[1]) != null) {
					if (PermissionsCore.doesHaveNode(p, "regios.data.list-player")) {
						p.sendMessage(mutable.listOwnedRegions(args[1]));
					} else {
						PermissionsCore.sendInvalidPerms(p);
					}
				} else {
					p.sendMessage("[Regios] Invalid player specified!");
				}
			}
		} else {
			p.sendMessage("<RED>" + "[Regios] Invalid number of arguments specified.");
			p.sendMessage("Proper usage: /regios list [owned/playername]");
		}
	}
	
	public void reload(RegiosPlayer p){
		mutable.reload();
		p.sendMessage("<DGREEN>" + "[Regios] Complete reload completed.");
	}
	
	public void listRegionBackups(Region r, String region, RegiosPlayer p){
		if(r == null){
			p.sendMessage("<RED>" + "[Regios] The region to inherit : " + "<BLUE>" + region + "<RED>" + " does not exist!");
			return;
		}
		File f = rm.getBackupsDirectory(r);
		if(f.listFiles().length < 1){
			p.sendMessage("<RED>" + "[Regios] The region " + "<BLUE>" + region + "<RED>" + " has no backups!");
		} else {
			StringBuilder sb = new StringBuilder();
			for(File backup : f.listFiles()){
				sb.append("<WHITE>").append(backup.getName().substring(0, backup.getName().lastIndexOf("."))).append("<BLUE>").append(", ");
			}
			p.sendMessage(sb.toString());
			return;
		}
	}
	
	public void setOwner(Region r, String name, String owner, RegiosPlayer p){
		if(r == null){
			p.sendMessage("<RED>" + "[Regios] The region to inherit : " + "<BLUE>" + name + "<RED>" + " does not exist!");
			return;
		}
		if(!r.canModify(p)){
			p.sendMessage("<RED>" + "[Regios] You are not permitted to modify this region!");
			return;
		}
		mutable.setOwner(r, owner);
		p.sendMessage("<DGREEN>" + "[Regios] Owner for region " + "<BLUE>" + name + "<DGREEN>" + " changed to " + "<BLUE>" + owner);
	}
	
	public void inherit(Region tin, Region inf, String tinName, String infName, RegiosPlayer p){
		if(tin == null){
			p.sendMessage("<RED>" + "[Regios] The region to inherit : " + "<BLUE>" + tinName + "<RED>" + " does not exist!");
			return;
		}
		if(inf == null){
			p.sendMessage("<RED>" + "[Regios] The region to inherit from : " + "<BLUE>" + infName + "<RED>" + " does not exist!");
			return;
		}
		if(!tin.canModify(p)){
			p.sendMessage("<RED>" + "[Regios] You are not permitted to modify this region!");
			return;
		}
		mutable.inherit(tin, inf);
		p.sendMessage("<DGREEN>" + "[Regios] Region " + "<BLUE>" + tinName + "<DGREEN>" + " inherited properties from region " + "<BLUE>" + infName);
		return;
	}

}
