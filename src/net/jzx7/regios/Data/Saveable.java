package net.jzx7.regios.Data;

import java.io.File;
import java.io.IOException;

import net.jzx7.regiosapi.regions.CuboidRegion;
import net.jzx7.regiosapi.regions.PolyRegion;
import net.jzx7.regiosapi.regions.Region;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import couk.Adamki11s.Extras.Cryptography.ExtrasCryptography;

public class Saveable  {
	
	ExtrasCryptography extCrypt = new ExtrasCryptography();
	
	private final File root = new File("plugins" + File.separator + "Regios"),
			 db_root = new File(root + File.separator + "Database");
	
	public void saveWorlds(){
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
				c.set(world + ".Protection.FireSpreadEnabled", true);
				c.set(world + ".Protection.ExplosionsEnabled", true);
				c.set(world + ".Protection.DragonProtect", true);
				c.set(world + ".Protection.EndermanBlock", false);
				c.set(world + ".Protection.DragonCreatesPortal", true);
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
	
	public synchronized void saveRegion(Region r){
		
		File region_root = new File(db_root + File.separator + r.getName());
		File region_core = new File(region_root + File.separator + r.getName() + ".rz");
		
		region_root.mkdir();
		
		try {
			region_core.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("Saving Region '" + r.getName() + "' @ " + region_core.getAbsoluteFile().toString());
		
		FileConfiguration c = YamlConfiguration.loadConfiguration(region_core);
		
		c.set("Region.Messages.WelcomeMessage", r.getWelcomeMessage().toString());
		c.set("Region.Messages.LeaveMessage", r.getLeaveMessage().toString());
		c.set("Region.Messages.ProtectionMessage", r.getProtectionMessage().toString());
		c.set("Region.Messages.PreventEntryMessage", r.getPreventEntryMessage().toString());
		c.set("Region.Messages.PreventExitMessage", r.getPreventExitMessage().toString());
		c.set("Region.Messages.ShowPvpWarning", Boolean.valueOf(r.isShowPvpWarning()));
		
		c.set("Region.Messages.ShowWelcomeMessage", Boolean.valueOf(r.isShowWelcomeMessage()));
		c.set("Region.Messages.ShowLeaveMessage", Boolean.valueOf(r.isShowLeaveMessage()));
		c.set("Region.Messages.ShowProtectionMessage", Boolean.valueOf(r.isShowProtectionMessage()));
		c.set("Region.Messages.ShowPreventEntryMessage", Boolean.valueOf(r.isShowPreventEntryMessage()));
		c.set("Region.Messages.ShowPreventExitMessage", Boolean.valueOf(r.isShowPreventExitMessage()));
		
		c.set("Region.Modes.ItemControlMode", r.getItemMode().toString());
		c.set("Region.Modes.ProtectionMode", r.getProtectionMode().toString());
		c.set("Region.Modes.PreventEntryMode", r.getPreventEntryMode().toString());
		c.set("Region.Modes.PreventExitMode", r.getPreventExitMode().toString());
		
		c.set("Region.Inventory.PermWipeOnEnter", r.isPermWipeOnEnter());
		c.set("Region.Inventory.PermWipeOnExit", r.isPermWipeOnExit());
		c.set("Region.Inventory.WipeAndCacheOnEnter", r.isWipeAndCacheOnEnter());
		c.set("Region.Inventory.WipeAndCacheOnExit", r.isWipeAndCacheOnExit());
		
		c.set("Region.Command.ForceCommand", r.isForceCommand());
		c.set("Region.Command.CommandSet", "");
		
		c.set("Region.Permissions.TemporaryCache.AddNodes", "");
		c.set("Region.Permissions.TemporaryCache.RemoveNodes", "");
		c.set("Region.Permissions.PermanentCache.AddNodes", "");
		c.set("Region.Permissions.PermanentCache.RemoveNodes", "");
		
		c.set("Region.Permissions.TempGroups.AddGroups", "");
		c.set("Region.Permissions.TempGroups.RemoveGroups", "");
		c.set("Region.Permissions.PermGroups.AddGroups", "");
		c.set("Region.Permissions.PermGroups.RemoveGroups", "");
		
		c.set("Region.General.Protected.General", Boolean.valueOf(r.isProtected()));
		c.set("Region.General.Protected.BlockBreak", Boolean.valueOf(r.is_protectionBreak()));
		c.set("Region.General.Protected.BlockPlace", Boolean.valueOf(r.is_protectionPlace()));
		c.set("Region.General.FireProtection", Boolean.valueOf(r.isFireProtection()));
		c.set("Region.General.FireSpread", Boolean.valueOf(r.isFireSpread()));
		c.set("Region.General.ExplosionsEnabled", Boolean.valueOf(r.isExplosionsEnabled()));
		c.set("Region.General.EndermanBlock", Boolean.valueOf(r.isBlockEndermanMod()));
		c.set("Region.General.PreventEntry", Boolean.valueOf(r.isPreventEntry()));
		c.set("Region.General.PreventExit", Boolean.valueOf(r.isPreventExit()));
		c.set("Region.General.PreventInteraction", Boolean.valueOf(r.isPreventInteraction()));
		c.set("Region.General.DoorsLocked", Boolean.valueOf(r.areDoorsLocked()));
		c.set("Region.General.ChestsLocked", Boolean.valueOf(r.areChestsLocked()));
		c.set("Region.General.Password.Enabled", Boolean.valueOf(r.isPasswordEnabled()));
		
		if(r.getPassword().length() > 3){
			c.set("Region.General.Password.Password", extCrypt.computeSHA2_384BitHash(r.getPassword().toString()));
		} else {
			c.set("Region.General.Password.Password", "");
		}
		
		c.set("Region.Other.MobSpawns", Boolean.valueOf(r.canMobsSpawn()));
		c.set("Region.Other.MonsterSpawns", Boolean.valueOf(r.canMonstersSpawn()));
		c.set("Region.Other.PvP", Boolean.valueOf(r.isPvp()));
		c.set("Region.Other.HealthEnabled", Boolean.valueOf(r.isHealthEnabled()));
		c.set("Region.Other.HealthRegen", r.getHealthRegen());
		c.set("Region.Other.LSPS", r.getLSPS());
		c.set("Region.Other.VelocityWarp", r.getVelocityWarp());
		
		c.set("Region.Essentials.Owner", r.getOwner().toString());
		c.set("Region.Essentials.SubOwners", r.getSubOwners());
		c.set("Region.Essentials.Name", r.getName().toString());
		c.set("Region.Essentials.World", r.getWorld().getName());
		
		if (r instanceof PolyRegion) {
			c.set("Region.Essentials.Points.xPoints", pointsToString(((PolyRegion) r).get2DPolygon().xpoints));
			c.set("Region.Essentials.Points.zPoints", pointsToString(((PolyRegion) r).get2DPolygon().ypoints));
			c.set("Region.Essentials.Points.nPoints", ((PolyRegion) r).get2DPolygon().npoints);
			c.set("Region.Essentials.Points.MaxY", ((PolyRegion) r).getMaxY());
			c.set("Region.Essentials.Points.MinY", ((PolyRegion) r).getMinY());
		} else if (r instanceof CuboidRegion) {
			c.set("Region.Essentials.Points.Point1", convertLocation(((CuboidRegion) r).getL1()));
			c.set("Region.Essentials.Points.Point2", convertLocation(((CuboidRegion) r).getL2()));
		}
		
		c.set("Region.Spout.Welcome.Enabled", r.isSpoutWelcomeEnabled());
		c.set("Region.Spout.Leave.Enabled", r.isSpoutWelcomeEnabled());
		c.set("Region.Spout.Welcome.Message", r.getSpoutWelcomeMessage());
		c.set("Region.Spout.Welcome.IconID", r.getSpoutWelcomeMaterial().getId());	
		c.set("Region.Spout.Leave.Message", r.getSpoutLeaveMessage());
		c.set("Region.Spout.Leave.IconID", r.getSpoutLeaveMaterial().getId());
		c.set("Region.Spout.Sound.PlayCustomMusic", r.isPlayCustomSoundUrl());
		c.set("Region.Spout.Sound.CustomMusicURL", r.getCustomSoundUrl());
		c.set("Region.Spout.Texture.UseTexture", r.isUseSpoutTexturePack());
		c.set("Region.Spout.Texture.TexturePackURL", r.getCustomSoundUrl());
		
		c.set("Region.Economy.ForSale", r.isForSale());
		c.set("Region.Economy.Price", r.getSalePrice());
		
		c.set("Region.Teleportation.Warp.Location", r.getWorld().getName() + ",0,0,0");
		
		c.set("Region.Block.BlockForm.Enabled", r.isBlockForm());
		c.set("Region.General.PlayerCap.Cap", r.getPlayerCap());
		
		c.set("Region.GameMode.Type", r.getGameMode().toString());
		c.set("Region.GameMode.Change", r.isChangeGameMode());
		
		try {
			c.save(region_core);
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		
		new File(region_root + File.separator + "Exceptions").mkdir();
		new File(region_root + File.separator + "Exceptions" + File.separator + "Players").mkdir();
		new File(region_root + File.separator + "Exceptions" + File.separator + "Nodes").mkdir();
		new File(region_root + File.separator + "Items").mkdir();
		new File(region_root + File.separator + "Backups").mkdir();
		new File(region_root + File.separator + "Logs").mkdir();
		try {
			new File(region_root + File.separator + "Logs" + File.separator + r.getName() + ".log").createNewFile();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		System.out.println("Region '" + r.getName() + "' saved successfully!");
	}
	
	public synchronized void updateInheritedRegion(Region r) throws IOException{
		
		this.deleteRegion(r.getName());
		
		File region_root = new File(db_root + File.separator + r.getName());
		File region_core = new File(region_root + File.separator + r.getName() + ".rz");
		
		region_root.mkdir();
		
		try {
			region_core.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("Saving Region '" + r.getName() + "' @ " + region_core.getAbsoluteFile().toString());
		
		FileConfiguration c = YamlConfiguration.loadConfiguration(region_core);
		
		c.set("Region.Messages.WelcomeMessage", r.getWelcomeMessage().toString());
		c.set("Region.Messages.LeaveMessage", r.getLeaveMessage().toString());
		c.set("Region.Messages.ProtectionMessage", r.getProtectionMessage().toString());
		c.set("Region.Messages.PreventEntryMessage", r.getPreventEntryMessage().toString());
		c.set("Region.Messages.PreventExitMessage", r.getPreventExitMessage().toString());
		c.set("Region.Messages.ShowPvpWarning", Boolean.valueOf(r.isShowPvpWarning()));
		
		c.set("Region.Messages.ShowWelcomeMessage", Boolean.valueOf(r.isShowWelcomeMessage()));
		c.set("Region.Messages.ShowLeaveMessage", Boolean.valueOf(r.isShowLeaveMessage()));
		c.set("Region.Messages.ShowProtectionMessage", Boolean.valueOf(r.isShowProtectionMessage()));
		c.set("Region.Messages.ShowPreventEntryMessage", Boolean.valueOf(r.isShowPreventEntryMessage()));
		c.set("Region.Messages.ShowPreventExitMessage", Boolean.valueOf(r.isShowPreventExitMessage()));
		
		c.set("Region.Modes.ItemControlMode", r.getItemMode().toString());
		c.set("Region.Modes.ProtectionMode", r.getProtectionMode().toString());
		c.set("Region.Modes.PreventEntryMode", r.getPreventEntryMode().toString());
		c.set("Region.Modes.PreventExitMode", r.getPreventExitMode().toString());
		
		c.set("Region.Inventory.PermWipeOnEnter", r.isPermWipeOnEnter());
		c.set("Region.Inventory.PermWipeOnExit", r.isPermWipeOnExit());
		c.set("Region.Inventory.WipeAndCacheOnEnter", r.isWipeAndCacheOnEnter());
		c.set("Region.Inventory.WipeAndCacheOnExit", r.isWipeAndCacheOnExit());
		
		c.set("Region.Command.ForceCommand", r.isForceCommand());
		StringBuilder sb = new StringBuilder();
		for(String s : r.getCommandSet()){
			sb.append(s).append(",");
		}
		c.set("Region.Command.CommandSet", sb.toString());
		sb.replace(0, sb.length(), "");
		
		/*
		 * Permission nodes
		 */
		for(String s : r.getTempNodesCacheAdd()){
			sb.append(s).append(",");
		}
		
		c.set("Region.Permissions.TemporaryCache.AddNodes", sb.toString());
		sb.replace(0, sb.length(), "");
		
		for(String s : r.getTempNodesCacheRem()){
			sb.append(s).append(",");
		}
		
		c.set("Region.Permissions.TemporaryCache.RemoveNodes", sb.toString());
		sb.replace(0, sb.length(), "");
		
		for(String s : r.getPermAddNodes()){
			sb.append(s).append(",");
		}
		
		c.set("Region.Permissions.PermanentCache.AddNodes", sb.toString());
		sb.replace(0, sb.length(), "");
		
		for(String s : r.getPermRemoveNodes()){
			sb.append(s).append(",");
		}
		
		c.set("Region.Permissions.PermanentCache.RemoveNodes", sb.toString());
		sb.replace(0, sb.length(), "");
		/*
		 * End Permission Nodes
		 */
		
		/*
		 * Groups
		 */
		for(String s : r.getTempAddGroups()){
			sb.append(s).append(",");
		}
		
		c.set("Region.Permissions.TempGroups.AddGroups", sb.toString());
		sb.replace(0, sb.length(), "");
		
		for(String s : r.getTempRemoveGroups()){
			sb.append(s).append(",");
		}
		
		c.set("Region.Permissions.TempGroups.RemoveGroups", sb.toString());
		sb.replace(0, sb.length(), "");
		
		for(String s : r.getPermAddGroups()){
			sb.append(s).append(",");
		}
		
		c.set("Region.Permissions.PermGroups.AddGroups", sb.toString());
		sb.replace(0, sb.length(), "");
		
		for(String s : r.getPermRemoveGroups()){
			sb.append(s).append(",");
		}
		
		c.set("Region.Permissions.PermGroups.RemoveGroups", sb.toString());
		sb.replace(0, sb.length(), "");
		/*
		 * End Groups
		 */
		
		c.set("Region.General.Protected", Boolean.valueOf(r.isProtected()));
		c.set("Region.General.FireProtection", Boolean.valueOf(r.isFireProtection()));
		c.set("Region.General.FireSpread", Boolean.valueOf(r.isFireSpread()));
		c.set("Region.General.ExplosionsEnabled", Boolean.valueOf(r.isExplosionsEnabled()));
		c.set("Region.General.EndermanBlock", Boolean.valueOf(r.isBlockEndermanMod()));
		c.set("Region.General.PreventEntry", Boolean.valueOf(r.isPreventEntry()));
		c.set("Region.General.PreventExit", Boolean.valueOf(r.isPreventExit()));
		c.set("Region.General.PreventInteraction", Boolean.valueOf(r.isPreventInteraction()));
		c.set("Region.General.DoorsLocked", Boolean.valueOf(r.areDoorsLocked()));
		c.set("Region.General.ChestsLocked", Boolean.valueOf(r.areChestsLocked()));
		c.set("Region.General.Password.Enabled", Boolean.valueOf(r.isPasswordEnabled()));
		
		if(r.getPassword().length() > 3){
			c.set("Region.General.Password.Password", extCrypt.computeSHA2_384BitHash(r.getPassword().toString()));
		} else {
			c.set("Region.General.Password.Password", "");
		}
		
		c.set("Region.Other.MobSpawns", Boolean.valueOf(r.canMobsSpawn()));
		c.set("Region.Other.MonsterSpawns", Boolean.valueOf(r.canMonstersSpawn()));
		c.set("Region.Other.PvP", Boolean.valueOf(r.isPvp()));
		c.set("Region.Other.HealthEnabled", Boolean.valueOf(r.isHealthEnabled()));
		c.set("Region.Other.HealthRegen", r.getHealthRegen());
		c.set("Region.Other.LSPS", r.getLSPS());
		c.set("Region.Other.VelocityWarp", r.getVelocityWarp());
		
		c.set("Region.Essentials.Owner", r.getOwner().toString());
		
		for(String s : r.getSubOwners()){
			sb.append(s).append(",");
		}
		
		c.set("Region.Essentials.SubOwners", sb.toString());
		sb.replace(0, sb.length(), "");
		
		c.set("Region.Essentials.Name", r.getName().toString());
		c.set("Region.Essentials.World", r.getWorld().getName());
		
		if (r instanceof PolyRegion) {
			c.set("Region.Essentials.Points.xPoints", pointsToString(((PolyRegion) r).get2DPolygon().xpoints));
			c.set("Region.Essentials.Points.zPoints", pointsToString(((PolyRegion) r).get2DPolygon().ypoints));
			c.set("Region.Essentials.Points.nPoints", ((PolyRegion) r).get2DPolygon().npoints);
			c.set("Region.Essentials.Points.MaxY", ((PolyRegion) r).getMaxY());
			c.set("Region.Essentials.Points.MinY", ((PolyRegion) r).getMinY());
		} else if (r instanceof CuboidRegion) {
			c.set("Region.Essentials.Points.Point1", convertLocation(((CuboidRegion) r).getL1()));
			c.set("Region.Essentials.Points.Point2", convertLocation(((CuboidRegion) r).getL2()));
		}
		
		c.set("Region.Spout.Welcome.Message", r.getSpoutWelcomeMessage());
		c.set("Region.Spout.Welcome.IconID", r.getSpoutWelcomeMaterial().getId());	
		c.set("Region.Spout.Leave.Message", r.getSpoutLeaveMessage());
		c.set("Region.Spout.Leave.IconID", r.getSpoutLeaveMaterial().getId());
		c.set("Region.Spout.Sound.PlayCustomMusic", r.isPlayCustomSoundUrl());
		c.set("Region.Spout.Sound.CustomMusicURL", r.getCustomSoundUrl());
		c.set("Region.Spout.Texture.UseTexture", r.isUseSpoutTexturePack());
		c.set("Region.Spout.Texture.TexturePackURL", r.getCustomSoundUrl());
		
		c.set("Region.Economy.ForSale", r.isForSale());
		c.set("Region.Economy.Price", r.getSalePrice());
		
		c.set("Region.Spout.Welcome.Enabled", r.isSpoutWelcomeEnabled());
		c.set("Region.Spout.Leave.Enabled", r.isSpoutWelcomeEnabled());
		
		c.set("Region.Teleportation.Warp.Location", r.getWarp().getWorld() + "," + r.getWarp().getBlockX() + "," + r.getWarp().getBlockY() + "," + r.getWarp().getBlockZ());
		
		c.set("Region.Block.BlockForm.Enabled", r.isBlockForm());
		c.set("Region.General.PlayerCap.Cap", r.getPlayerCap());
		
		c.set("Region.GameMode.Type", r.getGameMode().toString());
		c.set("Region.GameMode.Change", r.isChangeGameMode());
		
		c.save(region_core);
		
		new File(region_root + File.separator + "Exceptions").mkdir();
		new File(region_root + File.separator + "Exceptions" + File.separator + "Players").mkdir();
		new File(region_root + File.separator + "Exceptions" + File.separator + "Nodes").mkdir();
		new File(region_root + File.separator + "Items").mkdir();
		new File(region_root + File.separator + "Backups").mkdir();
		new File(region_root + File.separator + "Logs").mkdir();
		
		for(String ex : r.getExceptions()){
			new File(region_root + File.separator + "Exceptions" + File.separator + "Players" + File.separator + ex + ".excep").createNewFile();
		}
		
		for(int ex : r.getItems()){
			new File(region_root + File.separator + "Items" + File.separator + ex + ".excep").createNewFile();
		}
		
		for(String ex : r.getNodes()){
			new File(region_root + File.separator + "Exceptions" + File.separator + "Nodes" + File.separator + ex + ".excep").createNewFile();
		}
		
		try {
			new File(region_root + File.separator + "Logs" + File.separator + r.getName() + ".log").createNewFile();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		try {
			new File(region_root + File.separator + "Exceptions" + File.separator + "Players" + File.separator + r.getOwner() + ".excep").createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("Region '" + r.getName() + "' saved successfully!");
	}
	
	public String convertLocation(Location l){
		return l.getWorld().getName() + "," + l.getX() + "," + l.getY() + "," + l.getZ();
	}
	
	public String pointsToString(int[] points) {
		String pointsStr = "";
		
		for (int i : points) {
			pointsStr += (i + ",");
		}
		
		return pointsStr.substring(0, pointsStr.length() - 1);
	}
	
	public void deleteRegion(String s){
		if(doesRegionExist(s)){
			File f = new File(db_root + File.separator + s);
			f.delete();
		}
	}
	
	public boolean doesRegionExist(String s){
		File f = new File(db_root + File.separator + s);
		if(f.exists()){
			return true;
		} else {
			return false;
		}
	}

}
