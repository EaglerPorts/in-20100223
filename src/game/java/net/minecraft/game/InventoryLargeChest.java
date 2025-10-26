package net.minecraft.game;

import net.minecraft.game.item.ItemStack;

public final class InventoryLargeChest implements IInventory {
	private String name;
	private IInventory upperChest;
	private IInventory lowerChest;

	public InventoryLargeChest(String var1, IInventory var2, IInventory var3) {
		this.name = var1;
		this.upperChest = var2;
		this.lowerChest = var3;
	}

	public final int getSizeInventory() {
		return this.upperChest.getSizeInventory() + this.lowerChest.getSizeInventory();
	}

	public final String getInvName() {
		return this.name;
	}

	public final ItemStack getStackInSlot(int var1) {
		return var1 >= this.upperChest.getSizeInventory() ? this.lowerChest.getStackInSlot(var1 - this.upperChest.getSizeInventory()) : this.upperChest.getStackInSlot(var1);
	}

	public final ItemStack decrStackSize(int var1, int var2) {
		return var1 >= this.upperChest.getSizeInventory() ? this.lowerChest.decrStackSize(var1 - this.upperChest.getSizeInventory(), var2) : this.upperChest.decrStackSize(var1, var2);
	}

	public final void setInventorySlotContents(int var1, ItemStack var2) {
		if(var1 >= this.upperChest.getSizeInventory()) {
			this.lowerChest.setInventorySlotContents(var1 - this.upperChest.getSizeInventory(), var2);
		} else {
			this.upperChest.setInventorySlotContents(var1, var2);
		}
	}

	public final int getInventoryStackLimit() {
		return this.upperChest.getInventoryStackLimit();
	}
}
