package marioandweegee3.ironbarrels3.block.entity

import marioandweegee3.ironbarrels3.BarrelType
import marioandweegee3.ironbarrels3.block.BigBarrelBlock
import marioandweegee3.ironbarrels3.util.BasicSidedInventory
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.{DoubleInventory, Inventory, SidedInventory}
import net.minecraft.nbt.CompoundTag
import net.minecraft.sound.{SoundCategory, SoundEvents}
import net.minecraft.text.Text
import net.minecraft.util.Tickable
import net.minecraft.util.math.{BlockPos, Box}
import net.minecraft.world.World
import ninjaphenix.containerlib.ContainerLibrary
import ninjaphenix.containerlib.inventory.ScrollableContainer

class BigBarrelEntity(barrelType: BarrelType) extends BlockEntity(barrelType.getType) with Tickable {
	private var name:Text = barrelType.getDefaultName
	protected var viewerCount = 0
	
	protected var isOpen = false
	private val inv = new BasicSidedInventory(barrelType.getRows * 9)
	
	override def toTag(tag: CompoundTag): CompoundTag = {
		super.toTag(tag)
		tag.put("Inventory", inv.toTag(new CompoundTag()))
		tag
	}
	
	override def tick(): Unit = {
		assert(world != null)
		viewerCount = BigBarrelEntity.countViewers(world, this, pos)
		val state = getCachedState
		if(!world.isClient){
			if(viewerCount > 0){
				if(!state.get(BigBarrelBlock.OPEN)){
					world.setBlockState(pos, BigBarrelBlock.setOpen(state, open = true))
					world.playSound(null, pos, SoundEvents.BLOCK_BARREL_OPEN, SoundCategory.BLOCKS, 1, 1)
				}
			} else if(state.get(BigBarrelBlock.OPEN)){
				world.setBlockState(pos, BigBarrelBlock.setOpen(state, open = false))
				world.playSound(null, pos, SoundEvents.BLOCK_BARREL_CLOSE, SoundCategory.BLOCKS, 1, 1)
			}
		}
	}
	
	def getInventory:SidedInventory = inv
	
	def openContainerFor(player: PlayerEntity): Unit = {
		ContainerLibrary.openContainer(player, getPos, name)
	}
	
	def setName(text:Text): Unit = name = text
}

object BigBarrelEntity {
	def countViewers(world: World, barrelEntity: BigBarrelEntity, pos: BlockPos): Int = {
		var viewerCount = 0
		val players = world.getEntities(classOf[PlayerEntity], new Box(
			pos.getX.toFloat - 5.0F, pos.getY.toFloat - 5.0F, pos.getZ.toFloat - 5.0F,
			(pos.getX + 1).toFloat + 5.0F, (pos.getY + 1).toFloat + 5.0F, (pos.getZ + 1).toFloat + 5.0F),
			null)
		val itr = players.iterator
		while (true) {
			var inv:Inventory = null
			do {
				var player:PlayerEntity = null
				do {
					if (!itr.hasNext) return viewerCount
					player = itr.next
				} while ( {
					!player.container.isInstanceOf[ScrollableContainer]
				})
				inv = player.container.asInstanceOf[ScrollableContainer].getInventory
			} while ( {
				(inv != barrelEntity.inv) && (!inv.isInstanceOf[DoubleInventory] || !inv.asInstanceOf[DoubleInventory].isPart(barrelEntity.inv))
			})
			viewerCount += 1
		}
		viewerCount
	}
}
