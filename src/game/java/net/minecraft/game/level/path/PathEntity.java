package net.minecraft.game.level.path;

import net.minecraft.game.entity.Entity;
import net.minecraft.game.physics.Vec3D;

public final class PathEntity {
	private final PathPoint[] points;
	private int pathIndex;

	public PathEntity(PathPoint[] var1) {
		this.points = var1;
	}

	public final void incrementPathIndex() {
		++this.pathIndex;
	}

	public final boolean isFinished() {
		return this.pathIndex >= this.points.length;
	}

	public final Vec3D getPosition(Entity var1) {
		float var2 = (float)this.points[this.pathIndex].xCoord + (float)((int)(var1.width + 1.0F)) * 0.5F;
		float var3 = (float)this.points[this.pathIndex].yCoord;
		float var4 = (float)this.points[this.pathIndex].zCoord + (float)((int)(var1.width + 1.0F)) * 0.5F;
		return new Vec3D(var2, var3, var4);
	}
}
