package io.radston12.reddefense.networking;

import io.radston12.reddefense.RedDefenseMod;
import io.radston12.reddefense.networking.packets.server.OpenCustomDoorOnlyOwners;
import io.radston12.reddefense.networking.packets.server.SetPasscode;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class UnfallPacketHandler {
    private static final String PROTOCOL_VERSION = "1";
    private static int PACKET_ID = 0;
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(RedDefenseMod.MOD_ID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );


    public static void register() {
        INSTANCE.registerMessage(PACKET_ID++, SetPasscode.class, SetPasscode::encode, SetPasscode::new, SetPasscode::handle);
        INSTANCE.registerMessage(PACKET_ID++, OpenCustomDoorOnlyOwners.class, OpenCustomDoorOnlyOwners::encode, OpenCustomDoorOnlyOwners::new, OpenCustomDoorOnlyOwners::handle);
        //INSTANCE.registerMessage(PACKET_ID++, KeypadRequest.class, KeypadRequest::encode, KeypadRequest::new, KeypadRequest::handle);
    }

    public static <MSG> void sendMessageToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendMessageToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

}
