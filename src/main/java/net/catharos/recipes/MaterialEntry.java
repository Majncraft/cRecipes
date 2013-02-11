package net.catharos.recipes;

import org.bukkit.Material;

public class MaterialEntry {
	/** The material */
	public Material material;

	/** The block / item data */
	public short data;

	public MaterialEntry( Material mat, short data ) {
		this.material = mat;
		this.data = data;
	}
}
