package marioandweegee3.ironbarrels2;

import marioandweegee3.ironbarrels2.block.BigBarrelBlock;
import marioandweegee3.ironbarrels2.block.entity.BarrelEntities;
import marioandweegee3.ironbarrels2.block.entity.BigBarrelEntity;
import marioandweegee3.ironbarrels2.item.UpgradeKit;
import marioandweegee3.ironbarrels2.item.VanillaBarrelUpgradeKit;
import marioandweegee3.ml3api.registry.RegistryHelper;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class IronBarrels implements ModInitializer {
    public static final String modid = "ironbarrels2";
    public static final RegistryHelper helper = new RegistryHelper(modid);
    public static final Logger logger = helper.logger;

    public static final BlockEntityType<BigBarrelEntity> IRON_BARREL = BlockEntityType.Builder
            .create(() -> BarrelEntities.barrel(0), BigBarrelBlock.BARRELS[0]).build(null);
    public static final BlockEntityType<BigBarrelEntity> GOLD_BARREL = BlockEntityType.Builder
            .create(() -> BarrelEntities.barrel(1), BigBarrelBlock.BARRELS[1]).build(null);
    public static final BlockEntityType<BigBarrelEntity> DIAMOND_BARREL = BlockEntityType.Builder
            .create(() -> BarrelEntities.barrel(2), BigBarrelBlock.BARRELS[2]).build(null);
    public static final BlockEntityType<BigBarrelEntity> OBSIDIAN_BARREL = BlockEntityType.Builder
            .create(() -> BarrelEntities.barrel(3), BigBarrelBlock.BARRELS[3]).build(null);
    public static final BlockEntityType<BigBarrelEntity> COPPER_BARREL = BlockEntityType.Builder
            .create(() -> BarrelEntities.barrel(4), BigBarrelBlock.BARRELS[4]).build(null);
    public static final BlockEntityType<BigBarrelEntity> SILVER_BARREL = BlockEntityType.Builder
            .create(() -> BarrelEntities.barrel(5), BigBarrelBlock.BARRELS[5]).build(null);

    public static final Item debug_item = new Item(new Item.Settings().maxCount(1));

    @Override
    public void onInitialize() {

        helper.registerBlockEntity("iron_barrel", IRON_BARREL);
        helper.registerBlockEntity("gold_barrel", GOLD_BARREL);
        helper.registerBlockEntity("diamond_barrel", DIAMOND_BARREL);
        helper.registerBlockEntity("obsidian_barrel", OBSIDIAN_BARREL);
        helper.registerBlockEntity("copper_barrel", COPPER_BARREL);
        helper.registerBlockEntity("silver_barrel", SILVER_BARREL);

        Map<String, Block> barrels = new HashMap<>();
        barrels.put("iron_barrel", BigBarrelBlock.BARRELS[0]);
        barrels.put("gold_barrel", BigBarrelBlock.BARRELS[1]);
        barrels.put("diamond_barrel", BigBarrelBlock.BARRELS[2]);
        barrels.put("obsidian_barrel", BigBarrelBlock.BARRELS[3]);
        barrels.put("copper_barrel", BigBarrelBlock.BARRELS[4]);
        barrels.put("silver_barrel", BigBarrelBlock.BARRELS[5]);
        helper.registerAllBlocks(barrels, ItemGroup.DECORATIONS);

        helper.registerItem("wood_copper_kit", new VanillaBarrelUpgradeKit(BigBarrelBlock.BARRELS[4]));
        helper.registerItem("wood_iron_kit", new VanillaBarrelUpgradeKit(BigBarrelBlock.BARRELS[0]));
        helper.registerItem("wood_silver_kit", new VanillaBarrelUpgradeKit(BigBarrelBlock.BARRELS[5]));
        helper.registerItem("wood_gold_kit", new VanillaBarrelUpgradeKit(BigBarrelBlock.BARRELS[1]));
        helper.registerItem("wood_diamond_kit", new VanillaBarrelUpgradeKit(BigBarrelBlock.BARRELS[2]));
        helper.registerItem("wood_obsidian_kit", new VanillaBarrelUpgradeKit(BigBarrelBlock.BARRELS[3]));
        helper.registerItem("copper_iron_kit", new UpgradeKit(BigBarrelBlock.BARRELS[4], BigBarrelBlock.BARRELS[0]));
        helper.registerItem("copper_silver_kit", new UpgradeKit(BigBarrelBlock.BARRELS[4], BigBarrelBlock.BARRELS[5]));
        helper.registerItem("copper_gold_kit", new UpgradeKit(BigBarrelBlock.BARRELS[4], BigBarrelBlock.BARRELS[1]));
        helper.registerItem("copper_diamond_kit", new UpgradeKit(BigBarrelBlock.BARRELS[4], BigBarrelBlock.BARRELS[2]));
        helper.registerItem("copper_obsidian_kit",
                new UpgradeKit(BigBarrelBlock.BARRELS[4], BigBarrelBlock.BARRELS[3]));
        helper.registerItem("iron_silver_kit", new UpgradeKit(BigBarrelBlock.BARRELS[0], BigBarrelBlock.BARRELS[5]));
        helper.registerItem("iron_gold_kit", new UpgradeKit(BigBarrelBlock.BARRELS[0], BigBarrelBlock.BARRELS[1]));
        helper.registerItem("iron_diamond_kit", new UpgradeKit(BigBarrelBlock.BARRELS[0], BigBarrelBlock.BARRELS[2]));
        helper.registerItem("iron_obsidian_kit", new UpgradeKit(BigBarrelBlock.BARRELS[0], BigBarrelBlock.BARRELS[3]));
        helper.registerItem("silver_gold_kit", new UpgradeKit(BigBarrelBlock.BARRELS[5], BigBarrelBlock.BARRELS[1]));
        helper.registerItem("silver_diamond_kit", new UpgradeKit(BigBarrelBlock.BARRELS[5], BigBarrelBlock.BARRELS[2]));
        helper.registerItem("silver_obsidian_kit",
                new UpgradeKit(BigBarrelBlock.BARRELS[5], BigBarrelBlock.BARRELS[3]));
        helper.registerItem("gold_diamond_kit", new UpgradeKit(BigBarrelBlock.BARRELS[1], BigBarrelBlock.BARRELS[2]));
        helper.registerItem("gold_obsidian_kit", new UpgradeKit(BigBarrelBlock.BARRELS[1], BigBarrelBlock.BARRELS[3]));
        helper.registerItem("diamond_obsidian_kit",
                new UpgradeKit(BigBarrelBlock.BARRELS[2], BigBarrelBlock.BARRELS[3]));

        if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
            helper.registerItem("debug_item", debug_item);
        }
    }

    public static void log(String message) {
        logger.info("[" + modid + "] " + message);
    }

}