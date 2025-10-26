package net.minecraft.game.level.block;

public class StepSound {
	private String sound;
	public final float soundVolume;
	public final float soundPitch;

	public StepSound(String var1, float var2, float var3) {
		this.sound = var1;
		this.soundVolume = var2;
		this.soundPitch = var3;
	}

	public String stepSoundDir() {
		return "step." + this.sound;
	}

	public final String stepSoundDir2() {
		return "step." + this.sound;
	}
}
