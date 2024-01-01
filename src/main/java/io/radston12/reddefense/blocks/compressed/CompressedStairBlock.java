package io.radston12.reddefense.blocks.compressed;

import io.radston12.reddefense.RedDefenseMod;
import io.radston12.reddefense.datagen.ModBlockStateProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.VariantBlockStateBuilder;
import net.minecraftforge.registries.RegistryObject;

import java.util.stream.IntStream;

public class CompressedStairBlock extends BaseCompressedBlock{

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final EnumProperty<Half> HALF = BlockStateProperties.HALF;
    public static final EnumProperty<StairsShape> SHAPE = BlockStateProperties.STAIRS_SHAPE;
    protected static final VoxelShape AABB_SLAB_TOP = Block.box(0.0D, 8.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape AABB_SLAB_BOTTOM = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D);
    protected static final VoxelShape NWD_CORNER = Block.box(0.0D, 0.0D, 0.0D, 8.0D, 8.0D, 8.0D);
    protected static final VoxelShape SWD_CORNER = Block.box(0.0D, 0.0D, 8.0D, 8.0D, 8.0D, 16.0D);
    protected static final VoxelShape NWU_CORNER = Block.box(0.0D, 8.0D, 0.0D, 8.0D, 16.0D, 8.0D);
    protected static final VoxelShape SWU_CORNER = Block.box(0.0D, 8.0D, 8.0D, 8.0D, 16.0D, 16.0D);
    protected static final VoxelShape NED_CORNER = Block.box(8.0D, 0.0D, 0.0D, 16.0D, 8.0D, 8.0D);
    protected static final VoxelShape SED_CORNER = Block.box(8.0D, 0.0D, 8.0D, 16.0D, 8.0D, 16.0D);
    protected static final VoxelShape NEU_CORNER = Block.box(8.0D, 8.0D, 0.0D, 16.0D, 16.0D, 8.0D);
    protected static final VoxelShape SEU_CORNER = Block.box(8.0D, 8.0D, 8.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape[] SLAB_TOP_SHAPES = makeShapes(AABB_SLAB_TOP, NWD_CORNER, NED_CORNER, SWD_CORNER, SED_CORNER);
    protected static final VoxelShape[] SLAB_BOTTOM_SHAPES = makeShapes(AABB_SLAB_BOTTOM, NWU_CORNER, NEU_CORNER, SWU_CORNER, SEU_CORNER);
    private static final int[] SHAPE_BY_STATE = {
            12, 5, 3, 10, 14, 13, 7, 11, 13, 7, 11, 14, 8, 4, 1, 2, 4, 1, 2, 8
    };
    private final BlockState modelState;
    private final Block material;
    private final String bottom, side, top;


    public CompressedStairBlock(String name, Block vanilla, Block material) {
        super(name, vanilla, BlockBehaviour.Properties.of().mapColor(MapColor.PODZOL).strength(-1.0F, Float.MAX_VALUE));

        this.material = material;
        this.bottom = null;
        this.side = null;
        this.top = null;

        registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH).setValue(HALF, Half.BOTTOM).setValue(SHAPE, StairsShape.STRAIGHT));

        modelState = getVanillaBlock().defaultBlockState();
    }

    public CompressedStairBlock(String name, Block vanilla, String bottom, String side, String top) {
        super(name, vanilla, BlockBehaviour.Properties.of().mapColor(MapColor.PODZOL).strength(-1.0F, Float.MAX_VALUE));

        this.material = null;

        this.bottom = bottom;
        this.side   = side  ;
        this.top    = top   ;

        registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH).setValue(HALF, Half.BOTTOM).setValue(SHAPE, StairsShape.STRAIGHT));

        modelState = getVanillaBlock().defaultBlockState();
    }

    private static VoxelShape[] makeShapes(VoxelShape slabShape, VoxelShape nwCorner, VoxelShape neCorner, VoxelShape swCorner, VoxelShape seCorner) {
        return IntStream.range(0, 16).mapToObj(shape -> combineShapes(shape, slabShape, nwCorner, neCorner, swCorner, seCorner)).toArray(size -> new VoxelShape[size]);
    }

    private static VoxelShape combineShapes(int bitfield, VoxelShape slabShape, VoxelShape nwCorner, VoxelShape neCorner, VoxelShape swCorner, VoxelShape seCorner) {
        VoxelShape shape = slabShape;

        if ((bitfield & 1) != 0)
            shape = Shapes.or(slabShape, nwCorner);

        if ((bitfield & 2) != 0)
            shape = Shapes.or(shape, neCorner);

        if ((bitfield & 4) != 0)
            shape = Shapes.or(shape, swCorner);

        if ((bitfield & 8) != 0)
            shape = Shapes.or(shape, seCorner);

        return shape;
    }

    private static StairsShape getShapeProperty(BlockState state, BlockGetter level, BlockPos pos) {
        Direction dir = state.getValue(FACING);
        BlockState offsetState = level.getBlockState(pos.relative(dir));

        if (isBlockStairs(offsetState) && state.getValue(HALF) == offsetState.getValue(HALF)) {
            Direction offsetDir = offsetState.getValue(FACING);

            if (offsetDir.getAxis() != state.getValue(FACING).getAxis() && isDifferentStairs(state, level, pos, offsetDir.getOpposite())) {
                if (offsetDir == dir.getCounterClockWise())
                    return StairsShape.OUTER_LEFT;
                else
                    return StairsShape.OUTER_RIGHT;
            }
        }

        BlockState offsetOppositeState = level.getBlockState(pos.relative(dir.getOpposite()));

        if (isBlockStairs(offsetOppositeState) && state.getValue(HALF) == offsetOppositeState.getValue(HALF)) {
            Direction offsetOppositeDir = offsetOppositeState.getValue(FACING);

            if (offsetOppositeDir.getAxis() != state.getValue(FACING).getAxis() && isDifferentStairs(state, level, pos, offsetOppositeDir)) {
                if (offsetOppositeDir == dir.getCounterClockWise())
                    return StairsShape.INNER_LEFT;
                else
                    return StairsShape.INNER_RIGHT;
            }
        }

        return StairsShape.STRAIGHT;
    }

    private static boolean isDifferentStairs(BlockState state, BlockGetter level, BlockPos pos, Direction face) {
        BlockState offsetState = level.getBlockState(pos.relative(face));

        return !isBlockStairs(offsetState) || offsetState.getValue(FACING) != state.getValue(FACING) || offsetState.getValue(HALF) != state.getValue(HALF);
    }

    public static boolean isBlockStairs(BlockState state) {
        return state.getBlock() instanceof CompressedStairBlock || state.getBlock() instanceof StairBlock;
    }

    @Override
    public boolean useShapeForLightOcclusion(BlockState state) {
        return true;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return (state.getValue(HALF) == Half.TOP ? SLAB_TOP_SHAPES : SLAB_BOTTOM_SHAPES)[SHAPE_BY_STATE[getShapeIndex(state)]];
    }

    private int getShapeIndex(BlockState state) {
        return state.getValue(SHAPE).ordinal() * 4 + state.getValue(FACING).get2DDataValue();
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource rand) {
        getVanillaBlock().animateTick(state, level, pos, rand);
    }

    @Override
    public void attack(BlockState state, Level level, BlockPos pos, Player player) {
        modelState.attack(level, pos, player);
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (state.getBlock() != oldState.getBlock()) {
            modelState.neighborChanged(level, pos, Blocks.AIR, pos, false);
            getVanillaBlock().onPlace(modelState, level, pos, oldState, false);
        }
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock())
            modelState.onRemove(level, pos, newState, isMoving);

        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        getVanillaBlock().stepOn(level, pos, state, entity);
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        modelState.tick(level, pos, random);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        return modelState.use(level, player, hand, hit);
    }

    @Override
    public void wasExploded(Level level, BlockPos pos, Explosion explosion) {
        getVanillaBlock().wasExploded(level, pos, explosion);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        Direction dir = ctx.getClickedFace();
        BlockPos pos = ctx.getClickedPos();
        BlockState state = defaultBlockState().setValue(FACING, ctx.getHorizontalDirection()).setValue(HALF, dir != Direction.DOWN && (dir == Direction.UP || ctx.getClickLocation().y - pos.getY() <= 0.5D) ? Half.BOTTOM : Half.TOP);

        return state.setValue(SHAPE, getShapeProperty(state, ctx.getLevel(), pos));
    }

    @Override
    public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        return facing.getAxis().isHorizontal() ? state.setValue(SHAPE, getShapeProperty(state, level, currentPos)) : super.updateShape(state, facing, facingState, level, currentPos, facingPos);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        Direction direction = state.getValue(FACING);
        StairsShape shape = state.getValue(SHAPE);

        switch (mirror) {
            case LEFT_RIGHT:
                if (direction.getAxis() == Direction.Axis.Z) {
                    return switch (shape) {
                        case INNER_LEFT -> state.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.INNER_RIGHT);
                        case INNER_RIGHT -> state.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.INNER_LEFT);
                        case OUTER_LEFT -> state.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.OUTER_RIGHT);
                        case OUTER_RIGHT -> state.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.OUTER_LEFT);
                        default -> state.rotate(Rotation.CLOCKWISE_180);
                    };
                }
                break;
            case FRONT_BACK:
                if (direction.getAxis() == Direction.Axis.X) {
                    return switch (shape) {
                        case INNER_LEFT -> state.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.INNER_LEFT);
                        case INNER_RIGHT -> state.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.INNER_RIGHT);
                        case OUTER_LEFT -> state.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.OUTER_RIGHT);
                        case OUTER_RIGHT -> state.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.OUTER_LEFT);
                        case STRAIGHT -> state.rotate(Rotation.CLOCKWISE_180);
                    };
                }
                break;
            default:
                break;
        }

        return super.mirror(state, mirror);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, HALF, SHAPE);
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter level, BlockPos pos, PathComputationType type) {
        return false;
    }

    private String getBlockTexture(ModBlockStateProvider provider, String key) {
        if(getMaterial() != null)
            return provider.blockTexture(getMaterial()).toString();

        return switch (key) {
            case "bottom" -> bottom;
            case "side" -> side;
            default -> top;
        };
    }

    @Override
    public void generateBlockStateProviderData(ModBlockStateProvider provider, RegistryObject<? extends Block> block) {
        VariantBlockStateBuilder variantBuilder = provider.getVariantBuilder(block.get());

        String blockTextureBottom = getBlockTexture(provider, "bottom");
        String blockTextureSide = getBlockTexture(provider, "side");
        String blockTextureTop = getBlockTexture(provider, "top");

        ConfiguredModel normal =
                new ConfiguredModel(provider.models()
                        .withExistingParent(
                                getRegistryName(),
                                new ResourceLocation(
                                        RedDefenseMod.MOD_ID,
                                        "block/compressed_stairs"
                                )
                        )
                        .texture("bottom",
                                blockTextureBottom
                        ).texture("side",
                                blockTextureSide
                        ).texture("top",
                                blockTextureTop
                        )
                );

        ConfiguredModel inner =
                new ConfiguredModel(provider.models()
                        .withExistingParent(
                                getRegistryName() + "_inner",
                                new ResourceLocation(
                                        RedDefenseMod.MOD_ID,
                                        "block/compressed_stairs_inner"
                                )
                        )
                        .texture("bottom",
                                blockTextureBottom
                        ).texture("side",
                                blockTextureSide
                        ).texture("top",
                                blockTextureTop
                        )
                );

        ConfiguredModel outer =
                new ConfiguredModel(provider.models()
                        .withExistingParent(
                                getRegistryName() + "_outer",
                                new ResourceLocation(
                                        RedDefenseMod.MOD_ID,
                                        "block/compressed_stairs_outer"
                                )
                        )
                        .texture("bottom",
                                blockTextureBottom
                        ).texture("side",
                                blockTextureSide
                        ).texture("top",
                                blockTextureTop
                        )
                );

        variantBuilder.forAllStatesExcept(state -> {
            Direction facing = state.getValue(StairBlock.FACING);
            Half half = state.getValue(StairBlock.HALF);
            StairsShape shape = state.getValue(StairBlock.SHAPE);
            int yRot = (int) facing.getClockWise().toYRot(); // Stairs model is rotated 90 degrees clockwise for some reason
            if (shape == StairsShape.INNER_LEFT || shape == StairsShape.OUTER_LEFT) {
                yRot += 270; // Left facing stairs are rotated 90 degrees clockwise
            }
            if (shape != StairsShape.STRAIGHT && half == Half.TOP) {
                yRot += 90; // Top stairs are rotated 90 degrees clockwise
            }
            yRot %= 360;
            boolean uvlock = yRot != 0 || half == Half.TOP; // Don't set uvlock for states that have no rotation
            return ConfiguredModel.builder()
                    .modelFile(shape == StairsShape.STRAIGHT ? normal.model : shape == StairsShape.INNER_LEFT || shape == StairsShape.INNER_RIGHT ? inner.model : outer.model)
                    .rotationX(half == Half.BOTTOM ? 0 : 180)
                    .rotationY(yRot)
                    .uvLock(uvlock)
                    .build();
        }, StairBlock.WATERLOGGED);
    }

    @Override
    public String getTextureFromSides(Direction direction) {
        return "block/" + material.getDescriptionId();
    }


    public Block getMaterial() {
        return material;
    }
}
