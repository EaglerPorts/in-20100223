package net.minecraft.game.level;

public final class NextTickListEntry {
	public int xCoord;
	public int yCoord;
	public int zCoord;
	public int blockID;
	public int scheduledTime;

	public NextTickListEntry(int var1, int var2, int var3, int var4) {
		this.xCoord = var1;
		this.yCoord = var2;
		this.zCoord = var3;
		this.blockID = var4;
	}
}
