package net.minecraft.game.level.generator;

import java.util.ArrayList;
import java.util.Random;
import net.minecraft.game.level.MobSpawner;
import net.minecraft.game.level.World;
import net.minecraft.game.level.block.Block;
import net.minecraft.game.level.block.BlockFlower;
import net.minecraft.game.level.generator.noise.NoiseGeneratorDistort;
import net.minecraft.game.level.generator.noise.NoiseGeneratorOctaves;
import util.IProgressUpdate;
import util.MathHelper;

public final class LevelGenerator {
	private IProgressUpdate guiLoading;
	private int width;
	private int depth;
	private int height;
	private Random rand = new Random();
	private byte[] blocksByteArray;
	private int waterLevel;
	private int groundLevel;
	public boolean islandGen = false;
	public boolean floatingGen = false;
	public boolean flatGen = false;
	public int levelType;
	private int phaseBar;
	private int phases;
	private float phaseBareLength = 0.0F;
	private int[] floodFillBlocks = new int[1048576];

	public LevelGenerator(IProgressUpdate var1) {
		this.guiLoading = var1;
	}

	public final World generate(String var1, int var2, int var3, int var4) {
		int var5 = 1;
		if(this.floatingGen) {
			var5 = (var4 - 64) / 48 + 1;
		}

		this.phases = 13 + var5 * 4;
		this.guiLoading.displayProgressMessage("Generating level");
		World var6 = new World();
		var6.waterLevel = this.waterLevel;
		var6.groundLevel = this.groundLevel;
		this.width = var2;
		this.depth = var3;
		this.height = var4;
		this.blocksByteArray = new byte[var2 * var3 * var4];

		int var7;
		LevelGenerator var9;
		int var21;
		int var25;
		int var31;
		int var45;
		int var51;
		int var52;
		int var53;
		int var56;
		for(var7 = 0; var7 < var5; ++var7) {
			this.waterLevel = var4 - 32 - var7 * 48;
			this.groundLevel = this.waterLevel - 2;
			int[] var8;
			NoiseGeneratorOctaves var13;
			int var22;
			double var32;
			int[] var46;
			if(this.flatGen) {
				var8 = new int[var2 * var3];

				for(var45 = 0; var45 < var8.length; ++var45) {
					var8[var45] = 0;
				}

				this.loadingBar();
				this.loadingBar();
			} else {
				this.guiLoading.displayLoadingString("Raising..");
				this.loadingBar();
				var9 = this;
				NoiseGeneratorDistort var10 = new NoiseGeneratorDistort(new NoiseGeneratorOctaves(this.rand, 8), new NoiseGeneratorOctaves(this.rand, 8));
				NoiseGeneratorDistort var11 = new NoiseGeneratorDistort(new NoiseGeneratorOctaves(this.rand, 8), new NoiseGeneratorOctaves(this.rand, 8));
				NoiseGeneratorOctaves var12 = new NoiseGeneratorOctaves(this.rand, 6);
				var13 = new NoiseGeneratorOctaves(this.rand, 2);
				int[] var14 = new int[this.width * this.depth];
				var22 = 0;

				label349:
				while(true) {
					if(var22 >= var9.width) {
						var8 = var14;
						this.guiLoading.displayLoadingString("Eroding..");
						this.loadingBar();
						var46 = var14;
						var9 = this;
						var11 = new NoiseGeneratorDistort(new NoiseGeneratorOctaves(this.rand, 8), new NoiseGeneratorOctaves(this.rand, 8));
						NoiseGeneratorDistort var50 = new NoiseGeneratorDistort(new NoiseGeneratorOctaves(this.rand, 8), new NoiseGeneratorOctaves(this.rand, 8));
						var52 = 0;

						while(true) {
							if(var52 >= var9.width) {
								break label349;
							}

							var9.setNextPhase((float)var52 * 100.0F / (float)(var9.width - 1));

							for(var53 = 0; var53 < var9.depth; ++var53) {
								double var20 = var11.generateNoise((double)(var52 << 1), (double)(var53 << 1)) / 8.0D;
								var22 = var50.generateNoise((double)(var52 << 1), (double)(var53 << 1)) > 0.0D ? 1 : 0;
								if(var20 > 2.0D) {
									int var58 = var46[var52 + var53 * var9.width];
									var58 = ((var58 - var22) / 2 << 1) + var22;
									var46[var52 + var53 * var9.width] = var58;
								}
							}

							++var52;
						}
					}

					double var23 = Math.abs(((double)var22 / ((double)var9.width - 1.0D) - 0.5D) * 2.0D);
					var9.setNextPhase((float)var22 * 100.0F / (float)(var9.width - 1));

					for(var25 = 0; var25 < var9.depth; ++var25) {
						double var26 = Math.abs(((double)var25 / ((double)var9.depth - 1.0D) - 0.5D) * 2.0D);
						double var28 = var10.generateNoise((double)((float)var22 * 1.3F), (double)((float)var25 * 1.3F)) / 6.0D + -4.0D;
						double var30 = var11.generateNoise((double)((float)var22 * 1.3F), (double)((float)var25 * 1.3F)) / 5.0D + 10.0D + -4.0D;
						var32 = var12.generateNoise((double)var22, (double)var25) / 8.0D;
						if(var32 > 0.0D) {
							var30 = var28;
						}

						double var34 = Math.max(var28, var30) / 2.0D;
						if(var9.islandGen) {
							double var36 = Math.sqrt(var23 * var23 + var26 * var26) * (double)1.2F;
							double var39 = var13.generateNoise((double)((float)var22 * 0.05F), (double)((float)var25 * 0.05F)) / 4.0D + 1.0D;
							var36 = Math.min(var36, var39);
							var36 = Math.max(var36, Math.max(var23, var26));
							if(var36 > 1.0D) {
								var36 = 1.0D;
							}

							if(var36 < 0.0D) {
								var36 = 0.0D;
							}

							var36 *= var36;
							var34 = var34 * (1.0D - var36) - var36 * 10.0D + 5.0D;
							if(var34 < 0.0D) {
								var34 -= var34 * var34 * (double)0.2F;
							}
						} else if(var34 < 0.0D) {
							var34 *= 0.8D;
						}

						var14[var22 + var25 * var9.width] = (int)var34;
					}

					++var22;
				}
			}

			this.guiLoading.displayLoadingString("Soiling..");
			this.loadingBar();
			var46 = var8;
			var9 = this;
			int var49 = this.width;
			var51 = this.depth;
			var52 = this.height;
			NoiseGeneratorOctaves var54 = new NoiseGeneratorOctaves(this.rand, 8);
			NoiseGeneratorOctaves var55 = new NoiseGeneratorOctaves(this.rand, 8);

			for(var21 = 0; var21 < var49; ++var21) {
				double var57 = Math.abs(((double)var21 / ((double)var49 - 1.0D) - 0.5D) * 2.0D);
				var9.setNextPhase((float)var21 * 100.0F / (float)(var49 - 1));

				for(int var24 = 0; var24 < var51; ++var24) {
					double var64 = Math.abs(((double)var24 / ((double)var51 - 1.0D) - 0.5D) * 2.0D);
					double var27 = Math.max(var57, var64);
					var27 = var27 * var27 * var27;
					int var29 = (int)(var54.generateNoise((double)var21, (double)var24) / 24.0D) - 4;
					int var72 = var46[var21 + var24 * var49] + var9.waterLevel;
					var31 = var72 + var29;
					var46[var21 + var24 * var49] = Math.max(var72, var31);
					if(var46[var21 + var24 * var49] > var52 - 2) {
						var46[var21 + var24 * var49] = var52 - 2;
					}

					if(var46[var21 + var24 * var49] <= 0) {
						var46[var21 + var24 * var49] = 1;
					}

					var32 = var55.generateNoise((double)var21 * 2.3D, (double)var24 * 2.3D) / 24.0D;
					int var76 = (int)(Math.sqrt(Math.abs(var32)) * Math.signum(var32) * 20.0D) + var9.waterLevel;
					var76 = (int)((double)var76 * (1.0D - var27) + var27 * (double)var9.height);
					if(var76 > var9.waterLevel) {
						var76 = var9.height;
					}

					for(int var35 = 0; var35 < var52; ++var35) {
						int var79 = (var35 * var9.depth + var24) * var9.width + var21;
						int var37 = 0;
						if(var35 <= var72) {
							var37 = Block.dirt.blockID;
						}

						if(var35 <= var31) {
							var37 = Block.stone.blockID;
						}

						if(var9.floatingGen && var35 < var76) {
							var37 = 0;
						}

						if(var9.blocksByteArray[var79] == 0) {
							var9.blocksByteArray[var79] = (byte)var37;
						}
					}
				}
			}

			this.guiLoading.displayLoadingString("Growing..");
			this.loadingBar();
			var46 = var8;
			var9 = this;
			var49 = this.width;
			var51 = this.depth;
			var13 = new NoiseGeneratorOctaves(this.rand, 8);
			var54 = new NoiseGeneratorOctaves(this.rand, 8);
			var56 = this.waterLevel - 1;
			if(this.levelType == 2) {
				var56 += 2;
			}

			for(var21 = 0; var21 < var49; ++var21) {
				var9.setNextPhase((float)var21 * 100.0F / (float)(var49 - 1));

				for(var22 = 0; var22 < var51; ++var22) {
					boolean var60 = var13.generateNoise((double)var21, (double)var22) > 8.0D;
					if(var9.islandGen) {
						var60 = var13.generateNoise((double)var21, (double)var22) > -8.0D;
					}

					if(var9.levelType == 2) {
						var60 = var13.generateNoise((double)var21, (double)var22) > -32.0D;
					}

					boolean var61 = var54.generateNoise((double)var21, (double)var22) > 12.0D;
					if(var9.levelType == 1 || var9.levelType == 3) {
						var60 = var13.generateNoise((double)var21, (double)var22) > -8.0D;
					}

					var25 = var46[var21 + var22 * var49];
					int var65 = (var25 * var9.depth + var22) * var9.width + var21;
					int var67 = var9.blocksByteArray[((var25 + 1) * var9.depth + var22) * var9.width + var21] & 255;
					if((var67 == Block.waterMoving.blockID || var67 == Block.waterStill.blockID || var67 == 0) && var25 <= var9.waterLevel - 1 && var61) {
						var9.blocksByteArray[var65] = (byte)Block.gravel.blockID;
					}

					if(var67 == 0) {
						int var69 = -1;
						if(var25 <= var56 && var60) {
							var69 = Block.sand.blockID;
							if(var9.levelType == 1) {
								var69 = Block.grass.blockID;
							}
						}

						if(var9.blocksByteArray[var65] != 0 && var69 > 0) {
							var9.blocksByteArray[var65] = (byte)var69;
						}
					}
				}
			}
		}

		this.guiLoading.displayLoadingString("Carving..");
		this.loadingBar();
		var9 = this;
		var51 = this.width;
		var52 = this.depth;
		var53 = this.height;
		var56 = var51 * var52 * var53 / 256 / 64 << 1;

		for(var21 = 0; var21 < var56; ++var21) {
			var9.setNextPhase((float)var21 * 100.0F / (float)(var56 - 1));
			float var59 = var9.rand.nextFloat() * (float)var51;
			float var63 = var9.rand.nextFloat() * (float)var53;
			float var62 = var9.rand.nextFloat() * (float)var52;
			var25 = (int)((var9.rand.nextFloat() + var9.rand.nextFloat()) * 200.0F);
			float var66 = var9.rand.nextFloat() * (float)Math.PI * 2.0F;
			float var68 = 0.0F;
			float var71 = var9.rand.nextFloat() * (float)Math.PI * 2.0F;
			float var70 = 0.0F;
			float var73 = var9.rand.nextFloat() * var9.rand.nextFloat();

			for(var31 = 0; var31 < var25; ++var31) {
				var59 += MathHelper.sin(var66) * MathHelper.cos(var71);
				var62 += MathHelper.cos(var66) * MathHelper.cos(var71);
				var63 += MathHelper.sin(var71);
				var66 += var68 * 0.2F;
				var68 *= 0.9F;
				var68 += var9.rand.nextFloat() - var9.rand.nextFloat();
				var71 += var70 * 0.5F;
				var71 *= 0.5F;
				var70 *= 12.0F / 16.0F;
				var70 += var9.rand.nextFloat() - var9.rand.nextFloat();
				if(var9.rand.nextFloat() >= 0.25F) {
					float var74 = var59 + (var9.rand.nextFloat() * 4.0F - 2.0F) * 0.2F;
					float var33 = var63 + (var9.rand.nextFloat() * 4.0F - 2.0F) * 0.2F;
					float var77 = var62 + (var9.rand.nextFloat() * 4.0F - 2.0F) * 0.2F;
					float var75 = ((float)var9.height - var33) / (float)var9.height;
					float var80 = 1.2F + (var75 * 3.5F + 1.0F) * var73;
					float var78 = MathHelper.sin((float)var31 * (float)Math.PI / (float)var25) * var80;

					for(var5 = (int)(var74 - var78); var5 <= (int)(var74 + var78); ++var5) {
						for(int var81 = (int)(var33 - var78); var81 <= (int)(var33 + var78); ++var81) {
							for(int var40 = (int)(var77 - var78); var40 <= (int)(var77 + var78); ++var40) {
								float var41 = (float)var5 - var74;
								float var42 = (float)var81 - var33;
								float var48 = (float)var40 - var77;
								var41 = var41 * var41 + var42 * var42 * 2.0F + var48 * var48;
								if(var41 < var78 * var78 && var5 > 0 && var81 > 0 && var40 > 0 && var5 < var9.width - 1 && var81 < var9.height - 1 && var40 < var9.depth - 1) {
									var7 = (var81 * var9.depth + var40) * var9.width + var5;
									if(var9.blocksByteArray[var7] == Block.stone.blockID) {
										var9.blocksByteArray[var7] = 0;
									}
								}
							}
						}
					}
				}
			}
		}

		var7 = this.populateOre(Block.oreCoal.blockID, 1000, 10, (var4 << 2) / 5);
		int var44 = this.populateOre(Block.oreIron.blockID, 800, 8, var4 * 3 / 5);
		var45 = this.populateOre(Block.oreGold.blockID, 500, 6, (var4 << 1) / 5);
		var5 = this.populateOre(Block.oreDiamond.blockID, 800, 2, var4 / 5);
		System.out.println("Coal: " + var7 + ", Iron: " + var44 + ", Gold: " + var45 + ", Diamond: " + var5);
		this.guiLoading.displayLoadingString("Melting..");
		this.loadingBar();
		this.lavaGen();
		var6.cloudHeight = var4 + 2;
		if(this.floatingGen) {
			this.groundLevel = -128;
			this.waterLevel = this.groundLevel + 1;
			var6.cloudHeight = -16;
		} else if(!this.islandGen) {
			this.groundLevel = this.waterLevel + 1;
			this.waterLevel = this.groundLevel - 16;
		} else {
			this.groundLevel = this.waterLevel - 9;
		}

		this.guiLoading.displayLoadingString("Watering..");
		this.loadingBar();
		this.liquidThemeSpawner();
		if(!this.floatingGen) {
			var5 = Block.waterStill.blockID;
			if(this.levelType == 1) {
				var5 = Block.lavaStill.blockID;
			}

			for(var7 = 0; var7 < var2; ++var7) {
				this.floodFill(var7, this.waterLevel - 1, 0, 0, var5);
				this.floodFill(var7, this.waterLevel - 1, var3 - 1, 0, var5);
			}

			for(var7 = 0; var7 < var3; ++var7) {
				this.floodFill(var2 - 1, this.waterLevel - 1, var7, 0, var5);
				this.floodFill(0, this.waterLevel - 1, var7, 0, var5);
			}
		}

		if(this.levelType == 0) {
			var6.skyColor = 10079487;
			var6.fogColor = 16777215;
			var6.cloudColor = 16777215;
		}

		if(this.levelType == 1) {
			var6.cloudColor = 2164736;
			var6.fogColor = 1049600;
			var6.skyColor = 1049600;
			var6.skylightSubtracted = var6.skyBrightness = 7;
			var6.defaultFluid = Block.lavaMoving.blockID;
			if(this.floatingGen) {
				var6.cloudHeight = var4 + 2;
				this.waterLevel = -16;
			}
		}

		if(this.levelType == 2) {
			var6.skyColor = 13033215;
			var6.fogColor = 13033215;
			var6.cloudColor = 15658751;
			var6.skylightSubtracted = var6.skyBrightness = 15;
			var6.skyBrightness = 16;
			var6.cloudHeight = var4 + 64;
		}

		if(this.levelType == 3) {
			var6.skyColor = 7699847;
			var6.fogColor = 5069403;
			var6.cloudColor = 5069403;
			var6.skylightSubtracted = var6.skyBrightness = 12;
		}

		var6.waterLevel = this.waterLevel;
		var6.groundLevel = this.groundLevel;
		this.guiLoading.displayLoadingString("Assembling..");
		this.loadingBar();
		this.setNextPhase(0.0F);
		var6.generate(var2, var4, var3, this.blocksByteArray, (byte[])null);
		this.guiLoading.displayLoadingString("Building..");
		this.loadingBar();
		this.setNextPhase(0.0F);
		var6.findSpawn();
		generateHouse(var6);
		this.guiLoading.displayLoadingString("Planting..");
		this.loadingBar();
		if(this.levelType != 1) {
			this.growGrassOnDirt(var6);
		}

		this.loadingBar();
		this.growTrees(var6);
		if(this.levelType == 3) {
			for(var5 = 0; var5 < 50; ++var5) {
				this.growTrees(var6);
			}
		}

		short var43 = 100;
		if(this.levelType == 2) {
			var43 = 1000;
		}

		this.loadingBar();
		this.populateFlowersAndMushrooms(var6, Block.plantYellow, var43);
		this.loadingBar();
		this.populateFlowersAndMushrooms(var6, Block.plantRed, var43);
		this.loadingBar();
		this.populateFlowersAndMushrooms(var6, Block.mushroomBrown, 50);
		this.loadingBar();
		this.populateFlowersAndMushrooms(var6, Block.mushroomRed, 50);
		this.guiLoading.displayLoadingString("Lighting..");
		this.loadingBar();

		for(var7 = 0; var7 < 10000; ++var7) {
			this.setNextPhase((float)(var7 * 100 / 10000));
			var6.updateLighting();
		}

		this.guiLoading.displayLoadingString("Spawning..");
		this.loadingBar();
		MobSpawner var47 = new MobSpawner(var6);

		for(var2 = 0; var2 < 1000; ++var2) {
			this.setNextPhase((float)var2 * 100.0F / 999.0F);
			var47.performSpawning();
		}

		var6.createTime = System.currentTimeMillis();
		var6.authorName = var1;
		var6.name = "A Nice World";
		if(this.phaseBar != this.phases) {
			throw new IllegalStateException("Wrong number of phases! Wanted " + this.phases + ", got " + this.phaseBar);
		} else {
			return var6;
		}
	}

	private static void generateHouse(World var0) {
		int var1 = var0.xSpawn;
		int var2 = var0.ySpawn;
		int var3 = var0.zSpawn;

		for(int var4 = var1 - 3; var4 <= var1 + 3; ++var4) {
			for(int var5 = var2 - 2; var5 <= var2 + 2; ++var5) {
				for(int var6 = var3 - 3; var6 <= var3 + 3; ++var6) {
					int var7 = var5 < var2 - 1 ? Block.obsidian.blockID : 0;
					if(var4 == var1 - 3 || var6 == var3 - 3 || var4 == var1 + 3 || var6 == var3 + 3 || var5 == var2 - 2 || var5 == var2 + 2) {
						var7 = Block.stone.blockID;
						if(var5 >= var2 - 1) {
							var7 = Block.planks.blockID;
						}
					}

					if(var6 == var3 - 3 && var4 == var1 && var5 >= var2 - 1 && var5 <= var2) {
						var7 = 0;
					}

					var0.setBlockWithNotify(var4, var5, var6, var7);
				}
			}
		}

		var0.setBlockWithNotify(var1 - 3 + 1, var2, var3, Block.torch.blockID);
		var0.setBlockWithNotify(var1 + 3 - 1, var2, var3, Block.torch.blockID);
	}

	private void growGrassOnDirt(World var1) {
		for(int var2 = 0; var2 < this.width; ++var2) {
			this.setNextPhase((float)var2 * 100.0F / (float)(this.width - 1));

			for(int var3 = 0; var3 < this.height; ++var3) {
				for(int var4 = 0; var4 < this.depth; ++var4) {
					if(var1.getBlockId(var2, var3, var4) == Block.dirt.blockID && var1.getBlockLightValue(var2, var3 + 1, var4) >= 4 && !var1.getBlockMaterial(var2, var3 + 1, var4).getCanBlockGrass()) {
						var1.setBlock(var2, var3, var4, Block.grass.blockID);
					}
				}
			}
		}

	}

	private void growTrees(World var1) {
		int var2 = this.width * this.depth * this.height / 80000;

		for(int var3 = 0; var3 < var2; ++var3) {
			if(var3 % 100 == 0) {
				this.setNextPhase((float)var3 * 100.0F / (float)(var2 - 1));
			}

			int var4 = this.rand.nextInt(this.width);
			int var5 = this.rand.nextInt(this.height);
			int var6 = this.rand.nextInt(this.depth);

			for(int var7 = 0; var7 < 25; ++var7) {
				int var8 = var4;
				int var9 = var5;
				int var10 = var6;

				for(int var11 = 0; var11 < 20; ++var11) {
					var8 += this.rand.nextInt(12) - this.rand.nextInt(12);
					var9 += this.rand.nextInt(3) - this.rand.nextInt(6);
					var10 += this.rand.nextInt(12) - this.rand.nextInt(12);
					if(var8 >= 0 && var9 >= 0 && var10 >= 0 && var8 < this.width && var9 < this.height && var10 < this.depth) {
						var1.growTrees(var8, var9, var10);
					}
				}
			}
		}

	}

	private void populateFlowersAndMushrooms(World var1, BlockFlower var2, int var3) {
		var3 = (int)((long)this.width * (long)this.depth * (long)this.height * (long)var3 / 1600000L);

		for(int var4 = 0; var4 < var3; ++var4) {
			if(var4 % 100 == 0) {
				this.setNextPhase((float)var4 * 100.0F / (float)(var3 - 1));
			}

			int var5 = this.rand.nextInt(this.width);
			int var6 = this.rand.nextInt(this.height);
			int var7 = this.rand.nextInt(this.depth);

			for(int var8 = 0; var8 < 10; ++var8) {
				int var9 = var5;
				int var10 = var6;
				int var11 = var7;

				for(int var12 = 0; var12 < 10; ++var12) {
					var9 += this.rand.nextInt(4) - this.rand.nextInt(4);
					var10 += this.rand.nextInt(2) - this.rand.nextInt(2);
					var11 += this.rand.nextInt(4) - this.rand.nextInt(4);
					if(var9 >= 0 && var11 >= 0 && var10 > 0 && var9 < this.width && var11 < this.depth && var10 < this.height && var1.getBlockId(var9, var10, var11) == 0 && var2.canBlockStay(var1, var9, var10, var11)) {
						var1.setBlockWithNotify(var9, var10, var11, var2.blockID);
					}
				}
			}
		}

	}

	private int populateOre(int var1, int var2, int var3, int var4) {
		int var5 = 0;
		byte var26 = (byte)var1;
		int var6 = this.width;
		int var7 = this.depth;
		int var8 = this.height;
		var2 = var6 * var7 * var8 / 256 / 64 * var2 / 100;

		for(int var9 = 0; var9 < var2; ++var9) {
			this.setNextPhase((float)var9 * 100.0F / (float)(var2 - 1));
			float var10 = this.rand.nextFloat() * (float)var6;
			float var11 = this.rand.nextFloat() * (float)var8;
			float var12 = this.rand.nextFloat() * (float)var7;
			if(var11 <= (float)var4) {
				int var13 = (int)((this.rand.nextFloat() + this.rand.nextFloat()) * 75.0F * (float)var3 / 100.0F);
				float var14 = this.rand.nextFloat() * (float)Math.PI * 2.0F;
				float var15 = 0.0F;
				float var16 = this.rand.nextFloat() * (float)Math.PI * 2.0F;
				float var17 = 0.0F;

				for(int var18 = 0; var18 < var13; ++var18) {
					var10 += MathHelper.sin(var14) * MathHelper.cos(var16);
					var12 += MathHelper.cos(var14) * MathHelper.cos(var16);
					var11 += MathHelper.sin(var16);
					var14 += var15 * 0.2F;
					var15 *= 0.9F;
					var15 += this.rand.nextFloat() - this.rand.nextFloat();
					var16 += var17 * 0.5F;
					var16 *= 0.5F;
					var17 *= 0.9F;
					var17 += this.rand.nextFloat() - this.rand.nextFloat();
					float var19 = MathHelper.sin((float)var18 * (float)Math.PI / (float)var13) * (float)var3 / 100.0F + 1.0F;

					for(int var20 = (int)(var10 - var19); var20 <= (int)(var10 + var19); ++var20) {
						for(int var21 = (int)(var11 - var19); var21 <= (int)(var11 + var19); ++var21) {
							for(int var22 = (int)(var12 - var19); var22 <= (int)(var12 + var19); ++var22) {
								float var23 = (float)var20 - var10;
								float var24 = (float)var21 - var11;
								float var25 = (float)var22 - var12;
								var23 = var23 * var23 + var24 * var24 * 2.0F + var25 * var25;
								if(var23 < var19 * var19 && var20 > 0 && var21 > 0 && var22 > 0 && var20 < this.width - 1 && var21 < this.height - 1 && var22 < this.depth - 1) {
									int var27 = (var21 * this.depth + var22) * this.width + var20;
									if(this.blocksByteArray[var27] == Block.stone.blockID) {
										this.blocksByteArray[var27] = var26;
										++var5;
									}
								}
							}
						}
					}
				}
			}
		}

		return var5;
	}

	private void liquidThemeSpawner() {
		int var1 = Block.waterStill.blockID;
		if(this.levelType == 1) {
			var1 = Block.lavaStill.blockID;
		}

		int var2 = this.width * this.depth * this.height / 1000;

		for(int var3 = 0; var3 < var2; ++var3) {
			if(var3 % 100 == 0) {
				this.setNextPhase((float)var3 * 100.0F / (float)(var2 - 1));
			}

			int var4 = this.rand.nextInt(this.width);
			int var5 = this.rand.nextInt(this.height);
			int var6 = this.rand.nextInt(this.depth);
			if(this.blocksByteArray[(var5 * this.depth + var6) * this.width + var4] == 0) {
				long var7 = this.floodFill(var4, var5, var6, 0, 255);
				if(var7 > 0L && var7 < 640L) {
					this.floodFill(var4, var5, var6, 255, var1);
				} else {
					this.floodFill(var4, var5, var6, 255, 0);
				}
			}
		}

		this.setNextPhase(100.0F);
	}

	private void loadingBar() {
		++this.phaseBar;
		this.phaseBareLength = 0.0F;
		this.setNextPhase(0.0F);
	}

	private void setNextPhase(float var1) {
		if(var1 < 0.0F) {
			throw new IllegalStateException("Failed to set next phase!");
		} else {
			int var2 = (int)(((float)(this.phaseBar - 1) + var1 / 100.0F) * 100.0F / (float)this.phases);
			this.guiLoading.setLoadingProgress(var2);
		}
	}

	private void lavaGen() {
		int var1 = this.width * this.depth * this.height / 2000;
		int var2 = this.groundLevel;

		for(int var3 = 0; var3 < var1; ++var3) {
			if(var3 % 100 == 0) {
				this.setNextPhase((float)var3 * 100.0F / (float)(var1 - 1));
			}

			int var4 = this.rand.nextInt(this.width);
			int var5 = Math.min(Math.min(this.rand.nextInt(var2), this.rand.nextInt(var2)), Math.min(this.rand.nextInt(var2), this.rand.nextInt(var2)));
			int var6 = this.rand.nextInt(this.depth);
			if(this.blocksByteArray[(var5 * this.depth + var6) * this.width + var4] == 0) {
				long var7 = this.floodFill(var4, var5, var6, 0, 255);
				if(var7 > 0L && var7 < 640L) {
					this.floodFill(var4, var5, var6, 255, Block.lavaStill.blockID);
				} else {
					this.floodFill(var4, var5, var6, 255, 0);
				}
			}
		}

		this.setNextPhase(100.0F);
	}

	private long floodFill(int var1, int var2, int var3, int var4, int var5) {
		byte var6 = (byte)var5;
		byte var22 = (byte)var4;
		ArrayList var7 = new ArrayList();
		byte var8 = 0;
		int var9 = 1;

		int var10;
		for(var10 = 1; 1 << var9 < this.width; ++var9) {
		}

		while(1 << var10 < this.depth) {
			++var10;
		}

		int var11 = this.depth - 1;
		int var12 = this.width - 1;
		int var23 = var8 + 1;
		this.floodFillBlocks[0] = ((var2 << var10) + var3 << var9) + var1;
		long var14 = 0L;
		var1 = this.width * this.depth;

		while(var23 > 0) {
			--var23;
			var2 = this.floodFillBlocks[var23];
			if(var23 == 0 && var7.size() > 0) {
				this.floodFillBlocks = (int[])var7.remove(var7.size() - 1);
				var23 = this.floodFillBlocks.length;
			}

			var3 = var2 >> var9 & var11;
			int var13 = var2 >> var9 + var10;
			int var16 = var2 & var12;

			int var17;
			for(var17 = var16; var16 > 0 && this.blocksByteArray[var2 - 1] == var22; --var2) {
				--var16;
			}

			while(var17 < this.width && this.blocksByteArray[var2 + var17 - var16] == var22) {
				++var17;
			}

			int var18 = var2 >> var9 & var11;
			int var19 = var2 >> var9 + var10;
			if(var5 == 255 && (var16 == 0 || var17 == this.width - 1 || var13 == 0 || var13 == this.height - 1 || var3 == 0 || var3 == this.depth - 1)) {
				return -1L;
			}

			if(var18 != var3 || var19 != var13) {
				System.out.println("Diagonal flood!?");
			}

			boolean var24 = false;
			boolean var25 = false;
			boolean var20 = false;
			var14 += (long)(var17 - var16);

			for(var16 = var16; var16 < var17; ++var16) {
				this.blocksByteArray[var2] = var6;
				boolean var21;
				if(var3 > 0) {
					var21 = this.blocksByteArray[var2 - this.width] == var22;
					if(var21 && !var24) {
						if(var23 == this.floodFillBlocks.length) {
							var7.add(this.floodFillBlocks);
							this.floodFillBlocks = new int[1048576];
							var23 = 0;
						}

						this.floodFillBlocks[var23++] = var2 - this.width;
					}

					var24 = var21;
				}

				if(var3 < this.depth - 1) {
					var21 = this.blocksByteArray[var2 + this.width] == var22;
					if(var21 && !var25) {
						if(var23 == this.floodFillBlocks.length) {
							var7.add(this.floodFillBlocks);
							this.floodFillBlocks = new int[1048576];
							var23 = 0;
						}

						this.floodFillBlocks[var23++] = var2 + this.width;
					}

					var25 = var21;
				}

				if(var13 > 0) {
					byte var26 = this.blocksByteArray[var2 - var1];
					if((var6 == Block.lavaMoving.blockID || var6 == Block.lavaStill.blockID) && (var26 == Block.waterMoving.blockID || var26 == Block.waterStill.blockID)) {
						this.blocksByteArray[var2 - var1] = (byte)Block.stone.blockID;
					}

					var21 = var26 == var22;
					if(var21 && !var20) {
						if(var23 == this.floodFillBlocks.length) {
							var7.add(this.floodFillBlocks);
							this.floodFillBlocks = new int[1048576];
							var23 = 0;
						}

						this.floodFillBlocks[var23++] = var2 - var1;
					}

					var20 = var21;
				}

				++var2;
			}
		}

		return var14;
	}
}
