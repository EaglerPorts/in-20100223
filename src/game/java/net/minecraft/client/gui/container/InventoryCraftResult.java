package net.minecraft.client.gui.container;

import net.minecraft.game.IInventory;
import net.minecraft.game.item.ItemStack;

public final class InventoryCraftResult implements IInventory {
	private ItemStack[] stackResult = new ItemStack[1];

	public final int getSizeInventory() {
		return 1;
	}

	public final ItemStack getStackInSlot(int var1) {
		return this.stackResult[var1];
	}

	public final String getInvName() {
		return "Result";
	}

	public final ItemStack decrStackSize(int var1, int var2) {
		if(this.stackResult[var1] != null) {
			ItemStack var3 = this.stackResult[var1];
			this.stackResult[var1] = null;
			return var3;
		} else {
			return null;
		}
	}

	public final void setInventorySlotContents(int var1, ItemStack var2) {
		this.stackResult[var1] = var2;
	}

	public final int getInventoryStackLimit() {
		return 64;
	}
}
