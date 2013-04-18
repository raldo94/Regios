package net.jzx7.regios.Restrictions;

import java.util.ArrayList;

import net.jzx7.regios.Permissions.PermissionsCore;
import net.jzx7.regiosapi.entity.RegiosPlayer;

public class RestrictionParameters {

	public static ArrayList<String> size, count;
	private int regionLimit, regionWidthLimit, regionHeightLimit, regionLengthLimit;


	public RestrictionParameters(int regionLimit, int regionWidthLimit, int regionHeightLimit, int regionLengthLimit) {
		this.regionLimit = regionLimit;
		this.regionWidthLimit = regionWidthLimit;
		this.regionHeightLimit = regionHeightLimit;
		this.regionLengthLimit = regionLengthLimit;
	}
	
	public int getRegionLimit() {
		return regionLimit;
	}

	public int getRegionWidthLimit() {
		return regionWidthLimit;
	}

	public int getRegionHeightLimit() {
		return regionHeightLimit;
	}

	public int getRegionLengthLimit() {
		return regionLengthLimit;
	}
	
	public static RestrictionParameters getRestrictions(RegiosPlayer p)
	{
		int c = 0, w = 0, h = 0, l = 0;
		
		for (String node : size)
		{
			if(PermissionsCore.doesHaveNode(p, node))
			{
				int i = Integer.parseInt(node.substring(24));
				if (i > p.getRegiosWorld().getMaxHeight())
				{
					w = l = i;
					h = p.getRegiosWorld().getMaxHeight();
				} else {
					w = h = l = i;
				}
			}
		}
		
		for (String node : count)
		{
			if(PermissionsCore.doesHaveNode(p, node))
			{
				c = Integer.parseInt(node.substring(20));
			}
		}
		
		
		if (PermissionsCore.doesHaveNode(p, "regios.restrictions.none")) {
			c = w = l = 999999999;
			h = p.getRegiosWorld().getMaxHeight();
		}

		return new RestrictionParameters(c, w, h, l);
	}
}
