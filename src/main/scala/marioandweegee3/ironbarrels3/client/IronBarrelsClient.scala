package marioandweegee3.ironbarrels3.client

import com.swordglowsblue.artifice.api.Artifice
import com.swordglowsblue.artifice.api.ArtificeResourcePack.ClientResourcePackBuilder
import com.swordglowsblue.artifice.api.builder.assets.BlockStateBuilder.Variant
import com.swordglowsblue.artifice.api.builder.assets.{BlockStateBuilder, ModelBuilder}
import marioandweegee3.ironbarrels3.BarrelType
import marioandweegee3.ironbarrels3.IronBarrels._
import marioandweegee3.ironbarrels3.block.BigBarrelBlock
import net.fabricmc.api.ClientModInitializer
import net.minecraft.util.Identifier
import net.minecraft.util.math.Direction

object IronBarrelsClient extends ClientModInitializer{
	override def onInitializeClient(): Unit = {
		Artifice.registerAssets("ironbarrels3:assets", (pack:ClientResourcePackBuilder) => {
			for((name, block) <- blocks.filter((entry) => entry._2.isInstanceOf[BigBarrelBlock])){
				val id:Identifier = s"ironbarrels3:$name"
				pack.addBlockState(id, (state:BlockStateBuilder) => {
					for(direction <- Direction.values()){
						state.variant(s"facing=${direction.getName},open=true", (variant:Variant) => {
							variant.model(s"ironbarrels3:block/${name}_open")
							addDirectionToVariant(variant, direction)
						})
						state.variant(s"facing=${direction.getName},open=false", (variant:Variant) => {
							variant.model(s"ironbarrels3:block/$name")
							addDirectionToVariant(variant, direction)
						})
					}
				})
				
				val material:String = block.asInstanceOf[BigBarrelBlock].getBarrelType.name().toLowerCase()
				pack.addBlockModel(id, (model:ModelBuilder) => {
					model.parent("block/cube_bottom_top")
					model.texture("top", s"ironbarrels3:block/${material}/top")
					model.texture("side", s"ironbarrels3:block/${material}/side")
					model.texture("bottom", s"ironbarrels3:block/${material}/bottom")
				})
				pack.addBlockModel(s"ironbarrels3:${name}_open", (model:ModelBuilder) => {
					model.parent("block/cube_bottom_top")
					model.texture("top", s"ironbarrels3:block/${material}/top_open")
					model.texture("side", s"ironbarrels3:block/${material}/side")
					model.texture("bottom", s"ironbarrels3:block/${material}/bottom")
				})
				pack.addItemModel(id, (model:ModelBuilder) =>{
					model.parent(s"ironbarrels3:block/$name")
				})
			}
			
			for(b <- BarrelType.values()){
				val type1 = b.name().toLowerCase
				for(b2 <- BarrelType.values().filter((barrel:BarrelType) => b.compareTo(barrel) < 0)){
					val type2 = b2.name().toLowerCase
					
					pack.addItemModel(s"ironbarrels3:${type1}_to_${type2}_kit", (model:ModelBuilder) => {
						model.parent("item/generated")
						model.texture("layer0", "ironbarrels3:item/upgrade/base")
						model.texture("layer1", s"ironbarrels3:item/upgrade/from_$type1")
						model.texture("layer2", s"ironbarrels3:item/upgrade/to_$type2")
					})
				}
				
			}
		})
	}
	
	private def addDirectionToVariant(variant: Variant, direction: Direction):Unit = {
		direction match {
			case Direction.DOWN =>
				variant.rotationX(180)
			case Direction.EAST =>
				variant.rotationX(90)
				variant.rotationY(90)
			case Direction.NORTH =>
				variant.rotationX(90)
			case Direction.SOUTH =>
				variant.rotationX(90)
				variant.rotationY(180)
			case Direction.UP =>
			case Direction.WEST =>
				variant.rotationX(90)
				variant.rotationY(270)
		}
	}
}
