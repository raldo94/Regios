package couk.Adamki11s.Regios.Regions;

import java.awt.Polygon;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class PolyRegion extends Region {
	
	protected Polygon polySet;
	protected int minY, maxY;

	public PolyRegion(String owner, String name, int xpoints[], int zpoints[], int npoints, World world, Player p, boolean save) {
		super(owner, name, world, p, save);
		this.polySet = new Polygon(xpoints, zpoints, npoints);
		this.minY = (int) l1.getY();
		this.maxY = (int) l2.getY();
		
	}
	
}