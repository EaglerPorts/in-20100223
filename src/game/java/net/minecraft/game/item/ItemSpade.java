package net.minecraft.game.item;

import net.minecraft.game.level.block.Block;

public final class ItemSpade extends ItemTool {
	private static Block[] blocksEffectiveAgainst = new Block[]{Block.grass, Block.dirt, Block.sand, Block.gravel};

	public ItemSpade(int var1, int var2) {
		super(var1, 1, var2, blocksEffectiveAgainst);
	}
}
