package net.minecraft.game.level;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeSet;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.EntityLiving;
import net.minecraft.game.level.block.Block;
import net.minecraft.game.level.block.BlockContainer;
import net.minecraft.game.level.block.tileentity.TileEntity;
import net.minecraft.game.level.material.Material;
import net.minecraft.game.level.path.Pathfinder;
import net.minecraft.game.physics.AxisAlignedBB;
import net.minecraft.game.physics.MovingObjectPosition;
import net.minecraft.game.physics.Vec3D;
import util.MathHelper;

public final class World {
	private static float[] lightBrightnessTable = new float[16];
	public int width;
	public int length;
	public int height;
	public byte[] blocks;
	public byte[] data;
	public String name;
	public String authorName;
	public long createTime;
	public int xSpawn;
	public int ySpawn;
	public int zSpawn;
	public float rotSpawn;
	public int defaultFluid = Block.waterMoving.blockID;
	List worldAccesses = new ArrayList();
	private List tickList = new LinkedList();
	public Map map = new HashMap();
	private List list = new ArrayList();
	int[] heightMap;
	public Random random = new Random();
	private Random rand = new Random();
	private int randId = this.random.nextInt();
	public EntityMap entityMap;
	public int waterLevel;
	public int groundLevel;
	public int cloudHeight;
	public int skyColor = 10079487;
	public int fogColor = 16777215;
	public int cloudColor = 16777215;
	private int updateLCG = 0;
	private int playTime = 0;
	public EntityLiving playerEntity;
	public boolean survivalWorld = true;
	public int skyBrightness = 15;
	public int skylightSubtracted = 15;
	public Pathfinder pathFinder = new Pathfinder(this);
	private Light lightUpdates;
	public int worldTime = 0;
	private static short floodFillCounter;
	private short[] floodFillCounters = new short[1048576];
	private int[] coords = new int[1048576];
	private int[] floodedBlocks = new int[1048576];
	public int difficultySetting = 2;

	public final void load() {
		if(this.blocks == null) {
			throw new RuntimeException("The level is corrupt!");
		} else {
			this.worldAccesses = new ArrayList();
			this.heightMap = new int[this.width * this.length];
			Arrays.fill(this.heightMap, this.height);
			this.lightUpdates.updateSkylight(0, 0, this.width, this.length);
			this.random = new Random();
			this.randId = this.random.nextInt();
			this.tickList = new ArrayList();
			if(this.entityMap == null) {
				this.entityMap = new EntityMap(this.width, this.height, this.length);
			}

		}
	}

	public final void generate(int var1, int var2, int var3, byte[] var4, byte[] var5) {
		if(var5 != null && var5.length == 0) {
			var5 = null;
		}

		this.width = var1;
		this.length = var3;
		this.height = var2;
		this.blocks = var4;

		int var6;
		int var7;
		for(var2 = 0; var2 < this.width; ++var2) {
			for(var6 = 0; var6 < this.length; ++var6) {
				for(var7 = 0; var7 < this.height; ++var7) {
					int var8 = 0;
					if(var7 <= 1 && var7 < this.groundLevel - 1 && var4[((var7 + 1) * this.length + var6) * this.width + var2] == 0) {
						var8 = Block.lavaStill.blockID;
					} else if(var7 < this.groundLevel - 1) {
						var8 = Block.bedrock.blockID;
					} else if(var7 < this.groundLevel) {
						if(this.groundLevel > this.waterLevel && this.defaultFluid == Block.waterMoving.blockID) {
							var8 = Block.grass.blockID;
						} else {
							var8 = Block.dirt.blockID;
						}
					} else if(var7 < this.waterLevel) {
						var8 = this.defaultFluid;
					}

					var4[(var7 * this.length + var6) * this.width + var2] = (byte)var8;
					if(var7 == 1 && var2 != 0 && var6 != 0 && var2 != this.width - 1 && var6 != this.length - 1) {
						var7 = this.height - 2;
					}
				}
			}
		}

		this.heightMap = new int[var1 * var3];
		Arrays.fill(this.heightMap, this.height);
		if(var5 == null) {
			this.data = new byte[var4.length];
			this.lightUpdates = new Light(this);
			boolean var10 = true;
			World var11 = this;
			var2 = this.skylightSubtracted;

			for(var3 = 0; var3 < var11.width; ++var3) {
				for(int var12 = 0; var12 < var11.length; ++var12) {
					int var13;
					for(var13 = var11.height - 1; var13 > 0 && Block.lightOpacity[var11.getBlockId(var3, var13, var12)] == 0; --var13) {
					}

					var11.heightMap[var3 + var12 * var11.width] = var13 + 1;

					for(var13 = 0; var13 < var11.height; ++var13) {
						var6 = (var13 * var11.length + var12) * var11.width + var3;
						var7 = var11.heightMap[var3 + var12 * var11.width];
						var7 = var13 >= var7 ? var2 : 0;
						byte var14 = var11.blocks[var6];
						if(var7 < Block.lightValue[var14]) {
							var7 = Block.lightValue[var14];
						}

						var11.data[var6] = (byte)((var11.data[var6] & 240) + var7);
					}
				}
			}

			var11.lightUpdates.updateBlockLight(0, 0, 0, var11.width, var11.height, var11.length);
		} else {
			this.data = var5;
			this.lightUpdates = new Light(this);
		}

		for(var2 = 0; var2 < this.worldAccesses.size(); ++var2) {
			((IWorldAccess)this.worldAccesses.get(var2)).loadRenderers();
		}

		this.tickList.clear();
		this.findSpawn();
		this.load();
		System.gc();
	}

	public final void findSpawn() {
		Random var1 = new Random();
		int var2 = 0;

		while(true) {
			label58:
			while(true) {
				int var3;
				int var4;
				int var5;
				do {
					do {
						++var2;
						var3 = var1.nextInt(this.width / 2) + this.width / 4;
						var4 = var1.nextInt(this.length / 2) + this.length / 4;
						var5 = this.getFirstUncoveredBlock(var3, var4) + 1;
						if(var2 == 1000000) {
							this.xSpawn = var3;
							this.ySpawn = this.height + 100;
							this.zSpawn = var4;
							this.rotSpawn = 180.0F;
							return;
						}
					} while(var5 < 4);
				} while(var5 <= this.waterLevel);

				int var6;
				int var7;
				int var8;
				for(var6 = var3 - 3; var6 <= var3 + 3; ++var6) {
					for(var7 = var5 - 1; var7 <= var5 + 2; ++var7) {
						for(var8 = var4 - 3 - 2; var8 <= var4 + 3; ++var8) {
							if(this.getBlockMaterial(var6, var7, var8).isSolid()) {
								continue label58;
							}
						}
					}
				}

				var6 = var5 - 2;

				for(var7 = var3 - 3; var7 <= var3 + 3; ++var7) {
					for(var8 = var4 - 3 - 2; var8 <= var4 + 3; ++var8) {
						if(!Block.opaqueCubeLookup[this.getBlockId(var7, var6, var8)]) {
							continue label58;
						}
					}
				}

				this.xSpawn = var3;
				this.ySpawn = var5;
				this.zSpawn = var4;
				this.rotSpawn = 180.0F;
				return;
			}
		}
	}

	public final void addWorldAccess(IWorldAccess var1) {
		for(int var2 = 0; var2 < this.entityMap.entities.size(); ++var2) {
			var1.obtainEntitySkin((Entity)this.entityMap.entities.get(var2));
		}

		this.worldAccesses.add(var1);
	}

	public final void finalize() {
	}

	public final void removeWorldAccess(IWorldAccess var1) {
		this.worldAccesses.remove(var1);
	}

	public final ArrayList getCollidingBoundingBoxes(AxisAlignedBB var1) {
		ArrayList var2 = new ArrayList();
		int var3 = (int)var1.minX;
		int var4 = (int)var1.maxX + 1;
		int var5 = (int)var1.minY;
		int var6 = (int)var1.maxY + 1;
		int var7 = (int)var1.minZ;
		int var8 = (int)var1.maxZ + 1;
		if(var1.minX < 0.0F) {
			--var3;
		}

		if(var1.minY < 0.0F) {
			--var5;
		}

		if(var1.minZ < 0.0F) {
			--var7;
		}

		for(var3 = var3; var3 < var4; ++var3) {
			for(int var9 = var5; var9 < var6; ++var9) {
				for(int var10 = var7; var10 < var8; ++var10) {
					Block var11 = Block.blocksList[this.getBlockId(var3, var9, var10)];
					AxisAlignedBB var12;
					if(var11 != null) {
						var12 = var11.getCollisionBoundingBoxFromPool(var3, var9, var10);
						if(var12 != null && var1.intersectsWith(var12)) {
							var2.add(var12);
						}
					} else if(this.groundLevel < 0 && (var9 < this.groundLevel || var9 < this.waterLevel)) {
						var12 = Block.bedrock.getCollisionBoundingBoxFromPool(var3, var9, var10);
						if(var12 != null && var1.intersectsWith(var12)) {
							var2.add(var12);
						}
					}
				}
			}
		}

		return var2;
	}

	public final void swap(int var1, int var2, int var3, int var4, int var5, int var6) {
		int var7 = this.getBlockId(var1, var2, var3);
		int var8 = this.getBlockId(var4, var5, var6);
		this.setBlock(var1, var2, var3, var8);
		this.setBlock(var4, var5, var6, var7);
		this.notifyBlocksOfNeighborChange(var1, var2, var3, var8);
		this.notifyBlocksOfNeighborChange(var4, var5, var6, var7);
	}

	public final boolean setBlock(int var1, int var2, int var3, int var4) {
		if(var1 > 0 && var2 > 0 && var3 > 0 && var1 < this.width - 1 && var2 < this.height - 1 && var3 < this.length - 1) {
			if(var4 == this.blocks[(var2 * this.length + var3) * this.width + var1]) {
				return false;
			} else {
				if(var4 == 0 && (var1 == 0 || var3 == 0 || var1 == this.width - 1 || var3 == this.length - 1) && var2 >= this.groundLevel && var2 < this.waterLevel) {
					var4 = Block.waterMoving.blockID;
				}

				byte var5 = this.blocks[(var2 * this.length + var3) * this.width + var1];
				this.blocks[(var2 * this.length + var3) * this.width + var1] = (byte)var4;
				this.setBlockMetadata(var1, var2, var3, 0);
				if(var5 != 0) {
					Block.blocksList[var5].onBlockRemoval(this, var1, var2, var3);
				}

				if(var4 != 0) {
					Block.blocksList[var4].onBlockAdded(this, var1, var2, var3);
				}

				if(Block.lightOpacity[var5] != Block.lightOpacity[var4] || Block.lightValue[var5] != 0 || Block.lightValue[var4] != 0) {
					this.lightUpdates.updateSkylight(var1, var3, 1, 1);
					this.lightUpdates.updateBlockLight(var1, var2, var3, var1 + 1, var2 + 1, var3 + 1);
				}

				for(var4 = 0; var4 < this.worldAccesses.size(); ++var4) {
					((IWorldAccess)this.worldAccesses.get(var4)).markBlockAndNeighborsNeedsUpdate(var1, var2, var3);
				}

				return true;
			}
		} else {
			return false;
		}
	}

	public final boolean setBlockWithNotify(int var1, int var2, int var3, int var4) {
		if(this.setBlock(var1, var2, var3, var4)) {
			this.notifyBlocksOfNeighborChange(var1, var2, var3, var4);
			return true;
		} else {
			return false;
		}
	}

	public final void notifyBlocksOfNeighborChange(int var1, int var2, int var3, int var4) {
		this.notifyBlockOfNeighborChange(var1 - 1, var2, var3, var4);
		this.notifyBlockOfNeighborChange(var1 + 1, var2, var3, var4);
		this.notifyBlockOfNeighborChange(var1, var2 - 1, var3, var4);
		this.notifyBlockOfNeighborChange(var1, var2 + 1, var3, var4);
		this.notifyBlockOfNeighborChange(var1, var2, var3 - 1, var4);
		this.notifyBlockOfNeighborChange(var1, var2, var3 + 1, var4);
	}

	public final boolean setTileNoUpdate(int var1, int var2, int var3, int var4) {
		if(var1 >= 0 && var2 >= 0 && var3 >= 0 && var1 < this.width && var2 < this.height && var3 < this.length) {
			if(var4 == this.blocks[(var2 * this.length + var3) * this.width + var1]) {
				return false;
			} else {
				this.blocks[(var2 * this.length + var3) * this.width + var1] = (byte)var4;
				this.lightUpdates.updateBlockLight(var1, var2, var3, var1 + 1, var2 + 1, var3 + 1);
				return true;
			}
		} else {
			return false;
		}
	}

	private void notifyBlockOfNeighborChange(int var1, int var2, int var3, int var4) {
		if(var1 >= 0 && var2 >= 0 && var3 >= 0 && var1 < this.width && var2 < this.height && var3 < this.length) {
			Block var5 = Block.blocksList[this.blocks[(var2 * this.length + var3) * this.width + var1]];
			if(var5 != null) {
				var5.onNeighborBlockChange(this, var1, var2, var3, var4);
			}

		}
	}

	public final int getBlockId(int var1, int var2, int var3) {
		if(var1 < 0) {
			var1 = 0;
		} else if(var1 >= this.width) {
			var1 = this.width - 1;
		}

		if(var2 < 0) {
			var2 = 0;
		} else if(var2 >= this.height) {
			var2 = this.height - 1;
		}

		if(var3 < 0) {
			var3 = 0;
		} else if(var3 >= this.length) {
			var3 = this.length - 1;
		}

		return this.blocks[(var2 * this.length + var3) * this.width + var1] & 255;
	}

	public final boolean isBlockNormalCube(int var1, int var2, int var3) {
		Block var4 = Block.blocksList[this.getBlockId(var1, var2, var3)];
		return var4 == null ? false : var4.isOpaqueCube();
	}

	public final void updateEntities() {
		this.entityMap.updateEntities();

		for(int var1 = 0; var1 < this.list.size(); ++var1) {
			TileEntity var2 = (TileEntity)this.list.get(var1);
			var2.updateEntity();
		}

	}

	public final void updateLighting() {
		this.lightUpdates.updateLight();
	}

	public final float getStarBrightness(float var1) {
		var1 = this.getCelestialAngle(var1);
		var1 = 1.0F - (MathHelper.cos(var1 * (float)Math.PI * 2.0F) * 2.0F + 12.0F / 16.0F);
		if(var1 < 0.0F) {
			var1 = 0.0F;
		}

		if(var1 > 1.0F) {
			var1 = 1.0F;
		}

		return var1 * var1 * 0.5F;
	}

	public final Vec3D getSkyColor(float var1) {
		var1 = this.getCelestialAngle(var1);
		var1 = MathHelper.cos(var1 * (float)Math.PI * 2.0F) * 2.0F + 0.5F;
		if(var1 < 0.0F) {
			var1 = 0.0F;
		}

		if(var1 > 1.0F) {
			var1 = 1.0F;
		}

		float var2 = (float)(this.skyColor >> 16 & 255) / 255.0F;
		float var3 = (float)(this.skyColor >> 8 & 255) / 255.0F;
		float var4 = (float)(this.skyColor & 255) / 255.0F;
		var2 *= var1;
		var3 *= var1;
		var4 *= var1;
		return new Vec3D(var2, var3, var4);
	}

	public final float getCelestialAngle(float var1) {
		if(this.skyBrightness > 15) {
			return 0.0F;
		} else {
			var1 = ((float)this.worldTime + var1) / 24000.0F - 0.15F;
			return var1;
		}
	}

	public final Vec3D getFogColor(float var1) {
		var1 = this.getCelestialAngle(var1);
		var1 = MathHelper.cos(var1 * (float)Math.PI * 2.0F) * 2.0F + 0.5F;
		if(var1 < 0.0F) {
			var1 = 0.0F;
		}

		if(var1 > 1.0F) {
			var1 = 1.0F;
		}

		float var2 = (float)(this.fogColor >> 16 & 255) / 255.0F;
		float var3 = (float)(this.fogColor >> 8 & 255) / 255.0F;
		float var4 = (float)(this.fogColor & 255) / 255.0F;
		var2 *= var1 * 0.94F + 0.06F;
		var3 *= var1 * 0.94F + 0.06F;
		var4 *= var1 * 0.91F + 0.09F;
		return new Vec3D(var2, var3, var4);
	}

	public final Vec3D getCloudColor(float var1) {
		var1 = this.getCelestialAngle(var1);
		var1 = MathHelper.cos(var1 * (float)Math.PI * 2.0F) * 2.0F + 0.5F;
		if(var1 < 0.0F) {
			var1 = 0.0F;
		}

		if(var1 > 1.0F) {
			var1 = 1.0F;
		}

		float var2 = (float)(this.cloudColor >> 16 & 255) / 255.0F;
		float var3 = (float)(this.cloudColor >> 8 & 255) / 255.0F;
		float var4 = (float)(this.cloudColor & 255) / 255.0F;
		var2 *= var1 * 0.9F + 0.1F;
		var3 *= var1 * 0.9F + 0.1F;
		var4 *= var1 * 0.85F + 0.15F;
		return new Vec3D(var2, var3, var4);
	}

	public final int getSkyBrightness() {
		float var1 = this.getCelestialAngle(1.0F);
		var1 = MathHelper.cos(var1 * (float)Math.PI * 2.0F) * 1.5F + 0.5F;
		if(var1 < 0.0F) {
			var1 = 0.0F;
		}

		if(var1 > 1.0F) {
			var1 = 1.0F;
		}

		int var2 = (int)(var1 * ((float)(15 * this.skyBrightness) / 15.0F - 4.0F) + 4.0F);
		if(var2 > 15) {
			var2 = 15;
		}

		if(var2 < 4) {
			var2 = 4;
		}

		return var2;
	}

	public final void tick() {
		++this.worldTime;
		if(this.worldTime == 24000) {
			this.worldTime = 0;
		}

		int var1 = this.getSkyBrightness();
		if(this.skylightSubtracted > var1) {
			this.updateChunkLight(this.skylightSubtracted - 1);
		}

		if(this.skylightSubtracted < var1) {
			this.updateChunkLight(this.skylightSubtracted + 1);
		}

		++this.playTime;
		var1 = 1;

		int var2;
		for(var2 = 1; 1 << var1 < this.width; ++var1) {
		}

		while(1 << var2 < this.length) {
			++var2;
		}

		int var3 = this.length - 1;
		int var4 = this.width - 1;
		int var5 = this.height - 1;
		int var6 = this.tickList.size();
		if(var6 > 200) {
			var6 = 200;
		}

		int var7;
		int var10;
		for(var7 = 0; var7 < var6; ++var7) {
			NextTickListEntry var8 = (NextTickListEntry)this.tickList.remove(0);
			if(var8.scheduledTime > 0) {
				--var8.scheduledTime;
				this.tickList.add(var8);
			} else {
				int var12 = var8.zCoord;
				int var11 = var8.yCoord;
				var10 = var8.xCoord;
				if(var10 >= 0 && var11 >= 0 && var12 >= 0 && var10 < this.width && var11 < this.height && var12 < this.length) {
					byte var9 = this.blocks[(var8.yCoord * this.length + var8.zCoord) * this.width + var8.xCoord];
					if(var9 == var8.blockID && var9 > 0) {
						Block.blocksList[var9].updateTick(this, var8.xCoord, var8.yCoord, var8.zCoord, this.random);
					}
				}
			}
		}

		this.updateLCG += this.width * this.length * this.height;
		var6 = this.updateLCG / 200;
		this.updateLCG -= var6 * 200;

		for(var7 = 0; var7 < var6; ++var7) {
			this.randId = this.randId * 3 + 1013904223;
			int var13 = this.randId >> 2;
			int var14 = var13 & var4;
			var10 = var13 >> var1 & var3;
			var13 = var13 >> var1 + var2 & var5;
			byte var15 = this.blocks[(var13 * this.length + var10) * this.width + var14];
			if(Block.tickOnLoad[var15]) {
				Block.blocksList[var15].updateTick(this, var14, var13, var10, this.random);
			}
		}

	}

	public final int entitiesInLevelList(Class var1) {
		int var2 = 0;

		for(int var3 = 0; var3 < this.entityMap.entities.size(); ++var3) {
			Entity var4 = (Entity)this.entityMap.entities.get(var3);
			if(var1.isAssignableFrom(var4.getClass())) {
				++var2;
			}
		}

		return var2;
	}

	public final int getGroundLevel() {
		return this.groundLevel;
	}

	public final int getWaterLevel() {
		return this.waterLevel;
	}

	public final boolean getIsAnyLiquid(AxisAlignedBB var1) {
		int var2 = (int)var1.minX;
		int var3 = (int)var1.maxX + 1;
		int var4 = (int)var1.minY;
		int var5 = (int)var1.maxY + 1;
		int var6 = (int)var1.minZ;
		int var7 = (int)var1.maxZ + 1;
		if(var1.minX < 0.0F) {
			--var2;
		}

		if(var1.minY < 0.0F) {
			--var4;
		}

		if(var1.minZ < 0.0F) {
			--var6;
		}

		if(var2 < 0) {
			var2 = 0;
		}

		if(var4 < 0) {
			var4 = 0;
		}

		if(var6 < 0) {
			var6 = 0;
		}

		if(var3 > this.width) {
			var3 = this.width;
		}

		if(var5 > this.height) {
			var5 = this.height;
		}

		if(var7 > this.length) {
			var7 = this.length;
		}

		for(int var10 = var2; var10 < var3; ++var10) {
			for(var2 = var4; var2 < var5; ++var2) {
				for(int var8 = var6; var8 < var7; ++var8) {
					Block var9 = Block.blocksList[this.getBlockId(var10, var2, var8)];
					if(var9 != null && var9.material.getIsLiquid()) {
						return true;
					}
				}
			}
		}

		return false;
	}

	public final boolean isBoundingBoxBurning(AxisAlignedBB var1) {
		int var2 = (int)var1.minX;
		int var3 = (int)var1.maxX + 1;
		int var4 = (int)var1.minY;
		int var5 = (int)var1.maxY + 1;
		int var6 = (int)var1.minZ;
		int var10 = (int)var1.maxZ + 1;

		for(var2 = var2; var2 < var3; ++var2) {
			for(int var7 = var4; var7 < var5; ++var7) {
				for(int var8 = var6; var8 < var10; ++var8) {
					int var9 = this.getBlockId(var2, var7, var8);
					if(var9 == Block.fire.blockID || var9 == Block.lavaMoving.blockID || var9 == Block.lavaStill.blockID) {
						return true;
					}
				}
			}
		}

		return false;
	}

	public final boolean handleMaterialAcceleration(AxisAlignedBB var1, Material var2) {
		int var3 = (int)var1.minX;
		int var4 = (int)var1.maxX + 1;
		int var5 = (int)var1.minY;
		int var6 = (int)var1.maxY + 1;
		int var7 = (int)var1.minZ;
		int var11 = (int)var1.maxZ + 1;

		for(var3 = var3; var3 < var4; ++var3) {
			for(int var8 = var5; var8 < var6; ++var8) {
				for(int var9 = var7; var9 < var11; ++var9) {
					Block var10 = Block.blocksList[this.getBlockId(var3, var8, var9)];
					if(var10 != null && var10.material == var2) {
						return true;
					}
				}
			}
		}

		return false;
	}

	public final void scheduleBlockUpdate(int var1, int var2, int var3, int var4) {
		NextTickListEntry var5 = new NextTickListEntry(var1, var2, var3, var4);
		if(var4 > 0) {
			var3 = Block.blocksList[var4].tickRate();
			var5.scheduledTime = var3;
		}

		this.tickList.add(var5);
	}

	public final boolean checkIfAABBIsClear1(AxisAlignedBB var1) {
		return this.entityMap.getEntitiesWithinAABB((Entity)null, var1).size() == 0;
	}

	public final boolean checkIfAABBIsClear(AxisAlignedBB var1) {
		List var4 = this.entityMap.getEntitiesWithinAABB((Entity)null, var1);

		for(int var2 = 0; var2 < var4.size(); ++var2) {
			Entity var3 = (Entity)var4.get(var2);
			if(var3.preventEntitySpawning) {
				return false;
			}
		}

		return true;
	}

	public final List getEntitiesWithinAABBExcludingEntity(Entity var1, AxisAlignedBB var2) {
		return this.entityMap.getEntitiesWithinAABB(var1, var2);
	}

	public final boolean isSolid(float var1, float var2, float var3, float var4) {
		return this.isSolid(var1 - 0.1F, var2 - 0.1F, var3 - 0.1F) ? true : (this.isSolid(var1 - 0.1F, var2 - 0.1F, var3 + 0.1F) ? true : (this.isSolid(var1 - 0.1F, var2 + 0.1F, var3 - 0.1F) ? true : (this.isSolid(var1 - 0.1F, var2 + 0.1F, var3 + 0.1F) ? true : (this.isSolid(var1 + 0.1F, var2 - 0.1F, var3 - 0.1F) ? true : (this.isSolid(var1 + 0.1F, var2 - 0.1F, var3 + 0.1F) ? true : (this.isSolid(var1 + 0.1F, var2 + 0.1F, var3 - 0.1F) ? true : this.isSolid(var1 + 0.1F, var2 + 0.1F, var3 + 0.1F)))))));
	}

	private boolean isSolid(float var1, float var2, float var3) {
		int var4 = this.getBlockId((int)var1, (int)var2, (int)var3);
		return var4 > 0 && Block.blocksList[var4].isOpaqueCube();
	}

	private int getFirstUncoveredBlock(int var1, int var2) {
		int var3;
		for(var3 = this.height; (this.getBlockId(var1, var3 - 1, var2) == 0 || Block.blocksList[this.getBlockId(var1, var3 - 1, var2)].material == Material.air) && var3 > 0; --var3) {
		}

		return var3;
	}

	public final void setSpawnLocation(int var1, int var2, int var3, float var4) {
		this.xSpawn = var1;
		this.ySpawn = var2;
		this.zSpawn = var3;
		this.rotSpawn = var4;
	}

	public final float getLightBrightness(int var1, int var2, int var3) {
		return lightBrightnessTable[this.getBlockLightValue(var1, var2, var3)];
	}

	public final byte getBlockLightValue(int var1, int var2, int var3) {
		if(var1 < 0) {
			var1 = 0;
		} else if(var1 >= this.width) {
			var1 = this.width - 1;
		}

		if(var2 < 0) {
			var2 = 0;
		} else if(var2 >= this.height) {
			var2 = this.height - 1;
		}

		if(var3 < 0) {
			var3 = 0;
		} else if(var3 >= this.length) {
			var3 = this.length - 1;
		}

		return this.blocks[(var2 * this.length + var3) * this.width + var1] == Block.stairSingle.blockID ? (var2 < this.height - 1 ? (byte)(this.data[((var2 + 1) * this.length + var3) * this.width + var1] & 15) : 15) : (byte)(this.data[(var2 * this.length + var3) * this.width + var1] & 15);
	}

	public final byte getBlockMetadata(int var1, int var2, int var3) {
		if(var1 < 0) {
			var1 = 0;
		} else if(var1 >= this.width) {
			var1 = this.width - 1;
		}

		if(var2 < 0) {
			var2 = 0;
		} else if(var2 >= this.height) {
			var2 = this.height - 1;
		}

		if(var3 < 0) {
			var3 = 0;
		} else if(var3 >= this.length) {
			var3 = this.length - 1;
		}

		return (byte)(this.data[(var2 * this.length + var3) * this.width + var1] >>> 4 & 15);
	}

	public final void setBlockMetadata(int var1, int var2, int var3, int var4) {
		if(var1 < 0) {
			var1 = 0;
		} else if(var1 >= this.width) {
			var1 = this.width - 1;
		}

		if(var2 < 0) {
			var2 = 0;
		} else if(var2 >= this.height) {
			var2 = this.height - 1;
		}

		if(var3 < 0) {
			var3 = 0;
		} else if(var3 >= this.length) {
			var3 = this.length - 1;
		}

		this.data[(var2 * this.length + var3) * this.width + var1] = (byte)((this.data[(var2 * this.length + var3) * this.width + var1] & 15) + (var4 << 4));

		for(var4 = 0; var4 < this.worldAccesses.size(); ++var4) {
			((IWorldAccess)this.worldAccesses.get(var4)).markBlockAndNeighborsNeedsUpdate(var1, var2, var3);
		}

	}

	public final Material getBlockMaterial(int var1, int var2, int var3) {
		var1 = this.getBlockId(var1, var2, var3);
		return var1 == 0 ? Material.air : Block.blocksList[var1].material;
	}

	public final boolean isWater(int var1, int var2, int var3) {
		var1 = this.getBlockId(var1, var2, var3);
		return var1 > 0 && Block.blocksList[var1].material == Material.water;
	}

	public final MovingObjectPosition rayTraceBlocks(Vec3D var1, Vec3D var2) {
		if(!Float.isNaN(var1.xCoord) && !Float.isNaN(var1.yCoord) && !Float.isNaN(var1.zCoord)) {
			if(!Float.isNaN(var2.xCoord) && !Float.isNaN(var2.yCoord) && !Float.isNaN(var2.zCoord)) {
				int var3 = MathHelper.floor_float(var2.xCoord);
				int var4 = MathHelper.floor_float(var2.yCoord);
				int var5 = MathHelper.floor_float(var2.zCoord);
				int var6 = MathHelper.floor_float(var1.xCoord);
				int var7 = MathHelper.floor_float(var1.yCoord);
				int var8 = MathHelper.floor_float(var1.zCoord);
				int var9 = 20;

				while(var9-- >= 0) {
					if(Float.isNaN(var1.xCoord) || Float.isNaN(var1.yCoord) || Float.isNaN(var1.zCoord)) {
						return null;
					}

					if(var6 == var3 && var7 == var4 && var8 == var5) {
						return null;
					}

					float var10 = 999.0F;
					float var11 = 999.0F;
					float var12 = 999.0F;
					if(var3 > var6) {
						var10 = (float)var6 + 1.0F;
					}

					if(var3 < var6) {
						var10 = (float)var6;
					}

					if(var4 > var7) {
						var11 = (float)var7 + 1.0F;
					}

					if(var4 < var7) {
						var11 = (float)var7;
					}

					if(var5 > var8) {
						var12 = (float)var8 + 1.0F;
					}

					if(var5 < var8) {
						var12 = (float)var8;
					}

					float var13 = 999.0F;
					float var14 = 999.0F;
					float var15 = 999.0F;
					float var16 = var2.xCoord - var1.xCoord;
					float var17 = var2.yCoord - var1.yCoord;
					float var18 = var2.zCoord - var1.zCoord;
					if(var10 != 999.0F) {
						var13 = (var10 - var1.xCoord) / var16;
					}

					if(var11 != 999.0F) {
						var14 = (var11 - var1.yCoord) / var17;
					}

					if(var12 != 999.0F) {
						var15 = (var12 - var1.zCoord) / var18;
					}

					byte var19;
					if(var13 < var14 && var13 < var15) {
						if(var3 > var6) {
							var19 = 4;
						} else {
							var19 = 5;
						}

						var1.xCoord = var10;
						var1.yCoord += var17 * var13;
						var1.zCoord += var18 * var13;
					} else if(var14 < var15) {
						if(var4 > var7) {
							var19 = 0;
						} else {
							var19 = 1;
						}

						var1.xCoord += var16 * var14;
						var1.yCoord = var11;
						var1.zCoord += var18 * var14;
					} else {
						if(var5 > var8) {
							var19 = 2;
						} else {
							var19 = 3;
						}

						var1.xCoord += var16 * var15;
						var1.yCoord += var17 * var15;
						var1.zCoord = var12;
					}

					Vec3D var20 = new Vec3D(var1.xCoord, var1.yCoord, var1.zCoord);
					var6 = (int)(var20.xCoord = (float)MathHelper.floor_float(var1.xCoord));
					if(var19 == 5) {
						--var6;
						++var20.xCoord;
					}

					var7 = (int)(var20.yCoord = (float)MathHelper.floor_float(var1.yCoord));
					if(var19 == 1) {
						--var7;
						++var20.yCoord;
					}

					var8 = (int)(var20.zCoord = (float)MathHelper.floor_float(var1.zCoord));
					if(var19 == 3) {
						--var8;
						++var20.zCoord;
					}

					int var21 = this.getBlockId(var6, var7, var8);
					Block var23 = Block.blocksList[var21];
					if(var21 > 0 && var23.isCollidable()) {
						MovingObjectPosition var22 = var23.collisionRayTrace(this, var6, var7, var8, var1, var2);
						if(var22 != null) {
							return var22;
						}
					}
				}

				return null;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	public final boolean growTrees(int var1, int var2, int var3) {
		int var4 = this.random.nextInt(3) + 4;
		boolean var5 = true;
		if(var2 > 0 && var2 + var4 + 1 <= this.height) {
			int var6;
			int var8;
			int var9;
			int var10;
			for(var6 = var2; var6 <= var2 + 1 + var4; ++var6) {
				byte var7 = 1;
				if(var6 == var2) {
					var7 = 0;
				}

				if(var6 >= var2 + 1 + var4 - 2) {
					var7 = 2;
				}

				for(var8 = var1 - var7; var8 <= var1 + var7 && var5; ++var8) {
					for(var9 = var3 - var7; var9 <= var3 + var7 && var5; ++var9) {
						if(var8 >= 0 && var6 >= 0 && var9 >= 0 && var8 < this.width && var6 < this.height && var9 < this.length) {
							var10 = this.blocks[(var6 * this.length + var9) * this.width + var8] & 255;
							if(var10 != 0) {
								var5 = false;
							}
						} else {
							var5 = false;
						}
					}
				}
			}

			if(!var5) {
				return false;
			} else {
				var6 = this.blocks[((var2 - 1) * this.length + var3) * this.width + var1] & 255;
				if((var6 == Block.grass.blockID || var6 == Block.dirt.blockID) && var2 < this.height - var4 - 1) {
					this.setBlockWithNotify(var1, var2 - 1, var3, Block.dirt.blockID);

					int var13;
					for(var13 = var2 - 3 + var4; var13 <= var2 + var4; ++var13) {
						var8 = var13 - (var2 + var4);
						var9 = 1 - var8 / 2;

						for(var10 = var1 - var9; var10 <= var1 + var9; ++var10) {
							int var12 = var10 - var1;

							for(var6 = var3 - var9; var6 <= var3 + var9; ++var6) {
								int var11 = var6 - var3;
								if((Math.abs(var12) != var9 || Math.abs(var11) != var9 || this.random.nextInt(2) != 0 && var8 != 0) && !Block.opaqueCubeLookup[this.getBlockId(var10, var13, var6)]) {
									this.setBlockWithNotify(var10, var13, var6, Block.leaves.blockID);
								}
							}
						}
					}

					for(var13 = 0; var13 < var4; ++var13) {
						if(!Block.opaqueCubeLookup[this.getBlockId(var1, var2 + var13, var3)]) {
							this.setBlockWithNotify(var1, var2 + var13, var3, Block.wood.blockID);
						}
					}

					return true;
				} else {
					return false;
				}
			}
		} else {
			return false;
		}
	}

	public final Entity getPlayerEntity() {
		return this.playerEntity;
	}

	public final void spawnEntityInWorld(Entity var1) {
		this.entityMap.insert(var1);
		var1.setWorld(this);

		for(int var2 = 0; var2 < this.worldAccesses.size(); ++var2) {
			((IWorldAccess)this.worldAccesses.get(var2)).obtainEntitySkin(var1);
		}

	}

	public final void releaseEntitySkin(Entity var1) {
		this.entityMap.remove(var1);

		for(int var2 = 0; var2 < this.worldAccesses.size(); ++var2) {
			((IWorldAccess)this.worldAccesses.get(var2)).releaseEntitySkin(var1);
		}

	}

	public final void createExplosion(Entity var1, float var2, float var3, float var4, float var5) {
		this.playSoundAtPlayer(var2, var3, var4, "random.explode", 4.0F, (1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F) * 0.7F);
		TreeSet var6 = new TreeSet();
		float var7 = var5;

		int var8;
		int var9;
		int var10;
		float var11;
		float var18;
		int var19;
		int var20;
		int var21;
		int var22;
		for(var8 = 0; var8 < 16; ++var8) {
			for(var9 = 0; var9 < 16; ++var9) {
				for(var10 = 0; var10 < 16; ++var10) {
					if(var8 == 0 || var8 == 15 || var9 == 0 || var9 == 15 || var10 == 0 || var10 == 15) {
						var11 = (float)var8 / 15.0F * 2.0F - 1.0F;
						float var12 = (float)var9 / 15.0F * 2.0F - 1.0F;
						float var13 = (float)var10 / 15.0F * 2.0F - 1.0F;
						float var14 = (float)Math.sqrt((double)(var11 * var11 + var12 * var12 + var13 * var13));
						var11 /= var14;
						var12 /= var14;
						var13 /= var14;
						float var15 = var5 * (0.7F + this.random.nextFloat() * 0.6F);
						float var16 = var2;
						float var17 = var3;

						for(var18 = var4; var15 > 0.0F; var15 -= 0.22500001F) {
							var19 = (int)var16;
							var20 = (int)var17;
							var21 = (int)var18;
							var22 = this.getBlockId(var19, var20, var21);
							if(var22 > 0) {
								var15 -= (Block.blocksList[var22].getExplosionResistance() + 0.3F) * 0.3F;
							}

							if(var15 > 0.0F) {
								int var23 = var19 + (var20 << 10) + (var21 << 10 << 10);
								var6.add(Integer.valueOf(var23));
							}

							var16 += var11 * 0.3F;
							var17 += var12 * 0.3F;
							var18 += var13 * 0.3F;
						}
					}
				}
			}
		}

		var5 *= 2.0F;
		var8 = (int)(var2 - var5 - 1.0F);
		var9 = (int)(var2 + var5 + 1.0F);
		var10 = (int)(var3 - var5 - 1.0F);
		int var29 = (int)(var3 + var5 + 1.0F);
		int var30 = (int)(var4 - var5 - 1.0F);
		int var31 = (int)(var4 + var5 + 1.0F);
		List var32 = this.entityMap.getEntities(var1, (float)var8, (float)var10, (float)var30, (float)var9, (float)var29, (float)var31);
		Vec3D var33 = new Vec3D(var2, var3, var4);

		float var27;
		float var28;
		float var43;
		for(int var34 = 0; var34 < var32.size(); ++var34) {
			Entity var36 = (Entity)var32.get(var34);
			var27 = var36.posX - var2;
			var28 = var36.posY - var3;
			float var26 = var36.posZ - var4;
			var18 = MathHelper.sqrt_float(var27 * var27 + var28 * var28 + var26 * var26) / var5;
			if(var18 <= 1.0F) {
				var26 = var36.posX - var2;
				float var39 = var36.posY - var3;
				float var40 = var36.posZ - var4;
				float var41 = MathHelper.sqrt_float(var26 * var26 + var39 * var39 + var40 * var40);
				var26 /= var41;
				var39 /= var41;
				var40 /= var41;
				float var42 = this.getBlockDensity(var33, var36.boundingBox);
				var43 = (1.0F - var18) * var42;
				var36.attackEntityFrom(var1, (int)((var43 * var43 + var43) / 2.0F * 8.0F * var5 + 1.0F));
				var36.motionX += var26 * var43;
				var36.motionY += var39 * var43;
				var36.motionZ += var40 * var43;
			}
		}

		var5 = var7;
		ArrayList var35 = new ArrayList();
		var35.addAll(var6);

		for(int var37 = var35.size() - 1; var37 >= 0; --var37) {
			int var38 = ((Integer)var35.get(var37)).intValue();
			var8 = var38 & 1023;
			var19 = var38 >> 10 & 1023;
			var20 = var38 >> 20 & 1023;
			if(var8 >= 0 && var19 >= 0 && var20 >= 0 && var8 < this.width && var19 < this.height && var20 < this.length) {
				var21 = this.getBlockId(var8, var19, var20);

				for(var22 = 0; var22 <= 0; ++var22) {
					var43 = (float)var8 + this.random.nextFloat();
					var27 = (float)var19 + this.random.nextFloat();
					float var24 = (float)var20 + this.random.nextFloat();
					float var25 = var43 - var2;
					var7 = var27 - var3;
					var28 = var24 - var4;
					var11 = MathHelper.sqrt_float(var25 * var25 + var7 * var7 + var28 * var28);
					var25 /= var11;
					var7 /= var11;
					var28 /= var11;
					var11 = 0.5F / (var11 / var5 + 0.1F);
					var11 *= this.random.nextFloat() * this.random.nextFloat() + 0.3F;
					var25 *= var11;
					var7 *= var11;
					var28 *= var11;
					this.spawnParticle("explode", (var43 + var2) / 2.0F, (var27 + var3) / 2.0F, (var24 + var4) / 2.0F, var25, var7, var28);
					this.spawnParticle("smoke", var43, var27, var24, var25, var7, var28);
				}

				if(var21 > 0) {
					Block.blocksList[var21].dropBlockAsItemWithChance(this, var8, var19, var20, this.getBlockMetadata(var8, var19, var20), 0.3F);
					this.setBlockWithNotify(var8, var19, var20, 0);
					Block.blocksList[var21].onBlockDestroyedByExplosion(this, var8, var19, var20);
				}
			}
		}

	}

	private float getBlockDensity(Vec3D var1, AxisAlignedBB var2) {
		float var3 = 1.0F / ((var2.maxX - var2.minX) * 2.0F + 1.0F);
		float var4 = 1.0F / ((var2.maxY - var2.minY) * 2.0F + 1.0F);
		float var5 = 1.0F / ((var2.maxZ - var2.minZ) * 2.0F + 1.0F);
		int var6 = 0;
		int var7 = 0;

		for(float var8 = 0.0F; var8 <= 1.0F; var8 += var3) {
			for(float var9 = 0.0F; var9 <= 1.0F; var9 += var4) {
				for(float var10 = 0.0F; var10 <= 1.0F; var10 += var5) {
					float var11 = var2.minX + (var2.maxX - var2.minX) * var8;
					float var12 = var2.minY + (var2.maxY - var2.minY) * var9;
					float var13 = var2.minZ + (var2.maxZ - var2.minZ) * var10;
					if(this.rayTraceBlocks(new Vec3D(var11, var12, var13), var1) == null) {
						++var6;
					}

					++var7;
				}
			}
		}

		return (float)var6 / (float)var7;
	}

	public final Entity findSubclassOf(Class var1) {
		for(int var2 = 0; var2 < this.entityMap.entities.size(); ++var2) {
			Entity var3 = (Entity)this.entityMap.entities.get(var2);
			if(var1.isAssignableFrom(var3.getClass())) {
				return var3;
			}
		}

		return null;
	}

	public final int fluidFlowCheck(int var1, int var2, int var3, int var4, int var5) {
		if(var1 >= 0 && var2 >= 0 && var3 >= 0 && var1 < this.width && var2 < this.height && var3 < this.length) {
			int var6 = var1;
			int var7 = var3;
			int var8 = ((var2 << 10) + var3 << 10) + var1;
			byte var9 = 0;
			int var20 = var9 + 1;
			this.coords[0] = var1 + (var3 << 10);
			int var11 = -9999;
			if(var4 == Block.waterStill.blockID || var4 == Block.waterMoving.blockID) {
				var11 = Block.waterSource.blockID;
			}

			if(var4 == Block.lavaStill.blockID || var4 == Block.lavaMoving.blockID) {
				var11 = Block.lavaSource.blockID;
			}

			int var10;
			boolean var12;
			label170:
			do {
				var12 = false;
				int var13 = -1;
				var10 = 0;
				if(++floodFillCounter == 30000) {
					Arrays.fill(this.floodFillCounters, (short)0);
					floodFillCounter = 1;
				}

				while(true) {
					int var14;
					do {
						if(var20 <= 0) {
							++var2;
							int[] var21 = this.floodedBlocks;
							this.floodedBlocks = this.coords;
							this.coords = var21;
							var20 = var10;
							continue label170;
						}

						--var20;
						var14 = this.coords[var20];
					} while(this.floodFillCounters[var14] == floodFillCounter);

					var1 = var14 % 1024;
					var3 = var14 / 1024;
					int var15 = var3 - var7;

					for(var15 *= var15; var1 > 0 && this.floodFillCounters[var14 - 1] != floodFillCounter && (this.blocks[(var2 * this.length + var3) * this.width + var1 - 1] == var4 || this.blocks[(var2 * this.length + var3) * this.width + var1 - 1] == var5); --var14) {
						--var1;
					}

					if(var1 > 0 && this.blocks[(var2 * this.length + var3) * this.width + var1 - 1] == var11) {
						var12 = true;
					}

					boolean var16 = false;
					boolean var17 = false;

					for(boolean var18 = false; var1 < this.width && this.floodFillCounters[var14] != floodFillCounter && (this.blocks[(var2 * this.length + var3) * this.width + var1] == var4 || this.blocks[(var2 * this.length + var3) * this.width + var1] == var5); ++var1) {
						byte var19;
						boolean var22;
						if(var3 > 0) {
							var19 = this.blocks[(var2 * this.length + var3 - 1) * this.width + var1];
							if(var19 == var11) {
								var12 = true;
							}

							var22 = this.floodFillCounters[var14 - 1024] != floodFillCounter && (var19 == var4 || var19 == var5);
							if(var22 && !var16) {
								this.coords[var20++] = var14 - 1024;
							}

							var16 = var22;
						}

						if(var3 < this.length - 1) {
							var19 = this.blocks[(var2 * this.length + var3 + 1) * this.width + var1];
							if(var19 == var11) {
								var12 = true;
							}

							var22 = this.floodFillCounters[var14 + 1024] != floodFillCounter && (var19 == var4 || var19 == var5);
							if(var22 && !var17) {
								this.coords[var20++] = var14 + 1024;
							}

							var17 = var22;
						}

						if(var2 < this.height - 1) {
							var19 = this.blocks[((var2 + 1) * this.length + var3) * this.width + var1];
							var22 = var19 == var4 || var19 == var5;
							if(var22 && !var18) {
								this.floodedBlocks[var10++] = var14;
							}

							var18 = var22;
						}

						int var23 = var1 - var6;
						var23 *= var23;
						var23 += var15;
						if(var23 > var13) {
							var13 = var23;
							var8 = ((var2 << 10) + var3 << 10) + var1;
						}

						this.floodFillCounters[var14++] = floodFillCounter;
					}

					if(var1 < this.width && this.blocks[(var2 * this.length + var3) * this.width + var1] == var11) {
						var12 = true;
					}
				}
			} while(var10 > 0);

			if(var12) {
				return -9999;
			} else {
				return var8;
			}
		} else {
			return -1;
		}
	}

	public final int floodFill(int var1, int var2, int var3, int var4, int var5) {
		if(var1 >= 0 && var2 >= 0 && var3 >= 0 && var1 < this.width && var2 < this.height && var3 < this.length) {
			if(++floodFillCounter == 30000) {
				Arrays.fill(this.floodFillCounters, (short)0);
				floodFillCounter = 1;
			}

			byte var6 = 0;
			int var11 = var6 + 1;
			this.coords[0] = var1 + (var3 << 10);

			do {
				int var7;
				do {
					if(var11 <= 0) {
						return 1;
					}

					--var11;
					var7 = this.coords[var11];
				} while(this.floodFillCounters[var7] == floodFillCounter);

				var1 = var7 % 1024;
				var3 = var7 / 1024;
				if(var1 == 0 || var1 == this.width - 1 || var2 == 0 || var2 == this.height - 1 || var3 == 0 || var3 == this.length - 1) {
					return 2;
				}

				while(var1 > 0 && this.floodFillCounters[var7 - 1] != floodFillCounter && (this.blocks[(var2 * this.length + var3) * this.width + var1 - 1] == var4 || this.blocks[(var2 * this.length + var3) * this.width + var1 - 1] == var5)) {
					--var1;
					--var7;
				}

				if(var1 > 0 && this.blocks[(var2 * this.length + var3) * this.width + var1 - 1] == 0) {
					return 0;
				}

				boolean var8 = false;

				for(boolean var9 = false; var1 < this.width && this.floodFillCounters[var7] != floodFillCounter && (this.blocks[(var2 * this.length + var3) * this.width + var1] == var4 || this.blocks[(var2 * this.length + var3) * this.width + var1] == var5); ++var1) {
					if(var1 == 0 || var1 == this.width - 1) {
						return 2;
					}

					byte var10;
					boolean var12;
					if(var3 > 0) {
						var10 = this.blocks[(var2 * this.length + var3 - 1) * this.width + var1];
						if(var10 == 0) {
							return 0;
						}

						var12 = this.floodFillCounters[var7 - 1024] != floodFillCounter && (var10 == var4 || var10 == var5);
						if(var12 && !var8) {
							this.coords[var11++] = var7 - 1024;
						}

						var8 = var12;
					}

					if(var3 < this.length - 1) {
						var10 = this.blocks[(var2 * this.length + var3 + 1) * this.width + var1];
						if(var10 == 0) {
							return 0;
						}

						var12 = this.floodFillCounters[var7 + 1024] != floodFillCounter && (var10 == var4 || var10 == var5);
						if(var12 && !var9) {
							this.coords[var11++] = var7 + 1024;
						}

						var9 = var12;
					}

					this.floodFillCounters[var7] = floodFillCounter;
					++var7;
				}
			} while(var1 >= this.width || this.blocks[(var2 * this.length + var3) * this.width + var1] != 0);

			return 0;
		} else {
			return 0;
		}
	}

	public final void playSoundAtEntity(Entity var1, String var2, float var3, float var4) {
		for(int var5 = 0; var5 < this.worldAccesses.size(); ++var5) {
			float var6 = 16.0F;
			if(var3 > 1.0F) {
				var6 = 16.0F * var3;
			}

			if(this.playerEntity.getDistanceSqToEntity(var1) < var6 * var6) {
				((IWorldAccess)this.worldAccesses.get(var5)).playSound(var2, var1.posX, var1.posY - var1.yOffset, var1.posZ, var3, var4);
			}
		}

	}

	public final void playSoundEffect(float var1, float var2, float var3, String var4, float var5) {
		try {
			for(int var7 = 0; var7 < this.worldAccesses.size(); ++var7) {
				((IWorldAccess)this.worldAccesses.get(var7)).playMusic(var4, var1, var2, var3, 0.0F);
			}

		} catch (Exception var6) {
			var6.printStackTrace();
		}
	}

	public final void playSoundAtPlayer(float var1, float var2, float var3, String var4, float var5, float var6) {
		try {
			for(int var7 = 0; var7 < this.worldAccesses.size(); ++var7) {
				float var8 = 16.0F;
				if(var5 > 1.0F) {
					var8 = 16.0F * var5;
				}

				float var9 = var1 - this.playerEntity.posX;
				float var10 = var2 - this.playerEntity.posY;
				float var11 = var3 - this.playerEntity.posZ;
				if(var9 * var9 + var10 * var10 + var11 * var11 < var8 * var8) {
					((IWorldAccess)this.worldAccesses.get(var7)).playSound(var4, var1, var2, var3, var5, var6);
				}
			}

		} catch (Exception var12) {
			var12.printStackTrace();
		}
	}

	public final void extinguishFire(int var1, int var2, int var3, int var4) {
		if(var4 == 0) {
			--var2;
		}

		if(var4 == 1) {
			++var2;
		}

		if(var4 == 2) {
			--var3;
		}

		if(var4 == 3) {
			++var3;
		}

		if(var4 == 4) {
			--var1;
		}

		if(var4 == 5) {
			++var1;
		}

		if(this.getBlockId(var1, var2, var3) == Block.fire.blockID) {
			this.playSoundAtPlayer((float)var1 + 0.5F, (float)var2 + 0.5F, (float)var3 + 0.5F, "random.fizz", 0.5F, 2.6F + (this.random.nextFloat() - this.random.nextFloat()) * 0.8F);
			this.setBlockWithNotify(var1, var2, var3, 0);
		}

	}

	public final void setBlockTileEntity(int var1, int var2, int var3, TileEntity var4) {
		var4.worldObj = this;
		var4.xCoord = var1;
		var4.yCoord = var2;
		var4.zCoord = var3;
		this.map.put(Integer.valueOf(var1 + (var2 << 10) + (var3 << 10 << 10)), var4);
		this.list.add(var4);
	}

	public final void removeBlockTileEntity(int var1, int var2, int var3) {
		this.list.remove(this.map.remove(Integer.valueOf(var1 + (var2 << 10) + (var3 << 10 << 10))));
	}

	public final TileEntity getBlockTileEntity(int var1, int var2, int var3) {
		int var4 = var1 + (var2 << 10) + (var3 << 10 << 10);
		TileEntity var5 = (TileEntity)this.map.get(Integer.valueOf(var4));
		if(var5 == null) {
			int var6 = this.getBlockId(var1, var2, var3);
			BlockContainer var7 = (BlockContainer)Block.blocksList[var6];
			var7.onBlockAdded(this, var1, var2, var3);
			var5 = (TileEntity)this.map.get(Integer.valueOf(var4));
		}

		return var5;
	}

	public final void spawnParticle(String var1, float var2, float var3, float var4, float var5, float var6, float var7) {
		for(int var8 = 0; var8 < this.worldAccesses.size(); ++var8) {
			((IWorldAccess)this.worldAccesses.get(var8)).spawnParticle(var1, var2, var3, var4, var5, var6, var7);
		}

	}

	public final void randomDisplayUpdates(int var1, int var2, int var3) {
		for(int var4 = 0; var4 < 1000; ++var4) {
			int var5 = var1 + this.random.nextInt(16) - this.random.nextInt(16);
			int var6 = var2 + this.random.nextInt(16) - this.random.nextInt(16);
			int var7 = var3 + this.random.nextInt(16) - this.random.nextInt(16);
			int var8 = this.getBlockId(var5, var6, var7);
			if(var8 > 0) {
				Block.blocksList[var8].randomDisplayTick(this, var5, var6, var7, this.rand);
			}
		}

	}

	public final String debugSkylightUpdates() {
		return "" + this.tickList.size() + ". L: " + this.lightUpdates.debugLightUpdates();
	}

	public final void setLevel() {
		for(int var1 = 0; var1 < this.worldAccesses.size(); ++var1) {
			IWorldAccess var2 = (IWorldAccess)this.worldAccesses.get(var1);

			for(int var3 = 0; var3 < this.entityMap.entities.size(); ++var3) {
				var2.releaseEntitySkin((Entity)this.entityMap.entities.get(var1));
			}
		}

	}

	private void updateChunkLight(int var1) {
		this.lightUpdates.updateDaylightCycle(var1);
	}

	public final boolean canBlockSeeTheSky(int var1, int var2, int var3) {
		if(this.heightMap[var1 + var3 * this.width] <= var2) {
			return true;
		} else {
			while(var2 < this.height) {
				if(Block.opaqueCubeLookup[this.getBlockId(var1, var2, var3)]) {
					return false;
				}

				++var2;
			}

			return true;
		}
	}

	static {
		for(int var0 = 0; var0 <= 15; ++var0) {
			float var1 = 1.0F - (float)var0 / 15.0F;
			lightBrightnessTable[var0] = (1.0F - var1) / (var1 * 3.0F + 1.0F) * 0.95F + 0.05F;
		}

		floodFillCounter = 0;
	}
}
