package io.radston12.reddefense.gui.premade;

import io.radston12.reddefense.RedDefenseMod;
import io.radston12.reddefense.gui.OneTexturedGuiElement;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class SlotGuiElement extends OneTexturedGuiElement {

    public SlotGuiElement(int x, int y) {
        super(x, y, 18, 18, new ResourceLocation(RedDefenseMod.MOD_ID, "gui/elements/slot.png"));
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
