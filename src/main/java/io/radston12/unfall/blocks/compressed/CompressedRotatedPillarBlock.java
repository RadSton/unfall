package io.radston12.unfall.blocks.compressed;

import io.radston12.unfall.UnfallMod;
import io.radston12.unfall.datagen.ModBlockStateProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.VariantBlockStateBuilder;
import net.minecraftforge.registries.RegistryObject;

public class CompressedRotatedPillarBlock extends BaseCompressedBlock {

    public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.AXIS;

    private boolean sameTextureOnTop = false;

    private String end = null, side = null;

    private Block crafting;

    @Override
    public Block getCraftingBlock() {
        if(crafting != null) return crafting;
        return getVanillaBlock();
    }

    public CompressedRotatedPillarBlock(String name, Block vanilla, boolean isSameTextureOnTop, Block crafting) {
        this(name, vanilla);
        this.sameTextureOnTop = isSameTextureOnTop;
        this.crafting = crafting;
    }

    public CompressedRotatedPillarBlock(String name, Block vanilla, boolean isSameTextureOnTop) {
        this(name, vanilla);
        this.sameTextureOnTop = isSameTextureOnTop;
    }

    public CompressedRotatedPillarBlock(String name, Block vanilla, String end, String side) {
        this(name, vanilla);
        this.sameTextureOnTop = true;
        this.end = end;
        this.side = side;
    }

    public CompressedRotatedPillarBlock(String name, Block vanilla) {
        super(name, vanilla, BlockBehaviour.Properties.of().mapColor(MapColor.PODZOL).strength(-1.0F, Float.MAX_VALUE));

        registerDefaultState(defaultBlockState().setValue(AXIS, Direction.Axis.Y));
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotat) {
        return switch (rotat) {
            case COUNTERCLOCKWISE_90, CLOCKWISE_90 -> switch (state.getValue(AXIS)) {
                case X -> state.setValue(AXIS, Direction.Axis.Z);
                case Z -> state.setValue(AXIS, Direction.Axis.X);
                default -> state;
            };
            default -> state;
        };
    }


    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AXIS);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(AXIS, context.getClickedFace().getAxis());
    }

    @Override
    public void generateBlockStateProviderData(ModBlockStateProvider provider, RegistryObject<? extends Block> block) {
        try {
            VariantBlockStateBuilder variantBuilder = provider.getVariantBuilder(block.get());
            // models
            String sideTex = (side != null) ? side : provider.blockTexture(getVanillaBlock()).toString();
            String endTex = (end != null) ? end : provider.blockTexture(getVanillaBlock()).toString() + (sameTextureOnTop ? "" : "_top");

            ConfiguredModel model =
                    new ConfiguredModel(provider.models()
                            .withExistingParent(
                                    getRegistryName(),
                                    new ResourceLocation(
                                            UnfallMod.MOD_ID,
                                            "block/compressed_column"
                                    )
                            )
                            .texture("side",
                                    sideTex
                            ).texture("end",
                                    endTex
                            )
                    );

        /*

        axisBlock(block, blockTexture(block), extend(blockTexture(block), "_top"));

        ->
        axisBlock(block,
            models().cubeColumn(name(block), side, end),
            models().cubeColumnHorizontal(name(block) + "_horizontal", side, end));

            ->

            getVariantBuilder(block)
            .partialState().with(RotatedPillarBlock.AXIS, Axis.Y)
                .modelForState().modelFile(vertical).addModel()
            .partialState().with(RotatedPillarBlock.AXIS, Axis.Z)
                .modelForState().modelFile(horizontal).rotationX(90).addModel()
            .partialState().with(RotatedPillarBlock.AXIS, Axis.X)
                .modelForState().modelFile(horizontal).rotationX(90).rotationY(90).addModel();
         */


            variantBuilder
                    .partialState()
                    .with(RotatedPillarBlock.AXIS, Direction.Axis.Y)
                    .modelForState()
                    .modelFile(model.model)
                    .addModel()
                    .partialState()
                    .with(RotatedPillarBlock.AXIS, Direction.Axis.Z)
                    .modelForState().modelFile(model.model).rotationX(90).addModel()
                    .partialState()
                    .with(RotatedPillarBlock.AXIS, Direction.Axis.X)
                    .modelForState().modelFile(model.model).rotationX(90).rotationY(90).addModel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getTextureFromSides(Direction direction) {
        return switch (direction) {
            case UP, DOWN -> getVanillaBlock().getDescriptionId() + "_top";
            default -> getVanillaBlock().getDescriptionId();
        };
    }
}
