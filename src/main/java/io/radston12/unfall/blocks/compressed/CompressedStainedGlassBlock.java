package io.radston12.unfall.blocks.compressed;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BeaconBeamBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class CompressedStainedGlassBlock extends CompressedGlassBlock implements BeaconBeamBlock {

    private final DyeColor color;

    public CompressedStainedGlassBlock(String name, Block vanilla, DyeColor color) {
        super(name, vanilla);

        this.color = color;
    }

    @Override
    public float[] getBeaconColorMultiplier(BlockState state, LevelReader level, BlockPos pos, BlockPos beaconPos) {
        return color.getTextureDiffuseColors();
    }

    @Override
    public DyeColor getColor() {
        return color;
    }
}
