package couk.Adamki11s.Regios.CustomEvents;

import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class RegionCommandEvent extends Event {

	private CommandSender sender;
	private String label;
	private String[] args;
	private static final HandlerList handlers = new HandlerList();

	public RegionCommandEvent(String name) {
	}

	public CommandSender getSender(){
		return this.sender;
	}
	
	public String getLabel(){
		return this.label;
	}
	
	public String[] getArgs(){
		return this.args;
	}

	public void setProperties(CommandSender sender, String label, String[] args) {
		this.sender = sender;
		this.label = label;
		this.args = args;
	}

    public HandlerList getHandlers() {
        return handlers;
    }
     
    public static HandlerList getHandlerList() {
        return handlers;
    }

}
