package marioandweegee3.ironbarrels3.items

import marioandweegee3.ironbarrels3.BarrelType
import marioandweegee3.ironbarrels3.block.BigBarrelBlock
import marioandweegee3.ironbarrels3.block.entity.BigBarrelEntity
import net.minecraft.item.{Item, ItemUsageContext}
import net.minecraft.item.Item.Settings
import net.minecraft.state.property.Properties
import net.minecraft.util.ActionResult

class UpgradeKit(settings: Settings, from: BarrelType, to: BarrelType) extends Item(settings) {
	override def useOnBlock(context: ItemUsageContext): ActionResult = {
		val world = context.getWorld
		val pos = context.getBlockPos
		val block = world.getBlockState(pos).getBlock
		val stack = context.getStack
		if(block.isInstanceOf[BigBarrelBlock] && block == from.getBlock && !world.isClient){
			val inv = world.getBlockEntity(pos).asInstanceOf[BigBarrelEntity].getInventory
			
			world.setBlockState(pos, to.getBlock.getDefaultState.`with`(Properties.FACING, world.getBlockState(pos).get(Properties.FACING)))
			val inv2 = world.getBlockEntity(pos).asInstanceOf[BigBarrelEntity].getInventory
			
			for(i <- 0 until inv.getInvSize){
				inv2.setInvStack(i, inv.getInvStack(i))
			}
			stack.decrement(1)
			ActionResult.SUCCESS
		} else ActionResult.PASS
	}
}
