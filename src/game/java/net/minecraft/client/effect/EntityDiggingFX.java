package net.minecraft.client.effect;

import net.minecraft.client.render.Tessellator;
import net.minecraft.game.level.World;
import net.minecraft.game.level.block.Block;

public final class EntityDiggingFX extends EntityFX {
	public EntityDiggingFX(World var1, float var2, float var3, float var4, float var5, float var6, float var7, Block var8) {
		super(var1, var2, var3, var4, var5, var6, var7);
		this.particleTextureIndex = var8.blockIndexInTexture;
		this.particleGravity = var8.blockParticleGravity;
		this.particleRed = this.particleGreen = this.particleBlue = 0.6F;
		this.particleScale /= 2.0F;
	}

	public final int getFXLayer() {
		return 1;
	}

	public final void renderParticle(Tessellator var1, float var2, float var3, float var4, float var5, float var6, float var7) {
		float var8 = ((float)(this.particleTextureIndex % 16) + this.particleTextureJitterX / 4.0F) / 16.0F;
		float var9 = var8 + 0.999F / 64.0F;
		float var10 = ((float)(this.particleTextureIndex / 16) + this.particleTextureJitterY / 4.0F) / 16.0F;
		float var11 = var10 + 0.999F / 64.0F;
		float var12 = 0.1F * this.particleScale;
		float var13 = this.prevPosX + (this.posX - this.prevPosX) * var2;
		float var14 = this.prevPosY + (this.posY - this.prevPosY) * var2;
		float var15 = this.prevPosZ + (this.posZ - this.prevPosZ) * var2;
		var2 = this.getEntityBrightness(var2);
		var1.setColorOpaque_F(var2 * this.particleRed, var2 * this.particleGreen, var2 * this.particleBlue);
		var1.addVertexWithUV(var13 - var3 * var12 - var6 * var12, var14 - var4 * var12, var15 - var5 * var12 - var7 * var12, var8, var11);
		var1.addVertexWithUV(var13 - var3 * var12 + var6 * var12, var14 + var4 * var12, var15 - var5 * var12 + var7 * var12, var8, var10);
		var1.addVertexWithUV(var13 + var3 * var12 + var6 * var12, var14 + var4 * var12, var15 + var5 * var12 + var7 * var12, var9, var10);
		var1.addVertexWithUV(var13 + var3 * var12 - var6 * var12, var14 - var4 * var12, var15 + var5 * var12 - var7 * var12, var9, var11);
	}
}
