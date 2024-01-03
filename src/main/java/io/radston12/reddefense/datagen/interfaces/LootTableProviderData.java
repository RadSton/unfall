package io.radston12.reddefense.datagen.interfaces;

import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.predicates.ConditionUserBuilder;

public interface LootTableProviderData {
    <T extends ConditionUserBuilder<T>> T applyExplosionCondition(ItemLike itemLike, ConditionUserBuilder<T> conditionUserBuilder);
}
