package io.radston12.unfall.menus;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;

public class BlockCompressorMenu extends UnfallMenu {

    private final ContainerData data;
    public BlockCompressorMenu(int containerId, Inventory inv, FriendlyByteBuf data) {
        this(containerId, inv, inv.player.level().getBlockEntity(data.readBlockPos()), new SimpleContainerData(4));
    }

    public BlockCompressorMenu(int containerId, Inventory inv, BlockEntity entity, ContainerData data) {
        super(ModMenuTypes.BLOCK_COMPRESSING_MENU.get(), containerId, 2, inv, entity);
        this.data = data;

        addPlayerInventorySlots();

        getEntity().getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(iItemHandler -> {
            this.addAutomaticSlot(iItemHandler, 0,19,36);
            this.addAutomaticOutputSlot(iItemHandler, 1,138,36);
        });

        addDataSlots(data);
    }

    public boolean isCrafting() {
        return data.get(0) > 0;
    }

    public int getPercent() {

        return (int) (((float)data.get(0)/(float)data.get(1)) * 100);
    }

    public int getScaledEnergy() {
        int energy = data.get(2);
        int maxEnergy = data.get(3);
        int progArrowSize = 52;
        return (int) (((float)energy/(float)maxEnergy) * (float)progArrowSize);
    }

    public int getEnergyStored() {
        return data.get(2);
    }

    public int getMaxEnergyStored() {
        return data.get(3);
    }


}
