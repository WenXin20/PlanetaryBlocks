package com.wenxin2.planetary_blocks.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;

public class OxidizablePanelBlock extends IronBarsBlock implements SimpleWaterloggedBlock, WeatheringCopper
{
    private final WeatherState weatherState;

    public OxidizablePanelBlock(Properties properties, WeatherState weatherState) {
        super(properties);
        this.weatherState = weatherState;
        this.registerDefaultState(this.getStateDefinition().any().setValue(WATERLOGGED, Boolean.FALSE));
    }

    @Override
    public void randomTick(BlockState state, ServerLevel serverWorld, BlockPos pos, RandomSource source) {
        this.onRandomTick(state, serverWorld, pos, source);
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return getNext(state).isPresent();
    }

    @Override
    public WeatherState getAge() {
        return this.weatherState;
    }
}
