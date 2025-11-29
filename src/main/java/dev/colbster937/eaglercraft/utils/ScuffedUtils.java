package dev.colbster937.eaglercraft.utils;

import org.lwjgl.input.Keyboard;

import net.lax1dude.eaglercraft.EagRuntime;
import net.lax1dude.eaglercraft.HString;

public class ScuffedUtils {
  public static void showZipFileChooser() {
    EagRuntime.displayFileChooser("application/zip", ".zip");
  }

  public static String getFormattedTime(long ticks) {
    long t = (((ticks % 24000) + 24000) % 24000 + 6000) % 24000;
    long h = t / 1000;
    long m = (t % 1000) * 60 / 1000;
    return HString.format("The day is %s at %s", ticks / 24000,
        HString.format("%02d:%02d", h, m));
  }

  public static boolean isCtrlKeyDown() {
    return Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL);
  }

  public static boolean isShiftKeyDown() {
    return Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
  }

  public static boolean isStringEmpty(String str) {
    return str == null || str.isEmpty() || str.isBlank() || str.length() < 1;
  }
}
