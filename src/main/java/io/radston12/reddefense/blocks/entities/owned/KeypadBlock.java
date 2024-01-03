package io.radston12.reddefense.blocks.entities.owned;

import io.radston12.reddefense.blockentities.custom.BlockCompressorBlockEntity;
import io.radston12.reddefense.blockentities.custom.KeypadBlockEntity;
import io.radston12.reddefense.blocks.api.OwnableBlock;
import io.radston12.reddefense.blocks.compressed.CompressedDoorBlock;
import io.radston12.reddefense.datagen.ModBlockStateProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

public class KeypadBlock extends OwnableBlock {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public KeypadBlock() {
        super(Properties.of().strength(-1f, Float.MAX_VALUE), "keypad");

        this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH).setValue(POWERED, false));
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new KeypadBlockEntity(pos, state);
    }

    @Override
    public InteractionResult use(BlockState state, Level lvl, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (lvl.isClientSide()) return InteractionResult.sidedSuccess(true);

        BlockEntity entity = lvl.getBlockEntity(pos);
        if (entity instanceof KeypadBlockEntity keypadBlockEntity) {
            if (!keypadBlockEntity.isSetting()) {
                NetworkHooks.openScreen((ServerPlayer) player, (KeypadBlockEntity) entity, pos);
                return InteractionResult.sidedSuccess(true);
            }


            if (keypadBlockEntity.isOwner(player)) {
                player.sendSystemMessage(Component.translatable("block.reddefense.keypad.setup"));
                NetworkHooks.openScreen((ServerPlayer) player, (KeypadBlockEntity) entity, pos);
            } else
                player.displayClientMessage(Component.translatable("block.reddefense.keypad.notsetup"), true);

        } else
            throw new IllegalStateException("Our Container provider is missing!");

        return InteractionResult.sidedSuccess(true);
    }

    public void enablePower(BlockPos pos, Level lvl, BlockState state) {
        lvl.setBlockAndUpdate(pos, state.setValue(POWERED, true));
        lvl.updateNeighborsAt(pos, this);

        for(Direction dir : Direction.values()) {
            BlockState relativeBlock = lvl.getBlockState(pos.relative(dir));
            if(relativeBlock.getBlock() instanceof CompressedDoorBlock) {
                CompressedDoorBlock block = (CompressedDoorBlock) relativeBlock.getBlock();
                block.setOpen(lvl, pos.relative(dir),true);
            }
        }

        lvl.scheduleTick(pos, this, 60); // TODO: Config default 3 sec
    }

    @Override
    public void tick(BlockState state, ServerLevel lvl, BlockPos pos, RandomSource randomSource) {
        lvl.setBlockAndUpdate(pos, state.setValue(POWERED, false));
        lvl.updateNeighborsAt(pos, this);

        for(Direction dir : Direction.values()) {
            BlockState relativeBlock = lvl.getBlockState(pos.relative(dir));
            if(relativeBlock.getBlock() instanceof CompressedDoorBlock) {
                CompressedDoorBlock block = (CompressedDoorBlock) relativeBlock.getBlock();
                block.setOpen(lvl, pos.relative(dir),false);
            }
        }
    }

    @Override
    public void generateBlockStateProviderData(ModBlockStateProvider provider, RegistryObject<? extends Block> block) {
        // TODO: Blockstates / Model
    }

    @Override
    public boolean shouldAutomaticallyGenerateProviderData() {
        return false;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return super.getStateForPlacement(context).setValue(FACING, context.getPlayer().getDirection().getOpposite()).setValue(POWERED, false);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, POWERED);
    }

    @Override
    public boolean isSignalSource(BlockState state) {
        return true;
    }

    @Override
    public int getSignal(BlockState state, BlockGetter lvl, BlockPos pos, Direction dir) {
        return state.getValue(POWERED) ? 15 : 0;
    }

    @Override
    public int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction dir) {
        return state.getValue(POWERED) ? 15 : 0;
    }
}
