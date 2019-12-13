package marioandweegee3.ironbarrels2.client.gui;

import io.github.cottonmc.cotton.gui.CottonCraftingController;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WItemSlot;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import marioandweegee3.ironbarrels2.block.entity.BigBarrelEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.container.BlockContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;

public class BigBarrelController extends CottonCraftingController {
    BigBarrelEntity barrelEntity;

    public BigBarrelController(int syncId, PlayerInventory playerInv, int rows, int columns, BlockContext ctx){
        super(null, syncId, playerInv, getBlockInventory(ctx), getBlockPropertyDelegate(ctx));

        barrelEntity = ctx.run((world, pos)->{
            BlockEntity be = world.getBlockEntity(pos);

            if(be instanceof BigBarrelEntity){
                return (BigBarrelEntity)be;
            }

            else return null;
        }).orElse(null);

        barrelEntity.onInvOpen(playerInv.player);
        
        WGridPanel root = (WGridPanel) getRootPanel();

        root.add(new WLabel(barrelEntity.name, WLabel.DEFAULT_TEXT_COLOR), 0, 0);
        
        WItemSlot invSlots = WItemSlot.of(blockInventory, 0, columns, rows);
        root.add(invSlots, 0, 1);

        // List<WItemSlot> slots = new ArrayList<>();

        // for(int i = 0; i < barrelEntity.getInvSize() / 9; i++){
        //     slots.add(WItemSlot.of(blockInventory, i, 9, 1));
        // }

        // WItemListPanel inv = new WItemListPanel(slots, this);
        // root.add(inv, 0, 1);

        root.add(this.createPlayerInventoryPanel(), columns/2 - 4, rows + 2);
        // root.add(this.createPlayerInventoryPanel(), 0, 8);

        root.validate(this);
    }

    public void clearSlots(){
        this.slotList.clear();
    }

    public void triggerValidation(){
        rootPanel.validate(this);
        onContentChanged(blockInventory);
    }

    @Override
    public int getCraftingHeight() {
        return 0;
    }

    @Override
    public int getCraftingResultSlotIndex() {
        return -1;
    }

    @Override
    public int getCraftingSlotCount() {
        return 0;
    }

    @Override
    public int getCraftingWidth() {
        return 0;
    }

    @Override
    public void close(PlayerEntity player) {
        barrelEntity.onInvClose(player);

        super.close(player);
    }
}