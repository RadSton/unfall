package io.radston12.reddefense.networking.packets.server;

import io.radston12.reddefense.blockentities.custom.KeypadBlockEntity;
import io.radston12.reddefense.blocks.entities.owned.KeypadBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.network.NetworkEvent;

import java.security.Key;
import java.util.function.Supplier;

public class SetPasscode {
    private final String passcode;

    private final BlockPos pos;

    public SetPasscode(BlockPos pos, String code) {
        this.pos = pos;
        passcode = code;
    }

    public SetPasscode(FriendlyByteBuf buf) {
        this.pos = buf.readBlockPos();
        passcode = buf.readUtf();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeUtf(passcode);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {

        ServerPlayer player = ctx.get().getSender();

        ctx.get().enqueueWork(() -> {
            Level level = player.level();

            KeypadBlockEntity entity = (KeypadBlockEntity) level.getBlockEntity(pos);

            if (entity == null) return;

            if(entity.isSetting()) {
                entity.setPassword(passcode);
                player.displayClientMessage(Component.translatable("block.reddefense.keypad.set"), true);
                player.closeContainer();
                return;
            }

            if(entity.isCorrect(passcode)) {
                player.displayClientMessage(Component.translatable("block.reddefense.keypad.correct"), true);

                entity.enable();

            } else {
                player.displayClientMessage(Component.translatable("block.reddefense.keypad.incorrect"), true);
            }

            player.closeContainer();

        });

        ctx.get().setPacketHandled(true);
    }

    private static void checkAndUpdateAdjacentChest(BlockEntity be, Level level, BlockPos pos, String codeToSet, byte[] salt) {
        /*if (be.getBlockState().getValue(ChestBlock.TYPE) != ChestType.SINGLE) {
            BlockPos offsetPos = pos.relative(ChestBlock.getConnectedDirection(be.getBlockState()));
            BlockEntity otherBe = level.getBlockEntity(offsetPos);

            if (otherBe instanceof KeypadChestBlockEntity chestBe && be.getOwner().owns(chestBe)) {
                chestBe.hashAndSetPasscode(codeToSet, salt);
                level.sendBlockUpdated(offsetPos, otherBe.getBlockState(), otherBe.getBlockState(), 2);
            }
        }*/
    }

    private static void checkAndUpdateAdjacentDoor(BlockEntity be, Level level, String codeToSet, byte[] salt) {
       /* be.runForOtherHalf(otherBe -> {
            if (be.getOwner().owns(otherBe)) {
                otherBe.hashAndSetPasscode(codeToSet, salt);
                level.sendBlockUpdated(otherBe.getBlockPos(), otherBe.getBlockState(), otherBe.getBlockState(), 2);
            }
        });*/
    }
}