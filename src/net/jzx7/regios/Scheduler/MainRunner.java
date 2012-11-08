package net.jzx7.regios.Scheduler;

import net.jzx7.regios.RegiosPlugin;

import org.bukkit.Bukkit;


public class MainRunner {
	
	public static int taskid = 0;
	
	public static final void startMainRunner(){
		if(taskid == 0){
			mainRunner();
		}
	}
	
	public static final void stopMainRunner(){
		Bukkit.getServer().getScheduler().cancelTask(taskid);
	}
	
	private static final void mainRunner(){
		taskid = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(RegiosPlugin.regios, new Runnable() {	

			@Override
			public void run() {
				HealthRegeneration.loopRegenerators();
				LightningRunner.executeStrikes();
				LogRunner.pollLogMessages();
			}

		}, 20L, 20L); //Run every second 
	}

}
