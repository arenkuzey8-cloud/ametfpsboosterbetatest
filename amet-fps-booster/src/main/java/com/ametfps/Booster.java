package com.ametfps;

import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;

public final class Booster {
    private Booster() {}

    private static final int MIN_RENDER = 4;
    private static final int BACKGROUND_FPS = 20;

    // adaptif sistem
    private static int baseRender = -1;
    private static int offset;
    private static int ticks;
    private static int sampleCount;
    private static int sampleSum;

    // arka plan limiti
    private static boolean inBackground;
    private static int previousLimit;

    public static int offset() {
        return offset;
    }

    public static void resetAdaptive() {
        offset = 0;
        baseRender = -1;
        ticks = 0;
        sampleCount = 0;
        sampleSum = 0;
    }

    public static void tick(Minecraft mc) {
        backgroundTick(mc);
        adaptiveTick(mc);
    }

    /** Oyun kapanirken tum ayarlari orijinaline dondur ve kaydet. */
    public static void shutdown(Minecraft mc) {
        if (inBackground) {
            mc.options.framerateLimit().set(previousLimit);
            inBackground = false;
        }
        if (offset > 0) {
            OptionInstance<Integer> render = mc.options.renderDistance();
            render.set(Math.min(32, render.get() + offset));
            offset = 0;
        }
        for (Module m : Modules.ALL) m.revert();
        mc.options.save();
    }

    private static void backgroundTick(Minecraft mc) {
        boolean active = mc.isWindowActive();
        if (Config.background && !active && !inBackground) {
            previousLimit = mc.options.framerateLimit().get();
            mc.options.framerateLimit().set(BACKGROUND_FPS);
            inBackground = true;
        } else if (inBackground && (active || !Config.background)) {
            mc.options.framerateLimit().set(previousLimit);
            inBackground = false;
        }
    }

    private static void adaptiveTick(Minecraft mc) {
        if (mc.player == null || !Config.adaptive || inBackground) {
            ticks = 0;
            sampleCount = 0;
            sampleSum = 0;
            return;
        }
        if (++ticks < 20) return; // saniyede bir ornek
        ticks = 0;

        sampleSum += mc.getFps();
        if (++sampleCount < 5) return; // 5 saniyelik ortalama
        int avg = sampleSum / sampleCount;
        sampleSum = 0;
        sampleCount = 0;

        OptionInstance<Integer> render = mc.options.renderDistance();
        int current = render.get();
        if (offset == 0) baseRender = current;

        if (avg < Config.target && current > MIN_RENDER) {
            render.set(current - 1);
            offset++;
        } else if (avg > Config.target * 1.6D && offset > 0 && current < baseRender) {
            render.set(current + 1);
            offset--;
        }
    }
}
