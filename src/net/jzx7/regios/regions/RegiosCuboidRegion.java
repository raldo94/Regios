package net.jzx7.regios.regions;

import net.jzx7.regiosapi.regions.CuboidRegion;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;

public class RegiosCuboidRegion extends RegiosRegion implements CuboidRegion {
	
	protected Location l1;
	protected Location l2;
	
	public RegiosCuboidRegion(String owner, String name, Location l1, Location l2, World world, Player p, boolean save) {
		super(owner, name, world, p, save);
		this.l1 = new Location(l1.getWorld(), l1.getX(), l1.getY(), l1.getZ());
		this.l2 = new Location(l2.getWorld(), l2.getX(), l2.getY(), l2.getZ());

		rm.addRegion(this);
		if (p == null && save) {
			saveable.saveRegion(this);
		}
	}
	
	@Override
	public Location getL1() {
		return new Location(world, l1.getX(), l1.getY(), l1.getZ());
	}

	@Override
	public Location getL2() {
		return new Location(world, l2.getX(), l2.getY(), l2.getZ());
	}
	
	@Override
	public void setL1(Location l1) {
		this.l1 = l1;
	}

	@Override
	public void setL1(World w, double x, double y, double z) {
		l1 = new Location(w, x, y, z);
	}

	@Override
	public void setL2(Location l2) {
		this.l2 = l2;
	}

	@Override
	public void setL2(World w, double x, double y, double z) {
		l2 = new Location(w, x, y, z);
	}
	
	@Override 
	public void setBiome(Biome biome, Player p) {
		Location min = new Location(world, Math.min(l1.getX(), l2.getX()), Math.min(l1.getY(), l2.getY()), Math.min(l1.getZ(), l2.getZ()));
		Location max = new Location(world, Math.max(l1.getX(), l2.getX()), Math.max(l1.getY(), l2.getY()), Math.max(l1.getZ(), l2.getZ()));
		
		for (double x = min.getX(); x <= max.getX(); x++) {
			for (double z = min.getZ(); z <= max.getZ(); z++) {
				world.setBiome((int) x,(int) z, biome);
			}
		}

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