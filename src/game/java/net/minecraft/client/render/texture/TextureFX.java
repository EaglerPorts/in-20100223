package net.minecraft.client.render.texture;

public class TextureFX {
	public byte[] imageData = new byte[1024];
	public int iconIndex;
	public boolean anaglyphEnabled = false;
	public int textureId = 0;

	public TextureFX(int var1) {
		this.iconIndex = var1;
	}

	public void onTick() {
	}
}
