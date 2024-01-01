package io.radston12.reddefense.screens;

import io.radston12.reddefense.gui.premade.TextFieldGuiElement;
import io.radston12.reddefense.menus.BlockCompressingManagerMenu;
import io.radston12.reddefense.screens.api.UnfallScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
public class BlockCompressingManagerScreen extends UnfallScreen<BlockCompressingManagerMenu> {

    public BlockCompressingManagerScreen(BlockCompressingManagerMenu menu, Inventory inventory, Component tag) {
        super(menu, inventory, tag);

        setShouldInventoryCloseMenu(false);

        addGuiElement(new TextFieldGuiElement(17, 15, 144, (string) -> {

            return true;
        }));
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float deltaTime, int mouseX, int mouseY) {
        super.renderBg(guiGraphics, deltaTime, mouseX, mouseY);

    }

    @Override
    public void update() {

    }

}
