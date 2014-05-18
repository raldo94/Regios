package net.jzx7.regios.bukkit.listeners;

import java.io.IOException;
import java.util.ArrayList;

import net.jzx7.regios.Commands.CreationCommands;
import net.jzx7.regios.Data.ConfigurationData;
import net.jzx7.regios.Economy.EconomyCore;
import net.jzx7.regios.Economy.EconomyPending;
import net.jzx7.regios.Permissions.PermissionsCore;
import net.jzx7.regios.RBF.RBF_Core;
import net.jzx7.regios.RBF.ShareData;
import net.jzx7.regios.Scheduler.HealthRegeneration;
import net.jzx7.regios.Scheduler.LogRunner;
import net.jzx7.regios.bukkit.SpoutPlugin.SpoutInterface;
import net.jzx7.regios.bukkit.SpoutPlugin.SpoutRegion;
import net.jzx7.regios.entity.PlayerManager;
import net.jzx7.regios.messages.Message;
import net.jzx7.regios.messages.MsgFormat;
import net.jzx7.regios.regions.RegionManager;
import net.jzx7.regios.regions.SubRegionManager;
import net.jzx7.regios.util.RegionUtil;
import net.jzx7.regios.util.RegiosConversions;
import net.jzx7.regiosapi.entity.RegiosPlayer;
import net.jzx7.regiosapi.exceptions.InvalidNBTData;
import net.jzx7.regiosapi.location.RegiosPoint;
import net.jzx7.regiosapi.regions.CuboidRegion;
import net.jzx7.regiosapi.regions.PolyRegion;
import net.jzx7.regiosapi.regions.Region;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
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
import org.bukkit.material.Openable;

public class RegiosPlayerListener implements Listener {

	public static enum MSG {
		PROTECTION, AUTHENTICATION, PREVENT_ENTRY, PREVENT_EXIT, ECONOMY;
	}

	private static final RegionUtil regUtil = new RegionUtil();
	private static final SubRegionManager srm = new SubRegionManager();
	private static final CreationCommands creationCommands = new CreationCommands();
	private static final RegionManager rm = new RegionManager();
	private static final PlayerManager pm = new PlayerManager();

	private static void setTimestamp(RegiosPlayer p, MSG msg) {
		switch (msg) {
		case PROTECTION:
			pm.getTimeStampsProtection().put(p.getName(), System.currentTimeMillis());
			break;
		case AUTHENTICATION:
			pm.getTimeStampsAuth().put(p.getName(), System.currentTimeMillis());
			break;
		case PREVENT_ENTRY:
			pm.getTimeStampsPreventEntry().put(p.getName(), System.currentTimeMillis());
			break;
		case PREVENT_EXIT:
			pm.getTimeStampsPreventExit().put(p.getName(), System.currentTimeMillis());
			break;
		case ECONOMY:
			pm.getTimeStampsEconomy().put(p.getName(), System.currentTimeMillis());
			break;
		}
	}

	public static boolean isSendable(RegiosPlayer p, MSG msg) {
		boolean outcome = false;
		switch (msg) {
		case PROTECTION:
			outcome = (pm.getTimeStampsProtection().containsKey(p.getName()) ? (System.currentTimeMillis() > pm.getTimeStampsProtection().get(p.getName()) + 5000) : true);
			break;
		case AUTHENTICATION:
			outcome = (pm.getTimeStampsAuth().containsKey(p.getName()) ? (System.currentTimeMillis() > pm.getTimeStampsAuth().get(p.getName()) + 5000) : true);
			break;
		case PREVENT_ENTRY:
			outcome = (pm.getTimeStampsPreventEntry().containsKey(p.getName()) ? (System.currentTimeMillis() > pm.getTimeStampsPreventEntry().get(p.getName()) + 5000) : true);
			break;
		case PREVENT_EXIT:
			outcome = (pm.getTimeStampsPreventExit().containsKey(p.getName()) ? (System.currentTimeMillis() > pm.getTimeStampsPreventExit().get(p.getName()) + 5000) : true);
			break;
		case ECONOMY:
			outcome = (pm.getTimeStampsEconomy().containsKey(p.getName()) ? (System.currentTimeMillis() > pm.getTimeStampsEconomy().get(p.getName()) + 5000) : true);
			break;
		}
		if (outcome) {
			setTimestamp(p, msg);
		}
		return outcome;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerQuit(PlayerQuitEvent evt) {
		RegiosPlayer p = RegiosConversions.getRegiosPlayer(evt.getPlayer());
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
		pm.removeRegiosPlayer(p.getName());
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerJoin(PlayerJoinEvent evt) {
		RegiosPlayer p = RegiosConversions.getRegiosPlayer(evt.getPlayer());
		SpoutInterface.getSpoutEnabled().put(p.getName(), false);
		if (EconomyPending.isPending(p)) {
			EconomyPending.loadAndSendPending(p);
		}
		
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerInteract(PlayerInteractEvent evt) {

		if (evt.getClickedBlock() == null) {
			return;
		}

		RegiosPoint l = RegiosConversions.getPoint(evt.getClickedBlock().getLocation());
		RegiosPlayer p = RegiosConversions.getRegiosPlayer(evt.getPlayer());
		Region r = rm.getRegion(l);

		if (evt.getAction() == Action.LEFT_CLICK_BLOCK) {
			if (pm.getLoadingTerrain().containsKey(p.getName())) {
				if (p.getItemInHand().getId() == ConfigurationData.defaultSelectionTool) {
					ShareData sd = pm.getLoadingTerrain().get(p.getName());
					if (sd.getShareType().equalsIgnoreCase("blp")) {
						try {
							RBF_Core.blueprint.loadBlueprint(sd.getShareName(), sd.getPlayer(), l);
						} catch (IOException e) {
							e.printStackTrace();
						}
					} else if (sd.getShareType().equalsIgnoreCase("sch")) {
						try {
							try {
								RBF_Core.schematic.loadSchematic(sd.getShareName(), sd.getPlayer(), l);
							} catch (InvalidNBTData e) {
								e.printStackTrace();
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					pm.getLoadingTerrain().remove(p.getName());
				}
			}
		}

		Block b = evt.getClickedBlock();

		if (creationCommands.isSetting(p)) {
			Action act = evt.getAction();
			if (act == Action.LEFT_CLICK_BLOCK) {
				if (creationCommands.getSettingType(p).equalsIgnoreCase("cuboid")) {
					creationCommands.setFirst(p, RegiosConversions.getPoint(evt.getClickedBlock().getLocation()));
				} else if (creationCommands.getSettingType(p).equalsIgnoreCase("polygon")) {
					creationCommands.addPoint(p, RegiosConversions.getPoint(evt.getClickedBlock().getLocation()));
				}
				return;
			} else if (act == Action.RIGHT_CLICK_BLOCK) {
				if (creationCommands.getSettingType(p).equalsIgnoreCase("cuboid")) {
					creationCommands.setSecond(p, RegiosConversions.getPoint(evt.getClickedBlock().getLocation()));
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
					creationCommands.setFirstMod(p, RegiosConversions.getPoint(evt.getClickedBlock().getLocation()));
				} else if (creationCommands.getSettingType(p).equalsIgnoreCase("polygon")) {
					creationCommands.addMPoint(p, l);
				}
				return;
			} else if (act == Action.RIGHT_CLICK_BLOCK) {
				if (creationCommands.getSettingType(p).equalsIgnoreCase("cuboid")) {
					creationCommands.setSecondMod(p, RegiosConversions.getPoint(evt.getClickedBlock().getLocation()));
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
						p.sendMessage(Message.REGIONDOESNTEXIST.getMessage() + MsgFormat.colourFormat("<BLUE>" + sign.getLine(1).substring(2, sign.getLine(1).length())));
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
								LogRunner.addLogMessage(region, LogRunner.getPrefix(region) + (" Player '" + p.getName() + "' bought region from '" + region.getOwner() + "' for " + price + "."));
								p.sendMessage(ChatColor.GREEN + "[Regios] Region " + ChatColor.BLUE + region.getName() + ChatColor.GREEN + " purchased for " + ChatColor.GOLD + price + ChatColor.GREEN + "!");
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
						Openable d = (Openable) b.getState().getData();
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
		RegiosPoint l = RegiosConversions.getPoint(evt.getBlockClicked().getLocation());
		RegiosPlayer p = RegiosConversions.getRegiosPlayer(evt.getPlayer());

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
		RegiosPoint l = RegiosConversions.getPoint(evt.getBlockClicked().getLocation());
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
		RegiosPlayer p = RegiosConversions.getRegiosPlayer(evt.getPlayer());

		ArrayList<Region> currentRegionSet = new ArrayList<Region>();

		for (Region reg : rm.getRegions()) {
			if (reg instanceof PolyRegion) {
				PolyRegion pr = (PolyRegion) reg;
				Outerloop:
					for (int x = -8; x <= 8; x++) {
						for (int y = -8; y <= 8; y++) {
							for (int z = -8; z <= 8; z++) {
								if (regUtil.isInsidePolygon(l.add(x, y, z),pr.get2DPolygon(), pr.getMinY(), pr.getMaxY()) && (p.getRegiosWorld().getName() == pr.getWorld().getName())) {
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
								if (regUtil.isInsideCuboid(l.add(x, y, z), cr.getL1(), cr.getL2()) && (p.getRegiosWorld().getName() == cr.getWorld().getName())) {
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
	//		Location l = RegiosConversions.getPoint(evt.getBlockClicked().getLocation());
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
	//		Player p = RegiosConversions.getRegiosPlayer(evt.getPlayer());
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

		if (!((evt.getTo().getX() != evt.getFrom().getX()) || (evt.getTo().getY() != evt.getFrom().getY()) || (evt.getTo().getZ() != evt.getFrom().getZ()))) {
			return; // Cancel the check if the player did not move
		}

		RegiosPlayer p = RegiosConversions.getRegiosPlayer(evt.getPlayer());
		Region r;
		boolean authenticated = false, inRegion = false;

		if (pm.getRegionBinding().containsKey(p.getName())) {
			Region binding = pm.getRegionBinding().get(p.getName());
			if (binding == null) {
				return;
			}

			if (binding instanceof PolyRegion) {
				inRegion = regUtil.isInsidePolygon(p, ((PolyRegion) binding).get2DPolygon(), ((PolyRegion) binding).getMinY(), ((PolyRegion) binding).getMaxY()) && (p.getRegiosWorld().getName() == binding.getWorld().getName());
			} else if (binding instanceof CuboidRegion) {
				inRegion = regUtil.isInsideCuboid(p, ((CuboidRegion) binding).getL1(), ((CuboidRegion) binding).getL2()) && (p.getRegiosWorld().getName() == binding.getWorld().getName());
			}

			if (binding.isPreventEntry() && inRegion) {
				if (!binding.canEnter(p)) {
					if (!binding.isPasswordEnabled()) {
						if (pm.getOutsideRegionLocation().containsKey(p.getName())) {
							p.teleport(pm.getOutsideRegionLocation().get(p.getName()));
						}
						if (isSendable(p, MSG.PREVENT_ENTRY)) {
							rm.sendPreventEntryMessage(p, binding);
						}
						return;
					} else {
						if (!binding.isAuthenticated(p)) {
							if (isSendable(p, MSG.AUTHENTICATION)) {
								p.sendMessage(ConfigurationData.defaultAuthenticationMessage);
							}
							if (pm.getOutsideRegionLocation().containsKey(p.getName())) {
								p.teleport(pm.getOutsideRegionLocation().get(p.getName()));
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
						if (pm.getInsideRegionLocation().containsKey(p.getName())) {
							p.teleport(pm.getInsideRegionLocation().get(p.getName()));
						}
						if (isSendable(p, MSG.PREVENT_EXIT)) {
							rm.sendPreventExitMessage(p, binding);
						}
						return;
					} else {
						if (!binding.isAuthenticated(p)) {
							if (isSendable(p, MSG.AUTHENTICATION)) {
								p.sendMessage(ConfigurationData.defaultAuthenticationMessage);
							}
							if (pm.getInsideRegionLocation().containsKey(p.getName())) {
								p.teleport(pm.getInsideRegionLocation().get(p.getName()));
							}
							return;
						} else {
							authenticated = true;
						}
					}
				}
			}

			if (binding instanceof PolyRegion) {
				if (!(regUtil.isInsidePolygon(p, ((PolyRegion) binding).get2DPolygon(), ((PolyRegion) binding).getMinY(), ((PolyRegion) binding).getMaxY()) && (p.getRegiosWorld().getName() == binding.getWorld().getName()))) {
					rm.sendLeaveMessage(p, binding);
				}
			} else if (binding instanceof CuboidRegion) {
				if (!(regUtil.isInsideCuboid(p, ((CuboidRegion) binding).getL1(), ((CuboidRegion) binding).getL2()) && (p.getRegiosWorld() == binding.getWorld()) && (p.getRegiosWorld().getName() == binding.getWorld().getName()))) {
					rm.sendLeaveMessage(p, binding);
				}
			}


		}

		ArrayList<Region> currentRegionSet = new ArrayList<Region>();

		for (Region reg : rm.getRegions()) {
			if (reg instanceof PolyRegion) {
				PolyRegion pr = (PolyRegion) reg;
				if (regUtil.isInsidePolygon(p, pr.get2DPolygon(), pr.getMinY(), pr.getMaxY()) && (p.getRegiosWorld().getName() == reg.getWorld().getName())) {
					currentRegionSet.add(reg);
					if (pm.getInsideRegionLocation().containsKey(p.getName())) {
						pm.getInsideRegionLocation().put(p.getName(), p.getPoint());
					}
				}
			} else if (reg instanceof CuboidRegion) {
				if (regUtil.isInsideCuboid(p, ((CuboidRegion) reg).getL1(), ((CuboidRegion) reg).getL2()) && (p.getRegiosWorld().getName() == reg.getWorld().getName())) {
					currentRegionSet.add(reg);
					if (pm.getInsideRegionLocation().containsKey(p.getName())) {
						pm.getInsideRegionLocation().put(p.getName(), p.getPoint());
					}
				}
			}
		}

		if (currentRegionSet.isEmpty()) { // If player is in chunk range but not
			// inside region then cancel the
			// check.
			if (evt.getFrom().getBlockY() == evt.getTo().getBlockY()) { // To prevent people getting stuck if jumping into a region
				pm.getOutsideRegionLocation().put(p.getName(), p.getPoint());
			}
			return;
		}

		if (currentRegionSet.size() > 1) {
			r = srm.getCurrentRegion(currentRegionSet);
			pm.getRegionBinding().put(p.getName(), r);
		} else {
			r = currentRegionSet.get(0);
			pm.getRegionBinding().put(p.getName(), r);
		}

		if (r.isRegionFull(p)) {
			if (pm.getOutsideRegionLocation().containsKey(p.getName())) {
				p.teleport(pm.getOutsideRegionLocation().get(p.getName()));
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
				if (pm.getOutsideRegionLocation().containsKey(p.getName())) {
					p.teleport(pm.getOutsideRegionLocation().get(p.getName()));
				}
				if (isSendable(p, MSG.PREVENT_ENTRY)) {
					rm.sendPreventEntryMessage(p, r);
				}
				return;
			}
		}

		rm.sendWelcomeMessage(p, r);
		pm.getInsideRegionLocation().put(p.getName(), p.getPoint());

		// __________________________________
		// ^^^^ Messages & Entry control ^^^^
		// __________________________________

		if (r.getVelocityWarp() != 0) {
			RegiosPoint l = p.getPoint().getDirection().multiply(((r.getVelocityWarp()) * (0.3)) / 2);
			l.setY(0.1);
			p.setVelocity(l);
		}

	}

	//I don't think this is necessary atm. Keeping for sentimental value. :P
	//	@EventHandler(priority = EventPriority.HIGHEST)
	//	public void onPlayerTeleport(PlayerTeleportEvent evt) {
	//		Bukkit.getServer().getPluginManager().callEvent(new PlayerMoveEvent(RegiosConversions.getRegiosPlayer(evt.getPlayer()), evt.getFrom(), evt.getTo()));
	//	}
	//	
	//	@EventHandler(priority = EventPriority.HIGHEST)
	//	public void onPlayerPortal(PlayerPortalEvent evt) {
	//		Bukkit.getServer().getPluginManager().callEvent(new PlayerMoveEvent(RegiosConversions.getRegiosPlayer(evt.getPlayer()), evt.getFrom(), evt.getTo()));
	//	}

	public String getPoint(RegiosPoint l) {
		return l.getX() + " | " + l.getY() + " | " + l.getZ();
	}

}
