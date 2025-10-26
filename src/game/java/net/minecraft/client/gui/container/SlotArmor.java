package net.minecraft.client.gui.container;

import net.minecraft.game.IInventory;
import net.minecraft.game.item.ItemArmor;
import net.minecraft.game.item.ItemStack;

final class SlotArmor extends Slot {
	private int armorType;

	SlotArmor(GuiInventory var1, GuiContainer var2, IInventory var3, int var4, int var5, int var6, int var7) {
		super(var2, var3, var4, 8, var6);
		this.armorType = var7;
	}

	public final boolean isItemValid(ItemStack var1) {
		return var1.getItem() instanceof ItemArmor ? ((ItemArmor)var1.getItem()).armorType == this.armorType : false;
	}

	public final int getBackgroundIconIndex() {
		return 15 + (this.armorType << 4);
	}
}
