package net.catharos.recipes.util;

import java.util.HashMap;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class InventoryUtil {
	public static void givePlayer( Player player, List<ItemStack> items ) {
		givePlayer( player, (ItemStack[]) items.toArray( new ItemStack[items.size()] ) );
	}

	public static void givePlayer( Player player, ItemStack... items ) {
		HashMap<Integer, ItemStack> overflow = player.getInventory().addItem( items );

		for (ItemStack item : overflow.values()) {
			player.getWorld().dropItemNaturally( player.getLocation(), item );
		}
	}
}
