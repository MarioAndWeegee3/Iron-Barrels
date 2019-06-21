package marioandweegee3.ironbarrels.blocks;

import marioandweegee3.ironbarrels.IronBarrels;
import marioandweegee3.ironbarrels.blocks.entities.BigBarrelEntity;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.registry.Registry;

public class Barrels {
    public static int numBarrels = 0;
    
    public static BigBarrel ironBarrel;
    public static BigBarrel goldBarrel;
    public static BigBarrel diamondBarrel;
    public static BigBarrel obsidianBarrel;

    public static BlockEntityType<BigBarrelEntity> big_barrels;

    public static final String[] materials = {
        "wood",
        "iron",
        "gold",
        "diamond",
        "obsidian"
    };

    public static void registerBlocks(){
        IronBarrels.logger.info("Registering barrels!");
        ironBarrel = register(Blocks.IRON_BLOCK, "iron", 6);
        goldBarrel = register(Blocks.GOLD_BLOCK, "gold", 9);
        diamondBarrel = register(Blocks.DIAMOND_BLOCK, "diamond", 12);
        obsidianBarrel = register(Blocks.OBSIDIAN, "obsidian", 15);

        big_barrels = Registry.register(Registry.BLOCK_ENTITY, IronBarrels.makeID("big_barrels"), BlockEntityType.Builder.create(BigBarrelEntity::new, ironBarrel, goldBarrel, diamondBarrel, obsidianBarrel).build(null));
    }

    private static BigBarrel register(Block source, String material, int numRows){
        numBarrels++;
        return IronBarrels.registerBarrel(source, material, numRows);
    }
}