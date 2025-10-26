package util;

public final class MathHelper {
	private static final int[] MULTIPLY_DE_BRUIJN_BIT_POSITION = new int[] { 0, 1, 28, 2, 29, 14, 24, 3, 30, 22, 20, 15,
			25, 17, 4, 8, 31, 27,
			13, 23, 21, 19, 16, 7, 26, 12, 18, 6, 11, 5, 10, 9 };
	private static float[] SIN_TABLE = new float[65536];

	public static final float sin(float var0) {
		return SIN_TABLE[(int) (var0 * 10430.378F) & '\uffff'];
	}

	public static final float cos(float var0) {
		return SIN_TABLE[(int) (var0 * 10430.378F + 16384.0F) & '\uffff'];
	}

	public static final float sqrt_float(float var0) {
		return (float) Math.sqrt((double) var0);
	}

	public static int floor_float(float var0) {
		int var1 = (int) var0;
		return var0 < (float) var1 ? var1 - 1 : var1;
	}

	public static int floor_double(double var0) {
		int var2 = (int) var0;
		return var0 < (double) var2 ? var2 - 1 : var2;
	}

	public static float abs(float var0) {
		return var0 >= 0.0F ? var0 : -var0;
	}

	public static final float sqrt_double(double var0) {
		return (float) Math.sqrt(var0);
	}

	public static int clamp(int var0, int var1, int var2) {
		return Math.max(var1, Math.min(var2, var0));
	}

	public static float clamp_float(float var0, float var1, float var2) {
		return Math.max(var1, Math.min(var2, var0));
	}

	private static boolean isPowerOfTwo(int value) {
		return value != 0 && (value & value - 1) == 0;
	}

	private static int smallestEncompassingPowerOfTwo(int value) {
		int i = value - 1;
		i = i | i >> 1;
		i = i | i >> 2;
		i = i | i >> 4;
		i = i | i >> 8;
		i = i | i >> 16;
		return i + 1;
	}

	public static int log2DeBruijn(int value) {
		value = isPowerOfTwo(value) ? value : smallestEncompassingPowerOfTwo(value);
		return MULTIPLY_DE_BRUIJN_BIT_POSITION[(int) ((long) value * 125613361L >> 27) & 31];
	}

	static {
		for (int var0 = 0; var0 < 65536; ++var0) {
			SIN_TABLE[var0] = (float) Math.sin((double) var0 * Math.PI * 2.0D / 65536.0D);
		}

	}
}
