package io.github.gronnmann.coinflipper;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.gronnmann.coinflipper.animations.AnimationFileManager;
import io.github.gronnmann.coinflipper.animations.AnimationGUI;
import io.github.gronnmann.coinflipper.animations.AnimationsManager;
import io.github.gronnmann.coinflipper.bets.BettingManager;
import io.github.gronnmann.coinflipper.bets.BettingTimer;
import io.github.gronnmann.coinflipper.gui.CreationGUI;
import io.github.gronnmann.coinflipper.gui.SelectionScreen;
import io.github.gronnmann.coinflipper.gui.configurationeditor.FileEditSelector;
import io.github.gronnmann.coinflipper.hook.HookManager;
import io.github.gronnmann.coinflipper.metrics.BStats;
import io.github.gronnmann.coinflipper.mysql.SQLManager;
import io.github.gronnmann.coinflipper.stats.StatsManager;
import io.github.gronnmann.utils.coinflipper.Debug;
import io.github.gronnmann.utils.coinflipper.GeneralUtils;
import io.github.gronnmann.utils.coinflipper.ItemUtils;
import io.github.gronnmann.utils.coinflipper.VersionUtils;
import io.github.gronnmann.utils.coinflipper.input.InputManager;
import io.github.gronnmann.utils.pagedinventory.coinflipper.PagedInventoryManager;
import net.milkbowl.vault.economy.Economy;


public class CoinFlipper extends JavaPlugin{
	
	private static Economy economy;
	
	private static CoinFlipper main;
	
	public static int versionId;
	
	
	
	public void onEnable(){
		
		main = this;
		
		enableEconomy();
		
		versionId = GeneralUtils.getMinecraftVersion();
		Debug.print("Minecraft version detected: " + versionId);
		
		ConfigManager.getManager().setup();
		
		SQLManager.getManager().setup();
				
		SelectionScreen.getInstance().setup(this);
		FileEditSelector.getInstance().setup();
		StatsManager.getManager().load();
		
		AnimationFileManager.getManager().setup(this);
		AnimationGUI.getManager().setup();
		
		BettingManager.getManager().load();
		
		HookManager.getManager().registerHooks(this);
		
		CreationGUI.getInstance().generatePreset();
		
		this.getCommand("coinflipper").setExecutor(new CommandsManager());
		
		
		BettingTimer task = new BettingTimer();
		task.runTaskTimerAsynchronously(this, 0, 60*20);
		
		
		Bukkit.getPluginManager().registerEvents(new PagedInventoryManager(), this);
		
		
		Bukkit.getPluginManager().registerEvents(SelectionScreen.getInstance(), this);
		Bukkit.getPluginManager().registerEvents(FileEditSelector.getInstance(), this);
		Bukkit.getPluginManager().registerEvents(CreationGUI.getInstance(), this);
		
		Bukkit.getPluginManager().registerEvents(StatsManager.getManager(), this);
		
		
		Bukkit.getPluginManager().registerEvents(AnimationGUI.getManager(), this);
		
		InputManager.setupListeners();
		
		//if (versionId < 14) {
			int versionResponse = VersionUtils.versionFromGithub(this, "https://raw.githubusercontent.com/gronnmann/CoinFlipper/master/CoinFlipper/src/plugin.yml");
			
			if (versionResponse == VersionUtils.VERSION_NEWER){
				System.out.println("[CoinFlipper] You are running an experimental version of CoinFlipper.");
				System.out.println("[CoinFlipper] Expect bugs");
			}else if (versionResponse == VersionUtils.VERSION_OLDER){
				System.out.println("[CoinFlipper] You are using an old CoinFlipper version. Please update for best stability at:");
				System.out.println("[CoinFlipper] https://www.spigotmc.org/resources/coinflipper.33916/");
			}
		//}else {
			//System.out.print("[CoinFlipper] Could not check version of CoinFlipper. Version checking is not supported at 1.14+.");
		//}
		
		
		
		
		//Start metrics
		new BStats(this);
		
	}
	
	public void onDisable(){
		StatsManager.getManager().save();
		BettingManager.getManager().save();
		
		AnimationsManager.getManager().save();
		
		HookManager.getManager().onDisable();
		
		try{
			SQLManager.getManager().getSQLConnection().close();
		}catch(Exception e){e.printStackTrace();}
	}
	
	public boolean enableEconomy(){
		RegisteredServiceProvider<Economy> rsp = this.getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null){
			return false;
		}
		economy = rsp.getProvider();
		return economy != null;
	}
	
	
	public static Economy getEcomony(){
		return economy;
	}
	
	public static CoinFlipper getMain(){
		return main;
	}
}
