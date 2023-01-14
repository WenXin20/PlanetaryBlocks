package com.wenxin2.planetary_blocks.blocks;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class PlanetBlock extends RotatedPillarBlock
{
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final BooleanProperty ROTATION = BooleanProperty.create("rotation");
    public int getRotatationDuration() {
        return this.rotationDuration;
    }
    public int rotationDuration;

    public PlanetBlock(BlockBehaviour.Properties properties, Direction.Axis direction, int rotationDuration)
    {
        super(properties);
        this.rotationDuration = rotationDuration;
        this.registerDefaultState(this.defaultBlockState().setValue(AXIS, direction)
                .setValue(POWERED, Boolean.FALSE).setValue(ROTATION, Boolean.FALSE));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateBuilder) {
        stateBuilder.add(AXIS, POWERED, ROTATION);
    }

//    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult)
//    {
//        if (state.getValue(ROTATION))
//        {
//            return InteractionResult.CONSUME;
//        }
//        else if (!player.isShiftKeyDown()) {
//            this.rotate(state, world, pos);
//            this.playSound(player, world, pos, true);
//            world.gameEvent(player, GameEvent.BLOCK_ACTIVATE, pos);
//            return InteractionResult.sidedSuccess(world.isClientSide);
//        }
//        return InteractionResult.PASS;
//    }
//
//    public void rotate(BlockState state, Level world, BlockPos pos)
//    {
//        world.setBlock(pos, state.setValue(ROTATION, Boolean.valueOf(true)), 3);
//        this.updateNeighbours(state, world, pos);
//        world.scheduleTick(pos, this, this.getRotatationDuration());
//    }

    protected void playSound(@Nullable Player player, LevelAccessor world, BlockPos pos, boolean playSound)
    {
        world.playSound(playSound ? player : null, pos, this.getSound(playSound), SoundSource.BLOCKS, 0.3F, playSound ? 0.6F : 0.5F);
    }

    protected SoundEvent getSound(boolean playSound)
    {
        return SoundEvents.PLAYER_ATTACK_SWEEP;
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity entity, ItemStack stack)
    {
        this.updateRotation(state, world, pos);
    }

    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block neighborBlock, BlockPos pos2, boolean rotation)
    {
        this.updateRotation(state, world, pos);
        super.neighborChanged(state, world, pos, neighborBlock, pos, rotation);
//        if (!world.isClientSide)
//        {
//            boolean flag = state.getValue(POWERED);
//            if (flag != world.hasNeighborSignal(pos))
//            {
//                if (flag) {
//                    world.scheduleTick(pos, this, 4);
//                } else {
//                    world.setBlock(pos, state.cycle(POWERED).cycle(ROTATION), 2);
//                }
////                world.setBlock(pos, state.setValue(ROTATION, Boolean.valueOf(false)), 2);
//            }
//
//            if (world.getBlockState(pos).getValue(POWERED))
//            {
//                if (world.getBlockState(pos.above()).getBlock() instanceof PlanetBlock) {
//                    Direction.Axis axisAbove = world.getBlockState(pos.above()).getValue(AXIS);
//                    world.setBlock(pos.above(), state.setValue(ROTATION, Boolean.TRUE).setValue(AXIS, axisAbove), 3);
//                }
//                if (world.getBlockState(pos.below()).getBlock() instanceof PlanetBlock) {
//                    Direction.Axis axisBelow = world.getBlockState(pos.below()).getValue(AXIS);
//                    world.setBlock(pos.below(), state.setValue(ROTATION, Boolean.TRUE).setValue(AXIS, axisBelow), 3);
//                }
//                if (world.getBlockState(pos.north()).getBlock() instanceof PlanetBlock) {
//                    Direction.Axis axisNorth = world.getBlockState(pos.north()).getValue(AXIS);
//                    world.setBlock(pos.north(), state.setValue(ROTATION, Boolean.TRUE).setValue(AXIS, axisNorth), 3);
//                }
//                if (world.getBlockState(pos.south()).getBlock() instanceof PlanetBlock) {
//                    Direction.Axis axisSouth = world.getBlockState(pos.south()).getValue(AXIS);
//                    world.setBlock(pos.south(), state.setValue(ROTATION, Boolean.TRUE).setValue(AXIS, axisSouth), 3);
//                }
//                if (world.getBlockState(pos.east()).getBlock() instanceof PlanetBlock) {
//                    Direction.Axis axisEast = world.getBlockState(pos.east()).getValue(AXIS);
//                    world.setBlock(pos.east(), state.setValue(ROTATION, Boolean.TRUE).setValue(AXIS, axisEast), 3);
//                }
//                if (world.getBlockState(pos.west()).getBlock() instanceof PlanetBlock) {
//                    Direction.Axis axisWest = world.getBlockState(pos.west()).getValue(AXIS);
//                    world.setBlock(pos.west(), state.setValue(ROTATION, Boolean.TRUE).setValue(AXIS, axisWest), 3);
//                }
//            }
//        }
    }

    public void updateRotation(BlockState state, Level world, BlockPos pos)
    {
        if (!world.isClientSide)
        {
            Direction.Axis axis = world.getBlockState(pos).getValue(AXIS);
            Direction.Axis axisAbove = world.getBlockState(pos).getValue(AXIS);
            boolean flag = state.getValue(POWERED);
            if (world.getBlockState(pos).getValue(ROTATION) && world.getBlockState(pos.above()).getBlock() instanceof PlanetBlock)
            {
                world.setBlock(pos.above(), state.cycle(ROTATION).setValue(AXIS, axisAbove), 3);
            }
            else if (world.getBlockState(pos.above()).getBlock() instanceof PlanetBlock)
            {
                world.setBlock(pos.above(), state.cycle(ROTATION).setValue(AXIS, axisAbove), 3);
            }

            if (flag != world.hasNeighborSignal(pos))
            {
                if (flag)
                {
                    world.scheduleTick(pos, this, 4);
                    world.setBlock(pos, state.cycle(ROTATION).cycle(POWERED).setValue(AXIS, axis), 3);

                    if (world.getBlockState(pos.above()).getBlock() instanceof PlanetBlock)
                    world.setBlock(pos.above(), state.setValue(ROTATION, Boolean.FALSE).setValue(POWERED, Boolean.FALSE).setValue(AXIS, axisAbove), 3);
                }
                else {
                    world.setBlock(pos, state.cycle(ROTATION).cycle(POWERED).setValue(AXIS, axis), 3);

                    if (world.getBlockState(pos.above()).getBlock() instanceof PlanetBlock)
                    world.setBlock(pos.above(), state.setValue(ROTATION, Boolean.TRUE).setValue(POWERED, Boolean.TRUE).setValue(AXIS, axisAbove), 3);
                }
            }
        }
    }

//    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState state1, boolean b)
//    {
//        if (!state1.is(state.getBlock()))
//        {
//            this.updateState(state, level, pos, b);
//        }
//    }

//    public BlockState updateState(BlockState state, Level level, BlockPos pos, boolean b) {
//        state = this.updateRotation(level, pos, state, true);
//        if (!level.isClientSide)
//        {
//            boolean flag = state.getValue(POWERED);
//            if (flag != level.hasNeighborSignal(pos))
//            {
//                if (flag)
//                {
//                    level.scheduleTick(pos, this, 4);
//                }
//                else {
//                    level.setBlock(pos, state.cycle(POWERED).cycle(ROTATION), 2);
//                }
////                world.setBlock(pos, state.setValue(ROTATION, Boolean.valueOf(false)), 2);
//            }
//        }
//        return state;
//    }
//
//    public BlockState updateRotation(Level level, BlockPos pos, BlockState state, boolean b)
//    {
//        if (level.isClientSide)
//        {
//            return state;
//        }
//        else {
//            Direction.Axis axis = state.getValue(AXIS);
//            return this.defaultBlockState().setValue(AXIS, axis);
//        }
//    }

    public BlockState getStateForPlacement(BlockPlaceContext pos)
    {
//        BlockState blockstate = this.defaultBlockState();
//
//        if (pos.getLevel().hasNeighborSignal(pos.getClickedPos())) {
//            blockstate.setValue(POWERED, Boolean.valueOf(true)).setValue(ROTATION, Boolean.valueOf(true));
//        }

        return this.defaultBlockState().setValue(AXIS, pos.getClickedFace().getAxis())
                .setValue(POWERED, pos.getLevel().hasNeighborSignal(pos.getClickedPos()))
                .setValue(ROTATION, pos.getLevel().hasNeighborSignal(pos.getClickedPos()));
//        return this.updateState(pos.getLevel().getBlockState(pos.getClickedPos()), pos.getLevel(), pos.getLevel()., pos.getLevel().getBlockState(pos.getClickedPos()).getBlock());
    }

//    public void tick(BlockState state, ServerLevel server, BlockPos pos, RandomSource random)
//    {
//        if (state.getValue(ROTATION) /*&& !server.hasNeighborSignal(pos)*/)
//        {
////            this.checkRotation(state, server, pos);
////            server.setBlock(pos, state.setValue(ROTATION, Boolean.valueOf(false)), 3);
//            server.setBlock(pos, state.cycle(POWERED).cycle(ROTATION), 2);
////            this.updateNeighbours(state, server, pos);
//            this.playSound((Player)null, server, pos, false);
//        }
//    }

//    private BlockState updateShape(LevelReader world, BlockState state, BlockPos pos, BlockState state2, boolean flag, boolean flag1, boolean flag2, boolean flag3) {
//        BlockState blockstate = this.updateSides(state, flag);
//        return blockstate.setValue(ROTATION, Boolean.valueOf(true));
//    }
//
//    public BlockState updateShape(BlockState state, Direction direction, BlockState state2, LevelAccessor world, BlockPos pos, BlockPos pos2) {
//        if (state.getValue(ROTATION)) {
//            return super.updateShape(state, direction, state2, world, pos, pos2);
//        } else {
//            return state.getValue(ROTATION) ? this.topUpdate(world, state, pos2, state2) : this.sideUpdate(world, pos, state, pos2, state2, direction);
//        }
//    }

//    private BlockState updateSides(BlockState state, boolean flag)
//    {
//        return state.setValue(ROTATION, Boolean.valueOf(true));
//    }
//
//    private BlockState topUpdate(LevelReader world, BlockState state, BlockPos pos, BlockState state2)
//    {
//        boolean flag = state.getValue(ROTATION);
//        boolean flag1 = state.getValue(ROTATION);
//        boolean flag2 = state.getValue(ROTATION);
//        boolean flag3 = state.getValue(ROTATION);
//        return this.updateShape(world, state, pos, state2, flag, flag1, flag2, flag3);
//    }
//
//    private BlockState sideUpdate(LevelReader world, BlockPos pos, BlockState state, BlockPos state2, BlockState state3, Direction direction)
//    {
//        boolean flag = state.getValue(ROTATION);
//        boolean flag1 = state.getValue(ROTATION);
//        boolean flag2 = state.getValue(ROTATION);
//        boolean flag3 = state.getValue(ROTATION);
//        BlockPos blockpos = pos.above();
//        BlockState blockstate = world.getBlockState(blockpos);
//        return this.updateShape(world, state, blockpos, blockstate, flag, flag1, flag2, flag3);
//    }

//    private void checkRotation(BlockState state, Level world, BlockPos pos)
//    {
//        boolean flag = state.getValue(ROTATION);
//        if (flag) {
//            world.setBlock(pos, state.setValue(ROTATION, Boolean.valueOf(flag)), 3);
//            this.updateNeighbours(state, world, pos);
//            this.playSound((Player)null, world, pos, flag);
//        }
//
//        if (flag) {
//            world.scheduleTick(new BlockPos(pos), this, this.getRotatationDuration());
//        }
//
//    }

//    protected static Direction getConnectedDirection(BlockState p_53201_) {
//        switch ((AttachFace)p_53201_.getValue(FACE)) {
//            case CEILING:
//                return Direction.DOWN;
//            case FLOOR:
//                return Direction.UP;
//            default:
//                return p_53201_.getValue(FACING);
//        }
//    }
}
