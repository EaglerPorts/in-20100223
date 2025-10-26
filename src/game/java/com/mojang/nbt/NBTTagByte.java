package com.mojang.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public final class NBTTagByte extends NBTBase {
	public byte byteValue;

	public NBTTagByte() {
	}

	public NBTTagByte(byte var1) {
		this.byteValue = var1;
	}

	final void writeTagContents(DataOutput var1) throws IOException {
		var1.writeByte(this.byteValue);
	}

	final void readTagContents(DataInput var1) throws IOException {
		this.byteValue = var1.readByte();
	}

	public final byte getType() {
		return (byte)1;
	}

	public final String toString() {
		return "" + this.byteValue;
	}
}
