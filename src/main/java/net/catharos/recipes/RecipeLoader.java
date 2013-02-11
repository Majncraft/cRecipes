package net.catharos.recipes;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.catharos.recipes.crafting.CustomFurnaceRecipe;
import net.catharos.recipes.crafting.CustomRecipe;
import net.catharos.recipes.crafting.CustomShapedRecipe;
import net.catharos.recipes.crafting.CustomShapelessRecipe;
import net.catharos.recipes.util.MaterialUtil;
import net.catharos.recipes.util.TextUtil;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

public class RecipeLoader {
	protected cRecipes plugin;

	public RecipeLoader( cRecipes plugin ) {
		this.plugin = plugin;

		File recFile = new File( plugin.getDataFolder(), "recipes.yml" );
		if (!recFile.exists()) return;

		FileConfiguration recipes = YamlConfiguration.loadConfiguration( recFile );

		// 'Remove' vanilla recipes
		if (recipes.isList( "remove-vanilla" )) {
			List<String> removes = recipes.getStringList( "remove-vanilla" );

			if (!removes.isEmpty()) {
				Iterator<Recipe> ri = plugin.getServer().recipeIterator();

				while (ri.hasNext()) {
					Recipe recipe = ri.next();
					ItemStack result = recipe.getResult();

					for (String r : removes) {
						MaterialEntry mat = MaterialUtil.getMaterial( r );
						if (mat == null) continue;

						if (result.getType().equals( mat.material ) && result.getData().getData() == mat.data) {
							ri.remove();
							if (cRecipes.debug) {
								String mat_name = mat.material.name() + ((mat.data > 0) ? ":" + mat.data : "");
								plugin.getLogger().info( "Removed vanilla recipe for " + mat_name );
							}

							break;
						}
					}
				}
			}
		}

		// Shaped recipes
		if (recipes.isConfigurationSection( "shaped" )) {
			ConfigurationSection shaped = recipes.getConfigurationSection( "shaped" );
			for (String key : shaped.getKeys( false )) {
				if (shaped.isConfigurationSection( key )) {
					boolean success = this.loadRecipe( key, shaped.getConfigurationSection( key ), RecipeType.SHAPED );
					if (!cRecipes.debug) continue;

					if (success)
						plugin.getLogger().info( "Successfully added shaped recipe: " + key );
					else
						plugin.getLogger().info( "Error adding shaped recipe: " + key );
				}
			}
		}

		// Shapeless recipes
		if (recipes.isConfigurationSection( "shapeless" )) {
			ConfigurationSection shapeless = recipes.getConfigurationSection( "shapeless" );

			for (String key : shapeless.getKeys( false ))
				if (shapeless.isConfigurationSection( key )) {
					boolean success = this.loadRecipe( key, shapeless.getConfigurationSection( key ), RecipeType.SHAPELESS );
					if (!cRecipes.debug) continue;

					if (success)
						plugin.getLogger().info( "Successfully added shapeless recipe: " + key );
					else
						plugin.getLogger().info( "Error adding shapeless recipe: " + key );
				}
		}

		// Furnace recipes
		if (recipes.isConfigurationSection( "furnace" )) {
			ConfigurationSection furnace = recipes.getConfigurationSection( "furnace" );

			for (String key : furnace.getKeys( false ))
				if (furnace.isConfigurationSection( key )) {
					boolean success = this.loadRecipe( key, furnace.getConfigurationSection( key ), RecipeType.FURNACE );
					if (!cRecipes.debug) continue;

					if (success)
						plugin.getLogger().info( "Successfully added furnace recipe: " + key );
					else
						plugin.getLogger().info( "Error adding furnace recipe: " + key );
				}
		}
	}

	public boolean loadRecipe( String name, ConfigurationSection config, RecipeType type ) {
		// Output material
		String output = config.getString( "output", "" );
		if (output.isEmpty()) output = config.getString( "block" );

		MaterialEntry mEntry = MaterialUtil.getMaterial( output );
		if (mEntry == null) return false;

		// Crafting amount
		int amount = config.getInt( "amount", 1 );

		// Add the recipe
		CustomRecipe cr;

		// Set the item name?
		boolean setname = config.getBoolean( "set-name", true );

		try {
			switch (type) {
			case SHAPED:
				cr = new CustomShapedRecipe( name, mEntry.material, amount, mEntry.data, setname );
				if (!addShaped( (CustomShapedRecipe) cr, config )) return false;
				break;

			case SHAPELESS:
				cr = new CustomShapelessRecipe( name, mEntry.material, amount, mEntry.data, setname );
				if (!addShapeless( (CustomShapelessRecipe) cr, config )) return false;
				break;

			case FURNACE:
				String input = config.getString( "input", "" );

				MaterialEntry minEntry = MaterialUtil.getMaterial( input );
				if (minEntry == null) return false;

				cr = new CustomFurnaceRecipe( name, minEntry.material, minEntry.data, mEntry.material, amount, mEntry.data, setname );
				plugin.addRecipe( cr );
				break;

			default:
				cr = null;
			}
		} catch (Exception e) {
			plugin.getLogger().info( "Error loading recipe: " + e.getMessage() );
			return false;
		}

		// Craft permissions
		if (config.isString( "permission" )) cr.setPermission( config.getString( "permission" ) );
		if (config.isString( "messages.no-permission" )) cr.setNoPermissionMessage( config.getString( "messages.no-permission" ) );

		// Success message
		if (config.isString( "messages.success" )) cr.setSuccessMessage( config.getString( "messages.success" ) );

		// XP
		if (config.isInt( "xp.given" )) cr.setXPGiven( config.getInt( "xp.given" ) );
		if (config.isInt( "xp.needed" )) cr.setXPNeeded( config.getInt( "xp.needed" ) );
		if (config.isBoolean( "xp.subtract" )) cr.subtractXp( config.getBoolean( "xp.subtract" ) );

		// Levels
		if (config.isInt( "lvl.given" )) cr.setLvlGiven( config.getInt( "lvl.given" ) );
		if (config.isInt( "lvl.needed" )) cr.setLvlNeeded( config.getInt( "lvl.needed" ) );
		if (config.isBoolean( "lvl.subtract" )) cr.subtractLvl( config.getBoolean( "lvl.subtract" ) );

		// Item extra drops
		if (config.isList( "extras" )) {
			List<ItemStack> extras = new ArrayList<ItemStack>();

			for (String s : config.getStringList( "extras" )) {
				String[] i = s.split( ":" );

				if (i.length < 1) {
					plugin.getLogger().info( "Error loading recipe: " + ("Wrong format: " + s + " (should be like a:b[:c])") );
					continue;
				}

				MaterialEntry mat;

				mat = (i.length > 2) ? MaterialUtil.getMaterial( i[1] + ":" + i[2] ) : MaterialUtil.getMaterial( i[1] );
				if (mat == null) continue;

				extras.add( new ItemStack( mat.material, Integer.parseInt( i[0] ), mat.data ) );
			}

			cr.setExtraDrops( extras );
		}

		// Item lores
		if (config.isList( "details" )) {
			List<String> details = config.getStringList( "details" );
			List<String> parsed = new ArrayList<String>();

			for (String lore : details)
				parsed.add( TextUtil.parseColors( lore ) );

			cr.setDetails( parsed );
		}

		// Item drops
		List<ItemStack> drops = new ArrayList<ItemStack>();

		if (config.isList( "drops" )) {
			for (String s : config.getStringList( "drops" )) {
				MaterialEntry dropMat = MaterialUtil.getMaterial( s );
				if (dropMat == null) continue;

				CustomRecipe get = plugin.getRecipe( dropMat.material.getId(), dropMat.data );

				ItemStack item = null;

				if (get != null) {
					item = MaterialUtil.getSingle( get.getItem() );
				} else {
					item = new ItemStack( dropMat.material, 1, dropMat.data );
				}

				if (item.getType() != Material.AIR) drops.add( item );
			}
		} else {
			drops.add( MaterialUtil.getSingle( cr.getItem() ) );
		}

		cr.setDrops( drops );

		return true;
	}

	private boolean addShaped( CustomShapedRecipe cr, ConfigurationSection c ) throws Exception {
		ShapedRecipe recipe = cr.getRecipe();
		ConfigurationSection rc = c.getConfigurationSection( "recipe" );

		if (rc != null) {
			if (!rc.isList( "shape" )) throw new Exception( "Shape configuration is missing!" );

			List<String> shapeList = rc.getStringList( "shape" );
			recipe.shape( shapeList.toArray( new String[shapeList.size()] ) );

			ConfigurationSection ingredients = rc.getConfigurationSection( "ingredients" );
			for (String key : ingredients.getKeys( false )) {
				MaterialEntry mat = MaterialUtil.getMaterial( ingredients.getString( key ) );
				if (mat == null) continue;

				recipe.setIngredient( key.charAt( 0 ), mat.material, mat.data );
			}

			plugin.addRecipe( cr );
			return true;
		}

		return false;
	}

	private boolean addShapeless( CustomShapelessRecipe cr, ConfigurationSection c ) throws Exception {
		ShapelessRecipe recipe = cr.getRecipe();
		List<String> sl = c.getStringList( "recipe" );

		if (sl != null && !sl.isEmpty()) {
			for (String ingredient : sl) {
				String[] i = ingredient.split( ":" );
				if (i.length < 1) throw new Exception( "Wrong recipe format: " + ingredient + " (should be like a:b[:c])" );

				MaterialEntry mat;

				mat = (i.length > 2) ? MaterialUtil.getMaterial( i[1] + ":" + i[2] ) : MaterialUtil.getMaterial( i[1] );
				if (mat == null) continue;

				recipe.addIngredient( Integer.parseInt( i[0] ), mat.material, mat.data );
			}

			plugin.addRecipe( cr );
			return true;
		}

		return false;
	}

}
