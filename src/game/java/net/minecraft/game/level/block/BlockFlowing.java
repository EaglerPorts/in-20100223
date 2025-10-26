package net.minecraft.game.level.block;

import java.util.Random;
import net.minecraft.game.level.World;
import net.minecraft.game.level.material.Material;
import net.minecraft.game.physics.AxisAlignedBB;

public final class BlockFlowing extends BlockFluid {
	private int stillId1;
	private int movingId1;
	private Random rand = new Random();
	private int[] liquidIntArray = new int[]{0, 1, 2, 3};

	protected BlockFlowing(int var1, Material var2) {
		super(var1, var2);
		this.blockIndexInTexture = 14;
		if(var2 == Material.lava) {
			this.blockIndexInTexture = 30;
		}

		Block.isBlockFluid[var1] = true;
		this.movingId1 = var1;
		this.stillId1 = var1 + 1;
		this.setBlockBounds(0.01F, -0.09F, 0.01F, 1.01F, 0.90999997F, 1.01F);
		this.setTickOnLoad(true);
	}

	public final void onBlockAdded(World var1, int var2, int var3, int var4) {
		var1.scheduleBlockUpdate(var2, var3, var4, this.movingId1);
	}

	public final void updateTick(World var1, int var2, int var3, int var4, Random var5) {
		this.update(var1, var2, var3, var4, 0);
	}

	public final boolean update(World var1, int var2, int var3, int var4, int var5) {
		boolean var10 = false;
		boolean var6 = this.canFlow(var1, var2 - 1, var3, var4) || this.canFlow(var1, var2 + 1, var3, var4) || this.canFlow(var1, var2, var3, var4 - 1) || this.canFlow(var1, var2, var3, var4 + 1);
		int var7;
		int var8;
		int var9;
		if(var6 && var1.getBlockMaterial(var2, var3 - 1, var4) == this.material) {
			var5 = var1.floodFill(var2, var3 - 1, var4, this.movingId1, this.stillId1);
			if(var5 == 1) {
				var7 = var1.fluidFlowCheck(var2, var3, var4, this.movingId1, this.stillId1);
				if(var7 != -9999) {
					if(var7 < 0) {
						return false;
					}

					var8 = var7 % 1024;
					var7 >>= 10;
					var9 = var7 % 1024;
					var7 >>= 10;
					var2 = var7 % 1024;
					var1.setBlockWithNotify(var8, var2, var9, 0);
					return false;
				}

				return false;
			}
		}

		var10 = this.liquidSpread2(var1, var2, var3, var4, var2, var3 - 1, var4);

		for(var7 = 0; var7 < 4; ++var7) {
			var8 = this.rand.nextInt(4 - var7) + var7;
			var9 = this.liquidIntArray[var7];
			this.liquidIntArray[var7] = this.liquidIntArray[var8];
			this.liquidIntArray[var8] = var9;
			if(this.liquidIntArray[var7] == 0 && !var10) {
				var10 = this.liquidSpread2(var1, var2, var3, var4, var2 - 1, var3, var4);
			}

			if(this.liquidIntArray[var7] == 1 && !var10) {
				var10 = this.liquidSpread2(var1, var2, var3, var4, var2 + 1, var3, var4);
			}

			if(this.liquidIntArray[var7] == 2 && !var10) {
				var10 = this.liquidSpread2(var1, var2, var3, var4, var2, var3, var4 - 1);
			}

			if(this.liquidIntArray[var7] == 3 && !var10) {
				var10 = this.liquidSpread2(var1, var2, var3, var4, var2, var3, var4 + 1);
			}
		}

		if(!var10 && var6) {
			if(this.rand.nextInt(3) == 0) {
				if(this.rand.nextInt(3) == 0) {
					var10 = false;

					for(var7 = 0; var7 < 4; ++var7) {
						var8 = this.rand.nextInt(4 - var7) + var7;
						var9 = this.liquidIntArray[var7];
						this.liquidIntArray[var7] = this.liquidIntArray[var8];
						this.liquidIntArray[var8] = var9;
						if(this.liquidIntArray[var7] == 0 && !var10) {
							var10 = this.liquidSpread(var1, var2, var3, var4, var2 - 1, var3, var4);
						}

						if(this.liquidIntArray[var7] == 1 && !var10) {
							var10 = this.liquidSpread(var1, var2, var3, var4, var2 + 1, var3, var4);
						}

						if(this.liquidIntArray[var7] == 2 && !var10) {
							var10 = this.liquidSpread(var1, var2, var3, var4, var2, var3, var4 - 1);
						}

						if(this.liquidIntArray[var7] == 3 && !var10) {
							var10 = this.liquidSpread(var1, var2, var3, var4, var2, var3, var4 + 1);
						}
					}
				} else if(this.material == Material.lava) {
					var1.setBlockWithNotify(var2, var3, var4, Block.stone.blockID);
				} else {
					var1.setBlockWithNotify(var2, var3, var4, 0);
				}
			}

			return false;
		} else {
			if(this.material == Material.water) {
				var10 |= extinguishFireLava(var1, var2 - 1, var3, var4);
				var10 |= extinguishFireLava(var1, var2 + 1, var3, var4);
				var10 |= extinguishFireLava(var1, var2, var3, var4 - 1);
				var10 |= extinguishFireLava(var1, var2, var3, var4 + 1);
			}

			if(this.material == Material.lava) {
				var10 |= flow(var1, var2 - 1, var3, var4);
				var10 |= flow(var1, var2 + 1, var3, var4);
				var10 |= flow(var1, var2, var3, var4 - 1);
				var10 |= flow(var1, var2, var3, var4 + 1);
			}

			if(!var10) {
				var1.setTileNoUpdate(var2, var3, var4, this.stillId1);
			} else {
				var1.scheduleBlockUpdate(var2, var3, var4, this.movingId1);
			}

			return var10;
		}
	}

	private boolean liquidSpread(World var1, int var2, int var3, int var4, int var5, int var6, int var7) {
		if(this.canFlow(var1, var5, var6, var7)) {
			var1.setBlockWithNotify(var5, var6, var7, this.blockID);
			var1.scheduleBlockUpdate(var5, var6, var7, this.blockID);
			return true;
		} else {
			return false;
		}
	}

	private boolean liquidSpread2(World var1, int var2, int var3, int var4, int var5, int var6, int var7) {
		if(!this.canFlow(var1, var5, var6, var7)) {
			return false;
		} else {
			var2 = var1.fluidFlowCheck(var2, var3, var4, this.movingId1, this.stillId1);
			if(var2 != -9999) {
				if(var2 < 0) {
					return false;
				}

				var3 = var2 % 1024;
				var2 >>= 10;
				var4 = var2 % 1024;
				var2 >>= 10;
				var2 %= 1024;
				if((var2 > var6 || !this.canFlow(var1, var5, var6 - 1, var7)) && var2 <= var6 && var3 != 0 && var3 != var1.width - 1 && var4 != 0 && var4 != var1.length - 1) {
					return false;
				}

				var1.setBlockWithNotify(var3, var2, var4, 0);
			}

			var1.setBlockWithNotify(var5, var6, var7, this.blockID);
			var1.scheduleBlockUpdate(var5, var6, var7, this.blockID);
			return true;
		}
	}

	public final boolean shouldSideBeRendered(World var1, int var2, int var3, int var4, int var5) {
		if(var2 >= 0 && var3 >= 0 && var4 >= 0 && var2 < var1.width && var4 < var1.length) {
			int var6 = var1.getBlockId(var2, var3, var4);
			return var6 != this.movingId1 && var6 != this.stillId1 ? (var5 != 1 || var1.getBlockId(var2 - 1, var3, var4) != 0 && var1.getBlockId(var2 + 1, var3, var4) != 0 && var1.getBlockId(var2, var3, var4 - 1) != 0 && var1.getBlockId(var2, var3, var4 + 1) != 0 ? super.shouldSideBeRendered(var1, var2, var3, var4, var5) : true) : false;
		} else {
			return false;
		}
	}

	public final boolean isCollidable() {
		return false;
	}

	public final AxisAlignedBB getCollisionBoundingBoxFromPool(int var1, int var2, int var3) {
		return null;
	}

	public final boolean isOpaqueCube() {
		return false;
	}

	public final void onNeighborBlockChange(World var1, int var2, int var3, int var4, int var5) {
	}

	public final int tickRate() {
		return this.material == Material.lava ? 25 : 5;
	}

	public final int quantityDropped(Random var1) {
		return 0;
	}

	public final int getRenderBlockPass() {
		return this.material == Material.water ? 1 : 0;
	}

	private static boolean extinguishFireLava(World var0, int var1, int var2, int var3) {
		if(var0.getBlockId(var1, var2, var3) == Block.fire.blockID) {
			var0.setBlockWithNotify(var1, var2, var3, 0);
			return true;
		} else if(var0.getBlockId(var1, var2, var3) != Block.lavaMoving.blockID && var0.getBlockId(var1, var2, var3) != Block.lavaStill.blockID) {
			return false;
		} else {
			var0.setBlockWithNotify(var1, var2, var3, Block.stone.blockID);
			return true;
		}
	}

	private static boolean flow(World var0, int var1, int var2, int var3) {
		if(Block.fire.getChanceOfNeighborsEncouragingFire(var0.getBlockId(var1, var2, var3))) {
			Block.fire.fireSpread(var0, var1, var2, var3);
			return true;
		} else {
			return false;
		}
	}
}
