package net.catharos.recipes;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.catharos.recipes.cmd.ReloadCommand;
import net.catharos.recipes.crafting.CustomRecipe;
import net.catharos.recipes.listener.BlockListener;
import net.catharos.recipes.listener.CraftListener;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class cRecipes extends JavaPlugin implements Listener {
	public static boolean debug = false;
	private static cRecipes instance;

	private CraftListener craftListener;
	private BlockListener blockListener;

	protected Map<Integer, Map<Byte, CustomRecipe>> recipes;
	protected RecipeLoader loader;

	@Override
	public void onEnable() {
		instance = this;

		getConfig().options().copyDefaults( true );
		this.saveConfig();

		debug = getConfig().getBoolean( "debug-output", true );

		getDataFolder().mkdirs();

		recipes = new HashMap<Integer, Map<Byte, CustomRecipe>>();
		reload( true );

		craftListener = new CraftListener( this );
		blockListener = new BlockListener( this );

		getCommand( "creload" ).setExecutor( new ReloadCommand( this ) );
	}

	@Override
	public void onDisable() {
		reload( false );
	}

	public void reload( boolean load ) {
		getServer().resetRecipes();
		if (load) loader = new RecipeLoader( this );
	}

	public static cRecipes getInstance() {
		return instance;
	}

	public CraftListener getCraftListener() {
		return craftListener;
	}

	public BlockListener getBlockListener() {
		return blockListener;
	}

	/**
	 * Returns a list of stored {@link CustomRecipe}s
	 * 
	 * @return A list of {@link CustomRecipe}s
	 */
	public Map<Integer, Map<Byte, CustomRecipe>> getRecipes() {
		return recipes;
	}

	public void addRecipe( CustomRecipe recipe ) {
		int mat = recipe.getItem().getTypeId();
		Map<Byte, CustomRecipe> map = getRecipes().get( mat );

		if (map == null) {
			map = new HashMap<Byte, CustomRecipe>();
			recipes.put( mat, map );
		}

		map.put( recipe.getItem().getData().getData(), recipe );
		getServer().addRecipe( recipe.getRecipe() );
	}

	public CustomRecipe getRecipe( int mat, short data ) {
		Map<Byte, CustomRecipe> stored = getRecipes().get( mat );

		if (stored != null)
			return stored.get( (byte) data );
		else
			return null;
	}

}
