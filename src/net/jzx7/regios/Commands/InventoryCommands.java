package net.jzx7.regios.Commands;

import net.jzx7.regios.Mutable.MutableInventory;
import net.jzx7.regios.Permissions.PermissionsCore;
import net.jzx7.regiosapi.entity.RegiosPlayer;
import net.jzx7.regiosapi.regions.Region;

public class InventoryCommands extends PermissionsCore{
	
	MutableInventory mutable = new MutableInventory();
	
	public void setPermWipeOnExit(Region r, String region, String input, RegiosPlayer p){
		boolean val;
		try{
			val = Boolean.parseBoolean(input);
		} catch (Exception bfe){
			p.sendMessage("<RED>" + "[Regios] The value for the 2nd paramteter must be boolean!");
			return;
		}
		if(r == null){ p.sendMessage("<RED>" + "[Regios] The region " + "<BLUE>" + region + "<RED>" + " doesn't exist!"); return; } else {
			if(!r.canModify(p)){
				p.sendMessage("<RED>" + "[Regios] You are not permitted to modify this region!");
				return;
			}
			if(val){
				p.sendMessage("<DGREEN>" + "[Regios] Perm wipe on exit enabled for region " + "<BLUE>" + region);
			} else {
				p.sendMessage("<DGREEN>" + "[Regios] Perm wipe on exit disabled for region " + "<BLUE>" + region);
			}
		}
		mutable.editPermWipeOnExit(r, val);
	}
	
	public void setPermWipeOnEntry(Region r, String region, String input, RegiosPlayer p){
		boolean val;
		try{
			val = Boolean.parseBoolean(input);
		} catch (Exception bfe){
			p.sendMessage("<RED>" + "[Regios] The value for the 2nd paramteter must be boolean!");
			return;
		}
		if(r == null){ p.sendMessage("<RED>" + "[Regios] The region " + "<BLUE>" + region + "<RED>" + " doesn't exist!"); return; } else {
			if(!r.canModify(p)){
				p.sendMessage("<RED>" + "[Regios] You are not permitted to modify this region!");
				return;
			}
			if(val){
				p.sendMessage("<DGREEN>" + "[Regios] Perm wipe on entry enabled for region " + "<BLUE>" + region);
			} else {
				p.sendMessage("<DGREEN>" + "[Regios] Perm wipe on entry disabled for region " + "<BLUE>" + region);
			}
		}
		mutable.editPermWipeOnEnter(r, val);
	}
	
	public void setWipeAndCacheOnExit(Region r, String region, String input, RegiosPlayer p){
		boolean val;
		try{
			val = Boolean.parseBoolean(input);
		} catch (Exception bfe){
			p.sendMessage("<RED>" + "[Regios] The value for the 2nd paramteter must be boolean!");
			return;
		}
		if(r == null){ p.sendMessage("<RED>" + "[Regios] The region " + "<BLUE>" + region + "<RED>" + " doesn't exist!"); return; } else {
			if(!r.canModify(p)){
				p.sendMessage("<RED>" + "[Regios] You are not permitted to modify this region!");
				return;
			}
			if(val){
				p.sendMessage("<DGREEN>" + "[Regios] Wipe and cache on exit enabled for region " + "<BLUE>" + region);
			} else {
				p.sendMessage("<DGREEN>" + "[Regios] Wipe and cache on exit disabled for region " + "<BLUE>" + region);
			}
		}
		mutable.editWipeAndCacheOnExit(r, val);
	}
	
	public void setWipeAndCacheOnEntry(Region r, String region, String input, RegiosPlayer p){
		boolean val;
		try{
			val = Boolean.parseBoolean(input);
		} catch (Exception bfe){
			p.sendMessage("<RED>" + "[Regios] The value for the 2nd paramteter must be boolean!");
			return;
		}
		if(r == null){ p.sendMessage("<RED>" + "[Regios] The region " + "<BLUE>" + region + "<RED>" + " doesn't exist!"); return; } else {
			if(!r.canModify(p)){
				p.sendMessage("<RED>" + "[Regios] You are not permitted to modify this region!");
				return;
			}
			if(val){
				p.sendMessage("<DGREEN>" + "[Regios] Wipe and cache on entry enabled for region " + "<BLUE>" + region);
			} else {
				p.sendMessage("<DGREEN>" + "[Regios] Wipe and cache on entry disabled for region " + "<BLUE>" + region);
			}
		}
		mutable.editWipeAndCacheOnEnter(r, val);
	}

}
