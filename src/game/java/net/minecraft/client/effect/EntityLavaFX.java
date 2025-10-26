package net.minecraft.client.effect;

import net.minecraft.client.render.Tessellator;
import net.minecraft.game.level.World;

public final class EntityLavaFX extends EntityFX {
	private float lavaParticleScale;

	public EntityLavaFX(World var1, float var2, float var3, float var4) {
		super(var1, var2, var3, var4, 0.0F, 0.0F, 0.0F);
		this.motionX1 *= 0.8F;
		this.motionY1 *= 0.8F;
		this.motionZ1 *= 0.8F;
		this.motionY1 = this.rand.nextFloat() * 0.4F + 0.05F;
		this.particleRed = this.particleGreen = this.particleBlue = 1.0F;
		this.particleScale *= this.rand.nextFloat() * 2.0F + 0.2F;
		this.lavaParticleScale = this.particleScale;
		this.particleMaxAge = (int)(16.0D / (Math.random() * 0.8D + 0.2D));
		this.noClip = false;
		this.particleTextureIndex = 49;
	}

	public final float getEntityBrightness(float var1) {
		return 1.0F;
	}

	public final void renderParticle(Tessellator var1, float var2, float var3, float var4, float var5, float var6, float var7) {
		float var8 = ((float)this.particleAge + var2) / (float)this.particleMaxAge;
		this.particleScale = this.lavaParticleScale * (1.0F - var8 * var8);
		super.renderParticle(var1, var2, var3, var4, var5, var6, var7);
	}

	public final void onEntityUpdate() {
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		if(this.particleAge++ >= this.particleMaxAge) {
			this.setEntityDead();
		}

		float var1 = (float)this.particleAge / (float)this.particleMaxAge;
		if(this.rand.nextFloat() > var1) {
			this.worldObj.spawnParticle("smoke", this.posX, this.posY, this.posZ, this.motionX1, this.motionY1, this.motionZ1);
		}

		this.motionY1 = (float)((double)this.motionY1 - 0.03D);
		this.moveEntity(this.motionX1, this.motionY1, this.motionZ1);
		this.motionX1 *= 0.999F;
		this.motionY1 *= 0.999F;
		this.motionZ1 *= 0.999F;
		if(this.onGround) {
			this.motionX1 *= 0.7F;
			this.motionZ1 *= 0.7F;
		}

	}
}
