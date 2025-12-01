package net.minecraft.client.render;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import net.lax1dude.eaglercraft.Random;

import net.lax1dude.eaglercraft.internal.buffer.IntBuffer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.effect.EntityBubbleFX;
import net.minecraft.client.effect.EntityExplodeFX;
import net.minecraft.client.effect.EntityFlameFX;
import net.minecraft.client.effect.EntityLavaFX;
import net.minecraft.client.effect.EntitySmokeFX;
import net.minecraft.client.effect.EntitySplashFX;
import net.minecraft.client.render.camera.ICamera;
import net.minecraft.client.render.entity.RenderManager;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.item.ItemStack;
import net.minecraft.game.level.EntityMap;
import net.minecraft.game.level.IWorldAccess;
import net.minecraft.game.level.World;
import net.minecraft.game.level.block.Block;
import net.minecraft.game.physics.AxisAlignedBB;
import net.minecraft.game.physics.MovingObjectPosition;
import net.minecraft.game.physics.Vec3D;
import org.lwjgl.opengl.GL11;

import util.GLAllocation;
import util.MathHelper;

public final class RenderGlobal implements IWorldAccess {
	private World worldObj;
	private RenderEngine renderEngine;
	private int glGenList;
	private IntBuffer renderIntBuffer = GLAllocation.createIntBuffer(65536);
	private List worldRenderersToUpdate = new ArrayList();
	private WorldRenderer[] sortedWorldRenderers;
	private WorldRenderer[] worldRenderers;
	private int renderChunksWide;
	private int renderChunksTall;
	private int renderChunksDeep;
	private int glRenderListBase;
	private Minecraft mc;
	private RenderBlocks globalRenderBlocks;
	private int cloudOffsetX = 0;
	private int glSkyList;
	private int countEntitiesTotal;
	private int countEntitiesRendered;
	private int countEntitiesHidden;
	private int[] dummyBuf50k = new int['\uc350'];
	private int renderersLoaded;
	private int renderersBeingClipped;
	private int renderersBeingOccluded;
	private int renderersBeingRendered;
	private float prevSortX = -9999.0F;
	private float prevSortY = -9999.0F;
	private float prevSortZ = -9999.0F;
	public float damagePartialTime;

	public RenderGlobal(Minecraft var1, RenderEngine var2) {
		this.mc = var1;
		this.renderEngine = var2;
		this.glGenList = GL11.glGenLists(2);
		this.glRenderListBase = GL11.glGenLists(786432);

		this.glSkyList = GL11.glGenLists(1);
		GL11.glNewList(this.glSkyList, GL11.GL_COMPILE);
		Random var5 = new Random(10842L);

		for (int var6 = 0; var6 < 500; ++var6) {
			GL11.glRotatef(var5.nextFloat() * 360.0F, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(var5.nextFloat() * 360.0F, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(var5.nextFloat() * 360.0F, 0.0F, 0.0F, 1.0F);
			Tessellator var3 = Tessellator.instance;
			float var4 = 0.25F + var5.nextFloat() * 0.25F;
			var3.startDrawingQuads();
			var3.addVertexWithUV(-var4, -100.0F, var4, 1.0F, 1.0F);
			var3.addVertexWithUV(var4, -100.0F, var4, 0.0F, 1.0F);
			var3.addVertexWithUV(var4, -100.0F, -var4, 0.0F, 0.0F);
			var3.addVertexWithUV(-var4, -100.0F, -var4, 1.0F, 0.0F);
			var3.draw();
		}

		GL11.glEndList();
	}

	public final void changeWorld(World var1) {
		if (this.worldObj != null) {
			this.worldObj.removeWorldAccess(this);
		}

		this.prevSortX = -9999.0F;
		this.prevSortY = -9999.0F;
		this.prevSortZ = -9999.0F;
		RenderManager.instance.set(var1);
		this.worldObj = var1;
		this.globalRenderBlocks = new RenderBlocks(var1);
		if (var1 != null) {
			var1.addWorldAccess(this);
			this.loadRenderers();
		}

	}

	public final void loadRenderers() {
		int var1;
		if (this.worldRenderers != null) {
			for (var1 = 0; var1 < this.worldRenderers.length; ++var1) {
				this.worldRenderers[var1].stopRendering();
			}
		}

		this.renderChunksWide = this.worldObj.width / 16;
		this.renderChunksTall = this.worldObj.height / 16;
		this.renderChunksDeep = this.worldObj.length / 16;
		this.worldRenderers = new WorldRenderer[this.renderChunksWide * this.renderChunksTall * this.renderChunksDeep];
		this.sortedWorldRenderers = new WorldRenderer[this.renderChunksWide * this.renderChunksTall
				* this.renderChunksDeep];
		var1 = 0;
		int var2 = 0;

		int var3;
		for (var3 = 0; var3 < this.renderChunksWide; ++var3) {
			for (int var4 = 0; var4 < this.renderChunksTall; ++var4) {
				for (int var5 = 0; var5 < this.renderChunksDeep; ++var5) {
					this.worldRenderers[(var5 * this.renderChunksTall + var4) * this.renderChunksWide + var3] = new WorldRenderer(
							this.worldObj, var3 << 4, var4 << 4, var5 << 4, 16, this.glRenderListBase + var1);

					++var2;
					this.sortedWorldRenderers[(var5 * this.renderChunksTall + var4) * this.renderChunksWide
							+ var3] = this.worldRenderers[(var5 * this.renderChunksTall + var4) * this.renderChunksWide + var3];
					var1 += 3;
				}
			}
		}

		for (var3 = 0; var3 < this.worldRenderersToUpdate.size(); ++var3) {
			((WorldRenderer) this.worldRenderersToUpdate.get(var3)).needsUpdate = false;
		}

		this.worldRenderersToUpdate.clear();
		GL11.glNewList(this.glGenList, GL11.GL_COMPILE);
		this.oobGroundRenderHeight();
		GL11.glEndList();
		GL11.glNewList(this.glGenList + 1, GL11.GL_COMPILE);
		this.oobWaterRenderHeight();
		GL11.glEndList();
		this.markBlocksForUpdate(0, 0, 0, this.worldObj.width, this.worldObj.height, this.worldObj.length);
	}

	public final void renderEntities(Vec3D var1, ICamera var2, float var3) {
		EntityMap var4 = this.worldObj.entityMap;
		RenderManager.instance.cacheActiveRenderInfo(this.worldObj, this.renderEngine, this.mc.thePlayer, var3);
		this.countEntitiesTotal = 0;
		this.countEntitiesRendered = 0;
		this.countEntitiesHidden = 0;

		for (int var5 = 0; var5 < var4.width; ++var5) {
			for (int var6 = 0; var6 < var4.depth; ++var6) {
				for (int var7 = 0; var7 < var4.height; ++var7) {
					List var8 = var4.entityGrid[(var7 * var4.depth + var6) * var4.width + var5];
					if (var8.size() != 0) {
						int var9 = (var5 << 3) + 4;
						int var10 = (var6 << 3) + 4;
						int var11 = (var7 << 3) + 4;
						this.countEntitiesTotal += var8.size();
						float var10001 = (float) var9;
						float var10002 = (float) var10;
						float var14 = (float) var11;
						float var13 = var10002;
						float var12 = var10001;
						boolean var10000;
						if (var12 >= 0.0F && var13 >= 0.0F && var14 >= 0.0F && var12 < (float) this.worldObj.width
								&& var13 < (float) this.worldObj.height && var14 < (float) this.worldObj.length) {
							int var17 = (int) (var12 / 16.0F);
							int var19 = (int) (var13 / 16.0F);
							int var20 = (int) (var14 / 16.0F);
							var10000 = this.worldRenderers[(var20 * this.renderChunksTall + var19) * this.renderChunksWide
									+ var17].isInFrustrum
									&& this.worldRenderers[(var20 * this.renderChunksTall + var19) * this.renderChunksWide
											+ var17].isVisible;
						} else {
							var10000 = true;
						}

						if (!var10000) {
							this.countEntitiesHidden += var8.size();
						} else {
							for (var9 = 0; var9 < var8.size(); ++var9) {
								Entity var15 = (Entity) var8.get(var9);
								var13 = var15.posX - var1.xCoord;
								var14 = var15.posY - var1.yCoord;
								var12 = var15.posZ - var1.zCoord;
								var13 = var13 * var13 + var14 * var14 + var12 * var12;
								float var16 = var13;
								AxisAlignedBB var18 = var15.boundingBox;
								var13 = var18.maxX - var18.minX;
								var14 = var18.maxY - var18.minY;
								var12 = var18.maxZ - var18.minZ;
								var12 = (var13 + var14 + var12) / 3.0F;
								var12 *= 64.0F;
								if (var16 < var12 * var12 && var2.isBoundingBoxInFrustrum(var15.boundingBox)
										&& (var15 != this.worldObj.playerEntity || this.mc.options.thirdPersonView)) {
									++this.countEntitiesRendered;
									RenderManager.instance.renderEntity(var15, var3);
								}
							}
						}
					}
				}
			}
		}

	}

	public final String getDebugInfoRenders() {
		return "C: " + this.renderersBeingRendered + "/" + this.renderersLoaded + ". F: " + this.renderersBeingClipped
				+ ", O: " + this.renderersBeingOccluded;
	}

	public final String getDebugInfoEntities() {
		return "E: " + this.countEntitiesRendered + "/" + this.countEntitiesTotal + ". B: " + this.countEntitiesHidden
				+ ", I: " + (this.countEntitiesTotal - this.countEntitiesHidden - this.countEntitiesRendered);
	}

	public final int sortAndRender(EntityPlayer var1, int var2) {
		if (var2 == 0) {
			this.renderersLoaded = 0;
			this.renderersBeingClipped = 0;
			this.renderersBeingOccluded = 0;
			this.renderersBeingRendered = 0;
		}

		float var3 = var1.posX - this.prevSortX;
		float var4 = var1.posY - this.prevSortY;
		float var5 = var1.posZ - this.prevSortZ;
		if (var3 * var3 + var4 * var4 + var5 * var5 > 16.0F) {
			this.prevSortX = var1.posX;
			this.prevSortY = var1.posY;
			this.prevSortZ = var1.posZ;
			Arrays.sort(this.sortedWorldRenderers, new EntitySorter(var1));
		}

		int var8 = 0 + this.renderSortedRenderers(0, this.sortedWorldRenderers.length, var2);

		return var8;
	}

	private int renderSortedRenderers(int var1, int var2, int var3) {
		int var4 = 0;

		for (var1 = var1; var1 < var2; ++var1) {
			if (var3 == 0) {
				++this.renderersLoaded;
				if (!this.sortedWorldRenderers[var1].isInFrustrum) {
					++this.renderersBeingClipped;
				}

				if (this.sortedWorldRenderers[var1].isInFrustrum && !this.sortedWorldRenderers[var1].isVisible) {
					++this.renderersBeingOccluded;
				}

				if (this.sortedWorldRenderers[var1].isInFrustrum && this.sortedWorldRenderers[var1].isVisible) {
					++this.renderersBeingRendered;
				}
			}

			if (this.sortedWorldRenderers[var1].isInFrustrum && this.sortedWorldRenderers[var1].isVisible) {
				var4 = this.sortedWorldRenderers[var1].getGLCallListForPass(this.dummyBuf50k, var4, var3);
			}
		}

		this.renderIntBuffer.clear();
		this.renderIntBuffer.put(this.dummyBuf50k, 0, var4);
		this.renderIntBuffer.flip();
		if (this.renderIntBuffer.remaining() > 0) {
			GL11.glCallLists(this.renderIntBuffer);
		}

		return this.renderIntBuffer.remaining();
	}

	public final void renderAllRenderLists() {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.renderEngine.getTexture("/terrain.png"));
		GL11.glCallLists(this.renderIntBuffer);
	}

	public final void updateClouds() {
		++this.cloudOffsetX;
	}

	public final void renderSky(float var1) {
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		Vec3D var2 = this.worldObj.getSkyColor(var1);
		float var3 = var2.xCoord;
		float var4 = var2.yCoord;
		float var9 = var2.zCoord;
		if (this.mc.options.anaglyph) {
			float var5 = (var3 * 30.0F + var4 * 59.0F + var9 * 11.0F) / 100.0F;
			var4 = (var3 * 30.0F + var4 * 70.0F) / 100.0F;
			var9 = (var3 * 30.0F + var9 * 70.0F) / 100.0F;
			var3 = var5;
			var4 = var4;
			var9 = var9;
		}

		GL11.glDepthMask(false);
		Tessellator var12 = Tessellator.instance;
		var12.startDrawingQuads();
		var12.setColorOpaque_F(var3, var4, var9);
		var9 = (float) (this.worldObj.height + 10);

		int var10;
		for (var10 = -2048; var10 < this.worldObj.width + 2048; var10 += 512) {
			for (int var13 = -2048; var13 < this.worldObj.length + 2048; var13 += 512) {
				var12.addVertex((float) var10, var9, (float) var13);
				var12.addVertex((float) (var10 + 512), var9, (float) var13);
				var12.addVertex((float) (var10 + 512), var9, (float) (var13 + 512));
				var12.addVertex((float) var10, var9, (float) (var13 + 512));
			}
		}

		var12.draw();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_FOG);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
		GL11.glPushMatrix();
		var3 = this.worldObj.playerEntity.lastTickPosX
				+ (this.worldObj.playerEntity.posX - this.worldObj.playerEntity.lastTickPosX) * var1;
		var4 = this.worldObj.playerEntity.lastTickPosY
				+ (this.worldObj.playerEntity.posY - this.worldObj.playerEntity.lastTickPosY) * var1;
		float var6 = this.worldObj.playerEntity.lastTickPosZ
				+ (this.worldObj.playerEntity.posZ - this.worldObj.playerEntity.lastTickPosZ) * var1;
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glTranslatef(var3, var4, var6);
		GL11.glRotatef(0.0F, 0.0F, 0.0F, 1.0F);
		GL11.glRotatef(this.worldObj.getCelestialAngle(var1) * 360.0F, 1.0F, 0.0F, 0.0F);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.renderEngine.getTexture("/terrain/sun.png"));
		var12.startDrawingQuads();
		var12.addVertexWithUV(-30.0F, 100.0F, -30.0F, 0.0F, 0.0F);
		var12.addVertexWithUV(30.0F, 100.0F, -30.0F, 1.0F, 0.0F);
		var12.addVertexWithUV(30.0F, 100.0F, 30.0F, 1.0F, 1.0F);
		var12.addVertexWithUV(-30.0F, 100.0F, 30.0F, 0.0F, 1.0F);
		var12.draw();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.renderEngine.getTexture("/terrain/moon.png"));
		var12.startDrawingQuads();
		var12.addVertexWithUV(-20.0F, -100.0F, 20.0F, 1.0F, 1.0F);
		var12.addVertexWithUV(20.0F, -100.0F, 20.0F, 0.0F, 1.0F);
		var12.addVertexWithUV(20.0F, -100.0F, -20.0F, 0.0F, 0.0F);
		var12.addVertexWithUV(-20.0F, -100.0F, -20.0F, 1.0F, 0.0F);
		var12.draw();
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		var9 = this.worldObj.getStarBrightness(var1);
		GL11.glColor4f(var9, var9, var9, var9);
		GL11.glCallList(this.glSkyList);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glEnable(GL11.GL_FOG);
		GL11.glPopMatrix();
		GL11.glDepthMask(true);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.renderEngine.getTexture("/clouds.png"));
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		Vec3D var11 = this.worldObj.getCloudColor(var1);
		var4 = var11.xCoord;
		var6 = var11.yCoord;
		var3 = var11.zCoord;
		float var7;
		if (this.mc.options.anaglyph) {
			var9 = (var4 * 30.0F + var6 * 59.0F + var3 * 11.0F) / 100.0F;
			var7 = (var4 * 30.0F + var6 * 70.0F) / 100.0F;
			var3 = (var4 * 30.0F + var3 * 70.0F) / 100.0F;
			var4 = var9;
			var6 = var7;
			var3 = var3;
		}

		var9 = (float) this.worldObj.cloudHeight;
		var7 = ((float) this.cloudOffsetX + var1) * (0.5F / 1024.0F) * 0.03F;
		var12.startDrawingQuads();
		var12.setColorOpaque_F(var4, var6, var3);

		for (int var8 = -2048; var8 < this.worldObj.width + 2048; var8 += 512) {
			for (var10 = -2048; var10 < this.worldObj.length + 2048; var10 += 512) {
				var12.addVertexWithUV((float) var8, var9, (float) (var10 + 512), (float) var8 * (0.5F / 1024.0F) + var7,
						(float) (var10 + 512) * (0.5F / 1024.0F));
				var12.addVertexWithUV((float) (var8 + 512), var9, (float) (var10 + 512),
						(float) (var8 + 512) * (0.5F / 1024.0F) + var7, (float) (var10 + 512) * (0.5F / 1024.0F));
				var12.addVertexWithUV((float) (var8 + 512), var9, (float) var10, (float) (var8 + 512) * (0.5F / 1024.0F) + var7,
						(float) var10 * (0.5F / 1024.0F));
				var12.addVertexWithUV((float) var8, var9, (float) var10, (float) var8 * (0.5F / 1024.0F) + var7,
						(float) var10 * (0.5F / 1024.0F));
				var12.addVertexWithUV((float) var8, var9, (float) var10, (float) var8 * (0.5F / 1024.0F) + var7,
						(float) var10 * (0.5F / 1024.0F));
				var12.addVertexWithUV((float) (var8 + 512), var9, (float) var10, (float) (var8 + 512) * (0.5F / 1024.0F) + var7,
						(float) var10 * (0.5F / 1024.0F));
				var12.addVertexWithUV((float) (var8 + 512), var9, (float) (var10 + 512),
						(float) (var8 + 512) * (0.5F / 1024.0F) + var7, (float) (var10 + 512) * (0.5F / 1024.0F));
				var12.addVertexWithUV((float) var8, var9, (float) (var10 + 512), (float) var8 * (0.5F / 1024.0F) + var7,
						(float) (var10 + 512) * (0.5F / 1024.0F));
			}
		}

		var12.draw();
	}

	public final void oobGroundRenderer() {
		float var1 = this.worldObj.getLightBrightness(0, this.worldObj.getGroundLevel(), 0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.renderEngine.getTexture("/dirt.png"));
		if (this.worldObj.getGroundLevel() > this.worldObj.getWaterLevel()
				&& this.worldObj.defaultFluid == Block.waterMoving.blockID) {
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.renderEngine.getTexture("/grass.png"));
		}

		GL11.glColor4f(var1, var1, var1, 1.0F);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glCallList(this.glGenList);
	}

	private void oobGroundRenderHeight() {
		Tessellator var1 = Tessellator.instance;
		float var2 = (float) this.worldObj.getGroundLevel();
		int var3 = 128;
		if (128 > this.worldObj.width) {
			var3 = this.worldObj.width;
		}

		if (var3 > this.worldObj.length) {
			var3 = this.worldObj.length;
		}

		int var4 = 2048 / var3;
		var1.startDrawingQuads();

		for (int var5 = -var3 * var4; var5 < this.worldObj.width + var3 * var4; var5 += var3) {
			for (int var6 = -var3 * var4; var6 < this.worldObj.length + var3 * var4; var6 += var3) {
				if (var2 < 0.0F || var5 < 0 || var6 < 0 || var5 >= this.worldObj.width || var6 >= this.worldObj.length) {
					var1.addVertexWithUV((float) var5, var2, (float) (var6 + var3), 0.0F, (float) var3);
					var1.addVertexWithUV((float) (var5 + var3), var2, (float) (var6 + var3), (float) var3, (float) var3);
					var1.addVertexWithUV((float) (var5 + var3), var2, (float) var6, (float) var3, 0.0F);
					var1.addVertexWithUV((float) var5, var2, (float) var6, 0.0F, 0.0F);
				}
			}
		}

		var1.draw();
	}

	public final void oobWaterRenderer() {
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.renderEngine.getTexture("/water.png"));
		float var1 = this.worldObj.getLightBrightness(0, this.worldObj.getWaterLevel(), 0);
		GL11.glColor4f(var1, var1, var1, 1.0F);
		GL11.glCallList(this.glGenList + 1);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.renderEngine.getTexture("/terrain.png"));
	}

	private void oobWaterRenderHeight() {
		float var1 = (float) this.worldObj.getWaterLevel();
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		Tessellator var2 = Tessellator.instance;
		int var3 = 128;
		if (128 > this.worldObj.width) {
			var3 = this.worldObj.width;
		}

		if (var3 > this.worldObj.length) {
			var3 = this.worldObj.length;
		}

		int var4 = 2048 / var3;
		var2.startDrawingQuads();
		float var5 = Block.waterMoving.minX;
		float var6 = Block.waterMoving.minZ;

		for (int var7 = -var3 * var4; var7 < this.worldObj.width + var3 * var4; var7 += var3) {
			for (int var8 = -var3 * var4; var8 < this.worldObj.length + var3 * var4; var8 += var3) {
				float var9 = var1 + Block.waterMoving.minY;
				if (var1 < 0.0F || var7 < 0 || var8 < 0 || var7 >= this.worldObj.width || var8 >= this.worldObj.length) {
					var2.addVertexWithUV((float) var7 + var5, var9, (float) (var8 + var3) + var6, 0.0F, (float) var3);
					var2.addVertexWithUV((float) (var7 + var3) + var5, var9, (float) (var8 + var3) + var6, (float) var3,
							(float) var3);
					var2.addVertexWithUV((float) (var7 + var3) + var5, var9, (float) var8 + var6, (float) var3, 0.0F);
					var2.addVertexWithUV((float) var7 + var5, var9, (float) var8 + var6, 0.0F, 0.0F);
					var2.addVertexWithUV((float) var7 + var5, var9, (float) var8 + var6, 0.0F, 0.0F);
					var2.addVertexWithUV((float) (var7 + var3) + var5, var9, (float) var8 + var6, (float) var3, 0.0F);
					var2.addVertexWithUV((float) (var7 + var3) + var5, var9, (float) (var8 + var3) + var6, (float) var3,
							(float) var3);
					var2.addVertexWithUV((float) var7 + var5, var9, (float) (var8 + var3) + var6, 0.0F, (float) var3);
				}
			}
		}

		var2.draw();
		GL11.glDisable(GL11.GL_BLEND);
	}

	public final void updateRenderers(EntityPlayer var1) {
		Collections.sort(this.worldRenderersToUpdate, new RenderSorter(var1));
		int var2 = this.worldRenderersToUpdate.size() - 1;
		int var3 = this.worldRenderersToUpdate.size();

		for (int var4 = 0; var4 < var3; ++var4) {
			WorldRenderer var5 = (WorldRenderer) this.worldRenderersToUpdate.get(var2 - var4);
			if (var5.distanceToEntitySquared(var1) > 2500.0F && var4 > 4) {
				return;
			}

			this.worldRenderersToUpdate.remove(var5);
			var5.updateRenderer();
			var5.needsUpdate = false;
		}

	}

	public final void drawBlockBreaking(MovingObjectPosition var1, int var2, ItemStack var3) {
		Tessellator var4 = Tessellator.instance;
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
		GL11.glColor4f(1.0F, 1.0F, 1.0F,
				(MathHelper.sin((float) System.currentTimeMillis() / 100.0F) * 0.2F + 0.4F) * 0.5F);
		if (this.damagePartialTime > 0.0F) {
			GL11.glBlendFunc(GL11.GL_DST_COLOR, GL11.GL_SRC_COLOR);
			int var5 = this.renderEngine.getTexture("/terrain.png");
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, var5);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.5F);
			GL11.glPushMatrix();
			var5 = this.worldObj.getBlockId(var1.blockX, var1.blockY, var1.blockZ);
			Block var6 = var5 > 0 ? Block.blocksList[var5] : null;
			GL11.glDisable(GL11.GL_ALPHA_TEST);
			var4.startDrawingQuads();
			var4.disableColor();
			if (var6 == null) {
				var6 = Block.stone;
			}

			this.globalRenderBlocks.renderBlockUsingTexture(var6, var1.blockX, var1.blockY, var1.blockZ,
					240 + (int) (this.damagePartialTime * 10.0F));
			var4.draw();
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			GL11.glDepthMask(true);
			GL11.glPopMatrix();
		}

		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
	}

	public final void drawSelectionBox(MovingObjectPosition var1, int var2) {
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.4F);
		GL11.glLineWidth(2.0F);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDepthMask(false);
		var2 = this.worldObj.getBlockId(var1.blockX, var1.blockY, var1.blockZ);
		if (var2 > 0) {
			AxisAlignedBB var3 = Block.blocksList[var2].getSelectedBoundingBoxFromPool(var1.blockX, var1.blockY, var1.blockZ)
					.expand(0.002F, 0.002F, 0.002F);
			Tessellator var4 = Tessellator.instance;
			var4.startDrawing(3);
			var4.addVertex(var3.minX, var3.minY, var3.minZ);
			var4.addVertex(var3.maxX, var3.minY, var3.minZ);
			var4.addVertex(var3.maxX, var3.minY, var3.maxZ);
			var4.addVertex(var3.minX, var3.minY, var3.maxZ);
			var4.addVertex(var3.minX, var3.minY, var3.minZ);
			var4.draw();
			var4.startDrawing(3);
			var4.addVertex(var3.minX, var3.maxY, var3.minZ);
			var4.addVertex(var3.maxX, var3.maxY, var3.minZ);
			var4.addVertex(var3.maxX, var3.maxY, var3.maxZ);
			var4.addVertex(var3.minX, var3.maxY, var3.maxZ);
			var4.addVertex(var3.minX, var3.maxY, var3.minZ);
			var4.draw();
			var4.startDrawing(1);
			var4.addVertex(var3.minX, var3.minY, var3.minZ);
			var4.addVertex(var3.minX, var3.maxY, var3.minZ);
			var4.addVertex(var3.maxX, var3.minY, var3.minZ);
			var4.addVertex(var3.maxX, var3.maxY, var3.minZ);
			var4.addVertex(var3.maxX, var3.minY, var3.maxZ);
			var4.addVertex(var3.maxX, var3.maxY, var3.maxZ);
			var4.addVertex(var3.minX, var3.minY, var3.maxZ);
			var4.addVertex(var3.minX, var3.maxY, var3.maxZ);
			var4.draw();
		}

		GL11.glDepthMask(true);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
	}

	private void markBlocksForUpdate(int var1, int var2, int var3, int var4, int var5, int var6) {
		var1 /= 16;
		var2 /= 16;
		var3 /= 16;
		var4 /= 16;
		var5 /= 16;
		var6 /= 16;
		if (var1 < 0) {
			var1 = 0;
		}

		if (var2 < 0) {
			var2 = 0;
		}

		if (var3 < 0) {
			var3 = 0;
		}

		if (var4 > this.renderChunksWide - 1) {
			var4 = this.renderChunksWide - 1;
		}

		if (var5 > this.renderChunksTall - 1) {
			var5 = this.renderChunksTall - 1;
		}

		if (var6 > this.renderChunksDeep - 1) {
			var6 = this.renderChunksDeep - 1;
		}

		for (var1 = var1; var1 <= var4; ++var1) {
			for (int var7 = var2; var7 <= var5; ++var7) {
				for (int var8 = var3; var8 <= var6; ++var8) {
					WorldRenderer var9 = this.worldRenderers[(var8 * this.renderChunksTall + var7) * this.renderChunksWide
							+ var1];
					if (!var9.needsUpdate) {
						var9.needsUpdate = true;
						this.worldRenderersToUpdate
								.add(this.worldRenderers[(var8 * this.renderChunksTall + var7) * this.renderChunksWide + var1]);
					}
				}
			}
		}

	}

	public final void markBlockAndNeighborsNeedsUpdate(int var1, int var2, int var3) {
		this.markBlocksForUpdate(var1 - 1, var2 - 1, var3 - 1, var1 + 1, var2 + 1, var3 + 1);
	}

	public final void markBlockRangeNeedsUpdate(int var1, int var2, int var3, int var4, int var5, int var6) {
		this.markBlocksForUpdate(var1 - 1, var2 - 1, var3 - 1, var4 + 1, var5 + 1, var6 + 1);
	}

	public final void clipRenderersByFrustrum(ICamera var1) {
		for (int var2 = 0; var2 < this.worldRenderers.length; ++var2) {
			this.worldRenderers[var2].updateInFrustrum(var1);
		}

	}

	public final void playSound(String var1, float var2, float var3, float var4, float var5, float var6) {
		this.mc.sndManager.playSound(var1, var2, var3, var4, var5, var6);
	}

	public final void spawnParticle(String var1, float var2, float var3, float var4, float var5, float var6, float var7) {
		float var8 = this.worldObj.playerEntity.posX - var2;
		float var9 = this.worldObj.playerEntity.posY - var3;
		float var10 = this.worldObj.playerEntity.posZ - var4;
		if (var8 * var8 + var9 * var9 + var10 * var10 <= 256.0F) {
			if (var1 == "bubble") {
				this.mc.effectRenderer.addEffect(new EntityBubbleFX(this.worldObj, var2, var3, var4, var5, var6, var7));
			} else if (var1 == "smoke") {
				this.mc.effectRenderer.addEffect(new EntitySmokeFX(this.worldObj, var2, var3, var4));
			} else if (var1 == "explode") {
				this.mc.effectRenderer.addEffect(new EntityExplodeFX(this.worldObj, var2, var3, var4, var5, var6, var7));
			} else if (var1 == "flame") {
				this.mc.effectRenderer.addEffect(new EntityFlameFX(this.worldObj, var2, var3, var4));
			} else if (var1 == "lava") {
				this.mc.effectRenderer.addEffect(new EntityLavaFX(this.worldObj, var2, var3, var4));
			} else if (var1 == "splash") {
				this.mc.effectRenderer.addEffect(new EntitySplashFX(this.worldObj, var2, var3, var4));
			} else {
				if (var1 == "largesmoke") {
					this.mc.effectRenderer.addEffect(new EntitySmokeFX(this.worldObj, var2, var3, var4, 2.5F));
				}

			}
		}
	}

	public final void playMusic(String var1, float var2, float var3, float var4, float var5) {
		this.mc.sndManager.playRandomMusicIfReady();
	}

	public final void obtainEntitySkin(Entity var1) {

	}

	public final void releaseEntitySkin(Entity var1) {

	}

	public final void updateAllRenderers() {
		GL11.glNewList(this.glGenList, GL11.GL_COMPILE);
		this.oobGroundRenderHeight();
		GL11.glEndList();
		GL11.glNewList(this.glGenList + 1, GL11.GL_COMPILE);
		this.oobWaterRenderHeight();
		GL11.glEndList();
	}
}
