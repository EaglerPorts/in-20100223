package net.minecraft.game.level.generator.noise;

public final class NoiseGeneratorDistort extends NoiseGenerator {
	private NoiseGenerator source;
	private NoiseGenerator distort;

	public NoiseGeneratorDistort(NoiseGenerator var1, NoiseGenerator var2) {
		this.source = var1;
		this.distort = var2;
	}

	public final double generateNoise(double var1, double var3) {
		return this.source.generateNoise(var1 + this.distort.generateNoise(var1, var3), var3);
	}
}
