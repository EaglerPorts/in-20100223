package net.minecraft.game.item;

import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.level.World;

public final class ItemSoup extends ItemFood {
	public ItemSoup(int var1, int var2) {
		super(26, 10);
	}

	public final ItemStack onItemRightClick(ItemStack var1, World var2, EntityPlayer var3) {
		super.onItemRightClick(var1, var2, var3);
		return new ItemStack(Item.bowlEmpty);
	}
}
