package me.kall.rage.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import me.kall.rage.Rage;
import me.kall.rage.api.RageHolder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements RageHolder {
    public LivingEntityMixin(EntityType<?> pType, World pLevel) {
        super(pType, pLevel);
    }

    @Unique private volatile int rage$currentRage;
    @Unique private int rage$decreaseInterval;
    @Unique private static final ResourceLocation PLAYER_ID = new ResourceLocation("minecraft:player");

    @ModifyVariable(method = "hurt", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private float rage$onHurt(float pAmount, @Local(argsOnly = true) DamageSource pSource) {
        this.rage$setRage(this.rage$getRage() + Rage.GAINED_RAGE_ON_BEING_ATTACKED.get());
        Entity entity = pSource.getEntity();
        Entity directEntity = pSource.getDirectEntity();
        if (entity instanceof LivingEntity) {
            if (((RageHolder)entity).rage$isFullRage()) {
                double damageBonus = ((RageHolder)entity).rage$getDamageBonus();
                pAmount = (float) (damageBonus * pAmount);
                ((RageHolder)entity).rage$setRage(0);
                if (Rage.PLAY_DING_ON_FULL_RAGE_ATTACK.get()) {
                    this.level.playSound(null, this.blockPosition(), SoundEvents.ARROW_HIT_PLAYER, entity.getSoundSource(), Rage.DING_VOLUME.get().floatValue(), Rage.DING_PITCH.get().floatValue());
                }
            } else {
                ((RageHolder)entity).rage$setRage(((RageHolder)entity).rage$getRage() + Rage.GAINED_RAGE_ON_ATTACKING.get());
            }
        }

        if (entity != null && directEntity != null && entity.getStringUUID().equals(directEntity.getStringUUID())) return pAmount;

        if (directEntity instanceof LivingEntity) {
            if (((RageHolder)directEntity).rage$isFullRage()) {
                double damageBonus = ((RageHolder)directEntity).rage$getDamageBonus() + 1;
                pAmount = (float) (damageBonus * pAmount);
                ((RageHolder)directEntity).rage$setRage(0);
                if (Rage.PLAY_DING_ON_FULL_RAGE_ATTACK.get()) {
                    this.level.playSound(null, this.blockPosition(), SoundEvents.ARROW_HIT_PLAYER, directEntity.getSoundSource(), Rage.DING_VOLUME.get().floatValue(), Rage.DING_PITCH.get().floatValue());
                }
            } else {
                ((RageHolder)directEntity).rage$setRage(((RageHolder)directEntity).rage$getRage() + Rage.GAINED_RAGE_ON_ATTACKING.get());
            }
        }

        return pAmount;
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void rage$onTick(CallbackInfo ci) {
        if (this.level instanceof ServerWorld) {
            this.rage$decreaseInterval += 1;
            if (this.rage$decreaseInterval == Rage.DECREASE_INTERVAL_TICKS.get()) {
                this.rage$decreaseInterval = 0;
                int lost = Rage.RAGE_THAT_ENTITY_LOSES_EVERY_INTERVAL.get();
                this.rage$currentRage = lost >= this.rage$getRage() ? 0 : this.rage$getRage() - lost;
            }

            if (Rage.SHOW_PARTICLE_ON_FULL_RAGE.get() && this.rage$isFullRage()) {
                ServerWorld serverWorld = (ServerWorld) this.level;
                serverWorld.sendParticles(ParticleTypes.CRIT, this.getX(), this.getY(), this.getZ(), 8, 0.2, 0.2, 0.2, 0.0);
            }
        }
    }

    @Override
    public int rage$getRage() {
        if (Rage.ONLY_PLAYERS_HAVCE_RAGE.get() && !PLAYER_ID.equals(this.getType().getRegistryName())) return 0;
        return this.rage$currentRage;
    }

    @Override
    public void rage$setRage(int newRage) {
        this.rage$currentRage = newRage;
    }
}
