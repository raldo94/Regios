package net.jzx7.regios.Permissions;

import net.jzx7.regiosapi.data.MODE;
import net.jzx7.regiosapi.regions.Region;

import org.bukkit.Material;
import org.bukkit.entity.Player;

public class PermChecks {
	
	protected boolean canModify(Player p, Region r) {
		if (PermissionsCore.doesHaveNode(p, ("regios.override." + r.getName())) || PermissionsCore.doesHaveNode(p, "regios.override.all")) {
			return true;
		}
		if (r.getOwner().equalsIgnoreCase(p.getName())) {
			return true;
		} else {
			if (r.getSubOwners() != null && r.getSubOwners().length > 0) {
				for (String subOwner : r.getSubOwners()) {
					if (subOwner.equalsIgnoreCase(p.getName())) {
						return true;
					}
				}
			}
			return false;
		}
	}

	protected boolean canItemBePlaced(Player p, Material m, Region r) {
		if (PermissionsCore.doesHaveNode(p, ("regios.bypass." + r.getName())) || PermissionsCore.doesHaveNode(p, "regios.bypass.all")) {
			return true;
		}
		if (PermissionsCore.doesHaveNode(p, "regios.exceptions.itemplace." + r.getName()) || PermissionsCore.doesHaveNode(p, "regios.exceptions.itemplace.all"))
		{
			return true;
		}
		if (canModify(p, r)) {
			return true;
		} else {
			for (String excep : r.getExceptionNodes()) {
				if (r.getItemMode() == MODE.Whitelist) {
					if (PermissionsCore.doesHaveNode(p, excep)) {
						return true;
					}
				} else if (r.getItemMode() == MODE.Blacklist) {
					if (PermissionsCore.doesHaveNode(p, excep)) {
						return false;
					}
				}
			}
			for (String excep : r.getExceptions()) {
				if (r.getItemMode() == MODE.Whitelist) {
					if (excep.equalsIgnoreCase(p.getName())) {
						return true;
					}
				} else if (r.getItemMode() == MODE.Blacklist) {
					if (excep.equalsIgnoreCase(p.getName())) {
						return false;
					}
				}
			}
			if (r.getItemMode() == MODE.Whitelist) {
				return r.getItems().contains(m.getId());
			} else if (r.getItemMode() == MODE.Blacklist) {
				return !r.getItems().contains(m.getId());
			}
			return false;
		}
	}

	protected boolean canBypassProtection(Player p, Region r) {
		if (PermissionsCore.doesHaveNode(p, ("regios.bypass." + r.getName())) || PermissionsCore.doesHaveNode(p, "regios.bypass.all")) {
			return true;
		}
		if (canModify(p, r)) {
			return true;
		} else {
			for (String excep : r.getExceptionNodes()) {
				if (r.getProtectionMode() == MODE.Whitelist) {
					if (PermissionsCore.doesHaveNode(p, excep)) {
						return true;
					}
				} else if (r.getProtectionMode() == MODE.Blacklist) {
					if (PermissionsCore.doesHaveNode(p, excep)) {
						return false;
					}
				}
			}
			for (String excep : r.getExceptions()) {
				if (r.getProtectionMode() == MODE.Whitelist) {
					if (excep.equalsIgnoreCase(p.getName())) {
						return true;
					}
				} else if (r.getProtectionMode() == MODE.Blacklist) {
					if (excep.equalsIgnoreCase(p.getName())) {
						return false;
					}
				}
			}
			if (r.getProtectionMode() == MODE.Whitelist) {
				return false;
			} else if (r.getProtectionMode() == MODE.Blacklist) {
				return true;
			}
			return false;
		}
	}

	protected boolean canEnter(Player p, Region r) {
		if (PermissionsCore.doesHaveNode(p, ("regios.bypass." + r.getName())) || PermissionsCore.doesHaveNode(p, "regios.bypass.all")) {
			return true;
		}
		if (PermissionsCore.doesHaveNode(p, "regios.exceptions.enter." + r.getName()) || PermissionsCore.doesHaveNode(p, "regios.exceptions.enter.all"))
		{
			return true;
		}
		if (canModify(p, r)) {
			return true;
		} else {
			for (String excep : r.getExceptionNodes()) {
				if (r.getPreventEntryMode() == MODE.Whitelist) {
					if (PermissionsCore.doesHaveNode(p, excep)) {
						return true;
					}
				} else if (r.getPreventEntryMode() == MODE.Blacklist) {
					if (PermissionsCore.doesHaveNode(p, excep)) {
						return false;
					}
				}
			}
			for (String excep : r.getExceptions()) {
				if (r.getPreventEntryMode() == MODE.Whitelist) {
					if (excep.equalsIgnoreCase(p.getName())) {
						return true;
					}
				} else if (r.getPreventEntryMode() == MODE.Blacklist) {
					if (excep.equalsIgnoreCase(p.getName())) {
						return false;
					}
				}
			}
			if (r.getPreventEntryMode() == MODE.Whitelist) {
				return false;
			} else if (r.getPreventEntryMode() == MODE.Blacklist) {
				return true;
			}
			return false;
		}
	}

	protected boolean canExit(Player p, Region r) {
		if (PermissionsCore.doesHaveNode(p, ("regios.bypass." + r.getName())) || PermissionsCore.doesHaveNode(p, "regios.bypass.all")) {
			return true;
		}
		if (PermissionsCore.doesHaveNode(p, "regios.exceptions.exit." + r.getName()) || PermissionsCore.doesHaveNode(p, "regios.exceptions.exit.all"))
		{
			return true;
		}
		if (canModify(p, r)) {
			return true;
		} else {
			for (String excep : r.getExceptionNodes()) {
				if (r.getPreventExitMode() == MODE.Whitelist) {
					if (PermissionsCore.doesHaveNode(p, excep)) {
						return true;
					}
				} else if (r.getPreventExitMode() == MODE.Blacklist) {
					if (PermissionsCore.doesHaveNode(p, excep)) {
						return false;
					}
				}
			}
			for (String excep : r.getExceptions()) {
				if (r.getPreventExitMode() == MODE.Whitelist) {
					if (excep.equalsIgnoreCase(p.getName())) {
						return true;
					}
				} else if (r.getPreventExitMode() == MODE.Blacklist) {
					if (excep.equalsIgnoreCase(p.getName())) {
						return false;
					}
				}
			}
			if (r.getPreventExitMode() == MODE.Whitelist) {
				return false;
			} else if (r.getPreventExitMode() == MODE.Blacklist) {
				return true;
			}
			return false;
		}
	}
}
