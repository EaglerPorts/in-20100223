package net.minecraft.game.item;

import net.minecraft.game.entity.misc.EntityItem;
import net.minecraft.game.level.World;
import net.minecraft.game.level.block.Block;
import net.minecraft.game.level.block.StepSound;
import net.minecraft.game.level.material.Material;

public final class ItemHoe extends Item {
	public ItemHoe(int var1, int var2) {
		super(var1);
		this.maxStackSize = 1;
		this.maxDamage = 32 << var2;
	}

	public final boolean onItemUse(ItemStack var1, World var2, int var3, int var4, int var5, int var6) {
		if(var3 > 0 && var4 > 0 && var5 > 0 && var3 < var2.width - 1 && var4 < var2.height - 1 && var5 < var2.length - 1) {
			var6 = var2.getBlockId(var3, var4, var5);
			Material var7 = var2.getBlockMaterial(var3, var4 + 1, var5);
			if((var7.isSolid() || var6 != Block.grass.blockID) && var6 != Block.dirt.blockID) {
				return false;
			} else {
				Block var12 = Block.tilledField;
				float var10001 = (float)var3 + 0.5F;
				float var10002 = (float)var4 + 0.5F;
				float var10003 = (float)var5 + 0.5F;
				String var10004 = var12.stepSound.stepSoundDir2();
				StepSound var8 = var12.stepSound;
				float var10005 = (var8.soundVolume + 1.0F) / 2.0F;
				var8 = var12.stepSound;
				var2.playSoundAtPlayer(var10001, var10002, var10003, var10004, var10005, var8.soundPitch * 0.8F);
				var2.setBlockWithNotify(var3, var4, var5, var12.blockID);
				var1.damageItem(1);
				if(var2.random.nextInt(8) == 0 && var6 == Block.grass.blockID) {
					for(int var9 = 0; var9 <= 0; ++var9) {
						float var10 = var2.random.nextFloat() * 0.7F + 0.15F;
						float var13 = var2.random.nextFloat() * 0.7F + 0.15F;
						EntityItem var11 = new EntityItem(var2, (float)var3 + var10, (float)var4 + 1.2F, (float)var5 + var13, new ItemStack(Item.seeds));
						var11.delayBeforeCanPickup = 10;
						var2.spawnEntityInWorld(var11);
					}
				}

				return true;
			}
		} else {
			return false;
		}
	}
}
