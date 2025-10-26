package net.minecraft.game.entity;

import com.mojang.nbt.NBTTagCompound;
import java.util.List;
import net.minecraft.game.level.World;
import net.minecraft.game.level.block.Block;
import net.minecraft.game.level.block.StepSound;
import util.MathHelper;

public class EntityLiving extends Entity {
	public int heartsHalvesLife = 20;
	public float renderYawOffset = 0.0F;
	public float prevRenderYawOffset = 0.0F;
	private float rotationYawHead;
	private float prevRotationYawHead;
	protected String texture = "/char.png";
	private int scoreValue = 0;
	public int health;
	public int prevHealth;
	private int livingSoundTime;
	public int hurtTime;
	public int maxHurtTime;
	public float attackedAtYaw = 0.0F;
	public int deathTime = 0;
	public int attackTime = 0;
	public float prevCameraPitch;
	public float cameraPitch;
	public float prevLimbYaw;
	public float limbYaw;
	public float limbSwing;
	protected int entityAge;
	protected float moveStrafing;
	protected float moveForward;
	private float randomYawVelocity;
	protected boolean isJumping;
	private float defaultPitch;
	protected float moveSpeed;

	public EntityLiving(World var1) {
		super(var1);
		Math.random();
		this.entityAge = 0;
		this.isJumping = false;
		this.defaultPitch = 0.0F;
		this.moveSpeed = 0.7F;
		this.health = 10;
		this.preventEntitySpawning = true;
		Math.random();
		this.setPosition(this.posX, this.posY, this.posZ);
		Math.random();
		this.rotationYaw = (float)(Math.random() * (double)((float)Math.PI) * 2.0D);
		this.stepHeight = 0.5F;
	}

	public final String getTexture() {
		return this.texture;
	}

	public final boolean canBeCollidedWith() {
		return !this.isDead;
	}

	public final boolean canBePushed() {
		return !this.isDead;
	}

	protected float getEyeHeight() {
		return this.height * 0.85F;
	}

	public void onEntityUpdate() {
		super.onEntityUpdate();
		if(this.rand.nextInt(1000) < this.livingSoundTime++) {
			this.livingSoundTime = -80;
			String var1 = this.getLivingSound();
			if(var1 != null) {
				this.worldObj.playSoundAtEntity(this, var1, 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
			}
		}

		float var2;
		float var3;
		float var4;
		if(this.isInsideOfWater()) {
			--this.air;
			if(this.air == -20) {
				this.air = 0;

				for(int var7 = 0; var7 < 8; ++var7) {
					var2 = this.rand.nextFloat() - this.rand.nextFloat();
					var3 = this.rand.nextFloat() - this.rand.nextFloat();
					var4 = this.rand.nextFloat() - this.rand.nextFloat();
					this.worldObj.spawnParticle("bubble", this.posX + var2, this.posY + var3, this.posZ + var4, this.motionX, this.motionY, this.motionZ);
				}

				this.attackEntityFrom((Entity)null, 2);
			}

			this.fire = 0;
		} else {
			this.air = this.maxAir;
		}

		this.prevCameraPitch = this.cameraPitch;
		if(this.attackTime > 0) {
			--this.attackTime;
		}

		if(this.hurtTime > 0) {
			--this.hurtTime;
		}

		if(this.heartsLife > 0) {
			--this.heartsLife;
		}

		if(this.health <= 0) {
			++this.deathTime;
			if(this.deathTime > 20) {
				this.setEntityDead();
			}
		}

		this.prevRenderYawOffset = this.renderYawOffset;
		this.prevRotationYaw = this.rotationYaw;
		this.prevRotationPitch = this.rotationPitch;
		this.onLivingUpdate();
		float var8 = this.posX - this.prevPosX;
		var2 = this.posZ - this.prevPosZ;
		var3 = MathHelper.sqrt_float(var8 * var8 + var2 * var2);
		var4 = this.renderYawOffset;
		float var5 = 0.0F;
		float var6 = 0.0F;
		if(var3 > 0.05F) {
			var6 = 1.0F;
			var5 = var3 * 3.0F;
			var4 = (float)Math.atan2((double)var2, (double)var8) * 180.0F / (float)Math.PI - 90.0F;
		}

		if(!this.onGround) {
			var6 = 0.0F;
		}

		this.rotationYawHead += (var6 - this.rotationYawHead) * 0.3F;

		for(var8 = var4 - this.renderYawOffset; var8 < -180.0F; var8 += 360.0F) {
		}

		while(var8 >= 180.0F) {
			var8 -= 360.0F;
		}

		this.renderYawOffset += var8 * 0.1F;

		for(var8 = this.rotationYaw - this.renderYawOffset; var8 < -180.0F; var8 += 360.0F) {
		}

		while(var8 >= 180.0F) {
			var8 -= 360.0F;
		}

		boolean var9 = var8 < -90.0F || var8 >= 90.0F;
		if(var8 < -75.0F) {
			var8 = -75.0F;
		}

		if(var8 >= 75.0F) {
			var8 = 75.0F;
		}

		this.renderYawOffset = this.rotationYaw - var8;
		this.renderYawOffset += var8 * 0.1F;
		if(var9) {
			var5 = -var5;
		}

		while(this.rotationYaw - this.prevRotationYaw < -180.0F) {
			this.prevRotationYaw -= 360.0F;
		}

		while(this.rotationYaw - this.prevRotationYaw >= 180.0F) {
			this.prevRotationYaw += 360.0F;
		}

		while(this.renderYawOffset - this.prevRenderYawOffset < -180.0F) {
			this.prevRenderYawOffset -= 360.0F;
		}

		while(this.renderYawOffset - this.prevRenderYawOffset >= 180.0F) {
			this.prevRenderYawOffset += 360.0F;
		}

		while(this.rotationPitch - this.prevRotationPitch < -180.0F) {
			this.prevRotationPitch -= 360.0F;
		}

		while(this.rotationPitch - this.prevRotationPitch >= 180.0F) {
			this.prevRotationPitch += 360.0F;
		}

		this.prevRotationYawHead += var5;
	}

	protected final void setSize(float var1, float var2) {
		super.setSize(var1, var2);
	}

	public final void heal(int var1) {
		if(this.health > 0) {
			this.health += var1;
			if(this.health > 20) {
				this.health = 20;
			}

			this.heartsLife = this.heartsHalvesLife / 2;
		}
	}

	public boolean attackEntityFrom(Entity var1, int var2) {
		if(!this.worldObj.survivalWorld) {
			return false;
		} else {
			this.entityAge = 0;
			if(this.health <= 0) {
				return false;
			} else {
				this.limbYaw = 1.5F;
				if((float)this.heartsLife > (float)this.heartsHalvesLife / 2.0F) {
					if(this.prevHealth - var2 >= this.health) {
						return false;
					}

					this.health = this.prevHealth - var2;
				} else {
					this.prevHealth = this.health;
					this.heartsLife = this.heartsHalvesLife;
					this.health -= var2;
					this.hurtTime = this.maxHurtTime = 10;
				}

				this.attackedAtYaw = 0.0F;
				if(var1 != null) {
					float var6 = var1.posX - this.posX;
					float var3 = var1.posZ - this.posZ;
					this.attackedAtYaw = (float)(Math.atan2((double)var3, (double)var6) * 180.0D / (double)((float)Math.PI)) - this.rotationYaw;
					float var5 = MathHelper.sqrt_float(var6 * var6 + var3 * var3);
					this.motionX /= 2.0F;
					this.motionY /= 2.0F;
					this.motionZ /= 2.0F;
					this.motionX -= var6 / var5 * 0.4F;
					this.motionY += 0.4F;
					this.motionZ -= var3 / var5 * 0.4F;
					if(this.motionY > 0.4F) {
						this.motionY = 0.4F;
					}
				} else {
					this.attackedAtYaw = (float)((int)(Math.random() * 2.0D) * 180);
				}

				if(this.health <= 0) {
					this.worldObj.playSoundAtEntity(this, this.getDeathSound(), 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
					this.onDeath(var1);
				} else {
					this.worldObj.playSoundAtEntity(this, this.getHurtSound(), 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
				}

				return true;
			}
		}
	}

	protected String getLivingSound() {
		return null;
	}

	protected String getHurtSound() {
		return "random.hurt";
	}

	protected String getDeathSound() {
		return "random.hurt";
	}

	public void onDeath(Entity var1) {
		int var4 = this.scoreValue();
		if(var4 > 0) {
			int var2 = this.rand.nextInt(3);

			for(int var3 = 0; var3 < var2; ++var3) {
				this.dropItemWithOffset(var4, 1);
			}
		}

	}

	protected int scoreValue() {
		return 0;
	}

	protected final void fall(float var1) {
		int var3 = (int)Math.ceil((double)(var1 - 3.0F));
		if(var3 > 0) {
			this.attackEntityFrom((Entity)null, var3);
			var3 = this.worldObj.getBlockId((int)this.posX, (int)(this.posY - 0.2F - this.yOffset), (int)this.posZ);
			if(var3 > 0) {
				StepSound var4 = Block.blocksList[var3].stepSound;
				this.worldObj.playSoundAtEntity(this, var4.stepSoundDir2(), var4.soundVolume * 0.5F, var4.soundPitch * (12.0F / 16.0F));
			}
		}

	}

	protected void writeEntityToNBT(NBTTagCompound var1) {
		var1.setShort("Health", (short)this.health);
		var1.setShort("HurtTime", (short)this.hurtTime);
		var1.setShort("DeathTime", (short)this.deathTime);
		var1.setShort("AttackTime", (short)this.attackTime);
	}

	protected void readEntityFromNBT(NBTTagCompound var1) {
		this.health = var1.getShort("Health");
		if(!var1.hasKey("Health")) {
			this.health = 10;
		}

		this.hurtTime = var1.getShort("HurtTime");
		this.deathTime = var1.getShort("DeathTime");
		this.attackTime = var1.getShort("AttackTime");
	}

	protected String getEntityString() {
		return "Mob";
	}

	public final boolean isEntityAlive() {
		return !this.isDead && this.health > 0;
	}

	public void onLivingUpdate() {
		++this.entityAge;
		float var2;
		float var3;
		if(this.entityAge > 600 && this.rand.nextInt(800) == 0) {
			Entity var1 = this.worldObj.getPlayerEntity();
			if(var1 != null) {
				var2 = var1.posX - this.posX;
				var3 = var1.posY - this.posY;
				float var5 = var1.posZ - this.posZ;
				var2 = var2 * var2 + var3 * var3 + var5 * var5;
				if(var2 < 1024.0F) {
					this.entityAge = 0;
				} else {
					this.setEntityDead();
				}
			}
		}

		if(this.health <= 0) {
			this.isJumping = false;
			this.moveStrafing = 0.0F;
			this.moveForward = 0.0F;
			this.randomYawVelocity = 0.0F;
		} else {
			this.updatePlayerActionState();
		}

		boolean var6 = this.handleWaterMovement();
		boolean var8 = this.handleLavaMovement();
		if(this.isJumping) {
			if(var6) {
				this.motionY += 0.04F;
			} else if(var8) {
				this.motionY += 0.04F;
			} else if(this.onGround) {
				this.motionY = 0.42F;
			}
		}

		this.moveStrafing *= 0.98F;
		this.moveForward *= 0.98F;
		this.randomYawVelocity *= 0.9F;
		var3 = this.moveForward;
		var2 = this.moveStrafing;
		float var4;
		if(this.handleWaterMovement()) {
			var4 = this.posY;
			this.moveFlying(var2, var3, 0.02F);
			this.moveEntity(this.motionX, this.motionY, this.motionZ);
			this.motionX *= 0.8F;
			this.motionY *= 0.8F;
			this.motionZ *= 0.8F;
			this.motionY = (float)((double)this.motionY - 0.02D);
			if(this.isCollidedHorizontally && this.isOffsetPositionInLiquid(this.motionX, this.motionY + 0.6F - this.posY + var4, this.motionZ)) {
				this.motionY = 0.3F;
			}
		} else if(this.handleLavaMovement()) {
			var4 = this.posY;
			this.moveFlying(var2, var3, 0.02F);
			this.moveEntity(this.motionX, this.motionY, this.motionZ);
			this.motionX *= 0.5F;
			this.motionY *= 0.5F;
			this.motionZ *= 0.5F;
			this.motionY = (float)((double)this.motionY - 0.02D);
			if(this.isCollidedHorizontally && this.isOffsetPositionInLiquid(this.motionX, this.motionY + 0.6F - this.posY + var4, this.motionZ)) {
				this.motionY = 0.3F;
			}
		} else {
			this.moveFlying(var2, var3, this.onGround ? 0.1F : 0.02F);
			this.moveEntity(this.motionX, this.motionY, this.motionZ);
			this.motionX *= 0.91F;
			this.motionY *= 0.98F;
			this.motionZ *= 0.91F;
			this.motionY = (float)((double)this.motionY - 0.08D);
			if(this.onGround) {
				this.motionX *= 0.6F;
				this.motionZ *= 0.6F;
			}
		}

		this.prevLimbYaw = this.limbYaw;
		var4 = this.posX - this.prevPosX;
		var2 = this.posZ - this.prevPosZ;
		var2 = MathHelper.sqrt_float(var4 * var4 + var2 * var2) * 4.0F;
		if(var2 > 1.0F) {
			var2 = 1.0F;
		}

		this.limbYaw += (var2 - this.limbYaw) * 0.4F;
		this.limbSwing += this.limbYaw;
		List var9 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(0.2F, 0.0F, 0.2F));
		if(var9 != null && var9.size() > 0) {
			for(int var7 = 0; var7 < var9.size(); ++var7) {
				Entity var10 = (Entity)var9.get(var7);
				if(var10.canBePushed()) {
					var10.applyEntityCollision(this);
				}
			}
		}

	}

	protected void updatePlayerActionState() {
		if(this.rand.nextFloat() < 0.07F) {
			this.moveStrafing = (this.rand.nextFloat() - 0.5F) * this.moveSpeed;
			this.moveForward = this.rand.nextFloat() * this.moveSpeed;
		}

		this.isJumping = this.rand.nextFloat() < 0.01F;
		if(this.rand.nextFloat() < 0.04F) {
			this.randomYawVelocity = (this.rand.nextFloat() - 0.5F) * 60.0F;
		}

		this.rotationYaw += this.randomYawVelocity;
		this.rotationPitch = 0.0F;
		boolean var1 = this.handleWaterMovement();
		boolean var2 = this.handleLavaMovement();
		if(var1 || var2) {
			this.isJumping = this.rand.nextFloat() < 0.8F;
		}

	}

	public boolean getCanSpawnHere(float var1, float var2, float var3) {
		this.setPosition(var1, var2 + this.height / 2.0F, var3);
		return this.worldObj.checkIfAABBIsClear1(this.boundingBox) && this.worldObj.getCollidingBoundingBoxes(this.boundingBox).size() == 0 && !this.worldObj.getIsAnyLiquid(this.boundingBox);
	}
}
