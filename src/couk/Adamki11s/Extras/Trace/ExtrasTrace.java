package couk.Adamki11s.Extras.Trace;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.bukkit.Server;
import org.bukkit.entity.Player;

import couk.Adamki11s.Extras.Extras.Extras;

public class ExtrasTrace extends TraceMethods {

	private long startTrace = 0, endTrace = 0;

	@Override
	public void startTrace() {
		startTrace = System.nanoTime();
	}

	@Override
	public void stopTrace() {
		endTrace = System.nanoTime() - startTrace;
	}

	@Override
	public long getTrace() {
		return (endTrace / 1000);
	}

	@Override
	public void logTraceTime() {
		System.out.println("[Extras] Trace time : " + getTrace() + "ms");
	}

	@Override
	public void logTraceTime(File file, String description){
		try{
			FileWriter fstream = new FileWriter(file, true);
	        BufferedWriter bw = new BufferedWriter(fstream);
	        bw.write("[Extras] Trace : " + description + " : " + getTrace() + "ms.");
	        bw.newLine();
	        bw.close();
		} catch (IOException ex){
			System.out.println("[Extras] Could not print trace to file! Caused by plugin : " + Extras.pluginName);
			ex.printStackTrace();
		}
	}

	@Override
	public void broadcastTraceTime(Server s) {
		s.broadcastMessage("Trace time : " + getTrace());
	}

	@Override
	public void sendTraceTime(Player p) {
		p.sendMessage("Trace time : " + getTrace());
		
	}

	@Override
	public void broadcastTraceTimeCustom(Server s, String prefix) {
		s.broadcastMessage(prefix + getTrace());
	}

	@Override
	public void sendTraceTimeCustom(Player p, String prefix) {
		p.sendMessage(prefix + getTrace());
	}

}
