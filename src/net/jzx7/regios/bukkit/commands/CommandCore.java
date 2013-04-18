package net.jzx7.regios.bukkit.commands;

import java.io.File;
import java.io.IOException;

import net.jzx7.regios.Commands.AdministrationCommands;
import net.jzx7.regios.Commands.AuthenticationCommands;
import net.jzx7.regios.Commands.CreationCommands;
import net.jzx7.regios.Commands.EconomyCommands;
import net.jzx7.regios.Commands.ExceptionCommands;
import net.jzx7.regios.Commands.FunCommands;
import net.jzx7.regios.Commands.HelpCommands;
import net.jzx7.regios.Commands.InfoCommands;
import net.jzx7.regios.Commands.InventoryCommands;
import net.jzx7.regios.Commands.MessageCommands;
import net.jzx7.regios.Commands.MiscCommands;
import net.jzx7.regios.Commands.MobCommands;
import net.jzx7.regios.Commands.ModeCommands;
import net.jzx7.regios.Commands.ModificationCommands;
import net.jzx7.regios.Commands.PermissionsCommands;
import net.jzx7.regios.Commands.ProtectionCommands;
import net.jzx7.regios.Commands.ProtectionMiscCommands;
import net.jzx7.regios.Commands.WarpCommands;
import net.jzx7.regios.Data.ConfigurationData;
import net.jzx7.regios.Mutable.Zippable;
import net.jzx7.regios.Permissions.PermissionsCore;
import net.jzx7.regios.RBF.RBF_Core;
import net.jzx7.regios.RBF.ShareData;
import net.jzx7.regios.WorldEdit.Commands.WorldEditCommands;
import net.jzx7.regios.bukkit.SpoutPlugin.SpoutInterface;
import net.jzx7.regios.bukkit.SpoutPlugin.Commands.SpoutCommands;
import net.jzx7.regios.bukkit.SpoutPlugin.GUI.RegionScreenManager;
import net.jzx7.regios.bukkit.SpoutPlugin.GUI.ScreenHolder;
import net.jzx7.regios.entity.PlayerManager;
import net.jzx7.regios.messages.CMDText;
import net.jzx7.regios.messages.Message;
import net.jzx7.regios.messages.MsgFormat;
import net.jzx7.regios.regions.RegionManager;
import net.jzx7.regios.util.RegiosConversions;
import net.jzx7.regiosapi.entity.RegiosPlayer;
import net.jzx7.regiosapi.events.RegionCommandEvent;
import net.jzx7.regiosapi.exceptions.FileExistanceException;
import net.jzx7.regiosapi.exceptions.InvalidNBTFormat;
import net.jzx7.regiosapi.exceptions.RegionExistanceException;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.getspout.spoutapi.player.SpoutPlayer;

public class CommandCore implements CommandExecutor {

	protected final AdministrationCommands admin = new AdministrationCommands();
	protected final AuthenticationCommands auth = new AuthenticationCommands();
	protected final CreationCommands creation = new CreationCommands();
	protected final EconomyCommands eco = new EconomyCommands();
	protected final ExceptionCommands excep = new ExceptionCommands();
	protected final FunCommands fun = new FunCommands();
	protected final HelpCommands help = new HelpCommands();
	protected final InfoCommands info = new InfoCommands();
	protected final MessageCommands msg = new MessageCommands();
	protected final MiscCommands miscCmd = new MiscCommands();
	protected final MobCommands mobs = new MobCommands();
	protected final ModeCommands mode = new ModeCommands();
	protected final ModificationCommands mod = new ModificationCommands();
	protected final InventoryCommands invent = new InventoryCommands();
	protected final PermissionsCommands perms = new PermissionsCommands();
	protected final ProtectionCommands protect = new ProtectionCommands();
	protected final ProtectionMiscCommands misc = new ProtectionMiscCommands();
	protected final SpoutCommands spout = new SpoutCommands();
	protected final WarpCommands warps = new WarpCommands();
	protected final RegionManager rm = new RegionManager();
	protected final PlayerManager pm = new PlayerManager();

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (label.equalsIgnoreCase("regios") || label.equalsIgnoreCase("reg") || label.equalsIgnoreCase("r")) {

			if (!(sender instanceof Player)) {
				System.out.println(Message.CONSOLEUNSUPPORTED.getMessage());
				return true;
			}

			RegiosPlayer p = RegiosConversions.getRegiosPlayer((Player) sender);

			if (args.length == 0) {
				help.help(p, args);
			}

			else if (args[0].equalsIgnoreCase("help")) {
				help.help(p, args);
			}

			else if (args[0].equalsIgnoreCase("set")) {
				creation.set(p, args);
			}

			else if (args[0].equalsIgnoreCase("create")) {
				creation.create(p, args);
			}

			else if (args[0].equalsIgnoreCase("cancel")) {
				creation.cancel(p, args);
			}

			else if (args[0].equalsIgnoreCase("edit")) {
				if (PermissionsCore.doesHaveNode(p, "regios.data.edit")) {
					if(!SpoutInterface.isGlobal_spoutEnabled()){
						p.sendMessage(Message.SPOUTPLUGINREQUIRED.getMessage());
					}
					if (SpoutInterface.doesPlayerHaveSpout(p)) {
						if (args.length == 2) {
							if (rm.getRegion(args[1]) == null) {
								p.sendMessage(Message.REGIOSDOESNTEXIST.getMessage());
							} else {
								ScreenHolder sh = ScreenHolder.getScreenHolder((SpoutPlayer) p);
								sh.addScreenHolder((SpoutPlayer)p, sh);
								((SpoutPlayer) p).sendNotification("Editing Region", MsgFormat.colourFormat("<DGREEN>" + args[1]), Material.FENCE);
								RegionScreenManager.drawPanelFramework((SpoutPlayer) p, rm.getRegion(args[1]), sh);
							}
						} else {
							p.sendMessage(Message.INVALIDARGUMENTCOUNT.getMessage());
							p.sendMessage(Message.PROPERUSAGE.getMessage() + CMDText.EDIT.getText());
						}
					} else {
						p.sendMessage(Message.SPOUTCRAFTREQUIRED.getMessage());
					}
				} else {
					PermissionsCore.sendInvalidPerms(p);
				}
			}

			/*
			 * Authentication
			 */

			else if (args[0].equalsIgnoreCase("auth")) {
				auth.auth(p, args);
			}

			/*
			 * End Authentication
			 */

			/*
			 * Warp
			 */
			else if ((args[0].equalsIgnoreCase("set-warp") || args[0].equalsIgnoreCase("setwarp"))) {
				warps.setwarp(p, args);
			}

			else if ((args[0].equalsIgnoreCase("reset-warp") || args[0].equalsIgnoreCase("resetwarp"))) {
				warps.resetwarp(p, args);
			}

			else if ((args[0].equalsIgnoreCase("warpto") || args[0].equalsIgnoreCase("warp-to") || args[0].equalsIgnoreCase("warp"))) {
				warps.warp(p, args);
			}

			/*
			 * End Warp
			 */

			/*
			 * Info
			 */

			else if (args[0].equalsIgnoreCase("info")) {
				info.info(p, args);
			}

			/*
			 * End Info
			 */

			/*
			 * Messages
			 */

			else if (args[0].equalsIgnoreCase("set-welcome")) {
				msg.setwelcome(p, args);
			}

			else if (args[0].equalsIgnoreCase("set-leave")) {
				msg.setleave(p, args);
			}

			else if (args[0].equalsIgnoreCase("set-prevent-exit")) {
				msg.setpreventexit(p, args);
			}

			else if (args[0].equalsIgnoreCase("set-prevent-entry")) {
				msg.setprevententry(p, args);
			}

			else if (args[0].equalsIgnoreCase("set-protection")) {
				msg.setprotection(p, args);
			}

			else if (args[0].equalsIgnoreCase("show-welcome")) {
				msg.showwelcome(p, args);
			}

			else if (args[0].equalsIgnoreCase("show-leave")) {
				msg.showleave(p, args);
			}

			else if (args[0].equalsIgnoreCase("show-prevent-entry")) {
				msg.showprevententry(p, args);
			}

			else if (args[0].equalsIgnoreCase("show-prevent-exit")) {
				msg.showpreventexit(p, args);
			}

			else if (args[0].equalsIgnoreCase("show-protection")) {
				msg.showprotection(p, args);
			}

			else if (args[0].equalsIgnoreCase("show-pvp")) {
				msg.showpvp(p, args);
			}

			/*
			 * Messages
			 */

			/*
			 * Mobs
			 */

			else if (args[0].equalsIgnoreCase("setmobspawns") || args[0].equalsIgnoreCase("sms") || args[0].equalsIgnoreCase("mobspawns")) {
				mobs.setMobSpawns(p, args);
			}

			/*
			 * End Mobs
			 */

			/*
			 * Protection
			 */

			else if (args[0].equalsIgnoreCase("protect")) {
				protect.protect(p, args);
			}

			else if (args[0].equalsIgnoreCase("unprotect")) {
				protect.unprotect(p, args);
			}

			else if (args[0].equalsIgnoreCase("allow-entry") || args[0].equalsIgnoreCase("allowentry")) {
				protect.allowentry(p, args);
			}

			else if (args[0].equalsIgnoreCase("allow-exit") || args[0].equalsIgnoreCase("allow-exit")) {
				protect.allowexit(p, args);
			}

			else if (args[0].equalsIgnoreCase("prevent-entry") || args[0].equalsIgnoreCase("prevententry")) {
				protect.prevententry(p, args);
			}

			else if (args[0].equalsIgnoreCase("prevent-exit") || args[0].equalsIgnoreCase("preventexit")) {
				protect.preventexit(p, args);
			}

			/*
			 * End Protection
			 */

			/*
			 * Region Modification
			 */

			else if (args[0].equalsIgnoreCase("modify")) {
				mod.modify(p, args);
			}

			else if (args[0].equalsIgnoreCase("delete")) {
				mod.delete(p, args);
			}

			else if (args[0].equalsIgnoreCase("rename")) {
				mod.rename(p, args);
			}

			else if (args[0].equalsIgnoreCase("expand")) {
				if (args.length == 2) {
					creation.expandMaxSelection(p);
				} else {
					mod.expand(p, args);
				}
			}

			else if (args[0].equalsIgnoreCase("shrink")) {
				mod.shrink(p, args);
			}
			
			else if (args[0].equalsIgnoreCase("shift")) {
				mod.shift(p, args);
			}

			/*
			 * End Region Modification
			 */

			/*
			 * Exceptions
			 */

			else if (args[0].equalsIgnoreCase("playerex")) {
				excep.playerex(p, args);
			}

			else if (args[0].equalsIgnoreCase("itemex")) {
				excep.itemex(p, args);
			}

			else if (args[0].equalsIgnoreCase("nodeex")) {
				excep.nodeex(p, args);
			}

			else if (args[0].equalsIgnoreCase("subowner")) {
				excep.subowner(p, args);
			}

			/*
			 * End Exceptions
			 */

			/*
			 * Economy
			 */

			else if (args[0].equalsIgnoreCase("for-sale")) {
				eco.forsale(p, args);
			}

			else if (args[0].equalsIgnoreCase("set-price")) {
				eco.setprice(p, args);
			}

			else if (args[0].equalsIgnoreCase("buy") || args[0].equalsIgnoreCase("buy-region")) {
				eco.buy(p, args);
			}

			else if (args[0].equalsIgnoreCase("list-for-sale") || args[0].equalsIgnoreCase("list-forsale")) {
				eco.listforsale(p, args);
			}

			/*
			 * End Economy
			 */

			/*
			 * Fun
			 */

			else if (args[0].equalsIgnoreCase("lsps")) {
				fun.lsps(p, args);
			}

			else if (args[0].equalsIgnoreCase("pvp")) {
				fun.pvp(p, args);
			}

			else if (args[0].equalsIgnoreCase("healthregen") || args[0].equalsIgnoreCase("health-regen")) {
				fun.healthregen(p, args);
			}

			else if (args[0].equalsIgnoreCase("health") || args[0].equalsIgnoreCase("health-enabled")) {
				fun.health(p, args);
			}

			else if (args[0].equalsIgnoreCase("vel-warp") || args[0].equalsIgnoreCase("velocity-warp")) {
				fun.velocity(p, args);
			}

			else if (args[0].equalsIgnoreCase("setbiome")) {
				fun.biome(p, args);
			}

			/*
			 * End Fun
			 */

			/*
			 * Misc. Protection
			 */

			else if (args.length == 3 && (args[0].equalsIgnoreCase("preventinteraction") || args[0].equalsIgnoreCase("prevent-interaction"))) {
				if (PermissionsCore.doesHaveNode(p, "regios.protection.prevent-interaction")) {
					misc.setInteraction(rm.getRegion(args[1]), args[1], args[2], p);
				} else {
					PermissionsCore.sendInvalidPerms(p);
				}
			}

			else if (args.length == 3 && (args[0].equalsIgnoreCase("doorslocked") || args[0].equalsIgnoreCase("doors-locked"))) {
				if (PermissionsCore.doesHaveNode(p, "regios.protection.doors-locked")) {
					misc.setDoorsLocked(rm.getRegion(args[1]), args[1], args[2], p);
				} else {
					PermissionsCore.sendInvalidPerms(p);
				}
			}

			else if (args.length == 3 && (args[0].equalsIgnoreCase("chestslocked") || args[0].equalsIgnoreCase("chests-locked"))) {
				if (PermissionsCore.doesHaveNode(p, "regios.protection.chests-locked")) {
					misc.setChestsLocked(rm.getRegion(args[1]), args[1], args[2], p);
				} else {
					PermissionsCore.sendInvalidPerms(p);
				}
			}

			else if (args.length == 3 && (args[0].equalsIgnoreCase("dispenserslocked") || args[0].equalsIgnoreCase("dispensers-locked"))) {
				if (PermissionsCore.doesHaveNode(p, "regios.protection.dispensers-locked")) {
					misc.setDispensersLocked(rm.getRegion(args[1]), args[1], args[2], p);
				} else {
					PermissionsCore.sendInvalidPerms(p);
				}
			}

			else if (args.length == 3 && (args[0].equalsIgnoreCase("setpassword") || args[0].equalsIgnoreCase("set-password"))) {
				if (PermissionsCore.doesHaveNode(p, "regios.protection.set-password")) {
					misc.setPassword(rm.getRegion(args[1]), args[1], args[2], p);
				} else {
					PermissionsCore.sendInvalidPerms(p);
				}
			}

			else if (args.length == 3 && (args[0].equalsIgnoreCase("usepassword") || args[0].equalsIgnoreCase("use-password"))) {
				if (PermissionsCore.doesHaveNode(p, "regios.protection.set-password")) {
					misc.setPasswordEnabled(rm.getRegion(args[1]), args[1], args[2], p);
				} else {
					PermissionsCore.sendInvalidPerms(p);
				}
			}

			else if (args.length == 3 && (args[0].equalsIgnoreCase("fireprotection") || args[0].equalsIgnoreCase("fire-protection"))) {
				if (PermissionsCore.doesHaveNode(p, "regios.protection.fire-protection")) {
					misc.setFireProtection(rm.getRegion(args[1]), args[1], args[2], p);
				} else {
					PermissionsCore.sendInvalidPerms(p);
				}
			}

			else if (args.length == 3 && (args[0].equalsIgnoreCase("firespread") || args[0].equalsIgnoreCase("fire-spread"))) {
				if (PermissionsCore.doesHaveNode(p, "regios.protection.fire-spread")) {
					misc.setFireSpread(rm.getRegion(args[1]), args[1], args[2], p);
				} else {
					PermissionsCore.sendInvalidPerms(p);
				}
			}

			else if (args.length == 3 && (args[0].equalsIgnoreCase("explosionsenabled") || args[0].equalsIgnoreCase("explosions-enabled") || args[0].equalsIgnoreCase("explenabled"))) {
				if (PermissionsCore.doesHaveNode(p, "regios.protection.explosions-enabled")) {
					misc.setTNTEnabled(rm.getRegion(args[1]), args[1], args[2], p);
				} else {
					PermissionsCore.sendInvalidPerms(p);
				}
			}

			else if (args.length == 3 && (args[0].equalsIgnoreCase("playercap") || args[0].equalsIgnoreCase("player-cap"))) {
				if (PermissionsCore.doesHaveNode(p, "regios.protection.player-cap")) {
					misc.setPlayerCap(rm.getRegion(args[1]), args[1], args[2], p);
				} else {
					PermissionsCore.sendInvalidPerms(p);
				}
			}

			else if (args.length == 3 && (args[0].equalsIgnoreCase("blockform") || args[0].equalsIgnoreCase("block-form"))) {
				if (PermissionsCore.doesHaveNode(p, "regios.protection.block-form")) {
					misc.setBlockForm(rm.getRegion(args[1]), args[1], args[2], p);
				} else {
					PermissionsCore.sendInvalidPerms(p);
				}
			}

			else if (args.length == 3 && (args[0].equalsIgnoreCase("endermanblock") || args[0].equalsIgnoreCase("enderman-block"))) {
				if (PermissionsCore.doesHaveNode(p, "regios.protection.enderman-block")) {
					misc.setEndermanBlock(rm.getRegion(args[1]), args[1], args[2], p);
				} else {
					PermissionsCore.sendInvalidPerms(p);
				}
			}

			/*
			 * End Misc. Protection
			 */

			/*
			 * Modes
			 */

			else if (args.length == 3 && (args[0].equalsIgnoreCase("protection-mode") || args[0].equalsIgnoreCase("protectionmode"))) {
				if (PermissionsCore.doesHaveNode(p, "regios.mode.protection")) {
					mode.setProtectionMode(rm.getRegion(args[1]), args[1], args[2], p);
				} else {
					PermissionsCore.sendInvalidPerms(p);
				}
			}

			else if (args.length == 3 && args[0].equalsIgnoreCase("prevent-entry-mode")) {
				if (PermissionsCore.doesHaveNode(p, "regios.mode.prevent-entry")) {
					mode.setPreventEntryMode(rm.getRegion(args[1]), args[1], args[2], p);
				} else {
					PermissionsCore.sendInvalidPerms(p);
				}
			}

			else if (args.length == 3 && args[0].equalsIgnoreCase("prevent-exit-mode")) {
				if (PermissionsCore.doesHaveNode(p, "regios.mode.prevent-exit")) {
					mode.setPreventExitMode(rm.getRegion(args[1]), args[1], args[2], p);
				} else {
					PermissionsCore.sendInvalidPerms(p);
				}
			}

			else if (args.length == 3 && (args[0].equalsIgnoreCase("item-mode") || args[0].equalsIgnoreCase("itemmode"))) {
				if (PermissionsCore.doesHaveNode(p, "regios.mode.items")) {
					mode.setItemControlMode(rm.getRegion(args[1]), args[1], args[2], p);
				} else {
					PermissionsCore.sendInvalidPerms(p);
				}
			}

			/*
			 * End Modes
			 */

			/*
			 * Inventory
			 */

			else if (args.length == 3 && (args[0].equalsIgnoreCase("perm-wipe-entry") || args[0].equalsIgnoreCase("perm-wipe-enter"))) {
				if (PermissionsCore.doesHaveNode(p, "regios.inventory.perm-wipe")) {
					invent.setPermWipeOnEntry(rm.getRegion(args[1]), args[1], args[2], p);
				} else {
					PermissionsCore.sendInvalidPerms(p);
				}
			}

			else if (args.length == 3 && (args[0].equalsIgnoreCase("perm-wipe-exit") || args[0].equalsIgnoreCase("perm-wipe-leave"))) {
				if (PermissionsCore.doesHaveNode(p, "regios.inventory.perm-wipe")) {
					invent.setPermWipeOnExit(rm.getRegion(args[1]), args[1], args[2], p);
				} else {
					PermissionsCore.sendInvalidPerms(p);
				}
			}

			else if (args.length == 3 && (args[0].equalsIgnoreCase("cache-wipe-entry") || args[0].equalsIgnoreCase("cache-wipe-enter"))) {
				if (PermissionsCore.doesHaveNode(p, "regios.inventory.cache-wipe")) {
					invent.setWipeAndCacheOnEntry(rm.getRegion(args[1]), args[1], args[2], p);
				} else {
					PermissionsCore.sendInvalidPerms(p);
				}
			}

			else if (args.length == 3 && (args[0].equalsIgnoreCase("cache-wipe-exit") || args[0].equalsIgnoreCase("cache-wipe-leave"))) {
				if (PermissionsCore.doesHaveNode(p, "regios.inventory.cache-wipe")) {
					invent.setWipeAndCacheOnExit(rm.getRegion(args[1]), args[1], args[2], p);
				} else {
					PermissionsCore.sendInvalidPerms(p);
				}
			}

			/*
			 * End Inventory
			 */

			/*
			 * Permissions
			 */

			else if (args.length == 3 && (args[0].equalsIgnoreCase("perm-cache-add-add"))) {
				if (PermissionsCore.doesHaveNode(p, "regios.permissions.cache")) {
					perms.addToTempAddCache(rm.getRegion(args[1]), args[1], args[2], p);
				} else {
					PermissionsCore.sendInvalidPerms(p);
				}
			}

			else if (args.length == 3 && (args[0].equalsIgnoreCase("perm-cache-rem-add"))) {
				if (PermissionsCore.doesHaveNode(p, "regios.permissions.cache")) {
					perms.addToTempRemCache(rm.getRegion(args[1]), args[1], args[2], p);
				} else {
					PermissionsCore.sendInvalidPerms(p);
				}
			}

			else if (args.length == 3 && (args[0].equalsIgnoreCase("perm-add-add"))) {
				if (PermissionsCore.doesHaveNode(p, "regios.permissions.perm-add")) {
					perms.addToPermAddCache(rm.getRegion(args[1]), args[1], args[2], p);
				} else {
					PermissionsCore.sendInvalidPerms(p);
				}
			}

			else if (args.length == 3 && (args[0].equalsIgnoreCase("perm-rem-add"))) {
				if (PermissionsCore.doesHaveNode(p, "regios.permissions.perm-rem")) {
					perms.addToPermRemCache(rm.getRegion(args[1]), args[1], args[2], p);
				} else {
					PermissionsCore.sendInvalidPerms(p);
				}
			}

			else if (args.length == 3 && (args[0].equalsIgnoreCase("perm-cache-add-rem"))) {
				if (PermissionsCore.doesHaveNode(p, "regios.permissions.cache")) {
					perms.removeFromTempAddCache(rm.getRegion(args[1]), args[1], args[2], p);
				} else {
					PermissionsCore.sendInvalidPerms(p);
				}
			}

			else if (args.length == 3 && (args[0].equalsIgnoreCase("perm-cache-rem-rem"))) {
				if (PermissionsCore.doesHaveNode(p, "regios.permissions.cache")) {
					perms.removeFromTempRemCache(rm.getRegion(args[1]), args[1], args[2], p);
				} else {
					PermissionsCore.sendInvalidPerms(p);
				}
			}

			else if (args.length == 3 && (args[0].equalsIgnoreCase("perm-add-rem"))) {
				if (PermissionsCore.doesHaveNode(p, "regios.permissions.perm-add")) {
					perms.removeFromPermAddCache(rm.getRegion(args[1]), args[1], args[2], p);
				} else {
					PermissionsCore.sendInvalidPerms(p);
				}
			}

			else if (args.length == 3 && (args[0].equalsIgnoreCase("perm-rem-rem"))) {
				if (PermissionsCore.doesHaveNode(p, "regios.permissions.perm-rem")) {
					perms.removeFromPermRemCache(rm.getRegion(args[1]), args[1], args[2], p);
				} else {
					PermissionsCore.sendInvalidPerms(p);
				}
			}

			else if (args.length == 2 && (args[0].equalsIgnoreCase("perm-cache-add-reset"))) {
				if (PermissionsCore.doesHaveNode(p, "regios.permissions.cache")) {
					perms.resetTempAddCache(rm.getRegion(args[1]), args[1], p);
				} else {
					PermissionsCore.sendInvalidPerms(p);
				}
			}

			else if (args.length == 2 && (args[0].equalsIgnoreCase("perm-cache-rem-reset"))) {
				if (PermissionsCore.doesHaveNode(p, "regios.permissions.cache")) {
					perms.resetTempRemCache(rm.getRegion(args[1]), args[1], p);
				} else {
					PermissionsCore.sendInvalidPerms(p);
				}
			}

			else if (args.length == 2 && (args[0].equalsIgnoreCase("perm-add-reset"))) {
				if (PermissionsCore.doesHaveNode(p, "regios.permissions.perm-add")) {
					perms.resetPermAddCache(rm.getRegion(args[1]), args[1], p);
				} else {
					PermissionsCore.sendInvalidPerms(p);
				}
			}

			else if (args.length == 2 && (args[0].equalsIgnoreCase("perm-rem-reset"))) {
				if (PermissionsCore.doesHaveNode(p, "regios.permissions.perm-rem")) {
					perms.resetPermRemCache(rm.getRegion(args[1]), args[1], p);
				} else {
					PermissionsCore.sendInvalidPerms(p);
				}
			}

			else if (args.length == 2 && (args[0].equalsIgnoreCase("perm-add-list"))) {
				if (PermissionsCore.doesHaveNode(p, "regios.permissions.perm-add")) {
					perms.listPermAdd(rm.getRegion(args[1]), args[1], p);
				} else {
					PermissionsCore.sendInvalidPerms(p);
				}
			}

			else if (args.length == 2 && (args[0].equalsIgnoreCase("perm-rem-list"))) {
				if (PermissionsCore.doesHaveNode(p, "regios.permissions.perm-rem")) {
					perms.listPermRemCache(rm.getRegion(args[1]), args[1], p);
				} else {
					PermissionsCore.sendInvalidPerms(p);
				}
			}

			else if (args.length == 2 && (args[0].equalsIgnoreCase("perm-cache-add-list"))) {
				if (PermissionsCore.doesHaveNode(p, "regios.permissions.cache")) {
					perms.listTempAddCache(rm.getRegion(args[1]), args[1], p);
				} else {
					PermissionsCore.sendInvalidPerms(p);
				}
			}

			else if (args[0].equalsIgnoreCase("perm-cache-rem-list")) {
				if(args.length == 2)
				{
					if (PermissionsCore.doesHaveNode(p, "regios.permissions.cache")) {
						perms.listTempRemCache(rm.getRegion(args[1]), args[1], p);
					} else {
						PermissionsCore.sendInvalidPerms(p);
					}
				}
			}

			else if (args[0].equalsIgnoreCase("perm-add-group-add") || args[0].equalsIgnoreCase("permaddgroupadd"))
			{
				if (args.length == 3)
				{
					if (PermissionsCore.doesHaveNode(p, "regios.permissons.group.add")) {
						perms.addPermGroupAdd(rm.getRegion(args[1]), args[1], p, args[2]);
					}
				}
			}

			else if (args[0].equalsIgnoreCase("perm-remove-group-add") || args[0].equalsIgnoreCase("permremovegroupadd"))
			{
				if (args.length == 3)
				{
					if (PermissionsCore.doesHaveNode(p, "regios.permissons.group.remove")) {
						perms.addPermGroupRemove(rm.getRegion(args[1]), args[1], p, args[2]);
					}
				}
			}

			else if (args[0].equalsIgnoreCase("perm-add-group-remove") || args[0].equalsIgnoreCase("permaddgroupremove"))
			{
				if (args.length == 3)
				{
					if (PermissionsCore.doesHaveNode(p, "regios.permissons.group.add")) {
						perms.removePermGroupAdd(rm.getRegion(args[1]), args[1], p, args[2]);
					}
				}
			}

			else if (args[0].equalsIgnoreCase("perm-remove-group-remove") || args[0].equalsIgnoreCase("permremovegroupremove"))
			{
				if (args.length == 3)
				{
					if (PermissionsCore.doesHaveNode(p, "regios.permissons.group.remove")) {
						perms.removePermGroupRemove(rm.getRegion(args[1]), args[1], p, args[2]);
					}
				}
			}

			else if (args[0].equalsIgnoreCase("temp-add-group-add"))
			{
				if (args.length == 3)
				{
					if (PermissionsCore.doesHaveNode(p, "regios.permissons.group.add")) {
						perms.addTempGroupAdd(rm.getRegion(args[1]), args[1], p, args[2]);
					}
				}
			}

			else if (args[0].equalsIgnoreCase("temp-add-group-remove"))
			{
				if (args.length == 3)
				{
					if (PermissionsCore.doesHaveNode(p, "regios.permissons.group.remove")) {
						perms.removeTempGroupAdd(rm.getRegion(args[1]), args[1], p, args[2]);
					}
				}
			}

			else if (args[0].equalsIgnoreCase("temp-remove-group-add"))
			{
				if (args.length == 3)
				{
					if (PermissionsCore.doesHaveNode(p, "regios.permissons.group.add")) {
						perms.addTempGroupRemove(rm.getRegion(args[1]), args[1], p, args[2]);
					}
				}
			}

			else if (args[0].equalsIgnoreCase("temp-remove-group-remove"))
			{
				if (args.length == 3)
				{
					if (PermissionsCore.doesHaveNode(p, "regios.permissons.group.remove")) {
						perms.removeTempGroupRemove(rm.getRegion(args[1]), args[1], p, args[2]);
					}
				}
			}

			else if (args[0].equalsIgnoreCase("list-perm-add-group"))
			{
				if (args.length == 2)
				{
					if (PermissionsCore.doesHaveNode(p, "regios.permissons.group.add")) {
						perms.listAddPermGroup(rm.getRegion(args[1]), args[1], p);
					}
				}
			}

			else if (args[0].equalsIgnoreCase("list-perm-remove-group"))
			{
				if (args.length == 2)
				{
					if (PermissionsCore.doesHaveNode(p, "regios.permissons.group.remove")) {
						perms.listRemovePermGroup(rm.getRegion(args[1]), args[1], p);
					}
				}
			}

			else if (args[0].equalsIgnoreCase("list-temp-add-group"))
			{
				if (args.length == 2)
				{
					if (PermissionsCore.doesHaveNode(p, "regios.permissons.group.add")) {
						perms.listAddTempGroup(rm.getRegion(args[1]), args[1], p);
					}
				}
			}

			else if (args[0].equalsIgnoreCase("list-temp-remove-group"))
			{
				if (args.length == 2)
				{
					if (PermissionsCore.doesHaveNode(p, "regios.permissons.group.remove")) {
						perms.listRemoveTempGroup(rm.getRegion(args[1]), args[1], p);
					}
				}
			}

			/*
			 * Permissions
			 */

			/*
			 * Spout
			 */

			else if (args.length >= 3 && args[0].equalsIgnoreCase("set-spout-welcome")) {
				if (PermissionsCore.doesHaveNode(p, "regios.spout.messages")) {
					spout.setWelcome(rm.getRegion(args[1]), args[1], args, p);
				} else {
					PermissionsCore.sendInvalidPerms(p);
				}
			}

			else if (args.length >= 3 && args[0].equalsIgnoreCase("show-spout-welcome")) {
				if (PermissionsCore.doesHaveNode(p, "regios.spout.messages")) {
					spout.setUseWelcome(rm.getRegion(args[1]), args[1], args[2], p);
				} else {
					PermissionsCore.sendInvalidPerms(p);
				}
			}

			else if (args.length >= 3 && args[0].equalsIgnoreCase("set-spout-leave")) {
				if (PermissionsCore.doesHaveNode(p, "regios.spout.messages")) {
					spout.setLeave(rm.getRegion(args[1]), args[1], args, p);
				} else {
					PermissionsCore.sendInvalidPerms(p);
				}
			}

			else if (args.length >= 3 && args[0].equalsIgnoreCase("show-spout-leave")) {
				if (PermissionsCore.doesHaveNode(p, "regios.spout.messages")) {
					spout.setUseLeave(rm.getRegion(args[1]), args[1], args[2], p);
				} else {
					PermissionsCore.sendInvalidPerms(p);
				}
			}

			else if (args.length == 3 && args[0].equalsIgnoreCase("spout-welcome-id")) {
				if (PermissionsCore.doesHaveNode(p, "regios.spout.messages")) {
					spout.setWelcomeMaterial(rm.getRegion(args[1]), args[1], args[2], p);
				} else {
					PermissionsCore.sendInvalidPerms(p);
				}
			}

			else if (args.length == 3 && args[0].equalsIgnoreCase("spout-leave-id")) {
				if (PermissionsCore.doesHaveNode(p, "regios.spout.messages")) {
					spout.setLeaveMaterial(rm.getRegion(args[1]), args[1], args[2], p);
				} else {
					PermissionsCore.sendInvalidPerms(p);
				}
			}

			else if (args.length == 3 && args[0].equalsIgnoreCase("spout-texture-url")) {
				if (PermissionsCore.doesHaveNode(p, "regios.spout.texture")) {
					spout.setTexturePackURL(rm.getRegion(args[1]), args[1], args[2], p);
				} else {
					PermissionsCore.sendInvalidPerms(p);
				}
			}

			else if (args.length == 3 && args[0].equalsIgnoreCase("use-texture-url")) {
				if (PermissionsCore.doesHaveNode(p, "regios.spout.texture")) {
					spout.setUseTextures(rm.getRegion(args[1]), args[1], args[2], p);
				} else {
					PermissionsCore.sendInvalidPerms(p);
				}
			}

			else if (args.length == 3 && args[0].equalsIgnoreCase("use-music-url")) {
				if (PermissionsCore.doesHaveNode(p, "regios.spout.music")) {
					spout.setUseMusic(rm.getRegion(args[1]), args[1], args[2], p);
				} else {
					PermissionsCore.sendInvalidPerms(p);
				}
			}

			else if (args.length == 3 && args[0].equalsIgnoreCase("add-music-url")) {
				if (PermissionsCore.doesHaveNode(p, "regios.spout.music")) {
					spout.setAddMusic(rm.getRegion(args[1]), args[1], args[2], p);
				} else {
					PermissionsCore.sendInvalidPerms(p);
				}
			}

			else if (args.length == 3 && args[0].equalsIgnoreCase("rem-music-url")) {
				if (PermissionsCore.doesHaveNode(p, "regios.spout.music")) {
					spout.setRemoveMusic(rm.getRegion(args[1]), args[1], args[2], p);
				} else {
					PermissionsCore.sendInvalidPerms(p);
				}
			}

			else if (args.length == 2 && args[0].equalsIgnoreCase("reset-music-url")) {
				if (PermissionsCore.doesHaveNode(p, "regios.spout.music")) {
					spout.setResetMusic(rm.getRegion(args[1]), args[1], args[2], p);
				} else {
					PermissionsCore.sendInvalidPerms(p);
				}
			}

			/*
			 * End Spout
			 */

			/*
			 * Misc. Cmd
			 */

			else if (args[0].equalsIgnoreCase("check")) {
				miscCmd.check(p, args);
			}

			else if (args[0].equalsIgnoreCase("cmdset") || args[0].equalsIgnoreCase("cmd-set") || args[0].equalsIgnoreCase("commandset") || args[0].equalsIgnoreCase("command-set")) {
				miscCmd.cmdset(p, args);
			}

			else if (args[0].equalsIgnoreCase("gamemode") || args[0].equalsIgnoreCase("gm")) {
				miscCmd.gamemode(p, args);
			}

			else if (args[0].equalsIgnoreCase("plot")) {
				miscCmd.plot(p, args);
			}

			/*
			 * End Misc. Cmd
			 */

			/*
			 * Administration
			 */

			else if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
				if (PermissionsCore.doesHaveNode(p, "regios.data.reload")) {
					admin.reload(p);
				} else {
					PermissionsCore.sendInvalidPerms(p);
				}
			}

			else if (args.length == 1 && args[0].equalsIgnoreCase("version")) {
				if (PermissionsCore.doesHaveNode(p, "regios.data.version")) {
					p.sendMessage(Message.REGIOSVERSION.getMessage());
				} else {
					PermissionsCore.sendInvalidPerms(p);
				}
			}

			else if (args.length == 3 && args[0].equalsIgnoreCase("set-owner")) {
				if (PermissionsCore.doesHaveNode(p, "regios.data.set-owner")) {
					admin.setOwner(rm.getRegion(args[1]), args[1], args[2], p);
				} else {
					PermissionsCore.sendInvalidPerms(p);
				}
			}

			else if (args[0].equalsIgnoreCase("list")) {
				admin.listRegions(p, args);
			}

			else if (args.length == 3 && args[0].equalsIgnoreCase("inherit")) {
				if (PermissionsCore.doesHaveNode(p, "regios.data.inherit")) {
					admin.inherit(rm.getRegion(args[1]), rm.getRegion(args[2]), args[1], args[2], p);
				} else {
					PermissionsCore.sendInvalidPerms(p);
				}
			}

			else if (args.length == 2 && (args[0].equalsIgnoreCase("list-backups") || args[0].equalsIgnoreCase("list-backup"))) {
				admin.listRegionBackups(rm.getRegion(args[1]), args[1], p);
			}

			else if (args.length == 3 && (args[0].equalsIgnoreCase("backup-region") || args[0].equalsIgnoreCase("save-region"))) {
				if (PermissionsCore.doesHaveNode(p, "regios.data.backup-region")) {
					RBF_Core.backup.startSave(rm.getRegion(args[1]), args[2], p);
				} else {
					PermissionsCore.sendInvalidPerms(p);
				}
			}

			else if ((args[0].equalsIgnoreCase("save-blueprint") || args[0].equalsIgnoreCase("saveblueprint") || args[0].equalsIgnoreCase("savebp"))) {
				if (PermissionsCore.doesHaveNode(p, "regios.data.save-blueprint")) {
					if (args.length == 2) {
						if(ConfigurationData.useWorldEdit)
						{
							new WorldEditCommands().createBlueprintWE(p, args[1]);
						} else {
							creation.createBlueprint(p, args[1]);
						}
					} else {
						p.sendMessage(Message.INVALIDARGUMENTCOUNT.getMessage());
						p.sendMessage(Message.PROPERUSAGE.getMessage() + CMDText.SAVEBLUEPRINT.getText());
					}
				} else {
					PermissionsCore.sendInvalidPerms(p);
				}
			}

			else if (args.length == 2 && (args[0].equalsIgnoreCase("load-blueprint") || args[0].equalsIgnoreCase("loadblueprint") || args[0].equalsIgnoreCase("loadbp"))) {
				if (PermissionsCore.doesHaveNode(p, "regios.data.load-blueprint")) {
					File f = new File("plugins" + File.separator + "Regios" + File.separator + "Blueprints" + File.separator + args[1] + ".blp");

					if (!f.exists()) {
						p.sendMessage(Message.BLUEPRINTDOESNTEXIST.getMessage());
					}
					pm.getLoadingTerrain().put(p.getName(), new ShareData(args[1], "blp", p));
					p.sendMessage(Message.BLUEPRINTPASTE.getMessage());
				} else {
					PermissionsCore.sendInvalidPerms(p);
				}
			}
			
			else if (args[0].equalsIgnoreCase("save-schematic") || args[0].equalsIgnoreCase("saveschematic") || args[0].equalsIgnoreCase("savesch")) {
				if (PermissionsCore.doesHaveNode(p, "regios.data.save-schematic")) {
					if (args.length == 2) {
						if(ConfigurationData.useWorldEdit)
						{
							new WorldEditCommands().createSchematicWE(p, args[1]);
						} else {
							creation.createSchematic(p, args[1]);
						}
					} else {
						p.sendMessage(Message.INVALIDARGUMENTCOUNT.getMessage());
						p.sendMessage(Message.PROPERUSAGE.getMessage() + CMDText.SAVESCHEMATIC.getText());
					}
				} else {
					PermissionsCore.sendInvalidPerms(p);
				}
			}
			
			else if (args.length == 2 && (args[0].equalsIgnoreCase("load-schematic") || args[0].equalsIgnoreCase("loadschematic") || args[0].equalsIgnoreCase("loadsch"))) {
				if (PermissionsCore.doesHaveNode(p, "regios.data.load-blueprint")) {
					File f = new File("plugins" + File.separator + "Regios" + File.separator + "Schematics" + File.separator + args[1] + ".schematic");

					if (!f.exists()) {
						p.sendMessage(Message.SCHEMATICDOESNTEXIST.getMessage());
					}
					pm.getLoadingTerrain().put(p.getName(), new ShareData(args[1], "sch", p));
					p.sendMessage(Message.SCHEMATICPASTE.getMessage());
				} else {
					PermissionsCore.sendInvalidPerms(p);
				}
			}

			else if (args.length == 1 && (args[0].equalsIgnoreCase("undo"))) {
				if (PermissionsCore.doesHaveNode(p, "regios.data.load-blueprint")) {
					RBF_Core.undoLoad(p);
				} else {
					PermissionsCore.sendInvalidPerms(p);
				}
			}

			else if (args.length == 3 && (args[0].equalsIgnoreCase("restore-region") || args[0].equalsIgnoreCase("load-region"))) {
				try {
					if (PermissionsCore.doesHaveNode(p, "regios.data.restore-region")) {
						RBF_Core.backup.loadBackup(rm.getRegion(args[1]), args[2], p);
					} else {
						PermissionsCore.sendInvalidPerms(p);
					}
				} catch (IOException e) {
					e.printStackTrace();
				} catch (RegionExistanceException e) {
					e.printStackTrace();
				} catch (FileExistanceException e) {
					e.printStackTrace();
				} catch (InvalidNBTFormat e) {
					e.printStackTrace();
				}
			}

			else if (args.length == 2 && args[0].equalsIgnoreCase("backup-database")) {
				try {
					if (PermissionsCore.doesHaveNode(p, "regios.data.backup-database")) {
						Zippable.zipDir(new File("plugins" + File.separator + "Regios" + File.separator + "Database"), new File("plugins" + File.separator + "Regios"
								+ File.separator + "Backups" + File.separator + args[1] + ".zip"), args[1], p);
					} else {
						PermissionsCore.sendInvalidPerms(p);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				p.sendMessage(Message.INVALIDCOMMAND.getMessage());
			}

			RegionCommandEvent event = new RegionCommandEvent("RegionCommandEvent");
			event.setProperties(sender, label, args);
			Bukkit.getServer().getPluginManager().callEvent(event);

			return true;
		}

		return true;
	}
}
