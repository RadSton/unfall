package io.radston12.reddefense.sound.api;

import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;

public class MovingSound extends AbstractTickableSoundInstance {

    private final Entity entity;


    public MovingSound(SoundEvent event, Entity entity) {
        this(event, SoundSource.RECORDS, entity.level().getRandom(), entity);
    }
    public MovingSound(SoundEvent event, Entity entity, int delay) {
        this(event, SoundSource.RECORDS, entity.level().getRandom(), entity, delay);
    }

    public MovingSound(SoundEvent soundEvent, SoundSource soundSource, RandomSource randomSource, Entity entity) {
        this(soundEvent,soundSource,randomSource, entity, 0);
    }

    public MovingSound(SoundEvent soundEvent, SoundSource soundSource, RandomSource randomSource, Entity entity, int delay) {
        super(soundEvent, soundSource, randomSource);
        this.entity = entity;

        // Setttings
        this.looping = false;
        this.delay = delay;
        this.volume = 1.0f;
    }

    @Override
    public void tick() {
        if(!this.entity.isAlive()) {
            this.stop();
            return;
        }

        this.x = this.entity.getX();
        this.y = this.entity.getY();
        this.z = this.entity.getZ();
    }
}
