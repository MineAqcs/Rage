package me.kall.rage;

import me.kall.rage.attribute.RageAttribute;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@Mod(Rage.MOD_ID)
public class Rage {
    public static final String MOD_ID = "rage";

    public static final DeferredRegister<Attribute> REGISTER = DeferredRegister.create(BuiltInRegistries.ATTRIBUTE, MOD_ID);
    public static final DeferredHolder<Attribute, Attribute> RAGE = REGISTER.register(MOD_ID, RageAttribute::new);

    public Rage(IEventBus modBus, Dist dist, ModContainer container) {
        container.registerConfig(ModConfig.Type.COMMON, Config.CONFIG);
        REGISTER.register(modBus);
        if (dist.isClient()) {
            container.registerConfig(ModConfig.Type.CLIENT, Config.CLIENT_CONFIG);
        }
    }

    public static class Config {
        public static final ModConfigSpec.BooleanValue NOTIFY_PLAYER_ON_RAGE_CHANGE;
        public static final ModConfigSpec.BooleanValue ONLY_PLAYERS_HAVE_RAGE;
        public static final ModConfigSpec.BooleanValue SHOW_PARTICLE_ON_FULL_RAGE;
        public static final ModConfigSpec.BooleanValue PLAY_DING_ON_FULL_RAGE_ATTACK;
        public static final ModConfigSpec.BooleanValue NOTIFY_PLAYER_ON_REACHING_FULL_RAGE;
        public static final ModConfigSpec.DoubleValue MAX_DAMAGE_BONUS;
        public static final ModConfigSpec.DoubleValue BASIC_DAMAGE_BONUS;
        public static final ModConfigSpec.DoubleValue DING_VOLUME;
        public static final ModConfigSpec.DoubleValue DING_PITCH;
        public static final ModConfigSpec.IntValue FULL_RAGE_VALUE;
        public static final ModConfigSpec.IntValue GAINED_RAGE_ON_ATTACKING;
        public static final ModConfigSpec.IntValue GAINED_RAGE_ON_BEING_ATTACKED;
        public static final ModConfigSpec.IntValue DECREASE_INTERVAL_TICKS;
        public static final ModConfigSpec.IntValue RAGE_THAT_ENTITY_LOSES_EVERY_INTERVAL;
        public static final ModConfigSpec CLIENT_CONFIG;
        public static final ModConfigSpec.IntValue WIDTH_OFFSET;
        public static final ModConfigSpec.IntValue HEIGHT_OFFSET;
        public static final ModConfigSpec CONFIG;

        static {
            ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
            builder.push("RageClient");
            WIDTH_OFFSET = builder.comment("Offset from your game's right border").defineInRange("WidthOffset", 5, 0, Integer.MAX_VALUE);
            HEIGHT_OFFSET = builder.comment("Offset from your game's bottom border").defineInRange("HeightOffset", 20, 0, Integer.MAX_VALUE);
            builder.pop();
            CLIENT_CONFIG = builder.build();
        }

        static {
            ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
            builder.push("Rage");
            BASIC_DAMAGE_BONUS = builder.comment("The entity's damage will be multiplied with this value when its rage is full").defineInRange("BasicDamageBonus", 3.0, 0.0, Double.MAX_VALUE);
            MAX_DAMAGE_BONUS = builder.comment("Extra rage value beyond the full rage will add extra damage bonus, here is the max limit (basic damage bonus included)").defineInRange("MaxDamageBonus", 5.0, 0.0, Double.MAX_VALUE);
            FULL_RAGE_VALUE = builder.comment("How much rage is considered as full").defineInRange("FullRageValue", 150, 0, Integer.MAX_VALUE);
            GAINED_RAGE_ON_ATTACKING = builder.comment("How much rage will entity get when it attacks").defineInRange("GainedRageOnAttacking", 50, 0, Integer.MAX_VALUE);
            GAINED_RAGE_ON_BEING_ATTACKED = builder.comment("How much rage will entity get when it is attacked").defineInRange("GainedRageOnBeingAttacked", 20, 0, Integer.MAX_VALUE);
            ONLY_PLAYERS_HAVE_RAGE = builder.comment("When enabled, only players will have rage, other entities' rage value will keep zero").define("OnlyPlayersHaveRage", false);
            builder.push("Notify");
            SHOW_PARTICLE_ON_FULL_RAGE = builder.comment("Show crit particle around the entity when its rage is full").define("ShowParticleOnFullRage", true);
            NOTIFY_PLAYER_ON_RAGE_CHANGE = builder.comment("Show a message including the players' currrent rage when it changes on their action bars").define("NotifyPlayerOnRageChange", false);
            NOTIFY_PLAYER_ON_REACHING_FULL_RAGE = builder.comment("Tell players that their rage is full on theiur action bars").define("NotifyPlayerOnFullRage", true);
            builder.pop();
            builder.push("Sound");
            PLAY_DING_ON_FULL_RAGE_ATTACK = builder.comment("Play Ding sound when entities with full rage attack others").define("PlayDingOnFullRageAttack", true);
            DING_VOLUME = builder.comment("The volume of the sound").defineInRange("DingVolume", 1.00, 0.00, Double.MAX_VALUE);
            DING_PITCH = builder.comment("The pitch of the sound").defineInRange("DingPitch", 1.00, 0.00, Double.MAX_VALUE);
            builder.pop();
            builder.push("Decrease");
            DECREASE_INTERVAL_TICKS = builder.comment("Between how many ticks the entities' rage will decrease").defineInRange("DecreaseIntervalTicks", 40, 0, Integer.MAX_VALUE);
            RAGE_THAT_ENTITY_LOSES_EVERY_INTERVAL = builder.comment("How much rage will the entities lose every interval").defineInRange("RageThatEntityLosesEveryInterval", 5, 0, Integer.MAX_VALUE);
            builder.pop();
            builder.pop();
            CONFIG = builder.build();
        }
    }
}
