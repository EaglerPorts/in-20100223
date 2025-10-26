package com.mojang.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public final class NBTTagDouble extends NBTBase {
	private double doubleValue;

	final void writeTagContents(DataOutput var1) throws IOException {
		var1.writeDouble(this.doubleValue);
	}

	final void readTagContents(DataInput var1) throws IOException {
		this.doubleValue = var1.readDouble();
	}

	public final byte getType() {
		return (byte)6;
	}

	public final String toString() {
		return "" + this.doubleValue;
	}
}
