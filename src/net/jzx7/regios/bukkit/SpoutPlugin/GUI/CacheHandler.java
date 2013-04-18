package net.jzx7.regios.bukkit.SpoutPlugin.GUI;

import net.jzx7.regios.RegiosPlugin;

import org.getspout.spoutapi.SpoutManager;


public class CacheHandler {
	
	public static void cacheObjects(){
		cache("http://dl.dropbox.com/u/27260323/Regios/GUI/Help%20GUI%20Texture.png");
		cache("http://dl.dropbox.com/u/27260323/Regios/GUI/Editor%20GUI%20Texture.png");
	}
	
	private static void cache(String url){
		SpoutManager.getFileManager().addToPreLoginCache(RegiosPlugin.regios, url);
	}

}
