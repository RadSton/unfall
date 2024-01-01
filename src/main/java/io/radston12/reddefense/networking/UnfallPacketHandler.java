package io.radston12.reddefense.networking;

import io.radston12.reddefense.RedDefenseMod;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class UnfallPacketHandler {
    private static final String PROTOCOL_VERSION = "1";
    private static int PACKET_ID = -1;
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(RedDefenseMod.MOD_ID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );


    public static void register() {
        //INSTANCE.registerMessage(PACKET_ID++, PortableJukeBoxMessagePacket.class, PortableJukeBoxMessagePacket::encode, PortableJukeBoxMessagePacket::new, PortableJukeBoxMessagePacket::handle);
    }

    public static <MSG> void sendMessageToServer(MSG message) {
        INSTANCE.sendTo(message, Minecraft.getInstance().getConnection().getConnection(), NetworkDirection.PLAY_TO_SERVER);
    }

}
