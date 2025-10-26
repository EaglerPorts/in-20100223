package net.minecraft.game.level.block;

import java.util.Random;
import net.minecraft.game.entity.misc.EntityItem;
import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.entity.player.InventoryPlayer;
import net.minecraft.game.item.Item;
import net.minecraft.game.item.ItemBlock;
import net.minecraft.game.item.ItemStack;
import net.minecraft.game.level.World;
import net.minecraft.game.level.material.Material;
import net.minecraft.game.physics.AxisAlignedBB;
import net.minecraft.game.physics.MovingObjectPosition;
import net.minecraft.game.physics.Vec3D;

public class Block {
	private static StepSound soundPowderFootstep = new StepSound("stone", 1.0F, 1.0F);
	private static StepSound soundWoodFootstep = new StepSound("wood", 1.0F, 1.0F);
	private static StepSound soundGravelFootstep = new StepSound("gravel", 1.0F, 1.0F);
	private static StepSound soundGrassFootstep = new StepSound("grass", 1.0F, 1.0F);
	private static StepSound soundStoneFootstep = new StepSound("stone", 1.0F, 1.0F);
	private static StepSound soundMetalFootstep = new StepSound("stone", 1.0F, 1.5F);
	private static StepSound soundGlassFootstep = new StepSoundGlass("stone", 1.0F, 1.0F);
	private static StepSound soundClothFootstep = new StepSound("cloth", 1.0F, 1.0F);
	private static StepSound soundSandFootstep = new StepSoundSand("sand", 1.0F, 1.0F);
	public static final Block[] blocksList = new Block[256];
	public static final boolean[] tickOnLoad = new boolean[256];
	public static final boolean[] opaqueCubeLookup = new boolean[256];
	public static final int[] lightOpacity = new int[256];
	private static boolean[] canBlockGrass = new boolean[256];
	public static final boolean[] isBlockFluid = new boolean[256];
	public static final int[] lightValue = new int[256];
	public static final Block stone;
	public static final BlockGrass grass;
	public static final Block dirt;
	public static final Block cobblestone;
	public static final Block planks;
	public static final Block sapling;
	public static final Block bedrock;
	public static final Block waterMoving;
	public static final Block waterStill;
	public static final Block lavaMoving;
	public static final Block lavaStill;
	public static final Block sand;
	public static final Block gravel;
	public static final Block oreGold;
	public static final Block oreIron;
	public static final Block oreCoal;
	public static final Block wood;
	public static final Block leaves;
	public static final Block sponge;
	public static final Block glass;
	public static final Block clothRed;
	public static final Block clothOrange;
	public static final Block clothYellow;
	public static final Block clothChartreuse;
	public static final Block clothGreen;
	public static final Block clothSpringGreen;
	public static final Block clothCyan;
	public static final Block clothCapri;
	public static final Block clothUltramarine;
	public static final Block clothViolet;
	public static final Block clothPurple;
	public static final Block clothMagenta;
	public static final Block clothRose;
	public static final Block clothDarkGray;
	public static final Block clothGray;
	public static final Block clothWhite;
	public static final BlockFlower plantYellow;
	public static final BlockFlower plantRed;
	public static final BlockFlower mushroomBrown;
	public static final BlockFlower mushroomRed;
	public static final Block blockGold;
	public static final Block blockSteel;
	public static final Block stairDouble;
	public static final Block stairSingle;
	public static final Block brick;
	public static final Block tnt;
	public static final Block bookShelf;
	public static final Block cobblestoneMossy;
	public static final Block obsidian;
	public static final Block torch;
	public static final BlockFire fire;
	public static final Block waterSource;
	public static final Block lavaSource;
	public static final Block crate;
	public static final Block cog;
	public static final Block oreDiamond;
	public static final Block blockDiamond;
	public static final Block workbench;
	public static final Block crops;
	public static final Block tilledField;
	public static final Block stoneOvenIdle;
	public static final Block stoneOvenActive;
	public int blockIndexInTexture;
	public final int blockID;
	private float hardness;
	private float resistance;
	public float minX;
	public float minY;
	public float minZ;
	public float maxX;
	public float maxY;
	public float maxZ;
	public StepSound stepSound;
	public float blockParticleGravity;
	public final Material material;

	protected Block(int var1, Material var2) {
		this.stepSound = soundPowderFootstep;
		this.blockParticleGravity = 1.0F;
		if(blocksList[var1] != null) {
			throw new IllegalArgumentException("Slot " + var1 + " is already occupied by " + blocksList[var1] + " when adding " + this);
		} else {
			this.material = var2;
			blocksList[var1] = this;
			this.blockID = var1;
			this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
			opaqueCubeLookup[var1] = this.isOpaqueCube();
			lightOpacity[var1] = this.isOpaqueCube() ? 255 : 0;
			canBlockGrass[var1] = this.renderAsNormalBlock();
			isBlockFluid[var1] = false;
		}
	}

	protected Block(int var1, int var2, Material var3) {
		this(var1, var3);
		this.blockIndexInTexture = var2;
	}

	protected final Block setLightOpacity(int var1) {
		lightOpacity[this.blockID] = var1;
		return this;
	}

	private Block setLightValue(float var1) {
		lightValue[this.blockID] = (int)(15.0F * var1);
		return this;
	}

	protected final Block setResistance(float var1) {
		this.resistance = var1 * 3.0F;
		return this;
	}

	public boolean renderAsNormalBlock() {
		return true;
	}

	public int getRenderType() {
		return 0;
	}

	protected final Block setHardness(float var1) {
		this.hardness = var1;
		if(this.resistance < var1 * 5.0F) {
			this.resistance = var1 * 5.0F;
		}

		return this;
	}

	protected final void setTickOnLoad(boolean var1) {
		tickOnLoad[this.blockID] = var1;
	}

	protected final void setBlockBounds(float var1, float var2, float var3, float var4, float var5, float var6) {
		this.minX = var1;
		this.minY = var2;
		this.minZ = var3;
		this.maxX = var4;
		this.maxY = var5;
		this.maxZ = var6;
	}

	public float getBlockBrightness(World var1, int var2, int var3, int var4) {
		return var1.getLightBrightness(var2, var3, var4);
	}

	public boolean shouldSideBeRendered(World var1, int var2, int var3, int var4, int var5) {
		return !var1.isBlockNormalCube(var2, var3, var4);
	}

	public int getBlockTexture(World var1, int var2, int var3, int var4, int var5) {
		return this.getBlockTextureFromSideAndMetadata(var5, var1.getBlockMetadata(var2, var3, var4));
	}

	public int getBlockTextureFromSideAndMetadata(int var1, int var2) {
		return this.getBlockTextureFromSide(var1);
	}

	public int getBlockTextureFromSide(int var1) {
		return this.blockIndexInTexture;
	}

	public final AxisAlignedBB getSelectedBoundingBoxFromPool(int var1, int var2, int var3) {
		return new AxisAlignedBB((float)var1 + this.minX, (float)var2 + this.minY, (float)var3 + this.minZ, (float)var1 + this.maxX, (float)var2 + this.maxY, (float)var3 + this.maxZ);
	}

	public AxisAlignedBB getCollisionBoundingBoxFromPool(int var1, int var2, int var3) {
		return new AxisAlignedBB((float)var1 + this.minX, (float)var2 + this.minY, (float)var3 + this.minZ, (float)var1 + this.maxX, (float)var2 + this.maxY, (float)var3 + this.maxZ);
	}

	public boolean isOpaqueCube() {
		return true;
	}

	public boolean isCollidable() {
		return true;
	}

	public void updateTick(World var1, int var2, int var3, int var4, Random var5) {
	}

	public void randomDisplayTick(World var1, int var2, int var3, int var4, Random var5) {
	}

	public void onBlockDestroyedByPlayer(World var1, int var2, int var3, int var4, int var5) {
	}

	public void onNeighborBlockChange(World var1, int var2, int var3, int var4, int var5) {
	}

	public int tickRate() {
		return 5;
	}

	public void onBlockAdded(World var1, int var2, int var3, int var4) {
	}

	public void onBlockRemoval(World var1, int var2, int var3, int var4) {
	}

	public int quantityDropped(Random var1) {
		return 1;
	}

	public int idDropped(int var1, Random var2) {
		return this.blockID;
	}

	public final float blockStrength(EntityPlayer var1) {
		if(this.hardness < 0.0F) {
			return 0.0F;
		} else if(!var1.canHarvestBlock(this)) {
			return 1.0F / this.hardness / 100.0F;
		} else {
			InventoryPlayer var2 = var1.inventory;
			float var4 = 1.0F;
			if(var2.mainInventory[var2.currentItem] != null) {
				ItemStack var5 = var2.mainInventory[var2.currentItem];
				var4 = 1.0F * var5.getItem().getStrVsBlock(this);
			}

			float var6 = var4;
			if(var1.isInsideOfWater()) {
				var6 = var4 / 5.0F;
			}

			if(!var1.onGround) {
				var6 /= 5.0F;
			}

			return var6 / this.hardness / 30.0F;
		}
	}

	public final void dropBlockAsItem(World var1, int var2, int var3, int var4, int var5) {
		this.dropBlockAsItemWithChance(var1, var2, var3, var4, var5, 1.0F);
	}

	public final void dropBlockAsItemWithChance(World var1, int var2, int var3, int var4, int var5, float var6) {
		int var7 = this.quantityDropped(var1.random);

		for(int var8 = 0; var8 < var7; ++var8) {
			if(var1.random.nextFloat() <= var6) {
				int var9 = this.idDropped(var5, var1.random);
				if(var9 > 0) {
					float var10 = var1.random.nextFloat() * 0.7F + 0.15F;
					float var11 = var1.random.nextFloat() * 0.7F + 0.15F;
					float var12 = var1.random.nextFloat() * 0.7F + 0.15F;
					EntityItem var13 = new EntityItem(var1, (float)var2 + var10, (float)var3 + var11, (float)var4 + var12, new ItemStack(var9));
					var13.delayBeforeCanPickup = 10;
					var1.spawnEntityInWorld(var13);
				}
			}
		}

	}

	public final float getExplosionResistance() {
		return this.resistance / 5.0F;
	}

	public MovingObjectPosition collisionRayTrace(World var1, int var2, int var3, int var4, Vec3D var5, Vec3D var6) {
		var5 = var5.addVector((float)(-var2), (float)(-var3), (float)(-var4));
		var6 = var6.addVector((float)(-var2), (float)(-var3), (float)(-var4));
		Vec3D var12 = var5.getIntermediateWithXValue(var6, this.minX);
		Vec3D var7 = var5.getIntermediateWithXValue(var6, this.maxX);
		Vec3D var8 = var5.getIntermediateWithYValue(var6, this.minY);
		Vec3D var9 = var5.getIntermediateWithYValue(var6, this.maxY);
		Vec3D var10 = var5.getIntermediateWithZValue(var6, this.minZ);
		var6 = var5.getIntermediateWithZValue(var6, this.maxZ);
		if(!this.isVecInsideYZBounds(var12)) {
			var12 = null;
		}

		if(!this.isVecInsideYZBounds(var7)) {
			var7 = null;
		}

		if(!this.isVecInsideXZBounds(var8)) {
			var8 = null;
		}

		if(!this.isVecInsideXZBounds(var9)) {
			var9 = null;
		}

		if(!this.isVecInsideXYBounds(var10)) {
			var10 = null;
		}

		if(!this.isVecInsideXYBounds(var6)) {
			var6 = null;
		}

		Vec3D var11 = null;
		if(var12 != null) {
			var11 = var12;
		}

		if(var7 != null && (var11 == null || var5.distance(var7) < var5.distance(var11))) {
			var11 = var7;
		}

		if(var8 != null && (var11 == null || var5.distance(var8) < var5.distance(var11))) {
			var11 = var8;
		}

		if(var9 != null && (var11 == null || var5.distance(var9) < var5.distance(var11))) {
			var11 = var9;
		}

		if(var10 != null && (var11 == null || var5.distance(var10) < var5.distance(var11))) {
			var11 = var10;
		}

		if(var6 != null && (var11 == null || var5.distance(var6) < var5.distance(var11))) {
			var11 = var6;
		}

		if(var11 == null) {
			return null;
		} else {
			byte var13 = -1;
			if(var11 == var12) {
				var13 = 4;
			}

			if(var11 == var7) {
				var13 = 5;
			}

			if(var11 == var8) {
				var13 = 0;
			}

			if(var11 == var9) {
				var13 = 1;
			}

			if(var11 == var10) {
				var13 = 2;
			}

			if(var11 == var6) {
				var13 = 3;
			}

			return new MovingObjectPosition(var2, var3, var4, var13, var11.addVector((float)var2, (float)var3, (float)var4));
		}
	}

	private boolean isVecInsideYZBounds(Vec3D var1) {
		return var1 == null ? false : var1.yCoord >= this.minY && var1.yCoord <= this.maxY && var1.zCoord >= this.minZ && var1.zCoord <= this.maxZ;
	}

	private boolean isVecInsideXZBounds(Vec3D var1) {
		return var1 == null ? false : var1.xCoord >= this.minX && var1.xCoord <= this.maxX && var1.zCoord >= this.minZ && var1.zCoord <= this.maxZ;
	}

	private boolean isVecInsideXYBounds(Vec3D var1) {
		return var1 == null ? false : var1.xCoord >= this.minX && var1.xCoord <= this.maxX && var1.yCoord >= this.minY && var1.yCoord <= this.maxY;
	}

	public void onBlockDestroyedByExplosion(World var1, int var2, int var3, int var4) {
	}

	public int getRenderBlockPass() {
		return 0;
	}

	public boolean canPlaceBlockAt(World var1, int var2, int var3, int var4) {
		return true;
	}

	public boolean blockActivated(World var1, int var2, int var3, int var4, EntityPlayer var5) {
		return false;
	}

	public void onEntityWalking(World var1, int var2, int var3, int var4) {
	}

	public void onBlockPlaced(World var1, int var2, int var3, int var4, int var5) {
	}

	static {
		Block var10000 = (new BlockStone(1, 1)).setHardness(1.5F).setResistance(10.0F);
		StepSound var1 = soundStoneFootstep;
		Block var0 = var10000;
		var0.stepSound = var1;
		stone = var0;
		var10000 = (new BlockGrass(2)).setHardness(0.6F);
		var1 = soundGrassFootstep;
		var0 = var10000;
		var0.stepSound = var1;
		grass = (BlockGrass)var0;
		var10000 = (new BlockDirt(3, 2)).setHardness(0.5F);
		var1 = soundGravelFootstep;
		var0 = var10000;
		var0.stepSound = var1;
		dirt = var0;
		var10000 = (new Block(4, 16, Material.rock)).setHardness(2.0F).setResistance(10.0F);
		var1 = soundStoneFootstep;
		var0 = var10000;
		var0.stepSound = var1;
		cobblestone = var0;
		var10000 = (new Block(5, 4, Material.wood)).setHardness(2.0F).setResistance(5.0F);
		var1 = soundWoodFootstep;
		var0 = var10000;
		var0.stepSound = var1;
		planks = var0;
		var10000 = (new BlockSapling(6, 15)).setHardness(0.0F);
		var1 = soundGrassFootstep;
		var0 = var10000;
		var0.stepSound = var1;
		sapling = var0;
		var10000 = (new Block(7, 17, Material.rock)).setHardness(-1.0F).setResistance(6000000.0F);
		var1 = soundStoneFootstep;
		var0 = var10000;
		var0.stepSound = var1;
		bedrock = var0;
		waterMoving = (new BlockFlowing(8, Material.water)).setHardness(100.0F).setLightOpacity(3);
		waterStill = (new BlockStationary(9, Material.water)).setHardness(100.0F).setLightOpacity(3);
		lavaMoving = (new BlockFlowing(10, Material.lava)).setHardness(0.0F).setLightValue(1.0F).setLightOpacity(255);
		lavaStill = (new BlockStationary(11, Material.lava)).setHardness(100.0F).setLightValue(1.0F).setLightOpacity(255);
		var10000 = (new BlockSand(12, 18)).setHardness(0.5F);
		var1 = soundSandFootstep;
		var0 = var10000;
		var0.stepSound = var1;
		sand = var0;
		var10000 = (new BlockGravel(13, 19)).setHardness(0.6F);
		var1 = soundGravelFootstep;
		var0 = var10000;
		var0.stepSound = var1;
		gravel = var0;
		var10000 = (new BlockOre(14, 32)).setHardness(3.0F).setResistance(5.0F);
		var1 = soundStoneFootstep;
		var0 = var10000;
		var0.stepSound = var1;
		oreGold = var0;
		var10000 = (new BlockOre(15, 33)).setHardness(3.0F).setResistance(5.0F);
		var1 = soundStoneFootstep;
		var0 = var10000;
		var0.stepSound = var1;
		oreIron = var0;
		var10000 = (new BlockOre(16, 34)).setHardness(3.0F).setResistance(5.0F);
		var1 = soundStoneFootstep;
		var0 = var10000;
		var0.stepSound = var1;
		oreCoal = var0;
		var10000 = (new BlockLog(17)).setHardness(2.0F);
		var1 = soundWoodFootstep;
		var0 = var10000;
		var0.stepSound = var1;
		wood = var0;
		var10000 = (new BlockLeaves(18, 52)).setHardness(0.2F).setLightOpacity(1);
		var1 = soundGrassFootstep;
		var0 = var10000;
		var0.stepSound = var1;
		leaves = var0;
		var10000 = (new BlockSponge(19)).setHardness(0.6F);
		var1 = soundGrassFootstep;
		var0 = var10000;
		var0.stepSound = var1;
		sponge = var0;
		var10000 = (new BlockGlass(20, 49, Material.glass, false)).setHardness(0.3F);
		var1 = soundGlassFootstep;
		var0 = var10000;
		var0.stepSound = var1;
		glass = var0;
		var10000 = (new Block(21, 64, Material.cloth)).setHardness(0.8F);
		var1 = soundClothFootstep;
		var0 = var10000;
		var0.stepSound = var1;
		clothRed = var0;
		var10000 = (new Block(22, 65, Material.cloth)).setHardness(0.8F);
		var1 = soundClothFootstep;
		var0 = var10000;
		var0.stepSound = var1;
		clothOrange = var0;
		var10000 = (new Block(23, 66, Material.cloth)).setHardness(0.8F);
		var1 = soundClothFootstep;
		var0 = var10000;
		var0.stepSound = var1;
		clothYellow = var0;
		var10000 = (new Block(24, 67, Material.cloth)).setHardness(0.8F);
		var1 = soundClothFootstep;
		var0 = var10000;
		var0.stepSound = var1;
		clothChartreuse = var0;
		var10000 = (new Block(25, 68, Material.cloth)).setHardness(0.8F);
		var1 = soundClothFootstep;
		var0 = var10000;
		var0.stepSound = var1;
		clothGreen = var0;
		var10000 = (new Block(26, 69, Material.cloth)).setHardness(0.8F);
		var1 = soundClothFootstep;
		var0 = var10000;
		var0.stepSound = var1;
		clothSpringGreen = var0;
		var10000 = (new Block(27, 70, Material.cloth)).setHardness(0.8F);
		var1 = soundClothFootstep;
		var0 = var10000;
		var0.stepSound = var1;
		clothCyan = var0;
		var10000 = (new Block(28, 71, Material.cloth)).setHardness(0.8F);
		var1 = soundClothFootstep;
		var0 = var10000;
		var0.stepSound = var1;
		clothCapri = var0;
		var10000 = (new Block(29, 72, Material.cloth)).setHardness(0.8F);
		var1 = soundClothFootstep;
		var0 = var10000;
		var0.stepSound = var1;
		clothUltramarine = var0;
		var10000 = (new Block(30, 73, Material.cloth)).setHardness(0.8F);
		var1 = soundClothFootstep;
		var0 = var10000;
		var0.stepSound = var1;
		clothViolet = var0;
		var10000 = (new Block(31, 74, Material.cloth)).setHardness(0.8F);
		var1 = soundClothFootstep;
		var0 = var10000;
		var0.stepSound = var1;
		clothPurple = var0;
		var10000 = (new Block(32, 75, Material.cloth)).setHardness(0.8F);
		var1 = soundClothFootstep;
		var0 = var10000;
		var0.stepSound = var1;
		clothMagenta = var0;
		var10000 = (new Block(33, 76, Material.cloth)).setHardness(0.8F);
		var1 = soundClothFootstep;
		var0 = var10000;
		var0.stepSound = var1;
		clothRose = var0;
		var10000 = (new Block(34, 77, Material.cloth)).setHardness(0.8F);
		var1 = soundClothFootstep;
		var0 = var10000;
		var0.stepSound = var1;
		clothDarkGray = var0;
		var10000 = (new Block(35, 78, Material.cloth)).setHardness(0.8F);
		var1 = soundClothFootstep;
		var0 = var10000;
		var0.stepSound = var1;
		clothGray = var0;
		var10000 = (new Block(36, 79, Material.cloth)).setHardness(0.8F);
		var1 = soundClothFootstep;
		var0 = var10000;
		var0.stepSound = var1;
		clothWhite = var0;
		var10000 = (new BlockFlower(37, 13)).setHardness(0.0F);
		var1 = soundGrassFootstep;
		var0 = var10000;
		var0.stepSound = var1;
		plantYellow = (BlockFlower)var0;
		var10000 = (new BlockFlower(38, 12)).setHardness(0.0F);
		var1 = soundGrassFootstep;
		var0 = var10000;
		var0.stepSound = var1;
		plantRed = (BlockFlower)var0;
		var10000 = (new BlockMushroom(39, 29)).setHardness(0.0F);
		var1 = soundGrassFootstep;
		var0 = var10000;
		var0.stepSound = var1;
		mushroomBrown = (BlockFlower)var0.setLightValue(2.0F / 16.0F);
		var10000 = (new BlockMushroom(40, 28)).setHardness(0.0F);
		var1 = soundGrassFootstep;
		var0 = var10000;
		var0.stepSound = var1;
		mushroomRed = (BlockFlower)var0;
		var10000 = (new BlockOreBlock(41, 39)).setHardness(3.0F).setResistance(10.0F);
		var1 = soundMetalFootstep;
		var0 = var10000;
		var0.stepSound = var1;
		blockGold = var0;
		var10000 = (new BlockOreBlock(42, 38)).setHardness(5.0F).setResistance(10.0F);
		var1 = soundMetalFootstep;
		var0 = var10000;
		var0.stepSound = var1;
		blockSteel = var0;
		var10000 = (new BlockStep(43, true)).setHardness(2.0F).setResistance(10.0F);
		var1 = soundStoneFootstep;
		var0 = var10000;
		var0.stepSound = var1;
		stairDouble = var0;
		var10000 = (new BlockStep(44, false)).setHardness(2.0F).setResistance(10.0F);
		var1 = soundStoneFootstep;
		var0 = var10000;
		var0.stepSound = var1;
		stairSingle = var0;
		var10000 = (new Block(45, 7, Material.rock)).setHardness(2.0F).setResistance(10.0F);
		var1 = soundStoneFootstep;
		var0 = var10000;
		var0.stepSound = var1;
		brick = var0;
		var10000 = (new BlockTNT(46, 8)).setHardness(0.0F);
		var1 = soundGrassFootstep;
		var0 = var10000;
		var0.stepSound = var1;
		tnt = var0;
		var10000 = (new BlockBookshelf(47, 35)).setHardness(1.5F);
		var1 = soundWoodFootstep;
		var0 = var10000;
		var0.stepSound = var1;
		bookShelf = var0;
		var10000 = (new Block(48, 36, Material.rock)).setHardness(2.0F).setResistance(10.0F);
		var1 = soundStoneFootstep;
		var0 = var10000;
		var0.stepSound = var1;
		cobblestoneMossy = var0;
		var10000 = (new BlockStone(49, 37)).setHardness(10.0F).setResistance(10.0F);
		var1 = soundStoneFootstep;
		var0 = var10000;
		var0.stepSound = var1;
		obsidian = var0;
		var10000 = (new BlockTorch(50, 80)).setHardness(0.0F).setLightValue(14.0F / 16.0F);
		var1 = soundWoodFootstep;
		var0 = var10000;
		var0.stepSound = var1;
		torch = var0;
		var10000 = (new BlockFire(51, 31)).setHardness(0.0F).setLightValue(1.0F);
		var1 = soundWoodFootstep;
		var0 = var10000;
		var0.stepSound = var1;
		fire = (BlockFire)var0;
		var10000 = (new BlockSource(52, waterMoving.blockID)).setHardness(0.0F);
		var1 = soundWoodFootstep;
		var0 = var10000;
		var0.stepSound = var1;
		waterSource = var0;
		var10000 = (new BlockSource(53, lavaMoving.blockID)).setHardness(0.0F);
		var1 = soundWoodFootstep;
		var0 = var10000;
		var0.stepSound = var1;
		lavaSource = var0;
		var10000 = (new BlockChest(54)).setHardness(2.5F);
		var1 = soundWoodFootstep;
		var0 = var10000;
		var0.stepSound = var1;
		crate = var0;
		var10000 = (new BlockGears(55, 62)).setHardness(0.5F);
		var1 = soundMetalFootstep;
		var0 = var10000;
		var0.stepSound = var1;
		cog = var0;
		var10000 = (new BlockOre(56, 50)).setHardness(3.0F).setResistance(5.0F);
		var1 = soundStoneFootstep;
		var0 = var10000;
		var0.stepSound = var1;
		oreDiamond = var0;
		var10000 = (new BlockOreBlock(57, 40)).setHardness(5.0F).setResistance(10.0F);
		var1 = soundMetalFootstep;
		var0 = var10000;
		var0.stepSound = var1;
		blockDiamond = var0;
		var10000 = (new BlockWorkbench(58)).setHardness(2.5F);
		var1 = soundWoodFootstep;
		var0 = var10000;
		var0.stepSound = var1;
		workbench = var0;
		var10000 = (new BlockCrops(59, 88)).setHardness(0.0F);
		var1 = soundGrassFootstep;
		var0 = var10000;
		var0.stepSound = var1;
		crops = var0;
		var10000 = (new BlockFarmland(60)).setHardness(0.6F);
		var1 = soundGravelFootstep;
		var0 = var10000;
		var0.stepSound = var1;
		tilledField = var0;
		var10000 = (new BlockFurnace(61, false)).setHardness(3.5F);
		var1 = soundStoneFootstep;
		var0 = var10000;
		var0.stepSound = var1;
		stoneOvenIdle = var0;
		var10000 = (new BlockFurnace(62, true)).setHardness(3.5F);
		var1 = soundStoneFootstep;
		var0 = var10000;
		var0.stepSound = var1;
		stoneOvenActive = var0.setLightValue(14.0F / 16.0F);

		for(int var2 = 0; var2 < 256; ++var2) {
			if(blocksList[var2] != null) {
				Item.itemsList[var2] = new ItemBlock(var2 - 256);
			}
		}

	}
}
