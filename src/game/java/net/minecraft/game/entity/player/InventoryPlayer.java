package net.minecraft.game.entity.player;

import net.minecraft.game.IInventory;
import net.minecraft.game.item.ItemArmor;
import net.minecraft.game.item.ItemStack;

public final class InventoryPlayer implements IInventory {
	public ItemStack[] mainInventory = new ItemStack[36];
	public ItemStack[] armorInventory = new ItemStack[4];
	public int currentItem = 0;
	private EntityPlayer player;

	public InventoryPlayer(EntityPlayer var1) {
		this.player = var1;
	}

	public final ItemStack getCurrentItem() {
		return this.mainInventory[this.currentItem];
	}

	private int getInventorySlotContainItem(int var1) {
		for(int var2 = 0; var2 < this.mainInventory.length; ++var2) {
			if(this.mainInventory[var2] != null && this.mainInventory[var2].itemID == var1) {
				return var2;
			}
		}

		return -1;
	}

	private int storeItemStack() {
		for(int var1 = 0; var1 < this.mainInventory.length; ++var1) {
			if(this.mainInventory[var1] == null) {
				return var1;
			}
		}

		return -1;
	}

	public final void getFirstEmptyStack(int var1) {
		var1 = this.getInventorySlotContainItem(var1);
		if(var1 >= 0 && var1 < 9) {
			this.currentItem = var1;
		}
	}

	public final boolean consumeInventoryItem(int var1) {
		var1 = this.getInventorySlotContainItem(var1);
		if(var1 < 0) {
			return false;
		} else {
			if(--this.mainInventory[var1].stackSize <= 0) {
				this.mainInventory[var1] = null;
			}

			return true;
		}
	}

	public final boolean storePartialItemStack(ItemStack var1) {
		if(var1.itemDamage == 0) {
			int var4 = var1.stackSize;
			int var3 = var1.itemID;
			int var6 = var3;
			InventoryPlayer var5 = this;
			int var7 = 0;

			int var10001;
			ItemStack var8;
			while(true) {
				if(var7 >= var5.mainInventory.length) {
					var10001 = -1;
					break;
				}

				if(var5.mainInventory[var7] != null && var5.mainInventory[var7].itemID == var6) {
					var8 = var5.mainInventory[var7];
					if(var5.mainInventory[var7].stackSize < var8.getItem().getItemStackLimit() && var5.mainInventory[var7].stackSize < 64) {
						var10001 = var7;
						break;
					}
				}

				++var7;
			}

			int var9 = var10001;
			if(var9 < 0) {
				var9 = this.storeItemStack();
			}

			if(var9 < 0) {
				var10001 = var4;
			} else {
				if(this.mainInventory[var9] == null) {
					this.mainInventory[var9] = new ItemStack(var3, 0);
				}

				var3 = var4;
				var8 = this.mainInventory[var9];
				if(var4 > var8.getItem().getItemStackLimit() - this.mainInventory[var9].stackSize) {
					var8 = this.mainInventory[var9];
					var3 = var8.getItem().getItemStackLimit() - this.mainInventory[var9].stackSize;
				}

				if(var3 > 64 - this.mainInventory[var9].stackSize) {
					var3 = 64 - this.mainInventory[var9].stackSize;
				}

				if(var3 == 0) {
					var10001 = var4;
				} else {
					var4 -= var3;
					this.mainInventory[var9].stackSize += var3;
					this.mainInventory[var9].animationsToGo = 5;
					var10001 = var4;
				}
			}

			var1.stackSize = var10001;
			if(var1.stackSize == 0) {
				return true;
			}
		}

		int var2 = this.storeItemStack();
		if(var2 >= 0) {
			this.mainInventory[var2] = var1;
			this.mainInventory[var2].animationsToGo = 5;
			return true;
		} else {
			return false;
		}
	}

	public final ItemStack decrStackSize(int var1, int var2) {
		ItemStack[] var3 = this.mainInventory;
		if(var1 >= this.mainInventory.length) {
			var3 = this.armorInventory;
			var1 -= this.mainInventory.length;
		}

		if(var3[var1] != null) {
			ItemStack var4;
			if(var3[var1].stackSize <= var2) {
				var4 = var3[var1];
				var3[var1] = null;
				return var4;
			} else {
				var4 = var3[var1].splitStack(var2);
				if(var3[var1].stackSize == 0) {
					var3[var1] = null;
				}

				return var4;
			}
		} else {
			return null;
		}
	}

	public final void setInventorySlotContents(int var1, ItemStack var2) {
		ItemStack[] var3 = this.mainInventory;
		if(var1 >= this.mainInventory.length) {
			var3 = this.armorInventory;
			var1 -= this.mainInventory.length;
		}

		var3[var1] = var2;
	}

	public final int getSizeInventory() {
		return this.mainInventory.length + 4;
	}

	public final ItemStack getStackInSlot(int var1) {
		ItemStack[] var2 = this.mainInventory;
		if(var1 >= this.mainInventory.length) {
			var2 = this.armorInventory;
			var1 -= this.mainInventory.length;
		}

		return var2[var1];
	}

	public final String getInvName() {
		return "Inventory";
	}

	public final int getInventoryStackLimit() {
		return 64;
	}

	public final int getPlayerArmorValue() {
		int var1 = 0;
		int var2 = 0;
		int var3 = 0;

		for(int var4 = 0; var4 < this.armorInventory.length; ++var4) {
			if(this.armorInventory[var4] != null && this.armorInventory[var4].getItem() instanceof ItemArmor) {
				int var5 = this.armorInventory[var4].isItemStackDamageable();
				int var6 = this.armorInventory[var4].itemDamage;
				var6 = var5 - var6;
				var2 += var6;
				var3 += var5;
				var5 = ((ItemArmor)this.armorInventory[var4].getItem()).damageReduceAmount;
				var1 += var5;
			}
		}

		if(var3 == 0) {
			return 0;
		} else {
			return (var1 - 1) * var2 / var3 + 1;
		}
	}
}
