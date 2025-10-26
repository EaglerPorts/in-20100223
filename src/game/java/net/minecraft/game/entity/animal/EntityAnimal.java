package net.minecraft.game.entity.animal;

import com.mojang.nbt.NBTTagCompound;
import net.minecraft.game.entity.EntityCreature;
import net.minecraft.game.level.World;
import net.minecraft.game.level.block.Block;

public abstract class EntityAnimal extends EntityCreature {
	public EntityAnimal(World var1) {
		super(var1);
	}

	protected final float getBlockPathWeight(int var1, int var2, int var3) {
		return this.worldObj.getBlockId(var1, var2 - 1, var3) == Block.grass.blockID ? 10.0F : this.worldObj.getLightBrightness(var1, var2, var3) - 0.5F;
	}

	protected void writeEntityToNBT(NBTTagCompound var1) {
		super.writeEntityToNBT(var1);
	}

	protected void readEntityFromNBT(NBTTagCompound var1) {
		super.readEntityFromNBT(var1);
	}

	public final boolean getCanSpawnHere(float var1, float var2, float var3) {
		return this.worldObj.getBlockLightValue((int)var1, (int)var2, (int)var3) > 8 && super.getCanSpawnHere(var1, var2, var3);
	}
}
