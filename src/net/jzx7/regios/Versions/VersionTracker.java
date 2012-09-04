package net.jzx7.regios.Versions;

import java.io.File;
import java.io.IOException;

import net.jzx7.regios.RegiosPlugin;


public class VersionTracker {

	static final File vtCore = new File("plugins" + File.separator + "Regios" + File.separator + "Versions" + File.separator + "Version Tracker");

	public static void createCurrentTracker() {
		try {
			VersionPatcher.runPatch(((RegiosPlugin) RegiosPlugin.regios).getRegiosVersion());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
