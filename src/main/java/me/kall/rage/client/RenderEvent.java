package me.kall.rage.client;

import me.kall.rage.Rage;
import me.kall.rage.api.RageHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.ServerLifecycleHooks;

@Mod.EventBusSubscriber(modid = Rage.MOD_ID)
public class RenderEvent {
    @SubscribeEvent
    public static void renderRage(RenderGuiOverlayEvent.Post event) {
        if (event.getOverlay() != VanillaGuiOverlay.DEBUG_TEXT.type()) return;
        Minecraft minecraft = Minecraft.getInstance();
        Player playerEntity = minecraft.player;
        if (playerEntity == null) return;
        Player player = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(playerEntity.getUUID());
        if (player == null) return;
        int currentRage = ((RageHolder)player).rage$getRage();

        Component component = Component.translatable("rage.current_rage", currentRage);
        Font fontRenderer = minecraft.font;

        int screenWidth = minecraft.getWindow().getGuiScaledWidth();
        int screenHeight = minecraft.getWindow().getGuiScaledHeight();
        int textWidth = fontRenderer.width(component);
        int x = screenWidth - textWidth - Rage.WIDTH_OFFSET.get();
        int y = screenHeight - Rage.HEIGHT_OFFSET.get();

        if (!((RageHolder)player).rage$isFullRage()) {
            fontRenderer.draw(event.getPoseStack(), component, x, y, 0xFFFFFF);
        } else {
            fontRenderer.draw(event.getPoseStack(), component, x, y, 0x8B0000);
        }
    }
}
