package net.minecraft.client.render.camera;

import net.minecraft.game.physics.AxisAlignedBB;

public interface ICamera {
	boolean isBoundingBoxInFrustrum(AxisAlignedBB var1);
}
