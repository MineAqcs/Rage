package me.kall.rage.mixin;

import me.kall.rage.Rage;
import me.kall.rage.api.RageHolder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerEntityMixin extends LivingEntity implements RageHolder {
    protected PlayerEntityMixin(EntityType<? extends LivingEntity> p_i48577_1_, Level p_i48577_2_) {
        super(p_i48577_1_, p_i48577_2_);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void rage$onTick(CallbackInfo ci) {
        if (Rage.Config.SHOW_PARTICLE_ON_FULL_RAGE.get() && this.rage$isFullRage() && this.level instanceof ServerLevel) {
            ServerLevel serverWorld = (ServerLevel) this.level;
            serverWorld.sendParticles(ParticleTypes.CRIT, this.getX(), this.getY(), this.getZ(), 8, 0.2, 0.2, 0.2, 0.0);
        }
    }
}
