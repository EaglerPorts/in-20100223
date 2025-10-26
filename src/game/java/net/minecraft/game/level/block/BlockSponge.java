package net.minecraft.game.level.block;

import net.minecraft.game.level.World;
import net.minecraft.game.level.material.Material;

public final class BlockSponge extends Block {
	protected BlockSponge(int var1) {
		super(19, Material.sponge);
		this.blockIndexInTexture = 48;
	}

	public final void onBlockAdded(World var1, int var2, int var3, int var4) {
		for(int var5 = var2 - 2; var5 <= var2 + 2; ++var5) {
			for(int var6 = var3 - 2; var6 <= var3 + 2; ++var6) {
				for(int var7 = var4 - 2; var7 <= var4 + 2; ++var7) {
					if(var1.isWater(var5, var6, var7)) {
						var1.setBlock(var5, var6, var7, 0);
					}
				}
			}
		}

	}

	public final void onBlockRemoval(World var1, int var2, int var3, int var4) {
		for(int var5 = var2 - 2; var5 <= var2 + 2; ++var5) {
			for(int var6 = var3 - 2; var6 <= var3 + 2; ++var6) {
				for(int var7 = var4 - 2; var7 <= var4 + 2; ++var7) {
					var1.notifyBlocksOfNeighborChange(var5, var6, var7, var1.getBlockId(var5, var6, var7));
				}
			}
		}

	}
}
