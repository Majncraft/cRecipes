package net.catharos.recipes.crafting;

import java.util.ArrayList;
import java.util.List;

import net.catharos.recipes.util.TextUtil;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class CustomRecipe {
	protected String name;
	protected String permission;
	protected String noPermMsg;
	protected String successMsg;
	protected String notEnoughXpMsg;
	protected String notEnoughLvlMsg;

	protected float xpNeeded;
	protected float xpGiven;

	protected int lvlNeeded;
	protected int lvlGiven;

	protected boolean remXp = false;
	protected boolean remLvl = false;

	protected final ItemStack item;

	protected List<ItemStack> drops;
	protected List<ItemStack> extras;
	protected List<String> details;

	public CustomRecipe( String name, ItemStack item ) {
		this.name = name;
		this.item = item;

		this.permission = "";
		this.noPermMsg = "";
		this.successMsg = "";

		this.xpNeeded = 0;
		this.xpGiven = 0;

		this.lvlNeeded = 0;
		this.lvlGiven = 0;

		this.drops = new ArrayList<ItemStack>();
		this.extras = new ArrayList<ItemStack>();
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
	 * Returns the permission string needed to craft this item. If no permission
	 * was set an empty string is being returned.
	 * 
	 * @return The Permission String
	 */
	public String getPermission() {
		return permission;
	}

	/**
	 * Sets the permission string needed to craft this item. If no permission is
	 * needed, the string can be empty.
	 * 
	 * @param permission The permission string to set
	 */
	public void setPermission( String permission ) {
		this.permission = permission;
	}

	/**
	 * Returns the message showed when the player lacks the permission to craft
	 * the item
	 * 
	 * @return The message to show
	 */
	public String getNoPermissionMessage() {
		return noPermMsg;
	}

	/**
	 * Sets the message showed when the player lacks the permission to craft the
	 * item
	 * 
	 * @param msg The message to show
	 */
	public void setNoPermissionMessage( String msg ) {
		this.noPermMsg = msg;
	}

	/**
	 * Returns the success message shown when a player successfully crafted a
	 * custom item or block
	 * 
	 * @return The success message
	 */
	public String getSuccessMessage() {
		return successMsg;
	}

	/**
	 * Sets the success message shown when a player successfully crafted a
	 * custom item or block
	 * 
	 * @param msg The success message
	 */
	public void setSuccessMessage( String msg ) {
		this.successMsg = msg;
	}

	public float getXPNeeded() {
		return this.xpNeeded;
	}

	public void setXPNeeded( float xp ) {
		this.xpNeeded = xp;
	}

	public boolean subtractXp() {
		return this.remXp;
	}

	public void subtractXp( boolean sub ) {
		this.remXp = sub;
	}

	public float getXPGiven() {
		return this.xpGiven;
	}

	public void setXPGiven( float xp ) {
		this.xpGiven = xp;
	}

	public String getNotEnoughXpMessage() {
		return this.notEnoughXpMsg;
	}

	public void setNotEnoughXpMessage( String msg ) {
		this.notEnoughXpMsg = msg;
	}

	public int getLvlNeeded() {
		return this.lvlNeeded;
	}

	public void setLvlNeeded( int lvl ) {
		this.lvlNeeded = lvl;
	}

	public boolean subtractLvl() {
		return this.remLvl;
	}

	public void subtractLvl( boolean sub ) {
		this.remLvl = sub;
	}

	public int getLvlGiven() {
		return this.lvlGiven;
	}

	public void setLvlGiven( int lvl ) {
		this.lvlGiven = lvl;
	}

	public String getNotEnoughLvlMessage() {
		return this.notEnoughLvlMsg;
	}

	public void setNotEnoughLvlMessage( String msg ) {
		this.notEnoughLvlMsg = msg;
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

	public List<ItemStack> getExtraDrops() {
		return extras;
	}

	public void setExtraDrops( List<ItemStack> extras ) {
		this.extras = extras;
	}

	/**
	 * Sets the item details for the result of this recipe
	 * 
	 * @param details A {@link List} of item details
	 */
	public void setDetails( List<String> details ) {
		ItemMeta meta = getItem().getItemMeta();
		meta.setLore( details );
		getItem().setItemMeta( meta );
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
		meta.setDisplayName( ChatColor.RESET + TextUtil.parseColors( name ) );
		getItem().setItemMeta( meta );
	}
}
