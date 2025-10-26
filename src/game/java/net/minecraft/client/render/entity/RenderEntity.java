package net.minecraft.client.render.entity;

import net.minecraft.game.entity.Entity;
import org.lwjgl.opengl.GL11;

public final class RenderEntity extends Render {
	public final void doRender(Entity var1, float var2, float var3, float var4, float var5, float var6) {
		GL11.glPushMatrix();
		GL11.glTranslatef(var2 - var1.lastTickPosX, var3 - var1.lastTickPosY, var4 - var1.lastTickPosZ);
		renderOffsetAABB(var1.boundingBox);
		GL11.glPopMatrix();
	}
}
