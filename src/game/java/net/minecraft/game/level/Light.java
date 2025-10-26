package net.minecraft.game.level;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.game.level.block.Block;

public final class Light {
	private int lightingUpdateCounter = 0;
	private List lightingUpdateList = new ArrayList();
	private World worldObj;
	private int worldWidth;
	private int worldLength;
	private int worldHeight;
	private byte[] blocks;
	private byte[] data;
	private int[] heightMap;
	private List skyLightList = new ArrayList();
	private List blockLightList = new ArrayList();
	private List lightingUpdateList1 = new ArrayList();
	private List lightingUpdateList2 = new ArrayList();
	private int[] lightingUpdateList3 = this.getLightingUpdates();
	private byte[] chunks;
	private MetadataChunkBlock metadataChunkBlock = null;
	private int lightValue = 0;
	private int skylightSubtracted;

	public Light(World var1) {
		this.worldObj = var1;
		this.worldWidth = var1.width;
		this.worldLength = var1.length;
		this.worldHeight = var1.height;
		this.chunks = new byte[var1.blocks.length / 8];
		this.blocks = var1.blocks;
		this.data = var1.data;
		this.heightMap = var1.heightMap;
	}

	private int[] getLightingUpdates() {
		return this.lightingUpdateList2.size() > 0 ? (int[])this.lightingUpdateList2.remove(this.lightingUpdateList2.size() - 1) : new int[-Short.MIN_VALUE];
	}

	public final void updateSkylight(int var1, int var2, int var3, int var4) {
		this.lightingUpdateList1.add(new MetadataChunkBlock(this, var1, var2, 0, var3, var4, 1));
	}

	public final void updateDaylightCycle(int var1) {
		if(var1 > 15) {
			var1 = 15;
		}

		if(var1 < 0) {
			var1 = 0;
		}

		this.skylightSubtracted = var1 - this.worldObj.skylightSubtracted;
		if(this.skylightSubtracted != 0) {
			this.lightValue = this.worldObj.skylightSubtracted;
			this.worldObj.skylightSubtracted = var1;

			while(this.metadataChunkBlock != null) {
				this.updateLight(64);
			}

			this.metadataChunkBlock = new MetadataChunkBlock(this, 0, 0, 0, this.worldObj.width, this.worldObj.height, this.worldObj.length);
		}
	}

	private void updateLight(int var1) {
		int var2 = this.metadataChunkBlock.x;
		int var3 = this.metadataChunkBlock.maxX;
		int var4 = this.metadataChunkBlock.z;
		int var5 = this.metadataChunkBlock.maxZ;

		int var6;
		int var7;
		for(var2 = var2; var2 < var3; ++var2) {
			if(var1-- <= 0 && var2 != var3 - 1) {
				this.metadataChunkBlock.x = var2;
				return;
			}

			for(var6 = var4; var6 < var5; ++var6) {
				for(var7 = this.heightMap[var2 + var6 * this.worldWidth] - 1; var7 > 0 && Block.lightOpacity[this.blocks[(var7 * this.worldLength + var6) * this.worldWidth + var2]] < 100; --var7) {
				}

				++var7;

				for(; var7 < this.worldHeight; ++var7) {
					int var8 = (var7 * this.worldLength + var6) * this.worldWidth + var2;
					if(Block.lightValue[this.blocks[var8]] == 0) {
						int var9 = this.data[var8] & 15;
						if(var9 <= this.lightValue) {
							if(this.skylightSubtracted < 0 && var9 > 0) {
								--this.data[var8];
							} else if(this.skylightSubtracted > 0 && var9 < 15) {
								++this.data[var8];
							}
						}
					}
				}
			}
		}

		for(var6 = 0; var6 < this.worldWidth; var6 += 32) {
			for(var7 = 0; var7 < this.worldLength; var7 += 32) {
				this.blockLightList.add(new MetadataChunkBlock(this, var6, 0, var7, var6 + 32, this.worldHeight, var7 + 32));
				this.skyLightList.add(new MetadataChunkBlock(this, var6, 0, var7, var6 + 32, this.worldHeight, var7 + 32));
			}
		}

		for(var6 = 0; var6 < this.worldObj.worldAccesses.size(); ++var6) {
			IWorldAccess var10 = (IWorldAccess)this.worldObj.worldAccesses.get(var6);
			var10.updateAllRenderers();
		}

		this.metadataChunkBlock = null;
	}

	public final void updateBlockLight(int var1, int var2, int var3, int var4, int var5, int var6) {
		this.blockLightList.add(new MetadataChunkBlock(this, var1, var2, var3, var4, var5, var6));
	}

	private void updateLists(int var1, int var2, int var3, int var4, int var5, int var6) {
		for(var2 = var2; var2 < var5; ++var2) {
			for(int var7 = var3; var7 < var6; ++var7) {
				for(int var8 = var1; var8 < var4; ++var8) {
					int var9 = var8 + var2 * this.worldWidth + var7 * this.worldWidth * this.worldHeight;
					if((this.chunks[var9 >> 3] & 1 << (var9 & 7)) == 0) {
						this.chunks[var9 >> 3] = (byte)(this.chunks[var9 >> 3] | 1 << (var9 & 7));
						this.lightingUpdateList3[this.lightingUpdateCounter++] = var9;
						if((this.chunks[var9 >> 3] & 1 << (var9 & 7)) == 0) {
							System.out.println("OMG ERROR!");
						}

						if(this.lightingUpdateCounter > this.lightingUpdateList3.length - 32) {
							var9 = this.lightingUpdateList3[--this.lightingUpdateCounter];
							this.lightingUpdateList3[this.lightingUpdateList3.length - 1] = this.lightingUpdateCounter;
							this.lightingUpdateList.add(this.lightingUpdateList3);
							this.lightingUpdateList3 = this.getLightingUpdates();
							this.lightingUpdateCounter = 1;
							this.lightingUpdateList3[0] = var9;
						}
					}
				}
			}
		}

	}

	public final void updateLight() {
		if(this.lightingUpdateList2.size() > 0) {
			this.lightingUpdateList2.remove(this.lightingUpdateList2.size() - 1);
		}

		int var1 = 5;

		while(this.skyLightList.size() > 0 && var1-- > 0) {
			MetadataChunkBlock var2 = (MetadataChunkBlock)this.skyLightList.remove(0);

			for(int var3 = 0; var3 < this.worldObj.worldAccesses.size(); ++var3) {
				((IWorldAccess)this.worldObj.worldAccesses.get(var3)).markBlockRangeNeedsUpdate(var2.x, var2.y, var2.z, var2.maxX, var2.maxY, var2.maxZ);
			}
		}

		if(this.metadataChunkBlock != null) {
			this.updateLight(8);
		} else {
			for(int var19 = 0; var19 < 16; ++var19) {
				boolean var20 = false;
				MetadataChunkBlock var21;
				if(this.blockLightList.size() > 0) {
					var20 = true;
					var21 = (MetadataChunkBlock)this.blockLightList.remove(0);
					this.updateLists(var21.x, var21.y, var21.z, var21.maxX, var21.maxY, var21.maxZ);
				}

				int var4;
				int var5;
				int var6;
				int var7;
				int var8;
				int var9;
				int var10;
				int var11;
				int var12;
				int var13;
				Light var22;
				if(this.lightingUpdateList1.size() > 0) {
					var20 = true;
					var21 = (MetadataChunkBlock)this.lightingUpdateList1.remove(0);
					var7 = var21.maxY;
					var6 = var21.maxX;
					var5 = var21.y;
					var4 = var21.x;
					var22 = this;

					for(var8 = var4; var8 < var4 + var6; ++var8) {
						for(var9 = var5; var9 < var5 + var7; ++var9) {
							var10 = var22.heightMap[var8 + var9 * var22.worldWidth];

							for(var11 = var22.worldHeight - 1; var11 > 0 && Block.lightOpacity[var22.blocks[(var11 * var22.worldLength + var9) * var22.worldWidth + var8]] == 0; --var11) {
							}

							var22.heightMap[var8 + var9 * var22.worldWidth] = var11 + 1;
							if(var10 != var11) {
								var12 = var10 < var11 ? var10 : var11;
								var13 = var10 > var11 ? var10 : var11;
								var22.updateLists(var8, var12, var9, var8 + 1, var13, var9 + 1);
							}
						}
					}
				}

				var22 = this;
				var4 = this.worldObj.skylightSubtracted;
				var5 = -999;
				var6 = -999;
				var7 = -999;
				var8 = -999;
				var9 = -999;
				var10 = -999;
				var11 = 1024;
				var12 = 0;

				while(var11-- > 0 && (var22.lightingUpdateCounter > 0 || var22.lightingUpdateList.size() > 0)) {
					++var12;
					if(var22.lightingUpdateCounter == 0) {
						if(var22.lightingUpdateList3 != null) {
							int[] var23 = var22.lightingUpdateList3;
							var22.lightingUpdateList2.add(var23);
						}

						var22.lightingUpdateList3 = (int[])var22.lightingUpdateList.remove(var22.lightingUpdateList.size() - 1);
						var22.lightingUpdateCounter = var22.lightingUpdateList3[var22.lightingUpdateList3.length - 1];
					}

					if(var22.lightingUpdateCounter > var22.lightingUpdateList3.length - 32) {
						var13 = var22.lightingUpdateList3[--var22.lightingUpdateCounter];
						var22.lightingUpdateList3[var22.lightingUpdateList3.length - 1] = var22.lightingUpdateCounter;
						var22.lightingUpdateList.add(var22.lightingUpdateList3);
						var22.lightingUpdateList3 = var22.getLightingUpdates();
						var22.lightingUpdateCounter = 1;
						var22.lightingUpdateList3[0] = var13;
					} else {
						var13 = var22.lightingUpdateList3[--var22.lightingUpdateCounter];
						var1 = var13 % var22.worldWidth;
						int var14 = var13 / var22.worldWidth % var22.worldHeight;
						int var15 = var13 / var22.worldWidth / var22.worldHeight % var22.worldLength;
						var22.chunks[var13 >> 3] = (byte)(var22.chunks[var13 >> 3] ^ 1 << (var13 & 7));
						var13 = var22.heightMap[var1 + var15 * var22.worldWidth];
						int var16 = var14 >= var13 ? var4 : 0;
						byte var24 = var22.blocks[(var14 * var22.worldLength + var15) * var22.worldWidth + var1];
						int var17 = Block.lightOpacity[var24];
						if(var17 > 100) {
							var16 = 0;
						} else if(var16 < 15) {
							var17 = var17;
							if(var17 == 0) {
								var17 = 1;
							}

							int var18;
							if(var1 > 0) {
								var18 = (var22.data[(var14 * var22.worldLength + var15) * var22.worldWidth + (var1 - 1)] & 15) - var17;
								if(var18 > var16) {
									var16 = var18;
								}
							}

							if(var1 < var22.worldWidth - 1) {
								var18 = (var22.data[(var14 * var22.worldLength + var15) * var22.worldWidth + var1 + 1] & 15) - var17;
								if(var18 > var16) {
									var16 = var18;
								}
							}

							if(var14 > 0) {
								var18 = (var22.data[((var14 - 1) * var22.worldLength + var15) * var22.worldWidth + var1] & 15) - var17;
								if(var18 > var16) {
									var16 = var18;
								}
							}

							if(var14 < var22.worldHeight - 1) {
								var18 = (var22.data[((var14 + 1) * var22.worldLength + var15) * var22.worldWidth + var1] & 15) - var17;
								if(var18 > var16) {
									var16 = var18;
								}
							}

							if(var15 > 0) {
								var18 = (var22.data[(var14 * var22.worldLength + (var15 - 1)) * var22.worldWidth + var1] & 15) - var17;
								if(var18 > var16) {
									var16 = var18;
								}
							}

							if(var15 < var22.worldLength - 1) {
								var18 = (var22.data[(var14 * var22.worldLength + var15 + 1) * var22.worldWidth + var1] & 15) - var17;
								if(var18 > var16) {
									var16 = var18;
								}
							}
						}

						if(var16 < Block.lightValue[var24]) {
							var16 = Block.lightValue[var24];
						}

						var17 = var22.data[(var14 * var22.worldLength + var15) * var22.worldWidth + var1] & 15;
						if(var17 != var16) {
							var22.data[(var14 * var22.worldLength + var15) * var22.worldWidth + var1] = (byte)((var22.data[(var14 * var22.worldLength + var15) * var22.worldWidth + var1] & 240) + var16);
							if(var1 > 0 && (var22.data[(var14 * var22.worldLength + var15) * var22.worldWidth + (var1 - 1)] & 15) != var16 - 1) {
								var13 = var1 - 1 + var14 * var22.worldWidth + var15 * var22.worldWidth * var22.worldHeight;
								if((var22.chunks[var13 >> 3] & 1 << (var13 & 7)) == 0) {
									var22.chunks[var13 >> 3] = (byte)(var22.chunks[var13 >> 3] | 1 << (var13 & 7));
									var22.lightingUpdateList3[var22.lightingUpdateCounter++] = var13;
								}
							}

							if(var1 < var22.worldWidth - 1 && (var22.data[(var14 * var22.worldLength + var15) * var22.worldWidth + var1 + 1] & 15) != var16 - 1) {
								var13 = var1 + 1 + var14 * var22.worldWidth + var15 * var22.worldWidth * var22.worldHeight;
								if((var22.chunks[var13 >> 3] & 1 << (var13 & 7)) == 0) {
									var22.chunks[var13 >> 3] = (byte)(var22.chunks[var13 >> 3] | 1 << (var13 & 7));
									var22.lightingUpdateList3[var22.lightingUpdateCounter++] = var13;
								}
							}

							if(var14 > 0 && (var22.data[((var14 - 1) * var22.worldLength + var15) * var22.worldWidth + var1] & 15) != var16 - 1) {
								var13 = var1 + (var14 - 1) * var22.worldWidth + var15 * var22.worldWidth * var22.worldHeight;
								if((var22.chunks[var13 >> 3] & 1 << (var13 & 7)) == 0) {
									var22.chunks[var13 >> 3] = (byte)(var22.chunks[var13 >> 3] | 1 << (var13 & 7));
									var22.lightingUpdateList3[var22.lightingUpdateCounter++] = var13;
								}
							}

							if(var14 < var22.worldHeight - 1 && (var22.data[((var14 + 1) * var22.worldLength + var15) * var22.worldWidth + var1] & 15) != var16 - 1) {
								var13 = var1 + (var14 + 1) * var22.worldWidth + var15 * var22.worldWidth * var22.worldHeight;
								if((var22.chunks[var13 >> 3] & 1 << (var13 & 7)) == 0) {
									var22.chunks[var13 >> 3] = (byte)(var22.chunks[var13 >> 3] | 1 << (var13 & 7));
									var22.lightingUpdateList3[var22.lightingUpdateCounter++] = var13;
								}
							}

							if(var15 > 0 && (var22.data[(var14 * var22.worldLength + (var15 - 1)) * var22.worldWidth + var1] & 15) != var16 - 1) {
								var13 = var1 + var14 * var22.worldWidth + (var15 - 1) * var22.worldWidth * var22.worldHeight;
								if((var22.chunks[var13 >> 3] & 1 << (var13 & 7)) == 0) {
									var22.chunks[var13 >> 3] = (byte)(var22.chunks[var13 >> 3] | 1 << (var13 & 7));
									var22.lightingUpdateList3[var22.lightingUpdateCounter++] = var13;
								}
							}

							if(var15 < var22.worldLength - 1 && (var22.data[(var14 * var22.worldLength + var15 + 1) * var22.worldWidth + var1] & 15) != var16 - 1) {
								var13 = var1 + var14 * var22.worldWidth + (var15 + 1) * var22.worldWidth * var22.worldHeight;
								if((var22.chunks[var13 >> 3] & 1 << (var13 & 7)) == 0) {
									var22.chunks[var13 >> 3] = (byte)(var22.chunks[var13 >> 3] | 1 << (var13 & 7));
									var22.lightingUpdateList3[var22.lightingUpdateCounter++] = var13;
								}
							}

							if(var5 == -999) {
								var5 = var1;
								var6 = var1;
								var7 = var14;
								var8 = var14;
								var9 = var15;
								var10 = var15;
							}

							if(var1 < var5) {
								var5 = var1;
							} else if(var1 > var6) {
								var6 = var1;
							}

							if(var14 > var8) {
								var8 = var14;
							} else if(var14 < var7) {
								var7 = var14;
							}

							if(var15 < var9) {
								var9 = var15;
							} else if(var15 > var10) {
								var10 = var15;
							}
						}
					}
				}

				if(var5 > -999) {
					var22.skyLightList.add(new MetadataChunkBlock(var22, var5, var7, var9, var6, var8, var10));
				}

				if(var12 > 0) {
					var20 = true;
				}
			}

		}
	}

	public final String debugLightUpdates() {
		return "" + (this.blockLightList.size() + this.skyLightList.size());
	}
}
