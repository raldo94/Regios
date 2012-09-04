package net.jzx7.regios.Commands;

import net.jzx7.regios.Mutable.MutableExceptions;
import net.jzx7.regios.Permissions.PermissionsCore;
import net.jzx7.regios.regions.RegionManager;
import net.jzx7.regiosapi.regions.Region;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;


public class ExceptionCommands extends PermissionsCore {

	MutableExceptions mutable = new MutableExceptions();
	RegionManager rm = new RegionManager();

	public void playerex(Player p, String[] args) {
		if (doesHaveNode(p, "regios.exceptions.players")) {
			if (args.length > 1) {
				if (args[1].equalsIgnoreCase("add")) {
					if (args.length == 4) {
						addPlayerException(rm.getRegion(args[2]), args[2], args[3], p);
						return;
					} else {
						p.sendMessage(ChatColor.RED + "[Regios] Invalid number of arguments specified.");
						p.sendMessage("Proper usage: /regios playerex add <regios> <player>");
						return;
					}
				} else if (args[1].equalsIgnoreCase("rem")) {
					if (args.length == 4) {
						removePlayerException(rm.getRegion(args[2]), args[2], args[3], p);
						return;
					} else {
						p.sendMessage(ChatColor.RED + "[Regios] Invalid number of arguments specified.");
						p.sendMessage("Proper usage: /regios playerex rem <region> <player>");
						return;
					}
				} else if (args[1].equalsIgnoreCase("list")) {
					if (args.length == 3) {
						listPlayerExceptions(rm.getRegion(args[2]), args[2], p);
						return;
					} else {
						p.sendMessage(ChatColor.RED + "[Regios] Invalid number of arguments specified.");
						p.sendMessage("Proper usage: /regios playerex list <region>");
						return;
					}
				} else if (args[1].equalsIgnoreCase("reset")) {
					if (args.length == 3) {
						erasePlayerExceptions(rm.getRegion(args[2]), args[2], p);
						return;
					} else {
						p.sendMessage(ChatColor.RED + "[Regios] Invalid number of arguments specified.");
						p.sendMessage("Proper usage: /regios playerex reset <region>");
						return;
					}
				} else {
					p.sendMessage(ChatColor.RED + "[Regios] Invalid argument specified.");
					p.sendMessage("Proper usage: /regios playerex add/rem/list/reset");
					return;
				}
			} else {
				p.sendMessage(ChatColor.RED + "[Regios] Invalid number of arguments specified.");
				p.sendMessage("Proper usage: /regios playerex add/rem/list/reset");
				return;
			}
		} else {
			sendInvalidPerms(p);
			return;
		}
	}
	
	public void itemex(Player p, String[] args) {
		if (doesHaveNode(p, "regios.exceptions.items")) {
			if (args.length > 1) {
				if (args[1].equalsIgnoreCase("add")) {
					if (args.length == 4) {
						addItemException(rm.getRegion(args[2]), args[2], args[3], p);
						return;
					} else {
						p.sendMessage(ChatColor.RED + "[Regios] Invalid number of arguments specified.");
						p.sendMessage("Proper usage: /regios itemex add <regios> <item>");
						return;
					}
				} else if (args[1].equalsIgnoreCase("rem")) {
					if (args.length == 4) {
						removeItemException(rm.getRegion(args[2]), args[2], args[3], p);
						return;
					} else {
						p.sendMessage(ChatColor.RED + "[Regios] Invalid number of arguments specified.");
						p.sendMessage("Proper usage: /regios itemex rem <region> <item>");
						return;
					}
				} else if (args[1].equalsIgnoreCase("list")) {
					if (args.length == 3) {
						listItemExceptions(rm.getRegion(args[2]), args[2], p);
						return;
					} else {
						p.sendMessage(ChatColor.RED + "[Regios] Invalid number of arguments specified.");
						p.sendMessage("Proper usage: /regios itemex list <region>");
						return;
					}
				} else if (args[1].equalsIgnoreCase("reset")) {
					if (args.length == 3) {
						eraseItemExceptions(rm.getRegion(args[2]), args[2], p);
						return;
					} else {
						p.sendMessage(ChatColor.RED + "[Regios] Invalid number of arguments specified.");
						p.sendMessage("Proper usage: /regios itemex reset <region>");
						return;
					}
				} else {
					p.sendMessage(ChatColor.RED + "[Regios] Invalid argument specified.");
					p.sendMessage("Proper usage: /regios itemex add/rem/list/reset");
					return;
				}
			} else {
				p.sendMessage(ChatColor.RED + "[Regios] Invalid number of arguments specified.");
				p.sendMessage("Proper usage: /regios itemex add/rem/list/reset");
				return;
			}
		} else {
			sendInvalidPerms(p);
			return;
		}
	}
	
	public void nodeex(Player p, String[] args) {
		if (doesHaveNode(p, "regios.exceptions.nodes")) {
			if (args.length > 1) {
				if (args[1].equalsIgnoreCase("add")) {
					if (args.length == 4) {
						addNodeException(rm.getRegion(args[2]), args[2], args[3], p);
						return;
					} else {
						p.sendMessage(ChatColor.RED + "[Regios] Invalid number of arguments specified.");
						p.sendMessage("Proper usage: /regios nodeex add <regios> <item>");
						return;
					}
				} else if (args[1].equalsIgnoreCase("rem")) {
					if (args.length == 4) {
						removeNodeException(rm.getRegion(args[2]), args[2], args[3], p);
						return;
					} else {
						p.sendMessage(ChatColor.RED + "[Regios] Invalid number of arguments specified.");
						p.sendMessage("Proper usage: /regios nodeex rem <region> <item>");
						return;
					}
				} else if (args[1].equalsIgnoreCase("list")) {
					if (args.length == 3) {
						listNodeExceptions(rm.getRegion(args[2]), args[2], p);
						return;
					} else {
						p.sendMessage(ChatColor.RED + "[Regios] Invalid number of arguments specified.");
						p.sendMessage("Proper usage: /regios nodeex list <region>");
						return;
					}
				} else if (args[1].equalsIgnoreCase("reset")) {
					if (args.length == 3) {
						eraseNodeExceptions(rm.getRegion(args[2]), args[2], p);
						return;
					} else {
						p.sendMessage(ChatColor.RED + "[Regios] Invalid number of arguments specified.");
						p.sendMessage("Proper usage: /regios nodeex reset <region>");
						return;
					}
				} else {
					p.sendMessage(ChatColor.RED + "[Regios] Invalid argument specified.");
					p.sendMessage("Proper usage: /regios nodeex add/rem/list/reset");
					return;
				}
			} else {
				p.sendMessage(ChatColor.RED + "[Regios] Invalid number of arguments specified.");
				p.sendMessage("Proper usage: /regios nodeex add/rem/list/reset");
				return;
			}
		} else {
			sendInvalidPerms(p);
			return;
		}
	}
	
	public void subowner(Player p, String[] args) {
		if (doesHaveNode(p, "regios.modify.players")) {
			if (args.length > 1) {
				if (args[1].equalsIgnoreCase("add")) {
					if (args.length == 4) {
						addSubOwner(rm.getRegion(args[2]), args[2], args[3], p);
						return;
					} else {
						p.sendMessage(ChatColor.RED + "[Regios] Invalid number of arguments specified.");
						p.sendMessage("Proper usage: /regios subowner add <regios> <item>");
						return;
					}
				} else if (args[1].equalsIgnoreCase("rem")) {
					if (args.length == 4) {
						removeSubowner(rm.getRegion(args[2]), args[2], args[3], p);
						return;
					} else {
						p.sendMessage(ChatColor.RED + "[Regios] Invalid number of arguments specified.");
						p.sendMessage("Proper usage: /regios subowner rem <region> <item>");
						return;
					}
				} else if (args[1].equalsIgnoreCase("list")) {
					if (args.length == 3) {
						listSubOwnerExceptions(rm.getRegion(args[2]), args[2], p);
						return;
					} else {
						p.sendMessage(ChatColor.RED + "[Regios] Invalid number of arguments specified.");
						p.sendMessage("Proper usage: /regios subowner list <region>");
						return;
					}
				} else if (args[1].equalsIgnoreCase("reset")) {
					if (args.length == 3) {
						eraseSubOwnerExceptions(rm.getRegion(args[2]), args[2], p);
						return;
					} else {
						p.sendMessage(ChatColor.RED + "[Regios] Invalid number of arguments specified.");
						p.sendMessage("Proper usage: /regios subowner reset <region>");
						return;
					}
				} else {
					p.sendMessage(ChatColor.RED + "[Regios] Invalid argument specified.");
					p.sendMessage("Proper usage: /regios subowner add/rem/list/reset");
					return;
				}
			} else {
				p.sendMessage(ChatColor.RED + "[Regios] Invalid number of arguments specified.");
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

	private void addPlayerException(Region r, String region, String ex, Player p) {
		if (r == null) {
			p.sendMessage(ChatColor.RED + "[Regios] The region " + ChatColor.BLUE + region + ChatColor.RED + " doesn't exist!");
			return;
		} else {
			if (!r.canModify(p)) {
				p.sendMessage(ChatColor.RED + "[Regios] You are not permitted to modify this region!");
				return;
			}
			if (!mutable.checkPlayerException(r, ex)) {
				p.sendMessage(ChatColor.GREEN + "[Regios] Exception added : " + ChatColor.BLUE + ex);
				mutable.addPlayerException(r, ex);
			} else {
				p.sendMessage(ChatColor.RED + "[Regios] Player " + ChatColor.BLUE + ex + ChatColor.RED + " is already an exception!");
			}
		}
	}

	private void removePlayerException(Region r, String region, String ex, Player p) {
		if (r == null) {
			p.sendMessage(ChatColor.RED + "[Regios] The region " + ChatColor.BLUE + region + ChatColor.RED + " doesn't exist!");
			return;
		} else {
			if (!r.canModify(p)) {
				p.sendMessage(ChatColor.RED + "[Regios] You are not permitted to modify this region!");
				return;
			}
			if (mutable.checkPlayerException(r, ex)) {
				p.sendMessage(ChatColor.GREEN + "[Regios] Exception removed : " + ChatColor.BLUE + ex);
				mutable.removePlayerException(r, ex);
			} else {
				p.sendMessage(ChatColor.RED + "[Regios] Player " + ChatColor.BLUE + ex + ChatColor.RED + " is not an exception!");
			}
		}
	}

	private void addItemException(Region r, String region, String ex, Player p) {
		int val = 0;
		try {
			val = Integer.parseInt(ex);
		} catch (NumberFormatException nfe) {
			p.sendMessage(ChatColor.RED + "[Regios] The item must be an integer!");
			return;
		}
		if (r == null) {
			p.sendMessage(ChatColor.RED + "[Regios] The region " + ChatColor.BLUE + region + ChatColor.RED + " doesn't exist!");
			return;
		} else {
			if(!checkException(val)){
				p.sendMessage(ChatColor.RED + "[Regios] The item id " + ChatColor.BLUE + val + ChatColor.RED + " is invalid!");
				return;
			}
			if (!r.canModify(p)) {
				p.sendMessage(ChatColor.RED + "[Regios] You are not permitted to modify this region!");
				return;
			}
			if (!mutable.checkItemException(r, val)) {
				p.sendMessage(ChatColor.GREEN + "[Regios] Item Exception added : " + ChatColor.BLUE + ex);
				mutable.addItemException(r, val);
			} else {
				p.sendMessage(ChatColor.RED + "[Regios] Item " + ChatColor.BLUE + ex + ChatColor.RED + " is already an exception!");
			}
		}
	}

	private void removeItemException(Region r, String region, String ex, Player p) {
		int val = 0;
		try {
			val = Integer.parseInt(ex);
		} catch (NumberFormatException nfe) {
			p.sendMessage(ChatColor.RED + "[Regios] The item must be an integer!");
			return;
		}
		if (r == null) {
			p.sendMessage(ChatColor.RED + "[Regios] The region " + ChatColor.BLUE + region + ChatColor.RED + " doesn't exist!");
			return;
		} else {
			if(!checkException(val)){
				p.sendMessage(ChatColor.RED + "[Regios] The item id " + ChatColor.BLUE + val + ChatColor.RED + " is invalid!");
				return;
			}
			if (!r.canModify(p)) {
				p.sendMessage(ChatColor.RED + "[Regios] You are not permitted to modify this region!");
				return;
			}
			if (mutable.checkItemException(r, val)) {
				p.sendMessage(ChatColor.GREEN + "[Regios] Item Exception removed : " + ChatColor.BLUE + ex);
				mutable.removeItemException(r, val);
			} else {
				p.sendMessage(ChatColor.RED + "[Regios] Item " + ChatColor.BLUE + ex + ChatColor.RED + " is not an exception!");
			}
		}
	}

	private void addNodeException(Region r, String region, String ex, Player p) {
		if (r == null) {
			p.sendMessage(ChatColor.RED + "[Regios] The region " + ChatColor.BLUE + region + ChatColor.RED + " doesn't exist!");
			return;
		} else {
			if (!r.canModify(p)) {
				p.sendMessage(ChatColor.RED + "[Regios] You are not permitted to modify this region!");
				return;
			}
			if (!mutable.checkNodeException(r, ex)) {
				p.sendMessage(ChatColor.GREEN + "[Regios] Exception Node added : " + ChatColor.BLUE + ex);
				mutable.addNodeException(r, ex);
			} else {
				p.sendMessage(ChatColor.RED + "[Regios] Node " + ChatColor.BLUE + ex + ChatColor.RED + " is already an exception!");
			}
		}
	}

	private void removeNodeException(Region r, String region, String ex, Player p) {
		if (r == null) {
			p.sendMessage(ChatColor.RED + "[Regios] The region " + ChatColor.BLUE + region + ChatColor.RED + " doesn't exist!");
			return;
		} else {
			if (!r.canModify(p)) {
				p.sendMessage(ChatColor.RED + "[Regios] You are not permitted to modify this region!");
				return;
			}
			if (mutable.checkNodeException(r, ex)) {
				p.sendMessage(ChatColor.GREEN + "[Regios] Exception Node added : " + ChatColor.BLUE + ex);
				mutable.removeNodeException(r, ex);
			} else {
				p.sendMessage(ChatColor.RED + "[Regios] Node " + ChatColor.BLUE + ex + ChatColor.RED + " is already an exception!");
			}
		}
	}

	private void erasePlayerExceptions(Region r, String region, Player p) {
		if (r == null) {
			p.sendMessage(ChatColor.RED + "[Regios] The region " + ChatColor.BLUE + region + ChatColor.RED + " doesn't exist!");
			return;
		} else {
			if (!r.canModify(p)) {
				p.sendMessage(ChatColor.RED + "[Regios] You are not permitted to modify this region!");
				return;
			}
			p.sendMessage(ChatColor.GREEN + "[Regios] All player exceptions removed for region : " + ChatColor.BLUE + region);
			mutable.eraseAllPlayerExceptions(r);
			return;
		}
	}

	private void eraseSubOwnerExceptions(Region r, String region, Player p) {
		if (r == null) {
			p.sendMessage(ChatColor.RED + "[Regios] The region " + ChatColor.BLUE + region + ChatColor.RED + " doesn't exist!");
			return;
		} else {
			if (!r.canModify(p)) {
				p.sendMessage(ChatColor.RED + "[Regios] You are not permitted to modify this region!");
				return;
			}
			p.sendMessage(ChatColor.GREEN + "[Regios] All Sub Owners removed for region : " + ChatColor.BLUE + region);
			mutable.eraseAllSubOwners(r);
			return;
		}
	}

	private void eraseItemExceptions(Region r, String region, Player p) {
		if (r == null) {
			p.sendMessage(ChatColor.RED + "[Regios] The region " + ChatColor.BLUE + region + ChatColor.RED + " doesn't exist!");
			return;
		} else {
			if (!r.canModify(p)) {
				p.sendMessage(ChatColor.RED + "[Regios] You are not permitted to modify this region!");
				return;
			}
			p.sendMessage(ChatColor.GREEN + "[Regios] All item exceptions removed for region : " + ChatColor.BLUE + region);
			mutable.eraseAllItemExceptions(r);
			return;
		}
	}

	private void eraseNodeExceptions(Region r, String region, Player p) {
		if (r == null) {
			p.sendMessage(ChatColor.RED + "[Regios] The region " + ChatColor.BLUE + region + ChatColor.RED + " doesn't exist!");
			return;
		} else {
			if (!r.canModify(p)) {
				p.sendMessage(ChatColor.RED + "[Regios] You are not permitted to modify this region!");
				return;
			}
			p.sendMessage(ChatColor.GREEN + "[Regios] All node exceptions removed for region : " + ChatColor.BLUE + region);
			mutable.eraseAllNodeExceptions(r);
			return;
		}
	}

	private void listItemExceptions(Region r, String region, Player p) {
		if (r == null) {
			p.sendMessage(ChatColor.RED + "[Regios] The region " + ChatColor.BLUE + region + ChatColor.RED + " doesn't exist!");
			return;
		} else {
			String regionSet = mutable.listItemExceptions(r);
			p.sendMessage(ChatColor.GREEN + "Regios Item Exception List : " + ChatColor.BLUE + region);
			p.sendMessage(regionSet);
		}
	}

	private void listPlayerExceptions(Region r, String region, Player p) {
		if (r == null) {
			p.sendMessage(ChatColor.RED + "[Regios] The region " + ChatColor.BLUE + region + ChatColor.RED + " doesn't exist!");
			return;
		} else {
			String regionSet = mutable.listPlayerExceptions(r);
			p.sendMessage(ChatColor.GREEN + "Regios Player Exception List : " + ChatColor.BLUE + region);
			p.sendMessage(regionSet);
		}
	}

	private void listSubOwnerExceptions(Region r, String region, Player p) {
		if (r == null) {
			p.sendMessage(ChatColor.RED + "[Regios] The region " + ChatColor.BLUE + region + ChatColor.RED + " doesn't exist!");
			return;
		} else {
			String regionSet = mutable.listSubOwnersExceptions(r);
			p.sendMessage(ChatColor.GREEN + "Regios Sub Owner Exception List : " + ChatColor.BLUE + region);
			p.sendMessage(regionSet);
		}
	}

	private void listNodeExceptions(Region r, String region, Player p) {
		if (r == null) {
			p.sendMessage(ChatColor.RED + "[Regios] The region " + ChatColor.BLUE + region + ChatColor.RED + " doesn't exist!");
			return;
		} else {
			String regionSet = mutable.listNodeExceptions(r);
			p.sendMessage(ChatColor.GREEN + "Regios Node Exception List : " + ChatColor.BLUE + region);
			p.sendMessage(regionSet);
		}
	}

	private void addSubOwner(Region r, String region, String message, Player p) {
		if (r == null) {
			p.sendMessage(ChatColor.RED + "[Regios] The region " + ChatColor.BLUE + region + ChatColor.RED + " doesn't exist!");
			return;
		} else {
			if (!r.canModify(p)) {
				p.sendMessage(ChatColor.RED + "[Regios] You are not permitted to modify this region!");
				return;
			}
			
			if (mutable.checkSubOwnerException(r, message)) {
				p.sendMessage(ChatColor.RED + "[Regios] The Sub Owner " + ChatColor.BLUE + message + ChatColor.RED + " already exists!");
				return;
			}
			p.sendMessage(ChatColor.GREEN + "[Regios] Sub Owner " + ChatColor.BLUE + message + ChatColor.GREEN + " added to region " + ChatColor.BLUE + region);
		}
		mutable.addSubOwner(r, message);
	}

	private void removeSubowner(Region r, String region, String ex, Player p) {
		if (r == null) {
			p.sendMessage(ChatColor.RED + "[Regios] The region " + ChatColor.BLUE + region + ChatColor.RED + " doesn't exist!");
			return;
		} else {
			if (!r.canModify(p)) {
				p.sendMessage(ChatColor.RED + "[Regios] You are not permitted to modify this region!");
				return;
			}
			if (mutable.checkSubOwnerException(r, ex)) {
				p.sendMessage(ChatColor.GREEN + "[Regios] Sub Owner removed : " + ChatColor.BLUE + ex);
				mutable.removeSubOwner(r, ex);
			} else {
				p.sendMessage(ChatColor.RED + "[Regios] Sub Owner " + ChatColor.BLUE + ex + ChatColor.RED + " does not exist!");
			}
		}
	}

	public void removeFromPermRemCache(Region r, String region, String message, Player p) {
		if (r == null) {
			p.sendMessage(ChatColor.RED + "[Regios] The region " + ChatColor.BLUE + region + ChatColor.RED + " doesn't exist!");
			return;
		} else {
			if (!r.canModify(p)) {
				p.sendMessage(ChatColor.RED + "[Regios] You are not permitted to modify this region!");
				return;
			}
			boolean nodeMatch = false;
			for (String s : r.getSubOwners()) {
				if (s.trim().equalsIgnoreCase(message.trim())) {
					nodeMatch = true;
				}
			}
			if (!nodeMatch) {
				p.sendMessage(ChatColor.RED + "[Regios] The Sub Owner " + ChatColor.BLUE + message + ChatColor.RED + " did not match any in the cache!");
				return;
			}
			p.sendMessage(ChatColor.GREEN + "[Regios] Sub Owner " + ChatColor.BLUE + message + ChatColor.GREEN + " removed from region cache " + ChatColor.BLUE + region);
		}
		mutable.removeSubOwner(r, message);
	}

	public void resetPermRemCache(Region r, String region, Player p) {
		if (r == null) {
			p.sendMessage(ChatColor.RED + "[Regios] The region " + ChatColor.BLUE + region + ChatColor.RED + " doesn't exist!");
			return;
		} else {
			if (!r.canModify(p)) {
				p.sendMessage(ChatColor.RED + "[Regios] You are not permitted to modify this region!");
				return;
			}
			p.sendMessage(ChatColor.GREEN + "[Regios] Sub Owners reset for region " + ChatColor.BLUE + region);
		}
		mutable.eraseAllSubOwners(r);
	}

}
