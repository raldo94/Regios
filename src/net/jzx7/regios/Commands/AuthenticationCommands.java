package net.jzx7.regios.Commands;

import net.jzx7.regios.Data.ConfigurationData;
import net.jzx7.regios.entity.PlayerManager;
import net.jzx7.regios.messages.Message;
import net.jzx7.regiosapi.entity.RegiosPlayer;
import net.jzx7.regiosapi.regions.Region;

public class AuthenticationCommands {
	
	private static final PlayerManager pm = new PlayerManager();
	
	public void auth(RegiosPlayer p, String[] args) {
		if (args.length == 2) {
			sendPassword(p, args[1], pm.getRegionBinding().get(p.getName()));
			return;
		} else {
			p.sendMessage(Message.INVALIDARGUMENTCOUNT.getMessage());
			p.sendMessage(Message.PROPERUSAGE.getMessage() + "/regios auth <password>");
			return;
		}
	}
	
	private void sendPassword(RegiosPlayer p, String password, Region r){
		if(r == null){
			p.sendMessage("<RED>" + "[Regios] You must first try to enter a region before authorising yourself!");
			return;
		}
		if(r.getAuthentication(password, p)){
			p.sendMessage(ConfigurationData.defaultAuthenticationSuccessMessage);
			return;
		} else {
			p.sendMessage("<RED>" + "[Regios] Invalid password for region " + "<BLUE>" + r.getName());
			return;
		}
		
	}

}
