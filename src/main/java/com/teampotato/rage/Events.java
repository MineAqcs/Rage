package com.teampotato.rage;

import com.teampotato.rage.api.RageHolder;
import com.teampotato.rage.api.event.FullRageAttackEvent;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.EntityEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import org.jetbrains.annotations.NotNull;

public class Events {
    public static void showParticleOnFullRagePlayer(PlayerTickEvent.Pre event) {
        showParticle(event);
    }

    public static void showParticleOnFullRageLiving(EntityTickEvent.Pre event) {
        showParticle(event);
    }

    private static void showParticle(EntityEvent event) {
        if (!Rage.showParticleOnFullRage.get()) return;
        Entity entity = event.getEntity();
        if (entity instanceof LivingEntity living && ((RageHolder) living).rage$isFullRage() && living.level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.CRIT, living.getX(), living.getY(), living.getZ(), 8, 0.2, 0.2, 0.2, 0.0);
        }
    }

    public static void bumpRageOnBeingHurt(LivingDamageEvent.Post event) {
        LivingEntity entity = event.getEntity();
        if (entity.level().isClientSide()) return;
        if (shouldBumpRage(event.getSource())) ((RageHolder)entity).rage$bumpRage();
    }

    private static boolean shouldBumpRage(@NotNull DamageSource damageSource) {
        Entity entity = damageSource.getEntity();
        Entity directEntity = damageSource.getDirectEntity();
        return entity instanceof LivingEntity || directEntity instanceof LivingEntity;
    }

    public static void bumpOrConsumeRageOnAttacking(LivingDamageEvent.Pre event) {
        LivingEntity attacked = event.getEntity();
        Level level = attacked.level();

        if (level.isClientSide()) return;

        DamageSource damageSource = event.getSource();
        Entity entity = damageSource.getEntity();
        Entity directEntity = damageSource.getDirectEntity();
        if (entity instanceof LivingEntity livingEntity) bumpOrConsumeRage(livingEntity, event, level, attacked);
        if (directEntity != null && directEntity.equals(entity)) return;
        if (directEntity instanceof LivingEntity directLiving) bumpOrConsumeRage(directLiving, event, level, attacked);
    }

    private static void bumpOrConsumeRage(LivingEntity sourceEntity, LivingDamageEvent.Pre event, Level level, LivingEntity attacked) {
        if (!((RageHolder)sourceEntity).rage$isFullRage()) {
            ((RageHolder)sourceEntity).rage$bumpRage();
            return;
        }
        double damageBonus = ((RageHolder)sourceEntity).rage$getDamageBonus();
        float newDamage = (float)(damageBonus * (double) event.getOriginalDamage());

        event.setNewDamage(newDamage);
        NeoForge.EVENT_BUS.post(new FullRageAttackEvent(attacked, sourceEntity, newDamage, (float) damageBonus));

        if (Rage.playDingOnFullRageAttack.get()) {
            level.playSound(null, attacked.blockPosition(), SoundEvents.ARROW_HIT_PLAYER, sourceEntity.getSoundSource(), Rage.dingVolume.get().floatValue(), Rage.dingPitch.get().floatValue());
        }

        ((RageHolder)sourceEntity).rage$clearRage();
    }
}
