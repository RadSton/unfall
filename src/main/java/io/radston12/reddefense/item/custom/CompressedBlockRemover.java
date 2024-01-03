package io.radston12.reddefense.item.custom;

import io.radston12.reddefense.blockentities.custom.OwnableBlockEntity;
import io.radston12.reddefense.blocks.api.OwnableBlock;
import io.radston12.reddefense.item.api.UnfallItem;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class CompressedBlockRemover extends UnfallItem {

    public CompressedBlockRemover() {
        super(new Properties());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        if (level.isClientSide()) return InteractionResultHolder.success(player.getItemInHand(interactionHand));

        BlockPos pos = getPlayerPOVHitResult(level, player, ClipContext.Fluid.ANY).getBlockPos();
        BlockState state = level.getBlockState(pos);
        Block block = state.getBlock();

        if (block instanceof OwnableBlock) {
            OwnableBlockEntity ownableBlock = (OwnableBlockEntity) level.getBlockEntity(pos);

            if(ownableBlock.isOwner(player))
                level.destroyBlock(pos, true, player);
            else
                player.displayClientMessage(Component.translatable("error.reddefense.notyourblock"), true);

        } else
            player.displayClientMessage(Component.translatable("error.reddefense.notcompressed"), true);

        return InteractionResultHolder.success(player.getItemInHand(interactionHand));
    }
}
