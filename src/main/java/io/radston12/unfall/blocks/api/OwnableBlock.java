package io.radston12.unfall.blocks.api;

import com.google.gson.Gson;
import mekanism.api.JsonConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.LevelResource;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;

public abstract class OwnableBlock extends UnfallBlock {

    public OwnableBlock(Properties properties, String name) {
        super(properties, name);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity player, ItemStack stack) {
        if(level.isClientSide()) return;

        if(player == null) {
            // put under public domain
            return;
        }

        String path = level.getServer().getWorldPath(LevelResource.ROOT).toString() + "/data/test.json";

        File file = new File(path);

        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
