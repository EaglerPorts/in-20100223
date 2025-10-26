package net.minecraft.client;

import com.mojang.nbt.NBTBase;
import com.mojang.nbt.NBTTagCompound;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.render.Tessellator;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import util.IProgressUpdate;

public class LoadingScreenRenderer implements IProgressUpdate {
	private String text;
	private Minecraft mc;
	private String title;
	private long start;

	public LoadingScreenRenderer(Minecraft var1) {
		this.text = "";
		this.title = "";
		this.start = System.currentTimeMillis();
		this.mc = var1;
	}

	public final void displayProgressMessage(String var1) {
		if(!this.mc.running) {
			throw new MinecraftError();
		} else {
			this.title = var1;
			ScaledResolution var3 = new ScaledResolution(this.mc.displayWidth, this.mc.displayHeight);
			int var2 = var3.getScaledWidth();
			int var4 = var3.getScaledHeight();
			GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glLoadIdentity();
			GL11.glOrtho(0.0D, (double)var2, (double)var4, 0.0D, 100.0D, 300.0D);
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			GL11.glLoadIdentity();
			GL11.glTranslatef(0.0F, 0.0F, -200.0F);
		}
	}

	public final void displayLoadingString(String var1) {
		if(!this.mc.running) {
			throw new MinecraftError();
		} else {
			this.start = 0L;
			this.text = var1;
			this.setLoadingProgress(-1);
			this.start = 0L;
		}
	}

	public final void setLoadingProgress(int var1) {
		if(!this.mc.running) {
			throw new MinecraftError();
		} else {
			long var2 = System.currentTimeMillis();
			if(var2 - this.start >= 20L) {
				this.start = var2;
				ScaledResolution var8 = new ScaledResolution(this.mc.displayWidth, this.mc.displayHeight);
				int var3 = var8.getScaledWidth();
				int var9 = var8.getScaledHeight();
				GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
				GL11.glMatrixMode(GL11.GL_PROJECTION);
				GL11.glLoadIdentity();
				GL11.glOrtho(0.0D, (double)var3, (double)var9, 0.0D, 100.0D, 300.0D);
				GL11.glMatrixMode(GL11.GL_MODELVIEW);
				GL11.glLoadIdentity();
				GL11.glTranslatef(0.0F, 0.0F, -200.0F);
				GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_COLOR_BUFFER_BIT);
				Tessellator var4 = Tessellator.instance;
				int var5 = this.mc.renderEngine.getTexture("/dirt.png");
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, var5);
				var4.startDrawingQuads();
				var4.setColorOpaque_I(4210752);
				var4.addVertexWithUV(0.0F, (float)var9, 0.0F, 0.0F, (float)var9 / 32.0F);
				var4.addVertexWithUV((float)var3, (float)var9, 0.0F, (float)var3 / 32.0F, (float)var9 / 32.0F);
				var4.addVertexWithUV((float)var3, 0.0F, 0.0F, (float)var3 / 32.0F, 0.0F);
				var4.addVertexWithUV(0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
				var4.draw();
				if(var1 >= 0) {
					var5 = var3 / 2 - 50;
					int var6 = var9 / 2 + 16;
					GL11.glDisable(GL11.GL_TEXTURE_2D);
					var4.startDrawingQuads();
					var4.setColorOpaque_I(8421504);
					var4.addVertex((float)var5, (float)var6, 0.0F);
					var4.addVertex((float)var5, (float)(var6 + 2), 0.0F);
					var4.addVertex((float)(var5 + 100), (float)(var6 + 2), 0.0F);
					var4.addVertex((float)(var5 + 100), (float)var6, 0.0F);
					var4.setColorOpaque_I(8454016);
					var4.addVertex((float)var5, (float)var6, 0.0F);
					var4.addVertex((float)var5, (float)(var6 + 2), 0.0F);
					var4.addVertex((float)(var5 + var1), (float)(var6 + 2), 0.0F);
					var4.addVertex((float)(var5 + var1), (float)var6, 0.0F);
					var4.draw();
					GL11.glEnable(GL11.GL_TEXTURE_2D);
				}

				this.mc.fontRenderer.drawStringWithShadow(this.title, (var3 - this.mc.fontRenderer.getStringWidth(this.title)) / 2, var9 / 2 - 4 - 16, 16777215);
				this.mc.fontRenderer.drawStringWithShadow(this.text, (var3 - this.mc.fontRenderer.getStringWidth(this.text)) / 2, var9 / 2 - 4 + 8, 16777215);
				Display.update();
			}
		}
	}

	public LoadingScreenRenderer() {
	}

	public static NBTTagCompound writeLevelTags(InputStream var0) throws IOException {
		DataInputStream var4 = new DataInputStream(new GZIPInputStream(var0));

		NBTTagCompound var5;
		try {
			NBTBase var1 = NBTBase.readTag(var4);
			if(!(var1 instanceof NBTTagCompound)) {
				throw new IOException("Root tag must be a named compound tag");
			}

			var5 = (NBTTagCompound)var1;
		} finally {
			var4.close();
		}

		return var5;
	}
}
