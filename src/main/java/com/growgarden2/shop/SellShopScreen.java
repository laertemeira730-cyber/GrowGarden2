package com.growgarden2.shop;

import com.growgarden2.plants.PlantKind;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class SellShopScreen extends AbstractContainerScreen<SellShopMenu> {
    public SellShopScreen(SellShopMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title); imageWidth = 320; imageHeight = 215;
        titleLabelX = 12; titleLabelY = 9; inventoryLabelY = 1000;
    }
    @Override
    protected void renderBg(GuiGraphics g, float partialTick, int mouseX, int mouseY) {
        int x = leftPos, y = topPos;
        g.fill(x, y, x + imageWidth, y + imageHeight, 0xFF0D141D);
        g.fill(x, y, x + imageWidth, y + 29, 0xFF0876D1);
        g.drawString(font, Component.literal("CENTRAL DE VENDAS"), x + 12, y + 10, 0xFFFFFFFF, false);
        g.fill(x + 7, y + 36, x + imageWidth - 7, y + 59, 0xFF202936);
        g.drawString(font, Component.literal("Seu dinheiro: $" + menu.getCoins()), x + 16, y + 44, 0xFFFFD34E, false);
        for (int i = 0; i < 3; i++) {
            PlantKind kind = menu.getVisibleKind(i); if (kind == null) continue;
            int rowY = y + 66 + i * 38;
            g.fill(x + 7, rowY, x + imageWidth - 7, rowY + 33, 0xFF202936);
            g.fill(x + 7, rowY, x + 10, rowY + 33, 0xFF55C98A);
            g.drawString(font, Component.literal(kind.displayName()), x + 17, rowY + 5, 0xFFFFFFFF, false);
            g.drawString(font, Component.literal("No inventário: " + menu.getCount(i) + "   Valor: $" + menu.getPrice(i) + " cada"),
                    x + 17, rowY + 19, 0xFFB8C7D8, false);
            int bx = x + 211, by = rowY + 5;
            boolean hovered = mouseX >= bx && mouseX < bx + 100 && mouseY >= by && mouseY < by + 23;
            int color = menu.getCount(i) > 0 ? (hovered ? 0xFF45B979 : 0xFF238B58) : 0xFF55545A;
            g.fill(bx, by, bx + 100, by + 23, color);
            g.drawCenteredString(font, Component.literal("VENDER TUDO"), bx + 50, by + 7, 0xFFFFFFFF);
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
    @Override protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {}
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            for (int i = 0; i < 3; i++) {
                int bx = leftPos + 211, by = topPos + 71 + i * 38;
                if (mouseX >= bx && mouseX < bx + 100 && mouseY >= by && mouseY < by + 23) {
                    if (menu.getCount(i) > 0 && minecraft != null && minecraft.gameMode != null)
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
