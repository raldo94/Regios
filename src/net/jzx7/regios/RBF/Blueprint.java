package net.jzx7.regios.RBF;

import java.awt.Polygon;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import net.jzx7.jnbt.ByteArrayTag;
import net.jzx7.jnbt.CompoundTag;
import net.jzx7.jnbt.IntTag;
import net.jzx7.jnbt.ListItemStackArrayTag;
import net.jzx7.jnbt.ListStringArrayTag;
import net.jzx7.jnbt.NBTInputStream;
import net.jzx7.jnbt.NBTOutputStream;
import net.jzx7.jnbt.NBTUtils;
import net.jzx7.jnbt.Tag;
import net.jzx7.regios.RegiosPlugin;
import net.jzx7.regios.Permissions.PermissionsCore;
import net.jzx7.regiosapi.block.RegiosBlock;
import net.jzx7.regiosapi.block.RegiosContainer;
import net.jzx7.regiosapi.block.RegiosSign;
import net.jzx7.regiosapi.entity.RegiosPlayer;
import net.jzx7.regiosapi.exceptions.FileExistanceException;
import net.jzx7.regiosapi.inventory.RegiosItemStack;
import net.jzx7.regiosapi.location.RegiosPoint;
import net.jzx7.regiosapi.worlds.RegiosWorld;

import org.bukkit.Bukkit;

public class Blueprint extends PermissionsCore {

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
				try {
					saveBlueprint(l1, l2, bckn, p);
				} catch (FileExistanceException e) {
					e.printStackTrace();
				}
			}

		}, 1L);
	}

	public synchronized void startSave(final int[] xs, final int[] zs, final int ns, final int minY, final int maxY, final String backupname, final RegiosPlayer p, final boolean b) {

		Bukkit.getServer().getScheduler().runTaskLaterAsynchronously(RegiosPlugin.regios, new Runnable() {

			@Override
			public void run() {
				for(char c : backupname.toCharArray()){
					for(char il : ILLEGAL_CHARACTERS){
						if(c == il){
							p.sendMessage("<RED>" + "[Regios] Invalid token " + "<YELLOW>" + c + "<RED>" + " in file name!");
							return;
						}
					}
				}
				try {
					saveBlueprint(new Polygon(xs, zs, ns), minY, maxY, backupname, p);
				} catch (FileExistanceException e) {
					e.printStackTrace();
				}
			}

		}, 1L);

	}

	protected synchronized void saveBlueprint(RegiosPoint l1, RegiosPoint l2, String backupname, RegiosPlayer p) throws FileExistanceException {
		try {
			if (p != null) {
				p.sendMessage("<DGREEN>" + "[Regios] Creating .blp Blueprint file...");
			}
			File f = new File("plugins" + File.separator + "Regios" + File.separator + "Blueprints" + File.separator + backupname + ".blp");

			if (!f.exists()) {
				f.createNewFile();
			} else {
				if (p != null) {
					p.sendMessage("<RED>" + "[Regios] A Blueprint file with the name " + "<BLUE>" + backupname + "<RED>" + " already exists!");
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
					p.sendMessage("<RED>" + "[Regios] The width is too large for a .blp file!");
					p.sendMessage("<RED>" + "[Regios] Max width : 65535. Your size : " + "<BLUE>" + width);
				}
				return;
			}
			if (height > 65535) {
				if (p != null) {
					p.sendMessage("<RED>" + "[Regios] The height is too large for a .blp file!");
					p.sendMessage("<RED>" + "[Regios] Max height : 65535. Your size : " + "<BLUE>" + width);
				}
				return;
			}
			if (length > 65535) {
				if (p != null) {
					p.sendMessage("<RED>" + "[Regios] The length is too large for a .blp file!");
					p.sendMessage("<RED>" + "[Regios] Max length : 65535. Your size : " + "<BLUE>" + width);
				}
				return;
			}

			HashMap<String, Tag> backupTag = new HashMap<String, Tag>();

			byte[] blockID = new byte[width * height * length];
			byte[] blockData = new byte[width * height * length];
			List<RegiosItemStack[]> containerData = new ArrayList<RegiosItemStack[]>();
			List<String[]> signData = new ArrayList<String[]>();

			int index = 0;

			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					for (int z = 0; z < length; z++) {
						RegiosBlock b = w.getBlockAt(min.getBlockX() + x, min.getBlockY() + y, min.getBlockZ() + z);
						blockID[index] = (byte) b.getId();
						blockData[index] = b.getData();

						if(b instanceof RegiosContainer) {
							containerData.add(((RegiosContainer) b).getContents());
						} else {
							containerData.add(null);
						}

						if(b instanceof RegiosSign) {
							signData.add(((RegiosSign)b).getText());
						} else {
							signData.add(null);
						}
						index++;
					}
				}
			}

			backupTag.put("BlockID", new ByteArrayTag("BlockID", blockID));
			backupTag.put("Data", new ByteArrayTag("Data", blockData));
			backupTag.put("ContainerData", new ListItemStackArrayTag("ContainerData", containerData)); //jzx7
			backupTag.put("SignData", new ListStringArrayTag("SignData", signData)); //jzx7
			backupTag.put("StartX", new IntTag("StartX", min.getBlockX()));
			backupTag.put("StartY", new IntTag("StartY", min.getBlockY()));
			backupTag.put("StartZ", new IntTag("StartZ", min.getBlockZ()));
			backupTag.put("XSize", new IntTag("XSize", width));
			backupTag.put("YSize", new IntTag("YSize", height));
			backupTag.put("ZSize", new IntTag("ZSize", length));

			CompoundTag compoundTag = new CompoundTag("BLP", backupTag);

			NBTOutputStream nbt = new NBTOutputStream(new FileOutputStream(f));
			nbt.writeTag(compoundTag);
			nbt.close();
			if (p != null) {
				p.sendMessage("<DGREEN>" + "[Regios] Blueprint " + "<BLUE>" + backupname + "<DGREEN>" + " saved to .blp file successfully!");
			}
		} catch (Exception ex) {
			if (p != null) {
				p.sendMessage("<RED>" + "[Regios] Error saving blueprint! Stack trace printed in console.");
			}
			ex.printStackTrace();
		}
	}

	protected synchronized void saveBlueprint(Polygon polygon, int minY, int maxY, String backupname, RegiosPlayer p) throws FileExistanceException {
		try {
			if (p != null) {
				p.sendMessage("<DGREEN>" + "[Regios] Creating .blp Blueprint file...");
			}
			File f = new File("plugins" + File.separator + "Regios" + File.separator + "Blueprints" + File.separator + backupname + ".blp");

			if (!f.exists()) {
				f.createNewFile();
			} else {
				if (p != null) {
					p.sendMessage("<RED>" + "[Regios] A Blueprint file with the name " + "<BLUE>" + backupname + "<RED>" + " already exists!");
				}
				throw new FileExistanceException("UNKNOWN", true);
			}

			RegiosWorld w = p.getRegiosWorld();
			Rectangle2D rect = polygon.getBounds2D();

			int width = (int) (rect.getMaxX() - rect.getMinX());
			int height = maxY - minY;
			int length = (int) (rect.getMaxY() - rect.getMinY());

			width += 1;
			height += 1;
			length += 1;

			if (width > 65535) {
				if (p != null) {
					p.sendMessage("<RED>" + "[Regios] The width is too large for a .blp file!");

					p.sendMessage("<RED>" + "[Regios] Max width : 65535. Your size : " + "<BLUE>" + width);
				}
				return;
			}
			if (height > 65535) {
				if (p != null) {
					p.sendMessage("<RED>" + "[Regios] The height is too large for a .blp file!");

					p.sendMessage("<RED>" + "[Regios] Max height : 65535. Your size : " + "<BLUE>" + width);
				}
				return;
			}
			if (length > 65535) {
				if (p != null) {
					p.sendMessage("<RED>" + "[Regios] The length is too large for a .blp file!");

				}
				p.sendMessage("<RED>" + "[Regios] Max length : 65535. Your size : " + "<BLUE>" + width);
				return;
			}

			HashMap<String, Tag> backupTag = new HashMap<String, Tag>();

			byte[] blockID = new byte[width * height * length];
			byte[] blockData = new byte[width * height * length];
			List<RegiosItemStack[]> containerData = new ArrayList<RegiosItemStack[]>();
			List<String[]> signData = new ArrayList<String[]>();

			int index = 0;

			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					for (int z = 0; z < length; z++) {
						if (polygon.contains(x, z)) {
							RegiosBlock b = w.getBlockAt((int) rect.getMinX() + x, minY + y, (int) rect.getMinY() + z);
							blockID[index] = (byte) b.getId();
							blockData[index] = b.getData();
							if(b instanceof RegiosContainer) {
								containerData.add(((RegiosContainer) b).getContents());
							} else {
								containerData.add(null);
							}

							if(b instanceof RegiosSign) {
								signData.add(((RegiosSign)b).getText());
							} else {
								signData.add(null);
							}
						} else {
							blockID[index] = (Byte) null;
							blockData[index] = (Byte) null;
						}
						index++;
					}
				}
			}

			backupTag.put("BlockID", new ByteArrayTag("BlockID", blockID));
			backupTag.put("Data", new ByteArrayTag("Data", blockData));
			backupTag.put("ContainerData", new ListItemStackArrayTag("ContainerData", containerData)); //jzx7
			backupTag.put("SignData", new ListStringArrayTag("SignData", signData)); //jzx7
			backupTag.put("StartX", new IntTag("StartX", (int) rect.getMinX()));
			backupTag.put("StartY", new IntTag("StartY", minY));
			backupTag.put("StartZ", new IntTag("StartZ", (int) rect.getMinY()));
			backupTag.put("XSize", new IntTag("XSize", width));
			backupTag.put("YSize", new IntTag("YSize", height));
			backupTag.put("ZSize", new IntTag("ZSize", length));

			CompoundTag compoundTag = new CompoundTag("BLP", backupTag);

			NBTOutputStream nbt = new NBTOutputStream(new FileOutputStream(f));
			nbt.writeTag(compoundTag);
			nbt.close();
			if (p != null) {
				p.sendMessage("<DGREEN>" + "[Regios] Blueprint " + "<BLUE>" + backupname + "<DGREEN>" + " saved to .blp file successfully!");
			}
		} catch (Exception ex) {
			if (p != null) {
				p.sendMessage("<RED>" + "[Regios] Error saving blueprint! Stack trace printed in console.");
			}
			ex.printStackTrace();
		}
	}
	
	public void loadBlueprint(String sharename, RegiosPlayer p, RegiosPoint l) throws IOException {

		if (RBF_Core.getUndoCache().containsKey(p.getName())) {
			RBF_Core.getUndoCache().remove(p.getName());
		}

		ArrayList<PBD> blockss = new ArrayList<PBD>();
		RBF_Core.getUndoCache().put(p.getName(), blockss);

		File f = new File("plugins" + File.separator + "Regios" + File.separator + "Blueprints" + File.separator + sharename + ".blp");

		if (!f.exists()) {
			p.sendMessage("<RED>" + "[Regios] A blueprint file with the name " + "<BLUE>" + sharename + "<RED>" + " does not exist!");
			return;
		}

		p.sendMessage("<DGREEN>" + "[Regios] Restoring blueprint from " + sharename + ".blp file...");

		RegiosWorld w = p.getRegiosWorld();

		FileInputStream fis = new FileInputStream(f);
		NBTInputStream nbt = new NBTInputStream(new GZIPInputStream(fis));

		CompoundTag backupTag = (CompoundTag) nbt.readTag();
		Map<String, Tag> tagCollection = backupTag.getValue();

		if (!backupTag.getName().equals("BLP")) {
			p.sendMessage("<RED>" + "[Regios] Blueprint file in unexpected format! Tag does not match 'BLP'.");
		}

		int StartX = l.getBlockX();
		int StartY = l.getBlockY();
		int StartZ = l.getBlockZ();

		int width = NBTUtils.getChildTag(tagCollection, "XSize", IntTag.class).getValue();
		int height = NBTUtils.getChildTag(tagCollection, "YSize", IntTag.class).getValue();
		int length = NBTUtils.getChildTag(tagCollection, "ZSize", IntTag.class).getValue();

		byte[] blocks = NBTUtils.getChildTag(tagCollection, "BlockID", ByteArrayTag.class).getValue();
		byte[] blockData = NBTUtils.getChildTag(tagCollection, "Data", ByteArrayTag.class).getValue();
		List<RegiosItemStack[]> containerData = NBTUtils.getChildTag(tagCollection, "ContainerData", ListItemStackArrayTag.class).getValue();
		List<String[]> signData = NBTUtils.getChildTag(tagCollection, "SignData", ListStringArrayTag.class).getValue();
		
		int index = 0;

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				for (int z = 0; z < length; z++) {
					RegiosBlock b = w.getBlockAt(StartX + x, StartY + y, StartZ + z);
					blockss.add(new PBD(b, StartX + x, StartY + y, StartZ + z));
				}
			}
		}

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				for (int z = 0; z < length; z++) {
					RegiosBlock b = w.getBlockAt(StartX + x, StartY + y, StartZ + z);

					if(b instanceof RegiosContainer) {
						((RegiosContainer)b).clearInventory();
					}

					b.setId(blocks[index] & 0xFF);
					b.setData(blockData[index]);

					index++;
				}
			}
		}
		
		index = 0;
		
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				for (int z = 0; z < length; z++) {
					RegiosBlock b = w.getBlockAt(StartX + x, StartY + y, StartZ + z);
					if(b instanceof RegiosContainer) {
						((RegiosContainer)b).setContents(containerData.get(index));
					}

					if(b instanceof RegiosSign) {
						RegiosSign sign = (RegiosSign) b;
						int line = 0;
						for(String s : signData.get(index)) {
							if(line > 3) {
								break;
							}
							sign.setLine(line, s);
							line++;
						}
						//sign.update();
					}
					index++;
				}
			}
		}

		RBF_Core.getUndoCache().put(p.getName(), blockss);

		fis.close();
		nbt.close();

		p.sendMessage("<DGREEN>" + "[Regios] Blueprint " + "<BLUE>" + sharename + "<DGREEN>" + " loaded successfully from .blp file!");
	}
}
