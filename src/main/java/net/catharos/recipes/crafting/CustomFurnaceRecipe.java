package net.catharos.recipes.crafting;

import org.bukkit.Material;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class CustomFurnaceRecipe extends CustomRecipe {
	protected FurnaceRecipe recipe;

	public CustomFurnaceRecipe(Plugin plugin, String name, Material in, short data, Material out, int amount, short data2, boolean setname ) {
		super(plugin, name, new ItemStack( out, amount, data2 ) );

		if (setname) setName( name );
		recipe = new FurnaceRecipe( getItem(), in, data );
	}

	public FurnaceRecipe getRecipe() {
		return recipe;
	}
}
