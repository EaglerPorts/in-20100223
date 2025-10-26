package net.minecraft.client.effect;

import net.minecraft.game.level.World;

public final class EntitySplashFX extends EntityRainFX {
	public EntitySplashFX(World var1, float var2, float var3, float var4) {
		super(var1, var2, var3, var4);
		this.particleGravity = 0.04F;
		++this.particleTextureIndex;
	}
}
