package net.jzx7.regios.regions;

import net.jzx7.regiosapi.block.RegiosBiome;
import net.jzx7.regiosapi.entity.RegiosPlayer;
import net.jzx7.regiosapi.location.RegiosPoint;
import net.jzx7.regiosapi.regions.CuboidRegion;
import net.jzx7.regiosapi.worlds.RegiosWorld;

public class RegiosCuboidRegion extends RegiosRegion implements CuboidRegion {
	
	protected RegiosPoint l1;
	protected RegiosPoint l2;
	
	public RegiosCuboidRegion(String owner, String name, RegiosPoint l1, RegiosPoint l2, RegiosWorld world, RegiosPlayer p, boolean save) {
		super(owner, name, world, p, save);
		this.l1 = new RegiosPoint(l1.getRegiosWorld(), l1.getX(), l1.getY(), l1.getZ());
		this.l2 = new RegiosPoint(l2.getRegiosWorld(), l2.getX(), l2.getY(), l2.getZ());

		rm.addRegion(this);
		if (save) {
			saveable.saveRegion(this);
		}
	}
	
	@Override
	public RegiosPoint getL1() {
		return new RegiosPoint(world, l1.getX(), l1.getY(), l1.getZ());
	}

	@Override
	public RegiosPoint getL2() {
		return new RegiosPoint(world, l2.getX(), l2.getY(), l2.getZ());
	}
	
	@Override
	public void setL1(RegiosPoint l1) {
		this.l1 = l1;
	}

	@Override
	public void setL1(RegiosWorld w, float x, float y, float z) {
		l1 = new RegiosPoint(w, x, y, z);
	}

	@Override
	public void setL2(RegiosPoint l2) {
		this.l2 = l2;
	}

	@Override
	public void setL2(RegiosWorld w, float x, float y, float z) {
		l2 = new RegiosPoint(w, x, y, z);
	}
	
	@Override 
	public void setBiome(RegiosBiome biome, RegiosPlayer p) {
		RegiosPoint min = new RegiosPoint(world, Math.min(l1.getX(), l2.getX()), Math.min(l1.getY(), l2.getY()), Math.min(l1.getZ(), l2.getZ()));
		RegiosPoint max = new RegiosPoint(world, Math.max(l1.getX(), l2.getX()), Math.max(l1.getY(), l2.getY()), Math.max(l1.getZ(), l2.getZ()));
		
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