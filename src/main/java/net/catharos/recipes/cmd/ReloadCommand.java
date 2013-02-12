package net.catharos.recipes.cmd;

import java.io.File;

import net.catharos.recipes.cRecipes;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class ReloadCommand implements CommandExecutor {
	private cRecipes plugin;

	public ReloadCommand( cRecipes plugin ) {
		this.plugin = plugin;
	}

	public boolean onCommand( CommandSender sender, Command cmd, String label, String[] args ) {
		if (sender instanceof ConsoleCommandSender || (sender instanceof Player && ((Player) sender).isOp())) {
			try {
				plugin.getConfig().load( new File( plugin.getDataFolder(), "config.yml" ) );
			} catch (Exception e) {
				plugin.getLogger().severe( "Error reloading plugin config: " + e.getMessage() );
			}

			plugin.reload( true );

			return true;
		}

		return false;
	}

}
