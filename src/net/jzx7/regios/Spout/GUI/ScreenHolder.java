package net.jzx7.regios.Spout.GUI;

import java.util.ArrayList;
import java.util.HashMap;

import net.jzx7.regios.Data.HelpText;

import org.getspout.spoutapi.gui.GenericButton;
import org.getspout.spoutapi.gui.GenericContainer;
import org.getspout.spoutapi.gui.GenericLabel;
import org.getspout.spoutapi.gui.GenericTextField;
import org.getspout.spoutapi.gui.Widget;
import org.getspout.spoutapi.player.SpoutPlayer;


public class ScreenHolder {

	public static HashMap<SpoutPlayer, ScreenHolder> screenHolder = new HashMap<SpoutPlayer, ScreenHolder>();

	public ArrayList<GenericLabel> generalDataText() {
		ArrayList<GenericLabel> general = new ArrayList<GenericLabel>();
		
		for(String s : HelpText.GENERAL.getText()){
			general.add(new GenericLabel(s));
		}
		
		return general;
	}

	public ArrayList<GenericLabel> modeText() {
		ArrayList<GenericLabel> general = new ArrayList<GenericLabel>();
		
		for(String s : HelpText.MODE.getText()){
			general.add(new GenericLabel(s));
		}
		
		return general;
	}

	public ArrayList<GenericLabel> protectionText() {
		ArrayList<GenericLabel> general = new ArrayList<GenericLabel>();
		
		for(String s : HelpText.PROTECTION.getText()){
			general.add(new GenericLabel(s));
		}
		
		return general;
	}

	public ArrayList<GenericLabel> messagesText() {
		ArrayList<GenericLabel> general = new ArrayList<GenericLabel>();
		
		for(String s : HelpText.MESSAGES.getText()){
			general.add(new GenericLabel(s));
		}
		
		return general;
	}

	public ArrayList<GenericLabel> funText() {
		ArrayList<GenericLabel> general = new ArrayList<GenericLabel>();
		
		for(String s : HelpText.FUN.getText()){
			general.add(new GenericLabel(s));
		}
		
		return general;
	}

	public ArrayList<GenericLabel> otherText() {
		ArrayList<GenericLabel> general = new ArrayList<GenericLabel>();
		
		for(String s : HelpText.MISC.getText()){
			general.add(new GenericLabel(s));
		}
		
		return general;
	}

	public ArrayList<GenericLabel> inventText() {
		ArrayList<GenericLabel> general = new ArrayList<GenericLabel>();
		
		for(String s : HelpText.INVENTORY.getText()){
			general.add(new GenericLabel(s));
		}
		
		return general;
	}

	public ArrayList<GenericLabel> exceptionText() {
		ArrayList<GenericLabel> general = new ArrayList<GenericLabel>();
		
		for(String s : HelpText.EXCEPTIONS.getText()){
			general.add(new GenericLabel(s));
		}
		
		return general;
	}

	public ArrayList<GenericLabel> modifyText() {
		ArrayList<GenericLabel> general = new ArrayList<GenericLabel>();
		
		for(String s : HelpText.MODIFICATION.getText()){
			general.add(new GenericLabel(s));
		}
		
		return general;
	}

	public ArrayList<GenericLabel> spoutText() {
		ArrayList<GenericLabel> general = new ArrayList<GenericLabel>();
		
		for(String s : HelpText.SPOUT.getText()){
			general.add(new GenericLabel(s));
		}
		
		return general;
	}

	public ArrayList<GenericLabel> permissionsText() {
		ArrayList<GenericLabel> general = new ArrayList<GenericLabel>();
		
		for(String s : HelpText.PERMISSIONS.getText()){
			general.add(new GenericLabel(s));
		}
		
		return general;
	}

	public ArrayList<GenericLabel> dataText() {
		ArrayList<GenericLabel> general = new ArrayList<GenericLabel>();
		
		for(String s : HelpText.DATA.getText()){
			general.add(new GenericLabel(s));
		}
		
		return general;
	}

	public SpoutPlayer sp;

	public Widget[] page1Widgets = { new GenericButton("Block Break Protection"),
			new GenericButton("Block Place Protection"),
			new GenericButton("Prevent Entry"),
			new GenericButton("Prevent Exit"),
			new GenericButton("Prevent Interaction"),
			new GenericButton("Doors Locked"),
			new GenericButton("Chests Locked"),
			new GenericButton("Fire Protection"),
			new GenericButton("Block Form"),
			new GenericButton("Mobs Spawn"),
			new GenericButton("Monsters Spawn"),
			new GenericButton("Show Welcome"),
			new GenericButton("Show Leave"),
			new GenericButton("Show Prevent Entry"),
			new GenericButton("Show Prevent Exit"),
			new GenericButton("Show Protection"),
			new GenericButton("Show Pvp"),
			new GenericButton("PvP"),
			new GenericButton("Health Enabled"),
			new GenericButton("Protection Mode"),
			new GenericButton("Prevent Entry Mode"),
			new GenericButton("Prevent Exit Mode"),
			new GenericButton("Item Mode"),
			new GenericButton("Force Commands"),
			new GenericButton("General Protection"), };

	public GenericButton escButton = new GenericButton(), pageForward = new GenericButton(), pageBackwards = new GenericButton();

	public GenericButton generalData, modes, protection, messages, fun, other, inventory, perms, exceptions, modify, spout, data;

	public GenericLabel pageTracker;

	public Widget[] page2Widgets = { new GenericTextField(),
			new GenericTextField(),
			new GenericTextField(),
			new GenericTextField(),
			new GenericTextField(),
			new GenericLabel("Welcome Message"),
			new GenericLabel("Leave Message"),
			new GenericLabel("Prevent Entry Message"),
			new GenericLabel("Prevent Exit Message"),
			new GenericLabel("Protection Message"),
			new GenericButton("Update Messages"),
			new GenericButton("Reset"),
			new GenericButton("Reset"),
			new GenericButton("Reset"),
			new GenericButton("Reset"),
			new GenericButton("Reset"),
			new GenericButton("Clear"),
			new GenericButton("Clear"),
			new GenericButton("Clear"),
			new GenericButton("Clear"),
			new GenericButton("Clear"), };

	public Widget[] page3Widgets = { new GenericLabel("LSPS"),
			new GenericTextField(),
			new GenericButton("Update"),
			new GenericLabel("Health-Regen"),
			new GenericTextField(),
			new GenericButton("Update"),
			new GenericLabel("Velocity-Warp"),
			new GenericTextField(),
			new GenericButton("Update"),
			new GenericButton("For Sale"),
			new GenericLabel("Sale Price"),
			new GenericTextField(),
			new GenericButton("Update"),
			new GenericButton("WipeAndCacheEnter"),
			new GenericButton("WipeAndCacheExit"),
			new GenericButton("PermWipeEnter"),
			new GenericButton("PermWipeExit"), };

	public Widget[] page4Widgets = { new GenericLabel("Exceptions"),
			new GenericButton("Players"),
			new GenericButton("Nodes"),
			new GenericButton("Sub Owners"),
			new GenericButton("Items"),
			new GenericTextField(),
			new GenericButton("Add"),
			new GenericButton("Remove"),
			new GenericButton("Erase"),
			new GenericLabel("Indicator"),
			new GenericButton("<"),
			new GenericButton(">"),
			new GenericContainer() };

	public Widget[] page5Widgets = { new GenericButton("Cache-Add"),
			new GenericButton("Cache-Rem"),
			new GenericButton("Perm-Add"),
			new GenericButton("Perm-Rem"),
			new GenericTextField(),
			new GenericButton("Add"),
			new GenericButton("Remove"),
			new GenericButton("Erase"),
			new GenericLabel("Indicator"),
			new GenericButton("<"),
			new GenericButton(">"),
			new GenericContainer(),
			new GenericButton("Cmd-Set"), };

	public Widget[] page6Widgets = { new GenericTextField(),
			new GenericTextField(),
			new GenericButton("Reset"),
			new GenericButton("Reset"),
			new GenericButton("Clear"),
			new GenericButton("Clear"),
			new GenericButton("Update"),
			new GenericTextField(),
			new GenericTextField(),
			new GenericButton("Update"),
			new GenericButton("Update"),
			new GenericTextField(),
			new GenericButton("Reset"),
			new GenericButton("Clear"),
			new GenericButton("Paste"),
			new GenericButton("Use Textures"),
			new GenericLabel("Spout Welcome Message"),
			new GenericLabel("Spout Leave Message"),
			new GenericLabel("Spout Welcome Icon ID"),
			new GenericLabel("Spout Leave Icon ID"),
			new GenericLabel("Texture Pack URL"),
			new GenericButton("Update"),
			new GenericButton("Enabled"),
			new GenericButton("Enabled"), };

	public Widget[] page7Widgets = { new GenericTextField(),
			new GenericLabel("Music"),
			new GenericLabel("Page : "),
			new GenericContainer(),
			new GenericButton("<"),
			new GenericButton(">"),
			new GenericButton("Add Music"),
			new GenericButton("Remove Music"),
			new GenericButton("Clear Music"), };

	public static HashMap<SpoutPlayer, ArrayList<Widget>> page4Exceptions = new HashMap<SpoutPlayer, ArrayList<Widget>>();

	public void addScreenHolder(SpoutPlayer sp, ScreenHolder sh) {
		screenHolder.put(sp, sh);
	}

	public static ScreenHolder getScreenHolder(SpoutPlayer sp) {
		if (screenHolder.containsKey(sp)) {
			return screenHolder.get(sp);
		} else {
			return new ScreenHolder();
		}
	}
}
