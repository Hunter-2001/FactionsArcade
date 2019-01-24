package com.ascendpvp.factionsarcade.utils;

import org.bukkit.entity.Player;
import com.ascendpvp.factionsarcade.FactionsArcadeMain;

public class Inventories {

	FactionsArcadeMain plugin;
	public Inventories(FactionsArcadeMain plugin) {
		this.plugin = plugin;
	}

	/*
	 * ----------------
	 * Load inventories
	 * ----------------
	 */

	//Load starter inventory
	public void starterInv(Player p) {
		p.getInventory().clear();
		p.getInventory().setItem(0, (plugin.items.get("create_faction_item")));
		p.getInventory().setItem(4, (plugin.items.get("ftop_item")));
		p.getInventory().setItem(8, plugin.items.get("warps_item"));
		return;
	}
}
