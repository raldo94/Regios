package net.jzx7.regios.bukkit.block;

import net.jzx7.regiosapi.block.RegiosBiome;

import org.bukkit.block.Biome;

public enum BukkitBiomes implements RegiosBiome {
	
	SWAMPLAND(Biome.SWAMPLAND),
    FOREST(Biome.FOREST),
    TAIGA(Biome.TAIGA),
    DESERT(Biome.DESERT),
    PLAINS(Biome.PLAINS),
    HELL(Biome.HELL),
    SKY(Biome.SKY),
    RIVER(Biome.RIVER),
    EXTREME_HILLS(Biome.EXTREME_HILLS),
    OCEAN(Biome.OCEAN),
    FROZEN_OCEAN(Biome.FROZEN_OCEAN),
    FROZEN_RIVER(Biome.FROZEN_RIVER),
    ICE_FLATS(Biome.ICE_FLATS),
    ICE_MOUNTAINS(Biome.ICE_MOUNTAINS),
    MUSHROOM_ISLAND(Biome.MUSHROOM_ISLAND),
    MUSHROOM_ISLAND_SHORE(Biome.MUSHROOM_ISLAND_SHORE),
    BEACHES(Biome.BEACHES),
    DESERT_HILLS(Biome.DESERT_HILLS),
    FOREST_HILLS(Biome.FOREST_HILLS),
    TAIGA_HILLS(Biome.TAIGA_HILLS),
    JUNGLE(Biome.JUNGLE),
    JUNGLE_HILLS(Biome.JUNGLE_HILLS);

	private Biome bukkitBiome;

    private BukkitBiomes(Biome biome) {
        this.bukkitBiome = biome;
    }
	
	@Override
	public String getName() {
		return bukkitBiome.name();
	}
	
}