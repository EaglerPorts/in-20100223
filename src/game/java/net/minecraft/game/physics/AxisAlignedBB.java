package net.minecraft.game.physics;

public final class AxisAlignedBB {
	private float epsilon = 0.0F;
	public float minX;
	public float minY;
	public float minZ;
	public float maxX;
	public float maxY;
	public float maxZ;

	public AxisAlignedBB(float var1, float var2, float var3, float var4, float var5, float var6) {
		this.minX = var1;
		this.minY = var2;
		this.minZ = var3;
		this.maxX = var4;
		this.maxY = var5;
		this.maxZ = var6;
	}

	public final AxisAlignedBB addCoord(float var1, float var2, float var3) {
		float var4 = this.minX;
		float var5 = this.minY;
		float var6 = this.minZ;
		float var7 = this.maxX;
		float var8 = this.maxY;
		float var9 = this.maxZ;
		if(var1 < 0.0F) {
			var4 += var1;
		}

		if(var1 > 0.0F) {
			var7 += var1;
		}

		if(var2 < 0.0F) {
			var5 += var2;
		}

		if(var2 > 0.0F) {
			var8 += var2;
		}

		if(var3 < 0.0F) {
			var6 += var3;
		}

		if(var3 > 0.0F) {
			var9 += var3;
		}

		return new AxisAlignedBB(var4, var5, var6, var7, var8, var9);
	}

	public final AxisAlignedBB expand(float var1, float var2, float var3) {
		if(this.minY > this.maxY) {
			throw new IllegalArgumentException("NOOOOOO!");
		} else {
			float var4 = this.minX - var1;
			float var5 = this.minY - var2;
			float var6 = this.minZ - var3;
			var1 += this.maxX;
			var2 += this.maxY;
			var3 += this.maxZ;
			return new AxisAlignedBB(var4, var5, var6, var1, var2, var3);
		}
	}

	public final float calculateXOffset(AxisAlignedBB var1, float var2) {
		if(var1.maxY > this.minY && var1.minY < this.maxY) {
			if(var1.maxZ > this.minZ && var1.minZ < this.maxZ) {
				float var3;
				if(var2 > 0.0F && var1.maxX <= this.minX) {
					var3 = this.minX - var1.maxX;
					if(var3 < var2) {
						var2 = var3;
					}
				}

				if(var2 < 0.0F && var1.minX >= this.maxX) {
					var3 = this.maxX - var1.minX;
					if(var3 > var2) {
						var2 = var3;
					}
				}

				return var2;
			} else {
				return var2;
			}
		} else {
			return var2;
		}
	}

	public final float calculateYOffset(AxisAlignedBB var1, float var2) {
		if(var1.maxX > this.minX && var1.minX < this.maxX) {
			if(var1.maxZ > this.minZ && var1.minZ < this.maxZ) {
				float var3;
				if(var2 > 0.0F && var1.maxY <= this.minY) {
					var3 = this.minY - var1.maxY;
					if(var3 < var2) {
						var2 = var3;
					}
				}

				if(var2 < 0.0F && var1.minY >= this.maxY) {
					var3 = this.maxY - var1.minY;
					if(var3 > var2) {
						var2 = var3;
					}
				}

				return var2;
			} else {
				return var2;
			}
		} else {
			return var2;
		}
	}

	public final float calculateZOffset(AxisAlignedBB var1, float var2) {
		if(var1.maxX > this.minX && var1.minX < this.maxX) {
			if(var1.maxY > this.minY && var1.minY < this.maxY) {
				float var3;
				if(var2 > 0.0F && var1.maxZ <= this.minZ) {
					var3 = this.minZ - var1.maxZ;
					if(var3 < var2) {
						var2 = var3;
					}
				}

				if(var2 < 0.0F && var1.minZ >= this.maxZ) {
					var3 = this.maxZ - var1.minZ;
					if(var3 > var2) {
						var2 = var3;
					}
				}

				return var2;
			} else {
				return var2;
			}
		} else {
			return var2;
		}
	}

	public final boolean intersectsWith(AxisAlignedBB var1) {
		return var1.maxX >= this.minX && var1.minX <= this.maxX ? (var1.maxY >= this.minY && var1.minY <= this.maxY ? var1.maxZ >= this.minZ && var1.minZ <= this.maxZ : false) : false;
	}

	public final void offset(float var1, float var2, float var3) {
		this.minX += var1;
		this.minY += var2;
		this.minZ += var3;
		this.maxX += var1;
		this.maxY += var2;
		this.maxZ += var3;
	}

	public final AxisAlignedBB copy() {
		return new AxisAlignedBB(this.minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ);
	}

	public final MovingObjectPosition calculateIntercept(Vec3D var1, Vec3D var2) {
		Vec3D var3 = var1.getIntermediateWithXValue(var2, this.minX);
		Vec3D var4 = var1.getIntermediateWithXValue(var2, this.maxX);
		Vec3D var5 = var1.getIntermediateWithYValue(var2, this.minY);
		Vec3D var6 = var1.getIntermediateWithYValue(var2, this.maxY);
		Vec3D var7 = var1.getIntermediateWithZValue(var2, this.minZ);
		var2 = var1.getIntermediateWithZValue(var2, this.maxZ);
		if(!this.isVecInYZ(var3)) {
			var3 = null;
		}

		if(!this.isVecInYZ(var4)) {
			var4 = null;
		}

		if(!this.isVecInXZ(var5)) {
			var5 = null;
		}

		if(!this.isVecInXZ(var6)) {
			var6 = null;
		}

		if(!this.isVecInXY(var7)) {
			var7 = null;
		}

		if(!this.isVecInXY(var2)) {
			var2 = null;
		}

		Vec3D var8 = null;
		if(var3 != null) {
			var8 = var3;
		}

		if(var4 != null && (var8 == null || var1.squareDistanceTo(var4) < var1.squareDistanceTo(var8))) {
			var8 = var4;
		}

		if(var5 != null && (var8 == null || var1.squareDistanceTo(var5) < var1.squareDistanceTo(var8))) {
			var8 = var5;
		}

		if(var6 != null && (var8 == null || var1.squareDistanceTo(var6) < var1.squareDistanceTo(var8))) {
			var8 = var6;
		}

		if(var7 != null && (var8 == null || var1.squareDistanceTo(var7) < var1.squareDistanceTo(var8))) {
			var8 = var7;
		}

		if(var2 != null && (var8 == null || var1.squareDistanceTo(var2) < var1.squareDistanceTo(var8))) {
			var8 = var2;
		}

		if(var8 == null) {
			return null;
		} else {
			byte var9 = -1;
			if(var8 == var3) {
				var9 = 4;
			}

			if(var8 == var4) {
				var9 = 5;
			}

			if(var8 == var5) {
				var9 = 0;
			}

			if(var8 == var6) {
				var9 = 1;
			}

			if(var8 == var7) {
				var9 = 2;
			}

			if(var8 == var2) {
				var9 = 3;
			}

			return new MovingObjectPosition(0, 0, 0, var9, var8);
		}
	}

	private boolean isVecInYZ(Vec3D var1) {
		return var1 == null ? false : var1.yCoord >= this.minY && var1.yCoord <= this.maxY && var1.zCoord >= this.minZ && var1.zCoord <= this.maxZ;
	}

	private boolean isVecInXZ(Vec3D var1) {
		return var1 == null ? false : var1.xCoord >= this.minX && var1.xCoord <= this.maxX && var1.zCoord >= this.minZ && var1.zCoord <= this.maxZ;
	}

	private boolean isVecInXY(Vec3D var1) {
		return var1 == null ? false : var1.xCoord >= this.minX && var1.xCoord <= this.maxX && var1.yCoord >= this.minY && var1.yCoord <= this.maxY;
	}
}
