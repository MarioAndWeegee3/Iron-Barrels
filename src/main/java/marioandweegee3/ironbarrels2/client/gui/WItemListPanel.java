package marioandweegee3.ironbarrels2.client.gui;

import java.util.List;

import io.github.cottonmc.cotton.gui.client.ScreenDrawing;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WItemSlot;
import io.github.cottonmc.cotton.gui.widget.WScrollBar;
import io.github.cottonmc.cotton.gui.widget.WWidget;
import io.github.cottonmc.cotton.gui.widget.data.Axis;

public class WItemListPanel extends WGridPanel {
    private final WScrollBar scrollBar = new WScrollBar(Axis.VERTICAL);
    private int lastScroll = -1;
    private final List<WItemSlot> list;
    private final BigBarrelController gui;

    public WItemListPanel(List<WItemSlot> list, BigBarrelController gui){
        this.list = list;
        this.gui = gui;
        scrollBar.setMaxValue(list.size());
    }

    @Override
    public void paintBackground(int x, int y, int mouseX, int mouseY) {
        if(getBackgroundPainter() != null){
            getBackgroundPainter().paintBackground(x, y, this);
        } else {
            ScreenDrawing.drawBeveledPanel(x, y, width, height);
        }

        if(scrollBar.getValue() != lastScroll){
            gui.clearSlots();
            gui.triggerValidation();
            lastScroll = scrollBar.getValue();
        }

        for(WWidget child : children){
            if(child.getY() >= 0 && child.getY() <= 5*grid) {
                child.paintBackground(x + child.getX(), y + child.getY(), mouseX - child.getX(), mouseY - child.getY());
            }
        }
    }

    @Override
    public void layout() {
        this.children.clear();
        this.add(scrollBar, 9, 0, 1, 6);
        int scrollOffset = scrollBar.getValue();
        for (int i = 0; i < list.size(); i++) {
            WItemSlot slot = list.get(i);
            this.add(slot, 0, i - scrollOffset);
        }
    }
}