package net.jzx7.regios.Commands;

import net.jzx7.regios.Mutable.MutableExceptions;
import net.jzx7.regios.Permissions.PermissionsCore;
import net.jzx7.regios.regions.RegionManager;
import net.jzx7.regiosapi.entity.RegiosPlayer;
import net.jzx7.regiosapi.regions.Region;

import org.bukkit.Material;


public class ExceptionCommands extends PermissionsCore {

	MutableExceptions mutable = new MutableExceptions();
	RegionManager rm = new RegionManager();

	public void playerex(RegiosPlayer p, String[] args) {
		if (doesHaveNode(p, "regios.exceptions.players")) {
			if (args.length > 1) {
				if (args[1].equalsIgnoreCase("add")) {
					if (args.length == 4) {
						addPlayerException(rm.getRegion(args[2]), args[2], args[3], p);
						return;
					} else {
						p.sendMessage("<RED>" + "[Regios] Invalid number of arguments specified.");
						p.sendMessage("Proper usage: /regios playerex add <regios> <player>");
						return;
					}
				} else if (args[1].equalsIgnoreCase("rem")) {
					if (args.length == 4) {
						removePlayerException(rm.getRegion(args[2]), args[2], args[3], p);
						return;
					} else {
						p.sendMessage("<RED>" + "[Regios] Invalid number of arguments specified.");
						p.sendMessage("Proper usage: /regios playerex rem <region> <player>");
						return;
					}
				} else if (args[1].equalsIgnoreCase("list")) {
					if (args.length == 3) {
						listPlayerExceptions(rm.getRegion(args[2]), args[2], p);
						return;
					} else {
						p.sendMessage("<RED>" + "[Regios] Invalid number of arguments specified.");
						p.sendMessage("Proper usage: /regios playerex list <region>");
						return;
					}
				} else if (args[1].equalsIgnoreCase("reset")) {
					if (args.length == 3) {
						erasePlayerExceptions(rm.getRegion(args[2]), args[2], p);
						return;
					} else {
						p.sendMessage("<RED>" + "[Regios] Invalid number of arguments specified.");
						p.sendMessage("Proper usage: /regios playerex reset <region>");
						return;
					}
				} else {
					p.sendMessage("<RED>" + "[Regios] Invalid argument specified.");
					p.sendMessage("Proper usage: /regios playerex add/rem/list/reset");
					return;
				}
			} else {
				p.sendMessage("<RED>" + "[Regios] Invalid number of arguments specified.");
				p.sendMessage("Proper usage: /regios playerex add/rem/list/reset");
				return;
			}
		} else {
			sendInvalidPerms(p);
			return;
		}
	}
	
	public void itemex(RegiosPlayer p, String[] args) {
		if (doesHaveNode(p, "regios.exceptions.items")) {
			if (args.length > 1) {
				if (args[1].equalsIgnoreCase("add")) {
					if (args.length == 4) {
						addItemException(rm.getRegion(args[2]), args[2], args[3], p);
						return;
					} else {
						p.sendMessage("<RED>" + "[Regios] Invalid number of arguments specified.");
						p.sendMessage("Proper usage: /regios itemex add <regios> <item>");
						return;
					}
				} else if (args[1].equalsIgnoreCase("rem")) {
					if (args.length == 4) {
						removeItemException(rm.getRegion(args[2]), args[2], args[3], p);
						return;
					} else {
						p.sendMessage("<RED>" + "[Regios] Invalid number of arguments specified.");
						p.sendMessage("Proper usage: /regios itemex rem <region> <item>");
						return;
					}
				} else if (args[1].equalsIgnoreCase("list")) {
					if (args.length == 3) {
						listItemExceptions(rm.getRegion(args[2]), args[2], p);
						return;
					} else {
						p.sendMessage("<RED>" + "[Regios] Invalid number of arguments specified.");
						p.sendMessage("Proper usage: /regios itemex list <region>");
						return;
					}
				} else if (args[1].equalsIgnoreCase("reset")) {
					if (args.length == 3) {
						eraseItemExceptions(rm.getRegion(args[2]), args[2], p);
						return;
					} else {
						p.sendMessage("<RED>" + "[Regios] Invalid number of arguments specified.");
						p.sendMessage("Proper usage: /regios itemex reset <region>");
						return;
					}
				} else {
					p.sendMessage("<RED>" + "[Regios] Invalid argument specified.");
					p.sendMessage("Proper usage: /regios itemex add/rem/list/reset");
					return;
				}
			} else {
				p.sendMessage("<RED>" + "[Regios] Invalid number of arguments specified.");
				p.sendMessage("Proper usage: /regios itemex add/rem/list/reset");
				return;
			}
		} else {
			sendInvalidPerms(p);
			return;
		}
	}
	
	public void nodeex(RegiosPlayer p, String[] args) {
		if (doesHaveNode(p, "regios.exceptions.nodes")) {
			if (args.length > 1) {
				if (args[1].equalsIgnoreCase("add")) {
					if (args.length == 4) {
						addNodeException(rm.getRegion(args[2]), args[2], args[3], p);
						return;
					} else {
						p.sendMessage("<RED>" + "[Regios] Invalid number of arguments specified.");
						p.sendMessage("Proper usage: /regios nodeex add <regios> <item>");
						return;
					}
				} else if (args[1].equalsIgnoreCase("rem")) {
					if (args.length == 4) {
						removeNodeException(rm.getRegion(args[2]), args[2], args[3], p);
						return;
					} else {
						p.sendMessage("<RED>" + "[Regios] Invalid number of arguments specified.");
						p.sendMessage("Proper usage: /regios nodeex rem <region> <item>");
						return;
					}
				} else if (args[1].equalsIgnoreCase("list")) {
					if (args.length == 3) {
						listNodeExceptions(rm.getRegion(args[2]), args[2], p);
						return;
					} else {
						p.sendMessage("<RED>" + "[Regios] Invalid number of arguments specified.");
						p.sendMessage("Proper usage: /regios nodeex list <region>");
						return;
					}
				} else if (args[1].equalsIgnoreCase("reset")) {
					if (args.length == 3) {
						eraseNodeExceptions(rm.getRegion(args[2]), args[2], p);
						return;
					} else {
						p.sendMessage("<RED>" + "[Regios] Invalid number of arguments specified.");
						p.sendMessage("Proper usage: /regios nodeex reset <region>");
						return;
					}
				} else {
					p.sendMessage("<RED>" + "[Regios] Invalid argument specified.");
					p.sendMessage("Proper usage: /regios nodeex add/rem/list/reset");
					return;
				}
			} else {
				p.sendMessage("<RED>" + "[Regios] Invalid number of arguments specified.");
				p.sendMessage("Proper usage: /regios nodeex add/rem/list/reset");
				return;
			}
		} else {
			sendInvalidPerms(p);
			return;
		}
	}
	
	public void subowner(RegiosPlayer p, String[] args) {
		if (doesHaveNode(p, "regios.modify.players")) {
			if (args.length > 1) {
				if (args[1].equalsIgnoreCase("add")) {
					if (args.length == 4) {
						addSubOwner(rm.getRegion(args[2]), args[2], args[3], p);
						return;
					} else {
						p.sendMessage("<RED>" + "[Regios] Invalid number of arguments specified.");
						p.sendMessage("Proper usage: /regios subowner add <regios> <item>");
						return;
					}
				} else if (args[1].equalsIgnoreCase("rem")) {
					if (args.length == 4) {
						removeSubowner(rm.getRegion(args[2]), args[2], args[3], p);
						return;
					} else {
						p.sendMessage("<RED>" + "[Regios] Invalid number of arguments specified.");
						p.sendMessage("Proper usage: /regios subowner rem <region> <item>");
						return;
					}
				} else if (args[1].equalsIgnoreCase("list")) {
					if (args.length == 3) {
						listSubOwnerExceptions(rm.getRegion(args[2]), args[2], p);
						return;
					} else {
						p.sendMessage("<RED>" + "[Regios] Invalid number of arguments specified.");
						p.sendMessage("Proper usage: /regios subowner list <region>");
						return;
					}
				} else if (args[1].equalsIgnoreCase("reset")) {
					if (args.length == 3) {
						eraseSubOwnerExceptions(rm.getRegion(args[2]), args[2], p);
						return;
					} else {
						p.sendMessage("<RED>" + "[Regios] Invalid number of arguments specified.");
						p.sendMessage("Proper usage: /regios subowner reset <region>");
						return;
					}
				} else {
					p.sendMessage("<RED>" + "[Regios] Invalid argument specified.");
					p.sendMessage("Proper usage: /regios subowner add/rem/list/reset");
					return;
				}
			} else {
				p.sendMessage("<RED>" + "[Regios] Invalid number of arguments specified.");
				p.sendMessage("Proper usage: /regios subowner add/rem/list/reset");
				return;
			}
		} else {
			sendInvalidPerms(p);
			return;
		}
	}
	
	private boolean checkException(int val) {
		if (Material.getMaterial(val) != null) {
			return true;
		} else {
			return false;
		}
	}

	private void addPlayerException(Region r, String region, String ex, RegiosPlayer p) {
		if (r == null) {
			p.sendMessage("<RED>" + "[Regios] The region " + "<BLUE>" + region + "<RED>" + " doesn't exist!");
			return;
		} else {
			if (!r.canModify(p)) {
				p.sendMessage("<RED>" + "[Regios] You are not permitted to modify this region!");
				return;
			}
			if (!mutable.checkPlayerException(r, ex)) {
				p.sendMessage("<DGREEN>" + "[Regios] Exception added : " + "<BLUE>" + ex);
				mutable.addPlayerException(r, ex);
			} else {
				p.sendMessage("<RED>" + "[Regios] Player " + "<BLUE>" + ex + "<RED>" + " is already an exception!");
			}
		}
	}

	private void removePlayerException(Region r, String region, String ex, RegiosPlayer p) {
		if (r == null) {
			p.sendMessage("<RED>" + "[Regios] The region " + "<BLUE>" + region + "<RED>" + " doesn't exist!");
			return;
		} else {
			if (!r.canModify(p)) {
				p.sendMessage("<RED>" + "[Regios] You are not permitted to modify this region!");
				return;
			}
			if (mutable.checkPlayerException(r, ex)) {
				p.sendMessage("<DGREEN>" + "[Regios] Exception removed : " + "<BLUE>" + ex);
				mutable.removePlayerException(r, ex);
			} else {
				p.sendMessage("<RED>" + "[Regios] Player " + "<BLUE>" + ex + "<RED>" + " is not an exception!");
			}
		}
	}

	private void addItemException(Region r, String region, String ex, RegiosPlayer p) {
		int val = 0;
		try {
			val = Integer.parseInt(ex);
		} catch (NumberFormatException nfe) {
			p.sendMessage("<RED>" + "[Regios] The item must be an integer!");
			return;
		}
		if (r == null) {
			p.sendMessage("<RED>" + "[Regios] The region " + "<BLUE>" + region + "<RED>" + " doesn't exist!");
			return;
		} else {
			if(!checkException(val)){
				p.sendMessage("<RED>" + "[Regios] The item id " + "<BLUE>" + val + "<RED>" + " is invalid!");
				return;
			}
			if (!r.canModify(p)) {
				p.sendMessage("<RED>" + "[Regios] You are not permitted to modify this region!");
				return;
			}
			if (!mutable.checkItemException(r, val)) {
				p.sendMessage("<DGREEN>" + "[Regios] Item Exception added : " + "<BLUE>" + ex);
				mutable.addItemException(r, val);
			} else {
				p.sendMessage("<RED>" + "[Regios] Item " + "<BLUE>" + ex + "<RED>" + " is already an exception!");
			}
		}
	}

	private void removeItemException(Region r, String region, String ex, RegiosPlayer p) {
		int val = 0;
		try {
			val = Integer.parseInt(ex);
		} catch (NumberFormatException nfe) {
			p.sendMessage("<RED>" + "[Regios] The item must be an integer!");
			return;
		}
		if (r == null) {
			p.sendMessage("<RED>" + "[Regios] The region " + "<BLUE>" + region + "<RED>" + " doesn't exist!");
			return;
		} else {
			if(!checkException(val)){
				p.sendMessage("<RED>" + "[Regios] The item id " + "<BLUE>" + val + "<RED>" + " is invalid!");
				return;
			}
			if (!r.canModify(p)) {
				p.sendMessage("<RED>" + "[Regios] You are not permitted to modify this region!");
				return;
			}
			if (mutable.checkItemException(r, val)) {
				p.sendMessage("<DGREEN>" + "[Regios] Item Exception removed : " + "<BLUE>" + ex);
				mutable.removeItemException(r, val);
			} else {
				p.sendMessage("<RED>" + "[Regios] Item " + "<BLUE>" + ex + "<RED>" + " is not an exception!");
			}
		}
	}

	private void addNodeException(Region r, String region, String ex, RegiosPlayer p) {
		if (r == null) {
			p.sendMessage("<RED>" + "[Regios] The region " + "<BLUE>" + region + "<RED>" + " doesn't exist!");
			return;
		} else {
			if (!r.canModify(p)) {
				p.sendMessage("<RED>" + "[Regios] You are not permitted to modify this region!");
				return;
			}
			if (!mutable.checkNodeException(r, ex)) {
				p.sendMessage("<DGREEN>" + "[Regios] Exception Node added : " + "<BLUE>" + ex);
				mutable.addNodeException(r, ex);
			} else {
				p.sendMessage("<RED>" + "[Regios] Node " + "<BLUE>" + ex + "<RED>" + " is already an exception!");
			}
		}
	}

	private void removeNodeException(Region r, String region, String ex, RegiosPlayer p) {
		if (r == null) {
			p.sendMessage("<RED>" + "[Regios] The region " + "<BLUE>" + region + "<RED>" + " doesn't exist!");
			return;
		} else {
			if (!r.canModify(p)) {
				p.sendMessage("<RED>" + "[Regios] You are not permitted to modify this region!");
				return;
			}
			if (mutable.checkNodeException(r, ex)) {
				p.sendMessage("<DGREEN>" + "[Regios] Exception Node added : " + "<BLUE>" + ex);
				mutable.removeNodeException(r, ex);
			} else {
				p.sendMessage("<RED>" + "[Regios] Node " + "<BLUE>" + ex + "<RED>" + " is already an exception!");
			}
		}
	}

	private void erasePlayerExceptions(Region r, String region, RegiosPlayer p) {
		if (r == null) {
			p.sendMessage("<RED>" + "[Regios] The region " + "<BLUE>" + region + "<RED>" + " doesn't exist!");
			return;
		} else {
			if (!r.canModify(p)) {
				p.sendMessage("<RED>" + "[Regios] You are not permitted to modify this region!");
				return;
			}
			p.sendMessage("<DGREEN>" + "[Regios] All player exceptions removed for region : " + "<BLUE>" + region);
			mutable.eraseAllPlayerExceptions(r);
			return;
		}
	}

	private void eraseSubOwnerExceptions(Region r, String region, RegiosPlayer p) {
		if (r == null) {
			p.sendMessage("<RED>" + "[Regios] The region " + "<BLUE>" + region + "<RED>" + " doesn't exist!");
			return;
		} else {
			if (!r.canModify(p)) {
				p.sendMessage("<RED>" + "[Regios] You are not permitted to modify this region!");
				return;
			}
			p.sendMessage("<DGREEN>" + "[Regios] All Sub Owners removed for region : " + "<BLUE>" + region);
			mutable.eraseAllSubOwners(r);
			return;
		}
	}

	private void eraseItemExceptions(Region r, String region, RegiosPlayer p) {
		if (r == null) {
			p.sendMessage("<RED>" + "[Regios] The region " + "<BLUE>" + region + "<RED>" + " doesn't exist!");
			return;
		} else {
			if (!r.canModify(p)) {
				p.sendMessage("<RED>" + "[Regios] You are not permitted to modify this region!");
				return;
			}
			p.sendMessage("<DGREEN>" + "[Regios] All item exceptions removed for region : " + "<BLUE>" + region);
			mutable.eraseAllItemExceptions(r);
			return;
		}
	}

	private void eraseNodeExceptions(Region r, String region, RegiosPlayer p) {
		if (r == null) {
			p.sendMessage("<RED>" + "[Regios] The region " + "<BLUE>" + region + "<RED>" + " doesn't exist!");
			return;
		} else {
			if (!r.canModify(p)) {
				p.sendMessage("<RED>" + "[Regios] You are not permitted to modify this region!");
				return;
			}
			p.sendMessage("<DGREEN>" + "[Regios] All node exceptions removed for region : " + "<BLUE>" + region);
			mutable.eraseAllNodeExceptions(r);
			return;
		}
	}

	private void listItemExceptions(Region r, String region, RegiosPlayer p) {
		if (r == null) {
			p.sendMessage("<RED>" + "[Regios] The region " + "<BLUE>" + region + "<RED>" + " doesn't exist!");
			return;
		} else {
			String regionSet = mutable.listItemExceptions(r);
			p.sendMessage("<DGREEN>" + "Regios Item Exception List : " + "<BLUE>" + region);
			p.sendMessage(regionSet);
		}
	}

	private void listPlayerExceptions(Region r, String region, RegiosPlayer p) {
		if (r == null) {
			p.sendMessage("<RED>" + "[Regios] The region " + "<BLUE>" + region + "<RED>" + " doesn't exist!");
			return;
		} else {
			String regionSet = mutable.listPlayerExceptions(r);
			p.sendMessage("<DGREEN>" + "Regios Player Exception List : " + "<BLUE>" + region);
			p.sendMessage(regionSet);
		}
	}

	private void listSubOwnerExceptions(Region r, String region, RegiosPlayer p) {
		if (r == null) {
			p.sendMessage("<RED>" + "[Regios] The region " + "<BLUE>" + region + "<RED>" + " doesn't exist!");
			return;
		} else {
			String regionSet = mutable.listSubOwnersExceptions(r);
			p.sendMessage("<DGREEN>" + "Regios Sub Owner Exception List : " + "<BLUE>" + region);
			p.sendMessage(regionSet);
		}
	}

	private void listNodeExceptions(Region r, String region, RegiosPlayer p) {
		if (r == null) {
			p.sendMessage("<RED>" + "[Regios] The region " + "<BLUE>" + region + "<RED>" + " doesn't exist!");
			return;
		} else {
			String regionSet = mutable.listNodeExceptions(r);
			p.sendMessage("<DGREEN>" + "Regios Node Exception List : " + "<BLUE>" + region);
			p.sendMessage(regionSet);
		}
	}

	private void addSubOwner(Region r, String region, String message, RegiosPlayer p) {
		if (r == null) {
			p.sendMessage("<RED>" + "[Regios] The region " + "<BLUE>" + region + "<RED>" + " doesn't exist!");
			return;
		} else {
			if (!r.canModify(p)) {
				p.sendMessage("<RED>" + "[Regios] You are not permitted to modify this region!");
				return;
			}
			
			if (mutable.checkSubOwnerException(r, message)) {
				p.sendMessage("<RED>" + "[Regios] The Sub Owner " + "<BLUE>" + message + "<RED>" + " already exists!");
				return;
			}
			p.sendMessage("<DGREEN>" + "[Regios] Sub Owner " + "<BLUE>" + message + "<DGREEN>" + " added to region " + "<BLUE>" + region);
		}
		mutable.addSubOwner(r, message);
	}

	private void removeSubowner(Region r, String region, String ex, RegiosPlayer p) {
		if (r == null) {
			p.sendMessage("<RED>" + "[Regios] The region " + "<BLUE>" + region + "<RED>" + " doesn't exist!");
			return;
		} else {
			if (!r.canModify(p)) {
				p.sendMessage("<RED>" + "[Regios] You are not permitted to modify this region!");
				return;
			}
			if (mutable.checkSubOwnerException(r, ex)) {
				p.sendMessage("<DGREEN>" + "[Regios] Sub Owner removed : " + "<BLUE>" + ex);
				mutable.removeSubOwner(r, ex);
			} else {
				p.sendMessage("<RED>" + "[Regios] Sub Owner " + "<BLUE>" + ex + "<RED>" + " does not exist!");
			}
		}
	}

	public void removeFromPermRemCache(Region r, String region, String message, RegiosPlayer p) {
		if (r == null) {
			p.sendMessage("<RED>" + "[Regios] The region " + "<BLUE>" + region + "<RED>" + " doesn't exist!");
			return;
		} else {
			if (!r.canModify(p)) {
				p.sendMessage("<RED>" + "[Regios] You are not permitted to modify this region!");
				return;
			}
			boolean nodeMatch = false;
			for (String s : r.getSubOwners()) {
				if (s.trim().equalsIgnoreCase(message.trim())) {
					nodeMatch = true;
				}
			}
			if (!nodeMatch) {
				p.sendMessage("<RED>" + "[Regios] The Sub Owner " + "<BLUE>" + message + "<RED>" + " did not match any in the cache!");
				return;
			}
			p.sendMessage("<DGREEN>" + "[Regios] Sub Owner " + "<BLUE>" + message + "<DGREEN>" + " removed from region cache " + "<BLUE>" + region);
		}
		mutable.removeSubOwner(r, message);
	}

	public void resetPermRemCache(Region r, String region, RegiosPlayer p) {
		if (r == null) {
			p.sendMessage("<RED>" + "[Regios] The region " + "<BLUE>" + region + "<RED>" + " doesn't exist!");
			return;
		} else {
			if (!r.canModify(p)) {
				p.sendMessage("<RED>" + "[Regios] You are not permitted to modify this region!");
				return;
			}
			p.sendMessage("<DGREEN>" + "[Regios] Sub Owners reset for region " + "<BLUE>" + region);
		}
		mutable.eraseAllSubOwners(r);
	}

}
