package io.radston12.unfall.datagen;

import io.radston12.unfall.UnfallMod;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = UnfallMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();
        PackOutput output = gen.getPackOutput();
        ExistingFileHelper fileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        gen.addProvider(event.includeServer(), new ModRecipeProvider(output));
        gen.addProvider(event.includeClient(), ModLootTableProvider.create(output));

        gen.addProvider(event.includeClient(), new ModBlockStateProvider(output, fileHelper));
        gen.addProvider(event.includeClient(), new ModItemModelProvider(output, fileHelper));

        gen.addProvider(event.includeServer(), new ModBlockTagsProvider(output, lookupProvider, fileHelper));
        // TODO: ModItemTagGenerator
    }
}
