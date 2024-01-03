package io.radston12.reddefense.blockentities;

import io.radston12.reddefense.RedDefenseMod;
import io.radston12.reddefense.blockentities.custom.BlockCompressorBlockEntity;
import io.radston12.reddefense.blockentities.custom.KeypadBlockEntity;
import io.radston12.reddefense.blockentities.custom.OwnableBlockEntity;
import io.radston12.reddefense.blocks.ModBlocks;
import io.radston12.reddefense.blocks.api.OwnableBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, RedDefenseMod.MOD_ID);

    public static final RegistryObject<BlockEntityType<BlockCompressorBlockEntity>> BLOCK_COMPRESSING = BLOCK_ENTITIES.register("block_compressor", ()-> BlockEntityType.Builder.of(BlockCompressorBlockEntity::new, ModBlocks.TEST_BLOCK.get()).build(null));
    public static final RegistryObject<BlockEntityType<KeypadBlockEntity>> KEYPAD = BLOCK_ENTITIES.register("keypad", ()-> BlockEntityType.Builder.of(KeypadBlockEntity::new, ModBlocks.KEYPAD.get()).build(null));

    public static final RegistryObject<BlockEntityType<OwnableBlockEntity>> OWNABLE_BLOCK = getOwnableBlock();


    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }

    public static RegistryObject<BlockEntityType<OwnableBlockEntity>> getOwnableBlock() {
        return BLOCK_ENTITIES.register("ownable_block", () -> {
            List<Block> blocks = new ArrayList<>();

            for(String key : ModBlocks.BLOCKS_MAP.keySet()) {
                if(!key.startsWith("compressed")) continue;
                Block block = ModBlocks.BLOCKS_MAP.get(key).get();
                if(block instanceof OwnableBlock) blocks.add(block);
            }

            return BlockEntityType.Builder.of(OwnableBlockEntity::new, blocks.toArray(new Block[blocks.size()])).build(null);
        });
    }


}
