package com.ascendpvp.factionsarcade.cmds;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.ascendpvp.factionsarcade.FactionsArcadeMain;

public class FactionLeave implements CommandExecutor {

	FactionsArcadeMain plugin;
	public FactionLeave(FactionsArcadeMain plugin) {
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
		//Check to see if player owns the faction
		if(plugin.playerData.getInt(pid.toString() + ".factionrank") == 4) {
			p.sendMessage(plugin.chatMessages.get("leader_cannot_leave"));
		}
		
		//Broadcast the faction leave to all members of the faction world
		for(Player worldPlayer : Bukkit.getWorld(plugin.playerData.getString(pid.toString() + ".faction")).getPlayers()) {
			worldPlayer.sendMessage(plugin.chatMessages.get("player_left_faction").replace("#playerLeaving#", p.getName()));
		}
		
		p.sendMessage(plugin.chatMessages.get("faction_leave"));
		p.teleport(new Location(Bukkit.getWorld("world"), 0.5, 100, 0.5));
		p.getInventory().clear();
		plugin.invHelp.starterInv(p);
		plugin.playerData.set(pid.toString() + ".faction", "none");
		plugin.playerData.set(pid.toString() + ".factionrank", 1);
		plugin.help.savePlayerData();
		
		return false;
	}
}
