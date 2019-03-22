package com.ascendpvp.factionsarcade.events.menuitems.warps;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import com.ascendpvp.factionsarcade.FactionsArcadeMain;

public class MineWarp implements Listener {

	FactionsArcadeMain plugin;
	public MineWarp(FactionsArcadeMain plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if(!e.getInventory().getName().contains("Faction Menu")) return;
		
		e.setCancelled(true);
		if(e.getSlot() != 11) return;
		
		e.getWhoClicked().teleport(new Location(e.getWhoClicked().getWorld(), 56.5, 56, 37.5, 132, 23));
		
	}
}
