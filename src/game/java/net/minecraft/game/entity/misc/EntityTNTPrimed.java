package net.minecraft.game.entity.misc;

import com.mojang.nbt.NBTTagCompound;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.level.World;
import util.MathHelper;

public class EntityTNTPrimed extends Entity {
	public int fuse = 0;

	public EntityTNTPrimed(World var1, float var2, float var3, float var4) {
		super(var1);
		this.preventEntitySpawning = true;
		this.setSize(0.98F, 0.98F);
		this.yOffset = this.height / 2.0F;
		this.setPosition(var2, var3, var4);
		float var5 = (float)(Math.random() * (double)((float)Math.PI) * 2.0D);
		this.motionX = -MathHelper.sin(var5 * (float)Math.PI / 180.0F) * 0.02F;
		this.motionY = 0.2F;
		this.motionZ = -MathHelper.cos(var5 * (float)Math.PI / 180.0F) * 0.02F;
		this.canTriggerWalking = false;
		this.fuse = 80;
		this.prevPosX = var2;
		this.prevPosY = var3;
		this.prevPosZ = var4;
	}

	public final boolean canBeCollidedWith() {
		return !this.isDead;
	}

	public final void onEntityUpdate() {
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		this.motionY -= 0.04F;
		this.moveEntity(this.motionX, this.motionY, this.motionZ);
		this.motionX *= 0.98F;
		this.motionY *= 0.98F;
		this.motionZ *= 0.98F;
		if(this.onGround) {
			this.motionX *= 0.7F;
			this.motionZ *= 0.7F;
			this.motionY *= -0.5F;
		}

		if(this.fuse-- <= 0) {
			this.setEntityDead();
			this.worldObj.createExplosion((Entity)null, this.posX, this.posY, this.posZ, 4.0F);
		} else {
			this.worldObj.spawnParticle("smoke", this.posX, this.posY + 0.5F, this.posZ, 0.0F, 0.0F, 0.0F);
		}
	}

	protected final void writeEntityToNBT(NBTTagCompound var1) {
		var1.setByte("Fuse", (byte)this.fuse);
	}

	protected final void readEntityFromNBT(NBTTagCompound var1) {
		this.fuse = var1.getByte("Fuse");
	}

	protected final String getEntityString() {
		return "PrimedTnt";
	}

	public final float getShadowSize() {
		return 0.0F;
	}
}
