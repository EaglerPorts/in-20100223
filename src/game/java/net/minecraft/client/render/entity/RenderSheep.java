package net.minecraft.client.render.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.game.entity.EntityLiving;
import net.minecraft.game.entity.animal.EntitySheep;

public final class RenderSheep extends RenderLiving {
	public RenderSheep(ModelBase var1, ModelBase var2, float var3) {
		super(var1, 0.7F);
		this.setRenderPassModel(var2);
	}

	protected final boolean shouldRenderPass(EntityLiving var1, int var2) {
		EntitySheep var10001 = (EntitySheep)var1;
		int var3 = var2;
		EntitySheep var4 = var10001;
		this.loadTexture("/mob/sheep_fur.png");
		return var3 == 0 && !var4.sheared;
	}
}
