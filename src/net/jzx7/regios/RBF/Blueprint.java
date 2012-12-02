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
import net.jzx7.regiosapi.exceptions.FileExistanceException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;


public class Blueprint extends PermissionsCore {

	private static final char[] ILLEGAL_CHARACTERS = { '/', '\n', '\r', '\t', '\0', '\f', '`', '?', '*', '\\', '<', '>', '|', '\"', ':' };

	public synchronized void startSave(final Location l1, final Location l2, final String bckn, final Player p) {

		Bukkit.getServer().getScheduler().scheduleAsyncDelayedTask(RegiosPlugin.regios, new Runnable() {

			@Override
			public void run() {
				for(char c : bckn.toCharArray()){
					for(char il : ILLEGAL_CHARACTERS){
						if(c == il){
							p.sendMessage(ChatColor.RED + "[Regios] Invalid token " + ChatColor.YELLOW + c + ChatColor.RED + " in file name!");
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

	public synchronized void startSave(final int[] xPoints, final int[] zPoints, final int nPoints, final int minY, final int maxY, final String backupname, final Player p, final boolean b) {

		Bukkit.getServer().getScheduler().scheduleAsyncDelayedTask(RegiosPlugin.regios, new Runnable() {

			@Override
			public void run() {
				for(char c : backupname.toCharArray()){
					for(char il : ILLEGAL_CHARACTERS){
						if(c == il){
							p.sendMessage(ChatColor.RED + "[Regios] Invalid token " + ChatColor.YELLOW + c + ChatColor.RED + " in file name!");
							return;
						}
					}
				}
				try {
					saveBlueprint(new Polygon(xPoints, zPoints, nPoints), minY, maxY, backupname, p);
				} catch (FileExistanceException e) {
					e.printStackTrace();
				}
			}

		}, 1L);

	}

	protected synchronized void saveBlueprint(Location l1, Location l2, String backupname, Player p) throws FileExistanceException {
		try {
			if (p != null) {
				p.sendMessage(ChatColor.GREEN + "[Regios] Creating .blp Blueprint file...");
			}
			File f = new File("plugins" + File.separator + "Regios" + File.separator + "Blueprints" + File.separator + backupname + ".blp");

			if (!f.exists()) {
				f.createNewFile();
			} else {
				if (p != null) {
					p.sendMessage(ChatColor.RED + "[Regios] A Blueprint file with the name " + ChatColor.BLUE + backupname + ChatColor.RED + " already exists!");
				}
				throw new FileExistanceException("UNKNOWN", true);
			}

			World w = l1.getWorld();
			Location max = new Location(w, Math.max(l1.getX(), l2.getX()), Math.max(l1.getY(), l2.getY()), Math.max(l1.getZ(), l2.getZ()))
			, min = new Location(w, Math.min(l1.getX(), l2.getX()), Math.min(l1.getY(), l2.getY()), Math.min(l1.getZ(), l2.getZ()));

			int width = max.getBlockX() - min.getBlockX();
			int height = max.getBlockY() - min.getBlockY();
			int length = max.getBlockZ() - min.getBlockZ();

			width += 1;
			height += 1;
			length += 1;

			if (width > 65535) {
				if (p != null) {
					p.sendMessage(ChatColor.RED + "[Regios] The width is too large for a .blp file!");
					p.sendMessage(ChatColor.RED + "[Regios] Max width : 65535. Your size : " + ChatColor.BLUE + width);
				}
				return;
			}
			if (height > 65535) {
				if (p != null) {
					p.sendMessage(ChatColor.RED + "[Regios] The height is too large for a .blp file!");
					p.sendMessage(ChatColor.RED + "[Regios] Max height : 65535. Your size : " + ChatColor.BLUE + width);
				}
				return;
			}
			if (length > 65535) {
				if (p != null) {
					p.sendMessage(ChatColor.RED + "[Regios] The length is too large for a .blp file!");
					p.sendMessage(ChatColor.RED + "[Regios] Max length : 65535. Your size : " + ChatColor.BLUE + width);
				}
				return;
			}

			HashMap<String, Tag> backupTag = new HashMap<String, Tag>();

			byte[] blockID = new byte[width * height * length];
			byte[] blockData = new byte[width * height * length];
			List<ItemStack[]> containerData = new ArrayList<ItemStack[]>();
			List<String[]> signData = new ArrayList<String[]>();

			int index = 0;

			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					for (int z = 0; z < length; z++) {
						Block b = w.getBlockAt(min.getBlockX() + x, min.getBlockY() + y, min.getBlockZ() + z);
						blockID[index] = (byte) b.getTypeId();
						blockData[index] = b.getData();

						if(b.getState() instanceof InventoryHolder) {
							containerData.add(((InventoryHolder) b.getState()).getInventory().getContents());
						} else {
							containerData.add(null);
						}

						if(b.getState() instanceof Sign) {
							signData.add(((Sign)b.getState()).getLines());
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
				p.sendMessage(ChatColor.GREEN + "[Regios] Blueprint " + ChatColor.BLUE + backupname + ChatColor.GREEN + " saved to .blp file successfully!");
			}
		} catch (Exception ex) {
			if (p != null) {
				p.sendMessage(ChatColor.RED + "[Regios] Error saving blueprint! Stack trace printed in console.");
			}
			ex.printStackTrace();
		}
	}

	protected synchronized void saveBlueprint(Polygon polygon, int minY, int maxY, String backupname, Player p) throws FileExistanceException {
		try {
			if (p != null) {
				p.sendMessage(ChatColor.GREEN + "[Regios] Creating .blp Blueprint file...");
			}
			File f = new File("plugins" + File.separator + "Regios" + File.separator + "Blueprints" + File.separator + backupname + ".blp");

			if (!f.exists()) {
				f.createNewFile();
			} else {
				if (p != null) {
					p.sendMessage(ChatColor.RED + "[Regios] A Blueprint file with the name " + ChatColor.BLUE + backupname + ChatColor.RED + " already exists!");
				}
				throw new FileExistanceException("UNKNOWN", true);
			}

			World w = p.getWorld();
			Rectangle2D rect = polygon.getBounds2D();

			int width = (int) (rect.getMaxX() - rect.getMinX());
			int height = maxY - minY;
			int length = (int) (rect.getMaxY() - rect.getMinY());

			width += 1;
			height += 1;
			length += 1;

			if (width > 65535) {
				if (p != null) {
					p.sendMessage(ChatColor.RED + "[Regios] The width is too large for a .blp file!");

					p.sendMessage(ChatColor.RED + "[Regios] Max width : 65535. Your size : " + ChatColor.BLUE + width);
				}
				return;
			}
			if (height > 65535) {
				if (p != null) {
					p.sendMessage(ChatColor.RED + "[Regios] The height is too large for a .blp file!");

					p.sendMessage(ChatColor.RED + "[Regios] Max height : 65535. Your size : " + ChatColor.BLUE + width);
				}
				return;
			}
			if (length > 65535) {
				if (p != null) {
					p.sendMessage(ChatColor.RED + "[Regios] The length is too large for a .blp file!");

				}
				p.sendMessage(ChatColor.RED + "[Regios] Max length : 65535. Your size : " + ChatColor.BLUE + width);
				return;
			}

			HashMap<String, Tag> backupTag = new HashMap<String, Tag>();

			byte[] blockID = new byte[width * height * length];
			byte[] blockData = new byte[width * height * length];
			List<ItemStack[]> containerData = new ArrayList<ItemStack[]>();
			List<String[]> signData = new ArrayList<String[]>();

			int index = 0;

			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					for (int z = 0; z < length; z++) {
						if (polygon.contains(x, z)) {
							Block b = w.getBlockAt((int) rect.getMinX() + x, minY + y, (int) rect.getMinY() + z);
							blockID[index] = (byte) b.getTypeId();
							blockData[index] = b.getData();
							if(b.getState() instanceof InventoryHolder) {
								containerData.add(((InventoryHolder) b.getState()).getInventory().getContents());
							} else {
								containerData.add(null);
							}

							if(b instanceof Sign) {
								signData.add(((Sign)b).getLines());
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
				p.sendMessage(ChatColor.GREEN + "[Regios] Blueprint " + ChatColor.BLUE + backupname + ChatColor.GREEN + " saved to .blp file successfully!");
			}
		} catch (Exception ex) {
			if (p != null) {
				p.sendMessage(ChatColor.RED + "[Regios] Error saving blueprint! Stack trace printed in console.");
			}
			ex.printStackTrace();
		}
	}
	
	public void loadBlueprint(String sharename, Player p, Location l) throws IOException {

		if (RBF_Core.undoCache.containsKey(p.getName())) {
			RBF_Core.undoCache.remove(p.getName());
		}

		ArrayList<PBD> blockss = new ArrayList<PBD>();
		RBF_Core.undoCache.put(p.getName(), blockss);

		File f = new File("plugins" + File.separator + "Regios" + File.separator + "Blueprints" + File.separator + sharename + ".blp");

		if (!f.exists()) {
			p.sendMessage(ChatColor.RED + "[Regios] A blueprint file with the name " + ChatColor.BLUE + sharename + ChatColor.RED + " does not exist!");
			return;
		}

		p.sendMessage(ChatColor.GREEN + "[Regios] Restoring blueprint from " + sharename + ".blp file...");

		World w = p.getWorld();

		FileInputStream fis = new FileInputStream(f);
		NBTInputStream nbt = new NBTInputStream(new GZIPInputStream(fis));

		CompoundTag backupTag = (CompoundTag) nbt.readTag();
		Map<String, Tag> tagCollection = backupTag.getValue();

		if (!backupTag.getName().equals("BLP")) {
			p.sendMessage(ChatColor.RED + "[Regios] Blueprint file in unexpected format! Tag does not match 'BLP'.");
		}

		int StartX = l.getBlockX();
		int StartY = l.getBlockY();
		int StartZ = l.getBlockZ();

		int width = (Integer) NBTUtils.getChildTag(tagCollection, "XSize", IntTag.class).getValue();
		int height = (Integer) NBTUtils.getChildTag(tagCollection, "YSize", IntTag.class).getValue();
		int length = (Integer) NBTUtils.getChildTag(tagCollection, "ZSize", IntTag.class).getValue();

		byte[] blocks = (byte[]) NBTUtils.getChildTag(tagCollection, "BlockID", ByteArrayTag.class).getValue();
		byte[] blockData = (byte[]) NBTUtils.getChildTag(tagCollection, "Data", ByteArrayTag.class).getValue();
		List<ItemStack[]> containerData = (List<ItemStack[]>) NBTUtils.getChildTag(tagCollection, "ContainerData", ListItemStackArrayTag.class).getValue();
		List<String[]> signData = (List<String[]>) NBTUtils.getChildTag(tagCollection, "SignData", ListStringArrayTag.class).getValue();
		
		int index = 0;

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				for (int z = 0; z < length; z++) {
					Block b = w.getBlockAt(StartX + x, StartY + y, StartZ + z);
					blockss.add(new PBD(b));
				}
			}
		}

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				for (int z = 0; z < length; z++) {
					Block b = w.getBlockAt(StartX + x, StartY + y, StartZ + z);

					if(b.getState() instanceof InventoryHolder) {
						((InventoryHolder)b.getState()).getInventory().clear();
					}

					b.setTypeId(blocks[index] & 0xFF);
					b.setData(blockData[index]);

					index++;
				}
			}
		}
		
		index = 0;
		
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				for (int z = 0; z < length; z++) {
					Block b = w.getBlockAt(StartX + x, StartY + y, StartZ + z);
					if(b.getState() instanceof InventoryHolder) {
						((InventoryHolder)b.getState()).getInventory().setContents(containerData.get(index));
					}

					if(b.getState() instanceof Sign) {
						Sign sign = (Sign) b.getState();
						int line = 0;
						for(String s : signData.get(index)) {
							if(line > 3) {
								break;
							}
							sign.setLine(line, s);
							line++;
						}
						sign.update();
					}
					index++;
				}
			}
		}

		RBF_Core.undoCache.put(p.getName(), blockss);

		fis.close();
		nbt.close();

		p.sendMessage(ChatColor.GREEN + "[Regios] Blueprint " + ChatColor.BLUE + sharename + ChatColor.GREEN + " loaded successfully from .blp file!");
	}
}
