package com.ametfps;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

/** Amet Fps Booster - sadece FPS. Sag Shift ile menu acilir. */
public class AmetFpsClient implements ClientModInitializer {

    public static final String MOD_ID = "ametfpsbooster";
    public static KeyMapping MENU_KEY;

    @Override
    public void onInitializeClient() {
        Config.load();

        KeyMapping.Category category = KeyMapping.Category.register(
            Identifier.fromNamespaceAndPath(MOD_ID, "main"));
        MENU_KEY = KeyMappingHelper.registerKeyMapping(new KeyMapping(
            "key.ametfpsbooster.menu", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_RIGHT_SHIFT, category));

        // Kayitli ayarlari oyun tamamen acildiktan sonra uygula (options hazir olsun).
        ClientLifecycleEvents.CLIENT_STARTED.register(mc -> {
            for (Module m : Modules.ALL) m.setState(Config.savedState(m.id), false);
        });

        ClientTickEvents.END_CLIENT_TICK.register(AmetFpsClient::tick);
        ClientLifecycleEvents.CLIENT_STOPPING.register(Booster::shutdown);
    }

    private static void tick(Minecraft mc) {
        Booster.tick(mc);
        if (mc.player == null) return;
        while (MENU_KEY.consumeClick()) {
            mc.gui.setScreen(new AmetMenuScreen());
        }
    }
}
