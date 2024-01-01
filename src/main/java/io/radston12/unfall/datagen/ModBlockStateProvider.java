package io.radston12.unfall.datagen;

import io.radston12.unfall.UnfallMod;
import io.radston12.unfall.blocks.ModBlocks;
import io.radston12.unfall.blocks.api.UnfallBlock;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlockStateProvider extends BlockStateProvider {

    public ModBlockStateProvider(PackOutput output, ExistingFileHelper fileHelper) {
        super(output, UnfallMod.MOD_ID, fileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        for(String key : ModBlocks.BLOCKS_MAP.keySet()) {
            RegistryObject<Block> block = (RegistryObject<Block>) ModBlocks.BLOCKS_MAP.get(key);

            boolean isUnfall = ModBlocks.UNFALL_BLOCKS.containsKey(key);

            if(isUnfall && !ModBlocks.UNFALL_BLOCKS.get(key).get().shouldAutomaticallyGenerateProviderData()) continue;

            simpleBlockItem(block.get(), new ModelFile.UncheckedModelFile(modLoc("block/" + key)));

            if(!isUnfall)
                simpleBlock(block.get());
        }



        for(String key : ModBlocks.UNFALL_BLOCKS.keySet()) {
            Supplier<UnfallBlock> block = ModBlocks.UNFALL_BLOCKS.get(key);
            if(!block.get().shouldAutomaticallyGenerateProviderData()) continue;
            block.get().generateBlockStateProviderData(this, ModBlocks.BLOCKS_MAP.get(key));
        }

    }

}
