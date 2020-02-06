package marioandweegee3.ironbarrels2.client.gui;

import io.github.cottonmc.cotton.gui.widget.WScrollBar;
import io.github.cottonmc.cotton.gui.widget.data.Axis;

// Taken from https://www.curseforge.com/minecraft/mc-mods/crafting-station-but-its-fabric

public class WBetterScrollbar extends WScrollBar {

    public WBetterScrollbar(Axis vertical) {
        super(vertical);
    }

    public void setWindow(int wind) {
        window = wind;
    }
}
