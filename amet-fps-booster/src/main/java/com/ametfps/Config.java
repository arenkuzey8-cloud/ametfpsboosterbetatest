package com.ametfps;

import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public final class Config {
    private Config() {}

    public static final int[] TARGETS = {60, 90, 120, 144, 240};

    public static boolean adaptive = true;    // FPS dusukse render distance'i otomatik azalt
    public static boolean background = true;  // pencere arkadayken FPS'i 20'ye sinirla
    public static int target = 60;            // adaptif hedef FPS

    private static final Properties saved = new Properties();

    private static Path file() {
        return FabricLoader.getInstance().getConfigDir().resolve("ametfpsbooster.properties");
    }

    public static void load() {
        Path f = file();
        if (!Files.exists(f)) {
            // Ilk acilista dengeli boost acik gelsin.
            for (int i = 0; i < Modules.PRESET_BALANCED.length; i++) {
                saved.setProperty("module." + Modules.ALL.get(i).id, Integer.toString(Modules.PRESET_BALANCED[i]));
            }
            return;
        }
        try (Reader r = Files.newBufferedReader(f)) {
            saved.load(r);
        } catch (IOException e) {
            return;
        }
        adaptive = Boolean.parseBoolean(saved.getProperty("adaptive", "true"));
        background = Boolean.parseBoolean(saved.getProperty("background", "true"));
        try {
            target = Integer.parseInt(saved.getProperty("target", "60"));
        } catch (NumberFormatException ignored) {}
    }

    public static int savedState(String id) {
        try {
            return Integer.parseInt(saved.getProperty("module." + id, "0"));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static void save() {
        Properties p = new Properties();
        p.setProperty("adaptive", Boolean.toString(adaptive));
        p.setProperty("background", Boolean.toString(background));
        p.setProperty("target", Integer.toString(target));
        for (Module m : Modules.ALL) p.setProperty("module." + m.id, Integer.toString(m.state()));
        try (Writer w = Files.newBufferedWriter(file())) {
            p.store(w, "Amet Fps Booster");
        } catch (IOException ignored) {}
    }
}
