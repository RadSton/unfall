package io.radston12.unfall.blockentities.custom;

import io.radston12.unfall.blockentities.ModBlockEntities;
import io.radston12.unfall.blockentities.api.EnergyAcceptor;
import io.radston12.unfall.item.ModItems;
import io.radston12.unfall.menus.BlockCompressorMenu;
import io.radston12.unfall.recipes.BlockCompressingRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class BlockCompressorBlockEntity extends BlockEntity implements MenuProvider {

    protected final ContainerData data;
    private final ItemStackHandler itemHandler = new ItemStackHandler(2);
    private final EnergyAcceptor energyAcceptor = new EnergyAcceptor(10000);
    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();
    private LazyOptional<IEnergyStorage> lazyEnergyHandler = LazyOptional.empty();
    private int progress = 0, maxProgress = 100;

    public BlockCompressorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.BLOCK_COMPRESSING.get(), pos, state);
        data = createContainerData();
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER)
            return lazyItemHandler.cast();

        if (cap == ForgeCapabilities.ENERGY)
            return lazyEnergyHandler.cast();

        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
        lazyEnergyHandler = LazyOptional.of(() -> energyAcceptor);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
        lazyEnergyHandler.invalidate();
    }


    @Override
    public Component getDisplayName() {
        return Component.translatable("block.unfall.entity.block_compressor");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInv, Player player) {
        return new BlockCompressorMenu(containerId, playerInv, this, data);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        tag.put("inventory", itemHandler.serializeNBT());
        tag.putInt("progress", progress);
        tag.putInt("energyStored", energyAcceptor.getEnergyStored());

        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag tag) {
        itemHandler.deserializeNBT(tag.getCompound("inventory"));
        progress = tag.getInt("progress");
        energyAcceptor.setEnergyStored(tag.getInt("energyStored"));
        super.load(tag);
    }


    public void tick(Level level, BlockPos pos, BlockState state) {
        if (!canCraft()) {
            progress = 0;
            return;
        }
        if (energyAcceptor.getEnergyStored() < 10) return; // Dont reset progress

        progress++;
        energyAcceptor.extractEnergy(10, false);
        setChanged(level, pos, state);

        if (progress >= maxProgress) {
            craftItem();
            progress = 0;
        }

    }

    private void craftItem() {
        Optional<BlockCompressingRecipe> recipe = getCurrentRecipe();

        if (recipe.isEmpty()) return;

        ItemStack result = recipe.get().getResultItem(getLevel().registryAccess());

        this.itemHandler.extractItem(0, 1, false);

        this.itemHandler.setStackInSlot(1, new ItemStack(result.getItem(),
                this.itemHandler.getStackInSlot(1).getCount() + result.getCount()));
    }

    private boolean canCraft() {
        Optional<BlockCompressingRecipe> recipe = getCurrentRecipe();

        if (recipe.isEmpty()) return false;

        ItemStack resultItem = recipe.get().getResultItem(getLevel().registryAccess());

        boolean isSameItemType = itemHandler.getStackInSlot(1).isEmpty() || itemHandler.getStackInSlot(1).getItem() == resultItem.getItem();
        boolean canInsert = (itemHandler.getStackInSlot(1).getCount() + resultItem.getCount()) <= itemHandler.getStackInSlot(1).getMaxStackSize();
        return isSameItemType && canInsert;
    }

    private Optional<BlockCompressingRecipe> getCurrentRecipe() {
        SimpleContainer inventory = new SimpleContainer(this.itemHandler.getSlots());

        for (int i = 0; i < itemHandler.getSlots(); i++)
            inventory.setItem(i, this.itemHandler.getStackInSlot(i));

        return getLevel().getRecipeManager().getRecipeFor(BlockCompressingRecipe.Type.INSTANCE, inventory, getLevel());
    }

    private ContainerData createContainerData() {
        return new ContainerData() {
            @Override
            public int get(int id) {
                return switch (id) {
                    case 0 -> progress;
                    case 1 -> maxProgress;
                    case 2 -> energyAcceptor.getEnergyStored();
                    case 3 -> energyAcceptor.getMaxEnergyStored();
                    default -> -1;
                };
            }

            @Override
            public void set(int id, int value) {
                switch (id) {
                    case 0 -> progress = value;
                    case 1 -> maxProgress = value;
                    case 2 -> energyAcceptor.setEnergyStored(value);
                    case 3 -> energyAcceptor.setMaxEnergyStored(value);
                }
                ;
            }

            @Override
            public int getCount() {
                return 4;
            }
        };
    }


}
