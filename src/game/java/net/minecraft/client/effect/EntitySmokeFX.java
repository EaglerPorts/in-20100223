package net.minecraft.client.effect;

import net.minecraft.client.render.Tessellator;
import net.minecraft.game.level.World;

public final class EntitySmokeFX extends EntityFX {
	private float smokeParticleScale;

	public EntitySmokeFX(World var1, float var2, float var3, float var4) {
		this(var1, var2, var3, var4, 1.0F);
	}

	public EntitySmokeFX(World var1, float var2, float var3, float var4, float var5) {
		super(var1, var2, var3, var4, 0.0F, 0.0F, 0.0F);
		this.motionX1 *= 0.1F;
		this.motionY1 *= 0.1F;
		this.motionZ1 *= 0.1F;
		this.particleRed = this.particleGreen = this.particleBlue = (float)(Math.random() * (double)0.3F);
		this.particleScale *= 12.0F / 16.0F;
		this.particleScale *= var5;
		this.smokeParticleScale = this.particleScale;
		this.particleMaxAge = (int)(8.0D / (Math.random() * 0.8D + 0.2D));
		this.particleMaxAge = (int)((float)this.particleMaxAge * var5);
		this.noClip = false;
	}

	public final void renderParticle(Tessellator var1, float var2, float var3, float var4, float var5, float var6, float var7) {
		float var8 = ((float)this.particleAge + var2) / (float)this.particleMaxAge * 32.0F;
		if(var8 < 0.0F) {
			var8 = 0.0F;
		}

		if(var8 > 1.0F) {
			var8 = 1.0F;
		}

		this.particleScale = this.smokeParticleScale * var8;
		super.renderParticle(var1, var2, var3, var4, var5, var6, var7);
	}

	public final void onEntityUpdate() {
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		if(this.particleAge++ >= this.particleMaxAge) {
			this.setEntityDead();
		}

		this.particleTextureIndex = 7 - (this.particleAge << 3) / this.particleMaxAge;
		this.motionY1 = (float)((double)this.motionY1 + 0.004D);
		this.moveEntity(this.motionX1, this.motionY1, this.motionZ1);
		if(this.posY == this.prevPosY) {
			this.motionX1 = (float)((double)this.motionX1 * 1.1D);
			this.motionZ1 = (float)((double)this.motionZ1 * 1.1D);
		}

		this.motionX1 *= 0.96F;
		this.motionY1 *= 0.96F;
		this.motionZ1 *= 0.96F;
		if(this.onGround) {
			this.motionX1 *= 0.7F;
			this.motionZ1 *= 0.7F;
		}

	}
}
