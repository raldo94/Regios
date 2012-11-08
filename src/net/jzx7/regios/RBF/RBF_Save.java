package net.jzx7.regios.RBF;

import java.awt.Polygon;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.jzx7.jnbt.ByteArrayTag;
import net.jzx7.jnbt.CompoundTag;
import net.jzx7.jnbt.IntTag;
import net.jzx7.jnbt.ListItemStackArrayTag;
import net.jzx7.jnbt.ListStringArrayTag;
import net.jzx7.jnbt.NBTOutputStream;
import net.jzx7.jnbt.Tag;
import net.jzx7.regios.RegiosPlugin;
import net.jzx7.regios.Permissions.PermissionsCore;
import net.jzx7.regiosapi.events.RegionBackupEvent;
import net.jzx7.regiosapi.exceptions.FileExistanceException;
import net.jzx7.regiosapi.exceptions.RegionExistanceException;
import net.jzx7.regiosapi.regions.CuboidRegion;
import net.jzx7.regiosapi.regions.PolyRegion;
import net.jzx7.regiosapi.regions.Region;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BrewingStand;
import org.bukkit.block.Chest;
import org.bukkit.block.Dispenser;
import org.bukkit.block.Furnace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;


public class RBF_Save extends PermissionsCore {

	private static final char[] ILLEGAL_CHARACTERS = { '/', '\n', '\r', '\t', '\0', '\f', '`', '?', '*', '\\', '<', '>', '|', '\"', ':' };

	public synchronized void startSave(final Region r, final Location l1, final Location l2, final String bckn, final Player p, final boolean sh) {

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
				if (!sh) {
					try {
						saveRegion(r, bckn, p);
					} catch (RegionExistanceException e) {
						e.printStackTrace();
					} catch (FileExistanceException e) {
						e.printStackTrace();
					}
				} else {
					try {
						saveBlueprint(l1, l2, bckn, p);
					} catch (FileExistanceException e) {
						e.printStackTrace();
					}
				}
			}

		}, 1L);
	}

	public synchronized void startSave(final Region r, final int[] xPoints, final int[] zPoints, final int nPoints, final int minY, final int maxY, final String backupname, final Player p, final boolean b) {

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
				if (!b) {
					try {
						saveRegion(r, backupname, p);
					} catch (RegionExistanceException e) {
						e.printStackTrace();
					} catch (FileExistanceException e) {
						e.printStackTrace();
					}
				} else {
					try {
						saveBlueprint(new Polygon(xPoints, zPoints, nPoints), minY, maxY, backupname, p);
					} catch (FileExistanceException e) {
						e.printStackTrace();
					}
				}
			}

		}, 1L);

	}
	
	protected synchronized void saveRegion(Region r, String backupname, Player p) throws RegionExistanceException, FileExistanceException {
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

		HashMap<String, Tag> backuptag = new HashMap<String, Tag>();

		// Copy
		byte[] blockID = new byte[width * height * length];
		byte[] blockData = new byte[width * height * length];
		List<ItemStack[]> containerData = new ArrayList<ItemStack[]>();
		List<String[]> signData = new ArrayList<String[]>();

		int index = 0;

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				for (int z = 0; z < length; z++) {
					blockID[index] = (byte) w.getBlockAt(min.getBlockX() + x, min.getBlockY() + y, min.getBlockZ() + z).getTypeId();
					blockData[index] = w.getBlockAt(min.getBlockX() + x, min.getBlockY() + y, min.getBlockZ() + z).getData();
					if(blockID[index] == 54) { //Save Chest contents for later restoration - jzx7
						Chest chest = (Chest) w.getBlockAt(min.getBlockX() + x, min.getBlockY() + y, min.getBlockZ() + z).getState();
						containerData.add(chest.getBlockInventory().getContents());
					} else if(blockID[index] == 61) { //Save Furnace contents for later restoration - jzx7
						Furnace furnace = (Furnace) w.getBlockAt(min.getBlockX() + x, min.getBlockY() + y, min.getBlockZ() + z).getState();
						containerData.add(furnace.getInventory().getContents());
					} else if(blockID[index] == 23) { //Save Dispenser contents for later restoration - jzx7
						Dispenser dispenser = (Dispenser) w.getBlockAt(min.getBlockX() + x, min.getBlockY() + y, min.getBlockZ() + z).getState();
						containerData.add(dispenser.getInventory().getContents());
					} else if(blockID[index] == 117) { //Save Brewing Stand contents for later restoration - jzx7
						BrewingStand brew = (BrewingStand) w.getBlockAt(min.getBlockX() + x, min.getBlockY() + y, min.getBlockZ() + z).getState();
						containerData.add(brew.getInventory().getContents());
					} else {
						containerData.add(null);
					}
					if(blockID[index] == 63 || blockID[index] == 68) { //Save sign data for later restoration -jzx7
						Sign sign = (Sign) w.getBlockAt(min.getBlockX() + x, min.getBlockY() + y, min.getBlockZ() + z).getState();
						signData.add(sign.getLines());
					} else {
						signData.add(null);
					}
					index++;
				}
			}
		}

		backuptag.put("BlockID", new ByteArrayTag("BlockID", blockID));
		backuptag.put("Data", new ByteArrayTag("Data", blockData));
		backuptag.put("ContainerData", new ListItemStackArrayTag("ContainerData", containerData)); //jzx7
		backuptag.put("SignData", new ListStringArrayTag("SignData", signData)); //jzx7
		backuptag.put("StartX", new IntTag("StartX", min.getBlockX()));
		backuptag.put("StartY", new IntTag("StartY", min.getBlockY()));
		backuptag.put("StartZ", new IntTag("StartZ", min.getBlockZ()));
		backuptag.put("XSize", new IntTag("XSize", width));
		backuptag.put("YSize", new IntTag("YSize", height));
		backuptag.put("ZSize", new IntTag("ZSize", length));

		CompoundTag compoundTag = new CompoundTag("RBF", backuptag);
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
			Location max = new Location(w, Math.max(l1.getX(), l2.getX()), Math.max(l1.getY(), l2.getY()), Math.max(l1.getZ(), l2.getZ())), min = new Location(w, Math.min(
					l1.getX(), l2.getX()), Math.min(l1.getY(), l2.getY()), Math.min(l1.getZ(), l2.getZ()));

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

				}
				p.sendMessage(ChatColor.RED + "[Regios] Max length : 65535. Your size : " + ChatColor.BLUE + width);
				return;
			}

			HashMap<String, Tag> backuptag = new HashMap<String, Tag>();

			byte[] blockID = new byte[width * height * length];
			byte[] blockData = new byte[width * height * length];
			List<ItemStack[]> containerData = new ArrayList<ItemStack[]>();
			List<String[]> signData = new ArrayList<String[]>();

			int index = 0;

			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					for (int z = 0; z < length; z++) {
						blockID[index] = (byte) w.getBlockAt(min.getBlockX() + x, min.getBlockY() + y, min.getBlockZ() + z).getTypeId();
						blockData[index] = w.getBlockAt(min.getBlockX() + x, min.getBlockY() + y, min.getBlockZ() + z).getData();
						if(blockID[index] == 54) { //Save Chest contents for later restoration - jzx7
							Chest chest = (Chest) w.getBlockAt(min.getBlockX() + x, min.getBlockY() + y, min.getBlockZ() + z).getState();
							containerData.add(chest.getBlockInventory().getContents());
						} else if(blockID[index] == 61) { //Save Furnace contents for later restoration - jzx7
							Furnace furnace = (Furnace) w.getBlockAt(min.getBlockX() + x, min.getBlockY() + y, min.getBlockZ() + z).getState();
							containerData.add(furnace.getInventory().getContents());
						} else if(blockID[index] == 23) { //Save Dispenser contents for later restoration - jzx7
							Dispenser dispenser = (Dispenser) w.getBlockAt(min.getBlockX() + x, min.getBlockY() + y, min.getBlockZ() + z).getState();
							containerData.add(dispenser.getInventory().getContents());
						} else if(blockID[index] == 117) { //Save Brewing Stand contents for later restoration - jzx7
							BrewingStand brew = (BrewingStand) w.getBlockAt(min.getBlockX() + x, min.getBlockY() + y, min.getBlockZ() + z).getState();
							containerData.add(brew.getInventory().getContents());
						} else {
							containerData.add(null);
						}
						if(blockID[index] == 63 || blockID[index] == 68) { //Save sign data for later restoration -jzx7
							Sign sign = (Sign) w.getBlockAt(min.getBlockX() + x, min.getBlockY() + y, min.getBlockZ() + z).getState();
							signData.add(sign.getLines());
						} else {
							signData.add(null);
						}
						index++;
					}
				}
			}

			backuptag.put("BlockID", new ByteArrayTag("BlockID", blockID));
			backuptag.put("Data", new ByteArrayTag("Data", blockData));
			backuptag.put("ContainerData", new ListItemStackArrayTag("ContainerData", containerData)); //jzx7
			backuptag.put("SignData", new ListStringArrayTag("SignData", signData)); //jzx7
			backuptag.put("StartX", new IntTag("StartX", min.getBlockX()));
			backuptag.put("StartY", new IntTag("StartY", min.getBlockY()));
			backuptag.put("StartZ", new IntTag("StartZ", min.getBlockZ()));
			backuptag.put("XSize", new IntTag("XSize", width));
			backuptag.put("YSize", new IntTag("YSize", height));
			backuptag.put("ZSize", new IntTag("ZSize", length));

			CompoundTag compoundTag = new CompoundTag("BLP", backuptag);

			NBTOutputStream nbt = new NBTOutputStream(new FileOutputStream(f));
			nbt.writeTag(compoundTag);
			nbt.close();
			if (p != null) {
				p.sendMessage(ChatColor.GREEN + "[Regios] Blueprint " + ChatColor.BLUE + backupname + ChatColor.GREEN + " saved to .blp file successfully!");
			}
		} catch (Exception ex) {
			if (p != null) {
				p.sendMessage(ChatColor.RED + "[Regios] Error saving region! Stack trace printed in console.");
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

			HashMap<String, Tag> backuptag = new HashMap<String, Tag>();

			byte[] blockID = new byte[width * height * length];
			byte[] blockData = new byte[width * height * length];
			List<ItemStack[]> containerData = new ArrayList<ItemStack[]>();
			List<String[]> signData = new ArrayList<String[]>();

			int index = 0;

			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					for (int z = 0; z < length; z++) {
						if (polygon.contains(x, z)) {
							blockID[index] = (byte) w.getBlockAt((int) rect.getMinX() + x, minY + y, (int) rect.getMinY() + z).getTypeId();
							blockData[index] = w.getBlockAt((int) rect.getMinX() + x, minY + y, (int) rect.getMinY() + z).getData();
							if(blockID[index] == 54) { //Save Chest contents for later restoration - jzx7
								Chest chest = (Chest) w.getBlockAt((int) rect.getMinX() + x, minY + y, (int) rect.getMinY() + z).getState();
								containerData.add(chest.getBlockInventory().getContents());
							} else if(blockID[index] == 61) { //Save Furnace contents for later restoration - jzx7
								Furnace furnace = (Furnace) w.getBlockAt((int) rect.getMinX() + x, minY + y, (int) rect.getMinY() + z).getState();
								containerData.add(furnace.getInventory().getContents());
							} else if(blockID[index] == 23) { //Save Dispenser contents for later restoration - jzx7
								Dispenser dispenser = (Dispenser) w.getBlockAt((int) rect.getMinX() + x, minY + y, (int) rect.getMinY() + z).getState();
								containerData.add(dispenser.getInventory().getContents());
							} else if(blockID[index] == 117) { //Save Brewing Stand contents for later restoration - jzx7
								BrewingStand brew = (BrewingStand) w.getBlockAt((int) rect.getMinX() + x, minY + y, (int) rect.getMinY() + z).getState();
								containerData.add(brew.getInventory().getContents());
							} else {
								containerData.add(null);
							}
							if(blockID[index] == 63 || blockID[index] == 68) { //Save sign data for later restoration -jzx7
								Sign sign = (Sign) w.getBlockAt((int) rect.getMinX() + x, minY + y, (int) rect.getMinY() + z).getState();
								signData.add(sign.getLines());
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

			backuptag.put("BlockID", new ByteArrayTag("BlockID", blockID));
			backuptag.put("Data", new ByteArrayTag("Data", blockData));
			backuptag.put("ContainerData", new ListItemStackArrayTag("ContainerData", containerData)); //jzx7
			backuptag.put("SignData", new ListStringArrayTag("SignData", signData)); //jzx7
			backuptag.put("StartX", new IntTag("StartX", (int) rect.getMinX()));
			backuptag.put("StartY", new IntTag("StartY", minY));
			backuptag.put("StartZ", new IntTag("StartZ", (int) rect.getMinY()));
			backuptag.put("XSize", new IntTag("XSize", width));
			backuptag.put("YSize", new IntTag("YSize", height));
			backuptag.put("ZSize", new IntTag("ZSize", length));

			CompoundTag compoundTag = new CompoundTag("BLP", backuptag);

			NBTOutputStream nbt = new NBTOutputStream(new FileOutputStream(f));
			nbt.writeTag(compoundTag);
			nbt.close();
			if (p != null) {
				p.sendMessage(ChatColor.GREEN + "[Regios] Blueprint " + ChatColor.BLUE + backupname + ChatColor.GREEN + " saved to .blp file successfully!");
			}
		} catch (Exception ex) {
			if (p != null) {
				p.sendMessage(ChatColor.RED + "[Regios] Error saving region! Stack trace printed in console.");
			}
			ex.printStackTrace();
		}
	}
}
