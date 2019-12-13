package marioandweegee3.ironbarrels2.block.entity;

import marioandweegee3.ironbarrels2.IronBarrels;
import marioandweegee3.ironbarrels2.block.BigBarrelBlock;
import net.minecraft.block.entity.BlockEntityType;

public class BarrelEntities {
    public static BigBarrelEntity barrel(int index){
        return new BigBarrelEntity(getType(index), BigBarrelBlock.BARRELS[index]);
    }

    protected static BlockEntityType<BigBarrelEntity> getType(int index){
        switch(index){
            case 0: return IronBarrels.IRON_BARREL;
            case 1: return IronBarrels.GOLD_BARREL;
            case 2: return IronBarrels.DIAMOND_BARREL;
            case 3: return IronBarrels.OBSIDIAN_BARREL;
            case 4: return IronBarrels.COPPER_BARREL;
            case 5: return IronBarrels.SILVER_BARREL;
        }
        IronBarrels.log("Invalid index "+index);
        return null;
    }
}