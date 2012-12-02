package net.jzx7.regios.Scheduler;

import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;

import net.jzx7.regiosapi.events.RegionLightningStrikeEvent;
import net.jzx7.regiosapi.regions.CuboidRegion;
import net.jzx7.regiosapi.regions.PolyRegion;
import net.jzx7.regiosapi.regions.Region;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

import couk.Adamki11s.Extras.Regions.ExtrasRegions;

public class LightningRunner {

	private static ExtrasRegions extReg = new ExtrasRegions();

	public static HashMap<Region, Integer> strikes = new HashMap<Region, Integer>();
	public static HashMap<Region, Integer> strikeCounter = new HashMap<Region, Integer>();

	public static void addRegion(Region r) {
		strikes.put(r, r.getLSPS());
		strikeCounter.put(r, 0);
	}

	public static void removeRegion(Region r) {
		if (strikes.containsKey(r)) {
			strikes.remove(r);
		}
		if (strikeCounter.containsKey(r)) {
			strikeCounter.remove(r);
		}
	}

	public static boolean doesStrikesContain(Region genericRegion) {
		return strikes.containsKey(genericRegion);
	}

	public static void executeStrikes() {
		for (Entry<Region, Integer> entry : strikes.entrySet()) {
			Region r = entry.getKey();
			int count = strikeCounter.get(r);
			if (count >= strikes.get(r)) {
				fireStrike(r);
			} else {
				incrementCounter(r);
			}
		}
	}

	private static void fireStrike(Region r) {
		resetCounter(r);
		Vector rl1 = null, rl2 = null;
		if (r instanceof PolyRegion) {
			Rectangle2D rect =  ((PolyRegion) r).get2DPolygon().getBounds2D();
			rl1 = new Vector(rect.getMinX(), ((PolyRegion) r).getMinY(), rect.getMinY());
			rl2 = new Vector(rect.getMaxX(), ((PolyRegion) r).getMaxY(), rect.getMaxY());
		} else if (r instanceof CuboidRegion) {
			rl1 = ((CuboidRegion) r).getL1().toVector();
			rl2 = ((CuboidRegion) r).getL2().toVector();
		}

		Location STRIKE = getStrikeLocation(rl1.getX(), rl2.getX(), rl1.getZ(), rl2.getZ(), rl1.getY(), rl2.getY(), r.getWorld(), r);

		r.getWorld().strikeLightning(STRIKE);

		RegionLightningStrikeEvent event = new RegionLightningStrikeEvent("RegionLightningStrikeEvent");
		event.setProperties(new Location(r.getWorld(), STRIKE.getX(), STRIKE.getY(), STRIKE.getZ(), 0, 0), r);
		Bukkit.getServer().getPluginManager().callEvent(event);
	}

	private static Location getStrikeLocation(double x1, double x2, double z1, double z2, double y1, double y2, World world, Region r) {
		double xdiff, zdiff, xStrike, yStrike, zStrike;
		boolean x1bigger = false, z1bigger = false;
		Location strike;

		if (x1 > x2) {
			xdiff = x1 - x2;
			x1bigger = true;
		} else {
			xdiff = x2 - x1;
			x1bigger = false;
		}
		if (z1 > z2) {
			zdiff = z1 - z2;
			z1bigger = true;
		} else {
			zdiff = z2 - z1;
			z1bigger = false;
		}
		if (y1 > y2) {
			yStrike = y2;
		} else {
			yStrike = y1;
		}

		while (0 < 1) {
			Random gen = new Random();
			int randXStrike = gen.nextInt((int) ((xdiff) + 1));
			int randZStrike = gen.nextInt((int) ((zdiff) + 1));

			if (x1bigger) {
				xStrike = (x2 + randXStrike);
			} else {
				xStrike = (x1 + randXStrike);
			}
			if (z1bigger) {
				zStrike = (z2 + randZStrike);
			} else {
				zStrike = (z1 + randZStrike);
			}

			strike = new Location(world, xStrike, yStrike, zStrike, 0, 0);
			if (r instanceof PolyRegion) {
				if (extReg.isInsidePolygon(strike, ((PolyRegion) r).get2DPolygon(), ((PolyRegion) r).getMinY(), ((PolyRegion) r).getMaxY())  && (strike.getWorld().getName() == r.getWorld().getName())) {
					return strike;
				}
			} else if (r instanceof CuboidRegion) {
				return new Location(world, xStrike, yStrike, zStrike, 0, 0);
			}
		}
	}

	private static void incrementCounter(Region r) {
		if (strikeCounter.containsKey(r)) {
			strikeCounter.put(r, strikeCounter.get(r) + 1);
		} else {
			strikeCounter.put(r, 0);
		}
	}

	private static void resetCounter(Region r) {
		strikeCounter.put(r, 0);
	}

}
