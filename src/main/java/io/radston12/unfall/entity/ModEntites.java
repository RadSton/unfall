package io.radston12.unfall.entity;

import io.radston12.unfall.UnfallMod;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntites {
    public static DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, UnfallMod.MOD_ID);

    public static final RegistryObject<EntityType<PlaneEntity>> PLANE = ENTITY_TYPES.register("plane", () -> EntityType.Builder.of(PlaneEntity::new, MobCategory.CREATURE).sized(3f,1.1f).build("plane"));

    public static void register(IEventBus bus) {
        ENTITY_TYPES.register(bus);
    }

}
