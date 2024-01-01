package io.radston12.unfall.blocks.api;

import io.radston12.unfall.datagen.ModBlockStateProvider;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.RegistryObject;

import javax.print.attribute.standard.Sides;

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

    public boolean shouldAutomaticallyGenerateProviderData () {
        return true;
    }

    public String getTexturePath(Direction direction) {
        return "block/" + name;
    }
}
