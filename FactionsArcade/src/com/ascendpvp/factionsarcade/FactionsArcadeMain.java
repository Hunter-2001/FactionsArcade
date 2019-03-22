package com.ascendpvp.factionsarcade;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.ascendpvp.factionsarcade.events.ChatFormat;
import com.ascendpvp.factionsarcade.events.FirstJoin;
import com.ascendpvp.factionsarcade.events.LoadOnJoin;
import com.ascendpvp.factionsarcade.events.MineshaftBlocks;
import com.ascendpvp.factionsarcade.events.items.CreateFaction;
import com.ascendpvp.factionsarcade.events.items.Schem;
import com.ascendpvp.factionsarcade.events.menuitems.upgrades.Tier1Blacksmith;
import com.ascendpvp.factionsarcade.events.menuitems.upgrades.Tier1Enchanter;
import com.ascendpvp.factionsarcade.events.menuitems.upgrades.Tier1Mine;
import com.ascendpvp.factionsarcade.events.menuitems.warps.BlacksmithWarp;
import com.ascendpvp.factionsarcade.events.menuitems.warps.EnchanterWarp;
import com.ascendpvp.factionsarcade.events.menuitems.warps.MineWarp;
import com.ascendpvp.factionsarcade.events.menuitems.warps.SpawnWarp;
import com.ascendpvp.factionsarcade.events.misc.NoNaturalSpawns;
import com.ascendpvp.factionsarcade.events.misc.NoVillagerDamage;
import com.ascendpvp.factionsarcade.events.misc.NoVoid;
import com.ascendpvp.factionsarcade.utils.Helpers;
import com.ascendpvp.factionsarcade.utils.Inventories;


public class FactionsArcadeMain extends JavaPlugin {

	public Helpers help;
	public Inventories invHelp;
	public Map<String, String> chatMessages;
	public Map<String, ItemStack> items;
	public Map<Integer, ItemStack> menuItems;
	public Map<String, ArrayList<String>> schematics;
	public Map<String, ArrayList<String>> playersInvited;
	public Map<String, Integer> payout;
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
		schematics = new HashMap<String, ArrayList<String>>();
		playersInvited = new HashMap<String, ArrayList<String>>();
		payout = new HashMap<String, Integer>();

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

		WorldCreator wc1 = new WorldCreator("BaseIsland");
		wc1.createWorld();
		WorldCreator wc2 = new WorldCreator("SchemWorld");
		wc2.createWorld();
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

		//Runnable to payout players for mining every 30 seconds
		new BukkitRunnable() {
			public void run() {
				for(Entry<String, Integer> s : payout.entrySet()) {
					playerData.set(s.getKey() + ".balance", playerData.getInt(s.getKey() + ".balance") + s.getValue());
					help.savePlayerData();
					Bukkit.getPlayer(UUID.fromString(s.getKey())).sendMessage(chatMessages.get("payout_message").replace("#amount#", s.getValue().toString()));
					payout.remove(s.getKey());
				}
			}
		}.runTaskTimer(this, 0, 20*10);
	}

	//Register all event handlers
	public void registerEvents() {
		Bukkit.getPluginManager().registerEvents(new FirstJoin(this), this);
		Bukkit.getPluginManager().registerEvents(new LoadOnJoin(this), this);
		Bukkit.getPluginManager().registerEvents(new CreateFaction(this), this);
		Bukkit.getPluginManager().registerEvents(new Schem(this), this);
		Bukkit.getPluginManager().registerEvents(new ChatFormat(this), this);
		Bukkit.getPluginManager().registerEvents(new MineWarp(this), this);
		Bukkit.getPluginManager().registerEvents(new EnchanterWarp(this), this);
		Bukkit.getPluginManager().registerEvents(new SpawnWarp(this), this);
		Bukkit.getPluginManager().registerEvents(new BlacksmithWarp(this), this);
		Bukkit.getPluginManager().registerEvents(new MineshaftBlocks(this), this);
		Bukkit.getPluginManager().registerEvents(new NoVillagerDamage(this), this);
		Bukkit.getPluginManager().registerEvents(new NoNaturalSpawns(this), this);
		Bukkit.getPluginManager().registerEvents(new NoVoid(this), this);
		Bukkit.getPluginManager().registerEvents(new Tier1Blacksmith(this), this);
		Bukkit.getPluginManager().registerEvents(new Tier1Mine(this), this);
		Bukkit.getPluginManager().registerEvents(new Tier1Enchanter(this), this);
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
	//Load schematics from flat .yml file into memory
	public void loadSchems() {
		ArrayList<String> TestIslandArray = help.loadSchem("TestIsland");
		schematics.put("TestIsland", TestIslandArray);
		ArrayList<String> EnchanterTowerArray = help.loadSchem("EnchanterTower");
		schematics.put("EnchanterTower", EnchanterTowerArray);
		ArrayList<String> BlacksmithArray = help.loadSchem("Blacksmith");
		schematics.put("Blacksmith", BlacksmithArray);
	}

	//Apply slowness to shopkeepers
	public void applySlowness() {
		for(World w : Bukkit.getWorlds()) {
			for(Entity e : w.getEntities()) {
				if(e.getType().equals(EntityType.VILLAGER)) {
					((Villager) e).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 200, false, false));
				}
			}
		}
	}
}

