package io.radston12.reddefense.blocks.compressed;

import io.radston12.reddefense.blocks.api.OwnableBlock;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;

public abstract class BaseCompressedBlock extends OwnableBlock {

    private final Block vanillaBlock;

    public BaseCompressedBlock(String name, Block vanilla) {
        this(name, vanilla, BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(-1.0F, 3600000.0F));
    }
    public BaseCompressedBlock(String name, Block vanilla, Properties properties) {
        super(properties, name);
        this.vanillaBlock = vanilla;
    }

    public Block getVanillaBlock() {
        return vanillaBlock;
    }

    public Block getCraftingBlock() {
        return vanillaBlock;
    }

    public abstract String getTextureFromSides(Direction direction);

    @Override
    public String getTexturePath(Direction direction) {
        return getTextureFromSides(direction);
    }
}
