package com.teampotato.rage.api.event;

import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.event.entity.living.LivingEvent;

public class FullRageAttackEvent extends LivingEvent {
    private final LivingEntity attacked, attacker;
    private final float damage, bonus;

    public FullRageAttackEvent(LivingEntity attacked, LivingEntity attacker, float damage, float bonus) {
        super(attacked);
        this.attacked = attacked;
        this.attacker = attacker;
        this.damage = damage;
        this.bonus = bonus;
    }

    public LivingEntity getAttacked() {
        return attacked;
    }

    public LivingEntity getAttacker() {
        return attacker;
    }
}
