/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.catharos.recipes.util;

import java.util.logging.Level;

import net.catharos.recipes.MaterialEntry;
import net.catharos.recipes.cRecipes;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class MaterialUtil {

	public static MaterialEntry getMaterial( String str ) {
		String[] sl = str.split( ":" );

		if (sl.length == 0) return null;

		Material mat = Material.getMaterial( sl[0].toUpperCase() );

		if (mat == null) {
			try {
				mat = Material.getMaterial( Integer.parseInt( sl[0] ) );
			} catch (Exception e) {
				if (cRecipes.debug) {
					String msg = "Invalid Material: '" + sl[0] + "' (Invalid number or invalid material name!): " + e.getMessage();
					cRecipes.getInstance().getLogger().log( Level.WARNING, msg );
				}
			}
		}

		if (mat != null) {
			short data = 0;
			if (sl.length > 1) data = Short.parseShort( sl[1] );

			return new MaterialEntry( mat, data );
		}

		return null;
	}

	public static ItemStack getSingle( ItemStack i ) {
		ItemStack o = i.clone();
		o.setAmount( 1 );

		return o;
	}
}
