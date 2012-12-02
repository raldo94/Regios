package net.jzx7.regios.RBF;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.DataFormatException;
import java.util.zip.GZIPInputStream;

import net.jzx7.jnbt.ByteArrayTag;
import net.jzx7.jnbt.ByteTag;
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
import net.jzx7.regiosapi.exceptions.FileExistanceException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.DoubleChestInventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BlockVector;

public class Schematic {

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
				saveSchematic(l1, l2, bckn, p);
			}

		}, 1L);
	}

	public void saveSchematic(Location l1, Location l2, String backupname, Player p) {
		try {
			if (p != null) {
				p.sendMessage(ChatColor.GREEN + "[Regios] Creating .schematic file...");
			}
			File f = new File("plugins" + File.separator + "Regios" + File.separator + "Schematics" + File.separator + backupname + ".schematic");

			if (!f.exists()) {
				f.createNewFile();
			} else {
				if (p != null) {
					p.sendMessage(ChatColor.RED + "[Regios] A schematic file with the name " + ChatColor.BLUE + backupname + ChatColor.RED + " already exists!");
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
					p.sendMessage(ChatColor.RED + "[Regios] The width is too large for a .schematic file!");
					p.sendMessage(ChatColor.RED + "[Regios] Max width : 65535. Your size : " + ChatColor.BLUE + width);
				}
				return;
			}
			if (height > 65535) {
				if (p != null) {
					p.sendMessage(ChatColor.RED + "[Regios] The height is too large for a .schematic file!");
					p.sendMessage(ChatColor.RED + "[Regios] Max height : 65535. Your size : " + ChatColor.BLUE + width);
				}
				return;
			}
			if (length > 65535) {
				if (p != null) {
					p.sendMessage(ChatColor.RED + "[Regios] The length is too large for a .schematic file!");
					p.sendMessage(ChatColor.RED + "[Regios] Max length : 65535. Your size : " + ChatColor.BLUE + width);
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
						blockID[index] = (byte) w.getBlockAt(min.getBlockX() + x, min.getBlockY() + y, min.getBlockZ() + z).getTypeId();
						blockData[index] = w.getBlockAt(min.getBlockX() + x, min.getBlockY() + y, min.getBlockZ() + z).getData();
						index++;
					}
				}
			}

			for (int x = 0; x < width; ++x) {
				for (int y = 0; y < height; ++y) {
					for (int z = 0; z < length; ++z) {
						index = y * width * length + z * width + x;
						Block b = w.getBlockAt(min.getBlockX() + x, min.getBlockY() + y, min.getBlockZ() + z);
						
						blockID[index] = (byte) b.getTypeId();
						blockData[index] = (byte) b.getData();

						if (b.getState() instanceof InventoryHolder) {
							InventoryHolder ih = (InventoryHolder) b.getState();

							Map<String, Tag> values = new HashMap<String, Tag>();
							if (ih.getInventory() instanceof DoubleChestInventory) {
								if (b.getRelative(BlockFace.NORTH).getTypeId() == Material.CHEST.getId() || b.getRelative(BlockFace.EAST).getTypeId() == Material.CHEST.getId()) {
									values.put("Items", new ListTag("Items", CompoundTag.class, serialize(((DoubleChestInventory) ih.getInventory()).getRightSide().getContents())));
								} else {
									values.put("Items", new ListTag("Items", CompoundTag.class, serialize(((DoubleChestInventory) ih.getInventory()).getLeftSide().getContents())));
								}
							} else {
								values.put("Items", new ListTag("Items", CompoundTag.class, serialize(ih.getInventory().getContents())));
							}
							if (values != null) {
								values.put("id", new StringTag("id", "Chest"));
								values.put("x", new IntTag("x", x));
								values.put("y", new IntTag("y", y));
								values.put("z", new IntTag("z", z));
								CompoundTag tileEntityTag =
										new CompoundTag("TileEntity", values);
								tileEntities.add(tileEntityTag);
							}

						} else if (b.getState() instanceof Sign) {
							Sign sign = (Sign) b.getState();

							Map<String, Tag> values = new HashMap<String, Tag>();
							values.put("Text1", new StringTag("Text1", sign.getLine(0)));
							values.put("Text2", new StringTag("Text2", sign.getLine(1)));
							values.put("Text3", new StringTag("Text3", sign.getLine(2)));
							values.put("Text4", new StringTag("Text4", sign.getLine(3)));
							if (values != null) {
								values.put("id", new StringTag("id", "Sign"));
								values.put("x", new IntTag("x", x));
								values.put("y", new IntTag("y", y));
								values.put("z", new IntTag("z", z));
								CompoundTag tileEntityTag =
										new CompoundTag("TileEntity", values);
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
				p.sendMessage(ChatColor.GREEN + "[Regios] Schematic " + ChatColor.BLUE + backupname + ChatColor.GREEN + " saved to .schematic file successfully!");
			}
		} catch (Exception ex) {
			if (p != null) {
				p.sendMessage(ChatColor.RED + "[Regios] Error saving schematic! Stack trace printed in console.");
			}
			ex.printStackTrace();
		}
	}

	public void loadSchematic(String sharename, Player p, Location l) throws IOException, DataFormatException {
		if (RBF_Core.undoCache.containsKey(p.getName())) {
			RBF_Core.undoCache.remove(p.getName());
		}

		ArrayList<PBD> blockss = new ArrayList<PBD>();
		RBF_Core.undoCache.put(p.getName(), blockss);

		File f = new File("plugins" + File.separator + "Regios" + File.separator + "Schematics" + File.separator + sharename + ".schematic");

		if (!f.exists()) {
			p.sendMessage(ChatColor.RED + "[Regios] A blueprint file with the name " + ChatColor.BLUE + sharename + ChatColor.RED + " does not exist!");
			return;
		}

		p.sendMessage(ChatColor.GREEN + "[Regios] Restoring region from " + sharename + ".schematic file...");

		World w = p.getWorld();

		FileInputStream fis = new FileInputStream(f);
		NBTInputStream nbt = new NBTInputStream(new GZIPInputStream(fis));

		CompoundTag backuptag = (CompoundTag) nbt.readTag();
		Map<String, Tag> tagCollection = backuptag.getValue();

		if (!backuptag.getName().equals("Schematic")) {
			p.sendMessage(ChatColor.RED + "[Regios] Schematic file in unexpected format! Tag does not match 'Schematic'.");
		}

		short width = NBTUtils.getChildTag(tagCollection, "Width", ShortTag.class).getValue();
		short length = NBTUtils.getChildTag(tagCollection, "Length", ShortTag.class).getValue();
		short height = NBTUtils.getChildTag(tagCollection, "Height", ShortTag.class).getValue();

		// Check type of Schematic
		String materials = NBTUtils.getChildTag(tagCollection, "Materials", StringTag.class).getValue();
		if (!materials.equals("Alpha")) {
			throw new DataFormatException("Schematic file is not an Alpha schematic");
		}

		byte[] blocks = NBTUtils.getChildTag(tagCollection, "Blocks", ByteArrayTag.class).getValue();
		byte[] blockData = NBTUtils.getChildTag(tagCollection, "Data", ByteArrayTag.class).getValue();

		List<Tag> tileEntities = NBTUtils.getChildTag(tagCollection, "TileEntities", ListTag.class).getValue();
		Map<BlockVector, Map<String, Tag>> tileEntitiesMap =
				new HashMap<BlockVector, Map<String, Tag>>();

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

			BlockVector vec = new BlockVector(x, y, z);
			tileEntitiesMap.put(vec, values);
		}

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				for (int z = 0; z < length; z++) {
					BlockVector pt = new BlockVector(x, y, z);
					Block b = w.getBlockAt(pt.add(l.toVector()).toLocation(w));
					blockss.add(new PBD(b));
				}
			}
		}

		for (int x = 0; x < width; ++x) {
			for (int y = 0; y < height; ++y) {
				for (int z = 0; z < length; ++z) {
					int index = y * width * length + z * width + x;
					BlockVector pt = new BlockVector(x, y, z);
					Block b = w.getBlockAt(new BlockVector(x, y, z).add(l.toVector()).toLocation(w));
					if (b.getState() instanceof InventoryHolder) {
						((InventoryHolder) b.getState()).getInventory().clear();
					}

					b.setTypeId(blocks[index] & 0xFF);
					b.setData(blockData[index]);

					if (b.getState() instanceof InventoryHolder && tileEntitiesMap.containsKey(pt)) {
						if (tileEntitiesMap.get(pt) == null) {
							return;
						}

						InventoryHolder ih = (InventoryHolder) b.getState();

						Tag t = tileEntitiesMap.get(pt).get("id");
						if (!(t instanceof StringTag) || !((StringTag) t).getValue().equals("Chest")) {
							throw new DataFormatException("'Chest' tile entity expected");
						}

						List<CompoundTag> items = new ArrayList<CompoundTag>();
						for (Tag tag : NBTUtils.getChildTag(tileEntitiesMap.get(pt), "Items", ListTag.class).getValue()) {
							if (!(tag instanceof CompoundTag)) {
								throw new DataFormatException("CompoundTag expected as child tag of InventoryHolder's Items");
							}

							items.add((CompoundTag) tag);
						}
						if (ih.getInventory() instanceof DoubleChestInventory) {
							if (b.getRelative(BlockFace.NORTH).getTypeId() == Material.CHEST.getId() || b.getRelative(BlockFace.EAST).getTypeId() == Material.CHEST.getId()) {
								((DoubleChestInventory) ih.getInventory()).getRightSide().setContents(deserialize(items, ((DoubleChestInventory) ih.getInventory()).getRightSide().getSize()));
							} else {
								((DoubleChestInventory) ih.getInventory()).getLeftSide().setContents(deserialize(items, ((DoubleChestInventory) ih.getInventory()).getLeftSide().getSize()));
							}
						} else {
							ih.getInventory().setContents(deserialize(items, ih.getInventory().getSize()));
						}
					}

					if (b.getState() instanceof Sign && tileEntitiesMap.containsKey(pt)) {
						if (tileEntitiesMap.get(pt) == null) {
							return;
						}

						Sign sign = (Sign) b.getState();
						
						Tag t;

						t = tileEntitiesMap.get(pt).get("id");
						if (!(t instanceof StringTag) || !((StringTag) t).getValue().equals("Sign")) {
							throw new DataFormatException("'Sign' tile entity expected");
						}

						t = tileEntitiesMap.get(pt).get("Text1");
						if (t instanceof StringTag) {
							sign.setLine(0, ((StringTag) t).getValue());
						}

						t = tileEntitiesMap.get(pt).get("Text2");
						if (t instanceof StringTag) {
							sign.setLine(1, ((StringTag) t).getValue());
						}

						t = tileEntitiesMap.get(pt).get("Text3");
						if (t instanceof StringTag) {
							sign.setLine(2, ((StringTag) t).getValue());
						}

						t = tileEntitiesMap.get(pt).get("Text4");
						if (t instanceof StringTag) {
							sign.setLine(3, ((StringTag) t).getValue());
						}
						sign.update();
					}
				}
			}
		}

		RBF_Core.undoCache.put(p.getName(), blockss);

		fis.close();
		nbt.close();

		p.sendMessage(ChatColor.GREEN + "[Regios]Schematic " + ChatColor.BLUE + sharename + ChatColor.GREEN + " loaded successfully from .schematic file!");
	}

	public List<CompoundTag> serialize(ItemStack[] items) {
		List<CompoundTag> tags = new ArrayList<CompoundTag>();
		for (int i = 0; i < items.length; ++i) {
			if (items[i] != null) {
				Map<String, Tag> tagData = new HashMap<String, Tag>();
				tagData.put("id", new ShortTag("id", (short) items[i].getTypeId()));
				tagData.put("Damage", new ShortTag("Damage", items[i].getDurability()));
				tagData.put("Count", new ByteTag("Count", (byte) items[i].getAmount()));
				if (items[i].getEnchantments().size() > 0) {
					List<CompoundTag> enchantmentList = new ArrayList<CompoundTag>();
					for(Map.Entry<Enchantment, Integer> entry : items[i].getEnchantments().entrySet()) {
						Map<String, Tag> enchantment = new HashMap<String, Tag>();
						enchantment.put("id", new ShortTag("id", (short) entry.getKey().getId()));
						enchantment.put("lvl", new ShortTag("lvl", entry.getValue().shortValue()));
						enchantmentList.add(new CompoundTag(null, enchantment));
					}

					Map<String, Tag> auxData = new HashMap<String, Tag>();
					auxData.put("ench", new ListTag("ench", CompoundTag.class, enchantmentList));
					tagData.put("tag", new CompoundTag("tag", auxData));
				}
				tagData.put("Slot", new ByteTag("Slot", (byte) i));
				tags.add(new CompoundTag("", tagData));
			}
		}
		return tags;
	}
	
	public ItemStack[] deserialize(List<CompoundTag> items, int invSize) {
		ItemStack[] stacks = new ItemStack[invSize];
		for (CompoundTag tag : items) {
			Map<String, Tag> itemTag = tag.getValue();
			short id = NBTUtils.getChildTag(itemTag, "id", ShortTag.class).getValue();
			short damage = NBTUtils.getChildTag(itemTag, "Damage", ShortTag.class).getValue();
			byte count = NBTUtils.getChildTag(itemTag, "Count", ByteTag.class).getValue();

			ItemStack stack = new ItemStack(id, count, damage);

			if (itemTag.containsKey("tag")) {
				Map<String, Tag> auxData = NBTUtils.getChildTag(itemTag, "tag", CompoundTag.class).getValue();
				ListTag ench = (ListTag)auxData.get("ench");
				for(Tag e : ench.getValue()) {
					Map<String, Tag> vars = ((CompoundTag) e).getValue();
					short enchId = NBTUtils.getChildTag(vars, "id", ShortTag.class).getValue();
					short enchLevel = NBTUtils.getChildTag(vars, "lvl", ShortTag.class).getValue();
					stack.addEnchantment(Enchantment.getById((int) enchId), (int) enchLevel);
				}
			}
			byte slot = NBTUtils.getChildTag(itemTag, "Slot", ByteTag.class).getValue();
			if (slot >= 0 && slot < stacks.length) {
				stacks[slot] = stack;
			}
		}
		return stacks;
	}
}
