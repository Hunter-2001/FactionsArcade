package com.ascendpvp.factionsarcade.events;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.ascendpvp.factionsarcade.FactionsArcadeMain;

public class MineshaftBlocks implements Listener {

	FactionsArcadeMain plugin;
	public MineshaftBlocks(FactionsArcadeMain plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void ironOre(BlockBreakEvent e) {
		Player p = e.getPlayer();
		String pname = p.getUniqueId().toString();
		if(e.getBlock().getType().equals(Material.IRON_ORE)) {
			e.setCancelled(true);
			putValues(pname, 10);
			createRunnable(e.getBlock().getLocation(), Material.IRON_ORE);
			e.getBlock().setType(Material.STONE);
			
		} else if(e.getBlock().getType().equals(Material.GOLD_ORE)) {
			e.setCancelled(true);
			putValues(pname, 20);
			createRunnable(e.getBlock().getLocation(), Material.GOLD_ORE);
			e.getBlock().setType(Material.STONE);
			
		} else if(e.getBlock().getType().equals(Material.DIAMOND_ORE)) {
			e.setCancelled(true);
			putValues(pname, 50);
			createRunnable(e.getBlock().getLocation(), Material.DIAMOND_ORE);
			e.getBlock().setType(Material.STONE);
			
		} else if(e.getBlock().getType().equals(Material.IRON_BLOCK)) {
			
		} else if(e.getBlock().getType().equals(Material.GOLD_BLOCK)) {
			
		} else if(e.getBlock().getType().equals(Material.DIAMOND_BLOCK)) {

		}
	}
	//Method of putting values into HashMap
	private void putValues(String pname, int amount) {
		if(plugin.payout.containsKey(pname)) {
			plugin.payout.put(pname, plugin.payout.get(pname) + amount);
			return;
		}
		plugin.payout.put(pname, amount);
	}
	//Creating a runnable for each block broken
	private void createRunnable(Location loc, Material block) {
		new BukkitRunnable() {
			public void run() {
				loc.getBlock().setType(block);
			}
		}.runTaskLater(plugin, 20*10);
	}
}
