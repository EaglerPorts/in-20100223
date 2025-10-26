package com.mojang.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public abstract class NBTBase {
	private String key = null;

	abstract void writeTagContents(DataOutput var1) throws IOException;

	abstract void readTagContents(DataInput var1) throws IOException;

	public abstract byte getType();

	public final String getKey() {
		return this.key == null ? "" : this.key;
	}

	public final NBTBase setKey(String var1) {
		this.key = var1;
		return this;
	}

	public static NBTBase readTag(DataInput var0) throws IOException {
		byte var1 = var0.readByte();
		if(var1 == 0) {
			return new NBTTagEnd();
		} else {
			NBTBase var3 = createTagOfType(var1);
			short var2 = var0.readShort();
			byte[] var4 = new byte[var2];
			var0.readFully(var4);
			var3.key = new String(var4, "UTF-8");
			var3.readTagContents(var0);
			return var3;
		}
	}

	public static void writeTag(NBTBase var0, DataOutput var1) throws IOException {
		var1.writeByte(var0.getType());
		if(var0.getType() != 0) {
			byte[] var2 = var0.getKey().getBytes("UTF-8");
			var1.writeShort(var2.length);
			var1.write(var2);
			var0.writeTagContents(var1);
		}
	}

	public static NBTBase createTagOfType(byte var0) {
		switch(var0) {
		case 0:
			return new NBTTagEnd();
		case 1:
			return new NBTTagByte();
		case 2:
			return new NBTTagShort();
		case 3:
			return new NBTTagInt();
		case 4:
			return new NBTTagLong();
		case 5:
			return new NBTTagFloat();
		case 6:
			return new NBTTagDouble();
		case 7:
			return new NBTTagByteArray();
		case 8:
			return new NBTTagString();
		case 9:
			return new NBTTagList();
		case 10:
			return new NBTTagCompound();
		default:
			return null;
		}
	}
}
