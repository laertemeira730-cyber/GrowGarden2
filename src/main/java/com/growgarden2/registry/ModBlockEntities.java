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
                    ModBlocks.CARROT_PLANT.get(),
                    ModBlocks.TOMATO_PLANT.get(),
                    ModBlocks.DRAGON_FRUIT_PLANT.get()
            ).build(null));

    private ModBlockEntities() {}
}
