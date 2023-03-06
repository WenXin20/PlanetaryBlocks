package com.wenxin2.planetary_blocks.init;

import net.minecraftforge.common.ForgeConfigSpec;

public class Config
{
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static ForgeConfigSpec CONFIG;

    public static final String CATEGORY_CLIENT = "Client";
    public static final String CATEGORY_COMMON = "Common";

    public static ForgeConfigSpec.BooleanValue classic_sun_burning;
    public static ForgeConfigSpec.BooleanValue classic_sun_particles;
    public static ForgeConfigSpec.BooleanValue sun_burning;
    public static ForgeConfigSpec.BooleanValue sun_particles;

    static
    {
        initializeConfig();
        CONFIG = BUILDER.build();
    }

    public static void initializeConfig()
    {
        BUILDER.comment("Planetary Blocks").push(CATEGORY_CLIENT);
        classic_sun_particles = BUILDER.comment("Enable particles for the Classic Sun block. " + "[Default: false]")
                .define("classic_sun_particles", false);
        sun_particles = BUILDER.comment("Enable particles for the Sun block. " + "[Default: true]")
                .define("sun_particles", true);
        BUILDER.pop();

        BUILDER.comment("Planetary Blocks").push(CATEGORY_COMMON);
        classic_sun_burning = BUILDER.comment("Enable fire damage for the Classic Sun block. " + "[Default: true]")
                .define("classic_sun_burning", true);
        sun_burning = BUILDER.comment("Enable fire damage for the Sun block. " + "[Default: true]")
                .define("sun_burning", true);
    }
}
