package net.minecraft.game.item;

import com.mojang.nbt.NBTTagCompound;
import net.minecraft.game.level.block.Block;

public final class ItemStack {
	public int stackSize;
	public int animationsToGo;
	public int itemID;
	public int itemDamage;

	public ItemStack(Block var1) {
		this((Block)var1, 1);
	}

	public ItemStack(Block var1, int var2) {
		this(var1.blockID, var2);
	}

	public ItemStack(Item var1) {
		this((Item)var1, 1);
	}

	public ItemStack(Item var1, int var2) {
		this(var1.shiftedIndex, var2);
	}

	public ItemStack(int var1) {
		this(var1, 1);
	}

	public ItemStack(int var1, int var2) {
		this.stackSize = 0;
		this.itemID = var1;
		this.stackSize = var2;
	}

	public ItemStack(int var1, int var2, int var3) {
		this.stackSize = 0;
		this.itemID = var1;
		this.stackSize = var2;
		this.itemDamage = var3;
	}

	public ItemStack(NBTTagCompound var1) {
		this.stackSize = 0;
		this.itemID = var1.getShort("id");
		this.stackSize = var1.getByte("Count");
		this.itemDamage = var1.getShort("Damage");
	}

	public final ItemStack splitStack(int var1) {
		this.stackSize -= var1;
		return new ItemStack(this.itemID, var1, this.itemDamage);
	}

	public final Item getItem() {
		return Item.itemsList[this.itemID];
	}

	public final NBTTagCompound writeToNBT(NBTTagCompound var1) {
		var1.setShort("id", (short)this.itemID);
		var1.setByte("Count", (byte)this.stackSize);
		var1.setShort("Damage", (short)this.itemDamage);
		return var1;
	}

	public final int isItemStackDamageable() {
		return Item.itemsList[this.itemID].getMaxDamage();
	}

	public final void damageItem(int var1) {
		this.itemDamage += var1;
		if(this.itemDamage > this.isItemStackDamageable()) {
			--this.stackSize;
			if(this.stackSize < 0) {
				this.stackSize = 0;
			}

			this.itemDamage = 0;
		}

	}
}
