package com.mojang.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public final class NBTTagLong extends NBTBase {
	public long longValue;

	public NBTTagLong() {
	}

	public NBTTagLong(long var1) {
		this.longValue = var1;
	}

	final void writeTagContents(DataOutput var1) throws IOException {
		var1.writeLong(this.longValue);
	}

	final void readTagContents(DataInput var1) throws IOException {
		this.longValue = var1.readLong();
	}

	public final byte getType() {
		return (byte)4;
	}

	public final String toString() {
		return "" + this.longValue;
	}
}
