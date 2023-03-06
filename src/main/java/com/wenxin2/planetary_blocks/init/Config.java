package com.wenxin2.planetary_blocks.init;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import com.wenxin2.planetary_blocks.PlanetaryBlocks;
import java.nio.file.Path;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;

public class Config
{
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static ForgeConfigSpec CONFIG;

    public static final String CATEGORY_CLIENT = "Client";

    public static ForgeConfigSpec.BooleanValue sunParticles;
    public static ForgeConfigSpec.BooleanValue classicSunParticles;

    static {
        initializeConfig();

        CONFIG = BUILDER.build();
    }

    public static void initializeConfig() {

        BUILDER.comment("Planetary Blocks").push(CATEGORY_CLIENT);
        classicSunParticles = BUILDER.comment("Enable particles for the Classic Sun block. " + "[Default: false]")
                .define("classicSunParticles", false);
        sunParticles = BUILDER.comment("Enable particles for the Sun block. " + "[Default: true]")
                .define("sunParticles", true);
    }

//    public static void loadConfig(ForgeConfigSpec spec, Path path) {
//        final CommentedFileConfig configData = CommentedFileConfig.builder(path).sync().autosave().writingMode(WritingMode.REPLACE).build();
//        configData.load();
//        spec.setConfig(configData);
//    }
}
