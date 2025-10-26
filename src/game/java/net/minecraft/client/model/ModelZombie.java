package net.minecraft.client.model;

import util.MathHelper;

public class ModelZombie extends ModelBiped {
	public final void setRotationAngles(float var1, float var2, float var3, float var4, float var5, float var6) {
		super.setRotationAngles(var1, var2, var3, var4, var5, var6);
		var1 = MathHelper.sin(0.0F);
		var2 = MathHelper.sin(0.0F);
		this.bipedRightArm.rotateAngleZ = 0.0F;
		this.bipedLeftArm.rotateAngleZ = 0.0F;
		this.bipedRightArm.rotateAngleY = -(0.1F - var1 * 0.6F);
		this.bipedLeftArm.rotateAngleY = 0.1F - var1 * 0.6F;
		this.bipedRightArm.rotateAngleX = (float)Math.PI * -0.5F;
		this.bipedLeftArm.rotateAngleX = (float)Math.PI * -0.5F;
		this.bipedRightArm.rotateAngleX -= var1 * 1.2F - var2 * 0.4F;
		this.bipedLeftArm.rotateAngleX -= var1 * 1.2F - var2 * 0.4F;
		this.bipedRightArm.rotateAngleZ += MathHelper.cos(var3 * 0.09F) * 0.05F + 0.05F;
		this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(var3 * 0.09F) * 0.05F + 0.05F;
		this.bipedRightArm.rotateAngleX += MathHelper.sin(var3 * 0.067F) * 0.05F;
		this.bipedLeftArm.rotateAngleX -= MathHelper.sin(var3 * 0.067F) * 0.05F;
	}
}
