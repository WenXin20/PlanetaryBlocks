package com.wenxin2.planetary_blocks.blocks;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.VoxelShape;

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
        this.registerDefaultState(this.defaultBlockState().setValue(RotatedPillarBlock.AXIS, direction)
                .setValue(POWERED, Boolean.valueOf(false)).setValue(ROTATION, Boolean.valueOf(false)));
    }

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

    private void updateNeighbours(BlockState state, Level world, BlockPos pos)
    {
        if (state.getValue(ROTATION))
        {
            world.updateNeighborsAt(pos, this);
//        world.updateNeighborsAt(pos.relative(getConnectedDirection(state).getOpposite()), this);
        }
    }

    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block block, BlockPos pos2, boolean rotation)
    {
        if (!world.isClientSide)
        {
            boolean flag = state.getValue(POWERED);
            if (flag != world.hasNeighborSignal(pos))
            {
                if (flag) {
                    world.scheduleTick(pos, this, 4);
                } else {
                    world.setBlock(pos, state.cycle(POWERED).cycle(ROTATION), 2);
                }
                world.setBlock(pos, state.setValue(ROTATION, Boolean.valueOf(false)), 2);
            }
        }
    }

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
    }

    public void tick(BlockState state, ServerLevel server, BlockPos pos, RandomSource random)
    {
        if (state.getValue(ROTATION) && !server.hasNeighborSignal(pos))
        {
//            this.checkRotation(state, server, pos);
//            server.setBlock(pos, state.setValue(ROTATION, Boolean.valueOf(false)), 3);
            server.setBlock(pos, state.cycle(POWERED).cycle(ROTATION), 2);
            this.updateNeighbours(state, server, pos);
            this.playSound((Player)null, server, pos, false);
        }
    }

    private BlockState updateShape(LevelReader world, BlockState state, BlockPos pos, BlockState state2, boolean flag, boolean flag1, boolean flag2, boolean flag3) {
        BlockState blockstate = this.updateSides(state, flag);
        return blockstate.setValue(ROTATION, Boolean.valueOf(true));
    }

    public BlockState updateShape(BlockState state, Direction direction, BlockState state2, LevelAccessor world, BlockPos pos, BlockPos pos2) {
        if (state.getValue(ROTATION)) {
            return super.updateShape(state, direction, state2, world, pos, pos2);
        } else {
            return state.getValue(ROTATION) ? this.topUpdate(world, state, pos2, state2) : this.sideUpdate(world, pos, state, pos2, state2, direction);
        }
    }

    private BlockState updateSides(BlockState state, boolean flag)
    {
        return state.setValue(ROTATION, Boolean.valueOf(true));
    }

    private BlockState topUpdate(LevelReader world, BlockState state, BlockPos pos, BlockState state2)
    {
        boolean flag = state.getValue(ROTATION);
        boolean flag1 = state.getValue(ROTATION);
        boolean flag2 = state.getValue(ROTATION);
        boolean flag3 = state.getValue(ROTATION);
        return this.updateShape(world, state, pos, state2, flag, flag1, flag2, flag3);
    }

    private BlockState sideUpdate(LevelReader world, BlockPos pos, BlockState state, BlockPos state2, BlockState state3, Direction direction)
    {
        boolean flag = state.getValue(ROTATION);
        boolean flag1 = state.getValue(ROTATION);
        boolean flag2 = state.getValue(ROTATION);
        boolean flag3 = state.getValue(ROTATION);
        BlockPos blockpos = pos.above();
        BlockState blockstate = world.getBlockState(blockpos);
        return this.updateShape(world, state, blockpos, blockstate, flag, flag1, flag2, flag3);
    }

    private void checkRotation(BlockState state, Level world, BlockPos pos)
    {
        boolean flag = state.getValue(ROTATION);
        if (flag) {
            world.setBlock(pos, state.setValue(ROTATION, Boolean.valueOf(flag)), 3);
            this.updateNeighbours(state, world, pos);
            this.playSound((Player)null, world, pos, flag);
        }

        if (flag) {
            world.scheduleTick(new BlockPos(pos), this, this.getRotatationDuration());
        }

    }

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
