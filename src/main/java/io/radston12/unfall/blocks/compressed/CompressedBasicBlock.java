package io.radston12.unfall.blocks.compressed;

import io.radston12.unfall.UnfallMod;
import io.radston12.unfall.datagen.ModBlockStateProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.RegistryObject;

public class CompressedBasicBlock extends BaseCompressedBlock {

    private String all = null;

    public CompressedBasicBlock(String name, Block vanilla, String all) {
        this(name, vanilla);
        this.all = all;
    }


    @Override
    public boolean isPortalFrame(BlockState state, BlockGetter level, BlockPos pos) {
        return getVanillaBlock() == Blocks.OBSIDIAN;
    }

    public CompressedBasicBlock(String name, Block vanilla) {
        super(name, vanilla);
    }

    @Override
    public String getTextureFromSides(Direction direction) {
        return getVanillaBlock().getDescriptionId();
    }

    @Override
    public void generateBlockStateProviderData(ModBlockStateProvider provider, RegistryObject<? extends Block> block) {

        if (all == null) all = provider.blockTexture(getVanillaBlock()).toString();

        provider.simpleBlock(block.get(), provider.models().withExistingParent(getRegistryName(), new ResourceLocation(UnfallMod.MOD_ID, "block/compressed_cube")).texture("all", all)); //cubeAll(ForgeRegistries.BLOCKS.getKey(block.get()).getPath(), provider.blockTexture(vanillaBlock)));

    }

}
