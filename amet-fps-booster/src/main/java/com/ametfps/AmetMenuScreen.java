package com.ametfps;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.Component;

import java.util.LinkedHashMap;
import java.util.Map;

public class AmetMenuScreen extends Screen {

    private final Map<Module, Button> moduleButtons = new LinkedHashMap<>();
    private Button adaptiveButton;
    private Button backgroundButton;
    private Button targetButton;

    public AmetMenuScreen() {
        super(Component.literal("Amet Fps Booster"));
    }

    @Override
    protected void init() {
        moduleButtons.clear();
        int cx = this.width / 2;
        int y = Math.max(8, this.height / 2 - 95);

        Button title = Button.builder(
            Component.literal("Amet Fps Booster").withStyle(ChatFormatting.AQUA, ChatFormatting.BOLD),
            b -> {}).bounds(cx - 170, y, 340, 20).build();
        title.active = false;
        this.addRenderableWidget(title);

        // Presetler
        y += 24;
        addPreset("Dengeli", Modules.PRESET_BALANCED, null, cx - 170, y);
        addPreset("Max", Modules.PRESET_MAX, null, cx - 84, y);
        addPreset("EXTREME", Modules.PRESET_EXTREME, ChatFormatting.RED, cx + 2, y);
        addPreset("Kapat", Modules.PRESET_OFF, null, cx + 88, y);

        // 3x3 ayar izgarasi
        y += 24;
        int i = 0;
        for (Module m : Modules.ALL) {
            int col = i % 3;
            int row = i / 3;
            Button btn = Button.builder(Component.empty(), b -> {
                m.cycle();
                refresh();
            }).bounds(cx - 170 + col * 115, y + row * 22, 110, 20).build();
            moduleButtons.put(m, this.addRenderableWidget(btn));
            i++;
        }
        y += ((Modules.ALL.size() + 2) / 3) * 22 + 2;

        // Adaptif / arka plan / hedef FPS
        adaptiveButton = this.addRenderableWidget(Button.builder(Component.empty(), b -> {
            Config.adaptive = !Config.adaptive;
            Booster.resetAdaptive();
            Config.save();
            refresh();
        }).bounds(cx - 170, y, 110, 20).build());
        backgroundButton = this.addRenderableWidget(Button.builder(Component.empty(), b -> {
            Config.background = !Config.background;
            Config.save();
            refresh();
        }).bounds(cx - 55, y, 110, 20).build());
        targetButton = this.addRenderableWidget(Button.builder(Component.empty(), b -> {
            int next = Config.TARGETS[0];
            for (int k = 0; k < Config.TARGETS.length; k++) {
                if (Config.TARGETS[k] == Config.target) {
                    next = Config.TARGETS[(k + 1) % Config.TARGETS.length];
                    break;
                }
            }
            Config.target = next;
            Config.save();
            refresh();
        }).bounds(cx + 60, y, 110, 20).build());

        y += 26;
        this.addRenderableWidget(Button.builder(Component.literal("Kapat"), b -> this.onClose())
            .bounds(cx - 50, y, 100, 20).build());

        refresh();
    }

    private void addPreset(String name, int[] states, ChatFormatting color, int x, int y) {
        Component label = Component.literal(name);
        if (color != null) label = Component.literal(name).withStyle(color, ChatFormatting.BOLD);
        this.addRenderableWidget(Button.builder(label, b -> {
            Modules.applyPreset(states);
            refresh();
        }).bounds(x, y, 82, 20).build());
    }

    private void refresh() {
        for (Map.Entry<Module, Button> e : moduleButtons.entrySet()) {
            Module m = e.getKey();
            String text = m.valueLabels == null ? m.label : m.label + ": " + m.valueText();
            e.getValue().setMessage(Component.literal(text)
                .withStyle(m.isOn() ? ChatFormatting.GREEN : ChatFormatting.WHITE));
        }
        adaptiveButton.setMessage(Component.literal("Adaptif")
            .withStyle(Config.adaptive ? ChatFormatting.GREEN : ChatFormatting.WHITE));
        backgroundButton.setMessage(Component.literal("Arka Plan Limiti")
            .withStyle(Config.background ? ChatFormatting.GREEN : ChatFormatting.WHITE));
        targetButton.setMessage(Component.literal("Hedef FPS: " + Config.target));
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        if (AmetFpsClient.MENU_KEY.matches(event)) {
            this.onClose();
            return true;
        }
        return super.keyPressed(event);
    }

    // Menu acikken oyun durmasin.
    public boolean isPauseScreen() {
        return false;
    }
}
