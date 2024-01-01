package io.radston12.reddefense.gui;

import io.radston12.reddefense.RedDefenseMod;
import io.radston12.reddefense.gui.premade.handlers.ButtonCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.lwjgl.glfw.GLFW;

public class ButtonGuiElement extends TwoTexturedGuiElement {

    private final ResourceLocation buttonIcon;
    private final ButtonCallback callback;
    private final Component component;

    public ButtonGuiElement(int x, int y, ResourceLocation texture, Component component,  ButtonCallback callback) {
        super(x, y, 20, 20, new ResourceLocation(RedDefenseMod.MOD_ID, "gui/elements/buttons/empty_unmarked.png"), new ResourceLocation(RedDefenseMod.MOD_ID, "gui/elements/buttons/empty_marked.png"));

        this.buttonIcon = texture;
        this.component = component;
        this.callback = callback;
    }

    @Override
    public void register(AbstractContainerMenu menu) {

    }

    private boolean clicked = false;
    @Override
    public void render(GuiGraphics graphics, float deltaTime, int mouseX, int mouseY) {
        if (isHovering(mouseX, mouseY)) {
            if(clicked) callback.onClick();
            renderTexture2(graphics);
        } else
            renderTexture1(graphics);

        renderCustomTexture(graphics, buttonIcon, getX(), getY(), 0, 0, 20, 20, 20, 20);
        clicked = false;
    }

    @Override
    public void renderTooltips(GuiGraphics graphics, int mouseX, int mouseY) {
        graphics.renderTooltip(Minecraft.getInstance().font, component, mouseX, mouseY);
    }

    @Override
    public void onKeyPress(int keycode) {
        clicked = keycode == GLFW.GLFW_MOUSE_BUTTON_LEFT;
    }
}
