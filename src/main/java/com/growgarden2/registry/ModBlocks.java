package com.growgarden2.registry;

import com.growgarden2.GrowGarden2;
import com.growgarden2.blocks.AreaMarkerBlock;
import com.growgarden2.blocks.PlantBlock;
import com.growgarden2.blocks.PlantPartBlock;
import com.growgarden2.plants.PlantKind;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
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

    private static BlockBehaviour.Properties plantProperties() {
        return BlockBehaviour.Properties.of().strength(0.2F).noCollission().noOcclusion()
                .sound(SoundType.CROP);
    }

    private static BlockBehaviour.Properties partProperties() {
        return BlockBehaviour.Properties.of().strength(0.2F).noCollission().noOcclusion()
                .noDrops().sound(SoundType.CROP);
    }

    public static final RegistryObject<Block> GARDEN_MARKER =
            BLOCKS.register("garden_marker", () -> new AreaMarkerBlock(markerProperties()));
    public static final RegistryObject<Block> SHOP_MARKER =
            BLOCKS.register("shop_marker", () -> new AreaMarkerBlock(markerProperties()));
    public static final RegistryObject<Block> SALES_MARKER =
            BLOCKS.register("sales_marker", () -> new AreaMarkerBlock(markerProperties()));

    public static final RegistryObject<PlantBlock> CARROT_PLANT =
            BLOCKS.register("carrot_plant", () -> new PlantBlock(plantProperties(), PlantKind.CARROT));
    public static final RegistryObject<PlantBlock> TOMATO_PLANT =
            BLOCKS.register("tomato_plant", () -> new PlantBlock(plantProperties(), PlantKind.TOMATO));
    public static final RegistryObject<PlantBlock> DRAGON_FRUIT_PLANT =
            BLOCKS.register("dragon_fruit_plant", () -> new PlantBlock(plantProperties(), PlantKind.DRAGON_FRUIT));

    public static final RegistryObject<Block> CARROT_LEAF =
            BLOCKS.register("carrot_leaf", () -> new PlantPartBlock(partProperties()));
    public static final RegistryObject<Block> TOMATO_LEAF =
            BLOCKS.register("tomato_leaf", () -> new PlantPartBlock(partProperties()));
    public static final RegistryObject<Block> TOMATO_FRUIT =
            BLOCKS.register("tomato_fruit", () -> new PlantPartBlock(partProperties()));
    public static final RegistryObject<Block> DRAGON_STEM =
            BLOCKS.register("dragon_stem", () -> new PlantPartBlock(partProperties()));
    public static final RegistryObject<Block> DRAGON_FRUIT =
            BLOCKS.register("dragon_fruit", () -> new PlantPartBlock(partProperties()));

    private ModBlocks() {}
}
