package net.minecraft.game.entity;

import com.mojang.nbt.NBTTagCompound;
import com.mojang.nbt.NBTTagFloat;
import com.mojang.nbt.NBTTagList;
import java.util.ArrayList;
import net.lax1dude.eaglercraft.Random;
import net.minecraft.game.entity.misc.EntityItem;
import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.item.ItemStack;
import net.minecraft.game.level.World;
import net.minecraft.game.level.block.Block;
import net.minecraft.game.level.block.StepSound;
import net.minecraft.game.level.material.Material;
import net.minecraft.game.physics.AxisAlignedBB;
import util.MathHelper;

public abstract class Entity {
	public boolean preventEntitySpawning = false;
	protected World worldObj;
	public float prevPosX;
	public float prevPosY;
	public float prevPosZ;
	public float posX;
	public float posY;
	public float posZ;
	public float motionX;
	public float motionY;
	public float motionZ;
	public float rotationYaw;
	public float rotationPitch;
	public float prevRotationYaw;
	public float prevRotationPitch;
	public AxisAlignedBB boundingBox;
	public boolean onGround = false;
	public boolean isCollidedHorizontally = false;
	private boolean surfaceCollision = true;
	public boolean isDead = false;
	public float yOffset = 0.0F;
	public float width = 0.6F;
	public float height = 1.8F;
	public float prevDistanceWalkedModified = 0.0F;
	public float distanceWalkedModified = 0.0F;
	protected boolean canTriggerWalking = true;
	private float fallDistance = 0.0F;
	private int nextStepDistance = 1;
	public float lastTickPosX;
	public float lastTickPosY;
	public float lastTickPosZ;
	private float ySize = 0.0F;
	public float stepHeight = 0.0F;
	public boolean noClip = false;
	private float entityCollisionReduction = 0.0F;
	protected Random rand = new Random();
	public int ticksExisted = 0;
	public int fireResistance = 1;
	public int fire = 0;
	protected int maxAir = 300;
	private boolean inWater = false;
	public int heartsLife = 0;
	public int air = 300;
	private boolean isFirstUpdate = true;
	public String skinUrl;

	public Entity(World var1) {
		this.worldObj = var1;
		this.setPosition(0.0F, 0.0F, 0.0F);
	}

	protected void preparePlayerToSpawn() {
		if (this.worldObj != null) {
			float var1 = (float) this.worldObj.xSpawn + 0.5F;
			float var2 = (float) this.worldObj.ySpawn;

			for (float var3 = (float) this.worldObj.zSpawn + 0.5F; var2 > 0.0F; ++var2) {
				this.setPosition(var1, var2, var3);
				if (this.worldObj.getCollidingBoundingBoxes(this.boundingBox).size() == 0) {
					break;
				}
			}

			this.motionX = this.motionY = this.motionZ = 0.0F;
			this.rotationYaw = this.worldObj.rotSpawn;
			this.rotationPitch = 0.0F;
		}
	}

	public void setEntityDead() {
		this.isDead = true;
	}

	protected void setSize(float var1, float var2) {
		this.width = var1;
		this.height = var2;
	}

	public final void setPosition(float var1, float var2, float var3) {
		this.posX = var1;
		this.posY = var2;
		this.posZ = var3;
		float var4 = this.width / 2.0F;
		float var5 = this.height / 2.0F;
		this.boundingBox = new AxisAlignedBB(var1 - var4, var2 - var5, var3 - var4, var1 + var4, var2 + var5, var3 + var4);
	}

	public void onEntityUpdate() {
		++this.ticksExisted;
		this.prevDistanceWalkedModified = this.distanceWalkedModified;
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		this.prevRotationPitch = this.rotationPitch;
		this.prevRotationYaw = this.rotationYaw;
		if (this.handleWaterMovement()) {
			if (!this.inWater && !this.isFirstUpdate) {
				float var1 = MathHelper.sqrt_float(
						this.motionX * this.motionX * 0.2F + this.motionY * this.motionY + this.motionZ * this.motionZ * 0.2F)
						* 0.2F;
				if (var1 > 1.0F) {
					var1 = 1.0F;
				}

				this.worldObj.playSoundAtEntity(this, "random.splash", var1,
						1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
				var1 = (float) ((int) this.boundingBox.minY);

				int var2;
				float var3;
				float var4;
				for (var2 = 0; (float) var2 < 1.0F + this.width * 20.0F; ++var2) {
					var3 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
					var4 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
					this.worldObj.spawnParticle("bubble", this.posX + var3, var1 + 1.0F, this.posZ + var4, this.motionX,
							this.motionY - this.rand.nextFloat() * 0.2F, this.motionZ);
				}

				for (var2 = 0; (float) var2 < 1.0F + this.width * 20.0F; ++var2) {
					var3 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
					var4 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
					this.worldObj.spawnParticle("splash", this.posX + var3, var1 + 1.0F, this.posZ + var4, this.motionX,
							this.motionY, this.motionZ);
				}
			}

			this.fallDistance = 0.0F;
			this.inWater = true;
			this.fire = 0;
		} else {
			this.inWater = false;
		}

		if (this.fire > 0) {
			if (this.fire % 20 == 0) {
				this.attackEntityFrom((Entity) null, 1);
			}

			--this.fire;
		}

		if (this.handleLavaMovement()) {
			this.attackEntityFrom((Entity) null, 10);
			this.fire = 600;
		}

		float var5;
		if (this.posX < -8.0F) {
			var5 = -(this.posX + 8.0F);
			this.motionX += var5 * 0.001F;
		}

		if (this.posZ < -8.0F) {
			var5 = -(this.posZ + 8.0F);
			this.motionZ += var5 * 0.001F;
		}

		if (this.posX > (float) this.worldObj.width + 8.0F) {
			var5 = this.posX - (float) this.worldObj.width + 8.0F;
			this.motionX -= var5 * 0.001F;
		}

		if (this.posZ > (float) this.worldObj.length + 8.0F) {
			var5 = this.posZ - (float) this.worldObj.length + 8.0F;
			this.motionZ -= var5 * 0.001F;
		}

		this.isFirstUpdate = false;
	}

	public final boolean isOffsetPositionInLiquid(float var1, float var2, float var3) {
		float var4 = var3;
		var3 = var2;
		var2 = var1;
		AxisAlignedBB var5 = this.boundingBox;
		var5 = new AxisAlignedBB(var5.minX + var4, var5.minY + var3, var5.minZ + var4, var5.maxX + var2, var5.maxY + var3,
				var5.maxZ + var4);
		ArrayList var6 = this.worldObj.getCollidingBoundingBoxes(var5);
		return var6.size() > 0 ? false : !this.worldObj.getIsAnyLiquid(var5);
	}

	public final void moveEntity(float var1, float var2, float var3) {
		if (this.noClip) {
			this.boundingBox.offset(var1, var2, var3);
			this.posX = (this.boundingBox.minX + this.boundingBox.maxX) / 2.0F;
			this.posY = this.boundingBox.minY + this.yOffset - this.ySize;
			this.posZ = (this.boundingBox.minZ + this.boundingBox.maxZ) / 2.0F;
		} else {
			float var4 = this.posX;
			float var5 = this.posZ;
			float var6 = var1;
			float var7 = var2;
			float var8 = var3;
			AxisAlignedBB var9 = this.boundingBox.copy();
			ArrayList var10 = this.worldObj.getCollidingBoundingBoxes(this.boundingBox.addCoord(var1, var2, var3));

			for (int var11 = 0; var11 < var10.size(); ++var11) {
				var2 = ((AxisAlignedBB) var10.get(var11)).calculateYOffset(this.boundingBox, var2);
			}

			this.boundingBox.offset(0.0F, var2, 0.0F);
			if (!this.surfaceCollision && var7 != var2) {
				var3 = 0.0F;
				var2 = var3;
				var1 = var3;
			}

			boolean var18 = this.onGround || var7 != var2 && var7 < 0.0F;

			int var12;
			for (var12 = 0; var12 < var10.size(); ++var12) {
				var1 = ((AxisAlignedBB) var10.get(var12)).calculateXOffset(this.boundingBox, var1);
			}

			this.boundingBox.offset(var1, 0.0F, 0.0F);
			if (!this.surfaceCollision && var6 != var1) {
				var3 = 0.0F;
				var2 = var3;
				var1 = var3;
			}

			for (var12 = 0; var12 < var10.size(); ++var12) {
				var3 = ((AxisAlignedBB) var10.get(var12)).calculateZOffset(this.boundingBox, var3);
			}

			this.boundingBox.offset(0.0F, 0.0F, var3);
			if (!this.surfaceCollision && var8 != var3) {
				var3 = 0.0F;
				var2 = var3;
				var1 = var3;
			}

			int var17;
			float var19;
			float var20;
			if (this.stepHeight > 0.0F && var18 && this.ySize < 0.05F && (var6 != var1 || var8 != var3)) {
				var20 = var1;
				var19 = var2;
				float var13 = var3;
				var1 = var6;
				var2 = this.stepHeight;
				var3 = var8;
				AxisAlignedBB var14 = this.boundingBox.copy();
				this.boundingBox = var9.copy();
				var10 = this.worldObj.getCollidingBoundingBoxes(this.boundingBox.addCoord(var6, var2, var8));

				for (var17 = 0; var17 < var10.size(); ++var17) {
					var2 = ((AxisAlignedBB) var10.get(var17)).calculateYOffset(this.boundingBox, var2);
				}

				this.boundingBox.offset(0.0F, var2, 0.0F);
				if (!this.surfaceCollision && var7 != var2) {
					var3 = 0.0F;
					var2 = var3;
					var1 = var3;
				}

				for (var17 = 0; var17 < var10.size(); ++var17) {
					var1 = ((AxisAlignedBB) var10.get(var17)).calculateXOffset(this.boundingBox, var1);
				}

				this.boundingBox.offset(var1, 0.0F, 0.0F);
				if (!this.surfaceCollision && var6 != var1) {
					var3 = 0.0F;
					var2 = var3;
					var1 = var3;
				}

				for (var17 = 0; var17 < var10.size(); ++var17) {
					var3 = ((AxisAlignedBB) var10.get(var17)).calculateZOffset(this.boundingBox, var3);
				}

				this.boundingBox.offset(0.0F, 0.0F, var3);
				if (!this.surfaceCollision && var8 != var3) {
					var3 = 0.0F;
					var2 = var3;
					var1 = var3;
				}

				if (var20 * var20 + var13 * var13 >= var1 * var1 + var3 * var3) {
					var1 = var20;
					var2 = var19;
					var3 = var13;
					this.boundingBox = var14.copy();
				} else {
					this.ySize = (float) ((double) this.ySize + 0.5D);
				}
			}

			this.posX = (this.boundingBox.minX + this.boundingBox.maxX) / 2.0F;
			this.posY = this.boundingBox.minY + this.yOffset - this.ySize;
			this.posZ = (this.boundingBox.minZ + this.boundingBox.maxZ) / 2.0F;
			this.isCollidedHorizontally = var6 != var1 || var8 != var3;
			this.onGround = var7 != var2 && var7 < 0.0F;
			if (this.onGround) {
				if (this.fallDistance > 0.0F) {
					this.fall(this.fallDistance);
					this.fallDistance = 0.0F;
				}
			} else if (var2 < 0.0F) {
				this.fallDistance -= var2;
			}

			if (var6 != var1) {
				this.motionX = 0.0F;
			}

			if (var7 != var2) {
				this.motionY = 0.0F;
			}

			if (var8 != var3) {
				this.motionZ = 0.0F;
			}

			var20 = this.posX - var4;
			var19 = this.posZ - var5;
			this.distanceWalkedModified = (float) ((double) this.distanceWalkedModified
					+ (double) MathHelper.sqrt_float(var20 * var20 + var19 * var19) * 0.6D);
			if (this.canTriggerWalking) {
				int var21 = (int) this.posX;
				int var23 = (int) (this.posY - 0.2F - this.yOffset);
				var17 = (int) this.posZ;
				int var16 = this.worldObj.getBlockId(var21, var23, var17);
				if (this.distanceWalkedModified > (float) this.nextStepDistance && var16 > 0) {
					++this.nextStepDistance;
					StepSound var15 = Block.blocksList[var16].stepSound;
					if (!Block.blocksList[var16].material.getIsLiquid()) {
						this.worldObj.playSoundAtEntity(this, var15.stepSoundDir2(), var15.soundVolume * 0.15F, var15.soundPitch);
					}

					Block.blocksList[var16].onEntityWalking(this.worldObj, var21, var23, var17);
				}
			}

			this.ySize *= 0.4F;
			boolean var22 = this.handleWaterMovement();
			if (this.worldObj.isBoundingBoxBurning(this.boundingBox)) {
				this.dealFireDamage(1);
				if (!var22) {
					++this.fire;
					if (this.fire == 0) {
						this.fire = 300;
					}
				}
			} else if (this.fire <= 0) {
				this.fire = -this.fireResistance;
			}

			if (var22 && this.fire > 0) {
				this.worldObj.playSoundAtEntity(this, "random.fizz", 0.7F,
						1.6F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
				this.fire = -this.fireResistance;
			}

		}
	}

	protected void dealFireDamage(int var1) {
		this.attackEntityFrom((Entity) null, 1);
	}

	protected void fall(float var1) {
	}

	public final boolean handleWaterMovement() {
		return this.worldObj.handleMaterialAcceleration(this.boundingBox.expand(0.0F, -0.4F, 0.0F), Material.water);
	}

	public final boolean isInsideOfWater() {
		int var1 = this.worldObj.getBlockId((int) this.posX, (int) (this.posY + this.getEyeHeight()), (int) this.posZ);
		return var1 != 0 ? Block.blocksList[var1].material == Material.water : false;
	}

	protected float getEyeHeight() {
		return 0.0F;
	}

	public final boolean handleLavaMovement() {
		return this.worldObj.handleMaterialAcceleration(this.boundingBox.expand(0.0F, -0.4F, 0.0F), Material.lava);
	}

	public final void moveFlying(float var1, float var2, float var3) {
		float var4 = MathHelper.sqrt_float(var1 * var1 + var2 * var2);
		if (var4 >= 0.01F) {
			if (var4 < 1.0F) {
				var4 = 1.0F;
			}

			var4 = var3 / var4;
			var1 *= var4;
			var2 *= var4;
			var3 = MathHelper.sin(this.rotationYaw * (float) Math.PI / 180.0F);
			var4 = MathHelper.cos(this.rotationYaw * (float) Math.PI / 180.0F);
			this.motionX += var1 * var4 - var2 * var3;
			this.motionZ += var2 * var4 + var1 * var3;
		}
	}

	public float getEntityBrightness(float var1) {
		int var4 = (int) this.posX;
		int var2 = (int) (this.posY + this.yOffset / 2.0F);
		int var3 = (int) this.posZ;
		return this.worldObj.getLightBrightness(var4, var2, var3);
	}

	public final void setWorld(World var1) {
		this.worldObj = var1;
	}

	public final void setPositionAndRotation(float var1, float var2, float var3, float var4, float var5) {
		this.prevPosX = this.posX = var1;
		this.prevPosY = this.posY = var2 + this.yOffset;
		this.prevPosZ = this.posZ = var3;
		this.rotationYaw = var4;
		this.rotationPitch = var5;
		this.setPosition(this.posX, this.posY, this.posZ);
	}

	public final float getDistanceSqToEntity(Entity var1) {
		float var2 = this.posX - var1.posX;
		float var3 = this.posY - var1.posY;
		float var4 = this.posZ - var1.posZ;
		return var2 * var2 + var3 * var3 + var4 * var4;
	}

	public void onCollideWithPlayer(EntityPlayer var1) {
	}

	public final void applyEntityCollision(Entity var1) {
		float var2 = var1.posX - this.posX;
		float var3 = var1.posZ - this.posZ;
		float var4 = var2 * var2 + var3 * var3;
		if (var4 >= 0.01F) {
			var4 = MathHelper.sqrt_float(var4);
			var2 /= var4;
			var3 /= var4;
			var2 /= var4;
			var3 /= var4;
			var2 *= 0.05F;
			var3 *= 0.05F;
			this.addVelocity(-var2, 0.0F, -var3);
			var1.addVelocity(var2, 0.0F, var3);
		}

	}

	private void addVelocity(float var1, float var2, float var3) {
		this.motionX += var1;
		this.motionY = this.motionY;
		this.motionZ += var3;
	}

	public boolean attackEntityFrom(Entity var1, int var2) {
		return false;
	}

	public boolean canBeCollidedWith() {
		return false;
	}

	public boolean canBePushed() {
		return false;
	}

	public String getTexture() {
		return null;
	}

	public final void writeToNBT(NBTTagCompound var1) {
		String var2 = this.getEntityString();
		if (!this.isDead && var2 != null) {
			var1.setString("id", var2);
			var1.setTag("Pos", newDoubleNBTList(new float[] { this.posX, this.posY, this.posZ }));
			var1.setTag("Motion", newDoubleNBTList(new float[] { this.motionX, this.motionY, this.motionZ }));
			var1.setTag("Rotation", newDoubleNBTList(new float[] { this.rotationYaw, this.rotationPitch }));
			var1.setFloat("FallDistance", this.fallDistance);
			var1.setShort("Fire", (short) this.fire);
			var1.setShort("Air", (short) this.air);
			this.writeEntityToNBT(var1);
		}
	}

	public final void readFromNBT(NBTTagCompound var1) {
		NBTTagList var2 = var1.getTagList("Pos");
		NBTTagList var3 = var1.getTagList("Motion");
		NBTTagList var4 = var1.getTagList("Rotation");
		this.posX = ((NBTTagFloat) var2.tagAt(0)).floatValue;
		this.posY = ((NBTTagFloat) var2.tagAt(1)).floatValue;
		this.posZ = ((NBTTagFloat) var2.tagAt(2)).floatValue;
		this.motionX = ((NBTTagFloat) var3.tagAt(0)).floatValue;
		this.motionY = ((NBTTagFloat) var3.tagAt(1)).floatValue;
		this.motionZ = ((NBTTagFloat) var3.tagAt(2)).floatValue;
		this.rotationYaw = ((NBTTagFloat) var4.tagAt(0)).floatValue;
		this.rotationPitch = ((NBTTagFloat) var4.tagAt(1)).floatValue;
		this.fallDistance = var1.getFloat("FallDistance");
		this.fire = var1.getShort("Fire");
		this.air = var1.getShort("Air");
		this.setPositionAndRotation(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
		this.readEntityFromNBT(var1);
	}

	protected abstract String getEntityString();

	protected abstract void readEntityFromNBT(NBTTagCompound var1);

	protected abstract void writeEntityToNBT(NBTTagCompound var1);

	private static NBTTagList newDoubleNBTList(float... var0) {
		NBTTagList var1 = new NBTTagList();
		var0 = var0;
		int var2 = var0.length;

		for (int var3 = 0; var3 < var2; ++var3) {
			float var4 = var0[var3];
			var1.setTag(new NBTTagFloat(var4));
		}

		return var1;
	}

	public float getShadowSize() {
		return this.height / 2.0F;
	}

	public final EntityItem dropItemWithOffset(int var1, int var2) {
		return this.entityDropItem(var1, 1, 0.0F);
	}

	public final EntityItem entityDropItem(int var1, int var2, float var3) {
		EntityItem var4 = new EntityItem(this.worldObj, this.posX, this.posY + var3, this.posZ, new ItemStack(var1, var2));
		var4.delayBeforeCanPickup = 10;
		this.worldObj.spawnEntityInWorld(var4);
		return var4;
	}

	public boolean isEntityAlive() {
		return !this.isDead;
	}
}
