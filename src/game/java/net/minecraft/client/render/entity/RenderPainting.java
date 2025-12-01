package net.minecraft.client.render.entity;

import net.lax1dude.eaglercraft.Random;
import net.minecraft.client.render.Tessellator;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.EntityPainting;
import net.minecraft.game.entity.EnumArt;
import org.lwjgl.opengl.GL11;

public final class RenderPainting extends Render {
	private Random rand = new Random();

	public final void doRender(Entity var1, float var2, float var3, float var4, float var5, float var6) {
		EntityPainting var22 = (EntityPainting) var1;
		this.rand.setSeed(187L);
		GL11.glPushMatrix();
		GL11.glTranslatef(var2, var3, var4);
		GL11.glRotatef(var5, 0.0F, 1.0F, 0.0F);
		GL11.glEnable(GL11.GL_NORMALIZE);
		this.loadTexture("/art/kz.png");
		EnumArt var23 = var22.art;
		GL11.glScalef(1.0F / 16.0F, 1.0F / 16.0F, 1.0F / 16.0F);
		int var27 = var23.offsetY;
		int var26 = var23.offsetX;
		int var25 = var23.sizeY;
		int var24 = var23.sizeX;
		var22 = var22;
		RenderPainting var21 = this;
		float var7 = (float) (-var24) / 2.0F;
		float var8 = (float) (-var25) / 2.0F;

		for (int var9 = 0; var9 < var24 / 16; ++var9) {
			for (int var10 = 0; var10 < var25 / 16; ++var10) {
				float var11 = var7 + (float) (var9 + 1 << 4);
				float var12 = var7 + (float) (var9 << 4);
				float var13 = var8 + (float) (var10 + 1 << 4);
				float var14 = var8 + (float) (var10 << 4);
				float var10002 = (var11 + var12) / 2.0F;
				float var18 = (var13 + var14) / 2.0F;
				float var17 = var10002;
				int var19 = (int) var22.posX;
				int var28 = (int) (var22.posY + var18 / 16.0F);
				int var20 = (int) var22.posZ;
				if (var22.direction == 0) {
					var19 = (int) (var22.posX + var17 / 16.0F);
				}

				if (var22.direction == 1) {
					var20 = (int) (var22.posZ - var17 / 16.0F);
				}

				if (var22.direction == 2) {
					var19 = (int) (var22.posX - var17 / 16.0F);
				}

				if (var22.direction == 3) {
					var20 = (int) (var22.posZ + var17 / 16.0F);
				}

				float var15 = var21.renderManager.worldObj.getLightBrightness(var19, var28, var20);
				GL11.glColor3f(var15, var15, var15);
				var15 = (float) (var26 + var24 - (var9 << 4)) / 256.0F;
				float var16 = (float) (var26 + var24 - (var9 + 1 << 4)) / 256.0F;
				var17 = (float) (var27 + var25 - (var10 << 4)) / 256.0F;
				var18 = (float) (var27 + var25 - (var10 + 1 << 4)) / 256.0F;
				Tessellator var29 = Tessellator.instance;
				var29.startDrawingQuads();
				var29.setNormal(0.0F, 0.0F, -1.0F);
				var29.addVertexWithUV(var11, var14, -0.5F, var16, var17);
				var29.addVertexWithUV(var12, var14, -0.5F, var15, var17);
				var29.addVertexWithUV(var12, var13, -0.5F, var15, var18);
				var29.addVertexWithUV(var11, var13, -0.5F, var16, var18);
				var29.setNormal(0.0F, 0.0F, 1.0F);
				var29.addVertexWithUV(var11, var13, 0.5F, 12.0F / 16.0F, 0.0F);
				var29.addVertexWithUV(var12, var13, 0.5F, 13.0F / 16.0F, 0.0F);
				var29.addVertexWithUV(var12, var14, 0.5F, 13.0F / 16.0F, 1.0F / 16.0F);
				var29.addVertexWithUV(var11, var14, 0.5F, 12.0F / 16.0F, 1.0F / 16.0F);
				var29.setNormal(0.0F, -1.0F, 0.0F);
				var29.addVertexWithUV(var11, var13, -0.5F, 12.0F / 16.0F, 0.001953125F);
				var29.addVertexWithUV(var12, var13, -0.5F, 13.0F / 16.0F, 0.001953125F);
				var29.addVertexWithUV(var12, var13, 0.5F, 13.0F / 16.0F, 0.001953125F);
				var29.addVertexWithUV(var11, var13, 0.5F, 12.0F / 16.0F, 0.001953125F);
				var29.setNormal(0.0F, 1.0F, 0.0F);
				var29.addVertexWithUV(var11, var14, 0.5F, 12.0F / 16.0F, 0.001953125F);
				var29.addVertexWithUV(var12, var14, 0.5F, 13.0F / 16.0F, 0.001953125F);
				var29.addVertexWithUV(var12, var14, -0.5F, 13.0F / 16.0F, 0.001953125F);
				var29.addVertexWithUV(var11, var14, -0.5F, 12.0F / 16.0F, 0.001953125F);
				var29.setNormal(-1.0F, 0.0F, 0.0F);
				var29.addVertexWithUV(var11, var13, 0.5F, 385.0F / 512.0F, 0.0F);
				var29.addVertexWithUV(var11, var14, 0.5F, 385.0F / 512.0F, 1.0F / 16.0F);
				var29.addVertexWithUV(var11, var14, -0.5F, 385.0F / 512.0F, 1.0F / 16.0F);
				var29.addVertexWithUV(var11, var13, -0.5F, 385.0F / 512.0F, 0.0F);
				var29.setNormal(1.0F, 0.0F, 0.0F);
				var29.addVertexWithUV(var12, var13, -0.5F, 385.0F / 512.0F, 0.0F);
				var29.addVertexWithUV(var12, var14, -0.5F, 385.0F / 512.0F, 1.0F / 16.0F);
				var29.addVertexWithUV(var12, var14, 0.5F, 385.0F / 512.0F, 1.0F / 16.0F);
				var29.addVertexWithUV(var12, var13, 0.5F, 385.0F / 512.0F, 0.0F);
				var29.draw();
			}
		}

		GL11.glDisable(GL11.GL_NORMALIZE);
		GL11.glPopMatrix();
	}
}
