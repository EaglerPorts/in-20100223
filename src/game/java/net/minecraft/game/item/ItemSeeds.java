package net.minecraft.game.item;

import net.minecraft.game.level.World;
import net.minecraft.game.level.block.Block;

public final class ItemSeeds extends Item {
	private int blockType;

	public ItemSeeds(int var1, int var2) {
		super(39);
		this.blockType = var2;
	}

	public final boolean onItemUse(ItemStack var1, World var2, int var3, int var4, int var5, int var6) {
		if(var6 != 1) {
			return false;
		} else if(var3 > 0 && var4 > 0 && var5 > 0 && var3 < var2.width - 1 && var4 < var2.height - 1 && var5 < var2.length - 1) {
			var6 = var2.getBlockId(var3, var4, var5);
			if(var6 == Block.tilledField.blockID) {
				var2.setBlockWithNotify(var3, var4 + 1, var5, this.blockType);
				--var1.stackSize;
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
}
