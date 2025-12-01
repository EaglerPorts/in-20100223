package net.minecraft.client.render;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.util.List;
import net.lax1dude.eaglercraft.Random;

import net.lax1dude.eaglercraft.internal.buffer.ByteBuffer;
import net.lax1dude.eaglercraft.internal.buffer.FloatBuffer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.RenderHelper;
import net.minecraft.client.controller.PlayerControllerCreative;
import net.minecraft.client.effect.EffectRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.player.EntityPlayerSP;
import net.minecraft.client.render.camera.ClippingHelperImplementation;
import net.minecraft.client.render.camera.Frustrum;
import net.minecraft.client.render.camera.IsomCamera;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.level.World;
import net.minecraft.game.level.block.Block;
import net.minecraft.game.level.material.Material;
import net.minecraft.game.physics.AxisAlignedBB;
import net.minecraft.game.physics.MovingObjectPosition;
import net.minecraft.game.physics.Vec3D;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import util.GLAllocation;
import util.MathHelper;

public final class EntityRenderer {
	private Minecraft mc;
	private boolean anaglyphEnable = false;
	private float farPlaneDistance = 0.0F;
	public ItemRenderer itemRenderer;
	private int rendererUpdateCount;
	private Entity pointedEntity = null;
	private int entityRendererInt1;
	private int entityRendererInt2;
	private DecimalFormat entityDecimalFormat = new DecimalFormat("0000");
	private ByteBuffer entityByteBuffer;
	private FloatBuffer entityFloatBuffer = GLAllocation.createFloatBuffer(16);
	private Random random = new Random();
	private volatile int unusedInt0 = 0;
	private volatile int unusedInt1 = 0;
	private FloatBuffer fogColorBuffer = GLAllocation.createFloatBuffer(16);
	private float fogColorRed;
	private float fogColorGreen;
	private float fogColorBlue;
	private float prevFogColor;
	private float fogColor;

	public EntityRenderer(Minecraft var1) {
		this.mc = var1;
		this.itemRenderer = new ItemRenderer(var1);
	}

	public final void updateRenderer() {
		this.prevFogColor = this.fogColor;
		float var1 = this.mc.theWorld.getLightBrightness((int) this.mc.thePlayer.posX, (int) this.mc.thePlayer.posY,
				(int) this.mc.thePlayer.posZ);
		float var2 = (float) (3 - this.mc.options.renderDistance) / 3.0F;
		var1 = var1 * (1.0F - var2) + var2;
		this.fogColor += (var1 - this.fogColor) * 0.1F;
		++this.rendererUpdateCount;
		this.itemRenderer.updateEquippedItem();
	}

	private Vec3D orientCamera(float var1) {
		EntityPlayerSP var2 = this.mc.thePlayer;
		float var3 = var2.prevPosX + (var2.posX - var2.prevPosX) * var1;
		float var4 = var2.prevPosY + (var2.posY - var2.prevPosY) * var1;
		var1 = var2.prevPosZ + (var2.posZ - var2.prevPosZ) * var1;
		return new Vec3D(var3, var4, var1);
	}

	private void hurtCameraEffect(float var1) {
		EntityPlayerSP var2 = this.mc.thePlayer;
		float var3 = (float) var2.hurtTime - var1;
		if (var2.health <= 0) {
			var1 += (float) var2.deathTime;
			GL11.glRotatef(40.0F - 8000.0F / (var1 + 200.0F), 0.0F, 0.0F, 1.0F);
		}

		if (var3 >= 0.0F) {
			var3 /= (float) var2.maxHurtTime;
			var3 = MathHelper.sin(var3 * var3 * var3 * var3 * (float) Math.PI);
			var1 = var2.attackedAtYaw;
			GL11.glRotatef(-var1, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(-var3 * 14.0F, 0.0F, 0.0F, 1.0F);
			GL11.glRotatef(var1, 0.0F, 1.0F, 0.0F);
		}
	}

	private void setupViewBobbing(float var1) {
		if (!this.mc.options.thirdPersonView) {
			EntityPlayerSP var2 = this.mc.thePlayer;
			float var3 = var2.distanceWalkedModified - var2.prevDistanceWalkedModified;
			var3 = var2.distanceWalkedModified + var3 * var1;
			float var4 = var2.prevCameraYaw + (var2.cameraYaw - var2.prevCameraYaw) * var1;
			var1 = var2.prevCameraPitch + (var2.cameraPitch - var2.prevCameraPitch) * var1;
			GL11.glTranslatef(MathHelper.sin(var3 * (float) Math.PI) * var4 * 0.5F,
					-Math.abs(MathHelper.cos(var3 * (float) Math.PI) * var4), 0.0F);
			GL11.glRotatef(MathHelper.sin(var3 * (float) Math.PI) * var4 * 3.0F, 0.0F, 0.0F, 1.0F);
			GL11.glRotatef(Math.abs(MathHelper.cos(var3 * (float) Math.PI + 0.2F) * var4) * 5.0F, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(var1, 1.0F, 0.0F, 0.0F);
		}
	}

	public final void updateCameraAndRender(float var1) {
		if (this.anaglyphEnable && !Display.isActive()) {
			this.mc.displayInGameMenu();
		}

		this.anaglyphEnable = Display.isActive();
		int var5;
		int var6;
		if (this.mc.inventoryScreen) {
			this.mc.mouseHelper.mouseXYChange();
			Mouse.getDX();
			byte var2 = 0;
			Mouse.getDY();
			byte var3 = 0;
			byte var4 = -1;
			if (this.mc.options.invertMouse) {
				var4 = 1;
			}

			var5 = var2 + this.mc.mouseHelper.deltaX;
			var6 = var3 - this.mc.mouseHelper.deltaY;
			/*
			 * if(var2 != 0 || this.entityRendererInt1 != 0) {
			 * System.out.println("xxo: " + var2 + ", " + this.entityRendererInt1 + ": " +
			 * this.entityRendererInt1 + ", xo: " + var5);
			 * }
			 */

			if (this.entityRendererInt1 != 0) {
				this.entityRendererInt1 = 0;
			}

			if (this.entityRendererInt2 != 0) {
				this.entityRendererInt2 = 0;
			}

			if (var2 != 0) {
				this.entityRendererInt1 = var2;
			}

			if (var3 != 0) {
				this.entityRendererInt2 = var3;
			}

			float var10001 = (float) var5;
			float var11 = (float) (var6 * var4);
			float var9 = var10001;
			EntityPlayerSP var7 = this.mc.thePlayer;
			float var13 = var7.rotationPitch;
			float var14 = var7.rotationYaw;
			var7.rotationYaw = (float) ((double) var7.rotationYaw + (double) var9 * 0.15D);
			var7.rotationPitch = (float) ((double) var7.rotationPitch - (double) var11 * 0.15D);
			if (var7.rotationPitch < -90.0F) {
				var7.rotationPitch = -90.0F;
			}

			if (var7.rotationPitch > 90.0F) {
				var7.rotationPitch = 90.0F;
			}

			var7.prevRotationPitch += var7.rotationPitch - var13;
			var7.prevRotationYaw += var7.rotationYaw - var14;
		} else {
			this.mc.mouseHelper.ungrabMouseCursor();
		}

		ScaledResolution var8 = new ScaledResolution(this.mc.displayWidth, this.mc.displayHeight);
		int var10 = var8.getScaledWidth();
		int var12 = var8.getScaledHeight();
		var5 = Mouse.getX() * var10 / this.mc.displayWidth;
		var6 = var12 - Mouse.getY() * var12 / this.mc.displayHeight - 1;
		if (this.mc.theWorld != null) {
			this.getMouseOver(var1);
			this.mc.ingameGUI.renderGameOverlay(var1);
		} else {
			GL11.glViewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
			GL11.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
			GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_COLOR_BUFFER_BIT);
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glLoadIdentity();
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			GL11.glLoadIdentity();
			this.setupOverlayRendering();
		}

		if (this.mc.currentScreen != null) {
			GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
			this.mc.currentScreen.drawScreen(var5, var6, var1);
		}
	}

	/*
	 * public final void grabLargeScreenshot() {
	 * this.mc.loadingScreen.displayProgressMessage("Grabbing large screenshot");
	 * File var1 = new File(System.getProperty("user.home", "."));
	 * int var2 = 0;
	 * 
	 * while(true) {
	 * File var3 = new File(var1, "mc_map_" +
	 * this.entityDecimalFormat.format((long)var2) + ".png");
	 * if(!var3.exists()) {
	 * var3 = var3.getAbsoluteFile();
	 * this.mc.loadingScreen.displayLoadingString("Rendering");
	 * this.mc.loadingScreen.setLoadingProgress(0);
	 * 
	 * try {
	 * int var19 = (this.mc.theWorld.width << 4) + (this.mc.theWorld.length << 4);
	 * var2 = (this.mc.theWorld.height << 4) + var19 / 2;
	 * BufferedImage var4 = new BufferedImage(var19, var2, 1);
	 * Graphics var5 = var4.getGraphics();
	 * int var6 = this.mc.displayWidth;
	 * int var7 = this.mc.displayHeight;
	 * int var8 = (var19 / var6 + 1) * (var2 / var7 + 1);
	 * int var9 = 0;
	 * 
	 * for(int var10 = 0; var10 < var19; var10 += var6) {
	 * for(int var11 = 0; var11 < var2; var11 += var7) {
	 * ++var9;
	 * this.mc.loadingScreen.setLoadingProgress(var9 * 100 / var8);
	 * int var10001 = var10 - var19 / 2;
	 * int var10002 = var11 - var2 / 2;
	 * float var12 = 0.0F;
	 * int var14 = var10002;
	 * int var13 = var10001;
	 * if(this.entityByteBuffer == null) {
	 * this.entityByteBuffer =
	 * GLAllocation.createDirectByteBuffer(this.mc.displayWidth *
	 * this.mc.displayHeight << 2);
	 * }
	 * 
	 * EntityPlayerSP var15 = this.mc.thePlayer;
	 * World var16 = this.mc.theWorld;
	 * RenderGlobal var17 = this.mc.renderGlobal;
	 * GL11.glViewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
	 * this.updateFogColor(0.0F);
	 * GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_COLOR_BUFFER_BIT);
	 * GL11.glEnable(GL11.GL_CULL_FACE);
	 * this.farPlaneDistance = (float)(512 >> (this.mc.options.renderDistance <<
	 * 1));
	 * GL11.glMatrixMode(GL11.GL_PROJECTION);
	 * GL11.glLoadIdentity();
	 * GL11.glOrtho(0.0D, (double)this.mc.displayWidth, 0.0D,
	 * (double)this.mc.displayHeight, 10.0D, 10000.0D);
	 * GL11.glMatrixMode(GL11.GL_MODELVIEW);
	 * GL11.glLoadIdentity();
	 * GL11.glTranslatef((float)(-var13), (float)(-var14), -5000.0F);
	 * GL11.glScalef(16.0F, -16.0F, -16.0F);
	 * float var22 = 1.0F;
	 * var22 = 0.0F;
	 * var22 = 0.0F;
	 * var22 = 0.0F;
	 * var22 = 0.0F;
	 * var22 = 0.0F;
	 * var22 = 0.5F;
	 * var22 = 1.0F;
	 * var22 = 0.0F;
	 * var22 = -1.0F;
	 * var22 = 1.0F;
	 * var22 = 0.0F;
	 * var22 = 0.0F;
	 * var22 = 0.0F;
	 * var22 = -0.5F;
	 * var22 = 1.0F;
	 * this.entityFloatBuffer.clear();
	 * this.entityFloatBuffer.put(1.0F).put(-0.5F).put(0.0F).put(0.0F);
	 * this.entityFloatBuffer.put(0.0F).put(1.0F).put(-1.0F).put(0.0F);
	 * this.entityFloatBuffer.put(1.0F).put(0.5F).put(0.0F).put(0.0F);
	 * this.entityFloatBuffer.put(0.0F).put(0.0F).put(0.0F).put(1.0F);
	 * this.entityFloatBuffer.flip();
	 * GL11.glMultMatrix(this.entityFloatBuffer);
	 * GL11.glRotatef(0.0F, 0.0F, 1.0F, 0.0F);
	 * GL11.glTranslatef((float)(-var16.width) / 2.0F, (float)(-var16.height) /
	 * 2.0F, (float)(-var16.length) / 2.0F);
	 * IsomCamera var24 = new IsomCamera();
	 * this.mc.renderGlobal.clipRenderersByFrustrum(var24);
	 * this.mc.renderGlobal.updateRenderers(var15);
	 * this.setupFog();
	 * GL11.glEnable(GL11.GL_FOG);
	 * GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_LINEAR);
	 * float var23 = (float)var16.height * 8.0F;
	 * GL11.glFogf(GL11.GL_FOG_START, 5000.0F - var23);
	 * GL11.glFogf(GL11.GL_FOG_END, 5000.0F + var23 * 8.0F);
	 * RenderHelper.enableStandardItemLighting();
	 * var17.renderEntities(this.orientCamera(0.0F), var24, 0.0F);
	 * RenderHelper.disableStandardItemLighting();
	 * GL11.glBindTexture(GL11.GL_TEXTURE_2D,
	 * this.mc.renderEngine.getTexture("/terrain.png"));
	 * var17.sortAndRender(var15, 0);
	 * var17.oobGroundRenderer();
	 * if(var16.cloudHeight < var16.height) {
	 * var17.renderSky(0.0F);
	 * }
	 * 
	 * GL11.glEnable(GL11.GL_BLEND);
	 * GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	 * GL11.glColorMask(false, false, false, false);
	 * var13 = var17.sortAndRender(var15, 1);
	 * GL11.glColorMask(true, true, true, true);
	 * if(var13 > 0) {
	 * var17.renderAllRenderLists();
	 * }
	 * 
	 * if(var16.getGroundLevel() >= 0) {
	 * var17.oobWaterRenderer();
	 * }
	 * 
	 * GL11.glDepthMask(true);
	 * GL11.glDisable(GL11.GL_BLEND);
	 * GL11.glDisable(GL11.GL_FOG);
	 * this.entityByteBuffer.clear();
	 * GL11.glPixelStorei(GL11.GL_PACK_ALIGNMENT, 1);
	 * GL11.glReadPixels(0, 0, this.mc.displayWidth, this.mc.displayHeight,
	 * GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, (ByteBuffer)this.entityByteBuffer);
	 * BufferedImage var21 = screenshotBuffer(this.entityByteBuffer, var6, var7);
	 * var5.drawImage(var21, var10, var11, (ImageObserver)null);
	 * }
	 * }
	 * 
	 * var5.dispose();
	 * this.mc.loadingScreen.displayLoadingString("Saving as " + var3.toString());
	 * this.mc.loadingScreen.setLoadingProgress(100);
	 * FileOutputStream var20 = new FileOutputStream(var3);
	 * ImageIO.write(var4, "png", var20);
	 * var20.close();
	 * return;
	 * } catch (Throwable var18) {
	 * var18.printStackTrace();
	 * return;
	 * }
	 * }
	 * 
	 * ++var2;
	 * }
	 * }
	 * 
	 * private static BufferedImage screenshotBuffer(ByteBuffer var0, int var1, int
	 * var2) {
	 * var0.position(0).limit(var1 * var2 << 2);
	 * BufferedImage var3 = new BufferedImage(var1, var2, 1);
	 * int[] var4 = ((DataBufferInt)var3.getRaster().getDataBuffer()).getData();
	 * 
	 * for(int var5 = 0; var5 < var1 * var2; ++var5) {
	 * int var6 = var0.get(var5 * 3) & 255;
	 * int var7 = var0.get(var5 * 3 + 1) & 255;
	 * int var8 = var0.get(var5 * 3 + 2) & 255;
	 * var4[var5] = var6 << 16 | var7 << 8 | var8;
	 * }
	 * 
	 * return var3;
	 * }
	 */

	private void getMouseOver(float var1) {
		EntityRenderer var7 = this;
		EntityPlayerSP var9 = this.mc.thePlayer;
		float var2 = var9.prevRotationPitch + (var9.rotationPitch - var9.prevRotationPitch) * var1;
		float var10 = var9.prevRotationYaw + (var9.rotationYaw - var9.prevRotationYaw) * var1;
		Vec3D var11 = this.orientCamera(var1);
		float var12 = MathHelper.cos(-var10 * ((float) Math.PI / 180.0F) - (float) Math.PI);
		float var13 = MathHelper.sin(-var10 * ((float) Math.PI / 180.0F) - (float) Math.PI);
		float var14 = -MathHelper.cos(-var2 * ((float) Math.PI / 180.0F));
		float var15 = MathHelper.sin(-var2 * ((float) Math.PI / 180.0F));
		float var16 = var13 * var14;
		float var17 = var12 * var14;
		float var18 = this.mc.playerController.getBlockReachDistance();
		Vec3D var19 = var11.addVector(var16 * var18, var15 * var18, var17 * var18);
		this.mc.objectMouseOver = this.mc.theWorld.rayTraceBlocks(var11, var19);
		float var20 = var18;
		var11 = this.orientCamera(var1);
		if (this.mc.objectMouseOver != null) {
			var20 = this.mc.objectMouseOver.hitVec.distance(var11);
		}

		if (this.mc.playerController instanceof PlayerControllerCreative) {
			var18 = 32.0F;
		} else {
			if (var20 > 3.0F) {
				var20 = 3.0F;
			}

			var18 = var20;
		}

		var19 = var11.addVector(var16 * var18, var15 * var18, var17 * var18);
		this.pointedEntity = null;
		List var8 = this.mc.theWorld.entityMap.getEntitiesWithinAABB(var9,
				var9.boundingBox.addCoord(var16 * var18, var15 * var18, var17 * var18));
		float var28 = 0.0F;

		for (int var41 = 0; var41 < var8.size(); ++var41) {
			Entity var21 = (Entity) var8.get(var41);
			if (var21.canBeCollidedWith()) {
				AxisAlignedBB var22 = var21.boundingBox.expand(0.1F, 0.1F, 0.1F);
				MovingObjectPosition var23 = var22.calculateIntercept(var11, var19);
				if (var23 != null) {
					var2 = var11.distance(var23.hitVec);
					if (var2 < var28 || var28 == 0.0F) {
						var7.pointedEntity = var21;
						var28 = var2;
					}
				}
			}
		}

		if (var7.pointedEntity != null && !(var7.mc.playerController instanceof PlayerControllerCreative)) {
			var7.mc.objectMouseOver = new MovingObjectPosition(var7.pointedEntity);
		}

		for (int var24 = 0; var24 < 2; ++var24) {
			if (this.mc.options.anaglyph) {
				if (var24 == 0) {
					GL11.glColorMask(false, true, true, false);
				} else {
					GL11.glColorMask(true, false, false, false);
				}
			}

			EntityPlayerSP var3 = this.mc.thePlayer;
			World var4 = this.mc.theWorld;
			RenderGlobal var5 = this.mc.renderGlobal;
			EffectRenderer var6 = this.mc.effectRenderer;
			GL11.glViewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
			this.updateFogColor(var1);
			GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_COLOR_BUFFER_BIT);
			GL11.glEnable(GL11.GL_CULL_FACE);
			float var27 = var1;
			this.farPlaneDistance = (float) (512 >> (this.mc.options.renderDistance << 1));
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glLoadIdentity();
			if (this.mc.options.anaglyph) {
				GL11.glTranslatef((float) (-((var24 << 1) - 1)) * 0.07F, 0.0F, 0.0F);
			}

			EntityPlayerSP var34 = this.mc.thePlayer;
			var13 = 70.0F;
			if (var34.isInsideOfWater()) {
				var13 = 60.0F;
			}

			if (this.mc.currentScreen == null) {
				if (Keyboard.isKeyDown(this.mc.options.keyBindZoom.keyCode)) {
					var13 /= 4.0F;
				}
			}

			if (var34.health <= 0) {
				var14 = (float) var34.deathTime + var1;
				var13 /= (1.0F - 500.0F / (var14 + 500.0F)) * 2.0F + 1.0F;
			}

			GLU.gluPerspective(var13, (float) this.mc.displayWidth / (float) this.mc.displayHeight, 0.05F,
					this.farPlaneDistance);
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			GL11.glLoadIdentity();
			if (this.mc.options.anaglyph) {
				GL11.glTranslatef((float) ((var24 << 1) - 1) * 0.1F, 0.0F, 0.0F);
			}

			this.hurtCameraEffect(var1);
			if (this.mc.options.fancyGraphics) {
				this.setupViewBobbing(var1);
			}

			EntityRenderer var30 = this;
			var34 = this.mc.thePlayer;
			var13 = var34.prevPosX + (var34.posX - var34.prevPosX) * var1;
			var14 = var34.prevPosY + (var34.posY - var34.prevPosY) * var1;
			var15 = var34.prevPosZ + (var34.posZ - var34.prevPosZ) * var1;
			if (!this.mc.options.thirdPersonView) {
				GL11.glTranslatef(0.0F, 0.0F, -0.1F);
			} else {
				var16 = 4.0F;
				float var25 = -MathHelper.sin(var34.rotationYaw / 180.0F * (float) Math.PI)
						* MathHelper.cos(var34.rotationPitch / 180.0F * (float) Math.PI) * 4.0F;
				var17 = MathHelper.cos(var34.rotationYaw / 180.0F * (float) Math.PI)
						* MathHelper.cos(var34.rotationPitch / 180.0F * (float) Math.PI) * 4.0F;
				var18 = -MathHelper.sin(var34.rotationPitch / 180.0F * (float) Math.PI) * 4.0F;

				for (int var39 = 0; var39 < 8; ++var39) {
					var20 = (float) (((var39 & 1) << 1) - 1);
					var27 = (float) (((var39 >> 1 & 1) << 1) - 1);
					var28 = (float) (((var39 >> 2 & 1) << 1) - 1);
					var20 *= 0.1F;
					var27 *= 0.1F;
					var28 *= 0.1F;
					MovingObjectPosition var42 = var30.mc.theWorld.rayTraceBlocks(
							new Vec3D(var13 + var20, var14 + var27, var15 + var28),
							new Vec3D(var13 - var25 + var20 + var28, var14 - var18 + var27, var15 - var17 + var28));
					if (var42 != null) {
						float var40 = var42.hitVec.distance(new Vec3D(var13, var14, var15));
						if (var40 < var16) {
							var16 = var40;
						}
					}
				}

				GL11.glTranslatef(0.0F, 0.0F, -var16);
			}

			GL11.glRotatef(var34.prevRotationPitch + (var34.rotationPitch - var34.prevRotationPitch) * var27, 1.0F, 0.0F,
					0.0F);
			GL11.glRotatef(var34.prevRotationYaw + (var34.rotationYaw - var34.prevRotationYaw) * var27 + 180.0F, 0.0F, 1.0F,
					0.0F);
			GL11.glTranslatef(-var13, -var14, -var15);
			ClippingHelperImplementation.init();
			this.setupFog();
			GL11.glEnable(GL11.GL_FOG);
			var5.renderSky(var1);
			this.setupFog();
			Frustrum var26 = new Frustrum();
			this.mc.renderGlobal.clipRenderersByFrustrum(var26);
			this.mc.renderGlobal.updateRenderers(var3);
			this.setupFog();
			GL11.glEnable(GL11.GL_FOG);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/terrain.png"));
			RenderHelper.disableStandardItemLighting();
			var5.sortAndRender(var3, 0);
			int var29;
			if (var4.isSolid(var3.posX, var3.posY, var3.posZ, 0.1F)) {
				var29 = (int) var3.posX;
				int var31 = (int) var3.posY;
				int var32 = (int) var3.posZ;
				RenderBlocks var33 = new RenderBlocks(var4);

				for (int var35 = var29 - 1; var35 <= var29 + 1; ++var35) {
					for (int var37 = var31 - 1; var37 <= var31 + 1; ++var37) {
						for (int var36 = var32 - 1; var36 <= var32 + 1; ++var36) {
							int var38 = var4.getBlockId(var35, var37, var36);
							if (var38 > 0) {
								var33.renderBlockAllFaces(Block.blocksList[var38], var35, var37, var36);
							}
						}
					}
				}
			}

			RenderHelper.enableStandardItemLighting();
			var5.renderEntities(this.orientCamera(var1), var26, var1);
			var6.renderLitParticles(var1);
			RenderHelper.disableStandardItemLighting();
			this.setupFog();
			var6.renderParticles(var3, var1);
			var5.oobGroundRenderer();
			if (this.mc.objectMouseOver != null && var3.isInsideOfWater()) {
				GL11.glDisable(GL11.GL_ALPHA_TEST);
				var5.drawBlockBreaking(this.mc.objectMouseOver, 0, var3.inventory.getCurrentItem());
				var5.drawSelectionBox(this.mc.objectMouseOver, 0);
				GL11.glEnable(GL11.GL_ALPHA_TEST);
			}

			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			this.setupFog();
			var5.oobWaterRenderer();
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glDisable(GL11.GL_CULL_FACE);
			// GL11.glColorMask(false, false, false, false);
			var29 = var5.sortAndRender(var3, 1);
			// GL11.glColorMask(true, true, true, true);
			if (this.mc.options.anaglyph) {
				if (var24 == 0) {
					GL11.glColorMask(false, true, true, false);
				} else {
					GL11.glColorMask(true, false, false, false);
				}
			}

			if (var29 > 0) {
				var5.renderAllRenderLists();
			}

			GL11.glDepthMask(true);
			GL11.glEnable(GL11.GL_CULL_FACE);
			GL11.glDisable(GL11.GL_BLEND);
			if (this.mc.objectMouseOver != null && !var3.isInsideOfWater()) {
				GL11.glDisable(GL11.GL_ALPHA_TEST);
				var5.drawBlockBreaking(this.mc.objectMouseOver, 0, var3.inventory.getCurrentItem());
				var5.drawSelectionBox(this.mc.objectMouseOver, 0);
				GL11.glEnable(GL11.GL_ALPHA_TEST);
			}

			GL11.glDisable(GL11.GL_FOG);
			GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
			GL11.glLoadIdentity();
			if (this.mc.options.anaglyph) {
				GL11.glTranslatef((float) ((var24 << 1) - 1) * 0.1F, 0.0F, 0.0F);
			}

			GL11.glPushMatrix();
			this.hurtCameraEffect(var1);
			if (this.mc.options.fancyGraphics) {
				this.setupViewBobbing(var1);
			}

			if (!this.mc.options.thirdPersonView) {
				this.itemRenderer.renderItemInFirstPerson(var1);
			}

			GL11.glPopMatrix();
			if (!this.mc.options.thirdPersonView) {
				this.itemRenderer.renderOverlays(var1);
				this.hurtCameraEffect(var1);
			}

			if (this.mc.options.fancyGraphics) {
				this.setupViewBobbing(var1);
			}

			if (!this.mc.options.anaglyph) {
				return;
			}
		}

		GL11.glColorMask(true, true, true, false);
	}

	public final void setupOverlayRendering() {
		ScaledResolution var1 = new ScaledResolution(this.mc.displayWidth, this.mc.displayHeight);
		int var2 = var1.getScaledWidth();
		int var3 = var1.getScaledHeight();
		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0.0D, (double) var2, (double) var3, 0.0D, 1000.0D, 3000.0D);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
	}

	private void updateFogColor(float var1) {
		World var2 = this.mc.theWorld;
		EntityPlayerSP var3 = this.mc.thePlayer;
		float var4 = 1.0F / (float) (4 - this.mc.options.renderDistance);
		var4 = 1.0F - (float) Math.pow((double) var4, 0.25D);
		Vec3D var5 = var2.getSkyColor(var1);
		float var6 = var5.xCoord;
		float var7 = var5.yCoord;
		float var13 = var5.zCoord;
		Vec3D var8 = var2.getFogColor(var1);
		this.fogColorRed = var8.xCoord;
		this.fogColorGreen = var8.yCoord;
		this.fogColorBlue = var8.zCoord;
		this.fogColorRed += (var6 - this.fogColorRed) * var4;
		this.fogColorGreen += (var7 - this.fogColorGreen) * var4;
		this.fogColorBlue += (var13 - this.fogColorBlue) * var4;
		Block var9 = Block.blocksList[var2.getBlockId((int) var3.posX, (int) (var3.posY + 0.12F), (int) var3.posZ)];
		if (var9 != null && var9.material != Material.air) {
			Material var10 = var9.material;
			if (var10 == Material.water) {
				this.fogColorRed = 0.02F;
				this.fogColorGreen = 0.02F;
				this.fogColorBlue = 0.2F;
			} else if (var10 == Material.lava) {
				this.fogColorRed = 0.6F;
				this.fogColorGreen = 0.1F;
				this.fogColorBlue = 0.0F;
			}
		}

		float var11 = this.prevFogColor + (this.fogColor - this.prevFogColor) * var1;
		this.fogColorRed *= var11;
		this.fogColorGreen *= var11;
		this.fogColorBlue *= var11;
		if (this.mc.options.anaglyph) {
			var1 = (this.fogColorRed * 30.0F + this.fogColorGreen * 59.0F + this.fogColorBlue * 11.0F) / 100.0F;
			var11 = (this.fogColorRed * 30.0F + this.fogColorGreen * 70.0F) / 100.0F;
			float var12 = (this.fogColorRed * 30.0F + this.fogColorBlue * 70.0F) / 100.0F;
			this.fogColorRed = var1;
			this.fogColorGreen = var11;
			this.fogColorBlue = var12;
		}

		GL11.glClearColor(this.fogColorRed, this.fogColorGreen, this.fogColorBlue, 0.0F);
	}

	private void setupFog() {
		World var1 = this.mc.theWorld;
		EntityPlayerSP var2 = this.mc.thePlayer;
		int var10000 = GL11.GL_FOG_COLOR;
		float var3 = 1.0F;
		float var6 = this.fogColorBlue;
		float var5 = this.fogColorGreen;
		float var4 = this.fogColorRed;
		this.fogColorBuffer.clear();
		this.fogColorBuffer.put(var4).put(var5).put(var6).put(1.0F);
		this.fogColorBuffer.flip();
		GL11.glFog(var10000, this.fogColorBuffer);
		GL11.glNormal3f(0.0F, -1.0F, 0.0F);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		Block var7 = Block.blocksList[var1.getBlockId((int) var2.posX, (int) (var2.posY + 0.12F), (int) var2.posZ)];
		if (var7 != null && var7.material.getIsLiquid()) {
			Material var8 = var7.material;
			GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_EXP);
			if (var8 == Material.water) {
				GL11.glFogf(GL11.GL_FOG_DENSITY, 0.1F);
			} else if (var8 == Material.lava) {
				GL11.glFogf(GL11.GL_FOG_DENSITY, 2.0F);
			}
		} else {
			GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_LINEAR);
			GL11.glFogf(GL11.GL_FOG_START, this.farPlaneDistance / 4.0F);
			GL11.glFogf(GL11.GL_FOG_END, this.farPlaneDistance);
		}

		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		GL11.glColorMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT);
	}
}
