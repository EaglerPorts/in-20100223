package net.minecraft.client.effect;

import java.util.ArrayList;
import java.util.List;
import net.lax1dude.eaglercraft.Random;
import net.minecraft.client.render.RenderEngine;
import net.minecraft.client.render.Tessellator;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.level.World;
import net.minecraft.game.level.block.Block;
import org.lwjgl.opengl.GL11;
import util.MathHelper;

public final class EffectRenderer {
	private World worldObj;
	private List[] fxLayers = new List[3];
	private RenderEngine renderEngine;
	private Random rand = new Random();

	public EffectRenderer(World var1, RenderEngine var2) {
		if (var1 != null) {
			this.worldObj = var1;
		}

		this.renderEngine = var2;

		for (int var3 = 0; var3 < 3; ++var3) {
			this.fxLayers[var3] = new ArrayList();
		}

	}

	public final void addEffect(EntityFX var1) {
		int var2 = var1.getFXLayer();
		this.fxLayers[var2].add(var1);
	}

	public final void updateEffects() {
		for (int var1 = 0; var1 < 3; ++var1) {
			for (int var2 = 0; var2 < this.fxLayers[var1].size(); ++var2) {
				EntityFX var3 = (EntityFX) this.fxLayers[var1].get(var2);
				var3.onEntityUpdate();
				if (var3.isDead) {
					this.fxLayers[var1].remove(var2--);
				}
			}
		}

	}

	public final void renderParticles(Entity var1, float var2) {
		float var3 = MathHelper.cos(var1.rotationYaw * (float) Math.PI / 180.0F);
		float var4 = MathHelper.sin(var1.rotationYaw * (float) Math.PI / 180.0F);
		float var5 = -var4 * MathHelper.sin(var1.rotationPitch * (float) Math.PI / 180.0F);
		float var6 = var3 * MathHelper.sin(var1.rotationPitch * (float) Math.PI / 180.0F);
		float var11 = MathHelper.cos(var1.rotationPitch * (float) Math.PI / 180.0F);

		for (int var7 = 0; var7 < 2; ++var7) {
			if (this.fxLayers[var7].size() != 0) {
				int var8 = 0;
				if (var7 == 0) {
					var8 = this.renderEngine.getTexture("/particles.png");
				}

				if (var7 == 1) {
					var8 = this.renderEngine.getTexture("/terrain.png");
				}

				GL11.glBindTexture(GL11.GL_TEXTURE_2D, var8);
				Tessellator var12 = Tessellator.instance;
				var12.startDrawingQuads();

				for (int var9 = 0; var9 < this.fxLayers[var7].size(); ++var9) {
					EntityFX var10 = (EntityFX) this.fxLayers[var7].get(var9);
					var10.renderParticle(var12, var2, var3, var11, var4, var5, var6);
				}

				var12.draw();
			}
		}

	}

	public final void renderLitParticles(float var1) {
		if (this.fxLayers[2].size() != 0) {
			Tessellator var2 = Tessellator.instance;

			for (int var3 = 0; var3 < this.fxLayers[2].size(); ++var3) {
				EntityFX var4 = (EntityFX) this.fxLayers[2].get(var3);
				var4.renderParticle(var2, var1, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
			}

		}
	}

	public final void clearEffects(World var1) {
		this.worldObj = var1;

		for (int var2 = 0; var2 < 3; ++var2) {
			this.fxLayers[var2].clear();
		}

	}

	public final void addBlockDestroyEffects(int var1, int var2, int var3) {
		int var4 = this.worldObj.getBlockId(var1, var2, var3);
		if (var4 != 0) {
			Block var11 = Block.blocksList[var4];

			for (int var5 = 0; var5 < 4; ++var5) {
				for (int var6 = 0; var6 < 4; ++var6) {
					for (int var7 = 0; var7 < 4; ++var7) {
						float var8 = (float) var1 + ((float) var5 + 0.5F) / 4.0F;
						float var9 = (float) var2 + ((float) var6 + 0.5F) / 4.0F;
						float var10 = (float) var3 + ((float) var7 + 0.5F) / 4.0F;
						this.addEffect(new EntityDiggingFX(this.worldObj, var8, var9, var10, var8 - (float) var1 - 0.5F,
								var9 - (float) var2 - 0.5F, var10 - (float) var3 - 0.5F, var11));
					}
				}
			}

		}
	}

	public final void addBlockHitEffects(int var1, int var2, int var3, int var4) {
		int var5 = this.worldObj.getBlockId(var1, var2, var3);
		if (var5 != 0) {
			Block var9 = Block.blocksList[var5];
			float var6 = (float) var1 + this.rand.nextFloat() * (var9.maxX - var9.minX - 0.2F) + 0.1F + var9.minX;
			float var7 = (float) var2 + this.rand.nextFloat() * (var9.maxY - var9.minY - 0.2F) + 0.1F + var9.minY;
			float var8 = (float) var3 + this.rand.nextFloat() * (var9.maxZ - var9.minZ - 0.2F) + 0.1F + var9.minZ;
			if (var4 == 0) {
				var7 = (float) var2 + var9.minY - 0.1F;
			}

			if (var4 == 1) {
				var7 = (float) var2 + var9.maxY + 0.1F;
			}

			if (var4 == 2) {
				var8 = (float) var3 + var9.minZ - 0.1F;
			}

			if (var4 == 3) {
				var8 = (float) var3 + var9.maxZ + 0.1F;
			}

			if (var4 == 4) {
				var6 = (float) var1 + var9.minX - 0.1F;
			}

			if (var4 == 5) {
				var6 = (float) var1 + var9.maxX + 0.1F;
			}

			this.addEffect((new EntityDiggingFX(this.worldObj, var6, var7, var8, 0.0F, 0.0F, 0.0F, var9))
					.multiplyVelocity(0.2F).multipleParticleScaleBy(0.6F));
		}
	}

	public final String getStatistics() {
		return "" + (this.fxLayers[0].size() + this.fxLayers[1].size() + this.fxLayers[2].size());
	}
}
