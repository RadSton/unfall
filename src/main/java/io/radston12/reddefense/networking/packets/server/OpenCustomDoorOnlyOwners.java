package io.radston12.reddefense.networking.packets.server;

import io.radston12.reddefense.blockentities.custom.KeypadBlockEntity;
import io.radston12.reddefense.blockentities.custom.OwnableBlockEntity;
import io.radston12.reddefense.blocks.compressed.CompressedCustomDoorBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.NetworkEvent;
import org.checkerframework.checker.units.qual.C;

import java.util.function.Supplier;

public class OpenCustomDoorOnlyOwners {

    private final BlockPos pos;

    public OpenCustomDoorOnlyOwners(BlockPos pos) {
        this.pos = pos;
    }

    public OpenCustomDoorOnlyOwners(FriendlyByteBuf buf) {
        this.pos = buf.readBlockPos();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {

        ServerPlayer player = ctx.get().getSender();

        ctx.get().enqueueWork(() -> {
            Level level = player.level();
            BlockState state = level.getBlockState(pos);

            if (!(state.getBlock() instanceof CompressedCustomDoorBlock)) {
                return;
            }

            CompressedCustomDoorBlock doorBlock = (CompressedCustomDoorBlock) state.getBlock();
            OwnableBlockEntity entity = (OwnableBlockEntity) level.getBlockEntity(pos);

            if (entity.isOwner(player) && doorBlock.onlyOwnerCanOpen()) {
                doorBlock.cylceOpen(level, state, pos, player);
                if(!doorBlock.isOpen(state))
                    player.displayClientMessage(Component.translatable("success.reddefense.dooropened"), true);
                 else player.displayClientMessage(Component.translatable("success.reddefense.doorclosed"), true);
            } else if (doorBlock.onlyOwnerCanOpen()) {
                player.displayClientMessage(Component.translatable("error.reddefense.notyourdoor"), true);
            }

        });

        ctx.get().setPacketHandled(true);
    }
}
