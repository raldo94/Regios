package net.jzx7.regios.Listeners;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.zip.DataFormatException;

import net.jzx7.regios.Commands.CreationCommands;
import net.jzx7.regios.Data.ConfigurationData;
import net.jzx7.regios.Economy.EconomyCore;
import net.jzx7.regios.Economy.EconomyPending;
import net.jzx7.regios.Permissions.PermissionsCore;
import net.jzx7.regios.RBF.RBF_Core;
import net.jzx7.regios.RBF.ShareData;
import net.jzx7.regios.Scheduler.HealthRegeneration;
import net.jzx7.regios.Scheduler.LogRunner;
import net.jzx7.regios.Spout.SpoutInterface;
import net.jzx7.regios.Spout.SpoutRegion;
import net.jzx7.regios.regions.RegionManager;
import net.jzx7.regios.regions.SubRegionManager;
import net.jzx7.regiosapi.regions.CuboidRegion;
import net.jzx7.regiosapi.regions.PolyRegion;
import net.jzx7.regiosapi.regions.Region;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.material.Door;

import couk.Adamki11s.Extras.Events.ExtrasEvents;
import couk.Adamki11s.Extras.Regions.ExtrasRegions;

public class RegiosPlayerListener implements Listener {

	public static enum MSG {
		PROTECTION, AUTHENTICATION, PREVENT_ENTRY, PREVENT_EXIT, ECONOMY;
	}

	private static final ExtrasEvents extEvt = new ExtrasEvents();
	private static final ExtrasRegions extReg = new ExtrasRegions();
	private static final SubRegionManager srm = new SubRegionManager();
	private static final CreationCommands creationCommands = new CreationCommands();
	private static final RegionManager rm = new RegionManager();

	public static HashMap<String, Region> regionBinding = new HashMap<String, Region>();
	public static HashMap<String, Region> currentRegion = new HashMap<String, Region>();

	private static HashMap<String, Location> outsideRegionLocation = new HashMap<String, Location>();
	private static HashMap<String, Location> insideRegionLocation = new HashMap<String, Location>();

	public static HashMap<String, Long> timeStampsProtection = new HashMap<String, Long>();
	public static HashMap<String, Long> timeStampsAuth = new HashMap<String, Long>();
	public static HashMap<String, Long> timeStampsPreventEntry = new HashMap<String, Long>();
	public static HashMap<String, Long> timeStampsPreventExit = new HashMap<String, Long>();
	public static HashMap<String, Long> timeStampsEconomy = new HashMap<String, Long>();

	public static HashMap<String, ShareData> loadingTerrain = new HashMap<String, ShareData>();

	private static void setTimestamp(Player p, MSG msg) {
		switch (msg) {
		case PROTECTION:
			timeStampsProtection.put(p.getName(), System.currentTimeMillis());
			break;
		case AUTHENTICATION:
			timeStampsAuth.put(p.getName(), System.currentTimeMillis());
			break;
		case PREVENT_ENTRY:
			timeStampsPreventEntry.put(p.getName(), System.currentTimeMillis());
			break;
		case PREVENT_EXIT:
			timeStampsPreventExit.put(p.getName(), System.currentTimeMillis());
			break;
		case ECONOMY:
			timeStampsEconomy.put(p.getName(), System.currentTimeMillis());
			break;
		}
	}

	public static boolean isSendable(Player p, MSG msg) {
		boolean outcome = false;
		switch (msg) {
		case PROTECTION:
			outcome = (timeStampsProtection.containsKey(p.getName()) ? (System.currentTimeMillis() > timeStampsProtection.get(p.getName()) + 5000) : true);
			break;
		case AUTHENTICATION:
			outcome = (timeStampsAuth.containsKey(p.getName()) ? (System.currentTimeMillis() > timeStampsAuth.get(p.getName()) + 5000) : true);
			break;
		case PREVENT_ENTRY:
			outcome = (timeStampsPreventEntry.containsKey(p.getName()) ? (System.currentTimeMillis() > timeStampsPreventEntry.get(p.getName()) + 5000) : true);
			break;
		case PREVENT_EXIT:
			outcome = (timeStampsPreventExit.containsKey(p.getName()) ? (System.currentTimeMillis() > timeStampsPreventExit.get(p.getName()) + 5000) : true);
			break;
		case ECONOMY:
			outcome = (timeStampsEconomy.containsKey(p.getName()) ? (System.currentTimeMillis() > timeStampsEconomy.get(p.getName()) + 5000) : true);
			break;
		}
		if (outcome) {
			setTimestamp(p, msg);
		}
		return outcome;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerQuit(PlayerQuitEvent evt) {
		Player p = evt.getPlayer();
		if (HealthRegeneration.isRegenerator(p)) {
			HealthRegeneration.removeRegenerator(p);
		}
		if (SpoutInterface.doesPlayerHaveSpout(p)) {
			SpoutRegion.stopMusicPlaying(p, null);
		}
		for (Region r : rm.getRegions()) {
			if (r.isAuthenticated(p)) {
				r.getAuthentication().put(p.getName(), false);
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerJoin(PlayerJoinEvent evt) {
		SpoutInterface.spoutEnabled.put(evt.getPlayer().getName(), false);
		if (EconomyPending.isPending(evt.getPlayer())) {
			EconomyPending.loadAndSendPending(evt.getPlayer());
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerInteract(PlayerInteractEvent evt) {

		if (evt.getClickedBlock() == null) {
			return;
		}

		Location l = evt.getClickedBlock().getLocation();
		Player p = evt.getPlayer();
		Region r = rm.getRegion(l);

		if (evt.getAction() == Action.LEFT_CLICK_BLOCK) {
			if (loadingTerrain.containsKey(p.getName())) {
				if (p.getItemInHand().getType() == ConfigurationData.defaultSelectionTool) {
					ShareData sd = loadingTerrain.get(p.getName());
					if (sd.getShareType().equalsIgnoreCase("blp")) {
						try {
							RBF_Core.blueprint.loadBlueprint(sd.getShareName(), sd.getPlayer(), l);
						} catch (IOException e) {
							e.printStackTrace();
						}
					} else if (sd.getShareType().equalsIgnoreCase("sch")) {
						try {
							RBF_Core.schematic.loadSchematic(sd.getShareName(), sd.getPlayer(), l);
						} catch (IOException e) {
							e.printStackTrace();
						} catch (DataFormatException e) {
							e.printStackTrace();
						}
					}
					loadingTerrain.remove(p.getName());
				}
			}
		}

		Block b = evt.getClickedBlock();

		if (creationCommands.isSetting(p)) {
			Action act = evt.getAction();
			if (act == Action.LEFT_CLICK_BLOCK) {
				if (creationCommands.getSettingType(p).equalsIgnoreCase("cuboid")) {
					creationCommands.setFirst(p, evt.getClickedBlock().getLocation());
				} else if (creationCommands.getSettingType(p).equalsIgnoreCase("polygon")) {
					creationCommands.addPoint(p, evt.getClickedBlock().getLocation());
				}
				return;
			} else if (act == Action.RIGHT_CLICK_BLOCK) {
				if (creationCommands.getSettingType(p).equalsIgnoreCase("cuboid")) {
					creationCommands.setSecond(p, evt.getClickedBlock().getLocation());
				} else if (creationCommands.getSettingType(p).equalsIgnoreCase("polygon")) {
					creationCommands.removeLastPoint(p);
				}
				return;
			}
		}

		if (creationCommands.isModding(p)) {
			Action act = evt.getAction();
			if (act == Action.LEFT_CLICK_BLOCK) {
				if (creationCommands.getSettingType(p).equalsIgnoreCase("cuboid")) {
					creationCommands.setFirstMod(p, evt.getClickedBlock().getLocation());
				} else if (creationCommands.getSettingType(p).equalsIgnoreCase("polygon")) {
					creationCommands.addMPoint(p, l);
				}
				return;
			} else if (act == Action.RIGHT_CLICK_BLOCK) {
				if (creationCommands.getSettingType(p).equalsIgnoreCase("cuboid")) {
					creationCommands.setSecondMod(p, evt.getClickedBlock().getLocation());
				} else if (creationCommands.getSettingType(p).equalsIgnoreCase("polygon")) {
					creationCommands.removeLastMPoint(p);
				}
				return;
			}
		}

		if (EconomyCore.isEconomySupportEnabled() && evt.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if ((b.getType() == Material.SIGN || b.getType() == Material.SIGN_POST || b.getTypeId() == 68)) {
				Sign sign = (Sign) b.getState();
				if (sign.getLine(0).contains("[Regios]")) {
					Region region = rm.getRegion(sign.getLine(1).substring(2, sign.getLine(1).length()));
					if (region == null) {
						p.sendMessage(ChatColor.RED + "[Regios] Sorry, This region no longer exists!");
						b.setTypeId(0);
						return;
					}
					if (region.getOwner().equals(p.getName())) {
						if (isSendable(p, MSG.ECONOMY)) {
							p.sendMessage(ChatColor.RED + "[Regios] You cannot buy this region as you already own it!");
						}
						evt.setCancelled(true);
						return;
					}
					if (region.isForSale()) {
						if (PermissionsCore.doesHaveNode(p, "regios.fun.buy")) {
							int price = region.getSalePrice();
							sign.setLine(2, String.valueOf(price));
							if (!EconomyCore.canAffordRegion(p.getName(), price)) {
								if (isSendable(p, MSG.ECONOMY)) {
									p.sendMessage(ChatColor.RED + "[Regios] You cannot afford this region!");
									LogRunner.addLogMessage(region, LogRunner.getPrefix(region)
											+ (" Player '" + p.getName() + "' tried to buy region but didn't have enough money."));
								}
								return;
							} else {
								EconomyCore.buyRegion(region, p.getName(), region.getOwner(), price);
								LogRunner.addLogMessage(region, LogRunner.getPrefix(region)
										+ (" Player '" + p.getName() + "' bought region from '" + region.getOwner() + "' for " + price + "."));
								p.sendMessage(ChatColor.GREEN + "[Regios] Region " + ChatColor.BLUE + region.getName() + ChatColor.GREEN + " purchased for "
										+ ChatColor.GOLD + price + ChatColor.GREEN + "!");
								b.setTypeId(0);
								return;
							}
						} else {
							PermissionsCore.sendInvalidPerms(p);
							return;
						}
					} else {
						if (isSendable(p, MSG.ECONOMY)) {
							p.sendMessage(ChatColor.RED + "[Regios] This region is not for sale, sorry!");
						}
						b.setTypeId(0);
						return;
					}
				}
			}
		} else {
			if ((b.getType() == Material.SIGN || b.getType() == Material.SIGN_POST || b.getTypeId() == 68)) {
				Sign sign = (Sign) b.getState();
				if (sign.getLine(0).contains("[Regios]")) {
					if (evt.getAction() == Action.LEFT_CLICK_BLOCK) {
						if (isSendable(p, MSG.ECONOMY)) {
							p.sendMessage(ChatColor.RED + "[Regios] You must right click to buy the relative region!");
						}
						return;
					} else if (!EconomyCore.isEconomySupportEnabled() && evt.getAction() == Action.RIGHT_CLICK_BLOCK) {
						if (isSendable(p, MSG.ECONOMY)) {
							p.sendMessage(ChatColor.RED + "[Regios] Economy support is not enabled in the configuration!");
						}
						return;
					}
				}
			}
		}

		if (r != null) 
		{
			if (r.isPreventInteraction()) {
				if (!r.canBypassProtection(p)) {
					if (isSendable(p, MSG.PROTECTION)) {
						p.sendMessage(ChatColor.RED + "[Regios] You cannot interact within this region!");
					}
					LogRunner.addLogMessage(r, LogRunner.getPrefix(r) + (" Player '" + p.getName() + "' tried to interact but did not have permissions."));
					evt.setCancelled(true);
					return;
				}
			}

			if (b.getTypeId() == 64 || b.getTypeId() == 71 || b.getTypeId() == 96) {
				if (r.areDoorsLocked()) {
					if (!r.canBypassProtection(p)) {
						if (isSendable(p, MSG.PROTECTION)) {
							p.sendMessage(ChatColor.RED + "[Regios] Doors are locked for this region!");
						}
						LogRunner.addLogMessage(r, LogRunner.getPrefix(r) + (" Player '" + p.getName() + "' tried to open a locked door but did not have permissions."));
						Door d = new Door(b.getType());
						d.setOpen(false);
						evt.setCancelled(true);
					}
				}
			}

			if (b.getTypeId() == 54 || b.getTypeId() == 95) {
				if (r.areChestsLocked()) {
					if (!r.canBypassProtection(p)) {
						if (isSendable(p, MSG.PROTECTION)) {
							p.sendMessage(ChatColor.RED + "[Regios] Chests are locked for this region!");
						}
						LogRunner.addLogMessage(r, LogRunner.getPrefix(r) + (" Player '" + p.getName() + "' tried to open a locked chest but did not have permissions."));
						p.closeInventory();
						evt.setCancelled(true);
					}
				}
			}

			if (b.getTypeId() == 23) {
				if (r.areDispensersLocked()) {
					if (!r.canBypassProtection(p)) {
						if (isSendable(p, MSG.PROTECTION)) {
							p.sendMessage(ChatColor.RED + "[Regios] Dispensers are locked for this region!");
						}
						LogRunner.addLogMessage(r, LogRunner.getPrefix(r) + (" Player '" + p.getName() + "' tried to open a locked dispenser but did not have permissions."));
						p.closeInventory();
						evt.setCancelled(true);
					}
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerBucketFill(PlayerBucketFillEvent evt) {
		Location l = evt.getBlockClicked().getLocation();
		Player p = evt.getPlayer();

		ArrayList<Region> regions = rm.getRegions(l);
		if (!regions.isEmpty())
		{
			for (Region r : regions) {
				if (r.isProtected()) {
					if (!r.canBypassProtection(p)) {
						LogRunner.addLogMessage(r, LogRunner.getPrefix(r)
								+ (" Player '" + p.getName() + "' tried to fill a " + evt.getBucket().toString() + " but was prevented."));
						rm.sendBuildMessage(p, r);
						evt.setCancelled(true);
						return;
					}
				}
			}
		}
	}

	// Old onPlayerBucketEmpty
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerBucketEmpty(PlayerBucketEmptyEvent evt) {
		Location l = evt.getBlockClicked().getLocation();
		BlockFace bf = evt.getBlockFace();
		if (bf.name().equalsIgnoreCase("UP"))
		{
			l.setY(l.getY() + 1);
		}
		if (bf.name().equalsIgnoreCase("DOWN"))
		{
			l.setY(l.getY() - 1);
		}
		if (bf.name().equalsIgnoreCase("NORTH"))
		{
			l.setZ(l.getZ() - 1);
		}
		if (bf.name().equalsIgnoreCase("SOUTH"))
		{
			l.setZ(l.getZ() + 1);
		}
		if (bf.name().equalsIgnoreCase("EAST"))
		{
			l.setX(l.getX() + 1);
		}
		if (bf.name().equalsIgnoreCase("WEST"))
		{
			l.setX(l.getX() - 1);
		}
		Player p = evt.getPlayer();

		ArrayList<Region> currentRegionSet = new ArrayList<Region>();

		for (Region reg : rm.getRegions()) {
			if (reg instanceof PolyRegion) {
				PolyRegion pr = (PolyRegion) reg;
				Outerloop:
					for (int x = -8; x <= 8; x++) {
						for (int y = -8; y <= 8; y++) {
							for (int z = -8; z <= 8; z++) {
								if (extReg.isInsidePolygon(l.add(x, y, z),pr.get2DPolygon(), pr.getMinY(), pr.getMaxY()) && (p.getWorld().getName() == pr.getWorld().getName())) {
									currentRegionSet.add(reg);
									break Outerloop;
								}
							}
						}
					}
			} else if (reg instanceof CuboidRegion) {
				CuboidRegion cr = (CuboidRegion) reg;
				Outerloop:
					for (int x = -8; x <= 8; x++) {
						for (int y = -8; y <= 8; y++) {
							for (int z = -8; z <= 8; z++) {
								if (extReg.isInsideCuboid(l.add(x, y, z), cr.getL1(), cr.getL2()) && (p.getWorld().getName() == cr.getWorld().getName())) {
									currentRegionSet.add(reg);
									break Outerloop;
								}
							}
						}
					}
			}
		}

		if (currentRegionSet.isEmpty()) { // If player is in chunk range but not
			// inside region then cancel the
			// check.
			return;
		} else {
			for (Region r : currentRegionSet) {
				if (r.isProtected()) {
					if (!r.canBuild(p)) {
						LogRunner.addLogMessage(r, LogRunner.getPrefix(r)
								+ (" Player '" + p.getName() + "' tried to empty a " + evt.getBucket().toString() + " but was prevented."));
						rm.sendBuildMessage(p, r);
						evt.setCancelled(true);
						return;
					}
				}
			}
		}
	}

	//New onPlayerBucketEmpty - out of use until lag issue can be fixed
	//	@EventHandler(priority = EventPriority.HIGHEST)
	//	public void onPlayerBucketEmpty(PlayerBucketEmptyEvent evt) {
	//		Location l = evt.getBlockClicked().getLocation();
	//		BlockFace bf = evt.getBlockFace();
	//		if (bf.name().equalsIgnoreCase("UP"))
	//		{
	//			l.setY(l.getY() + 1);
	//		}
	//		if (bf.name().equalsIgnoreCase("DOWN"))
	//		{
	//			l.setY(l.getY() - 1);
	//		}
	//		if (bf.name().equalsIgnoreCase("NORTH"))
	//		{
	//			l.setZ(l.getZ() - 1);
	//		}
	//		if (bf.name().equalsIgnoreCase("SOUTH"))
	//		{
	//			l.setZ(l.getZ() + 1);
	//		}
	//		if (bf.name().equalsIgnoreCase("EAST"))
	//		{
	//			l.setX(l.getX() + 1);
	//		}
	//		if (bf.name().equalsIgnoreCase("WEST"))
	//		{
	//			l.setX(l.getX() - 1);
	//		}
	//		Player p = evt.getPlayer();
	//
	//		ArrayList<Region> regions = rm.getRegions(l);
	//		if (!regions.isEmpty())
	//		{
	//			for (Region r : regions) {
	//				if (r.isProtected()) {
	//					if (!r.canBypassProtection(p)) {
	//						LogRunner.addLogMessage(r, LogRunner.getPrefix(r)
	//								+ (" Player '" + p.getName() + "' tried to empty a " + evt.getBucket().toString() + " but was prevented."));
	//						r.sendBuildMessage(p);
	//						evt.setCancelled(true);
	//						return;
	//					}
	//				}
	//			}
	//		}
	//	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerMove(PlayerMoveEvent evt) {

		if (!extEvt.didMove(evt)) {
			return; // Cancel the check if the player did not move
		}

		Player p = evt.getPlayer();
		Region r;

		boolean authenticated = false, inRegion = false;

		if (regionBinding.containsKey(p.getName())) {
			Region binding = regionBinding.get(p.getName());
			if (binding == null) {
				return;
			}

			if (binding instanceof PolyRegion) {
				inRegion = extReg.isInsidePolygon(p, ((PolyRegion) binding).get2DPolygon(), ((PolyRegion) binding).getMinY(), ((PolyRegion) binding).getMaxY()) && (p.getWorld().getName() == binding.getWorld().getName());
			} else if (binding instanceof CuboidRegion) {
				inRegion = extReg.isInsideCuboid(p, ((CuboidRegion) binding).getL1(), ((CuboidRegion) binding).getL2()) && (p.getWorld().getName() == binding.getWorld().getName());
			}

			if (binding.isPreventEntry() && inRegion) {
				if (!binding.canEnter(p)) {
					if (!binding.isPasswordEnabled()) {
						if (outsideRegionLocation.containsKey(p.getName())) {
							p.teleport(outsideRegionLocation.get(p.getName()));
						}
						if (isSendable(p, MSG.PREVENT_ENTRY)) {
							rm.sendPreventEntryMessage(p, binding);
						}
						return;
					} else {
						if (!binding.isAuthenticated(p)) {
							if (isSendable(p, MSG.AUTHENTICATION)) {
								p.sendMessage(ChatColor.RED + "Authentication required! Do /regios auth <password>");
							}
							if (outsideRegionLocation.containsKey(p.getName())) {
								p.teleport(outsideRegionLocation.get(p.getName()));
							}
							return;
						} else {
							authenticated = true;
						}
					}
				}
			}

			if (binding.isPreventExit() && !inRegion) {
				if (!binding.canExit(p)) {
					if (!binding.isPasswordEnabled()) {
						if (insideRegionLocation.containsKey(p.getName())) {
							p.teleport(insideRegionLocation.get(p.getName()));
						}
						if (isSendable(p, MSG.PREVENT_EXIT)) {
							rm.sendPreventExitMessage(p, binding);
						}
						return;
					} else {
						if (!binding.isAuthenticated(p)) {
							if (isSendable(p, MSG.AUTHENTICATION)) {
								p.sendMessage(ChatColor.RED + "Authentication required! Do /regios auth <password>");
							}
							if (insideRegionLocation.containsKey(p.getName())) {
								p.teleport(insideRegionLocation.get(p.getName()));
							}
							return;
						} else {
							authenticated = true;
						}
					}
				}
			}

			if (binding instanceof PolyRegion) {
				if (!(extReg.isInsidePolygon(p, ((PolyRegion) binding).get2DPolygon(), ((PolyRegion) binding).getMinY(), ((PolyRegion) binding).getMaxY()) && (p.getWorld().getName() == binding.getWorld().getName()))) {
					rm.sendLeaveMessage(p, binding);
				}
			} else if (binding instanceof CuboidRegion) {
				if (!(extReg.isInsideCuboid(p, ((CuboidRegion) binding).getL1(), ((CuboidRegion) binding).getL2()) && (p.getWorld() == binding.getWorld()) && (p.getWorld().getName() == binding.getWorld().getName()))) {
					rm.sendLeaveMessage(p, binding);
				}
			}


		}

		ArrayList<Region> currentRegionSet = new ArrayList<Region>();

		for (Region reg : rm.getRegions()) {
			if (reg instanceof PolyRegion) {
				PolyRegion pr = (PolyRegion) reg;
				if (extReg.isInsidePolygon(p, pr.get2DPolygon(), pr.getMinY(), pr.getMaxY()) && (p.getWorld().getName() == reg.getWorld().getName())) {
					currentRegionSet.add(reg);
					if (insideRegionLocation.containsKey(p.getName())) {
						insideRegionLocation.put(p.getName(), p.getLocation());
					}
				}
			} else if (reg instanceof CuboidRegion) {
				if (extReg.isInsideCuboid(p, ((CuboidRegion) reg).getL1(), ((CuboidRegion) reg).getL2()) && (p.getWorld().getName() == reg.getWorld().getName())) {
					currentRegionSet.add(reg);
					if (insideRegionLocation.containsKey(p.getName())) {
						insideRegionLocation.put(p.getName(), p.getLocation());
					}
				}
			}
		}

		if (currentRegionSet.isEmpty()) { // If player is in chunk range but not
			// inside region then cancel the
			// check.
			if (evt.getFrom().getBlockY() == evt.getTo().getBlockY()) { // To prevent people getting stuck if jumping into a region
				outsideRegionLocation.put(p.getName(), p.getLocation());
			}
			return;
		}

		if (currentRegionSet.size() > 1) {
			r = srm.getCurrentRegion(currentRegionSet);
			regionBinding.put(p.getName(), r);
		} else {
			r = currentRegionSet.get(0);
			regionBinding.put(p.getName(), r);
		}

		if (r.isRegionFull(p)) {
			if (outsideRegionLocation.containsKey(p.getName())) {
				p.teleport(outsideRegionLocation.get(p.getName()));
			}
			LogRunner.addLogMessage(r, LogRunner.getPrefix(r) + (" Player '" + p.getName() + "' tried to enter region but it was full."));
			if (isSendable(p, MSG.PREVENT_ENTRY)) {
				p.sendMessage(ChatColor.RED + "[Regios] This region is full! Only " + r.getPlayerCap() + " players are allowed inside at a time.");
			}
			return;
		}

		if (r.isPreventEntry() && !authenticated) {
			if (!r.canEnter(p)) {
				LogRunner.addLogMessage(r, LogRunner.getPrefix(r) + (" Player '" + p.getName() + "' tried to enter but did not have permissions."));
				if (outsideRegionLocation.containsKey(p.getName())) {
					p.teleport(outsideRegionLocation.get(p.getName()));
				}
				if (isSendable(p, MSG.PREVENT_ENTRY)) {
					rm.sendPreventEntryMessage(p, r);
				}
				return;
			}
		}

		rm.sendWelcomeMessage(p, r);
		insideRegionLocation.put(p.getName(), p.getLocation());

		// __________________________________
		// ^^^^ Messages & Entry control ^^^^
		// __________________________________

		if (r.getVelocityWarp() != 0) {
			p.setVelocity(p.getLocation().getDirection().multiply(((r.getVelocityWarp()) * (0.3)) / 2).setY(0.1));
		}

	}

	//I don't think this is necessary atm. Keeping for sentimental value. :P
	//	@EventHandler(priority = EventPriority.HIGHEST)
	//	public void onPlayerTeleport(PlayerTeleportEvent evt) {
	//		Bukkit.getServer().getPluginManager().callEvent(new PlayerMoveEvent(evt.getPlayer(), evt.getFrom(), evt.getTo()));
	//	}
	//	
	//	@EventHandler(priority = EventPriority.HIGHEST)
	//	public void onPlayerPortal(PlayerPortalEvent evt) {
	//		Bukkit.getServer().getPluginManager().callEvent(new PlayerMoveEvent(evt.getPlayer(), evt.getFrom(), evt.getTo()));
	//	}

	public String getLocation(Location l) {
		return l.getX() + " | " + l.getY() + " | " + l.getZ();
	}

}
