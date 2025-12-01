package net.minecraft.client.render.entity;

import net.lax1dude.eaglercraft.Random;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.render.RenderBlocks;
import net.minecraft.client.render.RenderEngine;
import net.minecraft.client.render.Tessellator;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.misc.EntityItem;
import net.minecraft.game.item.ItemStack;
import net.minecraft.game.level.block.Block;
import org.lwjgl.opengl.GL11;
import util.MathHelper;

public final class RenderItem extends Render {
	private RenderBlocks renderBlocks = new RenderBlocks();
	private Random random = new Random();

	public RenderItem() {
		this.shadowSize = 0.15F;
		this.shadowOpaque = 12.0F / 16.0F;
	}

	public final void renderItemIntoGUI(RenderEngine var1, ItemStack var2, int var3, int var4) {
		if (var2 != null) {
			int var9;
			if (var2.itemID < 256 && Block.blocksList[var2.itemID].getRenderType() == 0) {
				var9 = var2.itemID;
				RenderEngine.bindTexture(var1.getTexture("/terrain.png"));
				Block var8 = Block.blocksList[var9];
				GL11.glPushMatrix();
				GL11.glTranslatef((float) (var3 - 2), (float) (var4 + 3), 0.0F);
				GL11.glScalef(10.0F, 10.0F, 10.0F);
				GL11.glTranslatef(1.0F, 0.5F, 8.0F);
				GL11.glRotatef(210.0F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				this.renderBlocks.renderBlockOnInventory(var8);
				GL11.glPopMatrix();
			} else {
				if (var2.getItem().getIconIndex() >= 0) {
					GL11.glDisable(GL11.GL_LIGHTING);
					if (var2.itemID < 256) {
						RenderEngine.bindTexture(var1.getTexture("/terrain.png"));
					} else {
						RenderEngine.bindTexture(var1.getTexture("/gui/items.png"));
					}

					int var10000 = var3;
					int var10001 = var4;
					int var10002 = var2.getItem().getIconIndex() % 16 << 4;
					int var10003 = var2.getItem().getIconIndex() / 16 << 4;
					boolean var6 = true;
					var6 = true;
					var4 = var10003;
					var3 = var10002;
					var9 = var10001;
					int var7 = var10000;
					Tessellator var5 = Tessellator.instance;
					var5.startDrawingQuads();
					var5.addVertexWithUV((float) var7, (float) (var9 + 16), 0.0F, (float) var3 * 0.00390625F,
							(float) (var4 + 16) * 0.00390625F);
					var5.addVertexWithUV((float) (var7 + 16), (float) (var9 + 16), 0.0F, (float) (var3 + 16) * 0.00390625F,
							(float) (var4 + 16) * 0.00390625F);
					var5.addVertexWithUV((float) (var7 + 16), (float) var9, 0.0F, (float) (var3 + 16) * 0.00390625F,
							(float) var4 * 0.00390625F);
					var5.addVertexWithUV((float) var7, (float) var9, 0.0F, (float) var3 * 0.00390625F,
							(float) var4 * 0.00390625F);
					var5.draw();
					GL11.glEnable(GL11.GL_LIGHTING);
				}

			}
		}
	}

	public final void renderItemOverlayIntoGUI(FontRenderer var1, ItemStack var2, int var3, int var4) {
		if (var2 != null) {
			if (var2.stackSize > 1) {
				String var5 = "" + var2.stackSize;
				GL11.glDisable(GL11.GL_LIGHTING);
				GL11.glDisable(GL11.GL_DEPTH_TEST);
				var1.drawStringWithShadow(var5, var3 + 19 - 2 - var1.getStringWidth(var5), var4 + 6 + 3, 16777215);
				GL11.glEnable(GL11.GL_LIGHTING);
				GL11.glEnable(GL11.GL_DEPTH_TEST);
			}

			if (var2.itemDamage > 0) {
				int var9 = 13 - var2.itemDamage * 13 / var2.isItemStackDamageable();
				int var7 = 255 - var2.itemDamage * 255 / var2.isItemStackDamageable();
				GL11.glDisable(GL11.GL_LIGHTING);
				GL11.glDisable(GL11.GL_DEPTH_TEST);
				GL11.glDisable(GL11.GL_TEXTURE_2D);
				Tessellator var8 = Tessellator.instance;
				int var6 = 255 - var7 << 16 | var7 << 8;
				var7 = (255 - var7) / 4 << 16 | 16128;
				renderQuad(var8, var3 + 2, var4 + 13, 13, 2, 0);
				renderQuad(var8, var3 + 2, var4 + 13, 12, 1, var7);
				renderQuad(var8, var3 + 2, var4 + 13, var9, 1, var6);
				GL11.glEnable(GL11.GL_TEXTURE_2D);
				GL11.glEnable(GL11.GL_LIGHTING);
				GL11.glEnable(GL11.GL_DEPTH_TEST);
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			}

		}
	}

	private static void renderQuad(Tessellator var0, int var1, int var2, int var3, int var4, int var5) {
		var0.startDrawingQuads();
		var0.setColorOpaque_I(var5);
		var0.addVertex((float) var1, (float) var2, 0.0F);
		var0.addVertex((float) var1, (float) (var2 + var4), 0.0F);
		var0.addVertex((float) (var1 + var3), (float) (var2 + var4), 0.0F);
		var0.addVertex((float) (var1 + var3), (float) var2, 0.0F);
		var0.draw();
	}

	public final void doRender(Entity var1, float var2, float var3, float var4, float var5, float var6) {
		EntityItem var13 = (EntityItem) var1;
		RenderItem var12 = this;
		this.random.setSeed(187L);
		ItemStack var7 = var13.item;
		GL11.glPushMatrix();
		float var8 = MathHelper.sin(((float) var13.age + var6) / 10.0F + var13.hoverStart) * 0.1F + 0.1F;
		var6 = (((float) var13.age + var6) / 20.0F + var13.hoverStart) * (180.0F / (float) Math.PI);
		byte var9 = 1;
		if (var13.item.stackSize > 1) {
			var9 = 2;
		}

		if (var13.item.stackSize > 5) {
			var9 = 3;
		}

		if (var13.item.stackSize > 20) {
			var9 = 4;
		}

		GL11.glTranslatef(var2, var3 + var8, var4);
		GL11.glEnable(GL11.GL_NORMALIZE);
		if (var7.itemID < 256 && Block.blocksList[var7.itemID].getRenderType() == 0) {
			GL11.glRotatef(var6, 0.0F, 1.0F, 0.0F);
			this.loadTexture("/terrain.png");
			var2 = 0.25F;
			if (!Block.blocksList[var7.itemID].renderAsNormalBlock() && var7.itemID != Block.stairSingle.blockID) {
				var2 = 0.5F;
			}

			GL11.glScalef(var2, var2, var2);

			for (int var16 = 0; var16 < var9; ++var16) {
				GL11.glPushMatrix();
				if (var16 > 0) {
					var4 = (var12.random.nextFloat() * 2.0F - 1.0F) * 0.2F / var2;
					var5 = (var12.random.nextFloat() * 2.0F - 1.0F) * 0.2F / var2;
					var6 = (var12.random.nextFloat() * 2.0F - 1.0F) * 0.2F / var2;
					GL11.glTranslatef(var4, var5, var6);
				}

				var12.renderBlocks.renderBlockOnInventory(Block.blocksList[var7.itemID]);
				GL11.glPopMatrix();
			}
		} else {
			GL11.glScalef(0.5F, 0.5F, 0.5F);
			int var14 = var7.getItem().getIconIndex();
			if (var7.itemID < 256) {
				this.loadTexture("/terrain.png");
			} else {
				this.loadTexture("/gui/items.png");
			}

			Tessellator var15 = Tessellator.instance;
			var4 = (float) (var14 % 16 << 4) / 256.0F;
			var5 = (float) ((var14 % 16 << 4) + 16) / 256.0F;
			var6 = (float) (var14 / 16 << 4) / 256.0F;
			var2 = (float) ((var14 / 16 << 4) + 16) / 256.0F;

			for (int var17 = 0; var17 < var9; ++var17) {
				GL11.glPushMatrix();
				if (var17 > 0) {
					var8 = (var12.random.nextFloat() * 2.0F - 1.0F) * 0.3F;
					float var10 = (var12.random.nextFloat() * 2.0F - 1.0F) * 0.3F;
					float var11 = (var12.random.nextFloat() * 2.0F - 1.0F) * 0.3F;
					GL11.glTranslatef(var8, var10, var11);
				}

				GL11.glRotatef(180.0F - var12.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
				var15.startDrawingQuads();
				var15.setNormal(0.0F, 1.0F, 0.0F);
				var15.addVertexWithUV(-0.5F, -0.25F, 0.0F, var4, var2);
				var15.addVertexWithUV(0.5F, -0.25F, 0.0F, var5, var2);
				var15.addVertexWithUV(0.5F, 12.0F / 16.0F, 0.0F, var5, var6);
				var15.addVertexWithUV(-0.5F, 12.0F / 16.0F, 0.0F, var4, var6);
				var15.draw();
				GL11.glPopMatrix();
			}
		}

		GL11.glDisable(GL11.GL_NORMALIZE);
		GL11.glPopMatrix();
	}
}
