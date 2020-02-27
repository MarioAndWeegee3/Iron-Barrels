package marioandweegee3.ironbarrels3;

import marioandweegee3.ironbarrels3.block.entity.BigBarrelEntity;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Lazy;

import java.util.function.Supplier;

public enum BarrelType {
    COPPER(5, IronBarrels::COPPER, new TranslatableText("block.ironbarrels3.copper_barrel")),
    IRON(6, IronBarrels::IRON, new TranslatableText("block.ironbarrels3.iron_barrel")),
    SILVER(8, IronBarrels::SILVER, new TranslatableText("block.ironbarrels3.silver_barrel")),
    GOLD(9, IronBarrels::GOLD, new TranslatableText("block.ironbarrels3.gold_barrel")),
    DIAMOND(12, IronBarrels::DIAMOND, new TranslatableText("block.ironbarrels3.diamond_barrel"))
    ;

    private int rows;
    private Supplier<BlockEntityType<BigBarrelEntity>> typeLazy;
    private Text defaultName;

    BarrelType(int rows, Supplier<BlockEntityType<BigBarrelEntity>> typeLazy, Text defaultName){
        this.rows = rows;
        this.typeLazy = typeLazy;
        this.defaultName = defaultName;
    }

    public int getRows() {
        return rows;
    }

    public BlockEntityType<BigBarrelEntity> getType() {
        return typeLazy.get();
    }

    public Text getDefaultName() {
        return defaultName;
    }

    public Block getBlock() {
        return IronBarrels.blocks().apply(name().toLowerCase() + "_barrel");
    }
}
