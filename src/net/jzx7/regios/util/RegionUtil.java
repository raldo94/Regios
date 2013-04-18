package net.jzx7.regios.util;

import java.awt.Polygon;

import net.jzx7.regiosapi.entity.RegiosPlayer;
import net.jzx7.regiosapi.location.RegiosPoint;

public class RegionUtil {
	public boolean isInsideCuboid(RegiosPlayer p, RegiosPoint point1, RegiosPoint point2) {
		double x1 = point1.getX(), x2 = point2.getX(),
		       y1 = point1.getY(), y2 = point2.getY(),
		       z1 = point1.getZ(), z2 = point2.getZ(),
		       px = p.getPoint().getX(),
		       py = p.getPoint().getY(),
		       pz = p.getPoint().getZ();
		
		RegiosPoint max = new RegiosPoint(point1.getRegiosWorld(), Math.max(x1, x2), Math.max(y1, y2), Math.max(z1, z2));
		RegiosPoint min = new RegiosPoint(point1.getRegiosWorld(), Math.min(x1, x2), Math.min(y1, y2), Math.min(z1, z2));

		return (px >= min.getX() && px <= max.getX() + 1 && py >= min.getY() && py <= max.getY() && pz >= min.getZ() && pz <= max.getZ() + 1);
	}
	
	public boolean isInsideCuboid(RegiosPoint l, RegiosPoint point1, RegiosPoint point2) {
		double x1 = point1.getX(), x2 = point2.getX(),
		       y1 = point1.getY(), y2 = point2.getY(),
		       z1 = point1.getZ(), z2 = point2.getZ(),
		       px = l.getX(),
		       py = l.getY(),
		       pz = l.getZ();
		
		RegiosPoint max = new RegiosPoint(point1.getRegiosWorld(), Math.max(x1, x2), Math.max(y1, y2), Math.max(z1, z2));
		RegiosPoint min = new RegiosPoint(point1.getRegiosWorld(), Math.min(x1, x2), Math.min(y1, y2), Math.min(z1, z2));

		return (px >= min.getX() && px <= max.getX() && py >= min.getY() && py <= max.getY() && pz >= min.getZ() && pz <= max.getZ());
	}

	public boolean isInsidePolygon(RegiosPlayer p, Polygon poly, double minY, double maxY) {
		RegiosPoint l = p.getPoint();
		return isInsidePolygon(l, poly, minY, maxY);
	}
	
	public boolean isInsidePolygon(RegiosPoint l, Polygon poly, double minY, double maxY) {
		if (poly.getBounds2D().contains(l.getX(), l.getZ())) {
			if (poly.contains(l.getX(), l.getZ())) {
				if ((minY <= l.getY()) && (l.getY() <= maxY)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean isInsideRadius(RegiosPlayer p, RegiosPoint centre, int radius) {
		RegiosPoint point1 = new RegiosPoint(centre.getRegiosWorld(), centre.getX() - radius, centre.getY() - radius, centre.getZ() - radius);
		RegiosPoint point2 = new RegiosPoint(centre.getRegiosWorld(), centre.getX() + radius, centre.getY() + radius, centre.getZ() + radius);
		
		double x1 = point1.getX(), x2 = point2.getX(),
	       y1 = point1.getY(), y2 = point2.getY(),
	       z1 = point1.getZ(), z2 = point2.getZ(),
	       px = p.getPoint().getX(),
	       py = p.getPoint().getY(),
	       pz = p.getPoint().getZ();
		
		if( (((py <= y1) && 
				(py >= y2)) || 
				((py >= y1) && 
				(py <= y2))) && 
				(((pz <= z1) && 
				(pz >= z2)) || 
				((pz >= z1) && 
				(pz <= z2)))  &&  
				(((px <= x1) && 
				(px >= x2)) || 
				((px >= x1) && 
				(px <= x2))) && 
				(((px <= x1) && 
				(px >= x2)) || 
				((px >= x1) && 
				(px <= x2)))){
				return true;
			}		
		
		return false;
	}
}