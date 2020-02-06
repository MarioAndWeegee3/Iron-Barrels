package marioandweegee3.ironbarrels2.client.gui;

import java.util.ArrayList;
import java.util.List;

import io.github.cottonmc.cotton.gui.CottonCraftingController;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WItemSlot;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import marioandweegee3.ironbarrels2.block.entity.BigBarrelEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.container.BlockContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;

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

        root.add(new WLabel(barrelEntity.name, WLabel.DEFAULT_DARKMODE_TEXT_COLOR), 0, -1);
        
        // WItemSlot invSlots = WItemSlot.of(blockInventory, 0, columns, rows);
        // root.add(invSlots, 0, 1);

        List<ItemStack[]> itemRows = new ArrayList<>();

        for(int i = 0; i < barrelEntity.getInvSize() / 9; i++){
            List<ItemStack> stacks = new ArrayList<>();
            for(int slot = i; slot < i + 9; slot++){
                stacks.add(barrelEntity.getInvStack(slot));
            }
            itemRows.add(stacks.toArray(new ItemStack[0]));
        }

        ArrayList<WItemSlot> defList = new ArrayList<>();
        if (barrelEntity != null) {
            for (int i = 0; i < barrelEntity.getInvSize() / 9; i++) {
                defList.add(new WItemSlot(barrelEntity, i * 9, 9, 1, false, false));
            }
            root.add(new WItemListPanel(defList, this), 0, 0);
        }

        // root.add(this.createPlayerInventoryPanel(), columns/2 - 4, rows + 2);
        root.add(this.createPlayerInventoryPanel(), 0, 4);

        root.validate(this);
    }

    public void clearSlots(){
        this.slots.clear();
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