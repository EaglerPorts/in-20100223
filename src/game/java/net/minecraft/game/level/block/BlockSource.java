package net.minecraft.game.level.block;

import java.util.Random;
import net.minecraft.game.level.World;
import net.minecraft.game.level.material.Material;

public final class BlockSource extends Block {
	private int fluid;

	protected BlockSource(int var1, int var2) {
		super(var1, Block.blocksList[var2].blockIndexInTexture, Material.water);
		this.fluid = var2;
		this.setTickOnLoad(true);
	}

	public final void onBlockAdded(World var1, int var2, int var3, int var4) {
		super.onBlockAdded(var1, var2, var3, var4);
		if(var1.getBlockId(var2 - 1, var3, var4) == 0) {
			var1.setBlockWithNotify(var2 - 1, var3, var4, this.fluid);
		}

		if(var1.getBlockId(var2 + 1, var3, var4) == 0) {
			var1.setBlockWithNotify(var2 + 1, var3, var4, this.fluid);
		}

		if(var1.getBlockId(var2, var3, var4 - 1) == 0) {
			var1.setBlockWithNotify(var2, var3, var4 - 1, this.fluid);
		}

		if(var1.getBlockId(var2, var3, var4 + 1) == 0) {
			var1.setBlockWithNotify(var2, var3, var4 + 1, this.fluid);
		}

	}

	public final void updateTick(World var1, int var2, int var3, int var4, Random var5) {
		super.updateTick(var1, var2, var3, var4, var5);
		if(var1.getBlockId(var2 - 1, var3, var4) == 0) {
			var1.setBlockWithNotify(var2 - 1, var3, var4, this.fluid);
		}

		if(var1.getBlockId(var2 + 1, var3, var4) == 0) {
			var1.setBlockWithNotify(var2 + 1, var3, var4, this.fluid);
		}

		if(var1.getBlockId(var2, var3, var4 - 1) == 0) {
			var1.setBlockWithNotify(var2, var3, var4 - 1, this.fluid);
		}

		if(var1.getBlockId(var2, var3, var4 + 1) == 0) {
			var1.setBlockWithNotify(var2, var3, var4 + 1, this.fluid);
		}

	}
}
