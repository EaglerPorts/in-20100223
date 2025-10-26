package net.minecraft.game.level.block;

import java.util.Random;
import net.minecraft.game.level.material.Material;

public final class BlockLog extends Block {
	protected BlockLog(int var1) {
		super(17, Material.wood);
		this.blockIndexInTexture = 20;
	}

	public final int quantityDropped(Random var1) {
		return 1;
	}

	public final int idDropped(int var1, Random var2) {
		return Block.wood.blockID;
	}

	public final int getBlockTextureFromSide(int var1) {
		return var1 == 1 ? 21 : (var1 == 0 ? 21 : 20);
	}
}
