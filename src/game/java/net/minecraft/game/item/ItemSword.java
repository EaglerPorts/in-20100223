package net.minecraft.game.item;

import net.minecraft.game.level.block.Block;

public final class ItemSword extends Item {
	private int weaponDamage;

	public ItemSword(int var1, int var2) {
		super(var1);
		this.maxStackSize = 1;
		this.maxDamage = 32 << var2;
		this.weaponDamage = 4 + (var2 << 1);
	}

	public final float getStrVsBlock(Block var1) {
		return 1.5F;
	}

	public final void hitEntity(ItemStack var1) {
		var1.damageItem(1);
	}

	public final void onBlockDestroyed(ItemStack var1) {
		var1.damageItem(2);
	}

	public final int getDamageVsEntity() {
		return this.weaponDamage;
	}
}
