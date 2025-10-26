package net.minecraft.client.render.texture;

import net.minecraft.game.level.block.Block;

public final class TextureFlamesFX extends TextureFX {
	private float[] currentFireFrame = new float[320];
	private float[] lastFireFrame = new float[320];

	public TextureFlamesFX(int var1) {
		super(Block.fire.blockIndexInTexture + (var1 << 4));
	}

	public final void onTick() {
		int var1;
		int var2;
		int var3;
		int var5;
		int var6;
		for(var1 = 0; var1 < 16; ++var1) {
			for(var2 = 0; var2 < 20; ++var2) {
				var3 = 18;
				float var4 = this.currentFireFrame[var1 + ((var2 + 1) % 20 << 4)] * 18.0F;

				for(var5 = var1 - 1; var5 <= var1 + 1; ++var5) {
					for(var6 = var2; var6 <= var2 + 1; ++var6) {
						if(var5 >= 0 && var6 >= 0 && var5 < 16 && var6 < 20) {
							var4 += this.currentFireFrame[var5 + (var6 << 4)];
						}

						++var3;
					}
				}

				this.lastFireFrame[var1 + (var2 << 4)] = var4 / ((float)var3 * 1.06F);
				if(var2 >= 19) {
					this.lastFireFrame[var1 + (var2 << 4)] = (float)(Math.random() * Math.random() * Math.random() * 4.0D + Math.random() * (double)0.1F + (double)0.2F);
				}
			}
		}

		float[] var9 = this.lastFireFrame;
		this.lastFireFrame = this.currentFireFrame;
		this.currentFireFrame = var9;

		for(var2 = 0; var2 < 256; ++var2) {
			float var10 = this.currentFireFrame[var2] * 1.8F;
			if(var10 > 1.0F) {
				var10 = 1.0F;
			}

			if(var10 < 0.0F) {
				var10 = 0.0F;
			}

			var5 = (int)(var10 * 155.0F + 100.0F);
			var6 = (int)(var10 * var10 * 255.0F);
			int var7 = (int)(var10 * var10 * var10 * var10 * var10 * var10 * var10 * var10 * var10 * var10 * 255.0F);
			short var8 = 255;
			if(var10 < 0.5F) {
				var8 = 0;
			}

			if(this.anaglyphEnabled) {
				var1 = (var5 * 30 + var6 * 59 + var7 * 11) / 100;
				var3 = (var5 * 30 + var6 * 70) / 100;
				int var11 = (var5 * 30 + var7 * 70) / 100;
				var5 = var1;
				var6 = var3;
				var7 = var11;
			}

			this.imageData[var2 << 2] = (byte)var5;
			this.imageData[(var2 << 2) + 1] = (byte)var6;
			this.imageData[(var2 << 2) + 2] = (byte)var7;
			this.imageData[(var2 << 2) + 3] = (byte)var8;
		}

	}
}
