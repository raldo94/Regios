package net.jzx7.regios.regions;

import net.jzx7.regiosapi.regions.PolyRegion;
import java.awt.Polygon;
import java.awt.geom.Rectangle2D;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;

public class RegiosPolyRegion extends RegiosRegion  implements PolyRegion {

	protected Polygon polySet;
	protected double minY, maxY;

	public RegiosPolyRegion(String owner, String name, int[] xpoints, int[] zpoints, int npoints, double minY2, double maxY2, World world, Player p, boolean save) {
		super(owner, name, world, p, save);
		this.polySet = new Polygon(xpoints, zpoints, npoints);
		this.minY = minY2;
		this.maxY = maxY2;

		//		try {
		//			chunkGrid = new RegiosChunkGrid(polySet, minY, maxY, world);
		//		} catch (NullPointerException npe) {
		//			System.out.print("[Regios] Uh-oh, there's been an error loading the ChunkGrid for region: " + this.getName() + ". Please check the config file for this region and verify that both point1 and point2 are accurate as well as the World.");
		//		}

		rm.addRegion(this);
		if (p == null && save) {
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
	public void setBiome(Biome biome, Player p) {
		Rectangle2D rect = polySet.getBounds2D();

		for (double x = rect.getMinX(); x <= rect.getMaxX(); x++) {
			for (double z = rect.getMinY(); z <= rect.getMaxY(); z++) {
				if (polySet.contains(x, z)) {
					world.setBiome((int) x,(int) z, biome);
				}
			}
		}

		Location min = new Location(world, rect.getMinX(), minY, rect.getMinY());
		Location max = new Location(world, rect.getMaxX(), maxY, rect.getMaxY());

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