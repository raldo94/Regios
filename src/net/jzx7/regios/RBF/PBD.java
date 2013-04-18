package net.jzx7.regios.RBF;

import net.jzx7.regiosapi.block.RegiosBlock;
import net.jzx7.regiosapi.location.RegiosPoint;

public class PBD {
	
	private int x,y,z;
	private int id;
	private byte data;
	
	public PBD(RegiosBlock b, int x, int y, int z){ //Needed to create an object which can't be modifed at run time by Bukkit.
		this.x = x;
		this.y = y;
		this.z = z;
		this.id = b.getId();
		this.data = b.getData();
	}
	
	public RegiosPoint getL() {
		return new RegiosPoint(x, y, z);
	}
	public int getId() {
		return id;
	}
	public byte getData() {
		return data;
	}

}
