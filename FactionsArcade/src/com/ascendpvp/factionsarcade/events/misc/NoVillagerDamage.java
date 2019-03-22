package com.ascendpvp.factionsarcade.events.misc;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import com.ascendpvp.factionsarcade.FactionsArcadeMain;

public class NoVillagerDamage implements Listener {

	FactionsArcadeMain plugin;
	public NoVillagerDamage(FactionsArcadeMain plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onVillagerDamage(EntityDamageEvent e) {
		if(!e.getEntityType().equals(EntityType.VILLAGER)) return;
		e.setCancelled(true);
	}
}
