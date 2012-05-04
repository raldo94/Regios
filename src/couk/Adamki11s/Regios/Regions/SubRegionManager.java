package couk.Adamki11s.Regios.Regions;

import java.util.ArrayList;

import org.bukkit.entity.Player;

public class SubRegionManager {
	
	public Region getCurrentRegion(Player p, ArrayList<Region> regions){
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
		
		if(r.getL1().getBlockX() > r.getL2().getBlockX()){
			xVAL = r.getL1().getBlockX() - r.getL2().getBlockX();
		} else {
			xVAL = r.getL2().getBlockX() - r.getL1().getBlockX();
		}
		
		if(r.getL1().getBlockZ() > r.getL2().getBlockZ()){
			zVAL = r.getL1().getBlockZ() - r.getL2().getBlockZ();
		} else {
			zVAL = r.getL2().getBlockZ() - r.getL1().getBlockZ();
		}
		
		if(r.getL1().getBlockY() > r.getL2().getBlockY()){
			yVAL = r.getL1().getBlockY() - r.getL2().getBlockY();
		} else {
			yVAL = r.getL2().getBlockY() - r.getL1().getBlockY();
		}
		
		if(xVAL == 0)
			xVAL = 1;
		if(yVAL == 0)
			yVAL = 1;
		if(zVAL == 0)
			zVAL = 1;
		
		return xVAL * yVAL * zVAL;
	}
	
	private int getHorizontalBlockArea(Region r){
		int xVAL, zVAL;
		
		if(r.getL1().getBlockX() > r.getL2().getBlockX()){
			xVAL = r.getL1().getBlockX() - r.getL2().getBlockX();
		} else {
			xVAL = r.getL2().getBlockX() - r.getL1().getBlockX();
		}
		
		if(r.getL1().getBlockZ() > r.getL2().getBlockZ()){
			zVAL = r.getL1().getBlockZ() - r.getL2().getBlockZ();
		} else {
			zVAL = r.getL2().getBlockZ() - r.getL1().getBlockZ();
		}
		
		if(xVAL == 0)
			xVAL = 1;
		if(zVAL == 0)
			zVAL = 1;
		
		return xVAL * zVAL;
	}

}
