package com.ascendpvp.factionsarcade.cmds.admin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.ascendpvp.factionsarcade.FactionsArcadeMain;

public class GiveSchemItem implements CommandExecutor {

	FactionsArcadeMain plugin;
	public GiveSchemItem(FactionsArcadeMain plugin) {
		this.plugin = plugin;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		Player p = (Player) sender;
		p.getInventory().addItem(plugin.items.get("schem_item"));
		p.sendMessage(plugin.chatMessages.get("given_schem_item"));
		
		return false;
	}

}
