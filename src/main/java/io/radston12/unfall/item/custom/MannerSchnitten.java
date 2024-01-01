package io.radston12.unfall.item.custom;

import io.radston12.unfall.item.api.UnfallItem;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;

public class MannerSchnitten extends UnfallItem {

    public MannerSchnitten() {
        super(getProperties());
    }

    public static Item.Properties getProperties() {
        return new Item.Properties().food(
                new FoodProperties.Builder()
                        .nutrition(100)
                        .saturationMod(100)
                        .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 120 * 20, 2), 1f)
                        .effect(() -> new MobEffectInstance(MobEffects.DIG_SPEED, 90 * 20, 1), 1f)
                        .effect(() -> new MobEffectInstance(MobEffects.JUMP, 10 * 20, 1), 1f)
                        .build()
        );

    }
}
