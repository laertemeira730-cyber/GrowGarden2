package com.growgarden2.registry;

import com.growgarden2.GrowGarden2;
import com.growgarden2.plants.PlantBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, GrowGarden2.MOD_ID);
    public static final RegistryObject<BlockEntityType<PlantBlockEntity>> PLANT =
            BLOCK_ENTITIES.register("plant", () -> BlockEntityType.Builder.of(
                    PlantBlockEntity::new,
                    ModBlocks.MOON_BLOOM_PLANT.get(), ModBlocks.DRAGONS_BREATH_PLANT.get(),
                    ModBlocks.GHOST_PEPPER_PLANT.get(), ModBlocks.POISON_IVY_PLANT.get(),
                    ModBlocks.MUSHROOM_PLANT.get(), ModBlocks.VENUS_FLYTRAP_PLANT.get(),
                    ModBlocks.POMEGRANATE_PLANT.get(), ModBlocks.DRAGON_FRUIT_PLANT.get(),
                    ModBlocks.SUNFLOWER_PLANT.get(), ModBlocks.BAMBOO_PLANT.get(),
                    ModBlocks.MANGO_PLANT.get(), ModBlocks.PLUM_PLANT.get(),
                    ModBlocks.APPLE_PLANT.get(), ModBlocks.STRAWBERRY_PLANT.get(),
                    ModBlocks.POTATO_PLANT.get(), ModBlocks.CARROT_PLANT.get(),
                    ModBlocks.TOMATO_PLANT.get()
            ).build(null));
    private ModBlockEntities() {}
}
