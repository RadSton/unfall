package io.radston12.unfall.menus;

import io.radston12.unfall.item.custom.PortableDiscPlayer;
import io.radston12.unfall.menus.utils.MenuHelper;
import io.radston12.unfall.screens.PortableJukeBoxScreen;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.RecordItem;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;

public class PortableJukeBoxMenu extends UnfallMenu {

    private ItemStack portableJukeboxPlayer = null;

    public PortableJukeBoxMenu(int containerId, Inventory inv, FriendlyByteBuf buf) {
        super(ModMenuTypes.PORTABE_JUKEBOX_MENU.get(), containerId, 10, inv);

        setup(null);
    }

    public PortableJukeBoxMenu(int containerId, Inventory inv, PortableDiscPlayer data) {
        super(ModMenuTypes.PORTABE_JUKEBOX_MENU.get(), containerId, 10, inv);

        setup(data);
    }

    public void setup(PortableDiscPlayer data) {

        portableJukeboxPlayer = getPlayer().getItemInHand(InteractionHand.MAIN_HAND);
        if (portableJukeboxPlayer.isEmpty()) portableJukeboxPlayer = getPlayer().getItemInHand(InteractionHand.OFF_HAND);

        portableJukeboxPlayer.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent((handler) -> {;
            for (int i = 0; i < 9; i++)
                this.addAcceptingSlot(handler, i, i * 18 + 8, 18, (stack) -> (stack.getItem() instanceof RecordItem));
            this.addAcceptingSlot(handler, 9, 80, 51, (stack) -> {
                if(stack.isEmpty()) return false;
                return (stack.getItem() instanceof RecordItem);
            });
        });


        addPlayerInventorySlots();
    }

    public ItemStack getItemStackIn(int index) {
        return getSlot(index).getItem();
    }

    public SoundEvent getSoundEventToPlay(int index) {
        ItemStack stack = getItemStackIn(index);

        if(stack == null || stack.isEmpty() || !(stack.getItem() instanceof RecordItem)) return SoundEvents.VILLAGER_NO;

        return ((RecordItem) stack.getItem()).getSound();
    }

}
