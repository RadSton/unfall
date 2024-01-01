package io.radston12.unfall.item.custom;

import io.radston12.unfall.item.api.UnfallItem;
import io.radston12.unfall.menus.BlockCompressingManagerMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class CompressedBlockOwnerManager extends UnfallItem implements MenuProvider {

    public CompressedBlockOwnerManager() {
        super(new Properties());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        if(level.isClientSide()) return InteractionResultHolder.pass(player.getItemInHand(interactionHand));

        NetworkHooks.openScreen((ServerPlayer) player, this);

        return InteractionResultHolder.success(player.getItemInHand(interactionHand));
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("item.unfall.compressed_block_owner_manager");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
        return new BlockCompressingManagerMenu(containerId, inventory);
    }
}
