package io.radston12.reddefense.recipes.finished;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.radston12.reddefense.recipes.BlockCompressingRecipe;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

import javax.annotation.Nullable;
import java.util.List;

public class FinishedBlockCompressingRecipe implements FinishedRecipe {
    private final ResourceLocation id;
    private final String group;
    private final List<Ingredient> ingredients;
    private final Item result;
    private final Advancement.Builder advancement;
    private final ResourceLocation advancementId;
    private final RecipeSerializer<BlockCompressingRecipe> serializer;
    private final int count;

    public FinishedBlockCompressingRecipe(ResourceLocation pId, String pGroup, List<Ingredient> pIngredients, Item pResult, Advancement.Builder pAdvancement, ResourceLocation pAdvancementId, RecipeSerializer<BlockCompressingRecipe> pSerializer, int count) {
        this.id = pId;
        this.group = pGroup;
        this.ingredients = pIngredients;
        this.result = pResult;
        this.advancement = pAdvancement;
        this.advancementId = pAdvancementId;
        this.serializer = pSerializer;
        this.count = count;
    }

    public void serializeRecipeData(JsonObject pJson) {
        if (!this.group.isEmpty()) {
            pJson.addProperty("group", this.group);
        }

        JsonArray ingredientArray = new JsonArray();
        for (Ingredient ingredient : ingredients) ingredientArray.add(ingredient.toJson());
        pJson.add("ingredients", ingredientArray);

        JsonObject output = new JsonObject();

        output.addProperty("item", BuiltInRegistries.ITEM.getKey(this.result).toString());
        output.addProperty("count", count);

        pJson.add("output", output);
    }

    public RecipeSerializer<?> getType() {
        return this.serializer;
    }


    public ResourceLocation getId() {
        return this.id;
    }


    @Nullable
    public JsonObject serializeAdvancement() {
        return this.advancement.serializeToJson();
    }

    @Nullable
    public ResourceLocation getAdvancementId() {
        return this.advancementId;
    }
}
