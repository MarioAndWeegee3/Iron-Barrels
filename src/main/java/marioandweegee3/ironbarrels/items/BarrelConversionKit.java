package marioandweegee3.ironbarrels.items;

import marioandweegee3.ironbarrels.IronBarrels;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class BarrelConversionKit extends Item{
    private final Identifier from, to;

    public BarrelConversionKit(Identifier from, Identifier to) {
        super(new Settings().maxCount(16).group(ItemGroup.MISC));
        this.from = from;
        this.to = to;
    }

    public static BarrelConversionKit create(String from, String to, boolean isFromFromThis, boolean isToFromThis){
        Identifier fromID = new Identifier(isFromFromThis?IronBarrels.modid:"minecraft", from);
        Identifier toID = new Identifier(isToFromThis?IronBarrels.modid:"minecraft", to);
        return new BarrelConversionKit(fromID, toID);
    }

    public static BarrelConversionKit create(String from, String to){
        return create(from, to, true, true);
    }

    private boolean isInput(Block block){
        if((block == findBlock(from)) || (block == Blocks.BARREL && from.equals(IronBarrels.makeID("wood_barrel")))) return true;
        return false;
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        BlockState state = world.getBlockState(pos);
        if(!context.getPlayer().isSneaking()) return ActionResult.PASS;

        if(isInput(state.getBlock())){
            BlockEntity entity = world.getBlockEntity(pos);
            DefaultedList<ItemStack> inventory = DefaultedList.create(((Inventory)entity).getInvSize(), ItemStack.EMPTY);

            Inventories.fromTag(entity.toTag(new CompoundTag()), inventory);
            world.removeBlockEntity(pos);
            world.setBlockState(pos, Registry.BLOCK.get(to).getDefaultState().with(Properties.FACING, state.get(Properties.FACING)));
            entity = world.getBlockEntity(pos);
            entity.fromTag(Inventories.toTag(entity.toTag(new CompoundTag()), inventory));

            ItemStack stack = context.getStack();
            if(!world.isClient){
                stack.decrement(1);
            }

            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }

    private Block findBlock(Identifier identifier){
        return Registry.BLOCK.get(identifier);
    }
    
}