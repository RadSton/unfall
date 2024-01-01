package io.radston12.reddefense.blockentities.api;

import net.minecraftforge.energy.IEnergyStorage;

public class EnergyAcceptor implements IEnergyStorage {

    private int maxEnergy;
    private int energyStored = 0;

    public EnergyAcceptor(int maxEnergy) {
        this.maxEnergy = maxEnergy;
    }
    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        int toStore = Math.min(1000, maxReceive);
        toStore = Math.min(toStore, getMaxEnergyStored() - getEnergyStored());
        if (!simulate)
            energyStored += Math.min(toStore, getMaxEnergyStored() - toStore);
        return toStore;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        if(simulate) return Math.max((energyStored - maxExtract), 0);

        energyStored = Math.max((energyStored - maxExtract), 0);

        return energyStored;
    }

    @Override
    public int getEnergyStored() {
        return energyStored;
    }

    @Override
    public int getMaxEnergyStored() {
        return maxEnergy;
    }

    @Override
    public boolean canExtract() {
        return false;
    }

    @Override
    public boolean canReceive() {
        return energyStored != maxEnergy;
    }

    public void setEnergyStored(int energyStored) {
        this.energyStored = energyStored;
    }

    public void setMaxEnergyStored(int maxEnergyStored) {
        this.maxEnergy = maxEnergyStored;
    }

    public int getMaxEnergy() {
        return maxEnergy;
    }
}
