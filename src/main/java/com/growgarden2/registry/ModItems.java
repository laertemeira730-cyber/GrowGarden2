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
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, GrowGarden2.MOD_ID);

    public static final RegistryObject<Item> GARDEN_MARKER = ITEMS.register("garden_marker",
            () -> new BlockItem(ModBlocks.GARDEN_MARKER.get(), new Item.Properties()));
    public static final RegistryObject<Item> SHOP_MARKER = ITEMS.register("shop_marker",
            () -> new BlockItem(ModBlocks.SHOP_MARKER.get(), new Item.Properties()));
    public static final RegistryObject<Item> SALES_MARKER = ITEMS.register("sales_marker",
            () -> new BlockItem(ModBlocks.SALES_MARKER.get(), new Item.Properties()));

    public static final RegistryObject<Item> CARROT_SEEDS = ITEMS.register("carrot_seeds",
            () -> new PlantSeedItem(PlantKind.CARROT, ModBlocks.CARROT_PLANT, new Item.Properties()));
    public static final RegistryObject<Item> TOMATO_SEEDS = ITEMS.register("tomato_seeds",
            () -> new PlantSeedItem(PlantKind.TOMATO, ModBlocks.TOMATO_PLANT, new Item.Properties()));
    public static final RegistryObject<Item> DRAGON_FRUIT_SEEDS = ITEMS.register("dragon_fruit_seeds",
            () -> new PlantSeedItem(PlantKind.DRAGON_FRUIT, ModBlocks.DRAGON_FRUIT_PLANT, new Item.Properties()));

    public static final RegistryObject<Item> CARROT_CROP =
            ITEMS.register("carrot_crop", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> TOMATO_CROP =
            ITEMS.register("tomato_crop", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> DRAGON_FRUIT_CROP =
            ITEMS.register("dragon_fruit_crop", () -> new Item(new Item.Properties()));

    private ModItems() {}
}
