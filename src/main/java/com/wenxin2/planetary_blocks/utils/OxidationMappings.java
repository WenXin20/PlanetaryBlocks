package com.wenxin2.planetary_blocks.utils;

import com.google.common.base.Suppliers;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.wenxin2.planetary_blocks.init.ModRegistry;
import java.util.Optional;
import java.util.function.Supplier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;

public abstract class OxidationMappings implements WeatheringCopper {
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

    public static Optional<Block> getPrevious(Block block) {
        return Optional.ofNullable(OxidationMappings.PREVIOUS_BY_BLOCK.get().get(block));
    }

    public static Optional<BlockState> getPrevious(BlockState state) {
        return getPrevious(state.getBlock()).map((block) -> {
            return block.withPropertiesOf(state);
        });
    }

    public static Block getFirst(Block block2) {
        Block block = block2;

        for(Block block1 = OxidationMappings.PREVIOUS_BY_BLOCK.get().get(block2); block1 != null; block1 = OxidationMappings.PREVIOUS_BY_BLOCK.get().get(block1)) {
            block = block1;
        }

        return block;
    }

    public static BlockState getFirst(BlockState state) {
        return getFirst(state.getBlock()).withPropertiesOf(state);
    }

    public static Optional<BlockState> getWaxables(BlockState state) {
        return Optional.ofNullable(OxidationMappings.WAXABLES.get().get(state.getBlock())).map((block) -> {
            return block.withPropertiesOf(state);
        });
    }

    @Override
    public Optional<BlockState> getNext(BlockState state) {
        return Optional.ofNullable(OxidationMappings.NEXT_BY_BLOCK.get().get(state.getBlock())).map((block) -> block.withPropertiesOf(state));
    }

    public static Optional<BlockState> getWaxOffState(BlockState state) {
        return Optional.ofNullable(OxidationMappings.WAX_OFF_BY_BLOCK.get().get(state.getBlock())).map((blockState) -> blockState.withPropertiesOf(state));
    }

    public static Optional<BlockState> getPreviousOxidationState(BlockState state) {
        return Optional.ofNullable(OxidationMappings.PREVIOUS_BY_BLOCK.get().get(state.getBlock())).map((block) -> block.withPropertiesOf(state));
    }
}
