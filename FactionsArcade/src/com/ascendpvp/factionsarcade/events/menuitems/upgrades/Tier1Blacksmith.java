package com.ascendpvp.factionsarcade.events.menuitems.upgrades;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import com.ascendpvp.factionsarcade.FactionsArcadeMain;

public class Tier1Blacksmith implements Listener {
	
	FactionsArcadeMain plugin;
	public Tier1Blacksmith(FactionsArcadeMain plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if(!e.getInventory().getName().contains("Faction Menu")) return;
		
		e.setCancelled(true);
		if(e.getSlot() != 30) return;
		if(!(e.getWhoClicked() instanceof Player)) return;
		Player p = (Player) e.getWhoClicked();
		String pid = p.getUniqueId().toString();
		String pFac = plugin.playerData.getString(pid + ".faction");
		
		if(plugin.factionData.getInt(pFac + ".blacksmithlevel") > 0) {
			p.closeInventory();
			p.sendMessage(plugin.chatMessages.get("upgrade_already_purchased"));
			return;
		}
		
		if(!(plugin.playerData.getInt(pid + ".balance") >= 3000)) {
			p.closeInventory();
			p.sendMessage(plugin.chatMessages.get("balance_too_low"));
			return;
		}
		
		p.closeInventory();
		p.sendMessage(plugin.chatMessages.get("upgrade_success"));
		plugin.factionData.set(pFac + ".blacksmithlevel", 1);
		plugin.help.saveFactionData();
		plugin.help.printSchem("Blacksmith", Bukkit.getWorld(plugin.playerData.getString(pid + ".faction")));
	}
}
