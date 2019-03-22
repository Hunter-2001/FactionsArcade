package com.ascendpvp.factionsarcade.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.ascendpvp.factionsarcade.FactionsArcadeMain;

public class Helpers {

	FactionsArcadeMain plugin;
	public Helpers(FactionsArcadeMain plugin) {
		this.plugin = plugin;
	}

	//Handles easier chat colors
	public String cc(String s) {
		return ChatColor.translateAlternateColorCodes('&', s);
	}
	
	//For broadcasting a string to all online players
	public void broadcastMessage(String s) {
		for(Player p : Bukkit.getOnlinePlayers()) {
			p.sendMessage(s);
		}
	}

	//Prints off a help list pre-defined in the config
	public boolean printHelp(Player p) {
		List<String> help = (List<String>)plugin.getConfig().getStringList("help");
		for (String line : help) {
			String formatted = ChatColor.translateAlternateColorCodes('&', line);
			p.sendMessage(formatted);
		}
		return true;
	}

	//Handles naming items
	public ItemStack nameItem(ItemStack item, String name) {
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		item.setItemMeta(meta);
		return item;
	}
	
	//Handles naming items with lore
	public ItemStack nameItemLore(ItemStack item, String name, List<String> lore) {
		List<String> coloredLores = new ArrayList<String>();
		for(String s : lore) {
			coloredLores.add(cc(s));
		}
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		meta.setLore(coloredLores);
		item.setItemMeta(meta);
		return item;
	}

	//Check to see if an input string is alphanumeric
	public boolean isAlphanumeric(String str) {
		for (int i=0; i<str.length(); i++) {
			char c = str.charAt(i);
			if (c < 0x30 || (c >= 0x3a && c <= 0x40) || (c > 0x5a && c <= 0x60) || c > 0x7a) return false;
		}
		return true;
	}

	//Basic try/catch to save playerData file
	public void savePlayerData() {
		try {
			plugin.playerData.save(plugin.playerDataFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//Basic try/catch to save factionData file
	public void saveFactionData() {
		try {
			plugin.factionData.save(plugin.factionDataFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//Basic try/catch to save schemData file
	public void saveSchemData() {
		try {
			plugin.schems.save(plugin.schematicsFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//Function to setup a new faction
	public void setupFaction(String factionName, String pid) {
		plugin.factionData.set(factionName, null);
		plugin.factionData.set(factionName + ".minelevel", 1);
		plugin.factionData.set(factionName + ".blacksmithlevel", 0);
		plugin.factionData.set(factionName + ".enchanterlevel", 0);
		plugin.factionData.set(factionName + ".commandcenterlevel", 0);
		plugin.playerData.set(pid + ".faction", factionName);
		plugin.playerData.set(pid + ".factionrank", 4);
		savePlayerData();
		saveFactionData();
	}

	//Method to print a schematic to a specific world from an array of blocks
	public void printSchem(String schemName, World w) {
		new BukkitRunnable() {
			Iterator<String> itr = plugin.schematics.get(schemName).iterator();
			public void run() {
				//If there are no more blocks to be placed in the schematic...
				if(!itr.hasNext()) {
					
					//Spawn enchanter villager
					if(schemName.equalsIgnoreCase("Enchanter")) {
						Villager v = (Villager) w.spawnEntity(new Location(w, 49.5, 101, -2.5), EntityType.VILLAGER);
						v.setCustomName(plugin.help.cc("&b&lEnchanter"));
						v.setCustomNameVisible(true);
						v.setCanPickupItems(false);
						v.setProfession(Profession.LIBRARIAN);
						v.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 200, false, false));
						
						//Spawn blacksmith villager
					} else if(schemName.equalsIgnoreCase("Blacksmith")) {
						Villager v = (Villager) w.spawnEntity(new Location(w, -44.5, 67, -7.5), EntityType.VILLAGER);
						v.setCustomName(plugin.help.cc("&b&lBlacksmith"));
						v.setCustomNameVisible(true);
						v.setCanPickupItems(false);
						v.setProfession(Profession.BLACKSMITH);
						v.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 200, false, false));
					}
					cancel();
					return;
				}
				//Actual Printing of blocks
				String[] parts = itr.next().split(" ");
				String blockX = parts[0];
				String blockY = parts[1];
				String blockZ = parts[2];
				String blockType = parts[3];
				String blockData = parts[4];
				w.getBlockAt(Integer.parseInt(blockX), Integer.parseInt(blockY), Integer.parseInt(blockZ)).setType(Material.getMaterial(blockType));
				w.getBlockAt(Integer.parseInt(blockX), Integer.parseInt(blockY), Integer.parseInt(blockZ)).setData(Byte.parseByte(blockData));
				w.createExplosion(Integer.parseInt(blockX), Integer.parseInt(blockY), Integer.parseInt(blockZ), 0, false, false);
			}
		}.runTaskTimer(plugin, 20, 1);
	}

	//Method for saving a schematic to a file
	public void saveSchem(String schemName) {
		plugin.schems.set(schemName, null);
		int amountOfBlocks = 0;
		for(@SuppressWarnings("unused") String s : plugin.schematics.get(schemName)) {
			amountOfBlocks++;
		}
		for(String s : plugin.schematics.get(schemName)) {
			String[] parts = s.split(" ");
			String blockX = parts[0];
			String blockY = parts[1];
			String blockZ = parts[2];
			String blockType = parts[3];
			String blockData = parts[4];
			plugin.schems.set(schemName + "." + amountOfBlocks + ".x", Integer.parseInt(blockX));
			plugin.schems.set(schemName + "." + amountOfBlocks + ".y", Integer.parseInt(blockY));
			plugin.schems.set(schemName + "." + amountOfBlocks + ".z", Integer.parseInt(blockZ));
			plugin.schems.set(schemName + "." + amountOfBlocks + ".type", blockType);
			plugin.schems.set(schemName + "." + amountOfBlocks + ".data", Byte.parseByte(blockData));
			amountOfBlocks--;
		}
		saveSchemData();
	}

	//Method for reading schematic data from a flat file and storing it to an array
	public ArrayList<String> loadSchem(String schemName) {
		ArrayList<String> blocksToPut = new ArrayList<String>();
		for(String key : plugin.schems.getConfigurationSection(schemName).getKeys(true)) {
			String fullKey = schemName + "." + key;
			if(fullKey.contains(".x")) {
				int blockX = plugin.schems.getInt(fullKey);
				int blockY = plugin.schems.getInt(fullKey.replace(".x", ".y"));
				int blockZ = plugin.schems.getInt(fullKey.replace(".x", ".z"));
				String blockType = plugin.schems.getString(fullKey.replace(".x", ".type"));
				int blockData = plugin.schems.getInt(fullKey.replace(".x", ".data"));
				blocksToPut.add(blockX + " " + blockY + " " + blockZ + " " + blockType + " " + blockData);
			}
		}
		return (blocksToPut);
	}

	//Method for unloading a world
	public void unloadWorld(World world) {
		if(world == null) return;
		for(Player p : world.getPlayers()) {
			p.teleport(new Location(Bukkit.getWorld("world"), 0.5, 100, 0.5));
			plugin.invHelp.starterInv(p);
		}
		Bukkit.getServer().unloadWorld(world, false);
		System.out.println(world.getName() + " unloaded!");
	}

	//Method for deleting physical world file path
	public boolean deleteWorld(File path) {
		if(path.exists()) {
			File files[] = path.listFiles();
			for(int i=0; i<files.length; i++) {
				if(files[i].isDirectory()) {
					deleteWorld(files[i]);
				} else {
					files[i].delete();
				}
			}
		}
		return(path.delete());
	}
}
