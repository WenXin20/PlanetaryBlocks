package com.wenxin2.planetary_blocks.items;

import com.wenxin2.planetary_blocks.blocks.PlanetBlock;
import com.wenxin2.planetary_blocks.init.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class RotatorItem extends Item {
    public RotatorItem (Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = world.getBlockState(pos);

        if (!world.isClientSide && player != null && state.getBlock() instanceof PlanetBlock) {
            if (state.hasProperty(PlanetBlock.ROTATION) && Config.ENABLE_ROTATION.get())
            {
                world.setBlock(pos, state.cycle(PlanetBlock.ROTATION), 4);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.FAIL;
    }

}
