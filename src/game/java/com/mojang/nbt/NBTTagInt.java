package com.mojang.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public final class NBTTagInt extends NBTBase {
	public int intValue;

	public NBTTagInt() {
	}

	public NBTTagInt(int var1) {
		this.intValue = var1;
	}

	final void writeTagContents(DataOutput var1) throws IOException {
		var1.writeInt(this.intValue);
	}

	final void readTagContents(DataInput var1) throws IOException {
		this.intValue = var1.readInt();
	}

	public final byte getType() {
		return (byte)3;
	}

	public final String toString() {
		return "" + this.intValue;
	}
}
