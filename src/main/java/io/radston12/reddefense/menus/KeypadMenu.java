package io.radston12.reddefense.menus;

import io.radston12.reddefense.blockentities.custom.KeypadBlockEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.apache.commons.codec.binary.Hex;

import java.util.HexFormat;

public class KeypadMenu extends UnfallMenu {

    private final boolean isSetting;

    public KeypadMenu(int containerID, Inventory inv, BlockEntity entity, boolean isSetting) {
        super(ModMenuTypes.KEYPAD.get(), containerID, 0, inv, entity);

        this.isSetting = isSetting;

        setup();
    }

    public KeypadMenu(int containerID, Inventory inv, FriendlyByteBuf buf) {
        super(ModMenuTypes.KEYPAD.get(), containerID, 0, inv);

        automaticallyGetEntity();

        if (getEntity() == null) this.isSetting = false;
        else this.isSetting = ((KeypadBlockEntity) getEntity()).isSetting();

        setup();
    }

    public void setup() {
        addPlayerInventorySlots();
    }

    public boolean isSetting() {
        return isSetting;
    }
}
