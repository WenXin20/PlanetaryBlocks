package com.wenxin2.planetary_blocks.blocks;

import com.wenxin2.planetary_blocks.init.Config;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.NotNull;

public class EarthBlock extends HorizontalDirectionalBlock
{
    public static final EnumProperty<ColumnBlockStates> COLUMN = EnumProperty.create("column", ColumnBlockStates.class);
    public static final IntegerProperty POWERED = BlockStateProperties.POWER;
    public static final BooleanProperty ROTATION = BooleanProperty.create("rotation");
    public static final BooleanProperty NIGHT = BooleanProperty.create("night");

    public final boolean spawnParticles;

    public EarthBlock(Properties properties, boolean spawnParticles)
    {
        super(properties);
        this.spawnParticles = spawnParticles;
        this.registerDefaultState(this.getStateDefinition().any().setValue(NIGHT, Boolean.FALSE)
                .setValue(POWERED, 0).setValue(ROTATION, Boolean.FALSE).setValue(COLUMN, ColumnBlockStates.NONE));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateBuilder)
    {
        stateBuilder.add(COLUMN, FACING, NIGHT, POWERED, ROTATION);
    }

    @NotNull
    @Override
    @ParametersAreNonnullByDefault
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos)
    {
        super.updateShape(state, direction, neighborState, world, pos, neighborPos);

        Block blockAbove = world.getBlockState(pos.above()).getBlock();
        Block blockBelow = world.getBlockState(pos.below()).getBlock();

        if (blockAbove == this)
        {
            if (blockBelow == this)
                return state.setValue(COLUMN, ColumnBlockStates.MIDDLE);
            return state.setValue(COLUMN, ColumnBlockStates.BOTTOM);
        }
        if (blockBelow == this)
            return state.setValue(COLUMN, ColumnBlockStates.TOP);

        return state.setValue(COLUMN, ColumnBlockStates.NONE);
    }

    @NotNull
    @Override
    @ParametersAreNonnullByDefault
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity entity, ItemStack stack)
    {
        this.updateRedstone(state, world, pos);
    }

    @NotNull
    @Override
    @ParametersAreNonnullByDefault
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block neighborBlock, BlockPos pos2, boolean rotation)
    {
        this.updateRedstone(state, world, pos);
        super.neighborChanged(state, world, pos, neighborBlock, pos, rotation);
    }

    @NotNull
    @Override
    @ParametersAreNonnullByDefault
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource source)
    {
        if (Config.earth_night.get() && !Config.forever_earth_night.get())
            world.setBlock(pos, state.setValue(NIGHT, world.isNight()), 3);
        if (!Config.earth_night.get() && !Config.forever_earth_night.get() && world.getBlockState(pos).getValue(NIGHT) == Boolean.TRUE)
        {
            world.setBlock(pos, state.setValue(NIGHT, Boolean.FALSE), 3);
        }
        if (Config.forever_earth_night.get() && world.getBlockState(pos).getValue(NIGHT) == Boolean.FALSE)
        {
            world.setBlock(pos, state.setValue(NIGHT, Boolean.TRUE), 3);
        }
    }

    public void updateRedstone(BlockState state, Level world, BlockPos pos)
    {
        if (!world.isClientSide)
        {
            int power = world.getBestNeighborSignal(pos);
            world.setBlock(pos, state.setValue(POWERED, Mth.clamp(power, 0, 15)), 1 | 2 | 4);
            world.scheduleTick(pos, this, 4);
            this.updateRotation(state, world, pos);
//            world.playSound(null, pos, SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.BLOCKS, 5.25F, 0.05F);
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
                world.setBlock(pos, state.setValue(ROTATION, Config.enable_rotation.get()).setValue(POWERED, Mth.clamp(bestSignal, 0, 15)), 4);
            }
            else {
                world.scheduleTick(pos, this, 4);
                world.setBlock(pos, state.setValue(ROTATION, Boolean.FALSE).setValue(POWERED, 0), 4);
            }
            if (!Config.enable_rotation.get() && world.getBlockState(pos).getValue(ROTATION) == Boolean.TRUE)
            {
                world.setBlock(pos, state.setValue(ROTATION, Boolean.FALSE).setValue(POWERED, 0), 4);
            }
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        boolean night = false;
        boolean rotation = false;
        if (Config.forever_earth_night.get() || Config.earth_night.get())
        {
            night = Config.forever_earth_night.get();
            rotation = Config.enable_rotation.get();
        }
        else if (Config.earth_night.get())
            context.getLevel().isNight();
        else context.getLevel().hasNeighborSignal(context.getClickedPos());

//        if (!Config.enable_rotation.get())
//        {
//            rotation = Config.enable_rotation.get();
//        }

        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite())
                .setValue(COLUMN, ColumnBlockStates.NONE).setValue(NIGHT, night)
                .setValue(ROTATION, rotation);
    }

    @NotNull
    @Override
    @ParametersAreNonnullByDefault
    public int getSignal(BlockState state, BlockGetter block, BlockPos pos, Direction side)
    {
        return Math.max(0, state.getValue(POWERED) - 1);
    }
}
