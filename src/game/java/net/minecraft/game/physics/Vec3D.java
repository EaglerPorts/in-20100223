package net.minecraft.game.physics;

import util.MathHelper;

public final class Vec3D {
	public float xCoord;
	public float yCoord;
	public float zCoord;

	public Vec3D(float var1, float var2, float var3) {
		this.xCoord = var1;
		this.yCoord = var2;
		this.zCoord = var3;
	}

	public final Vec3D subtract(Vec3D var1) {
		return new Vec3D(this.xCoord - var1.xCoord, this.yCoord - var1.yCoord, this.zCoord - var1.zCoord);
	}

	public final Vec3D normalize() {
		float var1 = MathHelper.sqrt_float(this.xCoord * this.xCoord + this.yCoord * this.yCoord + this.zCoord * this.zCoord);
		return new Vec3D(this.xCoord / var1, this.yCoord / var1, this.zCoord / var1);
	}

	public final Vec3D addVector(float var1, float var2, float var3) {
		return new Vec3D(this.xCoord + var1, this.yCoord + var2, this.zCoord + var3);
	}

	public final float distance(Vec3D var1) {
		float var2 = var1.xCoord - this.xCoord;
		float var3 = var1.yCoord - this.yCoord;
		float var4 = var1.zCoord - this.zCoord;
		return MathHelper.sqrt_float(var2 * var2 + var3 * var3 + var4 * var4);
	}

	public final float squareDistanceTo(Vec3D var1) {
		float var2 = var1.xCoord - this.xCoord;
		float var3 = var1.yCoord - this.yCoord;
		float var4 = var1.zCoord - this.zCoord;
		return var2 * var2 + var3 * var3 + var4 * var4;
	}

	public final Vec3D getIntermediateWithXValue(Vec3D var1, float var2) {
		float var3 = var1.xCoord - this.xCoord;
		float var4 = var1.yCoord - this.yCoord;
		float var5 = var1.zCoord - this.zCoord;
		if(var3 * var3 < 1.0E-7F) {
			return null;
		} else {
			var2 = (var2 - this.xCoord) / var3;
			return var2 >= 0.0F && var2 <= 1.0F ? new Vec3D(this.xCoord + var3 * var2, this.yCoord + var4 * var2, this.zCoord + var5 * var2) : null;
		}
	}

	public final Vec3D getIntermediateWithYValue(Vec3D var1, float var2) {
		float var3 = var1.xCoord - this.xCoord;
		float var4 = var1.yCoord - this.yCoord;
		float var5 = var1.zCoord - this.zCoord;
		if(var4 * var4 < 1.0E-7F) {
			return null;
		} else {
			var2 = (var2 - this.yCoord) / var4;
			return var2 >= 0.0F && var2 <= 1.0F ? new Vec3D(this.xCoord + var3 * var2, this.yCoord + var4 * var2, this.zCoord + var5 * var2) : null;
		}
	}

	public final Vec3D getIntermediateWithZValue(Vec3D var1, float var2) {
		float var3 = var1.xCoord - this.xCoord;
		float var4 = var1.yCoord - this.yCoord;
		float var5 = var1.zCoord - this.zCoord;
		if(var5 * var5 < 1.0E-7F) {
			return null;
		} else {
			var2 = (var2 - this.zCoord) / var5;
			return var2 >= 0.0F && var2 <= 1.0F ? new Vec3D(this.xCoord + var3 * var2, this.yCoord + var4 * var2, this.zCoord + var5 * var2) : null;
		}
	}

	public final String toString() {
		return "(" + this.xCoord + ", " + this.yCoord + ", " + this.zCoord + ")";
	}
}
