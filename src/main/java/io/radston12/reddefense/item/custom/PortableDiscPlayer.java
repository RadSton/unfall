package io.radston12.reddefense.item.custom;

import io.radston12.reddefense.item.api.UnfallItem;
import io.radston12.reddefense.menus.PortableJukeBoxMenu;
import io.radston12.reddefense.menus.utils.SaveCallbackItemStackHandler;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PortableDiscPlayer extends UnfallItem implements MenuProvider  {

    public PortableDiscPlayer() {
        super(new Properties());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {

        ItemStack stack = player.getItemInHand(interactionHand);

        if(level.isClientSide())
            return InteractionResultHolder.sidedSuccess(stack, true);



        NetworkHooks.openScreen((ServerPlayer) player, this);

        return InteractionResultHolder.success(stack);
    }


    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> componentList, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, level, componentList, tooltipFlag);
    }


    @Override
    public Component getDisplayName() {
        return Component.translatable("item.reddefense.portablediscplayer.display");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerID, Inventory inventory, Player player) {
        return new PortableJukeBoxMenu(containerID, inventory, this);
    }

    @Override
    public @Nullable ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new PortableDiscPlayerCapability(stack);
    }

    static class PortableDiscPlayerCapability implements ICapabilityProvider {

        private final SaveCallbackItemStackHandler itemHandler ;
        private ItemStack stack;

        public PortableDiscPlayerCapability(ItemStack stack) {
            this.stack = stack;

            itemHandler = new SaveCallbackItemStackHandler(10, this::save);
            if(stack.getOrCreateTag().contains("inventory"))
                itemHandler.deserializeNBT(stack.getOrCreateTag().getCompound("inventory"));
        }

        @Override
        public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
            if(cap == ForgeCapabilities.ITEM_HANDLER)
                return LazyOptional.of(() -> itemHandler).cast();
            return LazyOptional.empty();
        }

        @Override
        public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
            return this.getCapability(cap);
        }

        public void save() {
            stack.getOrCreateTag().put("inventory", itemHandler.serializeNBT());
        }

    }
}
