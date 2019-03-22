package com.ascendpvp.factionsarcade.cmds;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.ascendpvp.factionsarcade.FactionsArcadeMain;

//Class to allow command executor to accept a faction invite
public class FactionAcceptInvite implements CommandExecutor {
	
	FactionsArcadeMain plugin;
	public FactionAcceptInvite(FactionsArcadeMain plugin) {
		this.plugin = plugin;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		Player p = (Player) sender;
		UUID pid = p.getUniqueId();
		//Ignore. Null Check.
		if(!plugin.playerData.contains(pid.toString())) {
			p.sendMessage(plugin.chatMessages.get("not_in_fac"));
			return false;
		}
		//Check to see if accepter is already in faction
		if(!plugin.playerData.getString(pid.toString() + ".faction").equals("none")) {
			p.sendMessage(plugin.chatMessages.get("player_already_in_faction"));
			return false;
		}
		//2 Checks to see if player has been invited to faction or not
		if(!plugin.playersInvited.containsKey(p.getName())) {
			p.sendMessage(plugin.chatMessages.get("not_invited_to_faction"));
			return false;
		}
		//If player tries to accept an invite to a faction they were not invited to
		String facName = args[1];
		if(!plugin.playersInvited.get(p.getName()).contains(facName)) {
			p.sendMessage(plugin.chatMessages.get("not_invited_to_faction"));
			return false;
		}
		
		//Remove player from invite list
		plugin.playersInvited.remove(p.getName());
		//Teleport player, clear inventory
		p.sendMessage(plugin.chatMessages.get("faction_joined").replace("#joinedFaction#", facName));
		p.teleport(new Location(Bukkit.getWorld(facName), 0.5, 100, 0.5));
		p.getInventory().clear();
		//Set player faction data to the desired faction name
		plugin.playerData.set(pid.toString() + ".faction", facName);
		plugin.help.savePlayerData();
		
		return false;
	}

}
