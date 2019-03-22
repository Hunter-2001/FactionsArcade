package com.ascendpvp.factionsarcade.cmds;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.ascendpvp.factionsarcade.FactionsArcadeMain;

public class FactionKick implements CommandExecutor {

	FactionsArcadeMain plugin;
	public FactionKick(FactionsArcadeMain plugin) {
		this.plugin = plugin;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		Player p = (Player) sender;
		UUID pid = p.getUniqueId();
		//Checks to see if command executor even has faction
		if(!plugin.playerData.contains(pid.toString())) {
			p.sendMessage(plugin.chatMessages.get("not_in_fac"));
			return false;
		}
		if(plugin.playerData.getString(pid.toString() + ".faction").equals("none")) {
			p.sendMessage(plugin.chatMessages.get("not_in_fac"));
			return false;
		}
		String pfac = plugin.playerData.getString(pid.toString() + ".faction");
		//If target is null
		if(Bukkit.getPlayer(args[1]) == null) {
			p.sendMessage(plugin.chatMessages.get("player_not_available"));
			return false;
		}
		Player targetp = Bukkit.getPlayer(args[1]);
		UUID targetid = Bukkit.getPlayer(args[1]).getUniqueId();
		//If command executor tries to kick someone outside of their own faction
		if(!plugin.playerData.getString(targetid.toString() + ".faction").equalsIgnoreCase(pfac)) {
			p.sendMessage(plugin.chatMessages.get("player_not_in_own_fac"));;
			return false;
		}
		//If player tries to kick themselves
		if(p == targetp) {
			p.sendMessage(plugin.chatMessages.get("no_permission_kick"));
			return false;
		}
		int targetRank = plugin.playerData.getInt(targetid.toString() + ".factionrank");
		int playerRank = plugin.playerData.getInt(pid.toString() + ".factionrank");
		//If command executors rank is not 2 or above
		if(!(playerRank >= 2)) {
			p.sendMessage(plugin.chatMessages.get("no_permission_kick"));
			return false;
		}
		//If command executors rank is not higher than the target
		if(!(playerRank > targetRank)) {
			p.sendMessage(plugin.chatMessages.get("no_permission_kick"));
			return false;
		}
		
		p.sendMessage(plugin.chatMessages.get("faction_kick").replace("#kickedPlayer#", targetp.getName()));
		targetp.sendMessage(plugin.chatMessages.get("faction_kicked"));
		targetp.teleport(new Location(Bukkit.getWorld("world"), 0.5, 100, 0.5));
		targetp.getInventory().clear();
		plugin.invHelp.starterInv(targetp);
		plugin.playerData.set(targetid.toString() + ".faction", "none");
		plugin.playerData.set(targetid.toString() + ".factionrank", 1);
		plugin.help.savePlayerData();
		/*
		 * Maybe add a broadcast throughout the faction?
		 * (Through the use of a helper)
		 */
		
		return false;
	}
}
