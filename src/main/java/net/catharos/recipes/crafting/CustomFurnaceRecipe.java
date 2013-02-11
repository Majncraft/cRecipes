package net.catharos.recipes.crafting;

import org.bukkit.Material;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;

public class CustomFurnaceRecipe extends CustomRecipe {
	protected FurnaceRecipe recipe;

	public CustomFurnaceRecipe( String name, Material in, short data, Material out, int amount, short data2, boolean setname ) {
		super( name, new ItemStack( out, amount, data2 ) );

		if (setname) setName( name );
		recipe = new FurnaceRecipe( getItem(), in, data );
	}

	public FurnaceRecipe getRecipe() {
		return recipe;
	}
}
