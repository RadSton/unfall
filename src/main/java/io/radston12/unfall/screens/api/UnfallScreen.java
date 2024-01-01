package io.radston12.unfall.screens.api;

import com.mojang.blaze3d.systems.RenderSystem;
import io.radston12.unfall.UnfallMod;
import io.radston12.unfall.gui.GuiElement;
import io.radston12.unfall.menus.UnfallMenu;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public abstract class UnfallScreen<T extends UnfallMenu> extends AbstractContainerScreen<T> {
    private static final ResourceLocation BASIC = new ResourceLocation(UnfallMod.MOD_ID, "gui/basic_gui.png");

    private final List<GuiElement> GUI_ELEMENTS = new ArrayList<>();

    private final T menu;

    private boolean shouldInventoryCloseMenu = true;

    public UnfallScreen(T menu, Inventory inventory, Component tag) {
        super(menu, inventory, tag);

        for(GuiElement ele : menu.getGeneratedGuiElements())
            addGuiElement(ele);

        this.menu = menu;
    }

    public abstract void update();

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float deltaTime, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, BASIC);

        guiGraphics.blit(BASIC, getCenterX(), getCenterY(), 0, 0, imageWidth, imageHeight);

        // Render GUI Elements
        for (GuiElement element : GUI_ELEMENTS)
            element.render(guiGraphics, deltaTime, mouseX, mouseY);

        update();
    }

    @Override
    protected void renderTooltip(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        // Render GUI Elements Tooltips

        for (GuiElement element : GUI_ELEMENTS)
            element.automaticlyRenderTooltip(guiGraphics, mouseX, mouseY);

        super.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        for(GuiElement el : GUI_ELEMENTS) el.onKeyPress(keyCode);

        if(shouldInventoryCloseMenu) return super.keyPressed(keyCode, scanCode, modifiers);

        if(keyCode != GLFW.GLFW_KEY_E) return super.keyPressed(keyCode, scanCode, modifiers);

        return true;
    }

    @Override
    public boolean charTyped(char p_94683_, int p_94684_) {
        for(GuiElement el : GUI_ELEMENTS) el.charTyped(p_94683_);
        return super.charTyped(p_94683_, p_94684_);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        for(GuiElement el : GUI_ELEMENTS) el.onKeyPress(mouseButton);

        return super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    public void addGuiElement(GuiElement element) {
        element.setParent(this);
        GUI_ELEMENTS.add(element);
    }

    @Override
    public T getMenu() {
        return menu;
    }

    public int getCenterX() {
        return (width - imageWidth) / 2;
    }

    public int getCenterY() {
        return (height - imageHeight) / 2;
    }

    public boolean shouldInventoryCloseMenu() {
        return shouldInventoryCloseMenu;
    }

    public void setShouldInventoryCloseMenu(boolean shouldInventoryCloseMenu) {
        this.shouldInventoryCloseMenu = shouldInventoryCloseMenu;
    }
}
