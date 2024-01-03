package io.radston12.reddefense.screens;

import io.radston12.reddefense.RedDefenseMod;
import io.radston12.reddefense.gui.premade.TexturedButtonGuiElement;
import io.radston12.reddefense.menus.PortableJukeBoxMenu;
import io.radston12.reddefense.screens.api.UnfallScreen;
import io.radston12.reddefense.sound.api.MovingSound;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class PortableJukeBoxScreen extends UnfallScreen<PortableJukeBoxMenu> {

    public PortableJukeBoxScreen(PortableJukeBoxMenu portableJukeBoxMenu, Inventory inv, Component tag) {
        super(portableJukeBoxMenu, inv, tag);

        addGuiElement(
                new TexturedButtonGuiElement(43,49, new ResourceLocation(RedDefenseMod.MOD_ID, "gui/elements/buttons/play.png"), Component.literal("Play Song"),
                        () -> {
                            Minecraft.getInstance().getSoundManager().stop();
                            Minecraft.getInstance().getSoundManager().queueTickingSound(new MovingSound(portableJukeBoxMenu.getSoundEventToPlay(9), inv.player));
                        }
                )
        );

        addGuiElement(
                new TexturedButtonGuiElement(112 ,49,
                        new ResourceLocation(RedDefenseMod.MOD_ID, "gui/elements/buttons/stop.png"),
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
