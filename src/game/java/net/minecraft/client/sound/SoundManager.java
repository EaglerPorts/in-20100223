package net.minecraft.client.sound;

import net.lax1dude.eaglercraft.EagRuntime;
import net.lax1dude.eaglercraft.EaglerInputStream;
import net.lax1dude.eaglercraft.internal.IAudioCacheLoader;
import net.lax1dude.eaglercraft.internal.IAudioHandle;
import net.lax1dude.eaglercraft.internal.IAudioResource;
import net.lax1dude.eaglercraft.internal.PlatformAudio;
import net.lax1dude.eaglercraft.internal.vfs2.VFile2;
import net.minecraft.client.GameSettings;
import net.minecraft.game.entity.EntityLiving;
import util.MathHelper;

public final class SoundManager {
	private SoundPool soundPoolSounds = new SoundPool();
	private SoundPool soundPoolMusic = new SoundPool();
	private int latestSoundID = 0;
	private GameSettings options;
	private boolean loaded = false;

	private IAudioHandle musicHandle;

	public final void loadSoundSettings(GameSettings var1) {
		this.options = var1;
		if(!this.loaded && (var1.sound || var1.music)) {
			this.tryToSetLibraryAndCodecs();
		}
	}

	private void tryToSetLibraryAndCodecs() {
		boolean var1 = this.options.sound;
		boolean var2 = this.options.music;
		this.options.sound = false;
		this.options.music = false;
		this.options.saveOptions();
		this.options.sound = var1;
		this.options.music = var2;
		this.options.saveOptions();
		this.loaded = true;
	}

	public final void onSoundOptionsChanged() {
		if(!this.loaded && (this.options.sound || this.options.music)) {
			this.tryToSetLibraryAndCodecs();
		}
		if (this.musicHandle != null && !this.musicHandle.shouldFree()) {
			if(!this.options.music) {
				musicHandle.end();
			} else {
				musicHandle.gain(1.0F);
			}
		}
	}

	public final void closeMinecraft() {
		if(musicHandle != null && !musicHandle.shouldFree()) {
			musicHandle.end();
		}
		musicHandle = null;
	}

	public final void addSound(String var1, VFile2 var2) {
		this.soundPoolSounds.addSound(var1, var2);
	}

	public final void addMusic(String var1, VFile2 var2) {
		this.soundPoolMusic.addSound(var1, var2);
	}

	public final void playRandomMusicIfReady(float var1, float var2, float var3) {
		if(this.loaded && this.options.music) {
			if(this.musicHandle == null || this.musicHandle.shouldFree()) {
				SoundPoolEntry var4 = this.soundPoolMusic.getRandomSoundFromSoundPool("calm");
				if (var4 != null) {
					IAudioResource trk = PlatformAudio.loadAudioDataNew("/music/" + var4.soundName, false, browserResourceLoader);
					if (trk != null) {
						this.musicHandle = PlatformAudio.beginPlaybackStatic(trk, 1.0F, 1.0F, false);
					}
				}
			}
		}
	}

	public final void setListener(EntityLiving var1, float var2) {
		if(this.loaded && this.options.sound && var1 != null) {
			try {
				float var9 = var1.prevRotationPitch + (var1.rotationPitch - var1.prevRotationPitch) * var2;
				float var3 = var1.prevRotationYaw + (var1.rotationYaw - var1.prevRotationYaw) * var2;
				double var4 = var1.prevPosX + (var1.posX - var1.prevPosX) * (double) var2;
				double var6 = var1.prevPosY + (var1.posY - var1.prevPosY) * (double) var2;
				double var8 = var1.prevPosZ + (var1.posZ - var1.prevPosZ) * (double) var2;
				PlatformAudio.setListener((float) var4, (float) var6, (float) var8, (float) var9, (float) var3);
			} catch (Throwable t) {
				// eaglercraft 1.5.2 had Infinity/NaN crashes for this function which
				// couldn't be resolved via if statement checks in the above variables
			}
		}
	}

	public final void playSound(String var1, float var2, float var3, float var4, float var5, float var6) {
		if(this.loaded && this.options.sound) {
			SoundPoolEntry var9 = this.soundPoolSounds.getRandomSoundFromSoundPool(var1);
			if(var9 != null && var5 > 0.0F) {
				this.latestSoundID = (this.latestSoundID + 1) % 256;
				float gain = MathHelper.clamp_float(var5, 0.0F, 1.0F);
				IAudioResource sfx = PlatformAudio.loadAudioDataNew("/sounds/" + var9.soundName, false, browserResourceLoader);
				if(sfx != null) {
					PlatformAudio.beginPlaybackStatic(sfx, gain, var6, false);
				}
			}
		}
	}

	public final void playSoundFX(String var1, float var2, float var3) {
		if(this.loaded && this.options.sound) {
			SoundPoolEntry var4 = this.soundPoolSounds.getRandomSoundFromSoundPool(var1);
			if(var4 != null) {
				this.latestSoundID = (this.latestSoundID + 1) % 256;
				IAudioResource sfx = PlatformAudio.loadAudioDataNew("/sounds/" + var4.soundName, false, browserResourceLoader);
				if(sfx != null) {
					PlatformAudio.beginPlaybackStatic(sfx, 0.25F, var3, false);
				}
			}
		}
	}

	private final IAudioCacheLoader browserResourceLoader = filename -> {
		try {
			return EaglerInputStream.inputStreamToBytesQuiet(EagRuntime.getResourceStream(filename));
		} catch (Throwable t) {
			return null;
		}
	};
}
