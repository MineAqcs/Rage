package com.teampotato.rage.mixin;

import com.google.common.base.Suppliers;
import com.teampotato.rage.Rage;
import com.teampotato.rage.api.RageHolder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements RageHolder {
    @Unique private volatile int rage$currentRage;
    @Unique private int rage$decreaseInterval;
    @Unique private static final ResourceLocation PLAYER_ID = ResourceLocation.fromNamespaceAndPath("minecraft", "player");
    @Unique private final Supplier<Boolean> INVALID_RAGE_HOLER = Suppliers.memoize(() -> !PLAYER_ID.equals(BuiltInRegistries.ENTITY_TYPE.getKey(this.getType())));

    public LivingEntityMixin(EntityType<?> arg, Level arg2) {
        super(arg, arg2);
    }

    @Override
    public int rage$getRage() {
        if (INVALID_RAGE_HOLER.get() && Rage.onlyPlayersHaveRage.get()) return 0;
        return this.rage$currentRage;
    }

    @Override
    public void raget$setRage(int newRage) {
        this.rage$currentRage = newRage;
    }

    @Override
    public void rage$bumpRage() {
        this.raget$setRage(this.rage$getRage() + Rage.gainedRageOnAttackingOrBeingHurt.get());
        this.rage$notifyPlayer();
    }

    @Unique
    private void rage$notifyPlayer() {
        LivingEntity self = (LivingEntity) (Object) this;
        if (!(self instanceof Player player)) return;
        if (Rage.notifyPlayerOnRageChange.get()) {
            player.displayClientMessage(Component.translatable("rage.notify", this.rage$getRage()), true);
        }

        if (Rage.notifyPlayerOnReachingFullRage.get() && this.rage$isFullRage()) {
            player.displayClientMessage(Component.translatable("rage.notify.full"), true);
        }
    }

    @Override
    public void rage$clearRage() {
        this.raget$setRage(0);
        this.rage$notifyPlayer();
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void onTick(CallbackInfo ci) {
        this.rage$decreaseInterval = this.rage$decreaseInterval + 1;
        if (this.rage$decreaseInterval == Rage.decreaseIntervalTicks.get()) {
            this.rage$decreaseRage();
            this.rage$decreaseInterval = 0;
        }
    }
}
