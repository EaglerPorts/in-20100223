package net.minecraft.client.gui;

import java.util.ArrayList;
import java.util.List;

import net.lax1dude.eaglercraft.Display;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.Tessellator;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class GuiScreen extends Gui {
	protected Minecraft mc;
	public int width;
	public int height;
	protected List controlList = new ArrayList();
	public boolean allowUserInput = false;
	protected FontRenderer fontRenderer;

	public void drawScreen(int var1, int var2, float var3) {
		for(int var5 = 0; var5 < this.controlList.size(); ++var5) {
			GuiButton var4 = (GuiButton)this.controlList.get(var5);
			var4.drawButton(this.mc, var1, var2);
		}

	}

	protected void keyTyped(char var1, int var2) {
		if(var2 == 1 || var2 == Keyboard.KEY_GRAVE) {
			this.mc.displayGuiScreen((GuiScreen)null);
			this.mc.setIngameFocus();
		}

	}

	protected void drawSlotInventory(int var1, int var2, int var3) {
		if(var3 == 0) {
			for(var3 = 0; var3 < this.controlList.size(); ++var3) {
				GuiButton var4 = (GuiButton)this.controlList.get(var3);
				if(var4.mousePressed(var1, var2)) {
					this.mc.sndManager.playSoundFX("random.click", 1.0F, 1.0F);
					this.actionPerformed(var4);
				}
			}
		}

	}

	protected void actionPerformed(GuiButton var1) {
	}

	public final void setWorldAndResolution(Minecraft var1, int var2, int var3) {
		this.mc = var1;
		this.fontRenderer = var1.fontRenderer;
		this.width = var2;
		this.height = var3;
		this.initGui();
	}

	public void initGui() {
	}

	public final void handleMouseInput() {
		if(Mouse.getEventButtonState()) {
			int var1 = Mouse.getEventX() * this.width / this.mc.displayWidth;
			int var2 = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
			this.drawSlotInventory(var1, var2, Mouse.getEventButton());
		} else {
			Mouse.getEventX();
			Mouse.getEventY();
			Mouse.getEventButton();
		}
	}

	public final void handleKeyboardInput() {
		if(Keyboard.getEventKeyState()) {
			if(Keyboard.getEventKey() == Keyboard.KEY_F11) {
				Display.toggleFullscreen();
				return;
			}

			this.keyTyped(Keyboard.getEventCharacter(), Keyboard.getEventKey());
		}

	}

	public void updateScreen() {
	}

	public void onGuiClosed() {
	}

	public final void drawDefaultBackground() {
		boolean var1 = false;
		if(this.mc.theWorld != null) {
			drawGradientRect(0, 0, this.width, this.height, 1610941696, -1607454624);
		} else {
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glDisable(GL11.GL_FOG);
			Tessellator var2 = Tessellator.instance;
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/dirt.png"));
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			var2.startDrawingQuads();
			var2.setColorOpaque_I(4210752);
			var2.addVertexWithUV(0.0F, (float)this.height, 0.0F, 0.0F, (float)this.height / 32.0F);
			var2.addVertexWithUV((float)this.width, (float)this.height, 0.0F, (float)this.width / 32.0F, (float)this.height / 32.0F);
			var2.addVertexWithUV((float)this.width, 0.0F, 0.0F, (float)this.width / 32.0F, 0.0F);
			var2.addVertexWithUV(0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
			var2.draw();
		}
	}

	public boolean doesGuiPauseGame() {
		return true;
	}
}
