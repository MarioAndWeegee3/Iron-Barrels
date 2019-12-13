package marioandweegee3.ironbarrels2;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.Logger;

import marioandweegee3.ironbarrels2.block.BigBarrelBlock;
import marioandweegee3.ironbarrels2.block.entity.BarrelEntities;
import marioandweegee3.ironbarrels2.block.entity.BigBarrelEntity;
import marioandweegee3.ironbarrels2.client.gui.BigBarrelController;
import marioandweegee3.ironbarrels2.item.UpgradeKit;
import marioandweegee3.ironbarrels2.item.VanillaBarrelUpgradeKit;
import marioandweegee3.ml3api.registry.RegistryHelper;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.container.BlockContext;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;

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

    public static final Identifier BIG_BARREL_ID = helper.makeId("big_barrel");

    @Override
    public void onInitialize() {

        helper.register("iron_barrel", IRON_BARREL);
        helper.register("gold_barrel", GOLD_BARREL);
        helper.register("diamond_barrel", DIAMOND_BARREL);
        helper.register("obsidian_barrel", OBSIDIAN_BARREL);
        helper.register("copper_barrel", COPPER_BARREL);
        helper.register("silver_barrel", SILVER_BARREL);

        Map<String, Block> barrels = new HashMap<>();
        barrels.put("iron_barrel", BigBarrelBlock.BARRELS[0]);
        barrels.put("gold_barrel", BigBarrelBlock.BARRELS[1]);
        barrels.put("diamond_barrel", BigBarrelBlock.BARRELS[2]);
        barrels.put("obsidian_barrel", BigBarrelBlock.BARRELS[3]);
        barrels.put("copper_barrel", BigBarrelBlock.BARRELS[4]);
        barrels.put("silver_barrel", BigBarrelBlock.BARRELS[5]);
        helper.registerAll(barrels, ItemGroup.DECORATIONS);

        ContainerProviderRegistry.INSTANCE.registerFactory(BIG_BARREL_ID, (syncId, id, player, buf) -> {
            return new BigBarrelController(syncId, player.inventory, buf.readInt(), buf.readInt(),
                    BlockContext.create(player.world, buf.readBlockPos()));
        });

        helper.register("wood_copper_kit", new VanillaBarrelUpgradeKit(BigBarrelBlock.BARRELS[4]));
        helper.register("wood_iron_kit", new VanillaBarrelUpgradeKit(BigBarrelBlock.BARRELS[0]));
        helper.register("wood_silver_kit", new VanillaBarrelUpgradeKit(BigBarrelBlock.BARRELS[5]));
        helper.register("wood_gold_kit", new VanillaBarrelUpgradeKit(BigBarrelBlock.BARRELS[1]));
        helper.register("wood_diamond_kit", new VanillaBarrelUpgradeKit(BigBarrelBlock.BARRELS[2]));
        helper.register("wood_obsidian_kit", new VanillaBarrelUpgradeKit(BigBarrelBlock.BARRELS[3]));
        helper.register("copper_iron_kit", new UpgradeKit(BigBarrelBlock.BARRELS[4], BigBarrelBlock.BARRELS[0]));
        helper.register("copper_silver_kit", new UpgradeKit(BigBarrelBlock.BARRELS[4], BigBarrelBlock.BARRELS[5]));
        helper.register("copper_gold_kit", new UpgradeKit(BigBarrelBlock.BARRELS[4], BigBarrelBlock.BARRELS[1]));
        helper.register("copper_diamond_kit", new UpgradeKit(BigBarrelBlock.BARRELS[4], BigBarrelBlock.BARRELS[2]));
        helper.register("copper_obsidian_kit", new UpgradeKit(BigBarrelBlock.BARRELS[4], BigBarrelBlock.BARRELS[3]));
        helper.register("iron_silver_kit", new UpgradeKit(BigBarrelBlock.BARRELS[0], BigBarrelBlock.BARRELS[5]));
        helper.register("iron_gold_kit", new UpgradeKit(BigBarrelBlock.BARRELS[0], BigBarrelBlock.BARRELS[1]));
        helper.register("iron_diamond_kit", new UpgradeKit(BigBarrelBlock.BARRELS[0], BigBarrelBlock.BARRELS[2]));
        helper.register("iron_obsidian_kit", new UpgradeKit(BigBarrelBlock.BARRELS[0], BigBarrelBlock.BARRELS[3]));
        helper.register("silver_gold_kit", new UpgradeKit(BigBarrelBlock.BARRELS[5], BigBarrelBlock.BARRELS[1]));
        helper.register("silver_diamond_kit", new UpgradeKit(BigBarrelBlock.BARRELS[5], BigBarrelBlock.BARRELS[2]));
        helper.register("silver_obsidian_kit", new UpgradeKit(BigBarrelBlock.BARRELS[5], BigBarrelBlock.BARRELS[3]));
        helper.register("gold_diamond_kit", new UpgradeKit(BigBarrelBlock.BARRELS[1], BigBarrelBlock.BARRELS[2]));
        helper.register("gold_obsidian_kit", new UpgradeKit(BigBarrelBlock.BARRELS[1], BigBarrelBlock.BARRELS[3]));
        helper.register("diamond_obsidian_kit", new UpgradeKit(BigBarrelBlock.BARRELS[2], BigBarrelBlock.BARRELS[3]));
    }

    public static void log(String message){
        logger.info("["+modid+"] "+message);
    }

}