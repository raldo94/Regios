package net.jzx7.regios.bukkit.SpoutPlugin.GUI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import net.jzx7.regios.RegiosPlugin;
import net.jzx7.regios.Mutable.MutableMisc;
import net.jzx7.regios.Mutable.MutablePermissions;
import net.jzx7.regios.bukkit.SpoutPlugin.GUI.RegionScreenManager.RGB;
import net.jzx7.regiosapi.regions.Region;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.getspout.spoutapi.gui.Button;
import org.getspout.spoutapi.gui.Container;
import org.getspout.spoutapi.gui.GenericButton;
import org.getspout.spoutapi.gui.GenericContainer;
import org.getspout.spoutapi.gui.GenericLabel;
import org.getspout.spoutapi.gui.GenericTextField;
import org.getspout.spoutapi.gui.RenderPriority;
import org.getspout.spoutapi.gui.TextField;
import org.getspout.spoutapi.gui.Widget;
import org.getspout.spoutapi.player.SpoutPlayer;


public class RegionScreen5 {
	
	//need to add cmd checking to this.

	public static enum PermToggle {
		CACHE_ADD, CACHE_REM, PERM_ADD, PERM_REMOVE, SET;
	}

	public static HashMap<SpoutPlayer, PermToggle> toggle = new HashMap<SpoutPlayer, PermToggle>();
	public static HashMap<SpoutPlayer, Integer> currentPage = new HashMap<SpoutPlayer, Integer>();

	static final MutablePermissions perms = new MutablePermissions();
	static final MutableMisc misc = new MutableMisc();

	public static int getExceptionPages(int exceptions) {
		if (exceptions <= 5) {
			return 1;
		} else {
			if ((exceptions % 5) != 0) {
				return (exceptions / 5) + 1;
			} else {
				return (exceptions / 5);
			}
		}
	}

	public static void switchToggle(SpoutPlayer sp, PermToggle tog, ScreenHolder sh, Region r, Button butt, boolean silent) {
		((GenericButton) sh.page5Widgets[0]).setTextColor(RGB.WHITE.getColour());
		((GenericButton) sh.page5Widgets[1]).setTextColor(RGB.WHITE.getColour());
		((GenericButton) sh.page5Widgets[2]).setTextColor(RGB.WHITE.getColour());
		((GenericButton) sh.page5Widgets[3]).setTextColor(RGB.WHITE.getColour());
		((GenericButton) sh.page5Widgets[12]).setTextColor(RGB.WHITE.getColour());
		((GenericButton) sh.page5Widgets[0]).setDirty(true);
		((GenericButton) sh.page5Widgets[1]).setDirty(true);
		((GenericButton) sh.page5Widgets[2]).setDirty(true);
		((GenericButton) sh.page5Widgets[3]).setDirty(true);
		((GenericButton) sh.page5Widgets[12]).setDirty(true);
		butt.setTextColor(RGB.GREEN.getColour());
		butt.setDirty(true);
		toggle.put(sp, tog);
		if (!silent) {
			sp.sendNotification("Permission Type Changed", ChatColor.GREEN + tog.toString(), Material.CACTUS);
		}
		updateButtons(sh, sp, tog);
		updateExceptionPages(sp, currentPage.get(sp), sh, r);
	}

	public static void nextPage(SpoutPlayer sp, Region r, ScreenHolder sh) {
		switch (toggle.get(sp)) {
		case CACHE_ADD:
			if (currentPage.get(sp) >= getExceptionPages(r.getTempNodesCacheAdd().length)) {
				sp.sendNotification(ChatColor.RED + "Error!", "No next page!", Material.FIRE);
				return;
			}
			break;
		case CACHE_REM:
			if (currentPage.get(sp) >= getExceptionPages(r.getTempNodesCacheRem().length)) {
				sp.sendNotification(ChatColor.RED + "Error!", "No next page!", Material.FIRE);
				return;
			}
			break;
		case PERM_ADD:
			if (currentPage.get(sp) >= getExceptionPages(r.getPermAddNodes().length)) {
				sp.sendNotification(ChatColor.RED + "Error!", "No next page!", Material.FIRE);
				return;
			}
			break;
		case PERM_REMOVE:
			if (currentPage.get(sp) >= getExceptionPages(r.getPermRemoveNodes().length)) {
				sp.sendNotification(ChatColor.RED + "Error!", "No next page!", Material.FIRE);
				return;
			}
			break;
		case SET:
			if (currentPage.get(sp) >= getExceptionPages(r.getCommandSet().length)) {
				sp.sendNotification(ChatColor.RED + "Error!", "No next page!", Material.FIRE);
				return;
			}
			break;
		}

		currentPage.put(sp, currentPage.get(sp) + 1);
		updateExceptionPages(sp, currentPage.get(sp), sh, r);
	}

	public static void prevPage(SpoutPlayer sp, Region r, ScreenHolder sh) {
		if (currentPage.get(sp) == 1) {
			sp.sendNotification(ChatColor.RED + "Error!", "No previous page!", Material.FIRE);
		} else {
			currentPage.put(sp, currentPage.get(sp) - 1);
			updateExceptionPages(sp, currentPage.get(sp), sh, r);
		}
	}

	public static void eraseExceptions(PermToggle toggle, SpoutPlayer sp, Region r) {
		switch (toggle) {
		case CACHE_ADD:
			perms.editResetTempAddCache(r);
			sp.sendNotification("Cache", ChatColor.GREEN + "Permissions erased", Material.PAPER);
			break;
		case CACHE_REM:
			perms.editResetTempRemCache(r);
			sp.sendNotification("Cache", ChatColor.GREEN + "Permissions erased", Material.PAPER);
			break;
		case PERM_ADD:
			perms.editResetPermAddCache(r);
			sp.sendNotification("Perm-Add", ChatColor.GREEN + "Permissions erased", Material.PAPER);
			break;
		case PERM_REMOVE:
			perms.editResetPermRemoveCache(r);
			sp.sendNotification("Perm-Remove", ChatColor.GREEN + "Permissions erased", Material.PAPER);
			break;
		case SET:
			misc.editResetForceCommandSet(r);
			sp.sendNotification("Command Set", ChatColor.GREEN + "Commands erased", Material.PAPER);
			break;
		}
		updateExceptionPages(sp, currentPage.get(sp), ScreenHolder.getScreenHolder(sp), r);
	}

	public static void addException(PermToggle toggle, SpoutPlayer sp, Region r, String ex, TextField tf) {
		switch (toggle) {
		case CACHE_ADD:
			if (perms.checkTempAddCache(r, ex)) {
				sp.sendNotification(ChatColor.RED + "Error!", "Permission Exists!", Material.FIRE);
				tf.setText("");
				tf.setDirty(true);
				break;
			} else {
				perms.editAddToTempAddCache(r, ex);
				sp.sendNotification(ChatColor.GREEN + "Temp Cache", "Permission added", Material.PAPER);
				tf.setText("");
				tf.setDirty(true);
				break;
			}
		case CACHE_REM:
			if (perms.checkTempRemCache(r, ex)) {
				sp.sendNotification(ChatColor.RED + "Error!", "Permission Exists!", Material.FIRE);
				tf.setText("");
				tf.setDirty(true);
				break;
			} else {
				perms.editAddToTempRemCache(r, ex);
				sp.sendNotification(ChatColor.GREEN + "Temp Cache", "Permission added", Material.PAPER);
				tf.setText("");
				tf.setDirty(true);
				break;
			}
		case PERM_ADD:
			if (perms.checkPermAdd(r, ex)) {
				sp.sendNotification(ChatColor.RED + "Error!", "Permission exists!", Material.FIRE);
				tf.setText("");
				tf.setDirty(true);
				break;
			} else {
				perms.editAddToPermAddCache(r, ex);
				sp.sendNotification(ChatColor.GREEN + "Perm Add", "Permission added", Material.PAPER);
				tf.setText("");
				tf.setDirty(true);
				break;
			}
		case PERM_REMOVE:
			if (perms.checkPermRemove(r, ex)) {
				sp.sendNotification(ChatColor.RED + "Error!", "Permission exists!", Material.FIRE);
				tf.setText("");
				tf.setDirty(true);
				break;
			} else {
				perms.editAddToPermRemoveCache(r, ex);
				sp.sendNotification(ChatColor.GREEN + "Perm Remove", "Permission added", Material.PAPER);
				tf.setText("");
				tf.setDirty(true);
				break;
			}
		case SET:
			if (misc.checkCommandSet(r, ex)) {
				sp.sendNotification(ChatColor.RED + "Error!", "Command already exists!", Material.FIRE);
				tf.setText("");
				tf.setDirty(true);
				break;
			} else {
				misc.editAddToForceCommandSet(r, ex);
				sp.sendNotification(ChatColor.GREEN + "Command Set", "Command added", Material.PAPER);
				tf.setText("");
				tf.setDirty(true);
				break;
			}
		}
		updateExceptionPages(sp, currentPage.get(sp), ScreenHolder.getScreenHolder(sp), r);
	}

	public static void removeException(PermToggle toggle, SpoutPlayer sp, Region r, String ex, TextField tf) {
		switch (toggle) {
		case CACHE_ADD:
			if (!perms.checkTempAddCache(r, ex)) {
				sp.sendNotification(ChatColor.RED + "Error!", "Permission doesn't exist", Material.FIRE);
				tf.setText("");
				tf.setDirty(true);
				break;
			} else {
				perms.editRemoveFromTempAddCache(r, ex);
				sp.sendNotification(ChatColor.GREEN + "Temp Cache", "Permission removed", Material.PAPER);
				tf.setText("");
				tf.setDirty(true);
				break;
			}
		case CACHE_REM:
			if (!perms.checkTempRemCache(r, ex)) {
				sp.sendNotification(ChatColor.RED + "Error!", "Permission doesn't exist", Material.FIRE);
				tf.setText("");
				tf.setDirty(true);
				break;
			} else {
				perms.editRemoveFromTempRemCache(r, ex);
				sp.sendNotification(ChatColor.GREEN + "Temp Cache", "Permission removed", Material.PAPER);
				tf.setText("");
				tf.setDirty(true);
				break;
			}
		case PERM_ADD:
			if (!perms.checkPermAdd(r, ex)) {
				sp.sendNotification(ChatColor.RED + "Error!", "Permission doesn't exist", Material.FIRE);
				tf.setText("");
				tf.setDirty(true);
				break;
			} else {
				perms.editRemoveFromPermAddCache(r, ex);
				sp.sendNotification(ChatColor.GREEN + "Perm Add", "Permission removed", Material.PAPER);
				tf.setText("");
				tf.setDirty(true);
				break;
			}
		case PERM_REMOVE:
			if (!perms.checkPermRemove(r, ex)) {
				sp.sendNotification(ChatColor.RED + "Error!", "Permission doesn't exist", Material.FIRE);
				tf.setText("");
				tf.setDirty(true);
				break;
			} else {
				perms.editRemoveFromPermRemoveCache(r, ex);
				sp.sendNotification(ChatColor.GREEN + "Perm Remove", "Permission removed", Material.PAPER);
				tf.setText("");
				tf.setDirty(true);
				break;
			}
		case SET:
			if (!misc.checkCommandSet(r, ex)) {
				sp.sendNotification(ChatColor.RED + "Error!", "Command doesn't exist", Material.FIRE);
				tf.setText("");
				tf.setDirty(true);
				break;
			} else {
				misc.editRemoveFromForceCommandSet(r, ex);
				sp.sendNotification(ChatColor.GREEN + "Commands Set", "Command removed", Material.PAPER);
				tf.setText("");
				tf.setDirty(true);
				break;
			}
		}
		updateExceptionPages(sp, currentPage.get(sp), ScreenHolder.getScreenHolder(sp), r);
	}
	
	public static void updateButtons(ScreenHolder sh, SpoutPlayer sp, PermToggle tog){
		if(tog == PermToggle.CACHE_ADD || tog == PermToggle.CACHE_REM || tog == PermToggle.PERM_ADD || tog == PermToggle.PERM_REMOVE){
			((GenericButton) sh.page5Widgets[5]).setText("Add Permission");
			((GenericButton) sh.page5Widgets[5]).setDirty(true);
			((GenericButton) sh.page5Widgets[6]).setText("Remove Permission");
			((GenericButton) sh.page5Widgets[6]).setDirty(true);
			((GenericButton) sh.page5Widgets[7]).setText("Erase Permissions");
			((GenericButton) sh.page5Widgets[7]).setDirty(true);
		} else {
			((GenericButton) sh.page5Widgets[5]).setText("Add Command");
			((GenericButton) sh.page5Widgets[5]).setDirty(true);
			((GenericButton) sh.page5Widgets[6]).setText("Remove Command");
			((GenericButton) sh.page5Widgets[6]).setDirty(true);
			((GenericButton) sh.page5Widgets[7]).setText("Erase Commands");
			((GenericButton) sh.page5Widgets[7]).setDirty(true);
		}
	}

	public static void updateExceptionPages(SpoutPlayer sp, int page, ScreenHolder sh, Region r) {
		for (Widget w : ((GenericContainer) sh.page5Widgets[11]).getChildren()) {
			((GenericContainer) sh.page5Widgets[11]).removeChild(w);
		}
		
		ArrayList<String> sortedTempAddNodes = new ArrayList<String>();
		ArrayList<String> sortedTempRemNodes = new ArrayList<String>();
		ArrayList<String> sortedAddNodes = new ArrayList<String>();
		ArrayList<String> sortedRemNodes = new ArrayList<String>();
		ArrayList<String> sortedCmdSet = new ArrayList<String>();
		
		if(r.getTempNodesCacheAdd() != null){
		for(String s : r.getTempNodesCacheAdd()){
			sortedTempAddNodes.add(s);
		}
		}
		if(r.getTempNodesCacheRem() != null){
			for(String s : r.getTempNodesCacheRem()){
				sortedTempRemNodes.add(s);
			}
		}
		if(r.getPermAddNodes() != null){
		for(String s : r.getPermAddNodes()){
			sortedAddNodes.add(s);
		}
		}
		if(r.getPermRemoveNodes() != null){
		for(String s : r.getPermRemoveNodes()){
			sortedRemNodes.add(s);
		}
		}
		if(r.getCommandSet() != null){
		for(String s : r.getCommandSet()){
			sortedCmdSet.add(s);
		}
		}
		
		Collections.sort(sortedTempAddNodes);
		Collections.sort(sortedTempRemNodes);
		Collections.sort(sortedAddNodes);
		Collections.sort(sortedRemNodes);
		Collections.sort(sortedCmdSet);
		
		for (int exc = ((page * 5) - 5); exc < ((page * 5)); exc++) {

			switch (toggle.get(sp)) {
			case CACHE_ADD:
				((GenericLabel) sh.page5Widgets[8]).setText("Page " + currentPage.get(sp) + " / " + getExceptionPages(r.getTempNodesCacheAdd().length));
				((GenericLabel) sh.page5Widgets[8]).setDirty(true);
				((GenericContainer) sh.page5Widgets[11]).setDirty(true);
				if (exc < (sortedTempAddNodes.size()) && sortedTempAddNodes.get(exc).length() >= 2) {
					GenericLabel ex = new GenericLabel((sortedTempAddNodes.get(exc)));
					ex.setTextColor(RGB.YELLOW.getColour());
					((GenericContainer) sh.page5Widgets[11]).addChild(ex);
				} else {
					GenericLabel ex = new GenericLabel("-");
					ex.setTextColor(RGB.YELLOW.getColour());
					((GenericContainer) sh.page5Widgets[11]).addChild(ex);
				}
				break;
			case CACHE_REM:
				((GenericLabel) sh.page5Widgets[8]).setText("Page " + currentPage.get(sp) + " / " + getExceptionPages(r.getTempNodesCacheRem().length));
				((GenericLabel) sh.page5Widgets[8]).setDirty(true);
				((GenericContainer) sh.page5Widgets[11]).setDirty(true);
				if (exc < (sortedTempRemNodes.size()) && sortedTempRemNodes.get(exc).length() >= 2) {
					GenericLabel ex = new GenericLabel((sortedTempRemNodes.get(exc)));
					ex.setTextColor(RGB.YELLOW.getColour());
					((GenericContainer) sh.page5Widgets[11]).addChild(ex);
				} else {
					GenericLabel ex = new GenericLabel("-");
					ex.setTextColor(RGB.YELLOW.getColour());
					((GenericContainer) sh.page5Widgets[11]).addChild(ex);
				}
				break;
			case PERM_ADD:
				((GenericLabel) sh.page5Widgets[8]).setText("Page " + currentPage.get(sp) + " / " + getExceptionPages(sortedAddNodes.size()));
				((GenericLabel) sh.page5Widgets[8]).setDirty(true);
				((GenericContainer) sh.page5Widgets[11]).setDirty(true);
				if (exc < (sortedAddNodes.size()) && sortedAddNodes.get(exc).length() >= 2) {
					GenericLabel ex = new GenericLabel(sortedAddNodes.get(exc));
					ex.setTextColor(RGB.YELLOW.getColour());
					((GenericContainer) sh.page5Widgets[11]).addChild(ex);
				} else {
					GenericLabel ex = new GenericLabel("-");
					ex.setTextColor(RGB.YELLOW.getColour());
					((GenericContainer) sh.page5Widgets[11]).addChild(ex);
				}
				break;
			case PERM_REMOVE:
				((GenericLabel) sh.page5Widgets[8]).setText("Page " + currentPage.get(sp) + " / " + getExceptionPages(r.getPermRemoveNodes().length));
				((GenericLabel) sh.page5Widgets[8]).setDirty(true);
				((GenericContainer) sh.page5Widgets[11]).setDirty(true);
				if ((exc < sortedRemNodes.size()) && sortedRemNodes.get(exc).length() >= 2) {
					GenericLabel ex = new GenericLabel(sortedRemNodes.get(exc));
					ex.setTextColor(RGB.YELLOW.getColour());
					((GenericContainer) sh.page5Widgets[11]).addChild(ex);
				} else {
					GenericLabel ex = new GenericLabel("-");
					ex.setTextColor(RGB.YELLOW.getColour());
					((GenericContainer) sh.page5Widgets[11]).addChild(ex);
				}
				break;
			case SET:
				((GenericLabel) sh.page5Widgets[8]).setText("Page " + currentPage.get(sp) + " / " + getExceptionPages(r.getCommandSet().length));
				((GenericLabel) sh.page5Widgets[8]).setDirty(true);
				((GenericContainer) sh.page5Widgets[11]).setDirty(true);
				if ((exc < sortedCmdSet.size()) && sortedCmdSet.get(exc).length() >= 2) {
					GenericLabel ex = new GenericLabel(sortedCmdSet.get(exc));
					ex.setTextColor(RGB.YELLOW.getColour());
					((GenericContainer) sh.page5Widgets[11]).addChild(ex);
				} else {
					GenericLabel ex = new GenericLabel("-");
					ex.setTextColor(RGB.YELLOW.getColour());
					((GenericContainer) sh.page5Widgets[11]).addChild(ex);
				}
				break;
			}

		}
	}

	public static void loadScreen(SpoutPlayer sp, Region r, Object[] oldWidgets, ScreenHolder sh) {

		Collections.sort(r.getExceptions());

		if (oldWidgets != null) {
			for (Object w : oldWidgets) {
				((Widget) w).setDirty(true);
				((Widget) w).shiftYPos(1000);// work around for overlap layer //
												// stack bug
			}
		}

		for (Widget w : sh.page5Widgets) {
			w.setPriority(RenderPriority.Lowest);
		}

		currentPage.put(sp, 1);
		toggle.put(sp, PermToggle.CACHE_ADD);

		((GenericButton) sh.page5Widgets[0]).setText("Cache Add");
		((GenericButton) sh.page5Widgets[0]).setHeight(20);
		((GenericButton) sh.page5Widgets[0]).setWidth(80);
		((GenericButton) sh.page5Widgets[0]).setX(5);
		((GenericButton) sh.page5Widgets[0]).setY(55);
		((GenericButton) sh.page5Widgets[0]).setHoverColor(RGB.YELLOW.getColour());
		((GenericButton) sh.page5Widgets[0]).setTextColor(RGB.GREEN.getColour());

		if (RegionScreenManager.popup.get(sp).containsWidget(sh.page5Widgets[0])) {
			RegionScreenManager.popup.get(sp).getWidget(sh.page5Widgets[0].getId()).setVisible(true);
			RegionScreenManager.popup.get(sp).getWidget(sh.page5Widgets[0].getId()).setDirty(true);
		} else {
			RegionScreenManager.popup.get(sp).attachWidget(RegiosPlugin.regios, sh.page5Widgets[0]);
		}
		
		((GenericButton) sh.page5Widgets[1]).setText("Cache Remove");
		((GenericButton) sh.page5Widgets[1]).setHeight(20);
		((GenericButton) sh.page5Widgets[1]).setWidth(80);
		((GenericButton) sh.page5Widgets[1]).setX(90);
		((GenericButton) sh.page5Widgets[1]).setY(55);
		((GenericButton) sh.page5Widgets[1]).setHoverColor(RGB.YELLOW.getColour());
		((GenericButton) sh.page5Widgets[1]).setTextColor(RGB.GREEN.getColour());

		if (RegionScreenManager.popup.get(sp).containsWidget(sh.page5Widgets[1])) {
			RegionScreenManager.popup.get(sp).getWidget(sh.page5Widgets[1].getId()).setVisible(true);
			RegionScreenManager.popup.get(sp).getWidget(sh.page5Widgets[1].getId()).setDirty(true);
		} else {
			RegionScreenManager.popup.get(sp).attachWidget(RegiosPlugin.regios, sh.page5Widgets[1]);
		}

		((GenericButton) sh.page5Widgets[2]).setText("Perm Add");
		((GenericButton) sh.page5Widgets[2]).setHeight(20);
		((GenericButton) sh.page5Widgets[2]).setWidth(80);
		((GenericButton) sh.page5Widgets[2]).setX(175);
		((GenericButton) sh.page5Widgets[2]).setY(55);
		((GenericButton) sh.page5Widgets[2]).setHoverColor(RGB.YELLOW.getColour());

		if (RegionScreenManager.popup.get(sp).containsWidget(sh.page5Widgets[2])) {
			RegionScreenManager.popup.get(sp).getWidget(sh.page5Widgets[2].getId()).setVisible(true);
			RegionScreenManager.popup.get(sp).getWidget(sh.page5Widgets[2].getId()).setDirty(true);
		} else {
			RegionScreenManager.popup.get(sp).attachWidget(RegiosPlugin.regios, sh.page5Widgets[2]);
		}

		((GenericButton) sh.page5Widgets[3]).setText("Perm Remove");
		((GenericButton) sh.page5Widgets[3]).setHeight(20);
		((GenericButton) sh.page5Widgets[3]).setWidth(80);
		((GenericButton) sh.page5Widgets[3]).setX(260);
		((GenericButton) sh.page5Widgets[3]).setY(55);
		((GenericButton) sh.page5Widgets[3]).setHoverColor(RGB.YELLOW.getColour());

		if (RegionScreenManager.popup.get(sp).containsWidget(sh.page5Widgets[3])) {
			RegionScreenManager.popup.get(sp).getWidget(sh.page5Widgets[3].getId()).setVisible(true);
			RegionScreenManager.popup.get(sp).getWidget(sh.page5Widgets[3].getId()).setDirty(true);
		} else {
			RegionScreenManager.popup.get(sp).attachWidget(RegiosPlugin.regios, sh.page5Widgets[3]);
		}
		
		((GenericButton) sh.page5Widgets[12]).setText("Command Set");
		((GenericButton) sh.page5Widgets[12]).setHeight(20);
		((GenericButton) sh.page5Widgets[12]).setWidth(80);
		((GenericButton) sh.page5Widgets[12]).setX(345);
		((GenericButton) sh.page5Widgets[12]).setY(55);
		((GenericButton) sh.page5Widgets[12]).setHoverColor(RGB.YELLOW.getColour());

		if (RegionScreenManager.popup.get(sp).containsWidget(sh.page5Widgets[12])) {
			RegionScreenManager.popup.get(sp).getWidget(sh.page5Widgets[12].getId()).setVisible(true);
			RegionScreenManager.popup.get(sp).getWidget(sh.page5Widgets[12].getId()).setDirty(true);
		} else {
			RegionScreenManager.popup.get(sp).attachWidget(RegiosPlugin.regios, sh.page5Widgets[12]);
		}
		
		switchToggle(sp, PermToggle.CACHE_ADD, sh, r, ((GenericButton) sh.page5Widgets[0]), true);

		((GenericTextField) sh.page5Widgets[4]).setText("");
		((GenericTextField) sh.page5Widgets[4]).setHeight(20);
		((GenericTextField) sh.page5Widgets[4]).setWidth(160);
		((GenericTextField) sh.page5Widgets[4]).setX(10);
		((GenericTextField) sh.page5Widgets[4]).setY(95);
		((GenericTextField) sh.page5Widgets[4]).setMaximumCharacters(25);
		((GenericTextField) sh.page5Widgets[4]).setFieldColor(RGB.BLACK.getColour());
		((GenericTextField) sh.page5Widgets[4]).setBorderColor(RGB.SPRING_GREEN.getColour());

		if (RegionScreenManager.popup.get(sp).containsWidget(sh.page5Widgets[4])) {
			RegionScreenManager.popup.get(sp).getWidget(sh.page5Widgets[4].getId()).setVisible(true);
			RegionScreenManager.popup.get(sp).getWidget(sh.page5Widgets[4].getId()).setDirty(true);
		} else {
			RegionScreenManager.popup.get(sp).attachWidget(RegiosPlugin.regios, sh.page5Widgets[4]);
		}

		((GenericButton) sh.page5Widgets[5]).setText("Add Permission");
		((GenericButton) sh.page5Widgets[5]).setHeight(20);
		((GenericButton) sh.page5Widgets[5]).setWidth(160);
		((GenericButton) sh.page5Widgets[5]).setX(10);
		((GenericButton) sh.page5Widgets[5]).setY(125);
		((GenericButton) sh.page5Widgets[5]).setHoverColor(RGB.YELLOW.getColour());
		((GenericButton) sh.page5Widgets[5]).setTextColor(RGB.GREEN.getColour());

		if (RegionScreenManager.popup.get(sp).containsWidget(sh.page5Widgets[5])) {
			RegionScreenManager.popup.get(sp).getWidget(sh.page5Widgets[5].getId()).setVisible(true);
			RegionScreenManager.popup.get(sp).getWidget(sh.page5Widgets[5].getId()).setDirty(true);
		} else {
			RegionScreenManager.popup.get(sp).attachWidget(RegiosPlugin.regios, sh.page5Widgets[5]);
		}

		((GenericButton) sh.page5Widgets[6]).setText("Remove Permission");
		((GenericButton) sh.page5Widgets[6]).setHeight(20);
		((GenericButton) sh.page5Widgets[6]).setWidth(160);
		((GenericButton) sh.page5Widgets[6]).setX(10);
		((GenericButton) sh.page5Widgets[6]).setY(155);
		((GenericButton) sh.page5Widgets[6]).setHoverColor(RGB.YELLOW.getColour());
		((GenericButton) sh.page5Widgets[6]).setTextColor(RGB.RED.getColour());

		if (RegionScreenManager.popup.get(sp).containsWidget(sh.page5Widgets[6])) {
			RegionScreenManager.popup.get(sp).getWidget(sh.page5Widgets[6].getId()).setVisible(true);
			RegionScreenManager.popup.get(sp).getWidget(sh.page5Widgets[6].getId()).setDirty(true);
		} else {
			RegionScreenManager.popup.get(sp).attachWidget(RegiosPlugin.regios, sh.page5Widgets[6]);
		}

		((GenericButton) sh.page5Widgets[7]).setText("Erase Permissions");
		((GenericButton) sh.page5Widgets[7]).setHeight(20);
		((GenericButton) sh.page5Widgets[7]).setWidth(160);
		((GenericButton) sh.page5Widgets[7]).setX(10);
		((GenericButton) sh.page5Widgets[7]).setY(185);
		((GenericButton) sh.page5Widgets[7]).setHoverColor(RGB.YELLOW.getColour());

		if (RegionScreenManager.popup.get(sp).containsWidget(sh.page5Widgets[7])) {
			RegionScreenManager.popup.get(sp).getWidget(sh.page5Widgets[7].getId()).setVisible(true);
			RegionScreenManager.popup.get(sp).getWidget(sh.page5Widgets[7].getId()).setDirty(true);
		} else {
			RegionScreenManager.popup.get(sp).attachWidget(RegiosPlugin.regios, sh.page5Widgets[7]);
		}

		((GenericLabel) sh.page5Widgets[8]).setX(215);
		((GenericLabel) sh.page5Widgets[8]).setY(85);
		((GenericLabel) sh.page5Widgets[8]).setWidth(50);
		((GenericLabel) sh.page5Widgets[8]).setHeight(20);
		((GenericLabel) sh.page5Widgets[8]).setTextColor(RGB.YELLOW.getColour());
		((GenericLabel) sh.page5Widgets[8]).setText("Page 1 / " + getExceptionPages(r.getTempNodesCacheAdd().length));
		((GenericLabel) sh.page5Widgets[8]).setTooltip(ChatColor.YELLOW + "  Toggle between permissions");

		if (RegionScreenManager.popup.get(sp).containsWidget(sh.page5Widgets[8])) {
			RegionScreenManager.popup.get(sp).getWidget(sh.page5Widgets[8].getId()).setVisible(true);
			RegionScreenManager.popup.get(sp).getWidget(sh.page5Widgets[8].getId()).setDirty(true);
		} else {
			RegionScreenManager.popup.get(sp).attachWidget(RegiosPlugin.regios, sh.page5Widgets[8]);
		}

		((GenericButton) sh.page5Widgets[9]).setX(215);
		((GenericButton) sh.page5Widgets[9]).setY(95);
		((GenericButton) sh.page5Widgets[9]).setWidth(35);
		((GenericButton) sh.page5Widgets[9]).setHeight(20);
		((GenericButton) sh.page5Widgets[9]).setTextColor(RGB.YELLOW.getColour());
		((GenericButton) sh.page5Widgets[9]).setText("<");

		if (RegionScreenManager.popup.get(sp).containsWidget(sh.page5Widgets[9])) {
			RegionScreenManager.popup.get(sp).getWidget(sh.page5Widgets[9].getId()).setVisible(true);
			RegionScreenManager.popup.get(sp).getWidget(sh.page5Widgets[9].getId()).setDirty(true);
		} else {
			RegionScreenManager.popup.get(sp).attachWidget(RegiosPlugin.regios, sh.page5Widgets[9]);
		}

		((GenericButton) sh.page5Widgets[10]).setX(345);
		((GenericButton) sh.page5Widgets[10]).setY(95);
		((GenericButton) sh.page5Widgets[10]).setWidth(35);
		((GenericButton) sh.page5Widgets[10]).setHeight(20);
		((GenericButton) sh.page5Widgets[10]).setTextColor(RGB.YELLOW.getColour());
		((GenericButton) sh.page5Widgets[10]).setText(">");

		if (RegionScreenManager.popup.get(sp).containsWidget(sh.page5Widgets[10])) {
			RegionScreenManager.popup.get(sp).getWidget(sh.page5Widgets[10].getId()).setVisible(true);
			RegionScreenManager.popup.get(sp).getWidget(sh.page5Widgets[10].getId()).setDirty(true);
		} else {
			RegionScreenManager.popup.get(sp).attachWidget(RegiosPlugin.regios, sh.page5Widgets[10]);
		}

		((GenericContainer) sh.page5Widgets[11]).setX(215);
		((GenericContainer) sh.page5Widgets[11]).setY(120);
		((GenericContainer) sh.page5Widgets[11]).setWidth(100);
		((GenericContainer) sh.page5Widgets[11]).setHeight(85);

		if (RegionScreenManager.popup.get(sp).containsWidget(sh.page5Widgets[11])) {
			for (Widget w : ((Container) RegionScreenManager.popup.get(sp).getWidget(sh.page5Widgets[11].getId())).getChildren()) {
				((Container) RegionScreenManager.popup.get(sp).getWidget(sh.page5Widgets[11].getId())).removeChild(w);
			}
		}

		for (int index = 0; index <= 4; index++) {
			if ((index < (r.getTempNodesCacheAdd()).length) && r.getTempNodesCacheAdd()[index].length() > 2) {
				GenericLabel ex = new GenericLabel((r.getTempNodesCacheAdd())[index]);
				ex.setTextColor(RGB.YELLOW.getColour());
				((GenericContainer) sh.page5Widgets[11]).addChild(ex);
			} else {
				GenericLabel ex = new GenericLabel("-");
				ex.setTextColor(RGB.YELLOW.getColour());
				((GenericContainer) sh.page5Widgets[11]).addChild(ex);
			}
		}

		if (RegionScreenManager.popup.get(sp).containsWidget(sh.page5Widgets[11])) {
			RegionScreenManager.popup.get(sp).getWidget(sh.page5Widgets[11].getId()).setVisible(true);
			RegionScreenManager.popup.get(sp).getWidget(sh.page5Widgets[11].getId()).setDirty(true);

		} else {
			RegionScreenManager.popup.get(sp).attachWidget(RegiosPlugin.regios, sh.page5Widgets[11]);
		}
		
		updateExceptionPages(sp, 1, sh, r);

	}

}
