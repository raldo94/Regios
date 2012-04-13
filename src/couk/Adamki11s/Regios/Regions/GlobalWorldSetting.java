package couk.Adamki11s.Regios.Regions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import couk.Adamki11s.Regios.Permissions.PermissionsCore;

public class GlobalWorldSetting {

	public String world;

	public boolean invert_protection = false, 
			invert_pvp = false, overridingPvp = false, 
			lightning_enabled = true, 
			stormEnabled = true, 
			creeperExplodes = true, 
			fireEnabled = true, 
			blockForm_enabled = true,
			tntEnabled = true;
	
	public ArrayList<EntityType> creaturesWhoSpawn = new ArrayList<EntityType>();

	public GlobalWorldSetting(String world) {
		this.world = world;
	}
	
	public boolean canCreatureSpawn(EntityType entityType){
		return creaturesWhoSpawn.contains(entityType);
	}
	
	public void addCreatureSpawn(EntityType ct){
		creaturesWhoSpawn.add(ct);
	}
	
	public boolean canBypassWorldChecks(Player p){
		return (PermissionsCore.doesHaveNode(p, "regios.worldprotection.bypass") || PermissionsCore.doesHaveNode(p, "regios." + world + ".bypass") || p.isOp());
	}
	
	public static void writeWorldsToConfiguration(){
		for(World w : Bukkit.getServer().getWorlds()){
			String world = w.getName();
			File root = new File("plugins" + File.separator + "Regios" + File.separator + "Configuration" + File.separator + "WorldConfigurations");
			if(!root.exists()){ root.mkdir(); }
			File f = new File("plugins" + File.separator + "Regios" + File.separator + "Configuration" + File.separator + "WorldConfigurations" + File.separator + world + ".rwc");
			if(!f.exists()){
				System.out.println("[Regios] Creating world configuration for world : " + world);
				try {
					f.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
				FileConfiguration c = YamlConfiguration.loadConfiguration(f);
				c.set(world + ".Protection.ProtectionEnabledOutsideRegions", false);
				c.set(world + ".PvP.EnabledOutsideRegions", true);
				c.set(world + ".PvP.OverrideServerPvP", false);
				c.set(world + ".Protection.FireEnabled", true);
				c.set(world + ".Protection.TNTEnabled", true);
				c.set(world + ".Weather.LightningEnabled", true);
				c.set(world + ".Mobs.Spawning.Chicken", true);
				c.set(world + ".Mobs.Spawning.Cow", true);
				c.set(world + ".Mobs.Spawning.Creeper", true);
				c.set(world + ".Mobs.Spawning.Ghast", true);
				c.set(world + ".Mobs.Spawning.Giant", true);
				c.set(world + ".Mobs.Spawning.Pig", true);
				c.set(world + ".Mobs.Spawning.PigZombie", true);
				c.set(world + ".Mobs.Spawning.Sheep", true);
				c.set(world + ".Mobs.Spawning.Skeleton", true);
				c.set(world + ".Mobs.Spawning.Slime", true);
				c.set(world + ".Mobs.Spawning.Spider", true);
				c.set(world + ".Mobs.Spawning.Squid", true);
				c.set(world + ".Mobs.Spawning.Zombie", true);
				c.set(world + ".Mobs.Spawning.Wolf", true);
				c.set(world + ".Mobs.Spawning.CaveSpider", true);
				c.set(world + ".Mobs.Spawning.Enderman", true);
				c.set(world + ".Mobs.Spawning.Silverfish", true);
				c.set(world + ".Mobs.Spawning.EnderDragon", true);
				c.set(world + ".Mobs.Spawning.Villager", true);
				c.set(world + ".Mobs.Spawning.Blaze", true);
				c.set(world + ".Mobs.Spawning.MushroomCow", true);
				c.set(world + ".Mobs.Spawning.MagmaCube", true);
				c.set(world + ".Mobs.Spawning.Snowman", true);
				c.set(world + ".Mobs.Spawning.IronGolem", true);
				c.set(world + ".Mobs.Spawning.Ocelot", true);
				c.set(world + ".Mobs.Creeper.DoesExplode", true);
				c.set(world + ".Block.BlockForm.Enabled", true);
				try {
					c.save(f);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void loadWorldsFromConfiguration(){
		for(World w : Bukkit.getServer().getWorlds()){
			System.out.println("[Regios] Loading world configuration for world : " + w.getName());
			String world = w.getName();
			File root = new File("plugins" + File.separator + "Regios" + File.separator + "Configuration" + File.separator + "WorldConfigurations");
			if(!root.exists()){ root.mkdir(); }
			File f = new File("plugins" + File.separator + "Regios" + File.separator + "Configuration" + File.separator + "WorldConfigurations" + File.separator + world + ".rwc");
			FileConfiguration c = YamlConfiguration.loadConfiguration(f);
			GlobalWorldSetting gws = new GlobalWorldSetting(world);
			gws.invert_protection = c.getBoolean(world + ".Protection.ProtectionEnabledOutsideRegions", false);
			gws.fireEnabled = c.getBoolean(world + ".Protection.FireEnabled", false);
			gws.tntEnabled = c.getBoolean(world + ".Protection.TNTEnabled", true);
			gws.invert_pvp = c.getBoolean(world + ".PvP.EnabledOutsideRegions", true);
			gws.lightning_enabled = c.getBoolean(world + ".Weather.LightningEnabled", true);
			gws.creeperExplodes = c.getBoolean(world + ".Mobs.Creeper.DoesExplode", true);
			gws.overridingPvp = c.getBoolean(world + ".PvP.OverrideServerPvP", true);
			gws.blockForm_enabled = c.getBoolean(world + ".Block.BlockForm.Enabled", true);
			if(c.getBoolean(world + ".Mobs.Spawning.Chicken", true)){ gws.addCreatureSpawn(EntityType.CHICKEN); }
			if(c.getBoolean(world + ".Mobs.Spawning.Cow", true)){ gws.addCreatureSpawn(EntityType.COW); }
			if(c.getBoolean(world + ".Mobs.Spawning.Creeper", true)){ gws.addCreatureSpawn(EntityType.CREEPER); }
			if(c.getBoolean(world + ".Mobs.Spawning.Ghast", true)){ gws.addCreatureSpawn(EntityType.GHAST); }
			if(c.getBoolean(world + ".Mobs.Spawning.Giant", true)){ gws.addCreatureSpawn(EntityType.GIANT); }
			if(c.getBoolean(world + ".Mobs.Spawning.Pig", true)){ gws.addCreatureSpawn(EntityType.PIG); }
			if(c.getBoolean(world + ".Mobs.Spawning.PigZombie", true)){ gws.addCreatureSpawn(EntityType.PIG_ZOMBIE); }
			if(c.getBoolean(world + ".Mobs.Spawning.Sheep", true)){ gws.addCreatureSpawn(EntityType.SHEEP); }
			if(c.getBoolean(world + ".Mobs.Spawning.Skeleton", true)){ gws.addCreatureSpawn(EntityType.SKELETON); }
			if(c.getBoolean(world + ".Mobs.Spawning.Slime", true)){ gws.addCreatureSpawn(EntityType.SLIME); }
			if(c.getBoolean(world + ".Mobs.Spawning.Spider", true)){ gws.addCreatureSpawn(EntityType.SPIDER); }
			if(c.getBoolean(world + ".Mobs.Spawning.Squid", true)){ gws.addCreatureSpawn(EntityType.SQUID); }
			if(c.getBoolean(world + ".Mobs.Spawning.Zombie", true)){ gws.addCreatureSpawn(EntityType.ZOMBIE); }
			if(c.getBoolean(world + ".Mobs.Spawning.Wolf", true)){ gws.addCreatureSpawn(EntityType.WOLF); }
			if(c.getBoolean(world + ".Mobs.Spawning.CaveSpider", true)){ gws.addCreatureSpawn(EntityType.CAVE_SPIDER); }
			if(c.getBoolean(world + ".Mobs.Spawning.Enderman", true)){ gws.addCreatureSpawn(EntityType.ENDERMAN); }
			if(c.getBoolean(world + ".Mobs.Spawning.Silverfish", true)){ gws.addCreatureSpawn(EntityType.SILVERFISH); }
			if(c.getBoolean(world + ".Mobs.Spawning.EnderDragon", true)){ gws.addCreatureSpawn(EntityType.ENDER_DRAGON); }
			if(c.getBoolean(world + ".Mobs.Spawning.Villager", true)){ gws.addCreatureSpawn(EntityType.VILLAGER); }
			if(c.getBoolean(world + ".Mobs.Spawning.Blaze", true)){ gws.addCreatureSpawn(EntityType.BLAZE); }
			if(c.getBoolean(world + ".Mobs.Spawning.MushroomCow", true)){ gws.addCreatureSpawn(EntityType.MUSHROOM_COW); }
			if(c.getBoolean(world + ".Mobs.Spawning.MagmaCube", true)){ gws.addCreatureSpawn(EntityType.MAGMA_CUBE); }
			if(c.getBoolean(world + ".Mobs.Spawning.Snowman", true)){ gws.addCreatureSpawn(EntityType.SNOWMAN); }
			if(c.getBoolean(world + ".Mobs.Spawning.IronGolem", true)){ gws.addCreatureSpawn(EntityType.IRON_GOLEM); }
			if(c.getBoolean(world + ".Mobs.Spawning.Ocelot", true)){ gws.addCreatureSpawn(EntityType.OCELOT); }
			GlobalRegionManager.addWorldSetting(gws);
		}
	}

}
