package com.ametfps;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.Component;

import java.util.LinkedHashMap;
import java.util.Map;

public class AmetMenuScreen extends Screen {

    private static final int PANEL_WIDTH = 410;
    private static final int PANEL_HEIGHT = 286;
    private final Map<Module, Button> moduleButtons = new LinkedHashMap<>();
    private Button adaptiveButton;
    private Button backgroundButton;
    private Button targetButton;

    public AmetMenuScreen() {
        super(Component.literal("Amet FPS Booster"));
    }

    @Override
    protected void init() {
        moduleButtons.clear();

        int cx = this.width / 2;
        int panelLeft = cx - PANEL_WIDTH / 2;
        int contentLeft = cx - 180;
        int y = Math.max(12, (this.height - PANEL_HEIGHT) / 2 + 43);

        // Presetler
        addPreset("DENGELİ", Modules.PRESET_BALANCED, null, contentLeft, y);
        addPreset("MAX", Modules.PRESET_MAX, null, contentLeft + 91, y);
        addPreset("EXTREME", Modules.PRESET_EXTREME, ChatFormatting.RED, contentLeft + 182, y);
        addPreset("KAPALI", Modules.PRESET_OFF, null, contentLeft + 273, y);

        // Modül kartları: 3 sütun, 3 satır
        y += 39;
        int i = 0;
        for (Module m : Modules.ALL) {
            int col = i % 3;
            int row = i / 3;
            Button btn = Button.builder(Component.empty(), b -> {
                m.cycle();
                refresh();
            }).bounds(contentLeft + col * 122, y + row * 29, 116, 25).build();
            moduleButtons.put(m, this.addRenderableWidget(btn));
            i++;
        }

        y += 3 * 29 + 8;
        adaptiveButton = this.addRenderableWidget(Button.builder(Component.empty(), b -> {
            Config.adaptive = !Config.adaptive;
            Booster.resetAdaptive();
            Config.save();
            refresh();
        }).bounds(contentLeft, y, 116, 25).build());

        backgroundButton = this.addRenderableWidget(Button.builder(Component.empty(), b -> {
            Config.background = !Config.background;
            Config.save();
            refresh();
        }).bounds(contentLeft + 122, y, 116, 25).build());

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
        }).bounds(contentLeft + 244, y, 116, 25).build());

        y += 34;
        this.addRenderableWidget(Button.builder(Component.literal("TAMAM"), b -> this.onClose())
            .bounds(cx - 55, y, 110, 24).build());

        refresh();
    }

    @Override
    protected void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.renderBackground(graphics, mouseX, mouseY, partialTick);

        int cx = this.width / 2;
        int top = Math.max(8, (this.height - PANEL_HEIGHT) / 2);
        int left = cx - PANEL_WIDTH / 2;
        int right = cx + PANEL_WIDTH / 2;
        int bottom = top + PANEL_HEIGHT;

        // Koyu, katmanlı panel ve ince vurgu çizgisi
        graphics.fill(left - 2, top - 2, right + 2, bottom + 2, 0xAA07111F);
        graphics.fill(left, top, right, bottom, 0xF0142030);
        graphics.fill(left, top, right, top + 3, 0xFF35C6D4);
        graphics.fill(left + 16, top + 34, right - 16, top + 35, 0xFF30475A);

        graphics.drawCenteredString(this.font,
            Component.literal("AMET").withStyle(ChatFormatting.AQUA, ChatFormatting.BOLD),
            cx - 49, top + 12, 0xFFFFFFFF);
        graphics.drawString(this.font,
            Component.literal("FPS BOOSTER").withStyle(ChatFormatting.WHITE, ChatFormatting.BOLD),
            cx - 13, top + 12, 0xFFFFFFFF);
        graphics.drawCenteredString(this.font,
            Component.literal("PERFORMANS AYARLARI  •  SAĞ SHIFT").withStyle(ChatFormatting.GRAY),
            cx, top + 25, 0xFF9AAFC0);

        graphics.drawString(this.font,
            Component.literal("HAZIR PROFİLLER").withStyle(ChatFormatting.AQUA, ChatFormatting.BOLD),
            left + 20, top + 43, 0xFF75DCE5);
        graphics.drawString(this.font,
            Component.literal("MODÜLLER").withStyle(ChatFormatting.AQUA, ChatFormatting.BOLD),
            left + 20, top + 111, 0xFF75DCE5);
        graphics.drawString(this.font,
            Component.literal("AKILLI SINIRLAR").withStyle(ChatFormatting.AQUA, ChatFormatting.BOLD),
            left + 20, top + 208, 0xFF75DCE5);
    }

    private void addPreset(String name, int[] states, ChatFormatting color, int x, int y) {
        Component label = Component.literal(name);
        if (color != null) label = label.withStyle(color, ChatFormatting.BOLD);
        this.addRenderableWidget(Button.builder(label, b -> {
            Modules.applyPreset(states);
            refresh();
        }).bounds(x, y, 86, 25).build());
    }

    private void refresh() {
        for (Map.Entry<Module, Button> e : moduleButtons.entrySet()) {
            Module m = e.getKey();
            String text = m.valueLabels == null ? m.label : m.label + ": " + m.valueText();
            e.getValue().setMessage(Component.literal(text)
                .withStyle(m.isOn() ? ChatFormatting.GREEN : ChatFormatting.GRAY));
        }
        adaptiveButton.setMessage(Component.literal("Adaptif: " + (Config.adaptive ? "AÇIK" : "KAPALI"))
            .withStyle(Config.adaptive ? ChatFormatting.GREEN : ChatFormatting.GRAY));
        backgroundButton.setMessage(Component.literal("Arka plan: " + (Config.background ? "AÇIK" : "KAPALI"))
            .withStyle(Config.background ? ChatFormatting.GREEN : ChatFormatting.GRAY));
        targetButton.setMessage(Component.literal("Hedef: " + Config.target + " FPS")
            .withStyle(ChatFormatting.AQUA));
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        if (AmetFpsClient.MENU_KEY.matches(event)) {
            this.onClose();
            return true;
        }
        return super.keyPressed(event);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
