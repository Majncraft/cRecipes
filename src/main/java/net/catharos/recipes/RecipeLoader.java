package net.catharos.recipes;

import java.io.File;
import java.util.List;

import net.catharos.recipes.crafting.CustomShapedRecipe;
import net.catharos.recipes.crafting.CustomShapelessRecipe;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.java.JavaPlugin;

public class RecipeLoader {
	private enum RecipeType {
		SHAPED, SHAPELESS
	}

	public RecipeLoader( JavaPlugin plugin ) {
		File recFile = new File( plugin.getDataFolder(), "recipes.yml" );
		if (!recFile.exists()) return;

		FileConfiguration recipes = YamlConfiguration.loadConfiguration( recFile );

		// Shaped recipes
		ConfigurationSection shaped = recipes.getConfigurationSection( "shaped" );
		for (String key : shaped.getKeys( false ))
			if (shaped.isConfigurationSection( key )) this.loadRecipe( key, shaped.getConfigurationSection( key ), RecipeType.SHAPED );

		// Shapeless recipes
		ConfigurationSection shapeless = recipes.getConfigurationSection( "shapeless" );
		for (String key : shapeless.getKeys( false ))
			if (shapeless.isConfigurationSection( key )) this.loadRecipe( key, shapeless.getConfigurationSection( key ), RecipeType.SHAPED );

	}

	public boolean loadRecipe( String name, ConfigurationSection config, RecipeType type ) {
		// Output material
		String[] out = config.getString( "block" ).split( ":" );

		if (out.length != 2) return false;
		Material mat = Material.getMaterial( config.getString( out[0] ) );
		byte data = Byte.parseByte( out[1] );

		// Crafting amount
		int amount = config.getInt( "amount", 1 );

		// Add the recipe
		if (type == RecipeType.SHAPED)
			return addShaped( new CustomShapedRecipe( name, mat, amount, data ), config );
		else if (type == RecipeType.SHAPELESS)
			return addShapeless( new CustomShapelessRecipe( name, mat, amount, data ), config );
		else
			return false;
	}

	private boolean addShaped( CustomShapedRecipe cr, ConfigurationSection c ) {
		ShapedRecipe recipe = cr.getRecipe();
		ConfigurationSection rc = c.getConfigurationSection( "recipe" );

		if (rc != null) {
			ConfigurationSection ingredients = rc.getConfigurationSection( "ingedients" );
			for (String key : ingredients.getKeys( false )) {
				String[] i = ingredients.getString( key ).split( ":" );

				if (i.length > 1) {
					recipe.setIngredient( key.charAt( 0 ), Material.getMaterial( Integer.parseInt( i[0] ) ), Integer.parseInt( i[1] ) );
				} else if (i.length == 1) {
					recipe.setIngredient( key.charAt( 0 ), Material.getMaterial( Integer.parseInt( i[0] ) ) );
				}
			}

			List<String> shapeList = rc.getStringList( "shape" );
			recipe.shape( shapeList.toArray( new String[shapeList.size()] ) );

			return true;
		}

		return false;
	}

	private boolean addShapeless( CustomShapelessRecipe cr, ConfigurationSection c ) {
		ShapelessRecipe recipe = cr.getRecipe();
		ConfigurationSection rc = c.getConfigurationSection( "recipe" );

		if (rc != null) {
			for (String key : rc.getKeys( false )) {
				String[] i = rc.getString( key ).split( ":" );

				if (i.length > 1) {
					recipe.addIngredient( key.charAt( 0 ), Material.getMaterial( Integer.parseInt( i[0] ) ), Integer.parseInt( i[1] ) );
				} else if (i.length == 1) {
					recipe.addIngredient( key.charAt( 0 ), Material.getMaterial( Integer.parseInt( i[0] ) ) );
				}
			}

			return true;
		}

		return false;
	}
}
