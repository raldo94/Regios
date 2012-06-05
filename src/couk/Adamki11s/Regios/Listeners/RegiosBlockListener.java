package couk.Adamki11s.Regios.Listeners;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
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
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockIgniteEvent.IgniteCause;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.inventory.ItemStack;

import couk.Adamki11s.Regios.Commands.CreationCommands;
import couk.Adamki11s.Regios.Data.ConfigurationData;
import couk.Adamki11s.Regios.Economy.EconomyCore;
import couk.Adamki11s.Regios.Listeners.RegiosPlayerListener.MSG;
import couk.Adamki11s.Regios.Permissions.PermissionsCore;
import couk.Adamki11s.Regios.Regions.GlobalRegionManager;
import couk.Adamki11s.Regios.Regions.GlobalWorldSetting;
import couk.Adamki11s.Regios.Regions.Region;
import couk.Adamki11s.Regios.Scheduler.LogRunner;

public class RegiosBlockListener implements Listener {
	final CreationCommands cc = new CreationCommands();

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
			ArrayList<Region> regions = GlobalRegionManager.getRegions(l);
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
				if (PermissionsCore.doesHaveNode(evt.getPlayer(), "regios.fun.sell")) {
					Region r = GlobalRegionManager.getRegion(lines[1]);
					Player p = evt.getPlayer();
					Block b = evt.getBlock();
					if (r == null) {
						if (RegiosPlayerListener.isSendable(evt.getPlayer(), MSG.ECONOMY)) {
							p.sendMessage(ChatColor.RED + "[Regios] The region " + ChatColor.BLUE + lines[1] + ChatColor.RED + " does not exist.");
						}
						b.setTypeId(0);
						p.getInventory().addItem(new ItemStack(323, 1));
						evt.setCancelled(true);
						return;
					} else {
						if (!PermissionsCore.canModify(r, evt.getPlayer())) {
							if (RegiosPlayerListener.isSendable(evt.getPlayer(), MSG.ECONOMY)) {
								p.sendMessage(ChatColor.RED + "[Regios] You don't have permissions to sell this region!");
								b.setTypeId(0);
								p.getInventory().addItem(new ItemStack(323, 1));
								evt.setCancelled(true);
								return;
							}
						}
						if (lines[2] != null)
						{
							try {
								r.setSalePrice(Integer.parseInt(lines[2]));
								r.setForSale(true);
							} catch (NumberFormatException ex) {
								if (RegiosPlayerListener.isSendable(evt.getPlayer(), MSG.ECONOMY)) {
									p.sendMessage(ChatColor.RED + "[Regios] Invalid price " + ChatColor.BLUE + lines[2] + ChatColor.RED + " entered!");
								}
								b.setTypeId(0);
								p.getInventory().addItem(new ItemStack(323, 1));
								evt.setCancelled(true);
								return;
							}
						} else if (!r.isForSale()) {
							if (RegiosPlayerListener.isSendable(evt.getPlayer(), MSG.ECONOMY)) {
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
						evt.getPlayer().sendMessage(ChatColor.GREEN + "[Regios] Sale sign created for region : " + ChatColor.BLUE + r.getName());
						evt.getPlayer().sendMessage(ChatColor.GREEN + "[Regios] Price : " + ChatColor.BLUE + r.getSalePrice());
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
		World w = evt.getBlock().getWorld();
		Location l = evt.getBlock().getLocation();
		if (GlobalRegionManager.getGlobalWorldSetting(w) != null) {
			if (!GlobalRegionManager.getGlobalWorldSetting(w).fireEnabled) {
				Block b = evt.getBlock();
				extinguishAround(b);
				evt.setCancelled(true);
				return;
			}
			if (!GlobalRegionManager.getGlobalWorldSetting(w).fireSpreadEnabled && evt.getCause() == IgniteCause.SPREAD) {
				evt.setCancelled(true);
				return;
			}
		}

		Region r = GlobalRegionManager.getRegion(l);

		if (r != null) {
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

	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockBurn(BlockBurnEvent evt)
	{
		Location l = evt.getBlock().getLocation();
		World w = l.getWorld();

		GlobalWorldSetting gws = GlobalRegionManager.getGlobalWorldSetting(w);

		Region r = GlobalRegionManager.getRegion(l);

		if (r == null)
		{
			if (gws != null) {
				if (!gws.fireEnabled) {
					evt.setCancelled(true);
				}
				if (!gws.fireSpreadEnabled) {
					evt.setCancelled(true);
				}
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
		World w = l.getWorld();

		GlobalWorldSetting gws = GlobalRegionManager.getGlobalWorldSetting(w);

		Region r = GlobalRegionManager.getRegion(l);

		if (r == null)
		{
			if (gws != null) {
				if (!gws.blockForm_enabled) {
					evt.setCancelled(true);
				}
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
		World w = b.getWorld();

		GlobalWorldSetting gws = GlobalRegionManager.getGlobalWorldSetting(w);

		Region r = GlobalRegionManager.getRegion(l);

		if (r == null)
		{
			if (gws != null) {
				if (gws.invert_protection) {
					if (!gws.canBypassWorldChecks(p)) {
						p.sendMessage(ChatColor.RED + "[Regios] You are not permitted to build in this area!");
						evt.setCancelled(true);
						return;
					}
				}
			}
			this.forceBucketEvent(evt);
			return;
		}

		if (r.getItems().isEmpty() && r.is_protectionPlace()) {
			if (r.canBypassProtection(p, r)) {
				return;
			} else {
				evt.setCancelled(true);
				r.sendBuildMessage(p);
				return;
			}
		}

		if (!r.getItems().isEmpty()) {
			if (r.canItemBePlaced(p, b.getType(), r)) {
				return;
			} else {
				if (!r.canBypassProtection(p, r)) {
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
		World w = b.getWorld();

		if ((b.getType() == Material.SIGN || b.getType() == Material.SIGN_POST || b.getTypeId() == 68)) {
			Sign sign = (Sign) b.getState();
			String[] lines = sign.getLines();
			if (sign.getLine(0).contains("[Regios]")) {
				Region reg = GlobalRegionManager.getRegion(sign.getLine(1).substring(0, sign.getLine(1).length()));
				if (reg != null) {
					if (!PermissionsCore.canModify(reg, p)) {
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

		GlobalWorldSetting gws = GlobalRegionManager.getGlobalWorldSetting(w);

		Region r = GlobalRegionManager.getRegion(l);

		if (r == null)
		{
			if (gws != null) {
				if (gws.invert_protection) {
					if (!gws.canBypassWorldChecks(p)) {
						p.sendMessage(ChatColor.RED + "[Regios] You are not permitted to build in this area!");
						evt.setCancelled(true);
						return;
					}
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

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockFromTo(BlockFromToEvent evt)
	{
		Block blockFrom = evt.getBlock();

		boolean isWater = blockFrom.getTypeId() == 8 || blockFrom.getTypeId() == 9;
		boolean isLava = blockFrom.getTypeId() == 10 || blockFrom.getTypeId() == 11;

		if(!isWater && !isLava)
		{
			return;
		}

		//TODO change when region priorities are implemented

		Region fr = GlobalRegionManager.getRegion(blockFrom.getLocation());
		Region tr = GlobalRegionManager.getRegion(evt.getToBlock().getLocation());

		if(fr != null)
		{
			if(tr != null)
			{
				if(fr.getName().equalsIgnoreCase(tr.getName()))
				{
					return;
				} else {
					if (tr.isProtected())
					{
						evt.setCancelled(true);
						return;
					}
				}
			}
		}

		if(tr != null)
		{
			if (tr.isProtected())
			{
				evt.setCancelled(true);
				return;
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPistonExtend(BlockPistonExtendEvent evt)
	{
		for(Block b : evt.getBlocks())
		{
			Region r = GlobalRegionManager.getRegion(b.getLocation());
			if (r != null)
			{
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
					Region r = GlobalRegionManager.getRegion(l);
					if (r != null)
					{
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
