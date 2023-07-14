package com.wenxin2.planetary_blocks.utils;

import com.google.common.base.Suppliers;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.wenxin2.planetary_blocks.init.ModRegistry;
import java.util.Optional;
import java.util.function.Supplier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChangeOverTimeBlock;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;

public interface OxidationMappings extends ChangeOverTimeBlock<WeatheringCopper.WeatherState> {
    Supplier<BiMap<Block, Block>> NEXT_BY_BLOCK = Suppliers.memoize(() ->
    {
        return ImmutableBiMap.<Block, Block>builder()
                .put(ModRegistry.COPPER_PEDESTAL.get(), ModRegistry.EXPOSED_COPPER_PEDESTAL.get())
                .put(ModRegistry.EXPOSED_COPPER_PEDESTAL.get(), ModRegistry.WEATHERED_COPPER_PEDESTAL.get())
                .put(ModRegistry.WEATHERED_COPPER_PEDESTAL.get(), ModRegistry.OXIDIZED_COPPER_PEDESTAL.get())
                .put(ModRegistry.COPPER_PANEL.get(), ModRegistry.EXPOSED_COPPER_PANEL.get())
                .put(ModRegistry.EXPOSED_COPPER_PANEL.get(), ModRegistry.WEATHERED_COPPER_PANEL.get())
                .put(ModRegistry.WEATHERED_COPPER_PANEL.get(), ModRegistry.OXIDIZED_COPPER_PANEL.get())
                .build();
    });

    Supplier<BiMap<Block, Block>> PREVIOUS_BY_BLOCK = Suppliers.memoize(() -> {
        return NEXT_BY_BLOCK.get().inverse();
    });

    Supplier<BiMap<Block, Block>> WAXABLES = Suppliers.memoize(() ->
    {
        return ImmutableBiMap.<Block, Block>builder()
                .put(ModRegistry.COPPER_PEDESTAL.get(), ModRegistry.WAXED_COPPER_PEDESTAL.get())
                .put(ModRegistry.EXPOSED_COPPER_PEDESTAL.get(), ModRegistry.WAXED_EXPOSED_COPPER_PEDESTAL.get())
                .put(ModRegistry.OXIDIZED_COPPER_PEDESTAL.get(), ModRegistry.WAXED_OXIDIZED_COPPER_PEDESTAL.get())
                .put(ModRegistry.WEATHERED_COPPER_PEDESTAL.get(), ModRegistry.WAXED_WEATHERED_COPPER_PEDESTAL.get())
                .build();
    });

    Supplier<BiMap<Block, Block>> WAX_OFF_BY_BLOCK = Suppliers.memoize(() -> {
        return WAXABLES.get().inverse();
    });

    static Optional<Block> getPrevious(Block block) {
        return Optional.ofNullable(OxidationMappings.PREVIOUS_BY_BLOCK.get().get(block));
    }

    static Optional<BlockState> getPrevious(BlockState state) {
        return getPrevious(state.getBlock()).map((block) -> {
            return block.withPropertiesOf(state);
        });
    }

    static Block getFirst(Block block2) {
        Block block = block2;

        for(Block block1 = PREVIOUS_BY_BLOCK.get().get(block2); block1 != null; block1 = PREVIOUS_BY_BLOCK.get().get(block1)) {
            block = block1;
        }

        return block;
    }

    static BlockState getFirst(BlockState state) {
        return getFirst(state.getBlock()).withPropertiesOf(state);
    }

    static Optional<BlockState> getWaxables(BlockState state) {
        return Optional.ofNullable(WAXABLES.get().get(state.getBlock())).map((block) -> {
            return block.withPropertiesOf(state);
        });
    }

    static Optional<Block> getNext(Block p_154905_) {
        return Optional.ofNullable(NEXT_BY_BLOCK.get().get(p_154905_));
    }

    static Optional<BlockState> getWaxOffState(BlockState state) {
        return Optional.ofNullable(WAX_OFF_BY_BLOCK.get().get(state.getBlock())).map((blockState) -> blockState.withPropertiesOf(state));
    }

    static Optional<BlockState> getPreviousOxidationState(BlockState state) {
        return Optional.ofNullable(PREVIOUS_BY_BLOCK.get().get(state.getBlock())).map((block) -> block.withPropertiesOf(state));
    }

    public static enum WeatherState {
        UNAFFECTED,
        EXPOSED,
        WEATHERED,
        OXIDIZED;
    }
}
