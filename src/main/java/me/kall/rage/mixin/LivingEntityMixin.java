package me.kall.rage.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import me.kall.rage.Rage;
import me.kall.rage.api.RageHolder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.Optional;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements RageHolder {
    @Shadow @Nullable public abstract AttributeInstance getAttribute(Attribute attribute);

    @Unique private int rage$decreaseInterval;
    @Unique private static final ResourceLocation PLAYER_ID = new ResourceLocation("minecraft:player");

    public LivingEntityMixin(EntityType<?> arg, Level arg2) {
        super(arg, arg2);
    }

    @ModifyVariable(method = "hurt", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private float rage$onHurt(float pAmount, @Local(argsOnly = true) DamageSource pSource) {
        this.rage$setRage(this.rage$getRage() + Rage.Config.GAINED_RAGE_ON_BEING_ATTACKED.get());
        Entity entity = pSource.getEntity();
        Entity directEntity = pSource.getDirectEntity();
        if (entity instanceof LivingEntity) {
            if (((RageHolder)entity).rage$isFullRage()) {
                double damageBonus = ((RageHolder)entity).rage$getDamageBonus();
                pAmount = (float) (damageBonus * pAmount);
                ((RageHolder)entity).rage$setRage(0);
                if (Rage.Config.PLAY_DING_ON_FULL_RAGE_ATTACK.get()) {
                    this.level.playSound(null, this.blockPosition(), SoundEvents.ARROW_HIT_PLAYER, entity.getSoundSource(), Rage.Config.DING_VOLUME.get().floatValue(), Rage.Config.DING_PITCH.get().floatValue());
                }
            } else {
                ((RageHolder)entity).rage$setRage(((RageHolder)entity).rage$getRage() + Rage.Config.GAINED_RAGE_ON_ATTACKING.get());
            }
        }

        if (entity != null && directEntity != null && entity.getStringUUID().equals(directEntity.getStringUUID())) return pAmount;

        if (directEntity instanceof LivingEntity) {
            if (((RageHolder)directEntity).rage$isFullRage()) {
                double damageBonus = ((RageHolder)directEntity).rage$getDamageBonus();
                pAmount = (float) (damageBonus * pAmount);
                ((RageHolder)directEntity).rage$setRage(0);
                if (Rage.Config.PLAY_DING_ON_FULL_RAGE_ATTACK.get()) {
                    this.level.playSound(null, this.blockPosition(), SoundEvents.ARROW_HIT_PLAYER, directEntity.getSoundSource(), Rage.Config.DING_VOLUME.get().floatValue(), Rage.Config.DING_PITCH.get().floatValue());
                }
            } else {
                ((RageHolder)directEntity).rage$setRage(((RageHolder)directEntity).rage$getRage() + Rage.Config.GAINED_RAGE_ON_ATTACKING.get());
            }
        }

        return pAmount;
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void rage$onTick(CallbackInfo ci) {
        if (this.level instanceof ServerLevel) {
            this.rage$decreaseInterval += 1;
            if (this.rage$decreaseInterval == Rage.Config.DECREASE_INTERVAL_TICKS.get()) {
                this.rage$decreaseInterval = 0;
                int lost = Rage.Config.RAGE_THAT_ENTITY_LOSES_EVERY_INTERVAL.get();
                this.rage$setRage(lost >= this.rage$getRage() ? 0 : this.rage$getRage() - lost);
            }

            if (Rage.Config.SHOW_PARTICLE_ON_FULL_RAGE.get() && this.rage$isFullRage()) {
                ServerLevel serverWorld = (ServerLevel) this.level;;
                serverWorld.sendParticles(ParticleTypes.CRIT, this.getX(), this.getY(), this.getZ(), 8, 0.2, 0.2, 0.2, 0.0);
            }
        }
    }

    @Override
    public int rage$getRage() {
        if (Rage.Config.ONLY_PLAYERS_HAVE_RAGE.get() && !PLAYER_ID.equals(this.getType().getRegistryName())) return 0;
        return this.rage$getAttribute().map(instance -> (int) instance.getBaseValue()).orElse(0);
    }

    @Override
    public void rage$setRage(int newRage) {
        this.rage$getAttribute().ifPresent(attributeInstance -> attributeInstance.setBaseValue(newRage));
    }

    @Unique
    private Optional<AttributeInstance> rage$getAttribute() {
        return Optional.ofNullable(this.getAttribute(Rage.RAGE.get()));
    }
}
