package marioandweegee3.ironbarrels.blocks.entities;

import marioandweegee3.ironbarrels.IronBarrels;
import marioandweegee3.ironbarrels.blocks.BigBarrel;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.container.Container;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;

public class BigBarrelEntity extends LootableContainerBlockEntity implements SidedInventory, Tickable{
    private Component defaultContainerName;
    private String materialName;
    private int invSize;
    private DefaultedList<ItemStack> inventory;
    private int[] slots;
    private boolean isOpen;

    public BigBarrelEntity(){
        this("wood", 3);
    }

    public BigBarrelEntity(String materialName, int numRows){
        super(Registry.BLOCK_ENTITY.get(IronBarrels.makeID("big_barrels")));
        defaultContainerName = new TranslatableComponent("ironbarrels."+materialName, new Object[0]);
        invSize = 9*numRows;
        this.materialName = materialName;
        inventory = DefaultedList.create(invSize, ItemStack.EMPTY);
        slots = new int[invSize];
        for(int i = 0; i<invSize; i++) slots[i] = i;
        this.isOpen = false;
    }

    public String getMaterial(){return materialName;};
    public void setMaterial(String newMaterial){this.materialName = newMaterial;}

    @Override
    protected DefaultedList<ItemStack> getInvStackList() {
        return inventory;
    }

    @Override
    protected void setInvStackList(DefaultedList<ItemStack> list) {
        this.inventory = list;
    }

    @Override
    protected Container createContainer(int arg0, PlayerInventory arg1) {
        return null;
    }

    @Override
    public int[] getInvAvailableSlots(Direction arg0) {
        return slots;
    }

    @Override
    public boolean canInsertInvStack(int slot, ItemStack stack, Direction dir) {
        return this.isValidInvStack(slot, stack);
    }

    @Override
    public boolean canExtractInvStack(int arg0, ItemStack arg1, Direction arg2) {
        return true;
    }

    @Override
    public int getInvSize() {
        return invSize;
    }

    @Override
    protected Component getContainerName() {
        return defaultContainerName;
    }

    @Override
    public boolean isInvEmpty() {
        for(ItemStack stack : inventory){
            if(!stack.isEmpty()) return false;
        }
        return true;
    }

    private void playSound(SoundEvent event){
        world.playSound(null, pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5, event, SoundCategory.BLOCKS, 0.5f, world.random.nextFloat()*0.1f);
    }

    @Override
    public void onInvOpen(PlayerEntity player) {
        playSound(SoundEvents.BLOCK_BARREL_OPEN);
        this.isOpen = true;
    }

    @Override
    public void onInvClose(PlayerEntity playerEntity_1) {
        playSound(SoundEvents.BLOCK_BARREL_CLOSE);
        this.isOpen = false;
    }

    @Override
    public void tick() {
        BlockState state = this.getCachedState();
        setOpen(state, isOpen);
    }

    public void setOpen(BlockState state, boolean open){
        this.world.setBlockState(this.pos, state.with(BigBarrel.OPEN, open), 3);
    }
}