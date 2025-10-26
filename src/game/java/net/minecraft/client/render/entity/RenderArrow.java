package net.minecraft.client.render.entity;

import net.minecraft.client.render.Tessellator;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.projectile.EntityArrow;
import org.lwjgl.opengl.GL11;
import util.MathHelper;

public final class RenderArrow extends Render {
	public final void doRender(Entity var1, float var2, float var3, float var4, float var5, float var6) {
		EntityArrow var8 = (EntityArrow)var1;
		this.loadTexture("/item/arrows.png");
		GL11.glPushMatrix();
		GL11.glTranslatef(var2, var3, var4);
		GL11.glRotatef(var8.prevRotationYaw + (var8.rotationYaw - var8.prevRotationYaw) * var6 - 90.0F, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(var8.prevRotationPitch + (var8.rotationPitch - var8.prevRotationPitch) * var6, 0.0F, 0.0F, 1.0F);
		Tessellator var7 = Tessellator.instance;
		GL11.glEnable(GL11.GL_NORMALIZE);
		var2 = (float)var8.arrowShake - var6;
		if(var2 > 0.0F) {
			var2 = -MathHelper.sin(var2 * 3.0F) * var2;
			GL11.glRotatef(var2, 0.0F, 0.0F, 1.0F);
		}

		GL11.glRotatef(45.0F, 1.0F, 0.0F, 0.0F);
		GL11.glScalef(0.05625F, 0.05625F, 0.05625F);
		GL11.glTranslatef(-4.0F, 0.0F, 0.0F);
		GL11.glNormal3f(0.05625F, 0.0F, 0.0F);
		var7.startDrawingQuads();
		var7.addVertexWithUV(-7.0F, -2.0F, -2.0F, 0.0F, 0.15625F);
		var7.addVertexWithUV(-7.0F, -2.0F, 2.0F, 0.15625F, 0.15625F);
		var7.addVertexWithUV(-7.0F, 2.0F, 2.0F, 0.15625F, 5.0F / 16.0F);
		var7.addVertexWithUV(-7.0F, 2.0F, -2.0F, 0.0F, 5.0F / 16.0F);
		var7.draw();
		GL11.glNormal3f(-0.05625F, 0.0F, 0.0F);
		var7.startDrawingQuads();
		var7.addVertexWithUV(-7.0F, 2.0F, -2.0F, 0.0F, 0.15625F);
		var7.addVertexWithUV(-7.0F, 2.0F, 2.0F, 0.15625F, 0.15625F);
		var7.addVertexWithUV(-7.0F, -2.0F, 2.0F, 0.15625F, 5.0F / 16.0F);
		var7.addVertexWithUV(-7.0F, -2.0F, -2.0F, 0.0F, 5.0F / 16.0F);
		var7.draw();

		for(int var9 = 0; var9 < 4; ++var9) {
			GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
			GL11.glNormal3f(0.0F, 0.0F, 0.05625F);
			var7.startDrawingQuads();
			var7.addVertexWithUV(-8.0F, -2.0F, 0.0F, 0.0F, 0.0F);
			var7.addVertexWithUV(8.0F, -2.0F, 0.0F, 0.5F, 0.0F);
			var7.addVertexWithUV(8.0F, 2.0F, 0.0F, 0.5F, 0.15625F);
			var7.addVertexWithUV(-8.0F, 2.0F, 0.0F, 0.0F, 0.15625F);
			var7.draw();
		}

		GL11.glDisable(GL11.GL_NORMALIZE);
		GL11.glPopMatrix();
	}
}
