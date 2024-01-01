package io.radston12.unfall.menus;

import io.radston12.unfall.gui.GuiElement;
import io.radston12.unfall.gui.premade.SlotGuiElement;
import io.radston12.unfall.menus.utils.CustomAcceptingSlot;
import io.radston12.unfall.menus.utils.MenuHelper;
import io.radston12.unfall.menus.utils.ResultSlotHandler;
import io.radston12.unfall.menus.utils.callbacks.ShouldAcceptItem;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class UnfallMenu extends AbstractContainerMenu {

    private final int slotCount;
    private final boolean isItemMenu;

    private final Inventory inv;
    private final Player player;
    private final BlockEntity entity;
    private final Level level;

    private final List<GuiElement> GENERATED_GUI_ELEMENTS = new ArrayList<>();

    public UnfallMenu(@Nullable MenuType<?> menuType, int containerID, int slotCount, Inventory inv, BlockEntity entity) {
        super(menuType, containerID);

        this.slotCount = slotCount;
        this.inv = inv;
        this.player = inv.player;
        this.level = player.level();
        this.entity = entity;

        this.isItemMenu = entity == null;
    }

    public UnfallMenu(@Nullable MenuType<?> menuType, int containerID, int slotCount, Inventory inv) {
        this(menuType, containerID, slotCount, inv, null);
    }

    public void addAutomaticSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        this.addSlot(new SlotItemHandler(itemHandler, index, xPosition, yPosition));
        GENERATED_GUI_ELEMENTS.add(new SlotGuiElement(xPosition - 1, yPosition - 1));
    }

    public void addAcceptingSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition, ShouldAcceptItem item) {
        this.addSlot(new CustomAcceptingSlot(itemHandler, index, xPosition, yPosition, item));
        GENERATED_GUI_ELEMENTS.add(new SlotGuiElement(xPosition - 1, yPosition - 1));
    }

    public void addAutomaticOutputSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        this.addSlot(new ResultSlotHandler(itemHandler, index, xPosition, yPosition));
        GENERATED_GUI_ELEMENTS.add(new SlotGuiElement(xPosition - 1, yPosition - 1));
    }


    protected void addPlayerInventorySlots() {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(inv, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(inv, i, 8 + i * 18, 142));
        }
    }

    public List<GuiElement> getGeneratedGuiElements () {
        return GENERATED_GUI_ELEMENTS;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int pIndex) {
        return MenuHelper.handleQuickMoveStack(player, pIndex, slotCount, this, this::moveItemStackTo);
    }

    @Override
    public boolean stillValid(Player player) {
        if(isItemMenu) return true;
        return stillValid(ContainerLevelAccess.create(level, entity.getBlockPos()), this.player, level.getBlockState(entity.getBlockPos()).getBlock());
    }


    public int getSlotCount() {
        return slotCount;
    }

    public boolean isItemMenu() {
        return isItemMenu;
    }

    public Inventory getInv() {
        return inv;
    }

    public Player getPlayer() {
        return player;
    }

    public BlockEntity getEntity() {
        return entity;
    }

    public Level getLevel() {
        return level;
    }
}
