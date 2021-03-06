package io.github.gronnmann.coinflipper.gui.configurationeditor;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.Plugin;

import io.github.gronnmann.coinflipper.CoinFlipper;
import io.github.gronnmann.coinflipper.customizable.Message;
import io.github.gronnmann.coinflipper.gui.configurationeditor.config.ConfigEditor;
import io.github.gronnmann.coinflipper.gui.configurationeditor.materials.MaterialChooser;
import io.github.gronnmann.coinflipper.gui.configurationeditor.materials.MaterialEditor;
import io.github.gronnmann.coinflipper.gui.configurationeditor.messages.MessageEditor;
import io.github.gronnmann.utils.coinflipper.ItemUtils;

public class FileEditSelector implements Listener{
	private FileEditSelector(){}
	private static FileEditSelector instance = new FileEditSelector();
	public static FileEditSelector getInstance(){
		return instance;
	}
	
	private Plugin pl;
	public Inventory selectionScreen;
	
	int CONFIG = 4, MATERIALS = 6, MESSAGES = 2;
		
	public void setup(){
		this.pl = CoinFlipper.getMain();
		selectionScreen = Bukkit.createInventory(new FileEditSelectorHolder(), 9, Message.GUI_CONFIGURATOR.getMessage());
		
		selectionScreen.setItem(MESSAGES, ItemUtils.createItem(Material.PAPER, ChatColor.GOLD + "messages.yml"));
		
		selectionScreen.setItem(CONFIG, ItemUtils.createItem(Material.INK_SACK, ChatColor.GOLD + "config.yml",10));
		
		selectionScreen.setItem(MATERIALS, ItemUtils.createItem(Material.GRASS, ChatColor.GOLD + "materials.yml"));
		
		ConfigEditor.getInstance().setup();
		Bukkit.getPluginManager().registerEvents(ConfigEditor.getInstance(), pl);
		
		
		MaterialEditor.getInstance().setup(pl);
		Bukkit.getPluginManager().registerEvents(MaterialEditor.getInstance(), pl);
		
		MaterialChooser.getInstance().setup();
		Bukkit.getPluginManager().registerEvents(MaterialChooser.getInstance(), pl);
		
		
		MessageEditor.getInstance().setup();
		Bukkit.getPluginManager().registerEvents(MessageEditor.getInstance(), pl);
		
		
		
	}
	
	public void openConfigurator(Player p){
		Bukkit.getScheduler().runTask(pl, ()->{			
			p.openInventory(selectionScreen);
		});
	}
	
	@EventHandler
	public void yamlRedirector(InventoryClickEvent e){
		if (e.getClickedInventory() == null)return;
		if (!(e.getClickedInventory().getHolder() instanceof FileEditSelectorHolder))return;
		e.setCancelled(true);
		
		if (e.getSlot() == CONFIG){
			ConfigEditor.getInstance().openEditor((Player) e.getWhoClicked());
		}
		else if (e.getSlot() == MATERIALS){
			MaterialEditor.getInstance().openEditor((Player)e.getWhoClicked());
		}else if (e.getSlot() == MESSAGES){
			MessageEditor.getInstance().openEditor((Player)e.getWhoClicked());
		}
		
	}
	
	
	
}

class FileEditSelectorHolder implements InventoryHolder{

	@Override
	public Inventory getInventory() {
		return null;
	}
	
}
