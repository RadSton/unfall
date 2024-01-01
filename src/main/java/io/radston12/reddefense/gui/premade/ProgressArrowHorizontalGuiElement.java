package io.radston12.reddefense.gui.premade;

import io.radston12.reddefense.RedDefenseMod;
import io.radston12.reddefense.gui.TwoTexturedGuiElement;
import io.radston12.reddefense.gui.premade.handlers.PercentageCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class ProgressArrowHorizontalGuiElement extends TwoTexturedGuiElement {

    private final PercentageCallback callback;

    public ProgressArrowHorizontalGuiElement(int x, int y, PercentageCallback callback) {
        super(x, y, 26, 8, new ResourceLocation(RedDefenseMod.MOD_ID, "gui/elements/progress/arrow_horizontal_empty.png"), new ResourceLocation(RedDefenseMod.MOD_ID, "gui/elements/progress/arrow_horizontal.png"));

        this.callback = callback;
    }

    @Override
    public void register(AbstractContainerMenu menu) {

    }

    @Override
    public void render(GuiGraphics graphics, float deltaTime, int mouseX, int mouseY) {
        renderTexture1(graphics, 0, 0, 26, 7, 26, 7);
        int percentage = callback.getPercentage();
        if (percentage == 0) return;

        int width = Math.min((int) (26 * (percentage / 99.9F)), 26);

        renderTexture2(graphics, 0, 0, width, 8);

    }

    @Override
    public void renderTooltips(GuiGraphics graphics, int mouseX, int mouseY) {
        graphics.renderTooltip(Minecraft.getInstance().font, Component.literal(callback.getPercentage() + "%"), mouseX, mouseY);
    }
}
