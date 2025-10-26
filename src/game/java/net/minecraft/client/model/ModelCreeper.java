package net.minecraft.client.model;

import util.MathHelper;

public final class ModelCreeper extends ModelBase {
	private ModelRenderer head = new ModelRenderer(0, 0);
	private ModelRenderer headwear;
	private ModelRenderer body;
	private ModelRenderer leg1;
	private ModelRenderer leg2;
	private ModelRenderer leg3;
	private ModelRenderer leg4;

	public ModelCreeper() {
		this.head.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.0F);
		this.head.setRotationPoint(0.0F, 4.0F, 0.0F);
		this.headwear = new ModelRenderer(32, 0);
		this.headwear.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.5F);
		this.headwear.setRotationPoint(0.0F, 4.0F, 0.0F);
		this.body = new ModelRenderer(16, 16);
		this.body.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, 0.0F);
		this.body.setRotationPoint(0.0F, 4.0F, 0.0F);
		this.leg1 = new ModelRenderer(0, 16);
		this.leg1.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, 0.0F);
		this.leg1.setRotationPoint(-2.0F, 16.0F, 4.0F);
		this.leg2 = new ModelRenderer(0, 16);
		this.leg2.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, 0.0F);
		this.leg2.setRotationPoint(2.0F, 16.0F, 4.0F);
		this.leg3 = new ModelRenderer(0, 16);
		this.leg3.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, 0.0F);
		this.leg3.setRotationPoint(-2.0F, 16.0F, -4.0F);
		this.leg4 = new ModelRenderer(0, 16);
		this.leg4.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, 0.0F);
		this.leg4.setRotationPoint(2.0F, 16.0F, -4.0F);
	}

	public final void render(float var1, float var2, float var3, float var4, float var5, float var6) {
		this.setRotationAngles(var1, var2, var3, var4, var5, 1.0F);
		this.head.render(1.0F);
		this.body.render(1.0F);
		this.leg1.render(1.0F);
		this.leg2.render(1.0F);
		this.leg3.render(1.0F);
		this.leg4.render(1.0F);
	}

	public final void setRotationAngles(float var1, float var2, float var3, float var4, float var5, float var6) {
		this.head.rotateAngleY = var4 / (180.0F / (float)Math.PI);
		this.head.rotateAngleX = var5 / (180.0F / (float)Math.PI);
		this.leg1.rotateAngleX = MathHelper.cos(var1 * 0.6662F) * 1.4F * var2;
		this.leg2.rotateAngleX = MathHelper.cos(var1 * 0.6662F + (float)Math.PI) * 1.4F * var2;
		this.leg3.rotateAngleX = MathHelper.cos(var1 * 0.6662F + (float)Math.PI) * 1.4F * var2;
		this.leg4.rotateAngleX = MathHelper.cos(var1 * 0.6662F) * 1.4F * var2;
	}
}
