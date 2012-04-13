package couk.Adamki11s.Regios.Data;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import couk.Adamki11s.Regios.Regions.Region;
import couk.Adamki11s.Regios.Regions.RegionLocation;

public class Saveable {
	
	private final File root = new File("plugins" + File.separator + "Regios"),
	 db_root = new File(root + File.separator + "Database");
	
	public synchronized void saveRegion(Region r, RegionLocation rl1, RegionLocation rl2){
		
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
		
		c.set("Region.General.Protected.General", Boolean.valueOf(r.is_protection()));
		c.set("Region.General.Protected.BlockBreak", Boolean.valueOf(r.is_protectionBreak()));
		c.set("Region.General.Protected.BlockPlace", Boolean.valueOf(r.is_protectionPlace()));
		c.set("Region.General.FireProtection", Boolean.valueOf(r.isFireProtection()));
		c.set("Region.General.TNTEnabled", Boolean.valueOf(r.isTNTEnabled()));
		c.set("Region.General.PreventEntry", Boolean.valueOf(r.isPreventEntry()));
		c.set("Region.General.PreventExit", Boolean.valueOf(r.isPreventExit()));
		c.set("Region.General.PreventInteraction", Boolean.valueOf(r.isPreventInteraction()));
		c.set("Region.General.DoorsLocked", Boolean.valueOf(r.isDoorsLocked()));
		c.set("Region.General.ChestsLocked", Boolean.valueOf(r.isChestsLocked()));
		c.set("Region.General.Password.Enabled", Boolean.valueOf(r.isPasswordEnabled()));
		
		if(r.getPassword().length() > 3){
			c.set("Region.General.Password.Password", r.getExCrypt().computeSHA2_384BitHash(r.getPassword().toString()));
		} else {
			c.set("Region.General.Password.Password", "");
		}
		
		c.set("Region.Other.MobSpawns", Boolean.valueOf(r.isMobSpawns()));
		c.set("Region.Other.MonsterSpawns", Boolean.valueOf(r.isMonsterSpawns()));
		c.set("Region.Other.PvP", Boolean.valueOf(r.isPvp()));
		c.set("Region.Other.HealthEnabled", Boolean.valueOf(r.isHealthEnabled()));
		c.set("Region.Other.HealthRegen", r.getHealthRegen());
		c.set("Region.Other.LSPS", r.getLSPS());
		c.set("Region.Other.VelocityWarp", r.getVelocityWarp());
		
		c.set("Region.Essentials.Owner", r.getOwner().toString());
		c.set("Region.Essentials.SubOwners", "");
		c.set("Region.Essentials.Name", r.getName().toString());
		c.set("Region.Essentials.World", r.getWorld().getName());
		c.set("Region.Essentials.Points.Point1", convertLocation(rl1));
		c.set("Region.Essentials.Points.Point2", convertLocation(rl2));
		
		c.set("Region.Spout.Welcome.Enabled", r.isSpoutWelcomeEnabled());
		c.set("Region.Spout.Leave.Enabled", r.isSpoutWelcomeEnabled());
		c.set("Region.Spout.Welcome.Message", r.getSpoutEntryMessage());
		c.set("Region.Spout.Welcome.IconID", r.getSpoutEntryMaterial().getId());	
		c.set("Region.Spout.Leave.Message", r.getSpoutExitMessage());
		c.set("Region.Spout.Leave.IconID", r.getSpoutExitMaterial().getId());
		c.set("Region.Spout.Sound.PlayCustomMusic", r.isPlayCustomSoundUrl());
		c.set("Region.Spout.Sound.CustomMusicURL", r.getCustomSoundUrl());
		c.set("Region.Spout.Texture.UseTexture", r.isUseSpoutTexturePack());
		c.set("Region.Spout.Texture.TexturePackURL", r.getCustomSoundUrl());
		
		c.set("Region.Economy.ForSale", r.isForSale());
		c.set("Region.Economy.Price", r.getSalePrice());
		
		c.set("Region.Teleportation.Warp.Location", r.getWorld().getName() + ",0,0,0");
		
		c.set("Region.Block.BlockForm.Enabled", r.isBlockForm());
		c.set("Region.General.PlayerCap.Cap", r.getPlayerCap());
		
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
		
		try {
			new File(region_root + File.separator + "Exceptions" + File.separator + "Players" + File.separator + r.getOwner() + ".excep").createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("Region '" + r.getName() + "' saved successfully!");
	}
	
	public synchronized void updateInheritedRegion(Region r, RegionLocation rl1, RegionLocation rl2) throws IOException{
		
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
		
		for(String s : r.getPermanentNodesCacheAdd()){
			sb.append(s).append(",");
		}
		
		c.set("Region.Permissions.PermanentCache.AddNodes", sb.toString());
		sb.replace(0, sb.length(), "");
		
		for(String s : r.getPermanentNodesCacheRemove()){
			sb.append(s).append(",");
		}
		
		c.set("Region.Permissions.PermanentCache.RemoveNodes", sb.toString());
		sb.replace(0, sb.length(), "");
		
		c.set("Region.General.Protected", Boolean.valueOf(r.is_protection()));
		c.set("Region.General.FireProtection", Boolean.valueOf(r.isFireProtection()));
		c.set("Region.General.TNTEnabled", Boolean.valueOf(r.isTNTEnabled()));
		c.set("Region.General.PreventEntry", Boolean.valueOf(r.isPreventEntry()));
		c.set("Region.General.PreventExit", Boolean.valueOf(r.isPreventExit()));
		c.set("Region.General.PreventInteraction", Boolean.valueOf(r.isPreventInteraction()));
		c.set("Region.General.DoorsLocked", Boolean.valueOf(r.isDoorsLocked()));
		c.set("Region.General.ChestsLocked", Boolean.valueOf(r.isChestsLocked()));
		c.set("Region.General.Password.Enabled", Boolean.valueOf(r.isPasswordEnabled()));
		
		if(r.getPassword().length() > 3){
			c.set("Region.General.Password.Password", r.getExCrypt().computeSHA2_384BitHash(r.getPassword().toString()));
		} else {
			c.set("Region.General.Password.Password", "");
		}
		
		c.set("Region.Other.MobSpawns", Boolean.valueOf(r.isMobSpawns()));
		c.set("Region.Other.MonsterSpawns", Boolean.valueOf(r.isMonsterSpawns()));
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
		c.set("Region.Essentials.Points.Point1", convertLocation(rl1));
		c.set("Region.Essentials.Points.Point2", convertLocation(rl2));
		
		c.set("Region.Spout.Welcome.Message", r.getSpoutEntryMessage());
		c.set("Region.Spout.Welcome.IconID", r.getSpoutEntryMaterial().getId());	
		c.set("Region.Spout.Leave.Message", r.getSpoutExitMessage());
		c.set("Region.Spout.Leave.IconID", r.getSpoutExitMaterial().getId());
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
	
	public String convertLocation(RegionLocation l){
		return l.getWorld().getName() + "," + l.getX() + "," + l.getY() + "," + l.getZ();
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
