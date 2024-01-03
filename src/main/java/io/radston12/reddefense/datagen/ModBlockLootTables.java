package io.radston12.reddefense.datagen;

import io.radston12.reddefense.blocks.ModBlocks;
import io.radston12.reddefense.blocks.api.UnfallBlock;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraftforge.registries.RegistryObject;

import java.util.Set;

public class ModBlockLootTables extends BlockLootSubProvider {
    public ModBlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        for(String key : ModBlocks.BLOCKS_MAP.keySet()) {
            if(ModBlocks.UNFALL_BLOCKS.containsKey(key)) {
                UnfallBlock block = ModBlocks.UNFALL_BLOCKS.get(key).get();
                if(!block.shouldAutomaticallyGenerateLootTable()) {
                    System.out.println("Block accepted: " + key );
                    LootTable.Builder builder = block.generateLootTableProviderData(this,  ModBlocks.BLOCKS_MAP.get(key), this::applyExplosionCondition);

                    System.out.println("Block (" + key + "): " + builder);
                    if(builder != null) {
                        add(ModBlocks.BLOCKS_MAP.get(key).get(), builder);
                        continue;
                    }
                }
            }

            this.dropSelf(ModBlocks.BLOCKS_MAP.get(key).get());
        }
    }


    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}
