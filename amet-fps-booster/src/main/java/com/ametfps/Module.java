package com.ametfps;

import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;

import java.util.ArrayList;
import java.util.List;

/**
 * Tek bir FPS ayari. state 0 = kapali (orijinal deger), 1.. = secilen deger.
 * valueLabels == null ise basit ac/kapat (state 0/1).
 */
public final class Module {

    public interface Applier {
        void apply(Options options, int state, List<Runnable> undo);
    }

    public final String id;
    public final String label;
    public final String[] valueLabels;
    private final Applier applier;
    private final List<Runnable> undo = new ArrayList<>();
    private int state;

    public Module(String id, String label, Applier applier) {
        this(id, label, null, applier);
    }

    public Module(String id, String label, String[] valueLabels, Applier applier) {
        this.id = id;
        this.label = label;
        this.valueLabels = valueLabels;
        this.applier = applier;
    }

    public int states() {
        return valueLabels == null ? 2 : valueLabels.length + 1;
    }

    public int state() {
        return state;
    }

    public boolean isOn() {
        return state > 0;
    }

    public String valueText() {
        if (state == 0) return "Kapalı";
        return valueLabels == null ? "Açık" : valueLabels[state - 1];
    }

    public void cycle() {
        setState((state + 1) % states(), true);
    }

    public void setState(int newState, boolean persist) {
        if (newState < 0 || newState >= states() || newState == state) return;
        revert();
        state = newState;
        if (newState > 0) applier.apply(Minecraft.getInstance().options, newState, undo);
        if (id.equals("renderdist")) Booster.resetAdaptive();
        if (persist) Config.save();
    }

    /** Eski degerleri geri yukler (state'e dokunmaz). */
    public void revert() {
        for (int i = undo.size() - 1; i >= 0; i--) undo.get(i).run();
        undo.clear();
    }

    public static <T> void set(List<Runnable> undo, OptionInstance<T> option, T value) {
        T old = option.get();
        undo.add(() -> option.set(old));
        option.set(value);
    }
}
