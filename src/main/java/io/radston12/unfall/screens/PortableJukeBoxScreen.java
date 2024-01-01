package io.radston12.unfall.screens;

import io.radston12.unfall.UnfallMod;
import io.radston12.unfall.gui.ButtonGuiElement;
import io.radston12.unfall.menus.PortableJukeBoxMenu;
import io.radston12.unfall.networking.UnfallPacketHandler;
import io.radston12.unfall.networking.packets.PortableJukeBoxMessagePacket;
import io.radston12.unfall.screens.api.UnfallScreen;
import io.radston12.unfall.sound.api.MovingSound;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.loading.FMLEnvironment;

public class PortableJukeBoxScreen extends UnfallScreen<PortableJukeBoxMenu> {

    public PortableJukeBoxScreen(PortableJukeBoxMenu portableJukeBoxMenu, Inventory inv, Component tag) {
        super(portableJukeBoxMenu, inv, tag);

        addGuiElement(
                new ButtonGuiElement(43,49, new ResourceLocation(UnfallMod.MOD_ID, "gui/elements/buttons/play.png"), Component.literal("Play Song"),
                        () -> {
                            Minecraft.getInstance().getSoundManager().stop();
                            Minecraft.getInstance().getSoundManager().queueTickingSound(new MovingSound(portableJukeBoxMenu.getSoundEventToPlay(9), inv.player));
                        }
                )
        );

        addGuiElement(
                new ButtonGuiElement(112 ,49,
                        new ResourceLocation(UnfallMod.MOD_ID, "gui/elements/buttons/stop.png"),
                        Component.literal("Stop Song"),
                        () -> Minecraft.getInstance().getSoundManager().stop()
                )
        );
    }

    private int i = 0;
    @Override
    public void update() {
        if(getMenu().getItemStackIn(9).isEmpty()) {
            if(i == 0)
                Minecraft.getInstance().getSoundManager().stop();

            i++;
            if(i >= 10) i = 0;
        } else
            i = 0;

    }

}
