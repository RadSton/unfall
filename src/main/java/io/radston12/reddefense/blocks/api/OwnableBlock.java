package io.radston12.reddefense.blocks.api;

import io.radston12.reddefense.blockentities.custom.OwnableBlockEntity;
import io.radston12.reddefense.blocks.compressed.CompressedDoorBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.storage.LevelResource;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;

public abstract class OwnableBlock extends SilentEntityBlock {


    public OwnableBlock(Properties properties, String name) {
        super(properties, name);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack stack) {
        if (level.isClientSide()) return;

        if (!(entity instanceof Player)) return;

        Player p = (Player) entity;

        BlockEntity bEntity = level.getBlockEntity(pos);

        if(bEntity == null) return; // Impossible but you never know
        if(!(bEntity instanceof OwnableBlockEntity)) return; // Impossible but you never know

        ((OwnableBlockEntity) bEntity).setOwner(p);

    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new OwnableBlockEntity(pos, state);
    }
}
