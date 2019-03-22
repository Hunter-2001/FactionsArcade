package com.ascendpvp.factionsarcade;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.ascendpvp.factionsarcade.cmds.FactionAcceptInvite;
import com.ascendpvp.factionsarcade.cmds.FactionBal;
import com.ascendpvp.factionsarcade.cmds.FactionDisband;
import com.ascendpvp.factionsarcade.cmds.FactionHome;
import com.ascendpvp.factionsarcade.cmds.FactionInvite;
import com.ascendpvp.factionsarcade.cmds.FactionKick;
import com.ascendpvp.factionsarcade.cmds.FactionLeave;
import com.ascendpvp.factionsarcade.cmds.FactionMenu;
import com.ascendpvp.factionsarcade.cmds.FactionPromote;
import com.ascendpvp.factionsarcade.cmds.FactionSetHome;
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
		//If command executor is a player
		if (!(sender instanceof Player)) {
			System.out.println(plugin.getConfig().getString("messages.sender_console"));
			return false;
		}
		Player p = (Player)sender;
		//If player simply types "/f", print help
		if (args.length == 0) {
			plugin.help.printHelp(p);
			return false;
		}
		//Switch for first argument of command
		switch (args[0].toLowerCase()) {

		//F SchemItem command
		case "schemitem": {
			if (!p.hasPermission("fa.schem.item")) {
				p.sendMessage(plugin.chatMessages.get("no_permission"));
				return false;
			}
			return new GiveSchemItem(plugin).onCommand(sender, cmd, label, args);
		}

		//F SaveSchem command
		case "saveschem": {
			if(!p.hasPermission("fa.saveschem")) {
				p.sendMessage(plugin.chatMessages.get("no_permission"));
				return false;
			}
			return new SaveSchematic(plugin).onCommand(sender, cmd, label, args);
		}

		//F Invite command
		case "invite": {
			if(args.length != 2) {
				plugin.help.printHelp(p);
				return false;
			}
			return new FactionInvite(plugin).onCommand(sender, cmd, label, args);
		}

		//F Join command
		case "join": {
			if(args.length != 2) {
				plugin.help.printHelp(p);
				return false;
			}
			return new FactionAcceptInvite(plugin).onCommand(sender, cmd, label, args);
		}

		//F Kick command
		case "kick": {
			if(args.length != 2) {
				plugin.help.printHelp(p);
				return false;
			}
			return new FactionKick(plugin).onCommand(sender, cmd, label, args);
		}

		//F Promote command
		case "promote": {
			if(args.length != 2) {
				plugin.help.printHelp(p);
				return false;
			}
			return new FactionPromote(plugin).onCommand(sender, cmd, label, args);
		}

		//F Leave command
		case "leave": {
			return new FactionLeave(plugin).onCommand(sender, cmd, label, args);
		}

		//F Disband command
		case "disband": {
			return new FactionDisband(plugin).onCommand(sender, cmd, label, args);
		}

		//F Menu command
		case "menu": {
			return new FactionMenu(plugin).onCommand(sender, cmd, label, args);
		}

		//F Home command
		case "home": {
			return new FactionHome(plugin).onCommand(sender, cmd, label, args);
		}

		//F SetHome command
		case "sethome": {
			return new FactionSetHome(plugin).onCommand(sender, cmd, label, args);
		}

		//F Balance command
		case "bal": {
			return new FactionBal(plugin).onCommand(sender, cmd, label, args);
		}

		default: 
			plugin.help.printHelp(p);
			break;
		}
		return false;
	}
}
