package net.minecraft.client.gui.container;

import net.minecraft.game.IInventory;
import net.minecraft.game.item.ItemStack;

public class Slot {
	public final int slotIndex;
	public final int xPos;
	public final int yPos;
	public final IInventory inventory;
	private final GuiContainer guiHandler;

	public Slot(GuiContainer var1, IInventory var2, int var3, int var4, int var5) {
		this.guiHandler = var1;
		this.inventory = var2;
		this.slotIndex = var3;
		this.xPos = var4;
		this.yPos = var5;
	}

	public final boolean isAtCursorPos(int var1, int var2) {
		int var3 = (this.guiHandler.width - this.guiHandler.xSize) / 2;
		int var4 = (this.guiHandler.height - this.guiHandler.ySize) / 2;
		var1 -= var3;
		var2 -= var4;
		return var1 >= this.xPos - 1 && var1 < this.xPos + 16 + 1 && var2 >= this.yPos - 1 && var2 < this.yPos + 16 + 1;
	}

	public void onPickupFromSlot() {
	}

	public boolean isItemValid(ItemStack var1) {
		return true;
	}

	public final void putStack(ItemStack var1) {
		this.inventory.setInventorySlotContents(this.slotIndex, var1);
	}

	public int getBackgroundIconIndex() {
		return -1;
	}
}
