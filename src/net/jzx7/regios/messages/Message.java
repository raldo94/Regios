package net.jzx7.regios.messages;

import net.jzx7.regios.RegiosPlugin;

public enum Message {
	
	CONSOLEUNSUPPORTED("Regios doesn't support console commands yet. Please log in and excute your command as a player."),
	SPOUTPLUGINREQUIRED("<RED>The Spout server plugin is required for this feature!"),
	SPOUTCRAFTREQUIRED("<RED>The Spoutcraft launcher is required for this feature!"),
	INVALIDARGUMENTCOUNT("<RED>Invalid number of arguments specified."),
	INVALIDCOMMAND("<RED>Invalid command! Use /regios help for a list of commands."),
	REGIOSDOESNTEXIST("<RED>This region does not exist!"),
	REGIOSVERSION("<BGREEN>Running version : <BLUE>" + RegiosPlugin.regios.getRegiosVersion()),
	BLUEPRINTPASTE("<BGREEN>Click the block where you wish to paste the blueprint."),
	BLUEPRINTDOESNTEXIST("<RED>A Blueprint file with that name does not exist!"),
	SCHEMATICPASTE("<BGREEN>Click the block where you wish to paste the schematic."),
	SCHEMATICDOESNTEXIST("<RED>A Blueprint file with that name does not exist!"),
	PROPERUSAGE("Proper Usage: "),
	RESERVEDWORD("<RED>You cannot use the reserved word: "),
	
	NOTIMPLEMENTED("Not implemented yet. Sorry! :("),
	
	INVALIDCHARACTERS("<RED>Name contained invalid characters : "),
	
	SPOUTDETECTED("Spout detected! Spout support enabled!"),
	SPOUTNOTDETECTED("Spout not detected. No Spout support."),
	WORLDEDITDETECTED("WorldEdit detected! WorldEdit support enabled!"),
	WORLDEDITNOTDETECTED("WorldEdit not detected. No World support."),
	VAULTDETECTED("Vault detected! Vault support enabled!"),
	VALUTNOTDETECTED("Vault not detected. No economy or permissions support. OP or superperms only."),
	
	PERMISSONSSUPPORT("Permissions support enabled for "),
	PERMISSIONDENIED("<RED>You don't have permissions to do this!"),
	
	ECONOMYSUPPORT("Economy support enabled for "),
	ECONOMYDISABLED("UseEconomy set to true, but no economy provider was found. Disabling economy support."),
	ECONOMYSIGNREMOVEALDENIED("<RED>You cannot destroy this sign!"),
	ECONOMYSIGNCREATED("<BGREEN>Sale sign created for region : "),
	ECONOMYPRICE("<BGREEN>Price: "),
	ECONOMYINVALIDPRICE("<RED>Invalid price entered: "),
	ECONOMYNOTFORSALE("<RED> This region is not for sale: "),
	
	SCHEDULERSTART("Starting scheduler task..."),
	SCHEDULERRUNNING("Scheduler task initiated!"),
	SCHEDULERSTOPPING("Shutting down scheduler task..."),
	SCHEDULERSTOPPED("Scheduler task stopped successfully!"),
	
	REGIOSENABLED("Regios version " + RegiosPlugin.regios.getRegiosVersion() + " enabled successfully!"),
	REGIOSDISABLED("Regios version " + RegiosPlugin.regios.getRegiosVersion() + " disabled successfully!"),
	
	WORLDEDITREGIONUNSUPPORTED("<RED>The type of region selected in WorldEdit is unsupported in Regios"),
	WORLDEDITSELECTREGIONFIRST("<RED>Select a region with WorldEdit first."),
	WORLDEDITMODETRUE("<GOLD>Command unavailable while WorldEdit mode is true"),
	
	REGIONALREADYEXISTS("<RED>A region with this name already exists: "),
	REGIONDOESNTEXIST("<RED>A region with this name does not exist: "),
	REGIONPVPDISABLED("<RED>Pvp disabled for this region!"),
	REGIONBUILDDENIED("<RED>You are not permitted to build in this area!"),
	REGIONITEMDENIED("<RED>You cannot place this item in this region!"),
	REGIONPOINTSNOTSET("<RED>You must set your points first!"),
	REGIONPOLYPOINTSNOTSET("<RED>You must set at least 3 points!"),
	REGIONCUBEPOINTSNOTSET("<RED>You must set 2 points!"),
	REGIONEXPANDMAX("<BGREEN>Selection expanded from bedrock to sky."),
	REGIONSETTINGCANCELLED("<RED>Region setting cancelled."),
	REGIONCREATEDSUCCESS("<BGREEN>Region created successfully: "),
	
	PLAYERALREADYSETTINGPOINTS("<RED>You are already setting a region!"),
	PLAYERSETCUBOIDINSTRUCT("<BGREEN>Left and right click to select points."),
	PLAYERSETPOLYGONINSTRUCT("<BGREEN>Left click to add point and right click to remove last point.")
	;
	
	private String message;
	
	private String prefix = "[Regios] ";
	
	Message(String msg){
		this.message = msg;
	}
	
	public String getMessage() {
		return prefix + MsgFormat.colourFormat(message);
	}
	
	public String getUnformattedMessage() {
		return prefix + MsgFormat.removeColourFormat(message);
	}
}