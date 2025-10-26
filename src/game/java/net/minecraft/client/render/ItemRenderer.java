package net.minecraft.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.RenderHelper;
import net.minecraft.client.player.EntityPlayerSP;
import net.minecraft.client.render.entity.Render;
import net.minecraft.client.render.entity.RenderManager;
import net.minecraft.client.render.entity.RenderPlayer;
import net.minecraft.game.item.ItemStack;
import net.minecraft.game.level.block.Block;
import org.lwjgl.opengl.GL11;
import util.MathHelper;

public final class ItemRenderer {
	private Minecraft mc;
	private ItemStack itemToRender = null;
	private float equippedProgress = 0.0F;
	private float prevEquippedProgress = 0.0F;
	private int swingProgress = 0;
	private boolean itemSwingState = false;
	private RenderBlocks renderBlocksInstance = new RenderBlocks();

	public ItemRenderer(Minecraft var1) {
		this.mc = var1;
	}

	public final void renderItemInFirstPerson(float var1) {
		float var2 = this.prevEquippedProgress + (this.equippedProgress - this.prevEquippedProgress) * var1;
		EntityPlayerSP var3 = this.mc.thePlayer;
		GL11.glPushMatrix();
		GL11.glRotatef(var3.prevRotationPitch + (var3.rotationPitch - var3.prevRotationPitch) * var1, 1.0F, 0.0F, 0.0F);
		GL11.glRotatef(var3.prevRotationYaw + (var3.rotationYaw - var3.prevRotationYaw) * var1, 0.0F, 1.0F, 0.0F);
		RenderHelper.enableStandardItemLighting();
		GL11.glPopMatrix();
		float var9 = this.mc.theWorld.getLightBrightness((int)var3.posX, (int)var3.posY, (int)var3.posZ);
		GL11.glColor4f(var9, var9, var9, 1.0F);
		float var4;
		float var5;
		if(this.itemToRender != null) {
			GL11.glPushMatrix();
			if(this.itemSwingState) {
				var9 = ((float)this.swingProgress + var1) / 8.0F;
				var4 = MathHelper.sin(var9 * (float)Math.PI);
				var5 = MathHelper.sin(MathHelper.sqrt_float(var9) * (float)Math.PI);
				GL11.glTranslatef(-var5 * 0.4F, MathHelper.sin(MathHelper.sqrt_float(var9) * (float)Math.PI * 2.0F) * 0.2F, -var4 * 0.2F);
			}

			GL11.glTranslatef(0.56F, -0.52F - (1.0F - var2) * 0.6F, -0.71999997F);
			GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
			GL11.glEnable(GL11.GL_NORMALIZE);
			if(this.itemSwingState) {
				var9 = ((float)this.swingProgress + var1) / 8.0F;
				var4 = MathHelper.sin(var9 * var9 * (float)Math.PI);
				var5 = MathHelper.sin(MathHelper.sqrt_float(var9) * (float)Math.PI);
				GL11.glRotatef(-var4 * 20.0F, 0.0F, 1.0F, 0.0F);
				GL11.glRotatef(-var5 * 20.0F, 0.0F, 0.0F, 1.0F);
				GL11.glRotatef(-var5 * 80.0F, 1.0F, 0.0F, 0.0F);
			}

			GL11.glScalef(0.4F, 0.4F, 0.4F);
			if(this.itemToRender.itemID < 256 && Block.blocksList[this.itemToRender.itemID].getRenderType() == 0) {
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/terrain.png"));
				this.renderBlocksInstance.renderBlockOnInventory(Block.blocksList[this.itemToRender.itemID]);
			} else {
				if(this.itemToRender.itemID < 256) {
					GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/terrain.png"));
				} else {
					GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/gui/items.png"));
				}

				Tessellator var11 = Tessellator.instance;
				ItemStack var10 = this.itemToRender;
				var5 = (float)(var10.getItem().getIconIndex() % 16 << 4) / 256.0F;
				var10 = this.itemToRender;
				var1 = (float)((var10.getItem().getIconIndex() % 16 << 4) + 16) / 256.0F;
				var10 = this.itemToRender;
				var2 = (float)(var10.getItem().getIconIndex() / 16 << 4) / 256.0F;
				var10 = this.itemToRender;
				var9 = (float)((var10.getItem().getIconIndex() / 16 << 4) + 16) / 256.0F;
				GL11.glEnable(GL11.GL_NORMALIZE);
				GL11.glTranslatef(0.0F, -0.3F, 0.0F);
				GL11.glScalef(1.5F, 1.5F, 1.5F);
				GL11.glRotatef(50.0F, 0.0F, 1.0F, 0.0F);
				GL11.glRotatef(335.0F, 0.0F, 0.0F, 1.0F);
				GL11.glTranslatef(-(15.0F / 16.0F), -(1.0F / 16.0F), 0.0F);
				var11.setNormal(0.0F, 0.0F, 1.0F);
				var11.startDrawingQuads();
				var11.addVertexWithUV(0.0F, 0.0F, 0.0F, var1, var9);
				var11.addVertexWithUV(1.0F, 0.0F, 0.0F, var5, var9);
				var11.addVertexWithUV(1.0F, 1.0F, 0.0F, var5, var2);
				var11.addVertexWithUV(0.0F, 1.0F, 0.0F, var1, var2);
				var11.draw();
				var11.setNormal(0.0F, 0.0F, -1.0F);
				var11.startDrawingQuads();
				var11.addVertexWithUV(0.0F, 1.0F, -(1.0F / 16.0F), var1, var2);
				var11.addVertexWithUV(1.0F, 1.0F, -(1.0F / 16.0F), var5, var2);
				var11.addVertexWithUV(1.0F, 0.0F, -(1.0F / 16.0F), var5, var9);
				var11.addVertexWithUV(0.0F, 0.0F, -(1.0F / 16.0F), var1, var9);
				var11.draw();
				var11.setNormal(-1.0F, 0.0F, 0.0F);
				var11.startDrawingQuads();

				int var6;
				float var7;
				float var8;
				for(var6 = 0; var6 < 16; ++var6) {
					var7 = (float)var6 / 16.0F;
					var8 = var1 + (var5 - var1) * var7 - 0.001953125F;
					var7 *= 1.0F;
					var11.addVertexWithUV(var7, 0.0F, -(1.0F / 16.0F), var8, var9);
					var11.addVertexWithUV(var7, 0.0F, 0.0F, var8, var9);
					var11.addVertexWithUV(var7, 1.0F, 0.0F, var8, var2);
					var11.addVertexWithUV(var7, 1.0F, -(1.0F / 16.0F), var8, var2);
				}

				var11.draw();
				var11.setNormal(1.0F, 0.0F, 0.0F);
				var11.startDrawingQuads();

				for(var6 = 0; var6 < 16; ++var6) {
					var7 = (float)var6 / 16.0F;
					var8 = var1 + (var5 - var1) * var7 - 0.001953125F;
					var7 = var7 * 1.0F + 1.0F / 16.0F;
					var11.addVertexWithUV(var7, 1.0F, -(1.0F / 16.0F), var8, var2);
					var11.addVertexWithUV(var7, 1.0F, 0.0F, var8, var2);
					var11.addVertexWithUV(var7, 0.0F, 0.0F, var8, var9);
					var11.addVertexWithUV(var7, 0.0F, -(1.0F / 16.0F), var8, var9);
				}

				var11.draw();
				var11.setNormal(0.0F, 1.0F, 0.0F);
				var11.startDrawingQuads();

				for(var6 = 0; var6 < 16; ++var6) {
					var7 = (float)var6 / 16.0F;
					var8 = var9 + (var2 - var9) * var7 - 0.001953125F;
					var7 = var7 * 1.0F + 1.0F / 16.0F;
					var11.addVertexWithUV(0.0F, var7, 0.0F, var1, var8);
					var11.addVertexWithUV(1.0F, var7, 0.0F, var5, var8);
					var11.addVertexWithUV(1.0F, var7, -(1.0F / 16.0F), var5, var8);
					var11.addVertexWithUV(0.0F, var7, -(1.0F / 16.0F), var1, var8);
				}

				var11.draw();
				var11.setNormal(0.0F, -1.0F, 0.0F);
				var11.startDrawingQuads();

				for(var6 = 0; var6 < 16; ++var6) {
					var7 = (float)var6 / 16.0F;
					var8 = var9 + (var2 - var9) * var7 - 0.001953125F;
					var7 *= 1.0F;
					var11.addVertexWithUV(1.0F, var7, 0.0F, var5, var8);
					var11.addVertexWithUV(0.0F, var7, 0.0F, var1, var8);
					var11.addVertexWithUV(0.0F, var7, -(1.0F / 16.0F), var1, var8);
					var11.addVertexWithUV(1.0F, var7, -(1.0F / 16.0F), var5, var8);
				}

				var11.draw();
				GL11.glDisable(GL11.GL_NORMALIZE);
			}

			GL11.glPopMatrix();
		} else {
			GL11.glPushMatrix();
			if(this.itemSwingState) {
				var9 = ((float)this.swingProgress + var1) / 8.0F;
				var4 = MathHelper.sin(var9 * (float)Math.PI);
				var5 = MathHelper.sin(MathHelper.sqrt_float(var9) * (float)Math.PI);
				GL11.glTranslatef(-var5 * 0.3F, MathHelper.sin(MathHelper.sqrt_float(var9) * (float)Math.PI * 2.0F) * 0.4F, -var4 * 0.4F);
			}

			GL11.glTranslatef(0.64000005F, -0.6F - (1.0F - var2) * 0.6F, -0.71999997F);
			GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
			GL11.glEnable(GL11.GL_NORMALIZE);
			if(this.itemSwingState) {
				var9 = ((float)this.swingProgress + var1) / 8.0F;
				var4 = MathHelper.sin(var9 * var9 * (float)Math.PI);
				var5 = MathHelper.sin(MathHelper.sqrt_float(var9) * (float)Math.PI);
				GL11.glRotatef(var5 * 70.0F, 0.0F, 1.0F, 0.0F);
				GL11.glRotatef(-var4 * 20.0F, 0.0F, 0.0F, 1.0F);
			}

			GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTextureForDownloadableImage(this.mc.thePlayer.skinUrl, this.mc.thePlayer.getTexture()));
			GL11.glTranslatef(-0.2F, -0.3F, 0.1F);
			GL11.glRotatef(120.0F, 0.0F, 0.0F, 1.0F);
			GL11.glRotatef(200.0F, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(-135.0F, 0.0F, 1.0F, 0.0F);
			GL11.glScalef(1.0F / 16.0F, 1.0F / 16.0F, 1.0F / 16.0F);
			GL11.glTranslatef(6.0F, 0.0F, 0.0F);
			Render var13 = RenderManager.instance.getEntityRenderObject(this.mc.thePlayer);
			RenderPlayer var12 = (RenderPlayer)var13;
			var12.drawFirstPersonHand();
			GL11.glPopMatrix();
		}

		GL11.glDisable(GL11.GL_NORMALIZE);
		RenderHelper.disableStandardItemLighting();
	}

	public final void renderOverlays(float var1) {
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		int var2;
		Tessellator var3;
		float var7;
		float var9;
		if(this.mc.thePlayer.fire > 0) {
			var2 = this.mc.renderEngine.getTexture("/terrain.png");
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, var2);
			var3 = Tessellator.instance;
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.9F);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

			for(var2 = 0; var2 < 2; ++var2) {
				GL11.glPushMatrix();
				int var4 = Block.fire.blockIndexInTexture + (var2 << 4);
				int var5 = (var4 & 15) << 4;
				var4 &= 240;
				float var6 = (float)var5 / 256.0F;
				float var10 = ((float)var5 + 15.99F) / 256.0F;
				var7 = (float)var4 / 256.0F;
				var9 = ((float)var4 + 15.99F) / 256.0F;
				GL11.glTranslatef((float)(-((var2 << 1) - 1)) * 0.24F, -0.3F, 0.0F);
				GL11.glRotatef((float)((var2 << 1) - 1) * 10.0F, 0.0F, 1.0F, 0.0F);
				var3.startDrawingQuads();
				var3.addVertexWithUV(-0.5F, -0.5F, -0.5F, var10, var9);
				var3.addVertexWithUV(0.5F, -0.5F, -0.5F, var6, var9);
				var3.addVertexWithUV(0.5F, 0.5F, -0.5F, var6, var7);
				var3.addVertexWithUV(-0.5F, 0.5F, -0.5F, var10, var7);
				var3.draw();
				GL11.glPopMatrix();
			}

			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glDisable(GL11.GL_BLEND);
		}

		if(this.mc.thePlayer.isInsideOfWater()) {
			var2 = this.mc.renderEngine.getTexture("/water.png");
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, var2);
			var3 = Tessellator.instance;
			float var8 = this.mc.thePlayer.getEntityBrightness(var1);
			GL11.glColor4f(var8, var8, var8, 0.5F);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glPushMatrix();
			var7 = -this.mc.thePlayer.rotationYaw / 64.0F;
			var9 = this.mc.thePlayer.rotationPitch / 64.0F;
			var3.startDrawingQuads();
			var3.addVertexWithUV(-1.0F, -1.0F, -0.5F, var7 + 4.0F, var9 + 4.0F);
			var3.addVertexWithUV(1.0F, -1.0F, -0.5F, var7 + 0.0F, var9 + 4.0F);
			var3.addVertexWithUV(1.0F, 1.0F, -0.5F, var7 + 0.0F, var9 + 0.0F);
			var3.addVertexWithUV(-1.0F, 1.0F, -0.5F, var7 + 4.0F, var9 + 0.0F);
			var3.draw();
			GL11.glPopMatrix();
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glDisable(GL11.GL_BLEND);
		}

		GL11.glEnable(GL11.GL_ALPHA_TEST);
	}

	public final void updateEquippedItem() {
		this.prevEquippedProgress = this.equippedProgress;
		if(this.itemSwingState) {
			++this.swingProgress;
			if(this.swingProgress == 8) {
				this.swingProgress = 0;
				this.itemSwingState = false;
			}
		}

		EntityPlayerSP var1 = this.mc.thePlayer;
		ItemStack var3 = var1.inventory.getCurrentItem();
		float var2 = var3 == this.itemToRender ? 1.0F : 0.0F;
		var2 -= this.equippedProgress;
		if(var2 < -0.4F) {
			var2 = -0.4F;
		}

		if(var2 > 0.4F) {
			var2 = 0.4F;
		}

		this.equippedProgress += var2;
		if(this.equippedProgress < 0.1F) {
			this.itemToRender = var3;
		}

	}

	public final void equipAnimationSpeed() {
		this.equippedProgress = 0.0F;
	}

	public final void equippedItemRender() {
		this.swingProgress = -1;
		this.itemSwingState = true;
	}

	public final void resetEquippedProgress() {
		this.equippedProgress = 0.0F;
	}
}
