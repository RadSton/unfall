package io.radston12.unfall.datagen;

import io.radston12.unfall.UnfallMod;
import io.radston12.unfall.blocks.compressed.CompressedBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagsProvider extends BlockTagsProvider {

    public ModBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, UnfallMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(BlockTags.ENCHANTMENT_POWER_PROVIDER).add(CompressedBlocks.COMPRESSED_BOOKSHELF.get());

        tag(BlockTags.BEACON_BASE_BLOCKS).add(
                CompressedBlocks.COMPRESSED_DIAMOND_BLOCK.get(),
                CompressedBlocks.COMPRESSED_EMERALD_BLOCK.get(),
                CompressedBlocks.COMPRESSED_GOLD_BLOCK.get(),
                CompressedBlocks.COMPRESSED_IRON_BLOCK.get(),
                CompressedBlocks.COMPRESSED_NETHERITE_BLOCK.get());

        tag(BlockTags.SAND).add(CompressedBlocks.COMPRESSED_SAND.get());
    }
}
