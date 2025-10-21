package me.kall.rage;

import me.kall.rage.attribute.RageAttribute;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod(Rage.MOD_ID)
public class Rage {
    public static final String MOD_ID = "rage";

    public static final DeferredRegister<Attribute> REGISTER = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, MOD_ID);
    public static final RegistryObject<Attribute> RAGE = REGISTER.register(MOD_ID, RageAttribute::new);

    public Rage() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.CONFIG);
        REGISTER.register(FMLJavaModLoadingContext.get().getModEventBus());
        if (FMLLoader.getDist().isClient()) {
            ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.CLIENT_CONFIG);
        }
    }

    public static class Config {
        public static final ForgeConfigSpec.BooleanValue NOTIFY_PLAYER_ON_RAGE_CHANGE;
        public static final ForgeConfigSpec.BooleanValue ONLY_PLAYERS_HAVE_RAGE;
        public static final ForgeConfigSpec.BooleanValue SHOW_PARTICLE_ON_FULL_RAGE;
        public static final ForgeConfigSpec.BooleanValue PLAY_DING_ON_FULL_RAGE_ATTACK;
        public static final ForgeConfigSpec.BooleanValue NOTIFY_PLAYER_ON_REACHING_FULL_RAGE;
        public static final ForgeConfigSpec.DoubleValue MAX_DAMAGE_BONUS;
        public static final ForgeConfigSpec.DoubleValue BASIC_DAMAGE_BONUS;
        public static final ForgeConfigSpec.DoubleValue DING_VOLUME;
        public static final ForgeConfigSpec.DoubleValue DING_PITCH;
        public static final ForgeConfigSpec.IntValue FULL_RAGE_VALUE;
        public static final ForgeConfigSpec.IntValue GAINED_RAGE_ON_ATTACKING;
        public static final ForgeConfigSpec.IntValue GAINED_RAGE_ON_BEING_ATTACKED;
        public static final ForgeConfigSpec.IntValue DECREASE_INTERVAL_TICKS;
        public static final ForgeConfigSpec.IntValue RAGE_THAT_ENTITY_LOSES_EVERY_INTERVAL;
        public static final ForgeConfigSpec CLIENT_CONFIG;
        public static final ForgeConfigSpec.IntValue WIDTH_OFFSET;
        public static final ForgeConfigSpec.IntValue HEIGHT_OFFSET;
        public static final ForgeConfigSpec CONFIG;

        static {
            ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
            builder.push("RageClient");
            WIDTH_OFFSET = builder.comment("Offset from your game's right border").defineInRange("WidthOffset", 5, 0, Integer.MAX_VALUE);
            HEIGHT_OFFSET = builder.comment("Offset from your game's bottom border").defineInRange("HeightOffset", 20, 0, Integer.MAX_VALUE);
            builder.pop();
            CLIENT_CONFIG = builder.build();
        }

        static {
            ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
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
