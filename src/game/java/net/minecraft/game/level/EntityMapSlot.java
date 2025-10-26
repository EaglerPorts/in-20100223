package net.minecraft.game.level;

import net.minecraft.game.entity.Entity;

final class EntityMapSlot {
	private int xSlot;
	private int ySlot;
	private int zSlot;
	private EntityMap entityMap;

	private EntityMapSlot(EntityMap var1, byte var2) {
		this.entityMap = var1;
	}

	public final EntityMapSlot init(float var1, float var2, float var3) {
		this.xSlot = (int)(var1 / 8.0F);
		this.ySlot = (int)(var2 / 8.0F);
		this.zSlot = (int)(var3 / 8.0F);
		if(this.xSlot < 0) {
			this.xSlot = 0;
		}

		if(this.ySlot < 0) {
			this.ySlot = 0;
		}

		if(this.zSlot < 0) {
			this.zSlot = 0;
		}

		if(this.xSlot >= this.entityMap.width) {
			this.xSlot = this.entityMap.width - 1;
		}

		if(this.ySlot >= this.entityMap.depth) {
			this.ySlot = this.entityMap.depth - 1;
		}

		if(this.zSlot >= this.entityMap.height) {
			this.zSlot = this.entityMap.height - 1;
		}

		return this;
	}

	public final void add(Entity var1) {
		if(this.xSlot >= 0 && this.ySlot >= 0 && this.zSlot >= 0) {
			this.entityMap.entityGrid[(this.zSlot * this.entityMap.depth + this.ySlot) * this.entityMap.width + this.xSlot].add(var1);
		}

	}

	public final void remove(Entity var1) {
		if(this.xSlot >= 0 && this.ySlot >= 0 && this.zSlot >= 0) {
			this.entityMap.entityGrid[(this.zSlot * this.entityMap.depth + this.ySlot) * this.entityMap.width + this.xSlot].remove(var1);
		}

	}

	EntityMapSlot(EntityMap var1) {
		this(var1, (byte)0);
	}

	static int a(EntityMapSlot var0) {
		return var0.xSlot;
	}

	static int b(EntityMapSlot var0) {
		return var0.ySlot;
	}

	static int c(EntityMapSlot var0) {
		return var0.zSlot;
	}
}
