package io.radston12.reddefense.blocks.compressed;

import io.radston12.reddefense.RedDefenseMod;
import io.radston12.reddefense.blockentities.custom.OwnableBlockEntity;
import io.radston12.reddefense.datagen.ModBlockStateProvider;
import io.radston12.reddefense.datagen.interfaces.LootTableProviderData;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.VariantBlockStateBuilder;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

// HEAVILY Modified copy from net.minecraft.world.level.block.DoorBlock
// sorry but I must say I personally think minecraft is in some places not programmed very well, they create a method that could be used everywhere and use it ONCE!
public class CompressedDoorBlock extends BaseCompressedBlock {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
    public static final EnumProperty<DoorHingeSide> HINGE = BlockStateProperties.DOOR_HINGE;
    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
    protected static final VoxelShape SOUTH_AABB = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 3.0D);
    protected static final VoxelShape NORTH_AABB = Block.box(0.0D, 0.0D, 13.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape WEST_AABB = Block.box(13.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape EAST_AABB = Block.box(0.0D, 0.0D, 0.0D, 3.0D, 16.0D, 16.0D);


    public CompressedDoorBlock(String name, Block vanilla) {
        super(name, vanilla);

        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(OPEN, false).setValue(HINGE, DoorHingeSide.LEFT).setValue(HALF, DoubleBlockHalf.LOWER));
    }


    @NotNull
    @Override
    public VoxelShape getShape(BlockState state, @NotNull BlockGetter getter, @NotNull BlockPos pos, @NotNull CollisionContext collisionContext) {
        Direction direction = state.getValue(FACING);
        boolean closed = !isOpen(state);
        boolean hingeRight = state.getValue(HINGE) == DoorHingeSide.RIGHT;

        return switch (direction) {
            case SOUTH -> closed ? SOUTH_AABB : (hingeRight ? EAST_AABB : WEST_AABB);
            case WEST -> closed ? WEST_AABB : (hingeRight ? SOUTH_AABB : NORTH_AABB);
            case NORTH -> closed ? NORTH_AABB : (hingeRight ? WEST_AABB : EAST_AABB);

            default -> closed ? EAST_AABB : (hingeRight ? NORTH_AABB : SOUTH_AABB);
        };
    }


    @Override
    @NotNull
    public BlockState updateShape(BlockState stateHalf, Direction direction, @NotNull BlockState state, @NotNull LevelAccessor lvlAccessor, @NotNull BlockPos posHalf, @NotNull BlockPos pos) {
        DoubleBlockHalf doubleblockhalf = stateHalf.getValue(HALF);

        if (direction.getAxis() == Direction.Axis.Y && doubleblockhalf == DoubleBlockHalf.LOWER == (direction == Direction.UP))
            return state.is(this) && state.getValue(HALF) != doubleblockhalf ? stateHalf.setValue(FACING, state.getValue(FACING)).setValue(OPEN, isOpen(state)).setValue(HINGE, state.getValue(HINGE)) : Blocks.AIR.defaultBlockState();

        return doubleblockhalf == DoubleBlockHalf.LOWER && direction == Direction.DOWN && !stateHalf.canSurvive(lvlAccessor, posHalf) ? Blocks.AIR.defaultBlockState() : super.updateShape(stateHalf, direction, state, lvlAccessor, posHalf, pos);
    }

    @Override
    public void playerWillDestroy(@NotNull Level lvl, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull Player player) {
        /*
        I don't know what this should do, but it doesn't seem important because the player has to be in creative
        if (!lvl.isClientSide && player.isCreative()) {
            DoublePlantBlock.preventCreativeDropFromBottomPart(lvl, pos, state, player);
        }*/

        super.playerWillDestroy(lvl, pos, state, player);
    }

    @Override
    public boolean isPathfindable(@NotNull BlockState state, @NotNull BlockGetter getter, @NotNull BlockPos pos, PathComputationType pathComputationType) {
        return switch (pathComputationType) {
            case LAND, AIR -> isOpen(state);
            default -> false;
        };
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext placeContext) {
        BlockPos blockpos = placeContext.getClickedPos();
        Level level = placeContext.getLevel();

        if (!(blockpos.getY() < level.getMaxBuildHeight() - 1 && level.getBlockState(blockpos.above()).canBeReplaced(placeContext)))
            return null;

        boolean flag = level.hasNeighborSignal(blockpos) || level.hasNeighborSignal(blockpos.above());

        return this.defaultBlockState().setValue(FACING, placeContext.getHorizontalDirection()).setValue(HINGE, this.getHinge(placeContext)).setValue(OPEN, flag).setValue(HALF, DoubleBlockHalf.LOWER);
    }

    @Override
    public void setPlacedBy(Level lvl, BlockPos pos, BlockState state, LivingEntity entity, ItemStack stack) {
        lvl.setBlock(pos.above(), state.setValue(HALF, DoubleBlockHalf.UPPER), 3);
        super.setPlacedBy(lvl, pos, state, entity, stack);
    }

    private DoorHingeSide getHinge(BlockPlaceContext placeContext) {
        BlockGetter blockgetter = placeContext.getLevel();
        BlockPos blockpos = placeContext.getClickedPos();
        Direction direction = placeContext.getHorizontalDirection();
        BlockPos blockPos1 = blockpos.above();
        Direction direction1 = direction.getCounterClockWise();
        BlockPos blockPos2 = blockpos.relative(direction1);
        BlockState blockstate = blockgetter.getBlockState(blockPos2);
        BlockPos blockPos3 = blockPos1.relative(direction1);
        BlockState blockState1 = blockgetter.getBlockState(blockPos3);
        Direction direction2 = direction.getClockWise();
        BlockPos blockPos4 = blockpos.relative(direction2);
        BlockState blockState2 = blockgetter.getBlockState(blockPos4);
        BlockPos blockPos5 = blockPos1.relative(direction2);
        BlockState blockState3 = blockgetter.getBlockState(blockPos5);

        int i = (blockstate.isCollisionShapeFullBlock(blockgetter, blockPos2) ? -1 : 0) + (blockState1.isCollisionShapeFullBlock(blockgetter, blockPos3) ? -1 : 0) + (blockState2.isCollisionShapeFullBlock(blockgetter, blockPos4) ? 1 : 0) + (blockState3.isCollisionShapeFullBlock(blockgetter, blockPos5) ? 1 : 0);

        boolean flag = blockstate.is(this) && blockstate.getValue(HALF) == DoubleBlockHalf.LOWER;
        boolean flag1 = blockState2.is(this) && blockState2.getValue(HALF) == DoubleBlockHalf.LOWER;

        if (!((!flag || flag1) && i <= 0)) return DoorHingeSide.RIGHT;
        if (!((!flag1 || flag) && i >= 0)) return DoorHingeSide.LEFT;

        int stepX = direction.getStepX();
        int stepZ = direction.getStepZ();

        Vec3 clickLocation = placeContext.getClickLocation();
        double distX = clickLocation.x - (double) blockpos.getX();
        double distZ = clickLocation.z - (double) blockpos.getZ();

        return (stepX >= 0 || !(distZ < 0.5D)) && (stepX <= 0 || !(distZ > 0.5D)) && (stepZ >= 0 || !(distX > 0.5D)) && (stepZ <= 0 || !(distX < 0.5D)) ? DoorHingeSide.LEFT : DoorHingeSide.RIGHT;

    }

    public void cylceOpen(Level lvl, BlockState state, BlockPos pos, Player player) {
        state = state.cycle(OPEN);
        lvl.setBlock(pos, state, 10);
        this.playSound(lvl, pos, this.isOpen(state));
        lvl.gameEvent(player, this.isOpen(state) ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, pos);
    }

    @Override
    @NotNull
    public InteractionResult use(@NotNull BlockState state, Level lvl, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand interactionHand, @NotNull BlockHitResult hitResult) {

        if (getVanillaBlock() == Blocks.IRON_DOOR)
            return super.use(state, lvl, pos, player, interactionHand, hitResult);

        cylceOpen(lvl, state, pos, player);

        return InteractionResult.sidedSuccess(lvl.isClientSide);
    }

    public boolean isOpen(BlockState state) {
        return state.getValue(OPEN);
    }

    public void playSound(Level lvl, BlockPos pos, boolean open) {
        lvl.playSound(null, pos, open ? SoundEvents.IRON_DOOR_OPEN : SoundEvents.IRON_DOOR_CLOSE, SoundSource.BLOCKS, 1.0F, 1.0F);
    }

    @Override
    public void neighborChanged(@NotNull BlockState state, Level lvl, @NotNull BlockPos pos, @NotNull Block block, @NotNull BlockPos neighbor, boolean unusedBool) {

        handleNeighborChange(lvl, pos, neighbor);

        if (getVanillaBlock() == Blocks.IRON_DOOR)
            return;

        boolean hasNeighborSignal = lvl.hasNeighborSignal(pos) || lvl.hasNeighborSignal(pos.relative(state.getValue(HALF) == DoubleBlockHalf.LOWER ? Direction.UP : Direction.DOWN));

        if (this.defaultBlockState().is(block)) return;

        if (hasNeighborSignal != isOpen(state)) {
            this.playSound(lvl, pos, hasNeighborSignal);
            lvl.gameEvent(null, hasNeighborSignal ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, pos);
        }

        lvl.setBlock(pos, state.setValue(OPEN, hasNeighborSignal), 2);


    }

    public void handleNeighborChange(Level level, BlockPos first, BlockPos second) {
        BlockState firstState = level.getBlockState(first);
        BlockState secondState = level.getBlockState(second);

        if (!(firstState.getBlock() instanceof CompressedDoorBlock && secondState.getBlock() instanceof CompressedDoorBlock)) {
            return;
        }

        OwnableBlockEntity firstEntity = (OwnableBlockEntity) level.getBlockEntity(first);
        OwnableBlockEntity secondEntity = (OwnableBlockEntity) level.getBlockEntity(second);

        if (firstEntity.getOwnerUUIDPart() == "" && secondEntity.getOwnerUUIDPart() != "") {
            firstEntity.setOwner(secondEntity.getOwnerName(), secondEntity.getOwnerUUIDPart());
        } else if (firstEntity.getOwnerUUIDPart() != "" && secondEntity.getOwnerUUIDPart() == "") {
            secondEntity.setOwner(firstEntity.getOwnerName(), firstEntity.getOwnerUUIDPart());
        }

    }

    public void setOpen(Level level, BlockPos pos, boolean open) {
        level.setBlockAndUpdate(pos, level.getBlockState(pos).setValue(OPEN, open));
        level.updateNeighborsAt(pos, this);
        playSound(level, pos, open);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader lvlReader, BlockPos pos) {
        BlockPos blockpos = pos.below();
        BlockState blockstate = lvlReader.getBlockState(blockpos);
        return state.getValue(HALF) == DoubleBlockHalf.LOWER ? blockstate.isFaceSturdy(lvlReader, blockpos, Direction.UP) : blockstate.is(this);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(@NotNull BlockState state, @NotNull Mirror mirror) {
        return mirror == Mirror.NONE ? state : state.rotate(mirror.getRotation(state.getValue(FACING))).cycle(HINGE);
    }

    @Override
    public long getSeed(BlockState state, BlockPos pos) {
        return Mth.getSeed(pos.getX(), pos.below(state.getValue(HALF) == DoubleBlockHalf.LOWER ? 0 : 1).getY(), pos.getZ());
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HALF, FACING, OPEN, HINGE);
    }

    @Override
    public boolean shouldAutomaticallyGenerateItem() {
        return false;
    }

    public ResourceLocation getTexture(ModBlockStateProvider provider) {
        return provider.blockTexture(getVanillaBlock());
    }

    @Override
    public void generateBlockStateProviderData(ModBlockStateProvider provider, RegistryObject<? extends Block> block) {

        provider.itemModels().withExistingParent(block.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                getTexture(provider).toString().replaceAll("block", "item"));

        VariantBlockStateBuilder variantBuilder = provider.getVariantBuilder(block.get());
        // models

        ConfiguredModel bottom_left =
                new ConfiguredModel(provider.models()
                        .withExistingParent(
                                getRegistryName() + "_bottom_left",
                                new ResourceLocation(
                                        RedDefenseMod.MOD_ID,
                                        "block/compressed_door_bottom_left"
                                )
                        )
                        .texture("bottom",
                                getTexture(provider) + "_bottom"
                        ).texture("top",
                                getTexture(provider) + "_top"
                        )
                );

        ConfiguredModel bottom_left_open =
                new ConfiguredModel(provider.models()
                        .withExistingParent(
                                getRegistryName() + "_bottom_left_open",
                                new ResourceLocation(
                                        RedDefenseMod.MOD_ID,
                                        "block/compressed_door_bottom_left_open"
                                )
                        )
                        .texture("bottom",
                                getTexture(provider) + "_bottom"
                        ).texture("top",
                                getTexture(provider) + "_top"
                        )
                );

        ConfiguredModel bottom_right =
                new ConfiguredModel(provider.models()
                        .withExistingParent(
                                getRegistryName() + "_bottom_right",
                                new ResourceLocation(
                                        RedDefenseMod.MOD_ID,
                                        "block/compressed_door_bottom_right"
                                )
                        )
                        .texture("bottom",
                                getTexture(provider) + "_bottom"
                        ).texture("top",
                                getTexture(provider) + "_top"
                        )
                );

        ConfiguredModel bottom_right_open =
                new ConfiguredModel(provider.models()
                        .withExistingParent(
                                getRegistryName() + "_bottom_right_open",
                                new ResourceLocation(
                                        RedDefenseMod.MOD_ID,
                                        "block/compressed_door_bottom_right_open"
                                )
                        )
                        .texture("bottom",
                                getTexture(provider) + "_bottom"
                        ).texture("top",
                                getTexture(provider) + "_top"
                        )
                );

        ConfiguredModel top_left =
                new ConfiguredModel(provider.models()
                        .withExistingParent(
                                getRegistryName() + "_top_left",
                                new ResourceLocation(
                                        RedDefenseMod.MOD_ID,
                                        "block/compressed_door_top_left"
                                )
                        )
                        .texture("bottom",
                                getTexture(provider) + "_bottom"
                        ).texture("top",
                                getTexture(provider) + "_top"
                        )
                );

        ConfiguredModel top_left_open =
                new ConfiguredModel(provider.models()
                        .withExistingParent(
                                getRegistryName() + "_top_left_open",
                                new ResourceLocation(
                                        RedDefenseMod.MOD_ID,
                                        "block/compressed_door_top_left_open"
                                )
                        )
                        .texture("bottom",
                                getTexture(provider) + "_bottom"
                        ).texture("top",
                                getTexture(provider) + "_top"
                        )
                );

        ConfiguredModel top_right =
                new ConfiguredModel(provider.models()
                        .withExistingParent(
                                getRegistryName() + "_top_right",
                                new ResourceLocation(
                                        RedDefenseMod.MOD_ID,
                                        "block/compressed_door_top_right"
                                )
                        )
                        .texture("bottom",
                                getTexture(provider) + "_bottom"
                        ).texture("top",
                                getTexture(provider) + "_top"
                        )
                );

        ConfiguredModel top_right_open =
                new ConfiguredModel(provider.models()
                        .withExistingParent(
                                getRegistryName() + "_top_right_open",
                                new ResourceLocation(
                                        RedDefenseMod.MOD_ID,
                                        "block/compressed_door_top_right_open"
                                )
                        )
                        .texture("bottom",
                                getTexture(provider) + "_bottom"
                        ).texture("top",
                                getTexture(provider) + "_top"
                        )
                );

        variantBuilder
                //facing=east,half=lower,hinge=left,open=false
                .partialState()
                .with(DoorBlock.FACING, Direction.EAST).with(DoorBlock.HALF, DoubleBlockHalf.LOWER).with(DoorBlock.HINGE, DoorHingeSide.LEFT).with(DoorBlock.OPEN, false)
                .modelForState()
                .modelFile(bottom_left.model)
                .addModel()

                //facing=east,half=lower,hinge=left,open=true
                .partialState()
                .with(DoorBlock.FACING, Direction.EAST).with(DoorBlock.HALF, DoubleBlockHalf.LOWER).with(DoorBlock.HINGE, DoorHingeSide.LEFT).with(DoorBlock.OPEN, true)
                .modelForState()
                .modelFile(bottom_left_open.model).rotationY(90)
                .addModel()

                //facing=east,half=lower,hinge=right,open=false
                .partialState()
                .with(DoorBlock.FACING, Direction.EAST).with(DoorBlock.HALF, DoubleBlockHalf.LOWER).with(DoorBlock.HINGE, DoorHingeSide.RIGHT).with(DoorBlock.OPEN, false)
                .modelForState()
                .modelFile(bottom_right.model)
                .addModel()

                //facing=east,half=lower,hinge=right,open=true
                .partialState()
                .with(DoorBlock.FACING, Direction.EAST).with(DoorBlock.HALF, DoubleBlockHalf.LOWER).with(DoorBlock.HINGE, DoorHingeSide.RIGHT).with(DoorBlock.OPEN, true)
                .modelForState()
                .modelFile(bottom_right_open.model).rotationY(270)
                .addModel()

                //facing=east,half=upper,hinge=left,open=false
                .partialState()
                .with(DoorBlock.FACING, Direction.EAST).with(DoorBlock.HALF, DoubleBlockHalf.UPPER).with(DoorBlock.HINGE, DoorHingeSide.LEFT).with(DoorBlock.OPEN, false)
                .modelForState()
                .modelFile(top_left.model)
                .addModel()

                .partialState()
                .with(DoorBlock.FACING, Direction.EAST).with(DoorBlock.HALF, DoubleBlockHalf.UPPER).with(DoorBlock.HINGE, DoorHingeSide.LEFT).with(DoorBlock.OPEN, true)
                .modelForState()
                .modelFile(top_left_open.model).rotationY(90)
                .addModel()

                .partialState()
                .with(DoorBlock.FACING, Direction.EAST).with(DoorBlock.HALF, DoubleBlockHalf.UPPER).with(DoorBlock.HINGE, DoorHingeSide.RIGHT).with(DoorBlock.OPEN, false)
                .modelForState()
                .modelFile(top_right.model)
                .addModel()

                .partialState()
                .with(DoorBlock.FACING, Direction.EAST).with(DoorBlock.HALF, DoubleBlockHalf.UPPER).with(DoorBlock.HINGE, DoorHingeSide.RIGHT).with(DoorBlock.OPEN, true)
                .modelForState()
                .modelFile(top_right_open.model).rotationY(270)
                .addModel()


                .partialState()
                .with(DoorBlock.FACING, Direction.NORTH).with(DoorBlock.HALF, DoubleBlockHalf.LOWER).with(DoorBlock.HINGE, DoorHingeSide.LEFT).with(DoorBlock.OPEN, true)
                .modelForState()
                .modelFile(bottom_left_open.model)
                .addModel()

                //facing=east,half=lower,hinge=right,open=false
                .partialState()
                .with(DoorBlock.FACING, Direction.NORTH).with(DoorBlock.HALF, DoubleBlockHalf.LOWER).with(DoorBlock.HINGE, DoorHingeSide.RIGHT).with(DoorBlock.OPEN, false)
                .modelForState()
                .modelFile(bottom_right.model).rotationY(270)
                .addModel()

                //facing=east,half=lower,hinge=right,open=true
                .partialState()
                .with(DoorBlock.FACING, Direction.NORTH).with(DoorBlock.HALF, DoubleBlockHalf.LOWER).with(DoorBlock.HINGE, DoorHingeSide.RIGHT).with(DoorBlock.OPEN, true)
                .modelForState()
                .modelFile(bottom_right_open.model).rotationY(180)
                .addModel()

                //facing=east,half=upper,hinge=left,open=false
                .partialState()
                .with(DoorBlock.FACING, Direction.NORTH).with(DoorBlock.HALF, DoubleBlockHalf.UPPER).with(DoorBlock.HINGE, DoorHingeSide.LEFT).with(DoorBlock.OPEN, false)
                .modelForState()
                .modelFile(top_left.model).rotationY(270)
                .addModel()

                .partialState()
                .with(DoorBlock.FACING, Direction.NORTH).with(DoorBlock.HALF, DoubleBlockHalf.UPPER).with(DoorBlock.HINGE, DoorHingeSide.LEFT).with(DoorBlock.OPEN, true)
                .modelForState()
                .modelFile(top_left_open.model)
                .addModel()


                .partialState()
                .with(DoorBlock.FACING, Direction.NORTH).with(DoorBlock.HALF, DoubleBlockHalf.UPPER).with(DoorBlock.HINGE, DoorHingeSide.RIGHT).with(DoorBlock.OPEN, true)
                .modelForState()
                .modelFile(top_right_open.model).rotationY(180)
                .addModel()


                .partialState()
                .with(DoorBlock.FACING, Direction.NORTH).with(DoorBlock.HALF, DoubleBlockHalf.LOWER).with(DoorBlock.HINGE, DoorHingeSide.LEFT).with(DoorBlock.OPEN, false)
                .modelForState()
                .modelFile(bottom_left.model).rotationY(270)
                .addModel()


                .partialState()
                .with(DoorBlock.FACING, Direction.NORTH).with(DoorBlock.HALF, DoubleBlockHalf.UPPER).with(DoorBlock.HINGE, DoorHingeSide.RIGHT).with(DoorBlock.OPEN, false)
                .modelForState()
                .modelFile(top_right.model).rotationY(270)
                .addModel()


                // end

                .partialState()
                .with(DoorBlock.FACING, Direction.SOUTH).with(DoorBlock.HALF, DoubleBlockHalf.LOWER).with(DoorBlock.HINGE, DoorHingeSide.LEFT).with(DoorBlock.OPEN, false)
                .modelForState()
                .modelFile(bottom_left.model).rotationY(90)
                .addModel()

                .partialState()
                .with(DoorBlock.FACING, Direction.SOUTH).with(DoorBlock.HALF, DoubleBlockHalf.LOWER).with(DoorBlock.HINGE, DoorHingeSide.LEFT).with(DoorBlock.OPEN, true)
                .modelForState()
                .modelFile(bottom_left_open.model).rotationY(180)
                .addModel()

                //facing=east,half=lower,hinge=right,open=false
                .partialState()
                .with(DoorBlock.FACING, Direction.SOUTH).with(DoorBlock.HALF, DoubleBlockHalf.LOWER).with(DoorBlock.HINGE, DoorHingeSide.RIGHT).with(DoorBlock.OPEN, false)
                .modelForState()
                .modelFile(bottom_right.model).rotationY(90)
                .addModel()

                //facing=east,half=lower,hinge=right,open=true
                .partialState()
                .with(DoorBlock.FACING, Direction.SOUTH).with(DoorBlock.HALF, DoubleBlockHalf.LOWER).with(DoorBlock.HINGE, DoorHingeSide.RIGHT).with(DoorBlock.OPEN, true)
                .modelForState()
                .modelFile(bottom_right_open.model)
                .addModel()

                //facing=east,half=upper,hinge=left,open=false
                .partialState()
                .with(DoorBlock.FACING, Direction.SOUTH).with(DoorBlock.HALF, DoubleBlockHalf.UPPER).with(DoorBlock.HINGE, DoorHingeSide.LEFT).with(DoorBlock.OPEN, false)
                .modelForState()
                .modelFile(top_left.model).rotationY(90)
                .addModel()

                .partialState()
                .with(DoorBlock.FACING, Direction.SOUTH).with(DoorBlock.HALF, DoubleBlockHalf.UPPER).with(DoorBlock.HINGE, DoorHingeSide.LEFT).with(DoorBlock.OPEN, true)
                .modelForState()
                .modelFile(top_left_open.model).rotationY(180)
                .addModel()

                .partialState()
                .with(DoorBlock.FACING, Direction.SOUTH).with(DoorBlock.HALF, DoubleBlockHalf.UPPER).with(DoorBlock.HINGE, DoorHingeSide.RIGHT).with(DoorBlock.OPEN, false)
                .modelForState()
                .modelFile(top_right.model).rotationY(90)
                .addModel()

                .partialState()
                .with(DoorBlock.FACING, Direction.SOUTH).with(DoorBlock.HALF, DoubleBlockHalf.UPPER).with(DoorBlock.HINGE, DoorHingeSide.RIGHT).with(DoorBlock.OPEN, true)
                .modelForState()
                .modelFile(top_right_open.model)
                .addModel()


                // end south


                .partialState()
                .with(DoorBlock.FACING, Direction.WEST).with(DoorBlock.HALF, DoubleBlockHalf.LOWER).with(DoorBlock.HINGE, DoorHingeSide.LEFT).with(DoorBlock.OPEN, false)
                .modelForState()
                .modelFile(bottom_left.model).rotationY(180)
                .addModel()

                .partialState()
                .with(DoorBlock.FACING, Direction.WEST).with(DoorBlock.HALF, DoubleBlockHalf.LOWER).with(DoorBlock.HINGE, DoorHingeSide.LEFT).with(DoorBlock.OPEN, true)
                .modelForState()
                .modelFile(bottom_left_open.model).rotationY(270)
                .addModel()

                //facing=east,half=lower,hinge=right,open=false
                .partialState()
                .with(DoorBlock.FACING, Direction.WEST).with(DoorBlock.HALF, DoubleBlockHalf.LOWER).with(DoorBlock.HINGE, DoorHingeSide.RIGHT).with(DoorBlock.OPEN, false)
                .modelForState()
                .modelFile(bottom_right.model).rotationY(180)
                .addModel()

                //facing=east,half=lower,hinge=right,open=true
                .partialState()
                .with(DoorBlock.FACING, Direction.WEST).with(DoorBlock.HALF, DoubleBlockHalf.LOWER).with(DoorBlock.HINGE, DoorHingeSide.RIGHT).with(DoorBlock.OPEN, true)
                .modelForState()
                .modelFile(bottom_right_open.model).rotationY(90)
                .addModel()

                //facing=east,half=upper,hinge=left,open=false
                .partialState()
                .with(DoorBlock.FACING, Direction.WEST).with(DoorBlock.HALF, DoubleBlockHalf.UPPER).with(DoorBlock.HINGE, DoorHingeSide.LEFT).with(DoorBlock.OPEN, false)
                .modelForState()
                .modelFile(top_left.model).rotationY(180)
                .addModel()

                .partialState()
                .with(DoorBlock.FACING, Direction.WEST).with(DoorBlock.HALF, DoubleBlockHalf.UPPER).with(DoorBlock.HINGE, DoorHingeSide.LEFT).with(DoorBlock.OPEN, true)
                .modelForState()
                .modelFile(top_left_open.model).rotationY(270)
                .addModel()

                .partialState()
                .with(DoorBlock.FACING, Direction.WEST).with(DoorBlock.HALF, DoubleBlockHalf.UPPER).with(DoorBlock.HINGE, DoorHingeSide.RIGHT).with(DoorBlock.OPEN, false)
                .modelForState()
                .modelFile(top_right.model).rotationY(180)
                .addModel()

                .partialState()
                .with(DoorBlock.FACING, Direction.WEST).with(DoorBlock.HALF, DoubleBlockHalf.UPPER).with(DoorBlock.HINGE, DoorHingeSide.RIGHT).with(DoorBlock.OPEN, true)
                .modelForState()
                .modelFile(top_right_open.model).rotationY(90)
                .addModel();
    }

    @Override
    public boolean shouldAutomaticallyGenerateLootTable() {
        return false;
    }

    @Override
    public LootTable.Builder generateLootTableProviderData(BlockLootSubProvider tables, RegistryObject<? extends Block> block, LootTableProviderData applyExplosionCondition) {
        return LootTable.lootTable()
                .withPool(
                        applyExplosionCondition.applyExplosionCondition(
                                block.get(),
                                LootPool.lootPool()
                                        .setRolls(
                                                ConstantValue.exactly(1.0F)
                                        )
                                        .add(
                                                LootItem.lootTableItem(block.get())
                                                        .when(
                                                                LootItemBlockStatePropertyCondition.hasBlockStateProperties(block.get())
                                                                        .setProperties(
                                                                                StatePropertiesPredicate.Builder.properties()
                                                                                        .hasProperty(DoorBlock.HALF, DoubleBlockHalf.LOWER)
                                                                        )
                                                        )
                                        )
                        )
                );
    }

    @Override
    public String getTextureFromSides(Direction direction) {
        return null;
    }
}
