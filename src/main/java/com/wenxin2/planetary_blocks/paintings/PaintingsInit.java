package com.wenxin2.planetary_blocks.paintings;

import com.wenxin2.planetary_blocks.PlanetaryBlocks;
import net.minecraft.world.entity.decoration.PaintingVariant;

public class PaintingsInit
{
    public static PaintingVariant createPainting(String id, int width, int height)
    {
        final PaintingVariant painting = new PaintingVariant(width, height);
        return painting;
    }

    public static void init()
    {
        PlanetaryBlocks.PAINTINGS.register("earth_map", () -> createPainting("earth_map", 64, 32));
    }
}
