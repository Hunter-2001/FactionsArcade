package com.ascendpvp.factionsarcade.cmds.admin;

import java.util.ArrayList;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import com.ascendpvp.factionsarcade.FactionsArcadeMain;

public class SaveSchematic implements CommandExecutor {

	FactionsArcadeMain plugin;
	public SaveSchematic(FactionsArcadeMain plugin) {
		this.plugin = plugin;
	}
	ArrayList<Block> blockArray = new ArrayList<Block>();

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if(args[1].equals(null)) return false;
		String schemName = args[1];
		World w = plugin.schemLeftClick.getWorld();
		
		for (int pos1X = plugin.schemLeftClick.getBlockX(); pos1X <= plugin.schemRightClick.getBlockX(); pos1X++) {
			for (int pos1Y = plugin.schemLeftClick.getBlockY(); pos1Y <= plugin.schemRightClick.getBlockY(); pos1Y++) {
				for (int pos1Z = plugin.schemLeftClick.getBlockZ(); pos1Z <= plugin.schemRightClick.getBlockZ(); pos1Z++) {
					blockArray.add(w.getBlockAt(pos1X, pos1Y, pos1Z));
					System.out.println(w.getBlockAt(pos1X, pos1Y, pos1Z));
				}
			}
		}
		plugin.schematics.put(schemName, blockArray);
		plugin.help.saveSchem(schemName);
		return false;
	}

}
