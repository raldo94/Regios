package couk.Adamki11s.Regios.API;

import couk.Adamki11s.Regios.Mutable.MutableAdministration;
import couk.Adamki11s.Regios.Mutable.MutableEconomy;
import couk.Adamki11s.Regios.Mutable.MutableExceptions;
import couk.Adamki11s.Regios.Mutable.MutableFun;
import couk.Adamki11s.Regios.Mutable.MutableInventory;
import couk.Adamki11s.Regios.Mutable.MutableMessages;
import couk.Adamki11s.Regios.Mutable.MutableMisc;
import couk.Adamki11s.Regios.Mutable.MutableMobs;
import couk.Adamki11s.Regios.Mutable.MutableModes;
import couk.Adamki11s.Regios.Mutable.MutableModification;
import couk.Adamki11s.Regios.Mutable.MutablePermissions;
import couk.Adamki11s.Regios.Mutable.MutableProtection;
import couk.Adamki11s.Regios.Mutable.MutableProtectionMisc;
import couk.Adamki11s.Regios.Mutable.MutableSpout;

public class SaveData {
	
	private final MutableAdministration mutableAdministration = new MutableAdministration();
	private final MutableEconomy mutableEconomy = new MutableEconomy();
	private final MutableExceptions mutableExceptions = new MutableExceptions();
	private final MutableFun mutableFun = new MutableFun();
	private final MutableInventory mutableInventory = new MutableInventory();
	private final MutableMessages mutableMessages = new MutableMessages();
	private final MutableMisc mutableMisc = new MutableMisc();
	private final MutableMobs mutableMobs = new MutableMobs();
	private final MutableModes mutableModes = new MutableModes();
	private final MutableModification mutableModification = new MutableModification();
	private final MutablePermissions mutablePermissions = new MutablePermissions();
	private final MutableProtection mutableProtection = new MutableProtection();
	private final MutableProtectionMisc mutableProtectionMisc = new MutableProtectionMisc();
	private final MutableSpout mutableSpout = new MutableSpout();
	
	public MutableAdministration getMutableAdministration() {
		return mutableAdministration;
	}
	public MutableEconomy getMutableEconomy() {
		return mutableEconomy;
	}
	public MutableExceptions getMutableExceptions() {
		return mutableExceptions;
	}
	public MutableFun getMutableFun() {
		return mutableFun;
	}
	public MutableInventory getMutableInventory() {
		return mutableInventory;
	}
	public MutableMessages getMutableMessages() {
		return mutableMessages;
	}
	public MutableMisc getMutableMisc() {
		return mutableMisc;
	}
	public MutableMobs getMutableMobs() {
		return mutableMobs;
	}
	public MutableModes getMutableModes() {
		return mutableModes;
	}
	public MutableModification getMutableModification() {
		return mutableModification;
	}
	public MutablePermissions getMutablePermissions() {
		return mutablePermissions;
	}
	public MutableProtection getMutableProtection() {
		return mutableProtection;
	}
	public MutableProtectionMisc getMutableProtectionMisc() {
		return mutableProtectionMisc;
	}
	public MutableSpout getMutableSpout() {
		return mutableSpout;
	}


}
