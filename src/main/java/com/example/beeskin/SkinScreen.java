package com.example.beeskin;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

public class SkinScreen extends Screen {
    private final ItemStack stack;

    public SkinScreen(ItemStack stack) {
        super(Text.literal("Sword Skin"));
        this.stack = stack;
    }

    @Override
    protected void init() {
        int cx = this.width / 2;
        int y = this.height / 2 - 20;

        addDrawableChild(ButtonWidget.builder(Text.literal("Bee Spear"),
                b -> BeeSkinClient.setSkinned(stack, true)).dimensions(cx - 100, y, 200, 20).build());

        addDrawableChild(ButtonWidget.builder(Text.literal("Default"),
                b -> BeeSkinClient.setSkinned(stack, false)).dimensions(cx - 100, y + 24, 200, 20).build());

        addDrawableChild(ButtonWidget.builder(Text.literal("Done"), b -> close())
                .dimensions(cx - 100, y + 56, 200, 20).build());
    }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        super.render(ctx, mouseX, mouseY, delta);
        ctx.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, this.height / 2 - 70, 0xFFFFFF);
        ctx.drawItem(stack, this.width / 2 - 8, this.height / 2 - 50); // live preview via the same mixin
    }
}
