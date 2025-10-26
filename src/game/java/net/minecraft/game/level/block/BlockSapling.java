package net.minecraft.game.level.block;

import java.util.Random;
import net.minecraft.game.level.World;

public final class BlockSapling extends BlockFlower {
	protected BlockSapling(int var1, int var2) {
		super(6, 15);
		this.setBlockBounds(10.0F * 0.01F, 0.0F, 10.0F * 0.01F, 0.9F, 0.8F, 0.9F);
	}

	public final void updateTick(World var1, int var2, int var3, int var4, Random var5) {
		super.updateTick(var1, var2, var3, var4, var5);
		if(var1.getBlockLightValue(var2, var3 + 1, var4) >= 9 && var5.nextInt(5) == 0) {
			byte var6 = var1.getBlockMetadata(var2, var3, var4);
			if(var6 < 15) {
				var1.setBlockMetadata(var2, var3, var4, var6 + 1);
				return;
			}

			var1.setTileNoUpdate(var2, var3, var4, 0);
			if(!var1.growTrees(var2, var3, var4)) {
				var1.setTileNoUpdate(var2, var3, var4, this.blockID);
			}
		}

	}
}
