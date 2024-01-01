package io.radston12.reddefense.item.api;

import net.minecraft.world.item.Item;

public abstract class UnfallItem extends Item {

    private final Properties defaultProperties;

    public UnfallItem(Properties properties) {
        super(properties);
        defaultProperties = properties;
    }

    public Item.Properties getCurrentProperties() {
        return defaultProperties;
    }

}

