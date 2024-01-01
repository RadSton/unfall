package io.radston12.reddefense.menus.utils.callbacks;

import net.minecraft.world.item.ItemStack;

public interface ShouldAcceptItem {

    boolean accept(ItemStack stack);
}
