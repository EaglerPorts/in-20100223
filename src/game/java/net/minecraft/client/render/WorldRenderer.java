package net.minecraft.client.render;

import net.minecraft.client.render.camera.ICamera;
import net.minecraft.client.render.entity.RenderItem;
import net.minecraft.game.entity.EntityLiving;
import net.minecraft.game.level.World;
import net.minecraft.game.level.block.Block;
import net.minecraft.game.physics.AxisAlignedBB;
import org.lwjgl.opengl.GL11;
import util.MathHelper;

public final class WorldRenderer {
	private World worldObj;
	private int glRenderList = -1;
	private static Tessellator tessellator = Tessellator.instance;
	public static int chunksUpdated = 0;
	private int posX;
	private int posY;
	private int posZ;
	private int sizeWidth;
	private int sizeHeight;
	private int sizeDepth;
	public boolean isInFrustrum = false;
	private boolean[] skipRenderPass = new boolean[2];
	private int posXPlus;
	private int posYPlus;
	private int posZPlus;
	public boolean needsUpdate;
	private AxisAlignedBB rendererBoundingBox;
	private RenderBlocks renderBlocks;
	public boolean isVisible = true;
	public boolean isWaitingOnOcclusionQuery;
	public int glOcclusionQuery;

	public WorldRenderer(World var1, int var2, int var3, int var4, int var5, int var6) {
		this.renderBlocks = new RenderBlocks(var1);
		this.worldObj = var1;
		this.posX = var2;
		this.posY = var3;
		this.posZ = var4;
		this.sizeWidth = this.sizeHeight = this.sizeDepth = 16;
		this.posXPlus = var2 + this.sizeWidth / 2;
		this.posYPlus = var3 + this.sizeHeight / 2;
		this.posZPlus = var4 + this.sizeDepth / 2;
		MathHelper.sqrt_float((float)(this.sizeWidth * this.sizeWidth + this.sizeHeight * this.sizeHeight + this.sizeDepth * this.sizeDepth));
		this.rendererBoundingBox = (new AxisAlignedBB((float)var2, (float)var3, (float)var4, (float)(var2 + this.sizeWidth), (float)(var3 + this.sizeHeight), (float)(var4 + this.sizeDepth))).expand(2.0F, 2.0F, 2.0F);
		this.glRenderList = var6;
		this.setDontDraw();
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glNewList(var6 + 2, GL11.GL_COMPILE);
		RenderItem.renderOffsetAABB(this.rendererBoundingBox);
		GL11.glEndList();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}

	public final void updateRenderer() {
		if(this.needsUpdate) {
			++chunksUpdated;
			int var1 = this.posX;
			int var2 = this.posY;
			int var3 = this.posZ;
			int var4 = this.posX + this.sizeWidth;
			int var5 = this.posY + this.sizeHeight;
			int var6 = this.posZ + this.sizeDepth;

			int var7;
			for(var7 = 0; var7 < 2; ++var7) {
				this.skipRenderPass[var7] = true;
			}

			for(var7 = 0; var7 < 2; ++var7) {
				boolean var8 = false;
				boolean var9 = false;
				tessellator.startDrawingQuads();
				GL11.glNewList(this.glRenderList + var7, GL11.GL_COMPILE);

				for(int var10 = var2; var10 < var5; ++var10) {
					for(int var11 = var3; var11 < var6; ++var11) {
						int var12 = (var10 * this.worldObj.length + var11) * this.worldObj.width + var1;

						for(int var13 = var1; var13 < var4; ++var13) {
							int var14 = this.worldObj.blocks[var12++] & 255;
							if(var14 > 0) {
								Block var15 = Block.blocksList[var14];
								if(var15.getRenderBlockPass() != var7) {
									var8 = true;
								} else {
									var9 |= this.renderBlocks.renderBlockByRenderType(var15, var13, var10, var11);
								}
							}
						}
					}
				}

				tessellator.draw();
				GL11.glEndList();
				if(var9) {
					this.skipRenderPass[var7] = false;
				}

				if(!var8) {
					break;
				}
			}

		}
	}

	public final float distanceToEntitySquared(EntityLiving var1) {
		float var2 = var1.posX - (float)this.posXPlus;
		float var3 = var1.posY - (float)this.posYPlus;
		float var4 = var1.posZ - (float)this.posZPlus;
		return var2 * var2 + var3 * var3 + var4 * var4;
	}

	private void setDontDraw() {
		for(int var1 = 0; var1 < 2; ++var1) {
			this.skipRenderPass[var1] = true;
		}

	}

	public final void stopRendering() {
		this.setDontDraw();
		this.worldObj = null;
	}

	public final int getGLCallListForPass(int[] var1, int var2, int var3) {
		if(!this.isInFrustrum) {
			return var2;
		} else {
			if(!this.skipRenderPass[var3]) {
				var1[var2++] = this.glRenderList + var3;
			}

			return var2;
		}
	}

	public final void updateInFrustrum(ICamera var1) {
		this.isInFrustrum = var1.isBoundingBoxInFrustrum(this.rendererBoundingBox);
	}

	public final void callOcclusionQueryList() {
		GL11.glCallList(this.glRenderList + 2);
	}
}
