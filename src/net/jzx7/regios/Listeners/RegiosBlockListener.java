package net.jzx7.regios.Listeners;

import java.util.ArrayList;

import net.jzx7.regios.Commands.CreationCommands;
import net.jzx7.regios.Data.ConfigurationData;
import net.jzx7.regios.Economy.EconomyCore;
import net.jzx7.regios.Listeners.RegiosPlayerListener.MSG;
import net.jzx7.regios.Permissions.PermissionsCore;
import net.jzx7.regios.Scheduler.LogRunner;
import net.jzx7.regios.regions.RegionManager;
import net.jzx7.regios.worlds.WorldManager;
import net.jzx7.regiosapi.regions.CuboidRegion;
import net.jzx7.regiosapi.regions.PolyRegion;
import net.jzx7.regiosapi.regions.Region;
import net.jzx7.regiosapi.worlds.RegiosWorld;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockFormEvent;
//import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockIgniteEvent.IgniteCause;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.inventory.ItemStack;

import couk.Adamki11s.Extras.Regions.ExtrasRegions;

public class RegiosBlockListener implements Listener {
	private static final CreationCommands cc = new CreationCommands();
	private static final ExtrasRegions extReg = new ExtrasRegions();
	private static final RegionManager rm = new RegionManager();
	private static final WorldManager wm = new WorldManager();

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
				Player p = evt.getPlayer();
				Location l = evt.getBlock().getLocation();
				ArrayList<Region> regions = rm.getRegions(l);
				if (!regions.isEmpty())
				{
					for (Region r : regions) {
						if (r.isProtected()) {
							if (!r.canBypassProtection(p)) {
								LogRunner.addLogMessage(r, LogRunner.getPrefix(r)
										+ (" Player '" + p.getName() + "' tried to place " + evt.getBlock().getType().toString() + " but was prevented."));
								r.sendBuildMessage(p);
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
					Player p = evt.getPlayer();
					if (PermissionsCore.doesHaveNode(p, "regios.fun.sell")) {
						Region r = rm.getRegion(lines[1]);
						Block b = evt.getBlock();
						if (r == null) {
							if (RegiosPlayerListener.isSendable(p, MSG.ECONOMY)) {
								p.sendMessage(ChatColor.RED + "[Regios] The region " + ChatColor.BLUE + lines[1] + ChatColor.RED + " does not exist.");
							}
							b.setTypeId(0);
							p.getInventory().addItem(new ItemStack(323, 1));
							evt.setCancelled(true);
							return;
						} else {
							if (!r.canModify(p)) {
								if (RegiosPlayerListener.isSendable(p, MSG.ECONOMY)) {
									p.sendMessage(ChatColor.RED + "[Regios] You don't have permissions to sell this region!");
									b.setTypeId(0);
									p.getInventory().addItem(new ItemStack(323, 1));
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
										p.sendMessage(ChatColor.RED + "[Regios] Invalid price " + ChatColor.BLUE + lines[2] + ChatColor.RED + " entered!");
									}
									b.setTypeId(0);
									p.getInventory().addItem(new ItemStack(323, 1));
									evt.setCancelled(true);
									return;
								}
							} else if (!r.isForSale()) {
								if (RegiosPlayerListener.isSendable(p, MSG.ECONOMY)) {
									p.sendMessage(ChatColor.RED + "[Regios] The region " + ChatColor.BLUE + lines[1] + ChatColor.RED + " is not for sale!");
								}
								b.setTypeId(0);
								p.getInventory().addItem(new ItemStack(323, 1));
								evt.setCancelled(true);
								return;
							}

							evt.setLine(0, ChatColor.GREEN + "[Regios]");
							evt.setLine(1, ChatColor.BLUE + r.getName());
							evt.setLine(2, ChatColor.RED + String.valueOf(r.getSalePrice()));
							evt.setLine(3, ChatColor.GREEN + "[Regios]");
							p.sendMessage(ChatColor.GREEN + "[Regios] Sale sign created for region : " + ChatColor.BLUE + r.getName());
							p.sendMessage(ChatColor.GREEN + "[Regios] Price : " + ChatColor.BLUE + r.getSalePrice());
						}
					} else {
						PermissionsCore.sendInvalidPerms(evt.getPlayer());
						evt.getBlock().setTypeId(0);
						evt.getPlayer().getInventory().addItem(new ItemStack(323, 1));
						evt.setCancelled(true);
						return;
					}
				}
			}
		}

		@EventHandler(priority = EventPriority.HIGHEST)
		public void onBlockIgnite(BlockIgniteEvent evt) {
			RegiosWorld w = wm.getRegiosWorld(evt.getBlock().getWorld());
			Location l = evt.getBlock().getLocation();

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
			Location l = evt.getBlock().getLocation();
			RegiosWorld w = wm.getRegiosWorld(l.getWorld());

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

			Location l = evt.getBlock().getLocation();
			RegiosWorld w = wm.getRegiosWorld(l.getWorld());

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
			Player p = evt.getPlayer();
			Block b = evt.getBlock();
			Location l = b.getLocation();
			RegiosWorld w = wm.getRegiosWorld(b.getWorld());

			Region r = rm.getRegion(l);

			if (r == null)
			{
				if (w.getProtection()) {
					if (!w.canBypassWorldChecks(p)) {
						p.sendMessage(ChatColor.RED + "[Regios] You are not permitted to build in this area!");
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
					r.sendBuildMessage(p);
					return;
				}
			}

			if (!r.getItems().isEmpty()) {
				if (r.canPlaceItem(p, b.getType())) {
					return;
				} else {
					if (!r.canBypassProtection(p)) {
						evt.setCancelled(true);
						p.sendMessage(ChatColor.RED + "[Regios] You cannot place this item in this region!");
						return;
					}
				}
			}

			if (r.is_protectionPlace()) {
				if (!r.canBypassProtection(p)) {
					evt.setCancelled(true);
					r.sendBuildMessage(p);
					return;
				}
			}

		}

		@EventHandler(priority = EventPriority.HIGHEST)
		public void onBlockBreak(BlockBreakEvent evt) {
			Player p = evt.getPlayer();
			Block b = evt.getBlock();
			Location l = b.getLocation();
			RegiosWorld w = wm.getRegiosWorld(b.getWorld());

			if ((b.getType() == Material.SIGN || b.getType() == Material.SIGN_POST || b.getTypeId() == 68)) {
				Sign sign = (Sign) b.getState();
				String[] lines = sign.getLines();
				if (sign.getLine(0).contains("[Regios]")) {
					Region reg = rm.getRegion(sign.getLine(1));
					if (reg != null) {
						if (!reg.canModify(p)) {
							p.sendMessage(ChatColor.RED + "[Regios] You cannot destroy this sign!");
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
						p.sendMessage(ChatColor.RED + "[Regios] The region relating to this sign no longer exists!");
						b.setTypeId(0);
						return;
					}
				}
			}

			if (cc.isSetting(p) || cc.isModding(p)) {
				if (p.getItemInHand().getType() == ConfigurationData.defaultSelectionTool) {
					evt.setCancelled(true);
					return;
				}
			}

			Region r = rm.getRegion(l);

			if (r == null)
			{
				if (w.getProtection()) {
					if (!w.canBypassWorldChecks(p)) {
						p.sendMessage(ChatColor.RED + "[Regios] You are not permitted to build in this area!");
						evt.setCancelled(true);
						return;
					}
				}
				return;
			}

			if (r.is_protectionBreak()) {
				if (!r.canBypassProtection(p)) {
					evt.setCancelled(true);
					r.sendBuildMessage(p);
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
				Region r = rm.getRegion(b.getLocation());
				if (r != null)
				{
					if (r instanceof CuboidRegion) {
						if(extReg.isInsideCuboid(evt.getBlock().getLocation(), ((CuboidRegion) r).getL1(), ((CuboidRegion) r).getL2()))
						{
							return;
						}
					} else if (r instanceof PolyRegion) {
						PolyRegion py = (PolyRegion) r;
						extReg.isInsidePolygon(evt.getBlock().getLocation(), py.get2DPolygon(), py.getMinY(), py.getMaxY());
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
				Location l = evt.getRetractLocation();
				if (l.getBlock().getTypeId() != 0)
				{
					if(l.getBlock().getPistonMoveReaction().equals(PistonMoveReaction.MOVE) || l.getBlock().getPistonMoveReaction().equals(PistonMoveReaction.BREAK))
					{
						Region r = rm.getRegion(l);
						if (r != null)
						{
							if (r instanceof CuboidRegion) {
								if(extReg.isInsideCuboid(evt.getBlock().getLocation(), ((CuboidRegion) r).getL1(), ((CuboidRegion) r).getL2()))
								{
									return;
								}
							} else if (r instanceof PolyRegion) {
								PolyRegion py = (PolyRegion) r;
								extReg.isInsidePolygon(evt.getBlock().getLocation(), py.get2DPolygon(), py.getMinY(), py.getMaxY());
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
