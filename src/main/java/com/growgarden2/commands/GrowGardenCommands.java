package com.growgarden2.commands;

import com.growgarden2.plants.PlantGrowthSpeed;
import com.growgarden2.shop.SeedShopMenu;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public final class GrowGardenCommands {
    private GrowGardenCommands() {}

    @SubscribeEvent
    public static void register(RegisterCommandsEvent event) {
        event.getDispatcher().register(Commands.literal("gg2")
                .requires(source -> source.hasPermission(2))
                .then(Commands.literal("money")
                        .then(Commands.argument("valor", IntegerArgumentType.integer(0))
                                .executes(context -> {
                                    if (!(context.getSource().getEntity() instanceof ServerPlayer player)) {
                                        context.getSource().sendFailure(Component.literal("Este comando só pode ser usado por um jogador."));
                                        return 0;
                                    }
                                    int value = IntegerArgumentType.getInteger(context, "valor");
                                    player.getPersistentData().putInt(SeedShopMenu.COINS_KEY, value);
                                    context.getSource().sendSuccess(() ->
                                            Component.literal("Seu dinheiro agora é " + value + " moedas."), true);
                                    return 1;
                                })))
                .then(Commands.literal("grow")
                        .then(Commands.argument("multiplicador", FloatArgumentType.floatArg(0.1F, 1000.0F))
                                .executes(context -> {
                                    float multiplier = FloatArgumentType.getFloat(context, "multiplicador");
                                    PlantGrowthSpeed.setMultiplier(multiplier);
                                    context.getSource().sendSuccess(() ->
                                            Component.literal("Velocidade de crescimento definida para " + multiplier
                                                    + "x. Use /gg2 grow 1 para voltar ao normal."), true);
                                    return 1;
                                }))));
    }
}
