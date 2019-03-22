package com.ascendpvp.factionsarcade.events.misc;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import com.ascendpvp.factionsarcade.FactionsArcadeMain;

public class NoVoid implements Listener {

	FactionsArcadeMain plugin;
	public NoVoid(FactionsArcadeMain plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		if(e.getPlayer().getLocation().getY() > 5) return;
		e.getPlayer().teleport(new Location(e.getPlayer().getWorld(), 0.5, 71, 0.5));
	}
}
