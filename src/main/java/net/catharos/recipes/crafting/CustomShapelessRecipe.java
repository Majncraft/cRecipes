package net.catharos.recipes.crafting;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.Plugin;

public class CustomShapelessRecipe extends CustomRecipe {
	protected ShapelessRecipe recipe;

	public CustomShapelessRecipe(Plugin plugin, String name, Material mat, int amount, short data, boolean setname) {
		super(plugin, name, new ItemStack( mat, amount, data ) );

		if (setname) setName( name );
		recipe = new ShapelessRecipe( getKey(name, plugin), getItem() );
	}

	public ShapelessRecipe getRecipe() {
		return recipe;
	}

    private NamespacedKey getKey(String name, Plugin plugin){
        return new NamespacedKey(plugin, name.replace(" ", "_"));
    }
}
