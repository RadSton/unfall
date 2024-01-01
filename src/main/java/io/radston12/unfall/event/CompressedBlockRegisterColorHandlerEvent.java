package io.radston12.unfall.event;

import io.radston12.unfall.UnfallMod;
import io.radston12.unfall.blocks.ModBlocks;
import io.radston12.unfall.item.ModItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.security.InvalidParameterException;

@Mod.EventBusSubscriber(modid = UnfallMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class CompressedBlockRegisterColorHandlerEvent {

    public static int CONST = 0x555555;
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onRegisterColorHandlersBlock(RegisterColorHandlersEvent.Block event) {
        for(String key : ModBlocks.UNFALL_BLOCKS.keySet()) {
            if(key.contains("black_concrete")) {
                event.register((state, world, pos, tintIndex) -> 0x000000, ModBlocks.BLOCKS_MAP.get(key).get());
                continue;
            }

            if(key.contains("compressed")) {
                event.register((state, world, pos, tintIndex) -> CONST, ModBlocks.BLOCKS_MAP.get(key).get());
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onRegisterColorHandlersItem(RegisterColorHandlersEvent.Item event) {
        for(String key : ModItems.ITEMS_MAP.keySet()) {
            if(key.contains("black_concrete")) {
                event.register((item, tintIndex) -> 0x000000, ModItems.ITEMS_MAP.get(key).get());
                continue;
            }

            if(key.contains("compressed")) {
                event.register((item, tintIndex) -> CONST, ModItems.ITEMS_MAP.get(key).get());
            }
        }

    }
}
