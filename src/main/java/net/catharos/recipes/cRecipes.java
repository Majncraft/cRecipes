package net.catharos.recipes;

import org.bukkit.plugin.java.JavaPlugin;

public class cRecipes extends JavaPlugin {

	public void onEnable() {
		new CookBook( this );
	}

	public void onDisable() {
	}

}
