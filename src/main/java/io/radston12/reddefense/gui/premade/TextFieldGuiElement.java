package io.radston12.reddefense.gui.premade;

import io.radston12.reddefense.RedDefenseMod;
import io.radston12.reddefense.gui.TwoTexturedGuiElement;
import io.radston12.reddefense.gui.premade.handlers.TextFieldCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.lwjgl.glfw.GLFW;

public class TextFieldGuiElement extends TwoTexturedGuiElement {

    private String string = "";
    private final TextFieldCallback callback;
    private final Component placeholder;

    private final boolean centerd;

    public TextFieldGuiElement(int offsetX, int offsetYy, int width, Component placeholder, TextFieldCallback callback) {
        super(offsetX, offsetYy, width, 22, new ResourceLocation(RedDefenseMod.MOD_ID, "gui/elements/textfield/marker.png"), new ResourceLocation(RedDefenseMod.MOD_ID, "gui/elements/textfield/field.png"));

        this.centerd = false;
        this.callback = callback;
        this.placeholder = placeholder;

    }

    public TextFieldGuiElement(int offsetX, int offsetYy, int width, Component placeholder, boolean center, TextFieldCallback callback) {
        super(offsetX, offsetYy, width, 22, new ResourceLocation(RedDefenseMod.MOD_ID, "gui/elements/textfield/marker.png"), new ResourceLocation(RedDefenseMod.MOD_ID, "gui/elements/textfield/field.png"));

        this.centerd = center;
        this.callback = callback;
        this.placeholder = placeholder;

    }

    @Override
    public void register(AbstractContainerMenu menu) {

    }

    @Override
    public void render(GuiGraphics graphics, float deltaTime, int mouseX, int mouseY) {
        renderTexture1(graphics, 0, 0, 1, getHeight(), 1, getHeight());
        renderTexture2(graphics, getX() + 1, getY(), 0, 0, getWidth(), getHeight(), 302, getHeight());
        renderTexture1(graphics, getEndX(), getY(), 0, 0, 1, getHeight(), 1, getHeight());

        if (!string.isEmpty()) {
            if(centerd) graphics.drawCenteredString(Minecraft.getInstance().font, string, getCenterX(), getCenterY() - 5, 0xFFFFFF);
            else graphics.drawString(Minecraft.getInstance().font, string, getX() + 7, getY() + 7, 0xFFFFFF);
        } else
            graphics.drawCenteredString(Minecraft.getInstance().font, placeholder, getCenterX(), getCenterY() - 5, 0x999999);
    }

    @Override
    public void renderTooltips(GuiGraphics graphics, int mouseX, int mouseY) {

    }

    @Override
    public void charTyped(char letter) {
        string += letter;
    }

    @Override
    public void onKeyPress(int keycode) {
        if (string.isEmpty()) return;

        if (keycode == GLFW.GLFW_KEY_BACKSPACE) string = string.substring(0, string.length() - 1);
        if (keycode == GLFW.GLFW_KEY_ENTER) if (callback.onEnter(string)) string = "";
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }
}
