package net.minecraft.client.gui;

public final class GuiErrorScreen extends GuiScreen {
	private String title;
	private String text;

	public GuiErrorScreen(String var1, String var2) {
		this.title = var1;
		this.text = var2;
	}

	public final void initGui() {
	}

	public final void drawScreen(int var1, int var2, float var3) {
		drawGradientRect(0, 0, this.width, this.height, -12574688, -11530224);
		drawCenteredString(this.fontRenderer, this.title, this.width / 2, 90, 16777215);
		drawCenteredString(this.fontRenderer, this.text, this.width / 2, 110, 16777215);
		super.drawScreen(var1, var2, var3);
	}

	protected final void keyTyped(char var1, int var2) {
	}
}
