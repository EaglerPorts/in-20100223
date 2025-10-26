package net.minecraft.game.level;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.physics.AxisAlignedBB;

public final class EntityMap {
	public int width;
	public int depth;
	public int height;
	private EntityMapSlot slot = new EntityMapSlot(this);
	private EntityMapSlot slot2 = new EntityMapSlot(this);
	public List[] entityGrid;
	public List entities = new ArrayList();
	private List entitiesExcludingEntity = new ArrayList();

	public EntityMap(int var1, int var2, int var3) {
		this.width = var1 / 8;
		this.depth = var2 / 8;
		this.height = var3 / 8;
		if(this.width == 0) {
			this.width = 1;
		}

		if(this.depth == 0) {
			this.depth = 1;
		}

		if(this.height == 0) {
			this.height = 1;
		}

		this.entityGrid = new ArrayList[this.width * this.depth * this.height];

		for(var1 = 0; var1 < this.width; ++var1) {
			for(var2 = 0; var2 < this.depth; ++var2) {
				for(var3 = 0; var3 < this.height; ++var3) {
					this.entityGrid[(var3 * this.depth + var2) * this.width + var1] = new ArrayList();
				}
			}
		}

	}

	public final void insert(Entity var1) {
		this.entities.add(var1);
		this.slot.init(var1.posX, var1.posY, var1.posZ).add(var1);
		var1.lastTickPosX = var1.posX;
		var1.lastTickPosY = var1.posY;
		var1.lastTickPosZ = var1.posZ;
	}

	public final void remove(Entity var1) {
		this.slot.init(var1.lastTickPosX, var1.lastTickPosY, var1.lastTickPosZ).remove(var1);
		this.slot.init(var1.posX, var1.posY, var1.posZ).remove(var1);
		this.entities.remove(var1);
	}

	public final List getEntities(Entity var1, float var2, float var3, float var4, float var5, float var6, float var7) {
		this.entitiesExcludingEntity.clear();
		return this.getEntities(var1, var2, var3, var4, var5, var6, var7, this.entitiesExcludingEntity);
	}

	private List getEntities(Entity var1, float var2, float var3, float var4, float var5, float var6, float var7, List var8) {
		EntityMapSlot var9 = this.slot.init(var2, var3, var4);
		EntityMapSlot var10 = this.slot2.init(var5, var6, var7);

		for(int var11 = EntityMapSlot.a(var9) - 1; var11 <= EntityMapSlot.a(var10) + 1; ++var11) {
			for(int var12 = EntityMapSlot.b(var9) - 1; var12 <= EntityMapSlot.b(var10) + 1; ++var12) {
				for(int var13 = EntityMapSlot.c(var9) - 1; var13 <= EntityMapSlot.c(var10) + 1; ++var13) {
					if(var11 >= 0 && var12 >= 0 && var13 >= 0 && var11 < this.width && var12 < this.depth && var13 < this.height) {
						List var14 = this.entityGrid[(var13 * this.depth + var12) * this.width + var11];

						for(int var15 = 0; var15 < var14.size(); ++var15) {
							Entity var16 = (Entity)var14.get(var15);
							if(var16 != var1) {
								AxisAlignedBB var17 = var16.boundingBox;
								if(var5 > var17.minX && var2 < var17.maxX ? (var6 > var17.minY && var3 < var17.maxY ? var7 > var17.minZ && var4 < var17.maxZ : false) : false) {
									var8.add(var16);
								}
							}
						}
					}
				}
			}
		}

		return var8;
	}

	public final List getEntitiesWithinAABB(Entity var1, AxisAlignedBB var2) {
		this.entitiesExcludingEntity.clear();
		return var2 == null ? this.entitiesExcludingEntity : this.getEntities(var1, var2.minX, var2.minY, var2.minZ, var2.maxX, var2.maxY, var2.maxZ, this.entitiesExcludingEntity);
	}

	public final void updateEntities() {
		for(int var1 = 0; var1 < this.entities.size(); ++var1) {
			Entity var2 = (Entity)this.entities.get(var1);
			var2.lastTickPosX = var2.posX;
			var2.lastTickPosY = var2.posY;
			var2.lastTickPosZ = var2.posZ;
			var2.onEntityUpdate();
			if(var2.isDead) {
				this.entities.remove(var1--);
				this.slot.init(var2.lastTickPosX, var2.lastTickPosY, var2.lastTickPosZ).remove(var2);
			} else {
				int var3 = (int)(var2.lastTickPosX / 8.0F);
				int var4 = (int)(var2.lastTickPosY / 8.0F);
				int var5 = (int)(var2.lastTickPosZ / 8.0F);
				int var6 = (int)(var2.posX / 8.0F);
				int var7 = (int)(var2.posY / 8.0F);
				int var8 = (int)(var2.posZ / 8.0F);
				if(var3 != var6 || var4 != var7 || var5 != var8) {
					EntityMapSlot var11 = this.slot.init(var2.lastTickPosX, var2.lastTickPosY, var2.lastTickPosZ);
					EntityMapSlot var9 = this.slot2.init(var2.posX, var2.posY, var2.posZ);
					if(!var11.equals(var9)) {
						var11.remove(var2);
						var9.add(var2);
					}
				}
			}
		}

	}
}
