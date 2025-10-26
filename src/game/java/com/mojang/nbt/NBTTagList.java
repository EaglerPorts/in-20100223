package com.mojang.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class NBTTagList extends NBTBase {
	private List tagList = new ArrayList();
	private byte tagType;

	final void writeTagContents(DataOutput var1) throws IOException {
		if(this.tagList.size() > 0) {
			this.tagType = ((NBTBase)this.tagList.get(0)).getType();
		} else {
			this.tagType = 1;
		}

		var1.writeByte(this.tagType);
		var1.writeInt(this.tagList.size());

		for(int var2 = 0; var2 < this.tagList.size(); ++var2) {
			((NBTBase)this.tagList.get(var2)).writeTagContents(var1);
		}

	}

	final void readTagContents(DataInput var1) throws IOException {
		this.tagType = var1.readByte();
		int var2 = var1.readInt();
		this.tagList = new ArrayList();

		for(int var3 = 0; var3 < var2; ++var3) {
			NBTBase var4 = NBTBase.createTagOfType(this.tagType);
			var4.readTagContents(var1);
			this.tagList.add(var4);
		}

	}

	public final byte getType() {
		return (byte)9;
	}

	public final String toString() {
		StringBuilder var10000 = (new StringBuilder()).append("").append(this.tagList.size()).append(" entries of type ");
		byte var1 = this.tagType;
		String var10001;
		switch(var1) {
		case 0:
			var10001 = "TAG_End";
			break;
		case 1:
			var10001 = "TAG_Byte";
			break;
		case 2:
			var10001 = "TAG_Short";
			break;
		case 3:
			var10001 = "TAG_Int";
			break;
		case 4:
			var10001 = "TAG_Long";
			break;
		case 5:
			var10001 = "TAG_Float";
			break;
		case 6:
			var10001 = "TAG_Double";
			break;
		case 7:
			var10001 = "TAG_Byte_Array";
			break;
		case 8:
			var10001 = "TAG_String";
			break;
		case 9:
			var10001 = "TAG_List";
			break;
		case 10:
			var10001 = "TAG_Compound";
			break;
		default:
			var10001 = "UNKNOWN";
		}

		return var10000.append(var10001).toString();
	}

	public final void setTag(NBTBase var1) {
		this.tagType = var1.getType();
		this.tagList.add(var1);
	}

	public final NBTBase tagAt(int var1) {
		return (NBTBase)this.tagList.get(var1);
	}

	public final int tagCount() {
		return this.tagList.size();
	}
}
