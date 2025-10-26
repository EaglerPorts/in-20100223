package net.minecraft.client.render.camera;

import net.minecraft.game.physics.AxisAlignedBB;

public final class IsomCamera implements ICamera {
	public final boolean isBoundingBoxInFrustrum(AxisAlignedBB var1) {
		return true;
	}
}
