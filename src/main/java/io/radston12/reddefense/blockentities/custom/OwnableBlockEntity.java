package io.radston12.reddefense.blockentities.custom;

import io.radston12.reddefense.blockentities.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Objects;

public class OwnableBlockEntity extends BlockEntity {

    private String ownerUUIDPart = "", name = "owner";

    public OwnableBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.OWNABLE_BLOCK.get(), pos, state);
    }
    public OwnableBlockEntity(BlockEntityType<?> entity, BlockPos pos, BlockState state) {
        super(entity, pos, state);
    }


    public void saveData(CompoundTag tag) {
        tag.putString("owId", ownerUUIDPart);
        tag.putString("ow", name);
    }

    public void loadData(CompoundTag tag) {
        ownerUUIDPart = tag.getString("owId");
        name = tag.getString("ow");
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        saveData(tag);
    }

    @Override
    public void load(CompoundTag tag) {
        loadData(tag);
    }

    public void setOwner(Player owner) {
        this.ownerUUIDPart = owner.getGameProfile().getId().toString().split("-")[0];
        this.name = owner.getName().getString();
    }

    public void setOwner(String name, String ownerUUIDPart) {
        this.name = name;
        this.ownerUUIDPart = ownerUUIDPart;
    }

    public boolean isOwner(Player owner) {
        return Objects.equals(this.ownerUUIDPart, owner.getGameProfile().getId().toString().split("-")[0]);
    }

    public String getOwnerName() {
        return this.name;
    }

    public String getOwnerUUIDPart() {
        return ownerUUIDPart;
    }
}
