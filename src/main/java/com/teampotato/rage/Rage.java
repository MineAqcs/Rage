package com.teampotato.rage;

import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.NeoForge;

@Mod(Rage.MOD_ID)
public final class Rage {
    public static final String MOD_ID = "rage";

    public static final ModConfigSpec CONFIG;

    public static ModConfigSpec.DoubleValue maxDamageBonus, basicDamageBonus, dingVolume, dingPitch;
    public static ModConfigSpec.IntValue fullRageValue, gainedRageOnAttackingOrBeingHurt, decreaseIntervalTicks, entityRageLossPerInterval;
    public static ModConfigSpec.BooleanValue notifyPlayerOnRageChange, onlyPlayersHaveRage, showParticleOnFullRage, playDingOnFullRageAttack, notifyPlayerOnReachingFullRage;


    public Rage(ModContainer container) {
        container.registerConfig(ModConfig.Type.COMMON, CONFIG);
        NeoForge.EVENT_BUS.register(Events.class);
    }

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
        builder.push("Rage");
        builder.push("Notification");
        notifyPlayerOnReachingFullRage = builder.comment("Tell the player that his/her rage is full on the action bar when it comes").define("NotifyPlayerOnFullRage", true);
        showParticleOnFullRage = builder.comment("Show crit particle around the entity whose rage is full").define("ShowParticleOnFullRage", true);
        notifyPlayerOnRageChange = builder.comment("Show a message including the player's currrent rage on the action bar when it changes").define("NotifyPlayerOnRageChange", false);
        builder.pop();
        builder.push("Sound");
        playDingOnFullRageAttack = builder.comment("Play Ding sound when entities with full rage attack others").define("PlayDingOnFullRageAttack", true);
        dingVolume = builder.comment("The volume of the Ding").defineInRange("DingVolume", 1.00, 0.00, Double.MAX_VALUE);
        dingPitch = builder.comment("The pitch of the Ding").defineInRange("DingPitch", 1.00, 0.00, Double.MAX_VALUE);
        builder.pop();
        builder.push("Decrease");
        decreaseIntervalTicks = builder.comment("Between how many ticks the entity's rage will decrease").defineInRange("DecreaseIntervalTicks", 40, 0, Integer.MAX_VALUE);
        entityRageLossPerInterval = builder.comment("How much rage will the entities lose per interval").defineInRange("EntityRageLossPerInterval", 5, 0, Integer.MAX_VALUE);
        builder.pop();
        builder.push("DamageBonus");
        basicDamageBonus = builder.comment("The entity's damage will be multiplied with this value when its rage is full").defineInRange("BasicDamageBonus", 3.0, 0.0, Double.MAX_VALUE);
        maxDamageBonus = builder.comment("Extra rage value beyond the full rage will add extra damage bonus, here is the max limit (basic damage bonus included)").defineInRange("MaxDamageBonus", 5.0, 0.0, Double.MAX_VALUE);
        builder.pop();
        builder.push("Misc");
        gainedRageOnAttackingOrBeingHurt = builder.comment("How much rage will entity get when it attacks/is attacked").defineInRange("GainedRageOnAttackingOrBeingHurt", 20, 0, Integer.MAX_VALUE);
        fullRageValue = builder.comment("How much rage is considered as full").defineInRange("FullRageValue", 150, 0, Integer.MAX_VALUE);
        onlyPlayersHaveRage = builder.comment("When enabled, only players will have rage, other entities' rage values will be always zero").define("OnlyPlayersHaveRage", false);
        builder.pop();
        builder.pop();
        CONFIG = builder.build();
    }
}
