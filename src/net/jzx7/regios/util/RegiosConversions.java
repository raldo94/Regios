package net.jzx7.regios.util;

import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import net.jzx7.regios.bukkit.block.BukkitBiomes;
import net.jzx7.regios.bukkit.entity.BukkitPlayer;
import net.jzx7.regios.bukkit.worlds.BukkitWorld;
import net.jzx7.regios.entity.PlayerManager;
import net.jzx7.regios.worlds.WorldManager;
import net.jzx7.regiosapi.block.RegiosBiome;
import net.jzx7.regiosapi.entity.RegiosPlayer;
import net.jzx7.regiosapi.inventory.RegiosItemStack;
import net.jzx7.regiosapi.location.RegiosPoint;
import net.jzx7.regiosapi.worlds.RegiosWorld;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class RegiosConversions {

	private static final WorldManager wm = new WorldManager();
	private static final PlayerManager pm = new PlayerManager();

	public static RegiosPoint getPoint(Location l) {
		return new RegiosPoint(getRegiosWorld(l.getWorld()), l.getX(), l.getY(), l.getZ());
	}

	public static Location getLocation(RegiosPoint l) {
		return new Location(((BukkitWorld) l.getRegiosWorld()).getWorld(), l.getX(), l.getY(), l.getZ(), l.getYaw(), l.getPitch());
	}

	public static RegiosPlayer getRegiosPlayer(Player p) {
		if (p == null) {
			return null;
		}
		return getRegiosPlayer(p.getName());
	}

	public static RegiosPlayer getRegiosPlayer(String name) {
		RegiosPlayer player = pm.getRegiosPlayer(name);
		if (player == null) {
			player = new BukkitPlayer(Bukkit.getPlayer(name));
			pm.addRegiosPlayer(name, player);
		}
		return player;
	}

	public static Player getPlayer(RegiosPlayer p) {
		return ((BukkitPlayer) p).getPlayer();
	}

	public static RegiosWorld getRegiosWorld(World w) {
		if (w == null) {
			return null;
		}
		return getRegiosWorld(w.getUID());
	}

	public static RegiosWorld getRegiosWorld(UUID id) {
		RegiosWorld world = wm.getRegiosWorld(id);
		if (world == null) {
			world = new BukkitWorld(Bukkit.getServer().getWorld(id));
			wm.addRegiosWorld(id, world);
		}

		return world;
	}

	public static RegiosWorld getRegiosWorld(String name) {
		return getRegiosWorld(Bukkit.getWorld(name));
	}

	public static void loadServerWorlds() {
		RegiosWorld rw;
		for (World w : Bukkit.getWorlds()) {
			rw = getRegiosWorld(w.getName());
			wm.addRegiosWorld(w.getUID(), rw);
		}
	}

	public static World getWorld(RegiosWorld w) {
		return ((BukkitWorld) w).getWorld();
	}

	public static RegiosItemStack getRegiosItemStack(ItemStack is) {
		return new RegiosItemStack(is.getTypeId(), is.getAmount(), is.getDurability());
	}

	public static ItemStack getItemStack(RegiosItemStack ris) {
		return new ItemStack(ris.getId(), ris.getAmount(), ris.getData());
	}

	public static RegiosBiome getBiome(String s)
	{
		for (RegiosBiome b : BukkitBiomes.values()) {
			if (b.getName().equalsIgnoreCase(s)) {
				return b;
			}

		}

		for (RegiosBiome b : BukkitBiomes.values()) {
			if (b.getName().toLowerCase().contains(s.toLowerCase())) {
				return b;
			}
		}

		return null;
	}

	public static String listBiomes() {
		String biomes = "";

		for (RegiosBiome b : BukkitBiomes.values()) {
			biomes += b.getName() + ", ";
		}

		return biomes.substring(0, biomes.length() - 1);
	}

	public static String getMaterialName(int id) {
		return Material.getMaterial(id).name();
	}

	public static RegiosItemStack[] convertItemStack(ItemStack[] bukkitStackArray) {
		int invSize = bukkitStackArray.length;;
		RegiosItemStack[] ris = new RegiosItemStack[invSize];

		for (int i = 0; i < invSize; ++i) {
			ItemStack bukkitStack = bukkitStackArray[i];
			if (bukkitStack != null && bukkitStack.getTypeId() > 0) {
				ris[i] = new RegiosItemStack(
						bukkitStack.getTypeId(),
						bukkitStack.getAmount(),
						bukkitStack.getDurability());
				try {
					for (Map.Entry<Enchantment, Integer> entry : bukkitStack.getEnchantments().entrySet()) {
						ris[i].getEnchantments().put(entry.getKey().getId(), entry.getValue());
					}
				} catch (Throwable ignore) {}
			}
		}

		return ris;
	}

	public static ItemStack[] convertRegiosItemStack(RegiosItemStack[] regiosStackArray) {
		int invSize = regiosStackArray.length;;
		ItemStack[] is = new ItemStack[invSize];

		for (int i = 0; i < invSize; ++i) {
			RegiosItemStack regiosStack = regiosStackArray[i];
			if (regiosStack != null && regiosStack.getId() > 0) {
				is[i] = new ItemStack(
						regiosStack.getId(),
						regiosStack.getAmount(),
						regiosStack.getData());
				try {
					for (Entry<Integer, Integer> entry : regiosStack.getEnchantments().entrySet()) {
						is[i].addEnchantment(Enchantment.getById(entry.getKey()), entry.getValue());
					}
				} catch (Throwable ignore) {}
			}
		}

		return is;
	}

	public static int getEntityTypeID(String name) {
		return EntityType.valueOf(name).getTypeId();
	}
}