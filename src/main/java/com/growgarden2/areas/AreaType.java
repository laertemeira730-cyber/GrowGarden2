package com.growgarden2.areas;

import com.growgarden2.registry.ModBlocks;
import net.minecraft.world.level.block.Block;

public enum AreaType {
    GARDEN("Jardim", ModBlocks.GARDEN_MARKER.get()),
    SHOP("Loja", ModBlocks.SHOP_MARKER.get()),
    SALES("Vendas", ModBlocks.SALES_MARKER.get());

    private final String displayName;
    private final Block markerBlock;

    AreaType(String displayName, Block markerBlock) {
        this.displayName = displayName;
        this.markerBlock = markerBlock;
    }

    public String displayName() { return displayName; }
    public boolean matches(Block block) { return markerBlock == block; }

    public static AreaType fromBlock(Block block) {
        for (AreaType type : values()) {
            if (type.matches(block)) return type;
        }
        return null;
    }
}
