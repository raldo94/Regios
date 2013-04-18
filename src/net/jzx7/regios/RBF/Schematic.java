package net.jzx7.regios.RBF;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.GZIPInputStream;

import net.jzx7.jnbt.ByteArrayTag;
import net.jzx7.jnbt.CompoundTag;
import net.jzx7.jnbt.IntTag;
import net.jzx7.jnbt.ListTag;
import net.jzx7.jnbt.NBTInputStream;
import net.jzx7.jnbt.NBTOutputStream;
import net.jzx7.jnbt.NBTUtils;
import net.jzx7.jnbt.ShortTag;
import net.jzx7.jnbt.StringTag;
import net.jzx7.jnbt.Tag;
import net.jzx7.regios.RegiosPlugin;
import net.jzx7.regiosapi.block.NBTData;
import net.jzx7.regiosapi.block.RegiosBlock;
import net.jzx7.regiosapi.block.RegiosContainer;
import net.jzx7.regiosapi.entity.RegiosPlayer;
import net.jzx7.regiosapi.exceptions.FileExistanceException;
import net.jzx7.regiosapi.exceptions.InvalidNBTData;
import net.jzx7.regiosapi.location.RegiosPoint;
import net.jzx7.regiosapi.worlds.RegiosWorld;

import org.bukkit.Bukkit;

public class Schematic {

	private static final char[] ILLEGAL_CHARACTERS = { '/', '\n', '\r', '\t', '\0', '\f', '`', '?', '*', '\\', '<', '>', '|', '\"', ':' };

	public synchronized void startSave(final RegiosPoint l1, final RegiosPoint l2, final String bckn, final RegiosPlayer p) {

		Bukkit.getServer().getScheduler().runTaskLaterAsynchronously(RegiosPlugin.regios, new Runnable() {

			@Override
			public void run() {
				for(char c : bckn.toCharArray()){
					for(char il : ILLEGAL_CHARACTERS){
						if(c == il){
							p.sendMessage("<RED>" + "[Regios] Invalid token " + "<YELLOW>" + c + "<RED>" + " in file name!");
							return;
						}
					}
				}
				saveSchematic(l1, l2, bckn, p);
			}

		}, 1L);
	}

	public void saveSchematic(RegiosPoint l1, RegiosPoint l2, String backupname, RegiosPlayer p) {
		try {
			if (p != null) {
				p.sendMessage("<DGREEN>" + "[Regios] Creating .schematic file...");
			}
			File f = new File("plugins" + File.separator + "Regios" + File.separator + "Schematics" + File.separator + backupname + ".schematic");

			if (!f.exists()) {
				f.createNewFile();
			} else {
				if (p != null) {
					p.sendMessage("<RED>" + "[Regios] A schematic file with the name " + "<BLUE>" + backupname + "<RED>" + " already exists!");
				}
				throw new FileExistanceException("UNKNOWN", true);
			}

			RegiosWorld w = l1.getRegiosWorld();
			RegiosPoint max = new RegiosPoint(w, Math.max(l1.getX(), l2.getX()), Math.max(l1.getY(), l2.getY()), Math.max(l1.getZ(), l2.getZ()))
			, min = new RegiosPoint(w, Math.min(l1.getX(), l2.getX()), Math.min(l1.getY(), l2.getY()), Math.min(l1.getZ(), l2.getZ()));

			int width = max.getBlockX() - min.getBlockX();
			int height = max.getBlockY() - min.getBlockY();
			int length = max.getBlockZ() - min.getBlockZ();

			width += 1;
			height += 1;
			length += 1;

			if (width > 65535) {
				if (p != null) {
					p.sendMessage("<RED>" + "[Regios] The width is too large for a .schematic file!");
					p.sendMessage("<RED>" + "[Regios] Max width : 65535. Your size : " + "<BLUE>" + width);
				}
				return;
			}
			if (height > 65535) {
				if (p != null) {
					p.sendMessage("<RED>" + "[Regios] The height is too large for a .schematic file!");
					p.sendMessage("<RED>" + "[Regios] Max height : 65535. Your size : " + "<BLUE>" + width);
				}
				return;
			}
			if (length > 65535) {
				if (p != null) {
					p.sendMessage("<RED>" + "[Regios] The length is too large for a .schematic file!");
					p.sendMessage("<RED>" + "[Regios] Max length : 65535. Your size : " + "<BLUE>" + width);
				}
				return;
			}

			HashMap<String, Tag> backupTag = new HashMap<String, Tag>();

			byte[] blockID = new byte[width * height * length];
			byte[] blockData = new byte[width * height * length];
			ArrayList<Tag> tileEntities = new ArrayList<Tag>();

			int index = 0;

			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					for (int z = 0; z < length; z++) {
						blockID[index] = (byte) w.getBlockAt(min.getBlockX() + x, min.getBlockY() + y, min.getBlockZ() + z).getId();
						blockData[index] = w.getBlockAt(min.getBlockX() + x, min.getBlockY() + y, min.getBlockZ() + z).getData();
						index++;
					}
				}
			}

			for (int x = 0; x < width; ++x) {
				for (int y = 0; y < height; ++y) {
					for (int z = 0; z < length; ++z) {
						index = y * width * length + z * width + x;
						RegiosBlock b = w.getBlockAt(min.getBlockX() + x, min.getBlockY() + y, min.getBlockZ() + z);
						
						blockID[index] = (byte) b.getId();
						blockData[index] = b.getData();

						if (b instanceof NBTData) {
	                        NBTData nbtData = (NBTData) b;

	                        CompoundTag rawTag = nbtData.getNBTData();
	                        if (rawTag != null) {
	                            Map<String, Tag> values = new HashMap<String, Tag>();
	                            for (Entry<String, Tag> entry : rawTag.getValue().entrySet()) {
	                                values.put(entry.getKey(), entry.getValue());
	                            }
	                            
	                            values.put("id", new StringTag("id", nbtData.getNBTID()));
	                            values.put("x", new IntTag("x", x));
	                            values.put("y", new IntTag("y", y));
	                            values.put("z", new IntTag("z", z));
	                            
	                            CompoundTag tileEntityTag = new CompoundTag("TileEntity", values);
	                            tileEntities.add(tileEntityTag);
	                        }
	                    }
					}
				}
			}

			backupTag.put("Width", new ShortTag("Width", (short) width));
			backupTag.put("Length", new ShortTag("Length", (short) length));
			backupTag.put("Height", new ShortTag("Height", (short) height));
			backupTag.put("Materials", new StringTag("Materials", "Alpha"));
			backupTag.put("Blocks", new ByteArrayTag("Blocks", blockID));
			backupTag.put("Data", new ByteArrayTag("Data", blockData));
			backupTag.put("Entities", new ListTag("Entities", CompoundTag.class, new ArrayList<Tag>()));
			backupTag.put("TileEntities", new ListTag("TileEntities", CompoundTag.class, tileEntities));

			CompoundTag compoundTag = new CompoundTag("Schematic", backupTag);

			NBTOutputStream nbt = new NBTOutputStream(new FileOutputStream(f));
			nbt.writeTag(compoundTag);
			nbt.close();

			if (p != null) {
				p.sendMessage("<DGREEN>" + "[Regios] Schematic " + "<BLUE>" + backupname + "<DGREEN>" + " saved to .schematic file successfully!");
			}
		} catch (Exception ex) {
			if (p != null) {
				p.sendMessage("<RED>" + "[Regios] Error saving schematic! Stack trace printed in console.");
			}
			ex.printStackTrace();
		}
	}

	public void loadSchematic(String sharename, RegiosPlayer p, RegiosPoint l) throws IOException, InvalidNBTData {
		if (RBF_Core.getUndoCache().containsKey(p.getName())) {
			RBF_Core.getUndoCache().remove(p.getName());
		}

		ArrayList<PBD> blockss = new ArrayList<PBD>();
		RBF_Core.getUndoCache().put(p.getName(), blockss);

		File f = new File("plugins" + File.separator + "Regios" + File.separator + "Schematics" + File.separator + sharename + ".schematic");

		if (!f.exists()) {
			p.sendMessage("<RED>" + "[Regios] A blueprint file with the name " + "<BLUE>" + sharename + "<RED>" + " does not exist!");
			return;
		}

		p.sendMessage("<DGREEN>" + "[Regios] Restoring region from " + sharename + ".schematic file...");

		RegiosWorld w = p.getRegiosWorld();

		FileInputStream fis = new FileInputStream(f);
		NBTInputStream nbt = new NBTInputStream(new GZIPInputStream(fis));

		CompoundTag backuptag = (CompoundTag) nbt.readTag();
		Map<String, Tag> tagCollection = backuptag.getValue();

		if (!backuptag.getName().equals("Schematic")) {
			p.sendMessage("<RED>" + "[Regios] Schematic file in unexpected format! Tag does not match 'Schematic'.");
		}

		short width = NBTUtils.getChildTag(tagCollection, "Width", ShortTag.class).getValue();
		short length = NBTUtils.getChildTag(tagCollection, "Length", ShortTag.class).getValue();
		short height = NBTUtils.getChildTag(tagCollection, "Height", ShortTag.class).getValue();

		// Check type of Schematic
		String materials = NBTUtils.getChildTag(tagCollection, "Materials", StringTag.class).getValue();
		if (!materials.equals("Alpha")) {
			throw new InvalidNBTData("Schematic file is not an Alpha schematic");
		}

		byte[] blocks = NBTUtils.getChildTag(tagCollection, "Blocks", ByteArrayTag.class).getValue();
		byte[] blockData = NBTUtils.getChildTag(tagCollection, "Data", ByteArrayTag.class).getValue();

		List<Tag> tileEntities = NBTUtils.getChildTag(tagCollection, "TileEntities", ListTag.class).getValue();
		Map<RegiosPoint, Map<String, Tag>> tileEntitiesMap = new HashMap<RegiosPoint, Map<String, Tag>>();

		for (Tag tag : tileEntities) {
			if (!(tag instanceof CompoundTag)) continue;
			CompoundTag t = (CompoundTag) tag;

			int x = 0;
			int y = 0;
			int z = 0;

			Map<String, Tag> values = new HashMap<String, Tag>();

			for (Map.Entry<String, Tag> entry : t.getValue().entrySet()) {
				if (entry.getKey().equals("x")) {
					if (entry.getValue() instanceof IntTag) {
						x = ((IntTag) entry.getValue()).getValue();
					}
				} else if (entry.getKey().equals("y")) {
					if (entry.getValue() instanceof IntTag) {
						y = ((IntTag) entry.getValue()).getValue();
					}
				} else if (entry.getKey().equals("z")) {
					if (entry.getValue() instanceof IntTag) {
						z = ((IntTag) entry.getValue()).getValue();
					}
				}

				values.put(entry.getKey(), entry.getValue());
			}

			RegiosPoint vec = new RegiosPoint(x, y, z);
			tileEntitiesMap.put(vec, values);
		}

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				for (int z = 0; z < length; z++) {
					RegiosPoint pt = new RegiosPoint(x, y, z);
					RegiosBlock b = w.getBlockAt(pt.add(l));
					blockss.add(new PBD(b, l.getBlockX(), l.getBlockY(), l.getBlockZ()));
				}
			}
		}

		for (int x = 0; x < width; ++x) {
			for (int y = 0; y < height; ++y) {
				for (int z = 0; z < length; ++z) {
					int index = y * width * length + z * width + x;
					RegiosPoint pt = new RegiosPoint(x, y, z);
					RegiosBlock b = w.getBlockAt(new RegiosPoint(x, y, z).add(l));
					if (b instanceof RegiosContainer) {
						((RegiosContainer) b).clearInventory();
					}

					b.setId(blocks[index] & 0xFF);
					b.setData(blockData[index]);

					if (b instanceof NBTData && tileEntitiesMap.containsKey(pt)) {
                        ((NBTData) b).setNBTData(new CompoundTag("", tileEntitiesMap.get(pt)));
                    }
				}
			}
		}

		RBF_Core.getUndoCache().put(p.getName(), blockss);

		fis.close();
		nbt.close();

		p.sendMessage("<DGREEN>" + "[Regios]Schematic " + "<BLUE>" + sharename + "<DGREEN>" + " loaded successfully from .schematic file!");
	}
}
