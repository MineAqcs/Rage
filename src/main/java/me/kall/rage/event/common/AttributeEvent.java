package me.kall.rage.event.common;

import me.kall.rage.Rage;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class AttributeEvent {
    @SubscribeEvent
    public static void registerAttribute(@NotNull EntityAttributeModificationEvent event) {
        event.getTypes().forEach(entityType -> event.add(entityType, Rage.RAGE.get()));
    }
}
