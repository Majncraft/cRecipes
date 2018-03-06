package net.catharos.recipes.crafting;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.Plugin;

public class CustomShapedRecipe extends CustomRecipe {
    Plugin plugin;
	protected ShapedRecipe recipe;

	public CustomShapedRecipe( Plugin plugin, String name, Material mat, int amount, short data, boolean setname ) {
		super(plugin, name, new ItemStack( mat, amount, data ) );

		if (setname) setName( name );
		recipe = new ShapedRecipe( getKey(name, plugin), getItem() );
	}

	public ShapedRecipe getRecipe() {
		return recipe;
	}

	private NamespacedKey getKey(String name, Plugin plugin){
		return new NamespacedKey(plugin, name.replace(" ", "_"));
	}
}
