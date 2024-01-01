package io.radston12.unfall.entity;

import io.radston12.unfall.event.CompressedBlockRegisterColorHandlerEvent;
import io.radston12.unfall.item.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class PlaneEntity extends Entity implements PlayerRideable {
    private float deltaRotation = 0;
    public PlaneEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void defineSynchedData() {

    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
    }


    @Override
    public InteractionResult interact(Player p, InteractionHand hand) {
        if (!this.level().isClientSide) {
            return setRiding(p) ? InteractionResult.CONSUME : InteractionResult.PASS;
        } else {
            return InteractionResult.SUCCESS;
        }
    }


    private boolean setRiding(Player p) {
        p.setYRot(this.getYRot());
        p.setXRot(this.getXRot());
        return p.startRiding(this, true);
    }

    @Override
    public boolean isPickable() {
        return true;  // Needs to be override or the interact method wont get called!
    }

    @Override
    public boolean canBeCollidedWith() {
        return true; // Needs to be override or the interact method wont get called!
    }

    @Nullable
    @Override
    public LivingEntity getControllingPassenger() {
        Entity entity = this.getFirstPassenger();
        LivingEntity lEntity = null;
        if (entity instanceof LivingEntity livingentity)
            lEntity = livingentity;

        return lEntity;
    }

    @Override
    public void tick() {
        super.tick();
    }

    private void controlBoat() {
        if (this.isVehicle()) {
            float f = 0.0F;
            if (Minecraft.getInstance().options.keyUp.isDown()) {
                --deltaRotation;
            }

            if (Minecraft.getInstance().options.keyDown.isDown()) {
                ++deltaRotation;
            }

            this.setYRot(this.getYRot() + this.deltaRotation);

            this.setDeltaMovement(this.getDeltaMovement().add((double)(Mth.sin(-this.getYRot() * ((float)Math.PI / 180F)) * f), 0.0D, (double)(Mth.cos(this.getYRot() * ((float)Math.PI / 180F)) * f)));

        }
    }

}
