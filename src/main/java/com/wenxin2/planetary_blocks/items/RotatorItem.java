package com.wenxin2.planetary_blocks.items;

import com.wenxin2.planetary_blocks.blocks.EarthBlock;
import com.wenxin2.planetary_blocks.blocks.PlanetBlock;
import com.wenxin2.planetary_blocks.init.Config;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
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
    private final int consumeItemDamage;
    public RotatorItem (Item.Properties properties, int damage) {
        super(properties);
        this.consumeItemDamage = damage;
    }

    @NotNull
    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        InteractionHand hand = Objects.requireNonNull(context.getPlayer()).getUsedItemHand();
        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = world.getBlockState(pos);
        ItemStack item = player.getItemInHand(hand);

        if ((state.getBlock() instanceof PlanetBlock || state.getBlock() instanceof EarthBlock)) {
            if (!world.isClientSide && state.hasProperty(PlanetBlock.ROTATION) && Config.ENABLE_ROTATION.get() && Config.ENABLE_PLANET_ROTATOR.get())
            {
                world.setBlock(pos, state.cycle(PlanetBlock.ROTATION), 4);
                item.hurtAndBreak(this.consumeItemDamage, player, (h) -> {
                    h.broadcastBreakEvent(hand);
                });
                return InteractionResult.SUCCESS;
            }
            world.playSound(player, pos, SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.BLOCKS, 0.5F, 0.75F);
        }
        return InteractionResult.FAIL;
    }

    @Override
    @ParametersAreNonnullByDefault
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> list, TooltipFlag tooltip) {
        if (!Config.ENABLE_ROTATION.get()) {
            list.add(Component.translatable("tooltip.planetary_blocks.rotation_disabled").withStyle(ChatFormatting.RED).withStyle(ChatFormatting.ITALIC));
        } else if (!Config.ENABLE_PLANET_ROTATOR.get()) {
            list.add(Component.translatable("tooltip.planetary_blocks.planet_rotator_disabled").withStyle(ChatFormatting.RED).withStyle(ChatFormatting.ITALIC));
        } else {
            list.add(Component.translatable("tooltip.planetary_blocks.rotation").withStyle(ChatFormatting.GREEN).withStyle(ChatFormatting.ITALIC));
        }
    }
}
