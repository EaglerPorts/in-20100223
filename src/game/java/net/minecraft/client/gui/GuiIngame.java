package net.minecraft.client.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.lax1dude.eaglercraft.EagRuntime;
import net.minecraft.client.ChatLine;
import net.minecraft.client.Minecraft;
import net.minecraft.client.RenderHelper;
import net.minecraft.client.player.EntityPlayerSP;
import net.minecraft.client.render.entity.RenderItem;
import net.minecraft.game.entity.player.InventoryPlayer;
import net.minecraft.game.item.ItemStack;
import org.lwjgl.opengl.GL11;

import dev.colbster937.eaglercraft.gui.GuiChat;

public final class GuiIngame extends Gui {
	private static RenderItem itemRenderer = new RenderItem();
	private List chatMessageList = new ArrayList();
	private Random rand = new Random();
	private Minecraft mc;
	private int updateCounter = 0;

	public GuiIngame(Minecraft var1) {
		this.mc = var1;
	}

	public final void renderGameOverlay(float var1) {
		ScaledResolution var2 = new ScaledResolution(this.mc.displayWidth, this.mc.displayHeight);
		int var3 = var2.getScaledWidth();
		int var19 = var2.getScaledHeight();
		FontRenderer var4 = this.mc.fontRenderer;
		this.mc.entityRenderer.setupOverlayRendering();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/gui/gui.png"));
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glEnable(GL11.GL_BLEND);
		InventoryPlayer var5 = this.mc.thePlayer.inventory;
		this.zLevel = -90.0F;
		this.drawTexturedModalRect(var3 / 2 - 91, var19 - 22, 0, 0, 182, 22);
		this.drawTexturedModalRect(var3 / 2 - 91 - 1 + var5.currentItem * 20, var19 - 22 - 1, 0, 22, 24, 22);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/gui/icons.png"));
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_ONE_MINUS_DST_COLOR, GL11.GL_ONE_MINUS_SRC_COLOR);
		this.drawTexturedModalRect(var3 / 2 - 7, var19 / 2 - 7, 0, 0, 16, 16);
		GL11.glDisable(GL11.GL_BLEND);
		boolean var20 = this.mc.thePlayer.heartsLife / 3 % 2 == 1;
		if(this.mc.thePlayer.heartsLife < 10) {
			var20 = false;
		}

		int var6 = this.mc.thePlayer.health;
		int var7 = this.mc.thePlayer.prevHealth;
		this.rand.setSeed((long)(this.updateCounter * 312871));
		int var10;
		int var12;
		if(this.mc.playerController.shouldDrawHUD()) {
			EntityPlayerSP var8 = this.mc.thePlayer;
			var10 = var8.inventory.getPlayerArmorValue();

			int var11;
			int var13;
			for(var11 = 0; var11 < 10; ++var11) {
				var12 = var19 - 32;
				if(var10 > 0) {
					var13 = var3 / 2 + 91 - (var11 << 3) - 9;
					if((var11 << 1) + 1 < var10) {
						this.drawTexturedModalRect(var13, var12, 34, 9, 9, 9);
					}

					if((var11 << 1) + 1 == var10) {
						this.drawTexturedModalRect(var13, var12, 25, 9, 9, 9);
					}

					if((var11 << 1) + 1 > var10) {
						this.drawTexturedModalRect(var13, var12, 16, 9, 9, 9);
					}
				}

				byte var26 = 0;
				if(var20) {
					var26 = 1;
				}

				int var14 = var3 / 2 - 91 + (var11 << 3);
				if(var6 <= 4) {
					var12 += this.rand.nextInt(2);
				}

				this.drawTexturedModalRect(var14, var12, 16 + var26 * 9, 0, 9, 9);
				if(var20) {
					if((var11 << 1) + 1 < var7) {
						this.drawTexturedModalRect(var14, var12, 70, 0, 9, 9);
					}

					if((var11 << 1) + 1 == var7) {
						this.drawTexturedModalRect(var14, var12, 79, 0, 9, 9);
					}
				}

				if((var11 << 1) + 1 < var6) {
					this.drawTexturedModalRect(var14, var12, 52, 0, 9, 9);
				}

				if((var11 << 1) + 1 == var6) {
					this.drawTexturedModalRect(var14, var12, 61, 0, 9, 9);
				}
			}

			if(this.mc.thePlayer.isInsideOfWater()) {
				var11 = (int)Math.ceil((double)(this.mc.thePlayer.air - 2) * 10.0D / 300.0D);
				var12 = (int)Math.ceil((double)this.mc.thePlayer.air * 10.0D / 300.0D) - var11;

				for(var13 = 0; var13 < var11 + var12; ++var13) {
					if(var13 < var11) {
						this.drawTexturedModalRect(var3 / 2 - 91 + (var13 << 3), var19 - 32 - 9, 16, 18, 9, 9);
					} else {
						this.drawTexturedModalRect(var3 / 2 - 91 + (var13 << 3), var19 - 32 - 9, 25, 18, 9, 9);
					}
				}
			}
		}

		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_NORMALIZE);
		GL11.glPushMatrix();
		GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
		RenderHelper.enableStandardItemLighting();
		GL11.glPopMatrix();

		for(var10 = 0; var10 < 9; ++var10) {
			int var25 = var3 / 2 - 90 + var10 * 20 + 2;
			int var21 = var19 - 16 - 3;
			ItemStack var22 = this.mc.thePlayer.inventory.mainInventory[var10];
			if(var22 != null) {
				float var9 = (float)var22.animationsToGo - var1;
				if(var9 > 0.0F) {
					GL11.glPushMatrix();
					float var26 = 1.0F + var9 / 5.0F;
					GL11.glTranslatef((float)(var25 + 8), (float)(var21 + 12), 0.0F);
					GL11.glScalef(1.0F / var26, (var26 + 1.0F) / 2.0F, 1.0F);
					GL11.glTranslatef((float)(-(var25 + 8)), (float)(-(var21 + 12)), 0.0F);
				}

				itemRenderer.renderItemIntoGUI(this.mc.renderEngine, var22, var25, var21);
				if(var9 > 0.0F) {
					GL11.glPopMatrix();
				}

				itemRenderer.renderItemOverlayIntoGUI(this.mc.fontRenderer, var22, var25, var21);
			}
		}

		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(GL11.GL_NORMALIZE);
		if(this.mc.options.showFPS) {
			var4.drawStringWithShadow("Minecraft Indev (" + this.mc.debug + ")", 2, 2, 16777215);
			Minecraft var23 = this.mc;
			var4.drawStringWithShadow(var23.renderGlobal.getDebugInfoRenders(), 2, 12, 16777215);
			var23 = this.mc;
			var4.drawStringWithShadow(var23.renderGlobal.getDebugInfoEntities(), 2, 22, 16777215);
			var23 = this.mc;
			var4.drawStringWithShadow("P: " + var23.effectRenderer.getStatistics() + ". T: " + var23.theWorld.debugSkylightUpdates(), 2, 32, 16777215);
			long var24 = EagRuntime.maxMemory();
			long var27 = EagRuntime.totalMemory();
			long var28 = EagRuntime.freeMemory();
			long var16 = var24 - var28;
			String var18 = "Free memory: " + var16 * 100L / var24 + "% of " + var24 / 1024L / 1024L + "MB";
			drawString(var4, var18, var3 - var4.getStringWidth(var18) - 2, 2, 14737632);
			var18 = "Allocated memory: " + var27 * 100L / var24 + "% (" + var27 / 1024L / 1024L + "MB)";
			drawString(var4, var18, var3 - var4.getStringWidth(var18) - 2, 12, 14737632);
		} else {
			if (this.mc.options.showFramerate) var4.drawStringWithShadow("Minecraft Indev (" + this.mc.fps + " fps)", 2, 2, 16777215);
			else var4.drawStringWithShadow("Minecraft Indev", 2, 2, 16777215);
			if (this.mc.options.showCoords) var4.drawStringWithShadow("x: " + (int) Math.floor(this.mc.thePlayer.posX) + ", y: " + (int) Math.floor(this.mc.thePlayer.posY) + ", z: " + (int) Math.floor(this.mc.thePlayer.posZ), 2, 12, 16777215);
		}

		String var23;
		int var26 = 10;
		boolean var28 = false;
		if(this.mc.currentScreen instanceof GuiChat) {
			var26 = 20;
			var28 = true;
		}

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glPushMatrix();
		GL11.glTranslatef(0.0F, (float)(var19 - 48), 0.0F);

		for(int var17 = 0; var17 < this.chatMessageList.size() && var17 < var26; ++var17) {
			if(((ChatLine)this.chatMessageList.get(var17)).updateCounter < 200 || var28) {
				double var31 = (double)((ChatLine)this.chatMessageList.get(var17)).updateCounter / 200.0D;
				var31 = 1.0D - var31;
				var31 *= 10.0D;
				if(var31 < 0.0D) {
					var31 = 0.0D;
				}

				if(var31 > 1.0D) {
					var31 = 1.0D;
				}

				var31 *= var31;
				int var21 = (int)(255.0D * var31);
				if(var28) {
					var21 = 255;
				}

				if(var21 > 0) {
					byte var32 = 2;
					int var22 = -var17 * 9;
					var23 = ((ChatLine)this.chatMessageList.get(var17)).message;
					this.drawRect(var32, var22 - 1, var32 + 320, var22 + 8, var21 / 2 << 24);
					GL11.glEnable(GL11.GL_BLEND);
					var4.drawStringWithShadow(var23, var32, var22, 16777215 + (var21 << 24));
				}
			}
		}

		GL11.glPopMatrix();
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glDisable(GL11.GL_BLEND);

	}

	public final void addChatMessage() {
		++this.updateCounter;

		for(int var1 = 0; var1 < this.chatMessageList.size(); ++var1) {
			++((ChatLine)this.chatMessageList.get(var1)).updateCounter;
		}

	}

	public void addChatMessage(String var1) {
		while(this.mc.fontRenderer.getStringWidth(var1) > 320) {
			int var2;
			for(var2 = 1; var2 < var1.length() && this.mc.fontRenderer.getStringWidth(var1.substring(0, var2 + 1)) <= 320; ++var2) {
			}

			this.addChatMessage(var1.substring(0, var2));
			var1 = var1.substring(var2);
		}

		this.chatMessageList.add(0, new ChatLine(var1));

		while(this.chatMessageList.size() > 50) {
			this.chatMessageList.remove(this.chatMessageList.size() - 1);
		}

	}
}
