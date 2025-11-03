package dev.colbster937.eaglercraft;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import net.lax1dude.eaglercraft.EagRuntime;
import net.lax1dude.eaglercraft.internal.FileChooserResult;
import net.lax1dude.eaglercraft.internal.vfs2.VFile2;
import net.minecraft.client.Minecraft;
import net.minecraft.client.PlayerLoader;
import net.minecraft.game.level.World;

public class LevelUtils {
  private static Minecraft mc;
  private static PlayerLoader pl;
  private static PlayerLoader pla;
  private static VFile2 lf;

  public static boolean levelSaved = false;

  public static void init(Minecraft imc) {
    mc = imc;
    pl = new PlayerLoader(imc, imc.loadingScreen);
    pla = new PlayerLoader(imc, null);
    lf = new VFile2(imc.mcDataDir, "level.mclevel");
  }

  public static void save(OutputStream os, boolean show) {
    try {
      if (show)
        pl.save(mc.theWorld, os);
      else
        pla.save(mc.theWorld, os);
      levelSaved = true;
    } catch (Exception e) {
      e.printStackTrace();
      EagRuntime.showPopup(e.getMessage());
    }
  }

  public static void save(boolean show) {
    save(lf.getOutputStream(), show);
  }

  public static void save() {
    save(true);
  }

  public static void export() {
    try {
      ByteArrayOutputStream baos = new ByteArrayOutputStream(1 << 20);
      save(baos, true);
      baos.close();
      EagRuntime.downloadFileWithName("level.mclevel", baos.toByteArray());
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

  public static void tick(int i) {
    if (i == 1) levelSaved = false;
		else if (i == 20) save(false);
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

  public static void tick() {
    tick(-1);
  }

  public static boolean savedLevel() {
    return lf.exists();
  }
}
