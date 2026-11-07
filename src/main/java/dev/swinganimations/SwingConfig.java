package dev.swinganimations;

import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public final class SwingConfig {
    public static boolean enabled = true;
    public static SwingStyle style = SwingStyle.Smooth;
    public static int speed = 70;      // 25..200
    public static int strength = 100;  // 20..200
    public static boolean noEquip = true;
    public static int scale = 100;     // 40..160
    public static int offsetX = 0;     // -60..60
    public static int offsetY = 0;
    public static int offsetZ = 0;

    private SwingConfig() {}

    private static Path path() {
        return FabricLoader.getInstance().getConfigDir().resolve("swinganimations.properties");
    }

    private static int clamp(String v, int def, int min, int max) {
        try {
            return Math.max(min, Math.min(max, Integer.parseInt(v.trim())));
        } catch (RuntimeException e) {
            return def;
        }
    }

    public static void load() {
        Properties p = new Properties();
        try (InputStream in = Files.newInputStream(path())) {
            p.load(in);
        } catch (IOException e) {
            return;
        }
        enabled = Boolean.parseBoolean(p.getProperty("enabled", "true"));
        noEquip = Boolean.parseBoolean(p.getProperty("noEquip", "true"));
        try {
            style = SwingStyle.valueOf(p.getProperty("style", "Smooth"));
        } catch (IllegalArgumentException e) {
            style = SwingStyle.Smooth;
        }
        speed = clamp(p.getProperty("speed", "70"), 70, 25, 200);
        strength = clamp(p.getProperty("strength", "100"), 100, 20, 200);
        scale = clamp(p.getProperty("scale", "100"), 100, 40, 160);
        offsetX = clamp(p.getProperty("offsetX", "0"), 0, -60, 60);
        offsetY = clamp(p.getProperty("offsetY", "0"), 0, -60, 60);
        offsetZ = clamp(p.getProperty("offsetZ", "0"), 0, -60, 60);
    }

    public static void save() {
        Properties p = new Properties();
        p.setProperty("enabled", String.valueOf(enabled));
        p.setProperty("noEquip", String.valueOf(noEquip));
        p.setProperty("style", style.name());
        p.setProperty("speed", String.valueOf(speed));
        p.setProperty("strength", String.valueOf(strength));
        p.setProperty("scale", String.valueOf(scale));
        p.setProperty("offsetX", String.valueOf(offsetX));
        p.setProperty("offsetY", String.valueOf(offsetY));
        p.setProperty("offsetZ", String.valueOf(offsetZ));
        try (OutputStream out = Files.newOutputStream(path())) {
            p.store(out, "Swing Animations");
        } catch (IOException ignored) {
        }
    }
}
