package net.minecraft.client.gui;

import org.lwjgl.input.Keyboard;

public final class GuiNameLevel extends GuiScreen {
	private GuiScreen parent;
	private String title = "Enter level name:";
	private int id;
	private String name;
	private int counter = 0;

	public GuiNameLevel(GuiScreen var1, String var2, int var3) {
		this.parent = var1;
		this.id = var3;
		this.name = var2;
		if(this.name.equals("-")) {
			this.name = "";
		}

	}

	public final void initGui() {
		this.controlList.clear();
		Keyboard.enableRepeatEvents(true);
		this.controlList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 120, "Save"));
		this.controlList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 144, "Cancel"));
		((GuiButton)this.controlList.get(0)).enabled = this.name.trim().length() > 1;
	}

	public final void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
	}

	public final void updateScreen() {
		++this.counter;
	}

	protected final void actionPerformed(GuiButton var1) {
		if(var1.enabled) {
			if(var1.id == 0 && this.name.trim().length() > 1) {
				this.name.trim();
				this.mc.displayGuiScreen((GuiScreen)null);
				this.mc.setIngameFocus();
			}

			if(var1.id == 1) {
				this.mc.displayGuiScreen(this.parent);
			}

		}
	}

	protected final void keyTyped(char var1, int var2) {
		if(var2 == 14 && this.name.length() > 0) {
			this.name = this.name.substring(0, this.name.length() - 1);
		}

		if("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789 ,.:-_\'*!\"#%/()=+?[]{}<>".indexOf(var1) >= 0 && this.name.length() < 64) {
			this.name = this.name + var1;
		}

		((GuiButton)this.controlList.get(0)).enabled = this.name.trim().length() > 1;
	}

	public final void drawScreen(int var1, int var2, float var3) {
		this.drawDefaultBackground();
		drawCenteredString(this.fontRenderer, this.title, this.width / 2, 40, 16777215);
		int var4 = this.width / 2 - 100;
		int var5 = this.height / 2 - 10;
		drawRect(var4 - 1, var5 - 1, var4 + 200 + 1, var5 + 20 + 1, -6250336);
		drawRect(var4, var5, var4 + 200, var5 + 20, -16777216);
		drawString(this.fontRenderer, this.name + (this.counter / 6 % 2 == 0 ? "_" : ""), var4 + 4, var5 + 6, 14737632);
		super.drawScreen(var1, var2, var3);
	}
}
