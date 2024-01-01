package io.radston12.unfall.gui.premade;

import io.radston12.unfall.UnfallMod;
import io.radston12.unfall.gui.OneTexturedGuiElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;

import java.util.function.Supplier;

public class SlotGuiElement extends OneTexturedGuiElement {

    public SlotGuiElement(int x, int y) {
        super(x, y, 18, 18, new ResourceLocation(UnfallMod.MOD_ID, "gui/elements/slot.png"));
    }

    @Override
    public void register(AbstractContainerMenu menu) {

    }

    @Override
    public void render(GuiGraphics graphics, float deltaTime, int mouseX, int mouseY) {
        renderTexture(graphics, 0,0,18,18);
    }

    @Override
    public void renderTooltips(GuiGraphics graphics, int mouseX, int mouseY) {

    }

}
