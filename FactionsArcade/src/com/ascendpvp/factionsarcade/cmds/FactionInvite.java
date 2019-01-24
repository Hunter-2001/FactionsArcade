package com.ascendpvp.factionsarcade.cmds;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.ascendpvp.factionsarcade.FactionsArcadeMain;

public class FactionInvite implements CommandExecutor {

	FactionsArcadeMain plugin;
	public FactionInvite(FactionsArcadeMain plugin) {
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
		if(plugin.playerData.getString(pid.toString() + ".faction").equals("none")) {
			p.sendMessage(plugin.chatMessages.get("not_in_fac"));
			return false;
		}
		String pfac = plugin.playerData.getString(pid.toString() + ".faction");
		if(Bukkit.getPlayer(args[1]) == null) {
			p.sendMessage(plugin.chatMessages.get("player_not_available"));
			return false;
		}
		Player targetp = Bukkit.getPlayer(args[1]);
		UUID targetid = Bukkit.getPlayer(args[1]).getUniqueId();
		//Ignore. Null Check.
		if(!plugin.playerData.contains(targetid.toString())) {
			p.sendMessage(plugin.chatMessages.get("not_in_fac"));
			return false;
		}
		if(!plugin.playerData.getString(targetid.toString() + ".faction").equals("none")) {
			p.sendMessage(plugin.chatMessages.get("target_already_in_faction"));
			return false;
		}
		targetp.sendMessage(plugin.chatMessages.get("faction_invite").replace("#senderFaction#", pfac));
		targetp.sendMessage(plugin.chatMessages.get("attention"));
		targetp.sendMessage(plugin.chatMessages.get("faction_invite_help"));
		p.sendMessage(plugin.chatMessages.get("player_invited").replace("#playerInvited#", targetp.getName().toString()));
		
		if(plugin.playersInvited.containsKey(targetp.getName().toString())) {
			ArrayList<String> currentInvites = plugin.playersInvited.get(targetp.getName().toString());
			currentInvites.add(pfac);
			plugin.playersInvited.put(targetp.getName().toString(), currentInvites);
			return false;
		} else {
			ArrayList<String> newInvite = new ArrayList<String>();
			newInvite.add(pfac);
			plugin.playersInvited.put(targetp.getName().toString(), newInvite);
		}
		
		return false;
	}

}
