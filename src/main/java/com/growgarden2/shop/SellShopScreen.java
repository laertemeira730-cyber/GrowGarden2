package com.growgarden2.shop;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class SellShopScreen extends AbstractContainerScreen<SellShopMenu> {
    private static final String[] NAMES = {"Cenouras", "Tomates", "Frutas-dragão"};

    public SellShopScreen(SellShopMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        imageWidth = 320;
        imageHeight = 190;
        titleLabelX = 12;
        titleLabelY = 9;
        inventoryLabelY = 1000;
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        int x = leftPos, y = topPos;
        graphics.fill(x, y, x + imageWidth, y + imageHeight, 0xFF0D141D);
        graphics.fill(x, y, x + imageWidth, y + 29, 0xFF0876D1);
        graphics.drawString(font, Component.literal("CENTRAL DE VENDAS"), x + 12, y + 10, 0xFFFFFFFF, false);
        graphics.fill(x + 7, y + 36, x + imageWidth - 7, y + 59, 0xFF202936);
        graphics.drawString(font, Component.literal("Seu dinheiro: $" + menu.getCoins()),
                x + 16, y + 44, 0xFFFFD34E, false);
        for (int i = 0; i < 3; i++) {
            int rowY = y + 66 + i * 38;
            graphics.fill(x + 7, rowY, x + imageWidth - 7, rowY + 33, 0xFF202936);
            graphics.fill(x + 7, rowY, x + 10, rowY + 33, 0xFF55C98A);
            graphics.drawString(font, Component.literal(NAMES[i]), x + 17, rowY + 5, 0xFFFFFFFF, false);
            graphics.drawString(font, Component.literal("No inventário: " + menu.getCount(i)
                    + "   Valor: $" + menu.getPrice(i) + " cada"), x + 17, rowY + 19, 0xFFB8C7D8, false);
            int bx = x + 211, by = rowY + 5;
            boolean hovered = mouseX >= bx && mouseX < bx + 100 && mouseY >= by && mouseY < by + 23;
            int color = menu.getCount(i) > 0 ? (hovered ? 0xFF45B979 : 0xFF238B58) : 0xFF55545A;
            graphics.fill(bx, by, bx + 100, by + 23, color);
            graphics.drawCenteredString(font, Component.literal("VENDER TUDO"),
                    bx + 50, by + 7, 0xFFFFFFFF);
        }
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {}

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            for (int i = 0; i < 3; i++) {
                int bx = leftPos + 211, by = topPos + 71 + i * 38;
                if (mouseX >= bx && mouseX < bx + 100 && mouseY >= by && mouseY < by + 23) {
                    if (menu.getCount(i) > 0 && minecraft != null && minecraft.gameMode != null) {
                        minecraft.gameMode.handleInventoryButtonClick(menu.containerId, i);
                    }
                    return true;
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }
}
