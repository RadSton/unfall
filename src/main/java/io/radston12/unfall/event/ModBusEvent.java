package io.radston12.unfall.event;

import io.radston12.unfall.UnfallMod;
import io.radston12.unfall.blocks.ModBlocks;
import io.radston12.unfall.entity.ModModelLayer;
import io.radston12.unfall.entity.PlaneModel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.security.InvalidParameterException;

@Mod.EventBusSubscriber(modid = UnfallMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModBusEvent {

    @SubscribeEvent
    public static void registerLayers(final EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModModelLayer.PLANE_LAYER, PlaneModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerAttributes(final EntityAttributeCreationEvent event) {

    }

}
