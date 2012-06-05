package couk.Adamki11s.Regios.Listeners;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.painting.PaintingBreakByEntityEvent;
import org.bukkit.event.painting.PaintingBreakEvent;
import org.bukkit.event.painting.PaintingPlaceEvent;

import couk.Adamki11s.Extras.Regions.ExtrasRegions;
import couk.Adamki11s.Regios.Regions.GlobalRegionManager;
import couk.Adamki11s.Regios.Regions.GlobalWorldSetting;
import couk.Adamki11s.Regios.Regions.Region;
import couk.Adamki11s.Regios.Scheduler.HealthRegeneration;
import couk.Adamki11s.Regios.Scheduler.LogRunner;

public class RegiosEntityListener implements Listener {

	private static final ExtrasRegions extReg = new ExtrasRegions();

	private boolean isPeacefulMob(EntityType ce)
	{
		if (ce == EntityType.CHICKEN || 
				ce == EntityType.COW || 
				ce == EntityType.PIG || 
				ce == EntityType.SHEEP || 
				ce == EntityType.SQUID || 
				ce == EntityType.SNOWMAN || 
				ce == EntityType.VILLAGER || 
				ce == EntityType.OCELOT || 
				ce == EntityType.IRON_GOLEM || 
				ce == EntityType.WOLF) {
			return true;
		} else {
			return false;
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityBlockChange(EntityChangeBlockEvent evt)
	{
		Location l = evt.getBlock().getLocation();
		World w = evt.getBlock().getWorld();
		
		if(evt.getEntity() instanceof Enderman)
		{
			GlobalWorldSetting gws = GlobalRegionManager.getGlobalWorldSetting(w);
			Region r = GlobalRegionManager.getRegion(l);
			
			if(r == null)
			{
				if (gws != null) {
					if (gws.blockEndermanMod) {
						evt.setCancelled(true);
					}
				}
				return;
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityExplode(EntityExplodeEvent evt)
	{
		if ((evt.getEntity() instanceof EnderDragon))
			if(GlobalRegionManager.getGlobalWorldSetting(evt.getLocation().getWorld()).dragonProtect)
			{
				evt.setCancelled(true);
			}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onCreatureSpawn(CreatureSpawnEvent evt) {

		Location l = evt.getEntity().getLocation();
		World w = l.getWorld();
		EntityType ce = evt.getEntityType();

		Region r = GlobalRegionManager.getRegion(l);

		if (r == null)
		{
			if (GlobalRegionManager.getGlobalWorldSetting(w) != null) {
				if (!GlobalRegionManager.getGlobalWorldSetting(w).canCreatureSpawn(ce)) {
					evt.setCancelled(true);
				}
			}
			return;
		}

		if (!r.canMobsSpawn()) {
			if (isPeacefulMob(ce)) {
				LogRunner.addLogMessage(r, LogRunner.getPrefix(r) + (" Mob '" + ce.getName() + "' tried to spawn but was prevented."));
				evt.setCancelled(true);
				return;
			}
		}

		if (!r.canMonstersSpawn()) {
			if (!isPeacefulMob(ce)) {
				LogRunner.addLogMessage(r, LogRunner.getPrefix(r) + (" Monster '" + ce.getName() + "' tried to spawn but was prevented."));
				evt.setCancelled(true);
				return;
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityDeath(EntityDeathEvent evt) {
		Entity e = evt.getEntity();
		if (e instanceof Player) {
			Player p = (Player) e;
			if (HealthRegeneration.isRegenerator(p)) {
				HealthRegeneration.removeRegenerator(p);
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPaintingBreak(PaintingBreakEvent evt) {

		Player cause;

		if (!(evt instanceof PaintingBreakByEntityEvent)) {
			return;
		}

		PaintingBreakByEntityEvent event = (PaintingBreakByEntityEvent) evt;
		if (!(event.getRemover() instanceof Player)) {
			return;
		}

		cause = (Player) event.getRemover();

		Location l = evt.getPainting().getLocation();
		World w = l.getWorld();

		GlobalWorldSetting gws = GlobalRegionManager.getGlobalWorldSetting(w);

		Region r = GlobalRegionManager.getRegion(l);

		if(r == null)
		{
			if (gws != null) {
				if (gws.invert_protection) {
					evt.setCancelled(true);
				}
			}
			return;
		}

		if (r.is_protectionBreak()) {
			if (!r.canBypassProtection(cause)) {
				LogRunner.addLogMessage(r, LogRunner.getPrefix(r) + (" Painting break was prevented."));
				r.sendBuildMessage(cause);
				evt.setCancelled(true);
				return;
			}
		}

	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPaintingPlace(PaintingPlaceEvent evt) {

		Player cause = evt.getPlayer();

		Location l = evt.getPainting().getLocation();
		World w = l.getWorld();

		GlobalWorldSetting gws = GlobalRegionManager.getGlobalWorldSetting(w);

		Region r = GlobalRegionManager.getRegion(l);

		if(r == null)
		{
			if (gws != null) {
				if (gws.invert_protection) {
					evt.setCancelled(true);
				}
			}
			return;
		}

		if (r.is_protectionPlace()) {
			if (!r.canBypassProtection(cause)) {
				LogRunner.addLogMessage(r, LogRunner.getPrefix(r) + (" Painting place was prevented."));
				r.sendBuildMessage(cause);
				evt.setCancelled(true);
				return;
			}
		}

	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onExplosionPrime(ExplosionPrimeEvent evt) {

		Location l = evt.getEntity().getLocation();
		World w = l.getWorld();
		Chunk c = w.getChunkAt(l);

		if (evt.getEntity() instanceof Creeper) {
			if (GlobalRegionManager.getGlobalWorldSetting(w) != null) {
				if (!GlobalRegionManager.getGlobalWorldSetting(w).creeperExplodes) {
					evt.setCancelled(true);
					evt.setRadius(0);
					return;
				}
			}
		}

		ArrayList<Region> regionSet = new ArrayList<Region>();

		for (Region region : GlobalRegionManager.getRegions()) {
			for (Chunk chunk : region.getChunkGrid().getChunks()) {
				if (chunk.getWorld() == w) {
					if (extReg.areChunksEqual(chunk, c)) {
						if (!regionSet.contains(region)) {
							regionSet.add(region);
						}
					}
				}
			}
		}

		if (regionSet.isEmpty()) {
			return;
		}

		ArrayList<Region> currentRegionSet = new ArrayList<Region>();

		for (Region reg : regionSet) {
			Location rl1 = reg.getL1(), rl2 = reg.getL2();
			if (rl1.getX() > rl2.getX()) {
				rl2.subtract(6, 0, 0);
				rl1.add(6, 0, 0);
			} else {
				rl2.add(6, 0, 0);
				rl1.subtract(6, 0, 0);
			}
			if (rl1.getZ() > rl2.getZ()) {
				rl2.subtract(0, 0, 6);
				rl1.add(0, 0, 6);
			} else {
				rl2.add(0, 0, 6);
				rl1.subtract(0, 0, 6);
			}
			if (rl1.getY() > rl2.getY()) {
				rl2.subtract(0, 10, 0);
				rl1.add(0, 10, 0);
			} else {
				rl2.add(0, 10, 0);
				rl1.subtract(0, 10, 0);
			}
			if (extReg.isInsideCuboid(l, rl1, rl2)) {
				currentRegionSet.add(reg);
			}
		}

		if (currentRegionSet.isEmpty()) { // If player is in chunk range but not
			// inside region then cancel the
			// check.
			return;
		} else {
			for (Region r : currentRegionSet) {
				if (r.isProtected() || !r.isTNTEnabled()) {
					LogRunner.addLogMessage(r, LogRunner.getPrefix(r) + (" Entity explosion was prevented."));
					evt.setCancelled(true);
					evt.setRadius(0);
					return;
				}
			}
		}

	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityDamage(EntityDamageEvent evt) {

		if (!(evt.getEntity() instanceof Player)) {
			return;
		}

		Location l = evt.getEntity().getLocation();
		World w = l.getWorld();

		GlobalWorldSetting gws = GlobalRegionManager.getGlobalWorldSetting(w);

		Region r = GlobalRegionManager.getRegion(l);

		if (r == null)
		{
			if (gws != null) {
				if (!gws.invert_pvp && gws.overridingPvp) {
					evt.setCancelled(true);
				}
			}
			return;
		}

		if (!r.isHealthEnabled()) {
			evt.setCancelled(true);
			evt.setDamage(0);
			return;
		}

		if (!r.isPvp()) {
			if (evt instanceof EntityDamageByEntityEvent) {
				EntityDamageByEntityEvent edevt = (EntityDamageByEntityEvent) evt;
				Entity damager;
				if (edevt.getDamager() instanceof Player && edevt.getEntity() instanceof Player) {
					damager = (Player) edevt.getDamager();
					LogRunner.addLogMessage(r, LogRunner.getPrefix(r)
							+ (" Player '" + ((Player) damager).getName() + "' tried to attack '" + ((Player) evt.getEntity()).getName() + " but was prevented."));
					((Player) damager).sendMessage(ChatColor.RED + "[Regios] You cannot fight within regions in this world!");
					evt.setCancelled(true);
					evt.setDamage(0);
					return;
				} else if (edevt.getDamager().getType() == EntityType.ARROW && edevt.getEntity() instanceof Player) { //Check to see if the player was shot by an arrow.
					Projectile arrow = (Arrow) edevt.getDamager();
					damager = arrow.getShooter(); //get the arrows shooter
					if(damager.getType() == EntityType.PLAYER) { //if shot by a player, cancel the event
						LogRunner.addLogMessage(r, LogRunner.getPrefix(r)
								+ (" Player '" + ((Player) damager).getName() + "' tried to attack '" + ((Player) evt.getEntity()).getName() + " but was prevented."));
						((Player) damager).sendMessage(ChatColor.RED + "[Regios] You cannot fight within regions in this world!");
						evt.setCancelled(true);
						evt.setDamage(0);
					}
					return;
				} else if (edevt.getDamager().getType() == EntityType.SPLASH_POTION && edevt.getEntity() instanceof Player) { //Check if player was hit by a potion
					Projectile potion = (ThrownPotion) edevt.getDamager();
					damager = potion.getShooter(); //get the potion's thrower
					if(damager.getType() == EntityType.PLAYER) { //if it was thrown by a player, cancel the event
						LogRunner.addLogMessage(r, LogRunner.getPrefix(r)
								+ (" Player '" + ((Player) damager).getName() + "' tried to attack '" + ((Player) evt.getEntity()).getName() + " but was prevented."));
						((Player) damager).sendMessage(ChatColor.RED + "[Regios] You cannot fight within regions in this world!");
						evt.setCancelled(true);
						evt.setDamage(0);
					}
					return;
				} else {
					return;
				}
			}
		}
	}
}
