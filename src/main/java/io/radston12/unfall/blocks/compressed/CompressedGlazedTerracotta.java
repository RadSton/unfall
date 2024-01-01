package io.radston12.unfall.blocks.compressed;

import io.radston12.unfall.UnfallMod;
import io.radston12.unfall.datagen.ModBlockStateProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.VariantBlockStateBuilder;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

public class CompressedGlazedTerracotta extends BaseCompressedBlock{

    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
    public CompressedGlazedTerracotta(String name, Block vanilla) {
        super(name, vanilla);

        registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.EAST));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateBui) {
        stateBui.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext placeCon) {
        return this.defaultBlockState().setValue(FACING, placeCon.getHorizontalDirection().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    public void generateBlockStateProviderData(ModBlockStateProvider provider, RegistryObject<? extends Block> block) {
            VariantBlockStateBuilder variantBuilder = provider.getVariantBuilder(block.get());
            // models

            ConfiguredModel model =
                    new ConfiguredModel(provider.models()
                            .withExistingParent(
                                    getRegistryName(),
                                    new ResourceLocation(
                                            UnfallMod.MOD_ID,
                                            "block/compressed_glazed_terracotta"
                                    )
                            )
                            .texture("pattern",
                                    provider.blockTexture(getVanillaBlock())
                            )
                    );

            variantBuilder
                    .partialState()
                    .with(HorizontalDirectionalBlock.FACING, Direction.SOUTH)
                    .modelForState()
                    .modelFile(model.model)
                    .addModel()
                    .partialState()
                    .with(HorizontalDirectionalBlock.FACING, Direction.WEST)
                    .modelForState().modelFile(model.model).rotationY(90).addModel()
                    .partialState()
                    .with(HorizontalDirectionalBlock.FACING, Direction.NORTH)
                    .modelForState().modelFile(model.model).rotationY(180).addModel()
                    .partialState()
                    .with(HorizontalDirectionalBlock.FACING, Direction.EAST)
                    .modelForState().modelFile(model.model).rotationY(270).addModel();
    }

    @Override
    public String getTextureFromSides(Direction direction) {
        return getVanillaBlock().getDescriptionId();
    }
}
