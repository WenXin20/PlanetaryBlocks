package com.wenxin2.planetary_blocks.init;

import net.minecraftforge.common.ForgeConfigSpec;

public class Config
{
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static ForgeConfigSpec CONFIG;

    public static final String CATEGORY_CLIENT = "Client";
    public static final String CATEGORY_COMMON = "Common";

    public static ForgeConfigSpec.BooleanValue FOREVER_EARTH_NIGHT;
    public static ForgeConfigSpec.BooleanValue CLASSIC_SUN_BURNING;
    public static ForgeConfigSpec.BooleanValue CLASSIC_SUN_PARTICLES;
    public static ForgeConfigSpec.BooleanValue ENABLE_ROTATION;
    public static ForgeConfigSpec.BooleanValue EARTH_NIGHT;
    public static ForgeConfigSpec.BooleanValue SUN_BURNING;
    public static ForgeConfigSpec.BooleanValue SUN_PARTICLES;
    public static ForgeConfigSpec.IntValue CLASSIC_MOON_PHASE;

    static
    {
        initializeConfig();
        CONFIG = BUILDER.build();
    }

    public static void initializeConfig()
    {
        BUILDER.comment("Planetary Blocks Configuration File").push(CATEGORY_CLIENT);
        CLASSIC_SUN_PARTICLES = BUILDER.comment("Enable particles for the Classic Sun block. " + "[Default: false]")
                .define("classic_sun_particles", false);
        SUN_PARTICLES = BUILDER.comment("Enable particles for the Sun block. " + "[Default: true]")
                .define("sun_particles", true);
        BUILDER.pop();

        BUILDER.push(CATEGORY_COMMON);
        CLASSIC_SUN_BURNING = BUILDER.comment("Enable fire damage for the Classic Sun block. " + "[Default: true]")
                .define("classic_sun_burning", true);
        CLASSIC_MOON_PHASE = BUILDER.comment("Preferred moon phase for the Classic Moon block. Set to -1 for the moon phases to sync with the in-game moon. " + "[Default: -1]")
                .defineInRange("classic_moon_phase", -1, -1, 7);
        EARTH_NIGHT = BUILDER.comment("Enable night mode for the Earth block. " + "[Default: true]")
                .define("earth_night", true);
        ENABLE_ROTATION = BUILDER.comment("Enable rotation for planetary blocks. " + "[Default: true]")
                .define("enable_rotation", true);
        FOREVER_EARTH_NIGHT = BUILDER.comment("Night mode for the Earth block is always enabled. " + "[Default: false]")
                .define("forever_earth_night", false);
        SUN_BURNING = BUILDER.comment("Enable fire damage for the Sun block. " + "[Default: true]")
                .define("sun_burning", true);
    }
}
