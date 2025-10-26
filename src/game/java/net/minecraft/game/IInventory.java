package net.minecraft.game;

import net.minecraft.game.item.ItemStack;

public interface IInventory {
	int getSizeInventory();

	ItemStack getStackInSlot(int var1);

	ItemStack decrStackSize(int var1, int var2);

	void setInventorySlotContents(int var1, ItemStack var2);

	String getInvName();

	int getInventoryStackLimit();
}
