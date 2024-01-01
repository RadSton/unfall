package io.radston12.unfall.recipes.builder;

import com.google.common.collect.Lists;
import io.radston12.unfall.recipes.BlockCompressingRecipe;
import io.radston12.unfall.recipes.finished.FinishedBlockCompressingRecipe;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class BlockCompressingRecipeBuilder implements RecipeBuilder {
    private final Item result;
    private final RecipeSerializer<BlockCompressingRecipe> serializer;
    private String group;
    private final List<Ingredient> ingredients = Lists.newArrayList();
    private final Advancement.Builder advancement = Advancement.Builder.recipeAdvancement();

    private final int count;

    public static BlockCompressingRecipeBuilder createInstance(Ingredient ingredient, ItemStack result) {
        return new BlockCompressingRecipeBuilder(ingredient, result.getItem(), result.getCount(), BlockCompressingRecipe.Serializer.INSTANCE);
    }
    public static BlockCompressingRecipeBuilder createInstance(Ingredient ingredient, Item result, int count) {
        return new BlockCompressingRecipeBuilder(ingredient, result, count, BlockCompressingRecipe.Serializer.INSTANCE);
    }

    public BlockCompressingRecipeBuilder(Ingredient ingredient, Item result, int count,  RecipeSerializer<BlockCompressingRecipe> serializer) {
        this.ingredients.add(ingredient);
        this.result = result;
        this.count = count;
        this.serializer = serializer;
    }

    @Override
    public RecipeBuilder unlockedBy(String pCriterionName, CriterionTriggerInstance pCriterionTrigger) {
        this.advancement.addCriterion(pCriterionName, pCriterionTrigger);
        return this;
    }

    @Override
    public RecipeBuilder group(@Nullable String group) {
        this.group = group;
        return this;
    }

    @Override
    public Item getResult() {
        return result;
    }

    @Override
    public void save(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ResourceLocation pRecipeId) {
        this.ensureValid(pRecipeId);
        this.advancement.parent(ROOT_RECIPE_ADVANCEMENT).addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(pRecipeId)).rewards(AdvancementRewards.Builder.recipe(pRecipeId)).requirements(RequirementsStrategy.OR);
        pFinishedRecipeConsumer.accept(
                new FinishedBlockCompressingRecipe(
                        pRecipeId,
                        this.group == null ? "" : this.group,
                        ingredients,
                        result,
                        advancement,
                        pRecipeId.withPrefix("recipes/block_compressing/"),
                        this.serializer,
                        count
                )
        );
    }

    private void ensureValid(ResourceLocation pId) {
        if (this.advancement.getCriteria().isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + pId);
        }
    }
}
