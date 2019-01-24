package com.ascendpvp.factionsarcade.events.items;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import com.ascendpvp.factionsarcade.FactionsArcadeMain;

public class Schem implements Listener {
	
	FactionsArcadeMain plugin;
	public Schem(FactionsArcadeMain plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onSchemClick(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if(p.getItemInHand() == null || p.getItemInHand().getItemMeta() == null) return;
		if(p.getItemInHand().getItemMeta().getDisplayName() == null) return;
		if(!e.getItem().equals(plugin.items.get("schem_item"))) return;
		e.setCancelled(true);
		
		//If player left clicks
		if(e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
			plugin.schemLeftClick.setWorld(e.getClickedBlock().getWorld());
			plugin.schemLeftClick.add(e.getClickedBlock().getLocation());
			p.sendMessage(plugin.chatMessages.get("pos1_selected").replace("#posLoc#", e.getClickedBlock().getLocation().toString()));
			return;
		}
		
		//If player right clicks
		if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			plugin.schemRightClick.setWorld(e.getClickedBlock().getWorld());
			plugin.schemRightClick.add(e.getClickedBlock().getLocation());
			p.sendMessage(plugin.chatMessages.get("pos2_selected").replace("#posLoc#", e.getClickedBlock().getLocation().toString()));
			return;
		}
		return;
	}
}
