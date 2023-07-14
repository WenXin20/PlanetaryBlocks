package com.wenxin2.planetary_blocks.utils;

import com.google.common.base.Suppliers;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.wenxin2.planetary_blocks.init.ModRegistry;
import java.util.function.Supplier;
import net.minecraft.world.level.block.Block;

public class OxidationMappings {
    public static Supplier<BiMap<Block, Block>> NEXT_BY_BLOCK = Suppliers.memoize(() ->
    {
        return ImmutableBiMap.<Block, Block>builder()
                .put(ModRegistry.COPPER_PEDESTAL.get(), ModRegistry.EXPOSED_COPPER_PEDESTAL.get())
                .put(ModRegistry.EXPOSED_COPPER_PEDESTAL.get(), ModRegistry.WEATHERED_COPPER_PEDESTAL.get())
                .put(ModRegistry.WEATHERED_COPPER_PEDESTAL.get(), ModRegistry.OXIDIZED_COPPER_PEDESTAL.get())
                .build();
    });

    public static final Supplier<BiMap<Block, Block>> PREVIOUS_BY_BLOCK = Suppliers.memoize(() -> {
        return NEXT_BY_BLOCK.get().inverse();
    });

    public static Supplier<BiMap<Block, Block>> WAXABLES = Suppliers.memoize(() ->
    {
        return ImmutableBiMap.<Block, Block>builder()
                .put(ModRegistry.COPPER_PEDESTAL.get(), ModRegistry.WAXED_COPPER_PEDESTAL.get())
                .put(ModRegistry.EXPOSED_COPPER_PEDESTAL.get(), ModRegistry.WAXED_EXPOSED_COPPER_PEDESTAL.get())
                .put(ModRegistry.OXIDIZED_COPPER_PEDESTAL.get(), ModRegistry.WAXED_OXIDIZED_COPPER_PEDESTAL.get())
                .put(ModRegistry.WEATHERED_COPPER_PEDESTAL.get(), ModRegistry.WAXED_WEATHERED_COPPER_PEDESTAL.get())
                .build();
    });

    public static final Supplier<BiMap<Block, Block>> WAX_OFF_BY_BLOCK = Suppliers.memoize(() -> {
        return WAXABLES.get().inverse();
    });
}
