package com.ascendpvp.factionsarcade.events.items;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.ascendpvp.factionsarcade.FactionsArcadeMain;

public class CreateFaction implements Listener {

	FactionsArcadeMain plugin;
	public CreateFaction(FactionsArcadeMain plugin) {
		this.plugin = plugin;
	}
	List<String> createTimeout = new ArrayList<String>();

	//Handle create faction click event and timeout for naming
	@EventHandler
	public void createFactionItemClick(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		String pid = p.getUniqueId().toString();
		if(p.getItemInHand() == null || p.getItemInHand().getItemMeta() == null) return;
		if(p.getItemInHand().getItemMeta().getDisplayName() == null) return;
		if(!e.getItem().equals(plugin.items.get("create_faction_item"))) return;
		//Check if player has already clicked item in past 30 seconds
		if(createTimeout.contains(pid)) return;
		e.setCancelled(true);
		p.sendMessage(plugin.chatMessages.get("attention"));
		p.sendMessage(plugin.chatMessages.get("name_faction"));
		p.sendMessage(plugin.chatMessages.get("attention"));
		createTimeout.add(pid);

		//Timeout if player doesn't input name
		new BukkitRunnable() {
			public void run() {
				if(!createTimeout.contains(pid)) return;
				p.sendMessage(plugin.chatMessages.get("attention"));
				p.sendMessage(plugin.chatMessages.get("name_faction_timeout"));
				p.sendMessage(plugin.chatMessages.get("attention"));
				createTimeout.remove(pid);
			}
		}.runTaskLater(plugin, 20*30);
	}

	//Handle the naming and creation of faction
	@EventHandler
	public void chatFactionName(PlayerChatEvent e) {
		Player p = e.getPlayer();
		String pid = p.getUniqueId().toString();
		if(!createTimeout.contains(pid)) return;
		String factionName = e.getMessage();
		e.setCancelled(true);

		if(factionName.equalsIgnoreCase("none")) return;
		//Check if faction name is alphanumeric
		if(!plugin.help.isAlphanumeric(factionName)) {
			p.sendMessage(plugin.chatMessages.get("faction_name_not_alphanumeric"));
			return;
		}
		//Check if faction name already exists
		if(plugin.factionData.contains(factionName)) {
			p.sendMessage(plugin.chatMessages.get("faction_already_exists"));
			return;
		}

		createTimeout.remove(pid);
		plugin.help.setupFaction(factionName, pid);//Setup faction method
		p.sendMessage(plugin.chatMessages.get("generating_faction_island"));
		p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 400, 5));
		p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 400, 5));
		//Create empty world
		WorldCreator wc = new WorldCreator(factionName);
		wc.type(WorldType.FLAT);
		wc.generatorSettings("2;0;1;");
		wc.createWorld();
		
		new BukkitRunnable() {
			public void run() {
				plugin.help.printSchem("skiddo123", Bukkit.getWorld(factionName));
			}
		}.runTaskLater(plugin, 20*20); //Wait 20 seconds

		new BukkitRunnable() {
			public void run() {
				p.teleport(new Location(Bukkit.getWorld(factionName), 0.5, 100, 0.5));
				p.getInventory().clear();
				p.sendMessage(plugin.chatMessages.get("teleporting_to_world").replace("#factionInput#", factionName));
			}
		}.runTaskLater(plugin, 20*15); //Wait 15 seconds

	}
}
