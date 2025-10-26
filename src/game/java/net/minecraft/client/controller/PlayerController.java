package net.minecraft.client.controller;

import net.minecraft.client.Minecraft;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.level.World;
import net.minecraft.game.level.block.Block;
import net.minecraft.game.level.block.StepSound;

public class PlayerController {
	protected final Minecraft mc;
	public boolean isInTestMode = false;

	public PlayerController(Minecraft var1) {
		this.mc = var1;
	}

	public void onWorldChange(World var1) {
	}

	public void clickBlock(int var1, int var2, int var3) {
		this.sendBlockRemoved(var1, var2, var3);
	}

	public boolean sendBlockRemoved(int var1, int var2, int var3) {
		this.mc.effectRenderer.addBlockDestroyEffects(var1, var2, var3);
		World var4 = this.mc.theWorld;
		Block var5 = Block.blocksList[var4.getBlockId(var1, var2, var3)];
		byte var6 = var4.getBlockMetadata(var1, var2, var3);
		boolean var7 = var4.setBlockWithNotify(var1, var2, var3, 0);
		if(var5 != null && var7) {
			SoundManager var10000 = this.mc.sndManager;
			String var10001 = var5.stepSound.stepSoundDir();
			float var10002 = (float)var1 + 0.5F;
			float var10003 = (float)var2 + 0.5F;
			float var10004 = (float)var3 + 0.5F;
			StepSound var8 = var5.stepSound;
			float var10005 = (var8.soundVolume + 1.0F) / 2.0F;
			var8 = var5.stepSound;
			var10000.playSound(var10001, var10002, var10003, var10004, var10005, var8.soundPitch * 0.8F);
			var5.onBlockDestroyedByPlayer(var4, var1, var2, var3, var6);
		}

		return var7;
	}

	public void sendBlockRemoving(int var1, int var2, int var3, int var4) {
	}

	public void resetBlockRemoving() {
	}

	public void setPartialTime(float var1) {
	}

	public float getBlockReachDistance() {
		return 5.0F;
	}

	public void onUpdate() {
	}

	public boolean shouldDrawHUD() {
		return true;
	}

	public void onRespawn(EntityPlayer var1) {
	}
}
