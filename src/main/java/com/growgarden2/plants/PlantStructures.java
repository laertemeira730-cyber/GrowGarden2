package com.growgarden2.plants;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import com.growgarden2.areas.AreaSavedData;
import com.growgarden2.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;

public final class PlantStructures {
    private PlantStructures() {}

    public static void rebuild(ServerLevel level, BlockPos base, UUID owner,
                               PlantKind kind, int stage, boolean mature) {
        if (!(level.getBlockEntity(base) instanceof PlantBlockEntity plant)) return;
        clearParts(level, base);
        List<Part> structure = structureFor(kind, stage, mature);
        List<BlockPos> placed = new ArrayList<>(), fruits = new ArrayList<>();
        AreaSavedData areas = AreaSavedData.get(level);
        for (Part part : structure) {
            BlockPos target = base.offset(part.x(), part.y(), part.z());
            if (!areas.isInsideGarden(owner, target) || !level.hasChunkAt(target)
                    || !level.getBlockState(target).isAir()) continue;
            if (level.setBlock(target, part.state(), 3)) {
                placed.add(target.immutable());
                if (part.fruit()) fruits.add(target.immutable());
            }
        }
        plant.setPartPositions(placed);
        plant.setFruitPositions(fruits);
    }

    public static void harvestFruits(ServerLevel level, BlockPos base) {
        if (!(level.getBlockEntity(base) instanceof PlantBlockEntity plant)) return;
        for (BlockPos fruit : plant.getFruitPositions()) {
            if (level.hasChunkAt(fruit) && !level.getBlockState(fruit).isAir())
                level.setBlock(fruit, Blocks.AIR.defaultBlockState(), 3);
        }
    }

    public static BlockPos findBase(Level level, BlockPos partPos) {
        for (int dy = 0; dy <= 6; dy++) for (int dx = -3; dx <= 3; dx++) for (int dz = -3; dz <= 3; dz++) {
            BlockPos candidate = partPos.offset(dx, -dy, dz);
            if (level.getBlockEntity(candidate) instanceof PlantBlockEntity plant
                    && plant.getPartPositions().contains(partPos)) return candidate;
        }
        return null;
    }

    public static void clearParts(ServerLevel level, BlockPos base) {
        if (!(level.getBlockEntity(base) instanceof PlantBlockEntity plant)) return;
        for (BlockPos partPos : plant.getPartPositions()) {
            if (level.hasChunkAt(partPos) && !level.getBlockState(partPos).isAir())
                level.setBlock(partPos, Blocks.AIR.defaultBlockState(), 3);
        }
        plant.setPartPositions(List.of());
        plant.setFruitPositions(List.of());
    }

    private static List<Part> structureFor(PlantKind kind, int stage, boolean mature) {
        List<Part> p = new ArrayList<>();
        switch (kind) {
            case CARROT -> carrot(p, stage);
            case TOMATO -> tomato(p, stage, mature);
            case DRAGON_FRUIT -> dragonFruit(p, stage, mature);
            case MOON_BLOOM -> moonBloom(p, stage, mature);
            case DRAGONS_BREATH -> dragonsBreath(p, stage, mature);
            case GHOST_PEPPER -> ghostPepper(p, stage, mature);
            case POISON_IVY -> poisonIvy(p, stage, mature);
            case MUSHROOM -> mushroom(p, stage);
            case VENUS_FLYTRAP -> venusFlytrap(p, stage, mature);
            case POMEGRANATE -> tree(p, stage, Blocks.ACACIA_LOG, Blocks.ACACIA_LEAVES, Blocks.RED_CONCRETE, mature);
            case SUNFLOWER -> sunflower(p, stage, mature);
            case BAMBOO -> bamboo(p, stage);
            case MANGO -> tree(p, stage, Blocks.JUNGLE_LOG, Blocks.JUNGLE_LEAVES, Blocks.YELLOW_WOOL, mature);
            case PLUM -> tree(p, stage, Blocks.DARK_OAK_LOG, Blocks.DARK_OAK_LEAVES, Blocks.PURPLE_WOOL, mature);
            case APPLE -> tree(p, stage, Blocks.OAK_LOG, Blocks.OAK_LEAVES, Blocks.RED_WOOL, mature);
            case STRAWBERRY -> strawberry(p, stage, mature);
            case POTATO -> potato(p, stage);
        }
        return p;
    }

    private static void carrot(List<Part> p, int s) {
        if (s == 1) add(p, ModBlocks.CARROT_LEAF.get(), 0, 1, 0);
        if (s >= 2) {
            add(p, ModBlocks.CARROT_LEAF.get(), 0, 1, 0);
            add(p, ModBlocks.CARROT_LEAF.get(), -1, 1, 0); add(p, ModBlocks.CARROT_LEAF.get(), 1, 1, 0);
            add(p, ModBlocks.CARROT_LEAF.get(), 0, 1, -1); add(p, ModBlocks.CARROT_LEAF.get(), 0, 1, 1);
            add(p, ModBlocks.CARROT_LEAF.get(), 0, 2, 0);
        }
    }
    private static void tomato(List<Part> p, int s, boolean mature) {
        add(p, ModBlocks.TOMATO_LEAF.get(), 0, 1, 0);
        if (s >= 1) {
            add(p, ModBlocks.TOMATO_LEAF.get(), -1, 1, 0); add(p, ModBlocks.TOMATO_LEAF.get(), 1, 1, 0);
            add(p, ModBlocks.TOMATO_LEAF.get(), 0, 1, -1); add(p, ModBlocks.TOMATO_LEAF.get(), 0, 1, 1);
            add(p, ModBlocks.TOMATO_LEAF.get(), 0, 2, 0);
        }
        if (s >= 2) {
            add(p, ModBlocks.TOMATO_LEAF.get(), -1, 2, 0); add(p, ModBlocks.TOMATO_LEAF.get(), 1, 2, 0);
            if (mature) {
                fruit(p, ModBlocks.TOMATO_FRUIT.get(), -1, 1, 1);
                fruit(p, ModBlocks.TOMATO_FRUIT.get(), 1, 1, -1);
                fruit(p, ModBlocks.TOMATO_FRUIT.get(), 0, 2, 1);
            }
        }
    }
    private static void dragonFruit(List<Part> p, int s, boolean mature) {
        add(p, ModBlocks.DRAGON_STEM.get(), 0, 1, 0);
        if (s >= 1) {
            add(p, ModBlocks.DRAGON_STEM.get(), 0, 2, 0);
            add(p, ModBlocks.DRAGON_STEM.get(), -1, 2, 0); add(p, ModBlocks.DRAGON_STEM.get(), 1, 2, 0);
        }
        if (s >= 2) {
            add(p, ModBlocks.DRAGON_STEM.get(), 0, 3, 0);
            add(p, ModBlocks.DRAGON_STEM.get(), 0, 2, -1); add(p, ModBlocks.DRAGON_STEM.get(), 0, 2, 1);
            if (mature) {
                fruit(p, ModBlocks.DRAGON_FRUIT.get(), -1, 2, 1);
                fruit(p, ModBlocks.DRAGON_FRUIT.get(), 1, 2, -1);
                fruit(p, ModBlocks.DRAGON_FRUIT.get(), 0, 3, 1);
            }
        }
    }
    private static void moonBloom(List<Part> p, int s, boolean mature) {
        add(p, Blocks.GREEN_TERRACOTTA, 0, 1, 0);
        if (s >= 1) {
            add(p, Blocks.GREEN_TERRACOTTA, 0, 2, 0);
            add(p, Blocks.GREEN_TERRACOTTA, 1, 1, 0); add(p, Blocks.GREEN_TERRACOTTA, -1, 1, 0);
        }
        if (s >= 2 && mature) {
            fruit(p, Blocks.WHITE_WOOL, 0, 3, 0); fruit(p, Blocks.WHITE_WOOL, 1, 3, 0);
            fruit(p, Blocks.WHITE_WOOL, -1, 3, 0); fruit(p, Blocks.WHITE_WOOL, 0, 3, 1);
            fruit(p, Blocks.WHITE_WOOL, 0, 3, -1); fruit(p, Blocks.SEA_LANTERN, 0, 4, 0);
        }
    }
    private static void dragonsBreath(List<Part> p, int s, boolean mature) {
        add(p, Blocks.BASALT, 0, 1, 0);
        if (s >= 1) { add(p, Blocks.CRIMSON_STEM, 0, 2, 0); add(p, Blocks.CRIMSON_STEM, 0, 3, 0); }
        if (s >= 2 && mature) {
            fruit(p, Blocks.NETHER_WART_BLOCK, 0, 4, 0); fruit(p, Blocks.NETHER_WART_BLOCK, 1, 4, 0);
            fruit(p, Blocks.NETHER_WART_BLOCK, -1, 4, 0); fruit(p, Blocks.MAGMA_BLOCK, 0, 5, 0);
        }
    }
    private static void ghostPepper(List<Part> p, int s, boolean mature) {
        add(p, Blocks.CRIMSON_STEM, 0, 1, 0);
        if (s >= 1) {
            add(p, Blocks.CRIMSON_STEM, 0, 2, 0);
            add(p, Blocks.RED_MUSHROOM_BLOCK, 1, 2, 0); add(p, Blocks.RED_MUSHROOM_BLOCK, -1, 2, 0);
        }
        if (s >= 2 && mature) {
            fruit(p, Blocks.RED_WOOL, -1, 3, 0); fruit(p, Blocks.RED_WOOL, 1, 3, 0);
            fruit(p, Blocks.RED_WOOL, 0, 3, 1);
        }
    }
    private static void poisonIvy(List<Part> p, int s, boolean mature) {
        add(p, Blocks.MOSS_BLOCK, 0, 1, 0);
        if (s >= 1) {
            addLeaf(p, Blocks.MANGROVE_LEAVES, -1, 1, 0); addLeaf(p, Blocks.MANGROVE_LEAVES, 1, 1, 0);
            addLeaf(p, Blocks.MANGROVE_LEAVES, 0, 2, 0);
        }
        if (s >= 2 && mature) {
            fruit(p, Blocks.GREEN_WOOL, -1, 2, 0); fruit(p, Blocks.GREEN_WOOL, 1, 2, 0);
            fruit(p, Blocks.GREEN_WOOL, 0, 3, 0);
        }
    }
    private static void mushroom(List<Part> p, int s) {
        add(p, Blocks.MUSHROOM_STEM, 0, 1, 0);
        if (s >= 1) { add(p, Blocks.MUSHROOM_STEM, 0, 2, 0); add(p, Blocks.BROWN_MUSHROOM_BLOCK, 0, 3, 0); }
        if (s >= 2) {
            add(p, Blocks.BROWN_MUSHROOM_BLOCK, 1, 3, 0); add(p, Blocks.BROWN_MUSHROOM_BLOCK, -1, 3, 0);
            add(p, Blocks.BROWN_MUSHROOM_BLOCK, 0, 3, 1); add(p, Blocks.BROWN_MUSHROOM_BLOCK, 0, 3, -1);
        }
    }
    private static void venusFlytrap(List<Part> p, int s, boolean mature) {
        add(p, Blocks.MOSS_BLOCK, 0, 1, 0);
        if (s >= 1) {
            add(p, Blocks.MOSS_BLOCK, 0, 2, 0);
            addLeaf(p, Blocks.AZALEA_LEAVES, -1, 2, 0); addLeaf(p, Blocks.AZALEA_LEAVES, 1, 2, 0);
        }
        if (s >= 2) {
            add(p, Blocks.MOSS_BLOCK, 0, 3, 0);
            if (mature) {
                fruit(p, Blocks.RED_MUSHROOM_BLOCK, 0, 4, 0);
                fruit(p, Blocks.RED_WOOL, -1, 3, 0); fruit(p, Blocks.RED_WOOL, 1, 3, 0);
            }
        }
    }
    private static void tree(List<Part> p, int s, Block log, Block leaves, Block fruitBlock, boolean mature) {
        add(p, log, 0, 1, 0);
        if (s >= 1) {
            add(p, log, 0, 2, 0);
            addLeaf(p, leaves, 0, 3, 0);
            addLeaf(p, leaves, -1, 2, 0); addLeaf(p, leaves, 1, 2, 0);
            addLeaf(p, leaves, 0, 2, -1); addLeaf(p, leaves, 0, 2, 1);
        }
        if (s >= 2) {
            add(p, log, 0, 3, 0);
            for (int dx = -1; dx <= 1; dx++) for (int dz = -1; dz <= 1; dz++)
                addLeaf(p, leaves, dx, 3, dz);
            addLeaf(p, leaves, 0, 4, 0);
            if (mature) {
                fruit(p, fruitBlock, -1, 3, 0); fruit(p, fruitBlock, 1, 3, 0);
                fruit(p, fruitBlock, 0, 4, -1); fruit(p, fruitBlock, 0, 4, 1);
            }
        }
    }
    private static void sunflower(List<Part> p, int s, boolean mature) {
        add(p, Blocks.GREEN_WOOL, 0, 1, 0);
        if (s >= 1) { add(p, Blocks.GREEN_WOOL, 0, 2, 0); add(p, Blocks.GREEN_WOOL, 0, 3, 0); }
        if (s >= 2 && mature) {
            fruit(p, Blocks.YELLOW_WOOL, 0, 4, 0); fruit(p, Blocks.YELLOW_WOOL, 1, 4, 0);
            fruit(p, Blocks.YELLOW_WOOL, -1, 4, 0); fruit(p, Blocks.YELLOW_WOOL, 0, 4, 1);
            fruit(p, Blocks.YELLOW_WOOL, 0, 4, -1); fruit(p, Blocks.ORANGE_WOOL, 0, 5, 0);
        }
    }
    private static void bamboo(List<Part> p, int s) {
        add(p, Blocks.BAMBOO_BLOCK, 0, 1, 0);
        if (s >= 1) { add(p, Blocks.BAMBOO_BLOCK, 0, 2, 0); addLeaf(p, Blocks.JUNGLE_LEAVES, 1, 2, 0); }
        if (s >= 2) {
            add(p, Blocks.BAMBOO_BLOCK, 0, 3, 0); add(p, Blocks.BAMBOO_BLOCK, 0, 4, 0);
            addLeaf(p, Blocks.JUNGLE_LEAVES, -1, 3, 0); addLeaf(p, Blocks.JUNGLE_LEAVES, 1, 3, 0);
            addLeaf(p, Blocks.JUNGLE_LEAVES, 0, 4, -1); addLeaf(p, Blocks.JUNGLE_LEAVES, 0, 4, 1);
        }
    }
    private static void strawberry(List<Part> p, int s, boolean mature) {
        add(p, Blocks.MOSS_BLOCK, 0, 1, 0);
        if (s >= 1) {
            addLeaf(p, Blocks.AZALEA_LEAVES, -1, 1, 0); addLeaf(p, Blocks.AZALEA_LEAVES, 1, 1, 0);
            addLeaf(p, Blocks.AZALEA_LEAVES, 0, 1, 1);
        }
        if (s >= 2 && mature) {
            fruit(p, Blocks.RED_WOOL, -1, 2, 0); fruit(p, Blocks.RED_WOOL, 1, 2, 0);
            fruit(p, Blocks.RED_WOOL, 0, 2, 1); fruit(p, Blocks.RED_WOOL, 0, 2, -1);
        }
    }
    private static void potato(List<Part> p, int s) {
        if (s == 0) add(p, Blocks.BROWN_TERRACOTTA, 0, 1, 0);
        if (s >= 1) {
            add(p, Blocks.GREEN_TERRACOTTA, 0, 1, 0);
            add(p, Blocks.GREEN_TERRACOTTA, -1, 1, 0); add(p, Blocks.GREEN_TERRACOTTA, 1, 1, 0);
        }
        if (s >= 2) { add(p, Blocks.GREEN_TERRACOTTA, 0, 2, 0); add(p, Blocks.BROWN_TERRACOTTA, 0, 1, 1); }
    }
    private static void add(List<Part> p, Block block, int x, int y, int z) {
        p.add(new Part(block.defaultBlockState(), x, y, z, false));
    }
    private static void fruit(List<Part> p, Block block, int x, int y, int z) {
        p.add(new Part(block.defaultBlockState(), x, y, z, true));
    }
    private static void addLeaf(List<Part> p, Block block, int x, int y, int z) {
        BlockState state = block.defaultBlockState();
        if (block instanceof LeavesBlock) state = state.setValue(LeavesBlock.PERSISTENT, true);
        p.add(new Part(state, x, y, z, false));
    }
    private record Part(BlockState state, int x, int y, int z, boolean fruit) {}
}
