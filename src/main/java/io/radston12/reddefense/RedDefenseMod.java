package io.radston12.reddefense;

import com.mojang.logging.LogUtils;
import io.radston12.reddefense.blockentities.ModBlockEntities;
import io.radston12.reddefense.blocks.ModBlocks;
import io.radston12.reddefense.commands.ModCommands;
import io.radston12.reddefense.entity.ModEntites;
import io.radston12.reddefense.entity.PlaneRenderer;
import io.radston12.reddefense.inventory.ModInventorys;
import io.radston12.reddefense.item.ModItems;
import io.radston12.reddefense.menus.ModMenuTypes;
import io.radston12.reddefense.networking.UnfallPacketHandler;
import io.radston12.reddefense.recipes.ModRecipes;
import io.radston12.reddefense.screens.BlockCompressorScreen;
import io.radston12.reddefense.screens.KeypadScreen;
import io.radston12.reddefense.screens.PortableJukeBoxScreen;
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

@Mod(RedDefenseMod.MOD_ID)
public class RedDefenseMod {
    public static final String MOD_ID = "reddefense";
    public static final Logger LOGGER = LogUtils.getLogger();

    public RedDefenseMod() {


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

    private void commonSetup(final FMLCommonSetupEvent event) {

    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            EntityRenderers.register(ModEntites.PLANE.get(), PlaneRenderer::new);

            MenuScreens.register(ModMenuTypes.BLOCK_COMPRESSING_MENU.get(), BlockCompressorScreen::new);
            MenuScreens.register(ModMenuTypes.PORTABE_JUKEBOX_MENU.get(), PortableJukeBoxScreen::new);
            MenuScreens.register(ModMenuTypes.KEYPAD.get(), KeypadScreen::new);
        }


    }

}
