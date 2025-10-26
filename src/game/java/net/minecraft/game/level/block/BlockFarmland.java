package net.minecraft.game.level.block;

import java.util.Random;
import net.minecraft.game.level.World;
import net.minecraft.game.level.material.Material;
import net.minecraft.game.physics.AxisAlignedBB;

public final class BlockFarmland extends Block {
	protected BlockFarmland(int var1) {
		super(60, Material.ground);
		this.blockIndexInTexture = 87;
		this.setTickOnLoad(true);
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 15.0F / 16.0F, 1.0F);
		this.setLightOpacity(255);
	}

	public final AxisAlignedBB getCollisionBoundingBoxFromPool(int var1, int var2, int var3) {
		return new AxisAlignedBB((float)var1, (float)var2, (float)var3, (float)(var1 + 1), (float)(var2 + 1), (float)(var3 + 1));
	}

	public final boolean isOpaqueCube() {
		return false;
	}

	public final boolean renderAsNormalBlock() {
		return false;
	}

	public final int getBlockTextureFromSideAndMetadata(int var1, int var2) {
		return var1 == 1 && var2 > 0 ? this.blockIndexInTexture - 1 : (var1 == 1 ? this.blockIndexInTexture : 2);
	}

	public final void updateTick(World var1, int var2, int var3, int var4, Random var5) {
		if(var5.nextInt(5) == 0) {
			int var8 = var4;
			int var7 = var3;
			int var6 = var2;
			World var12 = var1;
			int var9 = var2 - 4;

			int var10;
			int var11;
			boolean var10000;
			label69:
			while(true) {
				if(var9 > var6 + 4) {
					var10000 = false;
					break;
				}

				for(var10 = var7; var10 <= var7 + 1; ++var10) {
					for(var11 = var8 - 4; var11 <= var8 + 4; ++var11) {
						if(var12.getBlockMaterial(var9, var10, var11) == Material.water) {
							var10000 = true;
							break label69;
						}
					}
				}

				++var9;
			}

			if(var10000) {
				var1.setBlockMetadata(var2, var3, var4, 7);
				return;
			}

			byte var13 = var1.getBlockMetadata(var2, var3, var4);
			if(var13 > 0) {
				var1.setBlockMetadata(var2, var3, var4, var13 - 1);
				return;
			}

			var8 = var4;
			var7 = var3;
			var6 = var2;
			var12 = var1;
			var10 = var2;

			label49:
			while(true) {
				if(var10 > var6) {
					var10000 = false;
					break;
				}

				for(var11 = var8; var11 <= var8; ++var11) {
					if(var12.getBlockId(var10, var7 + 1, var11) == Block.crops.blockID) {
						var10000 = true;
						break label49;
					}
				}

				++var10;
			}

			if(!var10000) {
				var1.setBlockWithNotify(var2, var3, var4, Block.dirt.blockID);
			}
		}

	}

	public final void onEntityWalking(World var1, int var2, int var3, int var4) {
		if(var1.random.nextInt(4) == 0) {
			var1.setBlockWithNotify(var2, var3, var4, Block.dirt.blockID);
		}

	}

	public final void onNeighborBlockChange(World var1, int var2, int var3, int var4, int var5) {
		super.onNeighborBlockChange(var1, var2, var3, var4, var5);
		Material var6 = var1.getBlockMaterial(var2, var3 + 1, var4);
		if(var6.isSolid()) {
			var1.setBlockWithNotify(var2, var3, var4, Block.dirt.blockID);
		}

	}

	public final int idDropped(int var1, Random var2) {
		return Block.dirt.idDropped(0, var2);
	}
}
