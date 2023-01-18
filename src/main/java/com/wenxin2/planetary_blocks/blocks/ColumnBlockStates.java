package com.wenxin2.planetary_blocks.blocks;

import net.minecraft.util.StringRepresentable;

public enum ColumnBlockStates implements StringRepresentable
{
    TOP,
    MIDDLE,
    BOTTOM,
    NONE;

    public String toString()
    {
        return this.getSerializedName();
    }

    public String getSerializedName()
    {
        if (this == TOP)
            return "top";
        else if (this == MIDDLE)
            return "middle";
        else if (this == BOTTOM)
            return "bottom";
        return "none";
    }
}
