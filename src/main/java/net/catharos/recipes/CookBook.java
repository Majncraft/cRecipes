package net.catharos.recipes;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class CookBook implements Listener {
	public CookBook( JavaPlugin plugin ) {
		Bukkit.getServer().getPluginManager().registerEvents( this, plugin );
	}
}
