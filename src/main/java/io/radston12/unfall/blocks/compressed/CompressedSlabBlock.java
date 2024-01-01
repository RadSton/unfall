package io.radston12.unfall.blocks.compressed;

import io.radston12.unfall.UnfallMod;
import io.radston12.unfall.datagen.ModBlockStateProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nullable;

public class CompressedSlabBlock extends BaseCompressedBlock {

    public static final EnumProperty<SlabType> TYPE = BlockStateProperties.SLAB_TYPE;
    protected static final VoxelShape BOTTOM_SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D);
    protected static final VoxelShape TOP_SHAPE = Block.box(0.0D, 8.0D, 0.0D, 16.0D, 16.0D, 16.0D);


    private final Block material;
    public CompressedSlabBlock(String name, Block vanilla, Block material) {
        super(name, vanilla);

        registerDefaultState(stateDefinition.any().setValue(TYPE, SlabType.BOTTOM));

        this.material = material;
    }

    @Override
    public boolean useShapeForLightOcclusion(BlockState state) {
        return state.getValue(TYPE) != SlabType.DOUBLE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(TYPE);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(TYPE)) {
            case DOUBLE -> Shapes.block();
            case TOP -> TOP_SHAPE;
            default -> BOTTOM_SHAPE;
        };
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        Level level = ctx.getLevel();
        BlockPos pos = ctx.getClickedPos();
        BlockState state = level.getBlockState(pos);
        BlockEntity be = level.getBlockEntity(pos);

        if (state.getBlock() == this)
            return state.setValue(TYPE, SlabType.DOUBLE);

        BlockState stateToSet = defaultBlockState().setValue(TYPE, SlabType.BOTTOM);
        Direction dir = ctx.getClickedFace();

        return dir != Direction.DOWN && (dir == Direction.UP || ctx.getClickLocation().y - pos.getY() <= 0.5D) ? stateToSet : stateToSet.setValue(TYPE, SlabType.TOP);

    }

    @Override
    public boolean canBeReplaced(BlockState state, BlockPlaceContext ctx) {
        ItemStack stack = ctx.getItemInHand();
        SlabType type = state.getValue(TYPE);

        if (type != SlabType.DOUBLE && stack.getItem() == asItem()) {
            if (ctx.replacingClickedOnBlock()) {
                boolean clickedUpperHalf = ctx.getClickLocation().y - ctx.getClickedPos().getY() > 0.5D;
                Direction dir = ctx.getClickedFace();

                if (type == SlabType.BOTTOM)
                    return dir == Direction.UP || clickedUpperHalf && dir.getAxis().isHorizontal();
                else
                    return dir == Direction.DOWN || !clickedUpperHalf && dir.getAxis().isHorizontal();
            }
            else
                return true;
        }
        else
            return false;
    }


    @Override
    public boolean isPathfindable(BlockState state, BlockGetter level, BlockPos pos, PathComputationType type) {
        return false;
    }


    @Override
    public void generateBlockStateProviderData(ModBlockStateProvider provider, RegistryObject<? extends Block> block) {
        ConfiguredModel bottom =
                new ConfiguredModel(provider.models()
                        .withExistingParent(
                                getRegistryName(),
                                new ResourceLocation(
                                        UnfallMod.MOD_ID,
                                        "block/compressed_slab"
                                )
                        )
                        .texture("bottom",
                                provider.blockTexture(getMaterial())
                        ).texture("side",
                                provider.blockTexture(getMaterial())
                        ).texture("top",
                                provider.blockTexture(getMaterial())
                        )
                );

        ConfiguredModel top =
                new ConfiguredModel(provider.models()
                        .withExistingParent(
                                getRegistryName() + "_top",
                                new ResourceLocation(
                                        UnfallMod.MOD_ID,
                                        "block/compressed_slab_top"
                                )
                        )
                        .texture("bottom",
                                provider.blockTexture(getMaterial())
                        ).texture("side",
                                provider.blockTexture(getMaterial())
                        ).texture("top",
                                provider.blockTexture(getMaterial())
                        )
                );


        ModelFile doubleSlabModel = provider.models().getExistingFile(provider.mcLoc(getMaterial().getDescriptionId().split("\\.")[2]));

        //@formatter:off
        provider.getVariantBuilder(block.get())
                .partialState().with(SlabBlock.TYPE, SlabType.BOTTOM).addModels(bottom)
                .partialState().with(SlabBlock.TYPE, SlabType.TOP).addModels(top)
                .partialState().with(SlabBlock.TYPE, SlabType.DOUBLE).addModels(new ConfiguredModel(doubleSlabModel));
        //@formatter:on

    }

    @Override
    public String getTextureFromSides(Direction direction) {
        return null;
    }

    public Block getMaterial() {
        return material;
    }
}
