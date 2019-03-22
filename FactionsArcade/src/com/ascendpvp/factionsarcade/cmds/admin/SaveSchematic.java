package com.ascendpvp.factionsarcade.cmds.admin;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.ascendpvp.factionsarcade.FactionsArcadeMain;

//Class to save a cuboid region as an array to a hashmap
public class SaveSchematic implements CommandExecutor {

	FactionsArcadeMain plugin;
	public SaveSchematic(FactionsArcadeMain plugin) {
		this.plugin = plugin;
	}
	ArrayList<String> blockArray = new ArrayList<String>();

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if(args[1].equals(null)) return false;
		String schemName = args[1];
		World w = plugin.schemLeftClick.getWorld();
		Player p = (Player) sender;
		
		//Getting cuboid region based on selections made by player
		for (int pos1X = plugin.schemLeftClick.getBlockX(); pos1X <= plugin.schemRightClick.getBlockX(); pos1X++) {
			for (int pos1Y = plugin.schemLeftClick.getBlockY(); pos1Y <= plugin.schemRightClick.getBlockY(); pos1Y++) {
				for (int pos1Z = plugin.schemLeftClick.getBlockZ(); pos1Z <= plugin.schemRightClick.getBlockZ(); pos1Z++) {
					if(w.getBlockAt(pos1X, pos1Y, pos1Z).getType() == Material.AIR) continue;
					blockArray.add(pos1X + " " + pos1Y + " " + pos1Z + " " + w.getBlockAt(pos1X, pos1Y, pos1Z).getType().toString() + " " + w.getBlockAt(pos1X, pos1Y, pos1Z).getData());
				}
			}
		}
		//Store Array of blocks to hashmap and naming the access key according to "schemName"
		plugin.schematics.put(schemName, blockArray);
		for(String s : blockArray) {
			System.out.print(s);
		}
		plugin.help.saveSchem(schemName);
		p.sendMessage(plugin.chatMessages.get("schem_saved"));
		
		return false;
	}

}
