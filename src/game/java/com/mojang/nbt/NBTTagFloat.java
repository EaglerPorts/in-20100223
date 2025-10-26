package com.mojang.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public final class NBTTagFloat extends NBTBase {
	public float floatValue;

	public NBTTagFloat() {
	}

	public NBTTagFloat(float var1) {
		this.floatValue = var1;
	}

	final void writeTagContents(DataOutput var1) throws IOException {
		var1.writeFloat(this.floatValue);
	}

	final void readTagContents(DataInput var1) throws IOException {
		this.floatValue = var1.readFloat();
	}

	public final byte getType() {
		return (byte)5;
	}

	public final String toString() {
		return "" + this.floatValue;
	}
}
