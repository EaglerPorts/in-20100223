package com.mojang.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public final class NBTTagByteArray extends NBTBase {
	public byte[] byteArray;

	public NBTTagByteArray() {
	}

	public NBTTagByteArray(byte[] var1) {
		this.byteArray = var1;
	}

	final void writeTagContents(DataOutput var1) throws IOException {
		var1.writeInt(this.byteArray.length);
		var1.write(this.byteArray);
	}

	final void readTagContents(DataInput var1) throws IOException {
		int var2 = var1.readInt();
		this.byteArray = new byte[var2];
		var1.readFully(this.byteArray);
	}

	public final byte getType() {
		return (byte)7;
	}

	public final String toString() {
		return "[" + this.byteArray.length + " bytes]";
	}
}
