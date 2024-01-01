package io.radston12.unfall.inventory.custom;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;

public abstract class UnfallModInventory  {
    private final String name;
    private final CreativeModeTab.Builder builder;

    protected UnfallModInventory(CreativeModeTab.Builder builder, String name) {
        this.builder = builder;
        this.name = name;
    }

    public abstract void addItems(BuildCreativeModeTabContentsEvent event);

    public String getName() {
        return name;
    }

    public CreativeModeTab getCreativeModeTab() {
        return builder.build();
    }
}
