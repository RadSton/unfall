package io.radston12.reddefense.menus.utils;

import io.radston12.reddefense.menus.utils.callbacks.ShouldAcceptItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class CustomAcceptingSlot extends SlotItemHandler {

    private ShouldAcceptItem item;

    public CustomAcceptingSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition, ShouldAcceptItem item) {
        super(itemHandler, index, xPosition, yPosition);

        this.item = item;
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return item.accept(stack);
    }
}
