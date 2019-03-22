package com.ascendpvp.factionsarcade.cmds;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.ascendpvp.factionsarcade.FactionsArcadeMain;

public class FactionHome implements CommandExecutor {

	FactionsArcadeMain plugin;
	public FactionHome(FactionsArcadeMain plugin) {
		this.plugin = plugin;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		Player p = (Player) sender;
		UUID pid = p.getUniqueId();
		//Ignore. Quick Null Check.
		if(!plugin.playerData.contains(pid.toString())) {
			p.sendMessage(plugin.chatMessages.get("not_in_fac"));
			return false;
		}
		//Check to see if player even has faction
		if(plugin.playerData.getString(pid.toString() + ".faction").equals("none")) {
			p.sendMessage(plugin.chatMessages.get("not_in_fac"));
			return false;
		}
		String pFaction = plugin.playerData.getString(pid.toString() + ".faction");
		/*
		 * 
		 * ADD CHARGE TIME!
		 * 
		 */
		
		//If player doesn't have a faction home
		if(plugin.factionData.getString(pFaction + ".home") == null) {
		p.sendMessage(plugin.chatMessages.get("faction_home"));
		p.teleport(new Location(Bukkit.getWorld(plugin.playerData.getString(pid.toString() + ".faction")), 0.5, 100, 0.5));
		//If they do
		} else {
			p.sendMessage(plugin.chatMessages.get("faction_home"));
			String[] parts = plugin.factionData.getString(pFaction + ".home").split(" ");
			String x = parts[0];
			String y = parts[1];
			String z = parts[2];
			p.teleport(new Location(Bukkit.getWorld(plugin.playerData.getString(pid.toString() + ".faction")), Double.parseDouble(x), Double.parseDouble(y), Double.parseDouble(z)));
		}
		
		return false;
	}
}
