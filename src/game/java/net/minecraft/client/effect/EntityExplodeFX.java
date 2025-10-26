package net.minecraft.client.effect;

import net.minecraft.client.render.Tessellator;
import net.minecraft.game.level.World;

public final class EntityExplodeFX extends EntityFX {
	public EntityExplodeFX(World var1, float var2, float var3, float var4, float var5, float var6, float var7) {
		super(var1, var2, var3, var4, var5, var6, var7);
		this.motionX1 = var5 + (float)(Math.random() * 2.0D - 1.0D) * 0.05F;
		this.motionY1 = var6 + (float)(Math.random() * 2.0D - 1.0D) * 0.05F;
		this.motionZ1 = var7 + (float)(Math.random() * 2.0D - 1.0D) * 0.05F;
		this.particleRed = this.particleGreen = this.particleBlue = this.rand.nextFloat() * 0.3F + 0.7F;
		this.particleScale = this.rand.nextFloat() * this.rand.nextFloat() * 6.0F + 1.0F;
		this.particleMaxAge = (int)(16.0D / ((double)this.rand.nextFloat() * 0.8D + 0.2D)) + 2;
	}

	public final void renderParticle(Tessellator var1, float var2, float var3, float var4, float var5, float var6, float var7) {
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
		this.motionX1 *= 0.9F;
		this.motionY1 *= 0.9F;
		this.motionZ1 *= 0.9F;
		if(this.onGround) {
			this.motionX1 *= 0.7F;
			this.motionZ1 *= 0.7F;
		}

	}
}
