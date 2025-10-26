package net.minecraft.game.level.block;

import java.util.Random;
import net.minecraft.game.level.material.Material;

public final class BlockGlass extends BlockBreakable {
	public BlockGlass(int var1, int var2, Material var3, boolean var4) {
		super(20, 49, var3, false);
	}

	public final int quantityDropped(Random var1) {
		return 0;
	}
}
