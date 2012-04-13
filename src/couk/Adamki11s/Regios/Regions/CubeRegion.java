package couk.Adamki11s.Regios.Regions;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import couk.Adamki11s.Regios.Checks.ChunkGrid;

public class CubeRegion extends Region {
	
	public CubeRegion(String owner, String name, Location l1, Location l2, World world, Player p, boolean save) {
		super(owner, name, world, p, save);
		this.l1 = new RegionLocation(l1.getWorld(), l1.getX(), l1.getY(), l1.getZ());
		this.l2 = new RegionLocation(l2.getWorld(), l2.getX(), l2.getY(), l2.getZ());
		RegionLocation rl1 = new RegionLocation(l1.getWorld(), l1.getX(), l1.getY(), l1.getZ())
					 , rl2 = new RegionLocation(l2.getWorld(), l2.getX(), l2.getY(), l2.getZ());
		
		chunkGrid = new ChunkGrid(l1, l2, this);
		GlobalRegionManager.addRegion(this);
		if (p == null && save) {
			saveable.saveRegion(this, rl1, rl2);
		}
	}
	
}