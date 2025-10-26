package net.minecraft.game.entity.monster;

import com.mojang.nbt.NBTTagCompound;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.item.Item;
import net.minecraft.game.level.World;
import util.MathHelper;

public class EntitySpider extends EntityMob {
	public EntitySpider(World var1) {
		super(var1);
		this.texture = "/mob/spider.png";
		this.setSize(1.4F, 0.9F);
		this.moveSpeed = 0.8F;
	}

	protected final Entity findPlayerToAttack() {
		float var1 = this.getEntityBrightness(1.0F);
		if(var1 < 0.5F) {
			var1 = this.worldObj.playerEntity.getDistanceSqToEntity(this);
			if(var1 < 256.0F) {
				return this.worldObj.playerEntity;
			}
		}

		return null;
	}

	protected final void attackEntity(Entity var1, float var2) {
		float var3 = this.getEntityBrightness(1.0F);
		if(var3 > 0.5F && this.rand.nextInt(100) == 0) {
			this.playerToAttack = null;
		} else {
			if(var2 > 2.0F && var2 < 6.0F && this.rand.nextInt(10) == 0) {
				if(this.onGround) {
					var2 = var1.posX - this.posX;
					float var4 = var1.posZ - this.posZ;
					var3 = MathHelper.sqrt_float(var2 * var2 + var4 * var4);
					this.motionX = var2 / var3 * 0.5F * 0.8F + this.motionX * 0.2F;
					this.motionZ = var4 / var3 * 0.5F * 0.8F + this.motionZ * 0.2F;
					this.motionY = 0.4F;
					return;
				}
			} else {
				super.attackEntity(var1, var2);
			}

		}
	}

	protected final void writeEntityToNBT(NBTTagCompound var1) {
		super.writeEntityToNBT(var1);
	}

	protected final void readEntityFromNBT(NBTTagCompound var1) {
		super.readEntityFromNBT(var1);
	}

	protected final String getEntityString() {
		return "Spider";
	}

	protected final int scoreValue() {
		return Item.silk.shiftedIndex;
	}
}
