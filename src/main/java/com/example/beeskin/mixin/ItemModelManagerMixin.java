package com.example.beeskin.mixin;

import com.example.beeskin.BeeSkinClient;
import net.minecraft.client.item.ItemModelManager;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ItemModelManager.class)
public class ItemModelManagerMixin {
    /**
     * Swaps the item_model component on a client-side COPY of the stack right before rendering.
     * The real item and the server never see the change.
     */
    @ModifyVariable(method = "update", at = @At("HEAD"), argsOnly = true)
    private ItemStack beeskin$swapModel(ItemStack stack) {
        if (!BeeSkinClient.isSkinned(stack)) return stack;
        ItemStack copy = stack.copy();
        copy.set(DataComponentTypes.ITEM_MODEL, BeeSkinClient.BEE_SPEAR);
        return copy;
    }
}
