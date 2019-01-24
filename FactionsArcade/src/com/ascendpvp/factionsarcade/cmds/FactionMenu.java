package com.ascendpvp.factionsarcade.cmds;

import java.util.UUID;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.ascendpvp.factionsarcade.FactionsArcadeMain;

public class FactionMenu implements CommandExecutor {

	FactionsArcadeMain plugin;
	public FactionMenu(FactionsArcadeMain plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		Player p = (Player) sender;
		Inventory menuInv = loadFactionMenu();

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

		p.openInventory(menuInv);
		return false;
	}



	/*
	 * ----------------
	 * Load menu
	 * ----------------
	 */
	public Inventory loadFactionMenu() {
		int menuSize = plugin.getConfig().getInt("factionmenu.menusize");
		String menuName = plugin.help.cc(plugin.getConfig().getString("factionmenu.menun"));
		Inventory menuInv = Bukkit.createInventory(null, menuSize, menuName);
		for(Entry<Integer, ItemStack> entry : plugin.menuItems.entrySet()) {
			menuInv.setItem(entry.getKey(), entry.getValue());
		}
		return menuInv;
	}
}
