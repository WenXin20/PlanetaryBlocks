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
        return OxidationMappings.getNext(state.getBlock()).isPresent();
    }

    @Override
    public WeatheringCopper.WeatherState getAge() {
        return this.weatherState;
    }
}
