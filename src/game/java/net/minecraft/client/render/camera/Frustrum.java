package net.minecraft.client.render.camera;

import net.minecraft.game.physics.AxisAlignedBB;

public final class Frustrum implements ICamera {
	private ClippingHelper clippingHelper = ClippingHelperImplementation.init();

	public final boolean isBoundingBoxInFrustrum(AxisAlignedBB var1) {
		ClippingHelper var9 = this.clippingHelper;
		float var7 = var1.maxZ;
		float var6 = var1.maxY;
		float var5 = var1.maxX;
		float var4 = var1.minZ;
		float var3 = var1.minY;
		float var10 = var1.minX;
		var9 = var9;

		for(int var8 = 0; var8 < 6; ++var8) {
			if(var9.frustrum[var8][0] * var10 + var9.frustrum[var8][1] * var3 + var9.frustrum[var8][2] * var4 + var9.frustrum[var8][3] <= 0.0F && var9.frustrum[var8][0] * var5 + var9.frustrum[var8][1] * var3 + var9.frustrum[var8][2] * var4 + var9.frustrum[var8][3] <= 0.0F && var9.frustrum[var8][0] * var10 + var9.frustrum[var8][1] * var6 + var9.frustrum[var8][2] * var4 + var9.frustrum[var8][3] <= 0.0F && var9.frustrum[var8][0] * var5 + var9.frustrum[var8][1] * var6 + var9.frustrum[var8][2] * var4 + var9.frustrum[var8][3] <= 0.0F && var9.frustrum[var8][0] * var10 + var9.frustrum[var8][1] * var3 + var9.frustrum[var8][2] * var7 + var9.frustrum[var8][3] <= 0.0F && var9.frustrum[var8][0] * var5 + var9.frustrum[var8][1] * var3 + var9.frustrum[var8][2] * var7 + var9.frustrum[var8][3] <= 0.0F && var9.frustrum[var8][0] * var10 + var9.frustrum[var8][1] * var6 + var9.frustrum[var8][2] * var7 + var9.frustrum[var8][3] <= 0.0F && var9.frustrum[var8][0] * var5 + var9.frustrum[var8][1] * var6 + var9.frustrum[var8][2] * var7 + var9.frustrum[var8][3] <= 0.0F) {
				return false;
			}
		}

		return true;
	}
}
