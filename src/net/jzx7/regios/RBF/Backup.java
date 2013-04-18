package net.jzx7.regios.RBF;

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
import net.jzx7.regios.util.RegiosConversions;
import net.jzx7.regiosapi.block.RegiosBlock;
import net.jzx7.regiosapi.block.RegiosContainer;
import net.jzx7.regiosapi.block.RegiosSign;
import net.jzx7.regiosapi.entity.RegiosPlayer;
import net.jzx7.regiosapi.events.RegionBackupEvent;
import net.jzx7.regiosapi.events.RegionRestoreEvent;
import net.jzx7.regiosapi.exceptions.FileExistanceException;
import net.jzx7.regiosapi.exceptions.InvalidNBTFormat;
import net.jzx7.regiosapi.exceptions.RegionExistanceException;
import net.jzx7.regiosapi.inventory.RegiosItemStack;
import net.jzx7.regiosapi.location.RegiosPoint;
import net.jzx7.regiosapi.regions.CuboidRegion;
import net.jzx7.regiosapi.regions.PolyRegion;
import net.jzx7.regiosapi.regions.Region;
import net.jzx7.regiosapi.worlds.RegiosWorld;

import org.bukkit.Bukkit;

public class Backup extends PermissionsCore {

	private static final char[] ILLEGAL_CHARACTERS = { '/', '\n', '\r', '\t', '\0', '\f', '`', '?', '*', '\\', '<', '>', '|', '\"', ':' };

	public synchronized void startSave(final Region r, final String bckn, final RegiosPlayer p) {

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
					saveBackup(r, bckn, p);
				} catch (RegionExistanceException e) {
					e.printStackTrace();
				} catch (FileExistanceException e) {
					e.printStackTrace();
				}
			}

		}, 1L);
	}

	protected synchronized void saveBackup(Region r, String backupname, RegiosPlayer p) throws RegionExistanceException, FileExistanceException {
		if (p != null) {
			p.sendMessage("<DGREEN>" + "[Regios] Creating .rbf backup file...");
		}
		if (p != null) {
			if (!r.canModify(p)) {
				p.sendMessage("<RED>" + "[Regios] You are not permitted to modify this region!");
				return;
			}
		}

		if (r == null) {
			if (p != null) {
				p.sendMessage("<RED>" + "[Regios] That Region does not exist!");
			}
			throw new RegionExistanceException("UNKNOWN");
		}

		File f = new File("plugins" + File.separator + "Regios" + File.separator + "Database" + File.separator + r.getName() + File.separator + "Backups" + File.separator
				+ backupname + ".rbf");

		if (!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			if (p != null) {
				p.sendMessage("<RED>" + "[Regios] A backup with the name " + "<BLUE>" + backupname + "<RED>" + " already exists!");
			}
			throw new FileExistanceException("UNKNOWN", true);
		}

		RegiosPoint v1 = null, v2 = null;

		if (r instanceof PolyRegion) {
			Rectangle2D rect = ((PolyRegion) r).get2DPolygon().getBounds2D();
			v1 = new RegiosPoint(null, rect.getMinX(), ((PolyRegion) r).getMinY(), rect.getMinY());
			v2 = new RegiosPoint(null, rect.getMaxX(), ((PolyRegion) r).getMaxY(), rect.getMaxY());
		} else if (r instanceof CuboidRegion) {
			v1 = ((CuboidRegion) r).getL1();
			v2 = ((CuboidRegion) r).getL2();
		}

		RegiosWorld w = p.getRegiosWorld();
		RegiosPoint max = new RegiosPoint(w, Math.max(v1.getX(), v2.getX()), Math.max(v1.getY(), v2.getY()),
				Math.max(v1.getZ(), v2.getZ())), min = new RegiosPoint(w, Math.min(v1.getX(), v2.getX()),
						Math.min(v1.getY(), v2.getY()), Math.min(v1.getZ(), v2.getZ()));

		int width = max.getBlockX() - min.getBlockX();
		int height = max.getBlockY() - min.getBlockY();
		int length = max.getBlockZ() - min.getBlockZ();

		width += 1;
		height += 1;
		length += 1;

		if (width > 65535) {
			if (p != null) {
				p.sendMessage("<RED>" + "[Regios] The width is too large for a .rbf file!");

				p.sendMessage("<RED>" + "[Regios] Max width : 65535. Your size : " + "<BLUE>" + width);
			}
			return;
		}
		if (height > 65535) {
			if (p != null) {
				p.sendMessage("<RED>" + "[Regios] The height is too large for a .rbf file!");

				p.sendMessage("<RED>" + "[Regios] Max height : 65535. Your size : " + "<BLUE>" + width);
			}
			return;
		}
		if (length > 65535) {
			if (p != null) {
				p.sendMessage("<RED>" + "[Regios] The length is too large for a .rbf file!");

				p.sendMessage("<RED>" + "[Regios] Max length : 65535. Your size : " + "<BLUE>" + width);
			}
			return;
		}

		HashMap<String, Tag> backupTag = new HashMap<String, Tag>();

		// Copy
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

		CompoundTag compoundTag = new CompoundTag("RBF", backupTag);
		try {
			NBTOutputStream nbt = new NBTOutputStream(new FileOutputStream(f));
			nbt.writeTag(compoundTag);
			nbt.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		if (p != null) {
			p.sendMessage("<DGREEN>" + "[Regios] Region " + "<BLUE>" + backupname + "<DGREEN>" + " saved to .rbf file successfully!");
		}

		RegionBackupEvent event = new RegionBackupEvent("RegionBackupEvent");
		event.setProperties(r, backupname, RegiosConversions.getPlayer(p));
		Bukkit.getServer().getPluginManager().callEvent(event);
	}

	public void loadBackup(Region r, String backupname, RegiosPlayer p) throws IOException, RegionExistanceException, FileExistanceException, InvalidNBTFormat {
		if(p != null){
			if (!r.canModify(p)) {
				if (p != null) {
					p.sendMessage("<RED>" + "[Regios] You are not permitted to modify this region!");
				}
				return;
			}
		}

		if (r == null) {
			if (p != null) {
				p.sendMessage("<RED>" + "[Regios] That Region does not exist!");
			}
			throw new RegionExistanceException("UNKNOWN");
		}

		File f = new File("plugins" + File.separator + "Regios" + File.separator + "Database" + File.separator + r.getName() + File.separator + "Backups" + File.separator
				+ backupname + ".rbf");

		if (!f.exists()) {
			if (p != null) {
				p.sendMessage("<RED>" + "[Regios] A backup with the name " + "<BLUE>" + backupname + "<RED>" + " does not exist!");
			}
			throw new FileExistanceException("UNKNOWN", false);		
		}

		if (p != null) {
			p.sendMessage("<DGREEN>" + "[Regios] Restoring region from .rbf file...");
		}
		RegiosWorld w = p.getRegiosWorld();

		FileInputStream fis = new FileInputStream(f);
		NBTInputStream nbt = new NBTInputStream(new GZIPInputStream(fis));

		CompoundTag backuptag = (CompoundTag) nbt.readTag();
		Map<String, Tag> tagCollection = backuptag.getValue();

		if (!backuptag.getName().equals("RBF")) {
			if (p != null) {
				p.sendMessage("<RED>" + "[Regios] Backup file in unexpected format! Tag does not match 'RBF'.");
			}
			nbt.close();
			throw new InvalidNBTFormat("UNKNOWN", "RBF", backuptag.getName());
		}

		int StartX = NBTUtils.getChildTag(tagCollection, "StartX", IntTag.class).getValue();
		int StartY = NBTUtils.getChildTag(tagCollection, "StartY", IntTag.class).getValue();
		int StartZ = NBTUtils.getChildTag(tagCollection, "StartZ", IntTag.class).getValue();

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
					if(b instanceof RegiosContainer) {
						((RegiosContainer)b).clearInventory();
					}

					b.setId(blocks[index] & 0xFF);
					b.setData(blockData[index]);

					index++;
				}
			}
		}
		
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

		fis.close();
		nbt.close();

		RegionRestoreEvent event = new RegionRestoreEvent("RegionRestoreEvent");
		event.setProperties(r, backupname, RegiosConversions.getPlayer(p));
		Bukkit.getServer().getPluginManager().callEvent(event);

		if (p != null) {
			p.sendMessage("<DGREEN>" + "[Regios] Region" + "<BLUE>" + backupname + "<DGREEN>" + " restored successfully from .rbf file!");
		}
	}

}
