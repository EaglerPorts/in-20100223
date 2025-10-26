package net.minecraft.game.entity;

import net.minecraft.game.level.World;
import net.minecraft.game.level.path.PathEntity;
import net.minecraft.game.physics.Vec3D;
import util.MathHelper;

public class EntityCreature extends EntityLiving {
	private PathEntity pathToEntity;
	protected Entity playerToAttack;
	protected boolean hasAttacked = false;

	public EntityCreature(World var1) {
		super(var1);
	}

	protected void updatePlayerActionState() {
		this.hasAttacked = false;
		float var6;
		float var7;
		float var8;
		if(this.playerToAttack == null) {
			this.playerToAttack = this.findPlayerToAttack();
			if(this.playerToAttack != null) {
				this.pathToEntity = this.worldObj.pathFinder.createEntityPathTo(this, this.playerToAttack, 16.0F);
			}
		} else if(!this.playerToAttack.isEntityAlive()) {
			this.playerToAttack = null;
		} else {
			Entity var5 = this.playerToAttack;
			var7 = var5.posX - super.posX;
			var8 = var5.posY - super.posY;
			var6 = var5.posZ - super.posZ;
			float var1 = MathHelper.sqrt_float(var7 * var7 + var8 * var8 + var6 * var6);
			if(this.worldObj.rayTraceBlocks(new Vec3D(this.posX, this.posY + this.getEyeHeight(), this.posZ), new Vec3D(this.playerToAttack.posX, this.playerToAttack.posY + this.playerToAttack.getEyeHeight(), this.playerToAttack.posZ)) == null) {
				this.attackEntity(this.playerToAttack, var1);
			}
		}

		if(this.hasAttacked) {
			this.moveStrafing = 0.0F;
			this.moveForward = 0.0F;
			this.isJumping = false;
		} else {
			float var4;
			if(this.playerToAttack == null || this.pathToEntity != null && this.rand.nextInt(20) != 0) {
				if(this.pathToEntity == null || this.rand.nextInt(100) == 0) {
					int var10 = -1;
					int var2 = -1;
					int var3 = -1;
					var4 = -99999.0F;

					for(int var14 = 0; var14 < 200; ++var14) {
						int var16 = (int)(this.posX + (float)this.rand.nextInt(21) - 10.0F);
						int var17 = (int)(this.posY + (float)this.rand.nextInt(9) - 4.0F);
						int var18 = (int)(this.posZ + (float)this.rand.nextInt(21) - 10.0F);
						float var9 = this.getBlockPathWeight(var16, var17, var18);
						if(var9 > var4) {
							var4 = var9;
							var10 = var16;
							var2 = var17;
							var3 = var18;
						}
					}

					if(var10 > 0) {
						this.pathToEntity = this.worldObj.pathFinder.createEntityPathTo(this, var10, var2, var3, 16.0F);
					}
				}
			} else {
				this.pathToEntity = this.worldObj.pathFinder.createEntityPathTo(this, this.playerToAttack, 16.0F);
			}

			boolean var11 = this.handleWaterMovement();
			boolean var12 = this.handleLavaMovement();
			if(this.pathToEntity != null && this.rand.nextInt(100) != 0) {
				Vec3D var13 = this.pathToEntity.getPosition(this);
				var4 = this.width * 2.0F;

				float var15;
				while(var13 != null) {
					var8 = this.posZ;
					var7 = this.posY;
					var6 = this.posX;
					var6 -= var13.xCoord;
					var7 -= var13.yCoord;
					var15 = var8 - var13.zCoord;
					if(var6 * var6 + var7 * var7 + var15 * var15 >= var4 * var4 || var13.yCoord > this.posY) {
						break;
					}

					this.pathToEntity.incrementPathIndex();
					if(this.pathToEntity.isFinished()) {
						var13 = null;
						this.pathToEntity = null;
					} else {
						var13 = this.pathToEntity.getPosition(this);
					}
				}

				this.isJumping = false;
				if(var13 != null) {
					var15 = var13.xCoord - this.posX;
					var6 = var13.zCoord - this.posZ;
					var7 = var13.yCoord - this.posY;
					this.rotationYaw = (float)(Math.atan2((double)var6, (double)var15) * 180.0D / (double)((float)Math.PI)) - 90.0F;
					this.moveForward = this.moveSpeed;
					if(var7 > 0.0F) {
						this.isJumping = true;
					}
				}

				if(this.rand.nextFloat() < 0.8F && (var11 || var12)) {
					this.isJumping = true;
				}

			} else {
				super.updatePlayerActionState();
				this.pathToEntity = null;
			}
		}
	}

	protected void attackEntity(Entity var1, float var2) {
	}

	protected float getBlockPathWeight(int var1, int var2, int var3) {
		return 0.0F;
	}

	protected Entity findPlayerToAttack() {
		return null;
	}

	public boolean getCanSpawnHere(float var1, float var2, float var3) {
		return super.getCanSpawnHere(var1, var2, var3) && this.getBlockPathWeight((int)var1, (int)var2, (int)var3) >= 0.0F;
	}
}
