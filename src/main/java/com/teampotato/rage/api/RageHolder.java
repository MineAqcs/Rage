package com.teampotato.rage.api;

import com.teampotato.rage.Rage;

public interface RageHolder {
    int rage$getRage();

    void raget$setRage(int newRage);

    void rage$bumpRage();

    void rage$clearRage();

    default void rage$decreaseRage() {
        int loss = Rage.entityRageLossPerInterval.get();
        this.raget$setRage(loss >= this.rage$getRage() ? 0 : this.rage$getRage() - loss);
    }

    default boolean rage$isFullRage() {
        return this.rage$getRage() >= Rage.fullRageValue.get();
    }

    default double rage$getDamageBonus() {
        double bonus = Rage.basicDamageBonus.get() + ((((double) this.rage$getRage()) - Rage.fullRageValue.get().doubleValue()) / 100D);
        if (bonus > Rage.maxDamageBonus.get()) bonus = Rage.maxDamageBonus.get();
        return bonus;
    }
}
