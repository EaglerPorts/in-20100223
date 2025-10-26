package net.minecraft.game.level.block;

import java.util.Random;
import net.minecraft.game.item.Item;

public final class BlockGravel extends BlockSand {
	public BlockGravel(int var1, int var2) {
		super(13, 19);
	}

	public final int idDropped(int var1, Random var2) {
		return var2.nextInt(10) == 0 ? Item.flint.shiftedIndex : this.blockID;
	}
}
