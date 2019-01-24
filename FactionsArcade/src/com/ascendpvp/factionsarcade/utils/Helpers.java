package com.ascendpvp.factionsarcade.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.ascendpvp.factionsarcade.FactionsArcadeMain;

public class Helpers {

	FactionsArcadeMain plugin;
	public Helpers(FactionsArcadeMain plugin) {
		this.plugin = plugin;
	}

	public String cc(String s) {
		return ChatColor.translateAlternateColorCodes('&', s);
	}
	
	public void broadcastMessage(String s) {
		for(Player p : Bukkit.getOnlinePlayers()) {
			p.sendMessage(s);
		}
	}

	public boolean printHelp(Player p) {
		List<String> help = (List<String>)plugin.getConfig().getStringList("help");
		for (String line : help) {
			String formatted = ChatColor.translateAlternateColorCodes('&', line);
			p.sendMessage(formatted);
		}
		return true;
	}

	public ItemStack nameItem(ItemStack item, String name) {
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		item.setItemMeta(meta);
		return item;
	}
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

	public boolean isAlphanumeric(String str) {
		for (int i=0; i<str.length(); i++) {
			char c = str.charAt(i);
			if (c < 0x30 || (c >= 0x3a && c <= 0x40) || (c > 0x5a && c <= 0x60) || c > 0x7a) return false;
		}
		return true;
	}

	public void savePlayerData() {
		try {
			plugin.playerData.save(plugin.playerDataFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void saveFactionData() {
		try {
			plugin.factionData.save(plugin.factionDataFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void saveSchemData() {
		try {
			plugin.schems.save(plugin.schematicsFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setupFaction(String factionName, String pid) {
		plugin.factionData.set(factionName, null);
		plugin.factionData.set(factionName + ".testlevel", 1);
		plugin.playerData.set(pid + ".faction", factionName);
		plugin.playerData.set(pid + ".factionrank", 4);
		savePlayerData();
		saveFactionData();
	}

	public void printSchem(String schemName, World w) {
		for(Block b : plugin.schematics.get(schemName)) {
			w.getBlockAt(b.getLocation()).setType(b.getType());
			System.out.println(b.getType());
		}
	}

	public void saveSchem(String schemName) {
		plugin.schems.set(schemName, null);
		int amountOfBlocks = 0;
		for(@SuppressWarnings("unused") Block b : plugin.schematics.get(schemName)) {
			amountOfBlocks++;
		}
		for(Block b : plugin.schematics.get(schemName)) {
			plugin.schems.set(schemName + "." + amountOfBlocks + ".x", b.getX());
			plugin.schems.set(schemName + "." + amountOfBlocks + ".y", b.getY());
			plugin.schems.set(schemName + "." + amountOfBlocks + ".z", b.getZ());
			plugin.schems.set(schemName + "." + amountOfBlocks + ".type", b.getType().toString());
			amountOfBlocks--;
		}
		saveSchemData();
	}

	public ArrayList<Block> loadSchem(String schemName) {
		ArrayList<Block> blocksToPut = new ArrayList<Block>();
		for(String key : plugin.schems.getConfigurationSection(schemName).getKeys(true)) {
			String fullKey = schemName + "." + key;
			if(fullKey.contains(".x")) {
				int blockX = plugin.schems.getInt(fullKey);
				int blockY = plugin.schems.getInt(fullKey.replace(".x", ".y"));
				int blockZ = plugin.schems.getInt(fullKey.replace(".x", ".z"));
				/*
				 * 
				 *CHANGE WORLD TO SchemWorld 
				 * 
				 */
				Block schemBlock = Bukkit.getWorld("world").getBlockAt(blockX, blockY, blockZ);
				blocksToPut.add(schemBlock);
			}
		}
		return (blocksToPut);
	}

	public void unloadWorld(World world) {
		if(world == null) return;
		for(Player p : world.getPlayers()) {
			p.teleport(new Location(Bukkit.getWorld("world"), 0.5, 100, 0.5));
		}
		Bukkit.getServer().unloadWorld(world, false);
		System.out.println(world.getName() + " unloaded!");
	}

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
