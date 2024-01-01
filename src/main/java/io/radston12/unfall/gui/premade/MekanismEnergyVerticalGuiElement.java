package io.radston12.unfall.gui.premade;

import io.radston12.unfall.UnfallMod;
import io.radston12.unfall.energy.EnergyStringHelper;
import io.radston12.unfall.gui.TwoTexturedGuiElement;
import io.radston12.unfall.gui.premade.handlers.EnergyHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class MekanismEnergyVerticalGuiElement extends TwoTexturedGuiElement {

    private final EnergyHandler energy;

    public MekanismEnergyVerticalGuiElement(int x, int y, EnergyHandler energy) {
        super(x, y, 54, 6, new ResourceLocation(UnfallMod.MOD_ID, "gui/elements/horizontal_bar_bg.png"), new ResourceLocation(UnfallMod.MOD_ID, "gui/elements/horizontal_power.png"));

        this.energy = energy;
    }

    @Override
    public void register(AbstractContainerMenu menu) {

    }

    @Override
    public void render(GuiGraphics graphics, float deltaTime, int mouseX, int mouseY) {
        renderTexture1(graphics);
        renderTexture2(graphics,  getX() + 1,  getY() + 1,  0,  0,  (int) (((float)energy.getEnergy() / (float)energy.getMaxEnergy()) * 52) ,  4,  52,  4);
    }

    @Override
    public void renderTooltips(GuiGraphics graphics, int mouseX, int mouseY) {
        graphics.renderTooltip(Minecraft.getInstance().font, Component.literal(EnergyStringHelper.generateEnergyHoverText(energy.getEnergy(), energy.getMaxEnergy())), mouseX, mouseY);
    }

}
