package net.catharos.recipes;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.catharos.recipes.crafting.CustomRecipe;
import net.catharos.recipes.listener.BlockListener;
import net.catharos.recipes.listener.CraftListener;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.spaceemotion.updater.Updater;

public class cRecipes extends JavaPlugin implements Listener {
	public static boolean debug = false;
	private static cRecipes instance;

	private Updater updater;

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

		if (getConfig().getBoolean( "check-updates", true )) {
			this.updater = new Updater( this );
		}

		// Run async, to reduce lagg
		getServer().getScheduler().runTaskAsynchronously( this, new Runnable() {
			public void run() {
				try {
					Metrics metrics = new Metrics( getInstance() );
					metrics.start();
				} catch (IOException e) {
					getLogger().info( "cRecipes failed plugin metrics" );
				}
			}
		} );

		getDataFolder().mkdirs();

		recipes = new HashMap<Integer, Map<Byte, CustomRecipe>>();
		loader = new RecipeLoader( this );

		craftListener = new CraftListener( this );
		blockListener = new BlockListener( this );
	}

	@Override
	public void onDisable() {
		getServer().resetRecipes();
	}

	public static cRecipes getInstance() {
		return instance;
	}

	public Updater getUpdater() {
		return updater;
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
