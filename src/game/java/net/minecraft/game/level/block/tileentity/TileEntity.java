package net.minecraft.game.level.block.tileentity;

import com.mojang.nbt.NBTTagCompound;
import net.minecraft.game.level.World;

public class TileEntity {
	public World worldObj;
	public int xCoord;
	public int yCoord;
	public int zCoord;

	public void readFromNBT(NBTTagCompound var1) {
	}

	public void writeToNBT(NBTTagCompound var1) {
	}

	public void updateEntity() {
	}
}
