package com.ascendpvp.factionsarcade.cmds;

import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.ascendpvp.factionsarcade.FactionsArcadeMain;

public class FactionSetHome implements CommandExecutor {

	FactionsArcadeMain plugin;
	public FactionSetHome(FactionsArcadeMain plugin) {
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
		if(!(plugin.playerData.getInt(pid.toString() + ".factionrank") >= 2)) {
			p.sendMessage(plugin.chatMessages.get("cannot_sethome"));
			return false;
		}
		if(!p.getWorld().getName().equals(pFaction)) {
			p.sendMessage(plugin.chatMessages.get("invalid_sethome"));
			return false;
		}
		
		p.sendMessage(plugin.chatMessages.get("sethome_success"));
		plugin.factionData.set(pFaction + ".home", p.getLocation().getX() + " " + p.getLocation().getY() + " " + p.getLocation().getZ());
		plugin.help.saveFactionData();
		
		return false;
	}
}
