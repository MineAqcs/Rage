package me.kall.rage.api;

import me.kall.rage.Rage;

public interface RageHolder {
    int rage$getRage();

    void rage$setRage(int newRage);

    default boolean rage$isFullRage() {
        return this.rage$getRage() >= Rage.FULL_RAGE_VALUE.get();
    }

    default double rage$getDamageBonus() {
        double bonus = Rage.BASIC_DAMAGE_BONUS.get();
        bonus += ((double)this.rage$getRage() - Rage.FULL_RAGE_VALUE.get().doubleValue()) / 100D;
        if (bonus > Rage.MAX_DAMAGE_BONUS.get()) bonus = Rage.MAX_DAMAGE_BONUS.get();
        return bonus;
    }
}
