package com.example.beeskin;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

public class BeeSkinClient implements ClientModInitializer {
    public static final Identifier BEE_SPEAR = Identifier.of("beeskin", "bee_spear");

    /** Item ids (e.g. "minecraft:diamond_sword") that currently wear the bee spear skin. */
    private static final Set<String> SKINNED = new HashSet<>();
    private static final Path CONFIG = FabricLoader.getInstance().getConfigDir().resolve("beeskin.properties");

    private static KeyBinding openKey;

    @Override
    public void onInitializeClient() {
        load();
        openKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.beeskin.open", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_B, "key.category.beeskin"));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openKey.wasPressed()) {
                if (client.player == null || client.currentScreen != null) continue;
                ItemStack held = client.player.getMainHandStack();
                if (held.isIn(ItemTags.SWORDS)) {
                    client.setScreen(new SkinScreen(held.copy()));
                } else {
                    client.player.sendMessage(Text.literal("Hold a sword to change its skin."), true);
                }
            }
        });
    }

    public static boolean isSkinned(ItemStack stack) {
        if (stack.isEmpty() || SKINNED.isEmpty()) return false;
        return SKINNED.contains(Registries.ITEM.getId(stack.getItem()).toString());
    }

    public static void setSkinned(ItemStack stack, boolean on) {
        String id = Registries.ITEM.getId(stack.getItem()).toString();
        if (on) SKINNED.add(id); else SKINNED.remove(id);
        save();
    }

    private static void load() {
        if (!Files.exists(CONFIG)) return;
        try (InputStream in = Files.newInputStream(CONFIG)) {
            Properties p = new Properties();
            p.load(in);
            for (String s : p.getProperty("skinned", "").split(",")) if (!s.isBlank()) SKINNED.add(s.trim());
        } catch (IOException ignored) {}
    }

    private static void save() {
        try (OutputStream out = Files.newOutputStream(CONFIG)) {
            Properties p = new Properties();
            p.setProperty("skinned", String.join(",", SKINNED));
            p.store(out, "Bee Skin config");
        } catch (IOException ignored) {}
    }
}
