package io.radston12.reddefense.datagen;

import io.radston12.reddefense.RedDefenseMod;
import io.radston12.reddefense.blocks.ModBlocks;
import io.radston12.reddefense.blocks.compressed.BaseCompressedBlock;
import io.radston12.reddefense.item.ModItems;
import io.radston12.reddefense.recipes.builder.BlockCompressingRecipeBuilder;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;

import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public ModRecipeProvider(PackOutput packOutput) {
        super(packOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
        for (String block : ModBlocks.BLOCKS_MAP.keySet()) {
            if(!block.startsWith("compressed")) continue;

            Block b = ModBlocks.BLOCKS_MAP.get(block).get();
            if(!(b instanceof BaseCompressedBlock)) continue;

            BaseCompressedBlock compressedBlock = (BaseCompressedBlock) b;

            if(!ModItems.ITEMS_MAP.containsKey(block)) continue;
            Item item = ModItems.getItem(block);

            BlockCompressingRecipeBuilder.createInstance(Ingredient.of(compressedBlock.getCraftingBlock()), item, 1)
                    .unlockedBy(getHasName(compressedBlock.getCraftingBlock()), has(compressedBlock.getCraftingBlock())).
                    save(consumer, new ResourceLocation(RedDefenseMod.MOD_ID + ":compressed" + "_" + getItemName(compressedBlock.getCraftingBlock())));
        }
    }
}
