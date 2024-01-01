package io.radston12.reddefense.item;

import io.radston12.reddefense.RedDefenseMod;
import io.radston12.reddefense.item.custom.CompressedBlockOwnerManager;
import io.radston12.reddefense.item.custom.CompressedBlockRemover;
import io.radston12.reddefense.item.custom.MannerSchnitten;
import io.radston12.reddefense.item.custom.PortableDiscPlayer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, RedDefenseMod.MOD_ID);
    public static final HashMap<String, RegistryObject<? extends Item>> ITEMS_MAP = new HashMap<>();
    public static final List<String> BLOCK_ITEMS = new ArrayList<>();


    private static final RegistryObject<MannerSchnitten> MANNER_SCHNITTEN = registerItem("manner", () -> new MannerSchnitten());
    private static final RegistryObject<PortableDiscPlayer> PORTABLE_DISC_PLAYER = registerItem("portable_disc_player", () -> new PortableDiscPlayer());
    private static final RegistryObject<CompressedBlockRemover> COMPRESSSED_BLOCK_REMOVER = registerItem("compressed_block_remover", () -> new CompressedBlockRemover());
    private static final RegistryObject<CompressedBlockOwnerManager> COMPRESSSED_BLOCK_OWNER_MANAGER = registerItem("compressed_block_owner_manager", () -> new CompressedBlockOwnerManager());


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

    public static <T extends Item> RegistryObject<T> registerItem(String name, Supplier<T> item) {
        RegistryObject<T> registryObject = ITEMS.register(name, item);
        ITEMS_MAP.put(name, registryObject);
        return registryObject;
    }

    public static <T extends Item> RegistryObject<T> registerItem(String name, Supplier<T> item, boolean addToBlockList) {
        RegistryObject<T> registryObject = ITEMS.register(name, item);
        ITEMS_MAP.put(name, registryObject);
        if(addToBlockList) BLOCK_ITEMS.add(name);
        return registryObject;
    }

    public static RegistryObject<Item> registerBlockItem(String name, RegistryObject<Block> block) {
        return registerItem(name, () -> new BlockItem(block.get(), new Item.Properties()), true);
    }

    public static String getBlockItemIdentifier(int id) {
        return BLOCK_ITEMS.get(id);
    }

    public static RegistryObject<? extends Item> getItemAsRegistryObject(String name) {
        return ITEMS_MAP.get(name);
    }

    public static Item getItem(String name) {
        return ITEMS_MAP.get(name).get();
    }

    public static ItemStack getItemAsItemStack(String name) {
        return new ItemStack(ITEMS_MAP.get(name).get());
    }
}
