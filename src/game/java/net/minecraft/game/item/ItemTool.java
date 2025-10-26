package net.minecraft.game.item;

import net.minecraft.game.level.block.Block;

public class ItemTool extends Item {
	private Block[] blocksEffectiveAgainst;
	private float efficiencyOnProperMaterial = 4.0F;
	private int damageVsEntity;

	public ItemTool(int var1, int var2, int var3, Block[] var4) {
		super(var1);
		this.blocksEffectiveAgainst = var4;
		this.maxStackSize = 1;
		this.maxDamage = 32 << var3;
		this.efficiencyOnProperMaterial = (float)(var3 + 1 << 1);
		this.damageVsEntity = var2 + var3;
	}

	public final float getStrVsBlock(Block var1) {
		for(int var2 = 0; var2 < this.blocksEffectiveAgainst.length; ++var2) {
			if(this.blocksEffectiveAgainst[var2] == var1) {
				return this.efficiencyOnProperMaterial;
			}
		}

		return 1.0F;
	}

	public final void hitEntity(ItemStack var1) {
		var1.damageItem(2);
	}

	public final void onBlockDestroyed(ItemStack var1) {
		var1.damageItem(1);
	}

	public final int getDamageVsEntity() {
		return this.damageVsEntity;
	}
}
