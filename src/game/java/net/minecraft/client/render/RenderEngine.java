package net.minecraft.client.render;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.lax1dude.eaglercraft.EagRuntime;
import net.lax1dude.eaglercraft.internal.buffer.ByteBuffer;
import net.lax1dude.eaglercraft.internal.buffer.IntBuffer;
import net.lax1dude.eaglercraft.opengl.ImageData;
import net.minecraft.client.GameSettings;
import net.minecraft.client.render.texture.TextureFX;
import util.GLAllocation;

import org.lwjgl.opengl.GL11;

public class RenderEngine {
	private HashMap textureMap = new HashMap();
	private HashMap textureContentsMap = new HashMap();
	private IntBuffer singleIntBuffer = GLAllocation.createIntBuffer(1);
	private ByteBuffer imageData = GLAllocation.createDirectByteBuffer(262144);
	private List textureList = new ArrayList();
	private GameSettings options;
	private boolean clampTexture = false;

	public RenderEngine(GameSettings var1) {
		this.options = var1;
	}

	public final int getTexture(String var1) {
		Integer var2 = (Integer)this.textureMap.get(var1);
		if(var2 != null) {
			return var2.intValue();
		} else {
			this.singleIntBuffer.clear();
			GL11.glGenTextures(this.singleIntBuffer);
			int var4 = this.singleIntBuffer.get(0);
			if(var1.startsWith("##")) {
				this.setupTexture(unwrapImageByColumns(ImageData.loadImageFile(EagRuntime.getResourceStream(var1.substring(2)))), var4);
			} else if(var1.startsWith("%%")) {
				this.clampTexture = true;
				this.setupTexture(ImageData.loadImageFile(EagRuntime.getResourceStream(var1.substring(2))), var4);
				this.clampTexture = false;
			} else {
				this.setupTexture(ImageData.loadImageFile(EagRuntime.getResourceStream(var1)), var4);
			}

			this.textureMap.put(var1, Integer.valueOf(var4));
			return var4;
		}
	}

	private ImageData unwrapImageByColumns(ImageData var1) {
		int var2 = var1.getWidth() / 16;
		ImageData var3 = new ImageData(16, var1.getHeight() * var2, true);

		int var4 = var1.getWidth();
		int var5 = var1.getHeight();

		for (int var6 = 0; var6 < var2; ++var6) {
			int var7 = var6 * 16;
			int var8 = var6 * var5;

			for (int var9 = 0; var9 < var5; ++var9) {
				int var10 = var7 + var9 * var4;
				int var11 = (var8 + var9) * 16;
				System.arraycopy(var1.pixels, var10, var3.pixels, var11, 16);
			}
		}

		return var3;
	}

	private void setupTexture(ImageData var1, int var2) {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, var2);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		if(this.clampTexture) {
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);
		} else {
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
		}

		var2 = var1.getWidth();
		int var3 = var1.getHeight();
		int[] var4 = new int[var2 * var3];
		byte[] var5 = new byte[var2 * var3 << 2];
		var1.getRGB(0, 0, var2, var3, var4, 0, var2);

		for(int var11 = 0; var11 < var4.length; ++var11) {
			int var6 = var4[var11] >>> 24;
			int var7 = var4[var11] >> 16 & 255;
			int var8 = var4[var11] >> 8 & 255;
			int var9 = var4[var11] & 255;
			if(this.options != null && this.options.anaglyph) {
				int var10 = (var7 * 30 + var8 * 59 + var9 * 11) / 100;
				var8 = (var7 * 30 + var8 * 70) / 100;
				var9 = (var7 * 30 + var9 * 70) / 100;
				var7 = var10;
				var8 = var8;
				var9 = var9;
			}

			var5[var11 << 2] = (byte)var9;
			var5[(var11 << 2) + 1] = (byte)var8;
			var5[(var11 << 2) + 2] = (byte)var7;
			var5[(var11 << 2) + 3] = (byte)var6;
		}

		this.imageData.clear();
		this.imageData.put(var5);
		this.imageData.position(0).limit(var5.length);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, var2, var3, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, (ByteBuffer)this.imageData);
	}


	public int getTextureForDownloadableImage(String var1, String var2) {
		return getTexture(var2);
	}

	public final void releaseImageData(String var1) {

	}

	public final void registerTextureFX(TextureFX var1) {
		this.textureList.add(var1);
		var1.onTick();
	}

	public final void updateDynamicTextures() {
		int var1;
		TextureFX var2;
		for(var1 = 0; var1 < this.textureList.size(); ++var1) {
			var2 = (TextureFX)this.textureList.get(var1);
			var2.anaglyphEnabled = this.options.anaglyph;
			var2.onTick();
			this.imageData.clear();
			this.imageData.put(var2.imageData);
			this.imageData.position(0).limit(var2.imageData.length);
			GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, var2.iconIndex % 16 << 4, var2.iconIndex / 16 << 4, 16, 16, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, (ByteBuffer)this.imageData);
		}

		for(var1 = 0; var1 < this.textureList.size(); ++var1) {
			var2 = (TextureFX)this.textureList.get(var1);
			if(var2.textureId > 0) {
				this.imageData.clear();
				this.imageData.put(var2.imageData);
				this.imageData.position(0).limit(var2.imageData.length);
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, var2.textureId);
				GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, 16, 16, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, (ByteBuffer)this.imageData);
			}
		}

	}

	public final void refreshTextures() {
		Iterator var1 = this.textureContentsMap.keySet().iterator();

		int var2;
		ImageData var3;
		while(var1.hasNext()) {
			var2 = ((Integer)var1.next()).intValue();
			var3 = (ImageData)this.textureContentsMap.get(Integer.valueOf(var2));
			this.setupTexture(var3, var2);
		}

		var1 = this.textureMap.keySet().iterator();

		while(var1.hasNext()) {
			String var6 = (String)var1.next();

			if(var6.startsWith("##")) {
				var3 = unwrapImageByColumns(ImageData.loadImageFile(EagRuntime.getResourceStream(var6.substring(2))));
			} else if(var6.startsWith("%%")) {
				this.clampTexture = true;
				var3 = ImageData.loadImageFile(EagRuntime.getResourceStream(var6.substring(2)));
				this.clampTexture = false;
			} else {
				var3 = ImageData.loadImageFile(EagRuntime.getResourceStream(var6));
			}

			var2 = ((Integer)this.textureMap.get(var6)).intValue();
			this.setupTexture(var3, var2);
		}

	}

	public static void bindTexture(int var0) {
		if(var0 >= 0) {
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, var0);
		}
	}
}
