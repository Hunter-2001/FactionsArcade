package com.ascendpvp.factionsarcade.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.ascendpvp.factionsarcade.FactionsArcadeMain;

public class ChatFormat implements Listener {

	FactionsArcadeMain plugin;
	public ChatFormat(FactionsArcadeMain plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		
		String pid = e.getPlayer().getUniqueId().toString();
		String factionName = plugin.playerData.getString(pid + ".faction");
		int factionRank = plugin.playerData.getInt(pid + ".factionrank");
		
		if(factionRank == 1) {
			e.setFormat(e.getFormat().replace("{FACTION}", plugin.help.cc("&a" + factionName + "&r")));
			return;
		}
		if(factionRank == 2) {
			e.setFormat(e.getFormat().replace("{FACTION}", plugin.help.cc("&a" + factionName + "&r")));
			return;
		}
		if(factionRank == 3) {
			e.setFormat(e.getFormat().replace("{FACTION}", plugin.help.cc("&a" + factionName + "&r")));
			return;
		}
		if(factionRank == 4) {
			e.setFormat(e.getFormat().replace("{FACTION}", plugin.help.cc("&a" + factionName + "&r")));
			return;
		}
	}
}
