package net.minecraft.game.level.block;

import java.util.Random;
import net.minecraft.game.level.World;
import net.minecraft.game.level.material.Material;
import net.minecraft.game.physics.AxisAlignedBB;
import net.minecraft.game.physics.MovingObjectPosition;
import net.minecraft.game.physics.Vec3D;

public final class BlockTorch extends Block {
	protected BlockTorch(int var1, int var2) {
		super(50, 80, Material.circuits);
		this.setTickOnLoad(true);
	}

	public final AxisAlignedBB getCollisionBoundingBoxFromPool(int var1, int var2, int var3) {
		return null;
	}

	public final boolean isOpaqueCube() {
		return false;
	}

	public final boolean renderAsNormalBlock() {
		return false;
	}

	public final int getRenderType() {
		return 2;
	}

	public final boolean canPlaceBlockAt(World var1, int var2, int var3, int var4) {
		return var1.isBlockNormalCube(var2 - 1, var3, var4) ? true : (var1.isBlockNormalCube(var2 + 1, var3, var4) ? true : (var1.isBlockNormalCube(var2, var3, var4 - 1) ? true : (var1.isBlockNormalCube(var2, var3, var4 + 1) ? true : var1.isBlockNormalCube(var2, var3 - 1, var4))));
	}

	public final void onBlockPlaced(World var1, int var2, int var3, int var4, int var5) {
		byte var6 = var1.getBlockMetadata(var2, var3, var4);
		if(var5 == 1 && var1.isBlockNormalCube(var2, var3 - 1, var4)) {
			var6 = 5;
		}

		if(var5 == 2 && var1.isBlockNormalCube(var2, var3, var4 + 1)) {
			var6 = 4;
		}

		if(var5 == 3 && var1.isBlockNormalCube(var2, var3, var4 - 1)) {
			var6 = 3;
		}

		if(var5 == 4 && var1.isBlockNormalCube(var2 + 1, var3, var4)) {
			var6 = 2;
		}

		if(var5 == 5 && var1.isBlockNormalCube(var2 - 1, var3, var4)) {
			var6 = 1;
		}

		var1.setBlockMetadata(var2, var3, var4, var6);
	}

	public final void updateTick(World var1, int var2, int var3, int var4, Random var5) {
		super.updateTick(var1, var2, var3, var4, var5);
		if(var1.getBlockMetadata(var2, var3, var4) == 0) {
			this.onBlockAdded(var1, var2, var3, var4);
		}

	}

	public final void onBlockAdded(World var1, int var2, int var3, int var4) {
		if(var1.isBlockNormalCube(var2 - 1, var3, var4)) {
			var1.setBlockMetadata(var2, var3, var4, 1);
		} else if(var1.isBlockNormalCube(var2 + 1, var3, var4)) {
			var1.setBlockMetadata(var2, var3, var4, 2);
		} else if(var1.isBlockNormalCube(var2, var3, var4 - 1)) {
			var1.setBlockMetadata(var2, var3, var4, 3);
		} else if(var1.isBlockNormalCube(var2, var3, var4 + 1)) {
			var1.setBlockMetadata(var2, var3, var4, 4);
		} else if(var1.isBlockNormalCube(var2, var3 - 1, var4)) {
			var1.setBlockMetadata(var2, var3, var4, 5);
		}

		this.dropTorchIfCantStay(var1, var2, var3, var4);
	}

	public final void onNeighborBlockChange(World var1, int var2, int var3, int var4, int var5) {
		if(this.dropTorchIfCantStay(var1, var2, var3, var4)) {
			byte var7 = var1.getBlockMetadata(var2, var3, var4);
			boolean var6 = false;
			if(!var1.isBlockNormalCube(var2 - 1, var3, var4) && var7 == 1) {
				var6 = true;
			}

			if(!var1.isBlockNormalCube(var2 + 1, var3, var4) && var7 == 2) {
				var6 = true;
			}

			if(!var1.isBlockNormalCube(var2, var3, var4 - 1) && var7 == 3) {
				var6 = true;
			}

			if(!var1.isBlockNormalCube(var2, var3, var4 + 1) && var7 == 4) {
				var6 = true;
			}

			if(!var1.isBlockNormalCube(var2, var3 - 1, var4) && var7 == 5) {
				var6 = true;
			}

			if(var6) {
				this.dropBlockAsItem(var1, var2, var3, var4, var1.getBlockMetadata(var2, var3, var4));
				var1.setBlockWithNotify(var2, var3, var4, 0);
			}
		}

	}

	private boolean dropTorchIfCantStay(World var1, int var2, int var3, int var4) {
		if(!this.canPlaceBlockAt(var1, var2, var3, var4)) {
			this.dropBlockAsItem(var1, var2, var3, var4, var1.getBlockMetadata(var2, var3, var4));
			var1.setBlockWithNotify(var2, var3, var4, 0);
			return false;
		} else {
			return true;
		}
	}

	public final MovingObjectPosition collisionRayTrace(World var1, int var2, int var3, int var4, Vec3D var5, Vec3D var6) {
		byte var7 = var1.getBlockMetadata(var2, var3, var4);
		if(var7 == 1) {
			this.setBlockBounds(0.0F, 0.2F, 0.35F, 0.3F, 0.8F, 0.65F);
		} else if(var7 == 2) {
			this.setBlockBounds(0.7F, 0.2F, 0.35F, 1.0F, 0.8F, 0.65F);
		} else if(var7 == 3) {
			this.setBlockBounds(0.35F, 0.2F, 0.0F, 0.65F, 0.8F, 0.3F);
		} else if(var7 == 4) {
			this.setBlockBounds(0.35F, 0.2F, 0.7F, 0.65F, 0.8F, 1.0F);
		} else {
			this.setBlockBounds(0.4F, 0.0F, 0.4F, 0.6F, 0.6F, 0.6F);
		}

		return super.collisionRayTrace(var1, var2, var3, var4, var5, var6);
	}

	public final void randomDisplayTick(World var1, int var2, int var3, int var4, Random var5) {
		byte var9 = var1.getBlockMetadata(var2, var3, var4);
		float var6 = (float)var2 + 0.5F;
		float var7 = (float)var3 + 0.7F;
		float var8 = (float)var4 + 0.5F;
		if(var9 == 1) {
			var1.spawnParticle("smoke", var6 - 0.27F, var7 + 0.22F, var8, 0.0F, 0.0F, 0.0F);
			var1.spawnParticle("flame", var6 - 0.27F, var7 + 0.22F, var8, 0.0F, 0.0F, 0.0F);
		} else if(var9 == 2) {
			var1.spawnParticle("smoke", var6 + 0.27F, var7 + 0.22F, var8, 0.0F, 0.0F, 0.0F);
			var1.spawnParticle("flame", var6 + 0.27F, var7 + 0.22F, var8, 0.0F, 0.0F, 0.0F);
		} else if(var9 == 3) {
			var1.spawnParticle("smoke", var6, var7 + 0.22F, var8 - 0.27F, 0.0F, 0.0F, 0.0F);
			var1.spawnParticle("flame", var6, var7 + 0.22F, var8 - 0.27F, 0.0F, 0.0F, 0.0F);
		} else if(var9 == 4) {
			var1.spawnParticle("smoke", var6, var7 + 0.22F, var8 + 0.27F, 0.0F, 0.0F, 0.0F);
			var1.spawnParticle("flame", var6, var7 + 0.22F, var8 + 0.27F, 0.0F, 0.0F, 0.0F);
		} else {
			var1.spawnParticle("smoke", var6, var7, var8, 0.0F, 0.0F, 0.0F);
			var1.spawnParticle("flame", var6, var7, var8, 0.0F, 0.0F, 0.0F);
		}
	}
}
