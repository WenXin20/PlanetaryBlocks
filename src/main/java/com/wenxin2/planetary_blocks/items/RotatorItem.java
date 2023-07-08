package com.wenxin2.planetary_blocks.items;

import com.wenxin2.planetary_blocks.blocks.PlanetBlock;
import com.wenxin2.planetary_blocks.init.Config;
import java.util.List;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class RotatorItem extends Item {
    public RotatorItem (Item.Properties properties) {
        super(properties);
    }

    @NotNull
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

    @Override
    @ParametersAreNonnullByDefault
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> list, TooltipFlag tooltip) {
        if (!Config.ENABLE_ROTATION.get()) {
            list.add(Component.translatable("tooltip.planetary_blocks.rotation_disabled").withStyle(ChatFormatting.RED).withStyle(ChatFormatting.ITALIC));
        } else {
            list.add(Component.translatable("tooltip.planetary_blocks.rotation").withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC));
        }
    }
}
