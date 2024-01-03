package io.radston12.reddefense.datagen;

import io.radston12.reddefense.RedDefenseMod;
import io.radston12.reddefense.blocks.ModBlocks;
import io.radston12.reddefense.blocks.compressed.CompressedBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModBlockTagsProvider extends BlockTagsProvider {

    public ModBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, RedDefenseMod.MOD_ID, existingFileHelper);
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

        List<Block> b = new ArrayList<>();

        tag(BlockTags.SAND).add(CompressedBlocks.COMPRESSED_SAND.get());
        for (String blockKey : ModBlocks.BLOCKS_MAP.keySet())
            if (blockKey.startsWith("compressed"))
                b.add(ModBlocks.BLOCKS_MAP.get(blockKey).get());

        Block[] bArray = b.toArray(new Block[b.size()]);

        tag(BlockTags.WITHER_IMMUNE).add(bArray);
        tag(BlockTags.DRAGON_IMMUNE).add(bArray);
    }
}
