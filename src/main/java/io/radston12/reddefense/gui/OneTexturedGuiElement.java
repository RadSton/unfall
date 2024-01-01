package io.radston12.reddefense.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public abstract class OneTexturedGuiElement extends GuiElement{

    private final ResourceLocation texture;

    public OneTexturedGuiElement(int x, int y, int width, int height, ResourceLocation texture) {
        super(x, y, width, height);

        this.texture = texture;
    }

    public void renderTexture(GuiGraphics guiGraphics, int fromX, int fromY, int toX, int toY, int texWidth, int texHeight) {
        guiGraphics.blit(texture, getX(), getY(), 0, (float)fromX, (float)fromY, toX, toY, texWidth, texHeight);
    }

    public void renderTexture(GuiGraphics guiGraphics, int fromX, int fromY, int toX, int toY) {
        guiGraphics.blit(texture, getX(), getY(), 0, (float)fromX, (float)fromY, toX, toY, getWidth(), getHeight());
    }

    public void renderTexture(GuiGraphics guiGraphics, int toX, int toY) {
        guiGraphics.blit(texture, getX(), getY(), 0, (float)0, (float)0, toX, toY, getWidth(), getHeight());
    }

    public void renderTexture(GuiGraphics guiGraphics) {
        guiGraphics.blit(texture, getX(), getY(), 0, (float)0, (float)0, getWidth(), getHeight(), getWidth(), getHeight());
    }

    public ResourceLocation getTexture() {
        return texture;
    }
}
