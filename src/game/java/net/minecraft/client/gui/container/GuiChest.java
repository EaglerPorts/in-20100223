package net.minecraft.client.gui.container;

import net.minecraft.client.render.RenderEngine;
import net.minecraft.game.IInventory;
import org.lwjgl.opengl.GL11;

public final class GuiChest extends GuiContainer {
	private IInventory upperChestInventory;
	private IInventory lowerChestInventory;
	private int inventoryRows = 0;

	public GuiChest(IInventory var1, IInventory var2) {
		this.upperChestInventory = var1;
		this.lowerChestInventory = var2;
		this.allowUserInput = false;
		this.inventoryRows = var2.getSizeInventory() / 9;
		this.ySize = 114 + this.inventoryRows * 18;
		int var3 = (this.inventoryRows - 4) * 18;

		int var4;
		int var5;
		for(var4 = 0; var4 < this.inventoryRows; ++var4) {
			for(var5 = 0; var5 < 9; ++var5) {
				this.inventorySlots.add(new Slot(this, var2, var5 + var4 * 9, 8 + var5 * 18, 18 + var4 * 18));
			}
		}

		for(var4 = 0; var4 < 3; ++var4) {
			for(var5 = 0; var5 < 9; ++var5) {
				this.inventorySlots.add(new Slot(this, var1, var5 + (var4 + 1) * 9, 8 + var5 * 18, 103 + var4 * 18 + var3));
			}
		}

		for(var4 = 0; var4 < 9; ++var4) {
			this.inventorySlots.add(new Slot(this, var1, var4, 8 + var4 * 18, var3 + 161));
		}

	}

	protected final void drawGuiContainerForegroundLayer() {
		this.fontRenderer.drawString(this.lowerChestInventory.getInvName(), 8, 6, 4210752);
		this.fontRenderer.drawString(this.upperChestInventory.getInvName(), 8, this.ySize - 96 + 2, 4210752);
	}

	protected final void drawGuiContainerBackgroundLayer() {
		int var1 = this.mc.renderEngine.getTexture("/gui/container.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		RenderEngine.bindTexture(var1);
		var1 = (this.width - this.xSize) / 2;
		int var2 = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(var1, var2, 0, 0, this.xSize, this.inventoryRows * 18 + 17);
		this.drawTexturedModalRect(var1, var2 + this.inventoryRows * 18 + 17, 0, 126, this.xSize, 96);
	}
}
