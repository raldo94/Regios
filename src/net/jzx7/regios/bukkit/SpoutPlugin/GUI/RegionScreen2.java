package net.jzx7.regios.bukkit.SpoutPlugin.GUI;

import net.jzx7.regios.RegiosPlugin;
import net.jzx7.regios.bukkit.SpoutPlugin.GUI.RegionScreenManager.RGB;
import net.jzx7.regiosapi.regions.Region;

import org.bukkit.ChatColor;
import org.getspout.spoutapi.gui.GenericButton;
import org.getspout.spoutapi.gui.GenericLabel;
import org.getspout.spoutapi.gui.GenericTextField;
import org.getspout.spoutapi.gui.InGameHUD;
import org.getspout.spoutapi.gui.RenderPriority;
import org.getspout.spoutapi.gui.Widget;
import org.getspout.spoutapi.player.SpoutPlayer;


public class RegionScreen2 {

	public static void loadScreen(SpoutPlayer sp, Region r, Object[] oldWidgets, ScreenHolder sh) {
		InGameHUD hud = sp.getMainScreen();
		
		for(Widget widg : sh.page3Widgets){
			widg.setPriority(RenderPriority.Highest);
		}
		
		for(Widget widg : sh.page2Widgets){
			widg.setPriority(RenderPriority.Lowest);
		}
		
		for(Widget widg : sh.page1Widgets){
			widg.setPriority(RenderPriority.Highest);
		}

		if (oldWidgets != null) {
			for (Object w : oldWidgets) {
				//if (w instanceof Widget) {
					((Widget) w).setVisible(false);
					((Widget) w).setDirty(true);
				//}
			}
		}
		
		for(Widget w : sh.page1Widgets){
			w.setPriority(RenderPriority.Lowest);
		}

		((GenericTextField) sh.page2Widgets[0]).setX(15);
		((GenericTextField) sh.page2Widgets[0]).setY(55);
		((GenericTextField) sh.page2Widgets[0]).setFieldColor(RGB.BLACK.getColour());
		((GenericTextField) sh.page2Widgets[0]).setBorderColor(RGB.SPRING_GREEN.getColour());
		((GenericTextField) sh.page2Widgets[0]).setWidth(400);
		((GenericTextField) sh.page2Widgets[0]).setHeight(15);
		((GenericTextField) sh.page2Widgets[0]).setMaximumCharacters(70);
		((GenericTextField) sh.page2Widgets[0]).setText(r.getWelcomeMessage());
		
		((GenericLabel) sh.page2Widgets[5]).setText("Welcome Message");
		((GenericLabel) sh.page2Widgets[5]).setX(15);
		((GenericLabel) sh.page2Widgets[5]).setY(44);
		((GenericLabel) sh.page2Widgets[5]).setTextColor(RGB.YELLOW.getColour());
		((GenericLabel) sh.page2Widgets[5]).setTooltip(ChatColor.YELLOW + "  Message shown upon entering a region.");

		if (RegionScreenManager.popup.get(sp).containsWidget(sh.page2Widgets[0])) {
			RegionScreenManager.popup.get(sp).getWidget(sh.page2Widgets[0].getId()).setVisible(true);
			RegionScreenManager.popup.get(sp).getWidget(sh.page2Widgets[0].getId()).setDirty(true);
		} else {
			RegionScreenManager.popup.get(sp).attachWidget(RegiosPlugin.regios, sh.page2Widgets[0]);
		}
		
		((GenericButton) sh.page2Widgets[11]).setText("Reset");
		((GenericButton) sh.page2Widgets[11]).setWidth(40);
		((GenericButton) sh.page2Widgets[11]).setHeight(15);
		((GenericButton) sh.page2Widgets[11]).setX(376);
		((GenericButton) sh.page2Widgets[11]).setY(37);
		((GenericButton) sh.page2Widgets[11]).setTextColor(RGB.WHITE.getColour());
		((GenericButton) sh.page2Widgets[11]).setHoverColor(RGB.SPRING_GREEN.getColour());
		
		if (RegionScreenManager.popup.get(sp).containsWidget(sh.page2Widgets[11])) {
			RegionScreenManager.popup.get(sp).getWidget(sh.page2Widgets[11].getId()).setVisible(true);
			RegionScreenManager.popup.get(sp).getWidget(sh.page2Widgets[11].getId()).setDirty(true);
		} else {
			RegionScreenManager.popup.get(sp).attachWidget(RegiosPlugin.regios, sh.page2Widgets[11]);
		}
		
		if (RegionScreenManager.popup.get(sp).containsWidget(sh.page2Widgets[5])) {
			RegionScreenManager.popup.get(sp).getWidget(sh.page2Widgets[5].getId()).setVisible(true);
			RegionScreenManager.popup.get(sp).getWidget(sh.page2Widgets[5].getId()).setDirty(true);
		} else {
			RegionScreenManager.popup.get(sp).attachWidget(RegiosPlugin.regios, sh.page2Widgets[5]);
		}
		
		((GenericTextField) sh.page2Widgets[1]).setX(15);
		((GenericTextField) sh.page2Widgets[1]).setY(90);
		((GenericTextField) sh.page2Widgets[1]).setFieldColor(RGB.BLACK.getColour());
		((GenericTextField) sh.page2Widgets[1]).setBorderColor(RGB.SPRING_GREEN.getColour());
		((GenericTextField) sh.page2Widgets[1]).setWidth(400);
		((GenericTextField) sh.page2Widgets[1]).setHeight(15);
		((GenericTextField) sh.page2Widgets[1]).setMaximumCharacters(70);
		((GenericTextField) sh.page2Widgets[1]).setText(r.getLeaveMessage());
		
		((GenericLabel) sh.page2Widgets[6]).setText("Leave Message");
		((GenericLabel) sh.page2Widgets[6]).setX(15);
		((GenericLabel) sh.page2Widgets[6]).setY(79);
		((GenericLabel) sh.page2Widgets[6]).setTextColor(RGB.YELLOW.getColour());
		((GenericLabel) sh.page2Widgets[6]).setTooltip(ChatColor.YELLOW + "  Message shown upon leaving a region.");
		
		if (RegionScreenManager.popup.get(sp).containsWidget(sh.page2Widgets[6])) {
			RegionScreenManager.popup.get(sp).getWidget(sh.page2Widgets[6].getId()).setVisible(true);
			RegionScreenManager.popup.get(sp).getWidget(sh.page2Widgets[6].getId()).setDirty(true);
		} else {
			RegionScreenManager.popup.get(sp).attachWidget(RegiosPlugin.regios, sh.page2Widgets[6]);
		}

		if (RegionScreenManager.popup.get(sp).containsWidget(sh.page2Widgets[1])) {
			RegionScreenManager.popup.get(sp).getWidget(sh.page2Widgets[1].getId()).setVisible(true);
			RegionScreenManager.popup.get(sp).getWidget(sh.page2Widgets[1].getId()).setDirty(true);
		} else {
			RegionScreenManager.popup.get(sp).attachWidget(RegiosPlugin.regios, sh.page2Widgets[1]);
		}
		
		((GenericButton) sh.page2Widgets[12]).setText("Reset");
		((GenericButton) sh.page2Widgets[12]).setWidth(40);
		((GenericButton) sh.page2Widgets[12]).setHeight(15);
		((GenericButton) sh.page2Widgets[12]).setX(376);
		((GenericButton) sh.page2Widgets[12]).setY(72);
		((GenericButton) sh.page2Widgets[12]).setTextColor(RGB.WHITE.getColour());
		((GenericButton) sh.page2Widgets[12]).setHoverColor(RGB.SPRING_GREEN.getColour());
		
		if (RegionScreenManager.popup.get(sp).containsWidget(sh.page2Widgets[12])) {
			RegionScreenManager.popup.get(sp).getWidget(sh.page2Widgets[12].getId()).setVisible(true);
			RegionScreenManager.popup.get(sp).getWidget(sh.page2Widgets[12].getId()).setDirty(true);
		} else {
			RegionScreenManager.popup.get(sp).attachWidget(RegiosPlugin.regios, sh.page2Widgets[12]);
		}
		
		((GenericTextField) sh.page2Widgets[2]).setX(15);
		((GenericTextField) sh.page2Widgets[2]).setY(125);
		((GenericTextField) sh.page2Widgets[2]).setFieldColor(RGB.BLACK.getColour());
		((GenericTextField) sh.page2Widgets[2]).setBorderColor(RGB.SPRING_GREEN.getColour());
		((GenericTextField) sh.page2Widgets[2]).setWidth(400);
		((GenericTextField) sh.page2Widgets[2]).setHeight(15);
		((GenericTextField) sh.page2Widgets[2]).setMaximumCharacters(70);
		((GenericTextField) sh.page2Widgets[2]).setText(r.getPreventEntryMessage());
		
		((GenericLabel) sh.page2Widgets[7]).setText("Prevent Entry Message");
		((GenericLabel) sh.page2Widgets[7]).setX(15);
		((GenericLabel) sh.page2Widgets[7]).setY(114);
		((GenericLabel) sh.page2Widgets[7]).setTextColor(RGB.YELLOW.getColour());
		((GenericLabel) sh.page2Widgets[7]).setTooltip(ChatColor.YELLOW + "  Message shown upon prevention from entering a region.");
		
		if (RegionScreenManager.popup.get(sp).containsWidget(sh.page2Widgets[7])) {
			RegionScreenManager.popup.get(sp).getWidget(sh.page2Widgets[7].getId()).setVisible(true);
			RegionScreenManager.popup.get(sp).getWidget(sh.page2Widgets[7].getId()).setDirty(true);
		} else {
			RegionScreenManager.popup.get(sp).attachWidget(RegiosPlugin.regios, sh.page2Widgets[7]);
		}


		if (RegionScreenManager.popup.get(sp).containsWidget(sh.page2Widgets[2])) {
			RegionScreenManager.popup.get(sp).getWidget(sh.page2Widgets[2].getId()).setVisible(true);
			RegionScreenManager.popup.get(sp).getWidget(sh.page2Widgets[2].getId()).setDirty(true);
		} else {
			RegionScreenManager.popup.get(sp).attachWidget(RegiosPlugin.regios, sh.page2Widgets[2]);
		}
		
		((GenericButton) sh.page2Widgets[13]).setText("Reset");
		((GenericButton) sh.page2Widgets[13]).setWidth(40);
		((GenericButton) sh.page2Widgets[13]).setHeight(15);
		((GenericButton) sh.page2Widgets[13]).setX(376);
		((GenericButton) sh.page2Widgets[13]).setY(107);
		((GenericButton) sh.page2Widgets[13]).setTextColor(RGB.WHITE.getColour());
		((GenericButton) sh.page2Widgets[13]).setHoverColor(RGB.SPRING_GREEN.getColour());
		
		if (RegionScreenManager.popup.get(sp).containsWidget(sh.page2Widgets[13])) {
			RegionScreenManager.popup.get(sp).getWidget(sh.page2Widgets[13].getId()).setVisible(true);
			RegionScreenManager.popup.get(sp).getWidget(sh.page2Widgets[13].getId()).setDirty(true);
		} else {
			RegionScreenManager.popup.get(sp).attachWidget(RegiosPlugin.regios, sh.page2Widgets[13]);
		}
		
		((GenericTextField) sh.page2Widgets[3]).setX(15);
		((GenericTextField) sh.page2Widgets[3]).setY(160);
		((GenericTextField) sh.page2Widgets[3]).setFieldColor(RGB.BLACK.getColour());
		((GenericTextField) sh.page2Widgets[3]).setBorderColor(RGB.SPRING_GREEN.getColour());
		((GenericTextField) sh.page2Widgets[3]).setWidth(400);
		((GenericTextField) sh.page2Widgets[3]).setHeight(15);
		((GenericTextField) sh.page2Widgets[3]).setMaximumCharacters(70);
		((GenericTextField) sh.page2Widgets[3]).setText(r.getPreventExitMessage());
		
		((GenericLabel) sh.page2Widgets[8]).setText("Prevent Exit Message");
		((GenericLabel) sh.page2Widgets[8]).setX(15);
		((GenericLabel) sh.page2Widgets[8]).setY(149);
		((GenericLabel) sh.page2Widgets[8]).setTextColor(RGB.YELLOW.getColour());
		((GenericLabel) sh.page2Widgets[8]).setTooltip(ChatColor.YELLOW + "  Message shown upon prevention from leaving a region.");
		
		if (RegionScreenManager.popup.get(sp).containsWidget(sh.page2Widgets[8])) {
			RegionScreenManager.popup.get(sp).getWidget(sh.page2Widgets[8].getId()).setVisible(true);
			RegionScreenManager.popup.get(sp).getWidget(sh.page2Widgets[8].getId()).setDirty(true);
		} else {
			RegionScreenManager.popup.get(sp).attachWidget(RegiosPlugin.regios, sh.page2Widgets[8]);
		}


		if (RegionScreenManager.popup.get(sp).containsWidget(sh.page2Widgets[3])) {
			RegionScreenManager.popup.get(sp).getWidget(sh.page2Widgets[3].getId()).setVisible(true);
			RegionScreenManager.popup.get(sp).getWidget(sh.page2Widgets[3].getId()).setDirty(true);
		} else {
			RegionScreenManager.popup.get(sp).attachWidget(RegiosPlugin.regios, sh.page2Widgets[3]);
		}
		
		((GenericButton) sh.page2Widgets[14]).setText("Reset");
		((GenericButton) sh.page2Widgets[14]).setWidth(40);
		((GenericButton) sh.page2Widgets[14]).setHeight(15);
		((GenericButton) sh.page2Widgets[14]).setX(376);
		((GenericButton) sh.page2Widgets[14]).setY(142);
		((GenericButton) sh.page2Widgets[14]).setTextColor(RGB.WHITE.getColour());
		((GenericButton) sh.page2Widgets[14]).setHoverColor(RGB.SPRING_GREEN.getColour());
		
		if (RegionScreenManager.popup.get(sp).containsWidget(sh.page2Widgets[14])) {
			RegionScreenManager.popup.get(sp).getWidget(sh.page2Widgets[14].getId()).setVisible(true);
			RegionScreenManager.popup.get(sp).getWidget(sh.page2Widgets[14].getId()).setDirty(true);
		} else {
			RegionScreenManager.popup.get(sp).attachWidget(RegiosPlugin.regios, sh.page2Widgets[14]);
		}
		
		((GenericTextField) sh.page2Widgets[4]).setX(15);
		((GenericTextField) sh.page2Widgets[4]).setY(195);
		((GenericTextField) sh.page2Widgets[4]).setFieldColor(RGB.BLACK.getColour());
		((GenericTextField) sh.page2Widgets[4]).setBorderColor(RGB.SPRING_GREEN.getColour());
		((GenericTextField) sh.page2Widgets[4]).setWidth(400);
		((GenericTextField) sh.page2Widgets[4]).setHeight(15);
		((GenericTextField) sh.page2Widgets[4]).setMaximumCharacters(70);
		((GenericTextField) sh.page2Widgets[4]).setText(r.getProtectionMessage());
		
		((GenericLabel) sh.page2Widgets[9]).setText("Protection Message");
		((GenericLabel) sh.page2Widgets[9]).setX(15);
		((GenericLabel) sh.page2Widgets[9]).setY(184);
		((GenericLabel) sh.page2Widgets[9]).setTextColor(RGB.YELLOW.getColour());
		((GenericLabel) sh.page2Widgets[9]).setTooltip(ChatColor.YELLOW + "  Message shown upon building in a protected region.");
		
		if (RegionScreenManager.popup.get(sp).containsWidget(sh.page2Widgets[9])) {
			RegionScreenManager.popup.get(sp).getWidget(sh.page2Widgets[9].getId()).setVisible(true);
			RegionScreenManager.popup.get(sp).getWidget(sh.page2Widgets[9].getId()).setDirty(true);
		} else {
			RegionScreenManager.popup.get(sp).attachWidget(RegiosPlugin.regios, sh.page2Widgets[9]);
		}


		if (RegionScreenManager.popup.get(sp).containsWidget(sh.page2Widgets[4])) {
			RegionScreenManager.popup.get(sp).getWidget(sh.page2Widgets[4].getId()).setVisible(true);
			RegionScreenManager.popup.get(sp).getWidget(sh.page2Widgets[4].getId()).setDirty(true);
		} else {
			RegionScreenManager.popup.get(sp).attachWidget(RegiosPlugin.regios, sh.page2Widgets[4]);
		}
		
		((GenericButton) sh.page2Widgets[15]).setText("Reset");
		((GenericButton) sh.page2Widgets[15]).setWidth(40);
		((GenericButton) sh.page2Widgets[15]).setHeight(15);
		((GenericButton) sh.page2Widgets[15]).setX(376);
		((GenericButton) sh.page2Widgets[15]).setY(177);
		((GenericButton) sh.page2Widgets[15]).setTextColor(RGB.WHITE.getColour());
		((GenericButton) sh.page2Widgets[15]).setHoverColor(RGB.SPRING_GREEN.getColour());
		
		if (RegionScreenManager.popup.get(sp).containsWidget(sh.page2Widgets[15])) {
			RegionScreenManager.popup.get(sp).getWidget(sh.page2Widgets[15].getId()).setVisible(true);
			RegionScreenManager.popup.get(sp).getWidget(sh.page2Widgets[15].getId()).setDirty(true);
		} else {
			RegionScreenManager.popup.get(sp).attachWidget(RegiosPlugin.regios, sh.page2Widgets[15]);
		}
		
		((GenericButton) sh.page2Widgets[10]).setText("Update");
		((GenericButton) sh.page2Widgets[10]).setWidth(50);
		((GenericButton) sh.page2Widgets[10]).setHeight(20);
		((GenericButton) sh.page2Widgets[10]).setX(335);
		((GenericButton) sh.page2Widgets[10]).setY(hud.getHeight() - 24);
		((GenericButton) sh.page2Widgets[10]).setTextColor(RGB.WHITE.getColour());
		((GenericButton) sh.page2Widgets[10]).setHoverColor(RGB.SPRING_GREEN.getColour());
		
		if (RegionScreenManager.popup.get(sp).containsWidget(sh.page2Widgets[10])) {
			RegionScreenManager.popup.get(sp).getWidget(sh.page2Widgets[10].getId()).setVisible(true);
			RegionScreenManager.popup.get(sp).getWidget(sh.page2Widgets[10].getId()).setDirty(true);
		} else {
			RegionScreenManager.popup.get(sp).attachWidget(RegiosPlugin.regios, sh.page2Widgets[10]);
		}
		
		((GenericButton) sh.page2Widgets[16]).setText("Clear");
		((GenericButton) sh.page2Widgets[16]).setWidth(40);
		((GenericButton) sh.page2Widgets[16]).setHeight(15);
		((GenericButton) sh.page2Widgets[16]).setX(333);
		((GenericButton) sh.page2Widgets[16]).setY(37);
		((GenericButton) sh.page2Widgets[16]).setTextColor(RGB.WHITE.getColour());
		((GenericButton) sh.page2Widgets[16]).setHoverColor(RGB.SPRING_GREEN.getColour());
		
		if (RegionScreenManager.popup.get(sp).containsWidget(sh.page2Widgets[16])) {
			RegionScreenManager.popup.get(sp).getWidget(sh.page2Widgets[16].getId()).setVisible(true);
			RegionScreenManager.popup.get(sp).getWidget(sh.page2Widgets[16].getId()).setDirty(true);
		} else {
			RegionScreenManager.popup.get(sp).attachWidget(RegiosPlugin.regios, sh.page2Widgets[16]);
		}
		
		((GenericButton) sh.page2Widgets[17]).setText("Clear");
		((GenericButton) sh.page2Widgets[17]).setWidth(40);
		((GenericButton) sh.page2Widgets[17]).setHeight(15);
		((GenericButton) sh.page2Widgets[17]).setX(333);
		((GenericButton) sh.page2Widgets[17]).setY(72);
		((GenericButton) sh.page2Widgets[17]).setTextColor(RGB.WHITE.getColour());
		((GenericButton) sh.page2Widgets[17]).setHoverColor(RGB.SPRING_GREEN.getColour());
		
		if (RegionScreenManager.popup.get(sp).containsWidget(sh.page2Widgets[17])) {
			RegionScreenManager.popup.get(sp).getWidget(sh.page2Widgets[17].getId()).setVisible(true);
			RegionScreenManager.popup.get(sp).getWidget(sh.page2Widgets[17].getId()).setDirty(true);
		} else {
			RegionScreenManager.popup.get(sp).attachWidget(RegiosPlugin.regios, sh.page2Widgets[17]);
		}
		
		((GenericButton) sh.page2Widgets[18]).setText("Clear");
		((GenericButton) sh.page2Widgets[18]).setWidth(40);
		((GenericButton) sh.page2Widgets[18]).setHeight(15);
		((GenericButton) sh.page2Widgets[18]).setX(333);
		((GenericButton) sh.page2Widgets[18]).setY(107);
		((GenericButton) sh.page2Widgets[18]).setTextColor(RGB.WHITE.getColour());
		((GenericButton) sh.page2Widgets[18]).setHoverColor(RGB.SPRING_GREEN.getColour());
		
		if (RegionScreenManager.popup.get(sp).containsWidget(sh.page2Widgets[18])) {
			RegionScreenManager.popup.get(sp).getWidget(sh.page2Widgets[18].getId()).setVisible(true);
			RegionScreenManager.popup.get(sp).getWidget(sh.page2Widgets[18].getId()).setDirty(true);
		} else {
			RegionScreenManager.popup.get(sp).attachWidget(RegiosPlugin.regios, sh.page2Widgets[18]);
		}
		
		((GenericButton) sh.page2Widgets[19]).setText("Clear");
		((GenericButton) sh.page2Widgets[19]).setWidth(40);
		((GenericButton) sh.page2Widgets[19]).setHeight(15);
		((GenericButton) sh.page2Widgets[19]).setX(333);
		((GenericButton) sh.page2Widgets[19]).setY(142);
		((GenericButton) sh.page2Widgets[19]).setTextColor(RGB.WHITE.getColour());
		((GenericButton) sh.page2Widgets[19]).setHoverColor(RGB.SPRING_GREEN.getColour());
		
		if (RegionScreenManager.popup.get(sp).containsWidget(sh.page2Widgets[19])) {
			RegionScreenManager.popup.get(sp).getWidget(sh.page2Widgets[19].getId()).setVisible(true);
			RegionScreenManager.popup.get(sp).getWidget(sh.page2Widgets[19].getId()).setDirty(true);
		} else {
			RegionScreenManager.popup.get(sp).attachWidget(RegiosPlugin.regios, sh.page2Widgets[19]);
		}
		
		((GenericButton) sh.page2Widgets[20]).setText("Clear");
		((GenericButton) sh.page2Widgets[20]).setWidth(40);
		((GenericButton) sh.page2Widgets[20]).setHeight(15);
		((GenericButton) sh.page2Widgets[20]).setX(333);
		((GenericButton) sh.page2Widgets[20]).setY(177);
		((GenericButton) sh.page2Widgets[20]).setTextColor(RGB.WHITE.getColour());
		((GenericButton) sh.page2Widgets[20]).setHoverColor(RGB.SPRING_GREEN.getColour());
		
		if (RegionScreenManager.popup.get(sp).containsWidget(sh.page2Widgets[20])) {
			RegionScreenManager.popup.get(sp).getWidget(sh.page2Widgets[20].getId()).setVisible(true);
			RegionScreenManager.popup.get(sp).getWidget(sh.page2Widgets[20].getId()).setDirty(true);
		} else {
			RegionScreenManager.popup.get(sp).attachWidget(RegiosPlugin.regios, sh.page2Widgets[20]);
		}
		
	}

}
