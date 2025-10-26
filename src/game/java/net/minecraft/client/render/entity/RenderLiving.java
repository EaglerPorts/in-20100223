package net.minecraft.client.render.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.EntityLiving;
import org.lwjgl.opengl.GL11;
import util.MathHelper;

public class RenderLiving extends Render {
	protected ModelBase mainModel;
	private ModelBase renderPassModel;

	public RenderLiving(ModelBase var1, float var2) {
		this.mainModel = var1;
		this.shadowSize = var2;
	}

	public final void setRenderPassModel(ModelBase var1) {
		this.renderPassModel = var1;
	}

	public void a(EntityLiving var1, float var2, float var3, float var4, float var5, float var6) {
		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_CULL_FACE);

		try {
			var5 = var1.prevRenderYawOffset + (var1.renderYawOffset - var1.prevRenderYawOffset) * var6;
			float var7 = var1.prevRotationYaw + (var1.rotationYaw - var1.prevRotationYaw) * var6;
			float var8 = var1.prevRotationPitch + (var1.rotationPitch - var1.prevRotationPitch) * var6;
			GL11.glTranslatef(var2, var3, var4);
			var2 = (float)var1.ticksExisted + var6;
			GL11.glRotatef(180.0F - var5, 0.0F, 1.0F, 0.0F);
			if(var1.deathTime > 0) {
				var3 = ((float)var1.deathTime + var6 - 1.0F) / 20.0F * 1.6F;
				var3 = MathHelper.sqrt_float(var3);
				if(var3 > 1.0F) {
					var3 = 1.0F;
				}

				GL11.glRotatef(var3 * this.getDeathMaxRotation(var1), 0.0F, 0.0F, 1.0F);
			}

			GL11.glScalef(-(1.0F / 16.0F), -(1.0F / 16.0F), 1.0F / 16.0F);
			this.preRenderCallback(var1, var6);
			GL11.glTranslatef(0.0F, -24.0F, 0.0F);
			GL11.glEnable(GL11.GL_NORMALIZE);
			var3 = var1.prevLimbYaw + (var1.limbYaw - var1.prevLimbYaw) * var6;
			var4 = var1.limbSwing - var1.limbYaw * (1.0F - var6);
			if(var3 > 1.0F) {
				var3 = 1.0F;
			}

			this.loadDownloadableImageTexture(var1.skinUrl, var1.getTexture());
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			this.mainModel.render(var4, var3, var2, var7 - var5, var8, 1.0F);

			for(int var9 = 0; var9 < 4; ++var9) {
				if(this.shouldRenderPass(var1, var9)) {
					this.renderPassModel.render(var4, var3, var2, var7 - var5, var8, 1.0F);
					GL11.glDisable(GL11.GL_BLEND);
					GL11.glEnable(GL11.GL_ALPHA_TEST);
				}
			}

			float var15 = var1.getEntityBrightness(var6);
			int var14 = this.getColorMultiplier(var1, var15, var6);
			if(var14 >>> 24 > 0 || var1.hurtTime > 0 || var1.deathTime > 0) {
				GL11.glDisable(GL11.GL_TEXTURE_2D);
				GL11.glDisable(GL11.GL_ALPHA_TEST);
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				GL11.glDepthFunc(GL11.GL_EQUAL);
				if(var1.hurtTime > 0 || var1.deathTime > 0) {
					GL11.glColor4f(var15, 0.0F, 0.0F, 0.4F);
					this.mainModel.render(var4, var3, var2, var7 - var5, var8, 1.0F);

					for(int var10 = 0; var10 < 4; ++var10) {
						if(this.shouldRenderPass(var1, var10)) {
							GL11.glColor4f(var15, 0.0F, 0.0F, 0.4F);
							this.renderPassModel.render(var4, var3, var2, var7 - var5, var8, 1.0F);
						}
					}
				}

				if(var14 >>> 24 > 0) {
					float var16 = (float)(var14 >> 16 & 255) / 255.0F;
					var15 = (float)(var14 >> 8 & 255) / 255.0F;
					float var11 = (float)(var14 & 255) / 255.0F;
					var6 = (float)(var14 >>> 24) / 255.0F;
					GL11.glColor4f(var16, var15, var11, var6);
					this.mainModel.render(var4, var3, var2, var7 - var5, var8, 1.0F);

					for(int var12 = 0; var12 < 4; ++var12) {
						if(this.shouldRenderPass(var1, var12)) {
							GL11.glColor4f(var16, var15, var11, var6);
							this.renderPassModel.render(var4, var3, var2, var7 - var5, var8, 1.0F);
						}
					}
				}

				GL11.glDepthFunc(GL11.GL_LEQUAL);
				GL11.glDisable(GL11.GL_BLEND);
				GL11.glEnable(GL11.GL_ALPHA_TEST);
				GL11.glEnable(GL11.GL_TEXTURE_2D);
			}

			GL11.glDisable(GL11.GL_NORMALIZE);
		} catch (Exception var13) {
			var13.printStackTrace();
		}

		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glPopMatrix();
	}

	protected boolean shouldRenderPass(EntityLiving var1, int var2) {
		return false;
	}

	protected float getDeathMaxRotation(EntityLiving var1) {
		return 90.0F;
	}

	protected int getColorMultiplier(EntityLiving var1, float var2, float var3) {
		return 0;
	}

	protected void preRenderCallback(EntityLiving var1, float var2) {
	}

	public void doRender(Entity var1, float var2, float var3, float var4, float var5, float var6) {
		this.a((EntityLiving)var1, var2, var3, var4, var5, var6);
	}
}
