package com.ascendpvp.factionsarcade;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.ascendpvp.factionsarcade.cmds.FactionAcceptInvite;
import com.ascendpvp.factionsarcade.cmds.FactionDisband;
import com.ascendpvp.factionsarcade.cmds.FactionInvite;
import com.ascendpvp.factionsarcade.cmds.FactionKick;
import com.ascendpvp.factionsarcade.cmds.FactionLeave;
import com.ascendpvp.factionsarcade.cmds.FactionMenu;
import com.ascendpvp.factionsarcade.cmds.FactionPromote;
import com.ascendpvp.factionsarcade.cmds.admin.GiveSchemItem;
import com.ascendpvp.factionsarcade.cmds.admin.SaveSchematic;

import org.bukkit.command.CommandExecutor;

public class FactionCommands implements CommandExecutor {

	FactionsArcadeMain plugin;
	public FactionCommands(FactionsArcadeMain plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!cmd.getName().equalsIgnoreCase("f")) return false;
		if (!(sender instanceof Player)) {
			System.out.println(plugin.getConfig().getString("messages.sender_console"));
			return false;
		}
		Player p = (Player)sender;
		if (args.length == 0) {
			plugin.help.printHelp(p);
			return false;
		}
		//Switch for first arg of command
		switch (args[0].toLowerCase()) {
		case "schemitem": {
			if (!p.hasPermission("fa.schem.item")) {
				p.sendMessage(plugin.chatMessages.get("no_permission"));
				return false;
			}
			return new GiveSchemItem(plugin).onCommand(sender, cmd, label, args);
		}
		
		case "saveschem": {
			if(!p.hasPermission("fa.saveschem")) {
				p.sendMessage(plugin.chatMessages.get("no_permission"));
				return false;
			}
			return new SaveSchematic(plugin).onCommand(sender, cmd, label, args);
		}
		case "invite": {
			if(args.length != 2) {
				plugin.help.printHelp(p);
				return false;
			}
			return new FactionInvite(plugin).onCommand(sender, cmd, label, args);
		}
		case "test": { //Test command
			System.out.println(plugin.playersInvited.size());
			System.out.println(plugin.playersInvited.entrySet());
		}
		case "join": {
			if(args.length != 2) {
				plugin.help.printHelp(p);
				return false;
			}
			return new FactionAcceptInvite(plugin).onCommand(sender, cmd, label, args);
		}
		case "kick": {
			if(args.length != 2) {
				plugin.help.printHelp(p);
				return false;
			}
			return new FactionKick(plugin).onCommand(sender, cmd, label, args);
		}
		case "promote": {
			if(args.length != 2) {
				plugin.help.printHelp(p);
				return false;
			}
			return new FactionPromote(plugin).onCommand(sender, cmd, label, args);
		}
		case "leave": {
			return new FactionLeave(plugin).onCommand(sender, cmd, label, args);
		}
		case "disband": {
			return new FactionDisband(plugin).onCommand(sender, cmd, label, args);
		}
		case "menu": {
			return new FactionMenu(plugin).onCommand(sender, cmd, label, args);
		}

		default: 
			plugin.help.printHelp(p);
			break;
		}
		return false;
	}
}
