package io.radston12.reddefense.screens;

import io.radston12.reddefense.blockentities.custom.KeypadBlockEntity;
import io.radston12.reddefense.gui.premade.TextButtonWithCustomWidthGuiElement;
import io.radston12.reddefense.gui.premade.TextFieldGuiElement;
import io.radston12.reddefense.menus.KeypadMenu;
import io.radston12.reddefense.networking.UnfallPacketHandler;
import io.radston12.reddefense.networking.packets.server.SetPasscode;
import io.radston12.reddefense.screens.api.UnfallScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import java.util.function.Supplier;

public class KeypadScreen extends UnfallScreen<KeypadMenu> {

    private Supplier<Boolean> enabled = () -> 4 <= getGuiElement(TextFieldGuiElement.class).getString().length() && getGuiElement(TextFieldGuiElement.class).getString().length() <= 12;

    public KeypadScreen(KeypadMenu menu, Inventory inventory, Component tag) {
        super(menu, inventory, tag);

        setShouldInventoryCloseMenu(false);

        addGuiElement(
            new TextFieldGuiElement(7, 20, 162, Component.literal("Enter the passkey"), true, this::handleCode)
        );

        addGuiElement(new TextButtonWithCustomWidthGuiElement(7, 45, 162, Component.literal("Enter"), null, enabled,
                () -> handleCode(getGuiElement(TextFieldGuiElement.class).getString())
        ));

    }

    public boolean handleCode(String passcode) {
        KeypadBlockEntity entity = getKeypadBlockEntity();

        if(entity == null) {
            // Called when getKeypadBlockEntity() is null this can happen when the menu doesn't get it because it's initialized with the FriendlyByteBuf
            // Maybe the fix with automaticallyGetEntity() already prevents this, but I am sure there is some way (maybe lag) that it fails here and I want to prevent the client from crashing
            // Temporary error
            getMenu().getPlayer().sendSystemMessage(Component.literal("§4[ERROR] Failed to set passcode! (NullPointerException ; Please open a issue with your latest.log)"));
            // Here the best (worst) way to get a stacktrace:
            try {
                throw new NullPointerException("Failed to getBlockPos() from getKeypadBlockEntity() because getKeypadBlockEntity() is null!");
            } catch (Exception e) {
                e.printStackTrace();
            }
            onClose(); // Close menu
            return false;
        }

        UnfallPacketHandler.sendMessageToServer(new SetPasscode(getKeypadBlockEntity().getBlockPos(), passcode));

        return false;
    }

    @Override
    public void update() {

    }

    public KeypadBlockEntity getKeypadBlockEntity() {
        return ((KeypadBlockEntity) getMenu().getEntity());
    }
}
