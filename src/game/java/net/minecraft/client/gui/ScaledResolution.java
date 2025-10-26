package net.minecraft.client.gui;

public final class ScaledResolution {
	private int scaledWidth;
	private int scaledHeight;

	public ScaledResolution(int var1, int var2) {
		this.scaledWidth = var1;
		this.scaledHeight = var2;

		for(var1 = 1; this.scaledWidth / (var1 + 1) >= 320 && this.scaledHeight / (var1 + 1) >= 240; ++var1) {
		}

		this.scaledWidth /= var1;
		this.scaledHeight /= var1;
	}

	public final int getScaledWidth() {
		return this.scaledWidth;
	}

	public final int getScaledHeight() {
		return this.scaledHeight;
	}
}
