package net.minecraft.client.gui.container;

import net.minecraft.client.RenderHelper;
import net.minecraft.client.render.RenderEngine;
import net.minecraft.client.render.entity.RenderManager;
import net.minecraft.game.IInventory;
import net.minecraft.game.item.ItemStack;
import net.minecraft.game.item.recipe.CraftingManager;
import org.lwjgl.opengl.GL11;

public final class GuiInventory extends GuiContainer {
	private InventoryCrafting inventoryCrafting = new InventoryCrafting(this, 2, 2);
	private IInventory iInventory = new InventoryCraftResult();
	private float xSize_lo;
	private float ySize_lo;

	public GuiInventory(IInventory var1) {
		this.allowUserInput = true;
		this.inventorySlots.add(new SlotCrafting(this, this.inventoryCrafting, this.iInventory, 0, 144, 36));

		int var2;
		int var3;
		for(var2 = 0; var2 < 2; ++var2) {
			for(var3 = 0; var3 < 2; ++var3) {
				this.inventorySlots.add(new Slot(this, this.inventoryCrafting, var3 + (var2 << 1), 88 + var3 * 18, 26 + var2 * 18));
			}
		}

		for(var2 = 0; var2 < 4; ++var2) {
			this.inventorySlots.add(new SlotArmor(this, this, var1, var1.getSizeInventory() - 1 - var2, 8, 8 + var2 * 18, var2));
		}

		for(var2 = 0; var2 < 3; ++var2) {
			for(var3 = 0; var3 < 9; ++var3) {
				this.inventorySlots.add(new Slot(this, var1, var3 + (var2 + 1) * 9, 8 + var3 * 18, 84 + var2 * 18));
			}
		}

		for(var2 = 0; var2 < 9; ++var2) {
			this.inventorySlots.add(new Slot(this, var1, var2, 8 + var2 * 18, 142));
		}

	}

	public final void onGuiClosed() {
		super.onGuiClosed();

		for(int var1 = 0; var1 < this.inventoryCrafting.getSizeInventory(); ++var1) {
			ItemStack var2 = this.inventoryCrafting.getStackInSlot(var1);
			if(var2 != null) {
				this.mc.thePlayer.dropPlayerItem(var2);
			}
		}

	}

	public final void guiCraftingItemsCheck() {
		int[] var1 = new int[9];

		for(int var2 = 0; var2 < 3; ++var2) {
			for(int var3 = 0; var3 < 3; ++var3) {
				int var4 = -1;
				if(var2 < 2 && var3 < 2) {
					ItemStack var5 = this.inventoryCrafting.getStackInSlot(var2 + (var3 << 1));
					if(var5 != null) {
						var4 = var5.itemID;
					}
				}

				var1[var2 + var3 * 3] = var4;
			}
		}

		this.iInventory.setInventorySlotContents(0, CraftingManager.getInstance().findMatchingRecipe(var1));
	}

	protected final void drawGuiContainerForegroundLayer() {
		this.fontRenderer.drawString("Crafting", 86, 16, 4210752);
	}

	public final void drawScreen(int var1, int var2, float var3) {
		super.drawScreen(var1, var2, var3);
		this.xSize_lo = (float)var1;
		this.ySize_lo = (float)var2;
	}

	protected final void drawGuiContainerBackgroundLayer() {
		int var1 = this.mc.renderEngine.getTexture("/gui/inventory.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		RenderEngine.bindTexture(var1);
		var1 = (this.width - this.xSize) / 2;
		int var2 = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(var1, var2, 0, 0, this.xSize, this.ySize);
		GL11.glEnable(GL11.GL_NORMALIZE);
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		GL11.glPushMatrix();
		GL11.glTranslatef((float)(var1 + 51), (float)(var2 + 75), 50.0F);
		GL11.glScalef(-30.0F, 30.0F, 30.0F);
		GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
		float var3 = this.mc.thePlayer.renderYawOffset;
		float var4 = this.mc.thePlayer.rotationYaw;
		float var5 = this.mc.thePlayer.rotationPitch;
		float var6 = (float)(var1 + 51) - this.xSize_lo;
		float var7 = (float)(var2 + 75 - 50) - this.ySize_lo;
		GL11.glRotatef(135.0F, 0.0F, 1.0F, 0.0F);
		RenderHelper.enableStandardItemLighting();
		GL11.glRotatef(-135.0F, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(-((float)Math.atan((double)(var7 / 40.0F))) * 20.0F, 1.0F, 0.0F, 0.0F);
		this.mc.thePlayer.renderYawOffset = (float)Math.atan((double)(var6 / 40.0F)) * 20.0F;
		this.mc.thePlayer.rotationYaw = (float)Math.atan((double)(var6 / 40.0F)) * 40.0F;
		this.mc.thePlayer.rotationPitch = -((float)Math.atan((double)(var7 / 40.0F))) * 20.0F;
		GL11.glTranslatef(0.0F, this.mc.thePlayer.yOffset, 0.0F);
		RenderManager.instance.renderEntityWithPosYaw(this.mc.thePlayer, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F);
		this.mc.thePlayer.renderYawOffset = var3;
		this.mc.thePlayer.rotationYaw = var4;
		this.mc.thePlayer.rotationPitch = var5;
		GL11.glPopMatrix();
		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(GL11.GL_NORMALIZE);
	}
}
