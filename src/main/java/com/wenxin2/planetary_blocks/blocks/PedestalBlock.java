package com.wenxin2.planetary_blocks.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class PedestalBlock extends RotatedPillarBlock
{
    public static final EnumProperty<ColumnBlockStates> COLUMN = EnumProperty.create("column", ColumnBlockStates.class);
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    private static final VoxelShape VOXELS_MAIN =
            Block.box(0, 8, 0, 16, 16, 16);
    private static final VoxelShape VOXELS_TOP = Shapes.or(
            Block.box(0, 8, 0, 16, 16, 16),
            Block.box(2, 0, 2, 14, 8, 14)).optimize();
    private static final VoxelShape VOXELS_MIDDLE =
            Block.box(2, 0, 2, 14, 16, 14);
    private static final VoxelShape VOXELS_BOTTOM = Shapes.or(
            Block.box(2, 8, 2, 14, 16, 14),
            Block.box(0, 0, 0, 16, 8, 16)).optimize();

    public PedestalBlock(Properties properties, Direction.Axis direction) {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(AXIS, direction).setValue(COLUMN, ColumnBlockStates.NONE)
                .setValue(POWERED, Boolean.FALSE));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateBuilder) {
        stateBuilder.add(AXIS, COLUMN, POWERED);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(AXIS, context.getClickedFace().getAxis())
                .setValue(COLUMN, ColumnBlockStates.NONE).setValue(POWERED, context.getLevel().hasNeighborSignal(context.getClickedPos()));
    }

    // Precise selection box
    @Override
    public VoxelShape getShape(final BlockState state, final BlockGetter worldIn, final BlockPos pos,
                               final CollisionContext context)
    {
        if (state.getValue(COLUMN) == ColumnBlockStates.NONE) return VOXELS_MAIN;
        else
            switch (state.getValue(COLUMN))
            {
                case TOP:
                    switch (state.getValue(AXIS))
                    {
                        case X:
                            return VOXELS_TOP;
                    }
                    return VOXELS_TOP;
                case MIDDLE:
                    return VOXELS_MIDDLE;
                case BOTTOM:
                    return VOXELS_BOTTOM;
                default:
                    return VOXELS_MAIN;
            }
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor worldAccessor, BlockPos pos, BlockPos pos2) {
        Block blockAbove = worldAccessor.getBlockState(pos.above()).getBlock();
        Block blockBelow = worldAccessor.getBlockState(pos.below()).getBlock();
        Block blockNorth = worldAccessor.getBlockState(pos.north()).getBlock();
        Block blockSouth = worldAccessor.getBlockState(pos.south()).getBlock();
        Block blockEast = worldAccessor.getBlockState(pos.east()).getBlock();
        Block blockWest = worldAccessor.getBlockState(pos.west()).getBlock();

        boolean axisX = state.getValue(AXIS) == Direction.Axis.X;
        boolean axisY = state.getValue(AXIS) == Direction.Axis.Y;
        boolean axisZ = state.getValue(AXIS) == Direction.Axis.Z;

        if (blockEast == this && axisX) {
            if (blockWest == this)
                return state.setValue(COLUMN, ColumnBlockStates.MIDDLE);
            return state.setValue(COLUMN, ColumnBlockStates.BOTTOM);
        }
        if (blockWest == this && axisX)
            return state.setValue(COLUMN, ColumnBlockStates.TOP);

        if (blockAbove == this && axisY) {
            if (blockBelow == this)
                return state.setValue(COLUMN, ColumnBlockStates.MIDDLE);
            return state.setValue(COLUMN, ColumnBlockStates.BOTTOM);
        }
        if (blockBelow == this && axisY)
            return state.setValue(COLUMN, ColumnBlockStates.TOP);

        if (blockNorth == this && axisZ) {
            if (blockSouth == this)
                return state.setValue(COLUMN, ColumnBlockStates.MIDDLE);
            return state.setValue(COLUMN, ColumnBlockStates.BOTTOM);
        }
        if (blockSouth == this && axisZ)
            return state.setValue(COLUMN, ColumnBlockStates.TOP);

        return state.setValue(COLUMN, ColumnBlockStates.NONE);
    }

    @Override
    public int getSignal(BlockState state, BlockGetter blockGetter, BlockPos pos, Direction direction) {
        return state.getValue(POWERED) ? 15 : 0;
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState neighborState, boolean powered) {
        if (!powered && !state.is(neighborState.getBlock())) {
            if (state.getValue(POWERED)) {
                this.updateNeighbours(state, world, pos);
            }

            super.onRemove(state, world, pos, neighborState, powered);
        }
    }

    private void updateNeighbours(BlockState state, Level world, BlockPos pos) {
        world.updateNeighborsAt(pos, this);

        if (state.getValue(AXIS) == Direction.Axis.X) {
            world.updateNeighborsAt(pos.east(), this);
            world.updateNeighborsAt(pos.west(), this);
        }

        if (state.getValue(AXIS) == Direction.Axis.Y) {
            world.updateNeighborsAt(pos.above(), this);
            world.updateNeighborsAt(pos.below(), this);
        }

        if (state.getValue(AXIS) == Direction.Axis.X) {
            world.updateNeighborsAt(pos.north(), this);
            world.updateNeighborsAt(pos.south(), this);
        }
    }

    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block block, BlockPos neighborPos, boolean b) {
        if (!world.isClientSide) {
            boolean flag = state.getValue(POWERED);
            if (flag != world.hasNeighborSignal(pos)) {
                if (flag) {
                    world.scheduleTick(pos, this, 4);
                } else {
                    world.setBlock(pos, state.cycle(POWERED), 2);
                }
            }
        }
    }

    public void tick(BlockState state, ServerLevel serverWorld, BlockPos pos, RandomSource source) {
        if (state.getValue(POWERED) && !serverWorld.hasNeighborSignal(pos)) {
            serverWorld.setBlock(pos, state.cycle(POWERED), 2);
        }
    }

    public boolean isSignalSource(BlockState state) {
        return false;
    }
}
