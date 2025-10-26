package net.minecraft.client.effect;

import net.minecraft.game.level.World;
import net.minecraft.game.level.material.Material;

public final class EntityBubbleFX extends EntityFX {
	public EntityBubbleFX(World var1, float var2, float var3, float var4, float var5, float var6, float var7) {
		super(var1, var2, var3, var4, var5, var6, var7);
		this.particleRed = 1.0F;
		this.particleGreen = 1.0F;
		this.particleBlue = 1.0F;
		this.particleTextureIndex = 32;
		this.setSize(0.02F, 0.02F);
		this.particleScale *= this.rand.nextFloat() * 0.6F + 0.2F;
		this.motionX1 = var5 * 0.2F + (float)(Math.random() * 2.0D - 1.0D) * 0.02F;
		this.motionY1 = var6 * 0.2F + (float)(Math.random() * 2.0D - 1.0D) * 0.02F;
		this.motionZ1 = var7 * 0.2F + (float)(Math.random() * 2.0D - 1.0D) * 0.02F;
		this.particleMaxAge = (int)(8.0D / (Math.random() * 0.8D + 0.2D));
	}

	public final void onEntityUpdate() {
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		this.motionY1 = (float)((double)this.motionY1 + 0.002D);
		this.moveEntity(this.motionX1, this.motionY1, this.motionZ1);
		this.motionX1 *= 0.85F;
		this.motionY1 *= 0.85F;
		this.motionZ1 *= 0.85F;
		if(this.worldObj.getBlockMaterial((int)this.posX, (int)this.posY, (int)this.posZ) != Material.water) {
			this.setEntityDead();
		}

		if(this.particleMaxAge-- <= 0) {
			this.setEntityDead();
		}

	}
}
