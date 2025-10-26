package net.minecraft.client.gui;

public final class GuiNewLevel extends GuiScreen {
	private GuiScreen prevGui;
	private String[] worldType = new String[]{"Inland", "Island", "Floating", "Flat"};
	private String[] worldShape = new String[]{"Square", "Long", "Deep"};
	private String[] worldSize = new String[]{"Small", "Normal", "Huge"};
	private String[] worldTheme = new String[]{"Normal", "Hell", "Paradise", "Woods"};
	private int selectedWorldType = 1;
	private int selectedWorldShape = 0;
	private int selectedWorldSize = 1;
	private int selectedWorldTheme = 0;

	public GuiNewLevel(GuiScreen var1) {
		this.prevGui = var1;
	}

	public final void initGui() {
		this.controlList.clear();
		this.controlList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4, "Type: "));
		this.controlList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 24, "Shape:"));
		this.controlList.add(new GuiButton(2, this.width / 2 - 100, this.height / 4 + 48, "Size: "));
		this.controlList.add(new GuiButton(3, this.width / 2 - 100, this.height / 4 + 72, "Theme: "));
		this.controlList.add(new GuiButton(4, this.width / 2 - 100, this.height / 4 + 96 + 12, "Create"));
		this.controlList.add(new GuiButton(5, this.width / 2 - 100, this.height / 4 + 120 + 12, "Cancel"));
		this.worldOptions();
	}

	private void worldOptions() {
		((GuiButton)this.controlList.get(0)).displayString = "Type: " + this.worldType[this.selectedWorldType];
		((GuiButton)this.controlList.get(1)).displayString = "Shape: " + this.worldShape[this.selectedWorldShape];
		((GuiButton)this.controlList.get(2)).displayString = "Size: " + this.worldSize[this.selectedWorldSize];
		((GuiButton)this.controlList.get(3)).displayString = "Theme: " + this.worldTheme[this.selectedWorldTheme];
	}

	protected final void actionPerformed(GuiButton var1) {
		if(var1.id == 5) {
			this.mc.displayGuiScreen(this.prevGui);
		} else if(var1.id == 4) {
			this.mc.generateLevel(this.selectedWorldSize, this.selectedWorldShape, this.selectedWorldType, this.selectedWorldTheme);
			this.mc.displayGuiScreen((GuiScreen)null);
		} else if(var1.id == 0) {
			this.selectedWorldType = (this.selectedWorldType + 1) % this.worldType.length;
		} else if(var1.id == 1) {
			this.selectedWorldShape = (this.selectedWorldShape + 1) % this.worldShape.length;
		} else if(var1.id == 2) {
			this.selectedWorldSize = (this.selectedWorldSize + 1) % this.worldSize.length;
		} else if(var1.id == 3) {
			this.selectedWorldTheme = (this.selectedWorldTheme + 1) % this.worldTheme.length;
		}

		this.worldOptions();
	}

	public final void drawScreen(int var1, int var2, float var3) {
		this.drawDefaultBackground();
		drawCenteredString(this.fontRenderer, "Generate new level", this.width / 2, 40, 16777215);
		super.drawScreen(var1, var2, var3);
	}
}
