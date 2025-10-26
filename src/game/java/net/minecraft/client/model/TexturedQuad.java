package net.minecraft.client.model;

public final class TexturedQuad {
	public PositionTextureVertex[] vertexPositions;

	private TexturedQuad(PositionTextureVertex[] var1) {
		this.vertexPositions = var1;
	}

	public TexturedQuad(PositionTextureVertex[] var1, int var2, int var3, int var4, int var5) {
		this(var1);
		var1[0] = var1[0].setTexturePosition((float)var4 / 64.0F - 0.0015625F, (float)var3 / 32.0F + 0.003125F);
		var1[1] = var1[1].setTexturePosition((float)var2 / 64.0F + 0.0015625F, (float)var3 / 32.0F + 0.003125F);
		var1[2] = var1[2].setTexturePosition((float)var2 / 64.0F + 0.0015625F, (float)var5 / 32.0F - 0.003125F);
		var1[3] = var1[3].setTexturePosition((float)var4 / 64.0F - 0.0015625F, (float)var5 / 32.0F - 0.003125F);
	}
}
