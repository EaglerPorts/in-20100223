package net.minecraft.client.render.entity;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.render.RenderBlocks;
import net.minecraft.client.render.RenderEngine;
import net.minecraft.client.render.Tessellator;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.level.World;
import net.minecraft.game.level.block.Block;
import net.minecraft.game.physics.AxisAlignedBB;
import org.lwjgl.opengl.GL11;

public abstract class Render {
	protected RenderManager renderManager;
	protected float shadowSize;
	protected float shadowOpaque;

	public Render() {
		new ModelBiped();
		new RenderBlocks();
		this.shadowSize = 0.0F;
		this.shadowOpaque = 1.0F;
	}

	public abstract void doRender(Entity var1, float var2, float var3, float var4, float var5, float var6);

	protected final void loadTexture(String var1) {
		RenderEngine var2 = this.renderManager.renderEngine;
		RenderEngine.bindTexture(var2.getTexture(var1));
	}

	protected final void loadDownloadableImageTexture(String var1, String var2) {
		RenderEngine var3 = this.renderManager.renderEngine;
		RenderEngine.bindTexture(var3.getTextureForDownloadableImage(var1, var2));
	}

	public static void renderOffsetAABB(AxisAlignedBB var0) {
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		Tessellator var1 = Tessellator.instance;
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		var1.startDrawingQuads();
		var1.setNormal(0.0F, 0.0F, -1.0F);
		var1.addVertex(var0.minX, var0.maxY, var0.minZ);
		var1.addVertex(var0.maxX, var0.maxY, var0.minZ);
		var1.addVertex(var0.maxX, var0.minY, var0.minZ);
		var1.addVertex(var0.minX, var0.minY, var0.minZ);
		var1.setNormal(0.0F, 0.0F, 1.0F);
		var1.addVertex(var0.minX, var0.minY, var0.maxZ);
		var1.addVertex(var0.maxX, var0.minY, var0.maxZ);
		var1.addVertex(var0.maxX, var0.maxY, var0.maxZ);
		var1.addVertex(var0.minX, var0.maxY, var0.maxZ);
		var1.setNormal(0.0F, -1.0F, 0.0F);
		var1.addVertex(var0.minX, var0.minY, var0.minZ);
		var1.addVertex(var0.maxX, var0.minY, var0.minZ);
		var1.addVertex(var0.maxX, var0.minY, var0.maxZ);
		var1.addVertex(var0.minX, var0.minY, var0.maxZ);
		var1.setNormal(0.0F, 1.0F, 0.0F);
		var1.addVertex(var0.minX, var0.maxY, var0.maxZ);
		var1.addVertex(var0.maxX, var0.maxY, var0.maxZ);
		var1.addVertex(var0.maxX, var0.maxY, var0.minZ);
		var1.addVertex(var0.minX, var0.maxY, var0.minZ);
		var1.setNormal(-1.0F, 0.0F, 0.0F);
		var1.addVertex(var0.minX, var0.minY, var0.maxZ);
		var1.addVertex(var0.minX, var0.maxY, var0.maxZ);
		var1.addVertex(var0.minX, var0.maxY, var0.minZ);
		var1.addVertex(var0.minX, var0.minY, var0.minZ);
		var1.setNormal(1.0F, 0.0F, 0.0F);
		var1.addVertex(var0.maxX, var0.minY, var0.minZ);
		var1.addVertex(var0.maxX, var0.maxY, var0.minZ);
		var1.addVertex(var0.maxX, var0.maxY, var0.maxZ);
		var1.addVertex(var0.maxX, var0.minY, var0.maxZ);
		var1.draw();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}

	public final void setRenderManager(RenderManager var1) {
		this.renderManager = var1;
	}

	public final void renderShadow(Entity var1, float var2, float var3, float var4, float var5) {
		float var12;
		float var18;
		float var19;
		int var29;
		float var34;
		float var36;
		if(this.shadowSize > 0.0F) {
			var5 = this.renderManager.getDistanceToCamera(var2, var3, var4);
			var5 = (1.0F - var5 / 256.0F) * this.shadowOpaque;
			if(var5 > 0.0F) {
				float var9 = var5;
				float var8 = var4;
				float var7 = var3;
				float var6 = var2;
				Render var27 = this;
				GL11.glEnable(GL11.GL_BLEND);
				RenderEngine var10 = this.renderManager.renderEngine;
				RenderEngine.bindTexture(var10.getTexture("%%/shadow.png"));
				World var11 = this.renderManager.worldObj;
				GL11.glDepthMask(false);
				var12 = this.shadowSize;

				for(var29 = (int)(var2 - var12); var29 <= (int)(var6 + var12); ++var29) {
					for(int var13 = (int)(var7 - 2.0F); var13 <= (int)var7; ++var13) {
						for(int var14 = (int)(var8 - var12); var14 <= (int)(var8 + var12); ++var14) {
							int var15 = var11.getBlockId(var29, var13 - 1, var14);
							if(var15 > 0 && var11.getBlockLightValue(var29, var13, var14) > 3) {
								Block var16 = Block.blocksList[var15];
								Tessellator var25 = Tessellator.instance;
								var34 = (var9 - (var7 - (float)var13) / 2.0F) * 0.5F * var27.renderManager.worldObj.getLightBrightness(var29, var13, var14);
								if(var34 >= 0.0F) {
									GL11.glColor4f(1.0F, 1.0F, 1.0F, var34);
									var25.startDrawingQuads();
									var34 = (float)var29 + var16.minX;
									var18 = (float)var29 + var16.maxX;
									float var20 = (float)var13 + var16.minY;
									float var21 = (float)var14 + var16.minZ;
									var36 = (float)var14 + var16.maxZ;
									float var22 = (var6 - var34) / 2.0F / var12 + 0.5F;
									float var17 = (var6 - var18) / 2.0F / var12 + 0.5F;
									float var23 = (var8 - var21) / 2.0F / var12 + 0.5F;
									var19 = (var8 - var36) / 2.0F / var12 + 0.5F;
									var25.addVertexWithUV(var34, var20, var21, var22, var23);
									var25.addVertexWithUV(var34, var20, var36, var22, var19);
									var25.addVertexWithUV(var18, var20, var36, var17, var19);
									var25.addVertexWithUV(var18, var20, var21, var17, var23);
									var25.draw();
								}
							}
						}
					}
				}

				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				GL11.glDisable(GL11.GL_BLEND);
				GL11.glDepthMask(true);
			}
		}

		if(var1.fire > 0) {
			GL11.glDisable(GL11.GL_LIGHTING);
			int var26 = Block.fire.blockIndexInTexture;
			var29 = (var26 & 15) << 4;
			int var30 = var26 & 240;
			var12 = (float)var29 / 256.0F;
			float var31 = ((float)var29 + 15.99F) / 256.0F;
			float var32 = (float)var30 / 256.0F;
			float var33 = ((float)var30 + 15.99F) / 256.0F;
			GL11.glPushMatrix();
			GL11.glTranslatef(var2, var3, var4);
			var34 = var1.width * 1.4F;
			GL11.glScalef(var34, var34, var34);
			this.loadTexture("/terrain.png");
			Tessellator var35 = Tessellator.instance;
			var36 = 1.0F;
			var18 = 0.0F;
			var19 = var1.height / var1.width;
			GL11.glRotatef(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
			GL11.glTranslatef(0.0F, 0.0F, 0.4F + (float)((int)var19) * 0.02F);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			var35.startDrawingQuads();

			while(var19 > 0.0F) {
				var35.addVertexWithUV(var36 - 0.5F, 0.0F - var18, 0.0F, var31, var33);
				var35.addVertexWithUV(-0.5F, 0.0F - var18, 0.0F, var12, var33);
				var35.addVertexWithUV(-0.5F, 1.4F - var18, 0.0F, var12, var32);
				var35.addVertexWithUV(var36 - 0.5F, 1.4F - var18, 0.0F, var31, var32);
				--var19;
				--var18;
				var36 *= 0.9F;
				GL11.glTranslatef(0.0F, 0.0F, -0.04F);
			}

			var35.draw();
			GL11.glPopMatrix();
			GL11.glEnable(GL11.GL_LIGHTING);
		}

	}
}
