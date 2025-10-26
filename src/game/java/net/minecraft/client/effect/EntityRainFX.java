package net.minecraft.client.effect;

import net.minecraft.client.render.Tessellator;
import net.minecraft.game.level.World;
import net.minecraft.game.level.material.Material;

public class EntityRainFX extends EntityFX {
	public EntityRainFX(World var1, float var2, float var3, float var4) {
		super(var1, var2, var3, var4, 0.0F, 0.0F, 0.0F);
		this.motionX1 *= 0.3F;
		this.motionY1 = (float)Math.random() * 0.2F + 0.1F;
		this.motionZ1 *= 0.3F;
		this.particleRed = 1.0F;
		this.particleGreen = 1.0F;
		this.particleBlue = 1.0F;
		this.particleTextureIndex = 16;
		this.setSize(0.01F, 0.01F);
		this.particleGravity = 0.06F;
		this.particleMaxAge = (int)(8.0D / (Math.random() * 0.8D + 0.2D));
	}

	public final void renderParticle(Tessellator var1, float var2, float var3, float var4, float var5, float var6, float var7) {
		super.renderParticle(var1, var2, var3, var4, var5, var6, var7);
	}

	public final void onEntityUpdate() {
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		this.motionY1 -= this.particleGravity;
		this.moveEntity(this.motionX1, this.motionY1, this.motionZ1);
		this.motionX1 *= 0.98F;
		this.motionY1 *= 0.98F;
		this.motionZ1 *= 0.98F;
		if(this.particleMaxAge-- <= 0) {
			this.setEntityDead();
		}

		if(this.onGround) {
			if(Math.random() < 0.5D) {
				this.setEntityDead();
			}

			this.motionX1 *= 0.7F;
			this.motionZ1 *= 0.7F;
		}

		Material var1 = this.worldObj.getBlockMaterial((int)this.posX, (int)this.posY, (int)this.posZ);
		if(var1.getIsLiquid() || var1.isSolid()) {
			this.setEntityDead();
		}

	}
}
