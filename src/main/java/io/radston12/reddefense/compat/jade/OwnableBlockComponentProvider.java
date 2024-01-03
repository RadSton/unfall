package io.radston12.reddefense.compat.jade;

import io.radston12.reddefense.RedDefenseMod;
import io.radston12.reddefense.blockentities.custom.OwnableBlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public class OwnableBlockComponentProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor data, IPluginConfig config) {
        tooltip.add(Component.translatable("compat.reddefense.waila.owner", data.getServerData().getString("owner")));
    }

    @Override
    public void appendServerData(CompoundTag data, BlockAccessor accessor) {
        BlockEntity entity = accessor.getBlockEntity();
        data.putString("owner", ((OwnableBlockEntity) entity).getOwnerName());
    }

    @Override
    public ResourceLocation getUid() {
        return new ResourceLocation(RedDefenseMod.MOD_ID, "ownable_block");
    }
}
