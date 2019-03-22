package com.ascendpvp.factionsarcade.cmds;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.ascendpvp.factionsarcade.FactionsArcadeMain;

public class FactionBal implements CommandExecutor {

	FactionsArcadeMain plugin;
	public FactionBal(FactionsArcadeMain plugin) {
		this.plugin = plugin;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		Player p = (Player) sender;
		String pid = p.getUniqueId().toString();
		int bal = plugin.playerData.getInt(pid + ".balance");
		
		p.sendMessage(plugin.chatMessages.get("balance_message").replace("#amount#", String.valueOf(bal)));
		
		return false;
	}
}
