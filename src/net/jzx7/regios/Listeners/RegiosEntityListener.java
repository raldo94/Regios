package net.jzx7.regios.Listeners;

import java.util.ArrayList;

import net.jzx7.regios.Scheduler.HealthRegeneration;
import net.jzx7.regios.Scheduler.LogRunner;
import net.jzx7.regios.regions.RegionManager;
import net.jzx7.regios.worlds.WorldManager;
import net.jzx7.regiosapi.regions.CuboidRegion;
import net.jzx7.regiosapi.regions.PolyRegion;
import net.jzx7.regiosapi.regions.Region;
import net.jzx7.regiosapi.worlds.RegiosWorld;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;
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
import org.bukkit.event.entity.EntityCreatePortalEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;

import couk.Adamki11s.Extras.Regions.ExtrasRegions;

public class RegiosEntityListener implements Listener {

	private static final ExtrasRegions extReg = new ExtrasRegions();
	private static final RegionManager rm = new RegionManager();
	private static final WorldManager wm = new WorldManager();

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
				ce == EntityType.WOLF ||
				ce == EntityType.BAT) {
			return true;
		} else {
			return false;
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityBlockChange(EntityChangeBlockEvent evt) {
		Location l = evt.getBlock().getLocation();
		RegiosWorld w = wm.getRegiosWorld(evt.getBlock().getWorld());

		if(evt.getEntity() instanceof Enderman) {
			Region r = rm.getRegion(l);

			if(r == null) {
				if (w.getEndermanProtectionEnabled()) {
					evt.setCancelled(true);
				}
				return;
			}

			if(r.isBlockEndermanMod()) {
				evt.setCancelled(true);
				return;
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityCreatePortal(EntityCreatePortalEvent evt) {
		RegiosWorld w = wm.getRegiosWorld(evt.getEntity().getWorld());

		if ((evt.getEntity() instanceof EnderDragon)) {
			if (!w.getEnderDragonCreatesPortal()) {
				evt.setCancelled(true);
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityExplode(EntityExplodeEvent evt) {
		Location l = evt.getLocation();
		RegiosWorld w = wm.getRegiosWorld(l.getWorld());

		if ((evt.getEntity() instanceof EnderDragon)) {
			if(w.getDragonProtectionEnabled()) {
				evt.setCancelled(true);
				return;
			}
		}

		Region r = rm.getRegion(l);

		if (r == null) {
			if(!w.getExplosionsEnabled()) {
				evt.setCancelled(true);
				evt.setYield(0);
				return;
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onCreatureSpawn(CreatureSpawnEvent evt) {

		Location l = evt.getEntity().getLocation();
		RegiosWorld w = wm.getRegiosWorld(l.getWorld());
		EntityType ce = evt.getEntityType();

		Region r = rm.getRegion(l);

		if (r == null) {
			if (!w.canCreatureSpawn(ce)) {
				evt.setCancelled(true);
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
	public void onHangingBreak(HangingBreakEvent evt) {

		Player cause;

		if (!(evt instanceof HangingBreakByEntityEvent)) {
			return;
		}

		HangingBreakByEntityEvent event = (HangingBreakByEntityEvent) evt;
		if (!(event.getRemover() instanceof Player)) {
			return;
		}

		cause = (Player) event.getRemover();

		Location l = evt.getEntity().getLocation();
		RegiosWorld w = wm.getRegiosWorld(l.getWorld());

		Region r = rm.getRegion(l);

		if(r == null)
		{
			if (w.getProtection()) {
				if (!w.canBypassWorldChecks(cause)) {
					cause.sendMessage(ChatColor.RED + "[Regios] You are not permitted to build in this area!");
					evt.setCancelled(true);
					return;
				}
			}
			return;
		}

		if (r.is_protectionBreak()) {
			if (!r.canBypassProtection(cause)) {
				LogRunner.addLogMessage(r, LogRunner.getPrefix(r) + (" Hanging break was prevented."));
				r.sendBuildMessage(cause);
				evt.setCancelled(true);
				return;
			}
		}

	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onHangingPlace(HangingPlaceEvent evt) {

		Player cause = evt.getPlayer();

		Location l = evt.getEntity().getLocation();
		RegiosWorld w = wm.getRegiosWorld(l.getWorld());

		Region r = rm.getRegion(l);

		if(r == null)
		{
			if (w.getProtection()) {
				if (!w.canBypassWorldChecks(cause)) {
					cause.sendMessage(ChatColor.RED + "[Regios] You are not permitted to build in this area!");
					evt.setCancelled(true);
					return;
				}
			}
			return;
		}

		if (r.is_protectionPlace()) {
			if (!r.canBypassProtection(cause)) {
				LogRunner.addLogMessage(r, LogRunner.getPrefix(r) + (" Hanging place was prevented."));
				r.sendBuildMessage(cause);
				evt.setCancelled(true);
				return;
			}
		}

	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onExplosionPrime(ExplosionPrimeEvent evt) {

		Location l = evt.getEntity().getLocation();

		ArrayList<Region> currentRegionSet = new ArrayList<Region>();

		for (Region reg : rm.getRegions()) {
			if (reg instanceof PolyRegion) {
				PolyRegion pr = (PolyRegion) reg;
				Outerloop:
					for (int x = -6; x <= 6; x++) {
						for (int y = -6; y <= 6; y++) {
							for (int z = -6; z <= 6; z++) {
								Location l1 = new Location(l.getWorld(), l.getX(), l.getY(), l.getZ());
								if (extReg.isInsidePolygon(l1.add(x, y, z),pr.get2DPolygon(), pr.getMinY(), pr.getMaxY())) {
									currentRegionSet.add(reg);
									break Outerloop;
								}
							}
						}
					}
			} else if (reg instanceof CuboidRegion) {
				CuboidRegion cr = (CuboidRegion) reg;
				Outerloop:
					for (int x = -6; x <= 6; x++) {
						for (int y = -6; y <= 6; y++) {
							for (int z = -8; z <= 6; z++) {
								Location l1 = new Location(l.getWorld(), l.getX(), l.getY(), l.getZ());
								if (extReg.isInsideCuboid(l1.add(x, y, z), cr.getL1(), cr.getL2())) {
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
				if (r.isProtected() || !r.isExplosionsEnabled()) {
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
		RegiosWorld w = wm.getRegiosWorld(l.getWorld());

		Region r = rm.getRegion(l);

		if (r == null)
		{
			if (!w.getPVP()) {
				if (evt instanceof EntityDamageByEntityEvent) {
					EntityDamageByEntityEvent edevt = (EntityDamageByEntityEvent) evt;
					Entity damager;
					if (edevt.getDamager() instanceof Player && edevt.getEntity() instanceof Player) {
						damager = edevt.getDamager();
						evt.setCancelled(true);
						evt.setDamage(0);
						return;
					} else if (edevt.getDamager().getType() == EntityType.ARROW && edevt.getEntity() instanceof Player) { //Check to see if the player was shot by an arrow.
						Projectile arrow = (Arrow) edevt.getDamager();
						damager = arrow.getShooter(); //get the arrows shooter
						if(damager.getType() == EntityType.PLAYER) { //if shot by a player, cancel the event
							evt.setCancelled(true);
							evt.setDamage(0);
						}
						return;
					} else if (edevt.getDamager().getType() == EntityType.SPLASH_POTION && edevt.getEntity() instanceof Player) { //Check if player was hit by a potion
						Projectile potion = (ThrownPotion) edevt.getDamager();
						damager = potion.getShooter(); //get the potion's thrower
						if(damager.getType() == EntityType.PLAYER) { //if it was thrown by a player, cancel the event
							evt.setCancelled(true);
							evt.setDamage(0);
						}
						return;
					} else {
						return;
					}
				}
				return;
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
					damager = edevt.getDamager();
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
