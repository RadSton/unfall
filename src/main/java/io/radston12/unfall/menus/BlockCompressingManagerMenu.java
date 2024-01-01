package io.radston12.unfall.menus;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class BlockCompressingManagerMenu extends UnfallMenu {

    private final ItemStack stack;

    public BlockCompressingManagerMenu(int containerID, Inventory inv, FriendlyByteBuf buf) {
        super(ModMenuTypes.BLOCK_OWNER_MANAGER_MENU.get(), containerID, 0, inv);

        if (getPlayer().getMainHandItem().isEmpty()) this.stack = getPlayer().getOffhandItem();
        else this.stack = getPlayer().getMainHandItem();

        setup();
    }

    public BlockCompressingManagerMenu(int containerID, Inventory inv) {
        super(ModMenuTypes.BLOCK_OWNER_MANAGER_MENU.get(), containerID, 0, inv);

        if (getPlayer().getMainHandItem().isEmpty()) this.stack = getPlayer().getOffhandItem();
        else this.stack = getPlayer().getMainHandItem();

        setup();
    }


    public void setup() {
        addPlayerInventorySlots();
    }
}
