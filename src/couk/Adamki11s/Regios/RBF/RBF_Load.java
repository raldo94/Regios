package couk.Adamki11s.Regios.RBF;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BrewingStand;
import org.bukkit.block.Chest;
import org.bukkit.block.Dispenser;
import org.bukkit.block.Furnace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import couk.Adamki11s.Regios.CustomEvents.RegionRestoreEvent;
import couk.Adamki11s.Regios.CustomExceptions.FileExistanceException;
import couk.Adamki11s.Regios.CustomExceptions.InvalidNBTFormat;
import couk.Adamki11s.Regios.CustomExceptions.RegionExistanceException;
import couk.Adamki11s.Regios.Permissions.PermissionsCore;
import couk.Adamki11s.Regios.Regions.Region;
import couk.Adamki11s.jnbt.ByteArrayTag;
import couk.Adamki11s.jnbt.CompoundTag;
import couk.Adamki11s.jnbt.IntTag;
import couk.Adamki11s.jnbt.ListItemStackArrayTag;
import couk.Adamki11s.jnbt.ListStringArrayTag;
import couk.Adamki11s.jnbt.NBTInputStream;
import couk.Adamki11s.jnbt.Tag;

public class RBF_Load extends PermissionsCore {

	private Tag getChildTag(Map<String, Tag> items, String key, Class<? extends Tag> expected) {
		Tag tag = items.get(key);
		return tag;
	}

	@SuppressWarnings("unchecked")
	public void loadRegion(Region r, String backupname, Player p) throws IOException, RegionExistanceException, FileExistanceException, InvalidNBTFormat {
		if(p != null){
			if (!super.canModify(r, p)) {
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
			throw new InvalidNBTFormat("UNKNOWN", "RBF", backuptag.getName());
		}

		int StartX = (Integer) getChildTag(tagCollection, "StartX", IntTag.class).getValue();
		int StartY = (Integer) getChildTag(tagCollection, "StartY", IntTag.class).getValue();
		int StartZ = (Integer) getChildTag(tagCollection, "StartZ", IntTag.class).getValue();

		int width = (Integer) getChildTag(tagCollection, "XSize", IntTag.class).getValue();
		int height = (Integer) getChildTag(tagCollection, "YSize", IntTag.class).getValue();
		int length = (Integer) getChildTag(tagCollection, "ZSize", IntTag.class).getValue();

		byte[] blocks = (byte[]) getChildTag(tagCollection, "BlockID", ByteArrayTag.class).getValue();
		byte[] blockData = (byte[]) getChildTag(tagCollection, "Data", ByteArrayTag.class).getValue();
		List<ItemStack[]> containerData = (List<ItemStack[]>) getChildTag(tagCollection, "ContainerData", ListItemStackArrayTag.class).getValue();
		List<String[]> signData = (List<String[]>) getChildTag(tagCollection, "SignData", ListStringArrayTag.class).getValue();

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

					b.setTypeId((int) blocks[index]);
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
