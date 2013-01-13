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
			if (shaped.isConfigurationSection( key )) this.loadRecipe( key, shaped.getConfigurationSection( key ), true );

		// Shapeless recipes
		ConfigurationSection shapeless = recipes.getConfigurationSection( "shapeless" );
		for (String key : shapeless.getKeys( false ))
			if (shapeless.isConfigurationSection( key )) this.loadRecipe( key, shapeless.getConfigurationSection( key ), false );

	}

	public boolean loadRecipe( String name, ConfigurationSection config, boolean shaped ) {
		// Output material
		String[] out = config.getString( "block" ).split( ":" );

		if (out.length != 2) return false;
		Material mat = Material.getMaterial( config.getString( out[0] ) );
		byte data = Byte.parseByte( out[1] );

		// Crafting amount
		int amount = config.getInt( "amount", 1 );

		// Add the recipe
		CustomRecipe cr;

		if (shaped) {
			cr = new CustomShapedRecipe( name, mat, amount, data );
			if (!addShaped( (CustomShapedRecipe) cr, config )) return false;
		} else {
			cr = new CustomShapelessRecipe( name, mat, amount, data );
			if (!addShapeless( (CustomShapelessRecipe) cr, config )) return false;
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
		return true;
	}

	private boolean addShaped( CustomShapedRecipe cr, ConfigurationSection c ) {
		ShapedRecipe recipe = cr.getRecipe();
		ConfigurationSection rc = c.getConfigurationSection( "recipe" );

		if (rc != null) {
			ConfigurationSection ingredients = rc.getConfigurationSection( "ingredients" );
			for (String key : ingredients.getKeys( false )) {
				String[] i = ingredients.getString( key ).split( ":" );

				if (i.length > 1)
					recipe.setIngredient( key.charAt( 0 ), Material.getMaterial( Integer.parseInt( i[0] ) ), Integer.parseInt( i[1] ) );
				else
					recipe.setIngredient( key.charAt( 0 ), Material.getMaterial( Integer.parseInt( i[0] ) ) );
			}

			List<String> shapeList = rc.getStringList( "shape" );
			recipe.shape( shapeList.toArray( new String[shapeList.size()] ) );

			plugin.addRecipe( cr.getItem().getTypeId(), cr.getItem().getData().getData(), cr );

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

				if (i.length > 1)
					recipe.addIngredient( key.charAt( 0 ), Material.getMaterial( Integer.parseInt( i[0] ) ), Integer.parseInt( i[1] ) );
				else
					recipe.addIngredient( key.charAt( 0 ), Material.getMaterial( Integer.parseInt( i[0] ) ) );
			}

			plugin.addRecipe( cr.getItem().getTypeId(), cr.getItem().getData().getData(), cr );

			return true;
		}

		return false;
	}
}
