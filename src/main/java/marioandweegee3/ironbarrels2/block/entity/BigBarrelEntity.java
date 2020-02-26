package marioandweegee3.ironbarrels2.block.entity;

import java.util.Iterator;
import java.util.List;
import java.util.stream.IntStream;

import marioandweegee3.ironbarrels2.IronBarrels;
import marioandweegee3.ironbarrels2.block.BigBarrelBlock;
import net.minecraft.block.BarrelBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.container.GenericContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.DoubleInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Nameable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

public class BigBarrelEntity extends BlockEntity implements SidedInventory, Nameable {

    private Text name;

    protected DefaultedList<ItemStack> inv;
    protected int viewerCount;

    protected boolean isOpen;

    public BigBarrelEntity(BlockEntityType<? extends BigBarrelEntity> type, Block block){
        super(type);
        this.setName(new TranslatableText(block.getTranslationKey()));

        int rows;

        if(block instanceof BigBarrelBlock){
            BigBarrelBlock barrelBlock = (BigBarrelBlock) block;
            rows = barrelBlock.getRows();
        } else {
            rows = 1;
        }
        this.inv = DefaultedList.ofSize(rows * 9, ItemStack.EMPTY);
    }

    public void setItems(DefaultedList<ItemStack> items){
        for(int i = 0; i < items.size(); i++){
            inv.set(i, items.get(i).copy());
        }
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        tag = super.toTag(tag);
        Inventories.toTag(tag, inv);
        return tag;
    }

    @Override
    public void fromTag(CompoundTag tag) {
        super.fromTag(tag);
        Inventories.fromTag(tag, inv);
        markDirty();
    }

    @Override
    public int getInvSize() {
        return inv.size();
    }

    @Override
    public boolean isInvEmpty() {
        return inv.isEmpty();
    }

    @Override
    public boolean hasCustomName() {
        return true;
    }

    public void update() {
        assert world != null;
        this.viewerCount = countViewers(world, this, pos);
        if (this.viewerCount > 0) {
            this.scheduleUpdate();
        } else {
            BlockState state = this.getCachedState();
            if (!(state.getBlock() instanceof BigBarrelBlock)) {
                this.markInvalid();
            }
        }
    }

    public static int countViewers(World world, BigBarrelEntity barrelEntity, BlockPos pos) {
        int viewerCount = 0;
        List<PlayerEntity> players = world.getEntities(PlayerEntity.class,
                new Box((float) pos.getX() - 5.0F, (float) pos.getY() - 5.0F,
                        (float) pos.getZ() - 5.0F, (float) (pos.getX() + 1) + 5.0F,
                        (float) (pos.getY() + 1) + 5.0F, (float) (pos.getZ() + 1) + 5.0F), null);
        Iterator<PlayerEntity> itr = players.iterator();

        while (true) {
            Inventory inv;
            do {
                PlayerEntity player;
                do {
                    if (!itr.hasNext()) {
                        return viewerCount;
                    }

                    player = itr.next();
                } while (!(player.container instanceof GenericContainer));

                inv = ((GenericContainer) player.container).getInventory();
            } while (inv != barrelEntity && (!(inv instanceof DoubleInventory)
                    || !((DoubleInventory) inv).isPart(barrelEntity)));

            ++viewerCount;
        }
    }

    private void scheduleUpdate() {
        assert this.world != null;
        this.world.getBlockTickScheduler().schedule(this.getPos(), this.getCachedState().getBlock(), 5);
    }

    public void onInvOpen(PlayerEntity playerEntity_1) {
        if (!playerEntity_1.isSpectator()) {
            if (this.viewerCount < 0) {
                this.viewerCount = 0;
            }

            ++this.viewerCount;

            isOpen = true;
            assert world != null;
            BlockState state = world.getBlockState(this.pos);
            playSound(state, SoundEvents.BLOCK_BARREL_OPEN);

            this.scheduleUpdate();
        }

    }

    public void onInvClose(PlayerEntity player) {
        if (!player.isSpectator()) {
            --this.viewerCount;
        }

        if(this.viewerCount <= 0){
            this.viewerCount = 0;
            isOpen = false;
            playSound(player.world.getBlockState(this.pos), SoundEvents.BLOCK_BARREL_CLOSE);
        }
        this.scheduleUpdate();
    }

    public boolean isOpen(){
        return isOpen;
    }

    private void playSound(BlockState blockState_1, SoundEvent soundEvent_1) {
        Vec3i vec3i_1 = blockState_1.get(BarrelBlock.FACING).getVector();
        double double_1 = (double) this.pos.getX() + 0.5D + (double) vec3i_1.getX() / 2.0D;
        double double_2 = (double) this.pos.getY() + 0.5D + (double) vec3i_1.getY() / 2.0D;
        double double_3 = (double) this.pos.getZ() + 0.5D + (double) vec3i_1.getZ() / 2.0D;
        assert this.world != null;
        this.world.playSound(null, double_1, double_2, double_3, soundEvent_1, SoundCategory.BLOCKS,
                0.5F, this.world.random.nextFloat() * 0.1F + 0.9F);
    }

    @Override
    public void clear() {
        inv.clear();
    }

    @Override
    public Text getDisplayName() {
        return getName();
    }

    @Override
    public ItemStack getInvStack(int slot) {
        if(slot < 0 || slot >= inv.size()){
            IronBarrels.log("Invalid Slot "+slot);
            return ItemStack.EMPTY;
        }
        return inv.get(slot);
    }

    @Override
    public ItemStack takeInvStack(int slot, int amount) {
        markDirty();
        return Inventories.splitStack(this.inv, slot, amount);
    }

    @Override
    public ItemStack removeInvStack(int slot) {
        markDirty();
        return Inventories.removeStack(this.inv, slot);
    }

    @Override
    public void setInvStack(int slot, ItemStack stack) {
        if(slot < 0 || slot >= inv.size()){
            IronBarrels.log("Invalid Slot "+slot);
            return;
        }

        this.inv.set(slot, stack);
        if (stack.getCount() > this.getInvMaxStackAmount()) {
            stack.setCount(this.getInvMaxStackAmount());
        }
        markDirty();
    }

    @Override
    public boolean canPlayerUseInv(PlayerEntity player) {
        assert this.world != null;
        if (this.world.getBlockEntity(this.pos) != this) {
            return false;
        } else {
            return player.squaredDistanceTo((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D,
                    (double) this.pos.getZ() + 0.5D) <= 64.0D;
        }
    }

    @Override
    public Text getName() {
        return name;
    }

    public void setName(Text name) {
        this.name = name;
    }

    @Override
    public int[] getInvAvailableSlots(Direction side) {
        return IntStream.range(0, inv.size()).toArray();
    }

    @Override
    public boolean canInsertInvStack(int slot, ItemStack stack, Direction dir) {
        return true;
    }

    @Override
    public boolean canExtractInvStack(int slot, ItemStack stack, Direction dir) {
        return true;
    }
}