package net.minecraft.client;

import net.lax1dude.eaglercraft.EagRuntime;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiNewLevel;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.render.Tessellator;
import org.lwjgl.opengl.GL11;

import dev.colbster937.eaglercraft.EaglercraftVersion;
import dev.colbster937.eaglercraft.LevelUtils;
import util.MathHelper;

public final class GuiMainMenu extends GuiScreen {
	private float updateCounter = 0.0F;
	private String[] splashes = new String[]{"Pre-beta!", "As seen on TV!", "Awesome!", "100% pure!", "May contain nuts!", "Better than Prey!", "More polygons!", "Sexy!", "Limited edition!", "Flashing letters!", "Made by Notch!", "Coming soon!", "Best in class!", "When it\'s finished!", "Absolutely dragon free!", "Excitement!", "More than 5000 sold!", "One of a kind!", "700+ hits on YouTube!", "Indev!", "Spiders everywhere!", "Check it out!", "Holy cow, man!", "It\'s a game!", "Made in Sweden!", "Uses LWJGL!", "Reticulating splines!", "Minecraft!", "Yaaay!", "Alpha version!", "Singleplayer!", "Keyboard compatible!", "Undocumented!", "Ingots!", "Exploding creepers!", "That\'s not a moon!", "l33t!", "Create!", "Survive!", "Dungeon!", "Exclusive!", "The bee\'s knees!", "Down with O.P.P.!", "Closed source!", "Classy!", "Wow!", "Not on steam!", "9.95 euro!", "Half price!", "Oh man!", "Check it out!", "Awesome community!", "Pixels!", "Teetsuuuuoooo!", "Kaaneeeedaaaa!", "Now with difficulty!", "Enhanced!", "90% bug free!", "Pretty!", "12 herbs and spices!", "Fat free!", "Absolutely no memes!", "Free dental!", "Ask your doctor!", "Minors welcome!", "Cloud computing!", "Legal in Finland!", "Hard to label!", "Technically good!", "Bringing home the bacon!", "Indie!", "GOTY!", "Ceci n\'est pas une title screen!", "Euclidian!", "Now in 3D!", "Inspirational!", "Herregud!", "Complex cellular automata!", "Yes, sir!", "Played by cowboys!", "OpenGL 1.1!", "Thousands of colors!", "Try it!", "Age of Wonders is better!", "Try the mushroom stew!", "Sensational!", "Hot tamale, hot hot tamale!", "Play him off, keyboard cat!", "Guaranteed!", "Macroscopic!", "Bring it on!", "Random splash!", "Call your mother!", "Monster infighting!", "Loved by millions!", "Ultimate edition!", "Freaky!", "You\'ve got a brand new key!", "Water proof!", "Uninflammable!", "Whoa, dude!", "All inclusive!", "Tell your friends!", "NP is not in P!", "Notch <3 Ez!", "Music by C418!"};
	private String currentSplash = this.splashes[(int)(Math.random() * (double)this.splashes.length)];

	public final void updateScreen() {
		this.updateCounter += 0.01F;
		LevelUtils.tick();
	}

	protected final void keyTyped(char var1, int var2) {
	}

	public final void initGui() {
		this.controlList.clear();
		GuiButton b;
		this.controlList.add(b = new GuiButton(1, this.width / 2 - 100, this.height / 4 + 48, "Generate new level..."));
		this.controlList.add(b = new GuiButton(2, this.width / 2 - 100, this.height / 4 + 72, "Load level.."));
		b.enabled = LevelUtils.savedLevel();
		this.controlList.add(b = new GuiButton(3, this.width / 2 - 100, this.height / 4 + 96, "Import level.."));
		// b.enabled = false;
		this.controlList.add(b = new GuiButton(0, this.width / 2 - 100, this.height / 4 + 120, 98, 20, "Options..."));
		this.controlList.add(b = new GuiButton(4, this.width / 2 + 2, this.height / 4 + 120, 98, 20, "Github"));
		/* ((GuiButton)this.controlList.get(2)).enabled = false;
		if(this.mc.session == null) {
			((GuiButton)this.controlList.get(1)).enabled = false;
		} */

	}

	protected final void actionPerformed(GuiButton var1) {
		if(var1.id == 0) {
			this.mc.displayGuiScreen(new GuiOptions(this, this.mc.options));
		}

		if(var1.id == 1) {
			this.mc.displayGuiScreen(new GuiNewLevel(this));
		}

		if(this.mc.session != null && var1.id == 2) {
			LevelUtils.load(false);
		}

		if(this.mc.session != null && var1.id == 3) {
			LevelUtils.load(true);
		}

		if(var1.id == 4) {
			EagRuntime.openLink(EaglercraftVersion.PROJECT_GITHUB);
		}

	}

	public final void drawScreen(int var1, int var2, float var3) {
		this.drawDefaultBackground();
		Tessellator var4 = Tessellator.instance;
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/gui/logo.png"));
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		var4.setColorOpaque_I(16777215);
		this.drawTexturedModalRect((this.width - 256) / 2, 30, 0, 0, 256, 49);
		GL11.glPushMatrix();
		GL11.glTranslatef((float)(this.width / 2 + 90), 70.0F, 0.0F);
		GL11.glRotatef(-20.0F, 0.0F, 0.0F, 1.0F);
		float var15 = 1.8F - MathHelper.abs(MathHelper.sin((float)(System.currentTimeMillis() % 1000L) / 1000.0F * (float)Math.PI * 2.0F) * 0.1F);
		var15 = var15 * 100.0F / (float)(this.fontRenderer.getStringWidth(this.currentSplash) + 32);
		GL11.glScalef(var15, var15, var15);
		drawCenteredString(this.fontRenderer, this.currentSplash, 0, -8, 16776960);
		GL11.glPopMatrix();
		String var16 = "Copyright Mojang Specifications. Do not distribute.";
		drawString(this.fontRenderer, var16, this.width - this.fontRenderer.getStringWidth(var16) - 2, this.height - 10, 16777215);
		long var7 = EagRuntime.maxMemory();
		long var9 = EagRuntime.totalMemory();
		long var11 = EagRuntime.freeMemory();
		long var13 = var7 - var11;
		var16 = "Free memory: " + var13 * 100L / var7 + "% of " + var7 / 1024L / 1024L + "MB";
		drawString(this.fontRenderer, var16, this.width - this.fontRenderer.getStringWidth(var16) - 2, 2, 8421504);
		var16 = "Allocated memory: " + var9 * 100L / var7 + "% (" + var9 / 1024L / 1024L + "MB)";
		drawString(this.fontRenderer, var16, this.width - this.fontRenderer.getStringWidth(var16) - 2, 12, 8421504);
		String[] ts = EaglercraftVersion.getTitleString();
		drawString(this.fontRenderer, ts[0], 2, this.height - 10, 16777215);
		drawString(this.fontRenderer, ts[1], 2, this.height - 20, 16777215);
		// drawString(this.fontRenderer, ts[1], 2, 2, 8421504);
		super.drawScreen(var1, var2, var3);
	}
}
