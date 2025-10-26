package util;

import net.lax1dude.eaglercraft.EagRuntime;
import net.lax1dude.eaglercraft.internal.buffer.ByteBuffer;
import net.lax1dude.eaglercraft.internal.buffer.FloatBuffer;
import net.lax1dude.eaglercraft.internal.buffer.IntBuffer;

public class GLAllocation {
	public static synchronized ByteBuffer createDirectByteBuffer(int var0) {
		return EagRuntime.allocateByteBuffer(var0);
	}

	public static IntBuffer createIntBuffer(int var0) {
		return createDirectByteBuffer(var0 << 2).asIntBuffer();
	}

	public static FloatBuffer createFloatBuffer(int var0) {
		return createDirectByteBuffer(var0 << 2).asFloatBuffer();
	}
}