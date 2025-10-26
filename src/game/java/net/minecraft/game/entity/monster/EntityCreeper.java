package net.minecraft.game.entity.monster;

import com.mojang.nbt.NBTTagCompound;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.item.Item;
import net.minecraft.game.level.World;

public class EntityCreeper extends EntityMob {
	private int timeSinceIgnited;
	private int lastActiveTime;
	private int fuseTime = 30;
	private int creeperState = -1;

	public EntityCreeper(World var1) {
		super(var1);
		this.texture = "/mob/creeper.png";
	}

	protected final void writeEntityToNBT(NBTTagCompound var1) {
		super.writeEntityToNBT(var1);
	}

	protected final void readEntityFromNBT(NBTTagCompound var1) {
		super.readEntityFromNBT(var1);
	}

	protected final String getEntityString() {
		return "Creeper";
	}

	protected final void updatePlayerActionState() {
		this.lastActiveTime = this.timeSinceIgnited;
		if(this.timeSinceIgnited > 0 && this.creeperState < 0) {
			--this.timeSinceIgnited;
		}

		if(this.creeperState >= 0) {
			this.creeperState = 2;
		}

		super.updatePlayerActionState();
		if(this.creeperState != 1) {
			this.creeperState = -1;
		}

	}

	protected final void attackEntity(Entity var1, float var2) {
		if(this.creeperState <= 0 && var2 < 3.0F || this.creeperState > 0 && var2 < 7.0F) {
			if(this.timeSinceIgnited == 0) {
				this.worldObj.playSoundAtEntity(this, "random.fuse", 1.0F, 0.5F);
			}

			this.creeperState = 1;
			++this.timeSinceIgnited;
			if(this.timeSinceIgnited == this.fuseTime) {
				this.worldObj.createExplosion(this, this.posX, this.posY, this.posZ, 3.0F);
				this.setEntityDead();
			}

			this.hasAttacked = true;
		}

	}

	public final float c(float var1) {
		return ((float)this.lastActiveTime + (float)(this.timeSinceIgnited - this.lastActiveTime) * var1) / (float)(this.fuseTime - 2);
	}

	protected final int scoreValue() {
		return Item.gunpowder.shiftedIndex;
	}
}
