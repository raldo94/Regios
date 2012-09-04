package net.jzx7.regios.regions;

import java.awt.Polygon;
import java.util.ArrayList;

import net.jzx7.regiosapi.regions.CuboidRegion;
import net.jzx7.regiosapi.regions.PolyRegion;
import net.jzx7.regiosapi.regions.Region;

public class SubRegionManager {

	public Region getCurrentRegion(ArrayList<Region> regions){
		int hba = 999999999,
				tdba = 999999999;
		Region current = null;
		for(Region r : regions){
			int horizblockarea = getHorizontalBlockArea(r),
					tdBlockArea = get3DBlockArea(r);
			if(horizblockarea < hba){
				hba = horizblockarea;
				current = r;
				tdba = tdBlockArea;
			} else if(hba == horizblockarea){
				if(tdBlockArea < tdba){
					hba = horizblockarea;
					current = r;
					tdba = tdBlockArea;
				}
			}
		}
		return current;
	}

	private int get3DBlockArea(Region r){
		int xVAL, zVAL, yVAL;

		if (r instanceof PolyRegion) {
			Polygon pr = ((PolyRegion) r).get2DPolygon();

			int area = 0;         // Accumulates area in the loop
			int j = pr.npoints-1;  // The last vertex is the 'previous' one to the first

			for (int i=0; i<pr.npoints; i++)
			{ area = area +  (pr.xpoints[j]+pr.xpoints[i]) * (pr.ypoints[j]-pr.ypoints[i]); 
			j = i;  //j is previous vertex to i
			}
			return (int) ((area/2) * (((PolyRegion) r).getMaxY() - ((PolyRegion) r).getMinY()));

		} else if (r instanceof CuboidRegion) {
			CuboidRegion cr = (CuboidRegion) r;

			if(cr.getL1().getBlockX() > cr.getL2().getBlockX()){
				xVAL = cr.getL1().getBlockX() - cr.getL2().getBlockX();
			} else {
				xVAL = cr.getL2().getBlockX() - cr.getL1().getBlockX();
			}

			if(cr.getL1().getBlockZ() > cr.getL2().getBlockZ()){
				zVAL = cr.getL1().getBlockZ() - cr.getL2().getBlockZ();
			} else {
				zVAL = cr.getL2().getBlockZ() - cr.getL1().getBlockZ();
			}

			if(cr.getL1().getBlockY() > cr.getL2().getBlockY()){
				yVAL = cr.getL1().getBlockY() - cr.getL2().getBlockY();
			} else {
				yVAL = cr.getL2().getBlockY() - cr.getL1().getBlockY();
			}

			if(xVAL == 0)
				xVAL = 1;
			if(yVAL == 0)
				yVAL = 1;
			if(zVAL == 0)
				zVAL = 1;

			return xVAL * yVAL * zVAL;
		}
		return 0;
	}

	private int getHorizontalBlockArea(Region r){
		int xVAL, zVAL;

		if (r instanceof PolyRegion) {
			Polygon pr = ((PolyRegion) r).get2DPolygon();

			int area = 0;         // Accumulates area in the loop
			int j = pr.npoints-1;  // The last vertex is the 'previous' one to the first

			for (int i=0; i<pr.npoints; i++)
			{ area = area +  (pr.xpoints[j]+pr.xpoints[i]) * (pr.ypoints[j]-pr.ypoints[i]); 
			j = i;  //j is previous vertex to i
			}
			return area/2;

		} else if (r instanceof CuboidRegion) {
			CuboidRegion cr = (CuboidRegion) r;
			if(cr.getL1().getBlockX() > cr.getL2().getBlockX()){
				xVAL = cr.getL1().getBlockX() - cr.getL2().getBlockX();
			} else {
				xVAL = cr.getL2().getBlockX() - cr.getL1().getBlockX();
			}

			if(cr.getL1().getBlockZ() > cr.getL2().getBlockZ()){
				zVAL = cr.getL1().getBlockZ() - cr.getL2().getBlockZ();
			} else {
				zVAL = cr.getL2().getBlockZ() - cr.getL1().getBlockZ();
			}

			if(xVAL == 0)
				xVAL = 1;
			if(zVAL == 0)
				zVAL = 1;

			return xVAL * zVAL;
		}
		return 0;
	}

}
