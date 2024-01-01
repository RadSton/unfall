package io.radston12.reddefense.menus.utils;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public class SaveCallbackItemStackHandler extends ItemStackHandler {

    public interface Callback { void save(); }

    private final Callback callback;

    public SaveCallbackItemStackHandler(int size, Callback callback) {
        super(size);
        this.callback = callback;
    }

    @Override
    public void setStackInSlot(int slot, @NotNull ItemStack stack) {
        super.setStackInSlot(slot, stack);
        callback.save();
    }

    @Override
    public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        ItemStack toReturn =  super.insertItem(slot, stack, simulate);
        callback.save();
        return toReturn;
    }

    @Override
    public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
        ItemStack toReturn = super.extractItem(slot, amount, simulate);
        callback.save();
        return toReturn;
    }
}
