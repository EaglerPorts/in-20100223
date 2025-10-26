package net.minecraft.client.effect;

import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.entity.RenderManager;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.EntityLiving;
import net.minecraft.game.level.World;
import org.lwjgl.opengl.GL11;

public final class EntityPickupFX extends EntityFX {
	private Entity entityToPickUp;
	private EntityLiving entityPickingUp;
	private int age = 0;
	private int maxAge = 0;
	private float yOffs;

	public EntityPickupFX(World var1, Entity var2, EntityLiving var3, float var4) {
		super(var1, var2.posX, var2.posY, var2.posZ, var2.motionX, var2.motionY, var2.motionZ);
		this.entityToPickUp = var2;
		this.entityPickingUp = var3;
		this.maxAge = 3;
		this.yOffs = -0.5F;
	}

	public final void renderParticle(Tessellator var1, float var2, float var3, float var4, float var5, float var6, float var7) {
		float var9 = ((float)this.age + var2) / (float)this.maxAge;
		var9 *= var9;
		var3 = this.entityToPickUp.posX;
		var4 = this.entityToPickUp.posY;
		var5 = this.entityToPickUp.posZ;
		var6 = this.entityPickingUp.lastTickPosX + (this.entityPickingUp.posX - this.entityPickingUp.lastTickPosX) * var2;
		var7 = this.entityPickingUp.lastTickPosY + (this.entityPickingUp.posY - this.entityPickingUp.lastTickPosY) * var2 + this.yOffs;
		float var8 = this.entityPickingUp.lastTickPosZ + (this.entityPickingUp.posZ - this.entityPickingUp.lastTickPosZ) * var2;
		var3 += (var6 - var3) * var9;
		var4 += (var7 - var4) * var9;
		var9 = var5 + (var8 - var5) * var9;
		var5 = this.worldObj.getLightBrightness((int)var3, (int)var4, (int)var9);
		GL11.glColor4f(var5, var5, var5, 1.0F);
		RenderManager.instance.renderEntityWithPosYaw(this.entityToPickUp, var3, var4, var9, this.entityToPickUp.rotationYaw, var2);
	}

	public final void onEntityUpdate() {
		++this.age;
		if(this.age == this.maxAge) {
			this.setEntityDead();
		}

	}

	public final int getFXLayer() {
		return 2;
	}
}
