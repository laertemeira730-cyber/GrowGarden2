package com.growgarden2.registry;

import com.growgarden2.GrowGarden2;
import com.growgarden2.plants.PlantKind;
import com.growgarden2.plants.PlantSeedItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, GrowGarden2.MOD_ID);
    public static final RegistryObject<Item> GARDEN_MARKER = ITEMS.register("garden_marker", () -> new BlockItem(ModBlocks.GARDEN_MARKER.get(), new Item.Properties()));
    public static final RegistryObject<Item> SHOP_MARKER = ITEMS.register("shop_marker", () -> new BlockItem(ModBlocks.SHOP_MARKER.get(), new Item.Properties()));
    public static final RegistryObject<Item> SALES_MARKER = ITEMS.register("sales_marker", () -> new BlockItem(ModBlocks.SALES_MARKER.get(), new Item.Properties()));

    public static final RegistryObject<Item> MOON_BLOOM_SEEDS = seeds(PlantKind.MOON_BLOOM, ModBlocks.MOON_BLOOM_PLANT);
    public static final RegistryObject<Item> DRAGONS_BREATH_SEEDS = seeds(PlantKind.DRAGONS_BREATH, ModBlocks.DRAGONS_BREATH_PLANT);
    public static final RegistryObject<Item> GHOST_PEPPER_SEEDS = seeds(PlantKind.GHOST_PEPPER, ModBlocks.GHOST_PEPPER_PLANT);
    public static final RegistryObject<Item> POISON_IVY_SEEDS = seeds(PlantKind.POISON_IVY, ModBlocks.POISON_IVY_PLANT);
    public static final RegistryObject<Item> MUSHROOM_SEEDS = seeds(PlantKind.MUSHROOM, ModBlocks.MUSHROOM_PLANT);
    public static final RegistryObject<Item> VENUS_FLYTRAP_SEEDS = seeds(PlantKind.VENUS_FLYTRAP, ModBlocks.VENUS_FLYTRAP_PLANT);
    public static final RegistryObject<Item> POMEGRANATE_SEEDS = seeds(PlantKind.POMEGRANATE, ModBlocks.POMEGRANATE_PLANT);
    public static final RegistryObject<Item> DRAGON_FRUIT_SEEDS = seeds(PlantKind.DRAGON_FRUIT, ModBlocks.DRAGON_FRUIT_PLANT);
    public static final RegistryObject<Item> SUNFLOWER_SEEDS = seeds(PlantKind.SUNFLOWER, ModBlocks.SUNFLOWER_PLANT);
    public static final RegistryObject<Item> BAMBOO_SEEDS = seeds(PlantKind.BAMBOO, ModBlocks.BAMBOO_PLANT);
    public static final RegistryObject<Item> MANGO_SEEDS = seeds(PlantKind.MANGO, ModBlocks.MANGO_PLANT);
    public static final RegistryObject<Item> PLUM_SEEDS = seeds(PlantKind.PLUM, ModBlocks.PLUM_PLANT);
    public static final RegistryObject<Item> APPLE_SEEDS = seeds(PlantKind.APPLE, ModBlocks.APPLE_PLANT);
    public static final RegistryObject<Item> STRAWBERRY_SEEDS = seeds(PlantKind.STRAWBERRY, ModBlocks.STRAWBERRY_PLANT);
    public static final RegistryObject<Item> POTATO_SEEDS = seeds(PlantKind.POTATO, ModBlocks.POTATO_PLANT);
    public static final RegistryObject<Item> CARROT_SEEDS = seeds(PlantKind.CARROT, ModBlocks.CARROT_PLANT);
    public static final RegistryObject<Item> TOMATO_SEEDS = seeds(PlantKind.TOMATO, ModBlocks.TOMATO_PLANT);

    public static final RegistryObject<Item> MOON_BLOOM_CROP = crop("moon_bloom_crop");
    public static final RegistryObject<Item> DRAGONS_BREATH_CROP = crop("dragons_breath_crop");
    public static final RegistryObject<Item> GHOST_PEPPER_CROP = crop("ghost_pepper_crop");
    public static final RegistryObject<Item> POISON_IVY_CROP = crop("poison_ivy_crop");
    public static final RegistryObject<Item> MUSHROOM_CROP = crop("mushroom_crop");
    public static final RegistryObject<Item> VENUS_FLYTRAP_CROP = crop("venus_flytrap_crop");
    public static final RegistryObject<Item> POMEGRANATE_CROP = crop("pomegranate_crop");
    public static final RegistryObject<Item> DRAGON_FRUIT_CROP = crop("dragon_fruit_crop");
    public static final RegistryObject<Item> SUNFLOWER_CROP = crop("sunflower_crop");
    public static final RegistryObject<Item> BAMBOO_CROP = crop("bamboo_crop");
    public static final RegistryObject<Item> MANGO_CROP = crop("mango_crop");
    public static final RegistryObject<Item> PLUM_CROP = crop("plum_crop");
    public static final RegistryObject<Item> APPLE_CROP = crop("apple_crop");
    public static final RegistryObject<Item> STRAWBERRY_CROP = crop("strawberry_crop");
    public static final RegistryObject<Item> POTATO_CROP = crop("potato_crop");
    public static final RegistryObject<Item> CARROT_CROP = crop("carrot_crop");
    public static final RegistryObject<Item> TOMATO_CROP = crop("tomato_crop");

    private static RegistryObject<Item> seeds(PlantKind kind, RegistryObject<com.growgarden2.blocks.PlantBlock> block) {
        return ITEMS.register(kind.id() + "_seeds", () -> new PlantSeedItem(kind, block, new Item.Properties()));
    }
    private static RegistryObject<Item> crop(String id) { return ITEMS.register(id, () -> new Item(new Item.Properties())); }

    public static Item seedFor(PlantKind kind) {
        return switch (kind) {
            case MOON_BLOOM -> MOON_BLOOM_SEEDS.get();
            case DRAGONS_BREATH -> DRAGONS_BREATH_SEEDS.get();
            case GHOST_PEPPER -> GHOST_PEPPER_SEEDS.get();
            case POISON_IVY -> POISON_IVY_SEEDS.get();
            case MUSHROOM -> MUSHROOM_SEEDS.get();
            case VENUS_FLYTRAP -> VENUS_FLYTRAP_SEEDS.get();
            case POMEGRANATE -> POMEGRANATE_SEEDS.get();
            case DRAGON_FRUIT -> DRAGON_FRUIT_SEEDS.get();
            case SUNFLOWER -> SUNFLOWER_SEEDS.get();
            case BAMBOO -> BAMBOO_SEEDS.get();
            case MANGO -> MANGO_SEEDS.get();
            case PLUM -> PLUM_SEEDS.get();
            case APPLE -> APPLE_SEEDS.get();
            case STRAWBERRY -> STRAWBERRY_SEEDS.get();
            case POTATO -> POTATO_SEEDS.get();
            case CARROT -> CARROT_SEEDS.get();
            case TOMATO -> TOMATO_SEEDS.get();
        };
    }
    public static Item cropFor(PlantKind kind) {
        return switch (kind) {
            case MOON_BLOOM -> MOON_BLOOM_CROP.get();
            case DRAGONS_BREATH -> DRAGONS_BREATH_CROP.get();
            case GHOST_PEPPER -> GHOST_PEPPER_CROP.get();
            case POISON_IVY -> POISON_IVY_CROP.get();
            case MUSHROOM -> MUSHROOM_CROP.get();
            case VENUS_FLYTRAP -> VENUS_FLYTRAP_CROP.get();
            case POMEGRANATE -> POMEGRANATE_CROP.get();
            case DRAGON_FRUIT -> DRAGON_FRUIT_CROP.get();
            case SUNFLOWER -> SUNFLOWER_CROP.get();
            case BAMBOO -> BAMBOO_CROP.get();
            case MANGO -> MANGO_CROP.get();
            case PLUM -> PLUM_CROP.get();
            case APPLE -> APPLE_CROP.get();
            case STRAWBERRY -> STRAWBERRY_CROP.get();
            case POTATO -> POTATO_CROP.get();
            case CARROT -> CARROT_CROP.get();
            case TOMATO -> TOMATO_CROP.get();
        };
    }
    private ModItems() {}
}
