package com.growgarden2.shop;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class SeedShopScreen extends AbstractContainerScreen<SeedShopMenu> {
    private static final int[] PRICES = {10, 75, 250};
    private static final String[] NAMES = {"Semente de cenoura", "Semente de tomate", "Semente de fruta-dragão"};
    private static final String[] RARITIES = {"COMUM", "INCOMUM", "RARA"};

    public SeedShopScreen(SeedShopMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        imageWidth = 320;
        imageHeight = 190;
        titleLabelX = 12;
        titleLabelY = 9;
        inventoryLabelY = 1000;
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        int x = leftPos;
        int y = topPos;
        graphics.fill(x, y, x + imageWidth, y + imageHeight, 0xFF0D141D);
        graphics.fill(x, y, x + imageWidth, y + 29, 0xFF0876D1);
        graphics.fill(x + 5, y + 34, x + imageWidth - 5, y + 57, 0xFF202936);
        graphics.drawString(font, Component.literal("SEMENTES"), x + 12, y + 10, 0xFFFFFFFF, false);
        graphics.drawString(font, Component.literal("Seu dinheiro: $" + menu.getCoins()),
                x + 12, y + 41, 0xFFFFD34E, false);

        for (int i = 0; i < 3; i++) {
            int rowY = y + 64 + i * 39;
            graphics.fill(x + 7, rowY, x + imageWidth - 7, rowY + 34, 0xFF202936);
            graphics.fill(x + 7, rowY, x + 10, rowY + 34, 0xFFFF8A00);
            graphics.drawString(font, Component.literal(NAMES[i]), x + 18, rowY + 5, 0xFFFFFFFF, false);
            graphics.drawString(font, Component.literal(RARITIES[i]), x + 18, rowY + 19,
                    i == 2 ? 0xFFFF70A0 : (i == 1 ? 0xFF55C8FF : 0xFF9BE36D), false);
            int buttonX = x + 211;
            int buttonY = rowY + 5;
            boolean hovered = mouseX >= buttonX && mouseX < buttonX + 100
                    && mouseY >= buttonY && mouseY < buttonY + 24;
            int buttonColor = menu.getCoins() >= PRICES[i]
                    ? (hovered ? 0xFFFFA52A : 0xFFF07A00) : 0xFF55545A;
            graphics.fill(buttonX, buttonY, buttonX + 100, buttonY + 24, buttonColor);
            graphics.drawCenteredString(font, Component.literal("COMPRAR $" + PRICES[i]),
                    buttonX + 50, buttonY + 8, 0xFFFFFFFF);
        }
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        // The shop draws its own labels to keep the full-width pixel-style layout.
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            for (int i = 0; i < 3; i++) {
                int bx = leftPos + 211;
                int by = topPos + 69 + i * 39;
                if (mouseX >= bx && mouseX < bx + 100 && mouseY >= by && mouseY < by + 24) {
                    if (minecraft != null && minecraft.gameMode != null) {
                        minecraft.gameMode.handleInventoryButtonClick(menu.containerId, i);
                    }
                    return true;
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }
}
