package io.radston12.reddefense.inventory.custom;

import io.radston12.reddefense.item.ModItems;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;

public class ReinforcedInventory extends UnfallModInventory {

    public ReinforcedInventory() {
        super(createBuilder(), "unfall_reinforced");
    }

    public static CreativeModeTab.Builder createBuilder() {
        return CreativeModeTab.builder()
                .title(Component.translatable("creative.reddefense.reinforced"))
                .icon( () -> ModItems.getItemAsItemStack("manner"));
    }

    @Override
    public void addItems(BuildCreativeModeTabContentsEvent event) {
        for(String key : ModItems.BLOCK_ITEMS) {
           // if(key.contains("reinforced"))
                event.accept(ModItems.getItem(key));
        }
    }
}
