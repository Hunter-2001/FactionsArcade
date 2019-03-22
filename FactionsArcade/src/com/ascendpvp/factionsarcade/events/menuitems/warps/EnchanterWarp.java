package com.ascendpvp.factionsarcade.events.menuitems.warps;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import com.ascendpvp.factionsarcade.FactionsArcadeMain;

public class EnchanterWarp implements Listener {
	
	FactionsArcadeMain plugin;
	public EnchanterWarp(FactionsArcadeMain plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if(!e.getInventory().getName().contains("Faction Menu")) return;
		
		e.setCancelled(true);
		if(e.getSlot() != 12) return;
		
		e.getWhoClicked().teleport(new Location(e.getWhoClicked().getWorld(), 49.5, 87, -1.5, -114, -31));
		
	}
}
