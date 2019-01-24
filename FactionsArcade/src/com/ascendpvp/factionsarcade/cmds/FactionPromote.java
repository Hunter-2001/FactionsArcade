package com.ascendpvp.factionsarcade.cmds;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.ascendpvp.factionsarcade.FactionsArcadeMain;

public class FactionPromote implements CommandExecutor {

		FactionsArcadeMain plugin;
		public FactionPromote(FactionsArcadeMain plugin) {
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
			if(Bukkit.getPlayer(args[1]) == null) {
				p.sendMessage(plugin.chatMessages.get("player_not_available"));
				return false;
			}
			Player targetp = Bukkit.getPlayer(args[1]);
			UUID targetid = Bukkit.getPlayer(args[1]).getUniqueId();
			if(!plugin.playerData.getString(targetid.toString() + ".faction").equalsIgnoreCase(pfac)) {
				p.sendMessage(plugin.chatMessages.get("player_not_in_own_fac"));;
				return false;
			}
			if(p == targetp) {
				p.sendMessage(plugin.chatMessages.get("no_permission_promote"));
				return false;
			}
			int targetRank = plugin.playerData.getInt(targetid.toString() + ".factionrank");
			int playerRank = plugin.playerData.getInt(pid.toString() + ".factionrank");
			if(!(playerRank >= 3)) {
				p.sendMessage(plugin.chatMessages.get("no_permission_promote"));
				return false;
			}
			if(targetRank >= 3) {
				p.sendMessage(plugin.chatMessages.get("no_permission_promote"));
				return false;
			}
			
			p.sendMessage(plugin.chatMessages.get("player_promoted").replace("#targetPlayer#", targetp.getName()));
			targetp.sendMessage(plugin.chatMessages.get("target_promoted"));
			plugin.playerData.set(targetid.toString() + ".factionrank", targetRank + 1);
			plugin.help.savePlayerData();
			
			return false;
		}
}
