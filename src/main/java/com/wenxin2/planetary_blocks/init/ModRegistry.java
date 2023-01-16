package com.wenxin2.planetary_blocks.init;

import com.wenxin2.planetary_blocks.PlanetaryBlocks;
import com.wenxin2.planetary_blocks.blocks.PlanetBlock;
import java.util.function.Supplier;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = PlanetaryBlocks.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModRegistry {

    public static final RegistryObject<Block> MARS;

    static
    {
        MARS = registerBlock("mars_block",
                () -> new PlanetBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_RED)
                        .sound(SoundType.STONE).strength(1.5F, 6.0F)
                        .requiresCorrectToolForDrops(), Direction.Axis.Y), PlanetaryBlocks.CREATIVE_TAB);
    }

    public static RegistryObject<Block> registerBlock(String name, Supplier<? extends Block> block, CreativeModeTab tab)
    {
        RegistryObject<Block> blocks = PlanetaryBlocks.BLOCKS.register(name, block);
        PlanetaryBlocks.ITEMS.register(name, () -> new BlockItem(blocks.get(), new Item.Properties().tab(tab)));
        return blocks;
    }
}
