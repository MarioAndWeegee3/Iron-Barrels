package marioandweegee3.ironbarrels3

import com.swordglowsblue.artifice.api.Artifice
import com.swordglowsblue.artifice.api.ArtificeResourcePack.ServerResourcePackBuilder
import com.swordglowsblue.artifice.api.builder.data.TagBuilder
import com.swordglowsblue.artifice.api.builder.data.recipe.ShapedRecipeBuilder
import marioandweegee3.ironbarrels3.block.BigBarrelBlock
import marioandweegee3.ironbarrels3.block.entity.BigBarrelEntity
import marioandweegee3.ironbarrels3.items.{UpgradeKit, VanillaUpgradeKit}
import marioandweegee3.ml3api.registry.RegistryHelper
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.block.FabricBlockSettings
import net.fabricmc.fabric.api.tools.FabricToolTags
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.block.{Block, Blocks}
import net.minecraft.item.{Item, ItemGroup}
import net.minecraft.tag.ItemTags
import net.minecraft.util.Identifier

import scala.collection.mutable
import scala.jdk.CollectionConverters._

object IronBarrels extends ModInitializer {
	val barrelSettings:Block.Settings = FabricBlockSettings.copy(Blocks.IRON_DOOR).breakByTool(FabricToolTags.PICKAXES).breakByHand(true).build()
	
	val blocks: Map[String, Block] = Map[String, Block](
		("copper_barrel", new BigBarrelBlock(barrelSettings, 0)),
		("iron_barrel", new BigBarrelBlock(barrelSettings, 1)),
		("silver_barrel", new BigBarrelBlock(barrelSettings, 2)),
		("gold_barrel", new BigBarrelBlock(barrelSettings, 3)),
		("diamond_barrel", new BigBarrelBlock(barrelSettings, 4))
	)
	
	val COPPER:BlockEntityType[BigBarrelEntity] = new BlockEntityType[BigBarrelEntity](
		() => new BigBarrelEntity(BarrelType.COPPER),
		mutable.HashSet[Block](
			blocks("copper_barrel")
		).asJava,
		null
	)
	val IRON:BlockEntityType[BigBarrelEntity] = new BlockEntityType[BigBarrelEntity](
		() => new BigBarrelEntity(BarrelType.IRON),
		mutable.HashSet[Block](
			blocks("iron_barrel")
		).asJava,
		null
	)
	val SILVER:BlockEntityType[BigBarrelEntity] = new BlockEntityType[BigBarrelEntity](
		() => new BigBarrelEntity(BarrelType.SILVER),
		mutable.HashSet[Block](
			blocks("silver_barrel")
		).asJava,
		null
	)
	val GOLD:BlockEntityType[BigBarrelEntity] = new BlockEntityType[BigBarrelEntity](
		() => new BigBarrelEntity(BarrelType.GOLD),
		mutable.HashSet[Block](
			blocks("gold_barrel")
		).asJava,
		null
	)
	val DIAMOND:BlockEntityType[BigBarrelEntity] = new BlockEntityType[BigBarrelEntity](
		() => new BigBarrelEntity(BarrelType.DIAMOND),
		mutable.HashSet[Block](
			blocks("diamond_barrel")
		).asJava,
		null
	)
	
	val helper = new RegistryHelper("ironbarrels3")
	
	override def onInitialize(): Unit = {
		helper.registerAllBlocks(blocks.asJava, ItemGroup.DECORATIONS)
		
		helper.registerBlockEntity("copper_barrel", COPPER)
		helper.registerBlockEntity("iron_barrel", IRON)
		helper.registerBlockEntity("silver_barrel", SILVER)
		helper.registerBlockEntity("gold_barrel", GOLD)
		helper.registerBlockEntity("diamond_barrel", DIAMOND)
		
		Artifice.registerData(helper.makeId("data"), (pack:ServerResourcePackBuilder) => {
			pack.addItemTag("c:barrels", (tag:TagBuilder) => {
				tag.value("barrel")
			})
			pack.addBlockTag("c:barrels", (tag:TagBuilder) => {
				tag.value("barrel")
			})
			if(itemTagExists("c:copper_ingot")){
				pack.addShapedRecipe("ironbarrels3:wood_to_copper_kit", (recipe:ShapedRecipeBuilder) => {
					recipe.pattern(
						"cpc"
					)
					recipe.ingredientTag('p', "planks")
					recipe.ingredientTag('c', "c:copper_ingot")
					recipe.result("ironbarrels3:wood_to_copper_kit", 1)
				})
				pack.addShapedRecipe("ironbarrels3:copper_barrel", (recipe:ShapedRecipeBuilder) => {
					recipe.pattern(
						"cpc"
					)
					recipe.ingredientTag('p', "c:barrels")
					recipe.ingredientTag('c', "c:copper_ingot")
					recipe.result("ironbarrels3:copper_barrel", 1)
				})
			}
		})
		
		val kitSettings = new Item.Settings().group(ItemGroup.MISC)
		
		for(b <- BarrelType.values()){
			for(b2 <- BarrelType.values().filter((barrel:BarrelType) => b.compareTo(barrel) < 0)){
				val type1 = b.name().toLowerCase
				val type2 = b2.name().toLowerCase
				
				helper.registerItem(s"${type1}_to_${type2}_kit", new UpgradeKit(kitSettings, b, b2))
			}
			helper.registerItem(s"wood_to_${b.name().toLowerCase}_kit", new VanillaUpgradeKit(kitSettings, b))
		}
	}
	
	def itemTagExists(id:Identifier):Boolean = ItemTags.getContainer.getKeys.contains(id)
	
	@inline
	implicit def stringsToId(strings:(String, String)):Identifier = new Identifier(strings._1, strings._2)
	
	@inline
	implicit def stringToId(string: String):Identifier = new Identifier(string)
	
	@inline
	implicit def idToStrings(identifier: Identifier):(String, String) = (identifier.getNamespace, identifier.getPath)
}
