package net.minecraft.client.gui.container;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.RenderHelper;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.player.EntityPlayerSP;
import net.minecraft.client.render.RenderEngine;
import net.minecraft.client.render.entity.RenderItem;
import net.minecraft.game.IInventory;
import net.minecraft.game.item.ItemStack;
import org.lwjgl.opengl.GL11;

public abstract class GuiContainer extends GuiScreen {
	private static RenderItem itemRenderer = new RenderItem();
	private ItemStack itemStack = null;
	protected int xSize = 176;
	protected int ySize = 166;
	protected List inventorySlots = new ArrayList();

	public void drawScreen(int var1, int var2, float var3) {
		this.drawDefaultBackground();
		int var13 = (this.width - this.xSize) / 2;
		int var4 = (this.height - this.ySize) / 2;
		this.drawGuiContainerBackgroundLayer();
		GL11.glPushMatrix();
		GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
		RenderHelper.enableStandardItemLighting();
		GL11.glPopMatrix();
		GL11.glPushMatrix();
		GL11.glTranslatef((float)var13, (float)var4, 0.0F);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glEnable(GL11.GL_NORMALIZE);

		for(int var5 = 0; var5 < this.inventorySlots.size(); ++var5) {
			Slot var6;
			label24: {
				var6 = (Slot)this.inventorySlots.get(var5);
				IInventory var9 = var6.inventory;
				int var10 = var6.slotIndex;
				int var11 = var6.xPos;
				int var12 = var6.yPos;
				ItemStack var15 = var9.getStackInSlot(var10);
				if(var15 == null) {
					int var8 = var6.getBackgroundIconIndex();
					if(var8 >= 0) {
						GL11.glDisable(GL11.GL_LIGHTING);
						RenderEngine.bindTexture(this.mc.renderEngine.getTexture("/gui/items.png"));
						this.drawTexturedModalRect(var11, var12, var8 % 16 << 4, var8 / 16 << 4, 16, 16);
						GL11.glEnable(GL11.GL_LIGHTING);
						break label24;
					}
				}

				itemRenderer.renderItemIntoGUI(this.mc.renderEngine, var15, var11, var12);
				itemRenderer.renderItemOverlayIntoGUI(this.fontRenderer, var15, var11, var12);
			}

			if(var6.isAtCursorPos(var1, var2)) {
				GL11.glDisable(GL11.GL_LIGHTING);
				GL11.glDisable(GL11.GL_DEPTH_TEST);
				int var7 = var6.xPos;
				int var14 = var6.yPos;
				drawGradientRect(var7, var14, var7 + 16, var14 + 16, -2130706433, -2130706433);
				GL11.glEnable(GL11.GL_LIGHTING);
				GL11.glEnable(GL11.GL_DEPTH_TEST);
			}
		}

		if(this.itemStack != null) {
			GL11.glTranslatef(0.0F, 0.0F, 32.0F);
			itemRenderer.renderItemIntoGUI(this.mc.renderEngine, this.itemStack, var1 - var13 - 8, var2 - var4 - 8);
			itemRenderer.renderItemOverlayIntoGUI(this.fontRenderer, this.itemStack, var1 - var13 - 8, var2 - var4 - 8);
		}

		GL11.glDisable(GL11.GL_NORMALIZE);
		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		this.drawGuiContainerForegroundLayer();
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glPopMatrix();
	}

	protected void drawGuiContainerForegroundLayer() {
	}

	protected abstract void drawGuiContainerBackgroundLayer();

	protected final void drawSlotInventory(int var1, int var2, int var3) {
		if(var3 == 0 || var3 == 1) {
			int var6 = var2;
			int var4 = var1;
			GuiContainer var5 = this;
			int var7 = 0;

			Slot var10000;
			while(true) {
				if(var7 >= var5.inventorySlots.size()) {
					var10000 = null;
					break;
				}

				Slot var8 = (Slot)var5.inventorySlots.get(var7);
				if(var8.isAtCursorPos(var4, var6)) {
					var10000 = var8;
					break;
				}

				++var7;
			}

			Slot var11 = var10000;
			if(var11 != null) {
				ItemStack var12 = var11.inventory.getStackInSlot(var11.slotIndex);
				if(var12 == null && this.itemStack == null) {
					return;
				}

				if(var12 != null && this.itemStack == null) {
					var6 = var3 == 0 ? var12.stackSize : (var12.stackSize + 1) / 2;
					this.itemStack = var11.inventory.decrStackSize(var11.slotIndex, var6);
					if(var12.stackSize == 0) {
						var11.putStack((ItemStack)null);
					}

					var11.onPickupFromSlot();
				} else if(var12 == null && this.itemStack != null && var11.isItemValid(this.itemStack)) {
					var6 = var3 == 0 ? this.itemStack.stackSize : 1;
					if(var6 > var11.inventory.getInventoryStackLimit()) {
						var6 = var11.inventory.getInventoryStackLimit();
					}

					var11.putStack(this.itemStack.splitStack(var6));
					if(this.itemStack.stackSize == 0) {
						this.itemStack = null;
					}
				} else {
					if(var12 == null || this.itemStack == null) {
						return;
					}

					ItemStack var9;
					if(!var11.isItemValid(this.itemStack)) {
						if(var12.itemID == this.itemStack.itemID) {
							var9 = this.itemStack;
							if(var9.getItem().getItemStackLimit() > 1) {
								var6 = var12.stackSize;
								if(var6 > 0) {
									int var14 = var6 + this.itemStack.stackSize;
									var9 = this.itemStack;
									if(var14 <= var9.getItem().getItemStackLimit()) {
										this.itemStack.stackSize += var6;
										var12.splitStack(var6);
										if(var12.stackSize == 0) {
											var11.putStack((ItemStack)null);
										}

										var11.onPickupFromSlot();
										return;
									}
								}

								return;
							}
						}

						return;
					}

					if(var12.itemID != this.itemStack.itemID) {
						if(this.itemStack.stackSize > var11.inventory.getInventoryStackLimit()) {
							return;
						}

						var11.putStack(this.itemStack);
						this.itemStack = var12;
					} else {
						if(var12.itemID != this.itemStack.itemID) {
							return;
						}

						if(var3 == 0) {
							var6 = this.itemStack.stackSize;
							if(var6 > var11.inventory.getInventoryStackLimit() - var12.stackSize) {
								var6 = var11.inventory.getInventoryStackLimit() - var12.stackSize;
							}

							var9 = this.itemStack;
							if(var6 > var9.getItem().getItemStackLimit() - var12.stackSize) {
								var9 = this.itemStack;
								var6 = var9.getItem().getItemStackLimit() - var12.stackSize;
							}

							this.itemStack.splitStack(var6);
							if(this.itemStack.stackSize == 0) {
								this.itemStack = null;
							}

							var12.stackSize += var6;
						} else {
							if(var3 != 1) {
								return;
							}

							var6 = 1;
							if(1 > var11.inventory.getInventoryStackLimit() - var12.stackSize) {
								var6 = var11.inventory.getInventoryStackLimit() - var12.stackSize;
							}

							var9 = this.itemStack;
							if(var6 > var9.getItem().getItemStackLimit() - var12.stackSize) {
								var9 = this.itemStack;
								var6 = var9.getItem().getItemStackLimit() - var12.stackSize;
							}

							this.itemStack.splitStack(var6);
							if(this.itemStack.stackSize == 0) {
								this.itemStack = null;
							}

							var12.stackSize += var6;
						}
					}
				}
			} else if(this.itemStack != null) {
				int var13 = (this.width - this.xSize) / 2;
				var6 = (this.height - this.ySize) / 2;
				if(var1 < var13 || var2 < var6 || var1 >= var13 + this.xSize || var2 >= var6 + this.xSize) {
					EntityPlayerSP var10 = this.mc.thePlayer;
					if(var3 == 0) {
						var10.dropPlayerItem(this.itemStack);
						this.itemStack = null;
					}

					if(var3 == 1) {
						var10.dropPlayerItem(this.itemStack.splitStack(1));
						if(this.itemStack.stackSize == 0) {
							this.itemStack = null;
						}
					}
				}
			}
		}

	}

	protected final void keyTyped(char var1, int var2) {
		if(var2 == 1 || var2 == this.mc.options.keyBindInventory.keyCode) {
			this.mc.displayGuiScreen((GuiScreen)null);
		}

	}

	public void onGuiClosed() {
		if(this.itemStack != null) {
			this.mc.thePlayer.dropPlayerItem(this.itemStack);
		}

	}

	public void guiCraftingItemsCheck() {
	}

	public final boolean doesGuiPauseGame() {
		return false;
	}
}
