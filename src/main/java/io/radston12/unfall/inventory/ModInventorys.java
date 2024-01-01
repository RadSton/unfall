package io.radston12.unfall.inventory;

import io.radston12.unfall.UnfallMod;
import io.radston12.unfall.inventory.custom.ReinforcedInventory;
import io.radston12.unfall.inventory.custom.UnfallModInventory;
import io.radston12.unfall.item.ModItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;

public class ModInventorys {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, UnfallMod.MOD_ID);
    public static final HashMap<RegistryObject<CreativeModeTab>, String> INV_TO_IDENTIFIER = new HashMap<>();
    public static final HashMap<String, UnfallModInventory> IDENTIFIER_TO_REGISTRY = new HashMap<>();

    public static final RegistryObject<CreativeModeTab> REINFORCED_TAB = registerCreativeTab(new ReinforcedInventory());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }

    public static void addItems(BuildCreativeModeTabContentsEvent event) {
        for (RegistryObject<CreativeModeTab> tab : INV_TO_IDENTIFIER.keySet()) {
            if (tab.getKey() != event.getTabKey()) continue;

            String key = INV_TO_IDENTIFIER.get(tab);
            IDENTIFIER_TO_REGISTRY.get(key).addItems(event);
            break;

        }
    }

    private static RegistryObject<CreativeModeTab> registerCreativeTab(UnfallModInventory tab) {
        RegistryObject<CreativeModeTab> creativeModeTab = CREATIVE_MODE_TABS.register(tab.getName(), () -> tab.getCreativeModeTab());
        INV_TO_IDENTIFIER.put(creativeModeTab, tab.getName());
        IDENTIFIER_TO_REGISTRY.put(tab.getName(), tab);
        return creativeModeTab;
    }


}
