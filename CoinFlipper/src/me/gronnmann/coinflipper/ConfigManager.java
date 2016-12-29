package me.gronnmann.coinflipper;

import java.io.File;
import java.io.InputStream;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class ConfigManager {
	private ConfigManager(){}
	private static ConfigManager mng = new ConfigManager();
	public static ConfigManager getManager(){return mng;}
	
	private Plugin pl;
	private File configF, messagesF;
	private FileConfiguration config, messages;
	
	public void setup(Plugin p){
		
		this.pl = p;
		
		//Config
		configF = new File(p.getDataFolder(), "config.yml");
		if (!configF.exists()){
			p.saveDefaultConfig();
		}
		config = YamlConfiguration.loadConfiguration(configF);
		this.saveConfig();
		
		//Messages
		messagesF = new File(p.getDataFolder(), "messages.yml");
		if (!messagesF.exists()){
			try{
				messagesF.createNewFile();
				messages = YamlConfiguration.loadConfiguration(messagesF);
				this.copyDefaults(messages, "/messages.yml");
				this.saveMessages();
			}catch(Exception e){e.printStackTrace();}
		}else{
			messages = YamlConfiguration.loadConfiguration(messagesF);
		}
		
	}
	
	//Copy default option
	public void copyDefaults(FileConfiguration file, String resource){
		InputStream str;
		file.options().copyDefaults(true);
		try{
			str = pl.getClass().getResourceAsStream(resource);
			FileConfiguration defaults = YamlConfiguration.loadConfiguration(str);
			file.setDefaults(defaults);
			str.close();
		}catch(Exception e){e.printStackTrace();}
	}
	
	public FileConfiguration getConfig(){return config;}
	public FileConfiguration getMessages(){return messages;}
	
	public void saveConfig(){
		try{
			config.save(configF);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void saveMessages(){
		try{
			messages.save(messagesF);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}