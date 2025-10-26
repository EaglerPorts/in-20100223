package net.minecraft.client.controller;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.EntityPlayerSP;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.game.item.Item;
import net.minecraft.game.item.ItemStack;
import net.minecraft.game.level.MobSpawner;
import net.minecraft.game.level.World;
import net.minecraft.game.level.block.Block;
import net.minecraft.game.level.block.StepSound;

public final class PlayerControllerSP extends PlayerController {
	private int curBlockX = -1;
	private int curBlockY = -1;
	private int curBlockZ = -1;
	private float curBlockDamage = 0.0F;
	private float prevBlockDamage = 0.0F;
	private float blockDestroySoundCounter = 0.0F;
	private int blockHitWait = 0;
	private MobSpawner mobSpawner;

	public PlayerControllerSP(Minecraft var1) {
		super(var1);
	}

	public final boolean sendBlockRemoved(int var1, int var2, int var3) {
		int var4 = this.mc.theWorld.getBlockId(var1, var2, var3);
		byte var5 = this.mc.theWorld.getBlockMetadata(var1, var2, var3);
		boolean var6 = super.sendBlockRemoved(var1, var2, var3);
		EntityPlayerSP var7 = this.mc.thePlayer;
		ItemStack var9 = var7.inventory.getCurrentItem();
		if(var9 != null) {
			Item.itemsList[var9.itemID].onBlockDestroyed(var9);
			if(var9.stackSize == 0) {
				this.mc.thePlayer.destroyCurrentEquippedItem();
			}
		}

		if(var6 && this.mc.thePlayer.canHarvestBlock(Block.blocksList[var4])) {
			Block.blocksList[var4].dropBlockAsItem(this.mc.theWorld, var1, var2, var3, var5);
		}

		return var6;
	}

	public final void clickBlock(int var1, int var2, int var3) {
		int var4 = this.mc.theWorld.getBlockId(var1, var2, var3);
		if(var4 > 0 && Block.blocksList[var4].blockStrength(this.mc.thePlayer) >= 1.0F) {
			this.sendBlockRemoved(var1, var2, var3);
		}

	}

	public final void resetBlockRemoving() {
		this.curBlockDamage = 0.0F;
		this.blockHitWait = 0;
	}

	public final void sendBlockRemoving(int var1, int var2, int var3, int var4) {
		if(this.blockHitWait > 0) {
			--this.blockHitWait;
		} else {
			super.sendBlockRemoving(var1, var2, var3, var4);
			if(var1 == this.curBlockX && var2 == this.curBlockY && var3 == this.curBlockZ) {
				var4 = this.mc.theWorld.getBlockId(var1, var2, var3);
				if(var4 != 0) {
					Block var6 = Block.blocksList[var4];
					this.curBlockDamage += var6.blockStrength(this.mc.thePlayer);
					if(this.blockDestroySoundCounter % 4.0F == 0.0F && var6 != null) {
						SoundManager var10000 = this.mc.sndManager;
						String var10001 = var6.stepSound.stepSoundDir2();
						float var10002 = (float)var1 + 0.5F;
						float var10003 = (float)var2 + 0.5F;
						float var10004 = (float)var3 + 0.5F;
						StepSound var5 = var6.stepSound;
						float var10005 = (var5.soundVolume + 1.0F) / 8.0F;
						var5 = var6.stepSound;
						var10000.playSound(var10001, var10002, var10003, var10004, var10005, var5.soundPitch * 0.5F);
					}

					++this.blockDestroySoundCounter;
					if(this.curBlockDamage >= 1.0F) {
						this.sendBlockRemoved(var1, var2, var3);
						this.curBlockDamage = 0.0F;
						this.prevBlockDamage = 0.0F;
						this.blockDestroySoundCounter = 0.0F;
						this.blockHitWait = 5;
					}

				}
			} else {
				this.curBlockDamage = 0.0F;
				this.prevBlockDamage = 0.0F;
				this.blockDestroySoundCounter = 0.0F;
				this.curBlockX = var1;
				this.curBlockY = var2;
				this.curBlockZ = var3;
			}
		}
	}

	public final void setPartialTime(float var1) {
		if(this.curBlockDamage <= 0.0F) {
			this.mc.renderGlobal.damagePartialTime = 0.0F;
		} else {
			var1 = this.prevBlockDamage + (this.curBlockDamage - this.prevBlockDamage) * var1;
			this.mc.renderGlobal.damagePartialTime = var1;
		}
	}

	public final float getBlockReachDistance() {
		return 4.0F;
	}

	public final void onWorldChange(World var1) {
		super.onWorldChange(var1);
		this.mobSpawner = new MobSpawner(var1);
	}

	public final void onUpdate() {
		this.prevBlockDamage = this.curBlockDamage;
		this.mobSpawner.performSpawning();
	}
}
