package io.radston12.reddefense.blocks.api;

import io.radston12.reddefense.datagen.ModBlockLootTables;
import io.radston12.reddefense.datagen.ModBlockStateProvider;
import io.radston12.reddefense.datagen.interfaces.LootTableProviderData;
import net.minecraft.core.Direction;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraftforge.registries.RegistryObject;

public abstract class UnfallBlock extends Block {

    private final BlockBehaviour.Properties blockProperties;
    private final String name;

    public UnfallBlock(BlockBehaviour.Properties properties, String name) {
        super(properties);
        this.blockProperties = properties;
        this.name = name;
    }

    public BlockBehaviour.Properties getBlockProperties() {
        return blockProperties;
    }

    public String getRegistryName() {
        return name;
    }

    public abstract void generateBlockStateProviderData(ModBlockStateProvider provider, RegistryObject<? extends Block> block);

    public boolean shouldAutomaticallyGenerateProviderData() {
        return true;
    }

    public boolean shouldAutomaticallyGenerateItem() {
        return true;
    }


    public boolean shouldAutomaticallyGenerateLootTable() {
        return true;
    }

    public LootTable.Builder generateLootTableProviderData(BlockLootSubProvider tables, RegistryObject<? extends Block> block, LootTableProviderData applyExplosionCondition) {
        return null;
    }

    public String getTexturePath(Direction direction) {
        return "block/" + name;
    }
}
