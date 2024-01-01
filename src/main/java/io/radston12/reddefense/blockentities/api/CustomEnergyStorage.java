package io.radston12.reddefense.blockentities.api;

import net.minecraftforge.energy.EnergyStorage;

public class CustomEnergyStorage extends EnergyStorage {

    public CustomEnergyStorage(int capacity) {
        super(capacity);
    }

    public void setEnergy(int energy) {
        this.energy = Math.min(energy, capacity);
    }
}
