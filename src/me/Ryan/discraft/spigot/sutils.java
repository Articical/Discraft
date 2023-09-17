package me.Ryan.discraft.spigot;

import org.bukkit.entity.Player;

import me.Ryan.discraft.discord.utils;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.User;

public class sutils {
	
	public static int isPermitted(Player p) {
		
		String id = p.getUniqueId().toString();
		
		if(utils.usermap.containsKey(id)) {
			
			String discordid = utils.usermap.get(id);
			
			User u = utils.jda.getUserById(discordid);
			
			Guild server = utils.jda.getGuildById(utils.plugin.getConfig().getString("server.id"));
			
			if(server.getMember(u).getRoles().stream().filter(role -> role.getName().equals(utils.plugin.getConfig().getString("sub.role"))).findAny().orElse(null) !=null) {
				
				return 1;
				
			}
			
			return 2;
			
		}
		
		return 3;
		
	}
	
	public static boolean isRegistered(Player p) {
		
		if(isPermitted(p) == 1) {
			
			return true;
			
		}else {
			
			return false;
			
		}
		
	}

}
