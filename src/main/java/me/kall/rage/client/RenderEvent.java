package me.kall.rage.client;

import me.kall.rage.Rage;
import me.kall.rage.api.RageHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

@Mod.EventBusSubscriber(modid = Rage.MOD_ID)
public class RenderEvent {
    @SubscribeEvent
    public static void renderRage(RenderGameOverlayEvent.Post event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.TEXT) return;
        Minecraft minecraft = Minecraft.getInstance();
        PlayerEntity playerEntity = minecraft.player;
        if (playerEntity == null) return;
        PlayerEntity player = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(playerEntity.getUUID());
        if (player == null) return;
        int currentRage = ((RageHolder)player).rage$getRage();

        TranslationTextComponent textComponent = new TranslationTextComponent("rage.current_rage", currentRage);
        FontRenderer fontRenderer = minecraft.font;

        int screenWidth = minecraft.getWindow().getGuiScaledWidth();
        int screenHeight = minecraft.getWindow().getGuiScaledHeight();
        int textWidth = fontRenderer.width(textComponent);
        int x = screenWidth - textWidth - Rage.WIDTH_OFFSET.get();
        int y = screenHeight - Rage.HEIGHT_OFFSET.get();

        if (!((RageHolder)player).rage$isFullRage()) {
            fontRenderer.draw(event.getMatrixStack(), textComponent, x, y, 0xFFFFFF);
        } else {
            fontRenderer.draw(event.getMatrixStack(), textComponent, x, y, 0x8B0000);
        }
    }
}
