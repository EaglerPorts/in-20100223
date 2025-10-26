package net.minecraft.client.render;

import java.util.Comparator;
import net.minecraft.game.entity.player.EntityPlayer;

public final class EntitySorter implements Comparator {
	private EntityPlayer player;

	public EntitySorter(EntityPlayer var1) {
		this.player = var1;
	}

	public final int compare(Object var1, Object var2) {
		WorldRenderer var10001 = (WorldRenderer)var1;
		WorldRenderer var3 = (WorldRenderer)var2;
		WorldRenderer var4 = var10001;
		return var4.distanceToEntitySquared(this.player) < var3.distanceToEntitySquared(this.player) ? -1 : 1;
	}
}
