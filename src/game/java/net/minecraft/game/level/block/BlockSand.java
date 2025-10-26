package net.minecraft.game.level.block;

import java.util.Random;
import net.minecraft.game.level.World;
import net.minecraft.game.level.material.Material;

public class BlockSand extends Block {
	public BlockSand(int var1, int var2) {
		super(var1, var2, Material.sand);
		new Random();
	}

	public final void onBlockAdded(World var1, int var2, int var3, int var4) {
		this.tryToFall(var1, var2, var3, var4);
	}

	public final void onNeighborBlockChange(World var1, int var2, int var3, int var4, int var5) {
		this.tryToFall(var1, var2, var3, var4);
	}

	private void tryToFall(World var1, int var2, int var3, int var4) {
		int var5 = var3;

		while(true) {
			int var8 = var5 - 1;
			int var6 = var1.getBlockId(var2, var8, var4);
			boolean var10000;
			if(var6 == 0) {
				var10000 = true;
			} else if(var6 == Block.fire.blockID) {
				var10000 = true;
			} else {
				Material var10 = Block.blocksList[var6].material;
				var10000 = var10 == Material.water ? true : var10 == Material.lava;
			}

			if(!var10000 || var5 < 0) {
				if(var5 < 0) {
					var1.setTileNoUpdate(var2, var3, var4, 0);
				}

				if(var5 != var3) {
					var6 = var1.getBlockId(var2, var5, var4);
					if(var6 > 0 && Block.blocksList[var6].material != Material.air) {
						var1.setTileNoUpdate(var2, var5, var4, 0);
					}

					var1.swap(var2, var3, var4, var2, var5, var4);
				}

				return;
			}

			--var5;
			if(var1.getBlockId(var2, var5, var4) == Block.fire.blockID) {
				var1.setBlock(var2, var5, var4, 0);
			}
		}
	}
}
