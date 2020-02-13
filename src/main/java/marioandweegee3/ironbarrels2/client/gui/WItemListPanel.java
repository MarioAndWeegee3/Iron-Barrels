package marioandweegee3.ironbarrels2.client.gui;

import java.util.List;

import io.github.cottonmc.cotton.gui.widget.WItemSlot;
import io.github.cottonmc.cotton.gui.widget.WPanel;
import io.github.cottonmc.cotton.gui.widget.WWidget;
import io.github.cottonmc.cotton.gui.widget.data.Axis;

// Modification of https://www.curseforge.com/minecraft/mc-mods/crafting-station-but-its-fabric

public class WItemListPanel extends WPanel {

    private final WBetterScrollbar scrollBar = new WBetterScrollbar(Axis.VERTICAL);
    private int lastScroll = -1;
    private final List<WItemSlot> list;
    private final BigBarrelController gui;

    public WItemListPanel(List<WItemSlot> data, BigBarrelController gui) {
        this.list = data;
        this.gui = gui;
        scrollBar.setMaxValue(data.size());
    }

    @Override
    public void paintBackground(int x, int y, int mouseX, int mouseY) {
        if (scrollBar.getValue() != lastScroll) {
            gui.clearSlots();
            gui.triggerValidation();
            lastScroll = scrollBar.getValue();
        }

        for (WWidget child : children) {
            child.paintBackground(x + child.getX(), y + child.getY(), mouseX - child.getX(), mouseY - child.getY());
        }
    }

    @Override
    public void layout() {
        this.children.clear();
        this.children.add(scrollBar);
        int margin = 4;
        this.width = 18 * 9 + scrollBar.getWidth() + margin;
        this.height = 18 * 3;
        scrollBar.setLocation(this.width - scrollBar.getWidth(), margin);
        scrollBar.setSize(8, this.height - margin);
        scrollBar.setMaxValue(list.size());
        int layoutHeight = this.getHeight() - (margin * 2);
        int cellHeight = 18;
        int cellsHigh = layoutHeight / cellHeight;
        scrollBar.setWindow(cellsHigh);
        int scrollOffset = scrollBar.getValue();
        for (int i = 0; i < list.size(); i++) {
            WItemSlot slot = list.get(i);
            slot.setLocation(margin, margin + (cellHeight * (i - scrollOffset)));
            slot.setSize(this.width - (margin * 2) - scrollBar.getWidth(), cellHeight);
            if (slot.getY() >= 0 && slot.getY() <= this.height) {
                children.add(slot);
            }
        }
    }

}