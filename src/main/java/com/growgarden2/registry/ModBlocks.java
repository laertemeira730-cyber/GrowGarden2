package com.growgarden2.registry;

import java.util.List;
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
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, GrowGarden2.MOD_ID);
    private static BlockBehaviour.Properties markerProperties() {
        return BlockBehaviour.Properties.of().strength(0.5F).noCollission().noOcclusion();
    }
    private static BlockBehaviour.Properties plantProperties() {
        return BlockBehaviour.Properties.of().strength(0.2F).noCollission().noOcclusion().sound(SoundType.CROP);
    }
    private static BlockBehaviour.Properties partProperties() {
        return BlockBehaviour.Properties.of().strength(0.2F).noCollission().noOcclusion().sound(SoundType.CROP);
    }

    public static final RegistryObject<Block> GARDEN_MARKER = BLOCKS.register("garden_marker", () -> new AreaMarkerBlock(markerProperties()));
    public static final RegistryObject<Block> SHOP_MARKER = BLOCKS.register("shop_marker", () -> new AreaMarkerBlock(markerProperties()));
    public static final RegistryObject<Block> SALES_MARKER = BLOCKS.register("sales_marker", () -> new AreaMarkerBlock(markerProperties()));

    public static final RegistryObject<PlantBlock> MOON_BLOOM_PLANT = plant("moon_bloom_plant", PlantKind.MOON_BLOOM);
    public static final RegistryObject<PlantBlock> DRAGONS_BREATH_PLANT = plant("dragons_breath_plant", PlantKind.DRAGONS_BREATH);
    public static final RegistryObject<PlantBlock> GHOST_PEPPER_PLANT = plant("ghost_pepper_plant", PlantKind.GHOST_PEPPER);
    public static final RegistryObject<PlantBlock> POISON_IVY_PLANT = plant("poison_ivy_plant", PlantKind.POISON_IVY);
    public static final RegistryObject<PlantBlock> MUSHROOM_PLANT = plant("mushroom_plant", PlantKind.MUSHROOM);
    public static final RegistryObject<PlantBlock> VENUS_FLYTRAP_PLANT = plant("venus_flytrap_plant", PlantKind.VENUS_FLYTRAP);
    public static final RegistryObject<PlantBlock> POMEGRANATE_PLANT = plant("pomegranate_plant", PlantKind.POMEGRANATE);
    public static final RegistryObject<PlantBlock> DRAGON_FRUIT_PLANT = plant("dragon_fruit_plant", PlantKind.DRAGON_FRUIT);
    public static final RegistryObject<PlantBlock> SUNFLOWER_PLANT = plant("sunflower_plant", PlantKind.SUNFLOWER);
    public static final RegistryObject<PlantBlock> BAMBOO_PLANT = plant("bamboo_plant", PlantKind.BAMBOO);
    public static final RegistryObject<PlantBlock> MANGO_PLANT = plant("mango_plant", PlantKind.MANGO);
    public static final RegistryObject<PlantBlock> PLUM_PLANT = plant("plum_plant", PlantKind.PLUM);
    public static final RegistryObject<PlantBlock> APPLE_PLANT = plant("apple_plant", PlantKind.APPLE);
    public static final RegistryObject<PlantBlock> STRAWBERRY_PLANT = plant("strawberry_plant", PlantKind.STRAWBERRY);
    public static final RegistryObject<PlantBlock> POTATO_PLANT = plant("potato_plant", PlantKind.POTATO);
    public static final RegistryObject<PlantBlock> CARROT_PLANT = plant("carrot_plant", PlantKind.CARROT);
    public static final RegistryObject<PlantBlock> TOMATO_PLANT = plant("tomato_plant", PlantKind.TOMATO);

    public static final List<RegistryObject<PlantBlock>> PLANTS = List.of(
        MOON_BLOOM_PLANT, DRAGONS_BREATH_PLANT, GHOST_PEPPER_PLANT, POISON_IVY_PLANT,
        MUSHROOM_PLANT, VENUS_FLYTRAP_PLANT, POMEGRANATE_PLANT, DRAGON_FRUIT_PLANT,
        SUNFLOWER_PLANT, BAMBOO_PLANT, MANGO_PLANT, PLUM_PLANT, APPLE_PLANT,
        STRAWBERRY_PLANT, POTATO_PLANT, CARROT_PLANT, TOMATO_PLANT
    );
    private static RegistryObject<PlantBlock> plant(String id, PlantKind kind) {
        return BLOCKS.register(id, () -> new PlantBlock(plantProperties(), kind));
    }

    public static final RegistryObject<Block> CARROT_LEAF = BLOCKS.register("carrot_leaf", () -> new PlantPartBlock(partProperties()));
    public static final RegistryObject<Block> TOMATO_LEAF = BLOCKS.register("tomato_leaf", () -> new PlantPartBlock(partProperties()));
    public static final RegistryObject<Block> TOMATO_FRUIT = BLOCKS.register("tomato_fruit", () -> new PlantPartBlock(partProperties()));
    public static final RegistryObject<Block> DRAGON_STEM = BLOCKS.register("dragon_stem", () -> new PlantPartBlock(partProperties()));
    public static final RegistryObject<Block> DRAGON_FRUIT = BLOCKS.register("dragon_fruit", () -> new PlantPartBlock(partProperties()));
    private ModBlocks() {}
}
