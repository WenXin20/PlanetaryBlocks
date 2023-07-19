package com.wenxin2.planetary_blocks.init;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;

public class FoodRegistry {
    public static final FoodProperties MOON;

    static
    {
        MOON = (new FoodProperties.Builder()).nutrition(4).saturationMod(0.5F)
                .effect(new MobEffectInstance(MobEffects.SLOW_FALLING, 300, 0), 1.0F).alwaysEat().build();
    }
}
