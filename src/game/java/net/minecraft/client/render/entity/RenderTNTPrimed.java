package net.minecraft.client.render.entity;

import net.minecraft.client.render.RenderBlocks;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.misc.EntityTNTPrimed;
import net.minecraft.game.level.block.Block;
import org.lwjgl.opengl.GL11;

public final class RenderTNTPrimed extends Render {
	private RenderBlocks blockRenderer = new RenderBlocks();

	public RenderTNTPrimed() {
		this.shadowSize = 0.5F;
	}

	public final void doRender(Entity var1, float var2, float var3, float var4, float var5, float var6) {
		EntityTNTPrimed var10001 = (EntityTNTPrimed)var1;
		var5 = var4;
		var4 = var3;
		var3 = var2;
		EntityTNTPrimed var7 = var10001;
		GL11.glPushMatrix();
		GL11.glTranslatef(var3, var4, var5);
		if((float)var7.fuse - var6 + 1.0F < 10.0F) {
			var3 = 1.0F - ((float)var7.fuse - var6 + 1.0F) / 10.0F;
			if(var3 < 0.0F) {
				var3 = 0.0F;
			}

			if(var3 > 1.0F) {
				var3 = 1.0F;
			}

			var3 *= var3;
			var3 *= var3;
			var3 = 1.0F + var3 * 0.3F;
			GL11.glScalef(var3, var3, var3);
		}

		var3 = (1.0F - ((float)var7.fuse - var6 + 1.0F) / 100.0F) * 0.8F;
		this.loadTexture("/terrain.png");
		this.blockRenderer.renderBlockOnInventory(Block.tnt);
		if(var7.fuse / 5 % 2 == 0) {
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_DST_ALPHA);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, var3);
			this.blockRenderer.renderBlockOnInventory(Block.tnt);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
		}

		GL11.glPopMatrix();
	}
}
