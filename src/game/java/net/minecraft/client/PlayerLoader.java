package net.minecraft.client;

import net.minecraft.client.player.EntityPlayerSP;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.level.LevelLoader;
import net.minecraft.game.level.World;
import util.IProgressUpdate;

public final class PlayerLoader extends LevelLoader {
	private Minecraft mc;

	public PlayerLoader(Minecraft var1, IProgressUpdate var2) {
		super(var2);
		this.mc = var1;
	}

	protected final Entity loadEntity(World var1, String var2) {
		return (Entity)(var2.equals("LocalPlayer") ? new EntityPlayerSP(this.mc, var1, this.mc.session) : super.loadEntity(var1, var2));
	}
}
