package net.catharos.recipes.crafting;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class CustomRecipe {
	protected String name;
	protected final ItemStack item;

	protected List<ItemStack> drops;

	public CustomRecipe( String name, ItemStack item ) {
		this.name = name;
		this.item = item;

		this.drops = new ArrayList<ItemStack>();
	}

	/* ---------------- Public methods ---------------- */

	/**
	 * Returns the name of the item result
	 * 
	 * @return The name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the item result of this recipe
	 * 
	 * @return The {@link ItemStack} representation
	 */
	public ItemStack getItem() {
		return item;
	}

	/**
	 * Returns a list of item drops of this block
	 * 
	 * @return A {@link List} of item drops
	 */
	public List<ItemStack> getDrops() {
		return drops;
	}

	/**
	 * Sets the drops for this item block
	 * 
	 * @param drops The {@link List} of drops
	 */
	public void setDrops( List<ItemStack> drops ) {
		this.drops = drops;
	}

	/**
	 * Returns the {@link Recipe} for this block item
	 * 
	 * @return The crafting {@link Recipe}
	 */
	public abstract Recipe getRecipe();

	/* ---------------- Protected / Private Methods ---------------- */

	/**
	 * Sets the name of this block item
	 * 
	 * @param name The name to set
	 */
	protected void setName( String name ) {
		ItemMeta meta = getItem().getItemMeta();
		meta.setDisplayName( ChatColor.RESET + name );
		getItem().setItemMeta( meta );
	}
}
