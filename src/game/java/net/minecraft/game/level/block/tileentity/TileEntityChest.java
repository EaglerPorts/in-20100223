package net.minecraft.game.level.block.tileentity;

import com.mojang.nbt.NBTTagCompound;
import com.mojang.nbt.NBTTagList;
import net.minecraft.game.IInventory;
import net.minecraft.game.item.ItemStack;

public final class TileEntityChest extends TileEntity implements IInventory {
	private ItemStack[] chestContents = new ItemStack[36];

	public final int getSizeInventory() {
		return 27;
	}

	public final ItemStack getStackInSlot(int var1) {
		return this.chestContents[var1];
	}

	public final ItemStack decrStackSize(int var1, int var2) {
		if(this.chestContents[var1] != null) {
			ItemStack var3;
			if(this.chestContents[var1].stackSize <= var2) {
				var3 = this.chestContents[var1];
				this.chestContents[var1] = null;
				return var3;
			} else {
				var3 = this.chestContents[var1].splitStack(var2);
				if(this.chestContents[var1].stackSize == 0) {
					this.chestContents[var1] = null;
				}

				return var3;
			}
		} else {
			return null;
		}
	}

	public final void setInventorySlotContents(int var1, ItemStack var2) {
		this.chestContents[var1] = var2;
		if(var2 != null && var2.stackSize > 64) {
			var2.stackSize = 64;
		}

	}

	public final String getInvName() {
		return "Chest";
	}

	public final void readFromNBT(NBTTagCompound var1) {
		NBTTagList var5 = var1.getTagList("Items");
		this.chestContents = new ItemStack[27];

		for(int var2 = 0; var2 < var5.tagCount(); ++var2) {
			NBTTagCompound var3 = (NBTTagCompound)var5.tagAt(var2);
			int var4 = var3.getByte("Slot") & 255;
			if(var4 >= 0 && var4 < this.chestContents.length) {
				this.chestContents[var4] = new ItemStack(var3);
			}
		}

	}

	public final void writeToNBT(NBTTagCompound var1) {
		var1.setString("id", "Chest");
		NBTTagList var2 = new NBTTagList();

		for(int var3 = 0; var3 < this.chestContents.length; ++var3) {
			if(this.chestContents[var3] != null) {
				NBTTagCompound var4 = new NBTTagCompound();
				var4.setByte("Slot", (byte)var3);
				this.chestContents[var3].writeToNBT(var4);
				var2.setTag(var4);
			}
		}

		var1.setTag("Items", var2);
	}

	public final int getInventoryStackLimit() {
		return 64;
	}
}
