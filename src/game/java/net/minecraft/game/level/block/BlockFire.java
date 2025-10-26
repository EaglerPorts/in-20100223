package net.minecraft.game.level.block;

import java.util.Random;
import net.minecraft.game.level.World;
import net.minecraft.game.level.material.Material;
import net.minecraft.game.physics.AxisAlignedBB;

public final class BlockFire extends Block {
	private int[] chanceToEncourageFire = new int[256];
	private int[] abilityToCatchFire = new int[256];

	protected BlockFire(int var1, int var2) {
		super(51, 31, Material.fire);
		this.setBurnRate(Block.planks.blockID, 5, 20);
		this.setBurnRate(Block.wood.blockID, 5, 5);
		this.setBurnRate(Block.leaves.blockID, 30, 60);
		this.setBurnRate(Block.bookShelf.blockID, 30, 20);
		this.setBurnRate(Block.tnt.blockID, 15, 100);

		for(var1 = 0; var1 < 16; ++var1) {
			this.setBurnRate(Block.clothRed.blockID + var1, 30, 60);
		}

		this.setTickOnLoad(true);
	}

	private void setBurnRate(int var1, int var2, int var3) {
		this.chanceToEncourageFire[var1] = var2;
		this.abilityToCatchFire[var1] = var3;
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
		return 3;
	}

	public final int quantityDropped(Random var1) {
		return 0;
	}

	public final int tickRate() {
		return 20;
	}

	public final void updateTick(World var1, int var2, int var3, int var4, Random var5) {
		byte var6 = var1.getBlockMetadata(var2, var3, var4);
		if(var6 < 15) {
			var1.setBlockMetadata(var2, var3, var4, var6 + 1);
			var1.scheduleBlockUpdate(var2, var3, var4, this.blockID);
		}

		if(!this.canNeighborCatchFire(var1, var2, var3, var4)) {
			if(!var1.isBlockNormalCube(var2, var3 - 1, var4) || var6 > 3) {
				var1.setBlockWithNotify(var2, var3, var4, 0);
			}

		} else if(!this.canBlockCatchFire(var1, var2, var3 - 1, var4) && var6 == 15 && var5.nextInt(4) == 0) {
			var1.setBlockWithNotify(var2, var3, var4, 0);
		} else {
			if(var6 % 5 == 0 && var6 > 5) {
				this.tryToCatchBlockOnFire(var1, var2 + 1, var3, var4, 300, var5);
				this.tryToCatchBlockOnFire(var1, var2 - 1, var3, var4, 300, var5);
				this.tryToCatchBlockOnFire(var1, var2, var3 - 1, var4, 100, var5);
				this.tryToCatchBlockOnFire(var1, var2, var3 + 1, var4, 200, var5);
				this.tryToCatchBlockOnFire(var1, var2, var3, var4 - 1, 300, var5);
				this.tryToCatchBlockOnFire(var1, var2, var3, var4 + 1, 300, var5);

				for(int var16 = var2 - 1; var16 <= var2 + 1; ++var16) {
					for(int var7 = var4 - 1; var7 <= var4 + 1; ++var7) {
						for(int var8 = var3 - 1; var8 <= var3 + 4; ++var8) {
							if(var16 != var2 || var8 != var3 || var7 != var4) {
								int var9 = 100;
								if(var8 > var3 + 1) {
									var9 = 100 + (var8 - (var3 + 1)) * 100;
								}

								int var10000;
								if(var1.getBlockId(var16, var8, var7) != 0) {
									var10000 = 0;
								} else {
									int var15 = this.getChanceToEncourageFire(var1, var16 + 1, var8, var7, 0);
									var15 = this.getChanceToEncourageFire(var1, var16 - 1, var8, var7, var15);
									var15 = this.getChanceToEncourageFire(var1, var16, var8 - 1, var7, var15);
									var15 = this.getChanceToEncourageFire(var1, var16, var8 + 1, var7, var15);
									var15 = this.getChanceToEncourageFire(var1, var16, var8, var7 - 1, var15);
									var15 = this.getChanceToEncourageFire(var1, var16, var8, var7 + 1, var15);
									var10000 = var15;
								}

								int var10 = var10000;
								if(var10 > 0 && var5.nextInt(var9) <= var10) {
									var1.setBlockWithNotify(var16, var8, var7, this.blockID);
								}
							}
						}
					}
				}
			}

		}
	}

	private void tryToCatchBlockOnFire(World var1, int var2, int var3, int var4, int var5, Random var6) {
		int var7 = this.abilityToCatchFire[var1.getBlockId(var2, var3, var4)];
		if(var6.nextInt(var5) < var7) {
			boolean var8 = var1.getBlockId(var2, var3, var4) == Block.tnt.blockID;
			if(var6.nextInt(2) == 0) {
				var1.setBlockWithNotify(var2, var3, var4, this.blockID);
			} else {
				var1.setBlockWithNotify(var2, var3, var4, 0);
			}

			if(var8) {
				Block.tnt.onBlockDestroyedByPlayer(var1, var2, var3, var4, 0);
			}
		}

	}

	private boolean canNeighborCatchFire(World var1, int var2, int var3, int var4) {
		return this.canBlockCatchFire(var1, var2 + 1, var3, var4) ? true : (this.canBlockCatchFire(var1, var2 - 1, var3, var4) ? true : (this.canBlockCatchFire(var1, var2, var3 - 1, var4) ? true : (this.canBlockCatchFire(var1, var2, var3 + 1, var4) ? true : (this.canBlockCatchFire(var1, var2, var3, var4 - 1) ? true : this.canBlockCatchFire(var1, var2, var3, var4 + 1)))));
	}

	public final boolean isCollidable() {
		return false;
	}

	public final boolean canBlockCatchFire(World var1, int var2, int var3, int var4) {
		return this.chanceToEncourageFire[var1.getBlockId(var2, var3, var4)] > 0;
	}

	private int getChanceToEncourageFire(World var1, int var2, int var3, int var4, int var5) {
		int var6 = this.chanceToEncourageFire[var1.getBlockId(var2, var3, var4)];
		return var6 > var5 ? var6 : var5;
	}

	public final boolean canPlaceBlockAt(World var1, int var2, int var3, int var4) {
		return var1.isBlockNormalCube(var2, var3 - 1, var4) || this.canNeighborCatchFire(var1, var2, var3, var4);
	}

	public final void onNeighborBlockChange(World var1, int var2, int var3, int var4, int var5) {
		if(!var1.isBlockNormalCube(var2, var3 - 1, var4) && !this.canNeighborCatchFire(var1, var2, var3, var4)) {
			var1.setBlockWithNotify(var2, var3, var4, 0);
		}
	}

	public final void onBlockAdded(World var1, int var2, int var3, int var4) {
		if(!var1.isBlockNormalCube(var2, var3 - 1, var4) && !this.canNeighborCatchFire(var1, var2, var3, var4)) {
			var1.setBlockWithNotify(var2, var3, var4, 0);
		} else {
			var1.scheduleBlockUpdate(var2, var3, var4, this.blockID);
		}
	}

	public final boolean getChanceOfNeighborsEncouragingFire(int var1) {
		return this.chanceToEncourageFire[var1] > 0;
	}

	public final void fireSpread(World var1, int var2, int var3, int var4) {
		boolean var5 = false;
		var5 = fireCheck(var1, var2, var3 + 1, var4);
		if(!var5) {
			var5 = fireCheck(var1, var2 - 1, var3, var4);
		}

		if(!var5) {
			var5 = fireCheck(var1, var2 + 1, var3, var4);
		}

		if(!var5) {
			var5 = fireCheck(var1, var2, var3, var4 - 1);
		}

		if(!var5) {
			var5 = fireCheck(var1, var2, var3, var4 + 1);
		}

		if(!var5) {
			var5 = fireCheck(var1, var2, var3 - 1, var4);
		}

		if(!var5) {
			var1.setBlockWithNotify(var2, var3, var4, Block.fire.blockID);
		}

	}

	public final void randomDisplayTick(World var1, int var2, int var3, int var4, Random var5) {
		if(var5.nextInt(24) == 0) {
			var1.playSoundAtPlayer((float)var2 + 0.5F, (float)var3 + 0.5F, (float)var4 + 0.5F, "fire.fire", 1.0F + var5.nextFloat(), var5.nextFloat() * 0.7F + 0.3F);
		}

		int var6;
		float var7;
		float var8;
		float var9;
		if(!var1.isBlockNormalCube(var2, var3 - 1, var4) && !Block.fire.canBlockCatchFire(var1, var2, var3 - 1, var4)) {
			if(Block.fire.canBlockCatchFire(var1, var2 - 1, var3, var4)) {
				for(var6 = 0; var6 < 2; ++var6) {
					var7 = (float)var2 + var5.nextFloat() * 0.1F;
					var8 = (float)var3 + var5.nextFloat();
					var9 = (float)var4 + var5.nextFloat();
					var1.spawnParticle("largesmoke", var7, var8, var9, 0.0F, 0.0F, 0.0F);
				}
			}

			if(Block.fire.canBlockCatchFire(var1, var2 + 1, var3, var4)) {
				for(var6 = 0; var6 < 2; ++var6) {
					var7 = (float)(var2 + 1) - var5.nextFloat() * 0.1F;
					var8 = (float)var3 + var5.nextFloat();
					var9 = (float)var4 + var5.nextFloat();
					var1.spawnParticle("largesmoke", var7, var8, var9, 0.0F, 0.0F, 0.0F);
				}
			}

			if(Block.fire.canBlockCatchFire(var1, var2, var3, var4 - 1)) {
				for(var6 = 0; var6 < 2; ++var6) {
					var7 = (float)var2 + var5.nextFloat();
					var8 = (float)var3 + var5.nextFloat();
					var9 = (float)var4 + var5.nextFloat() * 0.1F;
					var1.spawnParticle("largesmoke", var7, var8, var9, 0.0F, 0.0F, 0.0F);
				}
			}

			if(Block.fire.canBlockCatchFire(var1, var2, var3, var4 + 1)) {
				for(var6 = 0; var6 < 2; ++var6) {
					var7 = (float)var2 + var5.nextFloat();
					var8 = (float)var3 + var5.nextFloat();
					var9 = (float)(var4 + 1) - var5.nextFloat() * 0.1F;
					var1.spawnParticle("largesmoke", var7, var8, var9, 0.0F, 0.0F, 0.0F);
				}
			}

			if(Block.fire.canBlockCatchFire(var1, var2, var3 + 1, var4)) {
				for(var6 = 0; var6 < 2; ++var6) {
					var7 = (float)var2 + var5.nextFloat();
					var8 = (float)(var3 + 1) - var5.nextFloat() * 0.1F;
					var9 = (float)var4 + var5.nextFloat();
					var1.spawnParticle("largesmoke", var7, var8, var9, 0.0F, 0.0F, 0.0F);
				}
			}

		} else {
			for(var6 = 0; var6 < 3; ++var6) {
				var7 = (float)var2 + var5.nextFloat();
				var8 = (float)var3 + var5.nextFloat() * 0.5F + 0.5F;
				var9 = (float)var4 + var5.nextFloat();
				var1.spawnParticle("largesmoke", var7, var8, var9, 0.0F, 0.0F, 0.0F);
			}

		}
	}

	private static boolean fireCheck(World var0, int var1, int var2, int var3) {
		int var4 = var0.getBlockId(var1, var2, var3);
		if(var4 == Block.fire.blockID) {
			return true;
		} else if(var4 == 0) {
			var0.setBlockWithNotify(var1, var2, var3, Block.fire.blockID);
			return true;
		} else {
			return false;
		}
	}
}
