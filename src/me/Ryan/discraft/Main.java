package me.Ryan.discraft;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import me.Ryan.discraft.discord.utils;

public class Main extends JavaPlugin{

	public ConsoleCommandSender console;
	
	@Override
	public void onEnable() {
		console = Bukkit.getServer().getConsoleSender();
		console.sendMessage("[DisCraft] Enabled!");
		new utils(this);
	}
	
	@Override
	public void onDisable() {
		console.sendMessage("[DisCraft] Disabled!");
	}
	
}
