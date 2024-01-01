package io.radston12.reddefense.blocks.compressed;

import io.radston12.reddefense.RedDefenseMod;
import io.radston12.reddefense.blocks.api.BlockPropertiesUtils;
import io.radston12.reddefense.datagen.ModBlockStateProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.registries.RegistryObject;

public class CompressedGlassBlock extends BaseCompressedBlock {

    private String all;
    private boolean tinted = false;

    public CompressedGlassBlock(String name, Block vanilla, boolean isTinted) {
        super(name, vanilla, BlockBehaviour.Properties.of().mapColor(MapColor.SNOW).strength(-1.0F, Float.MAX_VALUE).sound(SoundType.GLASS).noOcclusion().isValidSpawn(BlockPropertiesUtils::never).isRedstoneConductor(BlockPropertiesUtils::never).isSuffocating(BlockPropertiesUtils::never).isViewBlocking(BlockPropertiesUtils::never));
        this.tinted = isTinted;
    }
    public CompressedGlassBlock(String name, Block vanilla) {
        super(name, vanilla, BlockBehaviour.Properties.of().mapColor(MapColor.SNOW).strength(-1.0F, Float.MAX_VALUE).sound(SoundType.GLASS).noOcclusion().isValidSpawn(BlockPropertiesUtils::never).isRedstoneConductor(BlockPropertiesUtils::never).isSuffocating(BlockPropertiesUtils::never).isViewBlocking(BlockPropertiesUtils::never));
    }

    @Override
    public VoxelShape getVisualShape(BlockState pState, BlockGetter pReader, BlockPos pPos, CollisionContext pContext) {
        return Shapes.empty();
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter getter, BlockPos pos) {
        return !tinted;
    }

    @Override
    public float getShadeBrightness(BlockState state, BlockGetter getter, BlockPos pos) {
        return 1f;
    }

    @Override
    public int getLightBlock(BlockState state, BlockGetter getter, BlockPos pos) {
        if(tinted) return getter.getMaxLightLevel();
        return super.getLightBlock(state, getter, pos);
    }

    @Override
    public boolean shouldDisplayFluidOverlay(BlockState state, BlockAndTintGetter level, BlockPos pos, FluidState fluidState) {
        return true;
    }

    @Override
    public boolean skipRendering(BlockState state, BlockState nearby, Direction side) {
        return nearby.getBlock() == this || super.skipRendering(state, nearby, side);
    }

    @Override
    public void generateBlockStateProviderData(ModBlockStateProvider provider, RegistryObject<? extends Block> block) {
        if (all == null) all = provider.blockTexture(getVanillaBlock()).toString();

        provider.simpleBlock(block.get(), provider.models().withExistingParent(getRegistryName(), new ResourceLocation(RedDefenseMod.MOD_ID, "block/compressed_cube")).texture("all", all).renderType("translucent"));
    }

    @Override
    public String getTextureFromSides(Direction direction) {
        return null;
    }
}
