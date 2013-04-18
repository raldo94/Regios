package net.jzx7.regios.Commands;

import net.jzx7.regios.Mutable.MutableModes;
import net.jzx7.regios.Permissions.PermissionsCore;
import net.jzx7.regiosapi.data.MODE;
import net.jzx7.regiosapi.entity.RegiosPlayer;
import net.jzx7.regiosapi.regions.Region;

public class ModeCommands extends PermissionsCore {
	
	MutableModes mutable = new MutableModes();
	
	public void setProtectionMode(Region r, String region, String input, RegiosPlayer p){
		MODE m = MODE.toMode(input);
		if(r == null){ p.sendMessage("<RED>" + "[Regios] The region " + "<BLUE>" + region + "<RED>" + " doesn't exist!"); return; } else {
			if(!r.canModify(p)){
				p.sendMessage("<RED>" + "[Regios] You are not permitted to modify this region!");
				return;
			}
			if(m == null){
				p.sendMessage("<RED>" + "[Regios] The mode must either be Blacklist or Whitelist");
				return;
			}
			p.sendMessage("<DGREEN>" + "[Regios] Protection mode updated for region " + "<BLUE>" + region);
		}
		mutable.editProtectionMode(r, m);
	}
	
	public void setPreventEntryMode(Region r, String region, String input, RegiosPlayer p){
		MODE m = MODE.toMode(input);
		if(r == null){ p.sendMessage("<RED>" + "[Regios] The region " + "<BLUE>" + region + "<RED>" + " doesn't exist!"); return; } else {
			if(!r.canModify(p)){
				p.sendMessage("<RED>" + "[Regios] You are not permitted to modify this region!");
				return;
			}
			if(m == null){
				p.sendMessage("<RED>" + "[Regios] The mode must either be Blacklist or Whitelist");
				return;
			}
			p.sendMessage("<DGREEN>" + "[Regios] Prevent Entry mode updated for region " + "<BLUE>" + region);
		}
		mutable.editPreventEntryMode(r, m);
	}
	
	public void setPreventExitMode(Region r, String region, String input, RegiosPlayer p){
		MODE m = MODE.toMode(input);
		if(r == null){ p.sendMessage("<RED>" + "[Regios] The region " + "<BLUE>" + region + "<RED>" + " doesn't exist!"); return; } else {
			if(!r.canModify(p)){
				p.sendMessage("<RED>" + "[Regios] You are not permitted to modify this region!");
				return;
			}
			if(m == null){
				p.sendMessage("<RED>" + "[Regios] The mode must either be Blacklist or Whitelist");
				return;
			}
			p.sendMessage("<DGREEN>" + "[Regios] Prevent Exit mode updated for region " + "<BLUE>" + region);
		}
		mutable.editPreventExitMode(r, m);
	}
	
	public void setItemControlMode(Region r, String region, String input, RegiosPlayer p){
		MODE m = MODE.toMode(input);
		if(r == null){ p.sendMessage("<RED>" + "[Regios] The region " + "<BLUE>" + region + "<RED>" + " doesn't exist!"); return; } else {
			if(!r.canModify(p)){
				p.sendMessage("<RED>" + "[Regios] You are not permitted to modify this region!");
				return;
			}
			if(m == null){
				p.sendMessage("<RED>" + "[Regios] The mode must either be Blacklist or Whitelist");
				return;
			}
			p.sendMessage("<DGREEN>" + "[Regios] Item Control mode updated for region " + "<BLUE>" + region);
		}
		mutable.editItemControlMode(r, m);
	}

}
