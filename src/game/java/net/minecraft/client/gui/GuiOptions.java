package net.minecraft.client.gui;

import net.minecraft.client.GameSettings;

public final class GuiOptions extends GuiScreen {
	private GuiScreen parentScreen;
	private String screenTitle = "Options";
	private GameSettings options;

	public GuiOptions(GuiScreen var1, GameSettings var2) {
		this.parentScreen = var1;
		this.options = var2;
	}

	public final void initGui() {
		for(int var1 = 0; var1 < this.options.numberOfOptions; ++var1) {
			GuiButton b = new GuiSmallButton(var1, this.width / 2 - 155 + var1 % 2 * 160, this.height / 6 + 24 * (var1 >> 1), this.options.setOptionString(var1));
			if (var1 == 0) b.enabled = false;
			this.controlList.add(b);
		}

		this.controlList.add(new GuiButton(100, this.width / 2 - 100, this.height / 6 + 120 + 12, "Controls..."));
		this.controlList.add(new GuiButton(200, this.width / 2 - 100, this.height / 6 + 168, "Done"));
	}

	protected final void actionPerformed(GuiButton var1) {
		if(var1.enabled) {
			if(var1.id < 100) {
				this.options.setOptionValue(var1.id, 1);
				var1.displayString = this.options.setOptionString(var1.id);
			}

			if(var1.id == 100) {
				this.mc.displayGuiScreen(new GuiControls(this, this.options));
			}

			if(var1.id == 200) {
				this.mc.displayGuiScreen(this.parentScreen);
			}

		}
	}

	public final void drawScreen(int var1, int var2, float var3) {
		this.drawDefaultBackground();
		drawCenteredString(this.fontRenderer, this.screenTitle, this.width / 2, 20, 16777215);
		super.drawScreen(var1, var2, var3);
	}
}
