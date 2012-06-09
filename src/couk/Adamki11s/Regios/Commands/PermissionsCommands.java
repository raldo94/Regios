package couk.Adamki11s.Regios.Commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import couk.Adamki11s.Regios.Mutable.MutablePermissions;
import couk.Adamki11s.Regios.Permissions.PermissionsCore;
import couk.Adamki11s.Regios.Regions.Region;

public class PermissionsCommands extends PermissionsCore {

	MutablePermissions mutable = new MutablePermissions();

	public void addToTempAddCache(Region r, String region, String message, Player p) {
		if (r == null) {
			p.sendMessage(ChatColor.RED + "[Regios] The region " + ChatColor.BLUE + region + ChatColor.RED + " doesn't exist!");
			return;
		} else {
			if(!r.canModify(p)){
				p.sendMessage(ChatColor.RED + "[Regios] You are not permitted to modify this region!");
				return;
			}
			if(mutable.checkTempAddCache(r, message)){
				p.sendMessage(ChatColor.RED + "[Regios] The Node " + ChatColor.BLUE + message + ChatColor.RED + " already exists!");
				return;
			}
			p.sendMessage(ChatColor.GREEN + "[Regios] Node " + ChatColor.BLUE + message + ChatColor.GREEN + " added to region cache " + ChatColor.BLUE + region);
		}
		mutable.editAddToTempAddCache(r, message);
	}

	public void removeFromTempAddCache(Region r, String region, String message, Player p) {
		if (r == null) {
			p.sendMessage(ChatColor.RED + "[Regios] The region " + ChatColor.BLUE + region + ChatColor.RED + " doesn't exist!");
			return;
		} else {
			if(!r.canModify(p)){
				p.sendMessage(ChatColor.RED + "[Regios] You are not permitted to modify this region!");
				return;
			}
			if(!mutable.checkTempAddCache(r, message)){
				p.sendMessage(ChatColor.RED + "[Regios] The node " + ChatColor.BLUE + message + ChatColor.RED + " did not match any in the cache!");
				return;
			} else {
				p.sendMessage(ChatColor.GREEN + "[Regios] Node " + ChatColor.BLUE + message + ChatColor.GREEN + " removed from region cache " + ChatColor.BLUE + region);
			}
		}
		mutable.editRemoveFromTempAddCache(r, message);
	}

	public void resetTempAddCache(Region r, String region, Player p) {
		if (r == null) {
			p.sendMessage(ChatColor.RED + "[Regios] The region " + ChatColor.BLUE + region + ChatColor.RED + " doesn't exist!");
			return;
		} else {
			if(!r.canModify(p)){
				p.sendMessage(ChatColor.RED + "[Regios] You are not permitted to modify this region!");
				return;
			}
			p.sendMessage(ChatColor.GREEN + "[Regios] Node cache reset for region " + ChatColor.BLUE + region);
		}
		mutable.editResetTempAddCache(r);
	}
	
	public void addToTempRemCache(Region r, String region, String message, Player p) {
		if (r == null) {
			p.sendMessage(ChatColor.RED + "[Regios] The region " + ChatColor.BLUE + region + ChatColor.RED + " doesn't exist!");
			return;
		} else {
			if(!r.canModify(p)){
				p.sendMessage(ChatColor.RED + "[Regios] You are not permitted to modify this region!");
				return;
			}
			if(mutable.checkTempRemCache(r, message)){
				p.sendMessage(ChatColor.RED + "[Regios] The Node " + ChatColor.BLUE + message + ChatColor.RED + " already exists!");
				return;
			}
			p.sendMessage(ChatColor.GREEN + "[Regios] Node " + ChatColor.BLUE + message + ChatColor.GREEN + " added to region cache " + ChatColor.BLUE + region);
		}
		mutable.editAddToTempRemCache(r, message);
	}

	public void removeFromTempRemCache(Region r, String region, String message, Player p) {
		if (r == null) {
			p.sendMessage(ChatColor.RED + "[Regios] The region " + ChatColor.BLUE + region + ChatColor.RED + " doesn't exist!");
			return;
		} else {
			if(!r.canModify(p)){
				p.sendMessage(ChatColor.RED + "[Regios] You are not permitted to modify this region!");
				return;
			}
			if(!mutable.checkTempRemCache(r, message)){
				p.sendMessage(ChatColor.RED + "[Regios] The node " + ChatColor.BLUE + message + ChatColor.RED + " did not match any in the cache!");
				return;
			} else {
				p.sendMessage(ChatColor.GREEN + "[Regios] Node " + ChatColor.BLUE + message + ChatColor.GREEN + " removed from region cache " + ChatColor.BLUE + region);
			}
		}
		mutable.editRemoveFromTempRemCache(r, message);
	}

	public void resetTempRemCache(Region r, String region, Player p) {
		if (r == null) {
			p.sendMessage(ChatColor.RED + "[Regios] The region " + ChatColor.BLUE + region + ChatColor.RED + " doesn't exist!");
			return;
		} else {
			if(!r.canModify(p)){
				p.sendMessage(ChatColor.RED + "[Regios] You are not permitted to modify this region!");
				return;
			}
			p.sendMessage(ChatColor.GREEN + "[Regios] Node cache reset for region " + ChatColor.BLUE + region);
		}
		mutable.editResetTempRemCache(r);
	}

	public void addToPermAddCache(Region r, String region, String message, Player p) {
		if (r == null) {
			p.sendMessage(ChatColor.RED + "[Regios] The region " + ChatColor.BLUE + region + ChatColor.RED + " doesn't exist!");
			return;
		} else {
			if(!r.canModify(p)){
				p.sendMessage(ChatColor.RED + "[Regios] You are not permitted to modify this region!");
				return;
			}
			if(mutable.checkPermAdd(r, message)){
				p.sendMessage(ChatColor.RED + "[Regios] The Node " + ChatColor.BLUE + message + ChatColor.RED + " already exists!");
				return;
			}
			p.sendMessage(ChatColor.GREEN + "[Regios] Node " + ChatColor.BLUE + message + ChatColor.GREEN + " added to region cache " + ChatColor.BLUE + region);
		}
		mutable.editAddToPermAddCache(r, message);
	}

	public void removeFromPermAddCache(Region r, String region, String message, Player p) {
		if (r == null) {
			p.sendMessage(ChatColor.RED + "[Regios] The region " + ChatColor.BLUE + region + ChatColor.RED + " doesn't exist!");
			return;
		} else {
			if(!r.canModify(p)){
				p.sendMessage(ChatColor.RED + "[Regios] You are not permitted to modify this region!");
				return;
			}
			if(!mutable.checkPermAdd(r, message)){
				p.sendMessage(ChatColor.RED + "[Regios] The node " + ChatColor.BLUE + message + ChatColor.RED + " did not match any in the cache!");
				return;
			}
			p.sendMessage(ChatColor.GREEN + "[Regios] Node " + ChatColor.BLUE + message + ChatColor.GREEN + " removed from region cache " + ChatColor.BLUE + region);
		}
		mutable.editRemoveFromPermAddCache(r, message);
	}

	public void resetPermAddCache(Region r, String region, Player p) {
		if (r == null) {
			p.sendMessage(ChatColor.RED + "[Regios] The region " + ChatColor.BLUE + region + ChatColor.RED + " doesn't exist!");
			return;
		} else {
			if(!r.canModify(p)){
				p.sendMessage(ChatColor.RED + "[Regios] You are not permitted to modify this region!");
				return;
			}
			p.sendMessage(ChatColor.GREEN + "[Regios] Node cache reset for region " + ChatColor.BLUE + region);
		}
		mutable.editResetPermAddCache(r);
	}

	public void addToPermRemCache(Region r, String region, String message, Player p) {
		if (r == null) {
			p.sendMessage(ChatColor.RED + "[Regios] The region " + ChatColor.BLUE + region + ChatColor.RED + " doesn't exist!");
			return;
		} else {
			if(!r.canModify(p)){
				p.sendMessage(ChatColor.RED + "[Regios] You are not permitted to modify this region!");
				return;
			}
			if(mutable.checkPermRemove(r, message)){
				p.sendMessage(ChatColor.RED + "[Regios] The Node " + ChatColor.BLUE + message + ChatColor.RED + " already exists!");
				return;
			}
			p.sendMessage(ChatColor.GREEN + "[Regios] Node " + ChatColor.BLUE + message + ChatColor.GREEN + " added to region cache " + ChatColor.BLUE + region);
		}
		mutable.editAddToPermRemoveCache(r, message);
	}

	public void removeFromPermRemCache(Region r, String region, String message, Player p) {
		if (r == null) {
			p.sendMessage(ChatColor.RED + "[Regios] The region " + ChatColor.BLUE + region + ChatColor.RED + " doesn't exist!");
			return;
		} else {
			if(!r.canModify(p)){
				p.sendMessage(ChatColor.RED + "[Regios] You are not permitted to modify this region!");
				return;
			}
			if(!mutable.checkPermRemove(r, message)){
				p.sendMessage(ChatColor.RED + "[Regios] The node " + ChatColor.BLUE + message + ChatColor.RED + " did not match any in the cache!");
				return;
			}
			p.sendMessage(ChatColor.GREEN + "[Regios] Node " + ChatColor.BLUE + message + ChatColor.GREEN + " removed from region cache " + ChatColor.BLUE + region);
		}
		mutable.editRemoveFromPermRemoveCache(r, message);
	}

	public void resetPermRemCache(Region r, String region, Player p) {
		if (r == null) {
			p.sendMessage(ChatColor.RED + "[Regios] The region " + ChatColor.BLUE + region + ChatColor.RED + " doesn't exist!");
			return;
		} else {
			if(!r.canModify(p)){
				p.sendMessage(ChatColor.RED + "[Regios] You are not permitted to modify this region!");
				return;
			}
			p.sendMessage(ChatColor.GREEN + "[Regios] Node cache reset for region " + ChatColor.BLUE + region);
		}
		mutable.editResetPermRemoveCache(r);
	}
	
	public void listTempAddCache(Region r, String region, Player p){
		if (r == null) {
			p.sendMessage(ChatColor.RED + "[Regios] The region " + ChatColor.BLUE + region + ChatColor.RED + " doesn't exist!");
			return;
		} else {
			p.sendMessage(ChatColor.GREEN + "[Regios] Temp Node cache for region " + ChatColor.BLUE + region);
			p.sendMessage(mutable.listTempAddCache(r));
		}
	}
	
	public void listTempRemCache(Region r, String region, Player p){
		if (r == null) {
			p.sendMessage(ChatColor.RED + "[Regios] The region " + ChatColor.BLUE + region + ChatColor.RED + " doesn't exist!");
			return;
		} else {
			p.sendMessage(ChatColor.GREEN + "[Regios] Temp Node cache for region " + ChatColor.BLUE + region);
			p.sendMessage(mutable.listTempRemCache(r));
		}
	}
	
	public void listPermAdd(Region r, String region, Player p){
		if (r == null) {
			p.sendMessage(ChatColor.RED + "[Regios] The region " + ChatColor.BLUE + region + ChatColor.RED + " doesn't exist!");
			return;
		} else {
			p.sendMessage(ChatColor.GREEN + "[Regios] Perm Node adds for region " + ChatColor.BLUE + region);
			p.sendMessage(mutable.listPermAddCache(r));
		}
	}
	
	public void listPermRemCache(Region r, String region, Player p){
		if (r == null) {
			p.sendMessage(ChatColor.RED + "[Regios] The region " + ChatColor.BLUE + region + ChatColor.RED + " doesn't exist!");
			return;
		} else {
			p.sendMessage(ChatColor.GREEN + "[Regios] Perm Node removals for region " + ChatColor.BLUE + region);
			p.sendMessage(mutable.listPermRemCache(r));
		}
	}

	public void addPermGroupAdd(Region r, String region, Player p, String group){
		if (r == null) {
			p.sendMessage(ChatColor.RED + "[Regios] The region " + ChatColor.BLUE + region + ChatColor.RED + " doesn't exist!");
		} else {
			p.sendMessage(ChatColor.GREEN + "[Regios] Group " + ChatColor.BLUE + group + ChatColor.GREEN + " was added to group add list for region " + ChatColor.BLUE + region);
			mutable.editAddPermGroupAdd(r, group);
		}
	}
	
	public void addPermGroupRemove(Region r, String region, Player p, String group){
		if (r == null) {
			p.sendMessage(ChatColor.RED + "[Regios] The region " + ChatColor.BLUE + region + ChatColor.RED + " doesn't exist!");
		} else {
			p.sendMessage(ChatColor.GREEN + "[Regios] Group " + ChatColor.BLUE + group + ChatColor.GREEN + " was added to group remove list for region " + ChatColor.BLUE + region);
			mutable.editAddPermGroupRemove(r, group);
		}
	}
	
	public void removePermGroupAdd(Region r, String region, Player p, String group){
		if (r == null) {
			p.sendMessage(ChatColor.RED + "[Regios] The region " + ChatColor.BLUE + region + ChatColor.RED + " doesn't exist!");
		} else {
			p.sendMessage(ChatColor.GREEN + "[Regios] Group " + ChatColor.BLUE + group + ChatColor.GREEN + " was removed to group add list for region " + ChatColor.BLUE + region);
			mutable.editRemovePermGroupAdd(r, group);
		}
	}
	
	public void removePermGroupRemove(Region r, String region, Player p, String group){
		if (r == null) {
			p.sendMessage(ChatColor.RED + "[Regios] The region " + ChatColor.BLUE + region + ChatColor.RED + " doesn't exist!");
		} else {
			p.sendMessage(ChatColor.GREEN + "[Regios] Group " + ChatColor.BLUE + group + ChatColor.GREEN + " was removed to group remove list for region " + ChatColor.BLUE + region);
			mutable.editRemovePermGroupRemove(r, group);
		}
	}
	
	public void addTempGroupAdd(Region r, String region, Player p, String group){
		if (r == null) {
			p.sendMessage(ChatColor.RED + "[Regios] The region " + ChatColor.BLUE + region + ChatColor.RED + " doesn't exist!");
		} else {
			p.sendMessage(ChatColor.GREEN + "[Regios] Group " + ChatColor.BLUE + group + ChatColor.GREEN + " added to group add list for region " + ChatColor.BLUE + region);
			mutable.editAddTempGroupAdd(r, group);
		}
	}
	
	public void removeTempGroupAdd(Region r, String region, Player p, String group){
		if (r == null) {
			p.sendMessage(ChatColor.RED + "[Regios] The region " + ChatColor.BLUE + region + ChatColor.RED + " doesn't exist!");
		} else {
			p.sendMessage(ChatColor.GREEN + "[Regios] Group " + ChatColor.BLUE + group + ChatColor.GREEN + " added to group remove list for region " + ChatColor.BLUE + region);
			mutable.editRemoveTempGroupAdd(r, group);
		}
	}
	
	public void addTempGroupRemove(Region r, String region, Player p, String group){
		if (r == null) {
			p.sendMessage(ChatColor.RED + "[Regios] The region " + ChatColor.BLUE + region + ChatColor.RED + " doesn't exist!");
		} else {
			p.sendMessage(ChatColor.GREEN + "[Regios] Group " + ChatColor.BLUE + group + ChatColor.GREEN + " added to group add list for region " + ChatColor.BLUE + region);
			mutable.editAddTempGroupRemove(r, group);
		}
	}
	
	public void removeTempGroupRemove(Region r, String region, Player p, String group){
		if (r == null) {
			p.sendMessage(ChatColor.RED + "[Regios] The region " + ChatColor.BLUE + region + ChatColor.RED + " doesn't exist!");
		} else {
			p.sendMessage(ChatColor.GREEN + "[Regios] Group " + ChatColor.BLUE + group + ChatColor.GREEN + " added to group remove list for region " + ChatColor.BLUE + region);
			mutable.editRemoveTempGroupRemove(r, group);
		}
	}
	
	public void listAddTempGroup(Region r, String region, Player p){
		if (r == null) {
			p.sendMessage(ChatColor.RED + "[Regios] The region " + ChatColor.BLUE + region + ChatColor.RED + " doesn't exist!");
			return;
		} else {
			p.sendMessage(ChatColor.GREEN + "[Regios] Temp group add list for region " + ChatColor.BLUE + region);
			p.sendMessage(mutable.listTempAddGroup(r));
		}
	}
	
	public void listRemoveTempGroup(Region r, String region, Player p){
		if (r == null) {
			p.sendMessage(ChatColor.RED + "[Regios] The region " + ChatColor.BLUE + region + ChatColor.RED + " doesn't exist!");
			return;
		} else {
			p.sendMessage(ChatColor.GREEN + "[Regios] Temp group remove list for region " + ChatColor.BLUE + region);
			p.sendMessage(mutable.listTempRemGroup(r));
		}
	}
	
	public void listAddPermGroup(Region r, String region, Player p){
		if (r == null) {
			p.sendMessage(ChatColor.RED + "[Regios] The region " + ChatColor.BLUE + region + ChatColor.RED + " doesn't exist!");
			return;
		} else {
			p.sendMessage(ChatColor.GREEN + "[Regios] Perm group add list for region " + ChatColor.BLUE + region);
			p.sendMessage(mutable.listPermAddGroup(r));
			for (String group : r.getPermAddGroups())
			{
				p.sendMessage(group);
			}
		}
	}
	
	public void listRemovePermGroup(Region r, String region, Player p){
		if (r == null) {
			p.sendMessage(ChatColor.RED + "[Regios] The region " + ChatColor.BLUE + region + ChatColor.RED + " doesn't exist!");
			return;
		} else {
			p.sendMessage(ChatColor.GREEN + "[Regios] Perm group removal list for region " + ChatColor.BLUE + region);
			p.sendMessage(mutable.listPermRemGroup(r));
			for (String group : r.getPermRemoveGroups())
			{
				p.sendMessage(group);
			}
		}
	}
}
