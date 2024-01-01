package io.radston12.reddefense.networking.packets;

import io.radston12.reddefense.sound.api.MovingSound;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record PortableJukeBoxMessagePacket(int entityId, SoundEvent event, boolean startOrStop)  {

    public static void handle(PortableJukeBoxMessagePacket msg, Supplier<NetworkEvent.Context> ctx) {

        System.out.println("PortableJukeBoxMessagePacket::handle");
        NetworkEvent.Context context = ctx.get();

        context.enqueueWork(() -> {
            System.out.println(FMLEnvironment.dist);
            DistExecutor.unsafeRunWhenOn(Dist.DEDICATED_SERVER, () -> () -> msg.handle(context));
        });
        context.setPacketHandled(true);
    }

    public PortableJukeBoxMessagePacket(FriendlyByteBuf buf) {
        this(buf.readInt(), SoundEvent.readFromNetwork(buf), buf.readBoolean());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(entityId);
        event.writeToNetwork(buf);
        buf.writeBoolean(startOrStop);
    }

    public void handle(NetworkEvent.Context context) {

        System.out.println("PortableJukeBoxMessagePacket::private::handle");

        Level level = Minecraft.getInstance().level;
        if(level == null || level.isClientSide) return; // impossible

        System.out.println("PortableJukeBoxMessagePacket::private::handle");

        Entity entity = level.getEntity(entityId);
        if(entity == null) return;

        System.out.println("PortableJukeBoxMessagePacket::private::handle");

        Minecraft.getInstance().getSoundManager().stop();
        if(startOrStop) Minecraft.getInstance().getSoundManager().queueTickingSound(new MovingSound(event, entity));
    }


}
