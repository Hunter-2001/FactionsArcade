package com.ascendpvp.factionsarcade.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.ascendpvp.factionsarcade.FactionsArcadeMain;

public class FirstJoin implements Listener {

	FactionsArcadeMain plugin;
	public FirstJoin(FactionsArcadeMain plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	private void onDefaultJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		String pid = p.getUniqueId().toString();
		//Return if the player has already joined before
		if(plugin.playerData.contains(pid)) return;

		new BukkitRunnable() {
			public void run() {

				plugin.invHelp.starterInv(p);//Setup inv method
				plugin.playerData.set(pid, null);
				plugin.playerData.set(pid + ".username", p.getName());
				plugin.playerData.set(pid + ".faction", "none");
				plugin.playerData.set(pid + ".factionrank", 1);
				plugin.playerData.set(pid + ".level", 0);
				plugin.playerData.set(pid + ".balance", 0);
				plugin.help.savePlayerData();
			}
		}.runTaskLater(plugin, 5);
	}
}
