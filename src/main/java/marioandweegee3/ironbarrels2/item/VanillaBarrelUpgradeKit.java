package marioandweegee3.ironbarrels2.item;

import java.util.List;

import marioandweegee3.ironbarrels2.block.BigBarrelBlock;
import marioandweegee3.ironbarrels2.block.entity.BigBarrelEntity;
import net.minecraft.block.BarrelBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BarrelBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class VanillaBarrelUpgradeKit extends Item {
    protected BigBarrelBlock block;

    public VanillaBarrelUpgradeKit(BigBarrelBlock block){
        super(new Settings().group(ItemGroup.MISC));
        this.block = block;
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);

        tooltip.add(new TranslatableText("text.ironbarrels2.upgrade.vanilla").setStyle(new Style().setColor(Formatting.field_1080)));
    }

    public boolean upgrade(World world, BlockPos pos, BlockState original, ItemStack stack){
        if(!(original.getBlock() instanceof BarrelBlock) || world.isClient) return false;

        Direction dir = original.get(BarrelBlock.FACING);

        BarrelBlockEntity barrel = (BarrelBlockEntity) world.getBlockEntity(pos);

        if(ChestBlockEntity.countViewers(world, barrel, pos.getX(), pos.getY(), pos.getZ()) > 0) return false;

        DefaultedList<ItemStack> barrelItems = DefaultedList.ofSize(barrel.getInvSize(), ItemStack.EMPTY);
        for(int i = 0; i < barrel.getInvSize(); i++){
            barrelItems.set(i, barrel.getInvStack(i).copy());
        }

        BigBarrelEntity bigBarrel = (BigBarrelEntity) block.createBlockEntity(world);

        Text name = barrel.getName();
        boolean hasCustom = barrel.hasCustomName();

        if(hasCustom){
            bigBarrel.name = name;
        }

        world.removeBlockEntity(pos);
        world.breakBlock(pos, false);

        world.setBlockState(pos, block.getDefaultState().with(BarrelBlock.FACING, dir));
        
        BlockEntity be = world.getBlockEntity(pos);
        if(be instanceof BigBarrelEntity){
            BigBarrelEntity barrelEntity = (BigBarrelEntity) be;
            barrelEntity.setItems(barrelItems);
            if(hasCustom){
                barrelEntity.name = name;
            }
        }

        return true;
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if(context.getPlayer().isSneaking()){
            World world = context.getWorld();
            BlockPos pos = context.getBlockPos();
            if(upgrade(world, pos, world.getBlockState(pos), context.getStack())){
                if(!context.getPlayer().isCreative()){
                    context.getStack().decrement(1);
                }

                return ActionResult.field_5812;
            }
        }
        return ActionResult.field_21466;
    }
}