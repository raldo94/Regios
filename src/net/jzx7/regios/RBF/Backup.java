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
import net.jzx7.regiosapi.events.RegionBackupEvent;
import net.jzx7.regiosapi.events.RegionRestoreEvent;
import net.jzx7.regiosapi.exceptions.FileExistanceException;
import net.jzx7.regiosapi.exceptions.InvalidNBTFormat;
import net.jzx7.regiosapi.exceptions.RegionExistanceException;
import net.jzx7.regiosapi.regions.CuboidRegion;
import net.jzx7.regiosapi.regions.PolyRegion;
import net.jzx7.regiosapi.regions.Region;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BrewingStand;
import org.bukkit.block.Chest;
import org.bukkit.block.Dispenser;
import org.bukkit.block.Furnace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class Backup extends PermissionsCore {
	
	private static final char[] ILLEGAL_CHARACTERS = { '/', '\n', '\r', '\t', '\0', '\f', '`', '?', '*', '\\', '<', '>', '|', '\"', ':' };
	
	public synchronized void startSave(final Region r, final String bckn, final Player p) {

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
						saveBackup(r, bckn, p);
					} catch (RegionExistanceException e) {
						e.printStackTrace();
					} catch (FileExistanceException e) {
						e.printStackTrace();
					}
			}

		}, 1L);
	}

	protected synchronized void saveBackup(Region r, String backupname, Player p) throws RegionExistanceException, FileExistanceException {
		if (p != null) {
			p.sendMessage(ChatColor.GREEN + "[Regios] Creating .rbf backup file...");
		}
		if (p != null) {
			if (!r.canModify(p)) {
				p.sendMessage(ChatColor.RED + "[Regios] You are not permitted to modify this region!");
				return;
			}
		}

		if (r == null) {
			if (p != null) {
				p.sendMessage(ChatColor.RED + "[Regios] That Region does not exist!");
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
				p.sendMessage(ChatColor.RED + "[Regios] A backup with the name " + ChatColor.BLUE + backupname + ChatColor.RED + " already exists!");
			}
			throw new FileExistanceException("UNKNOWN", true);
		}

		Vector v1 = null, v2 = null;

		if (r instanceof PolyRegion) {
			Rectangle2D rect = ((PolyRegion) r).get2DPolygon().getBounds2D();
			v1 = new Vector(rect.getMinX(), ((PolyRegion) r).getMinY(), rect.getMinY());
			v2 = new Vector(rect.getMaxX(), ((PolyRegion) r).getMaxY(), rect.getMaxY());
		} else if (r instanceof CuboidRegion) {
			v1 = ((CuboidRegion) r).getL1().toVector();
			v2 = ((CuboidRegion) r).getL2().toVector();
		}

		World w = p.getWorld();
		Location max = new Location(w, Math.max(v1.getX(), v2.getX()), Math.max(v1.getY(), v2.getY()),
				Math.max(v1.getZ(), v2.getZ())), min = new Location(w, Math.min(v1.getX(), v2.getX()),
						Math.min(v1.getY(), v2.getY()), Math.min(v1.getZ(), v2.getZ()));

		int width = max.getBlockX() - min.getBlockX();
		int height = max.getBlockY() - min.getBlockY();
		int length = max.getBlockZ() - min.getBlockZ();

		width += 1;
		height += 1;
		length += 1;

		if (width > 65535) {
			if (p != null) {
				p.sendMessage(ChatColor.RED + "[Regios] The width is too large for a .rbf file!");

				p.sendMessage(ChatColor.RED + "[Regios] Max width : 65535. Your size : " + ChatColor.BLUE + width);
			}
			return;
		}
		if (height > 65535) {
			if (p != null) {
				p.sendMessage(ChatColor.RED + "[Regios] The height is too large for a .rbf file!");

				p.sendMessage(ChatColor.RED + "[Regios] Max height : 65535. Your size : " + ChatColor.BLUE + width);
			}
			return;
		}
		if (length > 65535) {
			if (p != null) {
				p.sendMessage(ChatColor.RED + "[Regios] The length is too large for a .rbf file!");

				p.sendMessage(ChatColor.RED + "[Regios] Max length : 65535. Your size : " + ChatColor.BLUE + width);
			}
			return;
		}

		HashMap<String, Tag> backupTag = new HashMap<String, Tag>();

		// Copy
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
					try {
						if(b.getState() instanceof InventoryHolder) {
							containerData.add(((InventoryHolder) b.getState()).getInventory().getContents());
						} else {
							containerData.add(null);
						}
					}
					catch (ClassCastException cce) {
						//The block isn't a container
					}

					if(b.getState() instanceof Sign) {
						signData.add(((Sign)b).getLines());
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
			p.sendMessage(ChatColor.GREEN + "[Regios] Region " + ChatColor.BLUE + backupname + ChatColor.GREEN + " saved to .rbf file successfully!");
		}

		RegionBackupEvent event = new RegionBackupEvent("RegionBackupEvent");
		event.setProperties(r, backupname, p);
		Bukkit.getServer().getPluginManager().callEvent(event);
	}
	
	public void loadBackup(Region r, String backupname, Player p) throws IOException, RegionExistanceException, FileExistanceException, InvalidNBTFormat {
		if(p != null){
			if (!r.canModify(p)) {
				if (p != null) {
					p.sendMessage(ChatColor.RED + "[Regios] You are not permitted to modify this region!");
				}
				return;
			}
		}

		if (r == null) {
			if (p != null) {
				p.sendMessage(ChatColor.RED + "[Regios] That Region does not exist!");
			}
			throw new RegionExistanceException("UNKNOWN");
		}

		File f = new File("plugins" + File.separator + "Regios" + File.separator + "Database" + File.separator + r.getName() + File.separator + "Backups" + File.separator
				+ backupname + ".rbf");

		if (!f.exists()) {
			if (p != null) {
				p.sendMessage(ChatColor.RED + "[Regios] A backup with the name " + ChatColor.BLUE + backupname + ChatColor.RED + " does not exist!");
			}
			throw new FileExistanceException("UNKNOWN", false);		
		}

		if (p != null) {
			p.sendMessage(ChatColor.GREEN + "[Regios] Restoring region from .rbf file...");
		}
		World w = p.getWorld();

		FileInputStream fis = new FileInputStream(f);
		NBTInputStream nbt = new NBTInputStream(new GZIPInputStream(fis));

		CompoundTag backuptag = (CompoundTag) nbt.readTag();
		Map<String, Tag> tagCollection = backuptag.getValue();

		if (!backuptag.getName().equals("RBF")) {
			if (p != null) {
				p.sendMessage(ChatColor.RED + "[Regios] Backup file in unexpected format! Tag does not match 'RBF'.");
			}
			nbt.close();
			throw new InvalidNBTFormat("UNKNOWN", "RBF", backuptag.getName());
		}

		int StartX = (Integer) NBTUtils.getChildTag(tagCollection, "StartX", IntTag.class).getValue();
		int StartY = (Integer) NBTUtils.getChildTag(tagCollection, "StartY", IntTag.class).getValue();
		int StartZ = (Integer) NBTUtils.getChildTag(tagCollection, "StartZ", IntTag.class).getValue();

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
					if(b.getType().equals(Material.CHEST)) { //Added to prevent chests/furnaces/etc from dropping everything in them when regenerating regions -jzx7
						Chest ih = (Chest) b.getState();
						ih.getBlockInventory().clear();
					} else if(b.getType().equals(Material.FURNACE)) {
						Furnace ih = (Furnace) b.getState();
						ih.getInventory().clear();
					} else if(b.getType().equals(Material.DISPENSER)) {
						Dispenser ih = (Dispenser) b.getState();
						ih.getInventory().clear();
					} else if(b.getType().equals(Material.BREWING_STAND)) {
						BrewingStand ih = (BrewingStand) b.getState();
						ih.getInventory().clear();
					}

					b.setTypeId(blocks[index]);
					b.setData(blockData[index]);

					if(b.getType().equals(Material.CHEST)) { //Added to load chest/furnace/etc inventory from backup
						Chest chest = (Chest) b.getState();
						chest.getBlockInventory().setContents(containerData.get(index));
					} else if(b.getType().equals(Material.FURNACE)) {
						Furnace furnace = (Furnace) b.getState();
						furnace.getInventory().setContents(containerData.get(index));
					}else if(b.getType().equals(Material.DISPENSER)) {
						Dispenser dispenser = (Dispenser) b.getState();
						dispenser.getInventory().setContents(containerData.get(index));
					} else if(b.getType().equals(Material.BREWING_STAND)) {
						BrewingStand brew = (BrewingStand) b.getState();
						brew.getInventory().setContents(containerData.get(index));
					} else if(b.getType().equals(Material.SIGN_POST) || b.getType().equals(Material.WALL_SIGN)) {
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

		fis.close();
		nbt.close();

		RegionRestoreEvent event = new RegionRestoreEvent("RegionRestoreEvent");
		event.setProperties(r, backupname, p);
		Bukkit.getServer().getPluginManager().callEvent(event);

		if (p != null) {
			p.sendMessage(ChatColor.GREEN + "[Regios] Region" + ChatColor.BLUE + backupname + ChatColor.GREEN + " restored successfully from .rbf file!");
		}
	}

}
