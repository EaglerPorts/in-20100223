package net.minecraft.game.level.generator.noise;

import net.lax1dude.eaglercraft.Random;
import util.MathHelper;

public final class NoiseGeneratorPerlin extends NoiseGenerator {
	private int[] permutations;

	public NoiseGeneratorPerlin() {
		this(new Random());
	}

	public NoiseGeneratorPerlin(Random var1) {
		this.permutations = new int[512];

		int var2;
		for (var2 = 0; var2 < 256; this.permutations[var2] = var2++) {
		}

		for (var2 = 0; var2 < 256; ++var2) {
			int var3 = var1.nextInt(256 - var2) + var2;
			int var4 = this.permutations[var2];
			this.permutations[var2] = this.permutations[var3];
			this.permutations[var3] = var4;
			this.permutations[var2 + 256] = this.permutations[var2];
		}

	}

	private static double generateNoise(double var0) {
		return var0 * var0 * var0 * (var0 * (var0 * 6.0D - 15.0D) + 10.0D);
	}

	private static double lerp(double var0, double var2, double var4) {
		return var2 + var0 * (var4 - var2);
	}

	private static double grad(int var0, double var1, double var3, double var5) {
		var0 &= 15;
		double var8 = var0 < 8 ? var1 : var3;
		double var10 = var0 < 4 ? var3 : (var0 != 12 && var0 != 14 ? var5 : var1);
		return ((var0 & 1) == 0 ? var8 : -var8) + ((var0 & 2) == 0 ? var10 : -var10);
	}

	public final double generateNoise(double var1, double var3) {
		double var10 = 0.0D;
		double var8 = var3;
		int var2 = MathHelper.floor_double(var1) & 255;
		int var21 = MathHelper.floor_double(var3) & 255;
		int var4 = MathHelper.floor_double(0.0D) & 255;
		double var6 = var1 - (double) MathHelper.floor_double(var1);
		var8 -= (double) MathHelper.floor_double(var8);
		var10 = 0.0D - (double) MathHelper.floor_double(0.0D);
		double var15 = generateNoise(var6);
		double var17 = generateNoise(var8);
		double var19 = generateNoise(var10);
		int var5 = this.permutations[var2] + var21;
		int var12 = this.permutations[var5] + var4;
		var5 = this.permutations[var5 + 1] + var4;
		var2 = this.permutations[var2 + 1] + var21;
		var21 = this.permutations[var2] + var4;
		var2 = this.permutations[var2 + 1] + var4;
		return lerp(var19,
				lerp(var17,
						lerp(var15, grad(this.permutations[var12], var6, var8, var10),
								grad(this.permutations[var21], var6 - 1.0D, var8, var10)),
						lerp(var15, grad(this.permutations[var5], var6, var8 - 1.0D, var10),
								grad(this.permutations[var2], var6 - 1.0D, var8 - 1.0D, var10))),
				lerp(var17,
						lerp(var15, grad(this.permutations[var12 + 1], var6, var8, var10 - 1.0D),
								grad(this.permutations[var21 + 1], var6 - 1.0D, var8, var10 - 1.0D)),
						lerp(var15, grad(this.permutations[var5 + 1], var6, var8 - 1.0D, var10 - 1.0D),
								grad(this.permutations[var2 + 1], var6 - 1.0D, var8 - 1.0D, var10 - 1.0D))));
	}
}
