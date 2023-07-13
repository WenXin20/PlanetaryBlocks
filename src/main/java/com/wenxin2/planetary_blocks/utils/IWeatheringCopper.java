package com.wenxin2.planetary_blocks.utils;

import com.google.common.base.Suppliers;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.wenxin2.planetary_blocks.init.ModRegistry;
import java.util.Optional;
import java.util.function.Supplier;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HoneycombItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChangeOverTimeBlock;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.fml.common.Mod;

public interface IWeatheringCopper extends ChangeOverTimeBlock<WeatheringCopper.WeatherState> {
    Supplier<BiMap<Block, Block>> NEXT_BY_BLOCK = Suppliers.memoize(() ->
    {
        ImmutableBiMap.Builder<Block, Block> builder = ImmutableBiMap.builder();
        builder.put(ModRegistry.COPPER_PEDESTAL.get(), ModRegistry.EXPOSED_COPPER_PEDESTAL.get());
        builder.put(ModRegistry.EXPOSED_COPPER_PEDESTAL.get(), ModRegistry.WEATHERED_COPPER_PEDESTAL.get());
        builder.put(ModRegistry.WEATHERED_COPPER_PEDESTAL.get(), ModRegistry.OXIDIZED_COPPER_PEDESTAL.get());
        return builder.build();
    });

    Supplier<BiMap<Block, Block>> PREVIOUS_BY_BLOCK = Suppliers.memoize(() -> NEXT_BY_BLOCK.get().inverse());

    Supplier<BiMap<Block, Block>> WAXED_BY_BLOCK = Suppliers.memoize(() ->
    {
        ImmutableBiMap.Builder<Block, Block> builder = ImmutableBiMap.builder();
        builder.put(ModRegistry.COPPER_PEDESTAL.get(), ModRegistry.EXPOSED_COPPER_PEDESTAL.get());
        builder.put(ModRegistry.EXPOSED_COPPER_PEDESTAL.get(), ModRegistry.WEATHERED_COPPER_PEDESTAL.get());
        builder.put(ModRegistry.WEATHERED_COPPER_PEDESTAL.get(), ModRegistry.OXIDIZED_COPPER_PEDESTAL.get());
        return builder.build();
    });

    Supplier<BiMap<Block, Block>> UNWAXED_BY_BLOCK = Suppliers.memoize(() -> WAXED_BY_BLOCK.get().inverse());

    static Optional<Block> getPrevious(Block block) {
        return Optional.ofNullable(PREVIOUS_BY_BLOCK.get().get(block));
    }
    static Optional<BlockState> getPrevious(BlockState stateIn) {
        return getPrevious(stateIn.getBlock()).map((block) -> block.withPropertiesOf(stateIn));
    }

    static Optional<Block> getNext(Block block) {
        return Optional.ofNullable(NEXT_BY_BLOCK.get().get(block));
    }
    default Optional<BlockState> getNext(BlockState state) {
        return getNext(state.getBlock()).map((block) -> block.withPropertiesOf(state));
    }

    static Optional<Block> getWaxed(Block block) {
        return Optional.ofNullable(WAXED_BY_BLOCK.get().get(block));
    }
    static Optional<BlockState> getWaxed(BlockState state) {
        return getWaxed(state.getBlock()).map((block) -> block.withPropertiesOf(state));
    }

    static Optional<Block> getUnWaxed(Block block) {
        return Optional.ofNullable(UNWAXED_BY_BLOCK.get().get(block));
    }
    static Optional<BlockState> getUnWaxed(BlockState state) {
        return getUnWaxed(state.getBlock()).map((block) -> block.withPropertiesOf(state));
    }

    static Block getFirst(Block baseBlock) {
        Block block = baseBlock;

        for(Block block1 = PREVIOUS_BY_BLOCK.get().get(block); block1 != null; block1 = PREVIOUS_BY_BLOCK.get().get(block1)) {
            block = block1;
        }

        return block;
    }

    static BlockState getFirst(BlockState state) {
        return getFirst(state.getBlock()).withPropertiesOf(state);
    }

    default float getChanceModifier() {
        return this.getAge() == WeatheringCopper.WeatherState.UNAFFECTED ? 0.75F : 1.0F;
    }

    static BlockState getToolModifiedState(ToolAction toolAction, BlockState state) {
        if (ToolActions.AXE_SCRAPE == toolAction) return getPrevious(state).orElse(null);
        if (ToolActions.AXE_WAX_OFF == toolAction) return getUnWaxed(state).orElse(null);
        return null;
    }

    static InteractionResult onUse(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.getItem() instanceof HoneycombItem) {
            Optional<BlockState> waxed = getWaxed(state);

            if (waxed.isPresent()) {
                if (player instanceof ServerPlayer) {
                    CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger((ServerPlayer)player, pos, stack);
                }
                stack.shrink(1);
                world.setBlock(pos, waxed.get(), 11);
                world.levelEvent(player, 3003, pos, 0);
                return InteractionResult.sidedSuccess(world.isClientSide);
            }
        }
        return InteractionResult.PASS;
    }
}
