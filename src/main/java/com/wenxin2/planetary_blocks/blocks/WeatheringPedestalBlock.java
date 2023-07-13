package com.wenxin2.planetary_blocks.blocks;

import com.google.common.base.Suppliers;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.wenxin2.planetary_blocks.init.ModRegistry;
import java.util.Optional;
import java.util.function.Supplier;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.HoneycombItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;

public class WeatheringPedestalBlock extends PedestalBlock implements SimpleWaterloggedBlock, WeatheringCopper
{
    private final WeatheringCopper.WeatherState weatherState;
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
                .put(ModRegistry.COPPER_PEDESTAL.get(), ModRegistry.EXPOSED_COPPER_PEDESTAL.get())
                .put(ModRegistry.EXPOSED_COPPER_PEDESTAL.get(), ModRegistry.WEATHERED_COPPER_PEDESTAL.get())
                .put(ModRegistry.WEATHERED_COPPER_PEDESTAL.get(), ModRegistry.OXIDIZED_COPPER_PEDESTAL.get())
                .build();
    });
    public static final Supplier<BiMap<Block, Block>> UNWAXED_BY_BLOCK = Suppliers.memoize(() -> {
        return WAXABLES.get().inverse();
    });

    public WeatheringPedestalBlock(Properties properties, BlockState state, WeatheringCopper.WeatherState weatherState, Direction.Axis direction) {
        super(properties, direction);
        this.weatherState = weatherState;
        this.registerDefaultState(this.getStateDefinition().any().setValue(AXIS, direction).setValue(COLUMN, ColumnBlockStates.NONE)
                .setValue(POWERED, Boolean.FALSE).setValue(WATERLOGGED, Boolean.FALSE));
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (player.getItemInHand(hand).getItem() instanceof HoneycombItem) {
            return getWaxed(state).map((stateWaxable) -> {
                ItemStack itemstack = player.getItemInHand(hand);
                if (player instanceof ServerPlayer) {
                    CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger((ServerPlayer)player, pos, itemstack);
                }

                itemstack.shrink(1);
                world.setBlock(pos, stateWaxable, 11);
                world.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, stateWaxable));
                world.levelEvent(player, 3003, pos, 0);
                return InteractionResult.sidedSuccess(world.isClientSide);
            }).orElse(InteractionResult.PASS);
        }

//        if (player.getItemInHand(hand).getItem() instanceof AxeItem) {
//            return getWeathered(state).map((stateWeathered) -> {
//                ItemStack itemstack = player.getItemInHand(hand);
//                if (player instanceof ServerPlayer) {
//                    CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger((ServerPlayer)player, pos, itemstack);
//                }
//
//                ToolActions.AXE_SCRAPE;
//                world.setBlock(pos, stateWeathered, 11);
//                world.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, stateWeathered));
//                world.levelEvent(player, 3003, pos, 0);
//                return InteractionResult.sidedSuccess(world.isClientSide);
//            }).orElse(InteractionResult.PASS);
//        }
        return InteractionResult.FAIL;
    }

    public static BlockState getToolModifiedState(ToolAction toolAction, BlockState state) {
        if (ToolActions.AXE_SCRAPE == toolAction) return getPrevious(state).orElse(null);
        if (ToolActions.AXE_WAX_OFF == toolAction) return getUnWaxed(state).orElse(null);
        return null;
    }

    public static Optional<BlockState> getWeathered(BlockState state) {
        return Optional.ofNullable(NEXT_BY_BLOCK.get().get(state.getBlock())).map((block) -> {
            return block.withPropertiesOf(state);
        });
    }

    static Optional<Block> getPrevious(Block block) {
        return Optional.ofNullable(PREVIOUS_BY_BLOCK.get().get(block));
    }
    static Optional<BlockState> getPrevious(BlockState stateIn) {
        return getPrevious(stateIn.getBlock()).map((block) -> block.withPropertiesOf(stateIn));
    }

    public static Optional<BlockState> getWaxed(BlockState state) {
        return Optional.ofNullable(WAXABLES.get().get(state.getBlock())).map((block) -> {
            return block.withPropertiesOf(state);
        });
    }

    static Optional<Block> getUnWaxed(Block block) {
        return Optional.ofNullable(UNWAXED_BY_BLOCK.get().get(block));
    }
    static Optional<BlockState> getUnWaxed(BlockState state) {
        return getUnWaxed(state.getBlock()).map((block) -> block.withPropertiesOf(state));
    }

    @Override
    public void randomTick(BlockState state, ServerLevel serverWorld, BlockPos pos, RandomSource source) {
        this.onRandomTick(state, serverWorld, pos, source);
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return WeatheringCopper.getNext(state.getBlock()).isPresent();
    }

    @Override
    public Optional<BlockState> getNext(BlockState state) {
        return Optional.ofNullable(NEXT_BY_BLOCK.get().get(state.getBlock())).map((block) -> block.withPropertiesOf(state));
    }

    public static Optional<BlockState> getPreviousState(BlockState state) {
        return Optional.ofNullable(PREVIOUS_BY_BLOCK.get().get(state.getBlock())).map((block) -> block.withPropertiesOf(state));
    }

    public static Block getFirst(Block baseBlock) {
        Block block = baseBlock;

        for(Block block1 = PREVIOUS_BY_BLOCK.get().get(block); block1 != null; block1 = PREVIOUS_BY_BLOCK.get().get(block1)) {
            block = block1;
        }

        return block;
    }

    public static BlockState getFirst(BlockState state) {
        return getFirst(state.getBlock()).withPropertiesOf(state);
    }

    @Override
    public WeatheringCopper.WeatherState getAge() {
        return this.weatherState;
    }

//    @Nullable
//    @Override
//    public BlockState getToolModifiedState(BlockState state, UseOnContext context, ToolAction toolAction, boolean simulate) {
//        BlockState a = IWeatheringCopper.getToolModifiedState(toolAction, state);
//        if (a != null) return a;
//        return super.getToolModifiedState(state, context, toolAction, simulate);
//    }
//
//    @Override
//    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
//        return IWeatheringCopper.onUse(state, level, pos, player, hand);
//    }
}
