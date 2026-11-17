package com.ametfps;

import net.minecraft.server.level.ParticleStatus;

import java.util.List;

public final class Modules {
    private Modules() {}

    private static final int[] RENDER = {2, 4, 6, 8, 10, 12};
    private static final int[] SIMULATION = {5, 6, 8, 10};
    private static final double[] ENTITY = {0.5D, 0.75D};

    // Siralama menudeki 3x3 izgarayla ayni.
    public static final List<Module> ALL = List.of(
        new Module("renderdist", "Render",
            new String[]{"2", "4", "6", "8", "10", "12"},
            (o, s, u) -> Module.set(u, o.renderDistance(), RENDER[s - 1])),
        new Module("simdist", "Simulation",
            new String[]{"5", "6", "8", "10"},
            (o, s, u) -> Module.set(u, o.simulationDistance(), SIMULATION[s - 1])),
        new Module("entitydist", "Entity",
            new String[]{"%50", "%75"},
            (o, s, u) -> Module.set(u, o.entityDistanceScaling(), ENTITY[s - 1])),

        new Module("particles", "Parçacık",
            new String[]{"Az", "Minimal"},
            (o, s, u) -> Module.set(u, o.particles(), s == 1 ? ParticleStatus.DECREASED : ParticleStatus.MINIMAL)),
        new Module("shadows", "Gölge Kapat",
            (o, s, u) -> Module.set(u, o.entityShadows(), false)),
        new Module("biomeblend", "Biome Blend 1",
            (o, s, u) -> Module.set(u, o.biomeBlendRadius(), 1)),

        new Module("vsync", "VSync Kapat",
            (o, s, u) -> Module.set(u, o.enableVsync(), false)),
        new Module("unlimitedfps", "Sınırsız FPS",
            (o, s, u) -> Module.set(u, o.framerateLimit(), 260)),
        new Module("smoothlight", "Smooth Light Kapat",
            (o, s, u) -> Module.set(u, o.ambientOcclusion(), false))
    );

    //                                        render sim ent part shad blend vsync unl smooth
    public static final int[] PRESET_BALANCED = {5, 2, 2, 1, 1, 1, 1, 1, 0}; // render 10, sim 6, entity %75
    public static final int[] PRESET_MAX      = {3, 1, 1, 2, 1, 1, 1, 1, 1}; // render 6,  sim 5, entity %50
    public static final int[] PRESET_EXTREME  = {2, 1, 1, 2, 1, 1, 1, 1, 1}; // render 4,  sim 5, entity %50
    public static final int[] PRESET_OFF      = {0, 0, 0, 0, 0, 0, 0, 0, 0};

    public static void applyPreset(int[] states) {
        for (int i = 0; i < ALL.size(); i++) ALL.get(i).setState(states[i], false);
        Config.save();
    }
}
