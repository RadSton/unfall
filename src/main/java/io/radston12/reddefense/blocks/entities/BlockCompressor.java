package io.radston12.reddefense.blocks.entities;

import io.radston12.reddefense.blockentities.ModBlockEntities;
import io.radston12.reddefense.blockentities.custom.BlockCompressorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlockCompressor extends BaseEntityBlock {

    public BlockCompressor(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BlockCompressorBlockEntity(pos, state);
    }

    @Override
    public InteractionResult use(BlockState state, Level lvl, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if(lvl.isClientSide()) return InteractionResult.sidedSuccess(true);

        BlockEntity entity = lvl.getBlockEntity(pos);
        if(entity instanceof BlockCompressorBlockEntity)
            NetworkHooks.openScreen((ServerPlayer) player, (BlockCompressorBlockEntity) entity, pos);
        else
            throw new IllegalStateException("Our Container provider is missing!");

        return InteractionResult.sidedSuccess(true);
    }

    @Override
    public @NotNull RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level lvl, BlockState state, BlockEntityType<T> blockEntityType) {
        if(lvl.isClientSide()) return null;

        return createTickerHelper(blockEntityType, ModBlockEntities.BLOCK_COMPRESSING.get(), (level, pos, blockState, blockEntity) -> {
            blockEntity.tick(level, pos, blockState);
        });
    }
}
