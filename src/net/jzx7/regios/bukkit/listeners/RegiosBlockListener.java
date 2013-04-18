package net.jzx7.regios.bukkit.listeners;

import java.util.ArrayList;

import net.jzx7.regios.Commands.CreationCommands;
import net.jzx7.regios.Data.ConfigurationData;
import net.jzx7.regios.Economy.EconomyCore;
import net.jzx7.regios.Permissions.PermissionsCore;
import net.jzx7.regios.Scheduler.LogRunner;
import net.jzx7.regios.bukkit.listeners.RegiosPlayerListener.MSG;
import net.jzx7.regios.messages.Message;
import net.jzx7.regios.messages.MsgFormat;
import net.jzx7.regios.regions.RegionManager;
import net.jzx7.regios.util.RegionUtil;
import net.jzx7.regios.util.RegiosConversions;
import net.jzx7.regiosapi.entity.RegiosPlayer;
import net.jzx7.regiosapi.inventory.RegiosItemStack;
import net.jzx7.regiosapi.location.RegiosPoint;
import net.jzx7.regiosapi.regions.CuboidRegion;
import net.jzx7.regiosapi.regions.PolyRegion;
import net.jzx7.regiosapi.regions.Region;
import net.jzx7.regiosapi.worlds.RegiosWorld;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockIgniteEvent.IgniteCause;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;

public class RegiosBlockListener implements Listener {
	private static final CreationCommands cc = new CreationCommands();
	private static final RegionUtil regUtil = new RegionUtil();
	private static final RegionManager rm = new RegionManager();

	ArrayList<Integer> waterBrokenBlockIDs = new ArrayList<Integer>() {
		private static final long serialVersionUID = 2203318228451900490L;
		{
			add(0);
			add(6);
			add(27);
			add(28);
			add(31);
			add(32);
			add(37);
			add(38);
			add(39);
			add(40);
			add(50);
			add(51);
			add(55);
			add(59);
			add(66);
			add(69);
			add(75);
			add(76);
			add(77);
			add(78);
			add(81);
			add(83);
			add(90);
			add(93);
			add(94);
			add(104);
			add(105);
			add(106);
			add(115);
		}};

		public void extinguish(Block b) {
			if (b.getType() == Material.FIRE) {
				b.setType(Material.AIR);
			}
		}

		public void extinguishAround(Block b) {
			if (b.getType() == Material.FIRE) {
				extinguish(b.getRelative(1, 0, 0));
				extinguish(b.getRelative(-1, 0, 0));
				extinguish(b.getRelative(0, 1, 0));
				extinguish(b.getRelative(0, -1, 0));
				extinguish(b.getRelative(0, 0, 1));
				extinguish(b.getRelative(0, 0, -1));
			}
		}

		public void forceBucketEvent(BlockPlaceEvent evt) {
			if (evt.getBlock().getType() == Material.LAVA || evt.getBlock().getType() == Material.STATIONARY_LAVA || evt.getBlock().getType() == Material.STATIONARY_WATER || evt.getBlock().getType() == Material.WATER) {
				RegiosPlayer p = RegiosConversions.getRegiosPlayer(evt.getPlayer());
				RegiosPoint l = RegiosConversions.getPoint(evt.getBlock().getLocation());
				ArrayList<Region> regions = rm.getRegions(l);
				if (!regions.isEmpty())
				{
					for (Region r : regions) {
						if (r.isProtected()) {
							if (!r.canBypassProtection(p)) {
								LogRunner.addLogMessage(r, LogRunner.getPrefix(r)
										+ (" Player '" + p.getName() + "' tried to place " + evt.getBlock().getType().toString() + " but was prevented."));
								rm.sendBuildMessage(p, r);
								evt.setCancelled(true);
								return;
							}
						}
					}
				}
			}
		}

		@EventHandler(priority = EventPriority.HIGHEST)
		public void onSignChange(SignChangeEvent evt) {
			if (!EconomyCore.isEconomySupportEnabled()) {
				return;
			} else {
				String[] lines = evt.getLines();
				if (!lines[0].equalsIgnoreCase("[Regios]")) {
					return;
				} else {
					RegiosPlayer p = RegiosConversions.getRegiosPlayer(evt.getPlayer());
					if (PermissionsCore.doesHaveNode(p, "regios.fun.sell")) {
						Region r = rm.getRegion(lines[1]);
						Block b = evt.getBlock();
						if (r == null) {
							if (RegiosPlayerListener.isSendable(p, MSG.ECONOMY)) {
								p.sendMessage(Message.REGIONDOESNTEXIST.getMessage() + MsgFormat.colourFormat("<BLUE>" + lines[1]));
							}
							b.setTypeId(0);
							p.addItem(new RegiosItemStack(323, 1));
							evt.setCancelled(true);
							return;
						} else {
							if (!r.canModify(p)) {
								if (RegiosPlayerListener.isSendable(p, MSG.ECONOMY)) {
									p.sendMessage(Message.PERMISSIONDENIED.getMessage());
									b.setTypeId(0);
									p.addItem(new RegiosItemStack(323, 1));
									evt.setCancelled(true);
									return;
								}
							}
							p.sendMessage("'" + lines[2] + "'");
							if (!lines[2].equalsIgnoreCase(""))
							{
								try {
									r.setSalePrice(Integer.parseInt(lines[2]));
									r.setForSale(true);
								} catch (NumberFormatException ex) {
									if (RegiosPlayerListener.isSendable(p, MSG.ECONOMY)) {
										p.sendMessage(MsgFormat.colourFormat(Message.ECONOMYINVALIDPRICE.getMessage() + "<BLUE>" + lines[2]));
									}
									b.setTypeId(0);
									p.addItem(new RegiosItemStack(323, 1));
									evt.setCancelled(true);
									return;
								}
							} else if (!r.isForSale()) {
								if (RegiosPlayerListener.isSendable(p, MSG.ECONOMY)) {
									p.sendMessage(MsgFormat.colourFormat(Message.ECONOMYNOTFORSALE.getMessage() + "<BLUE>" + lines[1]));
								}
								b.setTypeId(0);
								p.addItem(new RegiosItemStack(323, 1));
								evt.setCancelled(true);
								return;
							}

							evt.setLine(0, MsgFormat.colourFormat("<DGREEN>[Regios]"));
							evt.setLine(1, MsgFormat.colourFormat("<BLUE>" + r.getName()));
							evt.setLine(2, MsgFormat.colourFormat("<RED>" + String.valueOf(r.getSalePrice())));
							evt.setLine(3, "");
							p.sendMessage(MsgFormat.colourFormat(Message.ECONOMYSIGNCREATED.getMessage() + "<BLUE>" + r.getName()));
							p.sendMessage(MsgFormat.colourFormat(Message.ECONOMYPRICE.getMessage() + "<BLUE>" + r.getSalePrice()));
						}
					} else {
						PermissionsCore.sendInvalidPerms(RegiosConversions.getRegiosPlayer(evt.getPlayer()));
						evt.getBlock().setTypeId(0);
						RegiosConversions.getRegiosPlayer(evt.getPlayer()).addItem(new RegiosItemStack(323, 1));
						evt.setCancelled(true);
						return;
					}
				}
			}
		}

		@EventHandler(priority = EventPriority.HIGHEST)
		public void onBlockIgnite(BlockIgniteEvent evt) {
			RegiosWorld w = RegiosConversions.getRegiosWorld(evt.getBlock().getWorld());
			RegiosPoint l = RegiosConversions.getPoint(evt.getBlock().getLocation());

			Region r = rm.getRegion(l);

			if (r == null) {
				if (!w.getFireEnabled()) {
					Block b = evt.getBlock();
					extinguishAround(b);
					evt.setCancelled(true);
				}

				if (!w.getFireSpreadEnabled() && evt.getCause() == IgniteCause.SPREAD) {
					evt.setCancelled(true);
				}
				return;
			}
			if (r.isFireProtection()) {
				Block b = evt.getBlock();
				extinguishAround(b);
				evt.setCancelled(true);
				return;
			}

			if (!r.isFireSpread() && evt.getCause() == IgniteCause.SPREAD) {
				evt.setCancelled(true);
				return;
			}
		}

		@EventHandler(priority = EventPriority.HIGHEST)
		public void onBlockBurn(BlockBurnEvent evt)
		{
			RegiosPoint l = RegiosConversions.getPoint(evt.getBlock().getLocation());
			RegiosWorld w = l.getRegiosWorld();

			Region r = rm.getRegion(l);

			if (r == null)
			{
				if (!w.getFireEnabled()) {
					evt.setCancelled(true);
				}
				if (!w.getFireSpreadEnabled()) {
					evt.setCancelled(true);
				}
				return;
			}

			if (r.isFireProtection()) {
				Block b = evt.getBlock();
				extinguishAround(b);
				evt.setCancelled(true);
				return;
			}

			if (!r.isFireSpread()) {
				evt.setCancelled(true);
				return;
			}
		}

		@EventHandler(priority = EventPriority.HIGHEST)
		public void onBlockForm(BlockFormEvent evt) {

			RegiosPoint l = RegiosConversions.getPoint(evt.getBlock().getLocation());
			RegiosWorld w = l.getRegiosWorld();

			Region r = rm.getRegion(l);

			if (r == null)
			{
				if (!w.getBlockFormEnabled()) {
					evt.setCancelled(true);
				}
				return;
			}

			if (!r.isBlockForm()) {
				evt.setCancelled(true);
				return;
			}
		}

		@EventHandler(priority = EventPriority.HIGHEST)
		public void onBlockPlace(BlockPlaceEvent evt) {
			RegiosPlayer p = RegiosConversions.getRegiosPlayer(evt.getPlayer());
			Block b = evt.getBlock();
			RegiosPoint l = RegiosConversions.getPoint(b.getLocation());
			RegiosWorld w = RegiosConversions.getRegiosWorld(b.getWorld());

			Region r = rm.getRegion(l);

			if (r == null)
			{
				if (w.getProtection()) {
					if (!w.canBypassWorldChecks(p)) {
						p.sendMessage(Message.REGIONBUILDDENIED.getMessage());
						evt.setCancelled(true);
						return;
					}
				}
				this.forceBucketEvent(evt);
				return;
			}

			if (r.getItems().isEmpty() && r.is_protectionPlace()) {
				if (r.canBypassProtection(p)) {
					return;
				} else {
					evt.setCancelled(true);
					rm.sendBuildMessage(p, r);
					return;
				}
			}

			if (!r.getItems().isEmpty()) {
				if (r.canPlaceItem(p, b.getType().getId())) {
					return;
				} else {
					if (!r.canBypassProtection(p)) {
						evt.setCancelled(true);
						p.sendMessage(Message.REGIONITEMDENIED.getMessage());
						return;
					}
				}
			}

			if (r.is_protectionPlace()) {
				if (!r.canBypassProtection(p)) {
					evt.setCancelled(true);
					rm.sendBuildMessage(p, r);
					return;
				}
			}

		}

		@EventHandler(priority = EventPriority.HIGHEST)
		public void onBlockBreak(BlockBreakEvent evt) {
			RegiosPlayer p = RegiosConversions.getRegiosPlayer(evt.getPlayer());
			Block b = evt.getBlock();
			RegiosPoint l = RegiosConversions.getPoint(b.getLocation());
			RegiosWorld w = RegiosConversions.getRegiosWorld(b.getWorld());

			if ((b.getType() == Material.SIGN || b.getType() == Material.SIGN_POST || b.getTypeId() == 68)) {
				Sign sign = (Sign) b.getState();
				String[] lines = sign.getLines();
				if (sign.getLine(0).contains("[Regios]")) {
					Region reg = rm.getRegion(sign.getLine(1));
					if (reg != null) {
						if (!reg.canModify(p)) {
							p.sendMessage(Message.ECONOMYSIGNREMOVEALDENIED.getMessage());
							evt.setCancelled(true);
							int count = 0;
							for (String line : lines) {
								sign.setLine(count, line);
								count++;
							}
							sign.update();
							return;
						}
					} else {
						p.sendMessage(Message.REGIONDOESNTEXIST.getMessage());
						b.setTypeId(0);
						return;
					}
				}
			}

			if (cc.isSetting(p) || cc.isModding(p)) {
				if (p.getItemInHand().getId() == ConfigurationData.defaultSelectionTool) {
					evt.setCancelled(true);
					return;
				}
			}

			Region r = rm.getRegion(l);

			if (r == null)
			{
				if (w.getProtection()) {
					if (!w.canBypassWorldChecks(p)) {
						p.sendMessage(Message.REGIONBUILDDENIED.getMessage());
						evt.setCancelled(true);
						return;
					}
				}
				return;
			}

			if (r.is_protectionBreak()) {
				if (!r.canBypassProtection(p)) {
					evt.setCancelled(true);
					rm.sendBuildMessage(p, r);
					return;
				}
			}

		}

		//		@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
		//		public void onBlockFromTo(BlockFromToEvent evt)
		//		{
		//			Block blockFrom = evt.getBlock();
		//			Block blockTo = evt.getToBlock();
		//
		//			if(!waterBrokenBlockIDs.contains(blockTo.getTypeId()))
		//			{
		//				return;
		//			}
		//
		//			boolean isWater = blockFrom.getTypeId() == 8 || blockFrom.getTypeId() == 9;
		//			boolean isLava = blockFrom.getTypeId() == 10 || blockFrom.getTypeId() == 11;
		//
		//			if(!(isWater || isLava))
		//			{
		//				return;
		//			}
		//
		//			//TODO change when region priorities are implemented
		//
		//			Region fr = rm.getRegion(blockFrom.getLocation());
		//			Region tr = rm.getRegion(blockTo.getLocation());
		//
		//			if(fr != null)
		//			{
		//				if(tr != null)
		//				{
		//					if(fr.getName().equalsIgnoreCase(tr.getName()))
		//					{
		//						return;
		//					} else {
		//						if (tr.isProtected())
		//						{
		//							evt.setCancelled(true);
		//							return;
		//						}
		//					}
		//				}
		//			}else if(tr != null)
		//			{
		//				if (tr.isProtected())
		//				{
		//					evt.setCancelled(true);
		//					return;
		//				}
		//			}
		//		}

		@EventHandler(priority = EventPriority.HIGHEST)
		public void onPistonExtend(BlockPistonExtendEvent evt)
		{
			for(Block b : evt.getBlocks())
			{
				RegiosPoint l = RegiosConversions.getPoint(b.getLocation());
				Region r = rm.getRegion(l);
				if (r != null)
				{
					if (r instanceof CuboidRegion) {
						if(regUtil.isInsideCuboid(RegiosConversions.getPoint(evt.getBlock().getLocation()), ((CuboidRegion) r).getL1(), ((CuboidRegion) r).getL2()) && (b.getWorld().getName() == r.getWorld().getName()))
						{
							return;
						}
					} else if (r instanceof PolyRegion) {
						PolyRegion py = (PolyRegion) r;
						if(regUtil.isInsidePolygon(RegiosConversions.getPoint(evt.getBlock().getLocation()), py.get2DPolygon(), py.getMinY(), py.getMaxY()) && (b.getWorld().getName() == r.getWorld().getName())) {
							return;
						}
					}
					if(r.isProtected() || r.is_protectionBreak() || r.is_protectionPlace())
					{
						evt.setCancelled(true);
					}
				}
			}
		}

		@EventHandler(priority = EventPriority.HIGHEST)
		public void onPistonRetract(BlockPistonRetractEvent evt)
		{
			if(evt.isSticky())
			{
				RegiosPoint l = RegiosConversions.getPoint(evt.getRetractLocation());
				Block rb = evt.getRetractLocation().getBlock();
				
				if (rb.getTypeId() != 0)
				{
					if(rb.getPistonMoveReaction().equals(PistonMoveReaction.MOVE) || rb.getPistonMoveReaction().equals(PistonMoveReaction.BREAK))
					{
						Region r = rm.getRegion(l);
						if (r != null)
						{
							if (r instanceof CuboidRegion) {
								if(regUtil.isInsideCuboid(RegiosConversions.getPoint(evt.getBlock().getLocation()), ((CuboidRegion) r).getL1(), ((CuboidRegion) r).getL2()) && (evt.getBlock().getWorld().getName() == r.getWorld().getName()))
								{
									return;
								}
							} else if (r instanceof PolyRegion) {
								PolyRegion py = (PolyRegion) r;
								if(regUtil.isInsidePolygon(RegiosConversions.getPoint(evt.getBlock().getLocation()), py.get2DPolygon(), py.getMinY(), py.getMaxY()) && (evt.getBlock().getWorld().getName() == r.getWorld().getName())) {
									return;
								}
							}
							if(r.isProtected() || r.is_protectionBreak() || r.is_protectionPlace())
							{
								evt.setCancelled(true);
							}
						}
					}
				}
			}
		}
}
