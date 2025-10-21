package me.kall.rage.event.client;

import me.kall.rage.Rage;
import me.kall.rage.api.RageHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber(modid = Rage.MOD_ID, value = Dist.CLIENT)
public class RenderEvent {
    @SubscribeEvent
    public static void renderRage(RenderGuiLayerEvent.@NotNull Post event) {
        if (event.getName() != VanillaGuiLayers.HOTBAR) return;
        Minecraft minecraft = Minecraft.getInstance();
        Player playerEntity = minecraft.player;
        if (playerEntity == null) return;
        int currentRage = ((RageHolder)playerEntity).rage$getRage();

        Component textComponent = Component.translatable("rage.current_rage", currentRage);
        Font fontRenderer = minecraft.font;

        int screenWidth = minecraft.getWindow().getGuiScaledWidth();
        int screenHeight = minecraft.getWindow().getGuiScaledHeight();
        int textWidth = fontRenderer.width(textComponent);
        int x = screenWidth - textWidth - Rage.Config.WIDTH_OFFSET.get();
        int y = screenHeight - Rage.Config.HEIGHT_OFFSET.get();

        if (!((RageHolder)playerEntity).rage$isFullRage()) {
            event.getGuiGraphics().drawString(fontRenderer, textComponent, x, y, 0xFFFFFF);
        } else {
            event.getGuiGraphics().drawString(fontRenderer, textComponent, x, y, 0x8B0000);
        }
    }
}
