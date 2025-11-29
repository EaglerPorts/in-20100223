package dev.colbster937.eaglercraft.command;

import dev.colbster937.eaglercraft.SingleplayerCommands;
import net.lax1dude.eaglercraft.HString;
import net.minecraft.client.player.EntityPlayerSP;

public class TeleportCommand extends Command {
  public TeleportCommand() {
    super("/teleport", new String[] { "/tp" }, "<x> <y> <z>");
  }

  @Override
  public void run(String[] args) {
    if (args.length == 4) {
      EntityPlayerSP player = this.mc.thePlayer;
      float[] pos = this.getRelativePos(player, args[1], args[2], args[3]);
      this.tpPos(player, pos);
      SingleplayerCommands
          .showChat(HString.format("Teleported to %.3f, %.3f, %.3f", pos[0], pos[1], pos[2]));
    } else {
      this.showUsage(args[0]);
    }
  }

  private float[] getRelativePos(EntityPlayerSP player, String x, String y, String z) {
    return new float[] { this.getRelativeCoord(x, player.posX), this.getRelativeCoord(y, player.posY),
        this.getRelativeCoord(z, player.posZ) };
  }

  private void tpPos(EntityPlayerSP player, float[] pos) {
    player.motionX = 0.0F;
    player.motionY = 0.0F;
    player.motionZ = 0.0F;
    player.setPosition(pos[0], pos[1], pos[2]);
    player.prevPosX = pos[0];
    player.prevPosY = pos[1];
    player.prevPosZ = pos[2];
  }
}