package io.radston12.unfall.blocks.compressed;

import io.radston12.unfall.UnfallMod;
import io.radston12.unfall.datagen.ModBlockStateProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.registries.RegistryObject;

public class CompressedCarpetBlock extends BaseCompressedBlock {
    protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D);;

    private final Block material;

    public CompressedCarpetBlock(String name, Block vanilla, Block material) {
        super(name, vanilla, BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(-1.0F, 3600000.0F).forceSolidOn().noOcclusion());

        this.material = material;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext collisionContext) {
        return SHAPE;
    }


    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState state_, LevelAccessor levelA, BlockPos pos, BlockPos pos_) {
        return !state.canSurvive(levelA, pos) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, direction, state, levelA, pos, pos_);
    }


    @Override
    public boolean canSurvive(BlockState state, LevelReader reader, BlockPos pos) {
        return !reader.isEmptyBlock(pos.below());
    }

    public Block getMaterial() {
        return material;
    }

    @Override
    public void generateBlockStateProviderData(ModBlockStateProvider provider, RegistryObject<? extends Block> block) {
        provider.simpleBlock(block.get(), provider.models().withExistingParent(getRegistryName(), new ResourceLocation(UnfallMod.MOD_ID, "block/compressed_carpet")).texture("wool", provider.blockTexture(getMaterial()))); //cubeAll(ForgeRegistries.BLOCKS.getKey(block.get()).getPath(), provider.blockTexture(vanillaBlock)));

    }

    @Override
    public String getTextureFromSides(Direction direction) {
        return null;
    }
}
