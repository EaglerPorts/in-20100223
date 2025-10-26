package net.minecraft.game.entity.monster;

import com.mojang.nbt.NBTTagCompound;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.projectile.EntityArrow;
import net.minecraft.game.item.Item;
import net.minecraft.game.level.World;
import util.MathHelper;

public class EntitySkeleton extends EntityMob {
	public EntitySkeleton(World var1) {
		super(var1);
		this.texture = "/mob/skeleton.png";
	}

	public final void onLivingUpdate() {
		if(this.worldObj.skylightSubtracted > 7) {
			float var1 = this.getEntityBrightness(1.0F);
			if(var1 > 0.5F && this.worldObj.canBlockSeeTheSky((int)this.posX, (int)this.posY, (int)this.posZ) && this.rand.nextFloat() * 30.0F < (var1 - 0.4F) * 2.0F) {
				this.fire = 300;
			}
		}

		super.onLivingUpdate();
	}

	protected final void attackEntity(Entity var1, float var2) {
		if(var2 < 10.0F) {
			var2 = var1.posX - this.posX;
			float var3 = var1.posZ - this.posZ;
			if(this.attackTime == 0) {
				EntityArrow var4 = new EntityArrow(this.worldObj, this);
				++var4.posY;
				float var6 = var1.posY - 0.2F - var4.posY;
				float var5 = MathHelper.sqrt_float(var2 * var2 + var3 * var3) * 0.2F;
				this.worldObj.playSoundAtEntity(this, "random.bow", 1.0F, 1.0F / (this.rand.nextFloat() * 0.4F + 0.8F));
				this.worldObj.spawnEntityInWorld(var4);
				var4.setArrowHeading(var2, var6 + var5, var3, 0.6F, 12.0F);
				this.attackTime = 30;
			}

			this.rotationYaw = (float)(Math.atan2((double)var3, (double)var2) * 180.0D / (double)((float)Math.PI)) - 90.0F;
			this.hasAttacked = true;
		}

	}

	protected final void writeEntityToNBT(NBTTagCompound var1) {
		super.writeEntityToNBT(var1);
	}

	protected final void readEntityFromNBT(NBTTagCompound var1) {
		super.readEntityFromNBT(var1);
	}

	protected final String getEntityString() {
		return "Skeleton";
	}

	protected final int scoreValue() {
		return Item.arrow.shiftedIndex;
	}
}
