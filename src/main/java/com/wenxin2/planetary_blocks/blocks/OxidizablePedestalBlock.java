package com.wenxin2.planetary_blocks.blocks;

import com.wenxin2.planetary_blocks.utils.OxidationMappings;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;

public class OxidizablePedestalBlock extends PedestalBlock implements SimpleWaterloggedBlock, WeatheringCopper
{
    private final WeatheringCopper.WeatherState weatherState;

    public OxidizablePedestalBlock(Properties properties, WeatheringCopper.WeatherState weatherState, Direction.Axis direction) {
        super(properties, direction);
        this.weatherState = weatherState;
        this.registerDefaultState(this.getStateDefinition().any().setValue(AXIS, direction).setValue(COLUMN, ColumnBlockStates.NONE)
                .setValue(POWERED, Boolean.FALSE).setValue(WATERLOGGED, Boolean.FALSE));
    }

    @Override
    public void randomTick(BlockState state, ServerLevel serverWorld, BlockPos pos, RandomSource source) {
        this.onRandomTick(state, serverWorld, pos, source);
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return getNext(state).isPresent();
    }

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

    @Override
    public WeatheringCopper.WeatherState getAge() {
        return this.weatherState;
    }
}
