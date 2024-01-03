package io.radston12.reddefense.blocks;

import io.radston12.reddefense.RedDefenseMod;
import io.radston12.reddefense.blocks.api.UnfallBlock;
import io.radston12.reddefense.blocks.compressed.CompressedBlocks;
import io.radston12.reddefense.blocks.entities.BlockCompressor;
import io.radston12.reddefense.blocks.entities.owned.KeypadBlock;
import io.radston12.reddefense.item.ModItems;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.function.Supplier;

public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, RedDefenseMod.MOD_ID);
    public static final HashMap<String, RegistryObject<? extends Block>> BLOCKS_MAP = new HashMap<>();
    public static final HashMap<String, Supplier<UnfallBlock>> UNFALL_BLOCKS = new HashMap<>();

    public static final RegistryObject<Block> TEST_BLOCK = registerBlock("test_block", () -> new BlockCompressor(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.ANVIL)));
    public static final RegistryObject<Block> KEYPAD = registerBlock("keypad", KeypadBlock::new);

    public static void register(IEventBus event) {
       new CompressedBlocks();

        BLOCKS.register(event);

        for(String name : BLOCKS_MAP.keySet()) {
            ModItems.registerBlockItem(name, (RegistryObject<Block>) BLOCKS_MAP.get(name));
        }
    }

    public static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> blockSup) {
        RegistryObject<Block> block = BLOCKS.register(name, blockSup);
        BLOCKS_MAP.put(name, block);
        return (RegistryObject<T>) block;
    }

    public static <T extends Block> RegistryObject<T> registerUnfallBlock(String name, Supplier<UnfallBlock> unfallBlock) {
        RegistryObject<Block> block = BLOCKS.register(name, unfallBlock);
        BLOCKS_MAP.put(name, block);
        UNFALL_BLOCKS.put(name, unfallBlock);
        return (RegistryObject<T>) block;
    }

}
