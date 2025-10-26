package net.minecraft.game.level.block;

import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.level.World;
import net.minecraft.game.level.material.Material;

public final class BlockWorkbench extends Block {
	protected BlockWorkbench(int var1) {
		super(58, Material.wood);
		this.blockIndexInTexture = 59;
	}

	public final int getBlockTextureFromSide(int var1) {
		return var1 == 1 ? this.blockIndexInTexture - 16 : (var1 == 0 ? Block.planks.getBlockTextureFromSide(0) : (var1 != 2 && var1 != 4 ? this.blockIndexInTexture : this.blockIndexInTexture + 1));
	}

	public final boolean blockActivated(World var1, int var2, int var3, int var4, EntityPlayer var5) {
		var5.displayWorkbenchGUI();
		return true;
	}
}
