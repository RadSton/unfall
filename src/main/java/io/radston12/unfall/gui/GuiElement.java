package io.radston12.unfall.gui;

import io.radston12.unfall.screens.api.UnfallScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.inventory.AbstractContainerMenu;

public abstract class GuiElement {

    private int offsetX;
    private int offsetY;
    private final int width, height;
    private UnfallScreen parent;

    public GuiElement(int offsetX, int offsetYy, int width, int height) {
        this.offsetX = offsetX;
        this.offsetY = offsetYy;
        this.width = width;
        this.height = height;
    }

    public boolean isHovering(int mouseX, int mouseY) {
        return mouseX >= getX() && mouseX <= getEndX() && mouseY >= getY() && mouseY <= getEndY();
    }

    public void automaticlyRenderTooltip(GuiGraphics graphics, int mouseX, int mouseY) {
        if(isHovering(mouseX, mouseY))
            renderTooltips(graphics, mouseX, mouseY);
    }


    public void setParent(UnfallScreen parent) {
        this.parent = parent;
    }

    public UnfallScreen getParent() {
        return parent;
    }

    public abstract void register(AbstractContainerMenu menu);
    public abstract void render(GuiGraphics graphics, float deltaTime, int mouseX, int mouseY);
    public abstract void renderTooltips(GuiGraphics graphics, int mouseX, int mouseY);
    public void onKeyPress(int keycode) {}
    public void charTyped(char letter) {}

    public void setXOffset(int x) {
        this.offsetX = x;
    }

    public void setYOffset(int y) {
        this.offsetY = y;
    }

    public int getX() {
        return offsetX + parent.getCenterX();
    }

    public int getY() {
        return offsetY + parent.getCenterY();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getEndX() {
        return width + getX();
    }

    public int getEndY() {
        return height + getY();
    }
}
