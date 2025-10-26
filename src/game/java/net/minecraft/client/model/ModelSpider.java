package net.minecraft.client.model;

import util.MathHelper;

public final class ModelSpider extends ModelBase {
	private ModelRenderer spiderHead = new ModelRenderer(32, 4);
	private ModelRenderer spiderNeck;
	private ModelRenderer spiderBody;
	private ModelRenderer spiderLeg1;
	private ModelRenderer spiderLeg2;
	private ModelRenderer spiderLeg3;
	private ModelRenderer spiderLeg4;
	private ModelRenderer spiderLeg5;
	private ModelRenderer spiderLeg6;
	private ModelRenderer spiderLeg7;
	private ModelRenderer spiderLeg8;

	public ModelSpider() {
		this.spiderHead.addBox(-4.0F, -4.0F, -8.0F, 8, 8, 8, 0.0F);
		this.spiderHead.setRotationPoint(0.0F, 15.0F, -3.0F);
		this.spiderNeck = new ModelRenderer(0, 0);
		this.spiderNeck.addBox(-3.0F, -3.0F, -3.0F, 6, 6, 6, 0.0F);
		this.spiderNeck.setRotationPoint(0.0F, 15.0F, 0.0F);
		this.spiderBody = new ModelRenderer(0, 12);
		this.spiderBody.addBox(-5.0F, -4.0F, -6.0F, 10, 8, 12, 0.0F);
		this.spiderBody.setRotationPoint(0.0F, 15.0F, 9.0F);
		this.spiderLeg1 = new ModelRenderer(18, 0);
		this.spiderLeg1.addBox(-15.0F, -1.0F, -1.0F, 16, 2, 2, 0.0F);
		this.spiderLeg1.setRotationPoint(-4.0F, 15.0F, 2.0F);
		this.spiderLeg2 = new ModelRenderer(18, 0);
		this.spiderLeg2.addBox(-1.0F, -1.0F, -1.0F, 16, 2, 2, 0.0F);
		this.spiderLeg2.setRotationPoint(4.0F, 15.0F, 2.0F);
		this.spiderLeg3 = new ModelRenderer(18, 0);
		this.spiderLeg3.addBox(-15.0F, -1.0F, -1.0F, 16, 2, 2, 0.0F);
		this.spiderLeg3.setRotationPoint(-4.0F, 15.0F, 1.0F);
		this.spiderLeg4 = new ModelRenderer(18, 0);
		this.spiderLeg4.addBox(-1.0F, -1.0F, -1.0F, 16, 2, 2, 0.0F);
		this.spiderLeg4.setRotationPoint(4.0F, 15.0F, 1.0F);
		this.spiderLeg5 = new ModelRenderer(18, 0);
		this.spiderLeg5.addBox(-15.0F, -1.0F, -1.0F, 16, 2, 2, 0.0F);
		this.spiderLeg5.setRotationPoint(-4.0F, 15.0F, 0.0F);
		this.spiderLeg6 = new ModelRenderer(18, 0);
		this.spiderLeg6.addBox(-1.0F, -1.0F, -1.0F, 16, 2, 2, 0.0F);
		this.spiderLeg6.setRotationPoint(4.0F, 15.0F, 0.0F);
		this.spiderLeg7 = new ModelRenderer(18, 0);
		this.spiderLeg7.addBox(-15.0F, -1.0F, -1.0F, 16, 2, 2, 0.0F);
		this.spiderLeg7.setRotationPoint(-4.0F, 15.0F, -1.0F);
		this.spiderLeg8 = new ModelRenderer(18, 0);
		this.spiderLeg8.addBox(-1.0F, -1.0F, -1.0F, 16, 2, 2, 0.0F);
		this.spiderLeg8.setRotationPoint(4.0F, 15.0F, -1.0F);
	}

	public final void render(float var1, float var2, float var3, float var4, float var5, float var6) {
		this.setRotationAngles(var1, var2, var3, var4, var5, 1.0F);
		this.spiderHead.render(1.0F);
		this.spiderNeck.render(1.0F);
		this.spiderBody.render(1.0F);
		this.spiderLeg1.render(1.0F);
		this.spiderLeg2.render(1.0F);
		this.spiderLeg3.render(1.0F);
		this.spiderLeg4.render(1.0F);
		this.spiderLeg5.render(1.0F);
		this.spiderLeg6.render(1.0F);
		this.spiderLeg7.render(1.0F);
		this.spiderLeg8.render(1.0F);
	}

	public final void setRotationAngles(float var1, float var2, float var3, float var4, float var5, float var6) {
		this.spiderHead.rotateAngleY = var4 / (180.0F / (float)Math.PI);
		this.spiderHead.rotateAngleX = var5 / (180.0F / (float)Math.PI);
		this.spiderLeg1.rotateAngleZ = (float)Math.PI * -0.25F;
		this.spiderLeg2.rotateAngleZ = (float)Math.PI * 0.25F;
		this.spiderLeg3.rotateAngleZ = -((float)Math.PI * 0.185F);
		this.spiderLeg4.rotateAngleZ = (float)Math.PI * 0.185F;
		this.spiderLeg5.rotateAngleZ = -((float)Math.PI * 0.185F);
		this.spiderLeg6.rotateAngleZ = (float)Math.PI * 0.185F;
		this.spiderLeg7.rotateAngleZ = (float)Math.PI * -0.25F;
		this.spiderLeg8.rotateAngleZ = (float)Math.PI * 0.25F;
		this.spiderLeg1.rotateAngleY = (float)Math.PI * 0.25F;
		this.spiderLeg2.rotateAngleY = (float)Math.PI * -0.25F;
		this.spiderLeg3.rotateAngleY = (float)Math.PI * 0.125F;
		this.spiderLeg4.rotateAngleY = (float)Math.PI * -0.125F;
		this.spiderLeg5.rotateAngleY = (float)Math.PI * -0.125F;
		this.spiderLeg6.rotateAngleY = (float)Math.PI * 0.125F;
		this.spiderLeg7.rotateAngleY = (float)Math.PI * -0.25F;
		this.spiderLeg8.rotateAngleY = (float)Math.PI * 0.25F;
		var3 = -(MathHelper.cos(var1 * 0.6662F * 2.0F) * 0.4F) * var2;
		var4 = -(MathHelper.cos(var1 * 0.6662F * 2.0F + (float)Math.PI) * 0.4F) * var2;
		var5 = -(MathHelper.cos(var1 * 0.6662F * 2.0F + (float)Math.PI * 0.5F) * 0.4F) * var2;
		var6 = -(MathHelper.cos(var1 * 0.6662F * 2.0F + (float)Math.PI * 3.0F / 2.0F) * 0.4F) * var2;
		float var7 = Math.abs(MathHelper.sin(var1 * 0.6662F) * 0.4F) * var2;
		float var8 = Math.abs(MathHelper.sin(var1 * 0.6662F + (float)Math.PI) * 0.4F) * var2;
		float var9 = Math.abs(MathHelper.sin(var1 * 0.6662F + (float)Math.PI * 0.5F) * 0.4F) * var2;
		var1 = Math.abs(MathHelper.sin(var1 * 0.6662F + (float)Math.PI * 3.0F / 2.0F) * 0.4F) * var2;
		this.spiderLeg1.rotateAngleY += var3;
		this.spiderLeg2.rotateAngleY -= var3;
		this.spiderLeg3.rotateAngleY += var4;
		this.spiderLeg4.rotateAngleY -= var4;
		this.spiderLeg5.rotateAngleY += var5;
		this.spiderLeg6.rotateAngleY -= var5;
		this.spiderLeg7.rotateAngleY += var6;
		this.spiderLeg8.rotateAngleY -= var6;
		this.spiderLeg1.rotateAngleZ += var7;
		this.spiderLeg2.rotateAngleZ -= var7;
		this.spiderLeg3.rotateAngleZ += var8;
		this.spiderLeg4.rotateAngleZ -= var8;
		this.spiderLeg5.rotateAngleZ += var9;
		this.spiderLeg6.rotateAngleZ -= var9;
		this.spiderLeg7.rotateAngleZ += var1;
		this.spiderLeg8.rotateAngleZ -= var1;
	}
}
