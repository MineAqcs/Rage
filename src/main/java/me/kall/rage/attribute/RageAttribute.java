package me.kall.rage.attribute;

import net.minecraft.world.entity.ai.attributes.RangedAttribute;

public class RageAttribute extends RangedAttribute {
    public RageAttribute() {
        super("attribute.rage.id", 0.0, 0.0, Double.MAX_VALUE);
        setSyncable(true);
    }
}
