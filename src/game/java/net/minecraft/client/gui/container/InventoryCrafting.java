package net.minecraft.client.gui.container;

import net.minecraft.game.IInventory;
import net.minecraft.game.item.ItemStack;

public final class InventoryCrafting implements IInventory {
	private ItemStack[] stackList;
	private int inventoryWidth;
	private GuiContainer eventHandler;

	public InventoryCrafting(GuiContainer var1, int var2, int var3) {
		this.inventoryWidth = var2 * var3;
		this.stackList = new ItemStack[this.inventoryWidth];
		this.eventHandler = var1;
	}

	public final int getSizeInventory() {
		return this.inventoryWidth;
	}

	public final ItemStack getStackInSlot(int var1) {
		return this.stackList[var1];
	}

	public final String getInvName() {
		return "Crafting";
	}

	public final ItemStack decrStackSize(int var1, int var2) {
		if(this.stackList[var1] != null) {
			ItemStack var3;
			if(this.stackList[var1].stackSize <= var2) {
				var3 = this.stackList[var1];
				this.stackList[var1] = null;
				this.eventHandler.guiCraftingItemsCheck();
				return var3;
			} else {
				var3 = this.stackList[var1].splitStack(var2);
				if(this.stackList[var1].stackSize == 0) {
					this.stackList[var1] = null;
				}

				this.eventHandler.guiCraftingItemsCheck();
				return var3;
			}
		} else {
			return null;
		}
	}

	public final void setInventorySlotContents(int var1, ItemStack var2) {
		this.stackList[var1] = var2;
		this.eventHandler.guiCraftingItemsCheck();
	}

	public final int getInventoryStackLimit() {
		return 64;
	}
}
