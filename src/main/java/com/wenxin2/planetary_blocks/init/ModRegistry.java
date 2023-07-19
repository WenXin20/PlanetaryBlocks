package com.wenxin2.planetary_blocks.init;

import com.wenxin2.planetary_blocks.PlanetaryBlocks;
import com.wenxin2.planetary_blocks.blocks.ClassicMoonBlock;
import com.wenxin2.planetary_blocks.blocks.ClassicSunBlock;
import com.wenxin2.planetary_blocks.blocks.EarthBlock;
import com.wenxin2.planetary_blocks.blocks.MoonBlock;
import com.wenxin2.planetary_blocks.blocks.OxidizablePanelBlock;
import com.wenxin2.planetary_blocks.blocks.OxidizablePedestalBlock;
import com.wenxin2.planetary_blocks.blocks.PedestalBlock;
import com.wenxin2.planetary_blocks.blocks.PlanetBlock;
import com.wenxin2.planetary_blocks.blocks.SunBlock;
import com.wenxin2.planetary_blocks.items.RotatorItem;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = PlanetaryBlocks.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModRegistry {

    public static final RegistryObject<Block> CLASSIC_MOON;
    public static final RegistryObject<Block> CLASSIC_SUN;
    public static final RegistryObject<Block> DARK_SIDE_MOON;
    public static final RegistryObject<Block> EARTH;
    public static final RegistryObject<Block> LIGHT_SIDE_MOON;
    public static final RegistryObject<Block> MARS;
    public static final RegistryObject<Block> MERCURY;
    public static final RegistryObject<Block> MOON;
    public static final RegistryObject<Block> SUN;
    public static final RegistryObject<Block> VENUS;

    public static final RegistryObject<Block> COPPER_PANEL;
    public static final RegistryObject<Block> COPPER_PEDESTAL;
    public static final RegistryObject<Block> DIAMOND_PANEL;
    public static final RegistryObject<Block> DIAMOND_PEDESTAL;
    public static final RegistryObject<Block> EXPOSED_COPPER_PANEL;
    public static final RegistryObject<Block> EXPOSED_COPPER_PEDESTAL;
    public static final RegistryObject<Block> GOLD_PANEL;
    public static final RegistryObject<Block> GOLD_PEDESTAL;
    public static final RegistryObject<Block> IRON_BARS_PEDESTAL;
    public static final RegistryObject<Block> IRON_PANEL;
    public static final RegistryObject<Block> IRON_PEDESTAL;
    public static final RegistryObject<Block> NETHERITE_PANEL;
    public static final RegistryObject<Block> NETHERITE_PEDESTAL;
    public static final RegistryObject<Block> OXIDIZED_COPPER_PANEL;
    public static final RegistryObject<Block> OXIDIZED_COPPER_PEDESTAL;
    public static final RegistryObject<Block> WAXED_COPPER_PANEL;
    public static final RegistryObject<Block> WAXED_EXPOSED_COPPER_PANEL;
    public static final RegistryObject<Block> WAXED_OXIDIZED_COPPER_PANEL;
    public static final RegistryObject<Block> WAXED_WEATHERED_COPPER_PANEL;
    public static final RegistryObject<Block> WAXED_COPPER_PEDESTAL;
    public static final RegistryObject<Block> WAXED_EXPOSED_COPPER_PEDESTAL;
    public static final RegistryObject<Block> WAXED_OXIDIZED_COPPER_PEDESTAL;
    public static final RegistryObject<Block> WAXED_WEATHERED_COPPER_PEDESTAL;
    public static final RegistryObject<Block> WEATHERED_COPPER_PANEL;
    public static final RegistryObject<Block> WEATHERED_COPPER_PEDESTAL;

    public static final RegistryObject<Item> PLANET_ROTATOR;

    static
    {
        PLANET_ROTATOR = registerItem("planet_rotator",
                () -> new RotatorItem(new Item.Properties().durability(128).tab(PlanetaryBlocks.CREATIVE_TAB), 1));

        CLASSIC_SUN = registerBlock("classic_sun",
                () -> new ClassicSunBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_YELLOW)
                        .sound(SoundType.STONE).strength(3.5F, 100.0F).lightLevel(s -> 15)
                        .requiresCorrectToolForDrops().emissiveRendering(ModRegistry::always), Boolean.TRUE), PlanetaryBlocks.CREATIVE_TAB);
        CLASSIC_MOON = registerFoodBlock("classic_moon",
                () -> new ClassicMoonBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.SNOW)
                        .sound(SoundType.STONE).strength(3.5F, 100.0F).lightLevel(s -> 3).randomTicks()
                        .requiresCorrectToolForDrops(), Boolean.FALSE), FoodRegistry.MOON, PlanetaryBlocks.CREATIVE_TAB);
        SUN = registerBlock("sun_block",
                () -> new SunBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_YELLOW)
                        .sound(SoundType.STONE).strength(3.5F, 100.0F).lightLevel(s -> 15)
                        .requiresCorrectToolForDrops().emissiveRendering(ModRegistry::always), Direction.Axis.Y, Boolean.TRUE), PlanetaryBlocks.CREATIVE_TAB);
        MERCURY = registerBlock("mercury_block",
                () -> new PlanetBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_LIGHT_GRAY)
                        .sound(SoundType.STONE).strength(1.5F, 6.0F)
                        .requiresCorrectToolForDrops(), Direction.Axis.Y, Boolean.FALSE), PlanetaryBlocks.CREATIVE_TAB);
        VENUS = registerBlock("venus_block",
                () -> new PlanetBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.TERRACOTTA_YELLOW)
                        .sound(SoundType.STONE).strength(1.5F, 6.0F)
                        .requiresCorrectToolForDrops(), Direction.Axis.Y, Boolean.FALSE), PlanetaryBlocks.CREATIVE_TAB);
        EARTH = registerBlock("earth_block",
                () -> new EarthBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_BLUE)
                        .sound(SoundType.STONE).strength(1.5F, 6.0F).randomTicks()
                        .requiresCorrectToolForDrops(), Boolean.FALSE), PlanetaryBlocks.CREATIVE_TAB);
        MOON = registerFoodBlock("moon_block",
                () -> new MoonBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_LIGHT_GRAY)
                        .sound(SoundType.STONE).strength(1.0F, 3.0F)
                        .requiresCorrectToolForDrops(), Boolean.FALSE), FoodRegistry.MOON, PlanetaryBlocks.CREATIVE_TAB);
        LIGHT_SIDE_MOON = registerFoodBlock("light_side_moon",
                () -> new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_LIGHT_GRAY)
                        .sound(SoundType.STONE).strength(1.0F, 3.0F)
                        .requiresCorrectToolForDrops()), FoodRegistry.MOON, PlanetaryBlocks.CREATIVE_TAB);
        DARK_SIDE_MOON = registerFoodBlock("dark_side_moon",
                () -> new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_GRAY)
                        .sound(SoundType.STONE).strength(1.0F, 3.0F)
                        .requiresCorrectToolForDrops()), FoodRegistry.MOON, PlanetaryBlocks.CREATIVE_TAB);
        MARS = registerBlock("mars_block",
                () -> new PlanetBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_ORANGE)
                        .sound(SoundType.STONE).strength(1.5F, 6.0F)
                        .requiresCorrectToolForDrops(), Direction.Axis.Y, Boolean.FALSE), PlanetaryBlocks.CREATIVE_TAB);


        IRON_BARS_PEDESTAL = registerBlock("iron_bars_pedestal",
                () -> new PedestalBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.METAL)
                        .sound(SoundType.METAL).strength(5.0F, 6.0F)
                        .requiresCorrectToolForDrops(), Direction.Axis.Y), PlanetaryBlocks.CREATIVE_TAB);
        IRON_PEDESTAL = registerBlock("iron_pedestal",
                () -> new PedestalBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.METAL)
                        .sound(SoundType.METAL).strength(5.0F, 6.0F)
                        .requiresCorrectToolForDrops(), Direction.Axis.Y), PlanetaryBlocks.CREATIVE_TAB);
        GOLD_PEDESTAL = registerBlock("gold_pedestal",
                () -> new PedestalBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.GOLD)
                        .sound(SoundType.METAL).strength(3.0F, 6.0F)
                        .requiresCorrectToolForDrops(), Direction.Axis.Y), PlanetaryBlocks.CREATIVE_TAB);
        DIAMOND_PEDESTAL = registerBlock("diamond_pedestal",
                () -> new PedestalBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.DIAMOND)
                        .sound(SoundType.METAL).strength(5.0F, 6.0F)
                        .requiresCorrectToolForDrops(), Direction.Axis.Y), PlanetaryBlocks.CREATIVE_TAB);
        NETHERITE_PEDESTAL = registerBlock("netherite_pedestal",
                () -> new PedestalBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_BLACK)
                        .sound(SoundType.NETHERITE_BLOCK).strength(50.0F, 1200.0F)
                        .requiresCorrectToolForDrops(), Direction.Axis.Y), PlanetaryBlocks.CREATIVE_TAB);
        COPPER_PEDESTAL = registerBlock("copper_pedestal",
                () -> new OxidizablePedestalBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_ORANGE)
                        .sound(SoundType.COPPER).strength(3.0F, 6.0F)
                        .requiresCorrectToolForDrops(), WeatheringCopper.WeatherState.UNAFFECTED,
                        Direction.Axis.Y), PlanetaryBlocks.CREATIVE_TAB);
        EXPOSED_COPPER_PEDESTAL = registerBlock("exposed_copper_pedestal",
                () -> new OxidizablePedestalBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.TERRACOTTA_LIGHT_GRAY)
                        .sound(SoundType.COPPER).strength(3.0F, 6.0F)
                        .requiresCorrectToolForDrops(), WeatheringCopper.WeatherState.EXPOSED,
                        Direction.Axis.Y), PlanetaryBlocks.CREATIVE_TAB);
        WEATHERED_COPPER_PEDESTAL = registerBlock("weathered_copper_pedestal",
                () -> new OxidizablePedestalBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.WARPED_STEM)
                        .sound(SoundType.COPPER).strength(3.0F, 6.0F)
                        .requiresCorrectToolForDrops(), WeatheringCopper.WeatherState.WEATHERED,
                        Direction.Axis.Y), PlanetaryBlocks.CREATIVE_TAB);
        OXIDIZED_COPPER_PEDESTAL = registerBlock("oxidized_copper_pedestal",
                () -> new OxidizablePedestalBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.WARPED_NYLIUM)
                        .sound(SoundType.COPPER).strength(3.0F, 6.0F)
                        .requiresCorrectToolForDrops(), WeatheringCopper.WeatherState.OXIDIZED,
                        Direction.Axis.Y), PlanetaryBlocks.CREATIVE_TAB);
        WAXED_COPPER_PEDESTAL = registerBlock("waxed_copper_pedestal",
                () -> new PedestalBlock(BlockBehaviour.Properties.copy(COPPER_PEDESTAL.get()),
                        Direction.Axis.Y), PlanetaryBlocks.CREATIVE_TAB);
        WAXED_EXPOSED_COPPER_PEDESTAL = registerBlock("waxed_exposed_copper_pedestal",
                () -> new PedestalBlock(BlockBehaviour.Properties.copy(EXPOSED_COPPER_PEDESTAL.get()),
                        Direction.Axis.Y), PlanetaryBlocks.CREATIVE_TAB);
        WAXED_WEATHERED_COPPER_PEDESTAL = registerBlock("waxed_weathered_copper_pedestal",
                () -> new PedestalBlock(BlockBehaviour.Properties.copy(WEATHERED_COPPER_PEDESTAL.get()),
                        Direction.Axis.Y), PlanetaryBlocks.CREATIVE_TAB);
        WAXED_OXIDIZED_COPPER_PEDESTAL = registerBlock("waxed_oxidized_copper_pedestal",
                () -> new PedestalBlock(BlockBehaviour.Properties.copy(OXIDIZED_COPPER_PEDESTAL.get()),
                        Direction.Axis.Y), PlanetaryBlocks.CREATIVE_TAB);

        IRON_PANEL = registerBlock("iron_panel",
                () -> new IronBarsBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.METAL)
                        .sound(SoundType.METAL).strength(5.0F, 6.0F)
                        .requiresCorrectToolForDrops().noOcclusion()), PlanetaryBlocks.CREATIVE_TAB);
        GOLD_PANEL = registerBlock("gold_panel",
                () -> new IronBarsBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.GOLD)
                        .sound(SoundType.METAL).strength(3.0F, 6.0F)
                        .requiresCorrectToolForDrops().noOcclusion()), PlanetaryBlocks.CREATIVE_TAB);
        DIAMOND_PANEL = registerBlock("diamond_panel",
                () -> new IronBarsBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.DIAMOND)
                        .sound(SoundType.METAL).strength(5.0F, 6.0F)
                        .requiresCorrectToolForDrops().noOcclusion()), PlanetaryBlocks.CREATIVE_TAB);
        NETHERITE_PANEL = registerBlock("netherite_panel",
                () -> new IronBarsBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_BLACK)
                        .sound(SoundType.METAL).strength(50.0F, 1200.0F)
                        .requiresCorrectToolForDrops().noOcclusion()), PlanetaryBlocks.CREATIVE_TAB);
        COPPER_PANEL = registerBlock("copper_panel",
                () -> new OxidizablePanelBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_ORANGE)
                        .sound(SoundType.COPPER).strength(3.0F, 6.0F).requiresCorrectToolForDrops().noOcclusion(),
                        WeatheringCopper.WeatherState.UNAFFECTED), PlanetaryBlocks.CREATIVE_TAB);
        EXPOSED_COPPER_PANEL = registerBlock("exposed_copper_panel",
                () -> new OxidizablePanelBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.TERRACOTTA_LIGHT_GRAY)
                        .sound(SoundType.COPPER).strength(3.0F, 6.0F).requiresCorrectToolForDrops().noOcclusion(),
                        WeatheringCopper.WeatherState.EXPOSED), PlanetaryBlocks.CREATIVE_TAB);
        WEATHERED_COPPER_PANEL = registerBlock("weathered_copper_panel",
                () -> new OxidizablePanelBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.WARPED_STEM)
                        .sound(SoundType.COPPER).strength(3.0F, 6.0F).requiresCorrectToolForDrops().noOcclusion(),
                        WeatheringCopper.WeatherState.WEATHERED), PlanetaryBlocks.CREATIVE_TAB);
        OXIDIZED_COPPER_PANEL = registerBlock("oxidized_copper_panel",
                () -> new OxidizablePanelBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.WARPED_NYLIUM)
                        .sound(SoundType.COPPER).strength(3.0F, 6.0F).requiresCorrectToolForDrops().noOcclusion(),
                        WeatheringCopper.WeatherState.OXIDIZED), PlanetaryBlocks.CREATIVE_TAB);
        WAXED_COPPER_PANEL = registerBlock("waxed_copper_panel",
                () -> new IronBarsBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_ORANGE)
                        .sound(SoundType.COPPER).strength(3.0F, 6.0F)
                        .requiresCorrectToolForDrops().noOcclusion()), PlanetaryBlocks.CREATIVE_TAB);
        WAXED_EXPOSED_COPPER_PANEL = registerBlock("waxed_exposed_copper_panel",
                () -> new IronBarsBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.TERRACOTTA_LIGHT_GRAY)
                        .sound(SoundType.COPPER).strength(3.0F, 6.0F)
                        .requiresCorrectToolForDrops().noOcclusion()), PlanetaryBlocks.CREATIVE_TAB);
        WAXED_WEATHERED_COPPER_PANEL = registerBlock("waxed_weathered_copper_panel",
                () -> new IronBarsBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.WARPED_STEM)
                        .sound(SoundType.COPPER).strength(3.0F, 6.0F)
                        .requiresCorrectToolForDrops().noOcclusion()), PlanetaryBlocks.CREATIVE_TAB);
        WAXED_OXIDIZED_COPPER_PANEL = registerBlock("waxed_oxidized_copper_panel",
                () -> new IronBarsBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.WARPED_NYLIUM)
                        .sound(SoundType.COPPER).strength(3.0F, 6.0F)
                        .requiresCorrectToolForDrops().noOcclusion()), PlanetaryBlocks.CREATIVE_TAB);
    }

    public static RegistryObject<Block> registerBlock(String name, Supplier<? extends Block> block, CreativeModeTab tab)
    {
        RegistryObject<Block> blocks = PlanetaryBlocks.BLOCKS.register(name, block);
        PlanetaryBlocks.ITEMS.register(name, () -> new BlockItem(blocks.get(), new Item.Properties().tab(tab)));
        return blocks;
    }

    public static RegistryObject<Block> registerFoodBlock(String name, Supplier<? extends Block> block, FoodProperties foodProperties, CreativeModeTab tab)
    {
        RegistryObject<Block> blocks = PlanetaryBlocks.BLOCKS.register(name, block);
        PlanetaryBlocks.ITEMS.register(name, () -> new BlockItem(blocks.get(), new Item.Properties().tab(tab).food(foodProperties)));
        return blocks;
    }

    public static RegistryObject<Item> registerItem(String name, Supplier<? extends Item> item)
    {
        return PlanetaryBlocks.ITEMS.register(name, item);
    }

    private static boolean always(BlockState state, BlockGetter block, BlockPos pos)
    {
        return true;
    }
}
