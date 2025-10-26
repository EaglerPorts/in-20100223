package net.minecraft.game.level.block;

import java.util.Random;
import net.minecraft.game.level.World;
import net.minecraft.game.level.material.Material;

public final class BlockStationary extends BlockFluid {
	protected BlockStationary(int var1, Material var2) {
		super(var1, var2);
		this.movingId = var1 - 1;
		this.stillId = var1;
		this.setTickOnLoad(false);
	}

	public final void updateTick(World var1, int var2, int var3, int var4, Random var5) {
	}

	public final void onNeighborBlockChange(World var1, int var2, int var3, int var4, int var5) {
		boolean var6 = false;
		if(this.canFlow(var1, var2, var3 - 1, var4)) {
			var6 = true;
		}

		if(!var6 && this.canFlow(var1, var2 - 1, var3, var4)) {
			var6 = true;
		}

		if(!var6 && this.canFlow(var1, var2 + 1, var3, var4)) {
			var6 = true;
		}

		if(!var6 && this.canFlow(var1, var2, var3, var4 - 1)) {
			var6 = true;
		}

		if(!var6 && this.canFlow(var1, var2, var3, var4 + 1)) {
			var6 = true;
		}

		if(var5 != 0) {
			Material var7 = Block.blocksList[var5].material;
			if(this.material == Material.water && var7 == Material.lava || var7 == Material.water && this.material == Material.lava) {
				var1.setBlockWithNotify(var2, var3, var4, Block.stone.blockID);
				return;
			}
		}

		if(Block.fire.getChanceOfNeighborsEncouragingFire(var5)) {
			var6 = true;
		}

		if(var6) {
			var1.setTileNoUpdate(var2, var3, var4, this.movingId);
			var1.scheduleBlockUpdate(var2, var3, var4, this.movingId);
		}

	}
}
