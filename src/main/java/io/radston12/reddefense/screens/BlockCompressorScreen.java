package io.radston12.reddefense.screens;

import io.radston12.reddefense.RedDefenseMod;
import io.radston12.reddefense.gui.premade.MekanismEnergyVerticalGuiElement;
import io.radston12.reddefense.gui.premade.ProgressArrowHorizontalGuiElement;
import io.radston12.reddefense.gui.premade.handlers.EnergyHandler;
import io.radston12.reddefense.menus.BlockCompressorMenu;
import io.radston12.reddefense.screens.api.UnfallScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class BlockCompressorScreen extends UnfallScreen<BlockCompressorMenu> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(RedDefenseMod.MOD_ID, "gui/block_compressing_gui.png");

    public BlockCompressorScreen(BlockCompressorMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);

        addGuiElement(new MekanismEnergyVerticalGuiElement(116, 73, new EnergyHandler() {

            @Override
            public int getEnergy() {
                return menu.getEnergyStored();
            }

            @Override
            public int getMaxEnergy() {
                return menu.getMaxEnergyStored();
            }

        }));

        addGuiElement(new ProgressArrowHorizontalGuiElement(72, 41, getMenu()::getPercent));
    }

    @Override
    public void update() {

    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        super.renderBg(guiGraphics, partialTick, mouseX, mouseY);

       // if (getMenu().isCrafting()) guiGraphics.blit(TEXTURE, getCenterX() + 72, getCenterY() + 41, 176, 0, getMenu().getPercent(), 8);
    }

}
