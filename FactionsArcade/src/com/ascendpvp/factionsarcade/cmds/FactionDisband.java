package com.ascendpvp.factionsarcade.cmds;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.ascendpvp.factionsarcade.FactionsArcadeMain;

public class FactionDisband implements CommandExecutor {

	FactionsArcadeMain plugin;
	public FactionDisband(FactionsArcadeMain plugin) {
		this.plugin = plugin;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		Player p = (Player) sender;
		UUID pid = p.getUniqueId();
		if(!plugin.playerData.contains(pid.toString())) {
			p.sendMessage(plugin.chatMessages.get("not_in_fac"));
			return false;
		}
		//Check to see if player even has faction
		if(plugin.playerData.getString(pid.toString() + ".faction").equals("none")) {
			p.sendMessage(plugin.chatMessages.get("not_in_fac"));
			return false;
		}
		String pfac = plugin.playerData.getString(pid.toString() + ".faction");
		int plevel = plugin.playerData.getInt(pid.toString() + ".factionrank");
		//Check if player is owner
		if(plevel != 4){
			p.sendMessage(plugin.chatMessages.get("cannot_disband"));
			return false;
		}
		
		for(String key : plugin.playerData.getKeys(true)) {
			if(!key.contains(".faction")) continue;
			if(!plugin.playerData.getString(key).equalsIgnoreCase(pfac)) continue;
			plugin.playerData.set(key, "none");
			plugin.playerData.set(key.replace(".faction", ".factionrank"), 1);
		}
		plugin.factionData.set(pfac, null);
		plugin.help.savePlayerData();
		plugin.help.saveFactionData();
		
		World w = Bukkit.getWorld(pfac);
		File f = null;
		try {
			f = w.getWorldFolder().getCanonicalFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		plugin.help.unloadWorld(w);
		plugin.help.deleteWorld(f);
		plugin.help.broadcastMessage(plugin.chatMessages.get("disband_broadcast").replace("#disbandedFaction#", pfac));
		
		return false;
	}
	
}
