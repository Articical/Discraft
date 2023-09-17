package me.Ryan.discraft.discord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import javax.security.auth.login.LoginException;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerLoginEvent;

import me.Ryan.discraft.Main;
import me.Ryan.discraft.spigot.sutils;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.md_5.bungee.api.ChatColor;

public class utils extends ListenerAdapter implements Listener{
	public static Main plugin;
	public static JDA jda;
	public static HashMap<String,String> usermap;
	public HashMap<String,String> codemap;
	public Random rand = new Random();
	public TextChannel chatchannel;
	public TextChannel cmdchannel;
	public TextChannel authchannel;
	public utils(Main main) {
		
		utils.plugin = main;
		startbot();
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		usermap = new HashMap<>();
		codemap = new HashMap<>();
		jda.addEventListener(this);
		
	}
	
	public void startbot() {
		
		try {
			jda = new JDABuilder().setToken(plugin.getConfig().getString("Bot.Token")).build();
		} catch (LoginException e) {
			e.printStackTrace();
		}
		
		if(plugin.getConfig().getBoolean("chat.to.discord") == true) {
			
			chatchannel = jda.getTextChannelById(plugin.getConfig().getString("chat.channel.id"));
			
		}
		
		if(plugin.getConfig().getBoolean("command.to.server") == true) {
			
			cmdchannel = jda.getTextChannelById(plugin.getConfig().getString("cmd.channel.id"));
			
		}
		
		
		if(plugin.getConfig().getBoolean("auth.to.join")) {
			
			authchannel = jda.getTextChannelById(plugin.getConfig().getString("auth.to.join"));
			
		}
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		
		String message = e.getMessage();
		
		String playername = e.getPlayer().getName();
		
		message = playername + " - " + message; 
		
		if(plugin.getConfig().getBoolean("chat.to.discord") == true) {
		
			chatchannel.sendMessage("> " + message).queue();
		
		}
		
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onLogin(PlayerLoginEvent e) {
		
		if(plugin.getConfig().getString("loginverification") == "1") {
			
			if(sutils.isRegistered(e.getPlayer())) {
				
				e.getPlayer().sendMessage("Welcome back, " + e.getPlayer().getName());
				
				return;
				
			}else {
				
				if(!codemap.containsKey(e.getPlayer().getUniqueId().toString())) {
				
				List<String> alph = new ArrayList<String>();
				alph.add("PO");
				alph.add("AA");
				alph.add("AB");
				alph.add("TR");
				alph.add("DA");
				alph.add("HJ");
				alph.add("VF");
				alph.add("AS");
				alph.add("BG");
				alph.add("LM");
				alph.add("AC");
				
				int firsti = rand.nextInt(500000);
				int secondi = rand.nextInt(800000);
				
				String alp1 = alph.get(rand.nextInt(alph.toArray().length -1));
				
				String alp2 = alph.get(rand.nextInt(alph.toArray().length -1));
				
				String code = alp1 + firsti + alp2 + secondi;
				
				codemap.put(e.getPlayer().getUniqueId().toString(),code);
				
				e.getPlayer().kickPlayer("" + ChatColor.BOLD + ChatColor.WHITE + "Auth using " + ChatColor.DARK_PURPLE + "!auth " + code + ChatColor.WHITE + " in the discord Auth channel!");
				
				}else {
					
					String code = codemap.get(e.getPlayer().getUniqueId().toString());
					
					e.getPlayer().kickPlayer("" + ChatColor.BOLD + ChatColor.WHITE + "Auth using " + ChatColor.DARK_PURPLE + "!auth " + code + ChatColor.WHITE + " in the discord Auth channel!");
					
				}
				
				
			}
			
		}
		
	}
	
	public void onGuildMessageReceived(GuildMessageReceivedEvent e) {
		
		if(e.getAuthor().isBot() || e.getAuthor().isFake() || e.isWebhookMessage())return;
		
		if(e.getChannel() == chatchannel) {
			
			String message = e.getMessage().getContentRaw();
			User user = e.getAuthor();
			
			Bukkit.broadcastMessage(ChatColor.DARK_BLUE + "[Discord] " + ChatColor.WHITE + user.getName() + " - " + message);
		
		}else if(e.getChannel() == cmdchannel) {
			
			String message = e.getMessage().getContentRaw();
			
			Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), message);
			
		}else if(e.getChannel() == authchannel) {
			
			String[] args = e.getMessage().getContentRaw().split(" ");
			
			if(args[0] == "!auth") {
				
				if(e.getMember().getRoles().stream().filter(role -> role.getName().equals(plugin.getConfig().getString("auth.role"))).findAny().orElse(null) !=null) {
					
					e.getAuthor().openPrivateChannel().complete().sendMessage("Sorry, you've already authorised your minecraft account!").queue();
					return;
				}
			
				if(args.length!=2) {
				
					e.getChannel().sendMessage(e.getAuthor().getAsMention() + " Correct usage: !auth <code>").queue();
					
					return;
				
				}
				
				if(!codemap.containsKey(args[1].toString())) {
					
					e.getChannel().sendMessage(e.getAuthor().getAsMention() + " Sorry, that Code is incorrect!").queue();
					return;
					
				}
				
				String id = codemap.get(args[1]);
				
				String dID = e.getAuthor().getId().toString();
				
				usermap.put(id, dID);
			
			}
			
		}
		
	}

}
