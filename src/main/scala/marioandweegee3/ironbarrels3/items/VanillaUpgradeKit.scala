package marioandweegee3.ironbarrels3.items

import marioandweegee3.ironbarrels3.IronBarrels._
import marioandweegee3.ironbarrels3.BarrelType
import marioandweegee3.ironbarrels3.block.BigBarrelBlock
import marioandweegee3.ironbarrels3.block.entity.BigBarrelEntity
import net.minecraft.block.{BlockEntityProvider, InventoryProvider}
import net.minecraft.inventory.Inventory
import net.minecraft.item.{Item, ItemUsageContext}
import net.minecraft.item.Item.Settings
import net.minecraft.state.property.Properties
import net.minecraft.tag.BlockTags
import net.minecraft.util.ActionResult

class VanillaUpgradeKit(settings: Settings, to: BarrelType) extends Item(settings) {
	override def useOnBlock(context: ItemUsageContext): ActionResult = {
		val world = context.getWorld
		val pos = context.getBlockPos
		val block = world.getBlockState(pos).getBlock
		val stack = context.getStack
		if(BlockTags.getContainer.get("c:barrels").contains(block) && !world.isClient){
			val inv:Inventory = block match {
				case i:InventoryProvider => i.getInventory(world.getBlockState(pos), world, pos)
				case _:BlockEntityProvider | _ if block.hasBlockEntity =>
					world.getBlockEntity(pos) match {
						case inventory: Inventory => inventory
						case _ => null
					}
				case _ => null
			}
			
			if(inv == null) return ActionResult.PASS
			
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
