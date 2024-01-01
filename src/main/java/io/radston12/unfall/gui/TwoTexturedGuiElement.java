package io.radston12.unfall.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public abstract class TwoTexturedGuiElement extends GuiElement{
    private final ResourceLocation texture;
    private final ResourceLocation textureTwo;

    public TwoTexturedGuiElement(int x, int y, int width, int height, ResourceLocation texture, ResourceLocation textureTwo) {
        super(x, y, width, height);

        this.texture = texture;
        this.textureTwo = textureTwo;
    }

    public void renderCustomTexture(GuiGraphics guiGraphics, ResourceLocation texture, int x, int y, int fromX, int fromY, int toX, int toY, int texWidth, int texHeight) {
        guiGraphics.blit(texture, x, y, 0, (float)fromX, (float)fromY, toX, toY, texWidth, texHeight);
    }

    public void renderTexture1(GuiGraphics guiGraphics, int x, int y, int fromX, int fromY, int toX, int toY, int texWidth, int texHeight) {
        guiGraphics.blit(texture, x, y, 0, (float)fromX, (float)fromY, toX, toY, texWidth, texHeight);
    }

    public void renderTexture1(GuiGraphics guiGraphics, int fromX, int fromY, int toX, int toY, int texWidth, int texHeight) {
        guiGraphics.blit(texture, getX(), getY(), 0, (float)fromX, (float)fromY, toX, toY, texWidth, texHeight);
    }

    public void renderTexture1(GuiGraphics guiGraphics, int fromX, int fromY, int toX, int toY) {
        guiGraphics.blit(texture, getX(), getY(), 0, (float)fromX, (float)fromY, toX, toY, getWidth(), getHeight());
    }

    public void renderTexture1(GuiGraphics guiGraphics, int toX, int toY) {
        guiGraphics.blit(texture, getX(), getY(), 0, (float)0, (float)0, toX, toY, getWidth(), getHeight());
    }

    public void renderTexture1(GuiGraphics guiGraphics) {
        guiGraphics.blit(texture, getX(), getY(), 0, (float)0, (float)0, getWidth(), getHeight(), getWidth(), getHeight());
    }

    public void renderTexture2(GuiGraphics guiGraphics, int x, int y, int fromX, int fromY, int toX, int toY, int texWidth, int texHeight) {
        guiGraphics.blit(textureTwo, x, y, 0, (float)fromX, (float)fromY, toX, toY, texWidth, texHeight);
    }

    public void renderTexture2(GuiGraphics guiGraphics, int fromX, int fromY, int toX, int toY, int texWidth, int texHeight) {
        guiGraphics.blit(textureTwo, getX(), getY(), 0, (float)fromX, (float)fromY, toX, toY, texWidth, texHeight);
    }

    public void renderTexture2(GuiGraphics guiGraphics, int fromX, int fromY, int toX, int toY) {
        guiGraphics.blit(textureTwo, getX(), getY(), 0, (float)fromX, (float)fromY, toX, toY, getWidth(), getHeight());
    }

    public void renderTexture2(GuiGraphics guiGraphics, int toX, int toY) {
        guiGraphics.blit(textureTwo, getX(), getY(), 0, (float)0, (float)0, toX, toY, getWidth(), getHeight());
    }

    public void renderTexture2(GuiGraphics guiGraphics) {
        guiGraphics.blit(textureTwo, getX(), getY(), 0, (float)0, (float)0, getWidth(), getHeight(), getWidth(), getHeight());
    }

    public ResourceLocation getTexture() {
        return texture;
    }

    public ResourceLocation getTextureTwo() {
        return textureTwo;
    }
}
