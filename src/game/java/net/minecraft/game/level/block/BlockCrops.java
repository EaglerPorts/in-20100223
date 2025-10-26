package net.minecraft.game.level.block;

import java.util.Random;
import net.minecraft.game.entity.misc.EntityItem;
import net.minecraft.game.item.Item;
import net.minecraft.game.item.ItemStack;
import net.minecraft.game.level.World;

public final class BlockCrops extends BlockFlower {
	protected BlockCrops(int var1, int var2) {
		super(59, 88);
		this.blockIndexInTexture = 88;
		this.setTickOnLoad(true);
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.25F, 1.0F);
	}

	protected final boolean canThisPlantGrowOnThisBlockID(int var1) {
		return var1 == Block.tilledField.blockID;
	}

	public final void updateTick(World var1, int var2, int var3, int var4, Random var5) {
		super.updateTick(var1, var2, var3, var4, var5);
		if(var1.getBlockLightValue(var2, var3 + 1, var4) >= 9) {
			byte var6 = var1.getBlockMetadata(var2, var3, var4);
			if(var6 < 7) {
				int var11 = var4;
				int var10 = var3;
				int var9 = var2;
				World var8 = var1;
				float var12 = 1.0F;
				int var13 = var1.getBlockId(var2, var3, var4 - 1);
				int var14 = var1.getBlockId(var2, var3, var4 + 1);
				int var15 = var1.getBlockId(var2 - 1, var3, var4);
				int var16 = var1.getBlockId(var2 + 1, var3, var4);
				int var17 = var1.getBlockId(var2 - 1, var3, var4 - 1);
				int var18 = var1.getBlockId(var2 + 1, var3, var4 - 1);
				int var19 = var1.getBlockId(var2 + 1, var3, var4 + 1);
				int var20 = var1.getBlockId(var2 - 1, var3, var4 + 1);
				boolean var23 = var15 == this.blockID || var16 == this.blockID;
				boolean var22 = var13 == this.blockID || var14 == this.blockID;
				boolean var7 = var17 == this.blockID || var18 == this.blockID || var19 == this.blockID || var20 == this.blockID;

				for(var14 = var2 - 1; var14 <= var9 + 1; ++var14) {
					for(var16 = var11 - 1; var16 <= var11 + 1; ++var16) {
						var17 = var8.getBlockId(var14, var10 - 1, var16);
						float var24 = 0.0F;
						if(var17 == Block.tilledField.blockID) {
							var24 = 1.0F;
							if(var8.getBlockMetadata(var14, var10 - 1, var16) > 0) {
								var24 = 3.0F;
							}
						}

						if(var14 != var9 || var16 != var11) {
							var24 /= 4.0F;
						}

						var12 += var24;
					}
				}

				if(var7 || var23 && var22) {
					var12 /= 2.0F;
				}

				if(var5.nextInt((int)(100.0F / var12)) == 0) {
					int var21 = var6 + 1;
					var1.setBlockMetadata(var2, var3, var4, var21);
				}
			}
		}

	}

	public final int getBlockTextureFromSideAndMetadata(int var1, int var2) {
		if(var2 < 0) {
			var2 = 7;
		}

		return this.blockIndexInTexture + var2;
	}

	public final int getRenderType() {
		return 6;
	}

	public final void onBlockDestroyedByPlayer(World var1, int var2, int var3, int var4, int var5) {
		super.onBlockDestroyedByPlayer(var1, var2, var3, var4, var5);

		for(int var6 = 0; var6 < 3; ++var6) {
			if(var1.random.nextInt(15) <= var5) {
				float var7 = var1.random.nextFloat() * 0.7F + 0.15F;
				float var8 = var1.random.nextFloat() * 0.7F + 0.15F;
				float var9 = var1.random.nextFloat() * 0.7F + 0.15F;
				EntityItem var10 = new EntityItem(var1, (float)var2 + var7, (float)var3 + var8, (float)var4 + var9, new ItemStack(Item.seeds));
				var10.delayBeforeCanPickup = 10;
				var1.spawnEntityInWorld(var10);
			}
		}

	}

	public final int idDropped(int var1, Random var2) {
		System.out.println("Get resource: " + var1);
		return var1 == 7 ? Item.wheat.shiftedIndex : -1;
	}

	public final int quantityDropped(Random var1) {
		return 1;
	}
}
