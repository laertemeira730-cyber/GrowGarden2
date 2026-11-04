package com.growgarden2.registry;

import com.growgarden2.GrowGarden2;
import com.growgarden2.blocks.AreaMarkerBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, GrowGarden2.MOD_ID);

    private static BlockBehaviour.Properties markerProperties() {
        return BlockBehaviour.Properties.of().strength(0.5F).noCollission().noOcclusion();
    }

    public static final RegistryObject<Block> GARDEN_MARKER =
            BLOCKS.register("garden_marker", () -> new AreaMarkerBlock(markerProperties()));
    public static final RegistryObject<Block> SHOP_MARKER =
            BLOCKS.register("shop_marker", () -> new AreaMarkerBlock(markerProperties()));
    public static final RegistryObject<Block> SALES_MARKER =
            BLOCKS.register("sales_marker", () -> new AreaMarkerBlock(markerProperties()));

    private ModBlocks() {}
}
