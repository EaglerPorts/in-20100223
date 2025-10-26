package net.minecraft.client.sound;

import java.util.HashMap;
import java.util.Map;

import net.lax1dude.eaglercraft.EagRuntime;
import net.lax1dude.eaglercraft.EaglerInputStream;
import net.lax1dude.eaglercraft.Random;
import net.lax1dude.eaglercraft.internal.EnumPlatformType;
import net.lax1dude.eaglercraft.internal.IAudioCacheLoader;
import net.lax1dude.eaglercraft.internal.IAudioHandle;
import net.lax1dude.eaglercraft.internal.IAudioResource;
import net.lax1dude.eaglercraft.internal.PlatformAudio;
import net.minecraft.client.GameSettings;
import net.minecraft.game.entity.EntityLiving;
import net.peyton.eagler.minecraft.AudioUtils;

public class SoundManager {
	private GameSettings options;
	private Random rand = new Random();
	private int field_583_i = this.rand.nextInt(12000);
	
	private Map<String, IAudioResource> sounds = new HashMap<String, IAudioResource>();
	private Map<String, IAudioResource> music = new HashMap<String, IAudioResource>();
	
	private IAudioHandle musicHandle;
	
	private String[] newMusic = new String[]{"hal1.ogg", "hal2.ogg", "hal3.ogg", "hal4.ogg", "nuance1.ogg", "nuance2.ogg", "piano1.ogg", "piano2.ogg", "piano3.ogg"};

	public void loadSoundSettings(GameSettings var1) {
		this.options = var1;
	}

	public void onSoundOptionsChanged() {
		if(this.options.music) {
			if(this.musicHandle != null && !this.musicHandle.shouldFree()) {
				musicHandle.end();
			}
		} else {
			if(this.musicHandle != null && !this.musicHandle.shouldFree()) {
				musicHandle.gain(1.0F);
			}
		}

	}

	public void closeMinecraft() {
	}

	public void playRandomMusicIfReady() {
		if(this.options.music) {
			if(this.musicHandle == null || this.musicHandle.shouldFree()) {
				if(this.field_583_i > 0) {
					--this.field_583_i;
					return;
				}

				int var1 = rand.nextInt(newMusic.length);
				this.field_583_i = this.rand.nextInt(12000) + 12000;
				String name = "/music/" + newMusic[var1];
				
				IAudioResource trk = this.music.get(name);
				if (trk == null) {
					if (EagRuntime.getPlatformType() != EnumPlatformType.DESKTOP) {
						trk = PlatformAudio.loadAudioDataNew(name, false, browserResourceLoader);
					} else {
						trk = PlatformAudio.loadAudioData(name, false);
					}
					if (trk != null) {
						music.put(name, trk);
					}
				}
				
				if (trk != null) {
					musicHandle = PlatformAudio.beginPlaybackStatic(trk, 1.0f, 1.0f, false);
				}
			}
		}
	}

	public void setListener(EntityLiving var1, float var2) {
		if (var1 != null && this.options.sound) {
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

	public void playSound(String var1, float var2, float var3, float var4, float var5, float var6) {
		if(this.options.sound) {
			if(var5 > 0.0F) {
				IAudioResource trk;
				if(var1 == null) return;
				
				String sound = AudioUtils.getSound(var1);
				if(sound == null) {
					return;
				}
				trk = this.sounds.get(sound);
				if (trk == null) {
					if (EagRuntime.getPlatformType() != EnumPlatformType.DESKTOP) {
						trk = PlatformAudio.loadAudioDataNew(sound, true, browserResourceLoader);
					} else {
						trk = PlatformAudio.loadAudioData(sound, true);
					}
					if (trk != null) {
						sounds.put(sound, trk);
					}
				}
				
				if(trk != null) {
					PlatformAudio.beginPlayback(trk, var2, var3, var4, var5 * 1.0f, var6, false);
				}
			}
		}
	}

	public void playSoundFX(String var1, float var2, float var3) {
		if(this.options.sound) {
			
			if(var2 > 1.0F) {
				var2 = 1.0F;
			}
			var2 *= 0.25F;
			
			IAudioResource trk;
			if(var1 == null) return;
			
			String sound = AudioUtils.getSound(var1);
			if(sound == null) {
				return;
			}
			trk = this.sounds.get(sound);
			if (trk == null) {
				if (EagRuntime.getPlatformType() != EnumPlatformType.DESKTOP) {
					trk = PlatformAudio.loadAudioDataNew(sound, true, browserResourceLoader);
				} else {
					trk = PlatformAudio.loadAudioData(sound, true);
				}
				if (trk != null) {
					sounds.put(sound, trk);
				}
			}
			
			if(trk != null) {
				PlatformAudio.beginPlaybackStatic(trk, var2 * 1.0f, var3, false);
			}
		}
	}
	
	private final IAudioCacheLoader browserResourceLoader = filename -> {
		try {
			return EaglerInputStream.inputStreamToBytesQuiet(EagRuntime.getRequiredResourceStream(filename));
		} catch (Throwable t) {
			return null;
		}
	};
}