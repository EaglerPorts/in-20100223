package net.minecraft.client.gui;

import net.minecraft.client.player.EntityPlayerSP;
import org.lwjgl.opengl.GL11;

import dev.colbster937.eaglercraft.LevelUtils;

public final class GuiGameOver extends GuiScreen {
	public final void initGui() {
		this.controlList.clear();
		this.controlList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 72, "Generate new level..."));
		this.controlList.add(new GuiButton(2, this.width / 2 - 100, this.height / 4 + 96, "Load level.."));
		if(this.mc.session == null) {
			((GuiButton)this.controlList.get(1)).enabled = false;
		}

	}

	protected final void keyTyped(char var1, int var2) {
	}

	protected final void actionPerformed(GuiButton var1) {
		if(var1.id == 0) {
			this.mc.displayGuiScreen(new GuiOptions(this, this.mc.options));
		}

		if(var1.id == 1) {
			this.mc.displayGuiScreen(new GuiNewLevel(this));
		}

		if(this.mc.session != null && var1.id == 2) {
			LevelUtils.load();
		}

	}

	public final void drawScreen(int var1, int var2, float var3) {
		drawGradientRect(0, 0, this.width, this.height, 1615855616, -1602211792);
		GL11.glPushMatrix();
		GL11.glScalef(2.0F, 2.0F, 2.0F);
		drawCenteredString(this.fontRenderer, "Game over!", this.width / 2 / 2, 30, 16777215);
		GL11.glPopMatrix();
		FontRenderer var10000 = this.fontRenderer;
		StringBuilder var10001 = (new StringBuilder()).append("Score: &e");
		EntityPlayerSP var4 = this.mc.thePlayer;
		drawCenteredString(var10000, var10001.append(var4.getScore).toString(), this.width / 2, 100, 16777215);
		super.drawScreen(var1, var2, var3);
	}

	public final boolean doesGuiPauseGame() {
		return false;
	}

	public void updateScreen() {
		LevelUtils.tick();
	}
}
