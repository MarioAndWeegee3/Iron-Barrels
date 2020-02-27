package marioandweegee3.ironbarrels3.block

import java.lang.{Boolean => bool}

import marioandweegee3.ironbarrels3.BarrelType
import marioandweegee3.ironbarrels3.block.entity.BigBarrelEntity
import net.minecraft.block.Block.Settings
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.{Block, BlockEntityProvider, BlockState, InventoryProvider}
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.SidedInventory
import net.minecraft.item.ItemPlacementContext
import net.minecraft.state.StateManager
import net.minecraft.state.property.{BooleanProperty, DirectionProperty, Properties}
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.{BlockPos, Direction}
import net.minecraft.util.{ActionResult, Hand, ItemScatterer}
import net.minecraft.world.{BlockView, IWorld, World}

class BigBarrelBlock(settings:Settings, barType:Int) extends Block(settings) with BlockEntityProvider with InventoryProvider {
	setDefaultState(BigBarrelBlock.setOpen(getDefaultState.`with`(BigBarrelBlock.FACING, Direction.NORTH), open = false))
	
	override def appendProperties(builder: StateManager.Builder[Block, BlockState]): Unit = {
		builder.add(BigBarrelBlock.FACING, BigBarrelBlock.OPEN)
	}
	
	override def createBlockEntity(view: BlockView): BlockEntity = getBarrelType.getType.instantiate()
	
	override def onBreak(world: World, pos: BlockPos, state: BlockState, player: PlayerEntity): Unit = {
		if(!world.isClient){
			world.getBlockEntity(pos) match {
				case barrel:BigBarrelEntity => ItemScatterer.spawn(world, pos, barrel.getInventory)
				case e if(e != null) => e.markInvalid()
				case _ =>
			}
		}
		super.onBreak(world, pos, state, player)
	}
	
	override def getInventory(state: BlockState, world: IWorld, pos: BlockPos): SidedInventory = {
		assert(world != null)
		world.getBlockEntity(pos) match {
			case barrel:BigBarrelEntity => barrel.getInventory
			case e if(e != null) => e.markInvalid(); null
			case _ => null
		}
	}
	
	override def getPlacementState(ctx: ItemPlacementContext): BlockState = {
		getDefaultState.`with`(BigBarrelBlock.FACING, ctx.getPlayerLookDirection.getOpposite)
	}
	
	override def onUse(state: BlockState, world: World, pos: BlockPos, player: PlayerEntity, hand: Hand, hit: BlockHitResult): ActionResult = {
		if(!world.isClient){
			world.getBlockEntity(pos) match {
				case barrel:BigBarrelEntity => barrel.openContainerFor(player)
				case e if(e != null) => e.markInvalid()
				case _ =>
			}
		}
		ActionResult.SUCCESS
	}
	
	def getBarrelType:BarrelType = BarrelType.values()(barType)
	
}

object BigBarrelBlock {
	val FACING: DirectionProperty = Properties.FACING
	val OPEN: BooleanProperty = Properties.OPEN
	
	def setOpen(state: BlockState, open: Boolean): BlockState = state.`with`(BigBarrelBlock.OPEN, if(open) bool.TRUE else bool.FALSE)
}
