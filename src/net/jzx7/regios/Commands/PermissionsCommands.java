package net.jzx7.regios.Commands;

import net.jzx7.regios.Mutable.MutablePermissions;
import net.jzx7.regios.Permissions.PermissionsCore;
import net.jzx7.regiosapi.entity.RegiosPlayer;
import net.jzx7.regiosapi.regions.Region;

public class PermissionsCommands extends PermissionsCore {

	MutablePermissions mutable = new MutablePermissions();

	public void addToTempAddCache(Region r, String region, String message, RegiosPlayer p) {
		if (r == null) {
			p.sendMessage("<RED>" + "[Regios] The region " + "<BLUE>" + region + "<RED>" + " doesn't exist!");
			return;
		} else {
			if(!r.canModify(p)){
				p.sendMessage("<RED>" + "[Regios] You are not permitted to modify this region!");
				return;
			}
			if(mutable.checkTempAddCache(r, message)){
				p.sendMessage("<RED>" + "[Regios] The Node " + "<BLUE>" + message + "<RED>" + " already exists!");
				return;
			}
			p.sendMessage("<DGREEN>" + "[Regios] Node " + "<BLUE>" + message + "<DGREEN>" + " added to region cache " + "<BLUE>" + region);
		}
		mutable.editAddToTempAddCache(r, message);
	}

	public void removeFromTempAddCache(Region r, String region, String message, RegiosPlayer p) {
		if (r == null) {
			p.sendMessage("<RED>" + "[Regios] The region " + "<BLUE>" + region + "<RED>" + " doesn't exist!");
			return;
		} else {
			if(!r.canModify(p)){
				p.sendMessage("<RED>" + "[Regios] You are not permitted to modify this region!");
				return;
			}
			if(!mutable.checkTempAddCache(r, message)){
				p.sendMessage("<RED>" + "[Regios] The node " + "<BLUE>" + message + "<RED>" + " did not match any in the cache!");
				return;
			} else {
				p.sendMessage("<DGREEN>" + "[Regios] Node " + "<BLUE>" + message + "<DGREEN>" + " removed from region cache " + "<BLUE>" + region);
			}
		}
		mutable.editRemoveFromTempAddCache(r, message);
	}

	public void resetTempAddCache(Region r, String region, RegiosPlayer p) {
		if (r == null) {
			p.sendMessage("<RED>" + "[Regios] The region " + "<BLUE>" + region + "<RED>" + " doesn't exist!");
			return;
		} else {
			if(!r.canModify(p)){
				p.sendMessage("<RED>" + "[Regios] You are not permitted to modify this region!");
				return;
			}
			p.sendMessage("<DGREEN>" + "[Regios] Node cache reset for region " + "<BLUE>" + region);
		}
		mutable.editResetTempAddCache(r);
	}
	
	public void addToTempRemCache(Region r, String region, String message, RegiosPlayer p) {
		if (r == null) {
			p.sendMessage("<RED>" + "[Regios] The region " + "<BLUE>" + region + "<RED>" + " doesn't exist!");
			return;
		} else {
			if(!r.canModify(p)){
				p.sendMessage("<RED>" + "[Regios] You are not permitted to modify this region!");
				return;
			}
			if(mutable.checkTempRemCache(r, message)){
				p.sendMessage("<RED>" + "[Regios] The Node " + "<BLUE>" + message + "<RED>" + " already exists!");
				return;
			}
			p.sendMessage("<DGREEN>" + "[Regios] Node " + "<BLUE>" + message + "<DGREEN>" + " added to region cache " + "<BLUE>" + region);
		}
		mutable.editAddToTempRemCache(r, message);
	}

	public void removeFromTempRemCache(Region r, String region, String message, RegiosPlayer p) {
		if (r == null) {
			p.sendMessage("<RED>" + "[Regios] The region " + "<BLUE>" + region + "<RED>" + " doesn't exist!");
			return;
		} else {
			if(!r.canModify(p)){
				p.sendMessage("<RED>" + "[Regios] You are not permitted to modify this region!");
				return;
			}
			if(!mutable.checkTempRemCache(r, message)){
				p.sendMessage("<RED>" + "[Regios] The node " + "<BLUE>" + message + "<RED>" + " did not match any in the cache!");
				return;
			} else {
				p.sendMessage("<DGREEN>" + "[Regios] Node " + "<BLUE>" + message + "<DGREEN>" + " removed from region cache " + "<BLUE>" + region);
			}
		}
		mutable.editRemoveFromTempRemCache(r, message);
	}

	public void resetTempRemCache(Region r, String region, RegiosPlayer p) {
		if (r == null) {
			p.sendMessage("<RED>" + "[Regios] The region " + "<BLUE>" + region + "<RED>" + " doesn't exist!");
			return;
		} else {
			if(!r.canModify(p)){
				p.sendMessage("<RED>" + "[Regios] You are not permitted to modify this region!");
				return;
			}
			p.sendMessage("<DGREEN>" + "[Regios] Node cache reset for region " + "<BLUE>" + region);
		}
		mutable.editResetTempRemCache(r);
	}

	public void addToPermAddCache(Region r, String region, String message, RegiosPlayer p) {
		if (r == null) {
			p.sendMessage("<RED>" + "[Regios] The region " + "<BLUE>" + region + "<RED>" + " doesn't exist!");
			return;
		} else {
			if(!r.canModify(p)){
				p.sendMessage("<RED>" + "[Regios] You are not permitted to modify this region!");
				return;
			}
			if(mutable.checkPermAdd(r, message)){
				p.sendMessage("<RED>" + "[Regios] The Node " + "<BLUE>" + message + "<RED>" + " already exists!");
				return;
			}
			p.sendMessage("<DGREEN>" + "[Regios] Node " + "<BLUE>" + message + "<DGREEN>" + " added to region cache " + "<BLUE>" + region);
		}
		mutable.editAddToPermAddCache(r, message);
	}

	public void removeFromPermAddCache(Region r, String region, String message, RegiosPlayer p) {
		if (r == null) {
			p.sendMessage("<RED>" + "[Regios] The region " + "<BLUE>" + region + "<RED>" + " doesn't exist!");
			return;
		} else {
			if(!r.canModify(p)){
				p.sendMessage("<RED>" + "[Regios] You are not permitted to modify this region!");
				return;
			}
			if(!mutable.checkPermAdd(r, message)){
				p.sendMessage("<RED>" + "[Regios] The node " + "<BLUE>" + message + "<RED>" + " did not match any in the cache!");
				return;
			}
			p.sendMessage("<DGREEN>" + "[Regios] Node " + "<BLUE>" + message + "<DGREEN>" + " removed from region cache " + "<BLUE>" + region);
		}
		mutable.editRemoveFromPermAddCache(r, message);
	}

	public void resetPermAddCache(Region r, String region, RegiosPlayer p) {
		if (r == null) {
			p.sendMessage("<RED>" + "[Regios] The region " + "<BLUE>" + region + "<RED>" + " doesn't exist!");
			return;
		} else {
			if(!r.canModify(p)){
				p.sendMessage("<RED>" + "[Regios] You are not permitted to modify this region!");
				return;
			}
			p.sendMessage("<DGREEN>" + "[Regios] Node cache reset for region " + "<BLUE>" + region);
		}
		mutable.editResetPermAddCache(r);
	}

	public void addToPermRemCache(Region r, String region, String message, RegiosPlayer p) {
		if (r == null) {
			p.sendMessage("<RED>" + "[Regios] The region " + "<BLUE>" + region + "<RED>" + " doesn't exist!");
			return;
		} else {
			if(!r.canModify(p)){
				p.sendMessage("<RED>" + "[Regios] You are not permitted to modify this region!");
				return;
			}
			if(mutable.checkPermRemove(r, message)){
				p.sendMessage("<RED>" + "[Regios] The Node " + "<BLUE>" + message + "<RED>" + " already exists!");
				return;
			}
			p.sendMessage("<DGREEN>" + "[Regios] Node " + "<BLUE>" + message + "<DGREEN>" + " added to region cache " + "<BLUE>" + region);
		}
		mutable.editAddToPermRemoveCache(r, message);
	}

	public void removeFromPermRemCache(Region r, String region, String message, RegiosPlayer p) {
		if (r == null) {
			p.sendMessage("<RED>" + "[Regios] The region " + "<BLUE>" + region + "<RED>" + " doesn't exist!");
			return;
		} else {
			if(!r.canModify(p)){
				p.sendMessage("<RED>" + "[Regios] You are not permitted to modify this region!");
				return;
			}
			if(!mutable.checkPermRemove(r, message)){
				p.sendMessage("<RED>" + "[Regios] The node " + "<BLUE>" + message + "<RED>" + " did not match any in the cache!");
				return;
			}
			p.sendMessage("<DGREEN>" + "[Regios] Node " + "<BLUE>" + message + "<DGREEN>" + " removed from region cache " + "<BLUE>" + region);
		}
		mutable.editRemoveFromPermRemoveCache(r, message);
	}

	public void resetPermRemCache(Region r, String region, RegiosPlayer p) {
		if (r == null) {
			p.sendMessage("<RED>" + "[Regios] The region " + "<BLUE>" + region + "<RED>" + " doesn't exist!");
			return;
		} else {
			if(!r.canModify(p)){
				p.sendMessage("<RED>" + "[Regios] You are not permitted to modify this region!");
				return;
			}
			p.sendMessage("<DGREEN>" + "[Regios] Node cache reset for region " + "<BLUE>" + region);
		}
		mutable.editResetPermRemoveCache(r);
	}
	
	public void listTempAddCache(Region r, String region, RegiosPlayer p){
		if (r == null) {
			p.sendMessage("<RED>" + "[Regios] The region " + "<BLUE>" + region + "<RED>" + " doesn't exist!");
			return;
		} else {
			p.sendMessage("<DGREEN>" + "[Regios] Temp Node cache for region " + "<BLUE>" + region);
			p.sendMessage(mutable.listTempAddCache(r));
		}
	}
	
	public void listTempRemCache(Region r, String region, RegiosPlayer p){
		if (r == null) {
			p.sendMessage("<RED>" + "[Regios] The region " + "<BLUE>" + region + "<RED>" + " doesn't exist!");
			return;
		} else {
			p.sendMessage("<DGREEN>" + "[Regios] Temp Node cache for region " + "<BLUE>" + region);
			p.sendMessage(mutable.listTempRemCache(r));
		}
	}
	
	public void listPermAdd(Region r, String region, RegiosPlayer p){
		if (r == null) {
			p.sendMessage("<RED>" + "[Regios] The region " + "<BLUE>" + region + "<RED>" + " doesn't exist!");
			return;
		} else {
			p.sendMessage("<DGREEN>" + "[Regios] Perm Node adds for region " + "<BLUE>" + region);
			p.sendMessage(mutable.listPermAddCache(r));
		}
	}
	
	public void listPermRemCache(Region r, String region, RegiosPlayer p){
		if (r == null) {
			p.sendMessage("<RED>" + "[Regios] The region " + "<BLUE>" + region + "<RED>" + " doesn't exist!");
			return;
		} else {
			p.sendMessage("<DGREEN>" + "[Regios] Perm Node removals for region " + "<BLUE>" + region);
			p.sendMessage(mutable.listPermRemCache(r));
		}
	}

	public void addPermGroupAdd(Region r, String region, RegiosPlayer p, String group){
		if (r == null) {
			p.sendMessage("<RED>" + "[Regios] The region " + "<BLUE>" + region + "<RED>" + " doesn't exist!");
		} else {
			p.sendMessage("<DGREEN>" + "[Regios] Group " + "<BLUE>" + group + "<DGREEN>" + " was added to group add list for region " + "<BLUE>" + region);
			mutable.editAddPermGroupAdd(r, group);
		}
	}
	
	public void addPermGroupRemove(Region r, String region, RegiosPlayer p, String group){
		if (r == null) {
			p.sendMessage("<RED>" + "[Regios] The region " + "<BLUE>" + region + "<RED>" + " doesn't exist!");
		} else {
			p.sendMessage("<DGREEN>" + "[Regios] Group " + "<BLUE>" + group + "<DGREEN>" + " was added to group remove list for region " + "<BLUE>" + region);
			mutable.editAddPermGroupRemove(r, group);
		}
	}
	
	public void removePermGroupAdd(Region r, String region, RegiosPlayer p, String group){
		if (r == null) {
			p.sendMessage("<RED>" + "[Regios] The region " + "<BLUE>" + region + "<RED>" + " doesn't exist!");
		} else {
			p.sendMessage("<DGREEN>" + "[Regios] Group " + "<BLUE>" + group + "<DGREEN>" + " was removed to group add list for region " + "<BLUE>" + region);
			mutable.editRemovePermGroupAdd(r, group);
		}
	}
	
	public void removePermGroupRemove(Region r, String region, RegiosPlayer p, String group){
		if (r == null) {
			p.sendMessage("<RED>" + "[Regios] The region " + "<BLUE>" + region + "<RED>" + " doesn't exist!");
		} else {
			p.sendMessage("<DGREEN>" + "[Regios] Group " + "<BLUE>" + group + "<DGREEN>" + " was removed to group remove list for region " + "<BLUE>" + region);
			mutable.editRemovePermGroupRemove(r, group);
		}
	}
	
	public void addTempGroupAdd(Region r, String region, RegiosPlayer p, String group){
		if (r == null) {
			p.sendMessage("<RED>" + "[Regios] The region " + "<BLUE>" + region + "<RED>" + " doesn't exist!");
		} else {
			p.sendMessage("<DGREEN>" + "[Regios] Group " + "<BLUE>" + group + "<DGREEN>" + " added to group add list for region " + "<BLUE>" + region);
			mutable.editAddTempGroupAdd(r, group);
		}
	}
	
	public void removeTempGroupAdd(Region r, String region, RegiosPlayer p, String group){
		if (r == null) {
			p.sendMessage("<RED>" + "[Regios] The region " + "<BLUE>" + region + "<RED>" + " doesn't exist!");
		} else {
			p.sendMessage("<DGREEN>" + "[Regios] Group " + "<BLUE>" + group + "<DGREEN>" + " added to group remove list for region " + "<BLUE>" + region);
			mutable.editRemoveTempGroupAdd(r, group);
		}
	}
	
	public void addTempGroupRemove(Region r, String region, RegiosPlayer p, String group){
		if (r == null) {
			p.sendMessage("<RED>" + "[Regios] The region " + "<BLUE>" + region + "<RED>" + " doesn't exist!");
		} else {
			p.sendMessage("<DGREEN>" + "[Regios] Group " + "<BLUE>" + group + "<DGREEN>" + " added to group add list for region " + "<BLUE>" + region);
			mutable.editAddTempGroupRemove(r, group);
		}
	}
	
	public void removeTempGroupRemove(Region r, String region, RegiosPlayer p, String group){
		if (r == null) {
			p.sendMessage("<RED>" + "[Regios] The region " + "<BLUE>" + region + "<RED>" + " doesn't exist!");
		} else {
			p.sendMessage("<DGREEN>" + "[Regios] Group " + "<BLUE>" + group + "<DGREEN>" + " added to group remove list for region " + "<BLUE>" + region);
			mutable.editRemoveTempGroupRemove(r, group);
		}
	}
	
	public void listAddTempGroup(Region r, String region, RegiosPlayer p){
		if (r == null) {
			p.sendMessage("<RED>" + "[Regios] The region " + "<BLUE>" + region + "<RED>" + " doesn't exist!");
			return;
		} else {
			p.sendMessage("<DGREEN>" + "[Regios] Temp group add list for region " + "<BLUE>" + region);
			p.sendMessage(mutable.listTempAddGroup(r));
		}
	}
	
	public void listRemoveTempGroup(Region r, String region, RegiosPlayer p){
		if (r == null) {
			p.sendMessage("<RED>" + "[Regios] The region " + "<BLUE>" + region + "<RED>" + " doesn't exist!");
			return;
		} else {
			p.sendMessage("<DGREEN>" + "[Regios] Temp group remove list for region " + "<BLUE>" + region);
			p.sendMessage(mutable.listTempRemGroup(r));
		}
	}
	
	public void listAddPermGroup(Region r, String region, RegiosPlayer p){
		if (r == null) {
			p.sendMessage("<RED>" + "[Regios] The region " + "<BLUE>" + region + "<RED>" + " doesn't exist!");
			return;
		} else {
			p.sendMessage("<DGREEN>" + "[Regios] Perm group add list for region " + "<BLUE>" + region);
			p.sendMessage(mutable.listPermAddGroup(r));
			for (String group : r.getPermAddGroups())
			{
				p.sendMessage(group);
			}
		}
	}
	
	public void listRemovePermGroup(Region r, String region, RegiosPlayer p){
		if (r == null) {
			p.sendMessage("<RED>" + "[Regios] The region " + "<BLUE>" + region + "<RED>" + " doesn't exist!");
			return;
		} else {
			p.sendMessage("<DGREEN>" + "[Regios] Perm group removal list for region " + "<BLUE>" + region);
			p.sendMessage(mutable.listPermRemGroup(r));
			for (String group : r.getPermRemoveGroups())
			{
				p.sendMessage(group);
			}
		}
	}
}
