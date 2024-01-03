package io.radston12.reddefense.blockentities.custom;

import io.radston12.reddefense.blockentities.ModBlockEntities;
import io.radston12.reddefense.blocks.entities.owned.KeypadBlock;
import io.radston12.reddefense.menus.KeypadMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class KeypadBlockEntity extends OwnableBlockEntity implements MenuProvider {

    private String pwd = "";

    public KeypadBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.KEYPAD.get(), pos, state);
    }

    @Override
    public void load(CompoundTag tag) {
        pwd = tag.getString("key");
        super.load(tag);
    }

    public void enable(Player p) {
        if (!level.isClientSide() && getBlockState().getBlock() instanceof KeypadBlock block)
            block.enablePower(getBlockPos(), level, getBlockState(), p);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        tag.putString("key", pwd);
        super.saveAdditional(tag);
    }

    public String getPassword() {
        return pwd;
    }

    public void setPassword(String pwd) {
        this.pwd = pwd;
    }

    public boolean isSetting() {
        return this.pwd.isEmpty();
    }

    public boolean isCorrect(String strg) {
        return this.pwd.equals(strg);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("menu.reddefense.keypad");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory inv, Player player) {
        return new KeypadMenu(containerId, inv, this, isSetting());
    }
}
