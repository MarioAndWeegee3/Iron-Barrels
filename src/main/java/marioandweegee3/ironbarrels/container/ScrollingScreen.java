package marioandweegee3.ironbarrels.container;

import gd.rf.ninjaphenix.cursedchests.api.client.gui.container.ScrollableScreen;
import gd.rf.ninjaphenix.cursedchests.api.container.ScrollableContainer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.chat.Component;

public class ScrollingScreen extends ScrollableScreen {

    public ScrollingScreen(ScrollableContainer container, PlayerInventory playerInventory, Component containerTitle) {
        super(container, playerInventory, containerTitle);
    }
    
}