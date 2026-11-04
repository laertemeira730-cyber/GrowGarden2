package com.growgarden2.plants;

import java.util.UUID;
import java.util.function.Supplier;
import com.growgarden2.areas.AreaSavedData;
import com.growgarden2.blocks.PlantBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class PlantSeedItem extends Item {
    private final PlantKind kind;
    private final Supplier<PlantBlock> plantBlock;

    public PlantSeedItem(PlantKind kind, Supplier<PlantBlock> plantBlock, Properties properties) {
        super(properties);
        this.kind = kind;
        this.plantBlock = plantBlock;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (!(context.getLevel() instanceof ServerLevel level)) {
            return InteractionResult.SUCCESS;
        }
        if (!(context.getPlayer() instanceof ServerPlayer player)) {
            return InteractionResult.FAIL;
        }

        BlockPos soilPos = context.getClickedPos();
        BlockPos plantPos = soilPos.above();
        BlockState soil = level.getBlockState(soilPos);
        if (!soil.is(BlockTags.DIRT) && !soil.is(Blocks.FARMLAND)) {
            player.sendSystemMessage(Component.literal("Plante esta semente sobre terra ou terra arada."));
            return InteractionResult.FAIL;
        }
        if (!level.getBlockState(plantPos).canBeReplaced()) {
            player.sendSystemMessage(Component.literal("Este espaço está ocupado."));
            return InteractionResult.FAIL;
        }

        AreaSavedData areas = AreaSavedData.get(level);
        UUID gardenOwner = areas.getGardenOwnerAt(plantPos);
        if (gardenOwner == null || !gardenOwner.equals(player.getUUID())) {
            player.sendSystemMessage(Component.literal("Você só pode plantar dentro do seu próprio jardim."));
            return InteractionResult.FAIL;
        }

        PlantBlock block = plantBlock.get();
        if (!level.setBlock(plantPos, block.defaultBlockState(), 3)) {
            return InteractionResult.FAIL;
        }
        if (!(level.getBlockEntity(plantPos) instanceof PlantBlockEntity plant)) {
            level.removeBlock(plantPos, false);
            return InteractionResult.FAIL;
        }

        plant.initialize(player.getUUID());
        if (!player.getAbilities().instabuild) {
            context.getItemInHand().shrink(1);
        }
        player.sendSystemMessage(Component.literal("Você plantou " + kind.displayName().toLowerCase() + "!"));
        return InteractionResult.CONSUME;
    }
}
