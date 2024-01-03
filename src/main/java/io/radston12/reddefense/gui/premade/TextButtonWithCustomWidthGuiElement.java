package io.radston12.reddefense.gui.premade;

import io.radston12.reddefense.RedDefenseMod;
import io.radston12.reddefense.gui.TwoTexturedGuiElement;
import io.radston12.reddefense.gui.premade.handlers.ButtonCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.lwjgl.glfw.GLFW;

import java.util.function.Supplier;

public class TextButtonWithCustomWidthGuiElement extends TwoTexturedGuiElement {

    private final Component text;
    private final ButtonCallback callback;
    private final Component component;
    private final Supplier<Boolean> enabled;

    private final ResourceLocation disabled;

    public TextButtonWithCustomWidthGuiElement(int x, int y, int width, Component text, Component component, ButtonCallback callback) {
        this(x, y, width, text, component, () -> true, callback);
    }

    public TextButtonWithCustomWidthGuiElement(int x, int y, int width, Component text, Component component, Supplier<Boolean> enabled, ButtonCallback callback) {
        super(x, y, width, 20, new ResourceLocation(RedDefenseMod.MOD_ID, "gui/elements/buttons/empty_width_unmarked.png"), new ResourceLocation(RedDefenseMod.MOD_ID, "gui/elements/buttons/empty_width_marked.png"));

        this.disabled = new ResourceLocation(RedDefenseMod.MOD_ID, "gui/elements/buttons/empty_width_disabled.png");

        this.text = text;
        this.component = component;
        this.callback = callback;
        this.enabled = enabled;
    }

    @Override
    public void register(AbstractContainerMenu menu) {

    }

    private boolean clicked = false;

    @Override
    public void render(GuiGraphics graphics, float deltaTime, int mouseX, int mouseY) {

        if (isHovering(mouseX, mouseY) && clicked && enabled.get()) callback.onClick();

        ResourceLocation tex = getTexture(mouseX, mouseY);

        renderCustomTexture(graphics, tex, getX(), getY(), 0, 0, getWidth(), getHeight(), 256, getHeight());
        renderCustomTexture(graphics, tex, getEndX() - 2, getY(), 254, 0, 2, getHeight(), 256, getHeight());

        graphics.drawCenteredString(Minecraft.getInstance().font, text, getCenterX(), getCenterY() - 5, 0xFFFFFF);
        clicked = false;
    }

    private ResourceLocation getTexture(int mouseX, int mouseY) {
        if (!enabled.get()) return disabled;
        if (isHovering(mouseX, mouseY)) return getTextureTwo();
        return getTexture();
    }

    @Override
    public void renderTooltips(GuiGraphics graphics, int mouseX, int mouseY) {
        if (component != null)
            graphics.renderTooltip(Minecraft.getInstance().font, component, mouseX, mouseY);
    }

    @Override
    public void onKeyPress(int keycode) {
        clicked = keycode == GLFW.GLFW_MOUSE_BUTTON_LEFT;
    }
}
