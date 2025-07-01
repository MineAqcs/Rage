package me.kall.rage.event.client;

import me.kall.rage.Rage;
import me.kall.rage.api.RageHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Rage.MOD_ID, value = Dist.CLIENT)
public class RenderEvent {
    @SubscribeEvent
    public static void renderRage(RenderGameOverlayEvent.Post event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.TEXT) return;
        Minecraft minecraft = Minecraft.getInstance();
        Player playerEntity = minecraft.player;
        if (playerEntity == null) return;
        int currentRage = ((RageHolder)playerEntity).rage$getRage();

        TranslatableComponent textComponent = new TranslatableComponent("rage.current_rage", currentRage);
        Font fontRenderer = minecraft.font;

        int screenWidth = minecraft.getWindow().getGuiScaledWidth();
        int screenHeight = minecraft.getWindow().getGuiScaledHeight();
        int textWidth = fontRenderer.width(textComponent);
        int x = screenWidth - textWidth - Rage.Config.WIDTH_OFFSET.get();
        int y = screenHeight - Rage.Config.HEIGHT_OFFSET.get();

        if (!((RageHolder)playerEntity).rage$isFullRage()) {
            fontRenderer.draw(event.getMatrixStack(), textComponent, x, y, 0xFFFFFF);
        } else {
            fontRenderer.draw(event.getMatrixStack(), textComponent, x, y, 0x8B0000);
        }
    }
}
