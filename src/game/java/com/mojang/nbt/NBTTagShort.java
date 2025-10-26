package com.mojang.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public final class NBTTagShort extends NBTBase {
	public short shortValue;

	public NBTTagShort() {
	}

	public NBTTagShort(short var1) {
		this.shortValue = var1;
	}

	final void writeTagContents(DataOutput var1) throws IOException {
		var1.writeShort(this.shortValue);
	}

	final void readTagContents(DataInput var1) throws IOException {
		this.shortValue = var1.readShort();
	}

	public final byte getType() {
		return (byte)2;
	}

	public final String toString() {
		return "" + this.shortValue;
	}
}
