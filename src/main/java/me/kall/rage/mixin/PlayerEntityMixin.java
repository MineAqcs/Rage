package me.kall.rage.mixin;

import me.kall.rage.Rage;
import me.kall.rage.api.RageHolder;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements RageHolder {
    protected PlayerEntityMixin(EntityType<? extends LivingEntity> p_i48577_1_, World p_i48577_2_) {
        super(p_i48577_1_, p_i48577_2_);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void rage$onTick(CallbackInfo ci) {
        if (Rage.SHOW_PARTICLE_ON_FULL_RAGE.get() && this.rage$isFullRage() && this.level instanceof ServerWorld) {
            ServerWorld serverWorld = (ServerWorld) this.level;
            serverWorld.sendParticles(ParticleTypes.CRIT, this.getX(), this.getY(), this.getZ(), 8, 0.2, 0.2, 0.2, 0.0);
        }
    }
}
