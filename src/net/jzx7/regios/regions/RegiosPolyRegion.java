package net.jzx7.regios.regions;

import java.awt.Polygon;
import java.awt.geom.Rectangle2D;

import net.jzx7.regiosapi.block.RegiosBiome;
import net.jzx7.regiosapi.entity.RegiosPlayer;
import net.jzx7.regiosapi.location.RegiosPoint;
import net.jzx7.regiosapi.regions.PolyRegion;
import net.jzx7.regiosapi.worlds.RegiosWorld;

public class RegiosPolyRegion extends RegiosRegion  implements PolyRegion {

	protected Polygon polySet;
	protected double minY, maxY;

	public RegiosPolyRegion(String owner, String name, int[] xpoints, int[] zpoints, int npoints, double minY2, double maxY2, RegiosWorld world, RegiosPlayer p, boolean save) {
		super(owner, name, world, p, save);
		this.polySet = new Polygon(xpoints, zpoints, npoints);
		this.minY = minY2;
		this.maxY = maxY2;

		rm.addRegion(this);
		if (save) {
			saveable.saveRegion(this);
		}
	}

	@Override
	public void addPoint(int x, int z){
		this.polySet.addPoint(x, z);
	}

	/*
	 * Check Rectangle2D that encompasses polygon before refining search
	 */
	@Override
	public boolean insideBounds(int x, int z){
		return this.polySet.getBounds2D().contains(x, z);
	}

	/*
	 * Checks all bounds to see whether the player is inside the polygon
	 */
	@Override
	public boolean inside(int x, int z){
		return this.polySet.contains(x, z);
	}

	/*
	 * Check Rectangle2D that encompasses polygon before refining search
	 */
	@Override
	public boolean insideBounds(double x, double z){
		return this.polySet.getBounds2D().contains(x, z);
	}

	/*
	 * Checks all bounds to see whether the player is inside the polygon
	 */
	@Override
	public boolean inside(double x, double z){
		return this.polySet.contains(x, z);
	}

	@Override
	public Polygon get2DPolygon() {
		return polySet;
	}
	
	@Override
	public void set2DPolygon(Polygon polygon) {
		polySet = polygon;
	}

	@Override
	public double getMinY() {
		return minY;
	}

	@Override
	public double getMaxY() {
		return maxY;
	}

	@Override
	public void setMinY(double minY) {
		this.minY = minY;
	}

	@Override
	public void setMaxY(double maxY) {
		this.maxY = maxY;
	}

	@Override
	public void setBiome(RegiosBiome biome, RegiosPlayer p) {
		Rectangle2D rect = polySet.getBounds2D();

		for (double x = rect.getMinX(); x <= rect.getMaxX(); x++) {
			for (double z = rect.getMinY(); z <= rect.getMaxY(); z++) {
				if (polySet.contains(x, z)) {
					world.setBiome((int) x,(int) z, biome);
				}
			}
		}

		RegiosPoint min = new RegiosPoint(world, rect.getMinX(), minY, rect.getMinY());
		RegiosPoint max = new RegiosPoint(world, rect.getMaxX(), maxY, rect.getMaxY());

		int minChunkX = min.getBlockX() >> 4;
		int minChunkZ = min.getBlockZ() >> 4;
		int maxChunkX = max.getBlockX() >> 4;
		int maxChunkZ = max.getBlockZ() >> 4;

		for (int x = minChunkX; x <= maxChunkX; x++) {
			for (int z = minChunkZ; z <= maxChunkZ; z++) {
				world.refreshChunk(x, z);
			}
		}
	}
}