package net.jzx7.regios.bukkit.worlds;

import java.util.ArrayList;

import net.jzx7.regios.util.RegiosConversions;
import net.jzx7.regios.worlds.RegWorld;
import net.jzx7.regiosapi.block.RegiosBiome;
import net.jzx7.regiosapi.block.RegiosBlock;
import net.jzx7.regiosapi.block.RegiosChest;
import net.jzx7.regiosapi.block.RegiosDispenser;
import net.jzx7.regiosapi.block.RegiosFurnace;
import net.jzx7.regiosapi.block.RegiosNoteBlock;
import net.jzx7.regiosapi.block.RegiosSign;
import net.jzx7.regiosapi.block.RegiosSpawner;
import net.jzx7.regiosapi.location.RegiosPoint;

import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.block.Dispenser;
import org.bukkit.block.Furnace;
import org.bukkit.block.NoteBlock;
import org.bukkit.block.Sign;
import org.bukkit.entity.EntityType;

public class BukkitWorld extends RegWorld{

	private World world;
	
	private ArrayList<EntityType> creaturesWhoSpawn = new ArrayList<EntityType>();
	
	public BukkitWorld(World w) {
		this.world = w;;
	}
	
	public World getWorld() {
		return world;
	}
	
	@Override
	public String getName() {
		return world.getName();
	}

	@Override
	public boolean refreshChunk(int x, int y) {
		return world.refreshChunk(x, y);
	}

	@Override
	public void setBiome(int x, int y, RegiosBiome biome) {
		world.setBiome(x, y, Biome.valueOf(biome.getName()));
	}

	@Override
	public int getMaxHeight() {
		return world.getMaxHeight();
	}

	@Override
	public boolean canCreatureSpawn(int entityType){
		return creaturesWhoSpawn.contains(EntityType.fromId(entityType));
	}
	
	@Override
	public void addCreatureSpawn(int ct){
		creaturesWhoSpawn.add(EntityType.fromId(ct));
	}

	@Override
	public void strikeLightning(RegiosPoint l) {
		world.strikeLightning(RegiosConversions.getLocation(l));
	}

	@Override
	public RegiosBlock getBlockAt(int x, int y, int z) {
		Block b = world.getBlockAt(x, y, x);
		RegiosBlock rb;
		if (b instanceof Sign) {
			rb = new RegiosSign(b.getTypeId(), b.getData(), ((Sign) b).getLines());
		} else if (b instanceof Chest) {
			rb = new RegiosChest(b.getTypeId(), b.getData(), RegiosConversions.convertItemStack(((Chest) b).getInventory().getContents()));
		} else if (b instanceof Dispenser) {
			rb = new RegiosDispenser(b.getTypeId(), b.getData(), RegiosConversions.convertItemStack(((Dispenser) b).getInventory().getContents()));
		} else if (b instanceof Furnace) {
			rb = new RegiosFurnace(b.getTypeId(), b.getData(), RegiosConversions.convertItemStack(((Furnace) b).getInventory().getContents()));
			((RegiosFurnace) rb).setBurnTime(((Furnace) b).getBurnTime());
			((RegiosFurnace) rb).setCookTime(((Furnace) b).getCookTime());
		} else if (b instanceof NoteBlock) {
			rb = new RegiosNoteBlock(b.getTypeId(), b.getData(), ((NoteBlock) b).getNote().getId());
		} else if (b instanceof CreatureSpawner) {
			rb = new RegiosSpawner(b.getTypeId(), b.getData(), ((CreatureSpawner) b).getCreatureTypeName());
			((RegiosSpawner) rb).setDelay(((CreatureSpawner) b).getDelay());
		} else {
			rb = new RegiosBlock(b.getTypeId(), b.getData());
		}
		return rb;
	}

	@Override
	public RegiosBlock getBlockAt(RegiosPoint l) {
		Block b = world.getBlockAt(RegiosConversions.getLocation(l));
		RegiosBlock rb;
		if (b instanceof Sign) {
			rb = new RegiosSign(b.getTypeId(), b.getData(), ((Sign) b).getLines());
		} else if (b instanceof Chest) {
			rb = new RegiosChest(b.getTypeId(), b.getData(), RegiosConversions.convertItemStack(((Chest) b).getInventory().getContents()));
		} else if (b instanceof Dispenser) {
			rb = new RegiosDispenser(b.getTypeId(), b.getData(), RegiosConversions.convertItemStack(((Dispenser) b).getInventory().getContents()));
		} else if (b instanceof Furnace) {
			rb = new RegiosFurnace(b.getTypeId(), b.getData(), RegiosConversions.convertItemStack(((Furnace) b).getInventory().getContents()));
			((RegiosFurnace) rb).setBurnTime(((Furnace) b).getBurnTime());
			((RegiosFurnace) rb).setCookTime(((Furnace) b).getCookTime());
		} else if (b instanceof NoteBlock) {
			rb = new RegiosNoteBlock(b.getTypeId(), b.getData(), ((NoteBlock) b).getNote().getId());
		} else if (b instanceof CreatureSpawner) {
			rb = new RegiosSpawner(b.getTypeId(), b.getData(), ((CreatureSpawner) b).getCreatureTypeName());
			((RegiosSpawner) rb).setDelay(((CreatureSpawner) b).getDelay());
		} else {
			rb = new RegiosBlock(b.getTypeId(), b.getData());
		}
		return rb;
	}
	
}