package com.wenxin2.planetary_blocks.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.NotNull;

public class PlanetBlock extends RotatedPillarBlock
{
    public static final EnumProperty<ColumnBlockStates> COLUMN = EnumProperty.create("column", ColumnBlockStates.class);
    public static final IntegerProperty POWERED = BlockStateProperties.POWER;
    public static final BooleanProperty ROTATION = BooleanProperty.create("rotation");

    public final boolean spawnParticles;

    public PlanetBlock(BlockBehaviour.Properties properties, Direction.Axis direction, boolean spawnParticles)
    {
        super(properties);
        this.spawnParticles = spawnParticles;
        this.registerDefaultState(this.getStateDefinition().any().setValue(AXIS, direction)
                .setValue(POWERED, 0).setValue(ROTATION, Boolean.FALSE).setValue(COLUMN, ColumnBlockStates.NONE));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateBuilder) {
        stateBuilder.add(AXIS, COLUMN, POWERED, ROTATION);
    }

    @NotNull
    @Override
    public BlockState updateShape(@NotNull BlockState state, @NotNull Direction direction, @NotNull BlockState neighborState, @NotNull LevelAccessor world, @NotNull BlockPos pos, @NotNull BlockPos neighborPos)
    {
        super.updateShape(state, direction, neighborState, world, pos, neighborPos);

        Block blockAbove = world.getBlockState(pos.above()).getBlock();
        Block blockBelow = world.getBlockState(pos.below()).getBlock();
        Block blockNorth = world.getBlockState(pos.north()).getBlock();
        Block blockSouth = world.getBlockState(pos.south()).getBlock();
        Block blockEast = world.getBlockState(pos.east()).getBlock();
        Block blockWest = world.getBlockState(pos.west()).getBlock();

        boolean axisX = state.getValue(AXIS) == Direction.Axis.X;
        boolean axisY = state.getValue(AXIS) == Direction.Axis.Y;
        boolean axisZ = state.getValue(AXIS) == Direction.Axis.Z;

        if (blockEast == this && axisX)
        {
            if (blockWest == this)
                return state.setValue(COLUMN, ColumnBlockStates.MIDDLE);
            return state.setValue(COLUMN, ColumnBlockStates.BOTTOM);
        }
        if (blockWest == this && axisX)
            return state.setValue(COLUMN, ColumnBlockStates.TOP);

        if (blockAbove == this && axisY)
        {
            if (blockBelow == this)
                return state.setValue(COLUMN, ColumnBlockStates.MIDDLE);
            return state.setValue(COLUMN, ColumnBlockStates.BOTTOM);
        }
        if (blockBelow == this && axisY)
            return state.setValue(COLUMN, ColumnBlockStates.TOP);

        if (blockNorth == this && axisZ)
        {
            if (blockSouth == this)
                return state.setValue(COLUMN, ColumnBlockStates.MIDDLE);
            return state.setValue(COLUMN, ColumnBlockStates.BOTTOM);
        }
        if (blockSouth == this && axisZ)
            return state.setValue(COLUMN, ColumnBlockStates.TOP);

        return state.setValue(COLUMN, ColumnBlockStates.NONE);

    }

    @Override
    public void setPlacedBy(@NotNull Level world, @NotNull BlockPos pos, @NotNull BlockState state, LivingEntity entity, @NotNull ItemStack stack)
    {
        this.updateRedstone(state, world, pos);
    }

    @Override
    public void neighborChanged(@NotNull BlockState state, @NotNull Level world, @NotNull BlockPos pos, @NotNull Block neighborBlock, @NotNull BlockPos pos2, boolean rotation)
    {
        this.updateRedstone(state, world, pos);
        super.neighborChanged(state, world, pos, neighborBlock, pos, rotation);
    }

    public void updateRedstone(BlockState state, Level world, BlockPos pos)
    {
        if (!world.isClientSide)
        {
            int power = world.getBestNeighborSignal(pos);
            world.setBlock(pos, state.setValue(POWERED, Mth.clamp(power, 0, 15)), 1 | 2 | 4);
//            world.playSound(null, pos, SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.BLOCKS, 5.25F, 0.05F);
            world.scheduleTick(pos, this, 4);
            this.updateRotation(state, world, pos);
        }
    }

    public void updateRotation(BlockState state, Level world, BlockPos pos)
    {
        if (!world.isClientSide)
        {
            int bestSignal = world.getBestNeighborSignal(pos);
            int power = world.getBlockState(pos).getValue(POWERED);

            if (power > 0)
            {
                world.scheduleTick(pos, this, 4);
                world.setBlock(pos, state.setValue(ROTATION, Boolean.TRUE).setValue(POWERED, Mth.clamp(bestSignal, 0, 15)), 4);
            }
            else {
                world.scheduleTick(pos, this, 4);
                world.setBlock(pos, state.setValue(ROTATION, Boolean.FALSE).setValue(POWERED, 0), 4);
            }
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {

        return this.defaultBlockState().setValue(AXIS, context.getClickedFace().getAxis())
                .setValue(ROTATION, context.getLevel().hasNeighborSignal(context.getClickedPos()))
                .setValue(COLUMN, ColumnBlockStates.NONE);
    }

    public int getSignal(BlockState state, @NotNull BlockGetter block, @NotNull BlockPos pos, @NotNull Direction side) {
        return Math.max(0, state.getValue(POWERED) - 1);
    }
}
