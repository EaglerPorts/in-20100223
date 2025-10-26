package net.minecraft.client.sound;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.lax1dude.eaglercraft.internal.vfs2.VFile2;

public final class SoundPool {
	private Random rand = new Random();
	private Map nameToSoundPoolEntriesMapping = new HashMap();
	private int numberOfSoundPoolEntries = 0;

	public final SoundPoolEntry addSound(String var1, VFile2 var2) {
		String var3 = var1;

		for(var1 = var1.substring(0, var1.indexOf(".")); Character.isDigit(var1.charAt(var1.length() - 1)); var1 = var1.substring(0, var1.length() - 1)) {
		}

		var1 = var1.replaceAll("/", ".");
		if(!this.nameToSoundPoolEntriesMapping.containsKey(var1)) {
			this.nameToSoundPoolEntriesMapping.put(var1, new ArrayList());
		}

		SoundPoolEntry var5 = new SoundPoolEntry(var3);
		((List)this.nameToSoundPoolEntriesMapping.get(var1)).add(var5);
		++this.numberOfSoundPoolEntries;
		return var5;
	}

	public final SoundPoolEntry getRandomSoundFromSoundPool(String var1) {
		List var2 = (List)this.nameToSoundPoolEntriesMapping.get(var1);
		return var2 == null ? null : (SoundPoolEntry)var2.get(this.rand.nextInt(var2.size()));
	}
}
