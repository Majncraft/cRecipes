package net.catharos.recipes;

import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class cRecipes extends JavaPlugin implements Listener {

	public void onEnable() {
		getServer().getPluginManager().registerEvents( this, this );
	}

	@EventHandler
	public void e( BlockBreakEvent event ) {
		if (event.getPlayer().getGameMode() == GameMode.CREATIVE) return;
	}
}
