package com.ascendpvp.factionsarcade.events;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.ascendpvp.factionsarcade.FactionsArcadeMain;

public class LoadOnJoin implements Listener {

	FactionsArcadeMain plugin;
	public LoadOnJoin(FactionsArcadeMain plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	private void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		String pid = p.getUniqueId().toString();
	
		if(!plugin.playerData.contains(pid)) return;
		if(plugin.playerData.getString(pid + ".faction").equalsIgnoreCase("none")) return;
		
		WorldCreator wc = new WorldCreator(plugin.playerData.getString(pid + ".faction"));
		wc.createWorld();
		p.teleport(new Location(Bukkit.getWorld(plugin.playerData.getString(pid.toString() + ".faction")), 0.5, 71, 0.5));
		
		//Apply slowness to shopkeepers
		for(Entity en : Bukkit.getWorld(plugin.playerData.getString(pid.toString() + ".faction")).getEntities()) {
			if(en.getType().equals(EntityType.VILLAGER)) {
				((Villager) en).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 200, false, false));
			}
		}
	}
}
