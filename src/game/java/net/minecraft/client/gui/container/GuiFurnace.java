package net.minecraft.client.gui.container;

import net.minecraft.client.render.RenderEngine;
import net.minecraft.game.entity.player.InventoryPlayer;
import net.minecraft.game.level.block.tileentity.TileEntityFurnace;
import org.lwjgl.opengl.GL11;

public final class GuiFurnace extends GuiContainer {
	private TileEntityFurnace furnaceInventory;

	public GuiFurnace(InventoryPlayer var1, TileEntityFurnace var2) {
		new InventoryCraftResult();
		this.furnaceInventory = var2;
		this.inventorySlots.add(new Slot(this, var2, 0, 56, 17));
		this.inventorySlots.add(new Slot(this, var2, 1, 56, 53));
		this.inventorySlots.add(new Slot(this, var2, 2, 116, 35));

		int var4;
		for(var4 = 0; var4 < 3; ++var4) {
			for(int var3 = 0; var3 < 9; ++var3) {
				this.inventorySlots.add(new Slot(this, var1, var3 + (var4 + 1) * 9, 8 + var3 * 18, 84 + var4 * 18));
			}
		}

		for(var4 = 0; var4 < 9; ++var4) {
			this.inventorySlots.add(new Slot(this, var1, var4, 8 + var4 * 18, 142));
		}

	}

	protected final void drawGuiContainerForegroundLayer() {
		this.fontRenderer.drawString("Furnace", 60, 6, 4210752);
		this.fontRenderer.drawString("Inventory", 8, this.ySize - 96 + 2, 4210752);
	}

	protected final void drawGuiContainerBackgroundLayer() {
		int var1 = this.mc.renderEngine.getTexture("/gui/furnace.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		RenderEngine.bindTexture(var1);
		var1 = (this.width - this.xSize) / 2;
		int var2 = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(var1, var2, 0, 0, this.xSize, this.ySize);
		int var3;
		if(this.furnaceInventory.isBurning()) {
			var3 = this.furnaceInventory.getBurnTimeRemainingScaled(12);
			this.drawTexturedModalRect(var1 + 56, var2 + 36 + 12 - var3, 176, 12 - var3, 14, var3 + 2);
		}

		var3 = this.furnaceInventory.getCookProgressScaled(24);
		this.drawTexturedModalRect(var1 + 79, var2 + 34, 176, 14, var3 + 1, 16);
	}
}
