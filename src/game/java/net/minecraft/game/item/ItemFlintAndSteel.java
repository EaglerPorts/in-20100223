package net.minecraft.game.item;

import net.minecraft.game.level.World;
import net.minecraft.game.level.block.Block;

public final class ItemFlintAndSteel extends Item {
	public ItemFlintAndSteel(int var1) {
		super(3);
		this.maxStackSize = 1;
		this.maxDamage = 64;
	}

	public final boolean onItemUse(ItemStack var1, World var2, int var3, int var4, int var5, int var6) {
		if(var6 == 0) {
			--var4;
		}

		if(var6 == 1) {
			++var4;
		}

		if(var6 == 2) {
			--var5;
		}

		if(var6 == 3) {
			++var5;
		}

		if(var6 == 4) {
			--var3;
		}

		if(var6 == 5) {
			++var3;
		}

		if(var3 > 0 && var4 > 0 && var5 > 0 && var3 < var2.width - 1 && var4 < var2.height - 1 && var5 < var2.length - 1) {
			var6 = var2.getBlockId(var3, var4, var5);
			if(var6 == 0) {
				var2.playSoundAtPlayer((float)var3 + 0.5F, (float)var4 + 0.5F, (float)var5 + 0.5F, "fire.ignite", 1.0F, rand.nextFloat() * 0.4F + 0.8F);
				var2.setBlockWithNotify(var3, var4, var5, Block.fire.blockID);
			}

			var1.damageItem(1);
			return true;
		} else {
			return false;
		}
	}
}
