package com.ascendpvp.factionsarcade;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.ascendpvp.factionsarcade.events.ChatFormat;
import com.ascendpvp.factionsarcade.events.FirstJoin;
import com.ascendpvp.factionsarcade.events.items.CreateFaction;
import com.ascendpvp.factionsarcade.events.items.Schem;
import com.ascendpvp.factionsarcade.utils.Helpers;
import com.ascendpvp.factionsarcade.utils.Inventories;


public class FactionsArcadeMain extends JavaPlugin {

	public Helpers help;
	public Inventories invHelp;
	public Map<String, String> chatMessages;
	public Map<String, ItemStack> items;
	public Map<Integer, ItemStack> menuItems;
	public Map<String, ArrayList<Block>> schematics;
	public Map<String, ArrayList<String>> playersInvited;
	public Location schemLeftClick;
	public Location schemRightClick;
	public File playerDataFile;
	public FileConfiguration playerData;
	public File factionDataFile;
	public FileConfiguration factionData;
	public File schematicsFile;
	public FileConfiguration schems;

	public void onEnable() {
	
		registerEvents();
		getCommand("f").setExecutor(new FactionCommands(this));
		saveDefaultConfig();
		help = new Helpers(this);
		invHelp = new Inventories(this);
		chatMessages = new HashMap<String, String>();
		items = new HashMap<String, ItemStack>();
		menuItems = new HashMap<Integer, ItemStack>();
		schematics = new HashMap<String, ArrayList<Block>>();
		playersInvited = new HashMap<String, ArrayList<String>>();
		schemLeftClick = new Location(null, 0, 0, 0);
		schemRightClick = new Location(null, 0, 0, 0);
		
		playerDataFile = new File(getDataFolder(), "player_data.yml");
		playerData = YamlConfiguration.loadConfiguration(playerDataFile);
		help.savePlayerData();
		factionDataFile = new File(getDataFolder(), "faction_data.yml");
		factionData = YamlConfiguration.loadConfiguration(factionDataFile);
		help.saveFactionData();
		schematicsFile = new File(getDataFolder(), "schematics.yml");
		schems = YamlConfiguration.loadConfiguration(schematicsFile);
		help.saveSchemData();
		
		loadSchems();


		//Load chat messages
		for(String s : getConfig().getConfigurationSection("messages").getKeys(false)) {
			String chatMessage = getConfig().getString("messages." + s)
					.replace("#prefix#", getConfig().getString("plugin_prefix"));
			chatMessages.put(s, help.cc(chatMessage));
		}
		//Load custom given items
		for(String s : getConfig().getConfigurationSection("items").getKeys(false)) {
			String itemName = getConfig().getString("items." + s + ".item_name");
			String itemMaterial = getConfig().getString("items." + s + ".material");
			ItemStack item = help.nameItem(new ItemStack(Material.getMaterial(itemMaterial)), help.cc(itemName));
			items.put(s, item);
		}
		//Load faction menu items
		for(String s : getConfig().getConfigurationSection("factionmenu").getKeys(true)) {
			if(!s.contains(".name")) continue;
			String itemName = getConfig().getString("factionmenu." + s);
			int itemSlot = getConfig().getInt("factionmenu." + s.replace(".name", ".slot"));
			String itemMaterial = getConfig().getString("factionmenu." + s.replace(".name", ".material"));
			List<String> itemLore = getConfig().getStringList("factionmenu." + s.replace(".name", ".lore"));
			ItemStack item = help.nameItemLore(new ItemStack (Material.getMaterial(itemMaterial)), help.cc(itemName), itemLore);
			menuItems.put(itemSlot, item);
			System.out.println(item);
		}
	}
	
	public void registerEvents() {
		Bukkit.getPluginManager().registerEvents(new FirstJoin(this), this);
		Bukkit.getPluginManager().registerEvents(new CreateFaction(this), this);
		Bukkit.getPluginManager().registerEvents(new Schem(this), this);
		Bukkit.getPluginManager().registerEvents(new ChatFormat(this), this);
	}

	public Helpers help() {
		return this.help;
	}
	public Inventories invHelp() {
		return this.invHelp;
	}
	public FileConfiguration getPlayerConfig() {
		return playerData;
	}
	public FileConfiguration getSchemConfig() {
		return schems;
	}
	public void loadSchems() {
		ArrayList<Block> skiddo123Schem = help.loadSchem("skiddo123");
		schematics.put("skiddo123", skiddo123Schem);
	}
}

