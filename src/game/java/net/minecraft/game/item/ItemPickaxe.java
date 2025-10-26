package net.minecraft.game.item;

import net.minecraft.game.level.block.Block;
import net.minecraft.game.level.material.Material;

public final class ItemPickaxe extends ItemTool {
	private static Block[] blocksEffectiveAgainst = new Block[]{Block.cobblestone, Block.stairDouble, Block.stairSingle, Block.stone, Block.cobblestoneMossy, Block.oreIron, Block.blockSteel, Block.oreCoal, Block.blockGold, Block.oreGold, Block.oreDiamond, Block.blockDiamond};
	private int harvestLevel;

	public ItemPickaxe(int var1, int var2) {
		super(var1, 2, var2, blocksEffectiveAgainst);
		this.harvestLevel = var2;
	}

	public final boolean canHarvestBlock(Block var1) {
		return var1 == Block.obsidian ? this.harvestLevel == 3 : (var1 != Block.blockDiamond && var1 != Block.oreDiamond ? (var1 != Block.blockGold && var1 != Block.oreGold ? (var1 != Block.blockSteel && var1 != Block.oreIron ? (var1.material == Material.rock ? true : var1.material == Material.iron) : this.harvestLevel > 0) : this.harvestLevel >= 2) : this.harvestLevel >= 2);
	}
}
