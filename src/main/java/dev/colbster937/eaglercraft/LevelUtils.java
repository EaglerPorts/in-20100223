package dev.colbster937.eaglercraft;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import net.lax1dude.eaglercraft.EagRuntime;
import net.lax1dude.eaglercraft.internal.FileChooserResult;
import net.lax1dude.eaglercraft.internal.vfs2.VFile2;
import net.minecraft.client.Minecraft;
import net.minecraft.client.PlayerLoader;
import net.minecraft.game.level.World;

public class LevelUtils {
  private static Minecraft mc;
  private static PlayerLoader pl;
  private static VFile2 lf;

  public static void init(Minecraft imc) {
    mc = imc;
    pl = new PlayerLoader(mc, mc.loadingScreen);
    lf = new VFile2(mc.mcDataDir, "level.mclevel");
  }

  public static void tick() {
    if (EagRuntime.fileChooserHasResult()) {
      FileChooserResult result = EagRuntime.getFileChooserResult();
      if (result.fileName.endsWith(".mclevel")) {
        loadLevel(result.fileData);
      } else {
        EagRuntime.clearFileChooserResult();
        EagRuntime.showPopup("Please choose a valid indev level!");
      }
    }
  }

  public static void export() {
    try {
      ByteArrayOutputStream baos = new ByteArrayOutputStream(1 << 20);
      pl.save(mc.theWorld, baos);
      baos.close();
      EagRuntime.downloadFileWithName("level.mclevel", baos.toByteArray());
    } catch (Exception e) {
      e.printStackTrace();
      EagRuntime.showPopup(e.getMessage());
    }
  }

  public static void save() {
    try {
      pl.save(mc.theWorld, lf.getOutputStream());
    } catch (Exception e) {
      e.printStackTrace();
      EagRuntime.showPopup(e.getMessage());
    }
  }

  public static void load() {
    if (savedLevel())
      loadLevel(lf.getAllBytes());
    // EagRuntime.displayFileChooser("minecraft/mclevel", ".mclevel");
  }

  private static void loadLevel(byte[] data) {
    try {
      ByteArrayInputStream bais = new ByteArrayInputStream(data);
      World world = pl.load(bais);
      bais.close();
      mc.setLevel(world);
      mc.displayGuiScreen(null);
    } catch (Exception e) {
      e.printStackTrace();
      EagRuntime.showPopup(e.getMessage());
    }
  }

  public static boolean savedLevel() {
    return lf.exists();
  }
}
