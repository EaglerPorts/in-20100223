package net.minecraft.client.player;

import com.mojang.nbt.NBTTagCompound;
import com.mojang.nbt.NBTTagList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Session;
import net.minecraft.client.effect.EntityPickupFX;
import net.minecraft.client.gui.container.GuiChest;
import net.minecraft.client.gui.container.GuiCrafting;
import net.minecraft.client.gui.container.GuiFurnace;
import net.minecraft.game.IInventory;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.entity.player.InventoryPlayer;
import net.minecraft.game.item.ItemStack;
import net.minecraft.game.level.World;
import net.minecraft.game.level.block.tileentity.TileEntityFurnace;

public class EntityPlayerSP extends EntityPlayer {
	public MovementInput movementInput;
	private Minecraft mc;

	public EntityPlayerSP(Minecraft var1, World var2, Session var3) {
		super(var2);
		this.mc = var1;
		if(var3 != null) {
			this.skinUrl = "http://www.minecraft.net/skin/" + var3.username + ".png";
		}

	}

	public final void updatePlayerActionState() {
		this.moveStrafing = this.movementInput.moveStrafe;
		this.moveForward = this.movementInput.moveForward;
		this.isJumping = this.movementInput.jump;
	}

	public final void onLivingUpdate() {
		this.movementInput.updatePlayerMoveState();
		super.onLivingUpdate();
	}

	protected final void writeEntityToNBT(NBTTagCompound var1) {
		super.writeEntityToNBT(var1);
		var1.setInteger("Score", this.getScore);
		InventoryPlayer var10002 = this.inventory;
		NBTTagList var2 = new NBTTagList();
		InventoryPlayer var5 = var10002;

		int var3;
		NBTTagCompound var4;
		for(var3 = 0; var3 < var5.mainInventory.length; ++var3) {
			if(var5.mainInventory[var3] != null) {
				var4 = new NBTTagCompound();
				var4.setByte("Slot", (byte)var3);
				var5.mainInventory[var3].writeToNBT(var4);
				var2.setTag(var4);
			}
		}

		for(var3 = 0; var3 < var5.armorInventory.length; ++var3) {
			if(var5.armorInventory[var3] != null) {
				var4 = new NBTTagCompound();
				var4.setByte("Slot", (byte)(var3 + 100));
				var5.armorInventory[var3].writeToNBT(var4);
				var2.setTag(var4);
			}
		}

		var1.setTag("Inventory", var2);
	}

	protected final void readEntityFromNBT(NBTTagCompound var1) {
		super.readEntityFromNBT(var1);
		this.getScore = var1.getInteger("Score");
		NBTTagList var6 = var1.getTagList("Inventory");
		NBTTagList var2 = var6;
		InventoryPlayer var7 = this.inventory;
		var7.mainInventory = new ItemStack[36];
		var7.armorInventory = new ItemStack[4];

		for(int var3 = 0; var3 < var2.tagCount(); ++var3) {
			NBTTagCompound var4 = (NBTTagCompound)var2.tagAt(var3);
			int var5 = var4.getByte("Slot") & 255;
			if(var5 >= 0 && var5 < var7.mainInventory.length) {
				var7.mainInventory[var5] = new ItemStack(var4);
			}

			if(var5 >= 100 && var5 < var7.armorInventory.length + 100) {
				var7.armorInventory[var5 - 100] = new ItemStack(var4);
			}
		}

	}

	protected final String getEntityString() {
		return "LocalPlayer";
	}

	public final void displayGUIChest(IInventory var1) {
		this.mc.displayGuiScreen(new GuiChest(this.inventory, var1));
	}

	public final void displayWorkbenchGUI() {
		this.mc.displayGuiScreen(new GuiCrafting(this.inventory));
	}

	public final void displayGUIFurnace(TileEntityFurnace var1) {
		this.mc.displayGuiScreen(new GuiFurnace(this.inventory, var1));
	}

	public final void destroyCurrentEquippedItem() {
		this.inventory.setInventorySlotContents(this.inventory.currentItem, (ItemStack)null);
	}

	public final void onItemPickup(Entity var1) {
		this.mc.effectRenderer.addEffect(new EntityPickupFX(this.mc.theWorld, var1, this, -0.5F));
	}
}
