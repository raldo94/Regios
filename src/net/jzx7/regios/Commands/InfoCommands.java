package net.jzx7.regios.Commands;

import net.jzx7.regios.Permissions.PermissionsCore;
import net.jzx7.regios.regions.RegionManager;
import net.jzx7.regiosapi.entity.RegiosPlayer;
import net.jzx7.regiosapi.regions.Region;

public class InfoCommands extends PermissionsCore{
	
	private final RegionManager rm = new RegionManager();
	
	public boolean info(RegiosPlayer p, String[] args) {
		if (doesHaveNode(p, "regios.data.info")) {
			if (args.length == 2) {
				showInfo(rm.getRegion(args[1]), args[1], p);
				return true;
			} else {
				p.sendMessage("<RED>" + "[Regios] Invalid number of arguments specified.");
				p.sendMessage("Proper usage: /regios info <region>");
				return true;
			}
		} else {
			sendInvalidPerms(p);
			return true;
		}
	}
	
	private void showInfo(Region r, String region, RegiosPlayer p){
		if (r == null) {
			p.sendMessage("<RED>" + "[Regios] The region " + "<BLUE>" + region + "<RED>" + " doesn't exist!");
			return;
		} else {
			p.sendMessage("<DGREEN>" + "[Regios] Info for region " + "<BLUE>" + region);
			p.sendMessage("<BLUE>" + "Owner : " + "<DGREEN>" + r.getOwner());
			p.sendMessage("<BLUE>" + "Protected : " + "<DGREEN>" + r.isProtected());
			p.sendMessage("<BLUE>" + "Protected-BB : " + "<DGREEN>" + r.is_protectionBreak());
			p.sendMessage("<BLUE>" + "Protected-BP : " + "<DGREEN>" + r.is_protectionPlace());
			p.sendMessage("<BLUE>" + "Prevent-Entry : " + "<DGREEN>" + r.isPreventEntry());
			p.sendMessage("<BLUE>" + "Prevent-Exit : " + "<DGREEN>" + r.isPreventExit());
			p.sendMessage("<BLUE>" + "Player Cap : " + "<DGREEN>" + r.getPlayerCap());
			p.sendMessage("<BLUE>" + "Health : " + "<DGREEN>" + r.isHealthEnabled());
			p.sendMessage("<BLUE>" + "Health-Regen : " + "<DGREEN>" + r.getHealthRegen());
			p.sendMessage("<BLUE>" + "PvP : " + "<DGREEN>" + r.isPvp());
			return;
		}
	}

}
