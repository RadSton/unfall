package io.radston12.reddefense.compat.jade;

import io.radston12.reddefense.RedDefenseMod;
import io.radston12.reddefense.blockentities.custom.OwnableBlockEntity;
import io.radston12.reddefense.blocks.api.OwnableBlock;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin(RedDefenseMod.MOD_ID)
public class JadePlugin implements IWailaPlugin {

    private static OwnableBlockComponentProvider ownableBlockComponentProvider = new OwnableBlockComponentProvider();

    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerBlockDataProvider(ownableBlockComponentProvider, OwnableBlockEntity.class);
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(ownableBlockComponentProvider, OwnableBlock.class);
    }
}
