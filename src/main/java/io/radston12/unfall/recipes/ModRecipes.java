package io.radston12.unfall.recipes;

import io.radston12.unfall.UnfallMod;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes {

    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, UnfallMod.MOD_ID);

    public static final RegistryObject<RecipeSerializer<BlockCompressingRecipe>> BLOCK_COMPRESSING_RECIPE = SERIALIZERS.register("block_compressing", () -> BlockCompressingRecipe.Serializer.INSTANCE);

    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
    }
}
