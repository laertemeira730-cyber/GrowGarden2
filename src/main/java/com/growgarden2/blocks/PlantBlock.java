package com.growgarden2.blocks;

import com.growgarden2.plants.PlantBlockEntity;
import com.growgarden2.plants.PlantKind;
import com.growgarden2.plants.PlantStructures;
import com.growgarden2.registry.ModBlockEntities;
import com.growgarden2.registry.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;

public class PlantBlock extends BaseEntityBlock {
    public static final IntegerProperty STAGE = IntegerProperty.create("stage", 0, 2);
    private final PlantKind kind;
    public PlantBlock(BlockBehaviour.Properties properties, PlantKind kind) {
        super(properties); this.kind = kind;
        registerDefaultState(stateDefinition.any().setValue(STAGE, 0));
    }
    public PlantKind getKind() { return kind; }
    @Override protected void createBlockStateDefinition(StateDefinition.Builder<net.minecraft.world.level.block.Block, BlockState> builder) { builder.add(STAGE); }
    @Override public RenderShape getRenderShape(BlockState state) { return RenderShape.MODEL; }
    @Override public BlockEntity newBlockEntity(BlockPos pos, BlockState state) { return new PlantBlockEntity(pos, state); }
    @Override public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide ? null : createTickerHelper(type, ModBlockEntities.PLANT.get(), PlantBlockEntity::serverTick);
    }
    @Override public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (hand != InteractionHand.MAIN_HAND) return InteractionResult.PASS;
        return interactWithPlant(level, pos, player);
    }
    public static InteractionResult interactWithPlant(Level level, BlockPos pos, Player player) {
        if (level.isClientSide) return InteractionResult.SUCCESS;
        if (!(level.getBlockEntity(pos) instanceof PlantBlockEntity plant) || plant.getOwner() == null) return InteractionResult.CONSUME;
        if (!plant.getOwner().equals(player.getUUID())) {
            player.sendSystemMessage(Component.literal("Esta planta pertence a outro jogador."));
            return InteractionResult.CONSUME;
        }
        if (!plant.isMature()) {
            long seconds = plant.getRemainingSeconds();
            player.sendSystemMessage(Component.literal("Ainda está crescendo! Tempo restante: " + (seconds / 60) + "m " + (seconds % 60) + "s."));
            return InteractionResult.CONSUME;
        }
        PlantKind kind = ((PlantBlock) level.getBlockState(pos).getBlock()).getKind();
        ItemStack harvest = new ItemStack(ModItems.cropFor(kind), kind.harvestAmount());
        if (!player.getInventory().add(harvest)) player.drop(harvest, false);
        if (kind.isMultiHarvest() && level instanceof ServerLevel serverLevel) {
            PlantStructures.harvestFruits(serverLevel, pos);
            plant.beginRegrowth(kind.regrowTicks());
        } else {
            if (level instanceof ServerLevel serverLevel) PlantStructures.clearParts(serverLevel, pos);
            level.removeBlock(pos, false);
        }
        player.sendSystemMessage(Component.literal("Colheita realizada: " + kind.displayName() + "!"));
        return InteractionResult.CONSUME;
    }
    @Override public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean moving) {
        if (!state.is(newState.getBlock()) && level instanceof ServerLevel serverLevel) PlantStructures.clearParts(serverLevel, pos);
        super.onRemove(state, level, pos, newState, moving);
    }
}
