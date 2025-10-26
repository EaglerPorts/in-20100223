package net.minecraft.game.entity.projectile;

import com.mojang.nbt.NBTTagCompound;
import java.util.List;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.EntityLiving;
import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.item.Item;
import net.minecraft.game.item.ItemStack;
import net.minecraft.game.level.World;
import net.minecraft.game.physics.AxisAlignedBB;
import net.minecraft.game.physics.MovingObjectPosition;
import net.minecraft.game.physics.Vec3D;
import util.MathHelper;

public class EntityArrow extends Entity {
	private int xTile = -1;
	private int yTile = -1;
	private int zTile = -1;
	private int inTile = 0;
	private boolean inGround = false;
	public int arrowShake = 0;
	private EntityLiving owner;
	private int ticksInGround;
	private int ticksInAir = 0;

	public EntityArrow(World var1, EntityLiving var2) {
		super(var1);
		this.owner = var2;
		this.setSize(0.5F, 0.5F);
		this.setPositionAndRotation(var2.posX, var2.posY, var2.posZ, var2.rotationYaw, var2.rotationPitch);
		this.posX -= MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI) * 0.16F;
		this.posY -= 0.1F;
		this.posZ -= MathHelper.sin(this.rotationYaw / 180.0F * (float)Math.PI) * 0.16F;
		this.setPosition(this.posX, this.posY, this.posZ);
		this.yOffset = 0.0F;
		this.motionX = -MathHelper.sin(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI);
		this.motionZ = MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI);
		this.motionY = -MathHelper.sin(this.rotationPitch / 180.0F * (float)Math.PI);
		this.setArrowHeading(this.motionX, this.motionY, this.motionZ, 1.5F, 1.0F);
	}

	public final void setArrowHeading(float var1, float var2, float var3, float var4, float var5) {
		float var6 = MathHelper.sqrt_float(var1 * var1 + var2 * var2 + var3 * var3);
		var1 /= var6;
		var2 /= var6;
		var3 /= var6;
		var1 = (float)((double)var1 + this.rand.nextGaussian() * (double)0.0075F * (double)var5);
		var2 = (float)((double)var2 + this.rand.nextGaussian() * (double)0.0075F * (double)var5);
		var3 = (float)((double)var3 + this.rand.nextGaussian() * (double)0.0075F * (double)var5);
		var1 *= var4;
		var2 *= var4;
		var3 *= var4;
		this.motionX = var1;
		this.motionY = var2;
		this.motionZ = var3;
		var4 = MathHelper.sqrt_float(var1 * var1 + var3 * var3);
		this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2((double)var1, (double)var3) * 180.0D / (double)((float)Math.PI));
		this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2((double)var2, (double)var4) * 180.0D / (double)((float)Math.PI));
		this.ticksInGround = 0;
	}

	public final void onEntityUpdate() {
		super.onEntityUpdate();
		if(this.arrowShake > 0) {
			--this.arrowShake;
		}

		if(this.inGround) {
			int var1 = this.worldObj.getBlockId(this.xTile, this.yTile, this.zTile);
			if(var1 == this.inTile) {
				++this.ticksInGround;
				if(this.ticksInGround == 1200) {
					this.setEntityDead();
				}

				return;
			}

			this.inGround = false;
			this.motionX *= this.rand.nextFloat() * 0.2F;
			this.motionY *= this.rand.nextFloat() * 0.2F;
			this.motionZ *= this.rand.nextFloat() * 0.2F;
			this.ticksInGround = 0;
			this.ticksInAir = 0;
		} else {
			++this.ticksInAir;
		}

		Vec3D var10 = new Vec3D(this.posX, this.posY, this.posZ);
		Vec3D var2 = new Vec3D(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
		MovingObjectPosition var3 = this.worldObj.rayTraceBlocks(var10, var2);
		var10 = new Vec3D(this.posX, this.posY, this.posZ);
		var2 = new Vec3D(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
		if(var3 != null) {
			var2 = new Vec3D(var3.hitVec.xCoord, var3.hitVec.yCoord, var3.hitVec.zCoord);
		}

		Entity var4 = null;
		List var5 = this.worldObj.entityMap.getEntitiesWithinAABB(this, this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0F, 1.0F, 1.0F));
		float var6 = 0.0F;

		for(int var7 = 0; var7 < var5.size(); ++var7) {
			Entity var8 = (Entity)var5.get(var7);
			if(var8.canBeCollidedWith() && (var8 != this.owner || this.ticksInAir >= 5)) {
				AxisAlignedBB var9 = var8.boundingBox.expand(0.3F, 0.3F, 0.3F);
				MovingObjectPosition var13 = var9.calculateIntercept(var10, var2);
				if(var13 != null) {
					float var14 = var10.distance(var13.hitVec);
					if(var14 < var6 || var6 == 0.0F) {
						var4 = var8;
						var6 = var14;
					}
				}
			}
		}

		if(var4 != null) {
			var3 = new MovingObjectPosition(var4);
		}

		float var11;
		if(var3 != null) {
			if(var3.entityHit != null) {
				if(var3.entityHit.attackEntityFrom(this, 4)) {
					this.worldObj.playSoundAtEntity(this, "random.drr", 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
					this.setEntityDead();
				} else {
					this.motionX *= -0.1F;
					this.motionY *= -0.1F;
					this.motionZ *= -0.1F;
					this.rotationYaw += 180.0F;
					this.prevRotationYaw += 180.0F;
					this.ticksInAir = 0;
				}
			} else {
				this.xTile = var3.blockX;
				this.yTile = var3.blockY;
				this.zTile = var3.blockZ;
				this.inTile = this.worldObj.getBlockId(this.xTile, this.yTile, this.zTile);
				this.motionX = var3.hitVec.xCoord - this.posX;
				this.motionY = var3.hitVec.yCoord - this.posY;
				this.motionZ = var3.hitVec.zCoord - this.posZ;
				var11 = MathHelper.sqrt_float(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
				this.posX -= this.motionX / var11 * 0.05F;
				this.posY -= this.motionY / var11 * 0.05F;
				this.posZ -= this.motionZ / var11 * 0.05F;
				this.worldObj.playSoundAtEntity(this, "random.drr", 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
				this.inGround = true;
				this.arrowShake = 7;
			}
		}

		this.posX += this.motionX;
		this.posY += this.motionY;
		this.posZ += this.motionZ;
		var11 = MathHelper.sqrt_float(this.motionX * this.motionX + this.motionZ * this.motionZ);
		this.rotationYaw = (float)(Math.atan2((double)this.motionX, (double)this.motionZ) * 180.0D / (double)((float)Math.PI));

		for(this.rotationPitch = (float)(Math.atan2((double)this.motionY, (double)var11) * 180.0D / (double)((float)Math.PI)); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F) {
		}

		while(this.rotationPitch - this.prevRotationPitch >= 180.0F) {
			this.prevRotationPitch += 360.0F;
		}

		while(this.rotationYaw - this.prevRotationYaw < -180.0F) {
			this.prevRotationYaw -= 360.0F;
		}

		while(this.rotationYaw - this.prevRotationYaw >= 180.0F) {
			this.prevRotationYaw += 360.0F;
		}

		this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
		this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
		float var12 = 0.99F;
		if(this.handleWaterMovement()) {
			for(int var15 = 0; var15 < 4; ++var15) {
				this.worldObj.spawnParticle("bubble", this.posX - this.motionX * 0.25F, this.posY - this.motionY * 0.25F, this.posZ - this.motionZ * 0.25F, this.motionX, this.motionY, this.motionZ);
			}

			var12 = 0.8F;
		}

		this.motionX *= var12;
		this.motionY *= var12;
		this.motionZ *= var12;
		this.motionY -= 0.03F;
		this.setPosition(this.posX, this.posY, this.posZ);
	}

	protected final void writeEntityToNBT(NBTTagCompound var1) {
		var1.setShort("xTile", (short)this.xTile);
		var1.setShort("yTile", (short)this.yTile);
		var1.setShort("zTile", (short)this.zTile);
		var1.setByte("inTile", (byte)this.inTile);
		var1.setByte("shake", (byte)this.arrowShake);
		var1.setByte("inGround", (byte)(this.inGround ? 1 : 0));
	}

	protected final void readEntityFromNBT(NBTTagCompound var1) {
		this.xTile = var1.getShort("xTile");
		this.yTile = var1.getShort("yTile");
		this.zTile = var1.getShort("zTile");
		this.inTile = var1.getByte("inTile") & 255;
		this.arrowShake = var1.getByte("shake") & 255;
		this.inGround = var1.getByte("inGround") == 1;
	}

	protected final String getEntityString() {
		return "Arrow";
	}

	public final void onCollideWithPlayer(EntityPlayer var1) {
		if(this.inGround && this.owner == var1 && this.arrowShake <= 0 && var1.inventory.storePartialItemStack(new ItemStack(Item.arrow.shiftedIndex, 1))) {
			this.worldObj.playSoundAtEntity(this, "random.pop", 0.2F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
			var1.onItemPickup(this);
			this.setEntityDead();
		}

	}

	public final float getShadowSize() {
		return 0.0F;
	}
}
