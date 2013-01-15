package net.catharos.recipes;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.catharos.recipes.crafting.CustomRecipe;
import net.catharos.recipes.crafting.CustomShapedRecipe;
import net.catharos.recipes.crafting.CustomShapelessRecipe;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;

public class RecipeLoader {
	protected cRecipes plugin;

	public RecipeLoader( cRecipes plugin ) {
		this.plugin = plugin;

		File recFile = new File( plugin.getDataFolder(), "recipes.yml" );
		if (!recFile.exists()) return;

		FileConfiguration recipes = YamlConfiguration.loadConfiguration( recFile );

		// Shaped recipes
		ConfigurationSection shaped = recipes.getConfigurationSection( "shaped" );
		for (String key : shaped.getKeys( false ))
			if (shaped.isConfigurationSection( key )) {
				if (this.loadRecipe( key, shaped.getConfigurationSection( key ), true ))
					plugin.getLogger().info( "Successfully added shaped recipe: " + key );
				else
					plugin.getLogger().info( "Error adding shaped recipe: " + key );
			}

		// Shapeless recipes
		ConfigurationSection shapeless = recipes.getConfigurationSection( "shapeless" );
		for (String key : shapeless.getKeys( false ))
			if (shapeless.isConfigurationSection( key )) {
				if (this.loadRecipe( key, shapeless.getConfigurationSection( key ), false ))
					plugin.getLogger().info( "Successfully added shapeless recipe: " + key );
				else
					plugin.getLogger().info( "Error adding shapeless recipe: " + key );
			}

	}

	public boolean loadRecipe( String name, ConfigurationSection config, boolean shaped ) {
		// Output material
		String[] out = config.getString( "block" ).split( ":" );
		byte data = 0;

		if (out.length > 1) data = Byte.parseByte( out[1] );

		Material mat = getMaterial( out[0] );
		if (mat == null) return false;

		// Crafting amount
		int amount = config.getInt( "amount", 1 );

		// Add the recipe
		CustomRecipe cr;

		try {
			if (shaped) {
				cr = new CustomShapedRecipe( name, mat, amount, data );
				if (!addShaped( (CustomShapedRecipe) cr, config )) return false;
			} else {
				cr = new CustomShapelessRecipe( name, mat, amount, data );
				if (!addShapeless( (CustomShapelessRecipe) cr, config )) return false;
			}
		} catch (Exception e) {
			plugin.getLogger().info( "Error loading recipe: " + e.getMessage() );
			return false;
		}

		// Item drops
		List<ItemStack> drops = new ArrayList<ItemStack>();

		if (config.isList( "drops" )) {
			for (String s : config.getStringList( "drops" )) {
				String[] l = s.split( ":" );

				if (l.length == 2)
					drops.add( new ItemStack( Material.getMaterial( l[0] ), 1, Short.parseShort( l[1] ) ) );
				else
					drops.add( new ItemStack( Material.getMaterial( l[0] ), 1 ) );
			}
		} else {
			drops.add( cr.getItem() );
		}

		cr.setDrops( drops );

		// Item lores
		if (config.isList( "details" )) {
			List<String> details = config.getStringList( "details" );

			ItemMeta meta = cr.getItem().getItemMeta();
			meta.setLore( details );
			cr.getItem().setItemMeta( meta );
		}

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
				String[] i = ingredients.getString( key ).split( ":" );

				Material mat = getMaterial( i[0] );
				if (mat == null) return false;

				if (i.length > 1)
					recipe.setIngredient( key.charAt( 0 ), mat, Integer.parseInt( i[1] ) );
				else
					recipe.setIngredient( key.charAt( 0 ), mat );
			}

			plugin.addRecipe( cr.getItem().getTypeId(), cr.getItem().getData().getData(), cr );

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

				Material mat = getMaterial( i[1] );
				if (mat == null) return false;

				if (i.length > 2)
					recipe.addIngredient( Integer.parseInt( i[0] ), mat, Integer.parseInt( i[2] ) );
				else
					recipe.addIngredient( Integer.parseInt( i[0] ), mat );
			}

			plugin.addRecipe( cr.getItem().getTypeId(), cr.getItem().getData().getData(), cr );

			return true;
		}

		return false;
	}

	private Material getMaterial( String key ) {
		Material mat = Material.getMaterial( key );

		if (mat == null) try {
			mat = Material.getMaterial( Integer.parseInt( key ) );
		} catch (Exception e) {
		}

		return mat;
	}
}
