package net.jzx7.regios.Commands;

import net.jzx7.regios.Mutable.MutableProtectionMisc;
import net.jzx7.regios.Permissions.PermissionsCore;
import net.jzx7.regiosapi.entity.RegiosPlayer;
import net.jzx7.regiosapi.regions.Region;

public class ProtectionMiscCommands extends PermissionsCore {

	MutableProtectionMisc mutable = new MutableProtectionMisc();
	
	public void setPlayerCap(Region r, String region, String input, RegiosPlayer p) {
		int val;
		try {
			val = Integer.parseInt(input);
		} catch (Exception bfe) {
			p.sendMessage("<RED>" + "[Regios] The value for the 2nd paramteter must be an integer!");
			return;
		}
		if (r == null) {
			p.sendMessage("<RED>" + "[Regios] The region " + "<BLUE>" + region + "<RED>" + " doesn't exist!");
			return;
		} else {
			if(!r.canModify(p)){
				p.sendMessage("<RED>" + "[Regios] You are not permitted to modify this region!");
				return;
			}
			p.sendMessage("<DGREEN>" + "[Regios] Region " + "<BLUE>" + region + "<DGREEN>" + " player cap set to : " + "<BLUE>" + val);
		}
		mutable.editPlayerCap(r, val);
	}

	public void setInteraction(Region r, String region, String input, RegiosPlayer p) {
		boolean val;
		try {
			val = Boolean.parseBoolean(input);
		} catch (Exception bfe) {
			p.sendMessage("<RED>" + "[Regios] The value for the 2nd paramteter must be boolean!");
			return;
		}
		if (r == null) {
			p.sendMessage("<RED>" + "[Regios] The region " + "<BLUE>" + region + "<RED>" + " doesn't exist!");
			return;
		} else {
			if(!r.canModify(p)){
				p.sendMessage("<RED>" + "[Regios] You are not permitted to modify this region!");
				return;
			}
			if (val) {
				p.sendMessage("<DGREEN>" + "[Regios] Interaction Protection enabled for region " + "<BLUE>" + region);
			} else {
				p.sendMessage("<DGREEN>" + "[Regios] Interaction Protection disabled for region " + "<BLUE>" + region);
			}
		}
		mutable.editInteraction(r, val);
	}
	
	public void setBlockForm(Region r, String region, String input, RegiosPlayer p) {
		boolean val;
		try {
			val = Boolean.parseBoolean(input);
		} catch (Exception bfe) {
			p.sendMessage("<RED>" + "[Regios] The value for the 2nd paramteter must be boolean!");
			return;
		}
		if (r == null) {
			p.sendMessage("<RED>" + "[Regios] The region " + "<BLUE>" + region + "<RED>" + " doesn't exist!");
			return;
		} else {
			if(!r.canModify(p)){
				p.sendMessage("<RED>" + "[Regios] You are not permitted to modify this region!");
				return;
			}
			if (val) {
				p.sendMessage("<DGREEN>" + "[Regios] Block Forming enabled for region " + "<BLUE>" + region);
			} else {
				p.sendMessage("<DGREEN>" + "[Regios] Block Forming disabled for region " + "<BLUE>" + region);
			}
		}
		mutable.editBlockForm(r, val);
	}
	
	public void setEndermanBlock(Region r, String region, String input, RegiosPlayer p) {
		boolean val;
		try {
			val = Boolean.parseBoolean(input);
		} catch (Exception bfe) {
			p.sendMessage("<RED>" + "[Regios] The value for the 2nd paramteter must be boolean!");
			return;
		}
		if (r == null) {
			p.sendMessage("<RED>" + "[Regios] The region " + "<BLUE>" + region + "<RED>" + " doesn't exist!");
			return;
		} else {
			if(!r.canModify(p)){
				p.sendMessage("<RED>" + "[Regios] You are not permitted to modify this region!");
				return;
			}
			if (val) {
				p.sendMessage("<DGREEN>" + "[Regios] Enderman block protection enabled for region " + "<BLUE>" + region);
			} else {
				p.sendMessage("<DGREEN>" + "[Regios] Enderman block protection disabled for region " + "<BLUE>" + region);
			}
		}
		mutable.editEndermanBlock(r, val);
	}

	public void setChestsLocked(Region r, String region, String input, RegiosPlayer p) {
		boolean val;
		try {
			val = Boolean.parseBoolean(input);
		} catch (Exception bfe) {
			p.sendMessage("<RED>" + "[Regios] The value for the 2nd paramteter must be boolean!");
			return;
		}
		if (r == null) {
			p.sendMessage("<RED>" + "[Regios] The region " + "<BLUE>" + region + "<RED>" + " doesn't exist!");
			return;
		} else {
			if(!r.canModify(p)){
				p.sendMessage("<RED>" + "[Regios] You are not permitted to modify this region!");
				return;
			}
			if (val) {
				p.sendMessage("<DGREEN>" + "[Regios] Chest Locking enabled for region " + "<BLUE>" + region);
			} else {
				p.sendMessage("<DGREEN>" + "[Regios] Chest Locking disabled for region " + "<BLUE>" + region);
			}
		}
		mutable.editChestsLocked(r, val);
	}
	
	public void setDispensersLocked(Region r, String region, String input, RegiosPlayer p) {
		boolean val;
		try {
			val = Boolean.parseBoolean(input);
		} catch (Exception bfe) {
			p.sendMessage("<RED>" + "[Regios] The value for the 2nd paramteter must be boolean!");
			return;
		}
		if (r == null) {
			p.sendMessage("<RED>" + "[Regios] The region " + "<BLUE>" + region + "<RED>" + " doesn't exist!");
			return;
		} else {
			if(!r.canModify(p)){
				p.sendMessage("<RED>" + "[Regios] You are not permitted to modify this region!");
				return;
			}
			if (val) {
				p.sendMessage("<DGREEN>" + "[Regios] Dispenser Locking enabled for region " + "<BLUE>" + region);
			} else {
				p.sendMessage("<DGREEN>" + "[Regios] Dispenser Locking disabled for region " + "<BLUE>" + region);
			}
		}
		mutable.editDispensersLocked(r, val);
	}

	public void setDoorsLocked(Region r, String region, String input, RegiosPlayer p) {
		boolean val;
		try {
			val = Boolean.parseBoolean(input);
		} catch (Exception bfe) {
			p.sendMessage("<RED>" + "[Regios] The value for the 2nd paramteter must be boolean!");
			return;
		}
		if (r == null) {
			p.sendMessage("<RED>" + "[Regios] The region " + "<BLUE>" + region + "<RED>" + " doesn't exist!");
			return;
		} else {
			if(!r.canModify(p)){
				p.sendMessage("<RED>" + "[Regios] You are not permitted to modify this region!");
				return;
			}
			if (val) {
				p.sendMessage("<DGREEN>" + "[Regios] Door Locking enabled for region " + "<BLUE>" + region);
			} else {
				p.sendMessage("<DGREEN>" + "[Regios] Door Locking disabled for region " + "<BLUE>" + region);
			}
		}
		mutable.editDoorsLocked(r, val);
	}

	public void setPasswordEnabled(Region r, String region, String input, RegiosPlayer p) {
		boolean val;
		try {
			val = Boolean.parseBoolean(input);
		} catch (Exception bfe) {
			p.sendMessage("<RED>" + "[Regios] The value for the 2nd paramteter must be boolean!");
			return;
		}
		if (r == null) {
			p.sendMessage("<RED>" + "[Regios] The region " + "<BLUE>" + region + "<RED>" + " doesn't exist!");
			return;
		} else {
			if(!r.canModify(p)){
				p.sendMessage("<RED>" + "[Regios] You are not permitted to modify this region!");
				return;
			}
			if (val) {
				p.sendMessage("<DGREEN>" + "[Regios] Password Protection enabled for region " + "<BLUE>" + region);
				p.sendMessage("<BLUE>" + "[Regios] Be sure to enable prevent-entry or prevent-exit for this to have an effect.");
			} else {
				p.sendMessage("<DGREEN>" + "[Regios] Password Protection disabled for region " + "<BLUE>" + region);
				p.sendMessage("<BLUE>" + "[Regios] Be sure to enable prevent-entry or prevent-exit for this to have an effect.");
			}
		}
		mutable.editPasswordEnabled(r, val);
	}

	public void setFireProtection(Region r, String region, String input, RegiosPlayer p) {
		boolean val;
		try {
			val = Boolean.parseBoolean(input);
		} catch (Exception bfe) {
			p.sendMessage("<RED>" + "[Regios] The value for the 2nd paramteter must be boolean!");
			return;
		}
		if (r == null) {
			p.sendMessage("<RED>" + "[Regios] The region " + "<BLUE>" + region + "<RED>" + " doesn't exist!");
			return;
		} else {
			if(!r.canModify(p)){
				p.sendMessage("<RED>" + "[Regios] You are not permitted to modify this region!");
				return;
			}
			if (val) {
				p.sendMessage("<DGREEN>" + "[Regios] Fire Protection enabled for region " + "<BLUE>" + region);
			} else {
				p.sendMessage("<DGREEN>" + "[Regios] Fire Protection disabled for region " + "<BLUE>" + region);
			}
		}
		mutable.editFireProtection(r, val);
	}
	
	public void setFireSpread(Region r, String region, String input, RegiosPlayer p) {
		boolean val;
		try {
			val = Boolean.parseBoolean(input);
		} catch (Exception bfe) {
			p.sendMessage("<RED>" + "[Regios] The value for the 2nd paramteter must be boolean!");
			return;
		}
		if (r == null) {
			p.sendMessage("<RED>" + "[Regios] The region " + "<BLUE>" + region + "<RED>" + " doesn't exist!");
			return;
		} else {
			if(!r.canModify(p)){
				p.sendMessage("<RED>" + "[Regios] You are not permitted to modify this region!");
				return;
			}
			if (val) {
				p.sendMessage("<DGREEN>" + "[Regios] Fire Spread enabled for region " + "<BLUE>" + region);
			} else {
				p.sendMessage("<DGREEN>" + "[Regios] Fire Spread disabled for region " + "<BLUE>" + region);
			}
		}
		mutable.editFireSpread(r, val);
	}
	
	public void setTNTEnabled(Region r, String region, String input, RegiosPlayer p) {
		boolean val;
		try {
			val = Boolean.parseBoolean(input);
		} catch (Exception bfe) {
			p.sendMessage("<RED>" + "[Regios] The value for the 2nd paramteter must be boolean!");
			return;
		}
		if (r == null) {
			p.sendMessage("<RED>" + "[Regios] The region " + "<BLUE>" + region + "<RED>" + " doesn't exist!");
			return;
		} else {
			if(!r.canModify(p)){
				p.sendMessage("<RED>" + "[Regios] You are not permitted to modify this region!");
				return;
			}
			if (val) {
				p.sendMessage("<DGREEN>" + "[Regios] TNT enabled for region " + "<BLUE>" + region);
			} else {
				p.sendMessage("<DGREEN>" + "[Regios] TNT disabled for region " + "<BLUE>" + region);
			}
		}
		mutable.editTNTEnabled(r, val);
	}

	public void setPassword(Region r, String region, String input, RegiosPlayer p) {
		if (r == null) {
			p.sendMessage("<RED>" + "[Regios] The region " + "<BLUE>" + region + "<RED>" + " doesn't exist!");
			return;
		} else {
			if(!r.canModify(p)){
				p.sendMessage("<RED>" + "[Regios] You are not permitted to modify this region!");
				return;
			}
			p.sendMessage("<DGREEN>" + "[Regios] Password for region " + "<BLUE>" + region + "<DGREEN>" + " set to : " + "<BLUE>" + input);
		}
		mutable.editSetPassword(r, input);
	}

}
