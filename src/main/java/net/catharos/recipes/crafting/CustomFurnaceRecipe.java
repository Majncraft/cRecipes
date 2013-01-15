package net.catharos.recipes.crafting;

import org.bukkit.Material;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;

public class CustomFurnaceRecipe extends CustomRecipe {
	protected FurnaceRecipe recipe;

	public CustomFurnaceRecipe( String name, Material in, byte in_id, Material out, int amount, byte id ) {
		super( name, new ItemStack( out, amount, id ) );

		setName( name );
		recipe = new FurnaceRecipe( getItem(), in, in_id );
	}

	public FurnaceRecipe getRecipe() {
		return recipe;
	}
}
