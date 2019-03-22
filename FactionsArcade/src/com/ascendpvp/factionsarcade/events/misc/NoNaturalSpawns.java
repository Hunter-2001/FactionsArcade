package com.ascendpvp.factionsarcade.events.misc;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import com.ascendpvp.factionsarcade.FactionsArcadeMain;

public class NoNaturalSpawns implements Listener {

	FactionsArcadeMain plugin;
	public NoNaturalSpawns(FactionsArcadeMain plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onEntitySpawn(CreatureSpawnEvent e) {
		if(!e.getSpawnReason().equals(SpawnReason.NATURAL)) return;
		e.setCancelled(true);
	}
}
