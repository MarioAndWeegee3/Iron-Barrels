package marioandweegee3.ironbarrels2.item;

import java.util.List;

import marioandweegee3.ironbarrels2.block.BigBarrelBlock;
import marioandweegee3.ironbarrels2.block.entity.BigBarrelEntity;
import net.minecraft.block.BarrelBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
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

public class UpgradeKit extends Item {
    protected final BigBarrelBlock block1, block2;

    public UpgradeKit(BigBarrelBlock block1, BigBarrelBlock block2) {
        super(
            new Settings().group(ItemGroup.MISC)
        );

        this.block1 = block1;
        this.block2 = block2;
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);

        tooltip.add(getUpgradeText().setStyle(new Style().setColor(Formatting.GRAY)));
    }

    private TranslatableText getUpgradeText(){
        String text = "";
        switch(block1.index){
            case 0: text = "iron"; break;
            case 1: text = "gold"; break;
            case 2: text = "diamond"; break;
            case 3: text = "obsidian"; break;
            case 4: text = "copper"; break;
            case 5: text = "silver"; break;
        }

        return new TranslatableText("text.ironbarrels2.upgrade."+text);
    }

    public boolean upgrade(World world, BlockPos pos, BlockState original, ItemStack stack){
        if(!(original.getBlock() instanceof BigBarrelBlock) || world.isClient || original.getBlock() != block1) return false;

        Direction dir = original.get(BarrelBlock.FACING);

        BigBarrelEntity barrel = (BigBarrelEntity) world.getBlockEntity(pos);

        if(BigBarrelEntity.countViewers(world, barrel, pos) > 0) return false;

        DefaultedList<ItemStack> barrelItems = DefaultedList.ofSize(barrel.getInvSize(), ItemStack.EMPTY);
        for(int i = 0; i < barrel.getInvSize(); i++){
            barrelItems.set(i, barrel.getInvStack(i).copy());
        }

        BigBarrelEntity bigBarrel = (BigBarrelEntity) block2.createBlockEntity(world);

        Text name = barrel.getName();
        boolean hasCustom = barrel.hasCustomName();

        if(hasCustom){
            bigBarrel.name = name;
        }

        world.removeBlockEntity(pos);
        world.breakBlock(pos, false);

        world.setBlockState(pos, block2.getDefaultState().with(BarrelBlock.FACING, dir));
        
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

                return ActionResult.SUCCESS;
            }
        }
        return ActionResult.PASS;
    }

}