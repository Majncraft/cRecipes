package net.catharos.recipes;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.catharos.recipes.crafting.CustomRecipe;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class cRecipes extends JavaPlugin implements Listener {
	protected Map<Integer, Map<Byte, CustomRecipe>> recipes;

	public void onEnable() {
		getDataFolder().mkdirs();

		recipes = new HashMap<Integer, Map<Byte, CustomRecipe>>();

		getServer().getPluginManager().registerEvents( this, this );
	}

	/**
	 * Returns a list of stored {@link CustomRecipe}s
	 * 
	 * @return A list of {@link CustomRecipe}s
	 */
	public Map<Integer, Map<Byte, CustomRecipe>> getRecipes() {
		return recipes;
	}

	public void addRecipe( int mat, byte data, CustomRecipe recipe ) {
		Map<Byte, CustomRecipe> map = getRecipes().get( mat );

		if (map == null) {
			map = new HashMap<Byte, CustomRecipe>();
			recipes.put( mat, map );
		}

		map.put( data, recipe );
	}

	@EventHandler
	public void e( BlockBreakEvent event ) {
		if (event.getPlayer().getGameMode() == GameMode.CREATIVE) return;

		Block block = event.getBlock();

		for (Entry<Integer, Map<Byte, CustomRecipe>> e : getRecipes().entrySet()) {
			if (e.getKey() != block.getTypeId()) continue;

			CustomRecipe cr = e.getValue().get( block.getData() );

			if (cr != null) {
				for (ItemStack drop : cr.getDrops())
					block.getWorld().dropItemNaturally( block.getLocation(), drop );

				block.setType( Material.AIR );
				event.setCancelled( true );
			}
		}
	}
}
