package io.radston12.reddefense.event;

import io.radston12.reddefense.RedDefenseMod;
import io.radston12.reddefense.entity.ModModelLayer;
import io.radston12.reddefense.entity.PlaneModel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = RedDefenseMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModBusEvent {

    @SubscribeEvent
    public static void registerLayers(final EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModModelLayer.PLANE_LAYER, PlaneModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerAttributes(final EntityAttributeCreationEvent event) {

    }

}
