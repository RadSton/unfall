package io.radston12.unfall.blockentities;

import io.radston12.unfall.UnfallMod;
import io.radston12.unfall.blockentities.custom.BlockCompressorBlockEntity;
import io.radston12.unfall.blocks.ModBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, UnfallMod.MOD_ID);

    public static final RegistryObject<BlockEntityType<BlockCompressorBlockEntity>> BLOCK_COMPRESSING = BLOCK_ENTITIES.register("block_compressor", ()-> BlockEntityType.Builder.of(BlockCompressorBlockEntity::new, ModBlocks.TEST_BLOCK.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
