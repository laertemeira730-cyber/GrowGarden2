package com.growgarden2.shop;

import com.growgarden2.plants.PlantKind;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class SeedShopScreen extends AbstractContainerScreen<SeedShopMenu> {
    public SeedShopScreen(SeedShopMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        imageWidth = 320; imageHeight = 215; titleLabelX = 12; titleLabelY = 9; inventoryLabelY = 1000;
    }
    @Override
    protected void renderBg(GuiGraphics g, float partialTick, int mouseX, int mouseY) {
        int x = leftPos, y = topPos;
        g.fill(x, y, x + imageWidth, y + imageHeight, 0xFF0D141D);
        g.fill(x, y, x + imageWidth, y + 29, 0xFF0876D1);
        g.fill(x + 5, y + 34, x + imageWidth - 5, y + 57, 0xFF202936);
        g.drawString(font, Component.literal("LOJA DE SEMENTES"), x + 12, y + 10, 0xFFFFFFFF, false);
        g.drawString(font, Component.literal("Seu dinheiro: $" + menu.getCoins()), x + 12, y + 41, 0xFFFFD34E, false);
        for (int i = 0; i < 3; i++) {
            PlantKind kind = menu.getVisibleKind(i); if (kind == null) continue;
            int rowY = y + 64 + i * 39;
            g.fill(x + 7, rowY, x + imageWidth - 7, rowY + 34, 0xFF202936);
            g.fill(x + 7, rowY, x + 10, rowY + 34, tierColor(kind.tier()));
            g.drawString(font, Component.literal(kind.displayName()), x + 18, rowY + 5, 0xFFFFFFFF, false);
            g.drawString(font, Component.literal("NÍVEL " + kind.tier()), x + 18, rowY + 19, tierColor(kind.tier()), false);
            int bx = x + 211, by = rowY + 5;
            boolean hovered = mouseX >= bx && mouseX < bx + 100 && mouseY >= by && mouseY < by + 24;
            int color = menu.getCoins() >= kind.seedPrice() ? (hovered ? 0xFFFFA52A : 0xFFF07A00) : 0xFF55545A;
            g.fill(bx, by, bx + 100, by + 24, color);
            g.drawCenteredString(font, Component.literal("COMPRAR $" + kind.seedPrice()), bx + 50, by + 8, 0xFFFFFFFF);
        }
        g.drawString(font, Component.literal("Página " + (menu.getPage() + 1) + "/" + (menu.getMaxPage() + 1)),
                x + 12, y + 190, 0xFFB8C7D8, false);
        drawPageButton(g, x + 211, y + 184, "ANTERIOR", mouseX, mouseY, menu.getPage() > 0);
        drawPageButton(g, x + 263, y + 184, "PRÓXIMA", mouseX, mouseY, menu.getPage() < menu.getMaxPage());
    }
    private void drawPageButton(GuiGraphics g, int x, int y, String label, int mx, int my, boolean enabled) {
        boolean hover = mx >= x && mx < x + 50 && my >= y && my < y + 20;
        g.fill(x, y, x + 50, y + 20, !enabled ? 0xFF3D4651 : (hover ? 0xFF3285C7 : 0xFF245F91));
        g.drawCenteredString(font, Component.literal(label), x + 25, y + 6, 0xFFFFFFFF);
    }
    private int tierColor(String tier) {
        return switch (tier) {
            case "S" -> 0xFFFF6B6B;
            case "S/A", "A" -> 0xFFFFC65C;
            case "B" -> 0xFF55C8FF;
            default -> 0xFF9BE36D;
        };
    }
    @Override protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {}
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            for (int i = 0; i < 3; i++) {
                int bx = leftPos + 211, by = topPos + 69 + i * 39;
                if (mouseX >= bx && mouseX < bx + 100 && mouseY >= by && mouseY < by + 24) {
                    if (menu.getVisibleKind(i) != null && minecraft != null && minecraft.gameMode != null)
                        minecraft.gameMode.handleInventoryButtonClick(menu.containerId, i);
                    return true;
                }
            }
            if (mouseY >= topPos + 184 && mouseY < topPos + 204 && minecraft != null && minecraft.gameMode != null) {
                if (mouseX >= leftPos + 211 && mouseX < leftPos + 261 && menu.getPage() > 0) {
                    minecraft.gameMode.handleInventoryButtonClick(menu.containerId, 90); return true;
                }
                if (mouseX >= leftPos + 263 && mouseX < leftPos + 313 && menu.getPage() < menu.getMaxPage()) {
                    minecraft.gameMode.handleInventoryButtonClick(menu.containerId, 91); return true;
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }
}
