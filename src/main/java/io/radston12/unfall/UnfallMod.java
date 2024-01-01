package io.radston12.unfall;

import com.mojang.logging.LogUtils;
import io.radston12.unfall.blockentities.ModBlockEntities;
import io.radston12.unfall.blocks.ModBlocks;
import io.radston12.unfall.commands.ModCommands;
import io.radston12.unfall.entity.ModEntites;
import io.radston12.unfall.entity.PlaneRenderer;
import io.radston12.unfall.inventory.ModInventorys;
import io.radston12.unfall.item.ModItems;
import io.radston12.unfall.menus.ModMenuTypes;
import io.radston12.unfall.networking.UnfallPacketHandler;
import io.radston12.unfall.recipes.ModRecipes;
import io.radston12.unfall.screens.BlockCompressingManagerScreen;
import io.radston12.unfall.screens.BlockCompressorScreen;
import io.radston12.unfall.screens.PortableJukeBoxScreen;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(UnfallMod.MOD_ID)
public class UnfallMod {
    public static final String MOD_ID = "unfall";
    public static final Logger LOGGER = LogUtils.getLogger();

    public UnfallMod() {


        MinecraftForge.EVENT_BUS.addListener(ModCommands::registerCommands);
        MinecraftForge.EVENT_BUS.register(this);

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        UnfallPacketHandler.register();

        ModInventorys.register(modEventBus);
        modEventBus.addListener(ModInventorys::addItems);

        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModEntites.register(modEventBus);

        ModBlockEntities.register(modEventBus);
        ModMenuTypes.register(modEventBus);

        ModRecipes.register(modEventBus);

        modEventBus.addListener(this::commonSetup);
    }

    private void commonSetup(final FMLCommonSetupEvent event)    {

    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)    {

    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents    {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
           EntityRenderers.register(ModEntites.PLANE.get(), PlaneRenderer::new);

            MenuScreens.register(ModMenuTypes.BLOCK_COMPRESSING_MENU.get(), BlockCompressorScreen::new);
            MenuScreens.register(ModMenuTypes.PORTABE_JUKEBOX_MENU.get(), PortableJukeBoxScreen::new);
            MenuScreens.register(ModMenuTypes.BLOCK_OWNER_MANAGER_MENU.get(), BlockCompressingManagerScreen::new);
        }


    }

}
