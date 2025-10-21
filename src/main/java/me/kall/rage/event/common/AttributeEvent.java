package me.kall.rage.event.common;

import me.kall.rage.Rage;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber
public class AttributeEvent {
    @SubscribeEvent
    public static void registerAttribute(@NotNull EntityAttributeModificationEvent event) {
        event.getTypes().forEach(entityType -> event.add(entityType, Rage.RAGE.getDelegate()));
    }
}
