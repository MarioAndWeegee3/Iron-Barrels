package marioandweegee3.ironbarrels3.util

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.{Inventories, SidedInventory}
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundTag
import net.minecraft.util.DefaultedList
import net.minecraft.util.math.Direction

/**
 * Allows for the easy creation of instances of SidedInventory
 *
 * @param size : The size of the inventory
 * */
class BasicSidedInventory(size:Int) extends SidedInventory{
	override def getInvAvailableSlots(side: Direction): Array[Int] = (0 until size).toArray
	
	override def canInsertInvStack(slot: Int, stack: ItemStack, dir: Direction): Boolean = true
	
	override def canExtractInvStack(slot: Int, stack: ItemStack, dir: Direction): Boolean = true
	
	val stackList:DefaultedList[ItemStack] = DefaultedList.ofSize(size, ItemStack.EMPTY)
	
	def toTag(tag:CompoundTag):CompoundTag = Inventories.toTag(tag, stackList)
	
	def fromTag(tag:CompoundTag):Unit = Inventories.fromTag(tag, stackList)
	
	override def getInvStack(slot: Int): ItemStack =
		if (slot >= 0 && slot < this.stackList.size) this.stackList.get(slot)
		else ItemStack.EMPTY
	
	override def takeInvStack(slot: Int, amount: Int): ItemStack = {
		val itemStack = Inventories.splitStack(this.stackList, slot, amount)
		if (!itemStack.isEmpty) this.markDirty()
		itemStack
	}
	
	override def removeInvStack(slot: Int): ItemStack = {
		val itemStack = this.stackList.get(slot)
		if (itemStack.isEmpty) ItemStack.EMPTY
		else {
			this.stackList.set(slot, ItemStack.EMPTY)
			itemStack
		}
	}
	
	override def setInvStack(slot: Int, stack: ItemStack): Unit = {
		this.stackList.set(slot, stack)
		if (!stack.isEmpty && stack.getCount > this.getInvMaxStackAmount) stack.setCount(this.getInvMaxStackAmount)
		this.markDirty()
	}
	
	override def getInvSize: Int = this.size
	
	override def isInvEmpty: Boolean = {
		val var1 = this.stackList.iterator
		var itemStack:ItemStack = null
		do {
			if (!var1.hasNext) return true
			itemStack = var1.next
		} while ( {
			itemStack.isEmpty
		})
		false
	}
	
	override def canPlayerUseInv(player: PlayerEntity) = true
	
	override def clear(): Unit = {
		this.stackList.clear()
		this.markDirty()
	}
	
	override def markDirty(): Unit = ()
}
